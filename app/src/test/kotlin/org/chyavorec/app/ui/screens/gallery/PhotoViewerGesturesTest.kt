package org.chyavorec.app.ui.screens.gallery

import android.app.Application
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.network.ConnectivityChecker
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.Dispatcher
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
import kotlin.test.assertEquals

/**
 * Прегледът на цял екран: тап скрива/показва контролите (и системните ленти), плъзгане надолу
 * след прага го затваря, а по-кратко — не. С екранен четец контролите не се крият.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class)
class PhotoViewerGesturesTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val app = ApplicationProvider.getApplicationContext<Application>()
    private val server = MockWebServer()

    @OptIn(DelicateCoilApi::class)
    @Before fun start() {
        // Снимката липсва на сайта — прегледът стига бързо до едно и също, стабилно съдържание.
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse().setResponseCode(404)
        }
        server.start()
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(app).components {
                add(OkHttpNetworkFetcherFactory(callFactory = { OkHttpClient() }, connectivityChecker = { ConnectivityChecker.ONLINE }))
            }.build(),
        )
    }

    @OptIn(DelicateCoilApi::class)
    @After fun stop() {
        SingletonImageLoader.reset()
        server.shutdown()
    }

    private val photo get() = GalleryPhoto("1", "Сцена", server.url("/p/1-thumb.jpg").toString(), server.url("/p/1.jpg").toString(), "Празници", "")
    private val close get() = app.getString(R.string.action_close)
    private val missing get() = app.getString(R.string.photo_missing_on_site)
    private fun controlsShown() = compose.onAllNodesWithContentDescription(close).fetchSemanticsNodes().isNotEmpty()

    private fun showViewer(onClose: () -> Unit = {}) {
        compose.setContent { ChitalishteTheme { PhotoViewer(listOf(photo), 0, onClose) } }
        compose.waitUntil(10_000) { compose.onAllNodesWithText(missing).fetchSemanticsNodes().isNotEmpty() }
    }

    @Test fun tapTogglesTheControls() {
        showViewer()
        assertEquals(true, controlsShown())
        compose.onNodeWithText(missing).performTouchInput { click() }
        compose.waitUntil(5_000) { !controlsShown() }
        compose.onNodeWithText(missing).performTouchInput { click() }
        compose.waitUntil(5_000) { controlsShown() }
    }

    @Test fun dragPastTheThresholdCloses() {
        var closed = 0
        showViewer(onClose = { closed++ })
        // Кратко плъзгане — снимката се връща, прегледът остава.
        compose.onRoot().performTouchInput { swipeDown(startY = centerY, endY = centerY + height * 0.06f, durationMillis = 500) }
        compose.waitForIdle()
        assertEquals(0, closed)
        // След прага — затваря се.
        compose.onRoot().performTouchInput { swipeDown(startY = centerY, endY = bottom - 1f, durationMillis = 300) }
        compose.waitForIdle()
        assertEquals(1, closed)
    }

    @Test fun controlsStayWithAScreenReader() {
        shadowOf(compose.activity.getSystemService(AccessibilityManager::class.java)).setTouchExplorationEnabled(true)
        showViewer()
        compose.onNodeWithText(missing).performTouchInput { click() }
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
        assertEquals(true, controlsShown(), "с TalkBack „Затвори“ остава достъпно")
    }
}
