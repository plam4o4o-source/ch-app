package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.core.getOrNull
import org.chyavorec.data.invlib.SingleFlight
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderBundle
import org.chyavorec.domain.model.ReaderMessage
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

    /** Слушатели за „смяна на читателя“ (изход или нов вход) — чистят личните данни в паметта. */
    private val resetListeners = java.util.concurrent.CopyOnWriteArrayList<() -> Unit>()

    /** [listener] се вика при всеки изход и при всеки успешен вход (синхронно, бързо). */
    fun addReaderResetListener(listener: () -> Unit) {
        resetListeners += listener
    }

    private fun notifyReaderReset() {
        resetListeners.forEach { runCatching { it() } }
    }

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
                notifyReaderReset()
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
        notifyReaderReset()
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

/**
 * Общото място за читателските данни (профил, членство, заемания, история, лични
 * съобщения): един шифрован кеш на вид данни и една инстанция за цялото приложение.
 *
 * - Ако сървърът има capability `all`, една заявка `GET /v1/me/all` пълни всички кешове
 *   наведнъж; едновременните извиквания споделят една заявка. Без нея (по-стар мост)
 *   или ако отговорът не става — поотделни заявки, както преди.
 * - Свежест (без мрежа, освен при изрично опресняване): профил [PROFILE_FRESH_MILLIS],
 *   членство [MEMBERSHIP_FRESH_MILLIS], заемания [LOANS_FRESH_MILLIS],
 *   история [HISTORY_FRESH_MILLIS], съобщения [MESSAGES_FRESH_MILLIS].
 * - Профилът се държи и в [profile] (на ниво приложение), за да го споделят всички
 *   екрани, без всеки да го тегли сам. Изчиства се при изход/нов вход.
 */
class ReaderDataRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    private val clock: AppClock,
    /** Ако е зададен, членството се добавя към профила, когато `/v1/me` не го връща. */
    private val membershipService: MembershipService? = null,
) {
    private class Part<T>(
        val res: CachedResource<T>,
        val freshMillis: Long,
        val pick: (ReaderBundle) -> T?,
        val single: suspend (AuthSession) -> Outcome<T>,
    ) {
        val flight = SingleFlight<Outcome<Synced<T>>>()
    }

    private val profilePart = Part<ReaderProfile>(
        CachedResource(secureCache, KEY_PROFILE, ReaderProfile.serializer(), clock, PROFILE_FRESH_MILLIS),
        PROFILE_FRESH_MILLIS, { it.profile },
    ) { session ->
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

    private val membershipPart = Part<Membership>(
        CachedResource(secureCache, KEY_MEMBERSHIP, Membership.serializer(), clock, MEMBERSHIP_FRESH_MILLIS),
        MEMBERSHIP_FRESH_MILLIS, { it.membership },
    ) { session -> membershipService?.membership(session) ?: Outcome.Failure(AppError.NotAvailable(Feature.MEMBERSHIP)) }

    private val loansPart = Part<List<Loan>>(
        CachedResource(secureCache, KEY_LOANS, ListSerializer(Loan.serializer()), clock, LOANS_FRESH_MILLIS),
        LOANS_FRESH_MILLIS, { it.loans },
    ) { session -> service.loans(session) }

    private val historyPart = Part<List<HistoryItem>>(
        CachedResource(secureCache, KEY_HISTORY, ListSerializer(HistoryItem.serializer()), clock, HISTORY_FRESH_MILLIS),
        HISTORY_FRESH_MILLIS, { it.history },
    ) { session -> service.history(session) }

    private val messagesPart = Part<List<ReaderMessage>>(
        CachedResource(secureCache, KEY_MESSAGES, ListSerializer(ReaderMessage.serializer()), clock, MESSAGES_FRESH_MILLIS),
        MESSAGES_FRESH_MILLIS, { it.messages },
    ) { session -> service.messages(session) }

    private val allFlight = SingleFlight<Outcome<ReaderBundle>>()

    private val _profile = MutableStateFlow<Synced<ReaderProfile>?>(null)
    /** Последно известният профил на влезлия читател (`null` = няма/още не е зареден). */
    val profile: StateFlow<Synced<ReaderProfile>?> = _profile.asStateFlow()

    /** Сменя се при изход/нов вход: закъснели отговори за предишния читател не се показват. */
    @Volatile private var generation = 0L

    init {
        auth.addReaderResetListener {
            generation++
            _profile.value = null
        }
    }

    // --- профил ---

    suspend fun profile(force: Boolean): Outcome<Synced<ReaderProfile>> {
        // Пресен профил в паметта — без четене от диска (всеки екран го иска при отваряне).
        if (!force) {
            _profile.value?.takeIf { it.refreshError == null && isFresh(it, PROFILE_FRESH_MILLIS) }?.let {
                return Outcome.Success(it.copy(fromCache = false))
            }
        }
        val gen = generation
        val r = load(profilePart, force)
        if (r is Outcome.Success) publishProfile(gen, r.value)
        return r
    }

    /** Кешираният профил (може и остарял) — показва се веднага, докато тече опресняването. */
    suspend fun cachedProfile(): Synced<ReaderProfile>? {
        _profile.value?.let { return it }
        val gen = generation
        val c = profilePart.res.cached() ?: return null
        if (gen != generation) return null // междувременно изход/нов вход
        _profile.compareAndSet(null, c)
        return _profile.value ?: c
    }

    private fun publishProfile(gen: Long, value: Synced<ReaderProfile>) {
        if (gen == generation) _profile.value = value
    }

    // --- останалите ---

    suspend fun membership(force: Boolean): Outcome<Synced<Membership>> = load(membershipPart, force)
    suspend fun cachedMembership(): Synced<Membership>? = membershipPart.res.cached()

    suspend fun loans(force: Boolean): Outcome<Synced<List<Loan>>> = load(loansPart, force)
    suspend fun cachedLoans(): Synced<List<Loan>>? = loansPart.res.cached()
    internal suspend fun updateCachedLoans(transform: (List<Loan>) -> List<Loan>) = loansPart.res.updateCached(transform)

    suspend fun history(force: Boolean): Outcome<Synced<List<HistoryItem>>> = load(historyPart, force)
    suspend fun cachedHistory(): Synced<List<HistoryItem>>? = historyPart.res.cached()

    suspend fun messages(force: Boolean): Outcome<Synced<List<ReaderMessage>>> = load(messagesPart, force)
    suspend fun cachedMessages(): Synced<List<ReaderMessage>>? = messagesPart.res.cached()
    internal suspend fun updateCachedMessages(transform: (List<ReaderMessage>) -> List<ReaderMessage>) =
        messagesPart.res.updateCached(transform)

    // --- общото ---

    /** Без изрично опресняване едновременните извиквания за едни и същи данни споделят една заявка. */
    private suspend fun <T> load(part: Part<T>, force: Boolean): Outcome<Synced<T>> =
        if (force) loadNow(part, force = true) else part.flight.run { loadNow(part, force = false) }

    private suspend fun <T> loadNow(part: Part<T>, force: Boolean): Outcome<Synced<T>> {
        if (!force) {
            part.res.cached()?.let { c -> if (isFresh(c, part.freshMillis)) return Outcome.Success(c.copy(fromCache = false)) }
        }
        // Без сесия — без заявки (и без питане за възможностите); при грешка — кешът, както винаги.
        val sessionError = (auth.validSession() as? Outcome.Failure)?.error
        if (sessionError != null) return part.res.load(force = true) { Outcome.Failure(sessionError) }
        if (!auth.capabilities().all) return part.res.load(force = true) { auth.withSession(part.single) }
        return when (val b = fetchAll()) {
            is Outcome.Success -> {
                val value = part.pick(b.value)
                if (value != null) {
                    Outcome.Success(Synced(value, clock.now(), fromCache = false))
                } else {
                    // Сървърът не е върнал тази част — поотделно (ако изобщо се поддържа).
                    part.res.load(force = true) { auth.withSession(part.single) }
                }
            }
            is Outcome.Failure -> {
                val error = b.error
                if (error.allowsSingleFallback()) part.res.load(force = true) { auth.withSession(part.single) }
                else part.res.load(force = true) { Outcome.Failure(error) }
            }
        }
    }

    /**
     * `GET /v1/me/all` (една заявка за всички едновременни извиквания) и запис на всяка
     * върната част в нейния кеш. Профилът се показва веднага в [profile].
     */
    private suspend fun fetchAll(): Outcome<ReaderBundle> = allFlight.run {
        val gen = generation
        val r = auth.withSession { service.meAll(it) }
        if (r is Outcome.Success && gen == generation) store(r.value, gen)
        r
    }

    private suspend fun store(b: ReaderBundle, gen: Long) {
        b.profile?.let { p ->
            write(profilePart.res, p)
            publishProfile(gen, Synced(p, clock.now()))
        }
        b.membership?.let { write(membershipPart.res, it) }
        b.loans?.let { write(loansPart.res, it) }
        b.history?.let { write(historyPart.res, it) }
        b.messages?.let { write(messagesPart.res, it) }
    }

    /** Запис в кеша чрез обичайния път на [CachedResource] (със сегашния момент като време на синхронизация). */
    private suspend fun <T> write(res: CachedResource<T>, value: T) {
        res.load(force = true) { Outcome.Success(value) }
    }

    /** Същото правило като в [CachedResource]: по-пресни от [freshMillis] — без мрежа. */
    private fun isFresh(value: Synced<*>, freshMillis: Long): Boolean =
        clock.now().toEpochMilli() - value.syncedAt.toEpochMilli() in 0 until freshMillis

    /** Грешки, при които комбинираната заявка не става, но поотделните може да станат. */
    private fun AppError.allowsSingleFallback(): Boolean =
        this is AppError.Parse || this == AppError.NotFound || this is AppError.NotAvailable ||
            (this is AppError.Server && httpCode in setOf(404, 405, 501))

    companion object {
        const val PROFILE_FRESH_MILLIS = 12 * 60 * 60_000L
        const val MEMBERSHIP_FRESH_MILLIS = 6 * 60 * 60_000L
        const val LOANS_FRESH_MILLIS = 3 * 60_000L
        const val HISTORY_FRESH_MILLIS = 10 * 60_000L
        const val MESSAGES_FRESH_MILLIS = 5 * 60_000L

        internal const val KEY_PROFILE = "reader:profile"
        internal const val KEY_MEMBERSHIP = "reader:membership"
        internal const val KEY_LOANS = "reader:loans"
        internal const val KEY_HISTORY = "reader:history"
        internal const val KEY_MESSAGES = "reader:messages"
    }
}

/**
 * Профилът на читателя. [data] е общото хранилище — в приложението една инстанция
 * за всички читателски репозиторита (по подразбиране — собствено, за тестове).
 */
class ProfileRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    /** Ако е зададен, членството се добавя към профила (когато /v1/me не го връща). */
    membershipService: MembershipService? = null,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock, membershipService),
) {
    /** Споделеният профил (на ниво приложение) — всички екрани го четат оттук. */
    val state: StateFlow<Synced<ReaderProfile>?> get() = data.profile

    /** [force] = false → без мрежа, ако профилът е по-пресен от 12 часа. */
    suspend fun profile(force: Boolean = true): Outcome<Synced<ReaderProfile>> = data.profile(force)

    /** Кешираният профил (без мрежа) — веднага при отваряне на екран. */
    suspend fun cachedProfile(): Synced<ReaderProfile>? = data.cachedProfile()

    suspend fun requestAccountDeletion(): Outcome<Unit> = auth.withSession { service.requestAccountDeletion(it) }
}

