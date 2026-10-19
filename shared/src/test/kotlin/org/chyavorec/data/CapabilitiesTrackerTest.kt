package org.chyavorec.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.CapabilitiesStatus
import org.chyavorec.data.repository.CapabilitiesTracker
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

/** „Няма връзка“ не бива да изглежда като „библиотеката не е включила входа“. */
class CapabilitiesTrackerTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    private object NoSession : SessionStore {
        override suspend fun load(): AuthSession? = null
        override suspend fun save(session: AuthSession, persist: Boolean) = Unit
        override suspend fun isPersisted() = false
        override suspend fun clear() = Unit
    }

    private class FakeAuth : AuthenticationService {
        var next: Outcome<ServiceCapabilities> = Outcome.Failure(AppError.Network)
        var gate: CompletableDeferred<Unit>? = null
        var calls = 0
        override suspend fun capabilities(): Outcome<ServiceCapabilities> {
            calls++
            gate?.await()
            return next
        }
        override suspend fun login(cardNumber: String, password: CharArray) = Outcome.Failure(AppError.Network)
        override suspend fun refresh(session: AuthSession) = Outcome.Failure(AppError.Network)
        override suspend fun logout(session: AuthSession) = Outcome.Success(Unit)
        override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)
    }

    private fun tracker(auth: FakeAuth) = CapabilitiesTracker(AuthRepository(auth, NoSession, clock, InMemoryPayloadCache()))

    @Test fun offlineIsFailedNotDisabledAndRetrySucceeds() = runTest {
        val auth = FakeAuth()
        val t = tracker(auth)
        assertEquals(CapabilitiesStatus.Loading, t.status.value)
        t.refresh()
        assertEquals(CapabilitiesStatus.Failed(AppError.Network), t.status.value)
        assertEquals(null, t.known)

        // Връзката се връща — следващото питане успява (неуспехът не е запомнен като NONE).
        auth.next = Outcome.Success(ServiceCapabilities(login = true))
        t.refresh()
        assertEquals(CapabilitiesStatus.Known(ServiceCapabilities(login = true)), t.status.value)

        // Известен включен вход — без нови заявки.
        t.refresh()
        assertEquals(2, auth.calls)
    }

    @Test fun serverErrorIsFailedButMissingEndpointMeansDisabled() = runTest {
        val auth = FakeAuth().apply { next = Outcome.Failure(AppError.Server(503)) }
        val t = tracker(auth)
        t.refresh()
        assertEquals(CapabilitiesStatus.Failed(AppError.Server(503)), t.status.value)

        auth.next = Outcome.Failure(AppError.NotFound)
        t.refresh()
        assertEquals(CapabilitiesStatus.Known(ServiceCapabilities.NONE), t.status.value)
    }

    @Test fun knownDisabledIsRecheckedAndKeptWhenOffline() = runTest {
        val auth = FakeAuth().apply { next = Outcome.Success(ServiceCapabilities.NONE) }
        val t = tracker(auth)
        t.refresh()
        assertEquals(CapabilitiesStatus.Known(ServiceCapabilities.NONE), t.status.value)

        // Повторна проверка без мрежа не изтрива известния отговор.
        auth.next = Outcome.Failure(AppError.Network)
        t.refresh()
        assertEquals(CapabilitiesStatus.Known(ServiceCapabilities.NONE), t.status.value)
        assertEquals(2, auth.calls)
    }

    @Test fun retryShowsLoadingAndConcurrentRefreshAsksOnce() = runTest {
        val auth = FakeAuth()
        val t = tracker(auth)
        t.refresh()
        auth.gate = CompletableDeferred()
        auth.next = Outcome.Success(ServiceCapabilities(login = true))
        launch { t.refresh() }
        launch { t.refresh() }
        runCurrent()
        assertEquals(CapabilitiesStatus.Loading, t.status.value)
        auth.gate!!.complete(Unit)
        runCurrent()
        assertEquals(CapabilitiesStatus.Known(ServiceCapabilities(login = true)), t.status.value)
        assertEquals(2, auth.calls)
    }
}
