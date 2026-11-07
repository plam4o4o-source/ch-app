package org.chyavorec.data

import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.catalog.ReadingYear.Archetype
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import java.time.LocalDate
import java.time.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReadingYearTest {
    private fun h(id: String, date: String?, author: String = "", title: String = "Книга $id") =
        HistoryItem(id, null, title, author, dateOut = date)

    private fun book(udc: String, author: String = "") = CatalogBook(inv = 1, author = author, title = "", udc = udc)

    @Test fun otherYearsAreIgnored() {
        val history = listOf(h("1", "2025-12-31"), h("2", "2026-01-02"), h("3", "2027-01-01"))
        val s = ReadingYear.compute(history, emptyMap(), 2026)!!
        assertEquals(1, s.books)
        assertEquals(1, s.months[0])
        assertNull(ReadingYear.compute(history, emptyMap(), 2024))
    }

    @Test fun yearBoundaryIsSofiaTime() {
        // 23:30 UTC на 31 декември е вече 1 януари в София.
        val s = ReadingYear.compute(listOf(h("1", "2025-12-31T23:30:00Z")), emptyMap(), 2026)!!
        assertEquals(Month.JANUARY, s.peakMonth)
    }

    @Test fun monthsPeakAndStreak() {
        val dates = listOf("2026-02-03", "2026-03-01", "2026-03-20", "2026-04-11", "2026-07-02", "2026-07-09", "2026-07-30", "2026-08-01")
        val s = ReadingYear.compute(dates.mapIndexed { i, d -> h("$i", d) }, emptyMap(), 2026)!!
        assertEquals(listOf(0, 1, 2, 1, 0, 0, 3, 1, 0, 0, 0, 0), s.months)
        assertEquals(Month.JULY, s.peakMonth)
        assertEquals(3, s.peakCount)
        assertEquals(3, s.longestStreak, "февруари–април")
        assertEquals(1..3, s.streakMonths)
        assertEquals(5, s.activeMonths)
    }

    @Test fun peakTieGoesToTheLaterMonth() {
        val s = ReadingYear.compute(listOf(h("1", "2026-03-01"), h("2", "2026-09-01")), emptyMap(), 2026)!!
        assertEquals(Month.SEPTEMBER, s.peakMonth)
    }

    @Test fun favouriteAuthorNeedsTwoBooks() {
        val one = ReadingYear.compute(listOf(h("1", "2026-03-01", "Вазов, Иван"), h("2", "2026-04-01", "Талев, Димитър")), emptyMap(), 2026)!!
        assertNull(one.favouriteAuthor)
        assertEquals(2, one.authors)
        val two = ReadingYear.compute(
            listOf(h("1", "2026-03-01", "Вазов, Иван"), h("2", "2026-04-01", "Талев, Димитър"), h("3", "2026-05-01", "Вазов, Иван")),
            emptyMap(), 2026,
        )!!
        assertEquals("Вазов, Иван", two.favouriteAuthor)
        assertEquals(2, two.favouriteAuthorCount)
    }

    @Test fun sectionsFromTheCatalogOnly() {
        val history = listOf(h("1", "2026-03-01"), h("2", "2026-04-01"), h("3", "2026-05-01"), h("4", "2026-06-01"))
        val books = mapOf("1" to book("821.163.2"), "2" to book("821.111"), "3" to book("94(497.2)"))
        val s = ReadingYear.compute(history, books, 2026)!!
        assertEquals(listOf(ReadingStats.SectionCount(8, 2), ReadingStats.SectionCount(9, 1)), s.sections, "без „без раздел“")
        assertEquals(8, s.topSection?.section)
    }

    @Test fun archetypes() {
        assertEquals(Archetype.BOOKWORM, ReadingYear.archetype(books = 30, favouriteAuthorCount = 10, sections = 6, streak = 12))
        assertEquals(Archetype.LOYAL, ReadingYear.archetype(books = 10, favouriteAuthorCount = 3, sections = 5, streak = 8))
        assertEquals(Archetype.EXPLORER, ReadingYear.archetype(books = 12, favouriteAuthorCount = 3, sections = 4, streak = 8), "3 от 12 е под 30 %")
        assertEquals(Archetype.STEADY, ReadingYear.archetype(books = 8, favouriteAuthorCount = 0, sections = 2, streak = 6))
        assertEquals(Archetype.CURIOUS, ReadingYear.archetype(books = 2, favouriteAuthorCount = 0, sections = 0, streak = 1))
    }

    @Test fun archetypeFromData() {
        val history = (1..9).map { m -> h("$m", "2026-%02d-10".format(m), if (m % 2 == 0) "Вазов, Иван" else "Автор $m") }
        val s = ReadingYear.compute(history, emptyMap(), 2026)!!
        assertEquals(4, s.favouriteAuthorCount)
        assertEquals(Archetype.LOYAL, s.archetype)
        assertEquals(9, s.longestStreak)
    }

    @Test fun offeredYearsAndSeason() {
        assertEquals(listOf(2025, 2026), ReadingYear.offeredYears(LocalDate.of(2026, 1, 20)))
        assertEquals(listOf(2026), ReadingYear.offeredYears(LocalDate.of(2026, 2, 1)))
        assertEquals(listOf(2026), ReadingYear.offeredYears(LocalDate.of(2026, 12, 31)))
        assertTrue(ReadingYear.isSeason(LocalDate.of(2026, 12, 1)))
        assertTrue(ReadingYear.isSeason(LocalDate.of(2027, 1, 31)))
        assertTrue(!ReadingYear.isSeason(LocalDate.of(2026, 11, 30)))
    }
}
