package org.chyavorec.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteSearchDoc
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.EventsService
import org.chyavorec.domain.service.NewsService
import org.chyavorec.domain.service.SiteContentService
import java.security.MessageDigest
import java.time.Instant

private const val FIVE_MINUTES = 5 * 60 * 1000L
private const val ONE_HOUR = 60 * 60 * 1000L

internal fun cacheKey(prefix: String, value: String): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
    return prefix + digest.take(12).joinToString("") { "%02x".format(it) }
}

class NewsRepository(
    private val service: NewsService,
    private val cache: PayloadCache,
    private val clock: AppClock,
) {
    private val list = CachedResource(cache, "news:list", ListSerializer(NewsArticle.serializer()), clock, FIVE_MINUTES)

    suspend fun cached(): Synced<List<NewsArticle>>? = list.cached()

    suspend fun latest(force: Boolean = false): Outcome<Synced<List<NewsArticle>>> =
        list.load(force) { service.fetchLatest() }

    suspend fun article(article: NewsArticle, force: Boolean = false): Outcome<Synced<ArticleDetail>> =
        CachedResource(cache, cacheKey("news:article:", article.url), ArticleDetail.serializer(), clock, ONE_HOUR)
            .load(force) { service.fetchArticle(article) }

    suspend fun find(id: String): NewsArticle? = list.cached()?.data?.firstOrNull { it.id == id }
}

class EventsRepository(
    private val service: EventsService,
    cache: PayloadCache,
    private val clock: AppClock,
) {
    private val list = CachedResource(cache, "events:list", ListSerializer(Event.serializer()), clock, FIVE_MINUTES)

    suspend fun cached(): Synced<List<Event>>? = list.cached()

    suspend fun events(force: Boolean = false): Outcome<Synced<List<Event>>> =
        when (val r = list.load(force) { service.fetchEvents() }) {
            is Outcome.Failure -> r
            // Кешираният годишен календар се „превърта“ към следващото настъпване днес.
            is Outcome.Success -> Outcome.Success(r.value.map { rollForward(it) })
        }

    suspend fun cachedRolled(): Synced<List<Event>>? = list.cached()?.map { rollForward(it) }

    private fun rollForward(events: List<Event>): List<Event> {
        val today = clock.today()
        return events.map { e ->
            val d = e.date?.let { runCatching { java.time.LocalDate.parse(it) }.getOrNull() }
            if (e.recurring && d != null && d.isBefore(today)) {
                var next = d
                while (next!!.isBefore(today)) next = next.plusYears(1)
                e.copy(date = next.toString())
            } else e
        }.sortedBy { it.date }
    }

    suspend fun find(id: String): Event? = list.cached()?.data?.firstOrNull { it.id == id }

    fun upcoming(events: List<Event>): List<Event> {
        val today = clock.today().toString()
        return events.filter { (it.date ?: "") >= today }
    }
}

/**
 * Каталогът се държи в паметта като [CatalogSearchEngine]; суровият JSON се
 * кешира на диска, за да работи търсенето и офлайн (с ясна дата на данните).
 */
