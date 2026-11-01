package org.chyavorec.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

    /** Колко пъти е показан всеки раздел (само на главната нишка) — виж [visitsElsewhere]. */
    private val visits = HashMap<String, Int>()

    /** Показан е разделът [route] (при всяка смяна на екрана към него). */
    fun shown(route: String) {
        visits[route] = (visits[route] ?: 0) + 1
    }

    /** Показванията на другите раздели — промяна значи, че потребителят е ходил извън [route]. */
    fun visitsElsewhere(route: String): Int = visits.entries.sumOf { (r, n) -> if (r == route) 0 else n }
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

/**
 * „Търсене“ от клавиатурата (Ctrl+F или „/“, таблет/Chromebook): разделът на екрана отваря
 * или фокусира своето поле за търсене ([SearchShortcutEffect]).
 */
class SearchShortcutBus {
    private val events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val requests: SharedFlow<String> = events

    /** Полетата на екрана по раздели (само на главната нишка) — виж [emit]. */
    private val fields = HashMap<String, Int>()

    /** Поисква търсенето на [route]; `false` — полето не е на екрана (напр. превъртяно извън него). */
    fun emit(route: String): Boolean = (fields[route] ?: 0) > 0 && events.tryEmit(route)

    internal fun shown(route: String, on: Boolean) {
        fields[route] = (fields[route] ?: 0) + if (on) 1 else -1
    }
}

val LocalSearchShortcut = staticCompositionLocalOf<SearchShortcutBus?> { null }

/** Изпълнява [onSearch] (разгъва/фокусира полето), когато търсенето е поискано от клавиатурата на [route]. */
@Composable
fun SearchShortcutEffect(route: String, onSearch: suspend () -> Unit) {
    val bus = LocalSearchShortcut.current ?: return
    val action by rememberUpdatedState(onSearch)
    DisposableEffect(bus, route) {
        bus.shown(route, true)
        onDispose { bus.shown(route, false) }
    }
    LaunchedEffect(bus, route) {
        bus.requests.filter { it == route }.collectLatest { action() }
    }
}
