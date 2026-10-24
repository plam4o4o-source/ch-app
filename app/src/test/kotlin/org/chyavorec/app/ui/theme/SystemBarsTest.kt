package org.chyavorec.app.ui.theme

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.view.WindowCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Иконите в системните ленти следват темата на приложението и екраните с постоянен фон. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SystemBarsTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private val controller get() = compose.activity.window.let { WindowCompat.getInsetsController(it, it.decorView) }

    @Test fun iconsFollowAppThemeAndScreenRequests() {
        val mode = mutableStateOf(ThemeMode.LIGHT)
        val whiteScreen = mutableStateOf(false)
        val darkScreen = mutableStateOf(false)
        compose.setContent {
            ChitalishteTheme(mode = mode.value) {
                SystemBarsAppearance {
                    if (whiteScreen.value) SystemBarIcons(darkStatusBackground = false, darkNavigationBackground = false)
                    if (darkScreen.value) SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
                }
            }
        }
        compose.waitForIdle()
        assertTrue(controller.isAppearanceLightStatusBars)
        assertTrue(controller.isAppearanceLightNavigationBars)

        // „Тъмна“ тема в приложението — светли икони, независимо от системата.
        mode.value = ThemeMode.DARK
        compose.waitForIdle()
        assertFalse(controller.isAppearanceLightStatusBars)
        assertFalse(controller.isAppearanceLightNavigationBars)

        // Картата на цял екран (бял фон) — тъмни икони и при тъмна тема; при излизане — пак по темата.
        whiteScreen.value = true
        compose.waitForIdle()
        assertTrue(controller.isAppearanceLightStatusBars)
        // Най-новият екран печели (въведение над карта и т.н.).
        darkScreen.value = true
        compose.waitForIdle()
        assertFalse(controller.isAppearanceLightStatusBars)
        darkScreen.value = false
        compose.waitForIdle()
        assertTrue(controller.isAppearanceLightStatusBars)
        whiteScreen.value = false
        compose.waitForIdle()
        assertFalse(controller.isAppearanceLightStatusBars)
    }
}
