package org.chyavorec.app.ui

import org.chyavorec.app.data.local.TextScale
import org.junit.Test
import kotlin.test.assertEquals

/** „Размер на текста“ × системния мащаб — не над 2× (там свършват нелинейните таблици на Android). */
class AppFontScaleTest {
    @Test fun multipliesTheSystemScale() {
        assertEquals(1.3f, appFontScale(1f, TextScale.XLARGE.factor), 0.001f)
        assertEquals(0.9f, appFontScale(1f, TextScale.SMALL.factor), 0.001f)
        assertEquals(1.15f * 1.5f, appFontScale(1.5f, TextScale.LARGE.factor), 0.001f)
    }

    @Test fun isCappedAtTwice() {
        assertEquals(MAX_FONT_SCALE, appFontScale(2f, TextScale.XLARGE.factor))
        assertEquals(MAX_FONT_SCALE, appFontScale(1.8f, TextScale.LARGE.factor))
        assertEquals(MAX_FONT_SCALE, appFontScale(1.7f, TextScale.XLARGE.factor))
    }

    @Test fun smallerChoiceStillShrinksAtTheMaximum() {
        assertEquals(1.8f, appFontScale(2f, TextScale.SMALL.factor), 0.001f)
    }

    /** По-едър системен мащаб (над 2×, напр. от производителя) не се смалява. */
    @Test fun neverGoesBelowAnAlreadyLargerSystemScale() {
        assertEquals(2.2f, appFontScale(2.2f, TextScale.XLARGE.factor), 0.001f)
        assertEquals(2.2f, appFontScale(2.2f, TextScale.STANDARD.factor), 0.001f)
    }
}
