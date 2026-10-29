package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
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
import org.chyavorec.app.ui.components.WhatsNewSheet
import org.chyavorec.app.ui.theme.ChitalishteTheme
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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Текстовете от вълна 12 на английски: „Какво ново във версия X“, контактите без връзка
 * (вградените основни данни вместо грешка), „Моето“ и въведението, когато онлайн входът
 * не е включен („Добави читателска карта“ вместо „Вход“). Снимките — до тези на ScreenshotTest.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "en-w411dp-h1100dp-xhdpi")
class TextsScreenshotTest {
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
                // Страницата „Контакти“ не се зарежда — както без връзка при първо пускане.
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private val base get() = server.url("/").toString().trimEnd('/')

    private fun launch(settings: AppSettings = AppSettings(onboardingDone = true, introShown = true), deepLink: String? = null) {
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, settings, MutableStateFlow(deepLink), showIntro = false)
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

    private fun absent(text: String) {
        if (compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty()) {
            throw AssertionError("„$text“ не бива да се показва")
        }
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun whatsNewSheet() {
        compose.setContent {
            ChitalishteTheme {
                WhatsNewSheet(
                    versionName = "1.2.26",
                    notes = "• “Want to read”: save a book and get notified when it’s back on the shelf.\n" +
                        "• Subscribe to the community centre’s events in your phone’s calendar.\n• A fresher, tidier look.",
                    onDismiss = {},
                )
            }
        }
        waitFor("What’s new in version 1.2.26")
        shown("Want to read")
        shot("80-texts-whats-new-en")
    }

    /** Без контакти от сайта и без кеш: вградените основни данни и работното време, не грешка. */
    @Test fun contactsWithoutSite() {
        launch()
        // Бързото действие „Контакти“ на „Начало“.
        val contacts = hasText("Contacts") and hasClickAction()
        compose.waitUntil(15_000) { compose.onAllNodes(contacts).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(contacts).onFirst().performClick()
        waitFor("built into the app")
        shown("chitalishte_yavorec@abv.bg")
        shown("Monday – Thursday: 08:00 – 16:30")
        absent("Something went wrong")
        // Бутоните — по два на ред, еднакво широки; нечетният „Open website“ — на целия ред.
        fun bounds(label: String) = compose.onNode(hasText(label) and hasClickAction()).fetchSemanticsNode().boundsInRoot
        val email = bounds("Send email")
        val map = bounds("Open map")
        val site = bounds("Open website")
        assertEquals(email.top, map.top, 1f, "„Send email“ и „Open map“ — на един ред")
        assertEquals(email.width, map.width, 1f, "еднаква ширина")
        assertTrue(site.top >= email.bottom, "„Open website“ — на следващия ред")
        assertEquals(email.left, site.left, 1f, "„Open website“ — на целия ред")
        assertEquals(map.right, site.right, 1f, "„Open website“ — на целия ред")
        shot("81-texts-contacts-fallback-en")
    }

    /** Входът не е включен (без адрес на InvLib) — „Моето“ предлага карта, не вход. */
    @Test fun myWithoutLogin() {
        launch()
        val myTab = hasText("My library") and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(myTab).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(myTab).onFirst().performClick()
        waitFor("Add library card")
        shown("Add library card")
        absent("Sign in")
        shot("82-texts-my-no-login-en")
    }

    @Test fun onboardingWithoutLogin() {
        launch(settings = AppSettings(onboardingDone = false, introShown = true))
        repeat(4) {
            waitFor("Next")
            compose.onAllNodes(hasText("Next") and hasClickAction()).onFirst().performClick()
            compose.mainClock.advanceTimeBy(1_000)
        }
        waitFor("Add library card")
        shown("Continue")
        absent("Sign in as a reader")
        shot("83-texts-onboarding-no-login-en")
    }
}
