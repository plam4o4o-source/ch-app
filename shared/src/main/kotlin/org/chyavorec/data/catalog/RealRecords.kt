package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook

/**
 * Кои записи в каталога са истински, а кои — тестови. Истинските имат шестцифрен инвентарен номер
 * (тестовите са 1–4 цифри); някои тестови обаче са и с „истински“ номер — те се познават по думите
 * в заглавието/автора. Едно място за викторината и за „Книга на деня“.
 */
object RealRecords {
    /** Инвентарните номера на истинските записи са шестцифрени (тестовите са 1–4 цифри). */
    val INV_RANGE = 100_000L..999_999L

    /** Тестови записи, които може да са със „истински“ номер. */
    private val JUNK = listOf("тест", "test", "проба", "асдф", "xxx", "lorem")

    fun isRealInv(inv: Long): Boolean = inv in INV_RANGE

    /** Някой от [texts] съдържа дума от тестовите записи. */
    fun looksLikeTest(vararg texts: String): Boolean {
        val lower = texts.map { it.lowercase() }
        return JUNK.any { junk -> lower.any { junk in it } }
    }

    /** Истински запис: шестцифрен номер и без тестови думи в заглавието/автора. */
    fun isReal(book: CatalogBook): Boolean = isRealInv(book.inv) && !looksLikeTest(book.title, book.author)
}
