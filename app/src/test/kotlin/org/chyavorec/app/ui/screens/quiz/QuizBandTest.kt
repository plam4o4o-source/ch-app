package org.chyavorec.app.ui.screens.quiz

import android.app.Application
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.ConfettiState
import org.chyavorec.app.ui.components.LocalConfetti
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.theme.AppMotion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** „Избродирай отговорите“: схемата на лентата, кога се затваря и кога пуска конфети. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class QuizBandTest {
    @get:Rule val compose = createComposeRule()

    private val colors = QuizBandColors(Color.Yellow, Color.Red, Color.Gray, Color.Yellow, Color.White, Color.Yellow)

    @Test fun theMotifsAreTheBandCutIntoPieces() {
        val band = StitchCharts.band(5)
        val (segments, closing) = QuizBandCharts.of(5)
        assertEquals(5, segments.size)
        // Нищо не се губи и не се дублира: мотивите и затварящият бод са точно лентата.
        assertEquals(band.size, segments.sumOf { it.size } + closing.size)
        segments.forEach {
            assertEquals(QuizBandCharts.PERIOD, it.columns)
            assertEquals(QuizBandCharts.CHART_ROWS, it.rows)
            assertTrue(it.size > 0)
        }
        assertEquals(1, closing.columns)
        assertEquals(1, closing.size, "затварящият бод е един — връзката вдясно")
        assertEquals(41, QuizBandCharts.columns(5))
        assertEquals(41f / 9f, QuizBandCharts.aspect(5))
        // Еднаквите мотиви са едни и същи обекти (строят се веднъж).
        assertTrue(QuizBandCharts.of(5) === QuizBandCharts.of(5))
    }

    @Test fun theBandClosesOnlyWhenEveryAnswerIsRight() {
        assertFalse(QuizBandCharts.closed(emptyList(), 5))
        assertFalse(QuizBandCharts.closed(listOf(true, true, true, true), 5))
        assertFalse(QuizBandCharts.closed(listOf(true, true, false, true, true), 5))
        assertTrue(QuizBandCharts.closed(List(5) { true }, 5))
        assertTrue(QuizBandCharts.closed(List(3) { true }, 3))
        assertEquals(4, QuizBandCharts.stitched(listOf(true, true, false, true, true), 5))
    }

    /** Подгъвът е тичащ бод от прости черти: стъпката и дължината са тези на [StitchSpec.Running]. */
    @Test fun theHemIsRunningStitchAsPlainSegments() {
        val d = 2.625f
        val spec = StitchSpec.Running
        val points = runningStitchSegments(10f, 300f, 5f, d)
        assertEquals(0, points.size % 2, "двойки точки — начало и край на всяка черта")
        val period = (spec.dashDp + spec.gapDp) * d
        val on = spec.dashDp * d - spec.widthDp * d
        assertEquals(kotlin.math.ceil((300f - 10f) / period).toInt(), points.size / 2)
        for (k in 0 until points.size / 2) {
            val a = points[2 * k]
            val b = points[2 * k + 1]
            assertEquals(10f + k * period, a.x, 1e-3f)
            assertEquals(5f, a.y)
            assertTrue(b.x - a.x <= on + 1e-3f && b.x <= 300f)
        }
        assertTrue(runningStitchSegments(10f, 10f, 0f, d).isEmpty())
    }

    /** Конфетито — над средата на лентата, с [ConfettiLift] над горния ѝ край (не от средата ѝ). */
    @Test fun theConfettiStartsAboveTheBand() {
        val band = Rect(40f, 1500f, 1040f, 1700f)
        val o = QuizBandCharts.confettiOrigin(band, 189f)
        assertEquals(band.center.x, o.x)
        assertEquals(1500f - 189f, o.y)
        assertEquals(0f, QuizBandCharts.confettiOrigin(Rect(0f, 50f, 100f, 90f), 189f).y, "не над екрана")
        assertFalse(QuizBandCharts.confettiOrigin(null, 189f).isSpecified)
    }

    @Test fun theFifthRightAnswerClosesTheBandWithConfettiOnce() {
        val confetti = ConfettiState(seed = 1)
        val results = mutableStateOf(List(4) { true })
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false, LocalConfetti provides confetti) {
                QuizBand(results.value, 5, current = 4, colors = colors, modifier = Modifier.size(300.dp, 66.dp))
            }
        }
        compose.waitForIdle()
        assertEquals(0, confetti.bursts, "вече отговорените не се бродират наново")
        compose.mainClock.autoAdvance = false
        compose.runOnUiThread { results.value = List(5) { true }; Snapshot.sendApplyNotifications() }
        // Конфетито тръгва със затварящия бод — докато иглата довършва петия мотив, не след нея.
        compose.mainClock.advanceTimeBy(CLOSE_LEAD_MS - 100)
        assertEquals(0, confetti.bursts)
        compose.mainClock.advanceTimeBy(200)
        assertEquals(1, confetti.bursts)
        assertTrue(CLOSE_LEAD_MS < AppMotion.StitchMs)
        compose.mainClock.autoAdvance = true
        compose.mainClock.advanceTimeBy(2_000)
        compose.waitForIdle()
        assertEquals(1, confetti.bursts)
        compose.mainClock.advanceTimeBy(2_000)
        compose.waitForIdle()
        assertEquals(1, confetti.bursts, "само веднъж")
    }

    @Test fun aWrongLastAnswerOrReducedMotionMeansNoConfetti() {
        val confetti = ConfettiState(seed = 1)
        val results = mutableStateOf(List(4) { true })
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false, LocalConfetti provides confetti) {
                QuizBand(results.value, 5, current = null, colors = colors, modifier = Modifier.size(300.dp, 66.dp))
            }
        }
        compose.runOnIdle { results.value = List(4) { true } + false }
        compose.mainClock.advanceTimeBy(2_000)
        compose.waitForIdle()
        assertEquals(0, confetti.bursts)
    }

    @Test fun reducedMotionClosesTheBandWithoutConfetti() {
        val confetti = ConfettiState(seed = 1)
        val results = mutableStateOf(List(4) { true })
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true, LocalConfetti provides confetti) {
                QuizBand(results.value, 5, current = null, colors = colors, modifier = Modifier.size(300.dp, 66.dp))
            }
        }
        compose.runOnIdle { results.value = List(5) { true } }
        compose.waitForIdle()
        assertEquals(0, confetti.bursts)
    }

    /** Връщане към викторината (нов процес, завъртане): лентата е както е била — без бродиране и конфети. */
    @Test fun aRestoredBandDoesNotReplay() {
        val confetti = ConfettiState(seed = 1)
        val restore = StateRestorationTester(compose)
        restore.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false, LocalConfetti provides confetti) {
                QuizBand(List(5) { true }, 5, current = null, colors = colors, modifier = Modifier.size(300.dp, 66.dp))
            }
        }
        restore.emulateSavedInstanceStateRestore()
        compose.waitForIdle()
        assertEquals(0, confetti.bursts)
    }
}
