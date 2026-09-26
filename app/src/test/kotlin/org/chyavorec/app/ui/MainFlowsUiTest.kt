package org.chyavorec.app.ui

import android.app.Application
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.BuildConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant

/**
 * UI тестове на основните потоци върху истинския Compose UI (Robolectric), с
 * MockWebServer вместо chyavorec.org и GitHub.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class MainFlowsUiTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/news/rss/" -> MockResponse().setBody(TestResources.text("news-rss.xml"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/" -> MockResponse().setBody("<html><body><nav><a href='/index/history/0-64'>История</a></nav></body></html>")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(useMock: Boolean = false) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base,
            catalogUrls = listOf("$base/katalog.json"),
            inflibApiUrl = "",
            environment = "test",
            useMockData = useMock,
            versionName = "test",
            versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true), MutableStateFlow(null))
            }
        }
    }

    private fun waitForText(text: String, substring: Boolean = false) =
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }

    @Test fun homeShowsLatestNewsFromSite() {
        launch()
        waitForText("Покана за концерт по случай 1 ноември")
        compose.onAllNodesWithText("Покана за концерт по случай 1 ноември").onFirst().assertExists()
    }

    @Test fun catalogSearchAndBookDetails() {
        launch()
        compose.onAllNodesWithText("Каталог").onFirst().performClick()
        waitForText("резултата", substring = true)
        compose.onNode(hasSetTextAction()).performTextInput("Джиан")
        waitForText("\"Ох...\"")
        compose.onAllNodesWithText("\"Ох...\"").onFirst().performClick()
        waitForText("ЦБ/840/Д 51")
        compose.onNodeWithText("Сигнатура").assertExists()
        compose.onAllNodesWithText("Налична").onFirst().assertExists()
    }

    @Test fun guestSeesHonestLoginMessageAndCanAddCard() {
        launch(useMock = false)
        compose.onAllNodesWithText("Моето").onFirst().performClick()
        waitForText("Добре дошъл!")
        waitForText("Онлайн вход все още не се поддържа", substring = true)
        compose.onNodeWithText("Читателска карта").performClick()
        waitForText("Дигитална карта")
        compose.onAllNodes(hasSetTextAction()).onFirst().performTextInput("r-0042")
        compose.onNodeWithText("Запази").performClick()
        waitForText("Читателски № R-0042")
        compose.onNodeWithText("Покажи картата").performScrollTo().assertExists()
    }

    @Test fun demoLoginShowsLoansWithDueIndicators() {
        assumeTrue("демо данните съществуват само в dev flavor", BuildConfig.FLAVOR == "dev")
        launch(useMock = true)
        compose.onAllNodesWithText("Моето").onFirst().performClick()
        waitForText("Вход")
        compose.onAllNodes(hasText("Вход") and hasClickAction()).onFirst().performClick()
        waitForText("Читателски номер")
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("DEMO-0001")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("demo")
        compose.onAllNodes(hasText("Вход") and hasClickAction()).onFirst().performClick()
        waitForText("Демо Читател")
        compose.onNodeWithText("Моите книги").performClick()
        waitForText("Под игото")
        waitForText("остават 7 дни")
        waitForText("просрочена с 3 дни")
    }
}
