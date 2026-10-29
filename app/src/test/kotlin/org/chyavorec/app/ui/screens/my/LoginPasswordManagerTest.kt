package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import kotlin.test.assertTrue

/**
 * Мениджърът на пароли на входа (с подменен [FakeSystemPasswords] вместо системния прозорец):
 * след успешен вход се предлага запазване, а запазеният вход попълва полетата и се изпраща.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class LoginPasswordManagerTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val logins = CopyOnWriteArrayList<String>()
    private val paths = CopyOnWriteArrayList<String>()
    @Volatile private var loggedIn = false

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

    private fun launch(passwords: FakeSystemPasswords): AppContainer {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "https://chyavorec.org/api",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), client, passwords,
        )
        // Само екранът за вход (без навигацията и останалите екрани с техните заявки).
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalAppContainer provides container) {
                    LoginScreen(onBack = {}, onLoggedIn = { loggedIn = true }, onAddCard = {})
                }
            }
        }
        return container
    }

    private fun waitForForm() = compose.waitUntil(15_000) {
        compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().size >= 2 &&
            compose.onAllNodesWithText("Читателски номер").fetchSemanticsNodes().isNotEmpty()
    }

    private fun typeAndSubmit(card: String, pin: String) {
        compose.onAllNodes(hasSetTextAction())[0].performTextInput(card)
        compose.onAllNodes(hasSetTextAction())[1].performTextInput(pin)
        compose.onAllNodes(hasSetTextAction())[1].performImeAction()
    }

    private fun waitSignedIn(container: AppContainer, passwords: FakeSystemPasswords) = runCatching {
        compose.waitUntil(15_000) { container.authRepository.state.value is AuthState.SignedIn }
    }.getOrElse {
        // Диагностика: състоянието на входа и заявките (без ПИН-а).
        throw AssertionError("state=${container.authRepository.state.value}, logins=${logins.map { b -> b.replace(Regex("\\d{4}\"(?=[,}])"), "****\"") }}, paths=$paths, asked=${passwords.asked}\n" +
            compose.onRoot(useUnmergedTree = true).printToString(), it)
    }

    @Test fun successfulLoginOffersToSaveCardAndPin() {
        val passwords = FakeSystemPasswords()
        val container = launch(passwords)
        waitForForm()
        compose.waitUntil(5_000) { passwords.asked == 1 }
        typeAndSubmit("1024-5678 ", "1234")
        waitSignedIn(container, passwords)
        compose.waitUntil(5_000) { passwords.offered.isNotEmpty() && loggedIn }
        val offered = passwords.offered.single()
        assertEquals("1024-5678", offered.cardNumber)
        assertEquals("1234", offered.pin)
    }

    @Test fun withoutRememberMeSavingIsNotOffered() {
        val passwords = FakeSystemPasswords()
        val container = launch(passwords)
        waitForForm()
        compose.onNode(isToggleable()).performClick()
        typeAndSubmit("1024-5678", "1234")
        waitSignedIn(container, passwords)
        compose.waitForIdle()
        assertTrue(passwords.offered.isEmpty(), "save offered without „Запомни ме“")
    }

    @Test fun failedLoginOffersNothing() {
        val passwords = FakeSystemPasswords()
        launch(passwords)
        waitForForm()
        typeAndSubmit("1024-5678", "9999")
        compose.waitUntil(15_000) { compose.onAllNodes(hasText("Грешен читателски номер или ПИН.")).fetchSemanticsNodes().isNotEmpty() }
        assertEquals(1, logins.size)
        assertTrue(passwords.offered.isEmpty())
    }

    @Test fun savedLoginFillsAndSignsInWithOneTap() {
        val passwords = FakeSystemPasswords(saved = SavedLogin("1024-5678", "1234"))
        val container = launch(passwords)
        waitSignedIn(container, passwords)
        assertEquals(1, passwords.asked)
        assertTrue(logins.single().contains("\"1024-5678\""), logins.single().replace("1234", "****"))
        compose.waitForIdle()
        // Входът е от мениджъра — не се предлага да се запази отново.
        assertTrue(passwords.offered.isEmpty())
    }

    // ВРЕМЕННО: диагностика — стъпките на входа поотделно, извън главната нишка.
    @Test fun diagDirectLogin() {
        val container = launch(FakeSystemPasswords())
        compose.waitForIdle()
        val report = kotlinx.coroutines.runBlocking(kotlinx.coroutines.Dispatchers.Default) {
            suspend fun step(name: String, block: suspend () -> Any?): String {
                val t0 = System.nanoTime()
                val r = runCatching { kotlinx.coroutines.withTimeoutOrNull(4_000) { block() ?: "null" } }
                return "$name: ${r.getOrNull() ?: r.exceptionOrNull() ?: "TIMEOUT"} (${(System.nanoTime() - t0) / 1_000_000} ms)"
            }
            listOf(
                step("restoreThrottle") { container.authRepository.restoreThrottle() },
                step("readerCacheClear") { container.readerCache.clear() },
                step("caps") { container.authRepository.capabilities() },
                step("login") { container.authRepository.login("1024-5678", "1234".toCharArray(), remember = true) },
            ).joinToString("; ")
        }
        assertTrue(false, "report=$report state=${container.authRepository.state.value} paths=$paths")
    }
}
