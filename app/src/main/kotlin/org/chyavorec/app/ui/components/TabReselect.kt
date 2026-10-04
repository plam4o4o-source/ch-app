package org.chyavorec.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

/**
 * Сигнал „докоснат е вече избраният раздел“ от долната лента. Главните екрани
 * го слушат с [TabReselectEffect] и превъртат списъка си до началото.
 */
class TabReselectBus {
    private val events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val reselects: SharedFlow<String> = events

    fun emit(route: String) {
        events.tryEmit(route)
    }
}

val LocalTabReselect = staticCompositionLocalOf<TabReselectBus?> { null }

/** Изпълнява [onReselect] (напр. превъртане до началото), когато разделът [route] е докоснат повторно. */
@Composable
fun TabReselectEffect(route: String, onReselect: suspend () -> Unit) {
    val bus = LocalTabReselect.current ?: return
    val action by rememberUpdatedState(onReselect)
    LaunchedEffect(bus, route) {
        bus.reselects.filter { it == route }.collectLatest { action() }
    }
}
