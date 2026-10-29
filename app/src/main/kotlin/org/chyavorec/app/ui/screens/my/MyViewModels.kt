package org.chyavorec.app.ui.screens.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.data.security.SavedLogin
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.CapabilitiesStatus
import org.chyavorec.data.repository.CapabilitiesTracker
import org.chyavorec.domain.model.Contacts
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

/** Само известните възможности (`null` — още се питат или без връзка) за екраните, на които стига „да/не“. */
private fun StateFlow<CapabilitiesStatus>.knownCaps(scope: CoroutineScope): StateFlow<ServiceCapabilities?> =
    map { (it as? CapabilitiesStatus.Known)?.caps }
        .stateIn(scope, SharingStarted.Eagerly, (value as? CapabilitiesStatus.Known)?.caps)

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

    private val capsTracker = CapabilitiesTracker(c.authRepository)
    /** Известно ли е дали входът е включен — или връзката/сървърът не са отговорили (тогава „Опитай отново“). */
    val capabilitiesStatus: StateFlow<CapabilitiesStatus> = capsTracker.status
    /** Известните възможности; `null` — още се питат или питането не е успяло ([capabilitiesStatus]). */
    val capabilities: StateFlow<ServiceCapabilities?> = capsTracker.status.knownCaps(viewModelScope)

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
        viewModelScope.launch { capsTracker.refresh() }
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
     * Повторно питане за възможностите: след неуспех (без връзка — „Опитай отново“, връщане
     * на връзката, ново показване на „Моето“) или ако входът още не е включен. Ако питане
     * вече тече или входът е известен като включен — нищо не прави.
     */
    fun refreshCapabilities() {
        viewModelScope.launch { capsTracker.refresh() }
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
    /** Входът е временно заключен до този момент (епоха, ms); 0 — не е заключен. */
    val lockedUntilMillis: Long = 0,
    /** Колко опита остават до заключване — само след 3 неуспешни (иначе `null`, без предупреждение). */
    val attemptsLeft: Int? = null,
)

class LoginViewModel(private val c: AppContainer) : ViewModel() {
    private val _ui = MutableStateFlow(LoginUi())
    val ui: StateFlow<LoginUi> = _ui.asStateFlow()
    private val capsTracker = CapabilitiesTracker(c.authRepository)
    /** Зарежда се / известни / без връзка — „без връзка“ не е „входът не е включен“. */
    val capabilitiesStatus: StateFlow<CapabilitiesStatus> = capsTracker.status
    val isDemo = c.readerServices.isDemo

    /** Контактите на библиотеката за „Забравен ПИН?“ (`null` — още не са поискани). */
    private val _contacts = MutableStateFlow<Contacts?>(null)
    val contacts: StateFlow<Contacts?> = _contacts.asStateFlow()

    init {
        viewModelScope.launch { capsTracker.refresh() }
        // Заключване от по-ранен опит (екранът е отворен наново) — броячът продължава.
        _ui.update { it.withThrottle(lockSeconds = c.authRepository.throttle.remainingLockSeconds()) }
        // …и от преди рестарта на приложението: броячите се зареждат от диска (веднъж).
        viewModelScope.launch {
            c.authRepository.restoreThrottle()
            _ui.update { it.withThrottle(lockSeconds = c.authRepository.throttle.remainingLockSeconds()) }
        }
    }

    /**
     * Заключването и предупреждението „остават N опита“ — от LoginThrottle (без да
     * се променя логиката му). [lockSeconds] > 0 — заключено за толкова секунди.
     */
    private fun LoginUi.withThrottle(lockSeconds: Long): LoginUi {
        val t = c.authRepository.throttle
        val until = if (lockSeconds > 0) c.clock.now().toEpochMilli() + lockSeconds * 1000 else lockedUntilMillis
        return copy(
            lockedUntilMillis = until,
            attemptsLeft = t.attemptsLeft().takeIf { t.failedAttempts() >= WARN_AFTER_FAILURES && it > 0 },
        )
    }

    /** Броячът на екрана е стигнал нулата — бутонът се отключва, съобщението изчезва. */
    fun lockEnded() = _ui.update {
        if (it.lockedUntilMillis == 0L) it
        else it.copy(lockedUntilMillis = 0, error = if (it.error is AppError.RateLimited) null else it.error)
    }

    /** За „Забравен ПИН?“: контактите от сайта (кеша), иначе вградените резервни. */
    fun loadContacts() {
        if (_contacts.value != null) return
        viewModelScope.launch {
            val fromSite = (c.siteRepository.contacts(false) as? Outcome.Success)?.value?.data
            _contacts.value = fromSite ?: c.fallbackContacts
        }
    }

    /**
     * Повторен опит, ако сървърът не е отговорил („Опитай отново“, връщане на връзката) или
     * входът още не е включен. Тече ли вече питане — нищо не прави.
     */
    fun refreshCapabilities() {
        viewModelScope.launch { capsTracker.refresh() }
    }

    fun setCard(v: String) {
        fromPasswordManager = false
        _ui.update { it.copy(cardNumber = v.take(32), error = null) }
    }
    fun setPassword(v: String) {
        fromPasswordManager = false
        _ui.update { it.copy(password = v.take(128), error = null) }
    }
    fun setRemember(v: Boolean) = _ui.update { it.copy(remember = v) }

    /** Попълнено от мениджъра на пароли — тогава след входа не се предлага запазване отново. */
    private var fromPasswordManager = false
    /** Входът за запазване в мениджъра на пароли — само до показването на предложението, само в паметта. */
    private var saveOffer: SavedLogin? = null

