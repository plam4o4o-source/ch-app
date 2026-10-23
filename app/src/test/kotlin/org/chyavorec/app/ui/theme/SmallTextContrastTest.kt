package org.chyavorec.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import org.chyavorec.app.ui.components.STATUS_PILL_ALPHA
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Дребният текст (WCAG AA — 4.5:1) върху всеки фон, на който се появява:
 * - златото/акцентът и основният цвят (етикети над секции, дати, „Следващ срок“, бутони-текст);
 * - статусите „Налична“ / „Заета“ / „Скоро срок“ / „Просрочена“ като текст и в етикета си
 *   (собствен фон — [STATUS_PILL_ALPHA] от цвета — над повърхността или контейнера).
 * Фоновете са всички повърхности и контейнери на палитрата (светла и тъмна).
 */
class SmallTextContrastTest {
    private fun ratio(fg: Color, bg: Color) = ColorMath.contrast(fg.toArgb(), bg.toArgb())

    private fun MutableList<String>.check(name: String, fg: Color, bg: Color) {
        val c = ratio(fg, bg)
        if (c < ColorMath.MIN_TEXT_CONTRAST) add("$name = ${"%.2f".format(c)}")
    }

    @Test fun smallAccentAndStatusTextIsReadableOnEverySurface() {
        val failures = mutableListOf<String>()
        for (palette in ThemePalette.entries) {
            for (dark in listOf(false, true)) {
                val s = paletteScheme(palette, dark)
                val p = "$palette/${if (dark) "dark" else "light"}"
                val surfaces = listOf(
                    "background" to s.background, "surface" to s.surface,
                    "lowest" to s.surfaceContainerLowest, "low" to s.surfaceContainerLow, "container" to s.surfaceContainer,
                    "high" to s.surfaceContainerHigh, "highest" to s.surfaceContainerHighest,
                )
                val status = if (dark) {
                    listOf("ok" to StatusColors.OkDark, "warn" to StatusColors.WarnDark, "bad" to StatusColors.BadDark)
                } else {
                    listOf("ok" to StatusColors.OkLight, "warn" to StatusColors.WarnLight, "bad" to StatusColors.BadLight)
                }
                for ((bgName, bg) in surfaces) {
                    failures.check("$p accent/$bgName", paletteAccent(palette, dark), bg)
                    failures.check("$p primary/$bgName", s.primary, bg)
                    failures.check("$p secondary/$bgName", s.secondary, bg)
                    for ((n, c) in status) {
                        failures.check("$p $n/$bgName", c, bg)
                        failures.check("$p $n pill/$bgName", c, c.copy(alpha = STATUS_PILL_ALPHA).compositeOver(bg))
                    }
                }
                // Датата в значката на събитие (плътна — бордо с пергамент в „Яворец“).
                val (badge, onBadge) = paletteDateBadge(palette, dark)
                failures.check("$p date badge", onBadge, badge)
                // Избраният филтър (FilterChip) — в златния контейнер, като индикатора в лентата.
                failures.check("$p selected chip", s.onPrimaryContainer, s.primaryContainer)
                // „Просрочено“ на червената карта в „Начало“ — етикетът е в цвета на текста ѝ.
                val err = s.errorContainer
                failures.check("$p overdue pill/errorContainer", s.onErrorContainer, s.onErrorContainer.copy(alpha = STATUS_PILL_ALPHA).compositeOver(err))
            }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    /**
     * Въвеждането е винаги на тъмен фон (мастило → бордо): бутонът е злато с мастило, а не
     * основният цвят на темата — в светла тема той беше #785A10 върху бордо (1.76:1).
     */
    @Test fun onboardingButtonsAreReadableOnTheDarkGradient() {
        val failures = mutableListOf<String>()
        failures.check("button text", Brand.Ink, Brand.Gold)
        for ((n, bg) in listOf("ink" to Brand.Ink, "burgundy" to Brand.Burgundy)) {
            failures.check("outlined text/$n", Brand.Parchment, bg)
            failures.check("skip/$n", Brand.GoldLight, bg)
            // Бутонът и рамката му се различават от фона (не-текстов контраст, 3:1).
            if (ratio(Brand.Gold, bg) < 3.0) failures += "gold/$n = ${"%.2f".format(ratio(Brand.Gold, bg))}"
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
        assertTrue(ratio(Brand.GoldText, Brand.Burgundy) < 3.0)
    }

    /** Числата от анализа (за отчета): старите стойности не минаваха, новите — да. */
    @Test fun oldGoldAndWarnFailedWhereTheNewOnesPass() {
        val highest = Color(0xFFE9E0D1)
        assertTrue(ratio(Brand.GoldDark, highest) < ColorMath.MIN_TEXT_CONTRAST)
        assertTrue(ratio(Brand.GoldText, highest) >= ColorMath.MIN_TEXT_CONTRAST)
        val oldWarn = Color(0xFF9A5B00)
        val cream = Brand.Cream
        assertTrue(ratio(oldWarn, oldWarn.copy(alpha = 0.14f).compositeOver(cream)) < ColorMath.MIN_TEXT_CONTRAST)
        assertTrue(ratio(StatusColors.WarnLight, StatusColors.WarnLight.copy(alpha = STATUS_PILL_ALPHA).compositeOver(cream)) >= ColorMath.MIN_TEXT_CONTRAST)
    }
}
