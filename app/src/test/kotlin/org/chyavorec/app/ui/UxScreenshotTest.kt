package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
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
import org.chyavorec.app.FakeBarcodeScanner
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.data.scan.ScanOutcome
import org.chyavorec.app.data.local.TextScale
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
 * „Размер на текста“ (Настройки → Изглед) на „Много голям“ — „Начало“ и „Настройки“ не се
 * режат; скенерът без Google Play услуги (подменен — „недостъпен“) направо с „Въведи
 * номера на ръка“. Записват се до снимките на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class UxScreenshotTest {
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
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(textScale: TextScale = TextScale.XLARGE, deepLink: String? = null, scanner: FakeBarcodeScanner = FakeBarcodeScanner()) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(), scannerOverride = scanner,
        )
        val settings = AppSettings(onboardingDone = true, introShown = true, textScale = textScale)
        // „Настройки“ четат хранилището (не подадените настройки) — същият размер и там.
        runBlocking { container.settings.setTextScale(textScale) }
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                ChitalishteRoot(container, settings, MutableStateFlow(deepLink), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun click(text: String) {
        waitFor(text)
        compose.onAllNodes(hasText(text, substring = true) and hasClickAction()).onFirst().performClick()
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
        // С отворен диалог или лист (отделен прозорец) — екранът заедно с него.
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun homeExtraLargeText() {
        launch()
        waitFor("45-ти")
        // Етикетите на лентата (ограничени до 1.3× от ефективния мащаб) — цели, не „Нача…“.
        compose.onAllNodes(hasText("Начало") and isSelectable()).onFirst().assertIsDisplayed()
        shot("80-ux-text-xlarge-home")
    }

    @Test fun settingsExtraLargeText() {
        launch()
        tab("Още")
        click("Настройки")
        waitFor("Размер на текста")
        // След прехода към „Настройки“ — елементът е в първия екран, не само в дървото.
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_000)
        compose.onAllNodes(hasText("Много голям")).onFirst().performScrollTo().assertIsDisplayed()
        shot("81-ux-text-xlarge-settings")
    }

    @Test fun textSizeChoice() {
        launch(TextScale.STANDARD)
        tab("Още")
        click("Настройки")
        click("Размер на текста")
        waitFor("Много голям")
        compose.onNode(isDialog()).assertExists()
        shot("82-ux-text-size-dialog")
    }

    @Test fun scannerUnavailableFallsBackToManualEntry() {
        launch(TextScale.STANDARD, deepLink = "scan", scanner = FakeBarcodeScanner(ScanOutcome.Unavailable))
        // Без скенер панелът „на ръка“ се отваря сам — с обяснението защо.
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
        waitFor("Google Play услугите")
        shot("83-ux-scan-unavailable")
        // TalkBack: панелът е с име, а съдържанието под него (бутонът „Въведи…“ отзад) не е в дървото.
        compose.onAllNodes(SemanticsMatcher.expectValue(SemanticsProperties.PaneTitle, "Въведи номера на ръка")).assertCountEquals(1)
        compose.onAllNodes(hasText("Въведи номера на ръка") and hasClickAction()).assertCountEquals(0)
        // Затъмнението извън панела не е безименна област за докосване.
        for (node in compose.onAllNodes(hasClickAction()).fetchSemanticsNodes()) {
            val c = node.config
            val named = c.contains(SemanticsProperties.Text) || c.contains(SemanticsProperties.ContentDescription) || c.contains(SemanticsProperties.EditableText)
            check(named) { "безименно действие: $c" }
        }
        compose.onAllNodes(hasSetTextAction()).onFirst().performTextInput("399666")
        shot("84-ux-scan-manual")
        compose.onAllNodes(hasText("Търси") and hasClickAction()).onFirst().performClick()
        // Същият лист като при сканиране.
        waitFor("Отвори книгата")
        compose.onAllNodes(hasText("Отвори книгата")).onFirst().assertIsDisplayed()
        shot("85-ux-scan-manual-hit")
    }
}
