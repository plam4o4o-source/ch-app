package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Looper
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.chyavorec.app.AppConfig
import org.chyavorec.app.FakeSystemPasswords
import org.chyavorec.app.R
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.DataStoreThrottleStore
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.chyavorec.data.repository.ThrottleState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Екранът за вход след грешни опити: от първия грешен ПИН — колко опита остават (жив регион,
 * с правилното множествено число), при блокиране — обратно броене „1:00“ (м:сс); „Забравен ПИН?“ —
 * бутони за обаждане/имейл от контактите (сайтът без телефон — само имейл), а темата на
 * имейла е с номера на картата, без ПИН-а.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class LoginAttemptsTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()

    // Читателският вход се включва само за https:// адрес — тестовият сървър е с локален сертификат.
    private val cert = HeldCertificate.Builder().addSubjectAlternativeName("localhost").build()
    private val serverTls = HandshakeCertificates.Builder().heldCertificate(cert).build()
    private val clientTls = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
    private val client = OkHttpClient.Builder()
        .sslSocketFactory(clientTls.sslSocketFactory(), clientTls.trustManager)
        .addInterceptor { chain ->
            val url = chain.request().url
            val to = if (url.host == "chyavorec.org") url.newBuilder().host("localhost").port(server.port).build() else url
            chain.proceed(chain.request().newBuilder().url(to).build())
        }
        .build()

    @Before fun start() {
        // Броячите на опитите се пазят на диска (общ DataStore за процеса) — от нула.
        runBlocking { DataStoreThrottleStore(app).save(ThrottleState()) }
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/api/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"holds":false,"renew":false,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                "/api/v1/auth/login" -> MockResponse().setResponseCode(401)
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        val cm = app.getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @After fun stop() {
        server.shutdown()
        runBlocking { DataStoreThrottleStore(app).save(ThrottleState()) }
    }

    private fun launch(before: (AppContainer) -> Unit = {}): AppContainer {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "https://chyavorec.org/api",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(app, config, FixedClock(Instant.parse("2026-11-01T10:00:00Z")), XorTestCipher(), client, FakeSystemPasswords())
        runBlocking { container.settings.setPasswordManagerUsed(false) }
        before(container)
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalAppContainer provides container) {
                    LoginScreen(onBack = {}, onLoggedIn = {}, onAddCard = {})
                }
            }
        }
        compose.waitUntil(15_000) { compose.onAllNodesWithText(app.getString(R.string.login_card)).fetchSemanticsNodes().isNotEmpty() }
        return container
    }

    private fun pumpUntil(condition: () -> Boolean) = compose.waitUntil(15_000) {
        shadowOf(Looper.getMainLooper()).idle()
        condition()
    }

    private fun shown(text: String) = compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty()

    private fun wrongPin() {
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("9999")
        compose.onAllNodes(hasSetTextAction())[1].performImeAction()
    }

    private val polite = SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite)

    @Test fun everyWrongPinSaysHowManyAttemptsAreLeft() {
        launch()
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("0042")
        wrongPin()
        val four = app.resources.getQuantityString(R.plurals.login_attempts_left, 4, 4)
        pumpUntil { shown(four) }
        assertEquals("Остават 4 опита преди временно блокиране.", four)
        assertTrue(shown(app.getString(R.string.login_wrong)))
        // Предупреждението е жив регион — TalkBack го чете при всеки нов опит.
        compose.onNode(hasText(four) and polite).assertExists()

        wrongPin()
        pumpUntil { shown(app.resources.getQuantityString(R.plurals.login_attempts_left, 3, 3)) }
        // Единствено число: „Остава 1 опит“.
        assertEquals("Остава 1 опит преди временно блокиране.", app.resources.getQuantityString(R.plurals.login_attempts_left, 1, 1))
    }

    @Test fun lockoutShowsACountdown() {
        launch { c -> repeat(5) { c.authRepository.throttle.onFailure() } }
        val status = app.getString(R.string.login_locked_status)
        pumpUntil { shown(status) }
        compose.onNode(hasText(status) and polite).assertExists()
        // Първото блокиране е минута (броячът закръгля нагоре — „1:01“ или „1:00“).
        val countdown = app.getString(R.string.login_locked_countdown, "1:0")
        assertTrue(shown(countdown.substringBefore("1:0") + "1:0"))
        // Блокирано — без „остават N опита“.
        assertFalse(shown(app.resources.getQuantityString(R.plurals.login_attempts_left, 5, 5)))
    }

    @Test fun forgotPinOffersTheLibraryEmailWithTheCardNumberInTheSubject() {
        launch()
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("0042")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("5555")
        compose.onNode(hasText(app.getString(R.string.login_forgot)) and hasClickAction()).performClick()
        val email = app.getString(R.string.contacts_email) + " · chitalishte_yavorec@abv.bg"
        pumpUntil { shown(email) }
        // Сайтът (и резервните контакти) — без телефон: само имейлът.
        assertFalse(shown(app.getString(R.string.contacts_call)))
        compose.onNode(hasText(email) and hasClickAction()).performClick()
        compose.waitForIdle()
        val intent = assertNotNull(shadowOf(app).nextStartedActivity)
        assertEquals(Intent.ACTION_SENDTO, intent.action)
        assertEquals("Забравен ПИН — читателска карта 0042", intent.getStringExtra(Intent.EXTRA_SUBJECT))
        assertNull(intent.getStringExtra(Intent.EXTRA_TEXT))
        assertFalse(intent.extras.toString().contains("5555"))
    }

    @Test fun countdownFormat() {
        assertEquals("4:59", lockCountdown(299))
        assertEquals("0:05", lockCountdown(5))
        assertEquals("1:02:05", lockCountdown(3725))
        assertEquals("0:00", lockCountdown(-3))
    }
}
