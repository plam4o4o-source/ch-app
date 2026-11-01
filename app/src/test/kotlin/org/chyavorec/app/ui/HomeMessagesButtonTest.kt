package org.chyavorec.app.ui

import android.app.Application
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
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
import org.chyavorec.app.R
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
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals

/**
 * Бутонът горе на „Начало“ е само за съобщенията от читалището: етикет „Съобщения (3 непрочетени)“
 * и отваря „Съобщения“ (звънчето е само за настройките на известията — „Моето“, „Настройки“).
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class HomeMessagesButtonTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    /** Уникални за теста — прочетените (DataStore) се споделят между тестовете в процеса. */
    private val run = UUID.randomUUID().toString().take(8)

    @Before fun start() {
        val messages = (1..3).joinToString(",", "[", "]") { i ->
            """{"id":"m$i-$run","title":"Съобщение $i","body":"Текст $i","audience":"all","createdAt":"2026-09-2${i}T08:00:00Z"}"""
        }
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/data/app-messages.json" -> MockResponse().setBody(messages)
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    @Test fun unreadCountIsInTheLabelAndOpensMessages() {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        // Опресняването при старт е веднъж на процес (MessagesOnLaunch) — тук изрично.
        runBlocking { container.messages.refresh(force = true) }
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
            }
        }
        val button = hasContentDescription("Съобщения (3 непрочетени)") and hasClickAction()
        compose.waitUntil(15_000) { compose.onAllNodes(button).fetchSemanticsNodes().isNotEmpty() }
        compose.onNode(button).performClick()
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Съобщение 2")).fetchSemanticsNodes().isNotEmpty() }
    }

    @Test fun labelForms() {
        val res = ApplicationProvider.getApplicationContext<Application>().resources
        assertEquals("Съобщения (1 непрочетено)", res.getQuantityString(R.plurals.messages_unread, 1, 1))
        assertEquals("Съобщения (3 непрочетени)", res.getQuantityString(R.plurals.messages_unread, 3, 3))
    }
}
