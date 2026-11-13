package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import java.time.LocalDate

/**
 * „Книга на деня“ (уиджетът): всеки ден една и съща за всички книга от каталога, избрана по
 * датата — без случайност и без данни за читателя. [index] и [choose] са чисти функции;
 * [pick] чете малки части от търсачката (не целия каталог наведнъж). Записите в каталога
 * често са оскъдни (без автор, корица, анотация) — предпочитат се пълните, но липсата не пречи;
 * тестовите записи ([RealRecords]) се избягват, докато има истински.
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

    /** Колко записа се четат наведнъж, докато се търсят истинските (по страници, не целият каталог в списък). */
    private const val SCAN = 500

    /**
     * От [window]: истински запис ([RealRecords]) със заглавие — пълният (с автор) е с предимство;
     * ако няма истински — първата книга със заглавие и автор, иначе първата със заглавие, иначе нищо.
     */
    fun choose(window: List<CatalogBook>): CatalogBook? = chooseReal(window)
        ?: window.firstOrNull { it.title.isNotBlank() && it.author.isNotBlank() } ?: window.firstOrNull { it.title.isNotBlank() }

    private fun chooseReal(window: List<CatalogBook>): CatalogBook? {
        val real = window.filter { RealRecords.isReal(it) && it.title.isNotBlank() }
        return real.firstOrNull { it.author.isNotBlank() } ?: real.firstOrNull()
    }

    /**
     * Книгата на деня [date] от [searcher]. Истинските записи са с предимство (в живия каталог повечето
     * са тестови): от тях, подредени по заглавие, се взема мястото на деня ([index]) — първо сред
     * наличните, после сред всички. Ако истински няма изобщо — старото правило ([choose] върху малък
     * прозорец около мястото на деня). `null` — каталогът е празен.
     */
    suspend fun pick(searcher: CatalogSearcher, date: LocalDate): CatalogBook? {
        val queries = listOf(true, false).map { CatalogQuery(onlyAvailable = it, sort = CatalogSort.TITLE) }
        val firsts = arrayOfNulls<CatalogResults>(queries.size)
        suspend fun first(i: Int) = firsts[i] ?: searcher.searchFirst(queries[i], WINDOW).also { firsts[i] = it }
        for (i in queries.indices) {
            val first = first(i)
            if (first.total <= 0) continue
            val real = realRecords(searcher, queries[i], first)
            if (real.isNotEmpty()) return real[index(real.size, date)]
        }
        for (i in queries.indices) {
            val first = first(i)
            if (first.total <= 0) continue
            val at = index(first.total, date)
            val window = if (at < first.books.size) first.books.drop(at) + searcher.searchMore(queries[i], first.books.size, WINDOW).orEmpty()
            else searcher.searchMore(queries[i], at, WINDOW).orEmpty()
            choose(window)?.let { return it }
            choose(first.books)?.let { return it }
        }
        return null
    }

    /** Истинските записи със заглавие от целия резултат, по страници; с автор — ако има такива, само те. */
    private suspend fun realRecords(searcher: CatalogSearcher, query: CatalogQuery, first: CatalogResults): List<CatalogBook> {
        val real = ArrayList<CatalogBook>()
        fun take(page: List<CatalogBook>) = page.filterTo(real) { RealRecords.isReal(it) && it.title.isNotBlank() }
        take(first.books)
        var offset = first.books.size
        while (offset < first.total) {
            val page = searcher.searchMore(query, offset, SCAN).orEmpty()
            if (page.isEmpty()) break
            take(page)
            offset += page.size
        }
        return real.filter { it.author.isNotBlank() }.ifEmpty { real }
    }
}
