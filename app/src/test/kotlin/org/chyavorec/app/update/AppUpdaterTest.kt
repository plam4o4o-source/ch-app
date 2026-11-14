package org.chyavorec.app.update

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.data.update.Checksums
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant

/** Самообновяване: проверка, отхвърляне на повреден/чужд файл, изключено извън prodRelease. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class AppUpdaterTest {
    private val server = MockWebServer()
    private val apkBytes = "definitely not an apk".toByteArray()
    private var manifestCode = 5
    private var manifestSha = Checksums.sha256(apkBytes.inputStream())
    private val dmBytes = "primary.prof".toByteArray()
    private var dmSha = Checksums.sha256(dmBytes.inputStream())
    /** Списъкът „dm“ в update.json (празно — без него, както досегашните). */
    private var withDm = false

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/releases/latest/download/update.json" -> MockResponse().setBody(
                    """{"versionCode": $manifestCode, "versionName": "1.1.0", "apkUrl": "${server.url("/app.apk")}",
                        "sha256": "$manifestSha", "size": ${apkBytes.size}, "minSdk": 26, "notes": "Ново"${dmField()}}""",
                )
                "/app.apk" -> MockResponse().setBody(Buffer().write(apkBytes))
                "/app.s.dm" -> MockResponse().setBody(Buffer().write(dmBytes))
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() {
        server.shutdown()
        updatesDir().deleteRecursively()
    }

    private fun dmField(): String = if (!withDm) "" else
        """, "dm": [{"minSdk": 28, "maxSdk": 30, "url": "${server.url("/app.p.dm")}", "sha256": "${"0".repeat(64)}"},
                    {"minSdk": 31, "url": "${server.url("/app.s.dm")}", "sha256": "$dmSha", "size": ${dmBytes.size}}]"""

    private fun dmRequests(): Int {
        var n = 0
        while (true) {
            val r = server.takeRequest(10, java.util.concurrent.TimeUnit.MILLISECONDS) ?: return n
            if (r.path.orEmpty().endsWith(".dm")) n++
        }
    }

    private fun updater(selfUpdate: Boolean = true): AppUpdater = container(selfUpdate).updater

    private fun container(selfUpdate: Boolean = true): AppContainer {
        val config = AppConfig(
            siteBaseUrl = server.url("/").toString().trimEnd('/'),
            catalogUrls = emptyList(),
            inflibApiUrl = "",
            environment = "production",
            useMockData = false,
            versionName = "1.0.0",
            versionCode = 1,
            updateManifestUrl = server.url("/releases/latest/download/update.json").toString(),
            selfUpdate = selfUpdate,
            buildType = "release",
        )
        return AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
    }

    private fun updatesDir() = File(ApplicationProvider.getApplicationContext<Application>().noBackupFilesDir, "updates")

    @Test fun whatsNewShownOnceAfterUpdate() = runTest {
        val c = container()
        val u = c.updater
        // Записано при инсталирането на версията, която вече е инсталирана (versionCode 1).
        c.settings.setPendingWhatsNew(WhatsNew(1, "1.0.0", "• Ново", "• New").encode())
        u.loadWhatsNew()
        assertEquals("• New", u.whatsNew.value?.notesFor(english = true))
        assertEquals("• Ново", u.whatsNew.value?.notesFor(english = false))
        assertNull(c.settings.pendingWhatsNew())
        u.dismissWhatsNew()
        u.loadWhatsNew()
        assertNull(u.whatsNew.value)
        // По-нова версия, която още не е инсталирана — записът остава за после.
        c.settings.setPendingWhatsNew(WhatsNew(5, "1.1.0", "• Ново", "").encode())
        u.loadWhatsNew()
        assertNull(u.whatsNew.value)
        assertTrue(c.settings.pendingWhatsNew() != null)
        // Без английски текст — българският.
        assertEquals("• Ново", WhatsNew.decode(WhatsNew(5, "1.1.0", "• Ново", "").encode())?.notesFor(english = true))
        c.settings.setPendingWhatsNew(null)
    }

    @Test fun genericWhatsNewNotShown() = runTest {
        val c = container()
        val u = c.updater
        // Без раздел в store/whats-new.md release.yml записва само общия текст — листът не се показва.
        c.settings.setPendingWhatsNew(WhatsNew(1, "1.0.0", "Подобрения и поправки.", "Improvements and fixes.").encode())
        u.loadWhatsNew()
        assertNull(u.whatsNew.value)
        assertNull(c.settings.pendingWhatsNew())
        assertTrue(WhatsNew(1, "1.0.0", "• Подобрения и поправки.", "").isGeneric)
        // Истински бележки (дори само на български) — записът се пази, а листът решава по езика.
        val bgOnly = WhatsNew(1, "1.0.0", "• Ново", "Improvements and fixes.")
        assertFalse(bgOnly.isGeneric)
        assertFalse(bgOnly.isGenericFor(english = false))
        // Раздел само с `### bg`: на английски е само общият текст — листът не се показва.
        assertTrue(bgOnly.isGenericFor(english = true))
        assertTrue(WhatsNew(1, "1.0.0", "Подобрения и поправки.", "• New").isGenericFor(english = false))
        assertFalse(WhatsNew(1, "1.0.0", "Подобрения и поправки.", "• New").isGenericFor(english = true))
        // Без английски текст английският вижда българските бележки.
        assertFalse(WhatsNew(1, "1.0.0", "• Ново", "").isGenericFor(english = true))
        assertTrue(WhatsNew(1, "1.0.0", "• Подобрения и поправки.", "").isGenericFor(english = true))
        c.settings.setPendingWhatsNew(WhatsNew(1, "1.0.0", "• Ново", "• New").encode())
        u.loadWhatsNew()
        assertEquals("• Ново", u.whatsNew.value?.notesFor(english = false))
        u.dismissWhatsNew()
    }

    @Test fun disabledOutsideSelfUpdateBuild() = runTest {
        val u = updater(selfUpdate = false)
        assertFalse(u.enabled)
        assertNull(u.check(userInitiated = true))
        assertEquals(UpdateState.Idle, u.state.value)
        assertEquals(0, server.requestCount)
    }

    @Test fun findsNewerVersion() = runTest {
        val u = updater()
        val info = u.check(userInitiated = true)
        assertEquals(5, info?.versionCode)
        assertTrue(u.state.value is UpdateState.Available)
    }

    @Test fun upToDate() = runTest {
        manifestCode = 1
        val u = updater()
        assertNull(u.check(userInitiated = true))
        assertEquals(UpdateState.UpToDate, u.state.value)
    }

    @Test fun rejectsWrongChecksum() = runTest {
        manifestSha = "0".repeat(64)
        val u = updater()
        val info = requireNotNull(u.check(userInitiated = true))
        assertNull(u.download(info))
        assertEquals(UpdateState.Failed(UpdateFailure.CHECKSUM, info), u.state.value)
        assertTrue(updatesDir().listFiles().orEmpty().isEmpty())
    }

    @Test fun rejectsFileThatIsNotOurApk() = runTest {
        val u = updater()
        val info = requireNotNull(u.check(userInitiated = true))
        // Контролната сума съвпада, но файлът не е APK на това приложение → не се инсталира.
        assertNull(u.download(info))
        assertEquals(UpdateState.Failed(UpdateFailure.CHECKSUM, info), u.state.value)
        assertTrue(updatesDir().listFiles().orEmpty().isEmpty())
    }

    /**
     * „По-късно“ и рестарт: изтеглената версия е във файла, а състоянието — празно. Без файл
     * нищо не се проверява; с файл версията пак е „готова“ (за реда в настройките).
     */
    @Test fun restoresTheDownloadedVersionAfterRestart() = runTest {
        val u = updater()
        assertFalse(u.restoreDownloaded())
        assertEquals(0, server.requestCount)
        assertEquals(UpdateState.Idle, u.state.value)

        updatesDir().mkdirs()
        File(updatesDir(), "update-5.apk").writeBytes(apkBytes)
        // Отложена версия — диалогът не се показва, но състоянието я знае.
        assertTrue(u.restoreDownloaded())
        val ready = u.state.value
        assertTrue("$ready", ready is UpdateState.Ready)
        assertEquals("1.1.0", (ready as UpdateState.Ready).info.versionName)
        assertFalse(u.prompt.value)
        // Вече известно състояние — без нова проверка.
        val requests = server.requestCount
        assertFalse(u.restoreDownloaded())
        assertEquals(requests, server.requestCount)
        updatesDir().deleteRecursively()
    }

    /** .dm за Android 12+: изтегля се след APK-а, сумата се проверява, и е готов за сесията. */
    @Test fun downloadsMatchingDexMetadataAfterTheApk() = runTest {
        withDm = true
        val u = updater()
        u.sdkInt = 34
        u.apkCheck = { _, _ -> AppUpdater.ApkCheck.OK }
        val info = requireNotNull(u.check(userInitiated = true))
        assertEquals(2, info.dexMetadata.size)
        assertTrue(u.download(info) != null)
        assertEquals(UpdateState.Ready(info), u.state.value)
        val dm = File(updatesDir(), "update-5.dm")
        assertTrue(dm.readBytes().contentEquals(dmBytes))
        assertEquals(dm, u.dexMetadataForInstall(info))
        // Вече изтеглен — не се тегли пак.
        dmRequests()
        assertEquals(dm, u.fetchDexMetadata(info))
        assertEquals(0, dmRequests())
    }

    /** Грешна сума на .dm — без него; APK-ът и обновяването са както винаги. */
    @Test fun dexMetadataWithWrongChecksumIsSkipped() = runTest {
        withDm = true
        dmSha = "f".repeat(64)
        val u = updater()
        u.sdkInt = 34
        u.apkCheck = { _, _ -> AppUpdater.ApkCheck.OK }
        val info = requireNotNull(u.check(userInitiated = true))
        assertTrue(u.download(info) != null)
        assertEquals(UpdateState.Ready(info), u.state.value)
        assertFalse(File(updatesDir(), "update-5.dm").exists())
        assertFalse(File(updatesDir(), "update-5.dm.part").exists())
        assertNull(u.dexMetadataForInstall(info))
    }

    /** Под Android 9, без „dm“ в update.json, недостъпен файл или вече опитан — нищо не се тегли/записва. */
    @Test fun dexMetadataSkipPaths() = runTest {
        withDm = true
        val u = updater()
        u.apkCheck = { _, _ -> AppUpdater.ApkCheck.OK }
        val info = requireNotNull(u.check(userInitiated = true))
        dmRequests()
        u.sdkInt = 27
        assertNull(u.fetchDexMetadata(info))
        assertNull(u.dexMetadataForInstall(info))
        assertEquals(0, dmRequests())
        // Android 10: записът за 28–30 сочи несъществуващ файл (404) — без .dm, без грешка.
        u.sdkInt = 29
        assertNull(u.fetchDexMetadata(info))
        assertEquals(1, dmRequests())
        assertTrue(updatesDir().listFiles().orEmpty().none { it.name.contains(".dm") })
        // Без „dm“ в update.json — нито заявка.
        assertNull(u.fetchDexMetadata(info.copy(dexMetadata = emptyList())))
        assertEquals(0, dmRequests())
        // Вече опитано инсталиране с .dm на тази версия — следващите са без него.
        u.sdkInt = 34
        assertTrue(u.fetchDexMetadata(info) != null)
        File(updatesDir(), "update-5.dm.tried").createNewFile()
        assertNull(u.dexMetadataForInstall(info))
        dmRequests()
        assertNull(u.fetchDexMetadata(info))
        assertEquals(0, dmRequests())
    }

    /** Подменен файл след изтеглянето (друга сума) не стига до сесията и се изтрива. */
    @Test fun tamperedDexMetadataIsNotInstalled() = runTest {
        withDm = true
        val u = updater()
        u.sdkInt = 31
        val info = requireNotNull(u.check(userInitiated = true))
        val dm = requireNotNull(u.fetchDexMetadata(info))
        dm.writeBytes("something else".toByteArray())
        assertNull(u.dexMetadataForInstall(info))
        assertFalse(dm.exists())
    }
}
