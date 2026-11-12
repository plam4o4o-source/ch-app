package org.chyavorec.data.catalog

import org.chyavorec.data.catalog.ReadingYear.Archetype
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * „Твоята шевица“: лична лента, избродирана от читателската година — чиста функция на
 * [ReadingYear.Summary] (без случайност: едни и същи данни дават една и съща лента, а различни
 * данни — различна). Лентата е огледална: 12-те месеца са колони (височина = книгите през месеца),
 * отляво направо — януари…декември към средата, отдясно — огледално; в средата е мотивът на
 * читателския тип. Конецът на книжния бод е по раздела по УДК ([THREAD_SECTION] + цифрата).
 */
object PersonalShevitsa {
    /** Златен конец. */
    const val GOLD: Byte = 0

    /** Бордо конец. */
    const val BURGUNDY: Byte = 1

    /** Конец за раздел [s] (0–9) по УДК е `THREAD_SECTION + s`. */
    const val THREAD_SECTION: Byte = 2

    const val COLUMNS = 41
    const val ROWS = 15

    /** Най-много бодове в колона (вътрешните редове на лентата). */
    const val MAX_COLUMN = ROWS - 2

    /** Средата на лентата: колоната на мотива и редът на оста на стълбчетата. */
    const val CENTRE_X = COLUMNS / 2
    private const val CENTRE_Y = ROWS / 2
    private const val MOTIF = 13
    private const val MOTIF_R = MOTIF / 2

    /** Схемата: клетките в реда, в който иглата ги бродира (от средата навън, отгоре надолу). */
    class Chart internal constructor(
        val columns: Int,
        val rows: Int,
        val x: IntArray,
        val y: IntArray,
        val thread: ByteArray,
    ) {
        val size: Int get() = x.size
    }

    /** Месеците на „празната“ година: тиха вълна — лентата не е празна, а нежна и умерена. */
    private val DefaultMonths = intArrayOf(1, 2, 3, 2, 1, 2, 3, 4, 3, 2, 1, 2)

    /** Разделите на „празната“ година: литература, история и приложни знания — топли тонове. */
    private val DefaultSections = listOf(ReadingStats.SectionCount(8, 12), ReadingStats.SectionCount(9, 8), ReadingStats.SectionCount(6, 6))

    /** Пълната лента ([COLUMNS] × [ROWS]). */
    fun chart(s: ReadingYear.Summary): Chart {
        val seed = seedOf(s)
        val counts = scaledMonths(s)
        val empty = counts.sum() == 0
        val months = if (empty) DefaultMonths else counts
        val sections = if (empty) DefaultSections else s.sections
        val real = if (empty) months.sum() else realBooks(s)
        val cells = Cells()

        borders(cells, seed)
        // Рамка отстрани и пролуките до мотива.
        for (y in 1 until ROWS - 1) {
            cells.mirrored(0, y, if (y % 2 == 0) BURGUNDY else GOLD)
        }
        cells.mirrored(13, CENTRE_Y, GOLD)

        // Стълбчетата: книгите се редят от оста навън, а разделите — равномерно през годината.
        val sequence = threadSequence(months.sum(), real, sections, seed)
        var next = 0
        months.forEachIndexed { m, n ->
            var book = 0
            for (row in columnRows(n, seed)) {
                val t = if (row == null) BURGUNDY else sequence[(next + book++) % sequence.size]
                cells.mirrored(1 + m, row ?: CENTRE_Y, t)
            }
            next += n
        }

        motif(cells, s.archetype)
        return cells.build()
    }

    /**
     * Лентата за долния край на картата за споделяне: [COLUMNS] × 3 — кант, по един бод на месец
     * (конец по първата книга на месеца; празният месец — бордо зрънце) и кант. Огледална е.
     */
    fun strip(s: ReadingYear.Summary): Chart {
        val seed = seedOf(s)
        val counts = scaledMonths(s)
        val empty = counts.sum() == 0
        val months = if (empty) DefaultMonths else counts
        val sequence = threadSequence(months.sum(), if (empty) months.sum() else realBooks(s), if (empty) DefaultSections else s.sections, seed)
        val cells = Cells(rows = 3)
        for (x in 0 until COLUMNS) {
            val d = abs(x - CENTRE_X)
            val t = borderThread(d, seed)
            if (t >= 0) {
                cells.put(x, 0, t.toByte())
                cells.put(x, 2, t.toByte())
            }
        }
        var next = 0
        months.forEachIndexed { m, n ->
            cells.mirrored(1 + m, 1, if (n == 0) BURGUNDY else sequence[next % sequence.size])
            next += n
        }
        cells.mirrored(0, 1, GOLD)
        // Средата: ромбче в цвета на типа (по оста, ширина 13).
        for (dx in -MOTIF_R..MOTIF_R) {
            if (dx % 2 == 0) cells.put(CENTRE_X + dx, 1, if (abs(dx) % 4 == 0) GOLD else BURGUNDY)
        }
        return cells.build()
    }

