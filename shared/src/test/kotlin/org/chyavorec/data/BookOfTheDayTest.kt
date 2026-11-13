package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.data.catalog.BookOfTheDay
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.RealRecords
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Книга на деня“: детерминирана по дата, равномерна и търпима към оскъдни записи. */
class BookOfTheDayTest {
    private fun book(inv: Long, title: String, author: String = "Автор $inv", available: Boolean = true) =
        CatalogBook(inv, author, title, available = available)

    private fun searcher(books: List<CatalogBook>) =
        EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Библиотека", "Яворец", "2026-09-26", books, emptyList())))

    private val day = LocalDate.parse("2026-10-17")

    @Test fun indexIsStableForADateAndInRange() {
        repeat(50) { n ->
            val d = day.plusDays(n.toLong())
            val i = BookOfTheDay.index(2446, d)
            assertEquals(i, BookOfTheDay.index(2446, d))
            assertTrue(i in 0 until 2446, "$i")
        }
        assertEquals(0, BookOfTheDay.index(1, day))
    }

    @Test fun differentDaysGiveDifferentPlaces() {
        val places = (0 until 60).map { BookOfTheDay.index(2446, day.plusDays(it.toLong())) }.toSet()
        // 60 дни, 2446 места: почти няма повторения (равномерно разбъркване, не „ден по ред“).
        assertTrue(places.size >= 55, "${places.size}")
    }

    @Test fun chooseSkipsSparseRecords() {
        val sparse = book(1, "Само заглавие", author = "")
        val full = book(2, "Пълен запис", author = "Иван Вазов")
        assertEquals(full, BookOfTheDay.choose(listOf(sparse, full)))
        assertEquals(sparse, BookOfTheDay.choose(listOf(sparse, book(3, " ", author = "Някой"))))
        assertNull(BookOfTheDay.choose(listOf(book(4, "", author = "Някой"))))
        assertNull(BookOfTheDay.choose(emptyList()))
    }

    @Test fun picksTheSameBookForTheSameDayRegardlessOfListOrder() = runTest {
        val books = (1L..200L).map { book(it, "Книга %03d".format(it)) }
        val a = BookOfTheDay.pick(searcher(books), day)
        val b = BookOfTheDay.pick(searcher(books.reversed()), day)
        assertNotNull(a)
        assertEquals(a, b)
        val other = BookOfTheDay.pick(searcher(books), day.plusDays(1))
        assertTrue(other != a)
    }

    @Test fun prefersAvailableBooksAndFallsBackToAll() = runTest {
        val shelf = listOf(book(1, "Заета", available = false), book(2, "Налична"), book(3, "Друга заета", available = false))
        repeat(10) { assertEquals(2L, BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))?.inv) }
        val none = listOf(book(1, "Заета", available = false), book(2, "Друга заета", available = false))
        assertNotNull(BookOfTheDay.pick(searcher(none), day))
    }

    @Test fun nearTheEndOfTheListStillFindsAFullRecord() = runTest {
        // Само първата книга е пълна: местата в края намират „обратно“ нея или оскъден запис, но не нищо.
        val books = listOf(book(1, "Пълна", author = "Автор")) + (2L..40L).map { book(it, "Оскъдна $it", author = "") }
        repeat(30) { assertNotNull(BookOfTheDay.pick(searcher(books), day.plusDays(it.toLong()))) }
    }

    /** Живият каталог: ~12% истински записи (6-цифрени номера) сред тестови с кратки номера. */
    private fun mixedShelf(realEvery: Int = 8, total: Int = 800) = (1..total).map { n ->
        if (n % realEvery == 0) book(100_000L + n, "Истинска %04d".format(n), author = "Автор Истински")
        else book(n.toLong(), "Тестова %04d".format(n), author = "Автор $n")
    }

    @Test fun chooseTakesARealRecordOverTestOnes() {
        val test = book(5, "Тестова книга", author = "Някой")
        val real = book(123_456, "Под игото", author = "Иван Вазов")
        assertEquals(real, BookOfTheDay.choose(listOf(test, real)))
        // Истински без автор — пак пред тестов с автор.
        val realSparse = book(123_457, "Без автор", author = "")
        assertEquals(realSparse, BookOfTheDay.choose(listOf(test, realSparse)))
        // „Истински“ номер, но тестово заглавие — не е истински.
        val junk = book(123_458, "Тест 1", author = "Автор")
        assertEquals(test, BookOfTheDay.choose(listOf(test, junk)))
        // Без истински — старото правило.
        assertEquals(test, BookOfTheDay.choose(listOf(book(6, "Само заглавие", author = ""), test)))
    }

    @Test fun pickPrefersRealRecordsEvenWhenTheDayLandsOnTestOnes() = runTest {
        val shelf = mixedShelf()
        repeat(60) {
            val picked = BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))
            assertNotNull(picked)
            assertTrue(RealRecords.isReal(picked), "${day.plusDays(it.toLong())}: ${picked.inv}")
        }
    }

    @Test fun pickIsStableForADayAndVariesAcrossDays() = runTest {
        val shelf = mixedShelf()
        val picks = (0 until 40).map { BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))!!.inv }
        assertEquals(picks, (0 until 40).map { BookOfTheDay.pick(searcher(shelf.reversed()), day.plusDays(it.toLong()))!!.inv })
        assertTrue(picks.toSet().size >= 8, "${picks.toSet().size}")
    }

    @Test fun pickFindsTheOnlyRealRecordWhereverTheDayLands() = runTest {
        // Единственият истински запис е първи по заглавие.
        val shelf = listOf(book(100_001, "А истинска", author = "Автор")) + (2L..300L).map { book(it, "Тестова %04d".format(it)) }
        repeat(30) { assertEquals(100_001L, BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))?.inv) }
    }

    @Test fun pickFallsBackToTestRecordsWhenThereAreNoRealOnes() = runTest {
        val shelf = (1L..300L).map { book(it, "Тестова %04d".format(it)) }
        repeat(10) { assertNotNull(BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))) }
    }

    @Test fun aRealUnavailableRecordBeatsAnAvailableTestOne() = runTest {
        val shelf = listOf(book(100_500, "Истинска заета", available = false), book(7, "Тестова налична"), book(8, "Друга тестова"))
        repeat(10) { assertEquals(100_500L, BookOfTheDay.pick(searcher(shelf), day.plusDays(it.toLong()))?.inv) }
    }

    @Test fun emptyCatalogueGivesNothing() = runTest {
        assertNull(BookOfTheDay.pick(searcher(emptyList()), day))
    }
}
