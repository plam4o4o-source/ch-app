package org.chyavorec.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.FtsCatalogSearcher
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.SearchField
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogFetch
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.CatalogValidators
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
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

    /** Мрежата „виси“, докато [gate] не се отвори; брои заявките. */
    private class SlowService(raw: String) : CatalogService {
        private val inner = Service(raw)
        val gate = CompletableDeferred<Unit>()
        var fetches = 0
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> = inner.fetchCatalog()
        override suspend fun fetchCatalogConditional(known: CatalogValidators?): Outcome<CatalogFetch> {
            fetches++
            gate.await()
            return super.fetchCatalogConditional(known)
        }
    }

    /**
     * Всичко (и фоновата работа) върви на тестовия планировчик — детерминирано. Обхватът
     * не е backgroundScope: advanceUntilIdle изпълнява само „предните“ задачи.
     */
    private fun TestScope.repo(svc: CatalogService, index: FakeFtsIndexStore? = store) = CatalogRepository(
        svc, cache, clock, work = StandardTestDispatcher(testScheduler), index = index,
        scope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)),
    )

    @Test fun searchGoesThroughTheIndexWithoutBuildingTheEngine() = runTest {
        val repo = repo(Service(sample))
        val s = assertIs<Outcome.Success<*>>(repo.searcher()).value as Synced<*>
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
        // Променено съдържание → индексът се попълва отново (във фонов режим).
        svc.raw = sample.replace("Ох...", "Ах...")
        restarted.searcher(force = true)
        advanceUntilIdle()
        assertEquals(2, store.replaces)
        val changed = restarted.cachedSearcher()!!.data
        assertEquals(listOf(399666L), changed.search(CatalogQuery("ах")).map { it.inv })
        assertTrue(changed.search(CatalogQuery("ох")).isEmpty())
    }

    @Test fun changedCatalogIsIndexedInTheBackgroundWhileTheOldIndexIsServed() = runTest {
        val svc = Service(sample)
        val old = (repo(svc).searcher(force = true) as Outcome.Success).value.data
        val repo = repo(svc)
        val updates = mutableListOf<Synced<CatalogSearcher>>()
        backgroundScope.launch { repo.searcherUpdates.collect { updates += it } }
        store.replaceGate = CompletableDeferred()
        // Новият каталог: сменено заглавие и нов инв. №.
        svc.raw = sample.replace("Ох...", "Ах...").replace("\"inv\": 399666,", "\"inv\": 399667,")

        // Екранът „Каталог“ не чака попълването: старият индекс, отбелязан като кеширан.
        val stale = withTimeout(1_000) { (repo.searcher(force = true) as Outcome.Success).value }
        assertTrue(stale.fromCache)
        assertIs<FtsCatalogSearcher>(stale.data)
        assertEquals(listOf(399666L), stale.data.search(CatalogQuery("ох")).map { it.inv }, "старият индекс — цял, не празен")
        assertEquals(old.facets, stale.data.facets)
        assertEquals(1, store.replaces)
        // Справките (книга, скенер) не чакат попълването: книгата от стария индекс — веднага.
        val lookups = withTimeout(1_000) { repo.lookupSearcher()!! }
        assertTrue(lookups.fromCache)
        assertEquals("Ох...", withTimeout(1_000) { lookups.data.book(399666L) }?.title?.trim('"'))
        assertEquals(399560L, withTimeout(1_000) { lookups.data.book(399560L) }?.inv)
        // Книгата, която я няма в стария индекс, изчаква новия — но не мрежата.
        val added = async { lookups.data.book(399667L) }
        runCurrent()
        assertFalse(added.isCompleted)
        assertEquals(1, store.replaces)

        store.replaceGate!!.complete(Unit)
        advanceUntilIdle()
        assertEquals(2, store.replaces)
        assertTrue(added.await()!!.title.contains("Ах..."))
        val fresh = repo.cachedSearcher()!!
        assertFalse(fresh.fromCache)
        assertSame(fresh.data, updates.last().data, "новата търсачка е обявена на екрана")
        assertEquals(listOf(399667L), fresh.data.search(CatalogQuery("ах")).map { it.inv })
        assertSame(fresh.data, repo.lookupSearcher()!!.data, "справките — вече през новия индекс")
        assertNull(repo.inMemory(), "индексът в паметта не се строи")
    }

    /**
     * Потвърждението, че каталогът не е сменен (на всяко опресняване), сменя само датата:
     * търсачката не се обявява наново — „За теб“ и екранът „Каталог“ не смятат напразно.
     */
    @Test fun unchangedCatalogDoesNotAnnounceANewSearcher() = runTest {
        val repo = repo(Service(sample))
        val first = (repo.searcher(force = true) as Outcome.Success).value.data
        advanceUntilIdle()
        val updates = mutableListOf<Synced<CatalogSearcher>>()
        backgroundScope.launch { repo.searcherUpdates.collect { updates += it } }
        runCurrent()
        assertEquals(1, updates.size)
        repeat(3) {
            repo.refreshHomeSummary(force = true)
            repo.searcher(force = true)
            advanceUntilIdle()
        }
        runCurrent()
        assertEquals(1, updates.size, "само датата е нова — търсачката е същата")
        assertSame(first, repo.publishedSearcher()!!.data)
        assertEquals(1, store.replaces)
    }

    @Test fun homeRefreshIndexesInTheBackground() = runTest {
        val repo = repo(Service(sample))
        repo.refreshHomeSummary(force = true)
        advanceUntilIdle()
        assertEquals(1, store.replaces, "новият каталог се индексира веднага, не при първото търсене")
        assertIs<FtsCatalogSearcher>(repo.cachedSearcher()!!.data)
        assertEquals(1, store.replaces)
        assertNull(repo.inMemory())
    }

    @Test fun readersOfCachedDataDoNotWaitForTheNetwork() = runTest {
        repo(Service(sample)).searcher(force = true)
        val svc = SlowService(sample.replace("Ох...", "Ах..."))
        val repo = repo(svc)
        val refresh = async { repo.searcher(force = true) }
        val engine = async { repo.catalog(force = true) }
        runCurrent()
        assertEquals(1, svc.fetches, "едно сваляне за двете опреснявания")
        assertFalse(refresh.isCompleted)
        withTimeout(1_000) {
            assertNotNull(repo.cachedSearcher())
            assertNotNull(repo.homeSummary())
            assertTrue(repo.lookupSearcher()!!.data.book(399666L)!!.title.contains("Ох..."))
            assertNotNull(repo.cached())
        }
        svc.gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(1, svc.fetches)
        assertIs<Outcome.Success<*>>(refresh.await())
        assertIs<Outcome.Success<*>>(engine.await())
        assertTrue(repo.lookupSearcher()!!.data.book(399666L)!!.title.contains("Ах..."))
    }

    @Test fun lookupsGoThroughTheIndexWithoutParsingTheCatalog() = runTest {
        repo(Service(sample)).searcher(force = true)
        val restarted = repo(Service(sample).apply { fail = true })
        val s = restarted.lookupSearcher()!!
        assertIs<FtsCatalogSearcher>(s.data)
        assertTrue(s.fromCache)
        assertNotNull(s.data.book(399666L))
        assertNull(restarted.inMemory(), "индексът в паметта не се строи")
        assertEquals(1, store.replaces)
    }

    @Test fun lookupsFallBackToTheEngineWithoutAnIndex() = runTest {
        val repo = repo(Service(sample), index = null)
        val s = repo.lookupSearcher()!!
        assertIs<EngineCatalogSearcher>(s.data)
        assertNotNull(s.data.book(399666L))
        // С развалена база — пак индексът в паметта.
        store.failing = true
        val broken = repo(Service(sample))
        assertIs<EngineCatalogSearcher>(broken.lookupSearcher()!!.data)
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
        assertNull(repo.lookupSearcher())
    }

    /** „Изчисти кеша“: файлът, записаното до него, обобщението и индексът — после всичко наново. */
    @Test fun clearRemovesTheCatalogTheIndexAndTheSummary() = runTest {
        val svc = SlowService(sample).also { it.gate.complete(Unit) }
        val repo = repo(svc)
        assertIs<Outcome.Success<*>>(repo.searcher())
        assertIs<Outcome.Success<*>>(repo.refreshHomeSummary())
        assertNotNull(store.state)
        assertNotNull(cache.read(CatalogRepository.KEY))
        val fetches = svc.fetches
        var cleared = 0
        val watcher = launch { repo.cleared.collect { cleared++ } }
        runCurrent()

        repo.clear()
        runCurrent()
        assertEquals(1, cleared, "екранът на каталога научава за изчистването")
        listOf(CatalogRepository.KEY, CatalogRepository.META_KEY, CatalogRepository.FETCHED_AT_KEY, CatalogRepository.SUMMARY_KEY)
            .forEach { assertNull(cache.read(it), it) }
        assertNull(store.state, "индексът в базата е празен")
        assertTrue(store.rows.isEmpty())
        assertNull(repo.inMemory())
        assertNull(repo.publishedSearcher())
        assertNull(repo.homeSummary(), "без обобщение (и „За теб“) от стария каталог")
        assertNull(repo.cachedSearcher(), "без стара търсачка")
        assertNull(repo.lookupSearcher(network = false))

        // Следващото отваряне сваля каталога наново (без условна заявка — копие няма) и попълва индекса.
        val s = assertIs<Outcome.Success<*>>(repo.searcher()).value as Synced<*>
        advanceUntilIdle()
        assertEquals(fetches + 1, svc.fetches)
        assertIs<FtsCatalogSearcher>(s.data)
        assertEquals(60, store.rows.size)
        assertNotNull(cache.read(CatalogRepository.KEY))
        assertEquals(60, (s.data as CatalogSearcher).search(CatalogQuery()).size)
        assertNotNull(repo.homeSummary())
        watcher.cancel()
    }

    /** Изчистване по време на попълването на индекса: спряното попълване не връща стария каталог. */
    @Test fun clearDuringIndexPopulationLeavesNoStaleRows() = runTest {
        val gate = CompletableDeferred<Unit>()
        store.replaceGate = gate
        val repo = repo(Service(sample))
        // Само обобщението — индексът се попълва във фонов режим и чака [gate].
        assertIs<Outcome.Success<*>>(repo.refreshHomeSummary())
        runCurrent()
        assertNull(store.state)
        repo.clear()
        gate.complete(Unit)
        advanceUntilIdle()
        assertNull(store.state, "спряното попълване не записва")
        assertTrue(store.rows.isEmpty())
        assertNull(cache.read(CatalogRepository.META_KEY))
        assertNull(repo.lookupSearcher(network = false))
    }

    /**
     * Изчистването, прекъснато по средата (излизане от „Настройки“, докато базата се свива):
     * то пак стига до края — без търсачка и хеш в паметта към изтрития индекс, а екранът на
     * каталога научава за него. Следващото отваряне сваля каталога наново.
     */
    @Test fun clearCancelledMidwayStillForgetsTheCatalog() = runTest {
        val svc = SlowService(sample).also { it.gate.complete(Unit) }
        val repo = repo(svc)
        assertIs<Outcome.Success<*>>(repo.searcher())
        assertIs<Outcome.Success<*>>(repo.refreshHomeSummary())
        assertNotNull(repo.publishedSearcher())
        val fetches = svc.fetches
        var cleared = 0
        val watcher = launch { repo.cleared.collect { cleared++ } }
        runCurrent()
        val gate = CompletableDeferred<Unit>()
        store.clearGate = gate

        val clearing = launch { repo.clear() }
        runCurrent()
        assertNull(store.state, "индексът е изпразнен, изчистването още тече")
        clearing.cancel()
        runCurrent()
        gate.complete(Unit)
        advanceUntilIdle()
        assertTrue(clearing.isCancelled)
        assertEquals(1, cleared, "екранът на каталога научава за изчистването")
        listOf(CatalogRepository.KEY, CatalogRepository.META_KEY, CatalogRepository.FETCHED_AT_KEY, CatalogRepository.SUMMARY_KEY)
            .forEach { assertNull(cache.read(it), it) }
        assertNull(repo.publishedSearcher(), "без търсачка към изтрития индекс")
        assertNull(repo.inMemory())
        assertNull(repo.homeSummary())
        assertNull(repo.cachedSearcher())
        assertNull(repo.lookupSearcher(network = false))

        // Следващото търсене сваля каталога наново (не „потвърждава“ изчистения със 304).
        val s = assertIs<Outcome.Success<*>>(repo.searcher()).value as Synced<*>
        advanceUntilIdle()
        assertEquals(fetches + 1, svc.fetches)
        assertEquals(60, (s.data as CatalogSearcher).search(CatalogQuery()).size)
        assertNotNull(repo.lookupSearcher(network = false)!!.data.book(399666L))
        watcher.cancel()
    }

    /**
     * Базата на индекса е създадена наново (празна) под публикуваната търсачка: търсенето
     * и справките не връщат празно като вярно — минават през индекса в паметта веднага, а
     * индексът се попълва отново във фонов режим от кеширания katalog.json (без мрежа).
     */
    @Test fun lostIndexFallsBackToTheEngineAndIsRebuilt() = runTest {
        val svc = SlowService(sample).also { it.gate.complete(Unit) }
        val repo = repo(svc)
        val fts = assertIs<FtsCatalogSearcher>((repo.searcher() as Outcome.Success).value.data)
        val updates = mutableListOf<Synced<CatalogSearcher>>()
        backgroundScope.launch { repo.searcherUpdates.collect { updates += it } }
        runCurrent()
        assertEquals(1, store.replaces)
        val fetches = svc.fetches

        store.lose()
        // Попълването чака — дотогава всичко минава през индекса в паметта.
        val gate = CompletableDeferred<Unit>()
        store.replaceGate = gate
        assertEquals(listOf(399666L), fts.search(CatalogQuery("Джиан", SearchField.AUTHOR)).map { it.inv })
        assertEquals(399666L, fts.book(399666L)?.inv)
        assertEquals(60, fts.searchFirst(CatalogQuery(), 20).total)
        runCurrent()
        assertTrue(store.lost)
        assertNull(repo.publishedSearcher(), "търсачката към изгубения индекс е забравена")

        gate.complete(Unit)
        advanceUntilIdle()
        assertFalse(store.lost, "индексът е попълнен отново")
        assertEquals(2, store.replaces)
        assertEquals(60, store.rows.size)
        assertEquals(fetches, svc.fetches, "от кеширания katalog.json — без мрежа")
        val fresh = assertIs<FtsCatalogSearcher>(repo.cachedSearcher()!!.data)
        // Наблюдателят е в backgroundScope — advanceUntilIdle не чака фоновите задачи.
        runCurrent()
        assertTrue(fresh !== fts, "нова търсачка вместо тази към изгубения индекс")
        assertSame(fresh, updates.last().data, "новата търсачка е обявена на екрана")
        store.lookups.clear()
        assertEquals(399666L, repo.lookupSearcher(network = false)!!.data.book(399666L)?.inv)
        assertEquals(listOf("book"), store.lookups, "справката — пак през индекса в базата")
    }
}
