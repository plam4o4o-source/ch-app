package org.chyavorec.app.ui.screens.gallery

import android.app.Application
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.ConnectivityChecker
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.SocketPolicy
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.model.GalleryPhoto
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Снимка, която не се зарежда. Липсва на сайта (404): в мрежата на албума — „Снимката липсва“
 * с името ѝ (не празна плочка, която изглежда като „още се зарежда“), а в прегледа — „липсва на
 * сайта“ без безсмисленото „Опитай отново“. Без връзка: неутрална плочка с „Опитай отново“ (не
 * „липсва“), която опитва пак сама, щом връзката се върне; в прегледа — съобщение с „Опитай отново“.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class)
class BrokenPhotoTest {
    @get:Rule val compose = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<Application>()
    private val server = MockWebServer()
    private val requests = AtomicInteger()
    /** „Мрежата“: без нея сървърът прекъсва връзката, а проверката на Coil казва „офлайн“. */
    @Volatile private var online = true

    @OptIn(DelicateCoilApi::class)
    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (!online) return MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
                requests.incrementAndGet()
                return MockResponse().setResponseCode(404)
            }
        }
        server.start()
        val connectivity = object : ConnectivityChecker {
            override fun isOnline(): Boolean = online
        }
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(app).components {
                // Проверката на връзката на Coil не вижда мрежата на Robolectric — тук я задава тестът.
                add(OkHttpNetworkFetcherFactory(callFactory = { OkHttpClient() }, connectivityChecker = { connectivity }))
            }.build(),
        )
    }

    @OptIn(DelicateCoilApi::class)
    @After fun stop() {
        SingletonImageLoader.reset()
        server.shutdown()
    }

    private fun hasDescription(text: String) = compose.onAllNodesWithContentDescription(text).fetchSemanticsNodes().isNotEmpty()
    private fun hasText(text: String) = compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()

    @Test fun missingPhotoInTheAlbumGridIsShownAsUnavailableWithItsName() {
        val url = server.url("/pictures/izlozhba/1.jpg").toString()
        compose.setContent { ChitalishteTheme { PhotoTile(url, 110.dp, "Сцена", Modifier.size(110.dp)) } }
        compose.waitUntil(10_000) { hasDescription(app.getString(R.string.photo_unavailable_named, "Сцена")) }
        assertTrue(requests.get() >= 1, "снимката е поискана от сайта")
    }

    @Test fun offlineThumbnailIsNotMissingAndRetriesWhenTheConnectionReturns() {
        online = false
        val url = server.url("/pictures/izlozhba/3.jpg").toString()
        val reload = mutableIntStateOf(0)
        compose.setContent { ChitalishteTheme { PhotoTile(url, 110.dp, "Снимка 3", Modifier.size(110.dp), reload = reload.intValue) } }
        val unloaded = app.getString(R.string.photo_not_loaded_named, "Снимка 3")
        compose.waitUntil(10_000) { hasDescription(unloaded) }
        assertTrue(!hasDescription(app.getString(R.string.photo_unavailable_named, "Снимка 3")), "без връзка не е „липсва“")
        compose.onNodeWithContentDescription(app.getString(R.string.action_retry)).assertExists()
        assertEquals(0, requests.get())

        // Връзката се върна — плочката опитва сама (а сайтът казва, че снимката липсва).
        online = true
        reload.intValue++
        compose.waitUntil(10_000) { hasDescription(app.getString(R.string.photo_unavailable_named, "Снимка 3")) }
        assertTrue(requests.get() >= 1)
    }

    @Test fun offlineThumbnailRetriesOnTap() {
        online = false
        val url = server.url("/pictures/izlozhba/4.jpg").toString()
        compose.setContent { ChitalishteTheme { PhotoTile(url, 110.dp, "Снимка 4", Modifier.size(110.dp)) } }
        val retry = app.getString(R.string.action_retry)
        compose.waitUntil(10_000) { hasDescription(retry) }
        online = true
        compose.onNodeWithContentDescription(retry).performClick()
        compose.waitUntil(10_000) { hasDescription(app.getString(R.string.photo_unavailable_named, "Снимка 4")) && requests.get() >= 1 }
    }

    @Test fun missingPhotoInTheViewerSaysSoWithoutRetry() {
        val photo = GalleryPhoto("2", "", server.url("/pictures/izlozhba/2-thumb.jpg").toString(), server.url("/pictures/izlozhba/2.jpg").toString(), "Изложба", "")
        compose.setContent { ChitalishteTheme { ZoomableImage(photo, "Снимка 2", active = true) } }
        compose.waitUntil(10_000) { hasText(app.getString(R.string.photo_missing_on_site)) }
        assertTrue(!hasText(app.getString(R.string.action_retry)), "без „Опитай отново“ — снимката просто я няма")
    }

    @Test fun viewerWithoutConnectionShowsAnErrorWithRetry() {
        online = false
        val photo = GalleryPhoto("5", "", server.url("/pictures/izlozhba/5-thumb.jpg").toString(), server.url("/pictures/izlozhba/5.jpg").toString(), "Изложба", "")
        compose.setContent { ChitalishteTheme { ZoomableImage(photo, "Снимка 5", active = true) } }
        val retry = app.getString(R.string.action_retry)
        compose.waitUntil(10_000) { hasText(retry) }
        compose.onNodeWithText(app.getString(R.string.photo_load_failed)).assertExists()
        // „Опитай отново“ с върната връзка — нова заявка към сайта (който казва, че снимката липсва).
        online = true
        compose.onNodeWithText(retry).performClick()
        // Условието чете и екрана — иначе Robolectric не прекомпозира, докато чакаме.
        compose.waitUntil(10_000) { hasText(app.getString(R.string.photo_missing_on_site)) && requests.get() >= 1 }
    }
}
