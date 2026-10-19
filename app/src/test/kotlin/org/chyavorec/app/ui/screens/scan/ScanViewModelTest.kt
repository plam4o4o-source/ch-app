package org.chyavorec.app.ui.screens.scan

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.TestResources
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.wantsReconnectRefresh
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.core.ScanTarget
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Скенерът без връзка и без кеш на каталога (напр. веднага след „Изчисти кеша“): не виси
 * на „Зареждам каталога…“, а показва грешка; сканираният код не се губи и се търси, щом
 * каталогът се зареди — с „Опитай отново“ или сам, при връщане на връзката.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @get:Rule val main = MainDispatcherRule(dispatcher)
    private val clock = FixedClock(java.time.Instant.parse("2026-09-26T10:00:00Z"))

    private class Service : CatalogService {
        var online = false
        var fetches = 0
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
            fetches++
            if (!online) return Outcome.Failure(AppError.Network)
            val raw = TestResources.text("katalog-sample.json")
            return Outcome.Success(raw to KatalogParser.parse(raw))
        }
    }

    private fun repo(service: CatalogService) = CatalogRepository(service, InMemoryPayloadCache(), clock, dispatcher)

    @Test fun offlineWithoutCacheShowsAnErrorAndKeepsTheScannedCodeUntilReconnect() = runTest(dispatcher) {
        val service = Service()
        val vm = ScanViewModel(repo(service), clock)
        assertEquals(ScanCatalogStatus.LOADING, vm.catalogStatus.value)
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.UNAVAILABLE, vm.catalogStatus.value, "грешка, не безкраен „Зареждам каталога…“")
        assertIs<RefreshOnReconnect>(vm)
        assertTrue(vm.reconnectState.wantsReconnectRefresh(), "при връщане на връзката опитва сам")

        vm.onCode("399666")
        advanceUntilIdle()
        assertNull(vm.result.value)
        assertEquals(ScanTarget.Inventory(399666), vm.waiting.value, "кодът е запазен")

        // Връзката се върна.
        service.online = true
        vm.refreshOnReconnect()
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.READY, vm.catalogStatus.value)
        assertNull(vm.waiting.value)
        val hit = assertIs<ScanResult.Hit>(vm.result.value, "запазеният код е потърсен")
        assertEquals(399666L, hit.book.inv)
        assertFalse(vm.reconnectState.wantsReconnectRefresh(), "каталогът е готов — без повторно зареждане")
    }

    @Test fun retryLoadsTheCatalogAndLaterCodesAreLookedUpDirectly() = runTest(dispatcher) {
        val service = Service()
        val vm = ScanViewModel(repo(service), clock)
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.UNAVAILABLE, vm.catalogStatus.value)
        vm.retry()
        assertEquals(ScanCatalogStatus.LOADING, vm.catalogStatus.value)
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.UNAVAILABLE, vm.catalogStatus.value, "пак без връзка — пак грешка")

        service.online = true
        vm.retry()
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.READY, vm.catalogStatus.value)
        assertNull(vm.result.value, "нищо не е чакало")
        val fetches = service.fetches
        vm.retry()
        advanceUntilIdle()
        assertEquals(fetches, service.fetches, "готовият каталог не се зарежда пак")

        vm.onCode("399666")
        advanceUntilIdle()
        assertEquals(399666L, assertIs<ScanResult.Hit>(vm.result.value).book.inv)
        vm.dismiss()
        vm.onCode("12345678")
        advanceUntilIdle()
        assertEquals(ScanTarget.Inventory(12345678), assertIs<ScanResult.Miss>(vm.result.value).target)
    }

    @Test fun codesThatAreNotBooksDoNotWaitForTheCatalog() = runTest(dispatcher) {
        val service = Service()
        val vm = ScanViewModel(repo(service), clock)
        advanceUntilIdle()
        vm.onCode("https://example.com/x")
        advanceUntilIdle()
        assertIs<ScanTarget.Unknown>(assertIs<ScanResult.Miss>(vm.result.value).target)
        assertNull(vm.waiting.value)
    }

    @Test fun clearingTheCacheReloadsTheCatalog() = runTest(dispatcher) {
        val service = Service().apply { online = true }
        val repo = repo(service)
        val vm = ScanViewModel(repo, clock)
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.READY, vm.catalogStatus.value)

        service.online = false
        repo.clear()
        advanceUntilIdle()
        assertEquals(ScanCatalogStatus.UNAVAILABLE, vm.catalogStatus.value, "без търсачка към изчистения каталог")
        vm.onCode("399666")
        advanceUntilIdle()
        assertEquals(ScanTarget.Inventory(399666), vm.waiting.value)
        service.online = true
        vm.refreshOnReconnect()
        advanceUntilIdle()
        assertEquals(399666L, assertIs<ScanResult.Hit>(vm.result.value).book.inv)
    }
}
