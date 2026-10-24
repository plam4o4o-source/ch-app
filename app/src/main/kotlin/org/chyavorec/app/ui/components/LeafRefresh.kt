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
import androidx.compose.ui.graphics.Path
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

/** Зелен (летен) лист — в началото на дърпането. */
private val SummerLeaf = Color(0xFF6E8B3D)

/**
 * [PullToRefreshBox] с нашия индикатор: яворов лист ([LeafRefreshIndicator]) — дървото,
 * на което е кръстено Яворец — вместо стандартния кръг. Същите параметри като досегашните извиквания — подменя се само
 * името на функцията.
 */
@Composable
fun LeafRefreshBox(
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
        indicator = { LeafRefreshIndicator(state, isRefreshing, Modifier.align(Alignment.TopCenter)) },
        content = content,
    )
}

/**
 * Индикатор за „дръпни за опресняване“: яворов лист, който се завърта, докато
 * потребителят дърпа, и „есенясва“ (зелено → златно) при прага; при отпускане след
 * прага — кратко тактилно потвърждение, а докато трае опресняването — върти се.
 *
 * Прогресът на дърпането се чете само при рисуване (без рекомпозиция на кадър);
 * безкрайната анимация съществува само докато [isRefreshing]. При намалено
 * движение листът не се върти (само сменя цвета).
 */
@Composable
fun LeafRefreshIndicator(state: PullToRefreshState, isRefreshing: Boolean, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val gold = LocalExtendedColors.current.gold
    val veins = MaterialTheme.colorScheme.surfaceContainerHigh
    val haptic = LocalHapticFeedback.current
    val refreshing by rememberUpdatedState(isRefreshing)

    // Тактилен „щрак“ при достигане на прага по време на дърпане (не при програмно опресняване).
    LaunchedEffect(state) {
        snapshotFlow { state.distanceFraction >= 1f }
            .distinctUntilChanged()
            .collect { ripe -> if (ripe && !refreshing) haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate) }
    }

    val spin: State<Float>? = if (isRefreshing && !reduced) {
        rememberInfiniteTransition(label = "leaf-spin").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing)),
            label = "leaf-angle",
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
            // Последната четвърт от пътя до прага пожълтява по-бързо — ясен сигнал „пусни“.
            val color = lerp(SummerLeaf, gold, (ripeness * ripeness).coerceIn(0f, 1f))
            val angle = when {
                spin != null -> spin.value
                isRefreshing -> 0f
                reduced -> 0f
                else -> fraction.coerceAtMost(1.5f) * 240f
            }
            // Леко „набъбване“ на прага.
            val pop = if (!isRefreshing && fraction >= 1f) 1.08f else 1f
            rotate(angle) {
                scale(pop) { drawSycamoreLeaf(color, veins) }
            }
        }
    }
}

/**
 * Петделен лист на явор (Acer pseudoplatanus) в единичен квадрат: широки дялове със зъбче на
 * върха, плитки вдлъбнатини между тях и дръжка отдолу. Пътят се строи веднъж и само се мащабира при
 * рисуване (без алокации на кадър).
 */
private val SycamoreLeaf: Path = Path().apply {
    moveTo(0.43f, 0.76f)
    quadraticTo(0.24f, 0.82f, 0.10f, 0.70f) // долен ляв дял
    lineTo(0.04f, 0.70f); lineTo(0.08f, 0.64f) // зъбче
    quadraticTo(0.04f, 0.56f, 0.14f, 0.50f) // плитка вдлъбнатина
    quadraticTo(0.04f, 0.44f, 0.06f, 0.30f) // горен ляв дял
    lineTo(0.02f, 0.24f); lineTo(0.12f, 0.24f)
    quadraticTo(0.22f, 0.16f, 0.32f, 0.24f)
    quadraticTo(0.34f, 0.12f, 0.44f, 0.08f) // връх
    lineTo(0.50f, 0.00f); lineTo(0.56f, 0.08f)
    quadraticTo(0.66f, 0.12f, 0.68f, 0.24f)
    quadraticTo(0.78f, 0.16f, 0.88f, 0.24f) // горен десен дял
    lineTo(0.98f, 0.24f); lineTo(0.94f, 0.30f)
    quadraticTo(0.96f, 0.44f, 0.86f, 0.50f)
    quadraticTo(0.96f, 0.56f, 0.92f, 0.64f) // долен десен дял
    lineTo(0.96f, 0.70f); lineTo(0.90f, 0.70f)
    quadraticTo(0.76f, 0.82f, 0.57f, 0.76f)
    quadraticTo(0.50f, 0.72f, 0.43f, 0.76f)
    close()
}

/** Основата на листа — оттам тръгват жилките и дръжката. */
private val LeafBase = Offset(0.50f, 0.70f)

/** Върховете на петте дяла — краищата на главните жилки. */
private val LeafTips = listOf(
    Offset(0.50f, 0.08f), Offset(0.12f, 0.27f), Offset(0.88f, 0.27f), Offset(0.10f, 0.66f), Offset(0.90f, 0.66f),
)

private fun DrawScope.drawSycamoreLeaf(color: Color, veins: Color) {
    val w = size.width
    val h = size.height
    // Дръжка
    drawLine(color, Offset(w * 0.50f, h * 0.74f), Offset(w * 0.50f, h * 0.98f), strokeWidth = 1.6f * density, cap = StrokeCap.Round)
    scale(w, h, pivot = Offset.Zero) { drawPath(SycamoreLeaf, color) }
    // Главните жилки — в цвета на фона, тънки, от основата към всеки дял.
    val base = Offset(LeafBase.x * w, LeafBase.y * h)
    for (tip in LeafTips) {
        drawLine(veins, base, Offset(tip.x * w, tip.y * h), strokeWidth = 0.8f * density, cap = StrokeCap.Round)
    }
}
