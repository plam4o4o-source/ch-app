package org.chyavorec.data.catalog

import org.chyavorec.core.Isbn
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogHomeShelf
import org.chyavorec.domain.model.CatalogHomeSummary
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField
import java.text.CollationKey
import java.text.Collator

/**
 * Търсене в каталога в паметта. Индексът (нормализираните полета) се изгражда
 * веднъж на снимка; при 15 000 записа едно търсене отнема няколко милисекунди.
 *
 * Памет: всяко поле се пази нормализирано само веднъж (заглавие, автор, ключови думи
 * и „останалото“ — без повторение в общ низ), а българското азбучно подреждане е
 * предварително изчислено като цели числа (ранг) вместо CollationKey на запис.
 */
class CatalogSearchEngine(val snapshot: CatalogSnapshot) {

    private class Indexed(
        val book: CatalogBook,
        val title: String,
        val author: String,
        val keywords: String,
        /** Анотация, издател, вид, сигнатура, инв. №, ISBN — нормализирани, без горните три. */
        val rest: String,
        val isbnDigits: String,
        /** Годината, разчетена веднъж. */
        val year: Int?,
    ) {
        /** Място в азбучния ред (български) — равни заглавия/автори имат равен ранг. */
        var titleRank: Int = 0
        var authorRank: Int = 0
    }

    private val index: List<Indexed> = snapshot.books.map { b ->
        Indexed(
            book = b,
            title = TextNormalizer.normalize(b.title + " " + b.subtitle),
            author = TextNormalizer.normalize(b.author),
            keywords = TextNormalizer.normalize(b.keywords),
            rest = listOf(
                TextNormalizer.normalize(b.annotation),
                TextNormalizer.normalize(b.publisher),
                TextNormalizer.normalize(b.docType),
                b.callNumber.lowercase(), b.inv.toString(), b.isbn,
            ).joinToString(" "),
            isbnDigits = TextNormalizer.digits(b.isbn),
            year = b.yearNumber,
        )
    }

    init {
        val collator: Collator = Collator.getInstance(java.util.Locale.forLanguageTag("bg-BG"))
        assignRanks(index.map { collator.getCollationKey(it.book.title) }) { i, r -> index[i].titleRank = r }
        // Без автор — накрая.
        assignRanks(index.map { collator.getCollationKey(it.book.author.ifBlank { "￿" }) }) { i, r -> index[i].authorRank = r }
    }

    private val byInv: Map<Long, CatalogBook> = snapshot.books.associateBy { it.inv }

    fun book(inv: Long): CatalogBook? = byInv[inv]

    /** Екземпляр по инвентарен номер (етикетът/баркодът на библиотеката). */
    fun findByInv(inv: Long): CatalogBook? = byInv[inv]

    /**
     * Книга по ISBN: нормализира тирета/интервали и търси и двете форми
     * (ISBN-10 ↔ ISBN-13). Наличните екземпляри са с предимство.
     */
    fun findByIsbn(raw: String?): CatalogBook? {
        val wanted = Isbn.candidates(raw).filter { it.length >= 10 }
        if (wanted.isEmpty()) return null
        val hits = index.filter { it.isbnDigits.isNotEmpty() && it.isbnDigits in wanted }
        return hits.firstOrNull { it.book.available }?.book ?: hits.firstOrNull()?.book
    }

    /** Всички екземпляри с този ISBN (за броя налични в резултата от сканиране). */
    fun allByIsbn(raw: String?): List<CatalogBook> {
        val wanted = Isbn.candidates(raw).filter { it.length >= 10 }
        if (wanted.isEmpty()) return emptyList()
        return index.filter { it.isbnDigits.isNotEmpty() && it.isbnDigits in wanted }.map { it.book }
    }

    val facets: CatalogFacets by lazy {
        val years = index.mapNotNull { it.year }.filter { it in 1500..2100 }
        CatalogFacets(
            docTypes = snapshot.books.map { it.docType }.countedDistinct(),
            departments = snapshot.books.map { it.department }.countedDistinct(),
            languages = snapshot.books.map { it.language }.countedDistinct(),
            udcSections = snapshot.books.mapNotNull { it.udcSection }.distinct().sorted(),
            minYear = years.minOrNull(),
            maxYear = years.maxOrNull(),
        )
    }

