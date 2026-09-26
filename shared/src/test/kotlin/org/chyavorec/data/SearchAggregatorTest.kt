package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.model.TextRun
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchAggregatorTest {
    @Test fun searchesAcrossAllSources() {
        val engine = CatalogSearchEngine(CatalogSnapshot("", "", "", listOf(CatalogBook(1, "Вазов, Иван", "Под игото")), emptyList()))
        val news = listOf(NewsArticle("n", "Вечер, посветена на Вазов", "u"), NewsArticle("m", "Друго", "u2"))
        val events = listOf(Event("e", "Рецитал Вазов", "2026-10-01", sourceUrl = "u"))
        val link = SiteLink("История", "https://chyavorec.org/index/history/0-64", SiteSection.HISTORY)
        val page = SitePage(link.url, "История", listOf(ContentBlock.Paragraph(listOf(TextRun("През 1922 г. в Яворец … Вазов …")))), emptyList())
        val r = SearchAggregator.search("вазов", engine, news, events, listOf(link to page))
        assertEquals(1, r.books.size)
        assertEquals(listOf("Вазов, Иван"), r.authors)
        assertEquals(1, r.news.size)
        assertEquals(1, r.events.size)
        assertEquals(1, r.pages.size)
        assertTrue(r.pages[0].snippet.contains("Вазов"))
        assertTrue(SearchAggregator.search("  ", engine, news, events, emptyList()).isEmpty)
    }
}
