package org.chyavorec.app.ui.screens.my

import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.data.catalog.PersonalShevitsa
import org.chyavorec.data.catalog.ReadingYear
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Схемите на бродираните графики и преводът на „Твоята шевица“ към схема за бродиране — чисти функции. */
class ChartStitchesTest {
    @Test fun oneStitchPerBookInColumnsWithGaps() {
        val counts = listOf(0, 3, 1, 5)
        val c = ChartStitches.chart(counts)
        assertEquals(counts.sum(), c.size)
        assertEquals(2 * counts.size + 1, c.columns)
        // Колона 2i+1; най-ниският бод е на последния ред, няма бод в пролуките.
        for (i in 0 until c.size) assertTrue(c.col[i] % 2 == 1 && c.row[i] in 0 until c.rows)
        assertEquals(c.rows - 1, (0 until c.size).filter { c.col[it] == 3 }.maxOf { c.row[it] })
    }

    @Test fun peakMonthIsGoldAndTheRestBurgundy() {
        val counts = listOf(2, 5, 3, 5, 1)
        // При равенство — по-късният месец (както в годината).
        assertEquals(3, ChartStitches.peakIndex(counts))
        val c = ChartStitches.chart(counts)
        for (i in 0 until c.size) {
            val month = (c.col[i] - 1) / 2
            assertEquals(if (month == 3) StitchCharts.GOLD else StitchCharts.BURGUNDY, c.thread[i])
        }
        assertEquals(-1, ChartStitches.peakIndex(listOf(0, 0, 0)))
        assertEquals(-1, ChartStitches.peakIndex(emptyList()))
    }

    @Test fun cellShrinksOnlyAboveEightStitches() {
        // Най-високата колона + един ред за числото: до 8 книги — пълна клетка.
        assertEquals(1f, ChartStitches.cellScale(ChartStitches.rows(8)))
        assertTrue(ChartStitches.cellScale(ChartStitches.rows(9)) < 1f)
        assertEquals(1f, ChartStitches.cellScale(ChartStitches.rows(1)))
        // Висока колона: височината остава колкото 9 пълни реда.
        val rows = ChartStitches.rows(30)
        assertEquals(ChartStitches.FULL_ROWS.toFloat(), rows * ChartStitches.cellScale(rows), 0.001f)
    }

    @Test fun hugeAndEmptyCountsStayInBounds() {
        for (counts in listOf(List(12) { 0 }, List(12) { 1_000 }, listOf(-3, 4, 0), emptyList())) {
            val c = ChartStitches.chart(counts)
            for (i in 0 until c.size) assertTrue(c.col[i] in 0 until c.columns && c.row[i] in 0 until c.rows)
        }
        assertEquals(0, ChartStitches.chart(List(12) { 0 }).size)
    }

    @Test fun beltSegmentsAddUpAndEverySectionHasOne() {
        for (counts in listOf(listOf(14, 5, 3, 2, 3), listOf(1), listOf(100, 1, 1, 1, 1), listOf(7, 7, 7), listOf(1, 1, 1, 1, 1))) {
            val seg = ChartStitches.beltSegments(counts)
            assertEquals(BELT_SEGMENTS, seg.sum(), "$counts")
            assertTrue(seg.all { it >= 1 }, "$counts")
        }
        // Най-големият дял е и най-широк.
        val seg = ChartStitches.beltSegments(listOf(14, 5, 3))
        assertTrue(seg[0] > seg[1] && seg[1] > seg[2])
        assertEquals(0, ChartStitches.beltSegments(emptyList()).size)
        assertTrue(ChartStitches.beltSegments(listOf(0, 0)).all { it == 0 })
    }

    @Test fun personalBandTranslatesToAStitchChart() {
        val s = ReadingYear.Summary(
            2026, 12, listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), null, 0, 5, emptyList(), 12, ReadingYear.Archetype.STEADY,
        )
        val shared = PersonalShevitsa.chart(s)
        val c = bandChart(s)
        assertEquals(shared.size, c.size)
        assertEquals(PersonalShevitsa.COLUMNS, c.columns)
        assertEquals(PersonalShevitsa.ROWS, c.rows)
        assertTrue(c.angle.all { it in 0f..1f })
        assertEquals(10, SectionThreads.size)
        assertEquals(PersonalShevitsa.COLUMNS, bandStrip(s).columns)
        assertEquals(3, bandStrip(s).rows)
    }
}
