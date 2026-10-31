package org.chyavorec.app.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.provider.CalendarContract
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.ConnectivityChecker
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okio.Buffer
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.MessagePollPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Снимка и бутони в известията от фоновите задачи: нова новина — снимката от сайта (смалена) и
 * „Сподели“; без снимка (грешка, „Корици само по Wi-Fi“ по мобилни данни) — само текст;
 * напомняне за събитие — снимката и „Добави в календара“ от кеширания календар.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = WorkerTestApp::class)
class RichNotificationsWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    // 17.10.2026, 13:00 в София.
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    /** Уникален адрес на снимката — дисковият кеш на Coil не се споделя между тестовете. */
    private val cover = "/assets/uploads/news/" + UUID.randomUUID() + ".png"

    @OptIn(DelicateCoilApi::class)
    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.json("/data/news.json", """[{"id":"n-${UUID.randomUUID()}","title":"Изложба на шевици","date":"2026-10-16","excerpt":"Покана","content":"<p>Текст</p>","cover":"$cover"}]""")
        backend.json("/rss.xml", """<rss><channel><item><title>Изложба на шевици</title><link>/news/izlozhba</link></item></channel></rss>""")
        app.container = backend.container(app, clock)
        // Снимките — през тестовия сървър (с неговия сертификат); проверката на връзката на Coil не вижда мрежата на Robolectric.
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(app).components {
                add(OkHttpNetworkFetcherFactory(callFactory = { backend.client }, connectivityChecker = { ConnectivityChecker.ONLINE }))
            }.build(),
        )
        val s = app.container.settings
        s.setNotifyMessages(false)
        s.setNotifyLoans(false)
        s.setNotifyNews(true)
        s.setCoversWifiOnly(false)
        s.setLastNewsId("an-older-article-" + UUID.randomUUID())
        listOf("news", "loans", "events").forEach { s.setLastSyncStep(it, 0) }
        s.markAppOpened(clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, 0)
    }

    @OptIn(DelicateCoilApi::class)
    @After fun tearDown() = runBlocking<Unit> {
        app.container.settings.setCoversWifiOnly(false)
        SingletonImageLoader.reset()
        backend.shutdown()
    }

    /** PNG [width]×[height] — 2048 широка се смалява точно наполовина (до 1024). */
    private fun png(width: Int = 2048, height: Int = 1024): Buffer {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply { eraseColor(0xFF8B1E2D.toInt()) }
        val bytes = ByteArrayOutputStream().also { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }.toByteArray()
        return Buffer().write(bytes)
    }

    private fun serveCover(path: String = cover) {
        backend.routes[path] = { MockResponse().addHeader("Content-Type", "image/png").setBody(png()) }
    }

    private fun runSync() = runBlocking { TestListenableWorkerBuilder<PeriodicSyncWorker>(app).build().doWork() }

    private fun news() = nm.activeNotifications.single { it.id == NotificationIds.NEWS }.notification
    private fun template(n: Notification) = n.extras.getString(Notification.EXTRA_TEMPLATE)

    @Suppress("DEPRECATION")
    private fun picture(n: Notification): Bitmap? = n.extras.getParcelable(Notification.EXTRA_PICTURE)

    @Test fun newArticleWithCoverShowsDownscaledPictureAndShare() {
        serveCover()
        runSync()
        val n = news()
        assertEquals(Notification.BigPictureStyle::class.java.name, template(n))
        assertEquals(1, backend.count(cover))
        assertTrue(n.extras.containsKey(Notification.EXTRA_PICTURE) || n.extras.containsKey(Notification.EXTRA_PICTURE_ICON))
        picture(n)?.let { assertTrue(it.width <= RichNotifications.MAX_IMAGE_PX, "ширина ${it.width}") }
        assertNotNull(n.getLargeIcon())
        assertEquals(listOf(app.getString(R.string.action_share)), n.actions.map { it.title.toString() })
        assertEquals("Изложба на шевици", n.extras.getCharSequence(Notification.EXTRA_TEXT).toString())
    }

    @Test fun missingCoverFallsBackToText() {
        backend.routes[cover] = { MockResponse().setResponseCode(404) }
        runSync()
        val n = news()
        assertEquals(Notification.BigTextStyle::class.java.name, template(n))
        assertFalse(n.extras.containsKey(Notification.EXTRA_PICTURE))
        assertEquals(1, backend.count(cover))
        // Бутонът „Сподели“ остава.
        assertEquals(1, n.actions.size)
    }

    @Test fun coversOnWifiOnlyOnMeteredNetworkSkipsTheDownload() = runBlocking<Unit> {
        // Тестовата мрежа е без NOT_METERED — „с отчитане на трафика“.
        app.container.settings.setCoversWifiOnly(true)
        serveCover()
        runSync()
        assertEquals(Notification.BigTextStyle::class.java.name, template(news()))
        assertEquals(0, backend.count(cover))
    }

    @Test fun eventReminderUsesCachedEventForPictureAndCalendar() = runBlocking<Unit> {
        val eventCover = "/assets/uploads/events/" + UUID.randomUUID() + ".png"
        serveCover(eventCover)
        backend.json(
            "/javora/index.json",
            """{"generated":"2026-10-01","docs":[{"id":"event-42","type":"event","title":"Вечер на поезията","url":"/events",""" +
                """"day":"20","monthNum":"10","excerpt":"Стихове","cover":"$eventCover"}]}""",
        )
        assertIs<Outcome.Success<*>>(app.container.eventsRepository.events(force = true))
        shadowOf(app.packageManager).addResolveInfoForIntent(
            Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI),
            ResolveInfo().apply { isDefault = true; activityInfo = ActivityInfo().apply { packageName = "com.example.calendar"; name = "Insert" } },
        )
        val worker = TestListenableWorkerBuilder<EventReminderWorker>(app)
            .setInputData(workDataOf(ReminderScheduler.KEY_TITLE to "Вечер на поезията", ReminderScheduler.KEY_ID to "event-42"))
            .build()
        worker.doWork()
        val n = nm.activeNotifications.single { it.id == NotificationIds.event("event-42") }.notification
        assertEquals(Channel.EVENTS.id, n.channelId)
        assertEquals(Notification.BigPictureStyle::class.java.name, template(n))
        // Календарът на сайта е без място — само „Добави в календара“.
        assertEquals(listOf(app.getString(R.string.event_add_calendar)), n.actions.map { it.title.toString() })
        val calendar = shadowOf(n.actions.single().actionIntent)
        assertTrue(calendar.isActivityIntent && calendar.isImmutable)
    }

    @Test fun eventReminderWithoutCachedEventIsPlainText() = runBlocking<Unit> {
        val worker = TestListenableWorkerBuilder<EventReminderWorker>(app)
            .setInputData(workDataOf(ReminderScheduler.KEY_TITLE to "Сбирка", ReminderScheduler.KEY_ID to "unknown-" + UUID.randomUUID()))
            .build()
        worker.doWork()
        val n = nm.activeNotifications.single { it.notification.channelId == Channel.EVENTS.id }.notification
        assertEquals(Notification.BigTextStyle::class.java.name, template(n))
        assertTrue(n.actions.isNullOrEmpty())
    }
}
