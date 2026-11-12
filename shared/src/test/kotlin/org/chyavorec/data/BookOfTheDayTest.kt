package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.data.catalog.BookOfTheDay
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.EngineCatalogSearcher
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

    @Test fun emptyCatalogueGivesNothing() = runTest {
        assertNull(BookOfTheDay.pick(searcher(emptyList()), day))
    }
}
