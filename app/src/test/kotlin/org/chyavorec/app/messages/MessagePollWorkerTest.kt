package org.chyavorec.app.messages

import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Честата проверка за съобщения от сайта ([MessagePollWorker]): известие за ново съобщение,
 * адаптивният интервал (1 → 2 ч. при непроменен файл), „не е време“ и „заспиване“ след
 * 14 дни без отваряне — без заявки, грешка на сайта — нов опит след час. Еднократната задача
 * се планира точно за следващата дължима проверка и изобщо не се планира, когато няма смисъл.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class MessagePollWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val wm get() = WorkManager.getInstance(app)
    private val backend = TestBackend()
    private val clock = FixedClock(Instant.parse("2026-10-17T07:00:00Z"))
    /** Уникално за теста — настройките (DataStore) се споделят между тестовете в процеса. */
    private val id = "poll-" + UUID.randomUUID()
    private val now get() = clock.now().toEpochMilli()
    private val body get() =
        """[{"id":"$id","title":"Промяна в работното време","body":"От понеделник","audience":"all","priority":"high","createdAt":"2026-10-17T06:00:00Z"}]"""

    @Before fun setUp() = runBlocking<Unit> {
        WorkManagerTestInitHelper.initializeTestWorkManager(app, Configuration.Builder().setExecutor(SynchronousExecutor()).build())
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.json("/data/app-messages.json", body)
        app.container = backend.container(app, clock)
        val s = app.container.settings
        s.setNotifyMessages(true)
        s.setMessagesInitialized()
        // Отворено току-що, интервалът е най-краткият, без предишна проверка.
        s.markAppOpened(now, MessagePollPolicy.BASE_MILLIS, 0)
        s.setMessagePoll(0, MessagePollPolicy.BASE_MILLIS, "")
    }

    @After fun tearDown() = backend.shutdown()

    /** Пуска задачата; планираното преди това се отменя — остава само каквото планира тя. */
    private fun run(): ListenableWorker.Result = runBlocking {
        wm.cancelUniqueWork(MessagePollWorker.NAME).result.get()
        TestListenableWorkerBuilder<MessagePollWorker>(app).build().doWork()
    }

    /** Чакащата (планирана) следваща проверка или `null`. */
    private fun next(): WorkInfo? =
        wm.getWorkInfosForUniqueWork(MessagePollWorker.NAME).get().singleOrNull { it.state == WorkInfo.State.ENQUEUED }

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
        // Следващата проверка е планирана точно след интервала — без будене на всеки час „за нищо“.
        val work = next()!!
        assertEquals(MessagePollPolicy.BASE_MILLIS, work.initialDelayMillis)
        assertTrue(work.constraints.requiresBatteryNotLow())
    }

    @Test fun unchangedFileDoublesIntervalAndEarlyWakeSkipsNetwork() = runBlocking<Unit> {
        run()
        clock.advanceSeconds(61 * 60)
        run()
        assertEquals(2, backend.count("/data/app-messages.json"))
        assertEquals(2 * MessagePollPolicy.BASE_MILLIS, app.container.settings.messagePoll().interval)
        assertEquals(2 * MessagePollPolicy.BASE_MILLIS, next()!!.initialDelayMillis)
        // След 30 мин. още не е време (интервалът е 2 ч.) — без заявка; следващата — след остатъка.
        clock.advanceSeconds(30 * 60)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(2, backend.count("/data/app-messages.json"))
        assertEquals(90 * 60 * 1000L, next()!!.initialDelayMillis)
        // Вече известеното съобщение не се показва втори път.
        nm.cancelAll()
        clock.advanceSeconds(2 * 60 * 60)
        run()
        assertEquals(3, backend.count("/data/app-messages.json"))
        assertTrue(ours().isEmpty())
    }

    @Test fun openingTheAppDuringTheRequestKeepsTheShortInterval() = runBlocking<Unit> {
        run()
        clock.advanceSeconds(61 * 60)
        run()
        assertEquals(2 * MessagePollPolicy.BASE_MILLIS, app.container.settings.messagePoll().interval)
        clock.advanceSeconds(2 * 60 * 60)
        // Докато задачата чака сайта, читателят отваря приложението (MainActivity.onStart).
        backend.routes["/data/app-messages.json"] = {
            runBlocking { app.container.settings.markAppOpened(now, MessagePollPolicy.BASE_MILLIS, 0) }
            MockResponse().setBody(body)
        }
        run()
        assertEquals(3, backend.count("/data/app-messages.json"))
        // Непроменен файл, но отварянето печели: 1 ч., а не удвоените 4 ч.
        assertEquals(MessagePollPolicy.BASE_MILLIS, app.container.settings.messagePoll().interval)
        assertEquals(MessagePollPolicy.BASE_MILLIS, next()!!.initialDelayMillis)
    }

    @Test fun dormantAfterFourteenDaysWithoutOpening() = runBlocking<Unit> {
        app.container.settings.markAppOpened(now - 15 * 24 * 60 * 60 * 1000L, MessagePollPolicy.BASE_MILLIS, 0)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(0, backend.count("/data/app-messages.json"))
        assertTrue(ours().isEmpty())
        // Нищо не е планирано — до следващото отваряне.
        assertNull(next())
        // И планиране отвън (напр. старт на процеса) не я връща.
        MessagePollWorker.schedule(app, app.container, keep = true)
        assertNull(next())
    }

    @Test fun disabledMessagesNeverAskTheSiteAndCancelThePoll() = runBlocking<Unit> {
        MessagePollWorker.schedule(app, app.container)
        assertEquals(0L, next()!!.initialDelayMillis)
        app.container.settings.setNotifyMessages(false)
        try {
            // Изключената настройка отменя вече планираната проверка.
            MessagePollWorker.schedule(app, app.container)
            assertNull(next())
            run()
            assertEquals(0, backend.count("/data/app-messages.json"))
            assertNull(next())
        } finally {
            app.container.settings.setNotifyMessages(true)
        }
        // Включена отново — веднага (няма предишна проверка).
        MessagePollWorker.schedule(app, app.container)
        assertEquals(0L, next()!!.initialDelayMillis)
    }

    @Test fun siteErrorRetriesAfterTheShortestInterval() = runBlocking<Unit> {
        backend.json("/data/app-messages.json", "boom", code = 503)
        assertIs<ListenableWorker.Result.Success>(run())
        assertEquals(1, backend.count("/data/app-messages.json"))
        // Нищо не е записано — следващото събуждане пита пак, но след час (не веднага).
        assertEquals(0L, app.container.settings.messagePoll().lastPoll)
        assertTrue(ours().isEmpty())
        assertEquals(MessagePollPolicy.BASE_MILLIS, next()!!.initialDelayMillis)
    }
}
