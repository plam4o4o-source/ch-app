package org.chyavorec.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.core.logFailure
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
internal data class CatalogMeta(
    val hash: String,
    val etag: String? = null,
    val etagUrl: String? = null,
    /** Датата `generated` на записания каталог (за сверка с копие от по-старото място). */
    val generatedOn: String? = null,
)

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
 * индекс ([searcher], [cachedSearcher]), а книгата, скенерът, глобалното търсене и
 * историята правят справки през него ([lookupSearcher]) — без да строят индекса в
 * паметта. Индексът в базата се попълва във фонов режим (в една транзакция) веднага
 * щом се приеме нов каталог (и за началния екран); дотогава екранът „Каталог“ търси в
 * стария индекс (отбелязан като кеширан), после търсачката се подменя
 * ([searcherUpdates]). При грешка на базата търсенето минава през индекса в паметта.
 *
 * Свалянето от мрежата е най-много едно наведнъж (общо за всички чакащи) и е извън
 * ключалката: четенето на кешираното никога не чака мрежата — ключалката се взима
 * само за кратко, за да се приложи свален резултат.
 */
class CatalogRepository(
    private val service: CatalogService,
    private val cache: PayloadCache,
    private val clock: AppClock,
    private val work: CoroutineDispatcher = Dispatchers.Default,
    private val index: CatalogIndexStore? = null,
    /** Фоновата работа (общото сваляне, попълването на индекса); тестовете подават свой обхват. */
    scope: CoroutineScope? = null,
) {
    private enum class Want { SUMMARY, ENGINE, SEARCH }

    /**
     * Търсачката за екрана на каталога и хешът на каталога, за който е. [stale] — старият
     * индекс в базата, докато новият се попълва; [synced] е свежестта на самите данни.
     */
    private class PublishedSearcher(val hash: String?, val synced: Synced<CatalogSearcher>, val stale: Boolean = false) {
        /** Каквото вижда екранът: старият индекс — като кеширан. */
        val view: Synced<CatalogSearcher> get() = if (stale) synced.copy(fromCache = true) else synced
    }

    /** Търсачката е готова или първото попълване на индекса още тече. */
    private sealed interface Found {
        class Ready(val synced: Synced<CatalogSearcher>) : Found
        class Building(val job: Deferred<CatalogIndexState?>) : Found
    }

    /** Сваляне в ход (с какви условия е пуснато) — общо за всички, които го чакат. */
    private class Fetch(val validators: CatalogValidators?, val result: Deferred<Outcome<CatalogFetch>>)

    /** Попълване на индекса в ход — за каталога с хеш [hash]. */
    private class Rebuild(val hash: String?, val job: Deferred<CatalogIndexState?>)

    private val background = scope ?: CoroutineScope(SupervisorJob() + work)
    private val mutex = Mutex()
    @Volatile private var current: Synced<CatalogSearchEngine>? = null
    private val published = MutableStateFlow<PublishedSearcher?>(null)
    @Volatile private var summary: Synced<CatalogHomeSummary>? = null
    /** Последно разчетеният каталог (съответства на [meta]) — за да не се разчита пак скоро след това. */
    @Volatile private var lastSnapshot: SoftReference<CatalogSnapshot>? = null

    // Полетата по-долу се ползват само под [mutex].
    private var meta: CatalogMeta? = null
    private var metaLoaded = false
    /** Кога каталогът е потвърден от мрежата за последно (epoch ms). */
    private var lastFetchMillis: Long? = null
    private var fetchedAtLoaded = false
    /** Свежестта на каталога в този процес (последното опресняване) — за търсачките, публикувани след него. */
    private var freshness: Synced<Unit>? = null
    private var fetch: Fetch? = null
    private var rebuild: Rebuild? = null
    /** Хешът, за който попълването на индекса последно е пропаднало (справките не опитват пак). */
    private var failedHash: String? = null

    fun inMemory(): Synced<CatalogSearchEngine>? = current

    /** Индексът от паметта или от диска (без мрежа). */
    suspend fun cached(): Synced<CatalogSearchEngine>? = mutex.withLock {
        current ?: loadFromDisk()
    }

    /** Обобщението за началния екран (от паметта или диска) — без индекса за търсене. */
    suspend fun homeSummary(): Synced<CatalogHomeSummary>? = summary ?: mutex.withLock { summary ?: loadSummaryFromDisk() }

    /** Опреснява каталога (при нужда) за началния екран, без да строи индекса за търсене. */
    suspend fun refreshHomeSummary(force: Boolean = false): Outcome<Synced<CatalogHomeSummary>> =
        when (val r = sync(force, Want.SUMMARY)) {
            is Outcome.Failure -> r
            is Outcome.Success -> (summary ?: mutex.withLock { summary ?: loadSummaryFromDisk() })?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }

    suspend fun catalog(force: Boolean = false): Outcome<Synced<CatalogSearchEngine>> =
        when (val r = sync(force, Want.ENGINE)) {
            is Outcome.Failure -> r
            is Outcome.Success -> mutex.withLock { current ?: loadFromDisk() }?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }

    /** Търсачката за екрана на каталога от паметта, базата или диска (без мрежа). */
    suspend fun cachedSearcher(): Synced<CatalogSearcher>? = resolveSearcher()

    /**
     * Опреснява каталога (при нужда) и дава търсачката за екрана на каталога: през
     * пълнотекстовия индекс, ако има такъв, иначе — индекса в паметта.
     */
    suspend fun searcher(force: Boolean = false): Outcome<Synced<CatalogSearcher>> =
        when (val r = sync(force, Want.SEARCH)) {
            is Outcome.Failure -> r
            is Outcome.Success -> resolveSearcher()?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }

    /** Последно публикуваната търсачка (без чакане). */
    fun publishedSearcher(): Synced<CatalogSearcher>? = published.value?.view

    /** Всяка нова публикувана търсачка — напр. след фоновото попълване на индекса. */
    val searcherUpdates: Flow<Synced<CatalogSearcher>> = published.filterNotNull().map { it.view }

    /**
     * Справки (книга, скенер, глобално търсене, история): през пълнотекстовия индекс за
     * текущия каталог — без мрежа и без да разчита katalog.json (започнатото попълване на
     * индекса се изчаква). Само ако индекс още няма — както досега: индексът в паметта,
     * от диска и (без кеш, ако [network]) от мрежата.
     */
    suspend fun lookupSearcher(network: Boolean = true): Synced<CatalogSearcher>? =
        indexed() ?: (inMemory() ?: cached() ?: if (network) (catalog(false) as? Outcome.Success)?.value else null)
            ?.map { EngineCatalogSearcher(it) }

    /**
     * Освобождава индекса за търсене (напр. приложението е във фонов режим). При
     * следваща нужда се зарежда отново от диска; обобщението за началния екран остава.
     */
    fun trimMemory() {
        current = null
        lastSnapshot = null
        // Търсачката през базата е лека и остава; тази през индекса в паметта — не.
        if (published.value?.synced?.data is EngineCatalogSearcher) published.value = null
    }

    /** Всяко изчистване ([clear]) — екранът на каталога забравя търсачката и резултатите си. */
    val cleared: Flow<Unit> get() = clears

    private val clears = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    /**
     * „Изчисти кеша“: katalog.json и записаното до него (хеш/ETag, времето на сваляне,
     * обобщението за началния екран и „За теб“), индексът в базата и всичко в паметта.
     * Започнатото попълване на индекса се спира (не бива да върне стария каталог след
     * изчистването). Следващото търсене/отваряне сваля каталога наново — от нулата.
     */
    suspend fun clear() {
        val running = mutex.withLock {
            val job = rebuild?.job
            rebuild = null
            fetch = null
            job
        }
        running?.cancelAndJoin()
        mutex.withLock {
            // И на по-ранно запланувано попълване (верижно след спряното).
            rebuild?.job?.cancel()
            rebuild = null
            listOf(KEY, META_KEY, FETCHED_AT_KEY, SUMMARY_KEY).forEach { runCatching { cache.remove(it) }.logFailure("catalog.clear") }
            index?.let { store -> guarded { store.clear() } }
            meta = null
            metaLoaded = true
            lastFetchMillis = null
            fetchedAtLoaded = true
            freshness = null
            failedHash = null
            current = null
            summary = null
            lastSnapshot = null
            published.value = null
        }
        clears.tryEmit(Unit)
    }

    /** Ключалката се взима само за проверка и за прилагане на резултата — мрежата е извън нея. */
    private suspend fun sync(force: Boolean, want: Want): Outcome<Unit> {
        var validators: CatalogValidators? = null
        var pending = mutex.withLock {
            val m = loadMeta()
            val last = loadFetchedAt()
            val now = clock.now().toEpochMilli()
            if (!force && m != null && last != null && now - last in 0 until CATALOG_FRESH && ensureLoaded(want)) {
                markSynced(Instant.ofEpochMilli(last))
                return Outcome.Success(Unit)
            }
            validators = m?.let { CatalogValidators(it.hash, it.etag, it.etagUrl, it.generatedOn ?: knownGeneratedOn(it)) }
            startFetch(validators)
        }
        repeat(2) {
            val r = pending.await()
            mutex.withLock {
                when (r) {
                    is Outcome.Failure -> {
                        if (!ensureLoaded(want)) return r
                        markStale(r.error)
                        return Outcome.Success(Unit)
                    }
                    is Outcome.Success -> when (val f = r.value) {
                        is CatalogFetch.Unchanged -> {
                            val v = validators
                            if (v != null && v.hash == meta?.hash && ensureLoaded(want)) {
                                confirmUnchanged(f.etag, f.etagUrl)
                                return Outcome.Success(Unit)
                            }
                            // Копието на диска липсва или е повредено — сваляме изцяло, безусловно.
                            validators = null
                            pending = startFetch(null)
                        }
                        is CatalogFetch.Changed -> {
                            if (f.hash == meta?.hash && ensureLoaded(want)) {
                                // Същото съдържание (напр. вече приложено за друг чакащ) — пазим готовия индекс.
                                confirmUnchanged(f.etag, f.etagUrl)
                            } else {
                                applyChanged(f, want)
                            }
                            return Outcome.Success(Unit)
                        }
                    }
                }
            }
        }
        return Outcome.Failure(AppError.Parse("katalog.json"))
    }

    /**
     * Под [mutex]. Датата на кеширания каталог за запис от по-стара версия (без нея в [meta]) —
     * от обобщението; записва се, за да не се търси отново.
     */
    private suspend fun knownGeneratedOn(m: CatalogMeta): String? {
        val generated = (summary ?: loadSummaryFromDisk())?.data?.generatedOn?.takeIf { it.isNotBlank() } ?: return null
        if (meta?.hash == m.hash) saveMeta(m.copy(generatedOn = generated))
        return generated
    }

    /** Под [mutex]. Едно сваляне наведнъж: същата заявка, вече в ход, се споделя. */
    private fun startFetch(validators: CatalogValidators?): Deferred<Outcome<CatalogFetch>> {
        fetch?.let { if (it.validators == validators && it.result.isActive) return it.result }
        val result = background.async { service.fetchCatalogConditional(validators) }
        fetch = Fetch(validators, result)
        return result
    }

    private suspend fun ensureLoaded(want: Want): Boolean = when (want) {
        Want.ENGINE -> (current ?: loadFromDisk()) != null
        Want.SUMMARY -> (summary ?: loadSummaryFromDisk()) != null
        Want.SEARCH -> searchAvailable()
    }

    /** Под [mutex]: има ли от какво да се търси — без да се попълва индексът. */
    private suspend fun searchAvailable(): Boolean {
        if (index == null) return (current ?: loadFromDisk()) != null
        published.value?.let { if (!it.stale && it.hash == loadMeta()?.hash) return true }
        if (guarded { index.state() } != null) return true
        return (inMemorySnapshot() ?: loadSnapshotFromDisk()) != null
    }

    private fun markSynced(at: Instant) {
        current = current?.let { Synced(it.data, at) }
        summary = summary?.let { Synced(it.data, at) }
        freshness = Synced(Unit, at)
        published.value = published.value?.let { PublishedSearcher(it.hash, Synced(it.synced.data, at), it.stale) }
    }

    private fun markStale(error: AppError) {
        current = current?.copy(fromCache = true, refreshError = error)
        summary = summary?.copy(fromCache = true, refreshError = error)
        val at = freshness?.syncedAt ?: Instant.ofEpochMilli(lastFetchMillis ?: clock.now().toEpochMilli())
        freshness = Synced(Unit, at, fromCache = true, refreshError = error)
        published.value = published.value?.let { PublishedSearcher(it.hash, it.synced.copy(fromCache = true, refreshError = error), it.stale) }
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
        val rawSaved = runCatching { cache.writeBytes(KEY, f.raw) }.logFailure("catalog.save").isSuccess
        val s = withContext(work) { CatalogSearchEngine.homeSummary(f.snapshot) }
        saveSummary(s)
        if (rawSaved) saveMeta(CatalogMeta(f.hash, f.etag, f.etagUrl, f.snapshot.generatedOn)) else {
            meta = null
            runCatching { cache.remove(META_KEY) }
        }
        val at = clock.now()
        saveFetchedAt(at.toEpochMilli())
        freshness = Synced(Unit, at)
        lastSnapshot = SoftReference(f.snapshot)
        summary = Synced(s, at)
        // Пълнотекстовият индекс се попълва веднага, във фонов режим — за който и екран да е свален каталогът.
        if (index != null && rawSaved) rebuildIndex(f.snapshot, f.hash)
        // Индексът в паметта се строи само ако е поискан или вече е бил в паметта (екранът
        // на каталога без база); търсачката — ако е поискана или вече е била в ход.
        val hadSearcher = published.value != null
        published.value = null
        val engineNeeded = want == Want.ENGINE || current != null || (want == Want.SEARCH && index == null)
        current = if (engineNeeded) Synced(withContext(work) { CatalogSearchEngine(f.snapshot) }, at) else null
        if (want == Want.SEARCH || hadSearcher) findSearcher(build = true)
    }

    /** Търсачката за екрана на каталога; първото попълване на индекса се чака извън ключалката. */
    private suspend fun resolveSearcher(): Synced<CatalogSearcher>? {
        val job = mutex.withLock {
            when (val f = findSearcher(build = true)) {
                is Found.Ready -> return f.synced
                is Found.Building -> f.job
                null -> return null
            }
        }
        job.await()
        return mutex.withLock { (findSearcher(build = false) as? Found.Ready)?.synced }
    }

    /**
     * Търсачката за текущия каталог (под [mutex]): публикуваната; през пълнотекстовия
     * индекс, ако е за същия хеш; ако индексът е за стар каталог — пак той, докато новият
     * се попълва във фонов режим (пуска се при [build]); ако индекс няма — попълването му
     * ([Found.Building], при [build]); иначе (или ако базата не работи) — индексът в паметта.
     */
    private suspend fun findSearcher(build: Boolean): Found? {
        val m = loadMeta()
        published.value?.let { if (!it.stale && it.hash == m?.hash) return Found.Ready(it.view) }
        if (index != null) {
            val st = guarded { index.state() }
            if (st != null && m != null && st.hash == m.hash) return Found.Ready(publishIndex(st, stale = false))
            if (st != null) {
                if (build) rebuildIndex(inMemorySnapshot(), m?.hash)
                // Същата търсачка за стария индекс, ако вече е публикувана (екранът не търси пак напразно).
                val old = published.value?.takeIf { it.stale && it.hash == st.hash }
                return Found.Ready(old?.view ?: publishIndex(st, stale = true))
            }
            if (build) {
                val snapshot = inMemorySnapshot() ?: loadSnapshotFromDisk() ?: return null
                // Хешът може да е записан едва при разчитането на диска (кеш от по-стара версия).
                val hash = loadMeta()?.hash ?: return null
                return Found.Building(rebuildIndex(snapshot, hash))
            }
        }
        val engine = current ?: loadFromDisk() ?: return null
        return Found.Ready(publish(EngineCatalogSearcher(engine.data), loadMeta()?.hash, stale = false))
    }

    /**
     * Справките през индекса в базата за текущия каталог; `null` — индекс няма (и не може
     * да се попълни) или е грешка на базата. Попълването се чака извън ключалката.
     */
    private suspend fun indexed(): Synced<CatalogSearcher>? {
        val store = index ?: return null
        val job = mutex.withLock {
            val m = loadMeta() ?: return null
            published.value?.let { if (!it.stale && it.hash == m.hash) return it.view }
            val st = guarded { store.state() }
            if (st != null && st.hash == m.hash) return publishIndex(st, stale = false)
            if (failedHash == m.hash) return null
            // Старият индекс може да няма книгата — изчакваме новия (обикновено вече е в ход).
            rebuildIndex(inMemorySnapshot(), m.hash)
        }
        job.await() ?: return null
        return mutex.withLock {
            published.value?.takeIf { !it.stale && it.hash == meta?.hash }?.view
                ?: guarded { store.state() }?.takeIf { it.hash == meta?.hash }?.let { publishIndex(it, stale = false) }
        }
    }

    /** Под [mutex]. Каталогът в паметта (ако е разчетен) — съответства на [meta]. */
    private fun inMemorySnapshot(): CatalogSnapshot? = lastSnapshot?.get() ?: current?.data?.snapshot

    /**
     * Под [mutex]. Пуска попълването на индекса в базата във фонов режим (ако вече не тече
     * за [hash]): от [snapshot] или — без него — от диска, извън ключалката. Попълванията
     * вървят едно след друго (последното записано е за последния каталог); при успех
     * търсачката се публикува, ако каталогът междувременно не е сменен.
     */
    private fun rebuildIndex(snapshot: CatalogSnapshot?, hash: String?): Deferred<CatalogIndexState?> {
        rebuild?.let { if (it.hash == hash && it.job.isActive) return it.job }
        val previous = rebuild?.job
        val job = background.async {
            previous?.join()
            val source = if (snapshot != null && hash != null) snapshot to hash else readSnapshot()
            val state = source?.let { (snap, h) -> populate(snap, h) }
            mutex.withLock {
                if (state == null) {
                    failedHash = hash ?: meta?.hash
                } else {
                    // Кеш от по-стара версия без хеш — хешът е на прочетените байтове.
                    if (meta == null) saveMeta(CatalogMeta(state.hash))
                    if (meta?.hash == state.hash) {
                        failedHash = null
                        publishIndex(state, stale = false)
                    }
                }
            }
            state
        }
        rebuild = Rebuild(hash, job)
        return job
    }

    /** Под [mutex]. Публикува търсачката през индекса в базата за [state]. */
    private suspend fun publishIndex(state: CatalogIndexState, stale: Boolean): Synced<CatalogSearcher> {
        val store = checkNotNull(index)
        val fts = FtsCatalogSearcher(store, state, fallback = { mutex.withLock { (current ?: loadFromDisk())?.data } }, work = work)
        return publish(fts, state.hash, stale)
    }

    /** Под [mutex]. Свежестта е тази на последното опресняване (без такова — кешът от диска). */
    private suspend fun publish(s: CatalogSearcher, hash: String?, stale: Boolean): Synced<CatalogSearcher> {
        val f = freshness ?: Synced(Unit, Instant.ofEpochMilli(loadFetchedAt() ?: clock.now().toEpochMilli()), fromCache = true)
        val p = PublishedSearcher(hash, Synced(s, f.syncedAt, f.fromCache, f.refreshError), stale)
        published.value = p
        return p.view
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

    /** Разчита каталога от диска без ключалката (за фоновото попълване) — с хеша на прочетените байтове. */
    private suspend fun readSnapshot(): Pair<CatalogSnapshot, String>? {
        val payload = runCatching { cache.readBytes(KEY) }.getOrNull() ?: return null
        return withContext(work) { runCatching { KatalogParser.parse(payload.bytes) to katalogHash(payload.bytes) }.logFailure("catalog.disk").getOrNull() }
    }

    /**
     * Грешка на базата не спира каталога — връща `null` (без да поглъща отмяната).
     * Причината остава в локалния дневник (индексът се попълва отново при следващия опит).
     */
    private suspend fun <T> guarded(block: suspend () -> T): T? =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Diagnostics.record("catalog.index", e)
            null
        }

    /** Разчита каталога от диска (под [mutex]); записва хеша, ако липсва (кеш от по-стара версия). */
    private suspend fun loadSnapshotFromDisk(): CatalogSnapshot? {
        val payload = runCatching { cache.readBytes(KEY) }.getOrNull() ?: return null
        val m = loadMeta()
        val parsed = withContext(work) {
            runCatching { KatalogParser.parse(payload.bytes) to (if (m == null) katalogHash(payload.bytes) else null) }.logFailure("catalog.disk").getOrNull()
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
                }.logFailure("catalog.disk").getOrNull()
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
