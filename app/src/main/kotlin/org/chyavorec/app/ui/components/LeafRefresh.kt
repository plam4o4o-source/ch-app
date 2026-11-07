package org.chyavorec.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

/**
 * [PullToRefreshBox] с нашия индикатор: осморъка, която се бродира, докато потребителят
 * дърпа ([StitchRefreshIndicator]), вместо стандартния кръг. (Името е от яворовия лист,
 * който беше тук преди — извикванията не се променят.)
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
        indicator = { StitchRefreshIndicator(state, isRefreshing, Modifier.align(Alignment.TopCenter)) },
        content = content,
    )
}

/** Осморъката в индикатора — 11×11 бода (в 32 dp се четат като шевица, а не като точки). */
private val RefreshRosette: StitchChart = StitchCharts.rosette(radius = 5)

/**
 * Индикатор за „дръпни за опресняване“: осморъка, бродирана бод по бод пропорционално на
 * дърпането — на прага е завършена (и кратко тактилно потвърждение); докато трае
 * опресняването, по нея „тича“ светъл конец; накрая завършената шевица избледнява, докато
 * индикаторът се прибира.
 *
 * Дърпането се чете само при рисуване (без рекомпозиция на кадър); безкрайната анимация
 * съществува само докато [isRefreshing]. При намалено движение конецът не тича.
 */
@Composable
fun StitchRefreshIndicator(state: PullToRefreshState, isRefreshing: Boolean, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val haptic = LocalHapticFeedback.current
    val refreshing by rememberUpdatedState(isRefreshing)

    // Тактилен „щрак“ при достигане на прага по време на дърпане (не при програмно опресняване).
    LaunchedEffect(state) {
        snapshotFlow { state.distanceFraction >= 1f }
            .distinctUntilChanged()
            .collect { ripe -> if (ripe && !refreshing) haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate) }
    }

    // След опресняването шевицата остава цяла и избледнява с прибирането (не се „разшива“).
    var finishing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            finishing = false
        } else if (state.distanceFraction > 0f) {
            finishing = true
            snapshotFlow { state.distanceFraction <= 0f }.first { it }
            finishing = false
        }
    }

    val run: State<Float>? = if (isRefreshing && !reduced) {
        rememberInfiniteTransition(label = "thread-run").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing)),
            label = "thread-phase",
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
        StitchedMotif(
            RefreshRosette,
            progress = { if (isRefreshing || finishing) 1f else refreshProgress(state.distanceFraction) },
            modifier = Modifier.size(32.dp),
            glowPhase = { run?.value ?: -1f },
            alpha = { if (finishing) state.distanceFraction.coerceIn(0f, 1f) else 1f },
        )
    }
}

/** Колко от осморъката е избродирано при дърпане [distanceFraction] (1 = прагът): цялата — на прага. */
internal fun refreshProgress(distanceFraction: Float): Float = distanceFraction.coerceIn(0f, 1f)
