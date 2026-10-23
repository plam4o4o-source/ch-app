package org.chyavorec.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.screens.my.AccountViewModel
import org.chyavorec.app.ui.screens.my.LoansViewModel
import org.chyavorec.app.ui.screens.my.LoginViewModel
import org.chyavorec.core.AppError
import org.chyavorec.data.repository.CapabilitiesStatus

val LocalAppContainer = staticCompositionLocalOf<AppContainer> { error("AppContainer не е подаден") }

/** ViewModel със зависимости от [AppContainer] (ръчно DI, без рефлексия). */
@Composable
inline fun <reified VM : ViewModel> appViewModel(key: String? = null, crossinline create: (AppContainer) -> VM): VM {
    val container = LocalAppContainer.current
    return viewModel(key = key, factory = viewModelFactory { initializer { create(container).also { watchReconnect(it, container) } } })
}

/**
 * Като [appViewModel], но с [SavedStateHandle] (от CreationExtras на екрана в навигацията):
 * въведеното (напр. търсенето) оцелява и след като системата спре процеса във фонов режим.
 */
@Composable
inline fun <reified VM : ViewModel> appViewModelWithState(key: String? = null, crossinline create: (AppContainer, SavedStateHandle) -> VM): VM {
    val container = LocalAppContainer.current
    return viewModel(key = key, factory = viewModelFactory {
        initializer { create(container, createSavedStateHandle()).also { watchReconnect(it, container) } }
    })
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
 * им API: профилът (неизрично, както при отваряне), заеманията (единственото им
 * опресняване е от мрежата) и неразчетените без връзка възможности (дали входът е включен).
 */
fun watchReconnect(vm: ViewModel, container: AppContainer) = watchReconnect(vm, container.reconnected)

/** Като горното, но със самото събитие „връзката се върна“ (тестовете подават свое). */
fun watchReconnect(vm: ViewModel, reconnected: Flow<Unit>) {
    val action: (() -> Unit) = when (vm) {
        is RefreshOnReconnect -> { { if (vm.reconnectState.wantsReconnectRefresh()) vm.refreshOnReconnect() } }
        is AccountViewModel -> { {
            // Възможностите, ако без връзка не са били разчетени („Опитай отново“ в „Моето“).
            if (vm.capabilitiesStatus.value is CapabilitiesStatus.Failed) vm.refreshCapabilities()
            if (vm.profile.value.wantsReconnectRefresh()) vm.loadProfile(force = false)
        } }
        is LoginViewModel -> { { if (vm.capabilitiesStatus.value is CapabilitiesStatus.Failed) vm.refreshCapabilities() } }
        is LoansViewModel -> { { if (vm.state.value.wantsReconnectRefresh()) vm.refresh() } }
        else -> return
    }
    vm.viewModelScope.launch { reconnected.collect { action() } }
}
