package org.chyavorec.data.site

import org.chyavorec.core.Urls
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Разчита RSS 2.0 емисиите, които uCoz генерира автоматично за модулите
 * „Новини на сайта“ (`/news/rss/`) и „Фотоалбуми“ (`/photo/rss/`).
 */
class UcozRssParser(private val baseUrl: String) {

    private val extractor = HtmlContentExtractor(baseUrl)

    fun parseNews(xml: String): List<NewsArticle> = items(xml).mapNotNull { item ->
        val title = item.childText("title")?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
        val link = Urls.absolutize(item.childText("link"), baseUrl) ?: return@mapNotNull null
        val descriptionHtml = item.childText("description").orEmpty()
        val contentHtml = item.childTextNs("content:encoded")
        val descDoc = Jsoup.parseBodyFragment(descriptionHtml, baseUrl)
        val contentDoc = contentHtml?.let { Jsoup.parseBodyFragment(it, baseUrl) }
        val image = item.enclosureImage()
            ?: (contentDoc?.body()?.let { extractor.images(it).firstOrNull() })
            ?: extractor.images(descDoc.body()).firstOrNull()
        val summary = descDoc.body().text().trim().let { if (it.length > 400) it.take(397).trimEnd() + "…" else it }
        NewsArticle(
            id = item.childText("guid")?.takeIf { it.isNotBlank() } ?: link,
            title = Parser.unescapeEntities(title, false).trim(),
            url = link,
            publishedAtMillis = parseRfc822(item.childText("pubDate")),
            summary = summary,
            imageUrl = image,
            category = item.childText("category")?.trim()?.ifBlank { null },
            author = (item.childText("author") ?: item.childTextNs("dc:creator"))?.trim()?.ifBlank { null },
            contentBlocks = contentDoc?.body()?.let { extractor.extract(it) }.orEmpty(),
        )
    }

    fun parsePhotos(xml: String): List<GalleryPhoto> = items(xml).mapNotNull { item ->
        val link = Urls.absolutize(item.childText("link"), baseUrl) ?: return@mapNotNull null
        val descDoc = Jsoup.parseBodyFragment(item.childText("description").orEmpty(), baseUrl)
        val thumb = item.enclosureImage() ?: extractor.images(descDoc.body()).firstOrNull() ?: return@mapNotNull null
        GalleryPhoto(
            id = item.childText("guid")?.takeIf { it.isNotBlank() } ?: link,
            title = item.childText("title")?.let { Parser.unescapeEntities(it, false).trim() }.orEmpty(),
            thumbUrl = thumb,
            fullUrl = fullSizePhoto(thumb),
            album = item.childText("category")?.trim()?.ifBlank { null } ?: "Снимки",
            pageUrl = link,
            publishedAtMillis = parseRfc822(item.childText("pubDate")),
        )
    }

    private fun items(xml: String): List<Element> =
        Jsoup.parse(xml, baseUrl, Parser.xmlParser()).select("item")

    private fun Element.childText(name: String): String? =
        children().firstOrNull { it.tagName().equals(name, ignoreCase = true) }?.text()

    private fun Element.childTextNs(name: String): String? =
        children().firstOrNull { it.tagName().equals(name, ignoreCase = true) }?.let { it.wholeText().ifBlank { it.text() } }

    private fun Element.enclosureImage(): String? {
        val enc = children().firstOrNull {
            (it.tagName() == "enclosure" && it.attr("type").startsWith("image")) || it.tagName() == "media:content" || it.tagName() == "media:thumbnail"
        } ?: return null
        return Urls.absolutize(enc.attr("url"), baseUrl)
    }

    companion object {
        private val rfc822 = DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.ENGLISH)

        fun parseRfc822(value: String?): Long? {
            if (value.isNullOrBlank()) return null
            return runCatching { ZonedDateTime.parse(value.trim(), rfc822).toInstant().toEpochMilli() }.getOrNull()
                ?: runCatching { ZonedDateTime.parse(value.trim()).toInstant().toEpochMilli() }.getOrNull()
        }

        /**
         * uCoz пази миниатюрата на снимка от фотоалбума в подпапка `/2/`
         * (напр. `/_ph/1/2/123456.jpg`), а оригинала — без нея (`/_ph/1/123456.jpg`).
         * Ако адресът не следва този шаблон, се връща непроменен.
         */
        fun fullSizePhoto(thumb: String): String =
            Regex("(/_ph/\\d+)/2/").replace(thumb) { it.groupValues[1] + "/" }
    }
}