    /**
     * Вход с едно докосване: запазеният вход попълва полетата и се изпраща през [submit] —
     * със същото заключване след грешни опити и със „Запомни ме“ както е избрано.
     */
    fun signInWithSaved(login: SavedLogin) {
        if (_ui.value.submitting || login.cardNumber.isBlank() || login.pin.isEmpty()) return
        _ui.update { it.copy(cardNumber = login.cardNumber.take(32), password = login.pin.take(128), error = null) }
        fromPasswordManager = true
        submit()
    }

    /**
     * След успешен вход: картата и ПИН-ът за предложението „Запази в мениджъра на пароли“
     * (взимат се веднъж). Без „Запомни ме“ или от самия мениджър — `null`.
     */
    fun takeSaveOffer(): SavedLogin? = saveOffer.also { saveOffer = null }

    override fun onCleared() {
        saveOffer = null
    }

    fun submit() {
        val s = _ui.value
        if (s.cardNumber.isBlank() || s.password.isEmpty() || s.submitting) return
        if (s.lockedUntilMillis > c.clock.now().toEpochMilli()) return
        // Паролата се предава като CharArray и се зачиства веднага след заявката.
        val pwd = s.password.toCharArray()
        // Предложение за запазване — само при „Запомни ме“ (на споделен телефон без него няма какво да остава)
        // и ако входът не е дошъл от мениджъра. Не се записва никъде от приложението.
        val offer = if (s.remember && !fromPasswordManager && !isDemo) SavedLogin(s.cardNumber.trim(), s.password) else null
        _ui.update { it.copy(submitting = true, error = null, password = "") }
        viewModelScope.launch {
            when (val r = c.authRepository.login(s.cardNumber, pwd, s.remember)) {
                is Outcome.Success -> {
                    saveOffer = offer
                    _ui.update { it.copy(submitting = false, success = true, attemptsLeft = null) }
                }
                is Outcome.Failure -> _ui.update {
                    // Запазеният ПИН не е минал (напр. сменен): новият, въведен на ръка, може да го замени.
                    fromPasswordManager = false
                    // Петият грешен ПИН заключва веднага (LoginThrottle) — броячът тръгва още сега.
                    val lock = (r.error as? AppError.RateLimited)?.retryAfterSeconds
                        ?: c.authRepository.throttle.remainingLockSeconds()
                    it.copy(submitting = false, error = r.error).withThrottle(lockSeconds = lock)
                }
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

    private companion object {
        /** След толкова грешни опита се показва „остават N опита“. */
        const val WARN_AFTER_FAILURES = 3
    }
}

class LoansViewModel(private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<Loan>>())
    val state: StateFlow<ScreenState<List<Loan>>> = _state.asStateFlow()
    val renewResult = MutableStateFlow<Outcome<Loan>?>(null)
    val canRenew = MutableStateFlow(false)
    val isDemo = c.readerServices.isDemo

    private val _renewing = MutableStateFlow<Set<String>>(emptySet())
    /** Заемания, за които заявката за удължаване още тече (бутонът е с индикатор и неактивен). */
    val renewing: StateFlow<Set<String>> = _renewing.asStateFlow()

    private val _inCatalog = MutableStateFlow<Set<Long>>(emptySet())
    /** Инвентарните номера на заетите книги, които ги има в каталога (само те водят към страницата на книгата). */
    val inCatalog: StateFlow<Set<Long>> = _inCatalog.asStateFlow()

    init {
        viewModelScope.launch { canRenew.value = c.authRepository.capabilities().renew }
        // Следва входа: зарежда при вход (и при нов вход), изчиства при изход.
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            c.authRepository.state.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (_state.value.data == null) load(force = false)
                } else if (s !is AuthState.Unknown) { // изход или временно нечетима сесия
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
        _state.value.data?.let { resolveCatalog(it) }
        // Уиджетът показва броя и най-близкия срок — да не остава със стари данни.
        if (r is Outcome.Success && r.value.data != before) runCatching { c.refreshWidget() }
    }

    /**
     * Заявка за удължаване. Отговорът (202) вече съдържа заемането с `renewPending` —
     * то се сменя на място (и в кеша, от репозиторито), без ново теглене на целия списък.
     */
    fun renew(loan: Loan) {
        // Двойно натискане (или второ, докато първата заявка тече) не праща втора заявка.
        var started = false
        _renewing.update { cur ->
            started = loan.loanId !in cur
            if (started) cur + loan.loanId else cur
        }
        if (!started) return
        viewModelScope.launch {
            try {
                val r = c.libraryRepository.renew(loan.loanId)
                renewResult.value = r
                if (r is Outcome.Success) {
                    val updated = r.value
                    _state.update { s -> s.copy(data = s.data?.map { if (it.loanId == loan.loanId) updated else it }) }
                }
            } finally {
                _renewing.update { it - loan.loanId }
            }
        }
    }

    /**
     * Кои от заетите книги ги има в каталога — през индекса в базата/кеша, без мрежа.
     * Без каталог (първо пускане без връзка) картите просто не водят към книгата.
     */
    private suspend fun resolveCatalog(loans: List<Loan>) {
        val invs = loans.mapNotNull { it.inv }.toSet()
        if (invs.isEmpty() || invs.all { it in _inCatalog.value }) return
        val searcher = runCatching { c.catalogRepository.lookupSearcher(network = false)?.data }.getOrNull() ?: return
        val found = invs.filterTo(HashSet()) { inv -> runCatching { searcher.book(inv) }.getOrNull() != null }
        _inCatalog.update { it + found }
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
                } else if (s !is AuthState.Unknown) { // изход или временно нечетима сесия
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