    /**
     * Различните автори (в реда на каталога) с нормализирания им вид — за глобалното
     * търсене; изчислява се веднъж, при първа нужда.
     */
    val authorIndex: List<Pair<String, String>> by lazy {
        val seen = HashSet<String>()
        index.mapNotNull { item ->
            val a = item.book.author
            if (a.isBlank() || !seen.add(a)) null else a to item.author
        }
    }

    /** Най-често срещаните стойности първо. */
    private fun List<String>.countedDistinct(): List<String> =
        filter { it.isNotBlank() }.groupingBy { it }.eachCount().entries
            .sortedByDescending { it.value }.map { it.key }

    fun search(query: CatalogQuery): List<CatalogBook> {
        val tokens = TextNormalizer.tokens(query.text)
        val digits = TextNormalizer.digits(query.text)
        val scored = ArrayList<Scored>()
        for (item in index) {
            val b = item.book
            if (query.onlyAvailable && !b.available) continue
            if (query.docType != null && b.docType != query.docType) continue
            if (query.department != null && b.department != query.department) continue
            if (query.language != null && b.language != query.language) continue
            if (query.udcSection != null && b.udcSection != query.udcSection) continue
            if (query.yearFrom != null || query.yearTo != null) {
                val y = item.year ?: continue
                if (query.yearFrom != null && y < query.yearFrom) continue
                if (query.yearTo != null && y > query.yearTo) continue
            }
            val score = if (tokens.isEmpty()) 0 else score(item, query.field, tokens, digits) ?: continue
            scored += Scored(item, score)
        }
        val comparator: Comparator<Scored> = when (query.sort) {
            CatalogSort.RELEVANCE ->
                if (tokens.isEmpty()) BY_TITLE
                else compareByDescending<Scored> { it.score }.then(BY_TITLE)
            CatalogSort.TITLE -> BY_TITLE
            CatalogSort.AUTHOR -> BY_AUTHOR.then(BY_TITLE)
            CatalogSort.YEAR_DESC -> compareByDescending { it.item.year ?: Int.MIN_VALUE }
            CatalogSort.YEAR_ASC -> compareBy { it.item.year ?: Int.MAX_VALUE }
            CatalogSort.NEWEST -> compareByDescending<Scored> { it.item.book.registeredOn }
                .thenByDescending { it.item.book.inv }
        }
        scored.sortWith(comparator)
        return scored.map { it.item.book }
    }

    private class Scored(val item: Indexed, val score: Int)

    /** null = не съвпада; по-голямо число = по-добро съвпадение. */
    private fun score(item: Indexed, field: SearchField, tokens: List<String>, digits: String): Int? {
        return when (field) {
            SearchField.TITLE -> matchAll(item.title, tokens)?.let { it + prefixBonus(item.title, tokens) }
            SearchField.AUTHOR -> matchAll(item.author, tokens)?.let { it + prefixBonus(item.author, tokens) }
            SearchField.KEYWORD -> matchAll(item.keywords, tokens)
            SearchField.ISBN -> if (digits.length >= 3 && item.isbnDigits.contains(digits)) 10 else null
            SearchField.INVENTORY -> if (digits.isNotEmpty() && item.book.inv.toString() == digits.trimStart('0')) 100
            else if (digits.isNotEmpty() && item.book.inv.toString().startsWith(digits)) 10 else null
            SearchField.ALL -> {
                // Думите не съдържат интервали, затова „поне едно поле съдържа думата“ е
                // същото като търсене в слепените с интервал полета.
                for (t in tokens) {
                    if (!item.title.contains(t) && !item.author.contains(t) && !item.keywords.contains(t) && !item.rest.contains(t)) return null
                }
                var s = 1
                if (matchAll(item.title, tokens) != null) s += 20 + prefixBonus(item.title, tokens)
                if (matchAll(item.author, tokens) != null) s += 15 + prefixBonus(item.author, tokens)
                if (matchAll(item.keywords, tokens) != null) s += 5
                if (digits.isNotEmpty() && item.book.inv.toString() == digits) s += 100
                s
            }
        }
    }

