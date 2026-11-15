package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ConnectivityChecker
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.screens.messages.MessageCard
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.MessagePriority
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
import java.util.concurrent.atomic.AtomicLong

/**
 * „Начало“ с истински на вид плакати във въртележката (собствен текст горе и долу — като
 * афишите на сайта): заглавието на новината е на мастилена плоскост под снимката 16:9, не върху
 * текста на плаката; страничните карти са притъмнени. Под тях — имения ден с бродираното ромбче и
 * „Нови постъпления“ само с кориците (без рамка и празна ивица). Плюс картите на съобщенията със
 * знака „Ново“ (светла и тъмна тема).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1500dp-xhdpi")
class LookScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val poster: ByteArray by lazy { posterPng() }

    /** Кога сървърът за пръв път е дал плаката (System.nanoTime), 0 — още не. */
    private val posterServedAt = AtomicLong(0)

    @OptIn(DelicateCoilApi::class, ExperimentalCoilApi::class)
    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path?.substringBefore('?').orEmpty()
                return when {
                    path == "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                    path == "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                    path == "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                    path == "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                    path == "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
                    path == "/data/app-messages.json" -> MockResponse().setBody("[]")
                    // Снимките на новините — един и същ „плакат“.
                    path.startsWith("/assets/") -> {
                        posterServedAt.compareAndSet(0, System.nanoTime())
                        MockResponse().setHeader("Content-Type", "image/png").setBody(Buffer().write(poster))
                    }
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        server.start()
        // Само към тестовия сървър: чужди адреси (Open Library) — към него, т.е. 404.
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val url = chain.request().url
            val to = if (url.host == server.hostName) url else url.newBuilder().scheme("http").host(server.hostName).port(server.port).build()
            chain.proceed(chain.request().newBuilder().url(to).build())
        }.build()
        val online = object : ConnectivityChecker {
            override fun isOnline(): Boolean = true
        }
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(ApplicationProvider.getApplicationContext<Application>()).components {
                add(OkHttpNetworkFetcherFactory(callFactory = { client }, connectivityChecker = { online }))
            }.build(),
        )
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

    /** Плакат 3:2 като афишите на сайта: ярък фон, едър текст горе, дата долу, кръгове-„танцьори“. */
    private fun posterPng(): ByteArray {
        val w = 1200
        val h = 800
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val bg = Paint().apply { shader = LinearGradient(0f, 0f, w.toFloat(), h.toFloat(), 0xFFE0482E.toInt(), 0xFFF2B33D.toInt(), Shader.TileMode.CLAMP) }
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bg)
        val dot = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF7A1424.toInt() }
        for (i in 0 until 7) c.drawCircle(150f + i * 150f, 470f, 46f, dot)
        val big = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFFFFF.toInt(); textSize = 150f; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.CENTER
        }
        c.drawText("СЪБОР 2026", w / 2f, 230f, big)
        val small = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF1A1208.toInt(); textSize = 72f; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.CENTER
        }
        c.drawText("30 АВГУСТ · ЯВОРЕЦ", w / 2f, 680f, small)
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        return out.toByteArray()
    }

    private fun launch(mode: ThemeMode) {
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
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private fun home(mode: ThemeMode, name: String) {
        launch(mode)
        waitFor("45-ти")
        waitFor("Йоан Богослов")
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Нови постъпления")).fetchSemanticsNodes().isNotEmpty() }
        // Плакатът е даден от сървъра и Coil е имал време да го декодира и покаже (главната нишка
        // се изпълнява, докато waitUntil чака).
        compose.waitUntil(20_000) {
            val at = posterServedAt.get()
            at != 0L && System.nanoTime() - at > 2_000_000_000L
        }
        compose.mainClock.advanceTimeBy(2_000)
        shot("$name-top")
        // „Нови постъпления“ — кориците без рамка.
        compose.onAllNodes(hasScrollToNodeAction()).onFirst().performScrollToNode(hasText("Нови постъпления"))
        shot("$name-books")
    }

    @Test fun homePosterLight() = home(ThemeMode.LIGHT, "w24-home-poster-light")

    @Test fun homePosterDark() = home(ThemeMode.DARK, "w24-home-poster-dark")

    /** Съобщенията: новото е със знака „Ново“ (ромбче и златна дума), плочките са 36 dp. */
    @Test fun messageCards() {
        val msgs = listOf(
            AppMessage("1", "Промяна в работното време", "От 1 октомври библиотеката работи до 17:00.", priority = MessagePriority.HIGH, createdAt = "2026-09-25T08:00:00Z"),
            AppMessage("2", "Общо събрание на членовете", "Събранието е в салона на читалището от 18:00.", createdAt = "2026-09-24T08:00:00Z"),
            AppMessage("3", "", "Книгата, която запазихте, ви очаква.", createdAt = "2026-09-23T08:00:00Z", personal = true),
        )
        compose.setContent {
            Column {
                for (mode in listOf(ThemeMode.LIGHT, ThemeMode.DARK)) {
                    ChitalishteTheme(mode = mode) {
                        Column(
                            Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            msgs.forEachIndexed { i, m -> MessageCard(m, isNew = i != 1, openLink = {}) }
                        }
                    }
                }
            }
        }
        shot("w24-messages-new-mark")
    }
}
