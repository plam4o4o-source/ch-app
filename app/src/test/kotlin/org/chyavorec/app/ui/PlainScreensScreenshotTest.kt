package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
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
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.screens.my.SampleCardPreview
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
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
import kotlin.test.assertTrue

/**
 * Вълна 23, „довършените“ екрани: търсенето (етикети, плочки, откъси), настройките в групи с
 * карти и тихия знак в края, входът и „Моето“ без вход с примерната карта, съобщенията,
 * диагностиката, краят на „Документи“ и „Контакти“ и поздравът в празник (иконка „Изпрати
 * честитка“). Светла и тъмна тема, шрифт 1.3 и таблет. Само за визуален преглед — освен
 * проверките, че основното съдържание е на екрана.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class PlainScreensScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    // Онлайн входът се включва само за https:// адрес — тестовият сървър е с локален сертификат.
    private val cert = HeldCertificate.Builder().addSubjectAlternativeName("localhost").build()
    private val serverTls = HandshakeCertificates.Builder().heldCertificate(cert).build()
    private val clientTls = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
    private val client = OkHttpClient.Builder()
        .sslSocketFactory(clientTls.sslSocketFactory(), clientTls.trustManager)
        .addInterceptor { chain ->
            val url = chain.request().url
            val to = if (url.host == "chyavorec.org") url.newBuilder().host("localhost").port(server.port).build() else url
            chain.proceed(chain.request().newBuilder().url(to).build())
        }
        .build()

    private val messages = """[{"id":"msg-1","title":"Промяна в работното време","body":"От 1 октомври библиотеката работи до 17:00.",
        "audience":"all","priority":"high","createdAt":"2026-09-25T08:00:00Z"},
        {"id":"msg-2","title":"Общо събрание на членовете","body":"Събранието е в салона на читалището от 18:00.","audience":"all",
        "createdAt":"2026-09-24T08:00:00Z"}]"""

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
                "/data/app-messages.json" -> MockResponse().setBody(messages)
                "/api/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"holds":false,"renew":false,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @After fun stop() = server.shutdown()

    @Composable
    private fun FontScale(scale: Float, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = scale), content = content)
    }

    private fun launch(
        mode: ThemeMode = ThemeMode.LIGHT,
        fontScale: Float = 1f,
        now: String = "2026-09-26T10:00:00Z",
        deepLink: String? = null,
    ) {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = listOf("https://chyavorec.org/katalog.json"),
            inflibApiUrl = "https://chyavorec.org/api",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config, FixedClock(Instant.parse(now)), XorTestCipher(), client,
        )
        compose.setContent {
            FontScale(fontScale) {
                ChitalishteTheme(mode = mode) {
                    ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(deepLink), showIntro = false)
                }
            }
        }
    }

    private fun waitFor(text: String, substring: Boolean = true) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }

    private fun shown(text: String) {
        compose.waitForIdle()
        val nodes = compose.onAllNodes(hasText(text, substring = true))
        val count = nodes.fetchSemanticsNodes().size
        val visible = (0 until count).any { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }
        if (!visible) throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения)")
    }

    private fun tab(label: String) {
        val m = hasText(label) and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(m).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(m).onFirst().performClick()
    }

    private fun clickText(text: String) {
        waitFor(text, substring = false)
        compose.onAllNodes(hasText(text)).onFirst().performScrollTo().performClick()
    }

    /** До края на най-горния превъртащ се списък (тихият знак в края е без семантика). */
    private fun scrollToEnd() {
        compose.waitForIdle()
        repeat(8) { compose.onAllNodes(hasScrollAction()).onLast().performTouchInput { swipeUp() } }
        compose.waitForIdle()
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /* ---- Настройки: групи с етикет и карта, знакът в края ---- */

    private fun openSettings(mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f) {
        launch(mode, fontScale)
        tab("Още")
        clickText("Настройки")
        waitFor("ИЗГЛЕД")
    }

    @Test fun settingsLight() {
        openSettings()
        shown("ИЗГЛЕД")
        shown("Тема")
        shot("w23p-01-settings-light")
        scrollToEnd()
        shown("Диагностика")
        shot("w23p-02-settings-end-light")
    }

    @Test fun settingsDark() {
        openSettings(ThemeMode.DARK)
        shot("w23p-03-settings-dark")
        scrollToEnd()
        shot("w23p-04-settings-end-dark")
    }

    @Test fun settingsFont13() {
        openSettings(fontScale = 1.3f)
        shot("w23p-05-settings-font13")
    }

    @Config(qualifiers = "bg-w1280dp-h800dp-land-hdpi")
    @Test fun settingsTablet() {
        openSettings()
        shot("w23p-06-settings-tablet")
    }

    @Test fun diagnosticsDark() {
        openSettings(ThemeMode.DARK)
        clickText("Диагностика")
        waitFor("Сподели")
        shot("w23p-07-diagnostics-dark")
    }

    /* ---- „Моето“ без вход и входът: примерната карта ---- */

    private fun openMy(mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f) {
        launch(mode, fontScale)
        tab("Моето")
        waitFor("Така ще изглежда картата ти")
        // Входът е включен (capabilities) — бутонът „Вход“ под картата.
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Вход") and hasClickAction()).fetchSemanticsNodes().isNotEmpty() }
        assertTrue(compose.onAllNodes(hasContentDescription("Примерна читателска карта")).fetchSemanticsNodes().isNotEmpty())
    }

    @Test fun myGuestLight() {
        openMy()
        shown("Така ще изглежда картата ти")
        shown("Добави читателска карта")
        shot("w23p-10-my-guest-light")
    }

    @Test fun myGuestDark() {
        openMy(ThemeMode.DARK)
        shot("w23p-11-my-guest-dark")
    }

    @Test fun myGuestFont13() {
        openMy(fontScale = 1.3f)
        shot("w23p-12-my-guest-font13")
    }

    @Config(qualifiers = "bg-w1280dp-h800dp-land-hdpi")
    @Test fun myGuestTablet() {
        openMy()
        shot("w23p-13-my-guest-tablet")
    }

    private fun openLogin(mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f) {
        launch(mode, fontScale, deepLink = "login")
        compose.waitUntil(15_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size >= 2 }
        waitFor("Така ще изглежда картата ти")
    }

    @Test fun loginLight() {
        openLogin()
        shown("Така ще изглежда картата ти")
        shot("w23p-14-login-light")
        scrollToEnd()
        shot("w23p-15-login-end-light")
    }

    @Test fun loginDark() {
        openLogin(ThemeMode.DARK)
        shot("w23p-16-login-dark")
    }

    @Test fun loginFont13() {
        openLogin(fontScale = 1.3f)
        shot("w23p-17-login-font13")
    }

    /**
     * „Картата е издадена“: миг от отблясъка (кадри на ръка) — картата е повдигната, ивицата
     * минава по нея; до 0.5 s всичко е в покой. При намалено движение — без повдигане и ивица.
     */
    @Test fun cardIssuedFrames() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme {
                Row(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(Modifier.width(180.dp)) { SampleCardPreview(sharedKey = null, issued = true) }
                    Column(Modifier.width(180.dp)) {
                        CompositionLocalProvider(LocalReducedMotion provides true) { SampleCardPreview(sharedKey = null, issued = true) }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(220)
        val mid = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "w23p-18-card-issued-mid.png").outputStream().use { mid.compress(Bitmap.CompressFormat.PNG, 100, it) }
        compose.mainClock.advanceTimeBy(1_000)
        val end = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "w23p-19-card-issued-end.png").outputStream().use { end.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /* ---- Съобщения, документи, контакти ---- */

    @Test fun messagesLight() {
        launch(deepLink = "messages")
        waitFor("Промяна в работното време")
        shown("Общо събрание на членовете")
        shot("w23p-20-messages-light")
    }

    @Test fun messagesDark() {
        launch(ThemeMode.DARK, deepLink = "messages")
        waitFor("Промяна в работното време")
        shot("w23p-21-messages-dark")
    }

    @Test fun contactsEnd() {
        launch()
        tab("Още")
        clickText("Контакти")
        waitFor("Работно време")
        scrollToEnd()
        shot("w23p-22-contacts-end")
    }

    @Test fun documentsEnd() {
        launch()
        tab("Още")
        clickText("Документи и архив")
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(2_000)
        scrollToEnd()
        shot("w23p-23-documents-end")
    }

    /* ---- Търсене: етикети, плочки и откъси от цели думи ---- */

    private fun search(mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f, query: String = "ден") {
        launch(mode, fontScale)
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasSetTextAction()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size >= 2 }
        compose.onAllNodes(hasSetTextAction()).onLast().performTextInput(query)
        waitFor("декември")
    }

    @Test fun searchDark() {
        search(ThemeMode.DARK)
        shot("w23p-30-search-dark")
    }

    @Test fun searchPagesFont13() {
        search(fontScale = 1.3f)
        // Обхватът „Страници“ — откъсите от сайта.
        compose.onAllNodes(hasText("Страници") and hasClickAction()).onFirst().performClick()
        compose.waitForIdle()
        shot("w23p-31-search-pages-font13")
    }

    /* ---- „Начало“ в празник: „Изпрати честитка“ — иконка, без имения ден ---- */

    private fun babaMarta(mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f) {
        launch(mode, fontScale, now = "2026-03-01T09:00:00Z")
        waitFor("Баба Марта")
        compose.waitUntil(10_000) { compose.onAllNodes(hasContentDescription("Изпрати честитка")).fetchSemanticsNodes().isNotEmpty() }
        waitFor("45-ти")
        // Иконката е цел за докосване поне 48 dp (minimumInteractiveComponentSize).
        val send = compose.onAllNodes(hasContentDescription("Изпрати честитка") and hasClickAction()).onFirst().fetchSemanticsNode()
        val min = with(compose.density) { 48.dp.toPx() } - 1f
        assertTrue(maxOf(send.size.width, send.layoutInfo.width) >= min && maxOf(send.size.height, send.layoutInfo.height) >= min, "${send.size}")
        assertTrue(send.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty().isNotEmpty())
    }

    @Test fun homeBabaMartaDark() {
        babaMarta(ThemeMode.DARK)
        shot("w23p-40-home-baba-marta-dark")
    }

    @Test fun homeBabaMartaFont13() {
        babaMarta(fontScale = 1.3f)
        shot("w23p-41-home-baba-marta-font13")
    }

    @Config(qualifiers = "bg-w1280dp-h800dp-land-hdpi")
    @Test fun homeBabaMartaTablet() {
        babaMarta()
        shot("w23p-42-home-baba-marta-tablet")
    }
}
