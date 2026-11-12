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
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Печатът „Върната“ през „Моите книги“: само потвърдено от историята, веднъж, без фалшиви печати. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LoansStampsTest {
    @get:Rule val main = MainDispatcherRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val backend = TestBackend()
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    private lateinit var c: AppContainer
    private val api = TestBackend.API

    private fun loan(id: String, inv: Int) = """{"loanId":"$id","inv":$inv,"title":"Книга $id","dateDue":"2026-10-25","canRenew":true}"""
    private fun hist(id: String, inv: Int, back: String?) =
        """{"loanId":"$id","inv":$inv,"title":"Книга $id","author":"Автор","dateOut":"2026-09-20","dateIn":${back?.let { "\"$it\"" } ?: "null"}}"""

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.json(
            "$api/v1/capabilities",
            """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"renew":true,"history":true,"messages":false}""",
        )
        backend.loans(loan("L1", 1), loan("L2", 2))
        backend.json("$api/v1/me/history", """{"items":[${hist("L1", 1, null)},${hist("L2", 2, null)}]}""")
        c = backend.container(app, clock)
        assertIs<Outcome.Success<Unit>>(c.authRepository.login("0042", "1234".toCharArray(), remember = false))
    }

    @After fun tearDown() = backend.shutdown()

    private fun until(done: () -> Boolean) = runBlocking<Unit> {
        withTimeout(10_000) { while (!done()) { yield(); Thread.sleep(10) } }
    }

    private fun loaded(): LoansViewModel {
        val vm = LoansViewModel(c)
        until { vm.state.value.data?.size == 2 && runBlocking { c.settings.returnStamps().known != null } }
        return vm
    }

    private fun refreshAndWait(vm: LoansViewModel, loansLeft: Int) {
        vm.refresh()
        until { vm.state.value.data?.size == loansLeft && !vm.state.value.refreshing }
        // Стъпката на печатите върви след обновяването на състоянието.
        Thread.sleep(300)
    }

    @Test fun firstOpeningOnlyRemembers() {
        val vm = loaded()
        assertNull(vm.stamps.value)
        assertEquals(setOf("L1", "L2"), runBlocking { c.settings.returnStamps().known })
    }

    @Test fun returnedBookConfirmedByHistoryIsStampedOnce() {
        val vm = loaded()
        backend.loans(loan("L2", 2))
        backend.json("$api/v1/me/history", """{"items":[${hist("L1", 1, "2026-10-16")},${hist("L2", 2, null)}]}""")
        refreshAndWait(vm, 1)
        until { vm.stamps.value != null }
        val show = assertNotNull(vm.stamps.value)
        assertEquals(listOf("L1"), show.books.map { it.loanId })
        assertEquals(2026, show.year)
        // Годишният брояч: една върната през годината, преди печата — нула.
        assertEquals(0, show.yearBefore)
        vm.stampsDone()
        // Ново опресняване (или нов екран) — същата книга не се празнува пак.
        refreshAndWait(vm, 1)
        assertNull(vm.stamps.value)
        val again = LoansViewModel(c)
        until { again.state.value.data?.size == 1 }
        Thread.sleep(300)
        assertNull(again.stamps.value)
    }

    @Test fun historyThatCannotBeFetchedCelebratesNothingAndKeepsTheBookQueued() {
        val vm = loaded()
        backend.loans(loan("L2", 2))
        backend.routes.remove("$api/v1/me/history") // 404
        refreshAndWait(vm, 1)
        assertNull(vm.stamps.value)
        assertEquals(setOf("L1"), runBlocking { c.settings.returnStamps().pending.keys })
        // По-късно историята е достъпна и потвърждава.
        backend.json("$api/v1/me/history", """{"items":[${hist("L1", 1, "2026-10-16")}]}""")
        refreshAndWait(vm, 1)
        until { vm.stamps.value != null }
        assertEquals(listOf("L1"), vm.stamps.value!!.books.map { it.loanId })
    }

    @Test fun renewalIsNotCelebrated() {
        val vm = loaded()
        // Удължаване, при което заемането получава ново id, но е същата книга (същият инвентарен номер).
        backend.loans(loan("L1b", 1), loan("L2", 2))
        backend.json("$api/v1/me/history", """{"items":[${hist("L1", 1, "2026-10-16")},${hist("L2", 2, null)}]}""")
        refreshAndWait(vm, 2)
        assertNull(vm.stamps.value)
        assertTrue(runBlocking { c.settings.returnStamps().pending.isEmpty() })
    }

    @Test fun signedOutReaderGetsNothingAndLeavesNoTrace() = runBlocking<Unit> {
        val vm = loaded()
        c.authRepository.logout()
        c.settings.clearPersonal()
        assertNull(vm.stamps.value)
        val state = c.settings.returnStamps()
        assertNull(state.known)
        assertTrue(state.pending.isEmpty() && state.done.isEmpty())
    }
}
