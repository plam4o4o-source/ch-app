package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import kotlinx.coroutines.runBlocking
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
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * „Удължи“ от известието: приемникът веднага сменя известието и пуска задачата; задачата
 * (и при спряно приложение — сесията от хранилището) праща заявката и подменя известието
 * с резултата — изпратена заявка или причината за неуспех.
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

    private fun run(): ListenableWorker.Result = runBlocking {
        TestListenableWorkerBuilder<RenewActionWorker>(app)
            .setInputData(
                workDataOf(
                    RenewActionWorker.KEY_LOAN to loanId,
                    RenewActionWorker.KEY_NOTIFICATION to notificationId,
                    RenewActionWorker.KEY_TITLE to "Под игото",
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

        assertIs<ListenableWorker.Result.Success>(run())

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

    @Test fun signedOutAsksToSignInWithoutRequest() {
        backend.json(renewPath, loan(pending = true), code = 202)
        run()
        assertEquals(0, backend.count(renewPath))
        assertTrue(app.getString(R.string.error_unauthorized) in shown().text())
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
        }
        RenewActionReceiver().onReceive(app, intent)

        assertEquals(app.getString(R.string.notif_renew_sending), shown().title())
        val work = WorkManager.getInstance(app).getWorkInfosForUniqueWork(RenewActionWorker.uniqueName(loanId)).get()
        // Чака мрежа (тестовият WorkManager не я „вижда“) — задачата е поставена веднъж.
        assertEquals(1, work.size)
        assertTrue(work.single().state == WorkInfo.State.ENQUEUED || work.single().state == WorkInfo.State.SUCCEEDED)
        // Второ натискане — без втора задача.
        RenewActionReceiver().onReceive(app, intent)
        assertEquals(1, WorkManager.getInstance(app).getWorkInfosForUniqueWork(RenewActionWorker.uniqueName(loanId)).get().size)
    }
}
