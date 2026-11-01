package org.chyavorec.app.update

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.chyavorec.app.AppConfig
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.core.FixedClock
import org.chyavorec.data.update.Checksums
import org.chyavorec.data.update.UpdateInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Изтеглянето на нова версия като задача ([UpdateDownloadWorker]): известие с хода и „Отказ“
 * (който изтрива изтеглената част), неуспешна проверка, „Готово — докосни, за да инсталираш“
 * и без разрешение за известия — ходът само в диалога.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class UpdateDownloadWorkerTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val server = MockWebServer()
    /** 1 MB, по 32 KB на 100 ms (~3 s) — достатъчно бавно, за да се види ходът и да се откаже. */
    private val apkBytes = Random(7).nextBytes(1024 * 1024)
    @Volatile private var manifestSha = Checksums.sha256(apkBytes.inputStream())
    @Volatile private var throttle = true
    @Volatile private var paused = false
    private val apkRequests = AtomicInteger()

    @Before fun setUp() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/update.json" -> MockResponse().setBody(
                    """{"versionCode": 5, "versionName": "1.1.0", "apkUrl": "${server.url("/app.apk")}",
                        "sha256": "$manifestSha", "size": ${apkBytes.size}, "minSdk": 26, "notes": "Ново", "paused": $paused}""",
                )
                "/app.apk" -> {
                    apkRequests.incrementAndGet()
                    MockResponse().setBody(Buffer().write(apkBytes))
                        .apply { if (throttle) throttleBody(32 * 1024L, 100, TimeUnit.MILLISECONDS) }
                }
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
        TestBackend.allowNotificationsAndNetwork(app)
        updatesDir().deleteRecursively()
        app.container = container()
    }

    @After fun tearDown() = server.shutdown()

    private fun container(): AppContainer {
        val config = AppConfig(
            siteBaseUrl = server.url("/").toString().trimEnd('/'),
            catalogUrls = emptyList(),
            inflibApiUrl = "",
            environment = "production",
            useMockData = false,
            versionName = "1.0.0",
            versionCode = 1,
            updateManifestUrl = server.url("/update.json").toString(),
            selfUpdate = true,
            buildType = "release",
        )
        return AppContainer(app, config, FixedClock(Instant.parse("2026-11-01T10:00:00Z")), XorTestCipher(), OkHttpClient())
    }

    private val updater get() = app.container.updater
    private fun updatesDir() = File(app.noBackupFilesDir, "updates")
    private fun parts() = updatesDir().listFiles().orEmpty().filter { ".part" in it.name }

    /** Robolectric не чете APK файлове — подписът и пакетът „минават“ (сумата се проверява наистина). */
    private fun acceptApk() {
        updater.apkCheck = { _, _ -> AppUpdater.ApkCheck.OK }
    }

    private fun shown(id: Int): Notification? = runCatching { nm.activeNotifications.firstOrNull { it.id == id }?.notification }.getOrNull()
    private fun Notification.title() = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
    private fun Notification.text() = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()

    /**
     * Задачата — в друга нишка, а главната обработва опашката си: в края задачата пита
     * ProcessLifecycleOwner в главната нишка дали приложението е отворено.
     */
    private fun runWorker(whileRunning: () -> Unit = {}): ListenableWorker.Result {
        val worker = TestListenableWorkerBuilder<UpdateDownloadWorker>(app).build()
        val executor = Executors.newSingleThreadExecutor()
        try {
            val result = executor.submit<ListenableWorker.Result> { runBlocking { worker.doWork() } }
            val deadline = System.currentTimeMillis() + 20_000
            while (!result.isDone) {
                check(System.currentTimeMillis() < deadline) { "задачата не приключи" }
                shadowOf(Looper.getMainLooper()).idle()
                whileRunning()
                Thread.sleep(10)
            }
            return result.get()
        } finally {
            executor.shutdownNow()
        }
    }

    private fun await(what: String, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + 10_000
        while (!condition()) {
            check(System.currentTimeMillis() < deadline) { what }
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(10)
        }
    }

    @Test fun showsProgressAndCancelDeletesThePart() {
        runBlocking { assertNotNull(updater.check(userInitiated = true)) }
        val seen = mutableSetOf<Int>()
        var cancelled = false
        val result = runWorker {
            val n = shown(NotificationIds.UPDATE_PROGRESS) ?: return@runWorker
            val p = n.extras.getInt(Notification.EXTRA_PROGRESS)
            seen += p
            if (cancelled || p !in 1..99) return@runWorker
            // Определен ход: „Изтегляне на версия 1.1.0 — 45%“, лентата и бутонът „Отказ“.
            assertEquals(app.getString(R.string.notif_update_downloading, "1.1.0", p), n.title())
            assertEquals(100, n.extras.getInt(Notification.EXTRA_PROGRESS_MAX))
            assertTrue(!n.extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE))
            assertTrue((n.flags and Notification.FLAG_ONGOING_EVENT) != 0)
            val action = n.actions.single()
            assertEquals(app.getString(R.string.action_cancel), action.title.toString())
            assertTrue(parts().isNotEmpty(), "няма изтеглена част")
            // „Отказ“ — приемникът (както го вика системата от известието).
            UpdateInstallReceiver().onReceive(app, shadowOf(action.actionIntent).savedIntent)
            cancelled = true
        }
        assertIs<ListenableWorker.Result.Success>(result)
        assertTrue(cancelled, "ходът не се показа: $seen")
        await("частта не е изтрита") { parts().isEmpty() && updater.state.value is UpdateState.Available }
        assertTrue(updatesDir().listFiles().orEmpty().isEmpty())
        // Без известие за хода и без „готово“/„неуспех“ след отказа.
        assertNull(shown(NotificationIds.UPDATE_PROGRESS))
        assertNull(shown(NotificationIds.UPDATE))
    }

    @Test fun verificationFailureDeletesTheFileAndNotifies() {
        throttle = false
        manifestSha = "0".repeat(64)
        val result = runWorker()
        assertIs<ListenableWorker.Result.Success>(result)
        val state = updater.state.value
        assertIs<UpdateState.Failed>(state)
        assertEquals(UpdateFailure.CHECKSUM, state.reason)
        assertTrue(updatesDir().listFiles().orEmpty().isEmpty())
        assertNull(shown(NotificationIds.UPDATE_PROGRESS))
        // Приложението не е отворено — известие „не беше изтеглена“, докосване → диалогът (с „Опитай отново“).
        val n = assertNotNull(shown(NotificationIds.UPDATE))
        assertEquals(app.getString(R.string.notif_update_failed, "1.1.0"), n.title())
        assertEquals(app.getString(R.string.notif_update_failed_text), n.text())
    }

    @Test fun completionNotifiesTapToInstall() {
        throttle = false
        acceptApk()
        // Нов процес: задачата сама проверява коя е новата версия (и дали не е спряна — „paused“).
        assertEquals(UpdateState.Idle, updater.state.value)
        val result = runWorker()
        assertIs<ListenableWorker.Result.Success>(result)
        assertIs<UpdateState.Ready>(updater.state.value)
        assertTrue(File(updatesDir(), "update-5.apk").isFile)
        assertTrue(parts().isEmpty())
        assertNull(shown(NotificationIds.UPDATE_PROGRESS))
        val n = assertNotNull(shown(NotificationIds.UPDATE))
        assertEquals(app.getString(R.string.notif_update_ready, "1.1.0"), n.title())
        assertEquals(app.getString(R.string.notif_update_downloaded_text), n.text())
        assertNotNull(n.contentIntent)
    }

    @Test fun withoutNotificationPermissionProgressIsOnlyInTheDialog() {
        shadowOf(app).denyPermissions(Manifest.permission.POST_NOTIFICATIONS)
        acceptApk()
        runBlocking { assertNotNull(updater.check(userInitiated = true)) }
        val progress = mutableListOf<Float?>()
        val result = runWorker {
            (updater.state.value as? UpdateState.Downloading)?.let { progress += it.progress }
            assertTrue(nm.activeNotifications.isEmpty(), "известие без разрешение")
        }
        assertIs<ListenableWorker.Result.Success>(result)
        // Диалогът вижда хода (от AppUpdater.state), а известия няма.
        assertTrue(progress.any { it != null && it > 0f && it < 1f }, "$progress")
        assertIs<UpdateState.Ready>(updater.state.value)
        assertTrue(nm.activeNotifications.isEmpty())
    }

    /** Обновяванията до версията са спрени след показването на диалога — задачата не я тегли. */
    @Test fun pausedReleaseIsNotDownloaded() {
        runBlocking { assertNotNull(updater.check(userInitiated = true)) }
        paused = true
        assertIs<ListenableWorker.Result.Success>(runWorker())
        assertEquals(0, apkRequests.get())
        assertEquals(UpdateState.UpToDate, updater.state.value)
        assertTrue(nm.activeNotifications.isEmpty())
    }

    @Test fun unknownSizeIsIndeterminate() {
        val info = UpdateInfo(
            versionCode = 5, versionName = "1.1.0", apkUrl = "https://example.org/app.apk", sha256 = "0".repeat(64),
            sizeBytes = null, minSdk = 26, notes = "", releaseUrl = null,
        )
        val n = UpdateDownloadWorker.progressNotification(app, info, null)
        assertEquals(app.getString(R.string.notif_update_downloading_unknown, "1.1.0"), n.title())
        assertTrue(n.extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE))
        val half = UpdateDownloadWorker.progressNotification(app, info, 0.45f)
        assertEquals(app.getString(R.string.notif_update_downloading, "1.1.0", 45), half.title())
        assertEquals(45, half.extras.getInt(Notification.EXTRA_PROGRESS))
    }
}
