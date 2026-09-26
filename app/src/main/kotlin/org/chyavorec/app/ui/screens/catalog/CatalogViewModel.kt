package org.chyavorec.app.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField

data class CatalogUiState(
    val engine: ScreenState<CatalogSearchEngine> = ScreenState(),
    val query: CatalogQuery = CatalogQuery(),
    val results: List<CatalogBook> = emptyList(),
    val totalResults: Int = 0,
    val shown: Int = PAGE,
    val facets: CatalogFacets? = null,
    val suggestions: List<String> = emptyList(),
    val searching: Boolean = false,
) {
    val page: List<CatalogBook> get() = results.take(shown)
    val canLoadMore: Boolean get() = shown < results.size

    companion object { const val PAGE = 40 }
}

@OptIn(FlowPreview::class)
class CatalogViewModel(
    private val repo: CatalogRepository,
    /** Търсенето и debounce-ът вървят извън главната нишка (тестовете подават свой диспечер). */
    private val searchDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()
    private val queryFlow = MutableStateFlow(CatalogQuery())

    init {
        viewModelScope.launch {
            repo.cached()?.let { c -> _state.update { it.copy(engine = c.toState().copy(loading = true), facets = c.data.facets) }; runSearch() }
            refresh(force = false)
        }
        // Instant search: търсим 200 ms след последния натиснат клавиш.
        viewModelScope.launch(searchDispatcher) { queryFlow.debounce(200).collect { runSearch() } }
    }

    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.copy(engine = it.engine.startRefresh()) }
        val r = repo.catalog(force)
        _state.update { s -> s.copy(engine = s.engine.with(r), facets = (r as? Outcome.Success)?.value?.data?.facets ?: s.facets) }
        runSearch()
    }

    fun update(transform: (CatalogQuery) -> CatalogQuery) {
        val q = transform(_state.value.query)
        _state.update { it.copy(query = q, searching = true) }
        queryFlow.value = q
    }

    fun setText(text: String) = update { it.copy(text = text) }
    fun setField(field: SearchField) = update { it.copy(field = field) }
    fun setSort(sort: CatalogSort) = update { it.copy(sort = sort) }
    fun clearFilters() = update { CatalogQuery(text = it.text, field = it.field, sort = it.sort) }
    fun loadMore() = _state.update { it.copy(shown = it.shown + CatalogUiState.PAGE) }

    private suspend fun runSearch() {
        val engine = _state.value.engine.data ?: return
        val q = _state.value.query
        val (results, suggestions) = withContext(searchDispatcher) {
            engine.search(q) to (if (q.text.length >= 2 && q.field != SearchField.INVENTORY && q.field != SearchField.ISBN) engine.suggestions(q.text, 6) else emptyList())
        }
        _state.update {
            if (it.query != q) it // по-нова заявка вече е в ход
            else it.copy(results = results, totalResults = results.size, shown = CatalogUiState.PAGE, suggestions = suggestions, searching = false)
        }
    }
}

class BookViewModel(private val inv: Long, private val container: org.chyavorec.app.di.AppContainer) : ViewModel() {
    data class BookUi(
        val book: CatalogBook? = null,
        val loaded: Boolean = false,
        val copies: List<CatalogBook> = emptyList(),
        val sameAuthor: List<CatalogBook> = emptyList(),
        val generatedOn: String? = null,
        val syncedAt: java.time.Instant? = null,
        val fromCache: Boolean = false,
        val canHold: Boolean = false,
        val liveStatus: org.chyavorec.domain.model.BookStatus? = null,
    )

    val ui = MutableStateFlow(BookUi())
    val holdResult = MutableStateFlow<Outcome<Unit>?>(null)

    init {
        viewModelScope.launch {
            val synced = container.catalogRepository.inMemory() ?: container.catalogRepository.cached()
                ?: (container.catalogRepository.catalog(false) as? Outcome.Success)?.value
            val engine = synced?.data
            val book = engine?.book(inv)
            ui.value = BookUi(
                book = book,
                loaded = true,
                copies = book?.let { engine.copiesOf(it) }.orEmpty(),
                sameAuthor = book?.let { engine.byAuthor(it.author, it.inv) }.orEmpty(),
                generatedOn = engine?.snapshot?.generatedOn,
                syncedAt = synced?.syncedAt,
                fromCache = synced?.fromCache ?: false,
            )
            val caps = container.authRepository.capabilities()
            // „Заяви книгата“ се показва САМО ако библиотечният сървър го поддържа.
            ui.update { it.copy(canHold = caps.holds) }
            if (caps.availability) {
                (container.readerServices.reader.availability(inv) as? Outcome.Success)?.value?.let { s -> ui.update { it.copy(liveStatus = s) } }
            }
        }
    }

    fun placeHold() = viewModelScope.launch { holdResult.value = container.libraryRepository.placeHold(inv) }
}