    // ───────────────────────── месеците ─────────────────────────

    /** Дванадесет неотрицателни броя; при най-висок над [MAX_COLUMN] — пропорционално смалени (поне 1 за непразен). */
    internal fun scaledMonths(s: ReadingYear.Summary): IntArray {
        val raw = IntArray(12) { i -> s.months.getOrElse(i) { 0 }.coerceAtLeast(0) }
        val max = raw.max()
        if (max <= MAX_COLUMN) return raw
        return IntArray(12) { i -> if (raw[i] == 0) 0 else (raw[i].toDouble() * MAX_COLUMN / max).roundToInt().coerceIn(1, MAX_COLUMN) }
    }

    /**
     * Редовете на колона с [n] бода — огледални около оста: при нечетно [n] — оста и по (n-1)/2
     * нагоре и надолу; при четно — по n/2 нагоре и надолу, а на оста е бордо „възел“ (`null`).
     * Празният месец е само възелът. Първо — оста, после на смени нагоре/надолу (посоката — по [seed]).
     */
    internal fun columnRows(n: Int, seed: Int): List<Int?> {
        if (n <= 0) return listOf(null)
        val up = if (seed and 1 == 0) -1 else 1
        val out = ArrayList<Int?>(n + 1)
        if (n % 2 == 1) out += CENTRE_Y else out += null
        for (k in 1..n / 2) {
            out += CENTRE_Y + up * k
            out += CENTRE_Y - up * k
        }
        return out
    }

    /**
     * Конците за [books] книги: разделите се разпределят равномерно (най-големият дефицит първи),
     * непознатите книги — в злато. Започва се от отместване по [seed], за да не си приличат лентите.
     */
    internal fun threadSequence(books: Int, realBooks: Int, sections: List<ReadingStats.SectionCount>, seed: Int): ByteArray {
        val n = books.coerceAtLeast(1)
        val known = sections.filter { it.section != null && it.section in 0..9 && it.count > 0 }
        if (known.isEmpty()) return ByteArray(n) { GOLD }
        val threads = ArrayList<Byte>().apply { known.forEach { add((THREAD_SECTION + it.section!!).toByte()) } }
        val weights = ArrayList<Int>().apply { known.forEach { add(it.count) } }
        // Книгите без познат раздел — в злато.
        val unknown = realBooks - known.sumOf { it.count }
        if (unknown > 0) { threads += GOLD; weights += unknown }
        val total = weights.sum()
        val used = IntArray(weights.size)
        val out = ByteArray(n)
        for (k in 0 until n) {
            var best = 0
            var bestDeficit = Double.NEGATIVE_INFINITY
            for (c in weights.indices) {
                val deficit = weights[c].toDouble() * (k + 1) / total - used[c]
                if (deficit > bestDeficit + 1e-9) { bestDeficit = deficit; best = c }
            }
            used[best]++
            out[k] = threads[best]
        }
        val shift = Math.floorMod(seed ushr 3, n)
        return ByteArray(n) { out[(it + shift) % n] }
    }

    /** Книгите по месеците преди смаляването. */
    private fun realBooks(s: ReadingYear.Summary): Int = (0 until 12).sumOf { s.months.getOrElse(it) { 0 }.coerceAtLeast(0) }

    // ───────────────────────── кантът и мотивът ─────────────────────────

    /** Конецът на канта на разстояние [d] от средата (или -1 — без бод). Три варианта, по [seed]. */
    private fun borderThread(d: Int, seed: Int): Int = when (Math.floorMod(seed ushr 1, 3)) {
        0 -> if (d % 2 == 0) GOLD.toInt() else BURGUNDY.toInt()
        1 -> if (d % 3 == 0) BURGUNDY.toInt() else GOLD.toInt()
        else -> if (d % 2 == 0) GOLD.toInt() else -1
    }

    private fun borders(cells: Cells, seed: Int) {
        for (x in 0 until COLUMNS) {
            val t = borderThread(abs(x - CENTRE_X), seed)
            if (t >= 0) {
                cells.put(x, 0, t.toByte())
                cells.put(x, ROWS - 1, t.toByte())
            }
        }
    }

