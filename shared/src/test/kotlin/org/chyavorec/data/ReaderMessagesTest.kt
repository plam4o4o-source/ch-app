package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.invlib.RemoteInvLibClient
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.ReaderMessagesRepository
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.Inbox
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReaderMessagesTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-10-10T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")
    private val session = AuthSession("AT", "RT", Instant.parse("2026-10-10T12:00:00Z").toEpochMilli(), "R1")

    private val capsOn = """{"apiVersion":1,"login":true,"loans":true,"messages":true}"""
    private val capsOff = """{"apiVersion":1,"login":true,"loans":true,"history":true}"""
    private val body = """{"items":[
        {"id":"16","title":"","text":"По-стара","at":"2026-10-01T08:00:00.000Z","read":true},
        {"id":"17","title":"Запазена книга","text":"Книгата пристигна, вземете я до петък.","at":"2026-10-10T09:15:00.000Z","read":false},
        {"id":"","title":"без id","text":"x","at":"2026-10-10T09:00:00Z","read":false}
        ],"generated":"2026-10-10T09:30:00Z"}"""

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() = server.shutdown()

    private fun client() = RemoteInvLibClient(http, server.url("/api").toString(), clock, "test-device")

    private class MemStore(var s: AuthSession?) : SessionStore {
        override suspend fun load() = s
        override suspend fun save(session: AuthSession, persist: Boolean) { s = session }
        override suspend fun isPersisted() = s != null
        override suspend fun clear() { s = null }
    }

    @Test fun parsesMessagesNewestFirstAndSkipsInvalid() = runTest {
        server.enqueue(MockResponse().setBody(capsOn))
        server.enqueue(MockResponse().setBody(body).setHeader("Cache-Control", "no-store"))
        val c = client()
        assertTrue((c.capabilities() as Outcome.Success).value.messages)
        val list = (c.messages(session) as Outcome.Success).value
        assertEquals(listOf("17", "16"), list.map { it.id })
        assertEquals("Запазена книга", list[0].title)
        assertEquals("Книгата пристигна, вземете я до петък.", list[0].text)
        assertEquals("2026-10-10T09:15:00.000Z", list[0].at)
        assertFalse(list[0].read)
        assertEquals("", list[1].title)
        assertTrue(list[1].read)
        server.takeRequest() // capabilities
        val req = server.takeRequest()
        assertEquals("/api/v1/me/messages", req.path)
        assertEquals("GET", req.method)
        assertEquals("Bearer AT", req.getHeader("Authorization"))
        // Съдържанието не изтича през toString (логове, грешки).
        assertFalse(list[0].toString().contains("Книгата"))
    }

    @Test fun missingCapabilityIsNotAvailableWithoutRequest() = runTest {
        server.enqueue(MockResponse().setBody(capsOff))
        val c = client()
        assertFalse((c.capabilities() as Outcome.Success).value.messages)
        assertEquals(AppError.NotAvailable(Feature.MESSAGES), (c.messages(session) as Outcome.Failure).error)
        assertEquals(AppError.NotAvailable(Feature.MESSAGES), (c.markMessageRead(session, "17") as Outcome.Failure).error)
        assertEquals(1, server.requestCount)
        // Старата реализация без онлайн API също казва „няма“, не симулира.
        val none = UnavailableInvLibServices()
        assertEquals(AppError.NotAvailable(Feature.MESSAGES), (none.messages(session) as Outcome.Failure).error)
    }

    @Test fun markReadPostsEmptyBodyAndAccepts202() = runTest {
        server.enqueue(MockResponse().setBody(capsOn))
        server.enqueue(MockResponse().setResponseCode(202).setBody("""{"ok":true}"""))
        server.enqueue(MockResponse().setResponseCode(404).setBody("""{"error":"not_found"}"""))
        server.enqueue(MockResponse().setResponseCode(429).setHeader("Retry-After", "20"))
        val c = client()
        assertEquals(Outcome.Success(Unit), c.markMessageRead(session, "17"))
        server.takeRequest()
        val req = server.takeRequest()
        assertEquals("/api/v1/me/messages/17/read", req.path)
        assertEquals("POST", req.method)
        assertEquals(0L, req.bodySize)
        assertEquals("Bearer AT", req.getHeader("Authorization"))
        assertEquals(AppError.NotFound, (c.markMessageRead(session, "99") as Outcome.Failure).error)
        assertEquals(AppError.RateLimited(20), (c.markMessageRead(session, "17") as Outcome.Failure).error)
    }

    @Test fun repositoryCachesAndToleratesNotFound() = runTest {
        server.enqueue(MockResponse().setBody(capsOn))
        server.enqueue(MockResponse().setBody(body))
        server.enqueue(MockResponse().setResponseCode(202).setBody("""{"ok":true}"""))
        server.enqueue(MockResponse().setResponseCode(404))
        val c = client()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val auth = AuthRepository(c, MemStore(session), clock, cache)
        auth.restore()
        val repo = ReaderMessagesRepository(c, auth, cache, clock)

        val first = (repo.messages(force = false) as Outcome.Success).value
        assertEquals(listOf("17", "16"), first.data.map { it.id })
        // Кратък кеш: второ четене до 5 минути не ходи по мрежата.
        val count = server.requestCount
        assertEquals(first.data, (repo.messages(force = false) as Outcome.Success).value.data)
        assertEquals(count, server.requestCount)

        assertEquals(Outcome.Success(Unit), repo.markRead("17"))
        assertTrue(repo.cached()!!.data.first { it.id == "17" }.read)
        // 404 — съобщението вече го няма при читателя: не е грешка, няма повторни опити.
        assertEquals(Outcome.Success(Unit), repo.markRead("99"))

        // Изход → кешът с личните съобщения се изтрива.
        auth.signOutLocally()
        assertNull(repo.cached())
    }

    @Test fun repositoryRequiresSession() = runTest {
        val c = client()
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val auth = AuthRepository(c, MemStore(null), clock, cache)
        val repo = ReaderMessagesRepository(c, auth, cache, clock)
        assertEquals(AppError.Unauthorized, (repo.messages() as Outcome.Failure).error)
        assertEquals(0, server.requestCount)
    }

    @Test fun inboxMergesNewestFirstWithPrefixedIds() {
        val site = listOf(
            AppMessage("msg-1", "Работно време", "…", createdAt = "2026-10-05T08:00:00Z"),
            AppMessage("msg-2", "Събрание", "…", createdAt = "2026-10-10T08:00:00Z"),
        )
        val personal = listOf(
            ReaderMessage("17", "Запазена книга", "Пристигна", "2026-10-10T09:15:00.000Z"),
            ReaderMessage("16", "", "По-стара", "2026-10-01T08:00:00Z", read = true),
            ReaderMessage("15", "", "Без дата", ""),
        )
        val merged = Inbox.merge(site, personal)
        assertEquals(listOf("p:17", "msg-2", "msg-1", "p:16", "p:15"), merged.map { it.id })
        assertTrue(merged.first().personal)
        assertFalse(merged.first { it.id == "msg-1" }.personal)
        assertEquals("Пристигна", merged.first().body)
        assertEquals("17", Inbox.serverId("p:17"))
        assertNull(Inbox.serverId("msg-1"))
        assertNull(Inbox.serverId("p:"))
        assertEquals(site, Inbox.merge(site, emptyList()))
    }
}
