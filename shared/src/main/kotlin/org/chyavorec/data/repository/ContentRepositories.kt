package org.chyavorec.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import org.chyavorec.core.map
import org.chyavorec.core.Synced
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
import org.chyavorec.domain.service.EventsService
import org.chyavorec.domain.service.NewsService
import org.chyavorec.domain.service.SiteContentService

private const val FIVE_MINUTES = 5 * 60 * 1000L
private const val ONE_HOUR = 60 * 60 * 1000L
private const val TEN_MINUTES = 10 * 60 * 1000L
private const val SIX_HOURS = 6 * ONE_HOUR
private const val TWELVE_HOURS = 12 * ONE_HOUR
private const val ONE_DAY = 24 * ONE_HOUR

internal fun cacheKey(prefix: String, value: String): String =
    prefix + Hashing.hex(Hashing.sha256(value.toByteArray()), 12)

class NewsRepository(
    private val service: NewsService,
    private val cache: PayloadCache,
    private val clock: AppClock,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) {
    private val list = CachedResource(cache, "news:list", ListSerializer(NewsArticle.serializer()), clock, FIVE_MINUTES, work)

    suspend fun cached(): Synced<List<NewsArticle>>? = list.cached()

    suspend fun latest(force: Boolean = false): Outcome<Synced<List<NewsArticle>>> =
        list.load(force) { service.fetchLatest() }

    suspend fun article(article: NewsArticle, force: Boolean = false): Outcome<Synced<ArticleDetail>> =
        CachedResource(cache, cacheKey("news:article:", article.url), ArticleDetail.serializer(), clock, ONE_HOUR, work)
            .load(force) { service.fetchArticle(article) }

    /** По идентификатор или по публичния адрес (/news/<slug>) — за вътрешните връзки. */
    suspend fun find(id: String): NewsArticle? = list.cached()?.data?.firstOrNull { it.id == id || it.url.trimEnd('/') == id.trimEnd('/') }
}

class EventsRepository(
    private val service: EventsService,
    cache: PayloadCache,
    private val clock: AppClock,
    work: CoroutineDispatcher = Dispatchers.Default,
) {
    private val list = CachedResource(cache, "events:list", ListSerializer(Event.serializer()), clock, FIVE_MINUTES, work)

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
        }.sortedWith(EVENT_ORDER)
    }

    suspend fun find(id: String): Event? = cachedRolled()?.data?.firstOrNull { it.id == id }

    /** Предстоящите (от днес нататък), подредени по дата и час — независимо от реда на входа. */
    fun upcoming(events: List<Event>): List<Event> {
        val today = clock.today().toString()
        return events.filter { (it.date ?: "") >= today }.sortedWith(EVENT_ORDER)
    }

    private companion object {
        /**
         * Хронологичен ред след „превъртането“: повтарящо се събитие, преместено в
         * следващата година, отива на мястото си, а не остава преди по-близките.
         * Без дата — най-накрая; при равна дата — по час (без час — след часовите).
         */
        val EVENT_ORDER: Comparator<Event> =
            compareBy<Event, String?>(nullsLast()) { it.date }.thenBy(nullsLast()) { it.time }
    }
}


