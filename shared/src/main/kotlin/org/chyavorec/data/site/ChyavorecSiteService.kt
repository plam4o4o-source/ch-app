package org.chyavorec.data.site

import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpBody
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.service.EventsService
import org.chyavorec.domain.service.NewsService
import org.chyavorec.domain.service.SiteContentService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayInputStream

/**
 * Интеграция с chyavorec.org (платформа uCoz).
 *
 * Източници по стабилност (виж ANALYSIS.md):
 * 1. RSS емисии, генерирани от uCoz — `/news/rss/` и `/photo/rss/` (структурирани данни);
 * 2. страниците от Page Editor — контролирано извличане на блокове ([HtmlContentExtractor]).
 * uCoz има и платен uAPI (OAuth), но той не е публичен — не се използва.
 */
class ChyavorecSiteService(
    private val http: HttpFetcher,
    private val baseUrl: String,
    private val clock: AppClock,
    private val fallbackContacts: Contacts,
) : NewsService, EventsService, SiteContentService {

    private val rss = UcozRssParser(baseUrl)
    private val extractor = HtmlContentExtractor(baseUrl)
    private val events = EventExtractor(clock.zone())

    private fun url(path: String) = baseUrl.trimEnd('/') + path

    private fun HttpBody.html(): Document =
        Jsoup.parse(ByteArrayInputStream(bytes), charset, finalUrl)

    /** Текстът на XML емисия: кодировка от заглавката или от XML декларацията. */
    private fun HttpBody.xml(): String {
        val declared = charset ?: Regex("encoding=[\"']([A-Za-z0-9_-]+)[\"']")
            .find(String(bytes, 0, minOf(bytes.size, 200), Charsets.ISO_8859_1))?.groupValues?.get(1)
        val cs = declared?.let { runCatching { charset(it) }.getOrNull() } ?: Charsets.UTF_8
        return String(bytes, cs)
    }

    override suspend fun fetchLatest(): Outcome<List<NewsArticle>> =
        when (val r = http.get(url("/news/rss/"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> runCatching { rss.parseNews(r.value.xml()) }
                .fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse("news rss")) })
        }

    override suspend fun fetchArticle(article: NewsArticle): Outcome<ArticleDetail> {
        return when (val r = http.get(article.url)) {
            is Outcome.Failure -> {
                // Ако страницата не се зарежда, но RSS е дал пълния текст — показваме него.
                if (article.contentBlocks.isNotEmpty()) Outcome.Success(detailFromRss(article)) else r
            }
            is Outcome.Success -> runCatching {
                val doc = r.value.html()
                val container = extractor.findContainer(doc)
                var blocks = extractor.extract(container)
                if (blocks.isEmpty()) blocks = article.contentBlocks
                // Заглавието вече се показва отгоре.
                blocks = blocks.dropWhile { it is ContentBlock.Heading && it.text.trim() == article.title.trim() }
                val images = (listOfNotNull(article.imageUrl) + extractor.images(container)).distinct()
                ArticleDetail(article, blocks, images)
            }.fold({ Outcome.Success(it) }, { Outcome.Success(detailFromRss(article)) })
        }
    }

    private fun detailFromRss(a: NewsArticle) = ArticleDetail(
        a, a.contentBlocks,
        (listOfNotNull(a.imageUrl) + a.contentBlocks.filterIsInstance<ContentBlock.Image>().map { it.url }).distinct(),
    )

    override suspend fun fetchEvents(news: List<NewsArticle>): Outcome<List<Event>> {
        val fromNews = events.fromNews(news)
        val pageUrl = url(SiteLinkClassifier.knownPaths.getValue(SiteSection.EVENTS))
        val eventsPageUrl = (discoverLinks() as? Outcome.Success)?.value
            ?.firstOrNull { it.kind == SiteSection.EVENTS }?.url ?: pageUrl
        val fromPage = when (val r = http.get(eventsPageUrl)) {
            is Outcome.Success -> runCatching {
                events.fromEventsPage(extractor.findContainer(r.value.html()), eventsPageUrl, clock.today(), extractor)
            }.getOrDefault(emptyList())
            is Outcome.Failure -> emptyList()
        }
        return Outcome.Success(events.merge(fromPage, fromNews))
    }

    private var cachedLinks: List<SiteLink>? = null

    override suspend fun discoverLinks(): Outcome<List<SiteLink>> {
        cachedLinks?.let { return Outcome.Success(it) }
        return when (val r = http.get(url("/"))) {
            is Outcome.Failure -> Outcome.Success(SiteLinkClassifier.fallbackLinks(baseUrl))
            is Outcome.Success -> {
                val discovered = runCatching { SiteLinkClassifier.discover(r.value.html(), baseUrl) }.getOrDefault(emptyList())
                // Познатите страници, които не са намерени в менюто, се добавят отзад.
                val known = SiteLinkClassifier.fallbackLinks(baseUrl)
                    .filter { k -> discovered.none { it.kind == k.kind } }
                val all = discovered + known
                cachedLinks = all
                Outcome.Success(all)
            }
        }
    }

    override suspend fun fetchPage(url: String): Outcome<SitePage> =
        when (val r = http.get(url)) {
            is Outcome.Failure -> r
            is Outcome.Success -> runCatching {
                val doc = r.value.html()
                val container = extractor.findContainer(doc)
                SitePage(
                    url = url,
                    title = extractor.title(doc),
                    blocks = extractor.extract(container),
                    images = extractor.images(container),
                )
            }.fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse("page")) })
        }

    override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> {
        val contactsUrl = links.firstOrNull { it.kind == SiteSection.CONTACTS }?.url
            ?: url(SiteLinkClassifier.knownPaths.getValue(SiteSection.CONTACTS))
        return when (val r = http.get(contactsUrl)) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(
                runCatching { ContactsExtractor(baseUrl, fallbackContacts).extract(r.value.html(), contactsUrl) }
                    .getOrDefault(fallbackContacts),
            )
        }
    }

    override suspend fun fetchGallery(): Outcome<List<GalleryPhoto>> =
        when (val r = http.get(url("/photo/rss/"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> runCatching { rss.parsePhotos(r.value.xml()) }
                .fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse("photo rss")) })
        }
}
