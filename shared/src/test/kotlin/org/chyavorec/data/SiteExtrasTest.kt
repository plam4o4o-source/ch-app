package org.chyavorec.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.repository.QuizQuestionsRepository
import org.chyavorec.data.repository.TimelineRepository
import org.chyavorec.data.site.SiteExtrasService
import org.chyavorec.domain.repository.InMemoryPayloadCache
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

/** „Лента на времето“ и „Познай Яворец“: страницата „История“, `app-quiz.json` и кешът им (офлайн). */
class SiteExtrasTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-11-14T10:00:00Z"))
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test", Dispatchers.IO)
    private val requests = Collections.synchronizedList(ArrayList<RecordedRequest>())

    /** Отговорите по път; липсващ път — 404. */
    private val responses = Collections.synchronizedMap(HashMap<String, () -> MockResponse>())

    private val quiz = """
        { "questions": [
          { "id": "sabor-1", "text": "Кога е съборът на Яворец?", "options": ["Първата неделя на юни", "28 август", "6 май"], "answer": 1 },
          { "id": "bad", "text": "?", "options": ["а"], "answer": 3 },
          { "id": "chitalishte-1", "text": "Коя година е основано читалището?", "options": ["1922", "1958"], "answer": 0, "note": "17 декември 1922 г." }
        ] }
    """.trimIndent()

    @BeforeTest fun start() {
        server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    requests += request
                    return responses[request.path.orEmpty()]?.invoke() ?: MockResponse().setResponseCode(404)
                }
            }
            start()
        }
    }

    @AfterTest fun stop() = server.shutdown()

    private fun service() = SiteExtrasService(http, server.url("/").toString().trimEnd('/'), Dispatchers.Unconfined)

    private fun count(path: String) = requests.count { it.path == path }

    @Test fun historyPageGivesTheTimelineAndAPageWithoutItIsAParseError() = runBlocking {
        responses[SiteExtrasService.HISTORY_PATH] = { MockResponse().setBody(TestUtil.resource("site-history.html")) }
        val entries = assertIs<Outcome.Success<*>>(service().fetchHistory()).value as List<*>
        assertEquals(14, entries.size)

        // Сменени класове на сайта — не „празна хронология“, а грешка (екранът показва страницата).
        responses[SiteExtrasService.HISTORY_PATH] = { MockResponse().setBody("<html><body><h1>История</h1><p>Текст</p></body></html>") }
        assertEquals(AppError.Parse("history"), assertIs<Outcome.Failure>(service().fetchHistory()).error)

        responses[SiteExtrasService.HISTORY_PATH] = { MockResponse().setResponseCode(503) }
        assertEquals(AppError.Server(503), assertIs<Outcome.Failure>(service().fetchHistory()).error)
    }

    @Test fun quizFileIsOptionalAndBadRecordsAreSkipped() = runBlocking {
        // Няма файл — няма ръчни въпроси (не е грешка).
        assertEquals(emptyList<Any>(), assertIs<Outcome.Success<*>>(service().fetchQuizQuestions()).value)

        responses[SiteExtrasService.QUIZ_PATH] = { MockResponse().setBody(quiz) }
        val questions = assertIs<Outcome.Success<*>>(service().fetchQuizQuestions()).value as List<*>
        assertEquals(2, questions.size, "грешният запис е пропуснат")
        // Файлът се пита наново (no-cache): поправка на сайта стига до приложението веднага.
        assertEquals("no-cache", requests.last { it.path == SiteExtrasService.QUIZ_PATH }.getHeader("Cache-Control"))

        responses[SiteExtrasService.QUIZ_PATH] = { MockResponse().setBody("{ не е json") }
        assertEquals(AppError.Parse("app-quiz.json"), assertIs<Outcome.Failure>(service().fetchQuizQuestions()).error)

        responses[SiteExtrasService.QUIZ_PATH] = { MockResponse().setResponseCode(500) }
        assertEquals(AppError.Server(500), assertIs<Outcome.Failure>(service().fetchQuizQuestions()).error)
    }

    @Test fun timelineIsCachedForSixHoursAndShownOfflineFromTheCache() = runBlocking {
        responses[SiteExtrasService.HISTORY_PATH] = { MockResponse().setBody(TestUtil.resource("site-history.html")) }
        val cache = InMemoryPayloadCache()
        val repo = TimelineRepository(service(), cache, clock, Dispatchers.Unconfined)
        assertNull(repo.cached())

        val first = assertIs<Outcome.Success<Synced<List<*>>>>(repo.timeline()).value
        assertEquals(14, first.data.size)
        assertTrue(!first.fromCache)
        // До 6 часа — без мрежа.
        clock.advanceSeconds(60 * 60)
        repo.timeline()
        assertEquals(1, count(SiteExtrasService.HISTORY_PATH))

        // След 6 часа сайтът е недостъпен: хронологията е от кеша, с бележка за грешката.
        clock.advanceSeconds(6 * 60 * 60)
        responses[SiteExtrasService.HISTORY_PATH] = { MockResponse().setResponseCode(503) }
        val offline = assertIs<Outcome.Success<Synced<List<*>>>>(repo.timeline()).value
        assertEquals(14, offline.data.size)
        assertEquals(AppError.Server(503), offline.refreshError)
        assertEquals(2, count(SiteExtrasService.HISTORY_PATH))

        // Ново пускане на приложението (нов обект, същият кеш) — веднага от кеша, без мрежа.
        val again = TimelineRepository(service(), cache, clock, Dispatchers.Unconfined)
        val cached = assertNotNull(again.cached())
        assertEquals(14, cached.data.size)
        assertTrue(cached.fromCache)
        assertEquals(2, count(SiteExtrasService.HISTORY_PATH))
    }

    @Test fun quizQuestionsAreCachedAndAMissingFileIsAnEmptyList() = runBlocking {
        val cache = InMemoryPayloadCache()
        val repo = QuizQuestionsRepository(service(), cache, clock, Dispatchers.Unconfined)
        assertEquals(emptyList<Any>(), assertIs<Outcome.Success<Synced<List<*>>>>(repo.questions()).value.data)

        // Файлът се появява на сайта: изричното опресняване го взема, после е в кеша.
        responses[SiteExtrasService.QUIZ_PATH] = { MockResponse().setBody(quiz) }
        assertEquals(2, assertIs<Outcome.Success<Synced<List<*>>>>(repo.questions(force = true)).value.data.size)
        assertEquals(2, assertNotNull(QuizQuestionsRepository(service(), cache, clock, Dispatchers.Unconfined).cached()).data.size)

        // Повреден файл по-късно — остават последните добри въпроси.
        responses[SiteExtrasService.QUIZ_PATH] = { MockResponse().setBody("[") }
        val kept = assertIs<Outcome.Success<Synced<List<*>>>>(repo.questions(force = true)).value
        assertEquals(2, kept.data.size)
        assertEquals(AppError.Parse("app-quiz.json"), kept.refreshError)
    }
}
