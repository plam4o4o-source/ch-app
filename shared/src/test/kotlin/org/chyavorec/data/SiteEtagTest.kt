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
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.service.Fetched
import java.time.Instant
import java.util.Collections
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * news.json с ETag: условна заявка (If-None-Match) и 304 без тяло; без ETag — както досега.
 * rss.xml се тегли само когато файлът е нов (не се започва и отменя при всеки студен старт).
 */
class SiteEtagTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")

    @Volatile private var news = TestUtil.resource("site-news.json")
    @Volatile private var etag: String? = "\"v1\""
    private val seen = Collections.synchronizedList(ArrayList<Pair<String, String?>>())

    @BeforeTest fun start() {
        server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    val path = request.path.orEmpty()
                    seen += path to request.getHeader("If-None-Match")
                    return when (path) {
                        "/data/news.json" -> {
                            val tag = etag
                            if (tag != null && request.getHeader("If-None-Match") == tag) {
                                MockResponse().setResponseCode(304).setHeader("ETag", tag)
                            } else {
                                MockResponse().setBody(news).apply { tag?.let { setHeader("ETag", it) } }
                            }
                        }
                        "/rss.xml" -> MockResponse().setBody(TestUtil.resource("site-rss.xml"))
                        else -> MockResponse().setResponseCode(404)
                    }
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

    private fun requests(path: String) = seen.filter { it.first == path }

    @Test fun etagIsStoredWithTheHashAndMakesTheNextRequestConditional() = runTest {
        val svc = site()
        val first = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(null) as Outcome.Success).value)
        assertEquals("${Hashing.sha256Hex(news.toByteArray())}#\"v1\"", first.hash)
        assertEquals(1, requests("/rss.xml").size, "без известен хеш файлът е нов — rss.xml тръгва веднага")
        assertNull(requests("/data/news.json").single().second, "първата заявка не е условна")

        val second = (svc.fetchLatestIfChanged(first.hash) as Outcome.Success).value
        assertEquals(Fetched.Unchanged, second, "304 = непроменен")
        assertEquals("\"v1\"", requests("/data/news.json").last().second)
        assertEquals(1, svc.newsParses, "304 не разчита файла")
        assertEquals(1, requests("/rss.xml").size, "304 не тегли rss.xml")
    }

    @Test fun changedFileWithNewEtagIsParsedAndStoresTheNewEtag() = runTest {
        val svc = site()
        val first = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(null) as Outcome.Success).value)
        news = news.replace("\"title\"", "\"title\" ")
        etag = "\"v2\""
        val changed = assertIs<Fetched.Changed<*>>((svc.fetchLatestIfChanged(first.hash) as Outcome.Success).value)
        assertEquals(2, svc.newsParses)
        assertEquals("${Hashing.sha256Hex(news.toByteArray())}#\"v2\"", changed.hash)
    }

    @Test fun withoutEtagTheHashStillDecidesAndRssIsNotRequested() = runTest {
        etag = null
        val first = assertIs<Fetched.Changed<*>>((site().fetchLatestIfChanged(null) as Outcome.Success).value)
        assertEquals(Hashing.sha256Hex(news.toByteArray()), first.hash, "без ETag хешът е гол")
        val before = requests("/rss.xml").size

        // Студен старт: нов обект без запомнени адреси, но с хеш от диска.
        val cold = site()
        val again = (cold.fetchLatestIfChanged(first.hash) as Outcome.Success).value
        assertEquals(Fetched.Unchanged, again)
        assertEquals(0, cold.newsParses)
        assertEquals(before, requests("/rss.xml").size, "непроменен файл — нито една заявка за rss.xml")
        assertTrue(requests("/data/news.json").all { it.second == null }, "без ETag заявките не са условни")
    }

    @Test fun changedFileAfterColdStartFetchesRssAfterTheNews() = runTest {
        etag = null
        val first = assertIs<Fetched.Changed<*>>((site().fetchLatestIfChanged(null) as Outcome.Success).value)
        val before = requests("/rss.xml").size
        news = news.replace("\"title\"", "\"title\" ")

        val cold = site()
        val changed = assertIs<Fetched.Changed<*>>((cold.fetchLatestIfChanged(first.hash) as Outcome.Success).value)
        assertEquals(before + 1, requests("/rss.xml").size, "новият файл иска адресите от rss.xml")
        assertNotNull(changed.hash, "с rss.xml данните са пълни — хешът се помни")
        assertEquals(1, cold.newsParses)
    }
}
