package org.chyavorec.app.ui.screens.site

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals

/** „Документи“: бутонът „Отвори с приложение за PDF“ на всеки ред казва кой документ отваря. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class DocumentsA11yTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val titles = listOf("Устав на читалището", "Годишен отчет 2025", "План за дейността 2026")

    @Before fun start() {
        val files = titles.mapIndexed { i, t ->
            """{"id":"d$i","title":"$t","date":"2026-0${i + 1}-15","category":"Документи","url":"https://chyavorec.org/files/doc-$i.pdf"}"""
        }.joinToString(",", "[", "]")
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/files.json" -> MockResponse().setBody(files)
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    @Test fun pdfButtonsNameTheirDocument() {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalAppContainer provides container) { DocumentsScreen(onBack = {}, openLink = {}) }
            }
        }
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(titles[0])).fetchSemanticsNodes().isNotEmpty() }
        compose.waitForIdle()
        titles.forEach { t ->
            compose.onNode(hasContentDescription("Отвори с приложение за PDF: $t")).assertExists()
        }
        // Нито един бутон само с общото „Отвори с приложение за PDF“ — и никакви повтарящи се имена.
        val labels = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription))
            .fetchSemanticsNodes().flatMap { it.config[SemanticsProperties.ContentDescription] }
            .filter { it.startsWith("Отвори с приложение за PDF") }
        assertEquals(titles.size, labels.size, "$labels")
        assertEquals(labels.size, labels.toSet().size, "$labels")
    }
}
