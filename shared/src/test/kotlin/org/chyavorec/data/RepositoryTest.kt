package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.NewsService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RepositoryTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }

    private class FakeNews : NewsService {
        var calls = 0
        var result: Outcome<List<NewsArticle>> = Outcome.Success(listOf(NewsArticle("1", "Новина", "https://chyavorec.org/news/1")))
        override suspend fun fetchLatest(): Outcome<List<NewsArticle>> { calls++; return result }
        override suspend fun fetchArticle(article: NewsArticle): Outcome<ArticleDetail> = Outcome.Success(ArticleDetail(article, emptyList(), emptyList()))
    }

    @Test fun networkSuccessIsFresh() = runTest {
        val repo = NewsRepository(FakeNews(), cache, clock)
        val r = assertIs<Outcome.Success<*>>(repo.latest(force = true)).value as org.chyavorec.core.Synced<*>
        assertFalse(r.fromCache)
    }

    @Test fun offlineFallsBackToCacheAndMarksIt() = runTest {
        val svc = FakeNews()
        val repo = NewsRepository(svc, cache, clock)
        repo.latest(force = true)
        clock.advanceSeconds(3600)
        svc.result = Outcome.Failure(AppError.Network)
        val r = (repo.latest(force = true) as Outcome.Success).value
        assertTrue(r.fromCache)
        assertEquals(AppError.Network, r.refreshError)
        assertEquals(Instant.parse("2026-09-26T10:00:00Z"), r.syncedAt)
        assertEquals("Новина", r.data.single().title)
    }

    @Test fun noCacheAndNoNetworkIsFailure() = runTest {
        val svc = FakeNews().apply { result = Outcome.Failure(AppError.Network) }
        assertIs<Outcome.Failure>(NewsRepository(svc, cache, clock).latest(force = true))
    }

    @Test fun throttlesNonForcedRefresh() = runTest {
        val svc = FakeNews()
        val repo = NewsRepository(svc, cache, clock)
        repo.latest(force = false)
        repo.latest(force = false)
        assertEquals(1, svc.calls)
        clock.advanceSeconds(301)
        repo.latest(force = false)
        assertEquals(2, svc.calls)
        repo.latest(force = true)
        assertEquals(3, svc.calls)
    }

    @Test fun catalogOfflineKeepsLastSnapshotWithDate() = runTest {
        val raw = TestUtil.resource("katalog-sample.json")
        var fail = false
        val svc = object : CatalogService {
            override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> =
                if (fail) Outcome.Failure(AppError.Network) else Outcome.Success(raw to KatalogParser.parse(raw))
        }
        CatalogRepository(svc, cache, clock).catalog(force = true)
        fail = true
        // Нов процес: паметта е празна, но дисковият кеш остава.
        val fresh = CatalogRepository(svc, cache, clock)
        val r = (fresh.catalog(force = true) as Outcome.Success).value
        assertTrue(r.fromCache)
        assertEquals(60, r.data.snapshot.books.size)
        assertEquals("2026-09-19", r.data.snapshot.generatedOn)
    }

    private class CountingCatalog(val raw: String) : CatalogService {
        var fetches = 0
        var parses = 0
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
            fetches++; parses++
            return Outcome.Success(raw to KatalogParser.parse(raw))
        }
        override suspend fun fetchCatalogIfChanged(knownHash: String?): Outcome<Pair<String, CatalogSnapshot>?> {
            fetches++
            // Имитира GitHubCatalogService: същото съдържание не се разчита.
            if (knownHash != null) return Outcome.Success(null)
            parses++
            return Outcome.Success(raw to KatalogParser.parse(raw))
        }
    }

    @Test fun catalogDiskCopyStaysFreshAcrossRestart() = runTest {
        val svc = CountingCatalog(TestUtil.resource("katalog-sample.json"))
        CatalogRepository(svc, cache, clock).catalog(force = false)
        assertEquals(1, svc.fetches)
        clock.advanceSeconds(600)
        // Нов процес: дисковото копие е на 10 минути — без мрежа.
        val restarted = CatalogRepository(svc, cache, clock)
        assertTrue(restarted.cached()!!.fromCache)
        val r = (restarted.catalog(force = false) as Outcome.Success).value
        assertFalse(r.fromCache)
        assertEquals(1, svc.fetches)
        assertEquals(60, r.data.snapshot.books.size)
    }

    @Test fun catalogUnchangedContentIsNotReparsed() = runTest {
        val svc = CountingCatalog(TestUtil.resource("katalog-sample.json"))
        val repo = CatalogRepository(svc, cache, clock)
        val first = (repo.catalog(force = true) as Outcome.Success).value
        clock.advanceSeconds(3600)
        val second = (repo.catalog(force = false) as Outcome.Success).value
        assertEquals(2, svc.fetches)
        assertEquals(1, svc.parses)
        assertTrue(first.data === second.data, "същият индекс се преизползва")
        assertEquals(clock.now(), second.syncedAt)
    }
}
