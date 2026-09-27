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
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.MonthCalendar
import java.time.LocalDate
import org.chyavorec.domain.model.NewsArticle

data class HomeUiState(
    val news: ScreenState<List<NewsArticle>> = ScreenState(),
    val newBooks: List<CatalogBook> = emptyList(),
    val shelves: List<Pair<String, List<CatalogBook>>> = emptyList(),
    val catalogCount: Int? = null,
    val catalogGenerated: String? = null,
    val feast: DailyFeast? = null,
    val yearsSinceFounding: Int = 0,
    val eventsThisMonth: Int? = null,
    /** Датите от календара на сайта за текущия месец — сменя се сам с настъпването на новия месец. */
    val month: MonthCalendar? = null,
)

class HomeViewModel(
    private val news: NewsRepository,
    private val events: EventsRepository,
    private val catalog: CatalogRepository,
    private val site: SiteRepository,
    private val clock: AppClock,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(yearsSinceFounding = clock.today().year - FOUNDED))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Първо мигновено от кеша, после опресняване от мрежата.
            news.cached()?.let { c -> _state.update { it.copy(news = c.toState().copy(loading = true)) } }
            events.cachedRolled()?.let { c -> applyEvents(c.data) }
            catalog.cached()?.let { applyCatalog(it.data) }
            refresh(force = false)
        }
        viewModelScope.launch { site.feastToday()?.let { f -> _state.update { it.copy(feast = f) } } }
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(news = it.news.startRefresh()) }
            val n = async { news.latest(force) }
            val c = async { catalog.catalog(force) }
            val e = async { events.events(force) }
            _state.update { it.copy(news = it.news.with(n.await())) }
            (c.await() as? Outcome.Success)?.value?.data?.let { applyCatalog(it) }
            (e.await() as? Outcome.Success)?.value?.data?.let { applyEvents(it) }
        }
    }

    private var lastEvents: List<Event> = emptyList()
    private var computedFor: LocalDate? = null

    private fun applyEvents(list: List<Event>) {
        lastEvents = list
        val today = clock.today()
        computedFor = today
        val month = MonthCalendar.of(list, today)
        _state.update {
            it.copy(
                month = month,
                eventsThisMonth = month.items.size,
            )
        }
    }

    /**
     * При връщане в приложението: нов ден → „днес“ се мести; нов месец → показва
     * се календарът на новия месец и данните се опресняват от сайта.
     */
    fun onResume() {
        val today = clock.today()
        val last = computedFor ?: return
        if (today == last) return
        if (today.monthValue != last.monthValue || today.year != last.year) {
            viewModelScope.launch {
                (events.events(force = true) as? Outcome.Success)?.value?.data?.let { applyEvents(it) } ?: applyEvents(lastEvents)
            }
        } else {
            applyEvents(lastEvents)
        }
    }

    private fun applyCatalog(engine: CatalogSearchEngine) {
        _state.update {
            it.copy(
                newBooks = engine.newest(12),
                shelves = engine.shelfBooks(),
                catalogCount = engine.snapshot.books.size,
                catalogGenerated = engine.snapshot.generatedOn,
            )
        }
    }

    private companion object { const val FOUNDED = 1922 }
}
