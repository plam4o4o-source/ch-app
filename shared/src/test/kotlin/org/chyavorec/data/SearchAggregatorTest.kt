package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteSearchDoc
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchAggregatorTest {
    @Test fun searchesAcrossAllSources() {
        val engine = CatalogSearchEngine(CatalogSnapshot("", "", "", listOf(CatalogBook(1, "Вазов, Иван", "Под игото")), emptyList()))
        val news = listOf(NewsArticle("n", "Вечер, посветена на Вазов", "u"), NewsArticle("m", "Друго", "u2"))
        val events = listOf(Event("e", "Рецитал Вазов", "2026-10-01", sourceUrl = "u"))
        val page = SiteSearchDoc("page-history", "history", "История", "https://chyavorec.org/history", text = "През 1922 г. в Яворец … Вазов …")
        val newsDoc = SiteSearchDoc("news-x", "news", "Вечер, посветена на Вазов", "u")
        val r = SearchAggregator.search("вазов", engine, news, events, listOf(page, newsDoc))
        assertEquals(1, r.books.size)
        assertEquals(listOf("Вазов, Иван"), r.authors)
        assertEquals(1, r.news.size)
        assertEquals(1, r.events.size)
        assertEquals(1, r.pages.size)
        assertTrue(r.pages[0].snippet.contains("Вазов"))
        assertTrue(SearchAggregator.search("  ", engine, news, events, emptyList()).isEmpty)
    }

    /** Мястото (адресът на читалището при всички прояви) не се търси — „Левски“ не намира всяко събитие. */
    @Test fun eventPlaceIsNotSearched() {
        val place = "НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец"
        val events = listOf(
            Event("a", "Богоявление", "2027-01-06", place = place, sourceUrl = "u"),
            Event("b", "Ден на Левски", "2027-02-19", place = place, sourceUrl = "u"),
        )
        assertEquals(listOf("b"), SearchAggregator.search("левски", null, emptyList(), events, emptyList()).events.map { it.event.id })
    }

    /** Откъсът е от цели думи: без „…частника“, без заглавието отпред, „…“ само където текстът продължава. */
    @Test fun snippetKeepsWholeWords() {
        val text = "Клубове и кръжоци Клубът е създаден за участниците.\n\n  Дигиталният клуб събира младежи всеки ден след училище, " +
            "за да учат програмиране, фотография и работа с компютри в библиотеката на читалището."
        val s = SearchAggregator.snippet(text, "дигитал", radius = 40, lead = listOf("Клубове и кръжоци"))
        // Изречението на съвпадението започва след точката — откъсът тръгва оттам, не от средата на дума.
        assertTrue(s.startsWith("…Дигиталният клуб"), s)
        assertTrue(s.endsWith("…"), s)
        assertTrue(!s.contains("\n") && !s.contains("  "), s)
        val words = s.trim('…').split(' ')
        words.forEach { w -> assertTrue(text.contains(w), "„$w“ не е цяла дума от текста") }
        assertTrue(text.split(Regex("\\s+")).containsAll(words), s)
    }

    @Test fun snippetDropsLeadingTitleAndCategory() {
        val text = "Баба Дамяна разказва Статия 01 · Спомени Спомен от Освобождението — десетгодишно момиче вижда руските войници."
        val s = SearchAggregator.snippet(text, "спомен", lead = listOf("Баба Дамяна разказва", "Статия 01 · Спомени"))
        assertEquals("Спомен от Освобождението — десетгодишно момиче вижда руските войници.", s)
    }

    @Test fun snippetFromStartHasNoLeadingEllipsis() {
        assertEquals("Кратък текст за деня.", SearchAggregator.snippet("Кратък текст   за деня.", "ден"))
        // Без съвпадение (напр. само в заглавието) — началото на текста.
        assertEquals("Кратък текст за деня.", SearchAggregator.snippet("Кратък текст за деня.", "липсва"))
        assertEquals("", SearchAggregator.snippet("   ", "ден"))
    }

    @Test fun snippetPrefersWordStart() {
        // „ден“ е и в „създаден“, но откъсът е около думата, която започва с него.
        val text = "Клубът е създаден отдавна. " + "Много думи тук. ".repeat(12) + "Денят на будителите е празник."
        val s = SearchAggregator.snippet(text, "ден", radius = 30)
        assertTrue(s.startsWith("…Денят на будителите"), s)
    }
}
