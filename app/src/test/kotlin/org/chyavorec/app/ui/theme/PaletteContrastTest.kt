package org.chyavorec.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.junit.Test
import kotlin.test.assertTrue

/** Всяка палитра (светла и тъмна) държи текста на поне 4.5:1 спрямо фона си (WCAG AA). */
class PaletteContrastTest {
    private fun check(name: String, fg: Color, bg: Color, failures: MutableList<String>) {
        val c = ColorMath.contrast(fg.toArgb(), bg.toArgb())
        if (c < ColorMath.MIN_TEXT_CONTRAST) failures += "$name = ${"%.2f".format(c)}"
    }

    @Test fun everyPaletteIsReadable() {
        val failures = mutableListOf<String>()
        for (palette in ThemePalette.entries) {
            for (dark in listOf(false, true)) {
                val s = paletteScheme(palette, dark)
                val p = "$palette/${if (dark) "dark" else "light"}"
                check("$p onPrimary", s.onPrimary, s.primary, failures)
                check("$p onPrimaryContainer", s.onPrimaryContainer, s.primaryContainer, failures)
                check("$p onSecondary", s.onSecondary, s.secondary, failures)
                check("$p onSecondaryContainer", s.onSecondaryContainer, s.secondaryContainer, failures)
                check("$p onTertiary", s.onTertiary, s.tertiary, failures)
                check("$p onTertiaryContainer", s.onTertiaryContainer, s.tertiaryContainer, failures)
                check("$p onBackground", s.onBackground, s.background, failures)
                check("$p primary/background", s.primary, s.background, failures)
                check("$p secondary/background", s.secondary, s.background, failures)
                check("$p accent/background", paletteAccent(palette, dark), s.background, failures)
                check("$p onSurfaceVariant/surface", s.onSurfaceVariant, s.surface, failures)
                check("$p onSurfaceVariant/surfaceVariant", s.onSurfaceVariant, s.surfaceVariant, failures)
                for ((n, bg) in listOf(
                    "lowest" to s.surfaceContainerLowest, "low" to s.surfaceContainerLow, "container" to s.surfaceContainer,
                    "high" to s.surfaceContainerHigh, "highest" to s.surfaceContainerHighest,
                )) {
                    check("$p onSurface/$n", s.onSurface, bg, failures)
                    check("$p onSurfaceVariant/$n", s.onSurfaceVariant, bg, failures)
                }
            }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }
}
