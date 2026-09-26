package org.chyavorec.data

import org.chyavorec.TestUtil
import org.chyavorec.data.site.ContactsExtractor
import org.chyavorec.data.site.EventExtractor
import org.chyavorec.data.site.HtmlContentExtractor
import org.chyavorec.data.site.SiteLinkClassifier
import org.chyavorec.data.site.UcozRssParser
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.EventSource
import org.chyavorec.domain.model.SiteSection
import org.jsoup.Jsoup
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SiteParsingTest {
    private val base = "https://chyavorec.org"
    private val fallback = Contacts("НЧ", null, emptyList(), listOf("fallback@abv.bg"), base, null, emptyList(), "q", false, null)

    @Test fun parsesUcozNewsRss() {
        val news = UcozRssParser(base).parseNews(TestUtil.resource("news-rss.xml"))
        assertEquals(2, news.size)
        val first = news[0]
        assertEquals("Покана за концерт по случай 1 ноември", first.title)
        assertEquals("https://chyavorec.org/news/pokana_za_koncert/2026-10-20-15", first.url)
        assertEquals("https://chyavorec.org/_nw/1/12345.jpg", first.imageUrl)
        assertEquals("Събития", first.category)
        assertEquals("admin", first.author)
        assertTrue(first.summary.startsWith("Каним ви"))
        assertTrue(first.contentBlocks.isNotEmpty())
        assertEquals(1792487700000L, first.publishedAtMillis)
        assertEquals("Нови книги в библиотеката & дарения", news[1].title)
        assertNull(news[1].imageUrl)
    }

    @Test fun parsesUcozPhotoRss() {
        val photos = UcozRssParser(base).parsePhotos(TestUtil.resource("photo-rss.xml"))
        assertEquals(2, photos.size)
        assertEquals("https://chyavorec.org/_ph/1/2/123456.jpg", photos[0].thumbUrl)
        assertEquals("https://chyavorec.org/_ph/1/123456.jpg", photos[0].fullUrl)
        assertEquals("Празници", photos[0].album)
        assertEquals("https://chyavorec.org/_ph/2/555.jpg", photos[1].fullUrl)
    }

    @Test fun extractsPageEditorContent() {
        val doc = Jsoup.parse(TestUtil.resource("page-library.html"), base)
        val ex = HtmlContentExtractor(base)
        val blocks = ex.extract(doc)
        val text = blocks.joinToString("|") { it.toString() }
        assertFalse(text.contains("скрита"), "#hp трябва да е изключен")
        assertFalse(text.contains("classList"), "скриптовете трябва да са изключени")
        assertFalse(text.contains("Начало"), "навигацията трябва да е изключена")
        assertEquals("Нашата библиотека", (blocks.first { it is ContentBlock.Heading } as ContentBlock.Heading).text)
        val list = blocks.filterIsInstance<ContentBlock.BulletList>().single()
        assertEquals(2, list.items.size)
        assertTrue(list.items[1].any { it.bold && it.text == "периодика" })
        val link = blocks.filterIsInstance<ContentBlock.Paragraph>().flatMap { it.runs }.first { it.url != null }
        assertEquals("https://chyavorec.org/index/elektronen_katalog/0-70", link.url)
        val images = blocks.filterIsInstance<ContentBlock.Image>()
        assertEquals(listOf("https://chyavorec.org/images/library.jpg"), images.map { it.url })
        assertEquals("https://chyavorec.org/files/pravilnik.pdf", blocks.filterIsInstance<ContentBlock.LinkButton>().single().url)
        assertEquals("Книгата е прозорец към света.", blocks.filterIsInstance<ContentBlock.Quote>().single().text)
        assertEquals("Нашата библиотека", ex.title(doc))
    }

    @Test fun extractsContacts() {
        val doc = Jsoup.parse(TestUtil.resource("page-contacts.html"), base)
        val c = ContactsExtractor(base, fallback).extract(doc, "$base/index/kontakti/0-65")
        assertTrue(c.fromSite)
        assertEquals("пл. „Девети септември“ № 3, с. Яворец, общ. Габрово", c.address)
        assertEquals(listOf("+359897284168", "0897 284 168"), c.phones)
        assertEquals(listOf("chitalishte_yavorets@abv.bg"), c.emails)
        assertEquals(listOf("Понеделник – Петък: 09:00 – 16:30 ч."), c.workingHours)
        assertEquals("https://www.facebook.com/nchvasillevski1922/", c.facebook)
    }

    @Test fun contactsFallbackWhenNothingFound() {
        val doc = Jsoup.parse("<div id='mc'><p>Страницата се обновява. Моля, заповядайте отново по-късно за повече информация.</p></div>", base)
        assertEquals(fallback, ContactsExtractor(base, fallback).extract(doc, "x"))
    }

    @Test fun eventsFromNews() {
        val news = UcozRssParser(base).parseNews(TestUtil.resource("news-rss.xml"))
        val events = EventExtractor().fromNews(news)
        assertEquals(1, events.size)
        val e = events.single()
        assertEquals("2026-11-01", e.date)
        assertEquals("17:30", e.time)
        assertEquals("салона на читалището", e.place)
        assertEquals("НЧ „Васил Левски – 1922“", e.organizer)
        assertTrue(e.dateIsExplicit)
    }

    @Test fun eventsFromEventsPage() {
        val doc = Jsoup.parse(TestUtil.resource("page-events.html"), base)
        val ex = HtmlContentExtractor(base)
        val events = EventExtractor().fromEventsPage(ex.findContainer(doc), "$base/index/events/0-58", LocalDate.of(2026, 9, 26), ex)
        assertEquals(listOf("Коледен концерт", "Четене за деца"), events.map { it.title })
        assertEquals("2026-12-20", events[0].date)
        assertEquals("18:00", events[0].time)
        assertEquals("салона на читалището", events[0].place)
        assertEquals("10:30", events[1].time)
        assertTrue(events.all { it.source == EventSource.EVENTS_PAGE })
    }

    @Test fun mergeDeduplicatesAndSorts() {
        val ex = EventExtractor()
        val a = ex.fromNews(UcozRssParser(base).parseNews(TestUtil.resource("news-rss.xml")))
        val merged = ex.merge(a, a)
        assertEquals(1, merged.size)
    }

    @Test fun discoversNavigation() {
        val links = SiteLinkClassifier.discover(Jsoup.parse(TestUtil.resource("home.html"), base), base)
        val kinds = links.associate { it.title to it.kind }
        assertEquals(SiteSection.DIGITAL_CLUB, kinds["Дигитален клуб"])
        assertEquals(SiteSection.EVENTS, kinds["Събития"])
        assertEquals(SiteSection.CONTACTS, kinds["Свържете се"])
        assertEquals(SiteSection.PRIVACY, kinds["Политика за поверителност"])
        assertEquals(SiteSection.GALLERY, kinds["Галерия"])
        assertFalse(kinds.containsKey("Регистрация"))
        assertFalse(kinds.containsKey("Facebook"))
    }
}
