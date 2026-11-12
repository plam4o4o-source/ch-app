package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import java.time.LocalDate

/**
 * „Книга на деня“ (уиджетът): всеки ден една и съща за всички книга от каталога, избрана по
 * датата — без случайност и без данни за читателя. [index] и [choose] са чисти функции;
 * [pick] чете само един малък прозорец от търсачката (не целия каталог). Записите в каталога
 * често са оскъдни (без автор, корица, анотация) — предпочитат се пълните, но липсата не пречи.
 */
object BookOfTheDay {
    /** Колко поредни книги се разглеждат от избраното място нататък, докато се намери пълен запис. */
    const val WINDOW = 12

    /** Мястото [0, total) за деня [date] — стабилно, равномерно разбъркано (SplitMix64 върху деня). */
    fun index(total: Int, date: LocalDate): Int {
        require(total > 0)
        var z = date.toEpochDay() * -0x61c8864680b583ebL
        z = (z xor (z ushr 30)) * -0x40a7b892e31b1a47L
        z = (z xor (z ushr 27)) * -0x6b2fb644ecceee15L
        z = z xor (z ushr 31)
        return Math.floorMod(z, total.toLong()).toInt()
    }

    /** Първата книга със заглавие и автор от [window]; иначе първата със заглавие; иначе нищо. */
    fun choose(window: List<CatalogBook>): CatalogBook? =
        window.firstOrNull { it.title.isNotBlank() && it.author.isNotBlank() } ?: window.firstOrNull { it.title.isNotBlank() }

    /**
     * Книгата на деня [date] от [searcher]: сред наличните (по заглавие), а ако няма налични —
     * сред всички. `null` — каталогът е празен.
     */
    suspend fun pick(searcher: CatalogSearcher, date: LocalDate): CatalogBook? {
        for (onlyAvailable in listOf(true, false)) {
            val query = CatalogQuery(onlyAvailable = onlyAvailable, sort = CatalogSort.TITLE)
            val first = searcher.searchFirst(query, WINDOW)
            if (first.total <= 0) continue
            val at = index(first.total, date)
            val window = if (at < first.books.size) first.books.drop(at) + searcher.searchMore(query, first.books.size, WINDOW).orEmpty()
            else searcher.searchMore(query, at, WINDOW).orEmpty()
            choose(window)?.let { return it }
            choose(first.books)?.let { return it }
        }
        return null
    }
}
