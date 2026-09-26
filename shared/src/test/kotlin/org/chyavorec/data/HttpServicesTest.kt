package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.GitHubCatalogService
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.invlib.RemoteInvLibClient
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.MembershipStatus
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class HttpServicesTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() = server.shutdown()

    private fun url(path: String) = server.url(path).toString()

    @Test fun catalogFallsBackToSecondSource() = runTest {
        server.enqueue(MockResponse().setResponseCode(503))
        server.enqueue(MockResponse().setBody(TestUtil.resource("katalog-sample.json")))
        val svc = GitHubCatalogService(http, listOf(url("/raw/katalog.json"), url("/cdn/katalog.json")))
        val (_, snap) = (svc.fetchCatalog() as Outcome.Success).value
        assertEquals(60, snap.books.size)
        assertEquals("/raw/katalog.json", server.takeRequest().path)
        assertEquals("/cdn/katalog.json", server.takeRequest().path)
    }

    @Test fun catalogBothSourcesFail() = runTest {
        server.enqueue(MockResponse().setBody("<html>captive portal</html>"))
        server.enqueue(MockResponse().setResponseCode(404))
        val svc = GitHubCatalogService(http, listOf(url("/a"), url("/b")))
        assertEquals(AppError.NotFound, (svc.fetchCatalog() as Outcome.Failure).error)
    }

    @Test fun siteNewsFromRssAndArticlePage() = runTest {
        val base = server.url("/").toString().trimEnd('/')
        server.enqueue(MockResponse().setBody(TestUtil.resource("news-rss.xml")).setHeader("Content-Type", "application/rss+xml; charset=utf-8"))
        server.enqueue(MockResponse().setBody("<html><body><div class='eMessage'><h1>Покана за концерт по случай 1 ноември</h1><p>Пълният текст на новината е тук и е достатъчно дълъг.</p><img src='/_nw/1/9.jpg' width='600'></div></body></html>"))
        val fallback = Contacts("НЧ", null, emptyList(), emptyList(), base, null, emptyList(), "q", false, null)
        val svc = ChyavorecSiteService(http, base, clock, fallback)
        val news = (svc.fetchLatest() as Outcome.Success).value
        assertEquals("/news/rss/", server.takeRequest().path)
        val detail = (svc.fetchArticle(news[0].copy(url = "$base/news/1")) as Outcome.Success).value
        assertTrue(detail.blocks.none { it is org.chyavorec.domain.model.ContentBlock.Heading }, "заглавието не се повтаря")
        assertTrue(detail.gallery.any { it.endsWith("/_nw/1/9.jpg") })
    }

    @Test fun siteServerErrorIsTyped() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))
        val base = server.url("/").toString().trimEnd('/')
        val svc = ChyavorecSiteService(http, base, clock, Contacts("", null, emptyList(), emptyList(), "", null, emptyList(), "", false, null))
        assertEquals(AppError.Server(500), (svc.fetchLatest() as Outcome.Failure).error)
    }

    // --- Предложеният InvLib API (docs/API.md) ---

    private fun client() = RemoteInvLibClient(http, server.url("/api").toString(), clock, "test-device")
    private val caps = """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":true,"holds":false,"availability":true}"""

    @Test fun invlibLoginAndLoans() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setBody("""{"accessToken":"AT","refreshToken":"RT","expiresIn":3600,"readerId":"R7"}"""))
        server.enqueue(MockResponse().setBody("""{"loans":[{"loanId":"9","inv":156,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-12","dateDue":"2026-10-03","renewals":1,"canRenew":true,"unknown":"x"}]}"""))
        val c = client()
        val pwd = "p@ss".toCharArray()
        val session = (c.login("R-7", pwd) as Outcome.Success).value
        assertTrue(pwd.all { it == '\u0000' }, "паролата се зачиства")
        assertEquals("R7", session.readerId)
        assertEquals(clock.now().toEpochMilli() + 3_600_000, session.expiresAtMillis)
        server.takeRequest() // capabilities
        val loginReq = server.takeRequest()
        assertEquals("/api/v1/auth/login", loginReq.path)
        assertTrue(loginReq.body.readUtf8().contains("\"cardNumber\":\"R-7\""))
        val loans = (c.loans(session) as Outcome.Success).value
        assertEquals("2026-10-03", loans.single().dueOn)
        assertEquals("Bearer AT", server.takeRequest().getHeader("Authorization"))
    }

    @Test fun invlibUnsupportedFeatureIsNotFaked() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        val c = client()
        val r = c.placeHold(AuthSession("AT", null, 0, "R"), 156)
        assertEquals(AppError.NotAvailable(Feature.HOLDS), (r as Outcome.Failure).error)
        assertEquals(1, server.requestCount)
    }

    @Test fun invlibMembershipAndAvailability() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setBody("""{"memberNumber":"R-7","holderName":"Читател","since":"2020-01-01","validUntil":"2026-12-31","status":"active","barcode":"R-7"}"""))
        server.enqueue(MockResponse().setBody("""{"inv":156,"status":"on_loan","dueOn":"2026-10-03"}"""))
        val c = client()
        val m = (c.membership(AuthSession("AT", null, 0, "R")) as Outcome.Success).value
        assertEquals(MembershipStatus.ACTIVE, m.status)
        assertEquals(BookStatus.ON_LOAN, (c.availability(156) as Outcome.Success).value)
    }

    @Test fun invlibWrongPasswordAndRateLimit() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setResponseCode(401).setBody("""{"error":"invalid_credentials"}"""))
        server.enqueue(MockResponse().setResponseCode(429).setHeader("Retry-After", "30"))
        val c = client()
        assertEquals(AppError.Unauthorized, (c.login("1", "x".toCharArray()) as Outcome.Failure).error)
        assertEquals(AppError.RateLimited(30), (c.login("1", "x".toCharArray()) as Outcome.Failure).error)
    }

    @Test fun networkDownIsNetworkError() = runTest {
        server.shutdown()
        val r = http.get("http://127.0.0.1:1/none")
        assertIs<Outcome.Failure>(r)
        assertEquals(AppError.Network, r.error)
        assertFalse(false)
    }
}
