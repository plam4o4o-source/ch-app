package org.chyavorec.app.ui

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.screens.catalog.CatalogViewModel
import org.chyavorec.app.ui.screens.search.GlobalSearchViewModel
import org.chyavorec.app.ui.screens.search.SearchScope
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Търсенето оцелява след спиране на процеса във фонов режим: заявката (текст, поле, филтри,
 * подредба) е в SavedStateHandle, а новият ViewModel със същия (възстановен) SavedStateHandle
 * я показва и търси наново — не празно поле с целия каталог.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SavedSearchStateTest {
    private val dispatcher = StandardTestDispatcher()
    @get:Rule val main = MainDispatcherRule(dispatcher)
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    private val catalogService = object : CatalogService {
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
            val raw = TestResources.text("katalog-sample.json")
            return Outcome.Success(raw to KatalogParser.parse(raw))
        }
    }

    /** Както при възстановяване след спиране на процеса: само записаните стойности, в нов SavedStateHandle. */
    private fun restored(saved: SavedStateHandle) = SavedStateHandle(saved.keys().associateWith { saved.get<Any>(it) })

    @Test fun catalogQueryAndFiltersSurviveProcessDeath() = runTest(dispatcher) {
        val saved = SavedStateHandle()
        val before = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher, saved)
        advanceUntilIdle()
        assertEquals(60, before.state.value.totalResults)
        before.setField(SearchField.AUTHOR)
        before.setSort(CatalogSort.TITLE)
        before.setText("Джиан")
        advanceUntilIdle()
        assertEquals(listOf(399666L), before.state.value.results.map { it.inv })

        // Нов процес: нов кеш, ново хранилище — и възстановеният SavedStateHandle.
        val after = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher, restored(saved))
        val expected = CatalogQuery(text = "Джиан", field = SearchField.AUTHOR, sort = CatalogSort.TITLE)
        assertEquals(expected, after.state.value.query, "заявката е там още преди зареждането")
        advanceUntilIdle()
        val s = after.state.value
        assertEquals(expected, s.query)
        assertEquals(listOf(399666L), s.results.map { it.inv }, "търси се наново, не целият каталог")
        assertEquals(1, s.totalResults)
        assertTrue(!s.searching)
    }

    @Test fun catalogStartsEmptyWithoutSavedState() = runTest(dispatcher) {
        // Повреден запис (напр. от по-стара версия) — празна заявка, без срив.
        val saved = SavedStateHandle(mapOf("catalog.query" to "{not json"))
        val vm = CatalogViewModel(CatalogRepository(catalogService, InMemoryPayloadCache(), clock, dispatcher), dispatcher, saved)
        advanceUntilIdle()
        assertEquals(CatalogQuery(), vm.state.value.query)
        assertEquals(60, vm.state.value.totalResults)
    }

    @Test fun globalSearchQueryAndScopeSurviveProcessDeath() {
        val config = AppConfig(
            siteBaseUrl = "http://127.0.0.1:9", catalogUrls = listOf("http://127.0.0.1:9/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(ApplicationProvider.getApplicationContext(), config, clock, XorTestCipher(), OkHttpClient())
        val saved = SavedStateHandle()
        // Зареждането в init не се пуска (тестовият диспечер не се върти) — проверява се само състоянието.
        val before = GlobalSearchViewModel(container, saved)
        assertEquals("", before.ui.value.query)
        before.setQuery("Левски")
        before.setScope(SearchScope.BOOKS)

        val after = GlobalSearchViewModel(container, restored(saved))
        val ui = after.ui.value
        assertEquals("Левски", ui.query)
        assertEquals(SearchScope.BOOKS, ui.scope)
        assertTrue(ui.searching, "търсенето се пуска наново")
        // Без фоново търсене след края на теста (debounce-ът върви на Dispatchers.Default).
        listOf(before, after).forEach { it.viewModelScope.cancel() }
    }
}
