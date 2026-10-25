package org.chyavorec.app.ui.screens.catalog

import android.app.Application
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
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
import org.chyavorec.core.Outcome
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Споделена връзка към книга (`#zapis=<inv>`), добавена след последното сваляне на
 * каталога: кешираният каталог (още „пресен“) я няма — екранът опреснява каталога веднъж
 * и я намира; „няма я“ (с „Опитай отново“) — само ако я няма и в опреснения.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class BookViewModelTest {
    private val server = MockWebServer()
    private val sample = TestResources.text("katalog-sample.json")
    @Volatile private var body = sample
    @Volatile private var catalogRequests = 0
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/katalog.json" -> MockResponse().setBody(body).also { catalogRequests++ }
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
        return AppContainer(ApplicationProvider.getApplicationContext(), config, clock, XorTestCipher(), OkHttpClient())
    }

    /** ViewModel-ът работи на главната нишка (Robolectric) — тя се върти, докато книгата се потърси. */
    private fun BookViewModel.awaitLoaded(): BookViewModel.BookUi {
        val main = shadowOf(Looper.getMainLooper())
        val end = System.currentTimeMillis() + 20_000
        while (System.currentTimeMillis() < end) {
            main.idleFor(Duration.ofMillis(20))
            val u = ui.value
            if (u.loaded || u.error != null) return u
            Thread.sleep(20)
        }
        error("книгата не е потърсена: ${ui.value}")
    }

    @Test fun bookAddedAfterTheLastDownloadIsFoundAfterOneRefresh() {
        val c = container()
        runBlocking { assertIs<Outcome.Success<*>>(c.catalogRepository.searcher()) }
        assertEquals(1, catalogRequests)
        // 10 минути по-късно (каталогът е още „пресен“) в библиотеката е добавена нова книга.
        clock.advanceSeconds(10 * 60)
        body = sample.replace("\"inv\": 399666,", "\"inv\": 399667,")

        val added = BookViewModel(399667L, c).awaitLoaded()
        assertEquals(399667L, added.book?.inv, "намерена след опресняването: $added")
        assertEquals(2, catalogRequests)

        // Книга, която я няма и в опреснения каталог: „няма я“ без ново сваляне (току-що е свален)…
        val vm = BookViewModel(1L, c)
        val missing = vm.awaitLoaded()
        assertTrue(missing.loaded)
        assertNull(missing.book)
        assertEquals(2, catalogRequests)
        // …а „Опитай отново“ проверява пак.
        vm.retry()
        val again = vm.awaitLoaded()
        assertNull(again.book)
        assertEquals(3, catalogRequests)
    }
}
