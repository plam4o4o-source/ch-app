package org.chyavorec.app.ui.screens.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.model.ServiceCapabilities

/**
 * Кешът веднага (cache-first), без лента „няма връзка“ и без индикатор: опресняването
 * тече тихо във фон и ако не успее, резултатът ([ScreenState.with]) показва причината.
 */
internal fun <T> Synced<T>.asQuietState(): ScreenState<T> = ScreenState(data = data, loading = false, syncedAt = syncedAt)

/**
 * Общо състояние на „Моето“: вход, възможности на сървъра, ръчна карта, профил.
 *
 * Самият профил е общ за приложението ([org.chyavorec.data.repository.ProfileRepository.state]) —
 * „Моето“, картата (и на цял екран) и „Профил“ го споделят, вместо всеки екран да го тегли;
 * тук е само състоянието на зареждането за този екран.
 */
class AccountViewModel(private val c: AppContainer) : ViewModel() {
    val authState: StateFlow<AuthState> = c.authRepository.state
    val selfCard: StateFlow<SelfDeclaredCard?> = c.selfCardRepository.card
    val isDemo: Boolean = c.readerServices.isDemo

    private val _caps = MutableStateFlow<ServiceCapabilities?>(null)
    val capabilities: StateFlow<ServiceCapabilities?> = _caps.asStateFlow()

    /** С каква грешка е приключил последният опит за профила на този екран (`null` = без грешка/тече). */
    private val _loadError = MutableStateFlow<AppError?>(null)

    val profile: StateFlow<ScreenState<ReaderProfile>> =
        combine(c.profileRepository.state, _loadError, authState) { p, e, a -> profileState(p, e, a) }
            .stateIn(
                viewModelScope, SharingStarted.Eagerly,
                // Вече зареден (от друг екран) профил се вижда веднага, без „Зареждане…“.
                profileState(c.profileRepository.state.value, null, authState.value),
            )

    init {
        // Възможностите идват от мрежата — отделно, за да не чака профилът бавната връзка.
        viewModelScope.launch { _caps.value = c.authRepository.capabilities() }
        viewModelScope.launch {
            c.authRepository.restore()
            c.selfCardRepository.load()
            // Профилът следва входа: зарежда се и при нов вход (не само при старт).
            // Cache-first: от паметта/кеша веднага, от мрежата — само ако е по-стар от 12 часа.
            authState.collect { s ->
                if (s is AuthState.SignedIn) {
                    loadProfile()
                } else {
                    _loadError.value = null
                }
            }
        }
    }

    /**
     * Повторно питане за възможностите, ако първия път сървърът не е отговорил
     * (тогава те идват като „нищо не се поддържа“ и входът би останал скрит).
     * Ако първото зареждане още тече — нищо не прави.
     */
    fun refreshCapabilities() {
        val current = _caps.value ?: return
        if (current.login) return
        viewModelScope.launch { _caps.value = c.authRepository.capabilities() }
    }

    /** [force] = true — от мрежата независимо от свежестта на кеша. */
    fun loadProfile(force: Boolean = false) = viewModelScope.launch {
        refreshCapabilities()
        _loadError.value = null
        // Кешираният профил (може и остарял) се показва веднага, докато тече опресняването.
        c.profileRepository.cachedProfile()
        val r = c.profileRepository.profile(force)
        _loadError.value = (r as? Outcome.Failure)?.error
    }

    fun logout() = viewModelScope.launch {
        c.authRepository.logout()
        c.settings.clearPersonal()
        _loadError.value = null
    }

    fun saveSelfCard(number: String, name: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        onResult(c.selfCardRepository.save(number, name))
    }

    fun removeSelfCard() = viewModelScope.launch { c.selfCardRepository.clear() }

    val deletion = MutableStateFlow<Outcome<Unit>?>(null)
    fun requestDeletion() = viewModelScope.launch { deletion.value = c.profileRepository.requestAccountDeletion() }