/** Заемания („Моите книги“), запазване и подновяване. */
class LibraryRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock),
) {
    suspend fun cachedLoans(): Synced<List<Loan>>? = data.cachedLoans()

    /** [force] = false → без мрежа, ако списъкът е по-пресен от 3 минути. */
    suspend fun loans(force: Boolean = true): Outcome<Synced<List<Loan>>> = data.loans(force)

    /**
     * Заявка за удължаване. При успех (202) сървърът връща заемането с
     * `renewPending = true`; кешираният списък се обновява веднага (без нова заявка
     * за целия списък), за да не „изчезне“ чакащото състояние при следващо показване.
     */
    suspend fun renew(loanId: String): Outcome<Loan> {
        val r = auth.withSession { service.renew(it, loanId) }
        if (r is Outcome.Success) data.updateCachedLoans { list -> list.map { if (it.loanId == loanId) r.value else it } }
        return r
    }

    suspend fun placeHold(inv: Long): Outcome<Unit> = auth.withSession { service.placeHold(it, inv) }

    /** История на четенето — шифрован кеш (10 минути без мрежа), изтрива се при изход. */
    suspend fun history(force: Boolean = true): Outcome<Synced<List<HistoryItem>>> = data.history(force)

    suspend fun cachedHistory(): Synced<List<HistoryItem>>? = data.cachedHistory()
}

