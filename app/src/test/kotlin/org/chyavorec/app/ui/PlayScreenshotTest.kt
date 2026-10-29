package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
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
import org.chyavorec.app.data.local.WantToReadEntity
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.screens.my.CardData
import org.chyavorec.app.ui.screens.my.LibraryCard
import org.chyavorec.app.ui.screens.my.LibraryCardBack
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.MembershipStatus
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

/**
 * Снимки за Google Play: 1080×1920 (9:16, телефон), записват се като play-*.png
 * до останалите снимки и CI ги публикува в клон „screenshots“; tools/play_screenshots.py
 * ги рамкира в store/screenshots (номерът е редът в магазина).
 * Всяка снимка проверява, че основното ѝ съдържание е на екрана ([shown]) — снимка без
 * него (празен екран, само skeleton) проваля теста, а не стига до магазина.
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
        // В снимките е истинският адрес на сайта (напр. връзката за абонамент), а заявките
        // към него отиват към тестовия сървър.
        .addInterceptor { chain ->
            val url = chain.request().url
            val to = if (url.host == SITE_HOST) url.newBuilder().host("localhost").port(server.port).build() else url
            chain.proceed(chain.request().newBuilder().url(to).build())
        }
        .build()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @OptIn(DelicateCoilApi::class)
    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                // Заглавието на първата новина — кратко: във въртележката се събира в два реда, без „…“.
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json").replace("\"title\": \"$LONG_TITLE\"", "\"title\": \"$SHORT_TITLE\""))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                "$API/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":true,"holds":false,"renew":true,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                // Примерният читател: вход, профил с членството, заети книги.
                "$API/v1/auth/login" -> MockResponse().setBody("""{"accessToken":"AT-R1","refreshToken":"RT-R1","expiresIn":2592000,"readerId":"R1"}""")
                "$API/v1/me" -> MockResponse().setBody("""{"readerId":"R1","cardNumber":"1024-5678","fullName":"$READER","membership":$MEMBERSHIP}""")
                "$API/v1/me/membership" -> MockResponse().setBody(MEMBERSHIP)
                "$API/v1/me/loans" -> MockResponse().setBody(LOANS)
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        // Кориците — през същия клиент (към тестовия сървър), не от истинския сайт.
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(ApplicationProvider.getApplicationContext<Application>())
                .components { add(OkHttpNetworkFetcherFactory(callFactory = { client })) }
                .build(),
        )
        // В магазина снимките не бива да показват лентата „Няма интернет връзка“.
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @OptIn(DelicateCoilApi::class)
    @After fun stop() {
        SingletonImageLoader.reset()
        server.shutdown()
    }

    private fun launch(
        mode: ThemeMode = ThemeMode.LIGHT,
        settings: AppSettings = AppSettings(onboardingDone = true, introShown = true),
        deepLink: String? = null,
        signedIn: Boolean = false,
        wantToRead: List<WantToReadEntity> = emptyList(),
    ) {
        val container = container()
        if (signedIn) runBlocking {
            val r = container.authRepository.login("1024-5678", "1234".toCharArray(), remember = false)
            check(r is Outcome.Success) { "входът на примерния читател: $r" }
        }
        if (wantToRead.isNotEmpty()) runBlocking {
            val dao = container.database.wantToRead()
            dao.clear()
            wantToRead.forEach { dao.upsert(it) }
        }
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, settings, MutableStateFlow(deepLink), showIntro = false)
            }
        }
    }

    private fun container(): AppContainer {
        val config = AppConfig(
            siteBaseUrl = SITE, catalogUrls = listOf("$SITE/katalog.json"), inflibApiUrl = SITE + API,
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        return AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse(NOW)), XorTestCipher(), client,
        )
    }

    private fun waitFor(text: String, substring: Boolean = true) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }

    /** Поне един възел с [text] е на екрана (видим, не само в дървото). */
    private fun shown(text: String, substring: Boolean = true) {
        compose.waitForIdle()
        val nodes = compose.onAllNodes(hasText(text, substring = substring))
        val count = nodes.fetchSemanticsNodes().size
        val visible = (0 until count).any { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }
        if (!visible) throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения в дървото)")
    }

    /** Текстът [text] е в най-много [maxLines] реда и не е отрязан („…“). */
    private fun fits(text: String, maxLines: Int) {
        val node = compose.onAllNodes(hasText(text), useUnmergedTree = true).onFirst().fetchSemanticsNode()
        val layouts = mutableListOf<TextLayoutResult>()
        node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
        val layout = layouts.firstOrNull() ?: throw AssertionError("„$text“ — без оформление на текста")
        if (layout.lineCount > maxLines || layout.hasVisualOverflow) {
            throw AssertionError("„$text“ — ${layout.lineCount} реда, отрязан: ${layout.hasVisualOverflow}")
        }
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
        // С отворен лист (отделен прозорец) — екранът заедно с него.
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun homeLight() {
        launch()
        waitFor(SHORT_TITLE)
        waitFor("Йоан Богослов")
        shown("Васил Левски – 1922")
        shown(SHORT_TITLE)
        shown("Йоан Богослов")
        // Заглавието във въртележката е цяло (без „…“ по средата на израза).
        fits(SHORT_TITLE, maxLines = 2)
        shot("play-01-home-light")
    }

    @Test fun catalog() {
        launch()
        tab("Каталог")
        waitFor("резултата")
        shown("60 резултата")
        shown("Джиан, Филип")
        shot("play-02-catalog")
    }

    /** Книга, която не е на рафта: „Искам да прочета“ и „Кажи ми, когато е на рафта“. */
    @Test fun book() {
        launch(deepLink = "$SITE/elektronen-katalog#zapis=136")
        // „Кажи ми“ е превключвател с име (не текст) — до него е „Попитай библиотеката“.
        waitFor("Попитай библиотеката")
        shown("Белият детството")
        shown("Не е на рафта")
        shown("Искам да прочета")
        compose.onAllNodes(hasContentDescription("Кажи ми, когато е на рафта")).onFirst().assertIsDisplayed()
        // До корицата на 360 dp етикетът е на един ред (не „Искам да / прочета“).
        fits("Искам да прочета", maxLines = 1)
        shot("play-03-book")
    }

    @Test fun card() {
        val data = CardData(holder = READER, number = "1024-5678", barcode = "10245678", status = MembershipStatus.ACTIVE, verified = true)
        val container = container()
        compose.setContent {
            // Шевицата по ъглите на голямата карта чете настройките от контейнера.
            CompositionLocalProvider(LocalAppContainer provides container) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    Column(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).statusBarsPadding().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        SectionLabel("Читателска карта")
                        LibraryCard(data, large = true)
                        LibraryCardBack(data, large = true)
                    }
                }
            }
        }
        shown(READER)
        shown("1024-5678")
        shown("Активно")
        shot("play-04-card")
    }

    /** „Новини и събития“ на сегмента „Календар“ с отворения абонамент за календара. */
    @Test fun events() {
        launch(deepLink = "events")
        waitFor("Световен ден")
        val subscribe = hasContentDescription("Абонирай се за календара")
        compose.waitUntil(10_000) { compose.onAllNodes(subscribe).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(subscribe).onFirst().performClick()
        waitFor("Копирай връзката")
        shown("Календар")
        shown("Световен ден")
        shown("Абонирай се за календара")
        shot("play-05-events")
    }

    /** „Моите книги“ на влязъл читател: сроковете, просрочената книга, „Удължи“. */
    @Test fun loans() {
        launch(deepLink = "my/loans", signedIn = true)
        waitFor("Бай Ганьо")
        shown("Моите книги")
        shown("Тютюн")
        shown("просрочена")
        shown("Под игото")
        shown("Удължи")
        shot("play-06-loans")
    }

    @Test fun more() {
        launch()
        tab("Още")
        waitFor("Настройки")
        shown("Съобщения")
        shown("Дейности")
        shown("Галерия")
        shot("play-07-more")
    }

    /** „Искам да прочета“ (тъмна тема): наличността от каталога и „Кажи ми“ за книгата, която не е на рафта. */
    @Test fun wantToReadDark() {
        val t = Instant.parse(NOW).toEpochMilli()
        launch(
            ThemeMode.DARK,
            wantToRead = listOf(
                WantToReadEntity(136, "Белият детството", "Симеон Колев", t - 1_000, notifyWhenAvailable = true, lastKnownAvailable = false),
                WantToReadEntity(399581, "Ако останеш", "Коул, Кортни", t - 2_000, notifyWhenAvailable = false, lastKnownAvailable = true),
                WantToReadEntity(399600, "Анатомът", "Андахази, Федерико", t - 3_000, notifyWhenAvailable = false, lastKnownAvailable = true),
                WantToReadEntity(399570, "66 градуса Северна ширина", "Ридпат, Майкъл", t - 4_000, notifyWhenAvailable = false, lastKnownAvailable = true),
                WantToReadEntity(399687, "Алтъна", "Никова, Ивелина", t - 5_000, notifyWhenAvailable = false, lastKnownAvailable = true),
            ),
        )
        tab("Моето")
        waitFor("5 книги в списъка")
        compose.onAllNodes(hasText("Искам да прочета")).onFirst().performClick()
        waitFor("Анатомът")
        shown("Искам да прочета")
        shown("Белият детството")
        shown("Не е на рафта")
        shown("налична")
        shown("Анатомът")
        shot("play-08-want-to-read-dark")
    }

    private companion object {
        const val SITE_HOST = "chyavorec.org"
        const val SITE = "https://$SITE_HOST"
        const val API = "/api/invlib/yavorec"
        const val NOW = "2026-09-26T10:00:00Z"
        const val READER = "Мария Петрова"
        const val LONG_TITLE = "45-ти общински фолклорен събор на народното творчество „От Тимок до Вита“, село Черни Вит."
        const val SHORT_TITLE = "45-ти събор „От Тимок до Вита“"
        const val MEMBERSHIP = """{"memberNumber":"1024-5678","holderName":"$READER","validUntil":"2026-12-31","status":"active","barcode":"10245678"}"""

        // Към 26.09.2026: срок след 4 дни, просрочена с 2 дни (без удължаване), срок след три седмици.
        const val LOANS = """{"loans":[
            {"loanId":"L1","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-05","dateDue":"2026-09-30","renewals":0,"canRenew":true},
            {"loanId":"L2","inv":101,"title":"Тютюн","author":"Димитър Димов","dateOut":"2026-08-20","dateDue":"2026-09-24","renewals":2,"canRenew":false},
            {"loanId":"L3","inv":102,"title":"Бай Ганьо","author":"Алеко Константинов","dateOut":"2026-09-19","dateDue":"2026-10-17","renewals":0,"canRenew":true}
        ]}"""
    }
}
