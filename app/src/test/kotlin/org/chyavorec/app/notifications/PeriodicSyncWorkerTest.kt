package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.TestResources
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.Inbox
import org.chyavorec.domain.model.MessagePollPolicy
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
 * Общата фонова задача ([PeriodicSyncWorker]): известие за просрочена книга — веднъж и не
 * нощем; без вход — без заявки за заемания; нова новина → известие; лично съобщение —
 * известие със скрито съдържание на заключен екран.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class PeriodicSyncWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 17.10.2026, 13:00 в София — през деня.
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    /** Уникално за теста — настройките (DataStore) се споделят между тестовете в процеса. */
    private val loanId = "L-" + UUID.randomUUID()

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.json("/data/app-messages.json", "[]")
        backend.json("$API/v1/me/messages", """{"items":[]}""")
        app.container = backend.container(app, clock)
        val s = app.container.settings
        // Само проверяваната стъпка е включена; „дължими“ — от нулата.
        s.setNotifyMessages(false)
        s.setNotifyNews(false)
        s.setNotifyLoans(true)
        listOf("news", "loans", "events").forEach { s.setLastSyncStep(it, 0) }
        // Приложението е отворено току-що (не „спи“ — вж. 14-дневното правило).
        s.markAppOpened(clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, 0)
    }

    @After fun tearDown() = backend.shutdown()

    private fun run(): ListenableWorker.Result = runBlocking { TestListenableWorkerBuilder<PeriodicSyncWorker>(app).build().doWork() }

    private fun signIn() = runBlocking<Unit> {
        assertIs<Outcome.Success<Unit>>(app.container.authRepository.login("0042", "1234".toCharArray(), remember = false))
    }

    private fun overdue() = nm.activeNotifications.filter { it.id == NotificationIds.overdue(loanId) }

    private val overdueLoan get() = """{"loanId":"$loanId","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-01","dateDue":"2026-10-10"}"""

    @Test fun overdueLoanNotifiesOnceAndMarksStepDone() = runBlocking<Unit> {
        signIn()
        backend.loans(overdueLoan)
        assertIs<ListenableWorker.Result.Success>(run())
        val shown = overdue().single()
        assertEquals(Channel.LOANS.id, shown.notification.channelId)
        // Заглавието на книгата е лично — скрито на заключен екран.
        assertEquals(Notification.VISIBILITY_PRIVATE, shown.notification.visibility)
        assertTrue("$loanId:OVERDUE" in app.container.settings.notifiedLoans())
        assertEquals(clock.now().toEpochMilli(), app.container.settings.lastSyncStep("loans"))

        // Следващото пускане (стъпката пак е дължима) не повтаря известието.
        nm.cancelAll()
        app.container.settings.setLastSyncStep("loans", 0)
        clock.advanceSeconds(3600) // 14:00 същия ден
        run()
        assertEquals(2, backend.count("$API/v1/me/loans"))
        assertTrue(overdue().isEmpty())
    }

    @Test fun quietHoursDeferTheStep() = runBlocking<Unit> {
        clock.advanceSeconds(10 * 3600) // 23:00 в София
        signIn()
        backend.loans(overdueLoan)
        run()
        assertTrue(overdue().isEmpty())
        // Стъпката остава дължима — опит при следващото пускане (през деня).
        assertEquals(0L, app.container.settings.lastSyncStep("loans"))
        assertTrue("$loanId:OVERDUE" !in app.container.settings.notifiedLoans())
    }

    @Test fun signedOutReaderMeansNoLoanRequests() = runBlocking<Unit> {
        backend.loans(overdueLoan)
        run()
        assertEquals(0, backend.count("$API/v1/me/loans"))
        assertTrue(overdue().isEmpty())
        // Няма какво да се тегли — стъпката е изпълнена.
        assertEquals(clock.now().toEpochMilli(), app.container.settings.lastSyncStep("loans"))
    }

    /** Временно нечетима сесия не е изход: без заявки, но стъпката остава дължима за следващото пускане. */
    @Test fun unreadableSessionKeepsLoanStepDue() = runBlocking<Unit> {
        backend.loans(overdueLoan)
        backend.unreadableSession(app, clock)
        try {
            assertIs<ListenableWorker.Result.Success>(run())
            assertEquals(0, backend.count("$API/v1/me/loans"))
            assertTrue(overdue().isEmpty())
            assertEquals(0L, app.container.settings.lastSyncStep("loans"))
        } finally {
            app.container.authRepository.signOutLocally()
        }
    }

    @Test fun loanNotificationsOffStillSkipNetworkWithoutWidget() = runBlocking<Unit> {
        app.container.settings.setNotifyLoans(false)
        try {
            signIn()
            backend.loans(overdueLoan)
            run()
            assertEquals(0, backend.count("$API/v1/me/loans"))
        } finally {
            app.container.settings.setNotifyLoans(true)
        }
    }

    @Test fun newArticleNotifies() = runBlocking<Unit> {
        backend.json("/data/news.json", TestResources.text("site-news.json"))
        backend.json("/rss.xml", TestResources.text("site-rss.xml"))
        val s = app.container.settings
        s.setNotifyNews(true)
        s.setLastNewsId("an-older-article-" + UUID.randomUUID())
        run()
        val news = nm.activeNotifications.single { it.id == NotificationIds.NEWS }
        assertEquals(Channel.NEWS.id, news.notification.channelId)
        val newest = (app.container.newsRepository.latest(false) as Outcome.Success).value.data.first()
        assertEquals(newest.id, s.current().lastNewsId)
        assertEquals(clock.now().toEpochMilli(), s.lastSyncStep("news"))
    }

    @Test fun personalMessageNotifiesWithHiddenContent() = runBlocking<Unit> {
        val s = app.container.settings
        s.setNotifyMessages(true)
        signIn()
        // Първото пускане след вход само запомня наличните лични съобщения.
        run()
        val messageId = "pm-" + UUID.randomUUID()
        backend.json(
            "$API/v1/me/messages",
            """{"items":[{"id":"$messageId","title":"Запазена книга","text":"Книгата те чака","at":"2026-10-17T09:30:00Z","read":false}]}""",
        )
        clock.advanceSeconds(6 * 60) // след краткия кеш на личните съобщения
        run()
        val shown = nm.activeNotifications.single { it.id == NotificationIds.message(Inbox.personalId(messageId)) }
        assertEquals(Notification.VISIBILITY_PRIVATE, shown.notification.visibility)
        // Вече известено — не се повтаря.
        nm.cancelAll()
        clock.advanceSeconds(6 * 60)
        run()
        assertTrue(nm.activeNotifications.none { it.id == NotificationIds.message(Inbox.personalId(messageId)) })
    }

    @Test fun dormantAppStopsSiteMessagesButKeepsPersonalOnes() = runBlocking<Unit> {
        val s = app.container.settings
        s.setNotifyMessages(true)
        signIn()
        // Не е отваряно 15 дни: съобщенията от сайта не се проверяват (политиката за поверителност).
        s.markAppOpened(clock.now().toEpochMilli() - 15 * DAY_MS, MessagePollPolicy.BASE_MILLIS, 0)
        run()
        assertEquals(0, backend.count("/data/app-messages.json"))
        // Личните съобщения на влезлия читател — да.
        assertEquals(1, backend.count("$API/v1/me/messages"))
        // Отворено отново — и сайтът.
        s.markAppOpened(clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, 0)
        clock.advanceSeconds(6 * 60)
        run()
        assertEquals(1, backend.count("/data/app-messages.json"))
        assertEquals(2, backend.count("$API/v1/me/messages"))
    }

    @Test fun loansJustFetchedWithMeAllAreNotRequestedAgain() = runBlocking<Unit> {
        backend.json(
            "$API/v1/capabilities",
            """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"renew":true,"history":false,"messages":true,"all":true}""",
        )
        backend.json("$API/v1/me/all", """{"loans":{"loans":[$overdueLoan]},"messages":{"items":[]}}""")
        backend.loans(overdueLoan)
        app.container.settings.setNotifyMessages(true)
        signIn()
        run()
        // Стъпката „съобщения“ е взела всичко с една заявка — заеманията са от нея, без втора.
        assertEquals(1, backend.count("$API/v1/me/all"))
        assertEquals(0, backend.count("$API/v1/me/loans"))
        assertEquals(Channel.LOANS.id, overdue().single().notification.channelId)
        assertEquals(clock.now().toEpochMilli(), app.container.settings.lastSyncStep("loans"))
    }

    private fun renewLoan(due: String, pending: Boolean = false, result: String? = null): String {
        val renewResult = if (result != null) ""","renewResult":$result""" else ""
        return """{"loanId":"$loanId","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-20","dateDue":"$due",""" +
            """"canRenew":true,"renewPending":$pending$renewResult}"""
    }

    private fun renewNotes() = nm.activeNotifications.filter { it.id == NotificationIds.renew(loanId) }

    /** Следващото пускане след [hours] часа (стъпката за заеманията пак е дължима). */
    private fun runLater(hours: Long = 1) = runBlocking {
        app.container.settings.setLastSyncStep("loans", 0)
        clock.advanceSeconds(hours * 3600)
        run()
    }

    @Test fun approvedRenewalNotifiesOnceWithNewDueDate() = runBlocking<Unit> {
        signIn()
        backend.loans(renewLoan("2026-10-21", pending = true))
        run()
        assertTrue(renewNotes().isEmpty())

        backend.loans(renewLoan("2026-11-12", result = """{"status":"done","at":"2026-10-17T10:30:00Z"}"""))
        runLater()
        val n = renewNotes().single().notification
        assertEquals(Channel.LOANS.id, n.channelId)
        val text = n.extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        assertEquals(app.getString(R.string.notif_renew_done, "Под игото", Formatters.shortDate(app, "2026-11-12")), text)
        // Заглавието на книгата не се вижда на заключен екран.
        assertEquals(Notification.VISIBILITY_PRIVATE, n.visibility)
        assertEquals(
            app.getString(R.string.notif_public_renew),
            n.publicVersion!!.extras.getCharSequence(Notification.EXTRA_TITLE).toString(),
        )

        // Веднъж: следващото опресняване със същите данни не го повтаря.
        nm.cancelAll()
        runLater()
        assertTrue(renewNotes().isEmpty())
    }

    @Test fun rejectedRenewalNotifiesWithReason() = runBlocking<Unit> {
        signIn()
        backend.loans(renewLoan("2026-10-21", pending = true))
        run()
        backend.loans(renewLoan("2026-10-21", result = """{"status":"rejected","reason":"Запазена е от друг читател.","at":"2026-10-17T10:30:00Z"}"""))
        runLater()
        val text = renewNotes().single().notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        assertEquals(app.getString(R.string.notif_renew_rejected, "Под игото", "Запазена е от друг читател."), text)
    }

    @Test fun oldRenewalResultWithoutPendingRequestIsSilent() = runBlocking<Unit> {
        signIn()
        // Отговорът е отпреди (напр. видян на друг телефон) — заявката не е била „чакаща“ тук.
        backend.loans(renewLoan("2026-10-21", result = """{"status":"rejected","reason":"Не","at":"2026-10-10T10:30:00Z"}"""))
        run()
        assertTrue(renewNotes().isEmpty())
    }

    @Test fun renewalOutcomeWaitsForDaytime() = runBlocking<Unit> {
        signIn()
        backend.loans(renewLoan("2026-10-21", pending = true))
        run()
        backend.loans(renewLoan("2026-11-12", result = """{"status":"done","at":"2026-10-17T19:30:00Z"}"""))
        runLater(hours = 10) // 23:00 в София
        assertTrue(renewNotes().isEmpty())
        // Стъпката остава дължима; сутринта — известието.
        assertEquals(0L, app.container.settings.lastSyncStep("loans"))
        clock.advanceSeconds(10 * 3600) // 09:00
        run()
        assertEquals(1, renewNotes().size)
    }

    @Test fun disabledLoanNotificationsMeanNoRenewalNotification() = runBlocking<Unit> {
        signIn()
        backend.loans(renewLoan("2026-10-21", pending = true))
        run()
        app.container.settings.setNotifyLoans(false)
        try {
            backend.loans(renewLoan("2026-11-12", result = """{"status":"done","at":"2026-10-17T10:30:00Z"}"""))
            // Без известия — заеманията се теглят само за уиджета (тук няма) → нищо.
            runLater()
            assertTrue(renewNotes().isEmpty())
        } finally {
            app.container.settings.setNotifyLoans(true)
        }
    }

    @Test fun overdueRenewableLoanHasRenewAction() = runBlocking<Unit> {
        signIn()
        backend.loans(renewLoan("2026-10-10"))
        run()
        val n = overdue().single().notification
        assertEquals(Notification.CATEGORY_REMINDER, n.category)
        assertEquals(app.getString(R.string.notif_action_renew), n.actions.single().title.toString())
    }

    private companion object {
        const val API = TestBackend.API
        const val DAY_MS = 24 * 60 * 60 * 1000L
    }
}
