package org.chyavorec.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.screens.my.AccountViewModel
import org.chyavorec.app.ui.screens.my.LoansViewModel
import org.chyavorec.core.AppError

val LocalAppContainer = staticCompositionLocalOf<AppContainer> { error("AppContainer не е подаден") }

/** ViewModel със зависимости от [AppContainer] (ръчно DI, без рефлексия). */
@Composable
inline fun <reified VM : ViewModel> appViewModel(key: String? = null, crossinline create: (AppContainer) -> VM): VM {
    val container = LocalAppContainer.current
    return viewModel(key = key, factory = viewModelFactory { initializer { create(container).also { watchReconnect(it, container) } } })
}

/**
 * ViewModel, чийто екран показва данни от кеша или грешка и при връщане на връзката
 * ([AppContainer.reconnected]) трябва да се опресни сам — с обичайното (неизрично)
 * опресняване, както при отваряне на екрана.
 */
interface RefreshOnReconnect {
    /** Състоянието на основните данни на екрана — по него се решава дали има нужда. */
    val reconnectState: ScreenState<*>

    fun refreshOnReconnect()
}

/**
 * Дали при връщане на връзката си струва опресняване: данните са от кеша / с грешка при
 * опресняването, или екранът е с мрежова грешка — и в момента нищо не се зарежда (без
 * второ, едновременно опресняване). Изход от профила или липсващ запис не се „поправят“ от мрежата.
 */
fun ScreenState<*>.wantsReconnectRefresh(): Boolean =
    !loading && !refreshing &&
        (fromCache || refreshError != null || (error != null && error !is AppError.Unauthorized && error != AppError.NotFound))

/**
 * Свързва създадения ViewModel със събитието „връзката се върна“ (по едно на връщане,
 * вж. org.chyavorec.core.reconnections). Читателските екрани на „Моето“ — през публичния
 * им API: профилът (неизрично, както при отваряне) и заеманията (единственото им
 * опресняване е от мрежата).
 */
fun watchReconnect(vm: ViewModel, container: AppContainer) {
    val action: (() -> Unit) = when (vm) {
        is RefreshOnReconnect -> { { if (vm.reconnectState.wantsReconnectRefresh()) vm.refreshOnReconnect() } }
        is AccountViewModel -> { { if (vm.profile.value.wantsReconnectRefresh()) vm.loadProfile(force = false) } }
        is LoansViewModel -> { { if (vm.state.value.wantsReconnectRefresh()) vm.refresh() } }
        else -> return
    }
    vm.viewModelScope.launch { container.reconnected.collect { action() } }
}
