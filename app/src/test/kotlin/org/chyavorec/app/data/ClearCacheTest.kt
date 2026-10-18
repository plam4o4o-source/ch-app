package org.chyavorec.app.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.FavoriteNewsEntity
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.domain.model.CatalogQuery
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * „Изчисти кеша“ (Настройки): каталогът изчезва изцяло — файлът, записаното до него,
 * обобщението и индексът в базата — а личните данни (любими, читателският кеш) остават.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ClearCacheTest {
    private val server = MockWebServer()
    @Volatile private var catalogRequests = 0

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json")).also { catalogRequests++ }
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun container(): AppContainer {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        return AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
    }

    @Test fun clearsTheWholeCatalogButKeepsPersonalData(): Unit = runBlocking {
        val c = container()
        val catalog = c.catalogRepository
        assertIs<Outcome.Success<*>>(catalog.searcher())
        assertNotNull(catalog.lookupSearcher(network = false))
        assertNotNull(catalog.homeSummary())
        assertNotNull(c.catalogDatabase.index().stateJson(), "индексът в базата е попълнен")
        c.database.favorites().upsert(FavoriteNewsEntity("n1", "Новина", "https://chyavorec.org/news/n1", null, "", null, null, 1))
        c.readerCache.write("reader:history", "[]")
        val requests = catalogRequests

        c.clearPublicCache()

        listOf(CatalogRepository.KEY, CatalogRepository.META_KEY, CatalogRepository.FETCHED_AT_KEY, CatalogRepository.SUMMARY_KEY)
            .forEach { assertNull(c.publicCache.read(it), it) }
        assertNull(c.catalogDatabase.index().stateJson(), "без стари записи в индекса")
        assertNull(catalog.homeSummary(), "без обобщение и „За теб“ от стария каталог")
        assertNull(catalog.cachedSearcher())
        assertNull(catalog.lookupSearcher(network = false))
        // Личното остава.
        assertEquals(listOf("n1"), c.database.favorites().observeIds().first())
        assertNotNull(c.readerCache.read("reader:history"))

        // Следващото отваряне на каталога: свален наново, индексът — попълнен, без срив.
        assertIs<Outcome.Success<*>>(catalog.searcher())
        assertEquals(requests + 1, catalogRequests)
        val searcher = assertNotNull(catalog.lookupSearcher(network = false)).data
        assertEquals(60, searcher.search(CatalogQuery()).size)
        assertNotNull(c.catalogDatabase.index().stateJson())
    }
}
