package org.chyavorec.app.ui.theme

import org.junit.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Цветове от корицата → схема на екрана с детайлите: избор на семе и контраст на текста. */
class CoverPaletteTest {
    // Ролите на „Яворец“ (светла и тъмна) — както в Theme.kt.
    private val light = SchemeBase(
        dark = false,
        primary = ColorMath.of(0xFF8B6914),
        background = ColorMath.of(0xFFFAF7F2),
        onBackground = ColorMath.of(0xFF1A1208),
        surface = ColorMath.of(0xFFFAF7F2),
        onSurface = ColorMath.of(0xFF1A1208),
        surfaceVariant = ColorMath.of(0xFFF5F0E8),
        onSurfaceVariant = ColorMath.of(0xFF5A4D3D),
        surfaceContainerLowest = ColorMath.of(0xFFFFFFFF),
        surfaceContainerLow = ColorMath.of(0xFFFCF9F4),
        surfaceContainer = ColorMath.of(0xFFF5F0E8),
        surfaceContainerHigh = ColorMath.of(0xFFEFE8DC),
        surfaceContainerHighest = ColorMath.of(0xFFE9E0D1),
    )
    private val dark = SchemeBase(
        dark = true,
        primary = ColorMath.of(0xFFC9A84C),
        background = ColorMath.of(0xFF1A1208),
        onBackground = ColorMath.of(0xFFF5F0E8),
        surface = ColorMath.of(0xFF1A1208),
        onSurface = ColorMath.of(0xFFF5F0E8),
        surfaceVariant = ColorMath.of(0xFF33261A),
        onSurfaceVariant = ColorMath.of(0xFFD7CBB6),
        surfaceContainerLowest = ColorMath.of(0xFF120C05),
        surfaceContainerLow = ColorMath.of(0xFF261B10),
        surfaceContainer = ColorMath.of(0xFF2E2216),
        surfaceContainerHigh = ColorMath.of(0xFF392B1D),
        surfaceContainerHighest = ColorMath.of(0xFF443424),
    )
    private val oled = dark.copy(
        background = ColorMath.BLACK, surface = ColorMath.BLACK, surfaceContainerLowest = ColorMath.BLACK, tintSurfaces = false,
    )

    @Test fun seedPrefersVibrantThenDominant() {
        val vibrant = ColorMath.of(0xFFD03A2B)
        val dominant = ColorMath.of(0xFF2B5FD0)
        assertEquals(vibrant, CoverPalette.chooseSeed(vibrant, dominant))
        assertEquals(dominant, CoverPalette.chooseSeed(null, dominant))
        // Сив „жив“ цвят се прескача.
        assertEquals(dominant, CoverPalette.chooseSeed(ColorMath.of(0xFF808080), dominant))
    }

    @Test fun greyOrNearBlackCoverHasNoSeed() {
        assertNull(CoverPalette.chooseSeed(ColorMath.of(0xFF7A7A7A), ColorMath.of(0xFF101010), ColorMath.of(0xFFF8F8F8)))
        assertNull(CoverPalette.chooseSeed(null, null, null))
        // Почти черно, макар и „наситено“ по HSL.
        assertNull(CoverPalette.chooseSeed(ColorMath.of(0xFF140A0A), null))
    }

    @Test fun derivedSchemesAlwaysKeepTextContrast() {
        var derived = 0
        var total = 0
        for (base in listOf(light, dark, oled)) {
            var h = 0f
            while (h < 360f) {
                for (s in floatArrayOf(0.25f, 0.5f, 0.8f, 1f)) {
                    for (l in floatArrayOf(0.2f, 0.45f, 0.7f)) {
                        total++
                        val tones = CoverPalette.derive(ColorMath.fromHsl(h, s, l), base) ?: continue
                        derived++
                        assertTrue(CoverPalette.passesContrast(tones, base), "h=$h s=$s l=$l dark=${base.dark}")
                        assertTrue(ColorMath.contrast(tones.onPrimary, tones.primary) >= 4.5)
                        assertTrue(ColorMath.contrast(tones.primary, tones.background) >= 4.5)
                        assertTrue(ColorMath.contrast(base.onSurface, tones.surface) >= 4.5)
                    }
                }
                h += 15f
            }
        }
        // Почти всички цветове дават схема (откази — само при невъзможен контраст).
        assertTrue(derived >= total * 0.9, "derived $derived / $total")
    }

    @Test fun derivedPrimaryKeepsTheCoverHue() {
        // Синя корица → синкав (леко затоплен към златото) основен цвят.
        val tones = assertNotNull(CoverPalette.derive(ColorMath.of(0xFF2B5FD0), light))
        val hue = ColorMath.toHsl(tones.primary)[0]
        val seedHue = ColorMath.toHsl(ColorMath.of(0xFF2B5FD0))[0]
        assertTrue(abs(ColorMath.hueDelta(seedHue, hue)) <= 16f, "hue $hue vs seed $seedHue")
        assertTrue(abs(ColorMath.hueDelta(seedHue, hue)) > 0f, "harmonized toward gold")
    }

    @Test fun oledBackgroundStaysBlack() {
        val tones = assertNotNull(CoverPalette.derive(ColorMath.of(0xFFD03A2B), oled))
        assertEquals(ColorMath.BLACK, tones.background)
        assertEquals(ColorMath.BLACK, tones.surface)
    }

    @Test fun failingContrastIsRejected() {
        // Основа, при която текстът по фона сам по себе си не стига 4.5:1 → няма схема.
        val broken = light.copy(onSurfaceVariant = ColorMath.of(0xFFC0C0C0))
        assertNull(CoverPalette.derive(ColorMath.of(0xFF2B5FD0), broken))
    }
}
