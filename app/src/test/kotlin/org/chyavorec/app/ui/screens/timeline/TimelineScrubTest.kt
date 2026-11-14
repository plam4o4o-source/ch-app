package org.chyavorec.app.ui.screens.timeline

import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.timeline.TimelineEntry
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Плъзгачът „Десетилетие“ на лентата на времето: години, десетилетия и положения. */
class TimelineScrubTest {
    @Test fun yearsCarryForwardAndTodayIsThisYear() {
        val entries = listOf(
            TimelineEntry("Начало", "Без година"),
            TimelineEntry("1909", "a", year = 1909),
            TimelineEntry("1912–1918", "b", year = 1912, endYear = 1918),
            TimelineEntry("След войната", "c"),
            TimelineEntry("Днес", "d"),
        )
        assertContentEquals(intArrayOf(1900, 1909, 1912, 1912, 2026), TimelineScrub.years(entries, 2026))
    }

    @Test fun theBuiltInTimelineRunsFrom1900ToToday() {
        val years = TimelineScrub.years(QuizDefaults.timeline, 2026)
        assertEquals(QuizDefaults.timeline.size, years.size)
        assertTrue(years.first() >= TimelineScrub.START)
        assertTrue(years.toList().zipWithNext().all { (a, b) -> a <= b }, "годините не вървят назад: ${years.toList()}")
        assertTrue(years.size >= TimelineScrub.MIN_ENTRIES)
    }

    @Test fun decadesAndPositions() {
        assertEquals(1920, TimelineScrub.decade(1922))
        assertEquals(1900, TimelineScrub.decade(1900))
        assertEquals(2020, TimelineScrub.decade(2026))
        assertEquals(0, TimelineScrub.indexAt(0f, 20))
        assertEquals(19, TimelineScrub.indexAt(1f, 20))
        assertEquals(10, TimelineScrub.indexAt(0.5f, 21))
        assertEquals(19, TimelineScrub.indexAt(7f, 20))
        assertEquals(0, TimelineScrub.indexAt(0.5f, 0))
    }

    @Test fun fractionIsExactAtTheEndsAndRoundTripsInBetween() {
        assertEquals(0f, TimelineScrub.fraction(3, 10, 20, 100f, 800, canBack = false, canForward = true))
        assertEquals(1f, TimelineScrub.fraction(3, 10, 20, 100f, 800, canBack = true, canForward = false))
        // 20 реда по 100 px, екран 800 px → обхват 1200 px; 600 px превъртяни = 0.5.
        assertEquals(0.5f, TimelineScrub.fraction(6, 0, 20, 100f, 800, canBack = true, canForward = true), 1e-4f)
        assertEquals(6 to 0, TimelineScrub.target(0.5f, 20, 100f, 800))
        assertEquals(3 to 30, TimelineScrub.target(0.275f, 20, 100f, 800))
        assertEquals(0 to 0, TimelineScrub.target(0f, 20, 100f, 800))
        assertEquals(0 to 0, TimelineScrub.target(0.4f, 0, 100f, 800))
    }
}
