package org.chyavorec.app.ui.components

import android.app.Application
import android.os.VibrationEffect
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Подписите на вибрацията: нотите, резервата за 8–10 и спазването на системната настройка „Докосване“. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class HapticsTest {
    private val view get() = View(ApplicationProvider.getApplicationContext<Application>())

    private class FakeCompose : HapticFeedback {
        val types = mutableListOf<HapticFeedbackType>()
        override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { types += hapticFeedbackType }
    }

    private class FakeOutput(val supported: Boolean = true, val works: Boolean = true) : CompositionOutput {
        val played = mutableListOf<List<HapticScores.Note>>()
        override fun supports(primitives: IntArray) = supported
        override fun play(notes: List<HapticScores.Note>): Boolean { played += notes; return works }
    }

    private fun haptics(compose: FakeCompose, out: FakeOutput?, sdk: Int = 34, on: Boolean = true, posted: MutableList<Long> = mutableListOf()) =
        AppHaptics(view, compose, sdk, output = { out }, touchFeedbackOn = { on }, post = { delay, block -> posted += delay; block() })

    @Test fun primitiveConstantsMatchTheFramework() {
        assertEquals(VibrationEffect.Composition.PRIMITIVE_THUD, HapticScores.THUD)
        assertEquals(VibrationEffect.Composition.PRIMITIVE_TICK, HapticScores.TICK)
    }

    @Test fun signaturesHaveTheirShape() {
        assertEquals(2, HapticScores.stitch.size)
        assertTrue(HapticScores.stitch.all { it.primitive == HapticScores.TICK && it.scale <= 0.6f }, "бод: два меки тика")
        assertEquals(listOf(HapticScores.THUD, HapticScores.TICK), HapticScores.seal.map { it.primitive })
        // Разлистване: силата расте, паузите се скъсяват.
        val s = HapticScores.shuffle
        assertTrue(s.zipWithNext().all { (a, b) -> b.scale > a.scale }, "силата расте")
        assertTrue(s.drop(1).zipWithNext().all { (a, b) -> b.delayMs < a.delayMs }, "паузите се скъсяват")
        assertTrue(s.all { it.scale in 0f..1f })
    }

    @Test fun compositionIsUsedWhenSupported() {
        val compose = FakeCompose()
        val out = FakeOutput()
        val h = haptics(compose, out)
        assertTrue(h.usesComposition(HapticSignature.SEAL))
        h.play(HapticSignature.SEAL)
        assertEquals(listOf(HapticScores.seal), out.played)
        assertTrue(compose.types.isEmpty())
    }

    @Test fun touchFeedbackOffMeansSilence() {
        val compose = FakeCompose()
        val out = FakeOutput()
        haptics(compose, out, on = false).play(HapticSignature.STITCH)
        assertTrue(out.played.isEmpty())
        assertTrue(compose.types.isEmpty())
    }

    @Test fun oldAndroidFallsBackToStandardTypes() {
        val compose = FakeCompose()
        val out = FakeOutput()
        val posted = mutableListOf<Long>()
        val h = haptics(compose, out, sdk = 29, posted = posted)
        assertFalse(h.usesComposition(HapticSignature.SHUFFLE))
        h.play(HapticSignature.SHUFFLE)
        assertTrue(out.played.isEmpty())
        // Както досега: тик на всяко разместване, на ~160 ms.
        assertEquals(List(3) { HapticFeedbackType.SegmentTick }, compose.types)
        assertEquals(listOf(160L, 320L), posted)
    }

    @Test fun unsupportedPrimitivesOrFailureFallBack() {
        val a = FakeCompose()
        haptics(a, FakeOutput(supported = false)).play(HapticSignature.SEAL)
        assertEquals(listOf(HapticFeedbackType.Confirm, HapticFeedbackType.SegmentTick), a.types)
        val b = FakeCompose()
        haptics(b, FakeOutput(works = false)).play(HapticSignature.STITCH)
        assertEquals(2, b.types.size)
        val c = FakeCompose()
        haptics(c, null).play(HapticSignature.STITCH)
        assertEquals(2, c.types.size)
    }

    @Test fun saveOffIsTheUsualToggleOff() {
        val compose = FakeCompose()
        haptics(compose, FakeOutput()).save(false)
        assertEquals(listOf(HapticFeedbackType.ToggleOff), compose.types)
    }
}
