package org.chyavorec.app.diagnostics

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * „Бързодействие“ в „Диагностика“: времената на стартиране и плавността на екраните —
 * САМО на устройството (до споделяне от самия читател), без лични данни: само имената на
 * маршрутите (`book`, `home`…), без номера, заглавия или други параметри.
 */

/** Един старт. Времената са от началото на процеса (студен) или от създаването на екрана (топъл). */
@Immutable
@Serializable
data class StartupSample(
    val cold: Boolean,
    val firstFrameMs: Long,
    /** `null` — екранът е скрит преди пълното показване. */
    val fullyDrawnMs: Long? = null,
)

/** Кадрите на един маршрут (натрупано между сесиите). */
@Immutable
@Serializable
data class RouteFrames(val route: String, val frames: Long = 0, val janky: Long = 0, val worstMs: Long = 0) {
    val jankyPercent: Double get() = if (frames <= 0) 0.0 else janky * 100.0 / frames
}

@Immutable
@Serializable
data class PerfData(val starts: List<StartupSample> = emptyList(), val routes: List<RouteFrames> = emptyList()) {
    val isEmpty: Boolean get() = starts.isEmpty() && routes.isEmpty()
}

/** Медиана и последна стойност (`null` — няма измерване). */
@Immutable
data class MedianLast(val median: Long, val last: Long)

@Immutable
data class StartStats(val count: Int, val firstFrame: MedianLast, val fullyDrawn: MedianLast?)

/** Готовото за екрана: студените и топлите стартове поотделно и най-насечените екрани. */
@Immutable
data class PerfSummary(val cold: StartStats?, val warm: StartStats?, val topRoutes: List<RouteFrames>) {
    val isEmpty: Boolean get() = cold == null && warm == null && topRoutes.isEmpty()
}

/** Чистата логика (без Android): ограничения, сливане и медиани. */
object PerfStats {
    /** Колко последни старта се пазят. */
    const val MAX_STARTS = 20
    /** Маршрутите са постоянен списък (~35); границата е само предпазна. */
    const val MAX_ROUTES = 64
    /** Колко екрана показва „Бързодействие“. */
    const val TOP_ROUTES = 5
    /** Екран с по-малко кадри не се класира (2 кадъра, 1 насечен — не е 50 %). */
    const val MIN_ROUTE_FRAMES = 30L

    fun addStart(data: PerfData, sample: StartupSample, cap: Int = MAX_STARTS): PerfData =
        data.copy(starts = (data.starts + sample).takeLast(cap))

    /** Добавя новите кадри [delta] към натрупаните (по маршрут: сбор и най-бавен кадър). */
    fun addFrames(data: PerfData, delta: List<RouteFrames>, cap: Int = MAX_ROUTES): PerfData {
        if (delta.isEmpty()) return data
        val merged = LinkedHashMap<String, RouteFrames>()
        for (r in data.routes) merged[r.route] = r
        for (d in delta) {
            val old = merged[d.route]
            merged[d.route] = if (old == null) d else RouteFrames(d.route, old.frames + d.frames, old.janky + d.janky, maxOf(old.worstMs, d.worstMs))
        }
        // Над границата — остават маршрутите с най-много кадри.
        val routes = if (merged.size <= cap) merged.values.toList() else merged.values.sortedByDescending { it.frames }.take(cap)
        return data.copy(routes = routes)
    }

    /** Медианата (при четен брой — средното на двете средни, закръглено надолу). */
    fun median(values: List<Long>): Long? {
        if (values.isEmpty()) return null
        val s = values.sorted()
        val mid = s.size / 2
        return if (s.size % 2 == 1) s[mid] else (s[mid - 1] + s[mid]) / 2
    }

    fun summary(data: PerfData, top: Int = TOP_ROUTES): PerfSummary = PerfSummary(
        cold = startStats(data.starts.filter { it.cold }),
        warm = startStats(data.starts.filter { !it.cold }),
        topRoutes = data.routes.filter { it.frames >= MIN_ROUTE_FRAMES }
            .sortedWith(compareByDescending<RouteFrames> { it.jankyPercent }.thenByDescending { it.frames })
            .take(top),
    )

