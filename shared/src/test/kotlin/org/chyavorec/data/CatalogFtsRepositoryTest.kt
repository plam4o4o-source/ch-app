package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.FtsCatalogSearcher
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.SearchField
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** Хранилището на каталога с пълнотекстовия индекс: кога се попълва и кога се ползва. */
class CatalogFtsRepositoryTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
    private val store = FakeFtsIndexStore()
    private val sample = TestUtil.resource("katalog-sample.json")

    private class Service(var raw: String) : CatalogService {
        var fail = false
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> =
            if (fail) Outcome.Failure(AppError.Network) else Outcome.Success(raw to KatalogParser.parse(raw))
    }

    private fun repo(svc: CatalogService) = CatalogRepository(svc, cache, clock, index = store)

    @Test fun searchGoesThroughTheIndexWithoutBuildingTheEngine() = runTest {
        val repo = repo(Service(sample))
        val s = assertIs<Outcome.Success<*>>(repo.searcher()).value as org.chyavorec.core.Synced<*>
        val searcher = assertIs<FtsCatalogSearcher>(s.data)
        assertFalse(s.fromCache)
        assertNull(repo.inMemory(), "индексът в паметта не се строи")
        assertEquals(1, store.replaces)
        assertEquals(60, store.rows.size)
        assertEquals("2026-09-19", searcher.generatedOn)
        assertTrue(searcher.facets.docTypes.isNotEmpty())
        assertEquals(listOf(399666L), searcher.search(CatalogQuery("Джиан", SearchField.AUTHOR)).map { it.inv })
        assertEquals(60, searcher.search(CatalogQuery()).size)
    }

    @Test fun indexIsPopulatedOnlyWhenTheCatalogHashChanges() = runTest {
        val svc = Service(sample)
        val first = (repo(svc).searcher(force = true) as Outcome.Success).value.data
        // Ново стартиране, същото съдържание от мрежата — индексът не се пренаписва.
        val restarted = repo(svc)
        val cached = restarted.cachedSearcher()!!
        assertTrue(cached.fromCache)
        assertIs<FtsCatalogSearcher>(cached.data)
        val again = (restarted.searcher(force = true) as Outcome.Success).value
        assertSame(cached.data, again.data, "същата търсачка при непроменен каталог")
        assertEquals(1, store.replaces)
        assertIs<FtsCatalogSearcher>(first)
        // Променено съдържание → индексът се попълва отново.
        svc.raw = sample.replace("Ох...", "Ах...")
        val changed = (restarted.searcher(force = true) as Outcome.Success).value.data
        assertEquals(2, store.replaces)
        assertEquals(listOf(399666L), changed.search(CatalogQuery("ах")).map { it.inv })
        assertTrue(changed.search(CatalogQuery("ох")).isEmpty())
    }

    @Test fun homeRefreshDoesNotIndexButTheCatalogScreenDoes() = runTest {
        val repo = repo(Service(sample))
        repo.refreshHomeSummary(force = true)
        assertEquals(0, store.replaces)
        assertIs<FtsCatalogSearcher>(repo.cachedSearcher()!!.data)
        assertEquals(1, store.replaces)
    }

    @Test fun diskCacheFromOlderVersionIsIndexedOnFirstSearch() = runTest {
        CatalogRepository(Service(sample), cache, clock).catalog(force = true)
        assertEquals(0, store.replaces)
        val upgraded = repo(Service(sample).apply { fail = true })
        val s = upgraded.cachedSearcher()!!
        assertIs<FtsCatalogSearcher>(s.data)
        assertTrue(s.fromCache)
        assertEquals(1, store.replaces)
        // Офлайн: последният каталог остава, с грешката от опресняването.
        val r = (upgraded.searcher(force = true) as Outcome.Success).value
        assertEquals(AppError.Network, r.refreshError)
        assertEquals(1, store.replaces)
    }

    @Test fun brokenDatabaseFallsBackToTheInMemoryEngine() = runTest {
        store.failing = true
        val repo = repo(Service(sample))
        val s = (repo.searcher() as Outcome.Success).value
        val searcher = assertIs<EngineCatalogSearcher>(s.data)
        assertEquals(listOf(399666L), searcher.search(CatalogQuery("Джиан")).map { it.inv })
        repo.trimMemory()
        assertIs<EngineCatalogSearcher>(repo.cachedSearcher()!!.data)
    }

    @Test fun withoutIndexTheEngineIsUsed() = runTest {
        val repo = CatalogRepository(Service(sample), cache, clock)
        val s = (repo.searcher() as Outcome.Success).value
        assertIs<EngineCatalogSearcher>(s.data)
        assertEquals(60, s.data.search(CatalogQuery()).size)
    }

    @Test fun offlineWithoutAnythingFails() = runTest {
        val repo = repo(Service(sample).apply { fail = true })
        assertEquals(AppError.Network, (repo.searcher() as Outcome.Failure).error)
        assertNull(repo.cachedSearcher())
    }
}
