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
import org.chyavorec.app.ui.screens.catalog.CatalogViewModel
import org.chyavorec.app.ui.screens.news.NewsListViewModel
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.CatalogSnapshot
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
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock), dispatcher)
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

    @Test fun catalogOfflineWithoutCacheShowsError() = runTest(dispatcher) {
        catalogService.fail = true
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock), dispatcher)
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
        val vm = NewsListViewModel(NewsRepository(service, InMemoryPayloadCache(), clock), favs, clock)
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
}
