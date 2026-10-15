package org.chyavorec.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.domain.service.Fetched
import org.chyavorec.domain.repository.PayloadCache
import java.time.Instant

internal val cacheJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
    classDiscriminator = "type"
}

/**
 * „Мрежа, а при неуспех — кеш“ с изрично отбелязване на произхода.
 *
 * - при успех данните се записват и се връщат със `fromCache = false`;
 * - при грешка се връща кешът със `fromCache = true` и причината, за да може UI да
 *   покаже „Няма интернет връзка. Показваме последно наличните данни.“;
 * - ако няма и кеш — грешката.
 * [minRefreshMillis] пази от излишни заявки (основна защита срещу злоупотреба с API):
 * по-пресен кеш се връща без мрежа, освен при изрично опресняване.
 * JSON (де)кодирането върви на [work], не на извикващата (главна) нишка.
 *
 * Производителност:
 * - последната стойност се помни в паметта (докато [PayloadCache.version] на ключа не
 *   се промени — напр. при изчистване на кеша или изход от профила), така че повторното
 *   [cached] не чете и не декодира файла;
 * - едновременните [load] за един и същ ключ (и кеш) се изпълняват едно след друго, а
 *   чакащите получават резултата на току-що завършилото зареждане вместо втора заявка;
 * - непромененото съдържание не се презаписва — само се отбелязва новото време;
 * - при [loadIfChanged] до стойността се пази SHA-256 на суровия файл ([hashKey]), така че
 *   непромененият файл изобщо не се разчита (само се отбелязва новото време).
 *
 * [onStored] се вика след всеки успешен запис (от мрежата или промяна на място) —
 * под заключването на ключа, затова трябва да е бърз и да не чете същия кеш.
 */
