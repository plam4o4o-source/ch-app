package org.chyavorec.app.ui.screens.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.model.ServiceCapabilities

/** Общо състояние на „Моето“: вход, възможности на сървъра, ръчна карта, профил. */
class AccountViewModel(private val c: AppContainer) : ViewModel() {
    val authState: StateFlow<AuthState> = c.authRepository.state
    val selfCard: StateFlow<SelfDeclaredCard?> = c.selfCardRepository.card
    val isDemo: Boolean = c.readerServices.isDemo

    private val _caps = MutableStateFlow<ServiceCapabilities?>(null)
    val capabilities: StateFlow<ServiceCapabilities?> = _caps.asStateFlow()

    private val _profile = MutableStateFlow(ScreenState<ReaderProfile>(loading = false))
    val profile: StateFlow<ScreenState<ReaderProfile>> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            c.authRepository.restore()
            c.selfCardRepository.load()
            _caps.value = c.authRepository.capabilities()
            // Профилът следва входа: зарежда се и при нов вход (не само при старт),
            // и се изчиства при изход.
            authState.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (_profile.value.data == null) loadProfile()
                } else {
                    _profile.value = ScreenState(loading = false)
                }
            }
        }
    }

    fun loadProfile() = viewModelScope.launch {
        _profile.update { it.startRefresh() }
        _profile.update { it.with(c.profileRepository.profile()) }
    }

    fun logout() = viewModelScope.launch {
        c.authRepository.logout()
        c.settings.clearPersonal()
        _profile.value = ScreenState(loading = false)
    }

    fun saveSelfCard(number: String, name: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        onResult(c.selfCardRepository.save(number, name))
    }

    fun removeSelfCard() = viewModelScope.launch { c.selfCardRepository.clear() }

    val deletion = MutableStateFlow<Outcome<Unit>?>(null)
    fun requestDeletion() = viewModelScope.launch { deletion.value = c.profileRepository.requestAccountDeletion() }
}

data class LoginUi(
    val cardNumber: String = "",
    val password: String = "",
    val remember: Boolean = true,
    val submitting: Boolean = false,
    val error: AppError? = null,
    val success: Boolean = false,
    val resetSent: Boolean = false,
)

class LoginViewModel(private val c: AppContainer) : ViewModel() {
    private val _ui = MutableStateFlow(LoginUi())
    val ui: StateFlow<LoginUi> = _ui.asStateFlow()
    private val _caps = MutableStateFlow<ServiceCapabilities?>(null)
    val capabilities: StateFlow<ServiceCapabilities?> = _caps.asStateFlow()
    val isDemo = c.readerServices.isDemo

    init { viewModelScope.launch { _caps.value = c.authRepository.capabilities() } }

    fun setCard(v: String) = _ui.update { it.copy(cardNumber = v.take(32), error = null) }
    fun setPassword(v: String) = _ui.update { it.copy(password = v.take(128), error = null) }
    fun setRemember(v: Boolean) = _ui.update { it.copy(remember = v) }

    fun submit() {
        val s = _ui.value
        if (s.cardNumber.isBlank() || s.password.isEmpty() || s.submitting) return
        // Паролата се предава като CharArray и се зачиства веднага след заявката.
        val pwd = s.password.toCharArray()
        _ui.update { it.copy(submitting = true, error = null, password = "") }
        viewModelScope.launch {
            when (val r = c.authRepository.login(s.cardNumber, pwd, s.remember)) {
                is Outcome.Success -> _ui.update { it.copy(submitting = false, success = true) }
                is Outcome.Failure -> _ui.update { it.copy(submitting = false, error = r.error) }
            }
        }
    }

    fun forgotPassword() = viewModelScope.launch {
        val card = _ui.value.cardNumber
        if (card.isBlank()) return@launch
        when (val r = c.authRepository.requestPasswordReset(card)) {
            is Outcome.Success -> _ui.update { it.copy(resetSent = true) }
            is Outcome.Failure -> _ui.update { it.copy(error = r.error) }
        }
    }
}

class LoansViewModel(private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<Loan>>())
    val state: StateFlow<ScreenState<List<Loan>>> = _state.asStateFlow()
    val renewResult = MutableStateFlow<Outcome<Loan>?>(null)
    val canRenew = MutableStateFlow(false)
    val isDemo = c.readerServices.isDemo

    init {
        viewModelScope.launch {
            canRenew.value = c.authRepository.capabilities().renew
            refresh()
        }
    }

    fun refresh() = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(c.libraryRepository.loans(force = true)) }
    }

    fun renew(loan: Loan) = viewModelScope.launch {
        val r = c.libraryRepository.renew(loan.loanId)
        renewResult.value = r
        if (r is Outcome.Success) refresh()
    }
}

class MembershipViewModel(private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<Membership>())
    val state: StateFlow<ScreenState<Membership>> = _state.asStateFlow()
    val isDemo = c.readerServices.isDemo

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(c.membershipRepository.membership(force = true)) }
    }
}
