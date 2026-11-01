package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.FakeDeviceUnlock
import org.chyavorec.app.R
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
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
import org.robolectric.shadows.ShadowSystemClock
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * „Искай отключване за личните екрани“ (с [FakeDeviceUnlock] вместо системния прозорец):
 * картата (от плочката — връзка `my/card`) се показва едва след отключване, отказът води
 * назад към „Моето“, а отключването изтича след над минута във фонов режим.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class UnlockGateTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(unlock: FakeDeviceUnlock, required: Boolean = true): AppContainer {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            app, config, FixedClock(Instant.parse("2026-11-01T10:00:00Z")), XorTestCipher(), OkHttpClient(), unlockOverride = unlock,
        )
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                ChitalishteRoot(
                    container, AppSettings(onboardingDone = true, introShown = true, requireUnlock = required), MutableStateFlow("my/card"),
                    showIntro = false,
                )
            }
        }
        return container
    }

    private fun shown(text: String) = compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty()
    private fun waitFor(text: String) = compose.waitUntil(15_000) { shown(text) }

    /** Без заключване на личните екрани — картата направо, без проверка. */
    @Test fun offShowsTheCardWithoutAsking() {
        val unlock = FakeDeviceUnlock(true, true)
        launch(unlock, required = false)
        waitFor(app.getString(R.string.card_number_label))
        assertEquals(0, unlock.calls)
    }

    @Test fun onAsksAndSuccessShowsTheCard() {
        val unlock = FakeDeviceUnlock(true, true)
        val container = launch(unlock)
        waitFor(app.getString(R.string.card_number_label))
        assertEquals(1, unlock.calls)
        assertTrue(container.personalUnlock.unlocked.value)
        assertTrue(!shown(app.getString(R.string.unlock_locked_title)))
    }

    /** Отказ — обратно към „Моето“, без картата. */
    @Test fun cancelGoesBack() {
        val unlock = FakeDeviceUnlock(true, false)
        val container = launch(unlock)
        waitFor(app.getString(R.string.guest_title))
        compose.waitForIdle()
        assertEquals(1, unlock.calls)
        assertTrue(!container.personalUnlock.unlocked.value)
        assertTrue(!shown(app.getString(R.string.card_number_label)))
        assertTrue(!shown(app.getString(R.string.unlock_locked_title)))
    }

    /** Телефон без заключване на екрана — няма с какво да се провери: картата се показва. */
    @Test fun withoutAScreenLockTheCardIsShown() {
        val unlock = FakeDeviceUnlock(false, false)
        launch(unlock)
        waitFor(app.getString(R.string.card_number_label))
        assertEquals(0, unlock.calls)
    }

    @Test fun unlockExpiresAfterAMinuteInTheBackground() {
        val unlock = FakeDeviceUnlock(true, true)
        val container = launch(unlock)
        waitFor(app.getString(R.string.card_number_label))
        assertEquals(1, unlock.calls)
        val personal = container.personalUnlock
        // До минута във фонов режим — без нова проверка.
        compose.runOnIdle {
            personal.onBackground()
            ShadowSystemClock.advanceBy(Duration.ofSeconds(30))
            personal.onForeground()
        }
        compose.waitForIdle()
        assertTrue(personal.unlocked.value)
        assertEquals(1, unlock.calls)
        // Над минута — личният екран се заключва и пита отново.
        compose.runOnIdle {
            personal.onBackground()
            ShadowSystemClock.advanceBy(Duration.ofSeconds(61))
            personal.onForeground()
        }
        compose.waitUntil(15_000) { unlock.calls == 2 }
        waitFor(app.getString(R.string.card_number_label))
        assertTrue(personal.unlocked.value)
    }
}
