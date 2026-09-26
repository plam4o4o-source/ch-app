package org.chyavorec.app.ui.components

import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import java.time.Instant

/**
 * Единно състояние на асинхронен екран: зареждане (skeleton), съдържание,
 * празно, грешка — плюс информация за синхронизацията (кога, от кеш ли е).
 */
data class ScreenState<T>(
    val data: T? = null,
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val error: AppError? = null,
    val syncedAt: Instant? = null,
    val fromCache: Boolean = false,
    val refreshError: AppError? = null,
) {
    val showSkeleton: Boolean get() = loading && data == null

    fun startRefresh(): ScreenState<T> = if (data == null) copy(loading = true, error = null) else copy(refreshing = true)

    fun with(outcome: Outcome<Synced<T>>): ScreenState<T> = when (outcome) {
        is Outcome.Success -> ScreenState(
            data = outcome.value.data,
            loading = false,
            refreshing = false,
            syncedAt = outcome.value.syncedAt,
            fromCache = outcome.value.fromCache,
            refreshError = outcome.value.refreshError,
        )
        is Outcome.Failure -> if (data != null) {
            copy(loading = false, refreshing = false, refreshError = outcome.error, fromCache = true)
        } else {
            ScreenState(loading = false, error = outcome.error)
        }
    }

    fun <R> map(transform: (T) -> R): ScreenState<R> =
        ScreenState(data?.let(transform), loading, refreshing, error, syncedAt, fromCache, refreshError)
}

fun <T> Synced<T>.toState(): ScreenState<T> =
    ScreenState(data, loading = false, syncedAt = syncedAt, fromCache = fromCache, refreshError = refreshError)
