package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
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
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.screens.my.CardData
import org.chyavorec.app.ui.screens.my.LibraryCard
import org.chyavorec.app.ui.screens.my.LibraryCardBack
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.MembershipStatus
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
 * Снимки при много едър системен шрифт (2×) — текстът не бива да се реже (значката с
 * датата, бутоните, въведението, плочките и читателската карта), и екраните от вълна 8:
 * „Попитай библиотеката“ при заета книга, търсене по автор, датите на събитията.
 * Записват се до снимките на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class LargeFontScreenshotTest {
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

    private val base get() = server.url("/").toString().trimEnd('/')

    /** Мащабът на шрифта, както от системните настройки (Настройки › Дисплей › Размер на шрифта). */
    @Composable
    private fun FontScale(scale: Float, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = scale), content = content)
    }

    private fun launch(
        mode: ThemeMode = ThemeMode.LIGHT,
        fontScale: Float = 2f,
        settings: AppSettings = AppSettings(onboardingDone = true, introShown = true),
        deepLink: String? = null,
    ) {
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
                    ChitalishteRoot(container, settings, MutableStateFlow(deepLink), showIntro = false)
                }
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
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    // --- Едър шрифт (2×) ---

    @Test fun homeLarge() {
        launch()
        waitFor("45-ти")
        shot("50-font2-home")
    }

    @Test fun homeLargeDark() {
        launch(ThemeMode.DARK)
        waitFor("45-ти")
        shot("51-font2-home-dark")
    }

    @Test fun eventsLarge() {
        launch(deepLink = "events")
        waitFor("Световен ден")
        shot("52-font2-events")
    }

    @Test fun eventsLargeDark() {
        launch(ThemeMode.DARK, deepLink = "events")
        waitFor("Световен ден")
        shot("53-font2-events-dark")
    }

    @Test fun onboardingLarge() {
        launch(settings = AppSettings(onboardingDone = false, introShown = true))
        waitFor("Напред")
        shot("54-font2-onboarding")
    }

    @Test fun onboardingLargeLastSlide() {
        launch(settings = AppSettings(onboardingDone = false, introShown = true))
        repeat(4) {
            click("Напред")
            compose.mainClock.advanceTimeBy(1_000)
            compose.waitForIdle()
        }
        waitFor("Продължи без вход")
        shot("55-font2-onboarding-last")
    }

    @Test fun myHubLarge() {
        launch()
        tab("Моето")
        waitFor("Добре дошъл!")
        shot("56-font2-my")
    }

    private fun cards(name: String) {
        val data = CardData(
            holder = "Мария Иванова Петрова", number = "1024-5678", barcode = "10245678",
            status = MembershipStatus.ACTIVE, verified = true,
        )
        compose.setContent {
            FontScale(2f) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    Column(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).verticalScroll(rememberScrollState()).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        SectionLabel("Читателска карта")
                        LibraryCard(data)
                        LibraryCardBack(data)
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun libraryCardLarge() = cards("57-font2-card")

    // Тесен телефон: при 2× шрифт съдържанието не се събира в пропорциите — картата расте.
    @Config(qualifiers = "bg-w360dp-h1100dp-xhdpi")
    @Test fun libraryCardLargeNarrow() = cards("58-font2-card-narrow")

    // --- Заета книга, търсене по автор, дати на събития (обикновен шрифт) ---

    private val bookOnLoan get() = "$base/elektronen-katalog#zapis=136"

    @Test fun bookOnLoan() {
        launch(fontScale = 1f, deepLink = bookOnLoan)
        waitFor("Попитай библиотеката")
        shot("60-book-on-loan")
    }

    @Test fun bookOnLoanDark() {
        launch(ThemeMode.DARK, fontScale = 1f, deepLink = bookOnLoan)
        waitFor("Попитай библиотеката")
        shot("61-book-on-loan-dark")
    }

    @Test fun bookAskLibrarySheet() {
        launch(fontScale = 1f, deepLink = bookOnLoan)
        click("Попитай библиотеката")
        // Бутоните се появяват, щом контактите се заредят (тук — резервните, сайтът връща 404).
        waitFor("Изпрати имейл")
        shot("62-book-ask-library")
    }

    @Test fun bookAuthorOpensCatalogByAuthor() {
        launch(fontScale = 1f, deepLink = bookOnLoan)
        click("Симеон Колев")
        waitFor("данни към")
        shot("63-catalog-by-author")
    }

    @Test fun searchShowsFormattedEventDates() {
        launch(fontScale = 1f)
        click("Търси книга")
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasSetTextAction()).onFirst().performTextInput("ден")
        waitFor("декември")
        shot("64-search-event-dates")
    }

    @Config(qualifiers = "en-w411dp-h1100dp-xhdpi")
    @Test fun eventsEnglish() {
        launch(fontScale = 1f, deepLink = "events")
        waitFor("DEC")
        shot("65-events-en")
    }
}
