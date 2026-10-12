package org.chyavorec.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import org.chyavorec.app.ui.theme.LocalExtendedColors

/** Зелен (неузрял) клас — в началото на дърпането. */
private val UnripeWheat = Color(0xFF6E8B3D)

/**
 * [PullToRefreshBox] с нашия индикатор: житен клас ([WheatRefreshIndicator]) вместо
 * стандартния кръг. Същите параметри като досегашните извиквания — подменя се само
 * името на функцията.
 */
@Composable
fun WheatRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable BoxScope.() -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        indicator = { WheatRefreshIndicator(state, isRefreshing, Modifier.align(Alignment.TopCenter)) },
        content = content,
    )
}

/**
 * Индикатор за „дръпни за опресняване“: житен клас, който се завърта, докато
 * потребителят дърпа, и „узрява“ (зелено → златно) при прага; при отпускане след
 * прага — кратко тактилно потвърждение, а докато трае опресняването — върти се.
 *
 * Прогресът на дърпането се чете само при рисуване (без рекомпозиция на кадър);
 * безкрайната анимация съществува само докато [isRefreshing]. При намалено
 * движение класът не се върти (само сменя цвета).
 */
@Composable
fun WheatRefreshIndicator(state: PullToRefreshState, isRefreshing: Boolean, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val gold = LocalExtendedColors.current.gold
    val haptic = LocalHapticFeedback.current
    val refreshing by rememberUpdatedState(isRefreshing)

    // Тактилен „щрак“ при достигане на прага по време на дърпане (не при програмно опресняване).
    LaunchedEffect(state) {
        snapshotFlow { state.distanceFraction >= 1f }
            .distinctUntilChanged()
            .collect { ripe -> if (ripe && !refreshing) haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate) }
    }

    val spin: State<Float>? = if (isRefreshing && !reduced) {
        rememberInfiniteTransition(label = "wheat-spin").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing)),
            label = "wheat-angle",
        )
    } else {
        null
    }

    PullToRefreshDefaults.IndicatorBox(
        state = state,
        isRefreshing = isRefreshing,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Canvas(Modifier.size(28.dp).clearAndSetSemantics { }) {
            val fraction = state.distanceFraction
            val ripeness = if (isRefreshing) 1f else fraction.coerceIn(0f, 1f)
            // Последната четвърт от пътя до прага „доузрява“ по-бързо — ясен сигнал „пусни“.
            val color = lerp(UnripeWheat, gold, (ripeness * ripeness).coerceIn(0f, 1f))
            val angle = when {
                spin != null -> spin.value
                isRefreshing -> 0f
                reduced -> 0f
                else -> fraction.coerceAtMost(1.5f) * 240f
            }
            // Леко „набъбване“ на прага.
            val pop = if (!isRefreshing && fraction >= 1f) 1.08f else 1f
            rotate(angle) {
                scale(pop) { drawWheatEar(color) }
            }
        }
    }
}

/**
 * Житен клас в квадрата на DrawScope: стрък отдолу, зърна по двойки нагоре и
 * осилчета на върха. Рисува се с примитиви (без Path/алокации на кадър).
 */
private fun DrawScope.drawWheatEar(color: Color) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val stroke = 1.6f * density
    // Стрък
    drawLine(color, Offset(cx, h * 0.98f), Offset(cx, h * 0.30f), strokeWidth = stroke, cap = StrokeCap.Round)
    // Зърна: 4 двойки + връхно зърно; всяко е овал, наклонен навън.
    val grain = Size(w * 0.20f, h * 0.15f)
    for (i in 0 until 4) {
        val y = h * (0.66f - i * 0.12f)
        val k = 1f - i * 0.08f // към върха зърната са малко по-дребни
        val gs = Size(grain.width * k, grain.height * k)
        rotate(-32f, pivot = Offset(cx, y)) {
            drawOval(color, topLeft = Offset(cx - gs.width, y - gs.height / 2f), size = gs)
        }
        rotate(32f, pivot = Offset(cx, y)) {
            drawOval(color, topLeft = Offset(cx, y - gs.height / 2f), size = gs)
        }
    }
    val top = Size(grain.width * 0.8f, grain.height * 0.9f)
    drawOval(color, topLeft = Offset(cx - top.width / 2f, h * 0.14f), size = top)
    // Осилчета
    val awn = 0.9f * density
    drawLine(color, Offset(cx, h * 0.15f), Offset(cx, h * 0.02f), strokeWidth = awn, cap = StrokeCap.Round)
    drawLine(color, Offset(cx - w * 0.06f, h * 0.24f), Offset(cx - w * 0.20f, h * 0.06f), strokeWidth = awn, cap = StrokeCap.Round)
    drawLine(color, Offset(cx + w * 0.06f, h * 0.24f), Offset(cx + w * 0.20f, h * 0.06f), strokeWidth = awn, cap = StrokeCap.Round)
}
