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
import java.time.Instant
import java.util.Collections
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Колко пъти се сменя състоянието на началния екран при отваряне — всяка смяна е
 * прекомпозиция на HomeScreen. Кешът, мрежата и празникът идват на групи, не поотделно.
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
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Въздвижение на Светия Кръст Господен. **Кръстовден**"}""")
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
            main.idle()
            val s = vm.state.value
            if (states.size != seen) { seen = states.size; quietSince = System.currentTimeMillis() }
            val loaded = s.news.data != null && !s.news.refreshing && s.newBooks.isNotEmpty() && s.forYou.isNotEmpty() && s.feast != null
            if (loaded && System.currentTimeMillis() - quietSince > 1_500) break
            Thread.sleep(20)
        }
        scope.cancel()
        return states.toList()
    }

    @Test fun openingHomeChangesStateOnlyAFewTimes() {
        // Първо пускане: без кеш — всичко от мрежата.
        val cold = open(container())
        val last = cold.last()
        assertNotNull(last.news.data)
        assertTrue(last.newBooks.isNotEmpty() && last.forYou.isNotEmpty() && last.feast != null, "заредено: $last")
        assertTrue(cold.size <= 6, "първо пускане: ${cold.size} състояния")

        // Ново пускане на процеса (нов контейнер, същите файлове): кешът наведнъж, после мрежата.
        val warm = open(container())
        val first = warm.drop(1).first()
        assertTrue(first.news.data != null && first.newBooks.isNotEmpty() && first.catalogCount != null,
            "кешираните новини и книги — в една промяна: $first")
        assertTrue(warm.size <= 5, "с кеш: ${warm.size} състояния")
    }
}
