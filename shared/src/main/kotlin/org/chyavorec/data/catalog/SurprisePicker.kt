package org.chyavorec.data.catalog

import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.SearchField
import kotlin.random.Random

/**
 * Вкусът на читателя за „Изненадай ме“ — само от данни, които вече са на телефона:
 * „Искам да прочета“, историята на четене (влязъл читател) и последните търсения.
 */
data class SurpriseTastes(
    /** Любимите автори (както са в каталога), най-честите първо. */
    val authors: List<String> = emptyList(),
    /** Основните раздели на УДК → колко книги от тях. */
    val sections: Map<Int, Int> = emptyMap(),
    /** Последните търсения (както са написани), най-новите първо. */
    val searches: List<String> = emptyList(),
    /** Прочетеното и вече отбелязаното — не се предлага (инв. №). */
    val excludeInv: Set<Long> = emptySet(),
    /** Същото по заглавие (друг екземпляр на прочетена книга). */
    val excludeTitles: Set<String> = emptySet(),
) {
    internal val authorKeys: Set<String> = authors.mapNotNullTo(HashSet()) { ReadingRecommender.authorKey(it) }
    internal val searchWords: Set<String> = searches.flatMapTo(HashSet()) { s ->
        TextNormalizer.normalize(s).split(' ').filter { it.length >= MIN_WORD }
    }
    private val sectionTotal = sections.values.sum()

    /** Делът на раздела във вкуса (0..1). */
    internal fun sectionShare(section: Int?): Double =
        if (section == null || sectionTotal == 0) 0.0 else (sections[section] ?: 0).toDouble() / sectionTotal

    val isEmpty: Boolean get() = authors.isEmpty() && sections.isEmpty() && searches.isEmpty()

    companion object {
        private const val MIN_WORD = 3
        private const val MAX_AUTHORS = 6
        private const val MAX_SEARCHES = 5

        /**
         * [wanted] — книгите от „Искам да прочета“ (от каталога), [readBooks] — прочетените, които
         * са в каталога, [history] — цялата история (и книгите, отписани от каталога).
         */
        fun of(
            wanted: List<CatalogBook> = emptyList(),
            readBooks: List<CatalogBook> = emptyList(),
            history: List<HistoryItem> = emptyList(),
            searches: List<String> = emptyList(),
        ): SurpriseTastes {
            val authorCount = LinkedHashMap<String, Pair<String, Int>>()
            fun addAuthor(a: String) {
                val key = ReadingRecommender.authorKey(a) ?: return
                val (shown, n) = authorCount[key] ?: (a.trim() to 0)
                authorCount[key] = shown to n + 1
            }
            wanted.forEach { b -> b.authors.forEach(::addAuthor) }
            history.forEach { h -> h.author.split(';').filter { it.isNotBlank() }.forEach(::addAuthor) }
            val sections = HashMap<Int, Int>()
            (wanted + readBooks).forEach { b -> b.udcSection?.let { sections[it] = (sections[it] ?: 0) + 1 } }
            return SurpriseTastes(
                authors = authorCount.values.sortedByDescending { it.second }.map { it.first }.take(MAX_AUTHORS),
                sections = sections,
                searches = searches.map { it.trim() }.filter { it.length >= MIN_WORD }.distinct().take(MAX_SEARCHES),
                excludeInv = (wanted.map { it.inv } + history.mapNotNull { it.inv }).toSet(),
                excludeTitles = history.mapTo(HashSet()) { ReadingRecommender.titleKey(it.title, it.author) },
            )
        }
    }
}

/**
 * „Изненадай ме“: случайна книга на рафта, по-често от любимите автори, раздели и търсения;
 * без вкус — просто случайна налична, по-често с корица. Не повтаря скоро предложените.
 * [pick] и [weight] са чисти функции; [pool] събира кандидатите от търсачката.
 */
object SurprisePicker {
    const val AUTHOR_BOOST = 4.0
    const val SEARCH_BOOST = 2.5
    /** Разделът с целия вкус — ×(1 + 2); половината — ×2. */
    const val SECTION_BOOST = 2.0
    const val COVER_BOOST = 1.6
    /** Колко от последните предложения не се повтарят. */
    const val RECENT = 12

