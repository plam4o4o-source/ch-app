package org.chyavorec.data.catalog

import org.chyavorec.core.Isbn
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogHomeShelf
import org.chyavorec.domain.model.CatalogHomeSummary
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot

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
        /** Място в азбучния ред (български) — равни заглавия/автори имат равен ранг. */
        val titleRank: Int,
        val authorRank: Int,
    )

    private val index: List<Indexed> = run {
        val books = snapshot.books
        val (titleRanks, authorRanks) = CatalogMatcher.ranks(books)
        books.mapIndexed { i, b ->
            Indexed(
                book = b,
                title = CatalogIndexRows.normalizedTitle(b),
                author = TextNormalizer.normalize(b.author),
                keywords = TextNormalizer.normalize(b.keywords),
                rest = CatalogIndexRows.rest(b),
                isbnDigits = TextNormalizer.digits(b.isbn),
                year = b.yearNumber,
                titleRank = titleRanks[i],
                authorRank = authorRanks[i],
            )
        }
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

    val facets: CatalogFacets by lazy { facetsOf(snapshot) }

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

    fun search(query: CatalogQuery): List<CatalogBook> {
        val tokens = TextNormalizer.tokens(query.text)
        val digits = TextNormalizer.digits(query.text)
        val hits = ArrayList<CatalogMatcher.Hit>()
        for (item in index) {
            if (!CatalogMatcher.passesFilters(item.book, item.year, query)) continue
            val score = if (tokens.isEmpty()) 0
            else CatalogMatcher.score(item.title, item.author, item.keywords, item.rest, item.isbnDigits, item.book.inv, query.field, tokens, digits) ?: continue
            hits += CatalogMatcher.Hit(item.book, score, item.titleRank, item.authorRank, item.year)
        }
        CatalogMatcher.sort(hits, query.sort, hasText = tokens.isNotEmpty())
        return hits.map { it.book }
    }

    /** Предложения за автоматично допълване: заглавия и автори. */
    fun suggestions(prefix: String, limit: Int = 8): List<String> {
        val tokens = TextNormalizer.tokens(prefix)
        if (tokens.isEmpty()) return emptyList()
        val out = LinkedHashSet<String>()
        for (item in index) if (!addSuggestions(out, item.book, item.title, item.author, tokens, limit)) break
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

        /**
         * Добавя автора и заглавието на книгата към предложенията, ако съдържат всички думи.
         * `false` — предложенията са вече [limit].
         */
        internal fun addSuggestions(out: LinkedHashSet<String>, book: CatalogBook, title: String, author: String, tokens: List<String>, limit: Int): Boolean {
            if (out.size >= limit) return false
            if (CatalogMatcher.matchAll(author, tokens) != null && book.author.isNotBlank()) out += book.author
            if (out.size >= limit) return false
            if (CatalogMatcher.matchAll(title, tokens) != null) out += book.title
            return out.size < limit
        }

        /** Възможните стойности за филтрите — от реалните данни. */
        fun facetsOf(snapshot: CatalogSnapshot): CatalogFacets {
            val years = snapshot.books.mapNotNull { it.yearNumber }.filter { it in 1500..2100 }
            return CatalogFacets(
                docTypes = snapshot.books.map { it.docType }.countedDistinct(),
                departments = snapshot.books.map { it.department }.countedDistinct(),
                languages = snapshot.books.map { it.language }.countedDistinct(),
                udcSections = snapshot.books.mapNotNull { it.udcSection }.distinct().sorted(),
                minYear = years.minOrNull(),
                maxYear = years.maxOrNull(),
            )
        }

        /** Най-често срещаните стойности първо. */
        private fun List<String>.countedDistinct(): List<String> =
            filter { it.isNotBlank() }.groupingBy { it }.eachCount().entries
                .sortedByDescending { it.value }.map { it.key }

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
