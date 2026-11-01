package org.chyavorec.app.ui.components

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.ui.navigation.DETAIL_KEY

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
 * TalkBack чете първо списъка, после детайла.
 */
@Composable
fun ListDetailPane(
    state: SavedStateHandle,
    navigate: (String) -> Unit,
    detail: @Composable (route: String, close: () -> Unit) -> Unit,
    twoPane: Boolean = isTwoPaneWindow(),
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
    BackHandler(enabled = shown != null, onBack = close)
    // Списъкът е винаги първото дете — при избор не губи превъртането и състоянието си.
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxHeight().semantics { isTraversalGroup = true; traversalIndex = 0f }) { list() }
        if (shown != null) {
            VerticalDivider()
            Box(Modifier.weight(1.4f).fillMaxHeight().semantics { isTraversalGroup = true; traversalIndex = 1f }) {
                // Корицата е и в списъка, и в детайла едновременно — без shared element.
                CompositionLocalProvider(LocalSharedTransitionsEnabled provides false) {
                    key(shown) { detail(shown, close) }
                }
            }
        }
    }
}
