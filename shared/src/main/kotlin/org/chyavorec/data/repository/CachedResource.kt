package org.chyavorec.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
 */
internal class CachedResource<T>(
    private val cache: PayloadCache,
    private val key: String,
    private val serializer: KSerializer<T>,
    private val clock: AppClock,
    private val minRefreshMillis: Long,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend fun cached(): Synced<T>? {
        val payload = cache.read(key) ?: return null
        val value = withContext(work) { runCatching { cacheJson.decodeFromString(serializer, payload.text) }.getOrNull() } ?: return null
        return Synced(value, Instant.ofEpochMilli(payload.savedAtMillis), fromCache = true)
    }

    suspend fun load(force: Boolean, fetch: suspend () -> Outcome<T>): Outcome<Synced<T>> {
        val cached = cached()
        if (!force && cached != null) {
            val age = clock.now().toEpochMilli() - cached.syncedAt.toEpochMilli()
            if (age in 0 until minRefreshMillis) return Outcome.Success(cached.copy(fromCache = false))
        }
        return when (val r = fetch()) {
            is Outcome.Success -> {
                runCatching { cache.write(key, withContext(work) { cacheJson.encodeToString(serializer, r.value) }) }
                Outcome.Success(Synced(r.value, clock.now(), fromCache = false))
            }
            is Outcome.Failure -> {
                if (r.error is AppError.Unauthorized || r.error is AppError.NotAvailable) return r
                if (cached != null) Outcome.Success(cached.copy(refreshError = r.error)) else r
            }
        }
    }
}
