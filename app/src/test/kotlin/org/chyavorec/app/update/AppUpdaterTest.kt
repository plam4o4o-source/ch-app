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

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/releases/latest/download/update.json" -> MockResponse().setBody(
                    """{"versionCode": $manifestCode, "versionName": "1.1.0", "apkUrl": "${server.url("/app.apk")}",
                        "sha256": "$manifestSha", "size": ${apkBytes.size}, "minSdk": 26, "notes": "Ново"}""",
                )
                "/app.apk" -> MockResponse().setBody(Buffer().write(apkBytes))
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

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
}
