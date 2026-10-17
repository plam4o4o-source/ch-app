package org.chyavorec.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Локалният дневник на грешките — САМО на устройството. Приложението няма анализ, отчети
 * за сривове към трети страни и проследяване; дневникът напуска телефона единствено ако
 * читателят сам го сподели (Настройки → Информация → „Диагностика“).
 *
 * Пази последните [DiagnosticLog.maxEntries] записа (до [DiagnosticLog.maxBytes] байта): време,
 * етикет на мястото, вид на грешката с почистено съобщение ([DiagnosticSanitizer]) и съкратен стек.
 * Без лични данни: номерата, ПИН-овете, токените, имената и параметрите на адресите се махат.
 */
@Serializable
data class DiagnosticEntry(
    /** Последният път (епоха, ms). */
    val at: Long,
    /** Мястото в кода (напр. `worker.sync`) — постоянен етикет, не текст от потребителя. */
    val tag: String,
    /** Вид на грешката и почистеното съобщение, напр. `java.io.IOException: timeout`. */
    val error: String,
    val stack: List<String> = emptyList(),
    /** Колко пъти подред е повторена същата грешка на същото място. */
    val count: Int = 1,
)

/** Къде се пази дневникът (в приложението — файл в личната папка; в тестовете — паметта). */
interface DiagnosticStore {
    fun read(): String?
    fun write(text: String)
}

class InMemoryDiagnosticStore(@Volatile var text: String? = null) : DiagnosticStore {
    override fun read(): String? = text
    override fun write(text: String) { this.text = text }
}

/** Пръстеновиден буфер на записите — чиста логика, без Android. Безопасен за няколко нишки. */
class DiagnosticLog(
    private val store: DiagnosticStore,
    private val clock: () -> Long = System::currentTimeMillis,
    val maxEntries: Int = 200,
    val maxBytes: Int = 64 * 1024,
) {
    private val lock = Any()
    private var loaded: ArrayDeque<DiagnosticEntry>? = null

    private fun entriesLocked(): ArrayDeque<DiagnosticEntry> = loaded ?: ArrayDeque(decode(runCatching { store.read() }.getOrNull())).also {
        trim(it)
        loaded = it
    }

    /** Записва грешка [error] от мястото [tag]. Отмяната на корутина не е грешка и не се записва. */
    fun record(tag: String, error: Throwable) {
        if (DiagnosticSanitizer.isCancellation(error)) return
        add(DiagnosticEntry(clock(), DiagnosticSanitizer.tag(tag), DiagnosticSanitizer.describe(error), DiagnosticSanitizer.stack(error)))
    }

    /** Записва бележка без изключение (напр. „отговорът не се разчете“) — текстът също се почиства. */
    fun note(tag: String, message: String) {
        add(DiagnosticEntry(clock(), DiagnosticSanitizer.tag(tag), DiagnosticSanitizer.text(message)))
    }

    private fun add(entry: DiagnosticEntry) = synchronized(lock) {
        val list = entriesLocked()
        val last = list.lastOrNull()
        // Същата грешка на същото място подред (напр. всяко събуждане без мрежа) — един запис с брояч.
        if (last != null && last.tag == entry.tag && last.error == entry.error && last.stack == entry.stack) {
            list[list.lastIndex] = last.copy(at = entry.at, count = last.count + 1)
        } else {
            list.addLast(entry)
        }
        trim(list)
        persist(list)
    }

    /** Записите — от най-стария към най-новия. */
    fun entries(): List<DiagnosticEntry> = synchronized(lock) { entriesLocked().toList() }

    fun clear() = synchronized(lock) {
        loaded = ArrayDeque()
        runCatching { store.write("") }
        Unit
    }

    /** Обикновен текст за „Сподели“ — [header] (версия, Android) и записите, най-новият отгоре. */
    fun export(header: List<String>, zone: ZoneId = ZoneId.systemDefault()): String = format(entries(), header, zone)

    private fun trim(list: ArrayDeque<DiagnosticEntry>) {
        while (list.size > maxEntries) list.removeFirst()
        var bytes = list.sumOf { lineBytes(it) }
        while (bytes > maxBytes && list.isNotEmpty()) bytes -= lineBytes(list.removeFirst())
    }

    private fun persist(list: Collection<DiagnosticEntry>) {
        // Дневникът не бива никога да проваля самото приложение.
        runCatching { store.write(list.joinToString("\n") { json.encodeToString(DiagnosticEntry.serializer(), it) }) }
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true; encodeDefaults = false }
        private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        private fun lineBytes(e: DiagnosticEntry) = json.encodeToString(DiagnosticEntry.serializer(), e).toByteArray(Charsets.UTF_8).size + 1

        /** Ред по ред (JSON Lines); повредените редове се пропускат. */
        internal fun decode(text: String?): List<DiagnosticEntry> =
            text.orEmpty().lineSequence().filter { it.isNotBlank() }
                .mapNotNull { runCatching { json.decodeFromString(DiagnosticEntry.serializer(), it) }.getOrNull() }
                .toList()

        fun format(entries: List<DiagnosticEntry>, header: List<String>, zone: ZoneId): String = buildString {
            header.forEach { appendLine(it) }
            if (header.isNotEmpty()) appendLine()
            entries.asReversed().forEach { e ->
                append(TIME.format(Instant.ofEpochMilli(e.at).atZone(zone))).append("  [").append(e.tag).append("] ").append(e.error)
                if (e.count > 1) append("  ×").append(e.count)
                appendLine()
                e.stack.forEach { appendLine("    $it") }
            }
        }.trimEnd() + "\n"
    }
}

/**
 * Почистване на текста за дневника: всичко, което може да е лично, се маха, преди да
 * бъде записано. По-добре изрязано повече, отколкото изтекла лична информация.
 */
