package org.chyavorec.data

import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteSearchDoc

/** Резултат от глобалното търсене. */
sealed interface SearchHit {
    val title: String
    data class Book(val book: CatalogBook) : SearchHit { override val title get() = book.title }
    data class News(val article: NewsArticle) : SearchHit { override val title get() = article.title }
    data class EventHit(val event: Event) : SearchHit { override val title get() = event.title }
    data class Page(val doc: SiteSearchDoc, val snippet: String) : SearchHit { override val title get() = doc.title }
}

data class SearchResults(
    val books: List<SearchHit.Book> = emptyList(),
    val authors: List<String> = emptyList(),
    val news: List<SearchHit.News> = emptyList(),
    val events: List<SearchHit.EventHit> = emptyList(),
    val pages: List<SearchHit.Page> = emptyList(),
) {
    val isEmpty get() = books.isEmpty() && authors.isEmpty() && news.isEmpty() && events.isEmpty() && pages.isEmpty()
    val total get() = books.size + authors.size + news.size + events.size + pages.size
}

/**
 * Новините, събитията и документите от сайта с предварително нормализиран текст —
 * подготвя се веднъж ([SearchAggregator.prepare]), а не при всеки натиснат клавиш.
 */
class SearchCorpus internal constructor(
    internal val news: List<Pair<NewsArticle, String>>,
    internal val events: List<Pair<Event, String>>,
    internal val pages: List<Pair<SiteSearchDoc, String>>,
) {
    companion object {
        val EMPTY = SearchCorpus(emptyList(), emptyList(), emptyList())
    }
}

/** Глобално търсене в каталога, новините, събитията и индекса на сайта. */
object SearchAggregator {

    /** Новините и събитията се търсят отделно — от индекса на сайта остават страниците. */
    private val SKIPPED_PAGE_TYPES = setOf("news", "event")

    private fun haystack(vararg fields: String?): String = TextNormalizer.normalize(fields.filterNotNull().joinToString(" "))

    fun prepare(news: List<NewsArticle>, events: List<Event>, siteDocs: List<SiteSearchDoc>): SearchCorpus = SearchCorpus(
        news = news.map { it to haystack(it.title, it.summary, it.category) },
        // Без мястото: то е адресът на читалището при всички прояви — „Левски“ би намерило всяка.
        events = events.map { it to haystack(it.title, it.description) },
        pages = siteDocs.filter { it.type !in SKIPPED_PAGE_TYPES }.map { it to haystack(it.title, it.text, it.category) },
    )

    fun search(
        query: String,
        catalog: CatalogSearchEngine?,
        news: List<NewsArticle>,
        events: List<Event>,
        siteDocs: List<SiteSearchDoc>,
        limitPerType: Int = 20,
    ): SearchResults {
        if (TextNormalizer.tokens(query).isEmpty()) return SearchResults()
        return search(query, catalog, prepare(news, events, siteDocs), limitPerType)
    }

    fun search(query: String, catalog: CatalogSearchEngine?, corpus: SearchCorpus, limitPerType: Int = 20): SearchResults {
        val tokens = TextNormalizer.tokens(query)
        if (tokens.isEmpty()) return SearchResults()
        val books = catalog?.search(CatalogQuery(text = query))?.take(limitPerType).orEmpty()
        return assemble(tokens, books, catalog?.authors(tokens, AUTHORS).orEmpty(), corpus, limitPerType)
    }

    /**
     * Като горното, но каталогът е през [CatalogSearcher] — книгите се търсят както в екрана
     * „Каталог“ (през базата: думите като начало на дума), авторите — както досега. От базата
     * се четат само първите [limitPerType] книги (точкуват се леките записи на всички кандидати).
     */
    suspend fun search(query: String, catalog: CatalogSearcher?, corpus: SearchCorpus, limitPerType: Int = 20): SearchResults {
        val tokens = TextNormalizer.tokens(query)
        if (tokens.isEmpty()) return SearchResults()
        val books = catalog?.searchFirst(CatalogQuery(text = query), limitPerType)?.books?.take(limitPerType).orEmpty()
        return assemble(tokens, books, catalog?.authors(tokens, AUTHORS).orEmpty(), corpus, limitPerType)
    }

