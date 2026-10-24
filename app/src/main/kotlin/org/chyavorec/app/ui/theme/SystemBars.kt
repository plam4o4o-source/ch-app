package org.chyavorec.app.ui.theme

import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Показване „от край до край“ без оттеглените в Android 15 API.
 *
 * `enableEdgeToEdge()` от androidx.activity за съвместимост вика `Window.setStatusBarColor`,
 * `setNavigationBarColor` и `LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES` (Google Play ги отчита,
 * макар на Android 15+ да не се изпълняват). Тук същото се постига така:
 * - Android 15+: прозорецът и без това е от край до край — нищо не се вика;
 * - по-старите: съдържанието се рисува под лентите ([setUp]); прозрачните ленти, полупрозрачният
 *   фон зад 3-те бутона и изрязването за камерата идват от темата (`values-v27/v29/v30`);
 * - светли или тъмни икони в лентите — от [SystemBarsAppearance] според темата на приложението
 *   (не системната) и екрани с постоянен тъмен или светъл фон ([SystemBarIcons]).
 */
object EdgeToEdge {
    fun setUp(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}

/** Заявка на екран за цвета на иконите: `true` — фонът под лентата е тъмен (светли икони). */
private class BarRequest(val darkStatus: Boolean?, val darkNavigation: Boolean?)

private class SystemBarsState {
    val requests = mutableStateListOf<BarRequest>()
}

private val LocalSystemBars = staticCompositionLocalOf<SystemBarsState?> { null }

/**
 * Иконите в системните ленти следват темата на приложението ([LocalExtendedColors] — и при
 * „Тъмна“ тема в светла система), освен ако текущ екран не е заявил друго с [SystemBarIcons].
 */
@Composable
fun SystemBarsAppearance(content: @Composable () -> Unit) {
    val state = remember { SystemBarsState() }
    val view = LocalView.current
    val dark = LocalExtendedColors.current.isDark
    val top = state.requests.lastOrNull()
    val darkStatus = top?.darkStatus ?: dark
    val darkNavigation = top?.darkNavigation ?: dark
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkStatus
                isAppearanceLightNavigationBars = !darkNavigation
            }
        }
    }
    CompositionLocalProvider(LocalSystemBars provides state, content = content)
}

/**
 * Екран с фон, който не следва темата (въведението, картата на цял екран, скенерът): докато е
 * показан, иконите в лентите са за [darkStatusBackground] / [darkNavigationBackground]
 * (`null` — по темата). При излизане се връща предишното.
 */
@Composable
fun SystemBarIcons(darkStatusBackground: Boolean? = null, darkNavigationBackground: Boolean? = null) {
    val state = LocalSystemBars.current ?: return
    DisposableEffect(state, darkStatusBackground, darkNavigationBackground) {
        val request = BarRequest(darkStatusBackground, darkNavigationBackground)
        state.requests.add(request)
        onDispose { state.requests.remove(request) }
    }
}