object DiagnosticSanitizer {
    private const val MAX_TEXT = 240
    private const val MAX_FRAMES = 8
    private const val CAUSE_FRAMES = 3

    private val jsonInput = Regex("""(?is)\s*JSON input:.*$""")
    private val url = Regex("""(?i)\b([a-z][a-z0-9+.\-]*://)(?:[^/\s@"'<>]*@)?([^/\s?#"'<>]*)([^\s?#"'<>]*)(?:[?#][^\s"'<>]*)?""")
    private val email = Regex("""[\w.+\-]+@[\w\-]+(?:\.[\w\-]+)+""")
    private val secret = Regex("""(?i)\b(token|access_token|refresh_token|authorization|password|passwd|pin|secret|session|cookie|card|cardnumber)\s*[:=]\s*("[^"]*"|'[^']*'|[^\s,;&]+)""")
    private val bearer = Regex("""(?i)\b(bearer|basic)\s+[^\s,;]+""")
    private val jwt = Regex("""\beyJ[\w\-]+(?:\.[\w\-]+){1,2}""")
    private val longToken = Regex("""[A-Za-z0-9_\-+/=]{24,}""")
    private val quoted = Regex(""""[^"]*"|'[^']*'|„[^“”]*[“”]|«[^»]*»""")
    private val digits = Regex("""\d(?:[\s\-]?\d){3,}""")
    private val cyrillic = Regex("""[\p{IsCyrillic}]+(?:[\s\-.]+[\p{IsCyrillic}]+)*""")
    private val space = Regex("""\s+""")
    private val tagChars = Regex("""[^A-Za-z0-9_.:\-]""")

    /** Почистено съобщение (един ред, най-много [max] знака). */
    fun text(raw: String?, max: Int = MAX_TEXT): String {
        if (raw.isNullOrBlank()) return ""
        var s = raw.replace(jsonInput, "")
        // Адрес: схема, хост и път (без потребител/парола, параметри и фрагмент).
        s = url.replace(s) { m -> m.groupValues[1] + m.groupValues[2] + m.groupValues[3] }
        s = email.replace(s, "[email]")
        s = bearer.replace(s) { m -> m.groupValues[1] + " […]" }
        s = secret.replace(s) { m -> m.groupValues[1] + "=[…]" }
        s = jwt.replace(s, "[token]")
        s = longToken.replace(s) { m -> if (m.value.any { it.isDigit() } && m.value.any { it.isLetter() }) "[token]" else m.value }
        s = quoted.replace(s) { m -> m.value.first() + "…" + m.value.last() }
        s = digits.replace(s, "#")
        // Кирилица в техническо съобщение е почти винаги текст от данните (имена, заглавия).
        s = cyrillic.replace(s, "…")
        s = space.replace(s, " ").trim()
        return if (s.length > max) s.take(max - 1).trimEnd() + "…" else s
    }

    /** Етикетът на мястото — само латиница, цифри и `_.:-`, до 40 знака. */
    fun tag(raw: String): String = raw.replace(tagChars, "_").take(40).ifEmpty { "app" }

    /** „вид: съобщение“ на грешката и, ако има, на първопричината. */
    fun describe(t: Throwable): String {
        val head = kind(t)
        val root = rootCause(t)
        return if (root === t) head else "$head ← ${kind(root)}"
    }

    private fun kind(t: Throwable): String {
        val msg = text(t.message, max = 160)
        return if (msg.isEmpty()) t.javaClass.name else "${t.javaClass.name}: $msg"
    }

    private fun rootCause(t: Throwable): Throwable {
        var c = t
        val seen = HashSet<Throwable>()
        while (true) {
            val next = c.cause ?: return c
            if (!seen.add(c) || next === c) return c
            c = next
        }
    }

    /** Съкратен стек: първите кадри и, ако има първопричина, нейните първи кадри. */
    fun stack(t: Throwable): List<String> {
        val out = t.stackTrace.take(MAX_FRAMES).map(::frame).toMutableList()
        // Ако нито един от показаните кадри не е от приложението — първият, който е.
        if (out.none { it.startsWith("org.chyavorec") }) {
            t.stackTrace.drop(MAX_FRAMES).firstOrNull { it.className.startsWith("org.chyavorec") }?.let { out += "… " + frame(it) }
        }
        val root = rootCause(t)
        if (root !== t) {
            out += "caused by ${root.javaClass.name}"
            out += root.stackTrace.take(CAUSE_FRAMES).map(::frame)
        }
        return out
    }

    private fun frame(e: StackTraceElement): String = "${e.className}.${e.methodName}(${e.fileName ?: "?"}:${e.lineNumber})"

    /** Отмяна на корутина (вкл. изтекло време) — нормален край, не грешка. */
    fun isCancellation(t: Throwable): Boolean = t is java.util.concurrent.CancellationException
}

/**
 * Достъпът до дневника от всяко място (и от :shared). Докато приложението не е инсталирало
 * дневник ([install]) — напр. в тестовете — записите просто не се пазят.
 */
object Diagnostics {
    @Volatile var log: DiagnosticLog? = null
        private set

    fun install(log: DiagnosticLog?) { this.log = log }

    /** Записът никога не хвърля — дневникът не бива да проваля това, което описва. */
    fun record(tag: String, error: Throwable) {
        runCatching { log?.record(tag, error) }
    }

    fun note(tag: String, message: String) {
        runCatching { log?.note(tag, message) }
    }

    fun clear() {
        runCatching { log?.clear() }
    }
}

/** `runCatching { … }.logFailure("място")` — неуспехът остава в локалния дневник, резултатът не се променя. */
fun <T> Result<T>.logFailure(tag: String): Result<T> = onFailure { Diagnostics.record(tag, it) }
