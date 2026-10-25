package org.chyavorec.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.chyavorec.TestUtil
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [CatalogRepository.latestLookup] — наличността за „Искам да прочета“ след фоново опресняване:
 * от новия каталог (изчаква попълването на индекса), а не от стария индекс.
 */
class CatalogLatestLookupTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
    private val store = FakeFtsIndexStore()
    private val sample = TestUtil.resource("katalog-sample.json")

    private class Service(var raw: String) : CatalogService {
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> = Outcome.Success(raw to KatalogParser.parse(raw))
    }

    private fun TestScope.repo(svc: CatalogService) = CatalogRepository(
        svc, cache, clock, work = StandardTestDispatcher(testScheduler), index = store,
        scope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)),
    )

    @Test fun waitsForTheNewCatalogIndex() = runTest {
        val svc = Service(sample)
        val repo = repo(svc)
        repo.refreshHomeSummary(force = true)
        advanceUntilIdle()
        assertEquals(true, repo.latestLookup()!!.data.book(399666L)?.available)

        // Новият каталог: книгата вече не е на рафта; индексът се попълва бавно.
        store.replaceGate = CompletableDeferred()
        svc.raw = sample.replaceFirst("\"av\": 1,", "\"av\": 0,")
        repo.refreshHomeSummary(force = true)
        val lookup = async { repo.latestLookup()!! }
        runCurrent()
        assertFalse(lookup.isCompleted, "чака новия индекс, не отговаря от стария")
        store.replaceGate!!.complete(Unit)
        advanceUntilIdle()
        val fresh = lookup.await()
        assertFalse(fresh.fromCache)
        assertEquals(false, fresh.data.book(399666L)?.available)
    }

    @Test fun withoutIndexingInProgressAnswersAtOnce() = runTest {
        val repo = repo(Service(sample))
        repo.refreshHomeSummary(force = true)
        advanceUntilIdle()
        val s = repo.latestLookup()!!
        assertTrue(s.data.books(listOf(399666L, 399560L)).values.all { it.available })
    }
}
