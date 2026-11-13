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

/**
 * Един старт. Времената са от началото на процеса (студен) или от създаването на екрана (топъл).
 * [versionCode] — версията, която го е измерила (0 — неизвестна: записи от преди версиите);
 * [afterUpdate] — първият студен старт на тази версия след обновяване (кешовете са празни, а
 * профилът може още да не е компилиран) — не влиза в медианите, показва се отделно.
 */
@Immutable
@Serializable
data class StartupSample(
    val cold: Boolean,
    val firstFrameMs: Long,
    /** `null` — екранът е скрит преди пълното показване. */
    val fullyDrawnMs: Long? = null,
    val versionCode: Int = 0,
    val afterUpdate: Boolean = false,
)

/**
 * Кадрите на един маршрут и една версия (натрупано между сесиите). [worstMs] е само работата
 * в нишката на интерфейса; [worstTotalMs]/[sumTotalMs] — CPU времето на кадъра: интерфейс плюс
 * нишката за рендериране, БЕЗ видеокартата (FrameDataApi24.frameDurationCpuNanos; 0 — не е измерено).
 * Имената на полетата са стари и остават (така се четат вече записаните данни).
 */
@Immutable
@Serializable
data class RouteFrames(
    val route: String,
    val frames: Long = 0,
    val janky: Long = 0,
    val worstMs: Long = 0,
    val versionCode: Int = 0,
    val worstTotalMs: Long = 0,
    val sumTotalMs: Long = 0,
) {
    val jankyPercent: Double get() = if (frames <= 0) 0.0 else janky * 100.0 / frames
    val meanTotalMs: Long get() = if (frames <= 0 || sumTotalMs <= 0) 0 else sumTotalMs / frames
    val hasTotal: Boolean get() = worstTotalMs > 0
}

/** [names] — името на версията по код (за четим текст на по-старите версии). */
@Immutable
@Serializable
data class PerfData(
    val starts: List<StartupSample> = emptyList(),
    val routes: List<RouteFrames> = emptyList(),
    val names: Map<Int, String> = emptyMap(),
) {
    val isEmpty: Boolean get() = starts.isEmpty() && routes.isEmpty()
}

/** Медиана и последна стойност (`null` — няма измерване). */
@Immutable
data class MedianLast(val median: Long, val last: Long)

@Immutable
data class StartStats(val count: Int, val firstFrame: MedianLast, val fullyDrawn: MedianLast?)

/**
 * Готовото за екрана за версията [versionCode] (0 — неизвестна): студените и топлите стартове
 * поотделно, първият старт след обновяване и най-насечените екрани. [earlier] — предишната
 * версия, с която се сравнява.
 */
@Immutable
data class PerfSummary(
    val cold: StartStats?,
    val warm: StartStats?,
    val topRoutes: List<RouteFrames>,
    val versionCode: Int = 0,
    val versionName: String = "",
    val afterUpdate: StartupSample? = null,
    val earlier: PerfSummary? = null,
) {
    /** Няма измервания за тази версия (възможно е да има за по-стара — [earlier]). */
    val currentEmpty: Boolean get() = cold == null && warm == null && topRoutes.isEmpty() && afterUpdate == null
    val isEmpty: Boolean get() = currentEmpty && (earlier?.currentEmpty ?: true)
    /** Най-бавният кадър (само интерфейсът) от най-насечените екрани на версията. */
    val worstRoute: RouteFrames? get() = topRoutes.maxByOrNull { it.worstMs }
}

/** „Профил: компилиран / чака / няма“ — какво казва ProfileVerifier за базовия профил. */
@Immutable
data class ProfileState(val kind: Kind, val code: Int = -1) {
    enum class Kind { COMPILED, PENDING, NONE, UNKNOWN }

    companion object {
        val Unknown = ProfileState(Kind.UNKNOWN)

        fun of(compiled: Boolean, enqueued: Boolean, code: Int): ProfileState =
            ProfileState(if (compiled) Kind.COMPILED else if (enqueued) Kind.PENDING else Kind.NONE, code)
    }
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

    /** Колко най-нови версии пазят стартовете и кадрите (текущата и предишните, за сравнение). */
    const val KEEP_START_VERSIONS = 3
    const val KEEP_ROUTE_VERSIONS = 2

