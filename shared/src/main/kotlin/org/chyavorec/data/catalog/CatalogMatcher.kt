package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField
import java.text.CollationKey
import java.text.Collator
import java.util.Locale

/**
 * Общите правила на търсенето в каталога — филтри, точкуване и подреждане. Ползват ги
 * и индексът в паметта ([CatalogSearchEngine]), и пълнотекстовият индекс (Room FTS,
 * [FtsCatalogSearcher]), така че двата пътя дават еднакви резултати.
 */
internal object CatalogMatcher {

    /**
     * Съвпадение с точките му и всичко нужно за подреждането. [ord] е мястото в каталога —
     * книгата се взима по него едва след подреждането (в базата — само за показаната страница).
     */
    class Hit(val ord: Int, val score: Int, val titleRank: Int, val authorRank: Int, val year: Int?, val registeredOn: String, val inv: Long)

    /** Филтрите на заявката (без текста). */
    fun passesFilters(b: CatalogBook, year: Int?, q: CatalogQuery): Boolean {
        if (q.onlyAvailable && !b.available) return false
        if (q.docType != null && b.docType != q.docType) return false
        if (q.department != null && b.department != q.department) return false
        if (q.language != null && b.language != q.language) return false
        if (q.udcSection != null && b.udcSection != q.udcSection) return false
        if (q.yearFrom != null || q.yearTo != null) {
            val y = year ?: return false
            if (q.yearFrom != null && y < q.yearFrom) return false
            if (q.yearTo != null && y > q.yearTo) return false
        }
        return true
    }

    /**
     * null = не съвпада; по-голямо число = по-добро съвпадение. Полетата са
     * нормализирани ([CatalogIndexRows]); [rest] — анотация, издател, вид, сигнатура,
     * инв. № и ISBN, слепени с интервал.
     */
    fun score(
        title: String,
        author: String,
        keywords: String,
        rest: String,
        isbnDigits: String,
        inv: Long,
        field: SearchField,
        tokens: List<String>,
        digits: String,
    ): Int? = when (field) {
        SearchField.TITLE -> matchAll(title, tokens)?.let { it + prefixBonus(title, tokens) }
        SearchField.AUTHOR -> matchAll(author, tokens)?.let { it + prefixBonus(author, tokens) }
        SearchField.KEYWORD -> matchAll(keywords, tokens)
        SearchField.ISBN -> if (digits.length >= 3 && isbnDigits.contains(digits)) 10 else null
        SearchField.INVENTORY -> if (digits.isNotEmpty() && inv.toString() == digits.trimStart('0')) 100
        else if (digits.isNotEmpty() && inv.toString().startsWith(digits)) 10 else null
        SearchField.ALL -> scoreAll(title, author, keywords, rest, inv, tokens, digits)
    }

    private fun scoreAll(title: String, author: String, keywords: String, rest: String, inv: Long, tokens: List<String>, digits: String): Int? {
        // Думите не съдържат интервали, затова „поне едно поле съдържа думата“ е
        // същото като търсене в слепените с интервал полета.
        for (t in tokens) {
            if (!title.contains(t) && !author.contains(t) && !keywords.contains(t) && !rest.contains(t)) return null
        }
        var s = 1
        if (matchAll(title, tokens) != null) s += 20 + prefixBonus(title, tokens)
        if (matchAll(author, tokens) != null) s += 15 + prefixBonus(author, tokens)
        if (matchAll(keywords, tokens) != null) s += 5
        if (digits.isNotEmpty() && inv.toString() == digits) s += 100
        return s
    }

    fun matchAll(haystack: String, tokens: List<String>): Int? {
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

    private val BY_TITLE: Comparator<Hit> = Comparator { a, b -> a.titleRank.compareTo(b.titleRank) }
    private val BY_AUTHOR: Comparator<Hit> = Comparator { a, b -> a.authorRank.compareTo(b.authorRank) }

    /**
     * Подрежда съвпаденията на място. Сортирането е стабилно — при равенство остава
     * редът на подаване (редът в каталога).
     */
    fun sort(hits: MutableList<Hit>, sort: CatalogSort, hasText: Boolean) {
        val comparator: Comparator<Hit> = when (sort) {
            CatalogSort.RELEVANCE ->
                if (!hasText) BY_TITLE
                else compareByDescending<Hit> { it.score }.then(BY_TITLE)
            CatalogSort.TITLE -> BY_TITLE
            CatalogSort.AUTHOR -> BY_AUTHOR.then(BY_TITLE)
            CatalogSort.YEAR_DESC -> compareByDescending { it.year ?: Int.MIN_VALUE }
            CatalogSort.YEAR_ASC -> compareBy { it.year ?: Int.MAX_VALUE }
            CatalogSort.NEWEST -> compareByDescending<Hit> { it.registeredOn }
                .thenByDescending { it.inv }
        }
        hits.sortWith(comparator)
    }

    /**
     * Българското азбучно място на заглавието и автора на всеки запис (равните имат
     * равен ранг; без автор — накрая). Сравнението на ранговете дава реда на Collator.
     */
    fun ranks(books: List<CatalogBook>): Pair<IntArray, IntArray> {
        val collator: Collator = Collator.getInstance(Locale.forLanguageTag("bg-BG"))
        val title = IntArray(books.size)
        val author = IntArray(books.size)
        assignRanks(books.map { collator.getCollationKey(it.title) }) { i, r -> title[i] = r }
        assignRanks(books.map { collator.getCollationKey(it.author.ifBlank { "￿" }) }) { i, r -> author[i] = r }
        return title to author
    }

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
}