    /** Мотивът на типа, 13×13, в средата на лентата. */
    private fun motif(cells: Cells, a: Archetype) {
        fun put(dx: Int, dy: Int, t: Byte) {
            if (abs(dx) > MOTIF_R || abs(dy) > MOTIF_R) return
            cells.put(CENTRE_X + dx, CENTRE_Y + dy, t)
        }
        when (a) {
            Archetype.BOOKWORM, Archetype.CURIOUS -> {
                // Осморъка: четири листенца по осите и четири по диагоналите.
                put(0, 0, BURGUNDY)
                for (sx in intArrayOf(-1, 1)) for (sy in intArrayOf(-1, 1)) put(sx, sy, GOLD)
                for (p in 2..MOTIF_R) {
                    val w = minOf(1, p - 2, MOTIF_R - p)
                    for (q in -w..w) {
                        val t = if (w > 0 && q == 0) BURGUNDY else GOLD
                        put(q, -p, t); put(p, q, t); put(-q, p, t); put(-p, -q, t)
                    }
                }
                for (k in 3 until MOTIF_R) for ((sx, sy) in Diagonals) {
                    put(sx * k, sy * k, BURGUNDY)
                    if (k < MOTIF_R - 1) { put(sx * (k + 1), sy * k, GOLD); put(sx * k, sy * (k + 1), GOLD) }
                }
            }
            Archetype.STEADY -> {
                // Ромб в ромба с точка.
                for (dy in -MOTIF_R..MOTIF_R) for (dx in -MOTIF_R..MOTIF_R) {
                    val d = abs(dx) + abs(dy)
                    when {
                        d == MOTIF_R -> put(dx, dy, GOLD)
                        d == MOTIF_R - 3 -> put(dx, dy, BURGUNDY)
                        d <= 1 -> put(dx, dy, GOLD)
                    }
                }
            }
            Archetype.LOYAL -> {
                // Дървото на живота: ствол, три двойки клони и корона.
                for (dy in -MOTIF_R..MOTIF_R) put(0, dy, BURGUNDY)
                for (level in 0..2) {
                    val y = 4 - 3 * level
                    for (j in 1..(3 - level)) { put(j, y - j, GOLD); put(-j, y - j, GOLD) }
                }
                put(1, -5, GOLD); put(-1, -5, GOLD); put(0, -6, GOLD)
                for (j in -2..2) put(j, MOTIF_R, GOLD)
                put(-3, 6, BURGUNDY); put(3, 6, BURGUNDY)
            }
            Archetype.EXPLORER -> {
                // Зигзаг: две преплетени вълни (златна и бордо).
                for (dx in -MOTIF_R..MOTIF_R) {
                    val t = abs(dx) % 6
                    val v = if (t <= 3) t else 6 - t
                    put(dx, -v - 1, GOLD)
                    put(dx, v + 1, GOLD)
                    put(dx, if (v == 0) 0 else v - 1, BURGUNDY)
                    if (v > 0) put(dx, -(v - 1), BURGUNDY)
                }
            }
        }
    }

    private val Diagonals = listOf(1 to -1, 1 to 1, -1 to 1, -1 to -1)

    // ───────────────────────── разни ─────────────────────────

    /** Отпечатък на данните — устойчив между пускания (без `hashCode` на `Summary`, който включва списъци от класове). */
    internal fun seedOf(s: ReadingYear.Summary): Int {
        var h = 17
        fun mix(v: Int) { h = h * 31 + v; h = h xor (h ushr 15) }
        mix(s.year)
        mix(s.books)
        for (i in 0 until 12) mix(s.months.getOrElse(i) { 0 })
        for (sc in s.sections) { mix(sc.section ?: -1); mix(sc.count) }
        mix(s.authors)
        mix(s.longestStreak)
        mix(s.archetype.ordinal)
        mix(s.favouriteAuthorCount)
        s.favouriteAuthor?.forEach { mix(it.code) }
        return h and Int.MAX_VALUE
    }

    /** Събира клетките (по една на място; по-късното презаписва) и ги подрежда по реда на иглата. */
    private class Cells(private val rows: Int = ROWS) {
        private val map = LinkedHashMap<Int, Byte>()

        fun put(x: Int, y: Int, t: Byte) {
            if (x !in 0 until COLUMNS || y !in 0 until rows) return
            map[y * COLUMNS + x] = t
        }

        /** Клетката и огледалната ѝ по хоризонтала. */
        fun mirrored(x: Int, y: Int, t: Byte) {
            put(x, y, t)
            put(COLUMNS - 1 - x, y, t)
        }

        fun build(): Chart {
            val keys = map.keys.sortedWith(
                compareBy<Int> { abs(it % COLUMNS - CENTRE_X) }.thenBy { it / COLUMNS }.thenBy { it % COLUMNS },
            )
            return Chart(
                COLUMNS, rows,
                IntArray(keys.size) { keys[it] % COLUMNS },
                IntArray(keys.size) { keys[it] / COLUMNS },
                ByteArray(keys.size) { map.getValue(keys[it]) },
            )
        }
    }
}
