package org.chyavorec.app.ui.components

import android.app.Application
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Картата не остава в „Скорошни“ и в снимки: FLAG_SECURE само докато е на екрана. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SecureScreenTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private val secure get() = compose.activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0

    @Test fun flagIsSetWhileShownAndClearedOnLeaving() {
        val card = mutableStateOf(true)
        val fullscreen = mutableStateOf(false)
        compose.setContent {
            if (card.value) SecureScreen()
            if (fullscreen.value) SecureScreen()
        }
        compose.waitForIdle()
        assertTrue(secure)
        // Преходът „карта → цял екран“: двата екрана са за миг заедно — излизащият не сваля флага.
        fullscreen.value = true
        compose.waitForIdle()
        card.value = false
        compose.waitForIdle()
        assertTrue(secure)
        fullscreen.value = false
        compose.waitForIdle()
        assertFalse(secure)
    }
}
