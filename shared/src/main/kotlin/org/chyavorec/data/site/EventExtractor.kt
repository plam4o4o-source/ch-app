package org.chyavorec.data.site

import org.chyavorec.core.BulgarianDates
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.EventSource
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.plainText
import org.jsoup.nodes.Element
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Извлича събития от публикуваното на сайта съдържание. Сайтът няма
 * структуриран календар (JSON/iCal), затова:
 * 1. новина в категория „Събития“ (или подобна) → събитие;
 * 2. новина със „събитийна“ дума в заглавието И изрична дата в текста → събитие;
 * 3. блокове от страницата „Събития“, които имат заглавие и дата → събитие.
 * Нищо не се допълва „на сляпо“: без дата в текста се показва датата на
 * публикуване, отбелязана с [Event.dateIsExplicit] = false.
 */
class EventExtractor(private val zone: ZoneId = ZoneId.of("Europe/Sofia")) {

    private val eventCategoryWords = listOf("събити", "афиш", "покан", "календар", "предстоящ")
    private val eventTitleWords = listOf(
        "покана", "концерт", "представлени", "празник", "тържеств", "изложб", "среща", "събор",
        "вечер", "честване", "премиер", "фестивал", "конкурс", "работилниц", "спектакъл", "четене", "беседа",
    )
    private val placeRegex = Regex("(?:Място|Къде|Адрес)\\s*:\\s*([^\\n]{3,120})", RegexOption.IGNORE_CASE)
    private val placeInline = Regex(
        "\\b(?:в|във|пред)\\s+((?:салона|залата|читалището|библиотеката|двора|центъра|площада|храма|църквата|училището)[^.,;\\n]{0,60})",
        RegexOption.IGNORE_CASE,
    )
    private val organizerRegex = Regex("Организатор(?:и)?\\s*:\\s*([^\\n]{3,120})", RegexOption.IGNORE_CASE)

    fun fromNews(news: List<NewsArticle>): List<Event> = news.mapNotNull { fromArticle(it) }

    fun fromArticle(a: NewsArticle): Event? {
        val category = TextNormalizer.normalize(a.category)
        val title = TextNormalizer.normalize(a.title)
        val inEventCategory = eventCategoryWords.any { category.contains(it) }
        val eventLikeTitle = eventTitleWords.any { title.contains(it) }
        if (!inEventCategory && !eventLikeTitle) return null
        val body = buildString {
            append(a.title).append('\n').append(a.summary).append('\n')
            append(a.contentBlocks.plainText())
        }
        val pubDate = a.publishedAtMillis?.let { Instant.ofEpochMilli(it).atZone(zone).toLocalDate() }
        val refYear = pubDate?.year ?: LocalDate.now(zone).year
        var date = BulgarianDates.findDate(body, refYear)
        // Дата без година, която се оказва много преди публикацията → следващата година.
        if (date != null && pubDate != null && date.isBefore(pubDate.minusMonths(6)) && !Regex("\\d{4}").containsMatchIn(body)) {
            date = date.plusYears(1)
        }
        if (date == null && !inEventCategory) return null
        val finalDate = date ?: pubDate
        return Event(
            id = "news:" + a.id,
            title = a.title,
            date = finalDate?.toString(),
            time = BulgarianDates.findTime(body)?.toString(),
            place = findPlace(body),
            organizer = organizerRegex.find(body)?.groupValues?.get(1)?.trim()?.trimEnd('.'),
            description = a.summary.ifBlank { a.contentBlocks.plainText().take(400) },
            imageUrl = a.imageUrl,
            sourceUrl = a.url,
            category = a.category,
            dateIsExplicit = date != null,
            source = EventSource.NEWS,
        )
    }

    private fun findPlace(text: String): String? =
        placeRegex.find(text)?.groupValues?.get(1)?.trim()?.trimEnd('.')
            ?: placeInline.find(text)?.groupValues?.get(1)?.trim()?.let { "в $it" }

    /** Събития от страницата „Събития“: най-вътрешните блокове със заглавие и дата. */
    fun fromEventsPage(container: Element, pageUrl: String, today: LocalDate, extractor: HtmlContentExtractor): List<Event> {
        val candidates = container.select("article, li, div, section, tr").filter { el -> isEventBlock(el, today.year) }
        val innermost = candidates.filter { el -> candidates.none { other -> other !== el && other.parents().contains(el) } }
        return innermost.mapIndexedNotNull { index, el ->
            val text = el.wholeText().replace(Regex("[ \\t]+"), " ")
            val date = BulgarianDates.findDate(text, today.year) ?: return@mapIndexedNotNull null
            val heading = el.selectFirst("h1, h2, h3, h4, h5, strong, b")?.text()?.trim().orEmpty()
            if (heading.isBlank()) return@mapIndexedNotNull null
            val blocks = extractor.extract(el)
            Event(
                id = "page:$index:" + TextNormalizer.normalize(heading).take(40) + ":" + date,
                title = heading,
                date = date.toString(),
                time = BulgarianDates.findTime(text)?.toString(),
                place = findPlace(text),
                organizer = organizerRegex.find(text)?.groupValues?.get(1)?.trim(),
                description = blocks.filterNot { it is ContentBlock.Heading && it.text == heading }.plainText().trim().take(500),
                imageUrl = el.selectFirst("img")?.let { extractor.imageUrl(it) },
                sourceUrl = pageUrl,
                dateIsExplicit = true,
                source = EventSource.EVENTS_PAGE,
            )
        }
    }

    private fun isEventBlock(el: Element, year: Int): Boolean {
        val len = el.text().length
        if (len < 12 || len > 1500) return false
        if (el.selectFirst("h1, h2, h3, h4, h5, strong, b") == null) return false
        return BulgarianDates.findDate(el.text(), year) != null
    }

    /** Обединява събития от различни източници и маха повторенията. */
    fun merge(vararg lists: List<Event>): List<Event> {
        val seen = HashSet<String>()
        return lists.flatMap { it }.filter { e ->
            seen.add(TextNormalizer.normalize(e.title).take(50) + "|" + e.date)
        }.sortedWith(compareBy<Event> { it.date ?: "9999" }.thenBy { it.time ?: "" })
    }
}
