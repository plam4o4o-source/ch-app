package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
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
import org.chyavorec.app.ui.screens.catalog.BookViewModel
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * „Заяви книгата“: след успешна заявка бутонът става „Заявена“ (изключен) с обяснение и
 * второ натискане няма; заявката се помни — нов екран на книгата пак я показва като заявена.
 * Снимката „99-book-held“ — до тези на ScreenshotTest.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class HoldScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private var holdRequests = 0

    // Читателският вход е само за https:// адрес — тестовият сървър е с локален сертификат.
    private val cert = HeldCertificate.Builder().addSubjectAlternativeName("localhost").build()
    private val serverTls = HandshakeCertificates.Builder().heldCertificate(cert).build()
    private val clientTls = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
    private val client = OkHttpClient.Builder()
        .sslSocketFactory(clientTls.sslSocketFactory(), clientTls.trustManager)
        .addInterceptor { chain ->
            val url = chain.request().url
            val to = if (url.host == SITE_HOST) url.newBuilder().host("localhost").port(server.port).build() else url
            chain.proceed(chain.request().newBuilder().url(to).build())
        }
        .build()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                "$API/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"holds":true,"renew":false,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                "$API/v1/auth/login" -> MockResponse().setBody("""{"accessToken":"AT-R1","refreshToken":"RT-R1","expiresIn":2592000,"readerId":"R1"}""")
                "$API/v1/me" -> MockResponse().setBody("""{"readerId":"R1","cardNumber":"1024-5678","fullName":"Мария Петрова"}""")
                "$API/v1/me/loans" -> MockResponse().setBody("""{"loans":[]}""")
                "$API/v1/me/holds" -> { holdRequests++; MockResponse().setResponseCode(202).setBody("{}") }
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        // Без лентата „Няма интернет връзка“.
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @After fun stop() = server.shutdown()

    private fun container(): AppContainer {
        val config = AppConfig(
            siteBaseUrl = SITE, catalogUrls = listOf("$SITE/katalog.json"), inflibApiUrl = SITE + API,
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        return AppContainer(ApplicationProvider.getApplicationContext(), config, FixedClock(Instant.parse(NOW)), XorTestCipher(), client)
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun successfulHoldShowsHeldStateAndIsRemembered() {
        val container = container()
        runBlocking {
            // „Запомни ме“: читателският кеш (и запомнената заявка) е само за запомнен вход.
            val r = container.authRepository.login("1024-5678", "1234".toCharArray(), remember = true)
            check(r is Outcome.Success) { "входът на примерния читател: $r" }
        }
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow("$SITE/elektronen-katalog#zapis=$INV"), showIntro = false)
            }
        }
        waitFor("Заяви книгата")
        compose.onAllNodes(hasText("Заяви книгата") and hasClickAction()).onFirst().performScrollTo().performClick()
        waitFor("Заявена на")
        // Снекбарът с потвърждението си отива — на снимката е самото състояние.
        compose.mainClock.advanceTimeBy(8_000)
        compose.waitForIdle()
        shot("99-book-held")
        assertEquals(1, holdRequests)
        assertTrue(compose.onAllNodes(hasText("Заяви книгата")).fetchSemanticsNodes().isEmpty(), "бутонът за заявка остава")
        // „Заявена“ е изключен бутон — не праща втора заявка.
        val held = compose.onAllNodes(hasText("Заявена", substring = true) and SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))
        assertTrue(held.fetchSemanticsNodes().isNotEmpty(), "няма изключен „Заявена“")
        // Нов екран на същата книга (напр. след рестарт) — пак „Заявена“.
        val again = BookViewModel(INV, container)
        compose.waitUntil(15_000) { again.ui.value.heldAt != null }
        assertEquals(Instant.parse(NOW), again.ui.value.heldAt)
    }

    private companion object {
        const val SITE_HOST = "chyavorec.org"
        const val SITE = "https://$SITE_HOST"
        const val API = "/api/invlib/yavorec"
        const val NOW = "2026-09-26T10:00:00Z"
        /** „Ох...“ — налична в примерния каталог. */
        const val INV = 399666L
    }
}
