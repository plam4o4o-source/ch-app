package org.chyavorec.app.ui.components

import org.chyavorec.app.data.local.ReaderFontSize
import org.chyavorec.app.data.local.TextScale
import org.chyavorec.app.ui.MAX_FONT_SCALE
import org.chyavorec.app.ui.appFontScale
import org.junit.Test
import kotlin.test.assertEquals

/** Режимът за четене се умножава върху мащаба на приложението, но не прескача тавана от 2×. */
class ReaderFontScaleTest {
    @Test fun multipliesTheAppScale() {
        assertEquals(1.3f, readerFontScale(1f, ReaderFontSize.XL), 0.001f)
        assertEquals(1.15f * 1.2f, readerFontScale(1.2f, ReaderFontSize.L), 0.001f)
        assertEquals(1.5f, readerFontScale(1.5f, ReaderFontSize.M), 0.001f)
    }

    /** Системен 2× × „Размер на текста“ XL × четене XL — беше 2.6×, вече 2×. */
    @Test fun isCappedAtTwice() {
        val app = appFontScale(2f, TextScale.XLARGE.factor)
        assertEquals(MAX_FONT_SCALE, readerFontScale(app, ReaderFontSize.XL))
        assertEquals(MAX_FONT_SCALE, readerFontScale(1.8f, ReaderFontSize.L))
    }

    @Test fun smallerSizeStillShrinksAtTheMaximum() {
        assertEquals(1.8f, readerFontScale(MAX_FONT_SCALE, ReaderFontSize.S), 0.001f)
    }

    /** По-едър системен мащаб (над 2×) не се смалява, но и не расте повече. */
    @Test fun neverGoesBelowAnAlreadyLargerScale() {
        assertEquals(2.2f, readerFontScale(2.2f, ReaderFontSize.XL), 0.001f)
    }
}
