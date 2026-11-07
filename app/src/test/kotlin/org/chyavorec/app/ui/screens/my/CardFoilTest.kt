package org.chyavorec.app.ui.screens.my

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Наклон → фолио, отблясък и релеф (чиста функция). */
class CardFoilTest {
    private val eps = 1e-5f

    @Test fun restPosition() {
        val s = CardFoil.shine(0f, 0f)
        assertEquals(0.08f, s.hue, eps)
        assertEquals(0.5f, s.sheen, eps)
        // Отблясъкът е горе вдясно, светлината — отгоре вляво (светлият ръб на буквите е натам).
        assertEquals(0.72f, s.glintX, eps)
        assertEquals(0.22f, s.glintY, eps)
        assertTrue(s.embossX < 0f && s.embossY < 0f)
        assertEquals(0.22f, s.glintStrength, eps)
    }

    @Test fun glintAndSheenFollowTheTilt() {
        val left = CardFoil.shine(-0.6f, 0f)
        val right = CardFoil.shine(0.6f, 0f)
        assertTrue(right.glintX > left.glintX)
        assertTrue(right.sheen > left.sheen)
        val up = CardFoil.shine(0f, -0.6f)
        val down = CardFoil.shine(0f, 0.6f)
        assertTrue(down.glintY > up.glintY)
        // По-голям наклон — по-силен отблясък.
        assertTrue(CardFoil.shine(0.8f, 0.3f).glintStrength > CardFoil.shine(0.1f, 0f).glintStrength)
    }

    @Test fun embossShiftsWithTheLight() {
        val right = CardFoil.shine(1f, 0f)
        val left = CardFoil.shine(-1f, 0f)
        // Светлият ръб се мести с наклона, а в покой е горе вляво.
        assertTrue(right.embossX < left.embossX)
        assertTrue(CardFoil.shine(0f, 1f).embossY < CardFoil.shine(0f, -1f).embossY)
        assertTrue(kotlin.math.abs(right.embossX) <= 1.3f + eps)
    }

    @Test fun outOfRangeAndNanAreClamped() {
        assertEquals(CardFoil.shine(1f, -1f), CardFoil.shine(5f, -9f))
        assertEquals(CardFoil.shine(0f, 0f), CardFoil.shine(Float.NaN, Float.NaN))
    }

    @Test fun valuesStayInRange() {
        var x = -1f
        while (x <= 1f) {
            var y = -1f
            while (y <= 1f) {
                val s = CardFoil.shine(x, y)
                assertTrue("hue $x $y", s.hue >= 0f && s.hue < 1f)
                assertTrue(s.glintX in 0.05f..0.95f && s.glintY in 0.05f..0.95f)
                assertTrue(s.glintStrength in 0f..0.41f)
                assertTrue(s.sheen in -0.2f..1.2f)
                y += 0.1f
            }
            x += 0.1f
        }
    }
}
