package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
 * Мениджърът на пароли на екрана за вход (с подменен [FakeSystemPasswords] вместо системния
 * прозорец): при отваряне се пита веднъж, а запазеният вход попълва полетата и се изпраща.
 * Предложението за запазване, „Запомни ме“, грешен ПИН и заключването — в [LoginSaveOfferTest].
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

    @Test fun openingTheFormAsksThePasswordManagerOnce() {
        val passwords = FakeSystemPasswords()
        launch(passwords)
        waitForForm()
        compose.waitUntil(5_000) { passwords.asked == 1 }
        compose.waitForIdle()
        // Без запазен вход — нищо не се попълва и не се изпраща; пак се пита само веднъж.
        assertEquals(1, passwords.asked)
        assertEquals("", compose.onAllNodes(hasSetTextAction())[0].fetchSemanticsNode().config[SemanticsProperties.EditableText].text)
        assertTrue(logins.isEmpty())
    }

    @Test fun savedLoginFillsTheFormAndSubmitsIt() {
        val passwords = FakeSystemPasswords(saved = SavedLogin("1024-5678", "1234"))
        launch(passwords)
        compose.waitUntil(15_000) { logins.isNotEmpty() }
        assertEquals(1, passwords.asked)
        val body = logins.single()
        assertTrue("\"1024-5678\"" in body && "\"1234\"" in body, body.replace("1234", "****"))
        assertEquals("1024-5678", compose.onAllNodes(hasSetTextAction())[0].fetchSemanticsNode().config[SemanticsProperties.EditableText].text)
        // Предложението за запазване след успешен вход (и че не се прави за вход от мениджъра) —
        // в LoginSaveOfferTest (ViewModel-ът; тук мрежовите продължения в Compose теста не са надеждни).
        assertTrue(passwords.offered.isEmpty())
    }
}