class CatalogRepository(
    private val service: CatalogService,
    private val cache: PayloadCache,
    private val clock: AppClock,
) {
    private val mutex = Mutex()
    @Volatile private var current: Synced<CatalogSearchEngine>? = null

    fun inMemory(): Synced<CatalogSearchEngine>? = current

    suspend fun cached(): Synced<CatalogSearchEngine>? = mutex.withLock {
        current ?: loadFromDisk()?.also { current = it }
    }

    private suspend fun loadFromDisk(): Synced<CatalogSearchEngine>? {
        val payload = cache.read(KEY) ?: return null
        val snapshot = runCatching { KatalogParser.parse(payload.text) }.getOrNull() ?: return null
        return Synced(CatalogSearchEngine(snapshot), Instant.ofEpochMilli(payload.savedAtMillis), fromCache = true)
    }

    suspend fun catalog(force: Boolean = false): Outcome<Synced<CatalogSearchEngine>> = mutex.withLock {
        val existing = current ?: loadFromDisk()
        if (!force && existing != null && !existing.fromCache &&
            clock.now().toEpochMilli() - existing.syncedAt.toEpochMilli() < FIVE_MINUTES
        ) return Outcome.Success(existing)
        when (val r = service.fetchCatalog()) {
            is Outcome.Success -> {
                val (raw, snapshot) = r.value
                runCatching { cache.write(KEY, raw) }
                val fresh = Synced(CatalogSearchEngine(snapshot), clock.now())
                current = fresh
                Outcome.Success(fresh)
            }
            is Outcome.Failure -> {
                if (existing != null) {
                    val stale = existing.copy(fromCache = true, refreshError = r.error)
                    current = stale
                    Outcome.Success(stale)
                } else r
            }
        }
    }

    companion object {
        const val KEY = "catalog:katalog.json"
    }
}

class SiteRepository(
    private val service: SiteContentService,
    private val cache: PayloadCache,
    private val clock: AppClock,
) {
    private val links = CachedResource(cache, "site:links", ListSerializer(SiteLink.serializer()), clock, ONE_HOUR)
    private val gallery = CachedResource(cache, "site:gallery", ListSerializer(GalleryPhoto.serializer()), clock, FIVE_MINUTES)
    private val contacts = CachedResource(cache, "site:contacts", Contacts.serializer(), clock, ONE_HOUR)
    private val documents = CachedResource(cache, "site:documents", ListSerializer(SiteDocument.serializer()), clock, ONE_HOUR)
    private val index = CachedResource(cache, "site:index", ListSerializer(SiteSearchDoc.serializer()), clock, ONE_HOUR)

    suspend fun links(force: Boolean = false): Outcome<Synced<List<SiteLink>>> = links.load(force) { service.discoverLinks() }

    suspend fun cachedLinks(): List<SiteLink>? = links.cached()?.data

    suspend fun page(url: String, force: Boolean = false): Outcome<Synced<SitePage>> =
        CachedResource(cache, cacheKey("site:page:", url), SitePage.serializer(), clock, ONE_HOUR)
            .load(force) { service.fetchPage(url) }

    suspend fun cachedPage(url: String): SitePage? =
        CachedResource(cache, cacheKey("site:page:", url), SitePage.serializer(), clock, ONE_HOUR).cached()?.data

    suspend fun contacts(force: Boolean = false): Outcome<Synced<Contacts>> = contacts.load(force) {
        val l = (links(false) as? Outcome.Success)?.value?.data.orEmpty()
        service.fetchContacts(l)
    }

    suspend fun gallery(force: Boolean = false): Outcome<Synced<List<GalleryAlbum>>> =
        when (val r = gallery.load(force) { service.fetchGallery() }) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.map { photos -> albums(photos) })
        }

    suspend fun documents(force: Boolean = false): Outcome<Synced<List<SiteDocument>>> = documents.load(force) { service.fetchDocuments() }

    suspend fun searchIndex(force: Boolean = false): Outcome<Synced<List<SiteSearchDoc>>> = index.load(force) { service.fetchSearchIndex() }

    suspend fun cachedSearchIndex(): List<SiteSearchDoc>? = index.cached()?.data

    /** Празникът за днес (не се кешира на диска — важи само за деня). */
    suspend fun feastToday(): DailyFeast? {
        val d = clock.today()
        return (service.fetchFeast("${d.year}-${d.monthValue}-${d.dayOfMonth}") as? Outcome.Success)?.value
    }

    fun albums(photos: List<GalleryPhoto>): List<GalleryAlbum> =
        photos.groupBy { it.album }.map { (name, list) -> GalleryAlbum(name, list) }
            .sortedBy { if (it.name == "Новини") 1 else 0 }

    companion object {
        fun describe(error: AppError): String = error.toString()
    }
}
