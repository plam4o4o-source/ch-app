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
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.ui.RefreshOnReconnect
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
) : ViewModel(), RefreshOnReconnect {

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
        // Празникът за деня (обикновено от кеша) — в първата промяна, ако е готов дотогава.
        feastDay = clock.today()
        val feast = viewModelScope.async { site.feastToday() }
        viewModelScope.launch {
            // Първо мигновено от кеша (трите едновременно и с ЕДНА промяна на състоянието —
            // една прекомпозиция вместо по една за всеки източник), после — мрежата.
            val cachedNews = async { news.cached() }
            val cachedEvents = async { events.cachedRolled() }
            // Само малкото обобщение — индексът за търсене се строи едва в каталога/търсенето.
            val cachedSummary = async { catalog.homeSummary() }
            val n = cachedNews.await()
            val e = cachedEvents.await()
            val batch = Batch()
            // Началото на опресняването е в същата промяна (иначе още една веднага след нея).
            batch.add { s -> (n?.let { s.copy(news = it.toState().copy(loading = true)) } ?: s).withEvents(e?.data).let { it.copy(news = it.news.startRefresh()) } }
            // Обобщението (от диска) обикновено идва миг след новините — изчаква се за кратко
            // ([COALESCE_MS]), за да влезе в същата промяна; ако закъснее (строи се от стар кеш),
            // новините и събитията не го чакат. Празникът — само ако вече е готов (може да е от
            // мрежата, а опресняването не бива да чака нея).
            withTimeoutOrNull(COALESCE_MS) { cachedSummary.join() }
            val feastNow = feast.isCompleted
            if (feastNow) feast.await().let { f -> batch.add { it.copy(feast = f) } }
            if (!cachedSummary.isCompleted) batch.flush()
            cachedSummary.await()?.let { c -> batch.add(summary = c.data) { it.withCatalog(c.data) } }
            batch.flush()
            refresh(force = false, started = true)
            // Празникът, ако е закъснял за първата промяна — отделно, щом дойде.
            if (!feastNow) launch { feast.await().let { f -> _state.update { it.copy(feast = f) } } }
        }
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

    /** [started] — началото на опресняването вече е в състоянието (първото зареждане). */
    fun refresh(force: Boolean = true, started: Boolean = false) {
        viewModelScope.launch {
            if (!started) _state.update { it.copy(news = it.news.startRefresh()) }
            val n = async { news.latest(force) }
            val c = async { catalog.refreshHomeSummary(force) }
            val e = async { events.events(force) }
            // В реда както досега (новините, каталогът, събитията), но готовото към момента
            // влиза в същата промяна на състоянието; нищо не чака по-бавния източник.
            val batch = Batch()
            val newsResult = n.await()
            batch.add { it.copy(news = it.news.with(newsResult)) }
            withTimeoutOrNull(COALESCE_MS) { c.join(); e.join() }
            if (!c.isCompleted) batch.flush()
            (c.await() as? Outcome.Success)?.value?.data?.let { fresh -> batch.add(summary = fresh) { it.withCatalog(fresh) } }
            if (!e.isCompleted) batch.flush()
            (e.await() as? Outcome.Success)?.value?.data?.let { fresh -> batch.add { it.withEvents(fresh) } }
            batch.flush()
        }
    }

    /**
     * Натрупани промени на състоянието, приложени наведнъж ([flush]) — една емисия (и една
     * прекомпозиция на началния екран) вместо по една за всеки източник. Новото обобщение
     * на каталога се подава за „За теб“ едва след като редовете му са в състоянието.
     */
    private inner class Batch {
        private var change: ((HomeUiState) -> HomeUiState)? = null
        private var newSummary: CatalogHomeSummary? = null

        fun add(summary: CatalogHomeSummary? = null, transform: (HomeUiState) -> HomeUiState) {
            val previous = change
            change = if (previous == null) transform else { s -> transform(previous(s)) }
            if (summary != null) newSummary = summary
        }

        fun flush() {
            change?.let { c -> _state.update(c) }
            change = null
            newSummary?.let { this@HomeViewModel.summary.value = it }
            newSummary = null
        }
    }

    override val reconnectState: ScreenState<*> get() = _state.value.news
    override fun refreshOnReconnect() = refresh(force = false)

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

    /** Събитията в състоянието (`null` — без промяна). */
    private fun HomeUiState.withEvents(list: List<Event>?): HomeUiState {
        if (list == null) return this
        val today = clock.today()
        val horizon = today.plusDays(30).toString()
        return copy(
            upcoming = events.upcoming(list).take(8),
            eventsThisMonth = list.count { e -> (e.date ?: "") in today.toString()..horizon },
        )
    }

    /** Обобщението на каталога в състоянието (`null` — без промяна); „За теб“ — след него ([summary]). */
    private fun HomeUiState.withCatalog(summary: CatalogHomeSummary?): HomeUiState {
        if (summary == null) return this
        return copy(
            newBooks = summary.newest,
            shelves = summary.shelves.map { shelf -> shelf.name to shelf.books },
            catalogCount = summary.count,
            catalogGenerated = summary.generatedOn,
        )
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

    private companion object {
        const val FOUNDED = 1922

        /** Колко най-много се чака закъснял източник, за да влезе в същата промяна (≈ 6 кадъра). */
        const val COALESCE_MS = 100L
    }
}
