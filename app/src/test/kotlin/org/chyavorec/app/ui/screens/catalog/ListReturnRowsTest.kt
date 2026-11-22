package org.chyavorec.app.ui.screens.catalog

import org.chyavorec.domain.model.CatalogBook
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Празните места в първия кадър при връщане от книга: височината на запомнения ред — да; редът на
 * отворената книга, непознат ред, друга ширина/шрифт и книга с променени данни — не (строи се веднага).
 */
class ListReturnRowsTest {
    private val phone = ListLayoutKey(width = 1080, density = 2.625f, fontScale = 1f)
    private fun book(inv: Long, available: Boolean = true) = CatalogBook(inv = inv, author = "Автор $inv", title = "Заглавие $inv", available = available)

    @Test fun otherVisibleRowsGetTheirHeight() {
        val rows = ListReturnRows()
        rows.onOpen(2, phone, listOf(book(1) to 210, book(2) to 260, book(3) to 231))
        assertEquals(2L, rows.opened)
        assertEquals(210, rows.standIn(book(1), phone))
        assertEquals(231, rows.standIn(book(3), phone))
    }

    @Test fun theOpenedBookIsBuiltAtOnce() {
        val rows = ListReturnRows()
        rows.onOpen(2, phone, listOf(book(1) to 210, book(2) to 260))
        // Корицата ѝ прелита обратно — редът трябва да е в първия кадър.
        assertNull(rows.standIn(book(2), phone))
    }

    @Test fun unknownRowOrNothingOpened() {
        val rows = ListReturnRows()
        assertNull(rows.opened)
        assertNull(rows.standIn(book(1), phone))
        rows.onOpen(2, phone, listOf(book(1) to 210))
        assertNull(rows.standIn(book(7), phone))
    }

    @Test fun anotherLayoutForgetsTheHeights() {
        val rows = ListReturnRows()
        rows.onOpen(2, phone, listOf(book(1) to 210))
        // Завъртане или по-едър шрифт: редът е с друга височина.
        assertNull(rows.standIn(book(1), phone.copy(width = 2400)))
        assertNull(rows.standIn(book(1), phone.copy(fontScale = 1.3f)))
        rows.onOpen(3, phone.copy(width = 2400), listOf(book(4) to 180))
        assertNull(rows.standIn(book(1), phone.copy(width = 2400)))
        assertEquals(180, rows.standIn(book(4), phone.copy(width = 2400)))
    }

    @Test fun changedBookDataIsBuiltAtOnce() {
        val rows = ListReturnRows()
        rows.onOpen(2, phone, listOf(book(1) to 210))
        // Междувременно заета: с етикет редът е по-висок.
        assertNull(rows.standIn(book(1, available = false), phone))
    }

    @Test fun laterOpenKeepsEarlierRowsOfTheSameLayout() {
        val rows = ListReturnRows()
        rows.onOpen(2, phone, listOf(book(1) to 210, book(2) to 260))
        rows.onOpen(5, phone, listOf(book(5) to 240, book(6) to 199))
        assertEquals(5L, rows.opened)
        assertEquals(260, rows.standIn(book(2), phone))
        assertEquals(199, rows.standIn(book(6), phone))
        assertNull(rows.standIn(book(5), phone))
    }
}