    private companion object {
        fun profileState(p: Synced<ReaderProfile>?, error: AppError?, a: AuthState): ScreenState<ReaderProfile> = when {
            a !is AuthState.SignedIn -> ScreenState(loading = false)
            // Още няма профил: зарежда се (и преди първия опит), освен ако опитът е завършил с грешка.
            p == null -> if (error != null) ScreenState(loading = false, error = error) else ScreenState(loading = true)
            else -> ScreenState(
                data = p.data,
                loading = false,
                syncedAt = p.syncedAt,
                fromCache = p.fromCache && (p.refreshError != null || error != null),
                refreshError = p.refreshError ?: error,
            )
        }
    }
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

    /** Повторен опит, ако при първото зареждане сървърът не е отговорил. */
    fun refreshCapabilities() {
        val current = _caps.value ?: return
        if (current.login) return
        viewModelScope.launch { _caps.value = c.authRepository.capabilities() }
    }

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
        viewModelScope.launch { canRenew.value = c.authRepository.capabilities().renew }
        // Следва входа: зарежда при вход (и при нов вход), изчиства при изход.
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            c.authRepository.state.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (_state.value.data == null) load(force = false)
                } else if (s is AuthState.SignedOut) {
                    _state.value = ScreenState(loading = false, error = AppError.Unauthorized)
                }
            }
        }
    }

    /** Дръпване за опресняване / „Опитай пак“ — винаги от мрежата. */
    fun refresh() = load(force = true)

    /**
     * Cache-first: кешираните заемания се показват веднага, а от мрежата се теглят само
     * ако са по-стари от 3 минути (или при [force]).
     */
    private fun load(force: Boolean) = viewModelScope.launch {
        if (!canRenew.value) canRenew.value = c.authRepository.capabilities().renew
        if (force) {
            _state.update { it.startRefresh() }
        } else if (_state.value.data == null) {
            val cached = c.libraryRepository.cachedLoans()
            _state.update { cur ->
                when {
                    cur.data != null -> cur
                    cached != null -> cached.asQuietState()
                    else -> cur.startRefresh()
                }
            }
        }
        val before = _state.value.data
        val r = c.libraryRepository.loans(force)
        _state.update { it.with(r) }
        // Уиджетът показва броя и най-близкия срок — да не остава със стари данни.
        if (r is Outcome.Success && r.value.data != before) runCatching { c.refreshWidget() }
    }

    /**
     * Заявка за удължаване. Отговорът (202) вече съдържа заемането с `renewPending` —
     * то се сменя на място (и в кеша, от репозиторито), без ново теглене на целия списък.
     */
    fun renew(loan: Loan) = viewModelScope.launch {
        val r = c.libraryRepository.renew(loan.loanId)
        renewResult.value = r
        if (r is Outcome.Success) {
            val updated = r.value
            _state.update { s -> s.copy(data = s.data?.map { if (it.loanId == loan.loanId) updated else it }) }
        }
    }
}

class MembershipViewModel(private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<Membership>())
    val state: StateFlow<ScreenState<Membership>> = _state.asStateFlow()
    val isDemo = c.readerServices.isDemo

    init {
        // Следва входа: зарежда при вход (и при нов вход), изчиства при изход.
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            c.authRepository.state.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (_state.value.data == null) load(force = false)
                } else if (s is AuthState.SignedOut) {
                    _state.value = ScreenState(loading = false, error = AppError.Unauthorized)
                }
            }
        }
    }

    /** Дръпване за опресняване / „Опитай пак“ — винаги от мрежата. */
    fun refresh() = load(force = true)

    /** Cache-first: кешът веднага, от мрежата — само ако е по-стар от 6 часа (или при [force]). */
    private fun load(force: Boolean) = viewModelScope.launch {
        if (force) {
            _state.update { it.startRefresh() }
        } else if (_state.value.data == null) {
            val cached = c.membershipRepository.cached()
            _state.update { cur ->
                when {
                    cur.data != null -> cur
                    cached != null -> cached.asQuietState()
                    else -> cur.startRefresh()
                }
            }
        }
        _state.update { it.with(c.membershipRepository.membership(force)) }
    }
}
