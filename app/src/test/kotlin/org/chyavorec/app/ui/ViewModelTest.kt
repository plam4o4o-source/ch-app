package org.chyavorec.app.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import org.chyavorec.app.InMemoryFavoritesDao
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.TestResources
import org.chyavorec.app.data.local.FavoriteNewsEntity
import org.chyavorec.app.ui.screens.catalog.CatalogUiState
import org.chyavorec.app.ui.screens.catalog.CatalogViewModel
import org.chyavorec.app.ui.screens.news.ArticleViewModel
import org.chyavorec.app.ui.screens.news.NewsListViewModel
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SearchField
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.NewsService
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @get:Rule val main = MainDispatcherRule(dispatcher)
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    private val catalogService = object : CatalogService {
        var fail = false
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
            if (fail) return Outcome.Failure(AppError.Network)
            val raw = TestResources.text("katalog-sample.json")
            return Outcome.Success(raw to KatalogParser.parse(raw))
        }
    }

    @Test fun catalogSearchIsDebouncedAndFiltered() = runTest(dispatcher) {
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher)
        advanceUntilIdle()
        val initial = vm.state.value
        assertEquals(60, initial.totalResults)
        assertNotNull(initial.facets)
        vm.setField(SearchField.AUTHOR)
        vm.setText("Джиан")
        advanceTimeBy(50)
        assertTrue(vm.state.value.searching, "преди debounce все още търси")
        advanceUntilIdle()
        val s = vm.state.value
        assertEquals(listOf(399666L), s.results.map { it.inv })
        assertTrue(!s.searching)
    }

    /** Пак избраната подредба или „Изчисти“ без филтри не оставят „Търся…“ завинаги. */
    @Test fun catalogUnchangedQueryDoesNotStartSearching() = runTest(dispatcher) {
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher)
        advanceUntilIdle()
        vm.setSort(CatalogSort.TITLE)
        advanceUntilIdle()
        val searchId = vm.state.value.searchId
        vm.setSort(CatalogSort.TITLE)
        assertTrue(!vm.state.value.searching, "същата подредба")
        vm.clearFilters()
        assertTrue(!vm.state.value.searching, "„Изчисти“ без филтри")
        advanceUntilIdle()
        assertTrue(!vm.state.value.searching)
        assertEquals(searchId, vm.state.value.searchId)
        assertEquals(60, vm.state.value.totalResults)
    }

    /**
     * Опресненият във фонов режим каталог (нов katalog.json, нова търсачка) пази заявката и
     * заредените страници — списъкът не скача в началото; само новата заявка го връща там.
     */
    @Test fun catalogRefreshKeepsLoadedPagesAndQuery() = runTest(dispatcher) {
        var suffix = ""
        val service = object : CatalogService {
            override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
                // Различни байтове (друг SHA-256) — каталогът е „обновен“ и търсачката е нова.
                val raw = TestResources.text("katalog-sample.json") + suffix
                return Outcome.Success(raw to KatalogParser.parse(raw))
            }
        }
        val vm = CatalogViewModel(CatalogRepository(service, InMemoryPayloadCache(), clock, dispatcher), dispatcher)
        advanceUntilIdle()
        vm.setSort(CatalogSort.TITLE)
        advanceUntilIdle()
        val before = vm.state.value
        assertEquals(60, before.totalResults)
        assertEquals(CatalogUiState.PAGE, before.shown)
        vm.loadMore()
        advanceUntilIdle()
        assertEquals(2 * CatalogUiState.PAGE, vm.state.value.shown)
        val searchId = vm.state.value.searchId

        suffix = "\n"
        vm.refresh()
        advanceUntilIdle()
        val after = vm.state.value
        assertTrue(after.engine.data !== before.engine.data, "опресняването дава нова търсачка")
        assertEquals(CatalogQuery(sort = CatalogSort.TITLE), after.query)
        assertEquals(2 * CatalogUiState.PAGE, after.shown, "заредените страници остават")
        assertEquals(before.results.map { it.inv }, after.page.map { it.inv })
        assertEquals(searchId, after.searchId, "без връщане в началото")

        // Нова заявка — първата страница и връщане в началото.
        vm.setSort(CatalogSort.AUTHOR)
        advanceUntilIdle()
        assertEquals(CatalogUiState.PAGE, vm.state.value.shown)
        assertEquals(searchId + 1, vm.state.value.searchId)
    }

    /** След „Изчисти кеша“ каталогът се зарежда наново — без старите резултати и без срив. */
    @Test fun catalogReloadsCleanlyAfterTheCacheIsCleared() = runTest(dispatcher) {
        var fetches = 0
        val service = object : CatalogService {
            override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
                fetches++
                val raw = TestResources.text("katalog-sample.json")
                return Outcome.Success(raw to KatalogParser.parse(raw))
            }
        }
        val cache = InMemoryPayloadCache()
        val repo = CatalogRepository(service, cache, clock, dispatcher)
        val vm = CatalogViewModel(repo, dispatcher)
        advanceUntilIdle()
        vm.setField(SearchField.AUTHOR)
        vm.setText("Джиан")
        advanceUntilIdle()
        val engine = vm.state.value.engine.data
        assertEquals(listOf(399666L), vm.state.value.results.map { it.inv })
        assertEquals(1, fetches)

        repo.clear()
        cache.clear()
        advanceUntilIdle()
        val s = vm.state.value
        assertEquals(2, fetches, "каталогът е свален наново")
        assertTrue(s.engine.data !== engine, "нова търсачка, не изчистената")
        assertEquals(CatalogQuery(text = "Джиан", field = SearchField.AUTHOR), s.query, "заявката остава")
        assertEquals(listOf(399666L), s.results.map { it.inv })
        assertTrue(!s.searching && s.engine.error == null)
    }

    @Test fun catalogOfflineWithoutCacheShowsError() = runTest(dispatcher) {
        catalogService.fail = true
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher)
        advanceUntilIdle()
        assertEquals(AppError.Network, vm.state.value.engine.error)
        catalogService.fail = false
    }

    @Test fun newsFilteringAndFavorites() = runTest(dispatcher) {
        val service = object : NewsService {
            override suspend fun fetchLatest() = Outcome.Success(
                listOf(
                    NewsArticle("1", "Концерт за 1 ноември", "u1", category = "Събития"),
                    NewsArticle("2", "Нови книги", "u2", category = "Библиотека"),
                ),
            )
            override suspend fun fetchArticle(article: NewsArticle) = Outcome.Success(ArticleDetail(article, emptyList(), emptyList()))
        }
        val favs = InMemoryFavoritesDao()
        val vm = NewsListViewModel(NewsRepository(service, InMemoryPayloadCache(), clock, dispatcher), favs, clock, dispatcher)
        val job = launch { vm.ui.collect {} }
        advanceUntilIdle()
        assertEquals(2, vm.ui.value.visible.size)
        assertEquals(listOf("Събития", "Библиотека"), vm.ui.value.categories)
        vm.setCategory("Библиотека"); advanceUntilIdle()
        assertEquals(listOf("2"), vm.ui.value.visible.map { it.id })
        vm.setCategory(null); vm.setQuery("концерт"); advanceUntilIdle()
        assertEquals(listOf("1"), vm.ui.value.visible.map { it.id })
        vm.toggleFavorite(vm.ui.value.visible.first(), isFavorite = false); advanceUntilIdle()
        assertEquals(listOf("1"), favs.observeIds().first())
        vm.setQuery(""); vm.setFavoritesOnly(true); advanceUntilIdle()
        assertEquals(listOf("1"), vm.ui.value.visible.map { it.id })
        job.cancel()
    }

    /** Звездата на новина, отворена по адрес (App Link), е същата като от списъка/известието. */
    @Test fun articleFavoriteIsTheSameWhicheverWayItIsOpened() = runTest(dispatcher) {
        val url = "https://chyavorec.org/news/2026-10-01-sabor"
        val service = object : NewsService {
            override suspend fun fetchLatest() = Outcome.Success(listOf(NewsArticle("guid-7", "Събор", url)))
            override suspend fun fetchArticle(article: NewsArticle) = Outcome.Success(ArticleDetail(article, emptyList(), emptyList()))
        }
        val favs = InMemoryFavoritesDao()
        val repo = NewsRepository(service, InMemoryPayloadCache(), clock, dispatcher)
        // Запазена от списъка — по идентификатора си.
        val list = NewsListViewModel(repo, favs, clock, dispatcher)
        val listJob = launch { list.ui.collect {} }
        advanceUntilIdle()
        list.toggleFavorite(list.ui.value.visible.single(), isFavorite = false); advanceUntilIdle()
        assertEquals(listOf("guid-7"), favs.items.value.map { it.id })

        // Отворена по адрес — с www, http, котва и крайна черта: пак е любима.
        val byLink = ArticleViewModel("http://www.chyavorec.org/news/2026-10-01-sabor/#comments", repo, favs, clock)
        val linkJob = launch { byLink.isFavorite.collect {} }
        advanceUntilIdle()
        assertTrue(byLink.isFavorite.value, "любимата от списъка е любима и по адрес")
        // Премахване оттук маха същия запис, а не добавя втори.
        byLink.toggleFavorite(); advanceUntilIdle()
        assertTrue(favs.items.value.isEmpty())
        assertTrue(!byLink.isFavorite.value)
        byLink.toggleFavorite(); advanceUntilIdle()
        assertEquals(listOf("guid-7"), favs.items.value.map { it.id }, "записва се по идентификатора, не по адреса")

        // От известие (по идентификатор) — същата звезда.
        val byId = ArticleViewModel("guid-7", repo, favs, clock)
        val idJob = launch { byId.isFavorite.collect {} }
        advanceUntilIdle()
        assertTrue(byId.isFavorite.value)
        assertEquals(setOf("guid-7"), list.ui.value.favoriteIds)
        listOf(listJob, linkJob, idJob).forEach { it.cancel() }
    }

    /** Любима новина, която вече я няма в news.json, се отваря и по адрес (от любимите). */
    @Test fun articleByLinkFallsBackToTheFavoriteByUrl() = runTest(dispatcher) {
        val service = object : NewsService {
            override suspend fun fetchLatest() = Outcome.Success(emptyList<NewsArticle>())
            override suspend fun fetchArticle(article: NewsArticle) = Outcome.Success(ArticleDetail(article, emptyList(), emptyList()))
        }
        val favs = InMemoryFavoritesDao()
        favs.upsert(FavoriteNewsEntity("guid-old", "Стара", "https://chyavorec.org/news/old", null, "", null, null, 0))
        val vm = ArticleViewModel("https://chyavorec.org/news/old", NewsRepository(service, InMemoryPayloadCache(), clock, dispatcher), favs, clock)
        val job = launch { vm.isFavorite.collect {} }
        advanceUntilIdle()
        assertEquals("guid-old", vm.state.value.data?.article?.id)
        assertTrue(vm.isFavorite.value)
        job.cancel()
    }
}
