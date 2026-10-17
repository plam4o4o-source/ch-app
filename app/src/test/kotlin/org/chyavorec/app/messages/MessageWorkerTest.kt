package org.chyavorec.app.messages

import android.Manifest
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Известията за съобщения от сайта: две едновременни проверки (общата задача и честата)
 * дават едно известие; „известено“ се помни, затова следваща проверка не го повтаря.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class MessageWorkerTest {
    private val server = MockWebServer()
    /** Уникално за теста — настройките (DataStore) може да се споделят между тестовете. */
    private val messageId = "w-" + UUID.randomUUID()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)

    @Before fun start() {
        shadowOf(app).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/data/app-messages.json" -> MockResponse().setBody(
                    """[{"id":"$messageId","title":"Ново в читалището","body":"Покана за събрание",
                        "audience":"all","createdAt":"2026-09-25T08:00:00Z"}]""",
                )
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun container(): AppContainer {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base,
            catalogUrls = emptyList(),
            inflibApiUrl = "",
            environment = "test",
            useMockData = false,
            versionName = "test",
            versionCode = 1,
        )
        return AppContainer(app, config, FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient())
    }

    private fun ours() = nm.activeNotifications.filter { it.id == NotificationIds.message(messageId) }

    @Test fun concurrentChecksNotifyOnceAndRemember() = runTest {
        val c = container()
        c.settings.setNotifyMessages(true)
        c.settings.setMessagesInitialized()

        val results = listOf(
            async(Dispatchers.Default) { MessageWorker.refreshAndNotify(app, c, includePersonal = false) },
            async(Dispatchers.Default) { MessageWorker.refreshAndNotify(app, c, includePersonal = false, force = false) },
        ).awaitAll()
        results.forEach { assertNotNull(it) }

        val shown = ours().single()
        // Повторно показване със същото ID не звъни втори път.
        assertTrue(shown.notification.flags and Notification.FLAG_ONLY_ALERT_ONCE != 0)
        assertTrue(messageId in c.settings.notifiedMessageIds())

        // Потребителят е махнал известието — следващата проверка не го показва отново.
        nm.cancelAll()
        MessageWorker.refreshAndNotify(app, c, includePersonal = false)
        assertEquals(0, ours().size)
    }
}
