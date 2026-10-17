package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.TestBackend
import org.chyavorec.app.TestResources
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.Inbox
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

    private companion object {
        const val API = TestBackend.API
    }
}
