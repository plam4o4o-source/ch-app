package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
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

/**
 * „Новини и събития“: сегментът „Календар“ (връзка към събитията), месечният календар
 * хоризонтално и на таблет, абонаментът за календара. Снимките са до тези на ScreenshotTest
 * (app/build/reports/screenshots/), с номера от 80 нагоре.
 *
 * Примерният календар (site-index.json) има прояви само през януари и декември; днес е
 * 26 септември — затова календарът се превърта до декември.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class EventsScreenshotTest {
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

    private fun launch(mode: ThemeMode = ThemeMode.LIGHT, deepLink: String? = null) {
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
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(deepLink), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun clickDescription(description: String) {
        // Бутонът може да е в календара, превъртян нагоре — първо обратно до него.
        val lists = compose.onAllNodes(hasScrollToNodeAction())
        (0 until lists.fetchSemanticsNodes().size).forEach {
            runCatching { lists[it].performScrollToNode(hasContentDescription(description)) }
        }
        compose.waitUntil(15_000) { compose.onAllNodes(hasContentDescription(description)).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasContentDescription(description)).onFirst().performClick()
        compose.waitForIdle()
    }

    /** Поне един възел с [text] е видим на екрана (не само в дървото). */
    private fun shown(text: String) {
        compose.waitForIdle()
        val nodes = compose.onAllNodes(hasText(text, substring = true))
        val count = nodes.fetchSemanticsNodes().size
        if ((0 until count).none { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }) {
            throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения в дървото)")
        }
    }

    /**
     * [text] е в превъртащ се списък — превърта се до него (LazyColumn композира само видимото:
     * на нисък екран календарът сам заема почти цялата височина), после е видим.
     */
    private fun scrolledTo(text: String) {
        compose.waitForIdle()
        val lists = compose.onAllNodes(hasScrollToNodeAction())
        val found = (0 until lists.fetchSemanticsNodes().size).any {
            runCatching { lists[it].performScrollToNode(hasText(text, substring = true)) }.isSuccess
        }
        if (!found) throw AssertionError("„$text“ го няма в нито един списък")
        shown(text)
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Връзката „events“ → разделът „Новини“ на сегмента „Календар“; после месечният изглед, декември. */
    private fun openDecemberCalendar() {
        launch(deepLink = "events")
        waitFor("Световен ден")
        clickDescription("Изглед календар")
        waitFor("Септември 2026")
        // Празен месец: календарът остава (за смяна на месеца), а под него — кратък ред.
        scrolledTo("Няма прояви в този месец")
        repeat(3) { clickDescription("Следващ месец") }
        waitFor("Декември 2026")
    }

    @Test fun eventsLinkOpensTheCalendarSegment() {
        launch(deepLink = "events")
        waitFor("Световен ден")
        shown("Новини и събития")
        compose.onAllNodes(hasText("Календар") and isSelectable()).onFirst().assertIsSelected()
        // В долната лента разделът е с краткото си име и е избран.
        compose.onAllNodes(hasText("Новини") and isSelectable()).fetchSemanticsNodes().let {
            if (it.isEmpty()) throw AssertionError("няма раздел „Новини“")
        }
        shot("80-events-segment")
    }

    /**
     * Телефон хоризонтално (891 × 411 dp — от 840 dp е широкият изглед): календарът отляво,
     * проявите отдясно. Преди шестте реда квадратни клетки бяха ~660 dp, последните седмици
     * се рязаха, а списъкът за деня оставаше с нулева височина.
     */
    @Config(qualifiers = "bg-w891dp-h411dp-land-xxhdpi")
    @Test fun calendarLandscape() {
        openDecemberCalendar()
        shown("Декември 2026")
        shown("Световен ден")
        shown("Международен ден на инвалида")
        shot("81-events-calendar-landscape")
    }

    /** По-тесен телефон хоризонтално (под 840 dp): календарът и списъкът са в една превъртаща се колона. */
    @Config(qualifiers = "bg-w740dp-h360dp-land-xxhdpi")
    @Test fun calendarLandscapeNarrow() {
        openDecemberCalendar()
        scrolledTo("Международен ден на инвалида")
        shot("82-events-calendar-landscape-narrow")
    }

    @Config(qualifiers = "bg-w1280dp-h800dp-land-xhdpi")
    @Test fun calendarTablet() {
        openDecemberCalendar()
        shown("Декември 2026")
        shown("Световен ден")
        shot("83-events-calendar-tablet")
    }

    @Test fun subscribeSheet() {
        launch(deepLink = "events")
        waitFor("Световен ден")
        clickDescription("Абонирай се за календара")
        waitFor("Копирай връзката")
        shown("Копирай връзката")
        shown("Google Календар")
        shown("/events.ics")
        shot("84-events-subscribe")
    }

    @Config(qualifiers = "en-w411dp-h1100dp-xhdpi")
    @Test fun calendarSegmentEnglishDark() {
        launch(ThemeMode.DARK, deepLink = "events")
        waitFor("DEC")
        shown("News & events")
        compose.onAllNodes(hasText("Calendar") and isSelectable()).onFirst().assertIsSelected()
        shot("85-events-segment-en-dark")
    }
}
