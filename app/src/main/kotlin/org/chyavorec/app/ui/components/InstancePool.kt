package org.chyavorec.app.ui.components

import java.util.concurrent.Executor
import java.util.concurrent.FutureTask
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit

/**
 * Малък резерв от скъпи за създаване обекти (преведени шейдъри): [prepare] ги прави
 * във фонова нишка, [take] ги дава наготово, [release] ги връща, за да не се превеждат наново.
 *
 * - най-много едно създаване във фона в даден момент (Future); ако резервът е празен, а
 *   създаването тече, [take] изчаква него (до [waitMs]) вместо да създава второ копие;
 * - в резерва остават най-много [maxIdle] обекта — излишните при [release] се изхвърлят;
 * - когато резервът остане празен след [take], във фона тръгва ново създаване.
 *
 * Чист Kotlin (без Android) — тества се с подаден [Executor].
 */
internal class InstancePool<T : Any>(
    private val create: () -> T?,
    private val executor: Executor,
    private val maxIdle: Int = 2,
    private val waitMs: Long = 100,
) {
    private val lock = Any()
    private val idle = ArrayDeque<T>()
    private var inflight: FutureTask<Unit>? = null

    /** Колко обекта чакат в резерва. */
    val idleCount: Int get() = synchronized(lock) { idle.size }

    /** Тече ли създаване във фона. */
    val filling: Boolean get() = synchronized(lock) { inflight != null }

    /** Гарантира един обект в резерва (или на път към него) — във фонова нишка. */
    fun prepare() {
        startFill()
    }

    /**
     * Обект от резерва; иначе — изчаква създаването във фона (ако тече); иначе — създава на
     * място (както преди пула). След всяко взимане резервът се допълва във фона.
     */
    fun take(): T? {
        synchronized(lock) { idle.removeFirstOrNull() }?.let {
            startFill()
            return it
        }
        val pending = synchronized(lock) { inflight }
        if (pending != null) {
            try {
                pending.get(waitMs, TimeUnit.MILLISECONDS)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            } catch (_: Exception) {
                // Изтече времето или създаването е паднало — по-долу се създава на място.
            }
            synchronized(lock) { idle.removeFirstOrNull() }?.let {
                startFill()
                return it
            }
        }
        try {
            return create()
        } finally {
            startFill()
        }
    }

    /** Връща обект, който вече не се рисува; над [maxIdle] се изхвърля. */
    fun release(item: T) {
        synchronized(lock) {
            if (idle.size < maxIdle && item !in idle) idle.addLast(item)
        }
    }

    private fun startFill() {
        val task: FutureTask<Unit>
        synchronized(lock) {
            if (idle.isNotEmpty() || inflight != null) return
            task = FutureTask<Unit> { fill() }
            inflight = task
        }
        try {
            executor.execute(task)
        } catch (_: RejectedExecutionException) {
            synchronized(lock) { if (inflight === task) inflight = null }
        }
    }

    private fun fill() {
        try {
            val item = create()
            if (item != null) synchronized(lock) { if (idle.size < maxIdle) idle.addLast(item) }
        } finally {
            synchronized(lock) { inflight = null }
        }
    }
}
