package org.chyavorec.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.catalog.CatalogIndexRows
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogIndexStore
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.FtsCatalogSearcher
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.catalog.katalogHash
import org.chyavorec.domain.model.CatalogHomeSummary
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.service.CatalogFetch
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.CatalogValidators
import java.lang.ref.SoftReference
import java.time.Instant

/** Каталогът се обновява рядко (няколко пъти седмично) и е голям — по-дълъг прозорец. */
private const val CATALOG_FRESH = 30 * 60 * 1000L

/** Какво е записано до katalog.json: SHA-256 на байтовете и ETag-ът (за условна заявка). */
@Serializable
internal data class CatalogMeta(val hash: String, val etag: String? = null, val etagUrl: String? = null)

/**
 * Каталогът се държи в паметта като [CatalogSearchEngine]; суровият JSON се
 * кешира на диска, за да работи търсенето и офлайн (с ясна дата на данните).
 *
 * Моментът на последното успешно сваляне се пази до файла ([FETCHED_AT_KEY]), така че
 * прозорецът [CATALOG_FRESH] важи и след рестарт: по-пресен дисков кеш не се сваля
 * отново. До файла се пазят и SHA-256 на байтовете и ETag-ът ([META_KEY]): заявката
 * е условна (304 — тялото не се чете), а същото съдържание не се разчита, индексира и
 * презаписва повторно; хешът не се преизчислява при зареждане от диска.
 *
 * Началният екран чете само малкото обобщение ([homeSummary], [SUMMARY_KEY]), записано
 * заедно с каталога — индексът за търсене се строи едва при първо търсене/книга/скенер
 * ([catalog], [cached]) и може да се освободи при недостиг на памет ([trimMemory]).
 * Разчитането и индексът се строят на [work], не на главната нишка.
 *
 * С [index] (Room FTS4 в приложението) екранът „Каталог“ търси през пълнотекстовия
 * индекс ([searcher], [cachedSearcher]) и не строи индекса в паметта. Индексът в базата
 * се попълва (в една транзакция, на [work]) само когато хешът на каталога се смени; при
 * грешка на базата търсенето минава през индекса в паметта.
 */
