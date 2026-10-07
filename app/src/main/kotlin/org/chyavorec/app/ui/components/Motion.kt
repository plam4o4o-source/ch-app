package org.chyavorec.app.ui.components

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.delay

/**
 * Флагът „намалено движение“, прочетен веднъж в корена ([observeReducedMotion]) и
 * подаден надолу — елементите на списъците не четат системните настройки поотделно.
 * `null` = не е подаден (preview, екран извън корена) → чете се на място.
 */
val LocalReducedMotion = staticCompositionLocalOf<Boolean?> { null }

private fun readReducedMotion(context: android.content.Context): Boolean =
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

/**
 * Чете системната настройка и я опреснява при всяко връщане към приложението
 * (ON_RESUME) — за подаване в [LocalReducedMotion] веднъж близо до корена.
 */
@Composable
fun observeReducedMotion(): Boolean {
    val context = LocalContext.current
    var reduced by remember { mutableStateOf(readReducedMotion(context)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { reduced = readReducedMotion(context) }
    return reduced
}

/** Дали потребителят е изключил анимациите (Достъпност → „Премахване на анимациите“). */
@Composable
fun rememberReducedMotion(): Boolean {
    val provided = LocalReducedMotion.current
    if (provided != null) return provided
    val context = LocalContext.current
    return remember { readReducedMotion(context) }
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
/**
 * Пружинен преход на границите на shared element-а (леко „подскачане“ в края) —
 * при прекъснат жест „назад“ пружината естествено се връща без скок.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
private val SpringBounds = BoundsTransform { _: Rect, _: Rect ->
    spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow, visibilityThreshold = Rect.VisibilityThreshold)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElementKey(key: String): Modifier {
    val shared = LocalSharedScope.current ?: return this
    val nav = LocalNavAnimatedScope.current ?: return this
    if (rememberReducedMotion()) return this
    return with(shared) {
        this@sharedElementKey.sharedElement(rememberSharedContentState(key), animatedVisibilityScope = nav, boundsTransform = SpringBounds)
    }
}