    /**
     * Последните [cap] старта от всеки вид и версия — честите връщания (топли) не изместват студените,
     * а стартовете на нова версия не се смесват със стария (само най-новите [KEEP_START_VERSIONS] версии).
     * Първият студен старт на нова версия след записи на друга се бележи [StartupSample.afterUpdate].
     */
    fun addStart(data: PerfData, sample: StartupSample, cap: Int = MAX_STARTS): PerfData {
        val v = sample.versionCode
        val newVersion = v != 0 && data.starts.none { it.versionCode == v } && data.routes.none { it.versionCode == v }
        val flagged = if (sample.cold && newVersion && !data.isEmpty && !sample.afterUpdate) sample.copy(afterUpdate = true) else sample
        val all = data.starts + flagged
        val versions = all.map { it.versionCode }.distinct().sortedDescending().take(KEEP_START_VERSIONS).toSet()
        val counts = all.filter { it.versionCode in versions }.groupingBy { it.versionCode to it.cold }.eachCount().toMutableMap()
        // Колко от най-старите от всеки вид (и версия) отпадат.
        val kept = ArrayList<StartupSample>(all.size)
        for (s in all) {
            if (s.versionCode !in versions) continue
            val k = s.versionCode to s.cold
            val n = counts.getValue(k)
            if (n > cap) counts[k] = n - 1 else kept += s
        }
        return data.copy(starts = kept)
    }

    /** Запомня името на версията [code] (за четим текст по-късно). */
    fun noteVersion(data: PerfData, code: Int, name: String): PerfData {
        if (code == 0 || name.isBlank() || data.names[code] == name) return data
        val newest = (data.names + (code to name)).entries.sortedByDescending { it.key }.take(KEEP_START_VERSIONS + 1)
        return data.copy(names = newest.associate { it.key to it.value })
    }

    /** Добавя новите кадри [delta] към натрупаните (по маршрут: сбор и най-бавен кадър). */
    fun addFrames(data: PerfData, delta: List<RouteFrames>, cap: Int = MAX_ROUTES): PerfData {
        if (delta.isEmpty()) return data
        // По маршрут И версия: максимумът на нова версия не се смесва със стария.
        val merged = LinkedHashMap<Pair<String, Int>, RouteFrames>()
        for (r in data.routes) merged[r.route to r.versionCode] = r
        for (d in delta) {
            val key = d.route to d.versionCode
            val old = merged[key]
            merged[key] = if (old == null) d else RouteFrames(
                d.route, old.frames + d.frames, old.janky + d.janky, maxOf(old.worstMs, d.worstMs), d.versionCode,
                maxOf(old.worstTotalMs, d.worstTotalMs), old.sumTotalMs + d.sumTotalMs,
            )
        }
        // Само най-новите версии; над границата — маршрутите с най-много кадри.
        val versions = merged.values.map { it.versionCode }.distinct().sortedDescending().take(KEEP_ROUTE_VERSIONS).toSet()
        val current = merged.values.filter { it.versionCode in versions }
        val routes = if (current.size <= cap) current else current.sortedByDescending { it.frames }.take(cap)
        return data.copy(routes = routes)
    }

    /** Медианата (при четен брой — средното на двете средни, закръглено надолу). */
    fun median(values: List<Long>): Long? {
        if (values.isEmpty()) return null
        val s = values.sorted()
        val mid = s.size / 2
        return if (s.size % 2 == 1) s[mid] else (s[mid - 1] + s[mid]) / 2
    }

    /**
     * Обобщението за версията [currentVersion] (по подразбиране — най-новата в данните; [currentName]
     * — името ѝ, ако данните още не го знаят) и, за сравнение, предишната.
     */
    fun summary(data: PerfData, currentVersion: Int? = null, currentName: String? = null, top: Int = TOP_ROUTES): PerfSummary {
        val versions = (data.starts.map { it.versionCode } + data.routes.map { it.versionCode }).distinct().sortedDescending()
        val current = currentVersion ?: versions.firstOrNull() ?: 0
        val earlierVersion = versions.firstOrNull { it < current }
        return forVersion(data, current, currentName, top).copy(
            earlier = earlierVersion?.let { forVersion(data, it, null, top) },
        )
    }

