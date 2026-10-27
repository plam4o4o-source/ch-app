package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.data.local.WantToReadEntity
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
 * „Искам да прочета“: плочката в „Моето“, списъкът с наличността от каталога (налична, не е на
 * рафта, няма я в каталога; „Кажи ми“) и страницата на книга, която не е на рафта. Снимките
 * („80-watch-…“) се записват до тези на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class WantToReadScreenshotTest {
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
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(mode: ThemeMode) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val now = Instant.parse("2026-09-26T10:00:00Z")
        val container = AppContainer(ApplicationProvider.getApplicationContext(), config, FixedClock(now), XorTestCipher(), OkHttpClient())
        runBlocking {
            val dao = container.database.wantToRead()
            dao.clear()
            val t = now.toEpochMilli()
            // Налична, не е на рафта (с „Кажи ми“) и отписана (я няма в каталога).
            dao.upsert(WantToReadEntity(399666, "\"Ох...\"", "Джиан, Филип", t - 3_000, notifyWhenAvailable = false, lastKnownAvailable = true))
            dao.upsert(WantToReadEntity(624, "Белият дома", "Николай Симеонов", t - 1_000, notifyWhenAvailable = true, lastKnownAvailable = false))
            dao.upsert(WantToReadEntity(7, "Под игото", "Иван Вазов", t - 2_000, notifyWhenAvailable = false, lastKnownAvailable = false))
        }
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun shown(text: String) {
        compose.waitForIdle()
        val nodes = compose.onAllNodes(hasText(text, substring = true))
        val count = nodes.fetchSemanticsNodes().size
        val visible = (0 until count).any { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }
        if (!visible) throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения в дървото)")
    }

    private fun click(text: String) {
        waitFor(text)
        compose.onAllNodes(hasText(text, substring = true)).onFirst().performClick()
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** От „Моето“ до списъка — наличността е по текущия каталог. */
    private fun openList(mode: ThemeMode) {
        launch(mode)
        val tab = hasText("Моето") and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(tab).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(tab).onFirst().performClick()
        waitFor("3 книги в списъка")
        shown("3 книги в списъка")
        click("Искам да прочета")
        waitFor("Няма я в каталога")
        shown("Белият дома")
        shown("Не е на рафта")
        shown("налична")
        shown("Няма я в каталога")
        shown("Наличност към")
    }

    @Test fun listLight() {
        openList(ThemeMode.LIGHT)
        shot("80-watch-want-to-read-light")
    }

    @Test fun listDark() {
        openList(ThemeMode.DARK)
        shot("81-watch-want-to-read-dark")
    }

    /** Книга, която не е на рафта: „Попитай библиотеката“, „Кажи ми“ (вече включено) и отметката. */
    @Test fun bookNotOnShelf() {
        openList(ThemeMode.LIGHT)
        click("Белият дома")
        waitFor("Попитай библиотеката")
        shown("Попитай библиотеката")
        // Записът от базата идва асинхронно (Room) — след първия кадър.
        waitFor("Ще ти кажем, когато е на рафта")
        shown("Ще ти кажем, когато е на рафта")
        shown("Искам да прочета")
        shot("82-watch-book-not-on-shelf")
    }

    /** „Кажи ми“ е превключвател (за TalkBack — Switch, включен); изключването се отменя от снекбара. */
    @Test fun notifyToggleUndo() {
        openList(ThemeMode.LIGHT)
        click("Белият дома")
        waitFor("Ще ти кажем, когато е на рафта")
        val toggle = hasText("Ще ти кажем, когато е на рафта") and isToggleable() and
            SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch)
        compose.onAllNodes(toggle).onFirst().assertIsOn().performClick()
        waitFor("Няма да получаваш известие")
        val off = hasText("Кажи ми, когато е на рафта") and isToggleable()
        compose.waitUntil(10_000) { compose.onAllNodes(off).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(off).onFirst().assertIsOff()
        compose.onAllNodes(hasText("Отмени") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(toggle).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(toggle).onFirst().assertIsOn()
    }
}
