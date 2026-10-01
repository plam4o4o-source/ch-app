package org.chyavorec.data

import org.chyavorec.TestUtil
import org.chyavorec.data.site.ContactsExtractor
import org.chyavorec.data.site.HtmlContentExtractor
import org.chyavorec.data.site.SiteJsonParser
import org.chyavorec.data.site.SiteLinkClassifier
import org.chyavorec.domain.model.ContactPerson
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.DocumentKind
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.model.plainText
import org.jsoup.Jsoup
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Тестове върху РЕАЛНИ файлове от chyavorec.org (plam4o4o-source/site-yavorec). */
class SiteParsingTest {
    private val base = "https://chyavorec.org"
    private val parser = SiteJsonParser(base)
    private val fallback = Contacts("НЧ", null, emptyList(), emptyList(), listOf("fallback@abv.bg"), base, null, emptyList(), "q", false, null)

    @Test fun parsesNewsJsonWithPublicLinksFromRss() {
        val links = parser.parseRssLinks(TestUtil.resource("site-rss.xml"))
        val news = parser.parseNews(TestUtil.resource("site-news.json"), links)
        assertEquals(3, news.size)
        val first = news[0]
        assertTrue(first.title.startsWith("45-ти общински фолклорен събор"))
        assertEquals("https://chyavorec.org/news/45-ti-obshtinski-folkloren-sabor-na-narodnoto-tvorchestvo-ot-timok-do", first.url)
        assertEquals("Състави", first.category)
        assertTrue(first.imageUrl!!.startsWith("https://chyavorec.org/assets/uploads/news/"))
        assertTrue(first.contentBlocks.plainText().contains("Този събор е много повече от културно събитие"))
        assertEquals(LocalDate.of(2026, 8, 30).atStartOfDay(java.time.ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli(), first.publishedAtMillis)
        // подредени от най-новата
        assertTrue(news.zipWithNext().all { (a, b) -> (a.publishedAtMillis ?: 0) >= (b.publishedAtMillis ?: 0) })
    }

    @Test fun newsWithoutRssFallsBackToNewsPage() {
        val news = parser.parseNews(TestUtil.resource("site-news.json"), emptyMap())
        assertEquals("https://chyavorec.org/news", news[0].url)
    }

    @Test fun annualCalendarRollsToNextOccurrence() {
        val events = parser.parseEvents(TestUtil.resource("site-index.json"), LocalDate.of(2026, 9, 26))
        assertEquals(8, events.size)
        val spin = events.first()
        // Декемврийските са още тази година, януарските — догодина.
        assertEquals("Световен ден за борба срещу СПИН", spin.title)
        assertEquals("2026-12-01", spin.date)
        assertEquals("Читалище", spin.category)
        val bogoyavlenie = events.first { it.title == "Богоявление" }
        assertEquals("2027-01-06", bogoyavlenie.date)
        assertEquals("Народен обичай", bogoyavlenie.category)
        assertTrue(bogoyavlenie.recurring)
        assertEquals("https://chyavorec.org/events", bogoyavlenie.sourceUrl)
    }

    @Test fun nextOccurrenceOnTheSameDayIsToday() {
        assertEquals(LocalDate.of(2026, 1, 6), parser.nextOccurrence(1, 6, LocalDate.of(2026, 1, 6)))
        assertEquals(LocalDate.of(2028, 2, 29), parser.nextOccurrence(2, 29, LocalDate.of(2027, 3, 1)))
        assertEquals(LocalDate.of(2027, 2, 28), parser.nextOccurrence(2, 29, LocalDate.of(2026, 3, 1)))
    }

    @Test fun searchIndexAndPublications() {
        val docs = parser.parseIndex(TestUtil.resource("site-index.json"))
        assertEquals(21, docs.size)
        val about = docs.first { it.id == "page-about" }
        assertEquals("https://chyavorec.org/about", about.url)
        val pubs = parser.publications(docs)
        assertEquals(2, pubs.size)
        assertEquals("https://chyavorec.org/vestnik/baba_damjana_razkazva.pdf", pubs[0].url)
        assertEquals(DocumentKind.PUBLICATION, pubs[0].kind)
    }

    @Test fun filesJson() {
        val files = parser.parseFiles(TestUtil.resource("site-files.json"))
        assertEquals(3, files.size)
        val report = files.first { it.title == "Отчет за дейност 2025" }
        assertEquals("https://chyavorec.org/assets/uploads/files/f-msupekcxhslk-5_Otchet-D-2025.pdf", report.url)
        assertEquals("Годишни отчети", report.category)
        assertEquals("393.1 KB", report.size)
    }

    @Test fun extractsRealPageContent() {
        val doc = Jsoup.parse(TestUtil.resource("site-about.html"), base)
        val ex = HtmlContentExtractor(base)
        assertEquals("За нас", ex.title(doc))
        val blocks = ex.extract(doc)
        val text = blocks.plainText()
        assertTrue(text.contains("обществена организация с нестопанска цел"))
        assertTrue(text.contains("1922"), "броячите получават стойността от data-target")
        assertTrue(text.contains("7200"))
        assertTrue(text.contains("Пламен Христов"))
        assertFalse(text.contains("function("), "скриптовете не са съдържание")
        assertFalse(text.contains("Начало\nНовини"), "навигацията не е съдържание")
        assertTrue(blocks.none { it is ContentBlock.Image && (it.url.contains("logo-256") || it.url.endsWith("/x")) })
    }

    @Test fun extractsRealContacts() {
        val doc = Jsoup.parse(TestUtil.resource("site-kontakti.html"), base)
        val c = ContactsExtractor(base, fallback).extract(doc, "$base/kontakti")
        assertTrue(c.fromSite)
        assertEquals("пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334", c.address)
        assertEquals(listOf("chitalishte_yavorec@abv.bg"), c.emails)
        assertTrue(c.phones.isEmpty(), "сайтът не публикува телефон — не се измисля")
        assertEquals(listOf(ContactPerson("Председател", "Пламен Христов"), ContactPerson("Секретар", "Даниела Цвяткова")), c.persons)
        assertEquals("Понеделник: 08:00 – 16:30", c.workingHours.first())
        assertTrue(c.workingHours.contains("Петък: 08:00 – 16:00"))
        assertTrue(c.workingHours.contains("Неделя: Почивен ден"))
        assertEquals(7, c.workingHours.size)
    }

    @Test fun contactsFallbackWhenNothingFound() {
        val doc = Jsoup.parse("<div id='hp'><div class='hp-inner'><p>Страницата се обновява. Моля, заповядайте отново по-късно.</p></div></div>", base)
        assertEquals(fallback, ContactsExtractor(base, fallback).extract(doc, "x"))
    }

    @Test fun classifiesSiteSections() {
        assertEquals(SiteSection.ABOUT, SiteLinkClassifier.link("За нас", "/about", base).kind)
        assertEquals(SiteSection.EXHIBITION, SiteLinkClassifier.link("Изложба", "/fotodokumentalna-izlozhba", base).kind)
        assertEquals(SiteSection.ENSEMBLE, SiteLinkClassifier.link("Ансамбъл", "/yavorski-hlapeta", base).kind)
        assertEquals(SiteSection.NEWS, SiteLinkClassifier.link("Новина", "/news/abc", base).kind)
        assertEquals(SiteSection.DIGITAL_CLUB, SiteLinkClassifier.link("Дигитален клуб", "/digital", base).kind)
        assertEquals("https://chyavorec.org/library", SiteLinkClassifier.link("Библиотека", "/library", base).url)
    }
}
