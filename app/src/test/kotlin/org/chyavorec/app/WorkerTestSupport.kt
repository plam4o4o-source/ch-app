package org.chyavorec.app

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * Тестовото приложение за фоновите задачи (`@Config(application = WorkerTestApp::class)`):
 * задачите взимат контейнера през [AppContainerOwner], а тестът подава свой — към [TestBackend].
 */
class WorkerTestApp : Application(), AppContainerOwner {
    lateinit var container: AppContainer
    override val appContainer: AppContainer get() = container
}

/** Keystore „не отговаря“ (временна грешка), докато [broken]; иначе — тестовото шифроване. */
class FlakyKeystoreCipher(@Volatile var broken: Boolean = true) : BytesCipher {
    private val xor = XorTestCipher()
    override fun encrypt(plain: ByteArray) = xor.encrypt(plain)
    override fun decrypt(data: ByteArray): ByteArray {
        if (broken) throw java.security.ProviderException("keystore busy")
        return xor.decrypt(data)
    }
}

/**
 * Сайтът и мостът (InvLib API) на един HTTPS MockWebServer — читателският вход се включва
 * само за https:// адрес, затова сървърът е с локален сертификат, на който доверява само
 * [client]. Отговорите са по път (без заявката), всички заявки се записват.
 */
class TestBackend {
    val server = MockWebServer()
    private val cert = HeldCertificate.Builder().addSubjectAlternativeName("localhost").build()
    private val serverTls = HandshakeCertificates.Builder().heldCertificate(cert).build()
    private val clientTls = HandshakeCertificates.Builder().addTrustedCertificate(cert.certificate).build()
    val client: OkHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(clientTls.sslSocketFactory(), clientTls.trustManager)
        .callTimeout(10, TimeUnit.SECONDS)
        .build()

    val routes = ConcurrentHashMap<String, (RecordedRequest) -> MockResponse>()
    val requests = CopyOnWriteArrayList<RecordedRequest>()

    fun start(): TestBackend {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests += request
                return routes[request.path.orEmpty().substringBefore('?')]?.invoke(request) ?: MockResponse().setResponseCode(404)
            }
        }
        server.useHttps(serverTls.sslSocketFactory(), false)
        server.start()
        return this
    }

    fun shutdown() = server.shutdown()

    val base: String get() = server.url("/").newBuilder().host("localhost").build().toString().trimEnd('/')

    /** Брой заявки към [path] (без заявката след „?“). */
    fun count(path: String) = requests.count { it.path.orEmpty().substringBefore('?') == path }

    fun json(path: String, body: String, code: Int = 200) {
        routes[path] = { MockResponse().setResponseCode(code).setBody(body) }
    }

    fun container(app: Application, clock: AppClock, cipher: BytesCipher = XorTestCipher()): AppContainer {
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = base + API,
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        return AppContainer(app, config, clock, cipher, client)
    }

    /**
     * Нов процес със запазена („запомни ме“) сесия, която Keystore-ът временно не чете
     * ([FlakyKeystoreCipher.broken]). Входът е с верния ПИН — [reader] трябва да е извикан.
     */
    suspend fun unreadableSession(app: WorkerTestApp, clock: AppClock): FlakyKeystoreCipher {
        val cipher = FlakyKeystoreCipher(broken = false)
        app.container = container(app, clock, cipher)
        check(app.container.authRepository.login("0042", "1234".toCharArray(), remember = true) is Outcome.Success)
        cipher.broken = true
        app.container = container(app, clock, cipher)
        return cipher
    }

    /** Мост с вход, заемания и лични съобщения; верният ПИН е „1234“. */
    fun reader(readerId: String = "R1", messages: Boolean = true) {
        json(
            "$API/v1/capabilities",
            """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"renew":true,"history":false,"messages":$messages}""",
        )
        routes["$API/v1/auth/login"] = { request ->
            val body = request.body.clone().readUtf8()
            if ("\"1234\"" in body) {
                MockResponse().setBody("""{"accessToken":"AT-$readerId","refreshToken":"RT-$readerId","expiresIn":2592000,"readerId":"$readerId"}""")
            } else {
                MockResponse().setResponseCode(401).setBody("""{"error":"invalid_credentials"}""")
            }
        }
        json("$API/v1/auth/logout", "{}")
    }

    fun loans(vararg loans: String) = json("$API/v1/me/loans", """{"loans":[${loans.joinToString(",")}]}""")

    companion object {
        const val API = "/api/invlib/yavorec"

        /** Разрешени известия и мрежа „с интернет“ (иначе задачите не правят нищо). */
        fun allowNotificationsAndNetwork(app: Application) {
            shadowOf(app).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
            val cm = app.getSystemService(ConnectivityManager::class.java)
            val caps = ShadowNetworkCapabilities.newInstance()
            shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
            app.getSystemService(NotificationManager::class.java).cancelAll()
        }
    }
}
