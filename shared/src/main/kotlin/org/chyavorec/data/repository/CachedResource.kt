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
 * - непромененото съдържание не се презаписва — само се отбелязва новото време.
 */
internal class CachedResource<T>(
    private val cache: PayloadCache,
    private val key: String,
    private val serializer: KSerializer<T>,
    private val clock: AppClock,
    private val minRefreshMillis: Long,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) {
    private class Memo<T>(val value: T, val syncedAtMillis: Long, val version: Long)

    @Volatile private var memo: Memo<T>? = null

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
                cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, updated) })
                remember(updated, current.syncedAt.toEpochMilli())
            }.onFailure { memo = null }
        }
    }

    /** Изтрива стойността — и от паметта, и от кеша. */
    suspend fun clear() {
        withKeyLock {
            memo = null
            runCatching { cache.remove(key) }
        }
    }

    suspend fun load(force: Boolean, fetch: suspend () -> Outcome<T>): Outcome<Synced<T>> {
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
                val result = loadLocked(force) { fromNetwork = true; fetch() }
                flight.last = result
                flight.lastFromNetwork = fromNetwork
                flight.completed++
                result
            }
        } finally {
            Flights.release(cache, key, flight)
        }
    }

    private suspend fun loadLocked(force: Boolean, fetch: suspend () -> Outcome<T>): Outcome<Synced<T>> {
        val cached = cached()
        if (!force && cached != null) {
            val age = clock.now().toEpochMilli() - cached.syncedAt.toEpochMilli()
            if (age in 0 until minRefreshMillis) return Outcome.Success(cached.copy(fromCache = false))
        }
        return when (val r = fetch()) {
            is Outcome.Success -> {
                val now = clock.now()
                store(r.value, cached?.data, now.toEpochMilli())
                Outcome.Success(Synced(r.value, now, fromCache = false))
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized || r.error is AppError.NotAvailable) return r
                if (cached != null) Outcome.Success(cached.copy(refreshError = r.error)) else r
            }
        }
    }

    /** Записва [value]; ако е същото като [previous] — само опреснява времето на файла. */
    private suspend fun store(value: T, previous: T?, nowMillis: Long) {
        runCatching {
            val touched = previous != null && previous == value && cache.touch(key)
            if (!touched) cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, value) })
            remember(value, nowMillis)
        }.onFailure { memo = null }
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
