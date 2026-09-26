package org.chyavorec.data

import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.plainText

/** Резултат от глобалното търсене. */
sealed interface SearchHit {
    val title: String
    data class Book(val book: CatalogBook) : SearchHit { override val title get() = book.title }
    data class News(val article: NewsArticle) : SearchHit { override val title get() = article.title }
    data class EventHit(val event: Event) : SearchHit { override val title get() = event.title }
    data class Page(val link: SiteLink, val snippet: String) : SearchHit { override val title get() = link.title }
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

/** Глобално търсене в каталога, новините, събитията и страниците на сайта. */
object SearchAggregator {

    fun search(
        query: String,
        catalog: CatalogSearchEngine?,
        news: List<NewsArticle>,
        events: List<Event>,
        pages: List<Pair<SiteLink, SitePage?>>,
        limitPerType: Int = 20,
    ): SearchResults {
        val tokens = TextNormalizer.tokens(query)
        if (tokens.isEmpty()) return SearchResults()
        fun matches(vararg fields: String?): Boolean {
            val hay = TextNormalizer.normalize(fields.filterNotNull().joinToString(" "))
            return tokens.all { hay.contains(it) }
        }
        val books = catalog?.search(CatalogQuery(text = query))?.take(limitPerType).orEmpty()
        val authors = catalog?.snapshot?.books?.asSequence()
            ?.map { it.author }?.filter { it.isNotBlank() && matches(it) }?.distinct()?.take(8)?.toList().orEmpty()
        return SearchResults(
            books = books.map { SearchHit.Book(it) },
            authors = authors,
            news = news.filter { matches(it.title, it.summary, it.category) }.take(limitPerType).map { SearchHit.News(it) },
            events = events.filter { matches(it.title, it.description, it.place) }.take(limitPerType).map { SearchHit.EventHit(it) },
            pages = pages.mapNotNull { (link, page) ->
                val text = page?.blocks?.plainText().orEmpty()
                if (!matches(link.title, text)) return@mapNotNull null
                SearchHit.Page(link, snippet(text, tokens.first()))
            }.take(limitPerType),
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
