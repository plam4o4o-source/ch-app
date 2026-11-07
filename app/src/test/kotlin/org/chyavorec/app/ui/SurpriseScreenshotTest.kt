package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.provider.Settings
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.screens.catalog.SurpriseSession
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
import java.io.File
import java.time.Instant

/**
 * „Изненадай ме“ в каталога: бутонът → разбъркване → обърнатата карта с предложението
 * (налична книга), „Към книгата“ отваря книгата; при намалено движение — само поява.
 * Снимките „96-surprise-…“ — до тези на ScreenshotTest.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class SurpriseScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @Before fun start() {
        SurpriseSession.recent = emptyList()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() {
        Settings.Global.putFloat(ApplicationProvider.getApplicationContext<Application>().contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        server.shutdown()
    }

    private fun launch(mode: ThemeMode = ThemeMode.LIGHT) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config, FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
            }
        }
        val tab = hasText("Каталог") and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(tab).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(tab).onFirst().performClick()
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("резултата", substring = true)).fetchSemanticsNodes().isNotEmpty() }
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
    }

    private fun shot(name: String) {
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Възелът за TalkBack („Предложение: заглавие — автор“), който се сменя на място. */
    private val announcement = SemanticsMatcher("предложение") { n ->
        n.config.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }.any { it.startsWith("Предложение: ") }
    }

    private fun surprise() {
        compose.onAllNodes(hasContentDescription("Изненадай ме") and hasClickAction()).onFirst().performClick()
    }

    private fun awaitReveal() {
        compose.waitUntil(15_000) {
            compose.onAllNodes(hasText("Към книгата") and hasClickAction()).fetchSemanticsNodes().isNotEmpty() &&
                compose.onAllNodes(announcement).fetchSemanticsNodes().isNotEmpty()
        }
        compose.mainClock.advanceTimeBy(800)
        compose.waitForIdle()
    }

    @Test fun buttonShufflesAndRevealsAnAvailableBook() {
        launch()
        surprise()
        // По средата на разбъркването: картите с гръб-шевица, ветрило.
        compose.mainClock.autoAdvance = false
        compose.mainClock.advanceTimeBy(420)
        shot("96-surprise-shuffle")
        compose.mainClock.autoAdvance = true
        awaitReveal()
        shot("96-surprise-reveal-light")
        compose.onAllNodes(hasText("Още една") and hasClickAction()).onFirst().assertExists()
        // „Към книгата“ — страницата на предложената книга (налична).
        compose.onAllNodes(hasText("Към книгата") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Сигнатура")).fetchSemanticsNodes().isNotEmpty() }
        check(compose.onAllNodes(hasText("Налична")).fetchSemanticsNodes().isNotEmpty()) { "предложената книга не е налична" }
    }

    @Test fun anotherOneSuggestsADifferentBook() {
        launch(ThemeMode.DARK)
        surprise()
        awaitReveal()
        val first = compose.onAllNodes(announcement).onFirst().fetchSemanticsNode().config[SemanticsProperties.ContentDescription].first()
        shot("97-surprise-reveal-dark")
        compose.onAllNodes(hasText("Още една") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(15_000) {
            compose.onAllNodes(announcement).fetchSemanticsNodes().any { n ->
                n.config[SemanticsProperties.ContentDescription].first().let { it.startsWith("Предложение: ") && it != first }
            }
        }
        // „Затвори“ — обратно в каталога.
        compose.onAllNodes(hasContentDescription("Затвори") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(5_000) { compose.onAllNodes(hasText("Към книгата")).fetchSemanticsNodes().isEmpty() }
    }

    /** Намалено движение: без тесте — резултатът просто се появява. */
    @Test fun reducedMotionJustFadesIn() {
        Settings.Global.putFloat(ApplicationProvider.getApplicationContext<Application>().contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 0f)
        launch()
        surprise()
        awaitReveal()
        shot("98-surprise-reduced-motion")
    }
}