    private fun matchAll(haystack: String, tokens: List<String>): Int? {
        for (t in tokens) if (!haystack.contains(t)) return null
        return 1
    }

    private fun prefixBonus(haystack: String, tokens: List<String>): Int {
        val first = tokens.first()
        return when {
            haystack.startsWith(first) -> 10
            haystack.contains(" $first") -> 4
            else -> 0
        }
    }

    /** Предложения за автоматично допълване: заглавия и автори. */
    fun suggestions(prefix: String, limit: Int = 8): List<String> {
        val tokens = TextNormalizer.tokens(prefix)
        if (tokens.isEmpty()) return emptyList()
        val out = LinkedHashSet<String>()
        for (item in index) {
            if (out.size >= limit) break
            if (matchAll(item.author, tokens) != null && item.book.author.isNotBlank()) out += item.book.author
            if (out.size >= limit) break
            if (matchAll(item.title, tokens) != null) out += item.book.title
        }
        return out.toList()
    }

    /** Нови постъпления — по дата на постъпване (ключ `d`). */
    fun newest(limit: Int): List<CatalogBook> = newest(snapshot.books, limit)

    fun shelfBooks(): List<Pair<String, List<CatalogBook>>> = shelfBooks(snapshot, byInv)

    fun byAuthor(author: String, excludeInv: Long, limit: Int = 10): List<CatalogBook> {
        if (author.isBlank()) return emptyList()
        return snapshot.books.filter { it.author == author && it.inv != excludeInv }
            .distinctBy { it.title }.take(limit)
    }

    /** Другите екземпляри от същото заглавие (еднакво заглавие + автор). */
    fun copiesOf(book: CatalogBook): List<CatalogBook> =
        snapshot.books.filter { it.title == book.title && it.author == book.author && it.inv != book.inv }

    companion object {
        /** Брой нови постъпления на началния екран. */
        const val HOME_NEWEST = 12

        private val BY_TITLE: Comparator<Scored> = Comparator { a, b -> a.item.titleRank.compareTo(b.item.titleRank) }
        private val BY_AUTHOR: Comparator<Scored> = Comparator { a, b -> a.item.authorRank.compareTo(b.item.authorRank) }

        /**
         * Ранг по ключовете: подрежда ги веднъж и дава на равните ключове еднакъв ранг,
         * така че сравнението на ранговете дава същия ред като на ключовете.
         */
        private fun assignRanks(keys: List<CollationKey>, set: (Int, Int) -> Unit) {
            val order = keys.indices.sortedWith { a, b -> keys[a].compareTo(keys[b]) }
            var rank = 0
            for (pos in order.indices) {
                if (pos > 0 && keys[order[pos]].compareTo(keys[order[pos - 1]]) != 0) rank++
                set(order[pos], rank)
            }
        }

        fun newest(books: List<CatalogBook>, limit: Int): List<CatalogBook> =
            books.filter { it.registeredOn.isNotBlank() }
                .sortedWith(compareByDescending<CatalogBook> { it.registeredOn }.thenByDescending { it.inv })
                .take(limit)

        fun shelfBooks(snapshot: CatalogSnapshot, byInv: Map<Long, CatalogBook> = snapshot.books.associateBy { it.inv }): List<Pair<String, List<CatalogBook>>> =
            snapshot.shelves.map { shelf -> shelf.name to shelf.invNumbers.mapNotNull { byInv[it] } }
                .filter { it.second.isNotEmpty() }

        /** Обобщението за началния екран — без да се строи индексът за търсене. */
        fun homeSummary(snapshot: CatalogSnapshot): CatalogHomeSummary = CatalogHomeSummary(
            count = snapshot.books.size,
            generatedOn = snapshot.generatedOn,
            newest = newest(snapshot.books, HOME_NEWEST),
            shelves = shelfBooks(snapshot).map { (name, books) -> CatalogHomeShelf(name, books) },
        )
    }
}
