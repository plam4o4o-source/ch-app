package org.chyavorec.data

import org.chyavorec.data.catalog.RealRecords
import org.chyavorec.domain.model.CatalogBook
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Общото правило „истински запис“ — за викторината и за „Книга на деня“. */
class RealRecordsTest {
    private fun book(inv: Long, title: String = "Под игото", author: String = "Вазов, Иван") = CatalogBook(inv, author, title)

    @Test fun onlySixDigitInventoryNumbersAreReal() {
        assertTrue(RealRecords.isReal(book(100_000)))
        assertTrue(RealRecords.isReal(book(999_999)))
        assertFalse(RealRecords.isReal(book(99_999)))
        assertFalse(RealRecords.isReal(book(1_000_000)))
        assertFalse(RealRecords.isReal(book(312)))
        assertEquals(100_000L..999_999L, RealRecords.INV_RANGE)
    }

    @Test fun testWordsMakeARealNumberJunk() {
        assertFalse(RealRecords.isReal(book(399_998, title = "Тестова книга")))
        assertFalse(RealRecords.isReal(book(399_998, author = "Тестов Автор")))
        assertFalse(RealRecords.isReal(book(399_998, title = "My TEST")))
        assertFalse(RealRecords.isReal(book(399_998, title = "Lorem ipsum")))
        assertTrue(RealRecords.looksLikeTest("Асдф"))
        assertFalse(RealRecords.looksLikeTest("Анатомът", "Андахази, Федерико"))
    }
}
