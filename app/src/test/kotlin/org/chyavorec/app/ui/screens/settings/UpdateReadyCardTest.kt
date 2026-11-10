package org.chyavorec.app.ui.screens.settings

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.UpdateReadyCard
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.update.UpdateState
import org.chyavorec.core.FixedClock
import org.chyavorec.data.update.Checksums
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.Instant
import kotlin.test.assertTrue

/**
 * „Настройки › Обновления“ след „По-късно“ и рестарт: изтеглената версия е на видно място —
 * „Версия X е готова“ с „Инсталирай“, което започва инсталирането и отваря диалога.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class UpdateReadyCardTest {
    @get:Rule val compose = createComposeRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val server = MockWebServer()
    private val apkBytes = "definitely not an apk".toByteArray()
    private val updates get() = File(app.noBackupFilesDir, "updates")

    @Before fun start() {
        val sha = Checksums.sha256(apkBytes.inputStream())
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/update.json" -> MockResponse().setBody(
                    """{"versionCode": 5, "versionName": "1.2.33", "apkUrl": "${server.url("/app.apk")}",
                        "sha256": "$sha", "size": ${apkBytes.size}, "minSdk": 26, "notes": "• Ново"}""",
                )
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() {
        server.shutdown()
        updates.deleteRecursively()
    }

    /** Приложение с изтеглена и проверена версия 1.2.33, отложена с „По-късно“ (състоянието — Ready). */
    private fun readyContainer(): AppContainer {
        val config = AppConfig(
            siteBaseUrl = server.url("/").toString().trimEnd('/'), catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "production", useMockData = false, versionName = "1.2.32", versionCode = 1,
            updateManifestUrl = server.url("/update.json").toString(), selfUpdate = true, buildType = "release",
        )
        val c = AppContainer(app, config, FixedClock(Instant.parse("2026-11-09T10:00:00Z")), XorTestCipher(), OkHttpClient())
        updates.mkdirs()
        File(updates, "update-5.apk").writeBytes(apkBytes)
        runBlocking { c.updater.restoreDownloaded() }
        assertTrue(c.updater.state.value is UpdateState.Ready, "${c.updater.state.value}")
        return c
    }

    @Test fun settingsOfferTheDownloadedVersion() {
        val c = readyContainer()
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                CompositionLocalProvider(LocalAppContainer provides c) { SettingsScreen(onBack = {}, navigate = {}) }
            }
        }
        compose.waitUntil(10_000) { compose.onAllNodes(hasText("Версия 1.2.33 е готова")).fetchSemanticsNodes().isNotEmpty() }
        compose.onNode(hasText("Версия 1.2.33 е готова")).performScrollTo()
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File("build/reports/screenshots").apply { mkdirs() }.resolve("w20m-02-settings-update-ready.png")
            .outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

        compose.onNode(hasText("Инсталирай") and hasClickAction()).performClick()
        compose.waitUntil(5_000) { c.updater.prompt.value }
        // Инсталирането е започнало (тук — без разрешение за инсталиране: диалогът води до него).
        compose.waitUntil(5_000) { c.updater.state.value !is UpdateState.Ready }
    }

    /** Картата в двете теми — и „Налична е версия“ (още не е изтеглена). */
    @Test fun cardInBothThemes() {
        val c = readyContainer()
        val available = UpdateState.Available((c.updater.state.value as UpdateState.Ready).info)
        compose.setContent {
            Column {
                listOf(ThemeMode.LIGHT, ThemeMode.DARK).forEach { mode ->
                    ChitalishteTheme(mode = mode) {
                        Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(vertical = 8.dp)) {
                            UpdateReadyCard(c.updater.state.value, c.updater, Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                            UpdateReadyCard(available, c.updater, Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                        }
                    }
                }
            }
        }
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File("build/reports/screenshots").apply { mkdirs() }.resolve("w20m-03-update-card.png")
            .outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
