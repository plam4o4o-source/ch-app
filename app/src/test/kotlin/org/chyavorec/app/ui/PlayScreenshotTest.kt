package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.io.File
import java.time.Instant

/**
 * Снимки за Google Play: 1080×1920 (9:16, телефон), записват се като play-*.png
 * до останалите снимки и CI ги публикува в клон „screenshots“.
 * Очакванията са „меки“ — ако примерните данни не се покажат на малкия екран,
 * снимката пак се прави; тестът проверява само, че екранът се рисува.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w360dp-h640dp-xxhdpi")
class PlayScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

    // Приложението включва читателския вход само за https:// адрес, затова тестовият
    // сървър е с локален TLS сертификат, на който доверява само този OkHttpClient.
    private val cert = HeldCertificate.Builder().addSubjectAlternativeName("localhost").build()
    private val serverTls = HandshakeCertificates.Builder().heldCertificate(cert).build()
    private val clientTls = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
    private val client = OkHttpClient.Builder()
        .sslSocketFactory(clientTls.sslSocketFactory(), clientTls.trustManager)
        .build()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Въздвижение на Светия Кръст Господен. **Кръстовден**"}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                "/api/invlib/yavorec/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":true,"holds":false,"renew":false,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory())
        server.start()
        // В магазина снимките не бива да показват лентата „Няма интернет връзка“.
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @After fun stop() = server.shutdown()

    private fun launch(mode: ThemeMode = ThemeMode.LIGHT, settings: AppSettings = AppSettings(onboardingDone = true, introShown = true)) {
        val base = server.url("/").newBuilder().host("localhost").build().toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "$base/api/invlib/yavorec",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), client,
        )
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, settings, MutableStateFlow(null), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String, substring: Boolean = true) = runCatching {
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }
    }

    private fun tab(label: String) {
        val m = hasText(label) and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(m).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(m).onFirst().performClick()
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun homeLight() {
        launch()
        waitFor("45-ти")
        shot("play-01-home-light")
    }

    @Test fun homeDark() {
        launch(ThemeMode.DARK)
        waitFor("45-ти")
        shot("play-02-home-dark")
    }

    @Test fun catalog() {
        launch()
        tab("Каталог")
        waitFor("резултата")
        shot("play-03-catalog")
    }

    @Test fun my() {
        launch()
        tab("Моето")
        waitFor("Добре дошъл!")
        shot("play-04-my")
    }

    @Test fun more() {
        launch()
        tab("Още")
        waitFor("Настройки")
        shot("play-05-more")
    }
}
