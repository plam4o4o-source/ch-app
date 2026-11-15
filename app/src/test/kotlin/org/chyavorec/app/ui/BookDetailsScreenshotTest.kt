package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
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
import kotlin.test.assertTrue

/**
 * Страницата на книгата на тесен (320 dp) и обикновен (411 dp) телефон: на тесния „Искам да
 * прочета“ е под корицата, на цялата ширина (до нея се реже), на обикновения — до корицата;
 * редовете с данните са с един ритъм. Снимките — до тези на ScreenshotTest.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1400dp-xhdpi")
class BookDetailsScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(mode: ThemeMode = ThemeMode.LIGHT) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        // Книга с подзаглавие, издател, ключови думи и други екземпляри — всички редове на данните.
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(
                    container, AppSettings(onboardingDone = true, introShown = true),
                    MutableStateFlow("$base/elektronen-katalog#zapis=302"), showIntro = false,
                )
            }
        }
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Сигнатура")).fetchSemanticsNodes().isNotEmpty() }
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Бутонът „Искам да прочета“ — (лява, дясна) граница в dp. */
    private fun wantToReadBounds(): Pair<Float, Float> {
        val node = compose.onNode(hasText("Искам да прочета") and hasClickAction()).fetchSemanticsNode()
        val density = node.layoutInfo.density.density
        return node.boundsInRoot.left / density to node.boundsInRoot.right / density
    }

    @Config(qualifiers = "bg-w320dp-h1400dp-xhdpi")
    @Test fun narrowPhoneStacksWantToReadUnderTheCover() {
        launch()
        shot("w24-book-320")
        val (left, right) = wantToReadBounds()
        val width = compose.onRoot().fetchSemanticsNode().let { it.boundsInRoot.width / it.layoutInfo.density.density }
        // На цялата ширина (с полетата 20 dp), не до корицата — и не излиза вдясно.
        assertTrue(left <= 21f && right >= width - 21f && right <= width, "„Искам да прочета“: $left..$right в $width dp")
    }

    @Test fun regularPhoneKeepsWantToReadBesideTheCover() {
        launch()
        shot("w24-book-411")
        val (left, _) = wantToReadBounds()
        // До корицата (124 dp + полета).
        assertTrue(left > 150f, "„Искам да прочета“ започва от $left dp")
    }

    @Config(qualifiers = "bg-w320dp-h1400dp-xhdpi")
    @Test fun narrowPhoneDark() {
        launch(ThemeMode.DARK)
        shot("w24-book-320-dark")
    }
}
