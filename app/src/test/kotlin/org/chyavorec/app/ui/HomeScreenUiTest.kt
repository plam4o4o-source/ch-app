package org.chyavorec.app.ui

import android.app.Application
import android.provider.Settings
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.text.TextLayoutResult
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
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * „Начало“: бързите действия („Искам да прочета“ вместо повтарящите се „Събития“), без
 * пренасяне по букви при едър шрифт, и въртележката хоризонтално — не на целия екран.
 */
@RunWith(AndroidJUnit4::class)
// Истинско разполагане на текста (редове, препълване) — както в NavLayoutScreenshotTest.
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class HomeScreenUiTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

    @Before fun start() {
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

    @After fun stop() = server.shutdown()

    private fun launch(fontScale: Float = 1f) {
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
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = fontScale)) {
                ChitalishteTheme {
                    ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
                }
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    @Test fun wantToReadQuickActionOpensTheList() {
        launch()
        waitFor("45-ти")
        val action = hasText("Искам да прочета") and hasClickAction()
        compose.waitUntil(10_000) { compose.onAllNodes(action).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(action).onFirst().performClick()
        waitFor("Списъкът е празен")
    }

    /** „Много голям“ шрифт: етикетите на бързите действия се пренасят само между думите. */
    @Test fun quickActionLabelsDoNotBreakInsideAWordAtLargeFont() {
        launch(fontScale = 1.3f)
        waitFor("45-ти")
        listOf("Читателска карта", "Искам да прочета").forEach { label ->
            compose.waitForIdle()
            val nodes = compose.onAllNodes(hasText(label), useUnmergedTree = true).fetchSemanticsNodes()
                .filter { it.config.getOrNull(SemanticsActions.GetTextLayoutResult) != null }
            assertTrue(nodes.isNotEmpty(), "„$label“ липсва")
            nodes.forEach { node ->
                val layouts = mutableListOf<TextLayoutResult>()
                node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
                val layout = layouts.single()
                val text = layout.layoutInput.text.text
                for (line in 0 until layout.lineCount - 1) {
                    val end = layout.getLineEnd(line)
                    assertTrue(text[end - 1].isWhitespace(), "„$label“ е пренесен в дума: „${text.substring(0, end)}|${text.substring(end)}“")
                }
                // Без многоточие (hasVisualOverflow не става: центрираният текст е по-тесен от абзаца).
                assertTrue((0 until layout.lineCount).none { layout.isLineEllipsized(it) }, "„$label“ е отрязан")
            }
        }
    }

    /**
     * TalkBack: във въртележката няма безименни бутони — страничните карти (маскирани, с
     * прозрачен текст) са скрити, а голямата е с текста си и с „Следваща/Предишна новина“.
     */
    @Test fun carouselHasNoUnlabelledButtons() {
        launch()
        waitFor("45-ти")
        compose.waitForIdle()
        val cards = compose.onAllNodes(hasClickAction() and hasAnyAncestor(hasContentDescription("Последни новини"))).fetchSemanticsNodes()
        assertTrue(cards.size >= 2, "очакват се голямата и поне една странична карта, а са ${cards.size}")
        val forReader = cards.filter { it.config.getOrNull(SemanticsProperties.HideFromAccessibility) == null }
        assertEquals(1, forReader.size, "за екранния четец е само голямата карта")
        val main = forReader.single()
        assertTrue(main.config.getOrNull(SemanticsProperties.Text).orEmpty().any { it.text.isNotBlank() }, "голямата карта е без текст")
        val actions = main.config.getOrNull(SemanticsActions.CustomActions).orEmpty().map { it.label }
        assertTrue("Следваща новина" in actions, "$actions")
    }

    /**
     * Целите за докосване на „Начало“ — поне 48 × 48 dp, и картите на събитията (280 dp). Dump-ът
     * на емулатора мери само видимото: карта, отрязана от навигацията долу, излиза „280×28 dp“.
     */
    @Test fun touchTargetsAreAtLeast48dp() {
        launch()
        waitFor("45-ти")
        // Събитията са под първия екран — превърта се до първото, щом се появи в списъка.
        compose.waitUntil(15_000) {
            runCatching { compose.onAllNodes(hasScrollToNodeAction()).onFirst().performScrollToNode(hasText("Световен ден", substring = true)) }.isSuccess
        }
        compose.waitForIdle()
        val min = with(compose.density) { 48.dp.toPx() } - 1f
        fun label(n: androidx.compose.ui.semantics.SemanticsNode) =
            (n.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() +
                n.config.getOrNull(SemanticsProperties.Text).orEmpty().map { it.text }).joinToString(" ").take(40)
        // Страничните карти на въртележката са скрити от екранния четец (виж carouselHasNoUnlabelledButtons).
        val targets = compose.onAllNodes(hasClickAction()).fetchSemanticsNodes()
            .filter { it.config.getOrNull(SemanticsProperties.HideFromAccessibility) == null }
        assertTrue(targets.any { "Световен ден" in label(it) }, "картата на събитието липсва: ${targets.map(::label)}")
        // Размерът на целия възел: при minimumInteractiveComponentSize (IconButton) кликаемият
        // модификатор е 40 dp, а целта за докосване — 48 dp.
        val small = targets.filter { maxOf(it.size.width, it.layoutInfo.width) < min || maxOf(it.size.height, it.layoutInfo.height) < min }
            .map { "„${label(it)}“ ${it.size} / ${it.layoutInfo.width}x${it.layoutInfo.height}" }
        assertTrue(small.isEmpty(), "под 48 dp: $small")
    }

    /** TalkBack обявява раздела при отварянето му: „Начало“, после „Каталог“ (от заглавката). */
    @Test fun tabRootsHavePaneTitles() {
        launch()
        waitFor("45-ти")
        fun pane(title: String) = SemanticsMatcher.expectValue(SemanticsProperties.PaneTitle, title)
        compose.waitUntil(5_000) { compose.onAllNodes(pane("Начало")).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasText("Каталог") and isSelectable()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(pane("Каталог")).fetchSemanticsNodes().isNotEmpty() }
    }

    /** Колко време след смяната на раздел „Начало“ (въртележката) още е в дървото. */
    private fun homeShownAfterTabSwitch(ms: Long): Boolean {
        waitFor("45-ти")
        compose.waitForIdle()
        compose.mainClock.autoAdvance = false
        compose.onAllNodes(hasText("Каталог") and isSelectable()).onFirst().performClick()
        compose.mainClock.advanceTimeBy(ms)
        return compose.onAllNodes(hasContentDescription("Последни новини")).fetchSemanticsNodes().isNotEmpty()
    }

    /** Смяната на раздел е плъзгане по оста X (~300 ms) — след 100 ms „Начало“ още излиза. */
    @Test fun tabSwitchSlides() {
        launch()
        assertTrue(homeShownAfterTabSwitch(100), "без намалено движение има преход")
    }

    /** „Премахване на анимациите“: екраните се сменят без преход — „Начало“ го няма веднага. */
    @Test fun tabSwitchSnapsWithReducedMotion() {
        val resolver = ApplicationProvider.getApplicationContext<Application>().contentResolver
        Settings.Global.putFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 0f)
        try {
            launch()
            assertFalse(homeShownAfterTabSwitch(100), "при намалено движение екранът се сменя без преход")
        } finally {
            Settings.Global.putFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        }
    }

    @Config(qualifiers = "bg-w891dp-h411dp-land-xxhdpi")
    @Test fun heroDoesNotFillTheScreenInLandscape() {
        launch()
        waitFor("45-ти")
        compose.waitForIdle()
        val window = compose.onRoot().fetchSemanticsNode().boundsInRoot.height
        val hero = compose.onAllNodes(hasContentDescription("Последни новини")).onFirst().fetchSemanticsNode().boundsInRoot.height
        assertTrue(hero <= window * 0.5f, "въртележката е $hero от $window px")
    }
}