    private fun forVersion(data: PerfData, v: Int, name: String?, top: Int): PerfSummary {
        val starts = data.starts.filter { it.versionCode == v }
        val regular = starts.filter { !it.afterUpdate }
        return PerfSummary(
            cold = startStats(regular.filter { it.cold }),
            warm = startStats(regular.filter { !it.cold }),
            topRoutes = data.routes.filter { it.versionCode == v && it.frames >= MIN_ROUTE_FRAMES }
                .sortedWith(compareByDescending<RouteFrames> { it.jankyPercent }.thenByDescending { it.frames })
                .take(top),
            versionCode = v,
            versionName = name?.takeIf { it.isNotBlank() } ?: data.names[v].orEmpty(),
            afterUpdate = starts.lastOrNull { it.afterUpdate },
        )
    }

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

    /** „1.2.36 (36)“ или „unknown version“ — версията в текста за „Сподели“. */
    fun versionLabel(code: Int, name: String): String =
        if (code == 0) "unknown version" else if (name.isBlank()) "version $code" else "$name ($code)"

    /** Кратък текст за „Сподели“ (технически, на английски — както стековете в дневника). */
    fun export(s: PerfSummary, profile: ProfileState? = null): String = buildString {
        fun line(prefix: String, name: String, st: StartStats?) {
            if (st == null) return
            append("${prefix}startup $name n=${st.count}: first frame median ${st.firstFrame.median} ms, last ${st.firstFrame.last} ms")
            st.fullyDrawn?.let { append("; fully drawn median ${it.median} ms, last ${it.last} ms") }
            appendLine()
        }
        appendLine("performance ${versionLabel(s.versionCode, s.versionName)}")
        profile?.let {
            val text = when (it.kind) {
                ProfileState.Kind.COMPILED -> "compiled"
                ProfileState.Kind.PENDING -> "waiting for compilation"
                ProfileState.Kind.NONE -> "none"
                ProfileState.Kind.UNKNOWN -> "unknown"
            }
            appendLine("profile: $text" + if (it.code >= 0) " (code ${it.code})" else "")
        }
        s.afterUpdate?.let {
            append("startup ${if (it.cold) "cold" else "warm"} first after update: first frame ${it.firstFrameMs} ms")
            it.fullyDrawnMs?.let { d -> append("; fully drawn $d ms") }
            appendLine()
        }
        line("", "cold", s.cold)
        line("", "warm", s.warm)
        for (r in s.topRoutes) {
            append("frames ${r.route}: ${r.janky}/${r.frames} janky (${"%.1f".format(java.util.Locale.ROOT, r.jankyPercent)} %), worst ${r.worstMs} ms")
            if (r.hasTotal) append("; frame CPU time (UI + render thread, no GPU) mean ${r.meanTotalMs} ms, worst ${r.worstTotalMs} ms")
            appendLine()
        }
        if (s.currentEmpty) appendLine("no measurements for this version yet")
        s.earlier?.takeUnless { it.currentEmpty }?.let { e ->
            val p = "earlier ${versionLabel(e.versionCode, e.versionName)}: "
            e.afterUpdate?.let { appendLine("${p}startup first after update: first frame ${it.firstFrameMs} ms") }
            line(p, "cold", e.cold)
            line(p, "warm", e.warm)
            e.worstRoute?.let { appendLine("${p}worst frame ${it.worstMs} ms on ${it.route}" + if (it.hasTotal) ", frame CPU time ${it.worstTotalMs} ms" else "") }
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
    /** [кадри, насечени, най-бавен интерфейс (ns), сбор на CPU времето на кадъра (ns), най-бавно CPU време (ns)] по маршрут. */
    private val counts = HashMap<String, LongArray>()

    /** [totalNanos] — CPU времето на кадъра (интерфейс и нишка за рендериране, без видеокартата; 0 — не е измерено). */
    fun add(route: String, durationNanos: Long, jank: Boolean, totalNanos: Long = 0) = synchronized(lock) {
        val c = counts[route] ?: LongArray(5).also { if (counts.size < PerfStats.MAX_ROUTES) counts[route] = it }
        c[0]++
        if (jank) c[1]++
        if (durationNanos > c[2]) c[2] = durationNanos
        c[3] += totalNanos
        if (totalNanos > c[4]) c[4] = totalNanos
    }

    /** Натрупаното досега (и нулиране) — за запис при скриване на приложението; [versionCode] — на тази версия. */
    fun drain(versionCode: Int = 0): List<RouteFrames> = synchronized(lock) {
        val out = counts.entries.filter { it.value[0] > 0 }.map { (route, c) ->
            RouteFrames(route, c[0], c[1], c[2] / 1_000_000, versionCode, c[4] / 1_000_000, c[3] / 1_000_000)
        }
        counts.clear()
        out
    }
}
