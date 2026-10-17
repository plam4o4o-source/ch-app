package org.chyavorec.app.notifications

import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.LoanReminderKind
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Напомнянето за срок ([LoanReminderWorker]): показва се само ако книгата още е заета със
 * същия срок и без чакащо удължаване (по най-пресните данни), само в своя ден и през деня,
 * и най-много веднъж; без вход — нищо и без заявки.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class LoanReminderWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 18.10.2026, 10:00 в София — денят на напомнянето „2 дни преди“ срока 20.10.
    private val clock = FixedClock(Instant.parse("2026-10-18T07:00:00Z"))
    /** Уникално за теста — дневникът на известията (DataStore) се споделя между тестовете. */
    private val loanId = "L-" + UUID.randomUUID()

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        app.container = backend.container(app, clock)
        app.container.settings.setNotifyLoans(true)
    }

    @After fun tearDown() = backend.shutdown()

    private fun loan(due: String = "2026-10-20", pending: Boolean = false) =
        """{"loanId":"$loanId","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-20","dateDue":"$due","renewPending":$pending}"""

    private fun signIn() = runBlocking<Unit> {
        assertIs<Outcome.Success<Unit>>(app.container.authRepository.login("0042", "1234".toCharArray(), remember = false))
    }

    private fun run(kind: LoanReminderKind = LoanReminderKind.BEFORE, due: String = "2026-10-20"): ListenableWorker.Result = runBlocking {
        TestListenableWorkerBuilder<LoanReminderWorker>(app)
            .setInputData(
                workDataOf(
                    LoanReminderScheduler.KEY_LOAN to loanId,
                    LoanReminderScheduler.KEY_DUE to due,
                    LoanReminderScheduler.KEY_KIND to kind.name,
                ),
            )
            .build()
            .doWork()
    }

    private fun reminders() = nm.activeNotifications.filter { it.id == NotificationIds.dueSoon(loanId) }

    @Test fun remindsTwoDaysBeforeWithTitleAndDate() {
        signIn()
        backend.loans(loan())
        assertIs<ListenableWorker.Result.Success>(run())
        val n = reminders().single()
        assertEquals(Channel.LOANS.id, n.notification.channelId)
        val text = n.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT).toString()
        assertTrue("Под игото" in text && "20.10" in text, text)
        // Вече показано — повторното пускане (напр. след рестарт) не го повтаря.
        nm.cancelAll()
        run()
        assertTrue(reminders().isEmpty())
    }

    @Test fun returnedBookIsNotReminded() {
        signIn()
        backend.loans()
        run()
        assertEquals(1, backend.count("${TestBackend.API}/v1/me/loans"))
        assertTrue(reminders().isEmpty())
    }

    @Test fun renewedOrPendingLoanIsNotReminded() {
        signIn()
        backend.loans(loan(due = "2026-11-10"))
        run()
        assertTrue(reminders().isEmpty())
        backend.loans(loan(pending = true))
        clock.advanceSeconds(31 * 60) // кешът вече е стар → нова заявка
        run()
        assertEquals(2, backend.count("${TestBackend.API}/v1/me/loans"))
        assertTrue(reminders().isEmpty())
    }

    @Test fun freshCacheIsUsedWithoutNetwork() {
        signIn()
        backend.loans(loan())
        runBlocking { app.container.libraryRepository.loans(force = true) }
        clock.advanceSeconds(10 * 60)
        run()
        assertEquals(1, backend.count("${TestBackend.API}/v1/me/loans"))
        assertEquals(1, reminders().size)
    }

    @Test fun signedOutMeansNothingAndNoRequests() {
        backend.loans(loan())
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(0, backend.count("${TestBackend.API}/v1/me/loans"))
        assertTrue(reminders().isEmpty())
    }

    @Test fun lateOrNightDeliveryIsDropped() {
        signIn()
        backend.loans(loan())
        // Закъсняла задача: денят на „2 дни преди“ е минал.
        clock.advanceSeconds(24 * 3600)
        run()
        assertTrue(reminders().isEmpty())
        // В деня на срока, но в 22:00 — тихите часове.
        clock.advanceSeconds(24 * 3600 + 12 * 3600)
        run(LoanReminderKind.DUE_DAY)
        assertTrue(reminders().isEmpty())
        assertEquals(0, backend.count("${TestBackend.API}/v1/me/loans"))
    }

    @Test fun dueDayReminderReplacesTheEarlierOne() {
        signIn()
        backend.loans(loan())
        run()
        assertEquals(1, reminders().size)
        // В деня на срока, 10:00 — едно и също ID: „днес“ сменя „наближава“.
        clock.advanceSeconds(2 * 24 * 3600)
        run(LoanReminderKind.DUE_DAY)
        val n = reminders().single()
        assertEquals("Под игото", n.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT).toString())
    }

    @Test fun disabledLoanNotificationsShowNothing() = runBlocking<Unit> {
        app.container.settings.setNotifyLoans(false)
        try {
            signIn()
            backend.loans(loan())
            run()
            assertTrue(reminders().isEmpty())
            assertEquals(0, backend.count("${TestBackend.API}/v1/me/loans"))
        } finally {
            app.container.settings.setNotifyLoans(true)
        }
    }
}