    /** Ключът, по който се помнят предложенията (всички екземпляри на заглавието). */
    fun key(book: CatalogBook): String = ReadingRecommender.titleKey(book.title, book.author)

    /** Теглото на книгата: 0 — не е на рафта. */
    fun weight(book: CatalogBook, tastes: SurpriseTastes): Double {
        if (!book.available) return 0.0
        var w = 1.0
        if (book.coverUrl.isNotBlank()) w *= COVER_BOOST
        if (tastes.authorKeys.isNotEmpty() && book.authors.any { ReadingRecommender.authorKey(it) in tastes.authorKeys }) w *= AUTHOR_BOOST
        w *= 1.0 + SECTION_BOOST * tastes.sectionShare(book.udcSection)
        if (tastes.searchWords.isNotEmpty()) {
            val text = TextNormalizer.normalize(book.title + " " + book.author)
            if (tastes.searchWords.any { text.contains(it) }) w *= SEARCH_BOOST
        }
        return w
    }

    /**
     * Една книга от [pool]: налична, не прочетена/отбелязана, по едно от заглавие, без [recent]
     * (най-старото първо). Ако всичко вече е предлагано — пак, но не последното. `null` — няма налична.
     */
    fun pick(pool: List<CatalogBook>, tastes: SurpriseTastes, recent: List<String>, random: Random): CatalogBook? {
        val seen = HashSet<String>()
        val candidates = pool.filter {
            it.available && it.inv !in tastes.excludeInv && key(it).let { k -> k !in tastes.excludeTitles && seen.add(k) }
        }
        if (candidates.isEmpty()) return null
        val recentSet = recent.toHashSet()
        val from = candidates.filter { key(it) !in recentSet }
            .ifEmpty { candidates.filter { key(it) != recent.lastOrNull() } }
            .ifEmpty { candidates }
        val weights = DoubleArray(from.size) { weight(from[it], tastes) }
        val total = weights.sum()
        if (total <= 0.0) return from[random.nextInt(from.size)]
        var r = random.nextDouble() * total
        for (i in from.indices) {
            r -= weights[i]
            if (r < 0) return from[i]
        }
        return from.last()
    }

    /** Новият списък на скоро предложените (до [RECENT], най-новото накрая). */
    fun remember(recent: List<String>, book: CatalogBook): List<String> = (recent.filter { it != key(book) } + key(book)).takeLast(RECENT)

    /**
     * Кандидатите от търсачката (само налични): книгите на любимите автори, случайни от любимите
     * раздели, по последните търсения и случайни от целия каталог — няколко малки заявки, не целият каталог.
     */
    suspend fun pool(searcher: CatalogSearcher, tastes: SurpriseTastes, random: Random): List<CatalogBook> {
        val out = ArrayList<CatalogBook>()
        tastes.authors.take(3).forEach { a ->
            out += searcher.searchFirst(CatalogQuery(text = a, field = SearchField.AUTHOR, onlyAvailable = true), PER_TASTE).books.take(PER_TASTE)
        }
        tastes.sections.entries.sortedByDescending { it.value }.take(2).forEach { (s, _) ->
            out += sample(searcher, CatalogQuery(udcSection = s, onlyAvailable = true), random, PER_TASTE)
        }
        tastes.searches.take(3).forEach { q ->
            out += searcher.searchFirst(CatalogQuery(text = q, onlyAvailable = true), PER_SEARCH).books.take(PER_SEARCH)
        }
        out += sample(searcher, CatalogQuery(onlyAvailable = true), random, GENERAL)
        return out
    }

    private const val PER_TASTE = 16
    private const val PER_SEARCH = 8
    private const val GENERAL = 40

    /**
     * [n] случайни книги по заявката: от целия резултат, ако търсачката го дава наведнъж
     * (индексът в паметта); иначе — две страници от случайни места (индексът в базата).
     */
    internal suspend fun sample(searcher: CatalogSearcher, q: CatalogQuery, random: Random, n: Int): List<CatalogBook> {
        val first = searcher.searchFirst(q, n / 2)
        if (first.books.size >= first.total) return first.books.shuffled(random).take(n)
        val half = n / 2
        return buildList {
            repeat(2) {
                val offset = random.nextInt(first.total - half + 1)
                addAll(searcher.searchMore(q, offset, half) ?: first.books)
            }
        }
    }
}
