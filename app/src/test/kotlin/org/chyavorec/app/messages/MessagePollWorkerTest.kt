package org.chyavorec.app.messages

import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.core.FixedClock
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
 * Честата проверка за съобщения от сайта ([MessagePollWorker]): известие за ново съобщение,
 * адаптивният интервал (1 → 2 ч. при непроменен файл), „не е време“ и „заспиване“ след
 * 14 дни без отваряне — без заявки, грешка на сайта — нов опит при следващото събуждане.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class MessagePollWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    private val clock = FixedClock(Instant.parse("2026-10-17T07:00:00Z"))
    /** Уникално за теста — настройките (DataStore) се споделят между тестовете в процеса. */
    private val id = "poll-" + UUID.randomUUID()
    private val now get() = clock.now().toEpochMilli()

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.json(
            "/data/app-messages.json",
            """[{"id":"$id","title":"Промяна в работното време","body":"От понеделник","audience":"all","priority":"high","createdAt":"2026-10-17T06:00:00Z"}]""",
        )
        app.container = backend.container(app, clock)
        val s = app.container.settings
        s.setNotifyMessages(true)
        s.setMessagesInitialized()
        // Отворено току-що, интервалът е най-краткият, без предишна проверка.
        s.markAppOpened(now, MessagePollPolicy.BASE_MILLIS, 0)
        s.setMessagePoll(0, MessagePollPolicy.BASE_MILLIS, "")
    }

    @After fun tearDown() = backend.shutdown()

    private fun run(): ListenableWorker.Result = runBlocking { TestListenableWorkerBuilder<MessagePollWorker>(app).build().doWork() }

    private fun ours() = nm.activeNotifications.filter { it.id == NotificationIds.message(id) }

    @Test fun newMessageNotifiesAndStartsTheAdaptiveInterval() = runBlocking<Unit> {
        assertIs<ListenableWorker.Result.Success>(run())
        val shown = ours().single()
        assertEquals(Channel.MESSAGES_IMPORTANT.id, shown.notification.channelId)
        assertEquals(1, backend.count("/data/app-messages.json"))
        val poll = app.container.settings.messagePoll()
        assertEquals(now, poll.lastPoll)
        // Нов файл (сменен отпечатък) — пак най-краткият интервал.
        assertEquals(MessagePollPolicy.BASE_MILLIS, poll.interval)
        assertTrue(!poll.signature.isNullOrEmpty())
    }

    @Test fun unchangedFileDoublesIntervalAndEarlyWakeSkipsNetwork() = runBlocking<Unit> {
        run()
        clock.advanceSeconds(61 * 60)
        run()
        assertEquals(2, backend.count("/data/app-messages.json"))
        assertEquals(2 * MessagePollPolicy.BASE_MILLIS, app.container.settings.messagePoll().interval)
        // След 30 мин. още не е време (интервалът е 2 ч.) — без заявка.
        clock.advanceSeconds(30 * 60)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(2, backend.count("/data/app-messages.json"))
        // Вече известеното съобщение не се показва втори път.
        nm.cancelAll()
        clock.advanceSeconds(2 * 60 * 60)
        run()
        assertEquals(3, backend.count("/data/app-messages.json"))
        assertTrue(ours().isEmpty())
    }

    @Test fun dormantAfterFourteenDaysWithoutOpening() = runBlocking<Unit> {
        app.container.settings.markAppOpened(now - 15 * 24 * 60 * 60 * 1000L, MessagePollPolicy.BASE_MILLIS, 0)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(0, backend.count("/data/app-messages.json"))
        assertTrue(ours().isEmpty())
    }

    @Test fun disabledMessagesNeverAskTheSite() = runBlocking<Unit> {
        app.container.settings.setNotifyMessages(false)
        try {
            run()
            assertEquals(0, backend.count("/data/app-messages.json"))
        } finally {
            app.container.settings.setNotifyMessages(true)
        }
    }

    @Test fun siteErrorKeepsScheduleForNextWake() = runBlocking<Unit> {
        backend.json("/data/app-messages.json", "boom", code = 503)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(1, backend.count("/data/app-messages.json"))
        // Нищо не е записано — следващото събуждане пита пак.
        assertEquals(0L, app.container.settings.messagePoll().lastPoll)
        assertTrue(ours().isEmpty())
    }
}
