package org.chyavorec.app.ui.theme

import org.junit.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Цветовата математика (контраст по WCAG, HSL, хармонизиране) — чисти функции, без Android. */
class ColorMathTest {
    @Test fun contrastExtremes() {
        assertEquals(21.0, ColorMath.contrast(ColorMath.WHITE, ColorMath.BLACK), 0.01)
        assertEquals(1.0, ColorMath.contrast(ColorMath.of(0xFF8B6914), ColorMath.of(0xFF8B6914)), 1e-9)
        // Симетрично.
        assertEquals(
            ColorMath.contrast(ColorMath.of(0xFF1E5B3A), ColorMath.WHITE),
            ColorMath.contrast(ColorMath.WHITE, ColorMath.of(0xFF1E5B3A)),
            1e-12,
        )
    }

    @Test fun contrastKnownValues() {
        // #767676 върху бяло — класическата граница на WCAG AA (4.54:1).
        assertEquals(4.54, ColorMath.contrast(ColorMath.of(0xFF767676), ColorMath.WHITE), 0.01)
        assertTrue(ColorMath.readable(ColorMath.of(0xFF767676), ColorMath.WHITE))
        // #777777 вече не стига.
        assertTrue(!ColorMath.readable(ColorMath.of(0xFF777777), ColorMath.WHITE))
    }

    @Test fun hslRoundTrip() {
        val samples = longArrayOf(0xFF8B6914, 0xFF6B1F2A, 0xFF1E5B3A, 0xFF9C1C24, 0xFFC3CCD4, 0xFF000000, 0xFFFFFFFF, 0xFF336699, 0xFFE8D5A3)
        for (argb in samples) {
            val c = ColorMath.of(argb)
            val hsl = ColorMath.toHsl(c)
            val back = ColorMath.fromHsl(hsl[0], hsl[1], hsl[2])
            assertTrue(abs(ColorMath.red(back) - ColorMath.red(c)) <= 1, "R ${argb.toString(16)}")
            assertTrue(abs(ColorMath.green(back) - ColorMath.green(c)) <= 1, "G ${argb.toString(16)}")
            assertTrue(abs(ColorMath.blue(back) - ColorMath.blue(c)) <= 1, "B ${argb.toString(16)}")
        }
    }

    @Test fun primaryHues() {
        assertEquals(0f, ColorMath.toHsl(ColorMath.of(0xFFFF0000))[0], 0.01f)
        assertEquals(120f, ColorMath.toHsl(ColorMath.of(0xFF00FF00))[0], 0.01f)
        assertEquals(240f, ColorMath.toHsl(ColorMath.of(0xFF0000FF))[0], 0.01f)
    }

    @Test fun harmonizeMovesHalfwayButAtMost15Degrees() {
        assertEquals(15f, ColorMath.harmonizeHue(10f, 20f), 0.01f)
        assertEquals(15f, ColorMath.harmonizeHue(0f, 90f), 0.01f)
        // През 0°: 350 → 20 е +30°, наполовина = +15 → 5°.
        assertEquals(5f, ColorMath.harmonizeHue(350f, 20f), 0.01f)
        assertEquals(345f, ColorMath.harmonizeHue(0f, 200f), 0.01f)
    }

    @Test fun mixEnds() {
        val a = ColorMath.of(0xFF102030)
        val b = ColorMath.of(0xFFF0E0D0)
        assertEquals(a, ColorMath.mix(a, b, 0f))
        assertEquals(b, ColorMath.mix(a, b, 1f))
    }
}
