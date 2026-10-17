package org.chyavorec.app.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.domain.model.CatalogBook

/** Резултат от сканиране: книга от каталога или „няма я“. */
sealed interface ScanResult {
    data class Hit(val book: CatalogBook, val copies: List<CatalogBook>, val target: ScanTarget) : ScanResult
    data class Miss(val target: ScanTarget) : ScanResult
}

/**
 * Скенер на баркодове: разчита ISBN (EAN-13 с 978/979), инвентарен номер
 * (Code 39/128 етикет на библиотеката) или QR с адрес на каталога и търси в
 * каталога (индекса в базата; без него — в паметта). Всичко става на устройството —
 * кадрите не се записват.
 */
class ScanViewModel(private val c: AppContainer) : ViewModel() {
    private val _result = MutableStateFlow<ScanResult?>(null)
    val result: StateFlow<ScanResult?> = _result.asStateFlow()
    private val _catalogReady = MutableStateFlow(false)
    val catalogReady: StateFlow<Boolean> = _catalogReady.asStateFlow()

    @Volatile private var catalog: CatalogSearcher? = null
    @Volatile private var lastCode: String? = null
    @Volatile private var lastAt: Long = 0L
    @Volatile private var busy = false

    init {
        viewModelScope.launch {
            catalog = c.catalogRepository.lookupSearcher()?.data
            _catalogReady.value = catalog != null
        }
    }

    /** Извиква се от анализатора (фонова нишка). Еднакъв код се приема най-много веднъж на 3 s. */
    fun onCode(raw: String) {
        if (raw.isBlank() || busy || _result.value != null) return
        val now = c.clock.now().toEpochMilli()
        if (raw == lastCode && now - lastAt < DEBOUNCE_MS) return
        lastCode = raw; lastAt = now
        val cat = catalog ?: return
        busy = true
        // Справката е заявка към базата — извън нишката на анализатора.
        viewModelScope.launch {
            try {
                val target = ScanCodes.resolve(raw)
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
            } finally {
                busy = false
            }
        }
    }

    fun dismiss() {
        _result.value = null
        // Същият код може да се сканира отново след кратка пауза (не веднага, докато листът се затваря).
        lastAt = c.clock.now().toEpochMilli()
    }

    companion object { const val DEBOUNCE_MS = 3_000L }
}
