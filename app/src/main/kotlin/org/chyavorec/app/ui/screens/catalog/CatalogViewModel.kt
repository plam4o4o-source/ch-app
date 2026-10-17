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
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField

data class CatalogUiState(
    /** Търсачката (пълнотекстовият индекс в базата или индексът в паметта) и състоянието ѝ. */
    val engine: ScreenState<CatalogSearcher> = ScreenState(),
    val query: CatalogQuery = CatalogQuery(),
    val results: List<CatalogBook> = emptyList(),
    val totalResults: Int = 0,
    val shown: Int = PAGE,
    val facets: CatalogFacets? = null,
    val suggestions: List<String> = emptyList(),
    val searching: Boolean = false,
) {
    /** Изчислява се веднъж за състояние (не при всяка рекомпозиция). */
    val page: List<CatalogBook> = results.take(shown)
    val canLoadMore: Boolean get() = shown < results.size

    companion object { const val PAGE = 40 }
}

/**
 * Търсене, поискано от друг екран (скенера, празничния поздрав) — каталогът го поема
 * при показване. [departmentHint] избира отдела, чието име го съдържа (напр. детския);
 * [newest] подрежда най-новите книги първи.
 */
data class CatalogSearchRequest(
    val text: String,
    val field: SearchField = SearchField.ALL,
    val departmentHint: String? = null,
    val newest: Boolean = false,
)

object CatalogSearchRequests {
    val pending = MutableStateFlow<CatalogSearchRequest?>(null)
}

@OptIn(FlowPreview::class)
class CatalogViewModel(
    private val repo: CatalogRepository,
    /** Търсенето и debounce-ът вървят извън главната нишка (тестовете подават свой диспечер). */
    private val searchDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel(), RefreshOnReconnect {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()
    private val queryFlow = MutableStateFlow(CatalogQuery())
    /** Последното изпълнено търсене: същата търсачка със същата заявка не се търси повторно. */
    @Volatile private var lastSearch: Pair<CatalogSearcher, CatalogQuery>? = null

    init {
        viewModelScope.launch {
            repo.cachedSearcher()?.let { c -> _state.update { it.copy(engine = c.toState().copy(loading = true), facets = c.data.facets) }; runSearch() }
            refresh(force = false)
        }
        // Нова търсачка без опресняване от екрана (напр. индексът в базата е попълнен във
        // фонов режим, докато се е търсило в стария) — поема се и търсенето се повтаря.
        viewModelScope.launch {
            repo.searcherUpdates.collect { s ->
                val shown = _state.value.engine
                if (shown.data == null || shown.data === s.data) return@collect
                _state.update {
                    it.copy(
                        engine = it.engine.copy(data = s.data, syncedAt = s.syncedAt, fromCache = s.fromCache, refreshError = s.refreshError),
                        facets = s.data.facets,
                    )
                }
                runSearch()
            }
        }
        // Instant search: търсим 200 ms след последния натиснат клавиш. Началната заявка,
        // вече изпълнена при зареждането, не се търси повторно (вж. [lastSearch]).
        viewModelScope.launch(searchDispatcher) { queryFlow.debounce(200).collect { runSearch() } }
    }

    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.copy(engine = it.engine.startRefresh()) }
        val searched = repo.searcher(force)
        // Ако междувременно е публикувана по-нова търсачка (фоново попълване) — тя, не по-старата.
        val r = if (searched is Outcome.Success) repo.publishedSearcher()?.let { Outcome.Success(it) } ?: searched else searched
        _state.update { s -> s.copy(engine = s.engine.with(r), facets = (r as? Outcome.Success)?.value?.data?.facets ?: s.facets) }
        runSearch()
    }

    override val reconnectState: ScreenState<*> get() = _state.value.engine
    override fun refreshOnReconnect() { refresh(false) }

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
        val last = lastSearch
        if (last != null && last.first === engine && last.second == q) {
            // Напр. опресняване без нови данни — резултатите (и страницирането) остават.
            _state.update { if (it.query == q) it.copy(searching = false) else it }
            return
        }
        val (results, suggestions) = withContext(searchDispatcher) {
            engine.search(q) to (if (q.text.length >= 2 && q.field != SearchField.INVENTORY && q.field != SearchField.ISBN) engine.suggestions(q.text, 6) else emptyList())
        }
        var applied = false
        _state.update {
            if (it.query != q) it // по-нова заявка вече е в ход
            else {
                applied = true
                it.copy(results = results, totalResults = results.size, shown = CatalogUiState.PAGE, suggestions = suggestions, searching = false)
            }
        }
        if (applied) lastSearch = engine to q
    }
}

class BookViewModel(private val inv: Long, private val container: org.chyavorec.app.di.AppContainer) : ViewModel(), RefreshOnReconnect {
    data class BookUi(
        val book: CatalogBook? = null,
        /** Каталогът е прочетен и книгата е потърсена в него (`book == null` — наистина я няма). */
        val loaded: Boolean = false,
        /** Каталогът не е наличен (първо пускане без връзка, уиджет/връзка преди зареждане). */
        val error: AppError? = null,
        val copies: List<CatalogBook> = emptyList(),
        val sameAuthor: List<CatalogBook> = emptyList(),
        val generatedOn: String? = null,
        val syncedAt: java.time.Instant? = null,
        val fromCache: Boolean = false,
        /** Защо последното опресняване на каталога не е успяло (за лентата — без лъжливо „няма интернет“). */
        val refreshError: AppError? = null,
        val canHold: Boolean = false,
        val liveStatus: org.chyavorec.domain.model.BookStatus? = null,
    ) {
        val loading: Boolean get() = !loaded && error == null
    }

    val ui = MutableStateFlow(BookUi())
    val holdResult = MutableStateFlow<Outcome<Unit>?>(null)

    init { load() }

    /** „Опитай отново“ и връщане на връзката — само ако каталогът не е бил наличен. */
    fun retry() {
        if (ui.value.loading || ui.value.loaded) return
        load()
    }

    override val reconnectState: ScreenState<*>
        get() = ui.value.let { ScreenState(data = it.book, loading = it.loading, error = it.error) }

    override fun refreshOnReconnect() = retry()

    private fun load() {
        ui.update { it.copy(error = null) }
        viewModelScope.launch {
            // През индекса в базата (без мрежа и без да се разчита целият каталог). Без
            // никакъв кеш — каталогът от мрежата; неуспехът е „няма данни“, а не „няма книга“.
            var synced = container.catalogRepository.lookupSearcher(network = false)
            if (synced == null) {
                when (val r = container.catalogRepository.catalog(false)) {
                    is Outcome.Failure -> {
                        ui.update { it.copy(error = r.error) }
                        return@launch
                    }
                    is Outcome.Success -> synced = container.catalogRepository.lookupSearcher(network = false)
                }
            }
            val found = synced
            if (found == null) {
                ui.update { it.copy(error = AppError.Parse("katalog.json")) }
                return@launch
            }
            val catalog = found.data
            val book = catalog.book(inv)
            ui.value = BookUi(
                book = book,
                loaded = true,
                copies = book?.let { catalog.copiesOf(it) }.orEmpty(),
                sameAuthor = book?.let { catalog.byAuthor(it.author, it.inv) }.orEmpty(),
                generatedOn = catalog.generatedOn,
                syncedAt = found.syncedAt,
                fromCache = found.fromCache,
                refreshError = found.refreshError,
            )
            if (book == null) return@launch
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