class SiteRepository(
    private val service: SiteContentService,
    private val cache: PayloadCache,
    private val clock: AppClock,
    /** Източник на вече свалените новини за албума „Новини“ (без повторно теглене). */
    private val news: NewsRepository? = null,
) {
    // Навигацията, контактите, документите и страниците се менят рядко — 6 ч.; галерията (10+ страници) — 12 ч.
    private val links = CachedResource(cache, "site:links", ListSerializer(SiteLink.serializer()), clock, SIX_HOURS)
    private val gallery = CachedResource(cache, "site:gallery", ListSerializer(GalleryPhoto.serializer()), clock, TWELVE_HOURS)
    private val contacts = CachedResource(cache, "site:contacts", Contacts.serializer(), clock, SIX_HOURS)
    private val documents = CachedResource(cache, "site:documents", ListSerializer(SiteDocument.serializer()), clock, SIX_HOURS)
    private val index = CachedResource(cache, "site:index", ListSerializer(SiteSearchDoc.serializer()), clock, ONE_HOUR)

    /** Последно отворените страници — за да остане стойността им в паметта (вж. [CachedResource]). */
    private val pages = object : LinkedHashMap<String, CachedResource<SitePage>>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CachedResource<SitePage>>?) = size > RECENT_PAGES
    }

    private fun pageResource(url: String): CachedResource<SitePage> = synchronized(pages) {
        pages.getOrPut(url) { CachedResource(cache, cacheKey("site:page:", url), SitePage.serializer(), clock, SIX_HOURS) }
    }

    /** Албумите на последно върнатия списък снимки — общи за всички екрани на галерията. */
    @Volatile private var albumsMemo: Pair<List<GalleryPhoto>, List<GalleryAlbum>>? = null

    @Volatile private var oldFeastsCleared = false

    suspend fun links(force: Boolean = false): Outcome<Synced<List<SiteLink>>> = links.load(force) { service.discoverLinks() }

    suspend fun cachedLinks(): List<SiteLink>? = links.cached()?.data

    suspend fun page(url: String, force: Boolean = false): Outcome<Synced<SitePage>> =
        pageResource(url).load(force) { service.fetchPage(url) }

    suspend fun cachedPage(url: String): SitePage? = pageResource(url).cached()?.data

    suspend fun contacts(force: Boolean = false): Outcome<Synced<Contacts>> = contacts.load(force) {
        val l = (links(false) as? Outcome.Success)?.value?.data.orEmpty()
        service.fetchContacts(l)
    }

    /**
     * Албумите. Едновременните заявки (няколко екрана на галерията) се сливат в една, а
     * при непроменен списък снимки всички получават една и съща, вече групирана стойност.
     */
    suspend fun gallery(force: Boolean = false): Outcome<Synced<List<GalleryAlbum>>> =
        when (val r = gallery.load(force) { service.fetchGallery(newsForGallery()) }) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.map { photos -> albumsOf(photos) })
        }

    /** Албумите от кеша (без мрежа) — за мигновено показване. */
    suspend fun cachedGallery(): Synced<List<GalleryAlbum>>? = gallery.cached()?.map { albumsOf(it) }

    private fun albumsOf(photos: List<GalleryPhoto>): List<GalleryAlbum> {
        albumsMemo?.let { (p, a) -> if (p === photos) return a }
        return albums(photos).also { albumsMemo = photos to it }
    }

    /** Новините от кеша/мрежата на [news] (с неговия прозорец на свежест) или `null`. */
    private suspend fun newsForGallery(): List<NewsArticle>? {
        val repo = news ?: return null
        return (repo.latest(false) as? Outcome.Success)?.value?.data ?: repo.cached()?.data ?: emptyList()
    }

    suspend fun documents(force: Boolean = false): Outcome<Synced<List<SiteDocument>>> = documents.load(force) { service.fetchDocuments() }

    suspend fun searchIndex(force: Boolean = false): Outcome<Synced<List<SiteSearchDoc>>> = index.load(force) { service.fetchSearchIndex() }

    suspend fun cachedSearchIndex(): List<SiteSearchDoc>? = index.cached()?.data

    /**
     * Празникът за днес — кешира се за датата (`feast:ГГГГ-ММ-ДД`, 24 ч.), така че не се
     * тегли при всяко отваряне на началния екран и се показва и офлайн.
     */
    suspend fun feastToday(): DailyFeast? {
        val d = clock.today()
        if (!oldFeastsCleared) {
            oldFeastsCleared = true
            // Записите от предишните дни вече не трябват.
            for (back in 1L..7L) runCatching { cache.remove(feastKey(d.minusDays(back))) }
        }
        val res = CachedResource(cache, feastKey(d), DailyFeast.serializer(), clock, ONE_DAY)
        return (res.load(false) { service.fetchFeast("${d.year}-${d.monthValue}-${d.dayOfMonth}") } as? Outcome.Success)?.value?.data
    }

    private fun feastKey(date: java.time.LocalDate) = "feast:$date"

    fun albums(photos: List<GalleryPhoto>): List<GalleryAlbum> =
        photos.groupBy { it.album }.map { (name, list) -> GalleryAlbum(name, list) }
            .sortedBy { if (it.name == "Новини") 1 else 0 }

    companion object {
        private const val RECENT_PAGES = 8

        fun describe(error: AppError): String = error.toString()
    }
}

/**
 * Съобщенията от читалището. [isMember] решава дали се виждат и тези „само за членове“.
 * Файлът е публичен — филтрирането е за удобство, не за сигурност (затова админ
 * панелът предупреждава да не се пишат лични данни).
 */
class MessagesRepository(
    private val service: SiteContentService,
    cache: PayloadCache,
    private val clock: AppClock,
) {
    private val list = CachedResource(cache, "site:messages", ListSerializer(org.chyavorec.domain.model.AppMessage.serializer()), clock, TEN_MINUTES)

    suspend fun messages(isMember: Boolean, force: Boolean = false): Outcome<Synced<List<org.chyavorec.domain.model.AppMessage>>> =
        list.load(force) { service.fetchMessages() }.map { synced -> synced.map { visible(it, isMember) } }

    suspend fun cached(isMember: Boolean): List<org.chyavorec.domain.model.AppMessage> =
        list.cached()?.data?.let { visible(it, isMember) }.orEmpty()

    private fun visible(all: List<org.chyavorec.domain.model.AppMessage>, isMember: Boolean) =
        org.chyavorec.data.site.AppMessagesParser.visible(all, isMember, clock.now().atZone(org.chyavorec.data.site.AppMessagesParser.SOFIA).toLocalDate())
}
