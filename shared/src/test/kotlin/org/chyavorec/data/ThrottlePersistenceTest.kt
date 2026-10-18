package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.LoginThrottle
import org.chyavorec.data.repository.ThrottleState
import org.chyavorec.data.repository.ThrottleStore
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Защитата от налучкване на ПИН-а оцелява при рестарт (нов LoginThrottle със същото
 * хранилище), успешният вход я нулира, изходът — не; върнат назад часовник не заключва „завинаги“.
 */
class ThrottlePersistenceTest {
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))

    private class MemThrottleStore(var state: ThrottleState? = null) : ThrottleStore {
        var saves = 0
        override suspend fun load() = state
        override suspend fun save(state: ThrottleState) { this.state = state; saves++ }
    }

    private class BrokenThrottleStore : ThrottleStore {
        override suspend fun load(): ThrottleState = throw IllegalStateException("disk")
        override suspend fun save(state: ThrottleState) = throw IllegalStateException("disk")
    }

    private fun throttle(store: ThrottleStore) = LoginThrottle(clock, maxAttempts = 5, baseLockSeconds = 60, store = store)

    @Test fun lockSurvivesARestart() = runTest {
        val store = MemThrottleStore()
        val first = throttle(store)
        repeat(5) { first.onFailure(); first.persist() }
        assertEquals(61, first.remainingLockSeconds())
        clock.advanceSeconds(20)
        // „Рестарт“: нов брояч, същото хранилище.
        val second = throttle(store)
        second.restore()
        assertEquals(41, second.remainingLockSeconds())
        clock.advanceSeconds(41)
        assertEquals(0, second.remainingLockSeconds())
        // Следващото заключване е двойно по-дълго — и броят заключвания е запазен.
        repeat(5) { second.onFailure() }
        assertEquals(121, second.remainingLockSeconds())
    }

    @Test fun failedAttemptsBeforeTheLockSurviveARestart() = runTest {
        val store = MemThrottleStore()
        val first = throttle(store)
        repeat(3) { first.onFailure(); first.persist() }
        val second = throttle(store)
        second.restore()
        assertEquals(3, second.failedAttempts())
        assertEquals(2, second.attemptsLeft())
        repeat(2) { second.onFailure() }
        assertEquals(61, second.remainingLockSeconds())
    }

    @Test fun restoreDoesNotForgetFailuresMadeBeforeIt() = runTest {
        val store = MemThrottleStore(ThrottleState(failures = 1, lockouts = 0, lockedUntilMillis = 0))
        val t = throttle(store)
        repeat(2) { t.onFailure() }
        t.restore()
        // По-строгото от двете (паметта: 2, диска: 1), не сбор и не „нулиране“.
        assertEquals(2, t.failedAttempts())
        // Второ зареждане не пипа нищо.
        store.state = ThrottleState()
        t.restore()
        assertEquals(2, t.failedAttempts())
    }

    @Test fun clockMovedBackIsCappedToTheLastLockLength() = runTest {
        // Записано заключване „до след 10 дни“ (часовникът е бил напред/върнат назад) след едно
        // заключване от 60 s → не „завинаги“, а 60 s от сега.
        val tenDays = clock.now().toEpochMilli() + 10L * 24 * 3600 * 1000
        val t = throttle(MemThrottleStore(ThrottleState(failures = 0, lockouts = 1, lockedUntilMillis = tenDays)))
        t.restore()
        assertEquals(61, t.remainingLockSeconds())
        clock.advanceSeconds(61)
        assertEquals(0, t.remainingLockSeconds())
    }

    @Test fun clockMovedBackWhileRunningIsCapped() {
        val t = LoginThrottle(clock, maxAttempts = 1, baseLockSeconds = 60)
        t.onFailure()
        assertEquals(61, t.remainingLockSeconds())
        clock.advanceSeconds(-3 * 24 * 3600)
        assertEquals(61, t.remainingLockSeconds())
    }

    @Test fun legitimateLongLockIsNotCapped() = runTest {
        // Седмото заключване е 64 мин — не се скъсява.
        val until = clock.now().toEpochMilli() + 3840_000
        val t = throttle(MemThrottleStore(ThrottleState(failures = 0, lockouts = 7, lockedUntilMillis = until)))
        t.restore()
        assertEquals(3841, t.remainingLockSeconds())
    }

    @Test fun corruptValuesAreSanitized() = runTest {
        val t = throttle(MemThrottleStore(ThrottleState(failures = -4, lockouts = Int.MAX_VALUE, lockedUntilMillis = -1)))
        t.restore()
        assertEquals(0, t.failedAttempts())
        assertEquals(0, t.remainingLockSeconds())
        repeat(5) { t.onFailure() }
        assertEquals(3841, t.remainingLockSeconds())
    }

    @Test fun brokenStoreDoesNotBreakLogin() = runTest {
        val t = throttle(BrokenThrottleStore())
        t.restore()
        t.onFailure()
        t.persist()
        assertEquals(1, t.failedAttempts())
    }

    @Test fun successResetsTheStoredState() = runTest {
        val store = MemThrottleStore()
        val first = throttle(store)
        repeat(5) { first.onFailure() }
        first.persist()
        clock.advanceSeconds(61)
        first.onSuccess()
        first.persist()
        assertEquals(ThrottleState(), store.state)
        val second = throttle(store)
        second.restore()
        assertEquals(0, second.failedAttempts())
        repeat(5) { second.onFailure() }
        // След успех следващото заключване пак е най-краткото.
        assertEquals(61, second.remainingLockSeconds())
    }

    // --- през AuthRepository ---

    private class MemSessions : SessionStore {
        var s: AuthSession? = null
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session }
        override suspend fun isPersisted() = s != null
        override suspend fun clear() { s = null }
    }

    private inner class FakeAuth : AuthenticationService {
        var loginCalls = 0
        override suspend fun capabilities() = Outcome.Success(ServiceCapabilities(login = true))
        override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
            loginCalls++
            val ok = String(password) == "1234"
            password.fill('\u0000')
            return if (ok) Outcome.Success(AuthSession("a", "r", clock.now().toEpochMilli() + 3_600_000, "R1"))
            else Outcome.Failure(AppError.Unauthorized)
        }
        override suspend fun refresh(session: AuthSession) = Outcome.Success(session)
        override suspend fun logout(session: AuthSession): Outcome<Unit> = Outcome.Success(Unit)
        override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)
    }

    @Test fun lockFromBeforeARestartBlocksTheFirstLoginWithoutCallingTheServer() = runTest {
        val store = MemThrottleStore()
        val auth = FakeAuth()
        val before = AuthRepository(auth, MemSessions(), clock, InMemoryPayloadCache(), throttleStore = store)
        repeat(5) { before.login("0042", "0000".toCharArray(), remember = true) }
        assertEquals(5, auth.loginCalls)
        // Новият процес: екранът изпраща опит, без някой да е извикал restoreThrottle().
        val after = AuthRepository(auth, MemSessions(), clock, InMemoryPayloadCache(), throttleStore = store)
        assertEquals(AppError.RateLimited(61), assertIs<Outcome.Failure>(after.login("0042", "1234".toCharArray(), true)).error)
        assertEquals(5, auth.loginCalls)
    }

    @Test fun signOutDoesNotResetButSuccessDoes() = runTest {
        val store = MemThrottleStore()
        val auth = FakeAuth()
        val r = AuthRepository(auth, MemSessions(), clock, InMemoryPayloadCache(), throttleStore = store)
        r.login("0042", "1234".toCharArray(), true)
        repeat(3) { r.login("0042", "0000".toCharArray(), true) }
        r.logout()
        assertEquals(3, store.state?.failures)
        val restarted = AuthRepository(auth, MemSessions(), clock, InMemoryPayloadCache(), throttleStore = store)
        restarted.restoreThrottle()
        assertEquals(3, restarted.throttle.failedAttempts())
        assertIs<Outcome.Success<Unit>>(restarted.login("0042", "1234".toCharArray(), true))
        assertEquals(ThrottleState(), store.state)
    }
}
