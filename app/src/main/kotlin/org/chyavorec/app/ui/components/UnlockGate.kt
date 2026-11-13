package org.chyavorec.app.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.first
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer

/** „Искай отключване за личните екрани“ (Настройки › Поверителност) — от вече прочетените настройки. */
val LocalRequireUnlock = staticCompositionLocalOf { false }

/**
 * Отворена ли е личната зона за лични данни върху ПУБЛИЧЕН екран (страница на книга и т.н.), който не
 * е зад [UnlockGate]: настройката е изключена, личното е отключено или телефонът няма заключване на
 * екрана (както в [UnlockGate] — няма с какво да се провери).
 */
@Composable
fun rememberPersonalAreaOpen(): Boolean {
    val container = LocalAppContainer.current
    val unlocked by container.personalUnlock.unlocked.collectAsState()
    return !LocalRequireUnlock.current || unlocked || !container.deviceUnlock.available()
}

/**
 * Личен екран (картата, заеманията, историята, личните съобщения): при включено „Искай
 * отключване…“ съдържанието се показва едва след проверка със заключването на телефона;
 * отказът води назад ([onCancel]). Отключването важи за всички лични екрани, докато не изтече
 * (PersonalUnlock). Телефон без заключване на екрана няма с какво да провери — тогава екранът
 * се показва (настройката и не може да се включи без заключване).
 */
@Composable
fun UnlockGate(onCancel: () -> Unit, required: Boolean = true, content: @Composable () -> Unit) {
    val container = LocalAppContainer.current
    // Без пауза при спрян екран: заключеното при угаснал екран изчезва, преди да се покаже отново.
    val unlocked by container.personalUnlock.unlocked.collectAsState()
    val needed = required && LocalRequireUnlock.current && !unlocked
    val canCheck = remember(needed) { needed && container.deviceUnlock.available() }
    if (!canCheck) {
        content()
        return
    }
    val activity = LocalActivity.current ?: LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val title = stringResource(R.string.unlock_prompt_title)
    val subtitle = stringResource(R.string.unlock_prompt_subtitle)
    val cancel by rememberUpdatedState(onCancel)
    var attempt by remember { mutableIntStateOf(0) }
    LaunchedEffect(attempt) {
        // Едва на видим екран (след прехода, след включване на екрана и отключване на телефона).
        lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }
        if (container.deviceUnlock.authenticate(activity, title, subtitle)) container.personalUnlock.unlock() else cancel()
    }
    // Заглавието е в самото съобщение — горната лента е само за „Назад“.
    Scaffold(topBar = { BackTopBar("", onCancel) }) { padding ->
        Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            MessageView(
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.unlock_locked_title),
                message = stringResource(R.string.unlock_locked_desc),
                actionLabel = stringResource(R.string.action_unlock),
                onAction = { attempt++ },
            )
        }
    }
}
