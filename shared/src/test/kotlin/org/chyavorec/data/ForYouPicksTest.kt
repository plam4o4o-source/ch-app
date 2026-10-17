package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.ForYouPicks
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForYouPicksTest {
    private fun b(inv: Long, title: String, udc: String, date: String = "", available: Boolean = true, author: String = "Автор $title") =
        CatalogBook(inv, author, title, udc = udc, available = available, registeredOn = date)

    private val books = listOf(
        // Купени в няколко екземпляра — „най-четени“.
        b(1, "Под игото", "886.7", "2019-01-01"), b(2, "Под игото", "886.7", "2019-01-01"), b(3, "Под игото", "886.7", "2020-01-01"),
        b(4, "Атлас", "912", "2018-01-01", available = false), b(5, "Атлас", "912", "2018-01-01"),
        b(6, "Скрита", "5", "2018-01-01", available = false), b(7, "Скрита", "5", "2018-01-01", available = false),
        // Нови постъпления.
        b(10, "Роман А", "886", "2026-09-01"), b(11, "Роман Б", "886", "2026-08-01"), b(12, "История", "94", "2026-07-01"),
        b(13, "Физика", "53", "2026-06-01"), b(14, "Неналична", "7", "2026-10-01", available = false),
    )

    @Test fun mixesPopularAndNewAvailableOnly() {
        val r = ForYouPicks.popularAndNew(books).map { it.inv }
        assertEquals(1L, r.first(), "най-много екземпляри — първа")
        assertEquals(10L, r[1], "след нея най-новата налична")
        assertTrue(5L in r, "наличният екземпляр на „Атлас“")
        assertFalse(4L in r || 6L in r || 7L in r || 14L in r, "само налични")
        assertEquals(r.size, ForYouPicks.popularAndNew(books).map { it.title }.distinct().size, "по едно от заглавие")
    }

    @Test fun skipsBooksAlreadyOnHome() {
        val r = ForYouPicks.popularAndNew(books, skip = setOf(10, 11)).map { it.inv }
        assertFalse(10L in r || 11L in r)
        assertTrue(12L in r)
    }

    @Test fun newBooksAreVariedBySection() {
        val fresh = listOf(
            b(1, "A1", "8", "2026-09-05"), b(2, "A2", "8", "2026-09-04"), b(3, "A3", "8", "2026-09-03"),
            b(4, "B1", "9", "2026-09-02"), b(5, "C1", "3", "2026-09-01"),
        )
        assertEquals(listOf(1L, 4L, 5L, 2L, 3L), ForYouPicks.popularAndNew(fresh).map { it.inv })
    }

    @Test fun limitAndEmpty() {
        assertEquals(2, ForYouPicks.popularAndNew(books, limit = 2).size)
        assertTrue(ForYouPicks.popularAndNew(emptyList()).isEmpty())
    }

    @Test fun personalPutsAvailableFirstAndFillsUp() {
        val recs = listOf(b(20, "Х", "8", available = false), b(21, "У", "8"))
        val fallback = listOf(b(21, "У", "8"), b(22, "Ф", "9"), b(23, "Прочетена", "9"))
        val r = ForYouPicks.personal(recs, fallback, exclude = setOf(23)).map { it.inv }
        assertEquals(listOf(21L, 20L, 22L), r)
    }

    @Test fun personalWithEnoughRecommendationsIsNotPadded() {
        val recs = (1..8L).map { b(it, "Р$it", "8") }
        val r = ForYouPicks.personal(recs, listOf(b(99, "Друга", "9")), exclude = emptySet())
        assertEquals(8, r.size)
        assertFalse(r.any { it.inv == 99L })
    }

    @Test fun generalFallsBackToShelvesForOldSummaries() {
        val shelf = listOf(b(1, "Р1", "8"), b(2, "Р2", "8", available = false), b(3, "Р3", "9"))
        assertEquals(listOf(1L, 3L), ForYouPicks.general(null, listOf(shelf), exclude = emptySet()).map { it.inv })
        assertEquals(listOf(3L), ForYouPicks.general(listOf(b(1, "Р1", "8"), b(3, "Р3", "9")), emptyList(), exclude = setOf(1)).map { it.inv })
    }

    @Test fun homeSummaryCarriesPicksWithoutNewestRow() {
        // Повече от 12 нови постъпления — редът „Нови книги“ не изчерпва каталога.
        val fresh = (1..15).map { b(100L + it, "Нова $it", "8", "2026-10-%02d".format(it)) }
        val s = CatalogSearchEngine.homeSummary(CatalogSnapshot("Б", "Яворец", "2026-09-19", books + fresh, emptyList()))
        val picks = s.picks!!
        assertTrue(picks.isNotEmpty())
        assertTrue(picks.none { p -> s.newest.any { it.inv == p.inv } })
    }

    @Test fun homeSummaryBooksCarryOnlyWhatTheShelvesShow() {
        val withText = books.map { it.copy(annotation = "Дълга анотация " + it.inv, keywords = "роман; " + it.inv) }
        val fresh = (1..15).map { b(100L + it, "Нова $it", "8", "2026-10-%02d".format(it)).copy(annotation = "Нова анотация") }
        val shelf = org.chyavorec.domain.model.CatalogShelf("Препоръчани", withText.take(2).map { it.inv })
        val s = CatalogSearchEngine.homeSummary(CatalogSnapshot("Б", "Яворец", "2026-09-19", withText + fresh, listOf(shelf)))
        val all = s.newest + s.shelves.flatMap { it.books } + s.picks!!
        assertTrue(s.shelves.single().books.size == 2 && all.size > 2)
        assertTrue(all.all { it.annotation.isEmpty() && it.keywords.isEmpty() })
        // Всичко останало (корица, заглавие, автор, наличност, дата) е непокътнато.
        val byInv = (withText + fresh).associateBy { it.inv }
        for (x in all) assertEquals(byInv.getValue(x.inv).copy(annotation = "", keywords = ""), x)
    }
}
