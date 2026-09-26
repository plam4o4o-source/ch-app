package org.chyavorec.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle

data class HomeUiState(
    val news: ScreenState<List<NewsArticle>> = ScreenState(),
    val upcoming: List<Event> = emptyList(),
    val newBooks: List<CatalogBook> = emptyList(),
    val shelves: List<Pair<String, List<CatalogBook>>> = emptyList(),
    val catalogCount: Int? = null,
    val catalogGenerated: String? = null,
)

class HomeViewModel(
    private val news: NewsRepository,
    private val events: EventsRepository,
    private val catalog: CatalogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Първо мигновено от кеша, после опресняване от мрежата.
            news.cached()?.let { c -> _state.update { it.copy(news = c.toState().copy(loading = true)) } }
            events.cached()?.let { c -> _state.update { it.copy(upcoming = events.upcoming(c.data).take(6)) } }
            catalog.cached()?.let { applyCatalog(it.data) }
            refresh(force = false)
        }
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(news = it.news.startRefresh()) }
            val n = async { news.latest(force) }
            val c = async { catalog.catalog(force) }
            val newsResult = n.await()
            _state.update { it.copy(news = it.news.with(newsResult)) }
            (c.await() as? Outcome.Success)?.value?.data?.let { applyCatalog(it) }
            (events.events(force = false) as? Outcome.Success)?.value?.data?.let { list ->
                _state.update { it.copy(upcoming = events.upcoming(list).take(6)) }
            }
        }
    }

    private fun applyCatalog(engine: org.chyavorec.data.catalog.CatalogSearchEngine) {
        _state.update {
            it.copy(
                newBooks = engine.newest(12),
                shelves = engine.shelfBooks(),
                catalogCount = engine.snapshot.books.size,
                catalogGenerated = engine.snapshot.generatedOn,
            )
        }
    }
}
