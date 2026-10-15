package org.chyavorec.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.ForYouPicks
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.data.repository.LibraryRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogHomeSummary
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.occasion.SiteOccasion
import java.time.LocalDate

data class HomeUiState(
    val news: ScreenState<List<NewsArticle>> = ScreenState(),
    val upcoming: List<Event> = emptyList(),
    val newBooks: List<CatalogBook> = emptyList(),
    val shelves: List<Pair<String, List<CatalogBook>>> = emptyList(),
    /** Полицата „За теб“ (празна — не се показва). */
    val forYou: List<CatalogBook> = emptyList(),
    /** `true` — по историята на читателя; иначе „Най-четени и нови“. */
    val forYouPersonal: Boolean = false,
    val catalogCount: Int? = null,
    val catalogGenerated: String? = null,
    val feast: DailyFeast? = null,
    val yearsSinceFounding: Int = 0,
    val eventsThisMonth: Int? = null,
    /** Местните празници от сайта (за украсата): кешът веднага, после — от сайта. */
    val occasions: List<SiteOccasion> = emptyList(),
)

class HomeViewModel(
    private val news: NewsRepository,
    private val events: EventsRepository,
    private val catalog: CatalogRepository,
    private val site: SiteRepository,
    private val clock: AppClock,
    private val library: LibraryRepository,
    private val auth: AuthRepository,
    /** Включени ли са украсите — местните празници се теглят само тогава. */
    seasonal: Flow<Boolean>,
    /** Скритият с „×“ поздрав (вж. SettingsStore.dismissedOccasion). */
    dismissedOccasion: Flow<String?>,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(yearsSinceFounding = clock.today().year - FOUNDED))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    /**
     * Скритият днес поздрав — тук, а не в екрана: ViewModel-ът оцелява при смяна на таба,
     * така че при връщане към „Начало“ поздравът е известен още в първия кадър.
     * `null` = още се чете; "" = нищо не е скрито.
     */
    val dismissed: StateFlow<String?> = dismissedOccasion.map { it.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Обобщението на каталога — за „За теб“ (преизчислява се при всяко ново). */
    private val summary = MutableStateFlow<CatalogHomeSummary?>(null)

    /** Денят, за който е зареден празникът (при нов ден — наново). */
    private var feastDay: LocalDate? = null

    init {
        viewModelScope.launch {
            // Първо мигновено от кеша, после опресняване от мрежата.
            news.cached()?.let { c -> _state.update { it.copy(news = c.toState().copy(loading = true)) } }
            events.cachedRolled()?.let { c -> applyEvents(c.data) }
            // Само малкото обобщение — индексът за търсене се строи едва в каталога/търсенето.
            catalog.homeSummary()?.let { applyCatalog(it.data) }
            refresh(force = false)
        }
        loadFeast(clock.today())
        viewModelScope.launch {
            seasonal.distinctUntilChanged().collectLatest { on -> if (on) loadOccasions() }
        }
        viewModelScope.launch {
            // Запазената сесия (локално, без мрежа) — за да е ясно дали „За теб“ е лична.
            if (auth.state.value is AuthState.Unknown) auth.restore()
        }
        viewModelScope.launch {
            // „За теб“: при нов каталог, вход/изход, нова история или нова търсачка (попълнен индекс).
            // Само от кеша — без собствени заявки; collectLatest отменя остарялото изчисление.
            combine(
                summary.filterNotNull(),
                auth.state.filter { it !is AuthState.Unknown },
                library.historyVersion,
                catalog.searcherUpdates.map { }.onStart { emit(Unit) },
            ) { s, a, _, _ -> s to a }
                .collectLatest { (s, a) -> computeForYou(s, a) }
        }
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(news = it.news.startRefresh()) }
            val n = async { news.latest(force) }
            val c = async { catalog.refreshHomeSummary(force) }
            val e = async { events.events(force) }
            _state.update { it.copy(news = it.news.with(n.await())) }
            (c.await() as? Outcome.Success)?.value?.data?.let { applyCatalog(it) }
            (e.await() as? Outcome.Success)?.value?.data?.let { applyEvents(it) }
        }
    }

    /** Нов ден (след полунощ или при връщане към приложението) — празникът за деня наново. */
    fun onDay(today: LocalDate) {
        if (today != feastDay) loadFeast(today)
    }

    private fun loadFeast(day: LocalDate) {
        feastDay = day
        // Без празник за новия ден (няма връзка/кеш) редът изчезва — вчерашният би бил грешен.
        viewModelScope.launch { site.feastToday().let { f -> _state.update { it.copy(feast = f) } } }
    }

    /** Местните празници: кешът веднага (обикновено преди първия кадър), после сайтът (12 ч. кеш). */
    private suspend fun loadOccasions() {
        runCatching { site.cachedOccasions() }.getOrNull()?.let { c -> _state.update { it.copy(occasions = c) } }
        try {
            val fresh = site.occasions()
            _state.update { if (it.occasions == fresh) it else it.copy(occasions = fresh) }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Без местни празници — само общите от календара.
        }
    }

    private fun applyEvents(list: List<Event>) {
        val today = clock.today()
        val horizon = today.plusDays(30).toString()
        _state.update {
            it.copy(
                upcoming = events.upcoming(list).take(8),
                eventsThisMonth = list.count { e -> (e.date ?: "") in today.toString()..horizon },
            )
        }
    }

    private fun applyCatalog(summary: CatalogHomeSummary) {
        _state.update {
            it.copy(
                newBooks = summary.newest,
                shelves = summary.shelves.map { shelf -> shelf.name to shelf.books },
                catalogCount = summary.count,
                catalogGenerated = summary.generatedOn,
            )
        }
        this.summary.value = summary
    }

    /**
     * Полицата „За теб“. За влязъл читател с кеширана история — препоръките по нея (през
     * индекса в базата, без мрежа); за всички останали (или ако няма търсачка) —
     * „Най-четени и нови“ от обобщението. Без книгите, които вече са в другите редове.
     */
    private suspend fun computeForYou(s: CatalogHomeSummary, a: AuthState) {
        val onHome = HashSet<Long>().apply {
            s.newest.forEach { add(it.inv) }
            s.shelves.forEach { shelf -> shelf.books.forEach { add(it.inv) } }
        }
        val general = withContext(Dispatchers.Default) { ForYouPicks.general(s.picks, s.shelves.map { it.books }, exclude = onHome) }
        val personal = if (a is AuthState.SignedIn) personalPicks(general, onHome)?.takeIf { it.isNotEmpty() } else null
        _state.update { it.copy(forYou = personal ?: general, forYouPersonal = personal != null) }
    }

    /** `null` — няма история в кеша, няма търсачка или препоръки (тогава — общият подбор). */
    private suspend fun personalPicks(general: List<CatalogBook>, onHome: Set<Long>): List<CatalogBook>? = try {
        val history = library.cachedHistory()?.data.orEmpty()
        if (history.isEmpty()) {
            null
        } else {
            // Публикуваната търсачка, иначе индексът в базата — без мрежа (network = false).
            val searcher = catalog.publishedSearcher()?.data ?: catalog.lookupSearcher(network = false)?.data
            searcher?.let {
                withContext(Dispatchers.Default) {
                    val recs = it.recommend(history, limit = ForYouPicks.LIMIT * 2)
                    val read = history.mapNotNullTo(HashSet()) { h -> h.inv }
                    if (recs.isEmpty()) null else ForYouPicks.personal(recs, general, exclude = onHome + read)
                }
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }

    private companion object { const val FOUNDED = 1922 }
}
