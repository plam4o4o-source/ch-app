package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.core.getOrNull
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.repository.SelfCardStore
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService

sealed interface AuthState {
    data object Unknown : AuthState
    data object SignedOut : AuthState
    data class SignedIn(val readerId: String) : AuthState
}

/**
 * Защита от налучкване на пароли от страна на клиента: след [maxAttempts]
 * неуспешни опита входът се заключва, като всяко следващо заключване е двойно
 * по-дълго (60 s, 120 s, …). Истинската защита трябва да е и на сървъра.
 */
class LoginThrottle(private val clock: AppClock, private val maxAttempts: Int = 5, private val baseLockSeconds: Long = 60) {
    private var failures = 0
    private var lockouts = 0
    private var lockedUntil = 0L

    fun remainingLockSeconds(): Long {
        val left = (lockedUntil - clock.now().toEpochMilli()) / 1000
        return if (left > 0) left + 1 else 0
    }

    fun onFailure() {
        failures++
        if (failures >= maxAttempts) {
            val seconds = baseLockSeconds shl lockouts.coerceAtMost(6)
            lockedUntil = clock.now().toEpochMilli() + seconds * 1000
            lockouts++
            failures = 0
        }
    }

    fun onSuccess() { failures = 0; lockouts = 0; lockedUntil = 0 }
}

