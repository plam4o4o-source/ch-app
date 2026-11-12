package org.chyavorec.app.ui.components

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Чистите правила на подгъва и на падащата отметка. */
class HemAndBookmarkLogicTest {
    @Test fun hemUnpicksWithProgress() {
        assertEquals(1f, HemSpec.stitched(0f))
        assertTrue(HemSpec.stitched(0.5f) < HemSpec.stitched(0.2f))
        assertTrue(HemSpec.stitched(1f) in 0.05f..0.1f, "остава само малко бод")
        // Няма „щракване“ в първия кадър и не е по-плътен от 1.
        assertEquals(0f, HemSpec.alpha(0f))
        assertTrue(HemSpec.alpha(0.05f) < HemSpec.alpha(0.12f))
        assertTrue(HemSpec.alpha(1f) <= 1f)
    }

    @Test fun bookmarkDropsFromAboveAndOvershoots() {
        assertTrue(BookmarkDrop.offset(0f) < 0f, "започва нагоре")
        assertEquals(0f, BookmarkDrop.offset(1f))
        assertTrue(BookmarkDrop.offset(BookmarkDrop.OVERSHOOT) > 0f, "превишава под мястото си")
        assertTrue(BookmarkDrop.stretch(0f) < 1f && BookmarkDrop.stretch(1f) == 1f)
        assertTrue(BookmarkDrop.OVERSHOOT_AT_MS in 120..180)
    }
}
