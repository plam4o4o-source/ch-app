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
import org.chyavorec.domain.model.AuthSession
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
    val throttle = LoginThrottle(clock)

    suspend fun restore() {
        val s = store.load()
        _state.value = if (s != null) AuthState.SignedIn(s.readerId) else AuthState.SignedOut
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
        val session = store.load() ?: return Outcome.Failure(AppError.Unauthorized)
        if (session.expiresAtMillis - clock.now().toEpochMilli() > 60_000) return Outcome.Success(session)
        when (val r = service.refresh(session)) {
            is Outcome.Success -> {
                store.save(r.value, persist = true)
                Outcome.Success(r.value)
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized || r.error is AppError.NotAvailable) signOutLocally()
                r
            }
        }
    }

    suspend fun logout() {
        store.load()?.let { runCatching { service.logout(it) } }
        signOutLocally()
    }

    /** Изтрива сесията и всички кеширани лични данни. */
    suspend fun signOutLocally() {
        store.clear()
        readerCache.clear()
        _state.value = AuthState.SignedOut
    }

    suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit> = service.requestPasswordReset(cardNumber)
}

/** Изпълнява заявка с валидна сесия; при 401 → изход. */
internal suspend fun <T> AuthRepository.withSession(block: suspend (AuthSession) -> Outcome<T>): Outcome<T> {
    val session = when (val s = validSession()) {
        is Outcome.Failure -> return s
        is Outcome.Success -> s.value
    }
    val r = block(session)
    if (r is Outcome.Failure && r.error is AppError.Unauthorized) signOutLocally()
    return r
}

class ProfileRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
) {
    private val res = CachedResource(secureCache, "reader:profile", ReaderProfile.serializer(), clock, 0)
    suspend fun profile(force: Boolean = true): Outcome<Synced<ReaderProfile>> =
        res.load(force) { auth.withSession { service.profile(it) } }

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

    suspend fun cachedLoans(): Synced<List<Loan>>? = res.cached()

    suspend fun loans(force: Boolean = true): Outcome<Synced<List<Loan>>> =
        res.load(force) { auth.withSession { service.loans(it) } }

    suspend fun renew(loanId: String): Outcome<Loan> = auth.withSession { service.renew(it, loanId) }

    suspend fun placeHold(inv: Long): Outcome<Unit> = auth.withSession { service.placeHold(it, inv) }
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
