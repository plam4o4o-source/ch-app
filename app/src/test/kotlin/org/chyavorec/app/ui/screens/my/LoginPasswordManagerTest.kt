package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.os.Looper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.onAllNodesWithText
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
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.security.SavedLogin
import org.chyavorec.app.di.AppContainer
import androidx.compose.runtime.CompositionLocalProvider
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.chyavorec.data.repository.AuthState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.time.Instant
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Мениджърът на пароли на екрана за вход (с подменен [FakeSystemPasswords] вместо системния
 * прозорец): при отваряне се пита веднъж — само ако от приложението е запазван вход; след
 * успешен вход се предлага запазване, а запазеният вход попълва полетата и се изпраща.
 * „Запомни ме“, грешен ПИН и заключването — в [LoginSaveOfferTest].
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class LoginPasswordManagerTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val logins = CopyOnWriteArrayList<String>()
    private val paths = CopyOnWriteArrayList<String>()

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
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?').also { paths += it.orEmpty() }) {
                "/api/v1/capabilities" -> MockResponse().setBody(
                    """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"holds":false,"renew":false,"passwordReset":false,"accountDeletion":false,"push":false,"availability":false}""",
                )
                "/api/v1/auth/login" -> {
                    val body = request.body.readUtf8()
                    logins += body
                    if ("\"1234\"" in body) MockResponse().setBody("""{"accessToken":"AT","refreshToken":"RT","expiresIn":2592000,"readerId":"R1"}""")
                    else MockResponse().setResponseCode(401)
                }
                // След входа — профилът и заеманията (иначе 404 би изглеждал като изтрит профил).
                "/api/v1/me" -> MockResponse().setBody("""{"readerId":"R1","cardNumber":"1024-5678","fullName":"Мария Петрова"}""")
                "/api/v1/me/loans" -> MockResponse().setBody("""{"loans":[]}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        // С връзка (иначе приложението е „без интернет“ и входът чака мрежата).
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @After fun stop() = server.shutdown()

    /** [savedBefore] — от приложението вече е запазван вход в мениджъра на пароли. */
    private fun launch(passwords: FakeSystemPasswords, savedBefore: Boolean = true): AppContainer {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "https://chyavorec.org/api",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), client, passwords,
        )
        runBlocking { container.settings.setPasswordManagerUsed(savedBefore) }
        // Само екранът за вход (без навигацията и останалите екрани с техните заявки).
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalAppContainer provides container) {
                    LoginScreen(onBack = {}, onLoggedIn = {}, onAddCard = {})
                }
            }
        }
        return container
    }

    private fun waitForForm() = compose.waitUntil(15_000) {
        compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size >= 2 &&
            compose.onAllNodesWithText("Читателски номер").fetchSemanticsNodes().isNotEmpty()
    }

    /**
     * Изчакване, при което се изпълнява и опашката на главната нишка (Robolectric, PAUSED):
     * продълженията на viewModelScope (Dispatchers.Main) след мрежата/диска са там.
     */
    private fun pumpUntil(timeoutMs: Long = 15_000, condition: () -> Boolean) = compose.waitUntil(timeoutMs) {
        shadowOf(Looper.getMainLooper()).idle()
        condition()
    }

    @Test fun openingTheFormAsksThePasswordManagerOnce() {
        val passwords = FakeSystemPasswords()
        launch(passwords)
        waitForForm()
        pumpUntil(5_000) { passwords.asked == 1 }
        compose.waitForIdle()
        // Без запазен вход — нищо не се попълва и не се изпраща; пак се пита само веднъж.
        assertEquals(1, passwords.asked)
        assertEquals("", compose.onAllNodes(hasSetTextAction())[0].fetchSemanticsNode().config[SemanticsProperties.EditableText].text)
        assertTrue(logins.isEmpty())
    }

    /** Без запазван от приложението вход — без системния прозорец (под Android 14 — невидима activity). */
    @Test fun withoutASavedLoginThePasswordManagerIsNotAsked() {
        val passwords = FakeSystemPasswords(saved = SavedLogin("1024-5678", "1234"))
        launch(passwords, savedBefore = false)
        waitForForm()
        pumpUntil(2_000) { true }
        compose.waitForIdle()
        assertEquals(0, passwords.asked)
        assertTrue(logins.isEmpty())
    }

    @Test fun successfulLoginOffersToSaveCardAndPin() {
        val passwords = FakeSystemPasswords()
        val container = launch(passwords, savedBefore = false)
        waitForForm()
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("1024-5678 ")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("1234")
        compose.onAllNodes(hasSetTextAction())[1].performImeAction()
        pumpUntil { passwords.offered.isNotEmpty() }
        assertIs<AuthState.SignedIn>(container.authRepository.state.value)
        val offered = passwords.offered.single()
        assertEquals("1024-5678", offered.cardNumber)
        assertEquals("1234", offered.pin)
        // Запазено — при следващото отваряне мениджърът ще бъде попитан.
        pumpUntil { runBlocking { container.settings.passwordManagerUsed() } }
    }

    /** „Запомни ме“: целият ред е една отметка с име (TalkBack), докосване и по текста. */
    @Test fun rememberMeIsOneLabelledCheckbox() {
        launch(FakeSystemPasswords(), savedBefore = false)
        waitForForm()
        val row = compose.onNode(hasText("Запомни ме", substring = true) and isToggleable())
        row.assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
        row.assertIsOn()
        row.performClick()
        row.assertIsOff()
        // Отделна безименна отметка няма.
        assertEquals(1, compose.onAllNodes(isToggleable()).fetchSemanticsNodes().size)
    }

    @Test fun savedLoginFillsTheFormAndSignsIn() {
        val passwords = FakeSystemPasswords(saved = SavedLogin("1024-5678", "1234"))
        val container = launch(passwords)
        pumpUntil { container.authRepository.state.value is AuthState.SignedIn }
        assertEquals(1, passwords.asked)
        val body = logins.single()
        assertTrue("\"1024-5678\"" in body && "\"1234\"" in body, body.replace("1234", "****"))
        assertEquals("1024-5678", compose.onAllNodes(hasSetTextAction())[0].fetchSemanticsNode().config[SemanticsProperties.EditableText].text)
        pumpUntil(2_000) { true }
        // Входът е от мениджъра — не се предлага да се запази отново.
        assertTrue(passwords.offered.isEmpty())
    }
}
