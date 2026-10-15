package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.HistoryItem
import java.time.LocalDate
import java.time.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReadingStatsTest {
    private val today = LocalDate.of(2026, 10, 15)

    private fun h(id: String, date: String?, title: String = "Книга $id", author: String = "", inv: Long? = null) =
        HistoryItem(id, inv, title, author, dateOut = date)

    @Test fun emptyHistoryGivesNothing() = assertNull(ReadingStats.compute(emptyList(), emptyMap(), today))

    @Test fun oneItem() {
        val s = ReadingStats.compute(listOf(h("1", "2026-10-02", author = "Вазов, Иван")), emptyMap(), today)!!
        assertEquals(12, s.months.size)
        assertEquals(YearMonth.of(2025, 11), s.months.first().month, "12 месеца с текущия")
        assertEquals(YearMonth.of(2026, 10), s.months.last().month)
        assertEquals(1, s.months.last().count)
        assertEquals(1, s.lastMonthsTotal)
        assertEquals(1, s.thisYear)
        assertEquals(1, s.longestStreak)
        assertEquals("Вазов, Иван", s.favouriteAuthor)
        assertEquals(listOf(ReadingStats.SectionCount(null, 1)), s.sections, "без каталожен запис — без раздел")
        assertEquals(YearMonth.of(2026, 10), s.peakMonth?.month)
    }

    @Test fun monthsAreBucketedInSofiaTime() {
        // 22:30 UTC на 31 март е вече 1 април в София (UTC+3 през лятото).
        assertEquals(YearMonth.of(2026, 4), ReadingStats.monthOf("2026-03-31T22:30:00Z"))
        assertEquals(YearMonth.of(2026, 3), ReadingStats.monthOf("2026-03-31T20:30:00Z"))
        assertEquals(YearMonth.of(2026, 1), ReadingStats.monthOf("2025-12-31T23:00:00+00:00"))
        assertEquals(YearMonth.of(2026, 3), ReadingStats.monthOf("2026-03-31"))
        assertEquals(YearMonth.of(2026, 3), ReadingStats.monthOf("2026-03-31T23:59:00"))
        assertNull(ReadingStats.monthOf(null))
        assertNull(ReadingStats.monthOf("скоро"))
    }

    @Test fun windowCountsAndThisYear() {
        val history = listOf(
            h("a", "2026-03-05"), h("b", "2026-03-20"), h("c", "2026-03-31T22:30:00Z"), // последната е април
            h("d", "2025-11-01"), h("e", "2025-10-31"), // извън прозореца (октомври 2025)
            h("f", null), // без дата — само в общия брой
        )
        val s = ReadingStats.compute(history, emptyMap(), today)!!
        val byMonth = s.months.associate { it.month to it.count }
        assertEquals(2, byMonth[YearMonth.of(2026, 3)])
        assertEquals(1, byMonth[YearMonth.of(2026, 4)])
        assertEquals(1, byMonth[YearMonth.of(2025, 11)])
        assertEquals(4, s.lastMonthsTotal)
        assertEquals(3, s.thisYear)
        assertEquals(6, s.total)
        assertEquals(YearMonth.of(2026, 3), s.peakMonth?.month)
    }

    @Test fun duplicatedLoanIsCountedOnce() {
        val s = ReadingStats.compute(listOf(h("1", "2026-10-01"), h("1", "2026-10-01")), emptyMap(), today)!!
        assertEquals(1, s.total)
    }

    @Test fun longestStreakAcrossYears() {
        val history = listOf("2025-11-03", "2025-12-10", "2026-01-02", "2026-01-25", "2026-03-01", "2026-04-01").mapIndexed { i, d -> h("$i", d) }
        assertEquals(3, ReadingStats.compute(history, emptyMap(), today)!!.longestStreak)
    }

    @Test fun sectionsComeFromCatalogBooks() {
        val books = mapOf(
            "1" to CatalogBook(1, "Вазов, Иван", "Под игото", udc = "886.7-31"),
            "2" to CatalogBook(2, "Вазов, Иван", "Чичовци", udc = "886.7-31"),
            "3" to CatalogBook(3, "Smith", "Atlas", udc = "912"),
        )
        val history = listOf(h("1", "2026-01-01"), h("2", "2026-02-01"), h("3", "2026-03-01"), h("4", "2026-04-01"))
        val s = ReadingStats.compute(history, books, today)!!
        assertEquals(
            listOf(ReadingStats.SectionCount(8, 2), ReadingStats.SectionCount(9, 1), ReadingStats.SectionCount(null, 1)),
            s.sections,
        )
        assertEquals("Вазов, Иван", s.favouriteAuthor, "авторът от каталога")
        assertEquals(2, s.favouriteAuthorCount)
    }

    @Test fun favouriteAuthorTieGoesToTheMoreRecent() {
        val history = listOf(
            h("1", "2026-01-01", author = "Елин Пелин"), h("2", "2026-02-01", author = "Елин Пелин"),
            h("3", "2026-03-01", author = "Йовков, Йордан"), h("4", "2026-05-01", author = "Йовков, Йордан"),
            h("5", "2026-06-01", author = "и др."),
        )
        assertEquals("Йовков, Йордан", ReadingStats.compute(history, emptyMap(), today)!!.favouriteAuthor)
    }

    @Test fun noAuthorAtAll() = assertNull(ReadingStats.compute(listOf(h("1", "2026-01-01")), emptyMap(), today)!!.favouriteAuthor)

    @Test fun resolvesByInvThenByTitle() = runTest {
        val catalog = listOf(
            CatalogBook(1, "Вазов, Иван", "Под игото", udc = "886.7-31", available = true),
            CatalogBook(2, "Елин Пелин", "Гераците", udc = "886.7-32", available = true),
            CatalogBook(3, "Smith, John", "Atlas", udc = "912", available = true),
        )
        val searcher = EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-09-19", catalog, emptyList())))
        val history = listOf(
            h("a", "2026-01-01", "Под игото", "Вазов, Иван", inv = 1),
            h("b", "2026-02-01", "Гераците", "Елин Пелин"), // без инв. № — по заглавие и автор
            h("c", "2026-03-01", "ATLAS", "Smith, John", inv = 999), // изписан екземпляр — по заглавие
            h("d", "2026-04-01", "Непозната", "Някой"),
        )
        val r = ReadingStats.resolveBooks(searcher, history)
        assertEquals(1L, r["a"]?.inv)
        assertEquals(2L, r["b"]?.inv)
        assertEquals(3L, r["c"]?.inv)
        assertTrue("d" !in r)
        val s = ReadingStats.compute(history, r, today)!!
        assertEquals(listOf(8 to 2, 9 to 1, null to 1), s.sections.map { it.section to it.count })
    }

    @Test fun titleLookupsAreCapped() = runTest {
        val catalog = (1..5L).map { CatalogBook(it, "Автор", "Заглавие $it", udc = "8") }
        val searcher = EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-09-19", catalog, emptyList())))
        val history = (1..5).map { h("$it", "2026-01-01", "Заглавие $it", "Автор") }
        assertEquals(2, ReadingStats.resolveBooks(searcher, history, maxTitleLookups = 2).size)
    }
}
