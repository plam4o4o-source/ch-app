package org.chyavorec.app.ui.screens.scan

import android.app.Application
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
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
import org.chyavorec.app.FakeBarcodeScanner
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.data.scan.ScanOutcome
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Екранът „Сканиране“ със скенер без Google Play услуги ([FakeBarcodeScanner]): скенерът се
 * отваря сам при отваряне на екрана, разчетеният код показва листа с книгата, а след „Назад“
 * от скенера екранът остава с „Сканирай баркод“ и „Въведи номера на ръка“.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class ScanScreenTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

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

    private fun launch(scanner: FakeBarcodeScanner) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(), scannerOverride = scanner,
        )
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow("scan"), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    @Test fun scannedCodeOpensTheBookSheet() {
        val scanner = FakeBarcodeScanner(ScanOutcome.Code("399666"))
        launch(scanner)
        waitFor("Отвори книгата")
        assertEquals(1, scanner.calls)
    }

    @Test fun backFromTheScannerLeavesScanAgainAndManualEntry() {
        val scanner = FakeBarcodeScanner(ScanOutcome.Cancelled)
        launch(scanner)
        waitFor("Сканирай баркода на книгата")
        assertEquals(1, scanner.calls)
        // Без лист и без панела „на ръка“ — само изборът какво нататък.
        assertTrue(compose.onAllNodes(hasText("Отвори книгата")).fetchSemanticsNodes().isEmpty())
        assertTrue(compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isEmpty())
        compose.onAllNodes(hasText("Сканирай баркод") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(5_000) { scanner.calls == 2 }
        compose.onAllNodes(hasText("Въведи номера на ръка") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(5_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
    }
}
