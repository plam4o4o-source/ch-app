package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.data.local.EventReminderEntity
import org.chyavorec.app.data.local.RemindersDao
import org.chyavorec.app.update.UpdateDownloadWorker
import org.chyavorec.core.FixedClock
import org.chyavorec.data.update.UpdateInfo
import org.chyavorec.domain.model.Event
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Живите известия (Android 16+, Robolectric по подразбиране е на 36): ходът на изтеглянето и
 * „събитието започва скоро“ — ProgressStyle, молба за повишаване, чип (процент/обратно броене),
 * изтичане; под 36 — обичайните известия без промяна. Самото повишаване (FLAG_PROMOTED_ONGOING,
 * чипът в лентата) прави системата — в Robolectric не може да се провери.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class LiveUpdatesTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 17.10.2026, 13:00 в София (UTC+3).
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    private val now = clock.now().toEpochMilli()
    private val info = UpdateInfo(
        versionCode = 5, versionName = "1.1.0", apkUrl = "https://example.org/app.apk", sha256 = "0".repeat(64),
        sizeBytes = 1000, minSdk = 26, notes = "", releaseUrl = null,
    )

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        app.container = backend.container(app, clock)
        app.container.settings.setNotifyEvents(true)
        WorkManagerTestInitHelper.initializeTestWorkManager(app, Configuration.Builder().setExecutor(SynchronousExecutor()).build())
        // Приложение за карти — за „Маршрут“.
        shadowOf(app.packageManager).addResolveInfoForIntent(
            Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode("Читалището, с. Яворец"))).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            ResolveInfo().apply { isDefault = true; activityInfo = ActivityInfo().apply { packageName = "com.example.maps"; name = "Maps" } },
        )
    }

    @After fun tearDown() = backend.shutdown()

    private fun Notification.template() = extras.getString(Notification.EXTRA_TEMPLATE)
    private fun Notification.text(key: String) = extras.getCharSequence(key)?.toString()
    private fun Notification.promotionRequested() = extras.getBoolean(LiveUpdates.EXTRA_REQUEST_PROMOTED_ONGOING)
    private fun Notification.progressStyle() = Notification.Builder.recoverBuilder(app, this).style as Notification.ProgressStyle

    @Test fun downloadProgressIsALiveUpdate() {
        val n = UpdateDownloadWorker.progressNotification(app, info, 0.45f)
        assertEquals(Notification.ProgressStyle::class.java.name, n.template())
        assertTrue(n.promotionRequested())
        assertEquals("45%", n.extras.getString("android.shortCriticalText"))
        assertEquals(app.getString(R.string.notif_update_downloading, "1.1.0", 45), n.text(Notification.EXTRA_TITLE))
        assertEquals(45, n.extras.getInt(Notification.EXTRA_PROGRESS))
        assertEquals(100, n.extras.getInt(Notification.EXTRA_PROGRESS_MAX))
        assertTrue((n.flags and Notification.FLAG_ONGOING_EVENT) != 0)
        assertFalse(n.extras.getBoolean(Notification.EXTRA_COLORIZED), "повишеното известие не може да е оцветено")
        assertEquals(Channel.UPDATES.id, n.channelId)
        assertEquals(listOf(app.getString(R.string.action_cancel)), n.actions.map { it.title.toString() })
        // Лентата е в златото на марката.
        val segment = n.progressStyle().progressSegments.single()
        assertEquals(100, segment.length)
        assertEquals(ContextCompat.getColor(app, R.color.brand_gold), segment.color)
    }

    @Test fun unknownSizeIsIndeterminateWithoutChip() {
        val n = UpdateDownloadWorker.progressNotification(app, info, null)
        assertEquals(Notification.ProgressStyle::class.java.name, n.template())
        assertTrue(n.extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE))
        assertNull(n.extras.getString("android.shortCriticalText"))
    }

    @Config(sdk = [35])
    @Test fun downloadProgressBelow36IsThePlainNotification() {
        val n = UpdateDownloadWorker.progressNotification(app, info, 0.45f)
        assertNotEquals(Notification.ProgressStyle::class.java.name, n.template())
        assertFalse(n.promotionRequested())
        assertEquals(45, n.extras.getInt(Notification.EXTRA_PROGRESS))
        assertEquals(listOf(app.getString(R.string.action_cancel)), n.actions.map { it.title.toString() })
    }

    @Test fun eventLiveUpdateCountsDownToTheStart() {
        // Начало в 13:40 — 40 минути напред.
        val start = now + 40 * 60_000L
        val n = EventLiveUpdates.notification(app, "e-1", "Вечер на поезията", "Читалището", start, now, clock.zone())
        assertEquals(Notification.ProgressStyle::class.java.name, n.template())
        assertTrue(n.promotionRequested())
        assertEquals(Channel.EVENTS.id, n.channelId)
        assertEquals("Вечер на поезията", n.text(Notification.EXTRA_TITLE))
        assertEquals(app.getString(R.string.notif_event_live_text_place, "Читалището", "13:40"), n.text(Notification.EXTRA_TEXT))
        // Изминали 20 от 60 минути; последните 15 — в бордо, точка на половината.
        assertEquals(20, n.extras.getInt(Notification.EXTRA_PROGRESS))
        assertEquals(60, n.extras.getInt(Notification.EXTRA_PROGRESS_MAX))
        val style = n.progressStyle()
        assertEquals(listOf(45, 15), style.progressSegments.map { it.length })
        assertEquals(ContextCompat.getColor(app, R.color.brand_burgundy), style.progressSegments.last().color)
        assertEquals(listOf(30), style.progressPoints.map { it.position })
        // Чипът: обратно броене до началото (хронометър, без опресняване всяка минута).
        assertEquals(start, n.`when`)
        assertTrue(n.extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER))
        assertTrue(n.extras.getBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN))
        // Изчезва 5 минути след началото.
        assertEquals(45 * 60_000L, n.timeoutAfter)
        assertTrue((n.flags and Notification.FLAG_ONGOING_EVENT) != 0)
        assertEquals(listOf(app.getString(R.string.notif_action_route)), n.actions.map { it.title.toString() })
        assertTrue(shadowOf(n.actions.single().actionIntent).isActivity)
    }

    @Test fun eventWithoutPlaceHasNoRoute() {
        val n = EventLiveUpdates.notification(app, "e-2", "Сбирка", null, now + 50 * 60_000L, now, clock.zone())
        assertEquals(app.getString(R.string.notif_event_live_text, "13:50"), n.text(Notification.EXTRA_TEXT))
        assertTrue(n.actions.isNullOrEmpty())
    }

    /** Напомняне за събитие с час — и живо известие за T−60 мин; отмяната маха и двете. */
    @Test fun reminderSchedulesTheLiveUpdateAnHourBefore() = runBlocking<Unit> {
        val scheduler = ReminderScheduler(app, NoDao, clock)
        assertTrue(scheduler.schedule(event(time = "18:30")))
        val live = liveWork("ev-1").single()
        assertEquals(WorkInfo.State.ENQUEUED, live.state)
        // 18:30 − 1 ч − 13:00 = 4 ч 30 мин.
        assertEquals((4 * 60 + 30) * 60_000L, live.initialDelayMillis)
        scheduler.cancel("ev-1")
        assertTrue(liveWork("ev-1").all { it.state == WorkInfo.State.CANCELLED })
    }

    @Test fun eventWithoutTimeHasNoLiveUpdate() = runBlocking<Unit> {
        assertTrue(ReminderScheduler(app, NoDao, clock).schedule(event(date = "2026-10-18", time = null)))
        assertTrue(liveWork("ev-1").isEmpty())
    }

    @Config(sdk = [35])
    @Test fun below36OnlyTheUsualReminder() = runBlocking<Unit> {
        assertTrue(ReminderScheduler(app, NoDao, clock).schedule(event(time = "18:30")))
        assertTrue(liveWork("ev-1").isEmpty())
    }

    @Test fun workerPostsAndSchedulesOneRefresh() = runBlocking<Unit> {
        runLive(start = now + 40 * 60_000L)
        val n = shown("ev-1")!!
        assertTrue(n.extras.getBoolean(LiveUpdates.EXTRA_LIVE_EVENT))
        // Следващото опресняване: 30 мин преди началото (след 10 мин), не всяка минута.
        val next = liveWork("ev-1").single { it.state == WorkInfo.State.ENQUEUED }
        assertEquals(10 * 60_000L, next.initialDelayMillis)
    }

    @Test fun swipedAwayLiveUpdateIsNotShownAgain() = runBlocking<Unit> {
        runLive(start = now + 25 * 60_000L, refresh = true)
        assertNull(shown("ev-1"))
        assertTrue(liveWork("ev-1").none { it.state == WorkInfo.State.ENQUEUED })
    }

    @Test fun eventNotificationsOffMeansNoLiveUpdate() = runBlocking<Unit> {
        app.container.settings.setNotifyEvents(false)
        runLive(start = now + 40 * 60_000L)
        assertNull(shown("ev-1"))
    }

    @Test fun startedEventIsNotShown() = runBlocking<Unit> {
        runLive(start = now - 60_000L)
        assertNull(shown("ev-1"))
    }

    @Test fun windowMath() {
        val start = now + 90 * 60_000L
        assertEquals(30 * 60_000L, EventLiveWindow.delayUntilShow(now, start))
        assertEquals(0L, EventLiveWindow.delayUntilShow(now, now + 10 * 60_000L))
        assertNull(EventLiveWindow.delayUntilShow(now, now))
        assertEquals(0, EventLiveWindow.elapsedMinutes(start - 2 * EventLiveWindow.LEAD_MS, start))
        assertEquals(60, EventLiveWindow.elapsedMinutes(start + 1, start))
        assertEquals(start + 5 * 60_000L - now, EventLiveWindow.timeoutAfter(now, start))
        // T−60 → T−30 → T−10 → нищо.
        val s = now + 60 * 60_000L
        assertEquals(s - 30 * 60_000L, EventLiveWindow.nextRefresh(now, s))
        assertEquals(s - 10 * 60_000L, EventLiveWindow.nextRefresh(s - 30 * 60_000L, s))
        assertNull(EventLiveWindow.nextRefresh(s - 10 * 60_000L, s))
        assertEquals(
            Instant.parse("2026-10-17T15:30:00Z").toEpochMilli(),
            EventLiveWindow.startMillis(event(time = "18:30"), clock.zone()),
        )
        assertNull(EventLiveWindow.startMillis(event(time = null), clock.zone()))
    }

    private fun event(date: String = "2026-10-17", time: String?) =
        Event(id = "ev-1", title = "Вечер на поезията", date = date, time = time, place = "Читалището", sourceUrl = "https://example.org/e")

    private fun liveWork(id: String): List<WorkInfo> =
        WorkManager.getInstance(app).getWorkInfosForUniqueWork(EventLiveUpdates.uniqueName(id)).get()

    private fun shown(id: String): Notification? = nm.activeNotifications.firstOrNull { it.id == NotificationIds.event(id) }?.notification

    private suspend fun runLive(start: Long, refresh: Boolean = false) {
        TestListenableWorkerBuilder<EventLiveUpdateWorker>(app)
            .setInputData(
                workDataOf(
                    EventLiveUpdates.KEY_ID to "ev-1", EventLiveUpdates.KEY_TITLE to "Вечер на поезията",
                    EventLiveUpdates.KEY_PLACE to "Читалището", EventLiveUpdates.KEY_START to start, EventLiveUpdates.KEY_REFRESH to refresh,
                ),
            )
            .build()
            .doWork()
    }

    private object NoDao : RemindersDao {
        override fun observeIds(): Flow<List<String>> = flowOf(emptyList())
        override suspend fun upsert(item: EventReminderEntity) = Unit
        override suspend fun delete(id: String) = Unit
    }
}
