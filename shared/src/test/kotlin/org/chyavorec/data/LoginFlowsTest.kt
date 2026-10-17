package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.AppError
import org.chyavorec.core.DiagnosticLog
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.FixedClock
import org.chyavorec.core.InMemoryDiagnosticStore
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.LoginThrottle
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import java.time.Instant
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Входът (допълва AuthRepositoryTest): заключването при налучкване — двойно по-дълго
 * всеки път, брояч на опитите, нулиране; кои грешки се броят; изход и подновяване
 * при грешки на сървъра; записите в локалния дневник (без ПИН и токени).
 */
class LoginFlowsTest {
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))

    @AfterTest fun noLog() = Diagnostics.install(null)

    private class MemStore : SessionStore {
        var s: AuthSession? = null
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session }
        override suspend fun isPersisted() = s != null
        override suspend fun clear() { s = null }
    }

    /** ПИН „1234“ е верният; [loginError] — какво връща сървърът вместо проверка. */
    private inner class FakeAuth : AuthenticationService {
        var loginError: AppError? = null
        var refreshResult: Outcome<AuthSession>? = null
        var logoutThrows = false
        var logoutCalls = 0
        var loginCalls = 0
        override suspend fun capabilities() = Outcome.Success(ServiceCapabilities(login = true))
        override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
            loginCalls++
            val ok = String(password) == "1234"
            password.fill('\u0000')
            loginError?.let { return Outcome.Failure(it) }
            return if (ok) Outcome.Success(AuthSession("access-1", "refresh-1", clock.now().toEpochMilli() + 30_000, "R1"))
            else Outcome.Failure(AppError.Unauthorized)
        }
        override suspend fun refresh(session: AuthSession) =
            refreshResult ?: Outcome.Success(session.copy(accessToken = "access-2", expiresAtMillis = clock.now().toEpochMilli() + 3_600_000))
        override suspend fun logout(session: AuthSession): Outcome<Unit> {
            logoutCalls++
            if (logoutThrows) throw IllegalStateException("bridge down")
            return Outcome.Success(Unit)
        }
        override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)
    }

    private fun repo(auth: FakeAuth = FakeAuth(), store: MemStore = MemStore(), cache: InMemoryPayloadCache = InMemoryPayloadCache()) =
        AuthRepository(auth, store, clock, cache)

    // --- LoginThrottle ---

    @Test fun throttleCountsDownAttemptsAndLocks() {
        val t = LoginThrottle(clock, maxAttempts = 3, baseLockSeconds = 60)
        assertEquals(3, t.attemptsLeft())
        t.onFailure(); t.onFailure()
        assertEquals(2, t.failedAttempts())
        assertEquals(1, t.attemptsLeft())
        assertEquals(0, t.remainingLockSeconds())
        t.onFailure()
        // Заключено; броячът на опитите започва отначало за следващия кръг.
        assertEquals(61, t.remainingLockSeconds())
        assertEquals(0, t.failedAttempts())
        clock.advanceSeconds(30)
        assertEquals(31, t.remainingLockSeconds())
        clock.advanceSeconds(30)
        assertEquals(0, t.remainingLockSeconds())
    }

    @Test fun eachLockoutIsTwiceAsLongUntilCap() {
        val t = LoginThrottle(clock, maxAttempts = 1, baseLockSeconds = 60)
        val locks = (1..9).map {
            t.onFailure()
            val s = t.remainingLockSeconds() - 1
            clock.advanceSeconds(s + 1)
            s
        }
        // 60, 120, 240 … най-много 60 × 2⁶.
        assertEquals(listOf(60L, 120, 240, 480, 960, 1920, 3840, 3840, 3840), locks)
    }

    @Test fun successResetsEverything() {
        val t = LoginThrottle(clock, maxAttempts = 2, baseLockSeconds = 60)
        t.onFailure(); t.onFailure()
        assertTrue(t.remainingLockSeconds() > 0)
        t.onSuccess()
        assertEquals(0, t.remainingLockSeconds())
        assertEquals(2, t.attemptsLeft())
        // След успех следващото заключване пак е най-краткото.
        t.onFailure(); t.onFailure()
        assertEquals(61, t.remainingLockSeconds())
    }

    // --- вход ---

    @Test fun lockedLoginDoesNotCallServerAndWipesPin() = runTest {
        val auth = FakeAuth()
        val r = repo(auth)
        repeat(5) { r.login("0042", "0000".toCharArray(), remember = true) }
        assertEquals(5, auth.loginCalls)
        val pin = "1234".toCharArray()
        val locked = assertIs<Outcome.Failure>(r.login("0042", pin, remember = true)).error
        assertEquals(AppError.RateLimited(61), locked)
        assertEquals(5, auth.loginCalls)
        assertTrue(pin.all { it == '\u0000' })
        assertEquals(AuthState.Unknown, r.state.value)
    }

    @Test fun networkAndServerErrorsDoNotCountAsWrongPin() = runTest {
        val auth = FakeAuth()
        val r = repo(auth)
        auth.loginError = AppError.Network
        repeat(6) { assertEquals(AppError.Network, (r.login("0042", "1234".toCharArray(), true) as Outcome.Failure).error) }
        auth.loginError = AppError.Server(503)
        repeat(6) { r.login("0042", "1234".toCharArray(), true) }
        assertEquals(0, r.throttle.failedAttempts())
        auth.loginError = null
        assertIs<Outcome.Success<Unit>>(r.login("0042", "1234".toCharArray(), true))
        assertEquals(AuthState.SignedIn("R1"), r.state.value)
    }

    @Test fun successfulLoginClearsFailedAttempts() = runTest {
        val r = repo()
        repeat(3) { r.login("0042", "9999".toCharArray(), true) }
        assertEquals(3, r.throttle.failedAttempts())
        r.login("0042", "1234".toCharArray(), true)
        assertEquals(0, r.throttle.failedAttempts())
        assertEquals(5, r.throttle.attemptsLeft())
    }

    @Test fun serverRateLimitIsPassedThrough() = runTest {
        val auth = FakeAuth().apply { loginError = AppError.RateLimited(300) }
        val r = repo(auth)
        assertEquals(AppError.RateLimited(300), (r.login("0042", "1234".toCharArray(), true) as Outcome.Failure).error)
        // Заключването на сървъра не се смята за грешен ПИН на устройството.
        assertEquals(0, r.throttle.failedAttempts())
    }

    // --- изход и подновяване ---

    @Test fun logoutTellsServerAndClearsEvenIfServerFails() = runTest {
        val auth = FakeAuth().apply { logoutThrows = true }
        val store = MemStore()
        val cache = InMemoryPayloadCache()
        val r = repo(auth, store, cache)
        r.login("0042", "1234".toCharArray(), true)
        cache.write("r1/reader:loans", "[]")
        r.logout()
        assertEquals(1, auth.logoutCalls)
        assertNull(store.s)
        assertNull(cache.read("r1/reader:loans"))
        assertEquals(AuthState.SignedOut, r.state.value)
        // Без сесия — изходът не пита сървъра.
        r.logout()
        assertEquals(1, auth.logoutCalls)
    }

    @Test fun refreshNetworkErrorKeepsSession() = runTest {
        val auth = FakeAuth()
        val store = MemStore()
        val r = repo(auth, store)
        r.login("0042", "1234".toCharArray(), true) // изтича до 30 s → подновяване
        auth.refreshResult = Outcome.Failure(AppError.Network)
        assertEquals(AppError.Network, (r.validSession() as Outcome.Failure).error)
        assertNotNull(store.s)
        assertEquals(AuthState.SignedIn("R1"), r.state.value)
        // Мрежата се връща — подновяването минава със същата сесия.
        auth.refreshResult = null
        assertEquals("access-2", (r.validSession() as Outcome.Success).value.accessToken)
    }

    @Test fun refreshOnBridgeWithoutLoginSignsOut() = runTest {
        val auth = FakeAuth()
        val store = MemStore()
        val r = repo(auth, store)
        r.login("0042", "1234".toCharArray(), true)
        auth.refreshResult = Outcome.Failure(AppError.NotAvailable(org.chyavorec.core.Feature.LOGIN))
        assertIs<Outcome.Failure>(r.validSession())
        assertNull(store.s)
        assertEquals(AuthState.SignedOut, r.state.value)
    }

    @Test fun freshSessionIsNotRefreshed() = runTest {
        val auth = FakeAuth()
        val store = MemStore()
        store.s = AuthSession("long", "refresh", clock.now().toEpochMilli() + 3_600_000, "R1")
        val r = repo(auth, store)
        r.restore()
        auth.refreshResult = Outcome.Failure(AppError.Unauthorized) // би изхвърлило, ако се извика
        assertEquals("long", (r.validSession() as Outcome.Success).value.accessToken)
        assertEquals(AuthState.SignedIn("R1"), r.state.value)
    }

    // --- локален дневник ---

    @Test fun unexpectedFailuresAreLoggedWithoutSecrets() = runTest {
        val log = DiagnosticLog(InMemoryDiagnosticStore())
        Diagnostics.install(log)
        val auth = FakeAuth()
        val r = repo(auth)
        // Грешен ПИН и липса на мрежа са нормални — не се пишат.
        r.login("0042", "0000".toCharArray(), true)
        auth.loginError = AppError.Network
        r.login("0042", "1234".toCharArray(), true)
        assertTrue(log.entries().isEmpty())
        auth.loginError = AppError.Server(502)
        r.login("0042", "1234".toCharArray(), true)
        auth.loginError = null
        r.login("0042", "1234".toCharArray(), true)
        auth.refreshResult = Outcome.Failure(AppError.Parse("invlib api"))
        r.validSession()
        auth.logoutThrows = true
        r.logout()
        assertEquals(listOf("auth.login", "auth.refresh", "auth.logout"), log.entries().map { it.tag })
        assertEquals("Server(httpCode=502)", log.entries()[0].error)
        val text = log.export(emptyList())
        listOf("0042", "1234", "access-1", "refresh-1").forEach { assertFalse(it in text, "$it в дневника") }
    }
}
