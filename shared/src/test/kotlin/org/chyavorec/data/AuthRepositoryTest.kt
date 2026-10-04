package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.LibraryRepository
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SelfCardStore
import org.chyavorec.domain.repository.SessionStore
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.ReaderService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    private class MemStore : SessionStore {
        var s: AuthSession? = null
        var persisted = false
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session; persisted = persist }
        override suspend fun isPersisted() = s != null && persisted
        override suspend fun clear() { s = null; persisted = false }
    }

    private inner class FakeAuth : AuthenticationService {
        var refreshResult: Outcome<AuthSession>? = null
        var lastPassword: CharArray? = null
        override suspend fun capabilities() = Outcome.Success(ServiceCapabilities(login = true))
        override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
            lastPassword = password
            val ok = String(password) == "secret"
            password.fill('\u0000')
            return if (ok) Outcome.Success(AuthSession("a", "r", clock.now().toEpochMilli() + 30_000, "R1"))
            else Outcome.Failure(AppError.Unauthorized)
        }
        override suspend fun refresh(session: AuthSession) =
            refreshResult ?: Outcome.Success(session.copy(accessToken = "a2", expiresAtMillis = clock.now().toEpochMilli() + 3_600_000))
        override suspend fun logout(session: AuthSession) = Outcome.Success(Unit)
        override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)
    }

    @Test fun loginStoresSessionAndWipesPassword() = runTest {
        val store = MemStore(); val auth = FakeAuth()
        val repo = AuthRepository(auth, store, clock, InMemoryPayloadCache())
        assertIs<Outcome.Success<Unit>>(repo.login("123", "secret".toCharArray(), remember = false))
        assertEquals(AuthState.SignedIn("R1"), repo.state.value)
        assertFalse(store.persisted)
        assertTrue(auth.lastPassword!!.all { it == '\u0000' })
    }

    @Test fun expiringTokenIsRefreshed() = runTest {
        val store = MemStore()
        val repo = AuthRepository(FakeAuth(), store, clock, InMemoryPayloadCache())
        repo.login("123", "secret".toCharArray(), true)
        val s = (repo.validSession() as Outcome.Success).value
        assertEquals("a2", s.accessToken)
    }

    @Test fun rejectedRefreshSignsOutAndClearsPersonalCache() = runTest {
        val store = MemStore(); val auth = FakeAuth(); val readerCache = InMemoryPayloadCache()
        val repo = AuthRepository(auth, store, clock, readerCache)
        repo.login("123", "secret".toCharArray(), true)
        readerCache.write("reader:loans", "[]")
        auth.refreshResult = Outcome.Failure(AppError.Unauthorized)
        assertIs<Outcome.Failure>(repo.validSession())
        assertNull(store.s)
        assertNull(readerCache.read("reader:loans"))
        assertEquals(AuthState.SignedOut, repo.state.value)
    }

    @Test fun bruteForceIsThrottled() = runTest {
        val repo = AuthRepository(FakeAuth(), MemStore(), clock, InMemoryPayloadCache())
        repeat(5) { repo.login("1", "bad".toCharArray(), true) }
        val r = repo.login("1", "secret".toCharArray(), true)
        val err = assertIs<Outcome.Failure>(r).error
        assertIs<AppError.RateLimited>(err)
        clock.advanceSeconds(61)
        assertIs<Outcome.Success<Unit>>(repo.login("1", "secret".toCharArray(), true))
    }

    @Test fun unavailableBackendNeverFakesData() = runTest {
        val svc = UnavailableInvLibServices()
        val repo = AuthRepository(svc, MemStore(), clock, InMemoryPayloadCache())
        val r = repo.login("1", "x".toCharArray(), true)
        assertEquals(AppError.NotAvailable(Feature.LOGIN), (r as Outcome.Failure).error)
        assertEquals(ServiceCapabilities.NONE, repo.capabilities())
    }

    @Test fun loansRequireSession() = runTest {
        val reader = object : ReaderService {
            override suspend fun profile(session: AuthSession) = Outcome.Success(ReaderProfile("R1", "1", "Читател"))
            override suspend fun loans(session: AuthSession) = Outcome.Success(listOf(Loan("L1", 1, "Под игото", "Иван Вазов", null, "2026-09-12", "2026-10-03")))
            override suspend fun placeHold(session: AuthSession, inv: Long) = Outcome.Success(Unit)
            override suspend fun renew(session: AuthSession, loanId: String) = Outcome.Failure(AppError.NotFound)
            override suspend fun requestAccountDeletion(session: AuthSession) = Outcome.Success(Unit)
            override suspend fun availability(inv: Long) = Outcome.Success(BookStatus.AVAILABLE)
        }
        val store = MemStore()
        val auth = AuthRepository(FakeAuth(), store, clock, InMemoryPayloadCache())
        val lib = LibraryRepository(reader, auth, InMemoryPayloadCache(), clock)
        assertEquals(AppError.Unauthorized, (lib.loans() as Outcome.Failure).error)
        auth.login("1", "secret".toCharArray(), true)
        assertEquals("Под игото", (lib.loans() as Outcome.Success).value.data.single().title)
    }

    private class LoansReader(private val onLoans: suspend (AuthSession) -> Outcome<List<Loan>>) : ReaderService {
        override suspend fun profile(session: AuthSession) = Outcome.Success(ReaderProfile("R1", "1", "Читател"))
        override suspend fun loans(session: AuthSession) = onLoans(session)
        override suspend fun placeHold(session: AuthSession, inv: Long) = Outcome.Success(Unit)
        override suspend fun renew(session: AuthSession, loanId: String) = Outcome.Failure(AppError.NotFound)
        override suspend fun requestAccountDeletion(session: AuthSession) = Outcome.Success(Unit)
        override suspend fun availability(inv: Long) = Outcome.Success(BookStatus.AVAILABLE)
    }

    @Test fun refreshKeepsRememberChoiceAndOldRefreshToken() = runTest {
        val store = MemStore(); val auth = FakeAuth()
        val repo = AuthRepository(auth, store, clock, InMemoryPayloadCache())
        repo.login("123", "secret".toCharArray(), remember = false)
        auth.refreshResult = Outcome.Success(AuthSession("a3", null, clock.now().toEpochMilli() + 3_600_000, "R1"))
        val s = (repo.validSession() as Outcome.Success).value
        assertEquals("a3", s.accessToken)
        assertEquals("r", s.refreshToken)
        assertFalse(store.persisted)
    }

    @Test fun unauthorizedDataCallRefreshesOnceAndRetries() = runTest {
        val store = MemStore()
        store.s = AuthSession("old", "r", clock.now().toEpochMilli() + 3_600_000, "R1"); store.persisted = true
        val repo = AuthRepository(FakeAuth(), store, clock, InMemoryPayloadCache())
        repo.restore()
        var calls = 0
        val reader = LoansReader { session ->
            calls++
            if (session.accessToken == "old") Outcome.Failure(AppError.Unauthorized)
            else Outcome.Success(listOf(Loan("L1", 1, "Под игото", "Иван Вазов", null, null, null)))
        }
        val lib = LibraryRepository(reader, repo, InMemoryPayloadCache(), clock)
        assertEquals("Под игото", (lib.loans() as Outcome.Success).value.data.single().title)
        assertEquals(2, calls)
        assertEquals("a2", store.s?.accessToken)
        assertTrue(store.persisted)
        assertEquals(AuthState.SignedIn("R1"), repo.state.value)
    }

    @Test fun repeatedUnauthorizedAfterRefreshSignsOut() = runTest {
        val store = MemStore()
        store.s = AuthSession("old", "r", clock.now().toEpochMilli() + 3_600_000, "R1"); store.persisted = true
        val repo = AuthRepository(FakeAuth(), store, clock, InMemoryPayloadCache())
        repo.restore()
        val lib = LibraryRepository(LoansReader { Outcome.Failure(AppError.Unauthorized) }, repo, InMemoryPayloadCache(), clock)
        assertEquals(AppError.Unauthorized, (lib.loans() as Outcome.Failure).error)
        assertNull(store.s)
        assertEquals(AuthState.SignedOut, repo.state.value)
    }

    @Test fun resultForSignedOutUserIsNotCached() = runTest {
        val store = MemStore()
        store.s = AuthSession("a", "r", clock.now().toEpochMilli() + 3_600_000, "R1")
        val repo = AuthRepository(FakeAuth(), store, clock, InMemoryPayloadCache())
        repo.restore()
        val reader = LoansReader {
            store.s = null // изход по време на заявката
            Outcome.Success(listOf(Loan("L1", 1, "Под игото", "Иван Вазов")))
        }
        val lib = LibraryRepository(reader, repo, InMemoryPayloadCache(), clock)
        assertEquals(AppError.Unauthorized, (lib.loans() as Outcome.Failure).error)
        assertNull(lib.cachedLoans())
    }

    @Test fun forbiddenIsNotUnauthorized() {
        assertEquals(AppError.Unauthorized, HttpFetcher.mapHttpError(401, null))
        assertEquals(AppError.Server(403), HttpFetcher.mapHttpError(403, null))
    }

    @Test fun selfCardValidation() = runTest {
        val store = object : SelfCardStore {
            var c: SelfDeclaredCard? = null
            override suspend fun load() = c
            override suspend fun save(card: SelfDeclaredCard) { c = card }
            override suspend fun clear() { c = null }
        }
        val repo = SelfCardRepository(store)
        assertFalse(repo.save("Б-12", "Иван"))   // кирилица не е валидна за Code 39
        assertTrue(repo.save(" r-0042 ", "Иван"))
        assertEquals("R-0042", repo.card.value?.cardNumber)
    }
}
