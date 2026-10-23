package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
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
import kotlin.test.assertTrue

/**
 * Навигацията на телефон в хоризонтално положение (страничната лента побира и петия раздел,
 * „Още“) и при шрифт 1.3× (етикетите на разделите и текстовете на плочките в „Още“ не се режат
 * с многоточие). Снимките се записват до тези на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w891dp-h411dp-land-xxhdpi")
class NavLayoutScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
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
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    /** Мащабът на шрифта, както от системните настройки. */
    @Composable
    private fun FontScale(scale: Float, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = scale), content = content)
    }

    private fun launch(fontScale: Float = 1f, mode: ThemeMode = ThemeMode.LIGHT) {
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
            FontScale(fontScale) {
                ChitalishteTheme(mode = mode) {
                    ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
                }
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private val tabMatcher: (String) -> SemanticsMatcher = { hasText(it) and isSelectable() }

    private fun tab(label: String) {
        compose.waitUntil(10_000) { compose.onAllNodes(tabMatcher(label)).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(tabMatcher(label)).onFirst().performClick()
    }

    /** Разделът [label] е в лентата и се вижда на екрана (не е изрязан извън него). */
    private fun tabShown(label: String) {
        compose.waitForIdle()
        compose.onAllNodes(tabMatcher(label)).onFirst().assertIsDisplayed()
    }

    /** Текстът се вижда целият — без многоточие и без препълване (по разположението на текста). */
    private fun notCut(text: String, inTab: Boolean = false) {
        compose.waitForIdle()
        val matcher = if (inTab) hasText(text) and hasAnyAncestor(isSelectable()) else hasText(text)
        val nodes = compose.onAllNodes(matcher, useUnmergedTree = true).fetchSemanticsNodes()
            .filter { it.config.getOrNull(SemanticsActions.GetTextLayoutResult) != null }
        assertTrue(nodes.isNotEmpty(), "„$text“ липсва")
        nodes.forEach { node ->
            val layouts = mutableListOf<TextLayoutResult>()
            node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
            val layout = layouts.single()
            val ellipsized = (0 until layout.lineCount).any { layout.isLineEllipsized(it) }
            assertTrue(!ellipsized && !layout.hasVisualOverflow, "„$text“ е отрязан (${layout.lineCount} реда)")
        }
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    // --- Телефон, хоризонтално: страничната лента с всичките пет раздела ---

    @Test fun landscapeRailShowsAllTabs() {
        launch()
        waitFor("45-ти")
        listOf("Начало", "Каталог", "Моето", "Новини", "Още").forEach(::tabShown)
        shot("70-nav-landscape-home")
        tab("Още")
        waitFor("Галерия")
        shot("71-nav-landscape-more")
    }

    @Config(qualifiers = "en-w891dp-h411dp-land-xxhdpi")
    @Test fun landscapeRailShowsAllTabsEnglishLargeFont() {
        launch(fontScale = 1.3f, mode = ThemeMode.DARK)
        waitFor("45-ти")
        listOf("Home", "Catalogue", "My", "News", "More").forEach(::tabShown)
        notCut("Catalogue", inTab = true)
        shot("72-nav-landscape-font130-en-dark")
    }

    // --- Шрифт 1.3×, изправен телефон: етикетите на долната лента и плочките в „Още“ ---

    @Config(qualifiers = "en-w411dp-h891dp-xxhdpi")
    @Test fun font130BottomBarEnglish() {
        launch(fontScale = 1.3f)
        waitFor("45-ти")
        listOf("Home", "Catalogue", "My", "News", "More").forEach { notCut(it, inTab = true) }
        shot("73-nav-font130-home-en")
    }

    @Config(qualifiers = "bg-w411dp-h891dp-xxhdpi")
    @Test fun font130BottomBarBulgarian() {
        launch(fontScale = 1.3f)
        waitFor("45-ти")
        listOf("Начало", "Каталог", "Моето", "Новини", "Още").forEach { notCut(it, inTab = true) }
        tab("Още")
        waitFor("Галерия")
        notCut("Съобщения и покани от читалището")
        notCut("Устав, отчети, юбилейният вестник")
        shot("74-nav-font130-more-bg")
    }

    @Config(qualifiers = "en-w411dp-h891dp-xxhdpi")
    @Test fun font130MoreTilesEnglish() {
        launch(fontScale = 1.3f)
        tab("More")
        waitFor("Gallery")
        notCut("Announcements and invitations from the centre")
        notCut("Statute, reports, jubilee newspaper")
        notCut("Documents & archive")
        shot("75-nav-font130-more-en")
    }
}
