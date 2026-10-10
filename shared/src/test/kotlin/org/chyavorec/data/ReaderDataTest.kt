package org.chyavorec.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.invlib.RemoteInvLibClient
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.LibraryRepository
import org.chyavorec.data.repository.MembershipRepository
import org.chyavorec.data.repository.ProfileRepository
import org.chyavorec.data.repository.ReaderDataRepository
import org.chyavorec.data.repository.ReaderMessagesRepository
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.MembershipStatus
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

/** `/v1/me/all`, една заявка в движение, запомнени възможности, „прочетено“ на пакет, cache-first. */
class ReaderDataTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-10-10T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")
    private val session = AuthSession("AT", "RT", Instant.parse("2026-10-12T12:00:00Z").toEpochMilli(), "R1")

    /** Отговорите по път (без заявката); всички заявки се записват по ред. */
    private val routes = ConcurrentHashMap<String, () -> MockResponse>()
    private val requests = CopyOnWriteArrayList<RecordedRequest>()

    private fun count(path: String) = requests.count { it.path == "/api$path" }

    private val capsAll = """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":true,
        "renew":true,"history":true,"messages":true,"all":true,"messagesBatchRead":true}"""
    private val capsOld = """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":true,
        "renew":true,"history":true,"messages":true}"""

    private val meAll = """{
        "profile":{"readerId":"R1","cardNumber":"0001","fullName":"Иван Петров","category":"възрастен"},
        "membership":{"memberNumber":"M-1","holderName":"Иван Петров","status":"active","validUntil":"2026-12-31","barcode":"0001"},
        "loans":{"loans":[
            {"loanId":"L1","inv":5,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-10-01","dateDue":"2026-10-20","canRenew":true},
            {"loanId":"L2","inv":6,"title":"Тютюн","dateDue":"2026-10-15","renewPending":true,
             "renewResult":{"status":"rejected","reason":"Заявена от друг","at":"2026-10-09T10:00:00Z"}}]},
        "history":{"items":[{"loanId":"H1","inv":3,"title":"Бай Ганьо","dateOut":"2026-01-01","dateIn":"2026-01-20"}],"generated":"2026-10-10T09:30:00Z"},
        "messages":{"items":[
            {"id":"16","text":"По-стара","at":"2026-10-01T08:00:00Z","read":true},
            {"id":"17","title":"Запазена книга","text":"Пристигна","at":"2026-10-10T09:15:00Z","read":false},
            {"id":"","text":"без id"}],"generated":"2026-10-10T09:30:00Z"},
        "generated":"2026-10-10T09:30:00Z"}"""

    @BeforeTest fun start() {
        server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    requests += request
                    val path = request.path.orEmpty().removePrefix("/api")
                    return routes[path]?.invoke() ?: MockResponse().setResponseCode(404).setBody("""{"error":"not_found"}""")
                }
            }
            start()
        }
    }

    @AfterTest fun stop() = server.shutdown()

    private fun client(capsCache: InMemoryPayloadCache? = null, path: String = "/api") =
        RemoteInvLibClient(http, server.url(path).toString(), clock, "test-device", capsCache)

    private class MemStore(var s: AuthSession?) : SessionStore {
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session }
        override suspend fun isPersisted() = s != null
        override suspend fun clear() { s = null }
    }

    /** Репозиторитата, както в AppContainer: едно общо [ReaderDataRepository]. */
    private inner class Repos(caps: String, signedIn: Boolean = true) {
        init { routes["/v1/capabilities"] = { MockResponse().setBody(caps) } }
        val c = client()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val auth = AuthRepository(c, MemStore(if (signedIn) session else null), clock, cache)
        val data = ReaderDataRepository(c, auth, cache, clock, c)
        val profile = ProfileRepository(c, auth, cache, clock, c, data)
        val library = LibraryRepository(c, auth, cache, clock, data)
        val membership = MembershipRepository(c, auth, cache, clock, data)
        val messages = ReaderMessagesRepository(c, auth, cache, clock, data)
    }

    private fun <T> ok(o: Outcome<T>): T = when (o) {
        is Outcome.Success -> o.value
        is Outcome.Failure -> fail("очакван успех, а е $o")
    }

    // --- /v1/me/all ---

    @Test fun meAllParsesEverything() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll) }
        routes["/v1/me/all"] = { MockResponse().setBody(meAll) }
        val c = client()
        val b = ok(c.meAll(session))
        val p = assertNotNull(b.profile)
        assertEquals("Иван Петров", p.fullName)
        // Членството е и в профила (за картата/„Моето“), и отделно.
        assertEquals(MembershipStatus.ACTIVE, p.membership?.status)
        assertEquals("0001", b.membership?.barcodePayload)
        val loans = assertNotNull(b.loans)
        assertEquals(listOf("L1", "L2"), loans.map { it.loanId })
        assertEquals("2026-10-20", loans[0].dueOn)
        assertTrue(loans[1].renewPending)
        assertTrue(loans[1].renewResult!!.isRejected)
        assertEquals("Бай Ганьо", b.history?.single()?.title)
        // Като при /v1/me/messages: без празни id, най-новите първо.
        assertEquals(listOf("17", "16"), b.messages?.map { it.id })
        val req = requests.last()
        assertEquals("/api/v1/me/all", req.path)
        assertEquals("GET", req.method)
        assertEquals("Bearer AT", req.getHeader("Authorization"))
        // Без лични данни в toString.
        assertFalse(b.toString().contains("Петров"))
    }

    @Test fun meAllWithoutCapabilityIsNotAvailableWithoutRequest() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsOld) }
        val c = client()
        assertEquals(AppError.NotAvailable(Feature.PROFILE), (c.meAll(session) as Outcome.Failure).error)
        assertEquals(0, count("/v1/me/all"))
    }

    @Test fun oneCombinedCallFillsAllCaches() = runTest {
        routes["/v1/me/all"] = { MockResponse().setBody(meAll) }
        val r = Repos(capsAll)
        val p = ok(r.profile.profile(force = false))
        assertEquals("Иван Петров", p.data.fullName)
        assertFalse(p.fromCache)
        assertEquals(1, count("/v1/me/all"))

        // Останалите идват от кешовете, напълнени от същата заявка — без мрежа.
        assertEquals(listOf("L1", "L2"), ok(r.library.loans(force = false)).data.map { it.loanId })
        assertEquals("M-1", ok(r.membership.membership(force = false)).data.memberNumber)
        assertEquals("H1", ok(r.library.history(force = false)).data.single().loanId)
        assertEquals(listOf("17", "16"), ok(r.messages.messages(force = false)).data.map { it.id })
        assertEquals(1, count("/v1/me/all"))
        assertEquals(0, count("/v1/me") + count("/v1/me/loans") + count("/v1/me/membership") + count("/v1/me/history") + count("/v1/me/messages"))

        // Кешовете са записани (виждат се и от уиджета/известията).
        assertEquals(2, r.library.cachedLoans()?.data?.size)
        assertEquals("M-1", r.membership.cached()?.data?.memberNumber)
        assertNotNull(r.messages.cached())
        assertNotNull(r.library.cachedHistory())
        // Профилът е и в общия StateFlow (за всички екрани).
        assertEquals("Иван Петров", r.profile.state.value?.data?.fullName)
        assertEquals(MembershipStatus.ACTIVE, r.profile.state.value?.data?.membership?.status)
    }

    @Test fun concurrentCallersShareOneCombinedRequest() = runTest {
        routes["/v1/me/all"] = { MockResponse().setBody(meAll).setBodyDelay(200, TimeUnit.MILLISECONDS) }
        val r = Repos(capsAll)
        val results = listOf(
            async { r.profile.profile(force = false) },
            async { r.library.loans(force = false) },
            async { r.membership.membership(force = false) },
            async { r.messages.messages(force = false) },
            async { r.library.history(force = false) },
            async { r.library.loans(force = false) },
            async { r.profile.profile(force = false) },
        ).awaitAll()
        results.forEach { assertIs<Outcome.Success<*>>(it) }
        assertEquals(1, count("/v1/me/all"))
        assertEquals(1, count("/v1/capabilities"))
    }

    @Test fun concurrentCallersShareOneRequestWithoutCombinedCall() = runTest {
        routes["/v1/me/loans"] = {
            MockResponse().setBody("""{"loans":[{"loanId":"L1","title":"Под игото"}]}""").setBodyDelay(200, TimeUnit.MILLISECONDS)
        }
        val r = Repos(capsOld)
        val results = (1..5).map { async { r.library.loans(force = false) } }.awaitAll()
        results.forEach { assertEquals("L1", ok(it).data.single().loanId) }
        assertEquals(1, count("/v1/me/loans"))
        assertEquals(0, count("/v1/me/all"))
    }

    // --- резервни пътища ---

    @Test fun withoutCapabilityPerEndpointCallsAsBefore() = runTest {
        routes["/v1/me"] = { MockResponse().setBody("""{"readerId":"R1","cardNumber":"0001","fullName":"Иван Петров"}""") }
        routes["/v1/me/membership"] = { MockResponse().setBody("""{"memberNumber":"M-1","holderName":"Иван Петров","status":"expired"}""") }
        routes["/v1/me/loans"] = { MockResponse().setBody("""{"loans":[]}""") }
        val r = Repos(capsOld)
        val p = ok(r.profile.profile(force = false)).data
        // /v1/me без членство → второ питане за членството (както досега).
        assertEquals(MembershipStatus.EXPIRED, p.membership?.status)
        assertEquals(1, count("/v1/me/membership"))
        assertEquals(emptyList(), ok(r.library.loans(force = false)).data)
        assertEquals(0, count("/v1/me/all"))
    }

    @Test fun profileWithMembershipSkipsSecondCall() = runTest {
        routes["/v1/me"] = {
            MockResponse().setBody(
                """{"readerId":"R1","cardNumber":"0001","fullName":"Иван Петров",
                    "membership":{"memberNumber":"M-1","holderName":"Иван Петров","status":"active","barcode":"0001"}}""",
            )
        }
        val r = Repos(capsOld)
        val p = ok(r.profile.profile(force = true)).data
        assertEquals(MembershipStatus.ACTIVE, p.membership?.status)
        assertEquals("0001", p.membership?.barcodePayload)
        assertEquals(0, count("/v1/me/membership"))
    }

    @Test fun combinedCallNotFoundFallsBackToSingleEndpoint() = runTest {
        // Мостът обявява `all`, но адресът липсва (404) → поотделно.
        routes["/v1/me/loans"] = { MockResponse().setBody("""{"loans":[{"loanId":"L9","title":"Поотделно"}]}""") }
        val r = Repos(capsAll)
        assertEquals("L9", ok(r.library.loans(force = false)).data.single().loanId)
        assertEquals(1, count("/v1/me/all"))
        assertEquals(1, count("/v1/me/loans"))
    }

    @Test fun combinedCallWithMissingPartAsksForThatPartOnly() = runTest {
        routes["/v1/me/all"] = {
            MockResponse().setBody("""{"profile":{"readerId":"R1","cardNumber":"0001","fullName":"Иван"},"loans":{"loans":[]}}""")
        }
        routes["/v1/me/history"] = { MockResponse().setBody("""{"items":[{"loanId":"H7","title":"Отделно"}]}""") }
        val r = Repos(capsAll)
        assertEquals("H7", ok(r.library.history(force = false)).data.single().loanId)
        assertEquals(1, count("/v1/me/history"))
        // Върнатите части са в кеша — без нова заявка.
        assertEquals(emptyList(), ok(r.library.loans(force = false)).data)
        assertEquals("Иван", ok(r.profile.profile(force = false)).data.fullName)
        assertEquals(1, count("/v1/me/all"))
    }

    @Test fun combinedCallServerErrorKeepsCachedDataWithReason() = runTest {
        var fail = false
        routes["/v1/me/all"] = { if (fail) MockResponse().setResponseCode(503) else MockResponse().setBody(meAll) }
        val r = Repos(capsAll)
        ok(r.library.loans(force = false))
        fail = true
        clock.advanceSeconds(4 * 60)
        val stale = ok(r.library.loans(force = false))
        assertEquals(listOf("L1", "L2"), stale.data.map { it.loanId })
        assertTrue(stale.fromCache)
        assertEquals(AppError.Server(503), stale.refreshError)
        // 5xx не е причина за поотделни заявки (и те биха се провалили).
        assertEquals(0, count("/v1/me/loans"))
        assertEquals(2, count("/v1/me/all"))
    }

    @Test fun withoutSessionNoRequestsAtAll() = runTest {
        val r = Repos(capsAll, signedIn = false)
        assertEquals(AppError.Unauthorized, (r.library.loans(force = false) as Outcome.Failure).error)
        assertEquals(AppError.Unauthorized, (r.profile.profile(force = false) as Outcome.Failure).error)
        assertEquals(0, server.requestCount)
    }

    // --- cache-first: свежест ---

    @Test fun freshnessPerKindAndForce() = runTest {
        routes["/v1/me"] = { MockResponse().setBody("""{"readerId":"R1","cardNumber":"0001","fullName":"Иван"}""") }
        routes["/v1/me/membership"] = { MockResponse().setBody("""{"memberNumber":"M-1","holderName":"Иван","status":"active"}""") }
        routes["/v1/me/loans"] = { MockResponse().setBody("""{"loans":[]}""") }
        routes["/v1/me/history"] = { MockResponse().setBody("""{"items":[]}""") }
        routes["/v1/me/messages"] = { MockResponse().setBody("""{"items":[]}""") }
        val r = Repos(capsOld)
        suspend fun all() {
            ok(r.profile.profile(force = false)); ok(r.membership.membership(force = false)); ok(r.library.loans(force = false))
            ok(r.library.history(force = false)); ok(r.messages.messages(force = false))
        }
        fun counts() = listOf(count("/v1/me"), count("/v1/me/membership"), count("/v1/me/loans"), count("/v1/me/history"), count("/v1/me/messages"))

        all()
        // Профилът без членство пита и за него → /v1/me/membership два пъти общо.
        assertEquals(listOf(1, 2, 1, 1, 1), counts())
        all()
        assertEquals(listOf(1, 2, 1, 1, 1), counts())

        clock.advanceSeconds(3 * 60) // t = 3 мин: заеманията (3 мин) са остарели
        all(); assertEquals(listOf(1, 2, 2, 1, 1), counts())
        clock.advanceSeconds(2 * 60) // t = 5 мин: съобщенията (5 мин); заеманията са на 2 мин
        all(); assertEquals(listOf(1, 2, 2, 1, 2), counts())
        clock.advanceSeconds(5 * 60) // t = 10 мин: историята (10 мин), заеманията, съобщенията
        all(); assertEquals(listOf(1, 2, 3, 2, 3), counts())
        clock.advanceSeconds(6 * 3600) // t = 6 ч 10 мин: и членството (6 ч); профилът (12 ч) — още не
        all(); assertEquals(listOf(1, 3, 4, 3, 4), counts())
        clock.advanceSeconds(6 * 3600) // t = 12 ч 10 мин: профилът (+ неговото членство) и членството
        all(); assertEquals(listOf(2, 5, 5, 4, 5), counts())

        // Изрично опресняване (дръпване) — винаги от мрежата, и при пресен кеш.
        ok(r.library.loans(force = true))
        assertEquals(6, count("/v1/me/loans"))
    }

    @Test fun renewUpdatesCacheWithoutRefetchingLoans() = runTest {
        routes["/v1/me/loans"] = { MockResponse().setBody("""{"loans":[{"loanId":"L1","title":"Под игото","canRenew":true}]}""") }
        routes["/v1/me/loans/L1/renew"] = {
            MockResponse().setResponseCode(202).setBody("""{"loanId":"L1","title":"Под игото","canRenew":true,"renewPending":true}""")
        }
        val r = Repos(capsOld)
        ok(r.library.loans(force = false))
        assertTrue(ok(r.library.renew("L1")).renewPending)
        assertTrue(r.library.cachedLoans()!!.data.single().renewPending)
        assertTrue(ok(r.library.loans(force = false)).data.single().renewPending)
        assertEquals(1, count("/v1/me/loans"))
    }

    @Test fun sharedProfileIsClearedOnSignOut() = runTest {
        routes["/v1/me/all"] = { MockResponse().setBody(meAll) }
        routes["/v1/auth/logout"] = { MockResponse().setResponseCode(204) }
        val r = Repos(capsAll)
        ok(r.profile.profile(force = false))
        assertNotNull(r.profile.state.value)
        r.auth.logout()
        assertNull(r.profile.state.value)
        assertNull(r.profile.cachedProfile())
        assertNull(r.library.cachedLoans())
    }

    @Test fun cachedProfileSeedsSharedState() = runTest {
        routes["/v1/me/all"] = { MockResponse().setBody(meAll) }
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll) }
        val c = client()
        val auth = AuthRepository(c, MemStore(session), clock, cache)
        ok(ProfileRepository(c, auth, cache, clock, c).profile(force = false))
        // Ново стартиране: паметта е празна, кешът е на диска → показва се веднага, без мрежа.
        val fresh = ProfileRepository(c, auth, cache, clock, c)
        assertNull(fresh.state.value)
        val before = server.requestCount
        assertEquals("Иван Петров", fresh.cachedProfile()?.data?.fullName)
        assertEquals("Иван Петров", fresh.state.value?.data?.fullName)
        assertEquals(before, server.requestCount)
    }

    // --- възможности ---

    @Test fun capabilitiesSingleInFlight() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll).setBodyDelay(200, TimeUnit.MILLISECONDS) }
        val c = client()
        val all = (1..6).map { async { c.capabilities() } }.awaitAll()
        all.forEach { assertTrue(ok(it).all) }
        assertEquals(1, count("/v1/capabilities"))
        assertTrue(ok(c.capabilities()).messagesBatchRead)
        assertEquals(1, count("/v1/capabilities"))
    }

    @Test fun capabilitiesPersistFor24Hours() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll) }
        val public = InMemoryPayloadCache { clock.now().toEpochMilli() }
        assertTrue(ok(client(public).capabilities()).all)
        assertEquals(1, count("/v1/capabilities"))

        // Ново стартиране (нов клиент) — от публичния кеш, без мрежа.
        clock.advanceSeconds(23 * 3600)
        val second = client(public)
        assertTrue(ok(second.capabilities()).all)
        assertEquals(1, count("/v1/capabilities"))

        // След 24 ч — пак от мрежата (и в паметта на работещия клиент).
        clock.advanceSeconds(3600)
        routes["/v1/capabilities"] = { MockResponse().setBody(capsOld) }
        assertFalse(ok(second.capabilities()).all)
        assertEquals(2, count("/v1/capabilities"))
        assertFalse(ok(client(public).capabilities()).all)
        assertEquals(2, count("/v1/capabilities"))
    }

    @Test fun capabilitiesFailureIsNotRememberedAndOtherApiIgnored() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setResponseCode(500) }
        val public = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val c = client(public)
        assertIs<Outcome.Failure>(c.capabilities())
        // AuthRepository превръща неуспеха в NONE, както досега.
        assertEquals(ServiceCapabilities.NONE, AuthRepository(c, MemStore(null), clock, InMemoryPayloadCache()).capabilities())
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll) }
        assertTrue(ok(c.capabilities()).all)
        val n = count("/v1/capabilities")
        assertEquals(3, n)
        // Запис за друг адрес на API не важи.
        routes.clear()
        val other = client(public, path = "/other")
        assertIs<Outcome.Failure>(other.capabilities())
        assertEquals("/other/v1/capabilities", requests.last().path)
    }

    // --- „прочетено“ на пакет ---

    @Test fun batchReadRequestShape() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsAll) }
        routes["/v1/me/messages/read"] = { MockResponse().setResponseCode(202).setBody("""{"ok":true}""") }
        val c = client()
        assertEquals(Outcome.Success(Unit), c.markMessagesRead(session, listOf("17", "18", "17", "")))
        val req = requests.last()
        assertEquals("/api/v1/me/messages/read", req.path)
        assertEquals("POST", req.method)
        assertEquals("Bearer AT", req.getHeader("Authorization"))
        assertTrue(req.getHeader("Content-Type")!!.startsWith("application/json"))
        val body = Json.parseToJsonElement(req.body.readUtf8()).jsonObject
        assertEquals(listOf("17", "18"), body["ids"]!!.jsonArray.map { it.jsonPrimitive.content })
        assertEquals(setOf("ids"), body.keys)

        // Повече от 50 → на части по 50.
        val before = count("/v1/me/messages/read")
        assertEquals(Outcome.Success(Unit), c.markMessagesRead(session, (1..120).map { "m$it" }))
        assertEquals(before + 3, count("/v1/me/messages/read"))
    }

    @Test fun batchReadWithoutCapabilityIsNotAvailable() = runTest {
        routes["/v1/capabilities"] = { MockResponse().setBody(capsOld) }
        val c = client()
        assertEquals(AppError.NotAvailable(Feature.MESSAGES), (c.markMessagesRead(session, listOf("17")) as Outcome.Failure).error)
        assertEquals(0, count("/v1/me/messages/read"))
    }

    @Test fun repositoryBatchReadUpdatesCache() = runTest {
        routes["/v1/me/all"] = { MockResponse().setBody(meAll) }
        routes["/v1/me/messages/read"] = { MockResponse().setResponseCode(202).setBody("""{"ok":true}""") }
        val r = Repos(capsAll)
        ok(r.messages.messages(force = false))
        assertTrue(r.messages.supportsBatchRead())
        assertEquals(Outcome.Success(Unit), r.messages.markRead(listOf("17", "16")))
        assertTrue(r.messages.cached()!!.data.all { it.read })
        assertEquals(1, count("/v1/me/messages/read"))
        assertEquals(0, requests.count { it.path!!.matches(Regex("/api/v1/me/messages/\\d+/read")) })
    }
}