    private fun startStats(starts: List<StartupSample>): StartStats? {
        if (starts.isEmpty()) return null
        val first = MedianLast(median(starts.map { it.firstFrameMs })!!, starts.last().firstFrameMs)
        val drawn = starts.mapNotNull { it.fullyDrawnMs }
        val fully = median(drawn)?.let { MedianLast(it, drawn.last()) }
        return StartStats(starts.size, first, fully)
    }

    /**
     * Етикетът на маршрута — шаблонът без аргументите (`book/{inv}?personal={personal}` → `book`):
     * никога номер, заглавие или адрес.
     */
    fun routeTag(route: String?): String = route?.substringBefore('?')?.substringBefore("/{")?.ifEmpty { null } ?: "start"

    /** Кратък текст за „Сподели“ (технически, на английски — както стековете в дневника). */
    fun export(s: PerfSummary): String = buildString {
        fun line(name: String, st: StartStats?) {
            if (st == null) return
            append("startup $name n=${st.count}: first frame median ${st.firstFrame.median} ms, last ${st.firstFrame.last} ms")
            st.fullyDrawn?.let { append("; fully drawn median ${it.median} ms, last ${it.last} ms") }
            appendLine()
        }
        line("cold", s.cold)
        line("warm", s.warm)
        for (r in s.topRoutes) {
            appendLine("frames ${r.route}: ${r.janky}/${r.frames} janky (${"%.1f".format(java.util.Locale.ROOT, r.jankyPercent)} %), worst ${r.worstMs} ms")
        }
    }
}

/**
 * Файлът на „Бързодействие“ (малък JSON в личната папка, noBackup). Промените са във
 * фоновата нишка [io] — никога на главната; четенето (за екрана) — от паметта след първия път.
 */
class PerfStore(
    private val file: File,
    private val io: Executor = Executors.newSingleThreadExecutor { r -> Thread(r, "perf-stats").apply { isDaemon = true } },
) {
    private val lock = Any()
    private var cached: PerfData? = null

    fun read(): PerfData = synchronized(lock) { loadLocked() }

    /** Прилага [transform] и записва резултата — във фон. */
    fun update(transform: (PerfData) -> PerfData) = io.execute {
        synchronized(lock) {
            val next = transform(loadLocked())
            cached = next
            runCatching {
                file.parentFile?.mkdirs()
                val tmp = File(file.path + ".tmp")
                tmp.writeText(json.encodeToString(PerfData.serializer(), next))
                if (!tmp.renameTo(file)) {
                    file.writeText(tmp.readText())
                    tmp.delete()
                }
            }
        }
    }

    fun clear() = synchronized(lock) {
        cached = PerfData()
        runCatching { file.delete() }
        Unit
    }

    private fun loadLocked(): PerfData = cached ?: (
        runCatching { if (file.exists()) json.decodeFromString(PerfData.serializer(), file.readText()) else null }.getOrNull() ?: PerfData()
        ).also { cached = it }

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }
}

/**
 * Броячите на кадрите по маршрут за текущата сесия — пишат се от нишката на JankStats при
 * всеки кадър: без заделяне на памет (масив броячи на маршрут, създаден веднъж).
 */
class FrameCounter {
    private val lock = Any()
    /** [кадри, насечени, най-бавен (ns)] по маршрут. */
    private val counts = HashMap<String, LongArray>()

    fun add(route: String, durationNanos: Long, jank: Boolean) = synchronized(lock) {
        val c = counts[route] ?: LongArray(3).also { if (counts.size < PerfStats.MAX_ROUTES) counts[route] = it }
        c[0]++
        if (jank) c[1]++
        if (durationNanos > c[2]) c[2] = durationNanos
    }

    /** Натрупаното досега (и нулиране) — за запис при скриване на приложението. */
    fun drain(): List<RouteFrames> = synchronized(lock) {
        val out = counts.entries.filter { it.value[0] > 0 }.map { (route, c) -> RouteFrames(route, c[0], c[1], c[2] / 1_000_000) }
        counts.clear()
        out
    }
}