    private const val AUTHORS = 8

    private fun assemble(tokens: List<String>, books: List<CatalogBook>, authors: List<String>, corpus: SearchCorpus, limitPerType: Int): SearchResults {
        fun matches(hay: String): Boolean = tokens.all { hay.contains(it) }
        return SearchResults(
            books = books.map { SearchHit.Book(it) },
            authors = authors,
            news = corpus.news.asSequence().filter { matches(it.second) }.take(limitPerType).map { SearchHit.News(it.first) }.toList(),
            events = corpus.events.asSequence().filter { matches(it.second) }.take(limitPerType).map { SearchHit.EventHit(it.first) }.toList(),
            // Страници, архив, публикации и документи от търсещия индекс на сайта.
            pages = corpus.pages.asSequence().filter { matches(it.second) }.take(limitPerType).map { (d, _) ->
                SearchHit.Page(d, snippet(d.text.ifBlank { d.excerpt }, tokens.first(), lead = listOfNotNull(d.title, d.category)))
            }.toList(),
        )
    }

    /**
     * Откъс около първото съвпадение: само цели думи, без нови редове и двойни интервали. Ако
     * текстът започва със заглавието/категорията на страницата ([lead] — те вече са на реда),
     * те се изпускат. Преди съвпадението — до половин [radius] (или от началото на изречението),
     * общо — до 2 × [radius] знака; „…“ само там, където текстът продължава.
     */
    fun snippet(text: String, token: String, radius: Int = 70, lead: List<String> = emptyList()): String {
        val all = WORD.findAll(text).map { it.value }.toList()
        if (all.isEmpty()) return ""
        var words = all
        // Заглавието и категорията — по нормализирани думи (без „·“, тирета и т.н.), до три пъти.
        repeat(3) {
            val cut = lead.maxOfOrNull { leadingWords(words, it) } ?: 0
            if (cut in 1 until words.size) words = words.drop(cut)
        }
        val norm = words.map { TextNormalizer.normalize(it) }
        val hit = norm.indexOfFirst { w -> w.split(' ').any { it.startsWith(token) } }
            .takeIf { it >= 0 } ?: norm.indexOfFirst { it.contains(token) }
        var from = hit.coerceAtLeast(0)
        if (hit > 0) {
            var before = 0
            while (from > 0) {
                val prev = words[from - 1]
                // Началото на изречение е добро начало на откъса.
                if (SENTENCE_END.any { prev.endsWith(it) }) break
                if (before + prev.length + 1 > radius / 2) break
                before += prev.length + 1
                from--
            }
        }
        var to = from
        var length = 0
        while (to < words.size && (to == from || length + words[to].length + 1 <= radius * 2)) {
            length += words[to].length + 1
            to++
        }
        val body = words.subList(from, to).joinToString(" ")
        return buildString {
            if (from > 0) append('…')
            if (to < words.size) append(body.trimEnd(',', ';', ':', '—', '–', '-', '·').trimEnd()).append('…') else append(body)
        }
    }

    /** Колко от първите думи на [words] са точно [phrase] (по нормализирани думи); 0 — ако не започват с нея. */
    private fun leadingWords(words: List<String>, phrase: String): Int {
        val want = TextNormalizer.tokens(phrase)
        if (want.isEmpty()) return 0
        var matched = 0
        var i = 0
        while (i < words.size && matched < want.size) {
            val got = TextNormalizer.tokens(words[i])
            for (t in got) {
                if (matched >= want.size || t != want[matched]) return 0
                matched++
            }
            i++
        }
        if (matched < want.size) return 0
        // И разделителите след фразата („·“, „—“).
        while (i < words.size && TextNormalizer.tokens(words[i]).isEmpty()) i++
        return i
    }

    private val WORD = Regex("\\S+")
    private val SENTENCE_END = listOf(".", "!", "?", "…")
}