class AuthRepository(
    private val service: AuthenticationService,
    private val store: SessionStore,
    private val clock: AppClock,
    private val readerCache: PayloadCache,
) {
    private val _state = MutableStateFlow<AuthState>(AuthState.Unknown)
    val state: StateFlow<AuthState> = _state.asStateFlow()
    private val mutex = Mutex()
    /**
     * Кратко заключване само около записите в [store] (сверка + запис/изтриване).
     * Изходът не чака [mutex] (подновяване по мрежата може да е бавно), но закъснял
     * refresh не може да запише сесия между проверката си и изхода.
     * Ред на заключване: [mutex] → [storeLock], никога обратно.
     */
    private val storeLock = Mutex()
    val throttle = LoginThrottle(clock)

    /**
     * Първоначално четене на запазената сесия. Сменя състоянието САМО ако то още е
     * [AuthState.Unknown] — вход или изход, станали докато store.load() е траело,
     * имат предимство и не се презаписват със стария резултат.
     */
    suspend fun restore() {
        val s = store.load()
        _state.compareAndSet(AuthState.Unknown, if (s != null) AuthState.SignedIn(s.readerId) else AuthState.SignedOut)
    }

    suspend fun capabilities(): ServiceCapabilities =
        (service.capabilities() as? Outcome.Success)?.value ?: ServiceCapabilities.NONE

    suspend fun login(cardNumber: String, password: CharArray, remember: Boolean): Outcome<Unit> {
        val lock = throttle.remainingLockSeconds()
        if (lock > 0) {
            password.fill('\u0000')
            return Outcome.Failure(AppError.RateLimited(lock))
        }
        return when (val r = service.login(cardNumber, password)) {
            is Outcome.Success -> {
                throttle.onSuccess()
                store.save(r.value, persist = remember)
                _state.value = AuthState.SignedIn(r.value.readerId)
                Outcome.Success(Unit)
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized) throttle.onFailure()
                r
            }
        }
    }

    /**
     * Валидна сесия; подновява токена, ако изтича до минута. При отказан
     * refresh сесията се изтрива (изход), за да не остават невалидни данни.
     */
    suspend fun validSession(): Outcome<AuthSession> = mutex.withLock {
        val session = store.load() ?: run {
            // Няма сесия → състоянието не бива да остава „влязъл“ (или „неизвестно“).
            storeLock.withLock { if (store.load() == null) _state.value = AuthState.SignedOut }
            return Outcome.Failure(AppError.Unauthorized)
        }
        if (session.expiresAtMillis - clock.now().toEpochMilli() > 60_000) return Outcome.Success(session)
        refreshLocked(session)
    }

    /**
     * Принудително подновяване, без проверка на срока — когато сървърът е
     * отхвърлил [rejected] (401), въпреки че по часовника тя е още валидна.
     */
    internal suspend fun forceRefresh(rejected: AuthSession): Outcome<AuthSession> = mutex.withLock {
        val session = store.load() ?: return Outcome.Failure(AppError.Unauthorized)
        // Друга заявка вече е подновила токена — ползваме новия.
        if (session.accessToken != rejected.accessToken) return Outcome.Success(session)
        refreshLocked(session)
    }

    /** Дали [session] все още е текущата (не е излязъл/сменен потребителят междувременно). */
    internal suspend fun isCurrent(session: AuthSession): Boolean = store.load()?.readerId == session.readerId

    /**
     * Извиква се САМО под [mutex]. Резултатът се записва само ако в [store] все още
     * е същата сесия: ако междувременно потребителят е излязъл (или е влязъл друг),
     * закъснелият отговор се отхвърля — иначе би „възкресил“ прекратената сесия.
     */
    private suspend fun refreshLocked(session: AuthSession): Outcome<AuthSession> =
        when (val r = service.refresh(session)) {
            is Outcome.Success -> {
                // Сървърът може да не върне нов refresh токен — тогава старият остава.
                val renewed = r.value.copy(refreshToken = r.value.refreshToken ?: session.refreshToken)
                val saved = storeLock.withLock {
                    if (store.load()?.accessToken != session.accessToken) {
                        false
                    } else {
                        // Запазва избора „запомни ме“ от входа.
                        store.save(renewed, persist = store.isPersisted())
                        true
                    }
                }
                if (saved) Outcome.Success(renewed) else Outcome.Failure(AppError.Unauthorized)
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized || r.error is AppError.NotAvailable) signOutIfCurrent(session)
                r
            }
        }

    suspend fun logout() {
        store.load()?.let { runCatching { service.logout(it) } }
        signOutLocally()
    }

    /**
     * Изтрива сесията и всички кеширани лични данни. Не чака текущо подновяване
     * (то само ще установи, че сесията вече я няма, и няма да я запише отново).
     */
    suspend fun signOutLocally() {
        storeLock.withLock { clearLocked() }
    }

    /** Изход само ако [session] е още текущата — отказ за стара сесия не изхвърля нов вход. */
    internal suspend fun signOutIfCurrent(session: AuthSession) {
        storeLock.withLock {
            val current = store.load()
            if (current == null || current.accessToken == session.accessToken) clearLocked()
        }
    }

    /** Извиква се САМО под [storeLock]. */
    private suspend fun clearLocked() {
        store.clear()
        readerCache.clear()
        _state.value = AuthState.SignedOut
    }

    suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit> = service.requestPasswordReset(cardNumber)
}

/**
 * Изпълнява заявка с валидна сесия. При 401 подновява токена веднъж (без
 * оглед на срока) и повтаря заявката; изход само ако подновяването бъде
 * отказано или повторната заявка пак е 401. Ако междувременно потребителят е
 * излязъл (или е сменен), резултатът се отхвърля, за да не попадне в кеша.
 */
internal suspend fun <T> AuthRepository.withSession(block: suspend (AuthSession) -> Outcome<T>): Outcome<T> {
    val session = when (val s = validSession()) {
        is Outcome.Failure -> return s
        is Outcome.Success -> s.value
    }
    val first = block(session)
    val r = if (first is Outcome.Failure && first.error is AppError.Unauthorized) {
        // Отказан refresh вече е извикал signOutIfCurrent() в refreshLocked().
        val renewed = when (val s = forceRefresh(session)) {
            is Outcome.Failure -> return s
            is Outcome.Success -> s.value
        }
        val retry = block(renewed)
        if (retry is Outcome.Failure && retry.error is AppError.Unauthorized) {
            signOutIfCurrent(renewed)
            return retry
        }
        retry
    } else {
        first
    }
    if (r is Outcome.Success && !isCurrent(session)) return Outcome.Failure(AppError.Unauthorized)
    return r
}

class ProfileRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    /** Ако е зададен, членството се добавя към профила (/v1/me не го връща). */
    private val membershipService: MembershipService? = null,
) {
    private val res = CachedResource(secureCache, "reader:profile", ReaderProfile.serializer(), clock, 0)
    suspend fun profile(force: Boolean = true): Outcome<Synced<ReaderProfile>> =
        res.load(force) {
            auth.withSession<ReaderProfile> { session ->
                when (val p = service.profile(session)) {
                    is Outcome.Failure -> p
                    is Outcome.Success -> {
                        val ms = membershipService
                        if (p.value.membership != null || ms == null) {
                            p
                        } else {
                            // Неуспех при членството не проваля профила — просто без него.
                            Outcome.Success(p.value.copy(membership = ms.membership(session).getOrNull()))
                        }
                    }
                }
            }
        }

    suspend fun requestAccountDeletion(): Outcome<Unit> = auth.withSession { service.requestAccountDeletion(it) }
}

/** Заемания („Моите книги“), запазване и подновяване. */
class LibraryRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
) {
    private val res = CachedResource(secureCache, "reader:loans", ListSerializer(Loan.serializer()), clock, 0)
    /** Историята се сменя рядко — без мрежа, ако кешът е по-пресен от 10 минути (освен при изрично опресняване). */
    private val historyRes = CachedResource(secureCache, "reader:history", ListSerializer(HistoryItem.serializer()), clock, 10 * 60_000L)

    suspend fun cachedLoans(): Synced<List<Loan>>? = res.cached()

    suspend fun loans(force: Boolean = true): Outcome<Synced<List<Loan>>> =
        res.load(force) { auth.withSession { service.loans(it) } }

    /**
     * Заявка за удължаване. При успех (202) сървърът връща заемането с
     * `renewPending = true`; кешираният списък се обновява веднага, за да не
     * „изчезне“ чакащото състояние при следващо показване от кеша.
     */
    suspend fun renew(loanId: String): Outcome<Loan> {
        val r = auth.withSession { service.renew(it, loanId) }
        if (r is Outcome.Success) res.updateCached { list -> list.map { if (it.loanId == loanId) r.value else it } }
        return r
    }

    suspend fun placeHold(inv: Long): Outcome<Unit> = auth.withSession { service.placeHold(it, inv) }

    /** История на четенето — шифрован кеш, изтрива се при изход заедно с останалите читателски данни. */
    suspend fun history(force: Boolean = true): Outcome<Synced<List<HistoryItem>>> =
        historyRes.load(force) { auth.withSession { service.history(it) } }

    suspend fun cachedHistory(): Synced<List<HistoryItem>>? = historyRes.cached()
}

class MembershipRepository(
    private val service: MembershipService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
) {
    private val res = CachedResource(secureCache, "reader:membership", Membership.serializer(), clock, 0)
    suspend fun membership(force: Boolean = true): Outcome<Synced<Membership>> =
        res.load(force) { auth.withSession { service.membership(it) } }
}

/** Ръчно въведената карта — независима от онлайн API. */
class SelfCardRepository(private val store: SelfCardStore) {
    private val _card = MutableStateFlow<SelfDeclaredCard?>(null)
    val card: StateFlow<SelfDeclaredCard?> = _card.asStateFlow()

    suspend fun load() { _card.value = store.load() }

    /** Проверява формата: Code 39 допуска A–Z, 0–9 и „-. $/+%“. */
    fun validate(number: String): Boolean {
        val n = number.trim().uppercase()
        return n.length in 1..32 && n.all { it in CODE39_CHARS }
    }

    suspend fun save(number: String, name: String): Boolean {
        val n = number.trim().uppercase()
        if (!validate(n)) return false
        val card = SelfDeclaredCard(n, name.trim().take(80))
        store.save(card)
        _card.value = card
        return true
    }

    suspend fun clear() { store.clear(); _card.value = null }

    companion object {
        const val CODE39_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. \$/+%"
    }
}
