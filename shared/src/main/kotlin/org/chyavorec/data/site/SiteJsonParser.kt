package org.chyavorec.data.site

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.chyavorec.core.BulgarianDates
import org.chyavorec.core.Urls
import org.chyavorec.domain.model.DocumentKind
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.EventSource
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteSearchDoc
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.time.LocalDate
import java.time.ZoneId

/**
 * Разчита структурираните данни на chyavorec.org (статичен сайт във Vercel):
 * - `/data/news.json` — новините (заглавие, дата, категория, резюме, HTML тяло, корица, етикети);
 * - `/rss.xml` — публичните адреси на новините (`/news/<slug>`);
 * - `/javora/index.json` — търсещият индекс на сайта: страници, събития от календара,
 *   архивни експонати, исторически публикации и документи;
 * - `/data/files.json` — документите (устав, отчети, декларации).
 * Всички парсери са толерантни към липсващи/допълнителни полета.
 */
class SiteJsonParser(private val baseUrl: String) {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true }
    private val extractor = HtmlContentExtractor(baseUrl)

    @Serializable
    private data class NewsDto(
        val id: String = "",
        val title: String = "",
        val date: String = "",
        val category: String? = null,
        val excerpt: String = "",
        val content: String = "",
        val cover: String? = null,
        val tags: List<String> = emptyList(),
        val author: String? = null,
    )

    @Serializable
    private data class IndexDto(val generated: String = "", val docs: List<DocDto> = emptyList())

    @Serializable
    private data class DocDto(
        val id: String = "",
        val type: String = "",
        val title: String = "",
        val url: String = "",
        val excerpt: String = "",
        val text: String = "",
        val date: String? = null,
        val category: String? = null,
        val fileUrl: String? = null,
        val day: String? = null,
        val month: String? = null,
        val monthNum: String? = null,
        val badge: String? = null,
        val cover: String? = null,
        val size: String? = null,
    )

    @Serializable
    private data class FileDto(
        val id: String = "",
        val title: String = "",
        val date: String? = null,
        val category: String? = null,
        val size: String? = null,
        val url: String = "",
        val description: String = "",
    )

    private fun abs(url: String?): String? = Urls.absolutize(url, baseUrl)

    /** Заглавие → публичен адрес на новината (от rss.xml). */
    fun parseRssLinks(xml: String): Map<String, String> =
        Jsoup.parse(xml, baseUrl, Parser.xmlParser()).select("item").mapNotNull { item ->
            val title = item.selectFirst("title")?.text()?.trim().orEmpty()
            val link = item.selectFirst("link")?.text()?.trim().orEmpty()
            if (title.isEmpty() || link.isEmpty()) null else normalizeTitle(title) to (abs(link) ?: link)
        }.toMap()

    private fun normalizeTitle(t: String) = t.replace(Regex("\\s+"), " ").trim()

    fun parseNews(text: String, links: Map<String, String>): List<NewsArticle> =
        json.decodeFromString(ListSerializer(NewsDto.serializer()), text)
            .filter { it.title.isNotBlank() }
            .map { n ->
                val body = Jsoup.parseBodyFragment(n.content, baseUrl).body()
                NewsArticle(
                    id = n.id.ifBlank { n.title },
                    title = n.title.trim(),
                    url = links[normalizeTitle(n.title)] ?: (baseUrl.trimEnd('/') + "/news"),
                    publishedAtMillis = parseDate(n.date),
                    summary = n.excerpt.trim().ifBlank { body.text().take(300) },
                    imageUrl = abs(n.cover),
                    category = n.category?.trim()?.ifBlank { null },
                    author = n.author?.ifBlank { null },
                    contentBlocks = extractor.extract(body),
                )
            }
            .sortedByDescending { it.publishedAtMillis ?: 0 }

    private fun parseDate(iso: String): Long? = runCatching {
        LocalDate.parse(iso.take(10)).atStartOfDay(ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli()
    }.getOrNull()

    fun parseIndex(text: String): List<SiteSearchDoc> =
        json.decodeFromString(IndexDto.serializer(), text).docs.filter { it.title.isNotBlank() }.map { d ->
            SiteSearchDoc(
                id = d.id, type = d.type, title = d.title.trim(),
                url = abs(d.url) ?: d.url, excerpt = d.excerpt, text = d.text,
                date = d.date ?: d.monthNum?.let { m -> d.day?.let { "--$m-$it" } },
                category = d.category ?: d.badge, fileUrl = abs(d.fileUrl),
            )
        }

    /**
     * Събитията от годишния календар на сайта (`type = "event"`, ден + месец).
     * Датата е следващото настъпване спрямо [today] — календарът се повтаря всяка година.
     */
    fun parseEvents(text: String, today: LocalDate): List<Event> =
        json.decodeFromString(IndexDto.serializer(), text).docs.filter { it.type == "event" }.mapNotNull { d ->
            val day = d.day?.trim()?.toIntOrNull() ?: return@mapNotNull null
            val month = d.monthNum?.trim()?.toIntOrNull() ?: d.month?.let { BulgarianDates.monthIndex(it) } ?: return@mapNotNull null
            val date = nextOccurrence(month, day, today) ?: return@mapNotNull null
            Event(
                id = d.id,
                title = d.title.trim(),
                date = date.toString(),
                description = d.excerpt.trim(),
                imageUrl = abs(d.cover),
                sourceUrl = abs(d.url) ?: (baseUrl.trimEnd('/') + "/events"),
                category = d.badge?.trim()?.ifBlank { null },
                dateIsExplicit = true,
                recurring = true,
                source = EventSource.CALENDAR,
            )
        }.sortedBy { it.date }

    fun nextOccurrence(month: Int, day: Int, today: LocalDate): LocalDate? {
        val thisYear = runCatching { LocalDate.of(today.year, month, day) }.getOrNull()
            ?: runCatching { LocalDate.of(today.year, month, 28) }.getOrNull() ?: return null
        return if (thisYear.isBefore(today)) runCatching { LocalDate.of(today.year + 1, month, day) }
            .getOrElse { LocalDate.of(today.year + 1, month, 28) } else thisYear
    }

    fun parseFiles(text: String): List<SiteDocument> =
        json.decodeFromString(ListSerializer(FileDto.serializer()), text).filter { it.url.isNotBlank() }.map { f ->
            SiteDocument(
                id = f.id, title = f.title.trim(), url = abs(f.url) ?: f.url, date = f.date,
                category = f.category, size = f.size, description = f.description.trim(), kind = DocumentKind.DOCUMENT,
            )
        }

    /** Историческите публикации (PDF статии от юбилейния вестник) от индекса. */
    fun publications(docs: List<SiteSearchDoc>): List<SiteDocument> =
        docs.filter { it.type == "publication" && it.fileUrl != null }.map { d ->
            SiteDocument(
                id = d.id, title = d.title, url = d.fileUrl!!, category = d.category,
                description = d.excerpt, kind = DocumentKind.PUBLICATION,
            )
        }
}
