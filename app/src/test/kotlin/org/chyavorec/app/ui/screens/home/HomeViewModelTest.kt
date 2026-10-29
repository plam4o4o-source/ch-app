package org.chyavorec.app.ui.screens.home

import android.app.Application
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Duration
import java.time.Instant
import java.util.Collections
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Отварянето на началния екран: всичко се зарежда, а кешираните новини и книгите от
 * обобщението на каталога влизат в една промяна на състоянието (една прекомпозиция на
 * HomeScreen), не поотделно. Броят на промените не се проверява — той зависи от реда, в
 * който пристигат мрежовите отговори, т.е. от натоварването на машината.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class HomeViewModelTest {
    private val server = MockWebServer()

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
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

    /** Отваря началния екран и връща всяко различно състояние, докато всичко се зареди и утихне. */
    private fun open(c: AppContainer): List<HomeUiState> {
        val vm = HomeViewModel(
            c.newsRepository, c.eventsRepository, c.catalogRepository, c.siteRepository, c.clock,
            c.libraryRepository, c.authRepository, seasonal = flowOf(false), dismissedOccasion = flowOf(null),
            // Обобщението се чете на IO нишка, а изчакването му е по часовника на Robolectric,
            // който цикълът по-долу върти — с кратко изчакване на бавна машина новините от
            // кеша биха излезли сами. Минута (повече от целия цикъл) = винаги се изчаква.
            coalesceMs = 60_000,
        )
        val states = Collections.synchronizedList(ArrayList<HomeUiState>())
        val scope = CoroutineScope(Dispatchers.Unconfined)
        vm.state.onEach { states += it }.launchIn(scope)
        // ViewModel-ът работи на главната нишка (Robolectric) — тя се върти, докато
        // всичко пристигне и състоянието спре да се мени.
        val main = shadowOf(Looper.getMainLooper())
        val end = System.currentTimeMillis() + 20_000
        var quietSince = System.currentTimeMillis()
        var seen = 0
        while (System.currentTimeMillis() < end) {
            // Часовникът на Robolectric напредва само така (кратките изчаквания във ViewModel-а).
            main.idleFor(Duration.ofMillis(20))
            val s = vm.state.value
            if (states.size != seen) { seen = states.size; quietSince = System.currentTimeMillis() }
            val loaded = s.news.data != null && !s.news.refreshing && s.newBooks.isNotEmpty() && s.forYou.isNotEmpty() && s.feast != null
            if (loaded && System.currentTimeMillis() - quietSince > 1_500) break
            Thread.sleep(20)
        }
        scope.cancel()
        return states.toList()
    }

    @Test fun openingHomeLoadsEverythingAndShowsCachedNewsWithBooks() {
        // Първо пускане: без кеш — всичко от мрежата.
        val cold = open(container())
        assertLoaded(cold.last(), "първо пускане")

        // Ново пускане на процеса (нов контейнер, същите файлове): кешът наведнъж, после мрежата.
        val warm = open(container())
        assertLoaded(warm.last(), "с кеш")
        // Първото състояние с новини е от кеша (мрежата още се опреснява) и вече има книгите от
        // обобщението; и след него никое състояние с новини не е без книгите.
        val first = warm.first { it.news.data != null }
        assertTrue(first.news.refreshing, "първите новини са от кеша: $first")
        assertTrue(first.newBooks.isNotEmpty() && first.catalogCount != null, "кешираните новини и книги — в една промяна: $first")
        val withoutBooks = warm.filter { it.news.data != null && (it.newBooks.isEmpty() || it.catalogCount == null) }
        assertTrue(withoutBooks.isEmpty(), "новини без книгите: $withoutBooks")
    }

    /**
     * Празникът и събитията идват по-късно от останалото, но мястото им е запазено от първото
     * състояние — не се вмъкват после над вече показаните книги (началото не „скача“).
     */
    @Test fun feastAndEventsKeepTheirPlaceWhileLoading() {
        val states = open(container())
        states.forEach { s ->
            assertTrue(s.eventsPending || s.upcoming.isNotEmpty(), "без място за събитията: $s")
            assertTrue(s.feastPending || s.feast != null, "без място за празника: $s")
        }
        val last = states.last()
        assertTrue(!last.eventsPending && last.upcoming.isNotEmpty(), "събитията: $last")
        assertTrue(!last.feastPending && last.feast != null, "празникът: $last")
    }

    private fun assertLoaded(s: HomeUiState, label: String) {
        assertNotNull(s.news.data, label)
        assertTrue(s.news.data!!.isNotEmpty() && !s.news.refreshing, "$label — новините: $s")
        assertTrue(s.newBooks.isNotEmpty() && s.catalogCount != null, "$label — каталогът: $s")
        assertTrue(s.forYou.isNotEmpty(), "$label — „За теб“: $s")
        assertNotNull(s.feast, "$label — празникът")
    }
}
