package org.chyavorec.data.site

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import org.chyavorec.core.TrustedLinks
import org.chyavorec.core.logFailure
import org.chyavorec.core.map
import org.chyavorec.data.http.HttpBody
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSearchDoc
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.service.EventsService
import org.chyavorec.domain.service.Fetched
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
 * | местни празници за украсите | `/data/app-occasions.json` |
 *
 * Разчитането (JSON, RSS, Jsoup) върви на [work], не на извикващата (главна) нишка.
 */
class ChyavorecSiteService(
    private val http: HttpFetcher,
    private val baseUrl: String,
    private val clock: AppClock,
    private val fallbackContacts: Contacts,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) : NewsService, EventsService, SiteContentService {

    private companion object {
        const val RSS_FRESH = 24 * 60 * 60 * 1000L
    }

    private val parser = SiteJsonParser(baseUrl)
    private val extractor = HtmlContentExtractor(baseUrl)
    private val json = Json { ignoreUnknownKeys = true }

    private fun url(path: String) = baseUrl.trimEnd('/') + path

    private fun HttpBody.html(): Document = Jsoup.parse(ByteArrayInputStream(bytes), charset, finalUrl)

    private suspend fun <T> parse(what: String, block: () -> T): Outcome<T> = withContext(work) {
        // Сменен формат на сайта — причината остава в локалния дневник (без съдържанието).
        runCatching(block).logFailure("site.parse:$what").fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse(what)) })
    }

    /** Брой разчитания на news.json / index.json (за тестовете: непромененото не се разчита). */
    @Volatile internal var newsParses = 0
        private set
    @Volatile internal var indexParses = 0
        private set

    /** Суровият текст на файл с данни и SHA-256 на байтовете му (за [Fetched]). */
    private class RawJson(val text: String, val hash: String)

    private suspend fun raw(body: HttpBody): RawJson = withContext(work) { RawJson(body.text(), Hashing.sha256Hex(body.bytes)) }

    // --- търсещият индекс се ползва от няколко функции; държим го кратко в паметта ---
    private val indexMutex = Mutex()
    private var index: RawJson? = null
    private var indexAt = 0L

    private suspend fun indexJson(): Outcome<RawJson> = indexMutex.withLock {
        val now = clock.now().toEpochMilli()
        index?.takeIf { now - indexAt < 5 * 60_000 }?.let { return Outcome.Success(it) }
        when (val r = http.get(url("/javora/index.json"))) {
            is Outcome.Failure -> r
            is Outcome.Success -> raw(r.value).also { index = it; indexAt = now }.let { Outcome.Success(it) }
        }
    }

    /** Разчита index.json, само ако е различен от вече разчетения ([knownHash]). */
    private suspend fun <T> fromIndex(what: String, knownHash: String?, block: (String) -> T): Outcome<Fetched<T>> =
        when (val r = indexJson()) {
            is Outcome.Failure -> r
            is Outcome.Success -> {
                val raw = r.value
                if (knownHash != null && raw.hash == knownHash) Outcome.Success(Fetched.Unchanged)
                else parse(what) { indexParses++; block(raw.text) }.map { Fetched.Changed(it, raw.hash) }
            }
        }

    // --- rss.xml служи само за публичните адреси на новините: държим го до 24 ч. и го ---
    // --- теглим отново само ако се появи новина, която липсва в него.                  ---
    private val rssMutex = Mutex()
    private var rssLinks: Map<String, String>? = null
    private var rssAt = 0L
    /** Новините (заглавия), които липсваха в rss.xml при последното му сваляне — напр. по-старите. */
    private var rssMissing: Set<String> = emptySet()

    private suspend fun rssLinksFrom(result: Outcome<HttpBody>?): Map<String, String>? {
        val body = (result as? Outcome.Success)?.value ?: return null
        return withContext(work) { runCatching { parser.parseRssLinks(body.text()) }.getOrNull() }
    }

    private suspend fun rememberRss(links: Map<String, String>, articles: List<NewsArticle>, at: Long) = rssMutex.withLock {
        rssLinks = links
        rssAt = at
        rssMissing = articles.filter { parser.rssLinkFor(links, it.title) == null }.mapTo(HashSet()) { it.title }
    }

    // Без известен хеш съдържанието винаги е „ново“ (Fetched.Changed).
    override suspend fun fetchLatest(): Outcome<List<NewsArticle>> = fetchLatestIfChanged(null).changedOrError("news.json")

    /**
     * news.json се разчита (JSON + Jsoup за всяка новина) само ако SHA-256 на суровите
     * байтове се различава от [knownHash]; иначе — [Fetched.Unchanged], без разчитане и без rss.xml.
     * Хешът се връща само при пълни данни: ако rss.xml не е наличен и някоя новина е без
     * публичен адрес, следващото сваляне пак разчита файла (и опитва rss.xml отново).
     */
    override suspend fun fetchLatestIfChanged(knownHash: String?): Outcome<Fetched<List<NewsArticle>>> = coroutineScope {
        val now = clock.now().toEpochMilli()
        val known = rssMutex.withLock { rssLinks?.takeIf { now - rssAt in 0 until RSS_FRESH }?.let { it to rssMissing } }
        val rss = if (known == null) async { http.get(url("/rss.xml")) } else null
        when (val r = http.get(url("/data/news.json"))) {
            is Outcome.Failure -> { rss?.cancel(); r }
            is Outcome.Success -> {
                val raw = raw(r.value)
                if (knownHash != null && raw.hash == knownHash) {
                    rss?.cancel()
                    return@coroutineScope Outcome.Success(Fetched.Unchanged)
                }
                val links = known?.first ?: rssLinksFrom(rss?.await())
                var complete = links != null
                when (val parsed = parse("news.json") { newsParses++; parser.parseNews(raw.text, links.orEmpty()) }) {
                    is Outcome.Failure -> parsed
                    is Outcome.Success -> {
                        var articles = parsed.value
                        if (known == null) {
                            links?.let { rememberRss(it, articles, now) }
                        } else if (articles.any { parser.rssLinkFor(known.first, it.title) == null && it.title !in known.second }) {
                            // Нова новина, която я няма в запомнения rss.xml — опресняваме го.
                            val fresh = rssLinksFrom(http.get(url("/rss.xml")))
                            if (fresh != null) {
                                articles = articles.map { a -> parser.rssLinkFor(fresh, a.title)?.let { a.copy(url = it) } ?: a }
                                rememberRss(fresh, articles, now)
                            } else {
                                complete = false
                            }
                        }
                        Outcome.Success(Fetched.Changed(articles, raw.hash.takeIf { complete }))
                    }
                }
            }
        }
    }

    /** Пълният текст вече е в news.json — не е нужна втора заявка. */
    override suspend fun fetchArticle(article: NewsArticle): Outcome<ArticleDetail> {
        val images = (listOfNotNull(article.imageUrl) + article.contentBlocks.filterIsInstance<ContentBlock.Image>().map { it.url }).distinct()
        return Outcome.Success(ArticleDetail(article, article.contentBlocks, images, ArticleDetail.altsOf(article.contentBlocks)))
    }

    override suspend fun fetchEvents(): Outcome<List<Event>> = fetchEventsIfChanged(null).changedOrError("events")

    /**
     * Датите на събитията са следващото настъпване спрямо днес, но всички са годишни
     * (`recurring`) — кешираният списък се „превърта“ от хранилището, затова и
     * непромененият index.json не се разчита отново.
     */
    override suspend fun fetchEventsIfChanged(knownHash: String?): Outcome<Fetched<List<Event>>> =
        fromIndex("events", knownHash) { parser.parseEvents(it, clock.today()) }

    override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = fetchSearchIndexIfChanged(null).changedOrError("index")

    override suspend fun fetchSearchIndexIfChanged(knownHash: String?): Outcome<Fetched<List<SiteSearchDoc>>> =
        fromIndex("index", knownHash) { parser.parseIndex(it) }

    private fun <T> Outcome<Fetched<T>>.changedOrError(what: String): Outcome<T> = when (this) {
        is Outcome.Failure -> this
        is Outcome.Success -> when (val f = value) {
            is Fetched.Changed -> Outcome.Success(f.value)
            Fetched.Unchanged -> Outcome.Failure(AppError.Parse(what))
        }
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

    /**
     * Страница от сайта. Чужд адрес (или пренасочване към чужд хост) не се зарежда и не се
     * показва като страница на читалището — виж [TrustedLinks].
     */
    override suspend fun fetchPage(url: String): Outcome<SitePage> {
        if (!TrustedLinks.isSiteUrl(url, baseUrl)) {
            Diagnostics.note("site.page", "untrusted url")
            return Outcome.Failure(AppError.NotFound)
        }
        return when (val r = http.get(url)) {
            is Outcome.Failure -> r
            is Outcome.Success -> if (!TrustedLinks.isSiteUrl(r.value.finalUrl, baseUrl)) {
                Diagnostics.note("site.page", "redirect off site")
                Outcome.Failure(AppError.NotFound)
            } else parse("page") {
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
    }

    override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> {
        val contactsUrl = links.firstOrNull { it.kind == SiteSection.CONTACTS }?.url ?: url("/kontakti")
        return when (val r = http.get(contactsUrl)) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(
                withContext(work) {
                    runCatching { ContactsExtractor(baseUrl, fallbackContacts).extract(r.value.html(), contactsUrl) }
                        .getOrDefault(fallbackContacts)
                },
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

    override suspend fun fetchGallery(news: List<NewsArticle>?): Outcome<List<GalleryPhoto>> = coroutineScope {
        val newsPhotos = async {
            // Новините обикновено вече са свалени (кеш на хранилището) — не ги тегли повторно.
            (news ?: (fetchLatest() as? Outcome.Success)?.value).orEmpty().mapNotNull { a ->
                a.imageUrl?.let { GalleryPhoto("news:" + a.id, a.title, it, it, GalleryAlbum.NEWS, a.url, a.publishedAtMillis) }
            }
        }
        val pagePhotos = galleryPages.map { (album, path) ->
            async {
                when (val r = http.get(url(path))) {
                    is Outcome.Failure -> emptyList()
                    is Outcome.Success -> withContext(work) {
                        runCatching { photosFrom(r.value.html(), album, url(path)) }.getOrDefault(emptyList())
                    }
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
            // Мрежата от миниатюри — с най-малкия вариант от srcset; оригиналът — за прегледа.
            val thumb = extractor.thumbnailUrl(img) ?: src
            GalleryPhoto(album + ":" + src, (caption ?: img.attr("alt")).trim(), thumb, src, album, pageUrl)
        }
    }

    override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> = coroutineScope {
        val index = async { fetchSearchIndex() }
        val files = when (val r = http.get(url("/data/files.json"))) {
            is Outcome.Success -> withContext(work) { runCatching { parser.parseFiles(r.value.text()) }.getOrDefault(emptyList()) }
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

    override suspend fun fetchMessages(): Outcome<List<org.chyavorec.domain.model.AppMessage>> =
        when (val r = http.get(url("/data/app-messages.json"), mapOf("Cache-Control" to "no-cache"))) {
            is Outcome.Failure -> if (r.error == org.chyavorec.core.AppError.NotFound) Outcome.Success(emptyList()) else r
            is Outcome.Success -> withContext(work) { AppMessagesParser.parse(r.value.text()) }?.let { Outcome.Success(it) }
                ?: Outcome.Failure(org.chyavorec.core.AppError.Parse("app-messages.json"))
        }

    override suspend fun fetchOccasions(): Outcome<List<SiteOccasionEntry>> =
        when (val r = http.get(url("/data/app-occasions.json"))) {
            is Outcome.Failure -> if (r.error == org.chyavorec.core.AppError.NotFound) Outcome.Success(emptyList()) else r
            is Outcome.Success -> withContext(work) { SiteOccasionsParser.parse(r.value.text()) }?.let { Outcome.Success(it) }
                ?: Outcome.Failure(org.chyavorec.core.AppError.Parse("app-occasions.json"))
        }
}
