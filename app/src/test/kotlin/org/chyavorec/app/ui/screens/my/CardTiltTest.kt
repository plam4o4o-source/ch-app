package org.chyavorec.app.ui.screens.my

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin

/** Наклонът на картата: изглаждане, мъртва зона, покой на сензора и събуждане (чисти функции). */
class CardTiltTest {
    @Test fun smoothingMovesPartOfTheWay() {
        assertEquals(0.18f, TiltLogic.smooth(0f, 1f), 1e-6f)
        assertEquals(0.5f, TiltLogic.smooth(0.5f, 0.5f), 0f)
    }

    @Test fun deadBandSkipsTinyChanges() {
        assertFalse(TiltLogic.pastDeadBand(0.2f, -0.1f, 0.2f, -0.1f))
        assertFalse(TiltLogic.pastDeadBand(0.2f, -0.1f, 0.2025f, -0.1025f))
        assertTrue(TiltLogic.pastDeadBand(0.2f, -0.1f, 0.204f, -0.1f))
        assertTrue(TiltLogic.pastDeadBand(0.2f, -0.1f, 0.2f, -0.104f))
    }

    /** Трептене на сензора около едно положение: малко записи и после — нито един (нула кадри). */
    @Test fun jitterSettlesToNoWrites() {
        var shownX = 0.3f
        var shownY = -0.2f
        var sx = shownX
        var sy = shownY
        var writes = 0
        var lastWrite = -1
        for (i in 0 until 3000) {
            // ±0.002 шум около покоя.
            val nx = 0.3f + 0.002f * sin(i * 1.7f)
            val ny = -0.2f + 0.002f * cos(i * 2.3f)
            sx = TiltLogic.smooth(sx, nx)
            sy = TiltLogic.smooth(sy, ny)
            if (TiltLogic.pastDeadBand(shownX, shownY, sx, sy)) {
                shownX = sx; shownY = sy; writes++; lastWrite = i
            }
        }
        assertEquals("записи при трептене под мъртвата зона", 0, writes)
        assertEquals(-1, lastWrite)
    }

    /** Истинско движение се следи докрай: показаният наклон стига целта до мъртвата зона. */
    @Test fun realMovementIsFollowed() {
        var shown = 0f
        var s = 0f
        var writes = 0
        repeat(120) {
            s = TiltLogic.smooth(s, 0.6f)
            if (TiltLogic.pastDeadBand(shown, 0f, s, 0f)) { shown = s; writes++ }
        }
        assertTrue(writes > 5)
        assertEquals(0.6f, shown, TiltLogic.DEAD_BAND + 0.001f)
    }

    @Test fun restAfterFifteenSecondsWithoutMovement() {
        val t = RestTimer()
        t.reset(0L, 0.1f, 0.1f)
        assertFalse(t.resting(5_000L, 0.11f, 0.09f))
        assertFalse(t.resting(14_999L, 0.115f, 0.1f))
        assertTrue(t.resting(15_000L, 0.1f, 0.11f))
    }

    @Test fun movementRestartsTheRestTimer() {
        val t = RestTimer()
        t.reset(0L, 0f, 0f)
        assertFalse(t.resting(14_000L, 0.05f, 0f)) // движение над прага → таймерът отначало
        assertFalse(t.resting(28_000L, 0.05f, 0f))
        assertTrue(t.resting(29_000L, 0.06f, 0.01f))
        // Нов допир (reset) пак дава 15 s.
        t.reset(30_000L, 0.06f, 0.01f)
        assertFalse(t.resting(44_000L, 0.06f, 0.01f))
        assertTrue(t.resting(45_000L, 0.06f, 0.01f))
    }

    @Test fun gravityWakeNeedsARealTilt() {
        val flat = floatArrayOf(0f, 9.81f, 0f)
        // Шум на акселерометъра (≈0.03 m/s²) не буди сензора.
        assertFalse(TiltLogic.tilted(flat, 0.03f, 9.8f, -0.02f))
        // ≈1.5° — още не; ≈4° — да.
        val a15 = Math.toRadians(1.5)
        assertFalse(TiltLogic.tilted(flat, (9.81 * sin(a15)).toFloat(), (9.81 * cos(a15)).toFloat(), 0f))
        val a4 = Math.toRadians(4.0)
        assertTrue(TiltLogic.tilted(flat, (9.81 * sin(a4)).toFloat(), (9.81 * cos(a4)).toFloat(), 0f))
        // Наклонът от проверката на емулатора.
        assertTrue(TiltLogic.tilted(flat, 4.5f, 8.7f, 0f))
    }

    @Test fun gravityWakeIgnoresInvalidReadings() {
        val flat = floatArrayOf(0f, 9.81f, 0f)
        assertFalse(TiltLogic.tilted(flat, 0f, 0.5f, 0f))
        assertFalse(TiltLogic.tilted(flat, Float.NaN, 9.8f, 0f))
        assertFalse(TiltLogic.tilted(floatArrayOf(0f, 0f, 0f), 4.5f, 8.7f, 0f))
    }
}
