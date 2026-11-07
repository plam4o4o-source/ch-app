package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.chyavorec.app.ui.components.BOOK_OPEN_MAX_DEG
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.ConnectedButtonGroup
import org.chyavorec.app.ui.components.bookOpenAngle
import org.chyavorec.app.ui.components.bookOpening
import org.chyavorec.app.ui.components.tabRosette
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.CatalogBook
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Движенията на вълна 19: розетката на избрания раздел (междинни кадри), свързаните
 * бутони и „Разгръщане на книга“ (корицата при няколко ъгъла и междинен кадър на истинския
 * преход каталог → книга). За визуален преглед (клонът „screenshots“) — не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class MotionScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
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

    private fun save(bmp: Bitmap, name: String) =
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun capture(): Bitmap = compose.onRoot().captureToImage().asAndroidBitmap()

    /** Кадрите един до друг (по [heightPx] от горния край на всеки). */
    private fun strip(frames: List<Bitmap>, heightPx: Int = frames.first().height): Bitmap {
        val w = frames.first().width
        val out = Bitmap.createBitmap(w * frames.size, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        frames.forEachIndexed { i, f -> canvas.drawBitmap(f, (i * w).toFloat(), 0f, null) }
        return out
    }

    @Test fun angleOpensAndClosesWithTheTransition() {
        assertEquals(0f, bookOpenAngle(0f))
        assertEquals(BOOK_OPEN_MAX_DEG, bookOpenAngle(0.5f), 0.01f)
        assertTrue(bookOpenAngle(1f) < 0.01f, "в края корицата е затворена")
        assertTrue(bookOpenAngle(0.25f) in 1f..89f)
    }

    @Composable
    private fun RosetteRow(selected: Boolean, dark: Boolean) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surfaceContainer).padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp),
            ) {
                Box(
                    Modifier.tabRosette(
                        selected, reduced = false,
                        fill = MaterialTheme.colorScheme.primaryContainer,
                        stitch = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.45f),
                    ),
                ) { Icon(Icons.Outlined.Home, null, tint = MaterialTheme.colorScheme.onPrimaryContainer) }
            }
        }
    }

    /** Розетката „разцъфва“: кадри през 0, 30, 60, 90, 140, 220 и 600 ms след избора — светла и тъмна тема. */
    @Test fun navRosetteMorphFrames() {
        var selected by mutableStateOf(false)
        compose.mainClock.autoAdvance = false
        compose.setContent { Column { RosetteRow(selected, dark = false); RosetteRow(selected, dark = true) } }
        compose.mainClock.advanceTimeByFrame()
        selected = true
        val frames = listOf(0L, 30L, 30L, 30L, 50L, 80L, 380L).map { t ->
            compose.mainClock.advanceTimeBy(t)
            capture()
        }
        save(strip(frames), "w19m-01-nav-rosette-frames")
    }

    @Composable
    private fun Groups(dark: Boolean) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            Column(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ConnectedButtonGroup(listOf(false, true), true, {}, Modifier.fillMaxWidth()) { c, _ ->
                    Text(if (c) "Календар" else "Новини")
                }
                ConnectedButtonGroup(listOf(1f, 1.15f, 1.3f, 1.5f), 1.15f, {}, Modifier.fillMaxWidth()) { s, _ ->
                    Text("Аа", fontSize = (15 * s).sp)
                }
                ConnectedButtonGroup(listOf("Нормална", "Тясна"), "Нормална", {}, Modifier.fillMaxWidth()) { w, _ -> Text(w) }
            }
        }
    }

    /** Свързаните бутони: „Новини | Календар“, „Аа“ в четири размера и ширината в режима за четене. */
    @Test fun connectedButtonGroups() {
        compose.setContent { Column { Groups(dark = false); Groups(dark = true) } }
        compose.onAllNodes(hasText("Календар") and isSelectable()).onFirst()
            .assertIsSelected()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
        compose.waitForIdle()
        save(capture(), "w19m-02-button-groups")
    }

    private val book = CatalogBook(inv = 624, author = "Яна Язова", title = "Ох...", available = true)

    /** Корицата при 0°, 35°, 70°, 100° и 110°: страницата отдолу, а над 90° — форзацът. */
    @Test fun bookOpeningAngles() {
        val angles = listOf(0f, 35f, 70f, 100f, BOOK_OPEN_MAX_DEG)
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Row(
                    Modifier.background(MaterialTheme.colorScheme.background).padding(start = 64.dp, top = 24.dp, bottom = 24.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(76.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    angles.forEach { a -> BookCover(book, width = 64.dp, showStatusDot = false, modifier = Modifier.bookOpening { a }, decorative = true) }
                }
            }
        }
        compose.waitForIdle()
        save(capture(), "w19m-03-book-open-angles")
    }

    private fun launch() {
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
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow<String?>(null), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    /**
     * Истинският преход каталог → книга: кадри 60, 140 и 260 ms след докосването (корицата
     * „прелита“ и се разтваря), после книгата се отваря както досега.
     */
    @Test fun bookOpenTransitionFrames() {
        launch()
        val tab = hasText("Каталог") and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(tab).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(tab).onFirst().performClick()
        waitFor("резултата")
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
        compose.onNode(hasSetTextAction()).performTextInput("Джиан")
        waitFor("\"Ох...\"")
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()

        compose.mainClock.autoAdvance = false
        compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).onFirst().performClick()
        val frames = listOf(60L, 80L, 120L).map { t ->
            compose.mainClock.advanceTimeBy(t)
            capture()
        }
        // Горната половина — там са и редът в списъка, и корицата в детайлите.
        save(strip(frames, heightPx = frames.first().height / 2), "w19m-04-book-open-transition")

        compose.mainClock.autoAdvance = true
        waitFor("ЦБ/840/Д 51")
    }
}
