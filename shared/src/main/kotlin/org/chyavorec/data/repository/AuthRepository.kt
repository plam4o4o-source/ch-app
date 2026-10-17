package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.core.logFailure
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.CachedBytes
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService

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

    /** Неуспешни опити след последното заключване/успешен вход (само за показване). */
    fun failedAttempts(): Int = failures

    /** Колко още грешни опита остават до следващото заключване. */
    fun attemptsLeft(): Int = (maxAttempts - failures).coerceAtLeast(0)

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

    /**
     * [listener] се вика при всеки изход, при всеки успешен вход и при старт без запазена
     * сесия (синхронно, бързо).
     */
    fun addReaderResetListener(listener: () -> Unit) {
        resetListeners += listener
    }

    private fun notifyReaderReset() {
        resetListeners.forEach { runCatching { it() }.logFailure("auth.reset") }
    }

    /**
     * Първоначално четене на запазената сесия. Сменя състоянието САМО ако то още е
     * [AuthState.Unknown] — вход или изход, станали докато store.load() е траело,
     * имат предимство и не се презаписват със стария резултат.
     */
    suspend fun restore() {
        val s = store.load()
        if (s != null) {
            _state.compareAndSet(AuthState.Unknown, AuthState.SignedIn(s.readerId))
            return
        }
        // Няма сесия — напр. „не ме помни“ и процесът е бил спрян. Кешираното за предишния
        // читател не бива да остане за следващия на същия телефон: изтрива се както при изход.
        storeLock.withLock {
            if (_state.value == AuthState.Unknown && store.load() == null) clearLocked()
        }
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
                storeLock.withLock {
                    // Нов вход = възможно друг читател: всичко кеширано досега се изтрива ПРЕДИ
                    // записа на новата сесия (след това ключовете вече са на новия читател).
                    readerCache.clear()
                    notifyReaderReset()
                    lastScope = r.value.readerId to readerScopeOf(r.value.readerId)
                    store.save(r.value, persist = remember)
                    _state.value = AuthState.SignedIn(r.value.readerId)
                }
                Outcome.Success(Unit)
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized) throttle.onFailure()
                noteUnexpected("auth.login", r.error)
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
            // Няма сесия → състоянието не бива да остава „влязъл“ (или „неизвестно“), а
            // кешираното за предишния читател се изтрива (както при изход).
            storeLock.withLock { if (store.load() == null && _state.value != AuthState.SignedOut) clearLocked() }
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
                noteUnexpected("auth.refresh", r.error)
                r
            }
        }

    suspend fun logout() {
        store.load()?.let { runCatching { service.logout(it) }.logFailure("auth.logout") }
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
        lastScope = null
        readerCache.clear()
        notifyReaderReset()
        _state.value = AuthState.SignedOut
    }

    /** Последно изчисленото пространство: (id на читателя, хеш) — за [readerScopeNow]. */
    @Volatile private var lastScope: Pair<String, String>? = null

    /**
     * Пространството на текущия читател в читателския кеш — по сесията в [store] (единственият
     * източник на истина), `null` без вход. Ключовете в кеша са обвързани с него
     * ([ReaderScopedCache]), така че данни на един читател не могат да се покажат на друг.
     */
    internal suspend fun readerScope(): String? {
        val id = store.load()?.readerId ?: return null
        lastScope?.let { (known, scope) -> if (known == id) return scope }
        return readerScopeOf(id).also { lastScope = id to it }
    }

    /** Без четене на [store] (за [PayloadCache.version], която не е suspend) — последно известното. */
    internal fun readerScopeNow(): String? = lastScope?.second

    private val scopedCaches = java.util.IdentityHashMap<PayloadCache, PayloadCache>()

    /** Една обвивка на кеш (общи заключвания в [CachedResource] за всички репозиторита върху него). */
    internal fun scopedCache(base: PayloadCache): PayloadCache = synchronized(scopedCaches) {
        scopedCaches.getOrPut(base) { ReaderScopedCache(base, this) }
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
 * Неочакваните откази (грешка на сървъра, неразчетен отговор) — в локалния дневник; само
 * видът и HTTP кодът. Грешен ПИН, заключване, отказ и липса на мрежа са нормални и не се пишат.
 */
internal fun noteUnexpected(tag: String, error: AppError) {
    if (error is AppError.Server || error is AppError.Parse || error is AppError.Unexpected) Diagnostics.note(tag, error.toString())
}

/** Пространството в читателския кеш за читател [readerId]: кратък SHA-256 (id-то не се вижда в ключа). */
internal fun readerScopeOf(readerId: String): String =
    "r" + org.chyavorec.core.Hashing.sha256Hex(("reader:" + readerId).toByteArray(Charsets.UTF_8)).take(24)

/**
 * Читателският кеш с ключове на текущия читател (`<пространство>/<ключ>`). Без вход нищо не
 * се чете и не се записва. Дори ако изчистването при смяна на читателя се провали, данните
 * на предишния остават под неговото пространство и не се показват на следващия.
 */
internal class ReaderScopedCache(private val base: PayloadCache, private val auth: AuthRepository) : PayloadCache {
    private suspend fun scoped(key: String): String? = auth.readerScope()?.let { "$it/$key" }

    override suspend fun read(key: String): CachedPayload? = scoped(key)?.let { base.read(it) }
    override suspend fun readBytes(key: String): CachedBytes? = scoped(key)?.let { base.readBytes(it) }
    override suspend fun write(key: String, text: String) { scoped(key)?.let { base.write(it, text) } }
    override suspend fun writeBytes(key: String, bytes: ByteArray) { scoped(key)?.let { base.writeBytes(it, bytes) } }
    override suspend fun remove(key: String) { scoped(key)?.let { base.remove(it) } }
    override suspend fun clear() = base.clear()
    override suspend fun touch(key: String): Boolean = scoped(key)?.let { base.touch(it) } ?: false
    override fun version(key: String): Long? = auth.readerScopeNow()?.let { base.version("$it/$key") }
}
