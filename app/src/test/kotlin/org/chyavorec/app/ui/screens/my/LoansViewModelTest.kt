package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.TestBackend
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.Loan
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * „Моите книги“ ([LoansViewModel]): удължаване — една заявка при двойно натискане, заемането
 * се сменя на място (без ново теглене на списъка), отказите се показват и не пипат списъка.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LoansViewModelTest {
    @get:Rule val main = MainDispatcherRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val backend = TestBackend()
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    private lateinit var c: AppContainer
    private val api = TestBackend.API

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.loans(
            """{"loanId":"L1","inv":1,"title":"Под игото","dateDue":"2026-10-20","canRenew":true}""",
            """{"loanId":"L2","inv":2,"title":"Тютюн","dateDue":"2026-10-25","canRenew":true}""",
        )
        c = backend.container(app, clock)
        assertIs<Outcome.Success<Unit>>(c.authRepository.login("0042", "1234".toCharArray(), remember = false))
    }

    @After fun tearDown() = backend.shutdown()

    /** Изчаква [done] (заявките вървят в мрежовите нишки). */
    private fun until(done: () -> Boolean) = runBlocking<Unit> {
        withTimeout(10_000) { while (!done()) { yield(); Thread.sleep(10) } }
    }

    private fun loaded(): LoansViewModel {
        val vm = LoansViewModel(c)
        until { vm.state.value.data?.size == 2 && vm.canRenew.value }
        return vm
    }

    @Test fun renewReplacesOnlyThatLoanAndIgnoresDoubleTap() {
        backend.routes["$api/v1/me/loans/L1/renew"] = {
            MockResponse().setResponseCode(202).setBody("""{"loanId":"L1","inv":1,"title":"Под игото","dateDue":"2026-10-20","canRenew":true,"renewPending":true}""")
        }
        val vm = loaded()
        val loan = vm.state.value.data!!.first { it.loanId == "L1" }
        vm.renew(loan)
        vm.renew(loan)
        until { vm.renewResult.value != null && vm.renewing.value.isEmpty() }
        assertEquals(1, backend.count("$api/v1/me/loans/L1/renew"))
        val list: List<Loan> = vm.state.value.data!!
        assertEquals(listOf(true, false), list.map { it.renewPending })
        assertIs<Outcome.Success<Loan>>(vm.renewResult.value)
        // Без ново теглене на целия списък.
        assertEquals(1, backend.count("$api/v1/me/loans"))
    }

    @Test fun refusalIsShownAndListStaysTheSame() {
        backend.json("$api/v1/me/loans/L2/renew", """{"error":"not_allowed"}""", code = 409)
        val vm = loaded()
        val before = vm.state.value.data
        vm.renew(before!!.first { it.loanId == "L2" })
        until { vm.renewResult.value != null && vm.renewing.value.isEmpty() }
        assertEquals(AppError.Conflict(AppError.Conflict.NOT_ALLOWED), (vm.renewResult.value as Outcome.Failure).error)
        assertEquals(before, vm.state.value.data)
        assertFalse(vm.state.value.data!!.any { it.renewPending })
        // Отказът не изхвърля читателя.
        assertTrue(c.authRepository.state.value is org.chyavorec.data.repository.AuthState.SignedIn)
    }
}
