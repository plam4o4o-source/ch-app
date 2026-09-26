package org.chyavorec.app.ui.components

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/** Дали потребителят е изключил анимациите (Достъпност → „Премахване на анимациите“). */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
}

/**
 * Плавно появяване на елемент от списък (fade + лек slide) с малко закъснение по
 * индекс. Изпълнява се веднъж за елемента (състоянието оцелява при скролиране) и
 * само за първите елементи — по-нататъшните се показват веднага.
 */
@Composable
fun Modifier.animateEntrance(index: Int, maxStaggered: Int = 10): Modifier {
    val reduced = rememberReducedMotion()
    var shown by rememberSaveable { mutableStateOf(reduced || index >= maxStaggered) }
    val progress = remember { Animatable(if (shown) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!shown) {
            delay(index * 45L)
            progress.animateTo(1f, tween(420, easing = FastOutSlowInEasing))
            shown = true
        }
    }
    return this.graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * 22.dp.toPx()
    }
}

/* ---- Shared element преходи между списък и детайли (Navigation Compose) ---- */

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalNavAnimatedScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * Свързва елемент с общ [key] между два екрана (напр. корицата на книга в
 * списъка и в детайлите), така че да „прелети“ при навигация. Извън навигация
 * (preview, тестове) е no-op.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElementKey(key: String): Modifier {
    val shared = LocalSharedScope.current ?: return this
    val nav = LocalNavAnimatedScope.current ?: return this
    if (rememberReducedMotion()) return this
    return with(shared) {
        this@sharedElementKey.sharedElement(rememberSharedContentState(key), animatedVisibilityScope = nav)
    }
}
