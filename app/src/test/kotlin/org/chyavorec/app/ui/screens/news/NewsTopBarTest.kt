package org.chyavorec.app.ui.screens.news

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.math.abs

/**
 * „Новини и събития“: лентата със заглавието се скрива при превъртане надолу, а повторното
 * докосване на раздела (към началото на списъка) я показва отново — превъртането до
 * началото не минава през nestedScroll и само по себе си не я връща.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class NewsTopBarTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

    /** Достатъчно новини, за да има накъде да се превърти. */
    private val news = (1..25).joinToString(",", "[", "]") { i ->
        """{"id":"n-$i","title":"Новина номер $i","date":"2026-08-${(30 - i).toString().padStart(2, '0')}",
            "category":"Събитие","excerpt":"Кратко описание на новина $i.","content":"<p>Текст $i.</p>"}"""
    }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(news)
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

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
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow("news"), showIntro = false)
            }
        }
    }

    /** Разделът „Новини“ в долната лента (не сегментът със същото име). */
    private val newsTab = hasText("Новини") and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab)

    /** Горният ръб на сегментите под лентата — расте и намалява с височината ѝ. */
    private fun segmentsTop(): Float =
        compose.onAllNodes(hasText("Календар") and isSelectable()).onFirst().fetchSemanticsNode().boundsInRoot.top

    @Test fun reselectingTheTabShowsTheCollapsedTitleBarAgain() {
        launch()
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Новина номер 1")).fetchSemanticsNodes().isNotEmpty() }
        compose.waitForIdle()
        val expanded = segmentsTop()

        // Превъртане надолу — лентата се скрива (enterAlways).
        compose.onRoot().performTouchInput {
            swipe(Offset(centerX, height * 0.75f), Offset(centerX, height * 0.3f), durationMillis = 400)
        }
        compose.waitUntil(5_000) { segmentsTop() < expanded - 40f }

        // Повторно докосване на „Новини“ — списъкът е в началото, а лентата — отново видима.
        compose.onAllNodes(newsTab).onFirst().performClick()
        compose.waitUntil(5_000) { abs(segmentsTop() - expanded) < 1f }
        compose.waitUntil(5_000) { compose.onAllNodes(hasText("Новина номер 1")).fetchSemanticsNodes().isNotEmpty() }
    }
}