class CatalogRepository(
    private val service: CatalogService,
    private val cache: PayloadCache,
    private val clock: AppClock,
    private val work: CoroutineDispatcher = Dispatchers.Default,
    private val index: CatalogIndexStore? = null,
) {
    private enum class Want { SUMMARY, ENGINE, SEARCH }

    /** Търсачката за екрана на каталога и хешът на каталога, за който е. */
    private class PublishedSearcher(val hash: String?, val synced: Synced<CatalogSearcher>)

    private val mutex = Mutex()
    @Volatile private var current: Synced<CatalogSearchEngine>? = null
    @Volatile private var searcher: PublishedSearcher? = null
    @Volatile private var summary: Synced<CatalogHomeSummary>? = null
    /** Последно разчетеният каталог (съответства на [meta]) — за да не се разчита пак скоро след това. */
    @Volatile private var lastSnapshot: SoftReference<CatalogSnapshot>? = null

    // Полетата по-долу се ползват само под [mutex].
    private var meta: CatalogMeta? = null
    private var metaLoaded = false
    /** Кога каталогът е потвърден от мрежата за последно (epoch ms). */
    private var lastFetchMillis: Long? = null
    private var fetchedAtLoaded = false

    fun inMemory(): Synced<CatalogSearchEngine>? = current

    /** Индексът от паметта или от диска (без мрежа). */
    suspend fun cached(): Synced<CatalogSearchEngine>? = mutex.withLock {
        current ?: loadFromDisk()
    }

    /** Обобщението за началния екран (от паметта или диска) — без индекса за търсене. */
    suspend fun homeSummary(): Synced<CatalogHomeSummary>? = summary ?: mutex.withLock { summary ?: loadSummaryFromDisk() }

    /** Опреснява каталога (при нужда) за началния екран, без да строи индекса за търсене. */
    suspend fun refreshHomeSummary(force: Boolean = false): Outcome<Synced<CatalogHomeSummary>> = mutex.withLock {
        when (val r = sync(force, Want.SUMMARY)) {
            is Outcome.Failure -> r
            is Outcome.Success -> (summary ?: loadSummaryFromDisk())?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }
    }

    suspend fun catalog(force: Boolean = false): Outcome<Synced<CatalogSearchEngine>> = mutex.withLock {
        when (val r = sync(force, Want.ENGINE)) {
            is Outcome.Failure -> r
            is Outcome.Success -> (current ?: loadFromDisk())?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }
    }

    /** Търсачката за екрана на каталога от паметта, базата или диска (без мрежа). */
    suspend fun cachedSearcher(): Synced<CatalogSearcher>? = mutex.withLock { loadSearcher() }

    /**
     * Опреснява каталога (при нужда) и дава търсачката за екрана на каталога: през
     * пълнотекстовия индекс, ако има такъв, иначе — индекса в паметта.
     */
    suspend fun searcher(force: Boolean = false): Outcome<Synced<CatalogSearcher>> = mutex.withLock {
        when (val r = sync(force, Want.SEARCH)) {
            is Outcome.Failure -> r
            is Outcome.Success -> loadSearcher()?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }
    }

    /**
     * Освобождава индекса за търсене (напр. приложението е във фонов режим). При
     * следваща нужда се зарежда отново от диска; обобщението за началния екран остава.
     */
    fun trimMemory() {
        current = null
        lastSnapshot = null
        // Търсачката през базата е лека и остава; тази през индекса в паметта — не.
        if (searcher?.synced?.data is EngineCatalogSearcher) searcher = null
    }

    /** Под [mutex]. При успех търсеното ([want]) е в паметта. */
    private suspend fun sync(force: Boolean, want: Want): Outcome<Unit> {
        val m = loadMeta()
        val last = loadFetchedAt()
        val now = clock.now().toEpochMilli()
        if (!force && m != null && last != null && now - last in 0 until CATALOG_FRESH && ensureLoaded(want)) {
            markSynced(Instant.ofEpochMilli(last))
            return Outcome.Success(Unit)
        }
        var validators = m?.let { CatalogValidators(it.hash, it.etag, it.etagUrl) }
        repeat(2) {
            when (val r = service.fetchCatalogConditional(validators)) {
                is Outcome.Failure -> {
                    if (!ensureLoaded(want)) return r
                    markStale(r.error)
                    return Outcome.Success(Unit)
                }
                is Outcome.Success -> when (val f = r.value) {
                    is CatalogFetch.Unchanged -> {
                        if (validators != null && ensureLoaded(want)) {
                            confirmUnchanged(f.etag, f.etagUrl)
                            return Outcome.Success(Unit)
                        }
                        // Копието на диска липсва или е повредено — сваляме изцяло, безусловно.
                        validators = null
                    }
                    is CatalogFetch.Changed -> {
                        if (f.hash == meta?.hash && ensureLoaded(want)) {
                            // Същото съдържание — пазим готовия индекс и не презаписваме файла.
                            confirmUnchanged(f.etag, f.etagUrl)
                        } else {
                            applyChanged(f, want)
                        }
                        return Outcome.Success(Unit)
                    }
                }
            }
        }
        return Outcome.Failure(AppError.Parse("katalog.json"))
    }

    private suspend fun ensureLoaded(want: Want): Boolean = when (want) {
        Want.ENGINE -> (current ?: loadFromDisk()) != null
        Want.SUMMARY -> (summary ?: loadSummaryFromDisk()) != null
        Want.SEARCH -> loadSearcher() != null
    }

    private fun markSynced(at: Instant) {
        current = current?.let { Synced(it.data, at) }
        summary = summary?.let { Synced(it.data, at) }
        searcher = searcher?.let { PublishedSearcher(it.hash, Synced(it.synced.data, at)) }
    }

    private fun markStale(error: AppError) {
        current = current?.copy(fromCache = true, refreshError = error)
        summary = summary?.copy(fromCache = true, refreshError = error)
        searcher = searcher?.let { PublishedSearcher(it.hash, it.synced.copy(fromCache = true, refreshError = error)) }
    }

    private suspend fun confirmUnchanged(etag: String?, etagUrl: String?) {
        val m = meta
        if (m != null && etag != null && (etag != m.etag || etagUrl != m.etagUrl)) saveMeta(m.copy(etag = etag, etagUrl = etagUrl))
        val at = clock.now()
        saveFetchedAt(at.toEpochMilli())
        markSynced(at)
    }

    private suspend fun applyChanged(f: CatalogFetch.Changed, want: Want) {
        // Редът на записите е важен: суровият файл, обобщението, после хешът/ETag-ът —
        // прекъсване по средата води само до повторно (безусловно) сваляне.
        val rawSaved = runCatching { cache.writeBytes(KEY, f.raw) }.isSuccess
        val s = withContext(work) { CatalogSearchEngine.homeSummary(f.snapshot) }
        saveSummary(s)
        if (rawSaved) saveMeta(CatalogMeta(f.hash, f.etag, f.etagUrl)) else {
            meta = null
            runCatching { cache.remove(META_KEY) }
        }
        val at = clock.now()
        saveFetchedAt(at.toEpochMilli())
        lastSnapshot = SoftReference(f.snapshot)
        summary = Synced(s, at)
        // Индексът в паметта се строи само ако е поискан или вече е бил в паметта (екранът
        // на каталога без база); търсачката — ако е поискана или вече е била в ход.
        val hadSearcher = searcher != null
        searcher = null
        val engineNeeded = want == Want.ENGINE || current != null || (want == Want.SEARCH && index == null)
        current = if (engineNeeded) Synced(withContext(work) { CatalogSearchEngine(f.snapshot) }, at) else null
        if (want == Want.SEARCH || hadSearcher) loadSearcher(fresh = f.snapshot, freshAt = at)
    }

    /**
     * Търсачката за текущия каталог (под [mutex]): от паметта; през пълнотекстовия индекс,
     * ако е за същия хеш; иначе индексът се (пре)попълва от разчетения каталог; ако базата
     * не работи — през индекса в паметта. [fresh] — току-що свален каталог.
     */
    private suspend fun loadSearcher(fresh: CatalogSnapshot? = null, freshAt: Instant? = null): Synced<CatalogSearcher>? {
        val m = loadMeta()
        searcher?.let { if (it.hash == m?.hash) return it.synced }
        if (index != null) {
            val ready = m?.let { meta -> guarded { index.state() }?.takeIf { it.hash == meta.hash } }
                ?: run {
                    val snapshot = fresh ?: lastSnapshot?.get() ?: current?.data?.snapshot ?: loadSnapshotFromDisk() ?: return null
                    // Хешът може да е записан едва при разчитането на диска (кеш от по-стара версия).
                    loadMeta()?.hash?.let { hash -> populate(snapshot, hash) }
                }
            if (ready != null) {
                val fts = FtsCatalogSearcher(index, ready, fallback = { mutex.withLock { (current ?: loadFromDisk())?.data } }, work = work)
                return publish(fts, ready.hash, freshAt)
            }
        }
        val engine = current ?: loadFromDisk() ?: return null
        return publish(EngineCatalogSearcher(engine.data), loadMeta()?.hash, freshAt ?: engine.syncedAt, freshAt == null && engine.fromCache)
    }

    private suspend fun publish(s: CatalogSearcher, hash: String?, at: Instant?, fromCache: Boolean = at == null): Synced<CatalogSearcher> {
        val time = at ?: Instant.ofEpochMilli(loadFetchedAt() ?: clock.now().toEpochMilli())
        val synced = Synced(s, time, fromCache = fromCache)
        searcher = PublishedSearcher(hash, synced)
        return synced
    }

    /** Попълва пълнотекстовия индекс от [snapshot] (една транзакция); `null` — неуспех. */
    private suspend fun populate(snapshot: CatalogSnapshot, hash: String): CatalogIndexState? {
        val store = index ?: return null
        return guarded {
            val (state, rows) = withContext(work) {
                CatalogIndexState(hash, snapshot.generatedOn, snapshot.books.size, CatalogSearchEngine.facetsOf(snapshot)) to
                    CatalogIndexRows.build(snapshot)
            }
            store.replace(state, rows)
            state
        }
    }

    /** Грешка на базата не спира каталога — връща `null` (без да поглъща отмяната). */
    private suspend fun <T> guarded(block: suspend () -> T): T? =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }

    /** Разчита каталога от диска (под [mutex]); записва хеша, ако липсва (кеш от по-стара версия). */
    private suspend fun loadSnapshotFromDisk(): CatalogSnapshot? {
        val payload = runCatching { cache.readBytes(KEY) }.getOrNull() ?: return null
        val m = loadMeta()
        val parsed = withContext(work) {
            runCatching { KatalogParser.parse(payload.bytes) to (if (m == null) katalogHash(payload.bytes) else null) }.getOrNull()
        } ?: return null
        parsed.second?.let { saveMeta(CatalogMeta(it)) }
        lastSnapshot = SoftReference(parsed.first)
        return parsed.first
    }

    /** Зарежда дисковия кеш в паметта като индекс (под [mutex]). */
    private suspend fun loadFromDisk(): Synced<CatalogSearchEngine>? {
        val m = loadMeta()
        val fetchedAt = loadFetchedAt()
        val ready = lastSnapshot?.get()
        val engine: CatalogSearchEngine
        val savedAt: Long
        if (ready != null && m != null) {
            engine = withContext(work) { CatalogSearchEngine(ready) }
            savedAt = fetchedAt ?: clock.now().toEpochMilli()
        } else {
            val payload = runCatching { cache.readBytes(KEY) }.getOrNull() ?: return null
            val loaded = withContext(work) {
                runCatching {
                    val snapshot = KatalogParser.parse(payload.bytes)
                    // Хешът се смята само веднъж — ако липсва (кеш от по-стара версия).
                    Triple(snapshot, CatalogSearchEngine(snapshot), if (m == null) katalogHash(payload.bytes) else null)
                }.getOrNull()
            } ?: return null
            loaded.third?.let { saveMeta(CatalogMeta(it)) }
            lastSnapshot = SoftReference(loaded.first)
            engine = loaded.second
            savedAt = fetchedAt ?: payload.savedAtMillis
        }
        if (lastFetchMillis == null) lastFetchMillis = savedAt
        val synced = Synced(engine, Instant.ofEpochMilli(savedAt), fromCache = true)
        current = synced
        return synced
    }

    /** Зарежда обобщението от диска; ако го няма (кеш от по-стара версия) — създава го (под [mutex]). */
    private suspend fun loadSummaryFromDisk(): Synced<CatalogHomeSummary>? {
        val fetchedAt = loadFetchedAt()
        val stored = runCatching { cache.read(SUMMARY_KEY) }.getOrNull()
        if (stored != null) {
            val s = withContext(work) { runCatching { cacheJson.decodeFromString(CatalogHomeSummary.serializer(), stored.text) }.getOrNull() }
            if (s != null) return Synced(s, Instant.ofEpochMilli(fetchedAt ?: stored.savedAtMillis), fromCache = true).also { summary = it }
        }
        val snapshot = current?.data?.snapshot ?: lastSnapshot?.get() ?: loadSnapshotFromDisk() ?: return null
        val s = withContext(work) { CatalogSearchEngine.homeSummary(snapshot) }
        saveSummary(s)
        return Synced(s, Instant.ofEpochMilli(fetchedAt ?: clock.now().toEpochMilli()), fromCache = true).also { summary = it }
    }

    private suspend fun loadMeta(): CatalogMeta? {
        if (!metaLoaded) {
            metaLoaded = true
            meta = runCatching { cache.read(META_KEY)?.text?.let { cacheJson.decodeFromString(CatalogMeta.serializer(), it) } }.getOrNull()
        }
        return meta
    }

    private suspend fun saveMeta(m: CatalogMeta) {
        meta = m
        metaLoaded = true
        runCatching { cache.write(META_KEY, cacheJson.encodeToString(CatalogMeta.serializer(), m)) }
    }

    private suspend fun loadFetchedAt(): Long? {
        if (!fetchedAtLoaded) {
            fetchedAtLoaded = true
            lastFetchMillis = runCatching { cache.read(FETCHED_AT_KEY)?.text?.trim()?.toLongOrNull() }.getOrNull()
        }
        return lastFetchMillis
    }

    private suspend fun saveFetchedAt(millis: Long) {
        lastFetchMillis = millis
        fetchedAtLoaded = true
        runCatching { cache.write(FETCHED_AT_KEY, millis.toString()) }
    }

    private suspend fun saveSummary(s: CatalogHomeSummary) {
        runCatching { cache.write(SUMMARY_KEY, withContext(work) { cacheJson.encodeToString(CatalogHomeSummary.serializer(), s) }) }
    }

    companion object {
        const val KEY = "catalog:katalog.json"
        const val FETCHED_AT_KEY = "catalog:fetchedAt"
        /** SHA-256 и ETag на [KEY]. */
        const val META_KEY = "catalog:meta"
        /** Обобщението за началния екран ([CatalogHomeSummary]). */
        const val SUMMARY_KEY = "catalog:home"
    }
}
