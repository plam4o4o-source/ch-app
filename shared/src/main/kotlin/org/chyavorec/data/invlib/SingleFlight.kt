package org.chyavorec.data.invlib

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Една заявка „в движение“: едновременните извиквания на [run] споделят резултата на
 * първото, вместо всяко да ходи по мрежата. След като то приключи, следващото
 * извикване започва наново (тук няма кеширане на резултата).
 *
 * Ако извикването, което върши работата, бъде прекъснато, чакащите не се прекъсват
 * заедно с него — опитват отново (някое от тях поема работата).
 */
internal class SingleFlight<T> {
    private val lock = Mutex()
    private var current: CompletableDeferred<T>? = null

    suspend fun run(block: suspend () -> T): T {
        while (true) {
            var owner = false
            val deferred = lock.withLock {
                current ?: CompletableDeferred<T>().also {
                    current = it
                    owner = true
                }
            }
            if (!owner) {
                try {
                    return deferred.await()
                } catch (e: CancellationException) {
                    // Самите ние сме прекъснати → нагоре; иначе е прекъснат собственикът → нов опит.
                    currentCoroutineContext().ensureActive()
                    continue
                }
            }
            try {
                val value = block()
                deferred.complete(value)
                return value
            } catch (t: Throwable) {
                deferred.completeExceptionally(t)
                throw t
            } finally {
                withContext(NonCancellable) { lock.withLock { if (current === deferred) current = null } }
            }
        }
    }
}
