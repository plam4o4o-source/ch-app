package org.chyavorec.domain.embroidery

/**
 * Моделът на „Извези шевица“: квадратна мрежа от клетки за кръстат бод, пет традиционни цвята
 * и огледална симетрия. Чиста логика (тества се без Android); рисуването е в приложението.
 */

/** Петте конеца; кодът в мрежата е [code] (0 = празна клетка). */
enum class StitchColor {
    BURGUNDY, GOLD, BLACK, CREAM, GREEN;

    val code: Int get() = ordinal + 1

    companion object {
        /** Конецът с код [code] (1..5) или `null` — за празна или невалидна клетка. */
        fun of(code: Int): StitchColor? = entries.getOrNull(code - 1)
    }
}

/** Огледало: [MIRROR] — наляво/надясно (двойна), [QUAD] — и нагоре/надолу (четворна). */
enum class Symmetry { NONE, MIRROR, QUAD }

/** Готова (неизменна) шевица: [size]×[size] клетки, всяка 0 (празна) или код на [StitchColor]. */
class StitchPattern private constructor(val size: Int, private val cells: ByteArray) {
    init {
        require(size in MIN_SIZE..MAX_SIZE && cells.size == size * size) { "size=$size cells=${cells.size}" }
    }

    /** Кодът на клетката; извън мрежата — 0. */
    operator fun get(x: Int, y: Int): Int = if (x in 0 until size && y in 0 until size) cells[y * size + x].toInt() else 0

    val stitchCount: Int get() = cells.count { it.toInt() != 0 }
    val isEmpty: Boolean get() = cells.all { it.toInt() == 0 }

    internal fun copyCells(): ByteArray = cells.copyOf()

    /** "15:" и по една цифра на клетка (ред по ред) — за запис във файл. */
    fun encode(): String = buildString(size * size + 4) {
        append(size).append(':')
        for (c in cells) append('0' + c.toInt())
    }

    override fun equals(other: Any?): Boolean = other is StitchPattern && other.size == size && other.cells.contentEquals(cells)
    override fun hashCode(): Int = 31 * size + cells.contentHashCode()

    companion object {
        const val DEFAULT_SIZE = 15
        const val MIN_SIZE = 5
        const val MAX_SIZE = 31

        fun empty(size: Int = DEFAULT_SIZE): StitchPattern = StitchPattern(size, ByteArray(size * size))

        internal fun of(size: Int, cells: ByteArray): StitchPattern = StitchPattern(size, cells.copyOf())

        /**
         * Чете [encode]; `null` при всичко нередно (размер извън границите, различна дължина,
         * знак, различен от 0..5) — повреден файл не бива да срине екрана.
         */
        fun decode(text: String): StitchPattern? {
            val at = text.indexOf(':')
            if (at !in 1..2) return null
            val size = text.substring(0, at).toIntOrNull() ?: return null
            if (size !in MIN_SIZE..MAX_SIZE || text.length != at + 1 + size * size) return null
            val cells = ByteArray(size * size)
            for (i in cells.indices) {
                val d = text[at + 1 + i] - '0'
                if (d !in 0..StitchColor.entries.size) return null
                cells[i] = d.toByte()
            }
            return StitchPattern(size, cells)
        }

        /**
         * Шаблон от редове текст: '.' — празно, 'R' бордо, 'G' злато, 'K' черно, 'C' кремаво,
         * 'V' зелено. Всички редове са с еднаква дължина, равна на броя им.
         */
        fun ofRows(rows: List<String>): StitchPattern {
            val n = rows.size
            require(n in MIN_SIZE..MAX_SIZE && rows.all { it.length == n }) { "шаблонът не е квадрат" }
            val cells = ByteArray(n * n)
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, ch ->
                    cells[y * n + x] = when (ch) {
                        '.' -> 0
                        'R' -> StitchColor.BURGUNDY.code
                        'G' -> StitchColor.GOLD.code
                        'K' -> StitchColor.BLACK.code
                        'C' -> StitchColor.CREAM.code
                        'V' -> StitchColor.GREEN.code
                        else -> throw IllegalArgumentException("знак '$ch'")
                    }.toByte()
                }
            }
            return StitchPattern(n, cells)
        }
    }
}

/**
 * Платното, което се редактира: мрежата, огледалото и историята за „Върни/Повтори“. Едно
 * движение на пръста (от [beginStroke] до [endStroke]) е една стъпка в историята; историята е
 * до [MAX_UNDO] стъпки (най-старите отпадат).
 */
class StitchBoard(val size: Int = StitchPattern.DEFAULT_SIZE) {
    private class Edit(val index: IntArray, val before: ByteArray, val after: ByteArray)

    private val cells = ByteArray(size * size)
    private val undoStack = ArrayDeque<Edit>()
    private val redoStack = ArrayDeque<Edit>()
    /** Клетка → стойността ѝ преди движението (само първата промяна). */
    private var open: LinkedHashMap<Int, Byte>? = null

    var symmetry: Symmetry = Symmetry.NONE

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()
    val stitchCount: Int get() = cells.count { it.toInt() != 0 }

    operator fun get(x: Int, y: Int): Int = if (x in 0 until size && y in 0 until size) cells[y * size + x].toInt() else 0

    fun snapshot(): StitchPattern = StitchPattern.of(size, cells)

    fun beginStroke() {
        endStroke()
        open = LinkedHashMap()
    }

