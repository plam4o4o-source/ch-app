package org.chyavorec.app.ui.screens.news

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.navigation.DeepLinks
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant

/**
 * Четенето на статия: плаващата лента с действията (скрива се при четене надолу и се връща
 * нагоре), „конецът“ на прочетеното и паралаксът на снимката — снимки след превъртане.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class ArticleReadingTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    /** Дълга статия — има какво да се чете и превърта. */
    private val paragraphs = (1..14).joinToString("") { i ->
        "<p>Абзац $i. Читалището в Яворец събира хората от селото около книгите, песента и хорото. " +
            "Тази вечер библиотеката отвори врати за всички, които искат да чуят старите истории за селото и да научат нещо ново.</p>"
    }
    private val news = """[{"id":"n-long","title":"Вечер на шевицата в читалището","date":"2026-09-20","category":"Събитие",
        "excerpt":"Вечер на народното изкуство.","content":"$paragraphs","cover":"/cover.png"}]"""

    /** Заглавната снимка: топъл градиент (паралаксът се вижда по него). */
    private val cover: Buffer by lazy {
        val bmp = Bitmap.createBitmap(640, 400, Bitmap.Config.ARGB_8888)
        val paint = Paint().apply { shader = LinearGradient(0f, 0f, 640f, 400f, 0xFF6B1F2A.toInt(), 0xFFC9A84C.toInt(), Shader.TileMode.CLAMP) }
        Canvas(bmp).drawRect(0f, 0f, 640f, 400f, paint)
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        Buffer().write(out.toByteArray())
    }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(news)
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                "/cover.png" -> MockResponse().setHeader("Content-Type", "image/png").setBody(cover.clone())
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(mode: ThemeMode = ThemeMode.LIGHT) {
        // С връзка — без лентата „Няма интернет връзка“ и със заглавната снимка.
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(DeepLinks.article("n-long")), showIntro = false)
            }
        }
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Абзац 14", substring = true)).fetchSemanticsNodes().isNotEmpty() }
        compose.waitForIdle()
    }

    private val openSite = hasContentDescription("Отвори в сайта")

    private fun toolbarShown() = compose.onAllNodes(openSite).fetchSemanticsNodes().isNotEmpty()

    /** Статията (вертикалното превъртане), не лентата със снимки. */
    private val article = hasScrollAction() and SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange)

    private fun shot(name: String) {
        compose.mainClock.advanceTimeBy(800)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Превърта статията с [px] надолу и малко назад — лентата с действията се връща (както при четене). */
    private fun scrollArticleBy(px: Float) {
        compose.onAllNodes(article).onFirst().performSemanticsAction(SemanticsActions.ScrollBy) { it(0f, px) }
        compose.waitForIdle()
        compose.onAllNodes(article).onFirst().performSemanticsAction(SemanticsActions.ScrollBy) { it(0f, -60f) }
        compose.waitUntil(5_000) { toolbarShown() }
    }

    @Test fun toolbarHidesWhileReadingDownAndReturnsOnScrollUp() {
        launch()
        // Действията са в плаващата лента (горе — само „Назад“).
        compose.waitUntil(5_000) { toolbarShown() }
        compose.onAllNodes(hasContentDescription("Сподели")).onFirst().assertExists()
        compose.onRoot().performTouchInput { swipe(Offset(centerX, height * 0.7f), Offset(centerX, height * 0.4f), durationMillis = 300) }
        compose.waitUntil(5_000) { !toolbarShown() }
        compose.onRoot().performTouchInput { swipe(Offset(centerX, height * 0.4f), Offset(centerX, height * 0.55f), durationMillis = 300) }
        compose.waitUntil(5_000) { toolbarShown() }
    }

    @Test fun readingLight() {
        launch()
        scrollArticleBy(760f)
        shot("s19-04-article-reading")
    }

    @Test fun readingDark() {
        launch(ThemeMode.DARK)
        scrollArticleBy(320f)
        shot("s19-05-article-reading-dark")
    }
}
