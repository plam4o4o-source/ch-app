package org.chyavorec.core

import java.time.Instant

/**
 * Данни заедно с момента, в който са получени от сървъра.
 *
 * [fromCache] = true означава, че опресняването не е успяло и показваме последно
 * наличните данни. UI задължително показва „Последна синхронизация: …“ в този случай —
 * особено за наличност на книги и читателски данни, които не бива да изглеждат актуални.
 */
data class Synced<T>(
    val data: T,
    val syncedAt: Instant,
    val fromCache: Boolean = false,
    val refreshError: AppError? = null,
) {
    fun <R> map(transform: (T) -> R): Synced<R> =
        Synced(transform(data), syncedAt, fromCache, refreshError)
}
