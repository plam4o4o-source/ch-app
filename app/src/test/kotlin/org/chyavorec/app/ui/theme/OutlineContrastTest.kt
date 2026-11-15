package org.chyavorec.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import org.chyavorec.app.ui.components.FolkPlateBitmaps
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Не-текстов контраст (WCAG 1.4.11 — 3:1) на очертанията:
 * - `outline` — рамката на полетата (вход), на отговорите в „Познай Яворец“ и палецът на
 *   изключения превключвател — спрямо фона и спрямо пистата му (surfaceContainerHighest);
 * - ръбът на плочката ([FolkPlateBitmaps.RIM_ALPHA] от цвета на иконата) в тъмна тема —
 *   иначе тъмнозлатната плочка върху мастило (1.68:1) не показва формата си (осморъка/ромб).
 */
class OutlineContrastTest {
    private fun ratio(fg: Color, bg: Color) = ColorMath.contrast(fg.toArgb(), bg.toArgb())

    @Test fun outlinesAndPlateRimsStandOut() {
        val failures = mutableListOf<String>()
        fun check(name: String, fg: Color, bg: Color) {
            val c = ratio(fg, bg)
            if (c < 3.0) failures += "$name = ${"%.2f".format(c)}"
        }
        for (palette in ThemePalette.entries) {
            for (dark in listOf(false, true)) {
                val s = paletteScheme(palette, dark)
                val p = "$palette/${if (dark) "dark" else "light"}"
                check("$p outline/background", s.outline, s.background)
                check("$p outline/low", s.outline, s.surfaceContainerLow)
                check("$p switch thumb/track", s.outline, s.surfaceContainerHighest)
                if (dark) {
                    val rim = s.onPrimaryContainer.copy(alpha = FolkPlateBitmaps.RIM_ALPHA).compositeOver(s.background)
                    check("$p plate rim/background", rim, s.background)
                }
            }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    /** Числата от анализа: старото очертание на „Яворец“ беше 2.17:1 върху фона и 1.77:1 върху пистата. */
    @Test fun oldYavoretsOutlineFailed() {
        val old = Color(0xFFB8A988)
        assertTrue(ratio(old, Brand.Cream) < 3.0)
        assertTrue(ratio(old, Color(0xFFE9E0D1)) < 3.0)
        assertTrue(ratio(paletteScheme(ThemePalette.YAVORETS, false).outline, Brand.Cream) >= 4.0)
    }
}
