package org.chyavorec.app.ui.screens.scan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.data.scan.BarcodeScanner
import org.chyavorec.app.data.scan.ScanOutcome
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogBook

/** Резултат от сканиране: книга от каталога или „няма я“. */
sealed interface ScanResult {
    data class Hit(val book: CatalogBook, val copies: List<CatalogBook>, val target: ScanTarget) : ScanResult
    data class Miss(val target: ScanTarget) : ScanResult
}

/** Каталогът за скенера: зарежда се, готов е или не е достъпен (без мрежа и без кеш). */
enum class ScanCatalogStatus { LOADING, READY, UNAVAILABLE }

/** Скенерът на Google: затворен, отворен (над приложението) или недостъпен (без Play услуги/модул). */
enum class ScannerState { IDLE, OPEN, UNAVAILABLE }

/**
 * Скенер на баркодове: скенерът на Google ([BarcodeScanner]) разчита ISBN (EAN-13 с
 * 978/979), инвентарен номер (Code 39/128 етикет на библиотеката) или QR с адрес на
 * каталога, а тук се търси в каталога (индекса в базата; без него — в паметта).
 * Приложението получава само разчетения код — кадри няма.
 *
 * Ако каталогът не се зареди (няма връзка, а кешът е празен — напр. след „Изчисти кеша“),
 * екранът показва грешка с „Опитай отново“, а при връщане на връзката опитва сам
 * ([RefreshOnReconnect]). Сканираният междувременно код не се губи: последният се пази
 * ([waiting]) и се търси, щом каталогът се зареди. Код, който не е книга, не чака каталога.
 */
class ScanViewModel(private val repo: CatalogRepository, private val scanner: BarcodeScanner) : ViewModel(), RefreshOnReconnect {
    private val _result = MutableStateFlow<ScanResult?>(null)
    val result: StateFlow<ScanResult?> = _result.asStateFlow()
    private val _catalogStatus = MutableStateFlow(ScanCatalogStatus.LOADING)
    val catalogStatus: StateFlow<ScanCatalogStatus> = _catalogStatus.asStateFlow()
    private val _waiting = MutableStateFlow<ScanTarget?>(null)
    /** Последният код, сканиран без каталог — търси се, щом каталогът се зареди. */
    val waiting: StateFlow<ScanTarget?> = _waiting.asStateFlow()
    private val _scannerState = MutableStateFlow(ScannerState.IDLE)
    val scannerState: StateFlow<ScannerState> = _scannerState.asStateFlow()
    /** Скенерът е отварян сам (при първото показване на екрана) — не се отваря сам пак. */
    private var autoStarted = false

    /** Търсачката (data) и грешката при зареждането ѝ — само на главната нишка. */
    private var catalog = ScreenState<CatalogSearcher>()
    private var loading: Job? = null
    private var busy = false

    override val reconnectState: ScreenState<*> get() = catalog

    init {
        loadCatalog()
        // „Изчисти кеша“: търсачката към изтрития индекс се забравя, каталогът се зарежда наново.
        viewModelScope.launch {
            repo.cleared.collect {
                loading?.cancel()
                setCatalog(ScreenState())
                loadCatalog()
            }
        }
    }

    override fun refreshOnReconnect() = loadCatalog()

    /** „Опитай отново“ след неуспешно зареждане на каталога. */
    fun retry() = loadCatalog()

    private fun loadCatalog() {
        if (loading?.isActive == true || catalog.data != null) return
        setCatalog(catalog.startRefresh())
        loading = viewModelScope.launch {
            val searcher = repo.lookupSearcher()?.data
            setCatalog(if (searcher != null) ScreenState(searcher, loading = false) else ScreenState(loading = false, error = AppError.Network))
            resolveWaiting()
        }
    }

    private fun setCatalog(state: ScreenState<CatalogSearcher>) {
        catalog = state
        _catalogStatus.value = when {
            state.data != null -> ScanCatalogStatus.READY
            state.loading -> ScanCatalogStatus.LOADING
            else -> ScanCatalogStatus.UNAVAILABLE
        }
    }

    /**
     * Отваря скенера на Google ([activity] — екранът, над който се показва). [auto] — при
     * първото показване на екрана: тогава само веднъж (след „Назад“ от скенера екранът остава).
     * Без Play услуги или модул — [ScannerState.UNAVAILABLE] (екранът предлага ръчно въвеждане).
     */
    fun scan(activity: Context, auto: Boolean = false) {
        if (auto) {
            if (autoStarted) return
            autoStarted = true
        }
        if (_scannerState.value == ScannerState.OPEN || busy || _result.value != null) return
        _scannerState.value = ScannerState.OPEN
        viewModelScope.launch {
            val outcome = try {
                scanner.scan(activity)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                ScanOutcome.Unavailable
            }
            _scannerState.value = if (outcome == ScanOutcome.Unavailable) ScannerState.UNAVAILABLE else ScannerState.IDLE
            if (outcome is ScanOutcome.Code) onCode(outcome.raw)
        }
    }

    /** Разчетен код (от скенера). */
    fun onCode(raw: String) {
        if (raw.isBlank() || busy || _result.value != null) return
        handle(raw)
    }

    /** „Въведи номера на ръка“ (етикет, който не се чете, или без скенер): същата справка като при сканиране. */
    fun onManual(raw: String) = onCode(raw)

    private fun handle(raw: String) {
        busy = true
        viewModelScope.launch {
            try {
                val target = ScanCodes.resolve(raw)
                val cat = catalog.data
                when {
                    // Не е книга — „няма я“ и без каталог.
                    target is ScanTarget.Unknown -> { _result.value = ScanResult.Miss(target) }
                    cat == null -> { _waiting.value = target }
                    else -> lookup(target, cat)
                }
            } finally {
                busy = false
            }
        }
    }

    private suspend fun lookup(target: ScanTarget, searcher: CatalogSearcher) {
        var cat = searcher
        var book = find(target, cat)
        // Книга, добавена след последното сваляне на каталога: той се опреснява (най-много
        // веднъж за кратко време — поредните книги, които ги няма, не теглят мрежата) и се търси пак.
        if (book == null && target !is ScanTarget.Unknown) {
            repo.refreshForMissing()?.data?.let { fresh ->
                if (catalog.data != null) setCatalog(ScreenState(fresh, loading = false))
                cat = fresh
                book = find(target, fresh)
            }
        }
        val found = book
        _result.value = if (found != null) {
            val copies = when (target) {
                is ScanTarget.Isbn -> cat.allByIsbn(target.isbn).ifEmpty { listOf(found) }
                else -> listOf(found) + cat.copiesOf(found)
            }
            ScanResult.Hit(found, copies, target)
        } else {
            ScanResult.Miss(target)
        }
    }

    private suspend fun find(target: ScanTarget, cat: CatalogSearcher): CatalogBook? = when (target) {
        is ScanTarget.Isbn -> cat.findByIsbn(target.isbn)
        is ScanTarget.Inventory -> cat.book(target.inv)
        is ScanTarget.Unknown -> null
    }

    /** Запазеният код — щом каталогът е готов и няма отворен резултат. */
    private suspend fun resolveWaiting() {
        val cat = catalog.data ?: return
        val target = _waiting.value ?: return
        if (busy || _result.value != null) return
        _waiting.value = null
        busy = true
        try {
            lookup(target, cat)
        } finally {
            busy = false
        }
    }

    fun dismiss() {
        _result.value = null
        if (_waiting.value != null) viewModelScope.launch { resolveWaiting() }
    }
}
