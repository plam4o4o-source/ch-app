package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.SurprisePicker
import org.chyavorec.data.catalog.SurpriseTastes
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.HistoryItem
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Изненадай ме“: само налични, претеглено към вкуса, без повторение на скоро предложените. */
class SurprisePickerTest {
    private fun book(inv: Long, title: String, author: String = "Автор $inv", udc: String = "", available: Boolean = true, cover: String = "") =
        CatalogBook(inv, author, title, udc = udc, available = available, coverUrl = cover)

    private val shelf = listOf(
        book(1, "Под игото", "Вазов, Иван", udc = "886.7"),
        book(2, "Чичовци", "Вазов, Иван", udc = "886.7", available = false),
        book(3, "Тютюн", "Димов, Димитър", udc = "886.7"),
        book(4, "Атлас на Европа", "Smith, John", udc = "912"),
        book(5, "Физика за всеки", "Петров, Петър", udc = "53"),
        book(6, "Готварска книга", "Иванова, Мария", udc = "641"),
    )

    /** Колко пъти е избрана всяка книга при [n] избора с фиксирано семе. */
    private fun counts(pool: List<CatalogBook>, tastes: SurpriseTastes, n: Int = 4000): Map<Long, Int> {
        val random = Random(42)
        return (1..n).mapNotNull { SurprisePicker.pick(pool, tastes, emptyList(), random)?.inv }.groupingBy { it }.eachCount()
    }

    @Test fun neverPicksABookThatIsNotOnTheShelf() {
        val c = counts(shelf, SurpriseTastes())
        assertNull(c[2L])
        assertEquals(setOf(1L, 3L, 4L, 5L, 6L), c.keys)
        assertNull(SurprisePicker.pick(listOf(book(9, "Заета", available = false)), SurpriseTastes(), emptyList(), Random(1)))
    }

    @Test fun favouriteAuthorIsPickedMoreOften() {
        val tastes = SurpriseTastes(authors = listOf("Вазов, Иван"))
        val c = counts(shelf, tastes)
        // ×4 спрямо всяка друга (5 налични: 4/8 ≈ 50 %).
        assertTrue(c.getValue(1L) > 3 * c.getValue(4L), "$c")
        assertEquals(SurprisePicker.AUTHOR_BOOST, SurprisePicker.weight(shelf[0], tastes))
        assertEquals(0.0, SurprisePicker.weight(shelf[1], tastes))
    }

    @Test fun sectionsAndSearchesWeigh() {
        val tastes = SurpriseTastes(sections = mapOf(9 to 1), searches = listOf("физика"))
        // Целият вкус е в раздел 9 — ×3; търсенето — ×2.5.
        assertEquals(3.0, SurprisePicker.weight(shelf[3], tastes))
        assertEquals(SurprisePicker.SEARCH_BOOST, SurprisePicker.weight(shelf[4], tastes))
        assertEquals(1.0, SurprisePicker.weight(shelf[5], tastes))
    }

    @Test fun withoutTastesCoversArePreferred() {
        val pool = listOf(book(1, "С корица", cover = "https://example.org/c.jpg"), book(2, "Без корица"))
        val c = counts(pool, SurpriseTastes())
        assertTrue(c.getValue(1L) > c.getValue(2L) * 1.3, "$c")
    }

    @Test fun recentSuggestionsAreNotRepeated() {
        val random = Random(7)
        var recent = emptyList<String>()
        val picked = (1..5).map {
            val b = assertNotNull(SurprisePicker.pick(shelf, SurpriseTastes(), recent, random))
            recent = SurprisePicker.remember(recent, b)
            b.inv
        }
        // Петте налични — всяка точно веднъж.
        assertEquals(setOf(1L, 3L, 4L, 5L, 6L), picked.toSet())
        // Всичко е предлагано — пак се предлага, но не последната.
        repeat(20) { assertNotEquals(picked.last(), SurprisePicker.pick(shelf, SurpriseTastes(), recent, random)?.inv) }
    }

    @Test fun rememberKeepsTheLastFew() {
        var recent = emptyList<String>()
        (1..20L).forEach { recent = SurprisePicker.remember(recent, book(it, "Книга $it")) }
        assertEquals(SurprisePicker.RECENT, recent.size)
        assertEquals(SurprisePicker.key(book(20, "Книга 20")), recent.last())
        // Повторно предложена — само се мести накрая.
        recent = SurprisePicker.remember(recent, book(15, "Книга 15"))
        assertEquals(SurprisePicker.RECENT, recent.size)
        assertEquals(SurprisePicker.key(book(15, "Книга 15")), recent.last())
    }

    @Test fun readAndWantedAreExcluded() {
        val tastes = SurpriseTastes.of(
            wanted = listOf(shelf[2]),
            readBooks = listOf(shelf[0]),
            history = listOf(HistoryItem("L1", 1, "Под игото", "Вазов, Иван")),
            searches = listOf("  физика ", "ab"),
        )
        assertEquals(listOf("Димов, Димитър", "Вазов, Иван"), tastes.authors)
        assertEquals(mapOf(8 to 2), tastes.sections)
        assertEquals(listOf("физика"), tastes.searches)
        val c = counts(shelf, tastes)
        assertNull(c[1L])
        assertNull(c[3L])
        // Друг екземпляр на прочетеното заглавие — също не.
        assertNull(SurprisePicker.pick(listOf(book(77, "Под игото", "Вазов, Иван")), tastes, emptyList(), Random(3)))
    }

    @Test fun oneCopyPerTitle() {
        val pool = listOf(book(1, "Тютюн", "Димов"), book(2, "Тютюн", "Димов"), book(3, "Друга"))
        val c = counts(pool, SurpriseTastes())
        // Първият екземпляр от заглавието представя всички.
        assertNull(c[2L])
        assertEquals(setOf(1L, 3L), c.keys)
    }

    @Test fun poolHasOnlyAvailableBooksAndTheFavourites() = runTest {
        val books = (1..200L).map { book(it, "Книга $it", udc = (it % 9).toString(), available = it % 3 != 0L) } +
            book(500, "Под игото", "Вазов, Иван", udc = "886.7") + book(501, "Чичовци", "Вазов, Иван", available = false)
        val searcher = EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-10-01", books, emptyList())))
        val pool = SurprisePicker.pool(searcher, SurpriseTastes(authors = listOf("Вазов, Иван"), sections = mapOf(5 to 3)), Random(1))
        assertTrue(pool.all { it.available })
        assertTrue(pool.any { it.inv == 500L })
        assertTrue(pool.none { it.inv == 501L })
        assertTrue(pool.any { it.udcSection == 5 })
        // Общата извадка — 40 случайни, не първите в каталога.
        val general = SurprisePicker.pool(searcher, SurpriseTastes(), Random(2))
        assertEquals(40, general.size)
        assertNotEquals((1..60L).filter { it % 3 != 0L }.take(40), general.map { it.inv })
    }
}
