package org.chyavorec.app.ui.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.navigation.DETAIL_KEY

/** Колко се измества детайлът към списъка при пълен жест „назад“. */
private val BACK_SHIFT = 64.dp

/**
 * Прозорец за списък и детайл един до друг: поне 840 dp ширина (таблет, Chromebook
 * хоризонтално) и не нисък — телефонът хоризонтално (под 480 dp височина) остава с отделни екрани.
 */
@Composable
fun isTwoPaneWindow(): Boolean = windowWidth() >= 840.dp && windowHeight() >= 480.dp

/**
 * Списък с детайл отдясно (таблет): избраният екран (маршрутът му) е в [state] — състоянието
 * на списъка в навигацията, затова се пази при завъртане, смяна на раздела и след спиране на
 * процеса. „Назад“ първо затваря детайла, после списъка. На тесен прозорец (завъртане,
 * разделен екран) избраният детайл продължава на цял екран над списъка ([navigate]).
 * TalkBack чете първо списъка, после детайла. С [placeholder] дясната част е винаги там (на
 * широк прозорец) и без избран детайл показва него — за разделите-хъбове, чиито плочки иначе
 * биха се разтеглили по целия таблет.
 */
@Composable
fun ListDetailPane(
    state: SavedStateHandle,
    navigate: (String) -> Unit,
    detail: @Composable (route: String, close: () -> Unit) -> Unit,
    twoPane: Boolean = isTwoPaneWindow(),
    placeholder: (@Composable () -> Unit)? = null,
    list: @Composable () -> Unit,
) {
    val selectedFlow = remember(state) { state.getStateFlow<String?>(DETAIL_KEY, null) }
    val selected by selectedFlow.collectAsStateWithLifecycle()
    val close: () -> Unit = { state[DETAIL_KEY] = null }
    LaunchedEffect(twoPane, selected) {
        val route = selected
        if (!twoPane && route != null) {
            state[DETAIL_KEY] = null
            navigate(route)
        }
    }
    val shown = selected?.takeIf { twoPane }
    // Предсказуемо „назад“: детайлът се плъзга към списъка и избледнява с жеста; пуснат —
    // затваря се, отказан — пружината го връща. При намалено движение — само избледнява.
    val reduced = rememberReducedMotion()
    val scope = rememberCoroutineScope()
    val back = remember { Animatable(0f) }
    // Нов (или затворен) детайл — без остатък от предишния жест.
    LaunchedEffect(shown) { back.snapTo(0f) }
    PredictiveBackHandler(enabled = shown != null) { events ->
        try {
            events.collect { back.snapTo(it.progress) }
            close()
        } catch (e: CancellationException) {
            scope.launch { back.animateTo(0f, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) }
            throw e
        }
    }
    // Списъкът е винаги първото дете — при избор не губи превъртането и състоянието си.
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxHeight().semantics { isTraversalGroup = true; traversalIndex = 0f }) { list() }
        if (shown == null && twoPane && placeholder != null) {
            VerticalDivider()
            Box(Modifier.weight(1.4f).fillMaxHeight().semantics { isTraversalGroup = true; traversalIndex = 1f }) { placeholder() }
        }
        if (shown != null) {
            VerticalDivider()
            Box(
                Modifier
                    .weight(1.4f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        val p = back.value
                        alpha = 1f - 0.6f * p
                        if (!reduced) translationX = -p * BACK_SHIFT.toPx()
                    }
                    .semantics { isTraversalGroup = true; traversalIndex = 1f },
            ) {
                // Корицата е и в списъка, и в детайла едновременно — без shared element.
                CompositionLocalProvider(LocalSharedTransitionsEnabled provides false) {
                    key(shown) { detail(shown, close) }
                }
            }
        }
    }
}
