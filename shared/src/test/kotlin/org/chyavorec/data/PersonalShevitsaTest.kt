package org.chyavorec.data

import org.chyavorec.data.catalog.PersonalShevitsa
import org.chyavorec.data.catalog.PersonalShevitsa.COLUMNS
import org.chyavorec.data.catalog.PersonalShevitsa.ROWS
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.catalog.ReadingYear.Archetype
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersonalShevitsaTest {
    private fun summary(
        months: List<Int>,
        sections: List<ReadingStats.SectionCount> = emptyList(),
        archetype: Archetype = Archetype.CURIOUS,
        author: String? = null,
        year: Int = 2026,
    ) = ReadingYear.Summary(
        year = year, books = months.sum(), months = months,
        favouriteAuthor = author, favouriteAuthorCount = if (author != null) 2 else 0,
        authors = 3, sections = sections, longestStreak = 2, archetype = archetype,
    )

    private fun cellMap(c: PersonalShevitsa.Chart): Map<Int, Byte> =
        c.x.indices.associate { (c.y[it] * c.columns + c.x[it]) to c.thread[it] }

    private val sample = summary(
        listOf(0, 1, 2, 1, 0, 0, 3, 1, 0, 0, 5, 2),
        listOf(ReadingStats.SectionCount(8, 9), ReadingStats.SectionCount(9, 4), ReadingStats.SectionCount(3, 2)),
    )

    @Test fun bandIsMirroredLeftToRight() {
        for (a in Archetype.entries) {
            val c = PersonalShevitsa.chart(sample.copy(archetype = a))
            val m = cellMap(c)
            for ((k, t) in m) {
                val x = k % COLUMNS
                val y = k / COLUMNS
                assertEquals(t, m[y * COLUMNS + (COLUMNS - 1 - x)], "$a ($x,$y)")
            }
        }
    }

    @Test fun chartIsDeterministic() {
        val a = PersonalShevitsa.chart(sample)
        val b = PersonalShevitsa.chart(sample.copy())
        assertContentEquals(a.x, b.x)
        assertContentEquals(a.y, b.y)
        assertContentEquals(a.thread, b.thread)
    }

    @Test fun differentReadersGetDifferentBands() {
        val a = PersonalShevitsa.chart(sample)
        val others = listOf(
            sample.copy(months = sample.months.reversed()),
            sample.copy(months = sample.months.toMutableList().also { it[0] = 4 }),
            sample.copy(sections = listOf(ReadingStats.SectionCount(5, 15))),
            sample.copy(archetype = Archetype.LOYAL),
            sample.copy(favouriteAuthor = "Вазов, Иван"),
            sample.copy(year = 2027),
        )
        for (o in others) {
            val b = PersonalShevitsa.chart(o)
            val same = a.size == b.size && a.x.contentEquals(b.x) && a.y.contentEquals(b.y) && a.thread.contentEquals(b.thread)
            assertTrue(!same, "лентата трябва да се различава: $o")
        }
    }

    @Test fun cellsStayInsideTheGridAndAreUnique() {
        val summaries = listOf(
            sample,
            summary(List(12) { 0 }),
            summary(List(12) { 1 }),
            summary(List(12) { 13 }),
            summary(List(12) { 1_000_000 }),
            summary(List(12) { if (it == 4) Int.MAX_VALUE / 12 else 1 }),
            summary(listOf(-5, 0, 3)),
        )
        for (s in summaries) for (c in listOf(PersonalShevitsa.chart(s), PersonalShevitsa.strip(s))) {
            assertTrue(c.size > 0)
            for (i in 0 until c.size) {
                assertTrue(c.x[i] in 0 until c.columns && c.y[i] in 0 until c.rows, "($i) ${c.x[i]},${c.y[i]}")
            }
            assertEquals(c.size, cellMap(c).size, "клетките са различни")
        }
    }

    @Test fun emptyYearGetsALovelyDefaultBand() {
        val empty = PersonalShevitsa.chart(summary(List(12) { 0 }))
        val sample = PersonalShevitsa.chart(sample)
        assertTrue(empty.size > 100, "не е празна: ${empty.size}")
        // Стабилна: същото при всяко извикване.
        assertContentEquals(empty.thread, PersonalShevitsa.chart(summary(List(12) { 0 })).thread)
        assertTrue(empty.size >= sample.size / 2)
        assertTrue(PersonalShevitsa.strip(summary(List(12) { 0 })).size > 30)
        // Дори с липсващи месеци (повредени данни).
        assertTrue(PersonalShevitsa.chart(summary(emptyList())).size > 100)
    }

    @Test fun oneBookStillMakesABand() {
        val c = PersonalShevitsa.chart(summary(List(12) { if (it == 6) 1 else 0 }))
        val m = cellMap(c)
        // Колоната на юли: само книгата на оста; другите месеци — бордо зрънце.
        val julyX = 1 + 6
        val col = m.keys.filter { it % COLUMNS == julyX }.map { it / COLUMNS }
        assertEquals(listOf(ROWS / 2), col.filter { it in 1 until ROWS - 1 })
        for (x in 1..12) assertEquals(1, m.keys.count { it % COLUMNS == x && it / COLUMNS in 1 until ROWS - 1 })
        assertTrue(c.size > 100)
    }

    @Test fun twelveFullMonthsFillEveryColumnToTheirCount() {
        val months = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val c = PersonalShevitsa.chart(summary(months, listOf(ReadingStats.SectionCount(8, 60), ReadingStats.SectionCount(5, 18))))
        val m = cellMap(c)
        months.forEachIndexed { i, n ->
            val rows = m.keys.filter { it % COLUMNS == 1 + i }.map { it / COLUMNS }.filter { it in 1 until ROWS - 1 }
            // Четно число — книгите плюс бордо възел на оста; нечетно — само книгите.
            assertEquals(if (n % 2 == 0) n + 1 else n, rows.size, "месец ${i + 1}")
            // Колоната е симетрична около оста.
            assertEquals(rows.map { ROWS / 2 - it }.sorted(), rows.map { it - ROWS / 2 }.sorted())
        }
    }

    @Test fun hugeCountsAreScaledNotClipped() {
        val s = summary(List(12) { (it + 1) * 1000 })
        val scaled = PersonalShevitsa.scaledMonths(s)
        assertEquals(PersonalShevitsa.MAX_COLUMN, scaled.max())
        assertTrue(scaled.all { it in 1..PersonalShevitsa.MAX_COLUMN })
        // Редът се пази (по-малко → не повече).
        for (i in 1 until 12) assertTrue(scaled[i] >= scaled[i - 1])
        assertTrue(PersonalShevitsa.chart(s).size <= COLUMNS * ROWS)
    }

    @Test fun sectionsColourTheThreadAndUnknownBooksAreGold() {
        val withSections = PersonalShevitsa.chart(
            summary(List(12) { 2 }, listOf(ReadingStats.SectionCount(8, 12), ReadingStats.SectionCount(9, 12))),
        )
        val used = withSections.thread.map { it.toInt() }.toSet()
        assertTrue((PersonalShevitsa.THREAD_SECTION + 8) in used && (PersonalShevitsa.THREAD_SECTION + 9) in used)
        val none = PersonalShevitsa.chart(summary(List(12) { 2 }))
        assertTrue(none.thread.all { it == PersonalShevitsa.GOLD || it == PersonalShevitsa.BURGUNDY })
    }

    @Test fun stitchingStartsAtTheCentreAndGrowsOutwards() {
        val c = PersonalShevitsa.chart(sample)
        val d = IntArray(c.size) { kotlin.math.abs(c.x[it] - PersonalShevitsa.CENTRE_X) }
        for (i in 1 until d.size) assertTrue(d[i] >= d[i - 1], "иглата не се връща към средата ($i)")
    }

    @Test fun threadSequenceSpreadsSectionsEvenly() {
        val seq = PersonalShevitsa.threadSequence(
            20, 20, listOf(ReadingStats.SectionCount(8, 10), ReadingStats.SectionCount(9, 10)), seed = 0,
        )
        assertEquals(10, seq.count { it == (PersonalShevitsa.THREAD_SECTION + 8).toByte() })
        // Не са две големи групи: най-дългата редица от един цвят е кратка.
        var run = 1
        var best = 1
        for (i in 1 until seq.size) { run = if (seq[i] == seq[i - 1]) run + 1 else 1; best = maxOf(best, run) }
        assertTrue(best <= 2, "най-дълга редица $best")
    }
}
