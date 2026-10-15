package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.TestUtil
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.Fetched
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Непромененият news.json / index.json (същият SHA-256) не се разчита повторно. */
class SiteUnchangedTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")

    @Volatile private var news = TestUtil.resource("site-news.json")
    @Volatile private var index = TestUtil.resource("site-index.json")
    @Volatile private var rssOk = true

    @BeforeTest fun start() {
        server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                    "/data/news.json" -> MockResponse().setBody(news)
                    "/rss.xml" -> if (rssOk) MockResponse().setBody(TestUtil.resource("site-rss.xml")) else MockResponse().setResponseCode(503)
                    "/javora/index.json" -> MockResponse().setBody(index)
                    else -> MockResponse().setResponseCode(404)
                }
            }
            start()
        }
    }

    @AfterTest fun stop() = server.shutdown()

    private fun site(): ChyavorecSiteService {
        val base = server.url("/").toString().trimEnd('/')
        val fallback = Contacts("НЧ", null, emptyList(), emptyList(), emptyList(), base, null, emptyList(), "q", false, null)
        return ChyavorecSiteService(http, base, clock, fallback)
    }

    @Test fun serviceSkipsParsingWhenHashMatches() = runTest {
        val svc = site()
        val first = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(null) as Outcome.Success).value)
        assertEquals(Hashing.sha256Hex(news.toByteArray()), first.hash)
        assertEquals(1, svc.newsParses)

        assertEquals(Fetched.Unchanged, (svc.fetchLatestIfChanged(first.hash) as Outcome.Success).value)
        assertEquals(1, svc.newsParses, "същото съдържание — без разчитане")

        news = news.replace("\"title\"", "\"title\" ")
        val changed = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(first.hash) as Outcome.Success).value)
        assertEquals(2, svc.newsParses, "промененото съдържание се разчита")
        assertEquals(Hashing.sha256Hex(news.toByteArray()), changed.hash)
    }

    @Test fun repositoryTouchesCacheWithoutParsing() = runTest {
        val svc = site()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val repo = NewsRepository(svc, cache, clock)
        val first = (repo.latest(force = true) as Outcome.Success).value
        assertEquals(1, svc.newsParses)
        val writes = cache.writes

        clock.advanceSeconds(3_600)
        val second = (repo.latest(force = true) as Outcome.Success).value
        assertEquals(1, svc.newsParses, "непромененият news.json не се разчита")
        assertEquals(writes, cache.writes, "кешът само се „докосва“, без презаписване")
        assertEquals(first.data, second.data)
        assertFalse(second.fromCache)
        assertEquals(clock.now(), second.syncedAt)
        assertEquals(clock.now(), repo.cached()?.syncedAt, "новото време на синхронизация е и в кеша")

        news = news.replace("\"title\"", "\"title\" ")
        clock.advanceSeconds(3_600)
        repo.latest(force = true)
        assertEquals(2, svc.newsParses)
    }

    @Test fun freshRepositoryInstanceUsesStoredHash() = runTest {
        // Хешът се пази в кеша до стойността — и след рестарт на процеса (нови обекти).
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val svc1 = site()
        NewsRepository(svc1, cache, clock).latest(force = true)
        assertEquals(1, svc1.newsParses)
        val svc2 = site()
        val r = NewsRepository(svc2, cache, clock).latest(force = true)
        assertIs<Outcome.Success<*>>(r)
        assertEquals(0, svc2.newsParses)
    }

    @Test fun incompleteNewsWithoutRssIsParsedAgain() = runTest {
        rssOk = false
        val svc = site()
        val first = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(null) as Outcome.Success).value)
        assertNull(first.hash, "без rss.xml адресите са непълни — хешът не се помни")
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val repo = NewsRepository(svc, cache, clock)
        repo.latest(force = true)
        repo.latest(force = true)
        assertEquals(3, svc.newsParses)
        rssOk = true
        repo.latest(force = true)
        repo.latest(force = true)
        assertEquals(4, svc.newsParses, "с rss.xml — веднъж, после без разчитане")
    }

    @Test fun eventsAndSearchIndexSkipUnchangedIndex() = runTest {
        val svc = site()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val events = EventsRepository(svc, cache, clock)
        val siteRepo = SiteRepository(svc, cache, clock)

        val e1 = (events.events(force = true) as Outcome.Success).value
        assertNotNull(siteRepo.searchIndex(force = true) as? Outcome.Success)
        assertEquals(2, svc.indexParses)

        // След 5 минути index.json се тегли отново, но е същият — не се разчита.
        clock.advanceSeconds(600)
        val e2 = (events.events(force = true) as Outcome.Success).value
        val idx = (siteRepo.searchIndex(force = true) as Outcome.Success).value
        assertEquals(2, svc.indexParses)
        assertEquals(e1.data, e2.data)
        assertEquals(clock.now(), idx.syncedAt)

        index = index.replace("\"title\"", "\"title\" ")
        clock.advanceSeconds(600)
        events.events(force = true)
        siteRepo.searchIndex(force = true)
        assertEquals(4, svc.indexParses)
    }

    @Test fun clearingTheCacheForgetsTheHash() = runTest {
        val svc = site()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val repo = NewsRepository(svc, cache, clock)
        repo.latest(force = true)
        cache.clear()
        val r = (repo.latest(force = true) as Outcome.Success).value
        assertEquals(2, svc.newsParses)
        assertFalse(r.data.isEmpty())
    }
}
