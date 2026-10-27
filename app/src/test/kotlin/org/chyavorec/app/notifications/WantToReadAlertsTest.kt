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
import org.chyavorec.app.data.local.WantToReadEntity
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.MessagePollPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * „Кажи ми, когато е на рафта“ във фоновата задача ([PeriodicSyncWorker] → [WantToReadAlerts]):
 * известие при връщане на рафта — веднъж, в канала „Библиотека“, със скрито заглавие на
 * заключен екран; без книга с „Кажи ми“, с изключени известия или „спящо“ приложение —
 * каталогът изобщо не се пита; нощем известието изчаква сутринта.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class WantToReadAlertsTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 17.10.2026, 13:00 в София — през деня.
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    private val sample = TestResources.text("katalog-sample.json")
    private val dao get() = app.container.database.wantToRead()

    /** „Белият дома“ (инв. № 624) — в примерния каталог не е на рафта. */
    private val inv = 624L
    private val onShelf = sample.replace(Regex("(\"inv\": 624,[^}]*\"av\": )0")) { it.groupValues[1] + "1" }

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.json("/data/app-messages.json", "[]")
        backend.json("/katalog.json", sample)
        app.container = backend.container(app, clock)
        val s = app.container.settings
        // Само проверяваната стъпка.
        s.setNotifyMessages(false)
        s.setNotifyNews(false)
        s.setNotifyLoans(false)
        s.setNotifyLibrary(true)
        s.markAppOpened(clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, 0)
        dao.clear()
    }

    @After fun tearDown() = runBlocking<Unit> {
        app.container.settings.setNotifyLoans(true)
        app.container.settings.setNotifyNews(true)
        app.container.settings.setNotifyMessages(true)
        backend.shutdown()
    }

    private fun run(): ListenableWorker.Result = runBlocking { TestListenableWorkerBuilder<PeriodicSyncWorker>(app).build().doWork() }

    private fun shown() = nm.activeNotifications.filter { it.id == NotificationIds.wantToRead(inv) }

    private fun watch(notify: Boolean = true) = runBlocking {
        dao.upsert(WantToReadEntity(inv, "Белият дома", "Николай Симеонов", clock.now().toEpochMilli(), notify, lastKnownAvailable = false))
    }

    /** Следващото пускане — след прозореца на свежестта на каталога (30 мин.). */
    private fun runLater() {
        clock.advanceSeconds(31 * 60)
        run()
    }

    @Test fun bookBackOnTheShelfNotifiesOnce() = runBlocking<Unit> {
        watch()
        assertIs<ListenableWorker.Result.Success>(run())
        assertTrue(shown().isEmpty(), "още не е на рафта")
        assertFalse(dao.get(inv)!!.lastKnownAvailable)

        backend.json("/katalog.json", onShelf)
        runLater()
        val n = shown().single().notification
        assertEquals(Channel.LIBRARY.id, n.channelId)
        assertEquals(app.getString(R.string.notif_want_available, "Белият дома"), n.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        // Личният списък — на заключен екран само общ текст.
        assertEquals(Notification.VISIBILITY_PRIVATE, n.visibility)
        assertEquals(app.getString(R.string.notif_public_want), n.publicVersion!!.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        assertTrue(dao.get(inv)!!.lastKnownAvailable)
        // Списъкът остава след изход — известието също (не е на читателя от сесията).
        assertFalse(Notifier.isPersonal(n))
        Notifier.cancelPersonal(app)
        assertEquals(1, shown().size)

        // Същата наличност — без второ известие.
        nm.cancelAll()
        runLater()
        assertTrue(shown().isEmpty())
        assertEquals(3, backend.count("/katalog.json"))
    }

    @Test fun withoutNotifyFlagTheCatalogueIsNotRequested() {
        watch(notify = false)
        backend.json("/katalog.json", onShelf)
        run()
        assertEquals(0, backend.count("/katalog.json"))
        assertTrue(shown().isEmpty())
    }

    @Test fun libraryNotificationsOffOrDormantAppSkipTheCheck() = runBlocking<Unit> {
        watch()
        backend.json("/katalog.json", onShelf)
        app.container.settings.setNotifyLibrary(false)
        run()
        assertEquals(0, backend.count("/katalog.json"))
        app.container.settings.setNotifyLibrary(true)
        // Не е отваряно 15 дни — както съобщенията от сайта, каталогът не се пита.
        app.container.settings.markAppOpened(clock.now().toEpochMilli() - 15 * 24 * 3600_000L, MessagePollPolicy.BASE_MILLIS, 0)
        run()
        assertEquals(0, backend.count("/katalog.json"))
        assertTrue(shown().isEmpty())
        assertFalse(dao.get(inv)!!.lastKnownAvailable)
    }

    @Test fun atNightTheNotificationWaitsForTheMorning() = runBlocking<Unit> {
        watch()
        backend.json("/katalog.json", onShelf)
        clock.advanceSeconds(10 * 3600) // 23:00 в София
        run()
        assertTrue(shown().isEmpty())
        assertFalse(dao.get(inv)!!.lastKnownAvailable, "преходът не е записан — известието е за сутринта")
        clock.advanceSeconds(10 * 3600) // 09:00
        run()
        assertEquals(Channel.LIBRARY.id, shown().single().notification.channelId)
        assertTrue(dao.get(inv)!!.lastKnownAvailable)
    }
}
