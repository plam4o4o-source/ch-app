package org.chyavorec.data

import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.CatalogSearchEngine
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
        events = events.map { it to haystack(it.title, it.description, it.place) },
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
        fun matches(hay: String): Boolean = tokens.all { hay.contains(it) }
        val books = catalog?.search(CatalogQuery(text = query))?.take(limitPerType).orEmpty()
        val authors = catalog?.authorIndex?.asSequence()
            ?.filter { (_, normalized) -> matches(normalized) }?.map { it.first }?.take(8)?.toList().orEmpty()
        return SearchResults(
            books = books.map { SearchHit.Book(it) },
            authors = authors,
            news = corpus.news.asSequence().filter { matches(it.second) }.take(limitPerType).map { SearchHit.News(it.first) }.toList(),
            events = corpus.events.asSequence().filter { matches(it.second) }.take(limitPerType).map { SearchHit.EventHit(it.first) }.toList(),
            // Страници, архив, публикации и документи от търсещия индекс на сайта.
            pages = corpus.pages.asSequence().filter { matches(it.second) }.take(limitPerType).map { (d, _) ->
                SearchHit.Page(d, snippet(d.text.ifBlank { d.excerpt }, tokens.first()))
            }.toList(),
        )
    }

    fun snippet(text: String, token: String, radius: Int = 70): String {
        val norm = TextNormalizer.normalize(text)
        val idx = norm.indexOf(token)
        if (idx < 0) return text.take(radius * 2).trim()
        // Нормализираният текст е почти със същата дължина — достатъчно за откъс.
        val start = (idx - radius).coerceAtLeast(0)
        val end = (idx + radius).coerceAtMost(text.length)
        return (if (start > 0) "…" else "") + text.substring(start.coerceAtMost(text.length), end).replace('\n', ' ').trim() +
            (if (end < text.length) "…" else "")
    }
}
