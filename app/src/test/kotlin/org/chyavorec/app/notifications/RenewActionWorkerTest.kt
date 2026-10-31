package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.TestBackend.Companion.API
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * „Удължи“ от известието: приемникът веднага сменя известието и пуска задачата; задачата
 * (и при спряно приложение — сесията от хранилището) праща заявката и подменя известието
 * с резултата — изпратена заявка или причината за неуспех. Само от името на читателя на
 * известието: след изход или при друг читател — тихо, без заявка.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class RenewActionWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 18.10.2026, 10:00 в София.
    private val clock = FixedClock(Instant.parse("2026-10-18T07:00:00Z"))
    private val loanId = "L-" + UUID.randomUUID()
    private val notificationId get() = NotificationIds.dueSoon(loanId)
    private val renewPath get() = "$API/v1/me/loans/$loanId/renew"

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        app.container = backend.container(app, clock)
        app.container.settings.setNotifyLoans(true)
    }

    @After fun tearDown() = backend.shutdown()

    private fun loan(pending: Boolean = false) =
        """{"loanId":"$loanId","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-20","dateDue":"2026-10-20","canRenew":true,"renewPending":$pending}"""

    private fun signIn() = runBlocking<Unit> {
        assertIs<Outcome.Success<Unit>>(app.container.authRepository.login("0042", "1234".toCharArray(), remember = true))
    }

    /** [reader] — читателят на известието (`null` — бутон от 1.2.28 и по-старите). */
    private fun run(attempt: Int = 0, reader: String? = null): ListenableWorker.Result = runBlocking {
        TestListenableWorkerBuilder<RenewActionWorker>(app)
            .setRunAttemptCount(attempt)
            .setInputData(
                workDataOf(
                    RenewActionWorker.KEY_LOAN to loanId,
                    RenewActionWorker.KEY_NOTIFICATION to notificationId,
                    RenewActionWorker.KEY_TITLE to "Под игото",
                    RenewActionWorker.KEY_READER to reader,
                ),
            )
            .build()
            .doWork()
    }

    private fun shown(): Notification = nm.activeNotifications.single { it.id == notificationId }.notification
    private fun Notification.title() = extras.getCharSequence(Notification.EXTRA_TITLE).toString()
    private fun Notification.text() = extras.getCharSequence(Notification.EXTRA_TEXT).toString()

    @Test fun sendsRequestAndReplacesNotification() = runBlocking<Unit> {
        signIn()
        backend.loans(loan())
        app.container.libraryRepository.loans(force = true)
        backend.json(renewPath, loan(pending = true), code = 202)
        // Приложението е спряно: нов процес, сесията — от хранилището.
        app.container = backend.container(app, clock)

        assertIs<ListenableWorker.Result.Success>(run(reader = "R1"))

        assertEquals(1, backend.count(renewPath))
        val n = shown()
        assertEquals(app.getString(R.string.notif_renew_sent), n.title())
        assertTrue("Под игото" in n.text(), n.text())
        // Резултатът е без бутони, в групата на заеманията и скрит на заключен екран.
        assertTrue(n.actions.isNullOrEmpty())
        assertEquals(Notifier.LOANS_GROUP, n.group)
        assertEquals(Notification.VISIBILITY_PRIVATE, n.visibility)
        // Чакащото удължаване е запомнено — отговорът на библиотеката ще бъде известие.
        assertTrue(app.container.settings.renewWatch().orEmpty().any { it.startsWith("$loanId|") })
        assertTrue(app.container.libraryRepository.cachedLoans()!!.data.single().renewPending)
    }

    @Test fun conflictShowsTheReason() = runBlocking<Unit> {
        signIn()
        backend.json(renewPath, """{"error":"not_allowed"}""", code = 409)
        run()
        val n = shown()
        assertEquals(app.getString(R.string.notif_renew_failed), n.title())
        assertTrue(app.getString(R.string.renew_conflict_not_allowed) in n.text(), n.text())
    }

    /** „Изпраща се…“ със заглавието, както го оставя приемникът. */
    private fun sending() = Notifier.show(app, Channel.LOANS, notificationId, app.getString(R.string.notif_renew_sending), "Под игото", "my/loans")

    /** Изход между натискането и опита (напр. по време на паузата): без заявка и без известие със заглавието. */
    @Test fun signedOutFinishesSilently() {
        backend.json(renewPath, loan(pending = true), code = 202)
        sending()
        assertIs<ListenableWorker.Result.Success>(run(reader = "R1"))
        assertEquals(0, backend.count(renewPath))
        assertTrue(nm.activeNotifications.none { it.id == notificationId }, "известие след изхода")
    }

    /** Вход на друг читател между двата опита — не от неговото име. */
    @Test fun otherReaderIsNotRenewedFor() = runBlocking<Unit> {
        signIn()
        backend.json(renewPath, loan(pending = true), code = 202)
        sending()
        assertIs<ListenableWorker.Result.Success>(run(reader = "R0"))
        assertEquals(0, backend.count(renewPath))
        assertTrue(nm.activeNotifications.none { it.id == notificationId })
    }

    /** Изход: чакащият повтор на „Удължи“ (WorkManager, пауза между опитите) се отменя по етикета. */
    @Test fun signOutCancelsPendingRenewals() = runBlocking<Unit> {
        WorkManagerTestInitHelper.initializeTestWorkManager(
            app, Configuration.Builder().setExecutor(SynchronousExecutor()).build(),
        )
        val wm = WorkManager.getInstance(app)
        app.container.settings.setReaderTraces(true)
        app.container.watchReader()
        signIn()
        val waiting = OneTimeWorkRequestBuilder<RenewActionWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .addTag(RenewActionWorker.TAG)
            .build()
        wm.enqueueUniqueWork(RenewActionWorker.uniqueName(loanId), ExistingWorkPolicy.KEEP, waiting).result.get()
        app.container.authRepository.signOutLocally()
        withTimeout(5_000) { while (wm.getWorkInfoById(waiting.id).get()?.state != WorkInfo.State.CANCELLED) delay(20) }
        assertEquals(0, backend.count(renewPath))
    }

    /** Временно нечетима сесия не е „влез отново“: още опити, после „опитай пак по-късно“ — без заявка. */
    @Test fun unreadableSessionRetriesThenAsksToTryLater() = runBlocking<Unit> {
        backend.json(renewPath, loan(pending = true), code = 202)
        backend.unreadableSession(app, clock)
        try {
            assertIs<ListenableWorker.Result.Retry>(run())
            assertIs<ListenableWorker.Result.Retry>(run(attempt = 2))
            assertTrue(nm.activeNotifications.none { it.id == notificationId }, "докато има опити — без известие за неуспех")
            assertIs<ListenableWorker.Result.Success>(run(attempt = 3))
            assertEquals(0, backend.count(renewPath))
            val n = shown()
            assertEquals(app.getString(R.string.notif_renew_failed), n.title())
            assertTrue(app.getString(R.string.notif_renew_session_unavailable) in n.text(), n.text())
            assertTrue(app.getString(R.string.error_unauthorized) !in n.text(), n.text())
        } finally {
            app.container.authRepository.signOutLocally()
        }
    }

    @Test fun receiverShowsProgressAndEnqueuesExpeditedWork() {
        WorkManagerTestInitHelper.initializeTestWorkManager(
            app, Configuration.Builder().setExecutor(SynchronousExecutor()).build(),
        )
        val intent = Intent(app, RenewActionReceiver::class.java).apply {
            action = RenewActionReceiver.ACTION_RENEW
            putExtra(RenewActionReceiver.EXTRA_LOAN, loanId)
            putExtra(RenewActionReceiver.EXTRA_NOTIFICATION, notificationId)
            putExtra(RenewActionReceiver.EXTRA_TITLE, "Под игото")
            putExtra(RenewActionReceiver.EXTRA_READER, "R1")
        }
        RenewActionReceiver().onReceive(app, intent)

        assertEquals(app.getString(R.string.notif_renew_sending), shown().title())
        val work = WorkManager.getInstance(app).getWorkInfosForUniqueWork(RenewActionWorker.uniqueName(loanId)).get()
        // Чака мрежа (тестовият WorkManager не я „вижда“) — задачата е поставена веднъж.
        assertEquals(1, work.size)
        // С етикета — изходът го отменя (AppContainer.clearReaderTraces).
        assertTrue(RenewActionWorker.TAG in work.single().tags)
        assertTrue(work.single().state == WorkInfo.State.ENQUEUED || work.single().state == WorkInfo.State.SUCCEEDED)
        // Второ натискане — без втора задача.
        RenewActionReceiver().onReceive(app, intent)
        assertEquals(1, WorkManager.getInstance(app).getWorkInfosForUniqueWork(RenewActionWorker.uniqueName(loanId)).get().size)
    }
}
