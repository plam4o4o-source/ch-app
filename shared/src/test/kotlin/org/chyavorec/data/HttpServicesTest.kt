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
import org.chyavorec.domain.model.RenewResult
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

    private fun site(): ChyavorecSiteService {
        val base = server.url("/").toString().trimEnd('/')
        val fallback = Contacts("НЧ", null, emptyList(), emptyList(), emptyList(), base, null, emptyList(), "q", false, null)
        return ChyavorecSiteService(http, base, clock, fallback)
    }

    private fun siteDispatcher(failNews: Boolean = false) = object : okhttp3.mockwebserver.Dispatcher() {
        override fun dispatch(request: okhttp3.mockwebserver.RecordedRequest): MockResponse = when (request.path) {
            "/data/news.json" -> if (failNews) MockResponse().setResponseCode(500) else MockResponse().setBody(TestUtil.resource("site-news.json"))
            "/rss.xml" -> MockResponse().setBody(TestUtil.resource("site-rss.xml"))
            "/javora/index.json" -> MockResponse().setBody(TestUtil.resource("site-index.json"))
            "/data/files.json" -> MockResponse().setBody(TestUtil.resource("site-files.json"))
            "/kontakti" -> MockResponse().setBody(TestUtil.resource("site-kontakti.html"))
            "/about" -> MockResponse().setBody(TestUtil.resource("site-about.html"))
            "/fotodokumentalna-izlozhba" -> MockResponse().setBody(TestUtil.resource("site-izlozhba.html"))
            "/api/calendar?d=2026-9-26" -> MockResponse().setBody("""{"date":"2026-9-26","line":"**Св. ап. и ев. Йоан Богослов**"}""")
            else -> MockResponse().setResponseCode(404)
        }
    }

    @Test fun siteNewsEventsDocumentsFromVercelData() = runTest {
        server.dispatcher = siteDispatcher()
        val svc = site()
        val news = svc.fetchLatest().let { it as? Outcome.Success ?: error("news: $it") }.value
        assertEquals(3, news.size)
        assertTrue(news[0].url.endsWith("/news/45-ti-obshtinski-folkloren-sabor-na-narodnoto-tvorchestvo-ot-timok-do"))
        val detail = (svc.fetchArticle(news[0]) as Outcome.Success).value
        assertTrue(detail.blocks.isNotEmpty())
        assertEquals(news[0].imageUrl, detail.gallery.first())
        val events = svc.fetchEvents().let { it as? Outcome.Success ?: error("events: $it") }.value
        assertEquals(8, events.size)
        val docs = svc.fetchDocuments().let { it as? Outcome.Success ?: error("docs: $it") }.value
        assertEquals(3 + 2, docs.size)
        val links = svc.discoverLinks().let { it as? Outcome.Success ?: error("links: $it") }.value
        assertTrue(links.any { it.url.endsWith("/about") })
        assertTrue(links.any { it.url.endsWith("/policy") })
        val page = svc.fetchPage(server.url("/about").toString()).let { it as? Outcome.Success ?: error("page: $it") }.value
        assertEquals("За нас", page.title)
        val contacts = svc.fetchContacts(links).let { it as? Outcome.Success ?: error("contacts: $it") }.value
        assertTrue(contacts.fromSite)
        assertEquals("**Св. ап. и ев. Йоан Богослов**", svc.fetchFeast("2026-9-26").let { it as? Outcome.Success ?: error("feast: $it") }.value.line)
    }

    @Test fun siteGalleryUsesExhibitionCaptionsAndNewsCovers() = runTest {
        server.dispatcher = siteDispatcher()
        val photos = (site().fetchGallery() as Outcome.Success).value
        assertTrue(photos.any { it.album == "Новини" })
        val exh = photos.filter { it.album == "Фотодокументална изложба" }
        assertTrue(exh.size >= 5, "експонати: ${exh.size}")
        assertTrue(exh.any { it.title == "Хорът на сцена" })
        assertTrue(photos.none { it.fullUrl.contains("logo-256") || it.fullUrl.contains("signature") })
    }

    @Test fun siteServerErrorIsTyped() = runTest {
        server.dispatcher = siteDispatcher(failNews = true)
        assertEquals(AppError.Server(500), (site().fetchLatest() as Outcome.Failure).error)
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

    @Test fun invlibLoansWithoutDatesStillDecode() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setBody("""{"loans":[{"loanId":"1","title":"А","dateOut":null,"dateDue":null},{"loanId":"2","title":"Б"}]}"""))
        val loans = (client().loans(AuthSession("AT", null, 0, "R")) as Outcome.Success).value
        assertEquals(2, loans.size)
        assertEquals(null, loans[0].dueOn)
        assertEquals(null, loans[1].borrowedOn)
    }

    @Test fun invlibLoansDecodeRenewFields() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setBody("""{"loans":[
            {"loanId":"1","title":"А","dateDue":"2026-10-03","canRenew":true,"renewPending":true},
            {"loanId":"2","title":"Б","renewResult":{"status":"rejected","reason":"Има заявка от друг читател.","at":"2026-09-25T10:00:00Z"}},
            {"loanId":"3","title":"В","renewResult":null}]}"""))
        val loans = (client().loans(AuthSession("AT", null, 0, "R")) as Outcome.Success).value
        assertTrue(loans[0].renewPending)
        assertEquals(null, loans[0].renewResult)
        assertFalse(loans[1].renewPending)
        assertEquals(RenewResult("rejected", "Има заявка от друг читател.", "2026-09-25T10:00:00Z"), loans[1].renewResult)
        assertTrue(loans[1].renewResult!!.isRejected)
        assertEquals(null, loans[2].renewResult)
        // Кешът (kotlinx JSON) връща същото.
        val json = kotlinx.serialization.json.Json { encodeDefaults = false }
        val ser = kotlinx.serialization.builtins.ListSerializer(org.chyavorec.domain.model.Loan.serializer())
        assertEquals(loans, json.decodeFromString(ser, json.encodeToString(ser, loans)))
    }

    @Test fun invlibRenewAccepted202AndConflicts() = runTest {
        val renewCaps = """{"apiVersion":1,"login":true,"loans":true,"renew":true,"history":true}"""
        server.enqueue(MockResponse().setBody(renewCaps))
        server.enqueue(MockResponse().setResponseCode(202).setBody("""{"loanId":"9","title":"Под игото","dateDue":"2026-10-03","canRenew":true,"renewPending":true}"""))
        server.enqueue(MockResponse().setResponseCode(409).setBody("""{"error":"pending"}"""))
        server.enqueue(MockResponse().setResponseCode(409).setBody("""{"error":"not_allowed"}"""))
        server.enqueue(MockResponse().setResponseCode(404))
        server.enqueue(MockResponse().setResponseCode(429).setHeader("Retry-After", "10"))
        val c = client()
        val s = AuthSession("AT", null, 0, "R")
        val ok = (c.renew(s, "9") as Outcome.Success).value
        assertTrue(ok.renewPending)
        server.takeRequest() // capabilities
        val req = server.takeRequest()
        assertEquals("/api/v1/me/loans/9/renew", req.path)
        assertEquals("POST", req.method)
        assertEquals(0L, req.bodySize)
        assertEquals(AppError.Conflict(AppError.Conflict.PENDING), (c.renew(s, "9") as Outcome.Failure).error)
        assertEquals(AppError.Conflict(AppError.Conflict.NOT_ALLOWED), (c.renew(s, "9") as Outcome.Failure).error)
        assertEquals(AppError.NotFound, (c.renew(s, "x") as Outcome.Failure).error)
        assertEquals(AppError.RateLimited(10), (c.renew(s, "9") as Outcome.Failure).error)
    }

    @Test fun invlibHistory() = runTest {
        server.enqueue(MockResponse().setBody("""{"apiVersion":1,"login":true,"loans":true,"history":true}"""))
        server.enqueue(MockResponse().setBody("""{"items":[{"loanId":"h1","inv":156,"title":"Под игото","author":"Иван Вазов","dateOut":"2025-03-01","dateIn":"2025-03-20"},{"loanId":"h2","inv":null,"title":"Б","author":"","dateOut":null,"dateIn":null}],"generated":"2026-09-26T10:00:00Z"}"""))
        val c = client()
        assertTrue((c.capabilities() as Outcome.Success).value.history)
        val items = (c.history(AuthSession("AT", null, 0, "R")) as Outcome.Success).value
        assertEquals(2, items.size)
        assertEquals(156L, items[0].inv)
        assertEquals(2025, items[0].year)
        assertEquals(null, items[1].inv)
        assertEquals(null, items[1].year)
        server.takeRequest()
        assertEquals("/api/v1/me/history", server.takeRequest().path)
    }

    @Test fun invlibHistoryNotAvailableWithoutCapability() = runTest {
        server.enqueue(MockResponse().setBody(caps))
        val r = client().history(AuthSession("AT", null, 0, "R"))
        assertEquals(AppError.NotAvailable(Feature.HISTORY), (r as Outcome.Failure).error)
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

    @Test fun invlibRefresh403IsUnauthorizedButElsewhereStaysServer403() = runTest {
        val c = client()
        server.enqueue(MockResponse().setResponseCode(403).setBody("""{"error":"revoked"}"""))
        val refreshed = c.refresh(AuthSession("AT", "RT", 0, "R"))
        assertEquals(AppError.Unauthorized, (refreshed as Outcome.Failure).error)
        assertEquals("/api/v1/auth/refresh", server.takeRequest().path)

        server.enqueue(MockResponse().setBody(caps))
        server.enqueue(MockResponse().setResponseCode(403))
        val loans = c.loans(AuthSession("AT", "RT", 0, "R"))
        assertEquals(AppError.Server(403), (loans as Outcome.Failure).error)
    }

    @Test fun networkDownIsNetworkError() = runTest {
        server.shutdown()
        val r = http.get("http://127.0.0.1:1/none")
        assertIs<Outcome.Failure>(r)
        assertEquals(AppError.Network, r.error)
        assertFalse(false)
    }
}
