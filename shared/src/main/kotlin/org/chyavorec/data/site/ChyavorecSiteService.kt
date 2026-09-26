package org.chyavorec.data.site

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpBody
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSearchDoc
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.service.EventsService
import org.chyavorec.domain.service.NewsService
import org.chyavorec.domain.service.SiteContentService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayInputStream

/**
 * Интеграция с chyavorec.org — статичен сайт, хостван във Vercel
 * (изходен код: plam4o4o-source/site-yavorec).
 *
 * Източници (всички публични, без ключове):
 * | Данни | Адрес |
 * |---|---|
 * | новини | `/data/news.json` (+ `/rss.xml` за публичните адреси) |
 * | събития, страници, архив, публикации, търсене | `/javora/index.json` |
 * | документи | `/data/files.json` |
 * | съдържание на страница | `/<страница>` → `#hp .hp-inner` |
 * | празник на деня | `/api/calendar?d=Г-М-Д` (Vercel функция) |
 */
class ChyavorecSiteService(
    private val http: HttpFetcher,
    private val baseUrl: String,
    private val clock: AppClock,
    private val fallbackContacts: Contacts,
) : NewsService, EventsService, SiteContentService {

    private val parser = SiteJsonParser(baseUrl)
    private val extractor = HtmlContentExtractor(baseUrl)
    private val json = Json { ignoreUnknownKeys = true }

    private fun url(path: String) = baseUrl.trimEnd('/') + path

    private fun HttpBody.html(): Document = Jsoup.parse(ByteArrayInputStream(bytes), charset, finalUrl)

    private inline fun <T> parse(what: String, block: () -> T): Outcome<T> =
        runCatching(block).fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse(what)) })

    // --- търсещият индекс се ползва от няколко функции; държим го кратко в паметта ---
    private val indexMutex = Mutex()
    private var indexText: String? = null
    private var indexAt = 0L

    private suspend fun indexJson(): Outcome<String> = indexMutex.withLock {
        val now = clock.now().toEpochMilli()
        indexText?.takeIf { now - indexAt < 5 * 60_000 }?.let { return Outcome.Success(it) }
        when (val r = http.get(url("/javora/index.json"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> r.value.text().also { indexText = it; indexAt = now }.let { Outcome.Success(it) }
        }
    }

    override suspend fun fetchLatest(): Outcome<List<NewsArticle>> = coroutineScope {
        val rss = async { http.get(url("/rss.xml")) }
        when (val r = http.get(url("/data/news.json"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> {
                val links = (rss.await() as? Outcome.Success)?.value?.let { runCatching { parser.parseRssLinks(it.text()) }.getOrNull() }.orEmpty()
                parse("news.json") { parser.parseNews(r.value.text(), links) }
            }
        }
    }

    /** Пълният текст вече е в news.json — не е нужна втора заявка. */
    override suspend fun fetchArticle(article: NewsArticle): Outcome<ArticleDetail> {
        val images = (listOfNotNull(article.imageUrl) + article.contentBlocks.filterIsInstance<ContentBlock.Image>().map { it.url }).distinct()
        return Outcome.Success(ArticleDetail(article, article.contentBlocks, images))
    }

    override suspend fun fetchEvents(): Outcome<List<Event>> = when (val r = indexJson()) {
        is Outcome.Failure -> r
        is Outcome.Success -> parse("events") { parser.parseEvents(r.value, clock.today()) }
    }

    override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = when (val r = indexJson()) {
        is Outcome.Failure -> r
        is Outcome.Success -> parse("index") { parser.parseIndex(r.value) }
    }

    override suspend fun discoverLinks(): Outcome<List<SiteLink>> = when (val r = fetchSearchIndex()) {
        is Outcome.Failure -> r
        is Outcome.Success -> {
            val pages = r.value.filter { d ->
                d.type in setOf("page", "history") || (d.type in setOf("archive", "publication") && d.id.startsWith("page-"))
            }.map { SiteLinkClassifier.link(it.title, it.url, baseUrl) }
            val static = SiteLinkClassifier.staticPages.map { (t, p) -> SiteLinkClassifier.link(t, p, baseUrl) }
            Outcome.Success((pages + static).distinctBy { it.url.trimEnd('/') })
        }
    }

    override suspend fun fetchPage(url: String): Outcome<SitePage> =
        when (val r = http.get(url)) {
            is Outcome.Failure -> r
            is Outcome.Success -> parse("page") {
                val doc = r.value.html()
                val container = extractor.findContainer(doc)
                val blocks = extractor.extract(container)
                val title = extractor.title(doc)
                SitePage(
                    url = url,
                    title = title,
                    // Заглавието се показва в лентата — не го повтаряме в съдържанието.
                    blocks = blocks.dropWhile { it is ContentBlock.Heading && it.text == title }
                        .let { b -> if (b.firstOrNull() is ContentBlock.Heading && (b.first() as ContentBlock.Heading).text == title) b.drop(1) else b },
                    images = extractor.images(container),
                )
            }
        }

    override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> {
        val contactsUrl = links.firstOrNull { it.kind == SiteSection.CONTACTS }?.url ?: url("/kontakti")
        return when (val r = http.get(contactsUrl)) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(
                runCatching { ContactsExtractor(baseUrl, fallbackContacts).extract(r.value.html(), contactsUrl) }
                    .getOrDefault(fallbackContacts),
            )
        }
    }

    /** Страниците със снимки, които формират албумите на галерията. */
    private val galleryPages = listOf(
        "Фотодокументална изложба" to "/fotodokumentalna-izlozhba",
        "Фолклорна група „Явор“" to "/folklorna-grupa",
        "Клуб за народни танци" to "/tancov-klub",
        "Ансамбъл „Яворски хлапета“" to "/yavorski-hlapeta",
        "Село Яворец" to "/selo-yavorec",
        "Природата край Яворец" to "/nature",
        "История" to "/history",
        "Библиотека" to "/library",
        "Проекти" to "/proekti",
        "Клубове и кръжоци" to "/klubove-i-krzhoci",
    )

    override suspend fun fetchGallery(): Outcome<List<GalleryPhoto>> = coroutineScope {
        val newsPhotos = async {
            (fetchLatest() as? Outcome.Success)?.value.orEmpty().mapNotNull { a ->
                a.imageUrl?.let { GalleryPhoto("news:" + a.id, a.title, it, it, "Новини", a.url, a.publishedAtMillis) }
            }
        }
        val pagePhotos = galleryPages.map { (album, path) ->
            async {
                when (val r = http.get(url(path))) {
                    is Outcome.Failure -> emptyList()
                    is Outcome.Success -> runCatching { photosFrom(r.value.html(), album, url(path)) }.getOrDefault(emptyList())
                }
            }
        }
        val all = newsPhotos.await() + pagePhotos.awaitAll().flatten()
        if (all.isEmpty()) Outcome.Failure(AppError.Network) else Outcome.Success(all.distinctBy { it.fullUrl })
    }

    private fun photosFrom(doc: Document, album: String, pageUrl: String): List<GalleryPhoto> {
        val container = extractor.findContainer(doc)
        return container.select("img").mapNotNull { img ->
            val src = extractor.imageUrl(img) ?: return@mapNotNull null
            // Подписът под експоната (фотодокументалната изложба) е по-точен от alt текста.
            val caption = img.closest(".exh-card, figure")?.selectFirst(".exh-caption-title, figcaption")?.text()
            GalleryPhoto(album + ":" + src, (caption ?: img.attr("alt")).trim(), src, src, album, pageUrl)
        }
    }

    override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> = coroutineScope {
        val index = async { fetchSearchIndex() }
        val files = when (val r = http.get(url("/data/files.json"))) {
            is Outcome.Success -> runCatching { parser.parseFiles(r.value.text()) }.getOrDefault(emptyList())
            is Outcome.Failure -> emptyList()
        }
        val publications = (index.await() as? Outcome.Success)?.value?.let { parser.publications(it) }.orEmpty()
        if (files.isEmpty() && publications.isEmpty()) Outcome.Failure(AppError.Network)
        else Outcome.Success(files.sortedByDescending { it.date } + publications)
    }

    override suspend fun fetchFeast(date: String): Outcome<DailyFeast> =
        when (val r = http.get(url("/api/calendar?d=$date"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> parse("calendar") {
                val o = json.parseToJsonElement(r.value.text()).jsonObject
                val line = (o["line"] as? kotlinx.serialization.json.JsonPrimitive)?.takeIf { it.isString }?.content
                    ?: error("no feast")
                DailyFeast(o["date"]?.jsonPrimitive?.content ?: date, line)
            }
        }
}
