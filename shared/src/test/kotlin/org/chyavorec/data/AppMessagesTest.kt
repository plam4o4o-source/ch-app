package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.repository.MessagesRepository
import org.chyavorec.data.site.AppMessagesParser
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.MessageAudience
import org.chyavorec.domain.model.MessagePriority
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.PayloadCache
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppMessagesTest {
    private val sample = """
        [
          {"id":"msg-1","title":"Промяна в работното време","body":"От 1 октомври…","audience":"all","priority":"normal",
           "createdAt":"2026-09-20T08:00:00Z","author":"pacho"},
          {"id":"msg-2","title":"Събрание на членовете","body":"Общо събрание","audience":"members","priority":"high",
           "createdAt":"2026-09-25T08:00:00Z","expiresAt":"2026-10-05","url":"https://chyavorec.org/news/x"},
          {"id":"msg-3","title":"Стара покана","body":"…","audience":"all","createdAt":"2026-08-01T08:00:00Z","expiresAt":"2026-08-10"},
          {"id":"msg-4","title":"Без дата","body":"невалидно"},
          {"id":"msg-5","title":"Небезопасна връзка","body":"x","createdAt":"2026-09-21T08:00:00Z","url":"http://evil"},
          "junk"
        ]
    """.trimIndent()

    @Test fun parsesAndSkipsInvalid() {
        val list = requireNotNull(AppMessagesParser.parse(sample))
        assertEquals(listOf("msg-2", "msg-5", "msg-1", "msg-3"), list.map { it.id })
        val m2 = list.first()
        assertEquals(MessageAudience.MEMBERS, m2.audience)
        assertEquals(MessagePriority.HIGH, m2.priority)
        assertEquals("2026-10-05", m2.expiresOn)
        assertEquals("https://chyavorec.org/news/x", m2.url)
        assertNull(list.first { it.id == "msg-5" }.url)
        assertNull(AppMessagesParser.parse("{\"not\":\"array\"}"))
    }

    @Test fun visibilityByMembershipAndExpiry() {
        val list = requireNotNull(AppMessagesParser.parse(sample))
        val today = LocalDate.parse("2026-09-26")
        assertEquals(listOf("msg-5", "msg-1"), AppMessagesParser.visible(list, isMember = false, today).map { it.id })
        assertEquals(listOf("msg-2", "msg-5", "msg-1"), AppMessagesParser.visible(list, isMember = true, today).map { it.id })
        // Последният ден на валидност е включително.
        assertTrue(AppMessagesParser.visible(list, true, LocalDate.parse("2026-10-05")).any { it.id == "msg-2" })
        assertTrue(AppMessagesParser.visible(list, true, LocalDate.parse("2026-10-06")).none { it.id == "msg-2" })
    }

    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() = server.shutdown()

    private fun site(): ChyavorecSiteService {
        val base = server.url("/").toString().trimEnd('/')
        val fallback = Contacts("НЧ", null, emptyList(), emptyList(), emptyList(), base, null, emptyList(), "q", false, null)
        return ChyavorecSiteService(http, base, clock, fallback)
    }

    private class MemCache : PayloadCache {
        val map = mutableMapOf<String, CachedPayload>()
        override suspend fun read(key: String) = map[key]
        override suspend fun write(key: String, text: String) { map[key] = CachedPayload(text, 0L) }
        override suspend fun remove(key: String) { map.remove(key) }
        override suspend fun clear() = map.clear()
    }

    @Test fun repositoryFiltersForNonMembers() = runTest {
        server.enqueue(MockResponse().setBody(sample))
        val repo = MessagesRepository(site(), MemCache(), clock)
        val r = repo.messages(isMember = false, force = true)
        assertEquals(listOf("msg-5", "msg-1"), (r as Outcome.Success).value.data.map { it.id })
        assertEquals("/data/app-messages.json", server.takeRequest().path)
        assertEquals(listOf("msg-2", "msg-5", "msg-1"), repo.cached(isMember = true).map { it.id })
    }

    @Test fun noFileMeansNoMessages() = runTest {
        server.enqueue(MockResponse().setResponseCode(404))
        val r = site().fetchMessages()
        assertEquals(emptyList(), (r as Outcome.Success).value)
    }

    @Test fun brokenFileIsAnError() = runTest {
        server.enqueue(MockResponse().setBody("<html>"))
        assertIs<Outcome.Failure>(site().fetchMessages())
    }
}