internal class CachedResource<T>(
    private val cache: PayloadCache,
    private val key: String,
    private val serializer: KSerializer<T>,
    private val clock: AppClock,
    private val minRefreshMillis: Long,
    private val work: CoroutineDispatcher = Dispatchers.Default,
    private val onStored: ((T) -> Unit)? = null,
) {
    private class Memo<T>(val value: T, val syncedAtMillis: Long, val version: Long)

    @Volatile private var memo: Memo<T>? = null

    /** До стойността: SHA-256 на суровия файл, от който е разчетена (вж. [loadIfChanged]). */
    private val hashKey = "$key#sha256"

    private fun validMemo(): Memo<T>? {
        val m = memo ?: return null
        return if (cache.version(key) == m.version) m else null
    }

    private fun remember(value: T, syncedAtMillis: Long) {
        memo = cache.version(key)?.let { Memo(value, syncedAtMillis, it) }
    }

    suspend fun cached(): Synced<T>? {
        validMemo()?.let { return Synced(it.value, Instant.ofEpochMilli(it.syncedAtMillis), fromCache = true) }
        // Версията се взема ПРЕДИ четенето: запис между двете прави паметта невалидна, не грешна.
        val version = cache.version(key)
        val payload = cache.read(key) ?: return null
        val value = withContext(work) { runCatching { cacheJson.decodeFromString(serializer, payload.text) }.getOrNull() } ?: return null
        memo = version?.let { Memo(value, payload.savedAtMillis, it) }
        return Synced(value, Instant.ofEpochMilli(payload.savedAtMillis), fromCache = true)
    }

    /** Променя кешираната стойност на място (ако има такава), без да пипа времето на синхронизация. */
    suspend fun updateCached(transform: (T) -> T) {
        withKeyLock {
            val current = cached() ?: return@withKeyLock
            val updated = transform(current.data)
            if (updated == current.data) return@withKeyLock
            runCatching {
                // Стойността вече не е разчетеното съдържание на файла — хешът не важи.
                cache.remove(hashKey)
                cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, updated) })
                remember(updated, current.syncedAt.toEpochMilli())
            }.onFailure { memo = null }.onSuccess { stored(updated) }
        }
    }

    /** Изтрива стойността — и от паметта, и от кеша. */
    suspend fun clear() {
        withKeyLock {
            memo = null
            runCatching { cache.remove(hashKey) }
            runCatching { cache.remove(key) }
        }
    }

    suspend fun load(force: Boolean, fetch: suspend () -> Outcome<T>): Outcome<Synced<T>> = inFlight(force) { cached ->
        when (val r = fetch()) {
            is Outcome.Success -> fresh(r.value, cached, hashed = false, hash = null)
            is Outcome.Failure -> fallback(r, cached)
        }
    }

    /**
     * Като [load], но [fetch] получава SHA-256 на суровия файл, от който е разчетена
     * кешираната стойност (`null` — няма такава или е непозната), и може да отговори
     * [Fetched.Unchanged]: тогава кешът само се „докосва“ (ново време на синхронизация),
     * без разчитане и без презаписване.
     */
    suspend fun loadIfChanged(force: Boolean, fetch: suspend (knownHash: String?) -> Outcome<Fetched<T>>): Outcome<Synced<T>> =
        inFlight(force) { cached ->
            val known = if (cached != null) runCatching { cache.read(hashKey)?.text }.getOrNull() else null
            when (val r = fetch(known)) {
                is Outcome.Failure -> fallback(r, cached)
                is Outcome.Success -> when (val f = r.value) {
                    is Fetched.Changed -> fresh(f.value, cached, hashed = true, hash = f.hash)
                    Fetched.Unchanged ->
                        if (cached != null) unchanged(cached) else Outcome.Failure(AppError.Unexpected("unchanged without cache"))
                }
            }
        }

    private suspend fun inFlight(force: Boolean, fetch: suspend (cached: Synced<T>?) -> Outcome<Synced<T>>): Outcome<Synced<T>> {
        val flight = Flights.acquire(cache, key)
        val ticket = flight.completed
        try {
            return flight.mutex.withLock {
                if (flight.completed != ticket) {
                    // Докато сме чакали, друго зареждане на същия ключ е приключило — ползваме
                    // неговия резултат (при изрично опресняване — само ако е било от мрежата).
                    @Suppress("UNCHECKED_CAST")
                    val last = flight.last as? Outcome<Synced<T>>
                    if (last != null && (!force || flight.lastFromNetwork)) return@withLock last
                }
                var fromNetwork = false
                val result = loadLocked(force) { cached -> fromNetwork = true; fetch(cached) }
                flight.last = result
                flight.lastFromNetwork = fromNetwork
                flight.completed++
                result
            }
        } finally {
            Flights.release(cache, key, flight)
        }
    }

    private suspend fun loadLocked(force: Boolean, fetch: suspend (cached: Synced<T>?) -> Outcome<Synced<T>>): Outcome<Synced<T>> {
        val cached = cached()
        if (!force && cached != null) {
            val age = clock.now().toEpochMilli() - cached.syncedAt.toEpochMilli()
            if (age in 0 until minRefreshMillis) return Outcome.Success(cached.copy(fromCache = false))
        }
        return fetch(cached)
    }

    /** [hashed] — ресурсът пази хеша на суровия файл ([loadIfChanged]); [hash] — хешът на [value]. */
    private suspend fun fresh(value: T, cached: Synced<T>?, hashed: Boolean, hash: String?): Outcome<Synced<T>> {
        val now = clock.now()
        store(value, cached?.data, now.toEpochMilli(), hashed, hash)
        return Outcome.Success(Synced(value, now, fromCache = false))
    }

    private fun fallback(r: Outcome.Failure, cached: Synced<T>?): Outcome<Synced<T>> {
        if (r.error is AppError.Unauthorized || r.error is AppError.NotAvailable) return r
        return if (cached != null) Outcome.Success(cached.copy(refreshError = r.error)) else r
    }

    /** Файлът е същият като разчетения: стойността остава, отбелязва се само новото време. */
    private suspend fun unchanged(cached: Synced<T>): Outcome<Synced<T>> {
        val now = clock.now()
        runCatching {
            if (!cache.touch(key)) cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, cached.data) })
            remember(cached.data, now.toEpochMilli())
        }.onFailure { memo = null }.onSuccess { stored(cached.data) }
        return Outcome.Success(Synced(cached.data, now, fromCache = false))
    }

    /**
     * Записва [value]; ако е същото като [previous] — само опреснява времето на файла.
     * [hash] (SHA-256 на суровия файл) се записва след стойността, а старият се трие преди
     * нея — прекъснат запис оставя „без хеш“ (следващото сваляне разчита отново), не грешен хеш.
     */
    private suspend fun store(value: T, previous: T?, nowMillis: Long, hashed: Boolean = false, hash: String? = null) {
        runCatching {
            val touched = previous != null && previous == value && cache.touch(key)
            if (!touched) {
                if (hashed) cache.remove(hashKey)
                cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, value) })
            }
            if (hash != null) cache.write(hashKey, hash) else if (hashed && touched) cache.remove(hashKey)
            remember(value, nowMillis)
        }.onFailure { memo = null }.onSuccess { stored(value) }
    }

    /** Грешка в слушателя не проваля записа. */
    private fun stored(value: T) {
        onStored?.let { runCatching { it(value) } }
    }

    private suspend inline fun <R> withKeyLock(block: () -> R): R {
        val flight = Flights.acquire(cache, key)
        try {
            return flight.mutex.withLock { block() }
        } finally {
            Flights.release(cache, key, flight)
        }
    }

    /** Общ за всички инстанции на ключа (напр. няколко ViewModel-а, отворили една страница). */
    private class Flight {
        val mutex = Mutex()
        var users = 0
        @Volatile var completed = 0L
        @Volatile var last: Any? = null
        @Volatile var lastFromNetwork = false
    }

    /** Ключ по идентичност на кеша + ключа в него. */
    private class FlightKey(val cache: PayloadCache, val key: String) {
        override fun equals(other: Any?) = other is FlightKey && other.cache === cache && other.key == key
        override fun hashCode() = System.identityHashCode(cache) * 31 + key.hashCode()
    }

    /** Заключванията живеят само докато някой ги ползва — няма натрупване в паметта. */
    private object Flights {
        private val map = HashMap<FlightKey, Flight>()

        fun acquire(cache: PayloadCache, key: String): Flight = synchronized(map) {
            map.getOrPut(FlightKey(cache, key)) { Flight() }.also { it.users++ }
        }

        fun release(cache: PayloadCache, key: String, flight: Flight) = synchronized(map) {
            if (--flight.users == 0) map.remove(FlightKey(cache, key))
        }
    }
}