/**
 * Лични съобщения от библиотеката до читателя. Шифрован кеш ([secureCache] = читателският),
 * който се изтрива при изход заедно със заеманията и историята. Съдържанието не се логва.
 */
class ReaderMessagesRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock),
) {
    suspend fun cached(): Synced<List<ReaderMessage>>? = data.cachedMessages()

    /** Кратък кеш: без мрежа, ако е по-пресен от 5 минути (освен при изрично опресняване). */
    suspend fun messages(force: Boolean = true): Outcome<Synced<List<ReaderMessage>>> = data.messages(force)

    /** Дали сървърът приема „прочетено“ на пакет ([markRead] с няколко id). */
    suspend fun supportsBatchRead(): Boolean = auth.capabilities().messagesBatchRead

    /**
     * Праща „прочетено“. `404` (няма такова съобщение при читателя) се приема за успех —
     * няма смисъл от повторни опити. При успех кешираният списък се обновява веднага.
     */
    suspend fun markRead(messageId: String): Outcome<Unit> {
        val r = auth.withSession { service.markMessageRead(it, messageId) }
        if (r is Outcome.Failure && r.error !is AppError.NotFound) return r
        data.updateCachedMessages { list -> list.map { if (it.id == messageId) it.copy(read = true) else it } }
        return Outcome.Success(Unit)
    }

    /**
     * „Прочетено“ за няколко съобщения с една заявка (само при [supportsBatchRead]).
     * Както при единичното: `404` се приема за успех; при успех кешът се обновява.
     */
    suspend fun markRead(messageIds: Collection<String>): Outcome<Unit> {
        val ids = messageIds.filter { it.isNotBlank() }.distinct()
        if (ids.isEmpty()) return Outcome.Success(Unit)
        val r = auth.withSession { service.markMessagesRead(it, ids) }
        if (r is Outcome.Failure && r.error !is AppError.NotFound) return r
        val set = ids.toSet()
        data.updateCachedMessages { list -> list.map { if (it.id in set) it.copy(read = true) else it } }
        return Outcome.Success(Unit)
    }
}

class MembershipRepository(
    service: MembershipService,
    auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(
        service as? ReaderService ?: UnavailableInvLibServices(), auth, secureCache, clock, service,
    ),
) {
    /** [force] = false → без мрежа, ако членството е по-пресно от 6 часа. */
    suspend fun membership(force: Boolean = true): Outcome<Synced<Membership>> = data.membership(force)

    suspend fun cached(): Synced<Membership>? = data.cachedMembership()
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
