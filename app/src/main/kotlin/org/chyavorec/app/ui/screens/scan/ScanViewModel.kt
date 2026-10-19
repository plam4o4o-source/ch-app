package org.chyavorec.app.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppClock
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

/**
 * Скенер на баркодове: разчита ISBN (EAN-13 с 978/979), инвентарен номер
 * (Code 39/128 етикет на библиотеката) или QR с адрес на каталога и търси в
 * каталога (индекса в базата; без него — в паметта). Всичко става на устройството —
 * кадрите не се записват.
 *
 * Ако каталогът не се зареди (няма връзка, а кешът е празен — напр. след „Изчисти кеша“),
 * екранът показва грешка с „Опитай отново“, а при връщане на връзката опитва сам
 * ([RefreshOnReconnect]). Сканираният междувременно код не се губи: последният се пази
 * ([waiting]) и се търси, щом каталогът се зареди. Код, който не е книга, не чака каталога.
 */
class ScanViewModel(private val repo: CatalogRepository, private val clock: AppClock) : ViewModel(), RefreshOnReconnect {
    private val _result = MutableStateFlow<ScanResult?>(null)
    val result: StateFlow<ScanResult?> = _result.asStateFlow()
    private val _catalogStatus = MutableStateFlow(ScanCatalogStatus.LOADING)
    val catalogStatus: StateFlow<ScanCatalogStatus> = _catalogStatus.asStateFlow()
    private val _waiting = MutableStateFlow<ScanTarget?>(null)
    /** Последният код, сканиран без каталог — търси се, щом каталогът се зареди. */
    val waiting: StateFlow<ScanTarget?> = _waiting.asStateFlow()

    /** Търсачката (data) и грешката при зареждането ѝ — само на главната нишка. */
    private var catalog = ScreenState<CatalogSearcher>()
    private var loading: Job? = null
    @Volatile private var lastCode: String? = null
    @Volatile private var lastAt: Long = 0L
    @Volatile private var busy = false

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

    /** Извиква се от анализатора (фонова нишка). Еднакъв код се приема най-много веднъж на 3 s. */
    fun onCode(raw: String) {
        if (raw.isBlank() || busy || _result.value != null) return
        val now = clock.now().toEpochMilli()
        if (raw == lastCode && now - lastAt < DEBOUNCE_MS) return
        lastCode = raw; lastAt = now
        busy = true
        // Справката е заявка към базата — извън нишката на анализатора; състоянието се пипа на главната.
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

    private suspend fun lookup(target: ScanTarget, cat: CatalogSearcher) {
        val book = when (target) {
            is ScanTarget.Isbn -> cat.findByIsbn(target.isbn)
            is ScanTarget.Inventory -> cat.book(target.inv)
            is ScanTarget.Unknown -> null
        }
        _result.value = if (book != null) {
            val copies = when (target) {
                is ScanTarget.Isbn -> cat.allByIsbn(target.isbn).ifEmpty { listOf(book) }
                else -> listOf(book) + cat.copiesOf(book)
            }
            ScanResult.Hit(book, copies, target)
        } else {
            ScanResult.Miss(target)
        }
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
        // Същият код може да се сканира отново след кратка пауза (не веднага, докато листът се затваря).
        lastAt = clock.now().toEpochMilli()
        if (_waiting.value != null) viewModelScope.launch { resolveWaiting() }
    }

    companion object { const val DEBOUNCE_MS = 3_000L }
}