    /**
     * Слага бод с код [code] (0 — маха) в (x, y) и в огледалните му клетки. Извън мрежата — нищо.
     * @return дали нещо се промени (за вибрацията).
     */
    fun paint(x: Int, y: Int, code: Int): Boolean {
        if (x !in 0 until size || y !in 0 until size || code !in 0..StitchColor.entries.size) return false
        val stroke = open ?: LinkedHashMap<Int, Byte>().also { open = it }
        var changed = false
        for (i in mirrored(x, y)) {
            val old = cells[i]
            if (old.toInt() == code) continue
            stroke.putIfAbsent(i, old)
            cells[i] = code.toByte()
            changed = true
        }
        return changed
    }

    /** Затваря движението: ако е променило нещо — стъпка в историята (и се изчиства „Повтори“). */
    fun endStroke() {
        val stroke = open ?: return
        open = null
        val changed = stroke.filter { (i, old) -> cells[i] != old }
        if (changed.isEmpty()) return
        val index = changed.keys.toIntArray()
        push(Edit(index, ByteArray(index.size) { changed.getValue(index[it]) }, ByteArray(index.size) { cells[index[it]] }))
        redoStack.clear()
    }

    fun undo(): Boolean {
        endStroke()
        val e = undoStack.removeLastOrNull() ?: return false
        for (k in e.index.indices) cells[e.index[k]] = e.before[k]
        redoStack.addLast(e)
        return true
    }

    fun redo(): Boolean {
        endStroke()
        val e = redoStack.removeLastOrNull() ?: return false
        for (k in e.index.indices) cells[e.index[k]] = e.after[k]
        undoStack.addLast(e)
        return true
    }

    /** Цялата мрежа се заменя с [pattern] (шаблон, отворена шевица) — една стъпка, която се връща. */
    fun load(pattern: StitchPattern): Boolean {
        if (pattern.size != size) return false
        endStroke()
        val next = pattern.copyCells()
        val diff = cells.indices.filter { cells[it] != next[it] }
        if (diff.isEmpty()) return true
        val index = diff.toIntArray()
        push(Edit(index, ByteArray(index.size) { cells[index[it]] }, ByteArray(index.size) { next[index[it]] }))
        redoStack.clear()
        next.copyInto(cells)
        return true
    }

    /** Заменя мрежата без следа в историята (отваряне на запазена чернова); историята се изчиства. */
    fun reset(pattern: StitchPattern): Boolean {
        if (pattern.size != size) return false
        open = null
        undoStack.clear()
        redoStack.clear()
        pattern.copyCells().copyInto(cells)
        return true
    }

    fun clear() {
        load(StitchPattern.empty(size))
    }

    private fun push(e: Edit) {
        undoStack.addLast(e)
        while (undoStack.size > MAX_UNDO) undoStack.removeFirst()
    }

    /** Индексите на клетката и огледалата ѝ (без повторения). */
    internal fun mirrored(x: Int, y: Int): IntArray {
        val mx = size - 1 - x
        val my = size - 1 - y
        return when (symmetry) {
            Symmetry.NONE -> intArrayOf(y * size + x)
            Symmetry.MIRROR -> if (mx == x) intArrayOf(y * size + x) else intArrayOf(y * size + x, y * size + mx)
            Symmetry.QUAD -> linkedSetOf(y * size + x, y * size + mx, my * size + x, my * size + mx).toIntArray()
        }
    }

    companion object {
        const val MAX_UNDO = 100
    }
}

/** Геометрията на влаченето: клетките по права между две точки, за да не остават дупки. */
object StitchGeometry {
    /** Клетките от (x0, y0) до (x1, y1) включително (Брезенхам); първата е (x0, y0). */
    fun line(x0: Int, y0: Int, x1: Int, y1: Int): List<Pair<Int, Int>> {
        val out = ArrayList<Pair<Int, Int>>()
        var x = x0
        var y = y0
        val dx = kotlin.math.abs(x1 - x0)
        val dy = -kotlin.math.abs(y1 - y0)
        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1
        var err = dx + dy
        while (true) {
            out += x to y
            if (x == x1 && y == y1) break
            val e2 = 2 * err
            if (e2 >= dy) { err += dy; x += sx }
            if (e2 <= dx) { err += dx; y += sy }
        }
        return out
    }
}

/** Шаблоните, които не идват от схемите на приложението (те са от `StitchCharts`). */
object EmbroideryTemplates {
    /** Дървото на живота: стебло, четири двойки клонки с цветове, птичка отгоре и земя. */
    fun treeOfLife(): StitchPattern = StitchPattern.ofRows(
        listOf(
            ".......R.......",
            "......RGR......",
            ".......R.......",
            ".R.....G.....R.",
            "RGR.V..G..V.RGR",
            ".R...V.G.V...R.",
            "......VGV......",
            ".......G.......",
            ".R.....G.....R.",
            "RGR.V..G..V.RGR",
            ".R...V.G.V...R.",
            "......VGV......",
            ".......G.......",
            "....KKKGKKK....",
            "..KKKKKKKKKKK..",
        ),
    )

    /** Кривулица: три зигзага — златен, бордо и черен — с връх по средата (огледална). */
    fun zigzag(size: Int = StitchPattern.DEFAULT_SIZE): StitchPattern {
        val wave = intArrayOf(3, 2, 1, 0, 1, 2)
        val mid = size / 2
        val cells = ByteArray(size * size)
        val bands = listOf(StitchColor.GOLD to 1, StitchColor.BURGUNDY to 6, StitchColor.BLACK to 11)
        for (x in 0 until size) {
            val t = wave[Math.floorMod(x - mid, wave.size)]
            for ((color, top) in bands) {
                val y = top + t
                if (y in 0 until size) cells[y * size + x] = color.code.toByte()
            }
        }
        return StitchPattern.of(size, cells)
    }
}
