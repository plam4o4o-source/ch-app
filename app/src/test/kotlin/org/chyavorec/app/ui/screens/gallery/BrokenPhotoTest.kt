package org.chyavorec.app.ui.screens.gallery

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.networkObserverEnabled
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.model.GalleryPhoto
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertTrue

/**
 * Снимка, която липсва на сайта (404): в мрежата на албума — „Снимката липсва“ с икона (не
 * празна плочка, която изглежда като „още се зарежда“), а в прегледа — съобщение с
 * „Опитай отново“ (не черен екран).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class)
class BrokenPhotoTest {
    @get:Rule val compose = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<Application>()
    private val server = MockWebServer()
    private val requests = AtomicInteger()
    private val paths = java.util.concurrent.CopyOnWriteArrayList<String>()

    @OptIn(DelicateCoilApi::class)
    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests.incrementAndGet()
                paths += request.path.orEmpty()
                return MockResponse().setResponseCode(404)
            }
        }
        server.start()
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(app).components { add(OkHttpNetworkFetcherFactory(callFactory = { OkHttpClient() })) }
                // Без наблюдение на мрежата: Robolectric не съобщава промените ѝ на Coil.
                .networkObserverEnabled(false)
                .build(),
        )
        // Coil пита мрежата само при връзка.
        val cm = app.getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
    }

    @OptIn(DelicateCoilApi::class)
    @After fun stop() {
        SingletonImageLoader.reset()
        server.shutdown()
    }

    @Test fun missingPhotoInTheAlbumGridIsShownAsUnavailable() {
        val url = server.url("/pictures/izlozhba/1.jpg").toString()
        compose.setContent { ChitalishteTheme { PhotoTile(url, 110.dp, "Снимка 1", Modifier.size(110.dp)) } }
        val label = app.getString(R.string.photo_unavailable)
        compose.waitUntil(10_000) { compose.onAllNodesWithContentDescription(label).fetchSemanticsNodes().isNotEmpty() }
        assertTrue(requests.get() >= 1, "снимката е поискана от сайта")
    }

    @Test fun missingPhotoInTheViewerShowsAnErrorWithRetry() {
        val photo = GalleryPhoto("2", "", server.url("/pictures/izlozhba/2-thumb.jpg").toString(), server.url("/pictures/izlozhba/2.jpg").toString(), "Изложба", "")
        compose.setContent { ChitalishteTheme { ZoomableImage(photo, "Снимка 2", active = true) } }
        val retry = app.getString(R.string.action_retry)
        compose.waitUntil(10_000) { compose.onAllNodesWithText(retry).fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText(app.getString(R.string.photo_load_failed)).assertExists()
        // „Опитай отново“ — нова заявка към сайта, и пак съобщението (снимката още липсва).
        val before = requests.get()
        compose.onNodeWithText(retry).performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText(retry).fetchSemanticsNodes().isEmpty() || requests.get() > before }
        val again = runCatching { compose.waitUntil(10_000) { requests.get() > before } }.isSuccess
        assertTrue(again, "заявки: $before → ${requests.get()} $paths\n" + compose.onRoot().printToString())
        compose.waitUntil(10_000) { compose.onAllNodesWithText(retry).fetchSemanticsNodes().isNotEmpty() }
    }
}
