package org.chyavorec.app.ui.screens.settings

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.FakeDeviceUnlock
import org.chyavorec.app.R
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * „Настройки › Поверителност › Искай отключване за личните екрани“: без заключване на екрана
 * не се включва (с обяснение), а включването и изключването минават през проверката.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class RequireUnlockSettingTest {
    @get:Rule val compose = createComposeRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private var container: AppContainer? = null

    /** Настройките (DataStore) са общи за тестовете в процеса — без включено заключване след теста. */
    @After fun reset() = runBlocking { container?.settings?.setRequireUnlock(false); Unit }

    private fun launch(unlock: FakeDeviceUnlock, on: Boolean): AppContainer {
        val config = AppConfig(
            siteBaseUrl = "http://localhost", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(app, config, FixedClock(Instant.parse("2026-11-01T10:00:00Z")), XorTestCipher(), OkHttpClient(), unlockOverride = unlock)
        this.container = container
        runBlocking { container.settings.setRequireUnlock(on) }
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalAppContainer provides container) { SettingsScreen(onBack = {}, navigate = {}) }
            }
        }
        return container
    }

    private fun item() = compose.onNode(hasText(app.getString(R.string.settings_require_unlock)) and hasClickAction()).performScrollTo()
    private fun requireUnlock(c: AppContainer) = runBlocking { c.settings.current().requireUnlock }

    @Test fun withoutAScreenLockItCannotBeTurnedOn() {
        val unlock = FakeDeviceUnlock(false, true)
        val c = launch(unlock, on = false)
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(app.getString(R.string.settings_require_unlock))).fetchSemanticsNodes().isNotEmpty() }
        item().assertIsNotEnabled()
        compose.onNode(hasText(app.getString(R.string.settings_require_unlock_unavailable))).performScrollTo()
        assertFalse(requireUnlock(c))
        assertEquals(0, unlock.calls)
    }

    @Test fun turningOnAsksFirstAndUnlocks() {
        val unlock = FakeDeviceUnlock(true, true)
        val c = launch(unlock, on = false)
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(app.getString(R.string.settings_require_unlock))).fetchSemanticsNodes().isNotEmpty() }
        item().assertIsEnabled().performClick()
        compose.waitUntil(10_000) { requireUnlock(c) }
        assertEquals(1, unlock.calls)
        assertTrue(c.personalUnlock.unlocked.value)
    }

    /** Изключване без успешна проверка — остава включено (иначе всеки би го изключил). */
    @Test fun turningOffNeedsTheUnlockToo() {
        val unlock = FakeDeviceUnlock(true, false)
        val c = launch(unlock, on = true)
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(app.getString(R.string.settings_require_unlock))).fetchSemanticsNodes().isNotEmpty() }
        item().performClick()
        compose.waitUntil(10_000) { unlock.calls == 1 }
        compose.waitForIdle()
        assertTrue(requireUnlock(c))
    }
}
