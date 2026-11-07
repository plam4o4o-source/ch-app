package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Конфетито като състояние (не пиксели): успехът пуска избухване от мястото на докосването; при намалено движение — не. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class ConfettiTest {
    @get:Rule val compose = createComposeRule()

    private fun content(state: ConfettiState, reduced: Boolean) {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides reduced) {
                ConfettiHost(Modifier.size(300.dp), state = state) {
                    val success = rememberSuccessFeedback()
                    Box(Modifier.align(Alignment.BottomEnd).size(80.dp).testTag("action").clickable { success() }) { Text("OK") }
                }
            }
        }
    }

    @Test fun successBurstsFromTheTappedActionAndEnds() {
        val state = ConfettiState(seed = 1)
        content(state, reduced = false)
        compose.onNodeWithTag("action").performClick()
        // Часовникът тече сам: избухването се изиграва докрай и слоят изчезва.
        compose.waitForIdle()
        assertEquals(1, state.bursts)
        assertEquals(1, state.played, "конфетито не тръгна")
        assertFalse(state.isPlaying, "конфетито не спря")
        // От докоснатия бутон (долу вдясно), не от средата.
        val origin = state.origin
        assertTrue(origin.isSpecified)
        val side = compose.onNodeWithTag("action").fetchSemanticsNode().boundsInRoot
        assertTrue(side.contains(origin), "началото $origin е извън бутона $side")
    }

    @Test fun reducedMotionHasNoConfetti() {
        compose.mainClock.autoAdvance = false
        val state = ConfettiState(seed = 1)
        content(state, reduced = true)
        compose.onNodeWithTag("action").performClick()
        repeat(4) {
            compose.mainClock.advanceTimeBy(150)
            assertFalse(state.isPlaying)
        }
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
        // Успехът е отчетен (вибрацията и снекбарът остават) — само без частици.
        assertEquals(1, state.bursts)
        assertEquals(0, state.played)
    }

    @Test fun withoutAHostSuccessIsJustHaptic() {
        var called = false
        compose.setContent {
            val success = rememberSuccessFeedback()
            Box(Modifier.size(80.dp).testTag("action").clickable { success(); called = true })
        }
        compose.onNodeWithTag("action").performClick()
        compose.waitForIdle()
        assertTrue(called)
    }
}
