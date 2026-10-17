package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.AppError
import org.chyavorec.core.DiagnosticLog
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Feature
import org.chyavorec.core.FixedClock
import org.chyavorec.core.InMemoryDiagnosticStore
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.LibraryRepository
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.RenewResult
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.ReaderService
import java.time.Instant
import java.time.LocalDate
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Удължаване на срока (LibraryRepository.renew): успех само за едното заемане, отказите
 * (чакащо, непозволено, твърде много заявки, стар мост) не пипат кеша, 401 → подновяване и
 * повторен опит; и индикаторът на срока след удължаване.
 */
class LoanRenewTest {
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    private val today = LocalDate.of(2026, 10, 17)

    @AfterTest fun noLog() = Diagnostics.install(null)

    private class MemStore(var s: AuthSession?) : SessionStore {
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session }
        override suspend fun isPersisted() = s != null
        override suspend fun clear() { s = null }
    }

    private inner class Auth : AuthenticationService {
        var refreshes = 0
        var refreshResult: Outcome<AuthSession>? = null
        override suspend fun capabilities() = Outcome.Success(ServiceCapabilities(login = true, loans = true, renew = true))
        override suspend fun login(cardNumber: String, password: CharArray) = Outcome.Failure(AppError.Unauthorized)
        override suspend fun refresh(session: AuthSession): Outcome<AuthSession> {
            refreshes++
            return refreshResult ?: Outcome.Success(session.copy(accessToken = "fresh", expiresAtMillis = clock.now().toEpochMilli() + 3_600_000))
        }
        override suspend fun logout(session: AuthSession) = Outcome.Success(Unit)
        override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)
    }

    private val loans = listOf(
        Loan("L1", 1, "Под игото", "Иван Вазов", borrowedOn = "2026-09-20", dueOn = "2026-10-18", canRenew = true),
        Loan("L2", 2, "Тютюн", "Димитър Димов", borrowedOn = "2026-09-25", dueOn = "2026-10-25", canRenew = true),
    )

    /** Отговорът на удължаването се задава от теста; токените на заявките се записват. */
    private inner class Reader : ReaderService {
        var renewResult: (AuthSession, String) -> Outcome<Loan> = { _, id ->
            Outcome.Success(loans.first { it.loanId == id }.copy(renewPending = true))
        }
        val renewTokens = mutableListOf<String>()
        var loansCalls = 0
        override suspend fun profile(session: AuthSession) = Outcome.Success(ReaderProfile("R1", "1", "Читател"))
        override suspend fun loans(session: AuthSession): Outcome<List<Loan>> { loansCalls++; return Outcome.Success(loans) }
        override suspend fun placeHold(session: AuthSession, inv: Long) = Outcome.Success(Unit)
        override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> {
            renewTokens += session.accessToken
            return renewResult(session, loanId)
        }
        override suspend fun requestAccountDeletion(session: AuthSession) = Outcome.Success(Unit)
        override suspend fun availability(inv: Long) = Outcome.Success(BookStatus.AVAILABLE)
        override suspend fun history(session: AuthSession) = Outcome.Success(emptyList<HistoryItem>())
    }

    private inner class Setup(signedIn: Boolean = true) {
        val store = MemStore(if (signedIn) AuthSession("access", "refresh", clock.now().toEpochMilli() + 3_600_000, "R1") else null)
        val authService = Auth()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val auth = AuthRepository(authService, store, clock, cache)
        val reader = Reader()
        val library = LibraryRepository(reader, auth, cache, clock)
    }

    @Test fun successMarksOnlyThatLoanPendingInCache() = runTest {
        val s = Setup()
        s.auth.restore()
        s.library.loans(force = false)
        val r = s.library.renew("L1")
        assertTrue((r as Outcome.Success).value.renewPending)
        val cached = s.library.cachedLoans()!!.data
        assertEquals(listOf(true, false), cached.map { it.renewPending })
        assertEquals(1, s.reader.loansCalls)
    }

    @Test fun refusalsLeaveCacheUntouched() = runTest {
        val s = Setup()
        s.auth.restore()
        s.library.loans(force = false)
        val before = s.library.cachedLoans()!!.data
        val errors = listOf(
            AppError.Conflict(AppError.Conflict.PENDING),
            AppError.Conflict(AppError.Conflict.NOT_ALLOWED),
            AppError.RateLimited(30),
            AppError.NotFound,
            AppError.NotAvailable(Feature.RENEW),
            AppError.Network,
        )
        for (e in errors) {
            s.reader.renewResult = { _, _ -> Outcome.Failure(e) }
            assertEquals(e, (s.library.renew("L1") as Outcome.Failure).error)
            assertEquals(before, s.library.cachedLoans()!!.data)
            // Отказът не е изход.
            assertEquals(AuthState.SignedIn("R1"), s.auth.state.value)
        }
    }

    @Test fun expiredTokenIsRefreshedAndRenewRetried() = runTest {
        val s = Setup()
        s.auth.restore()
        s.reader.renewResult = { session, id ->
            if (session.accessToken == "access") Outcome.Failure(AppError.Unauthorized)
            else Outcome.Success(loans.first { it.loanId == id }.copy(renewPending = true))
        }
        assertTrue((s.library.renew("L2") as Outcome.Success).value.renewPending)
        assertEquals(listOf("access", "fresh"), s.reader.renewTokens)
        assertEquals(1, s.authService.refreshes)
        assertEquals("fresh", s.store.s?.accessToken)
    }

    @Test fun rejectedSessionSignsOutAndClearsLoans() = runTest {
        val s = Setup()
        s.auth.restore()
        s.library.loans(force = false)
        s.reader.renewResult = { _, _ -> Outcome.Failure(AppError.Unauthorized) }
        s.authService.refreshResult = Outcome.Failure(AppError.Unauthorized)
        assertEquals(AppError.Unauthorized, (s.library.renew("L1") as Outcome.Failure).error)
        assertEquals(AuthState.SignedOut, s.auth.state.value)
        assertNull(s.store.s)
        assertNull(s.library.cachedLoans())
    }

    @Test fun withoutSessionNothingIsSent() = runTest {
        val s = Setup(signedIn = false)
        s.auth.restore()
        assertEquals(AppError.Unauthorized, (s.library.renew("L1") as Outcome.Failure).error)
        assertTrue(s.reader.renewTokens.isEmpty())
    }

    @Test fun serverErrorGoesToLocalLogButRefusalsDoNot() = runTest {
        val log = DiagnosticLog(InMemoryDiagnosticStore())
        Diagnostics.install(log)
        val s = Setup()
        s.auth.restore()
        s.reader.renewResult = { _, _ -> Outcome.Failure(AppError.Conflict(AppError.Conflict.PENDING)) }
        s.library.renew("L1")
        assertTrue(log.entries().isEmpty())
        s.reader.renewResult = { _, _ -> Outcome.Failure(AppError.Server(500)) }
        s.library.renew("L1")
        assertEquals("loans.renew", log.entries().single().tag)
        assertFalse("L1" in log.export(emptyList()))
    }

    // --- индикаторът на срока ---

    @Test fun dueStatusFollowsTheRenewedDate() {
        val calc = LoanDueCalculator(dueSoonDays = 3)
        val loan = loans.first() // утре
        assertEquals(DueStatus.DUE_SOON, calc.status(loan, today))
        // Удължено до 15.11 → много време; при чакащо удължаване — без напомняне.
        val renewed = loan.copy(dueOn = "2026-11-15", renewals = 1, renewPending = false)
        assertEquals(DueStatus.PLENTY_OF_TIME, calc.status(renewed, today))
        assertNull(calc.reminderStatus(loan.copy(renewPending = true), today))
        assertEquals(DueStatus.DUE_SOON, calc.reminderStatus(loan, today))
    }

    @Test fun dueSoonWindowBoundaries() {
        val calc = LoanDueCalculator(dueSoonDays = 3)
        assertEquals(DueStatus.DUE_SOON, calc.status(today.plusDays(3), today))
        assertEquals(DueStatus.PLENTY_OF_TIME, calc.status(today.plusDays(4), today))
        assertEquals(DueStatus.OVERDUE, calc.status(today.minusDays(1), today))
        // Друг прозорец за „наближава“.
        assertEquals(DueStatus.DUE_SOON, LoanDueCalculator(dueSoonDays = 7).status(today.plusDays(7), today))
        assertEquals(DueStatus.PLENTY_OF_TIME, LoanDueCalculator(dueSoonDays = 0).status(today.plusDays(1), today))
    }

    @Test fun elapsedFractionIsClamped() {
        val calc = LoanDueCalculator()
        val borrowed = LocalDate.of(2026, 10, 1)
        val due = LocalDate.of(2026, 10, 21)
        assertEquals(0f, calc.elapsedFraction(borrowed, due, borrowed.minusDays(3)))
        assertEquals(1f, calc.elapsedFraction(borrowed, due, due.plusDays(5)))
        // Заемане и срок в един ден — без деление на нула.
        assertEquals(1f, calc.elapsedFraction(today, today, today.plusDays(1)))
    }

    @Test fun renewResultKinds() {
        assertTrue(RenewResult("rejected", "Заявена от друг читател", null).isRejected)
        assertFalse(RenewResult("done", null, null).isRejected)
        assertTrue(RenewResult("DONE", null, null).isDone)
    }
}
