package org.chyavorec.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.TestUtil
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.GitHubCatalogService
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.repository.CachedResource
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.data.site.HtmlContentExtractor
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSearchDoc
import org.chyavorec.domain.model.parseYear
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.service.CatalogFetch
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.CatalogValidators
import org.chyavorec.domain.service.SiteContentService
import org.jsoup.Jsoup
import java.nio.file.Files
import java.text.Collator
import java.time.Instant
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Кеширане и производителност: условни заявки, обобщение, памет на CachedResource, рангове. */
class CachingTest {
    private lateinit var server: MockWebServer
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val raw = TestUtil.resource("katalog-sample.json")

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() = server.shutdown()

    private fun http(cache: Cache? = null) =
        HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).cache(cache).build(), "test")

    // --- HttpFetcher / каталог: 304 ---

    @Test fun notModifiedFlag() {
        assertTrue(HttpFetcher.isNotModified(304, hasCacheResponse = true))
        assertTrue(HttpFetcher.isNotModified(null, hasCacheResponse = true))
        assertFalse(HttpFetcher.isNotModified(200, hasCacheResponse = true))
        assertFalse(HttpFetcher.isNotModified(200, hasCacheResponse = false))
    }

    @Test fun httpCacheRevalidationSkipsBody() = runTest {
        val dir = Files.createTempDirectory("okcache").toFile()
        val fetcher = http(Cache(dir, 1024 * 1024))
        server.enqueue(MockResponse().setBody("тяло").setHeader("ETag", "\"v1\"").setHeader("Cache-Control", "no-cache"))
        server.enqueue(MockResponse().setResponseCode(304).setHeader("ETag", "\"v1\""))
        val first = (fetcher.get(server.url("/f").toString(), skipBodyIfNotModified = true) as Outcome.Success).value
        assertFalse(first.notModified)
        assertEquals("тяло", first.text())
        val second = (fetcher.get(server.url("/f").toString(), skipBodyIfNotModified = true) as Outcome.Success).value
        assertTrue(second.notModified)
        assertEquals(0, second.bytes.size, "при 304 тялото не се чете")
        server.takeRequest()
        assertEquals("\"v1\"", server.takeRequest().getHeader("If-None-Match"))
        dir.deleteRecursively()
    }

    @Test fun catalog304IsUnchangedWithoutParsing() = runTest {
        val url = server.url("/raw/katalog.json").toString()
        server.enqueue(MockResponse().setBody(raw).setHeader("ETag", "\"abc\""))
        server.enqueue(MockResponse().setResponseCode(304).setHeader("ETag", "\"abc\""))
        val svc = GitHubCatalogService(http(), listOf(url))
        val first = assertIs<CatalogFetch.Changed>((svc.fetchCatalogConditional(null) as Outcome.Success).value)
        assertEquals("\"abc\"", first.etag)
        assertEquals(Hashing.sha256Hex(raw.toByteArray()), first.hash)
        assertEquals("no-store", server.takeRequest().getHeader("Cache-Control"))
        val second = (svc.fetchCatalogConditional(CatalogValidators(first.hash, first.etag, first.etagUrl)) as Outcome.Success).value
        assertIs<CatalogFetch.Unchanged>(second)
        assertEquals("\"abc\"", server.takeRequest().getHeader("If-None-Match"))
    }

    @Test fun catalogSameBytesWithoutEtagIsUnchanged() = runTest {
        val url = server.url("/raw/katalog.json").toString()
        server.enqueue(MockResponse().setBody(raw))
        val svc = GitHubCatalogService(http(), listOf(url))
        val r = (svc.fetchCatalogConditional(CatalogValidators(Hashing.sha256Hex(raw.toByteArray()))) as Outcome.Success).value
        assertIs<CatalogFetch.Unchanged>(r)
        assertNull(server.takeRequest().getHeader("If-None-Match"), "без ETag заявката не е условна")
    }

    // --- CatalogRepository: хеш, ETag, обобщение ---

    private inner class ConditionalCatalog : CatalogService {
        var fetches = 0
        var lastKnown: CatalogValidators? = null
        var fail = false
        override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> = error("не се ползва")
        override suspend fun fetchCatalogConditional(known: CatalogValidators?): Outcome<CatalogFetch> {
            fetches++
            lastKnown = known
            if (fail) return Outcome.Failure(AppError.Network)
            if (known?.etag == "\"e1\"") return Outcome.Success(CatalogFetch.Unchanged("\"e1\"", known.etagUrl))
            val bytes = raw.toByteArray()
            return Outcome.Success(CatalogFetch.Changed(bytes, KatalogParser.parse(bytes), Hashing.sha256Hex(bytes), "\"e1\"", "u"))
        }
    }

    @Test fun hashAndEtagArePersistedAndSentAfterRestart() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val svc = ConditionalCatalog()
        CatalogRepository(svc, cache, clock).catalog(force = true)
        assertNotNull(cache.read(CatalogRepository.META_KEY))
        clock.advanceSeconds(3600)
        // Нов процес: без да се строи индекс, заявката е условна със запазените хеш и ETag.
        val restarted = CatalogRepository(svc, cache, clock)
        val r = (restarted.refreshHomeSummary(force = false) as Outcome.Success).value
        assertEquals(Hashing.sha256Hex(raw.toByteArray()), svc.lastKnown?.hash)
        assertEquals("\"e1\"", svc.lastKnown?.etag)
        assertEquals("u", svc.lastKnown?.etagUrl)
        assertFalse(r.fromCache)
        assertEquals(clock.now(), r.syncedAt)
        assertNull(restarted.inMemory(), "началният екран не строи индекса за търсене")
    }

    @Test fun homeSummaryRoundTripWithoutEngine() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val svc = ConditionalCatalog()
        val repo = CatalogRepository(svc, cache, clock)
        val fresh = (repo.refreshHomeSummary(force = false) as Outcome.Success).value.data
        assertNull(repo.inMemory())
        val expected = CatalogSearchEngine.homeSummary(KatalogParser.parse(raw))
        assertEquals(expected, fresh)
        assertEquals(60, fresh.count)
        assertEquals("2026-09-19", fresh.generatedOn)
        assertTrue(fresh.newest.size <= CatalogSearchEngine.HOME_NEWEST)
        // Нов процес: обобщението идва от диска, без мрежа и без каталога.
        val restarted = CatalogRepository(svc, cache, clock)
        assertEquals(expected, restarted.homeSummary()!!.data)
        assertNull(restarted.inMemory())
        assertEquals(1, svc.fetches)
        // Търсенето строи индекса чак при поискване; в прозореца на свежест — без мрежа.
        val engine = (restarted.catalog(false) as Outcome.Success).value.data
        assertEquals(60, engine.snapshot.books.size)
        assertEquals(1, svc.fetches)
    }

    @Test fun legacyCacheGetsSummaryAndHashOnce() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        // Кеш от по-стара версия: само суровият файл и времето на сваляне.
        cache.write(CatalogRepository.KEY, raw)
        cache.write(CatalogRepository.FETCHED_AT_KEY, clock.now().toEpochMilli().toString())
        val svc = ConditionalCatalog()
        val repo = CatalogRepository(svc, cache, clock)
        assertEquals(60, repo.homeSummary()!!.data.count)
        assertNotNull(cache.read(CatalogRepository.SUMMARY_KEY))
        assertTrue(cache.read(CatalogRepository.META_KEY)!!.text.contains(Hashing.sha256Hex(raw.toByteArray())))
        assertIs<Outcome.Success<*>>(repo.refreshHomeSummary(false))
        assertEquals(0, svc.fetches, "в прозореца на свежест")
    }

    @Test fun trimMemoryDropsEngineAndReloadsFromDisk() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val svc = ConditionalCatalog()
        val repo = CatalogRepository(svc, cache, clock)
        val first = (repo.catalog(true) as Outcome.Success).value.data
        repo.trimMemory()
        assertNull(repo.inMemory())
        val again = repo.cached()!!.data
        assertTrue(first !== again)
        assertEquals(first.snapshot.books.size, again.snapshot.books.size)
        assertEquals(1, svc.fetches)
    }

    @Test fun catalogOfflineFallsBackToSummaryOnDisk() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val svc = ConditionalCatalog()
        CatalogRepository(svc, cache, clock).catalog(true)
        svc.fail = true
        val r = (CatalogRepository(svc, cache, clock).refreshHomeSummary(force = true) as Outcome.Success).value
        assertTrue(r.fromCache)
        assertEquals(AppError.Network, r.refreshError)
        assertEquals(60, r.data.count)
    }

    // --- Домейн/търсене ---

    @Test fun yearParsing() {
        assertEquals(1998, parseYear("[1998]"))
        assertEquals(2016, parseYear("2016 г."))
        assertEquals(1234, parseYear("12345"))
        assertEquals(2001, parseYear("ок. 99, 2001"))
        assertNull(parseYear("199"))
        assertNull(parseYear(""))
        assertEquals(1894, CatalogBook(1, "", "x", year = "1894").yearNumber)
    }

    @Test fun ranksSortLikeTheCollator() {
        val titles = listOf("Чичовци", "Под игото", "Ябълка", "атлас", "Атлас", "Ёлка", "Българи", "под игото", "Zebra", "Ъгъл", "", "Ангел")
        val authors = listOf("Вазов", "", "Елин Пелин", "Вазов", "Ботев", "", "Яворов", "вазов", "Smith", "Ангелов", "Ботев", "")
        val books = titles.mapIndexed { i, t -> CatalogBook(i.toLong() + 1, authors[i], t.ifEmpty { "-" }) }
        val engine = CatalogSearchEngine(CatalogSnapshot("Б", "Я", "2026-01-01", books, emptyList()))
        val collator = Collator.getInstance(Locale.forLanguageTag("bg-BG"))
        val byTitle = books.sortedWith(compareBy(collator) { it.title })
        assertEquals(byTitle.map { it.inv }, engine.search(CatalogQuery(sort = CatalogSort.TITLE)).map { it.inv })
        val byAuthor = books.sortedWith(
            Comparator<CatalogBook> { a, b -> collator.compare(a.author.ifBlank { "￿" }, b.author.ifBlank { "￿" }) }
                .then(compareBy(collator) { it.title }),
        )
        assertEquals(byAuthor.map { it.inv }, engine.search(CatalogQuery(sort = CatalogSort.AUTHOR)).map { it.inv })
    }

    @Test fun searchAllMatchesAcrossFieldsLikeBefore() {
        val books = listOf(
            CatalogBook(1, "Вазов, Иван", "Под игото", publisher = "Просвета", keywords = "роман"),
            CatalogBook(2, "Ботев", "Стихове", annotation = "поезия за свободата"),
        )
        val engine = CatalogSearchEngine(CatalogSnapshot("Б", "Я", "", books, emptyList()))
        // Думите от различни полета (автор + издател) пак намират записа.
        assertEquals(listOf(1L), engine.search(CatalogQuery("вазов просвета")).map { it.inv })
        assertEquals(listOf(2L), engine.search(CatalogQuery("свободата")).map { it.inv })
        assertEquals(emptyList(), engine.search(CatalogQuery("вазов свободата")).map { it.inv })
        assertEquals(listOf("Вазов, Иван"), engine.authorIndex.filter { it.second.contains("вазов") }.map { it.first })
    }

    // --- CachedResource ---

    /** Брои четенията (за проверка на паметта). */
    private class CountingCache(private val inner: InMemoryPayloadCache) : PayloadCache by inner {
        var reads = 0
        override suspend fun read(key: String): CachedPayload? { reads++; return inner.read(key) }
    }

    @Test fun cachedResourceMemoizesUntilTheCacheChanges() = runTest {
        val cache = CountingCache(InMemoryPayloadCache { clock.now().toEpochMilli() })
        val res = CachedResource(cache, "k", ListSerializer(String.serializer()), clock, 60_000)
        res.load(true) { Outcome.Success(listOf("a")) }
        val reads = cache.reads
        repeat(3) { assertEquals(listOf("a"), res.cached()!!.data) }
        assertEquals(reads, cache.reads, "повторното четене е от паметта")
        res.updateCached { it + "b" }
        assertEquals(listOf("a", "b"), res.cached()!!.data)
        cache.clear()
        assertNull(res.cached(), "изчистеният кеш изчиства и паметта")
        res.load(true) { Outcome.Success(listOf("c")) }
        res.clear()
        assertNull(res.cached())
    }

    @Test fun cachedResourceSkipsRewritingUnchangedContent() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val res = CachedResource(cache, "k", ListSerializer(String.serializer()), clock, 60_000)
        res.load(true) { Outcome.Success(listOf("a")) }
        assertEquals(1, cache.writes)
        clock.advanceSeconds(120)
        val r = (res.load(false) { Outcome.Success(listOf("a")) } as Outcome.Success).value
        assertEquals(1, cache.writes, "същото съдържание не се презаписва")
        assertEquals(clock.now(), r.syncedAt)
        assertEquals(clock.now().toEpochMilli(), cache.read("k")!!.savedAtMillis, "но времето се опреснява")
        // Прозорецът на свежест тече от новото време.
        var calls = 0
        res.load(false) { calls++; Outcome.Success(listOf("x")) }
        assertEquals(0, calls)
        res.load(true) { Outcome.Success(listOf("b")) }
        assertEquals(2, cache.writes)
    }

    @Test fun concurrentLoadsOfOneKeyAreMerged() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        // Два отделни обекта за един ключ (напр. два ViewModel-а за една страница).
        val a = CachedResource(cache, "page", String.serializer(), clock, 60_000)
        val b = CachedResource(cache, "page", String.serializer(), clock, 60_000)
        val gate = CompletableDeferred<Unit>()
        var calls = 0
        val first = async { a.load(true) { calls++; gate.await(); Outcome.Success("p") } }
        yield()
        val second = async { b.load(true) { calls++; Outcome.Success("q") } }
        yield()
        gate.complete(Unit)
        assertEquals("p", (first.await() as Outcome.Success).value.data)
        assertEquals("p", (second.await() as Outcome.Success).value.data)
        assertEquals(1, calls, "второто зареждане ползва резултата на първото")
    }

    // --- Галерия и празник ---

    @Test fun srcsetThumbnailIsTheSmallestCandidate() {
        assertEquals("s.jpg", HtmlContentExtractor.smallestSrcsetCandidate("m.jpg 640w, s.jpg 320w, l.jpg 1280w"))
        assertEquals("a.jpg", HtmlContentExtractor.smallestSrcsetCandidate("a.jpg, b.jpg 2x"))
        assertEquals("a,1.jpg", HtmlContentExtractor.smallestSrcsetCandidate("a,1.jpg 100w, b.jpg 200w"))
        assertNull(HtmlContentExtractor.smallestSrcsetCandidate("  "))
        val base = "https://chyavorec.org"
        val doc = Jsoup.parse(
            """<img src="/img/big.jpg" srcset="/img/big.jpg 1600w, /img/small.jpg 400w"><img src="/img/plain.jpg">""",
            base,
        )
        val ex = HtmlContentExtractor(base)
        val imgs = doc.select("img")
        assertEquals("https://chyavorec.org/img/small.jpg", ex.thumbnailUrl(imgs[0]))
        assertEquals("https://chyavorec.org/img/big.jpg", ex.imageUrl(imgs[0]))
        assertNull(ex.thumbnailUrl(imgs[1]))
    }

    private class FeastSite : SiteContentService {
        var feastCalls = 0
        var fail = false
        override suspend fun fetchFeast(date: String): Outcome<DailyFeast> {
            feastCalls++
            return if (fail) Outcome.Failure(AppError.Network) else Outcome.Success(DailyFeast(date, "Св. $date"))
        }
        override suspend fun discoverLinks(): Outcome<List<SiteLink>> = error("-")
        override suspend fun fetchPage(url: String): Outcome<SitePage> = error("-")
        override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> = error("-")
        override suspend fun fetchGallery(news: List<NewsArticle>?): Outcome<List<GalleryPhoto>> = error("-")
        override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> = error("-")
        override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = error("-")
        override suspend fun fetchMessages(): Outcome<List<org.chyavorec.domain.model.AppMessage>> = error("-")
    }

    @Test fun feastIsCachedPerDateAndWorksOffline() = runTest {
        val cache = InMemoryPayloadCache { clock.now().toEpochMilli() }
        val site = FeastSite()
        val repo = SiteRepository(site, cache, clock)
        assertEquals("Св. 2026-9-26", repo.feastToday()?.line)
        assertEquals("Св. 2026-9-26", repo.feastToday()?.line)
        assertEquals(1, site.feastCalls)
        assertNotNull(cache.read("feast:2026-09-26"))
        // Офлайн (и след прозореца на свежест) — последно наличното за същия ден.
        site.fail = true
        clock.advanceSeconds(60)
        assertEquals("Св. 2026-9-26", SiteRepository(site, cache, clock).feastToday()?.line)
        // Нов ден: старият запис се изтрива, новият не е наличен офлайн.
        clock.advanceSeconds(24 * 3600)
        assertNull(SiteRepository(site, cache, clock).feastToday())
        assertNull(cache.read("feast:2026-09-26"))
    }
}
