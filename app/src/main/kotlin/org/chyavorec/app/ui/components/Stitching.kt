package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.clearAndSetSemantics
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaMotif
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min

/*
 * Бродираната шевица: мотивите от Decor.kt/Ornaments.kt, „преведени“ на кръстат бод — схема
 * от клетки, всяка клетка е едно „X“. Бодовете се появяват един след друг (по реда на иглата),
 * а всеки бод — първо „/“, после „\“, както се бродира на ръка. Схемите се строят веднъж;
 * при рисуване няма алокации — само линии.
 */

/**
 * Схема за кръстат бод: [columns]×[rows] клетки и бодовете в реда, в който ги бродира иглата.
 * [thread] — конецът на всеки бод ([StitchCharts.GOLD]/[StitchCharts.BURGUNDY]); [angle] — ъгълът
 * на бода около центъра (0..1 по часовника от върха) — за „тичащия“ конец.
 */
@Immutable
class StitchChart internal constructor(
    val columns: Int,
    val rows: Int,
    internal val col: IntArray,
    internal val row: IntArray,
    internal val thread: ByteArray,
    internal val angle: FloatArray,
) {
    val size: Int get() = col.size
}

/** Клетка от схемата (координати от горния ляв ъгъл). */
internal data class StitchCell(val x: Int, val y: Int, val thread: Byte)

/** Схемите — по мотивите на шевицата ([ShevitsaMotif]). Чисти функции (тестват се без рисуване). */
object StitchCharts {
    const val GOLD: Byte = 0
    const val BURGUNDY: Byte = 1

    /**
     * Осморъка: осем ромбоидни листенца около средата — четири по осите (бордо с жълт кант)
     * и четири по диагоналите (злато с бордо „жилка“). [radius] — от средата до края (схема 2r+1).
     */
    fun rosette(radius: Int = 6): StitchChart {
        val r = radius.coerceAtLeast(4)
        val cells = LinkedHashMap<Long, StitchCell>()
        fun put(dx: Int, dy: Int, t: Byte) {
            if (abs(dx) > r || abs(dy) > r) return
            cells[key(dx, dy)] = StitchCell(dx + r, dy + r, t)
        }
        // Средата: точка в бордо и четири златни зрънца по диагоналите.
        put(0, 0, BURGUNDY)
        for (sx in intArrayOf(-1, 1)) for (sy in intArrayOf(-1, 1)) put(sx, sy, GOLD)
        // Листенцата по осите: ширина 1-3-…-3-1 от 2 до r; кантът е златен, жилката — бордо.
        for (p in 2..r) {
            val w = minOf(1, p - 2, r - p)
            for (q in -w..w) {
                val t = if (w > 0 && q == 0) BURGUNDY else GOLD
                put(q, -p, t); put(p, q, t); put(-q, p, t); put(-p, -q, t)
            }
        }
        // Листенцата по диагоналите: гръбче (k,k) от 3 до r-1 и странични бодове до него.
        for (k in 3 until r) {
            for ((sx, sy) in Diagonals) {
                put(sx * k, sy * k, BURGUNDY)
                if (k < r - 1) {
                    put(sx * (k + 1), sy * k, GOLD)
                    put(sx * k, sy * (k + 1), GOLD)
                }
            }
        }
        return radial(2 * r + 1, cells.values)
    }

    /** Ромб в ромба с точка — „пазещият“ знак ([ShevitsaMotif.RHOMBUS]): златен кант, бордо вътре. */
    fun rhombus(radius: Int = 7): StitchChart {
        val r = radius.coerceAtLeast(4)
        val cells = ArrayList<StitchCell>()
        for (dy in -r..r) for (dx in -r..r) {
            val d = abs(dx) + abs(dy)
            val t = when {
                d == r -> GOLD
                d == r - 3 -> BURGUNDY
                d <= 1 -> GOLD
                else -> null
            } ?: continue
            cells += StitchCell(dx + r, dy + r, t)
        }
        return radial(2 * r + 1, cells)
    }

    /**
     * Лента от [count] ромба (7 клетки висока) — като края на везана кърпа: златни ромбове с
     * бордо зрънце и бордо бод между тях. Бродира се отляво надясно, колона по колона.
     */
    fun band(count: Int = 5): StitchChart {
        val n = count.coerceAtLeast(1)
        val period = 8
        val cols = n * period + 1
        val cells = ArrayList<StitchCell>()
        for (x in 0 until cols) {
            val lx = x % period
            for (dy in -3..3) {
                val t = when {
                    lx == 0 && dy == 0 -> BURGUNDY
                    lx != 0 && abs(lx - 4) + abs(dy) == 3 -> GOLD
                    lx != 0 && abs(lx - 4) + abs(dy) == 0 -> BURGUNDY
                    else -> null
                } ?: continue
                cells += StitchCell(x, dy + 3, t)
            }
        }
        // Иглата върви колона по колона — надолу, после нагоре (както се шие ред).
        val ordered = cells.sortedWith(compareBy<StitchCell> { it.x }.thenBy { if (it.x % 2 == 0) it.y else -it.y })
        return chart(cols, 7, ordered)
    }

    /** Схемата за мотива на деня: ромбовете и осморъката са свои; другите — осморъка. */
    fun forMotif(motif: ShevitsaMotif, radius: Int = 6): StitchChart = when (motif) {
        ShevitsaMotif.RHOMBUS -> rhombus(radius)
        else -> rosette(radius)
    }

    private val Diagonals = listOf(1 to -1, 1 to 1, -1 to 1, -1 to -1)

    private fun key(dx: Int, dy: Int): Long = (dx.toLong() shl 32) or (dy.toLong() and 0xFFFFFFFFL)

    /** Кръгова схема: средата първа, после по часовника от върха (а при равен ъгъл — отвътре навън). */
    private fun radial(side: Int, cells: Collection<StitchCell>): StitchChart {
        val c = side / 2
        val ordered = cells.sortedWith(
            compareBy<StitchCell> { if (it.x == c && it.y == c) 0 else 1 }
                .thenBy { StitchMath.angleOf(it.x - c, it.y - c) }
                .thenBy { abs(it.x - c) + abs(it.y - c) },
        )
        return chart(side, side, ordered)
    }

    private fun chart(cols: Int, rows: Int, cells: List<StitchCell>): StitchChart {
        val cx = (cols - 1) / 2
        val cy = (rows - 1) / 2
        return StitchChart(
            cols, rows,
            IntArray(cells.size) { cells[it].x },
            IntArray(cells.size) { cells[it].y },
            ByteArray(cells.size) { cells[it].thread },
            FloatArray(cells.size) { StitchMath.angleOf(cells[it].x - cx, cells[it].y - cy) },
        )
    }
}

/** Математиката на бродирането — чисти функции. */
object StitchMath {
    /** Ъгълът на (dx, dy) около центъра: 0..1 по часовника от върха (y расте надолу); средата — 0. */
    fun angleOf(dx: Int, dy: Int): Float {
        if (dx == 0 && dy == 0) return 0f
        val a = atan2(dx.toDouble(), -dy.toDouble()) / (2 * PI)
        return (if (a < 0) a + 1 else a).toFloat().let { if (it >= 1f) 0f else it }
    }

    /** Колко от бода [index] (от [count]) е готов при общ прогрес [progress]: 0 — нищо, 1 — целият „X“. */
    fun stitchFraction(progress: Float, index: Int, count: Int): Float =
        if (count <= 0) 0f else (progress.coerceIn(0f, 1f) * count - index).coerceIn(0f, 1f)

    /** Първата диагонал („/“) — първата половина от бода. */
    fun firstLeg(fraction: Float): Float = (fraction * 2f).coerceIn(0f, 1f)

    /** Втората диагонал („\“) — втората половина, върху първата. */
    fun secondLeg(fraction: Float): Float = (fraction * 2f - 1f).coerceIn(0f, 1f)

    /** Колко бода са завършени при [progress]. */
    fun completed(progress: Float, count: Int): Int = (progress.coerceIn(0f, 1f) * count).toInt().coerceIn(0, count)

    /**
     * Яркостта на „тичащия“ конец за бод с ъгъл [angle], когато главата е при [phase] (и двете
     * 0..1): 1 в главата, затихва по опашката [tail] назад; пред главата — 0.
     */
    fun runGlow(angle: Float, phase: Float, tail: Float = 0.25f): Float {
        var d = (phase - angle) % 1f
        if (d < 0f) d += 1f
        return if (d > tail) 0f else 1f - d / tail
    }
}

/** Конецът в бордо: в тъмна тема — по-светъл, за да се вижда върху мастиления фон. */
@Composable
fun stitchBurgundy(): Color = if (LocalExtendedColors.current.isDark) DarkBurgundyThread else Brand.Burgundy

internal val DarkBurgundyThread = Color(0xFFC25B6B)
private val Sheen = Color.White

/**
 * Шевица, бродирана бод по бод до [progress] (0..1, чете се само при рисуване — без
 * рекомпозиция на кадър). [glowPhase] ≥ 0 — „тичащ“ конец по кръга (фаза 0..1), < 0 — без.
 * Декоративна — без семантика.
 */
@Composable
fun StitchedMotif(
    chart: StitchChart,
    progress: () -> Float,
    modifier: Modifier = Modifier,
    gold: Color = LocalExtendedColors.current.gold,
    burgundy: Color = stitchBurgundy(),
    glowPhase: () -> Float = { -1f },
    alpha: () -> Float = { 1f },
    sections: List<Color>? = null,
) {
    Spacer(
        modifier.clearAndSetSemantics { }.drawBehind {
            drawStitches(chart, progress(), gold, burgundy, glowPhase(), alpha(), sections)
        },
    )
}

/**
 * Шевица, която се избродира веднъж при появата си (~[durationMs]) и остава; при връщане към
 * екрана (и при намалено движение) — направо готовата. [stillProgress] — неподвижен кадър (тестове).
 */
@Composable
fun StitchInOrnament(
    chart: StitchChart,
    modifier: Modifier = Modifier,
    durationMs: Int = 1400,
    stillProgress: Float? = null,
    sections: List<Color>? = null,
) {
    val reduced = rememberReducedMotion()
    var played by rememberSaveable { mutableStateOf(false) }
    val animate = !played && !reduced && stillProgress == null
    val progress = remember { Animatable(if (animate) 0f else 1f) }
    if (animate) {
        LaunchedEffect(Unit) {
            progress.animateTo(1f, tween(durationMs, easing = LinearOutSlowInEasing))
            played = true
        }
    }
    StitchedMotif(chart, progress = { stillProgress ?: if (reduced) 1f else progress.value }, modifier = modifier, sections = sections)
}

/*
 * Един речник на бода за цялото приложение ([StitchSpec]): тичащ бод (тънки къси черти),
 * „тропосване“ (по-дълги, по-плътни) и кръстат бод. Всеки пунктир, конец и „X“ на екраните
 * идва оттук — затова изглеждат еднакво навсякъде.
 */

/** Трите вида бод. */
enum class StitchKind { RUNNING, BASTING, CROSS }

/**
 * Пунктирен бод: дебелина [widthDp], видима черта [dashDp] и видима празнина [gapDp] (с кръглите
 * краища — интервалите на ефекта са съответно по-къси/по-дълги). [stroke] пази готовите обекти
 * за последните няколко мащаба — при рисуване не се алокира нищо.
 */
class DashStitch internal constructor(
    val kind: StitchKind,
    val widthDp: Float,
    val dashDp: Float,
    val gapDp: Float,
) {
    private val keys = FloatArray(CACHE) { -1f }
    private val strokes = arrayOfNulls<Stroke>(CACHE)
    private var next = 0

    /**
     * Готовият [Stroke] при [pxPerDp] пиксела на dp (обикновено `density`; в мащабиран път —
     * `1.dp.toPx() / мащаба`). [scale] увеличава целия бод пропорционално (едра украса).
     */
    fun stroke(pxPerDp: Float, scale: Float = 1f): Stroke {
        val k = pxPerDp * scale
        for (i in 0 until CACHE) if (keys[i] == k) return strokes[i]!!
        val w = widthDp * k
        val made = Stroke(
            width = w,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf((dashDp * k - w).coerceAtLeast(0.01f * k), gapDp * k + w)),
        )
        keys[next] = k
        strokes[next] = made
        next = (next + 1) % CACHE
        return made
    }

    /** Само ефектът (за drawLine с четка). */
    fun pathEffect(pxPerDp: Float, scale: Float = 1f): PathEffect = stroke(pxPerDp, scale).pathEffect!!

    private companion object {
        const val CACHE = 4
    }
}

/** Пропорциите на кръстатия бод — дялове от клетката. */
object CrossStitchSpec {
    /** Дебелината на конеца. */
    const val STROKE = 0.26f

    /** Отстъпът на „X“ от ръба на клетката. */
    const val INSET = 0.12f

    /** Кантът (по желание): по-широк от конеца, рисува се под него. */
    const val OUTLINE = 1.5f

    /** Отблясъкът: тънка светла линия малко над средата на конеца. */
    const val SHINE = 0.32f
    const val LIFT = 0.16f
    const val SHINE_ALPHA = 0.28f
}

/** Единственият източник на бодовете (пунктири и „X“). */
object StitchSpec {
    /** Тичащ бод: ~1 dp, 3/3 — тънки контури, печати, малки знаци. */
    val Running = DashStitch(StitchKind.RUNNING, widthDp = 1.2f, dashDp = 3f, gapDp = 3f)

    /** Тропосване: ~2 dp, 6/5 — подгъви, перфорации, нишки на голяма площ. */
    val Basting = DashStitch(StitchKind.BASTING, widthDp = 1.6f, dashDp = 6f, gapDp = 5f)

    /** Кръстат бод — пропорциите са в [CrossStitchSpec]. */
    val Cross = CrossStitchSpec
}

/**
 * Един кръстат бод в клетка [left]..[right] × [top]..[bottom] (вече отстъпени с [CrossStitchSpec.INSET]):
 * първо „/“ (до [first]), после „\“ (до [second]) — като на ръка; с отблясък и, по желание, кант [outline].
 */
fun DrawScope.crossStitch(
    left: Float, top: Float, right: Float, bottom: Float,
    color: Color, cell: Float,
    first: Float = 1f, second: Float = 1f,
    alpha: Float = 1f, outline: Color? = null,
) {
    val stroke = cell * CrossStitchSpec.STROKE
    if (outline != null) {
        val w = stroke * CrossStitchSpec.OUTLINE
        leg(left, bottom, right, top, first, outline, w, 0f, 0f, alpha)
        leg(left, top, right, bottom, second, outline, w, 0f, 0f, alpha)
    }
    val shine = stroke * CrossStitchSpec.SHINE
    val lift = stroke * CrossStitchSpec.LIFT
    // „/“ — от долу-ляво нагоре-дясно.
    leg(left, bottom, right, top, first, color, stroke, shine, lift, alpha)
    // „\“ — отгоре, от горе-ляво надолу-дясно.
    leg(left, top, right, bottom, second, color, stroke, shine, lift, alpha)
}

/** Рисува [chart] до [progress]: всеки бод — две диагонали с тънък отблясък и „игла“ на текущия. */
fun DrawScope.drawStitches(
    chart: StitchChart,
    progress: Float,
    gold: Color,
    burgundy: Color,
    glowPhase: Float = -1f,
    alpha: Float = 1f,
    sections: List<Color>? = null,
) {
    val n = chart.size
    if (n == 0 || alpha <= 0f || progress <= 0f) return
    val cell = min(size.width / chart.columns, size.height / chart.rows)
    if (cell <= 0f) return
    val ox = (size.width - cell * chart.columns) / 2f
    val oy = (size.height - cell * chart.rows) / 2f
    val inset = cell * CrossStitchSpec.INSET
    val stroke = cell * CrossStitchSpec.STROKE
    val done = progress.coerceIn(0f, 1f) * n
    for (i in 0 until n) {
        val f = (done - i).coerceIn(0f, 1f)
        if (f <= 0f) break // бодовете са по ред — нататък нищо не е бродирано
        val left = ox + chart.col[i] * cell + inset
        val right = ox + (chart.col[i] + 1) * cell - inset
        val top = oy + chart.row[i] * cell + inset
        val bottom = oy + (chart.row[i] + 1) * cell - inset
        val th = chart.thread[i].toInt()
        // Конец 0 — злато, 1 — бордо, 2+ — цвят от [sections] (раздел по УДК); без палитра — злато.
        val base = when {
            th == StitchCharts.BURGUNDY.toInt() -> burgundy
            th >= 2 && sections != null -> sections.getOrNull(th - 2) ?: gold
            else -> gold
        }
        val glow = if (glowPhase >= 0f) StitchMath.runGlow(chart.angle[i], glowPhase) else 0f
        val color = if (glow > 0f) lerp(base, Sheen, 0.55f * glow) else base
        val a = StitchMath.firstLeg(f)
        val b = StitchMath.secondLeg(f)
        crossStitch(left, top, right, bottom, color, cell, a, b, alpha)
        if (f < 1f) {
            // Иглата: светла точка в края на конеца, който се бродира в момента.
            val head = if (b > 0f) Offset(left + (right - left) * b, top + (bottom - top) * b)
            else Offset(left + (right - left) * a, bottom + (top - bottom) * a)
            drawCircle(Sheen, stroke * 0.55f, head, alpha = 0.85f * alpha)
        }
    }
}

/**
 * Много кръстати бода наведнъж (платното на „Извези шевица“, до 225 клетки): по един път на
 * конец, всички канти под всички конци — съседните бодове се сливат като истински конец.
 * Пътищата се преизползват между кадрите.
 */
class CrossStitchBatch(threads: Int) {
    internal val paths = Array(threads) { Path() }
    private var strokeCell = -1f
    internal var edge = Stroke(0f)
    internal var body = Stroke(0f)
    internal var shine = Stroke(0f)

    /** Конецът, кантът и отблясъкът за клетка [cell] — строят се наново само при нов размер. */
    internal fun strokesFor(cell: Float) {
        if (cell == strokeCell) return
        strokeCell = cell
        val w = cell * CrossStitchSpec.STROKE
        edge = Stroke(w * CrossStitchSpec.OUTLINE, cap = StrokeCap.Round, join = StrokeJoin.Round)
        body = Stroke(w, cap = StrokeCap.Round, join = StrokeJoin.Round)
        shine = Stroke(w * CrossStitchSpec.SHINE, cap = StrokeCap.Round, join = StrokeJoin.Round)
    }

    fun reset() {
        for (p in paths) p.rewind()
    }

    /** Бод с конец [thread] (0..threads-1) в клетката с горен ляв ъгъл ([x], [y]) и страна [cell]. */
    fun add(thread: Int, x: Float, y: Float, cell: Float) {
        val p = paths.getOrNull(thread) ?: return
        val inset = cell * CrossStitchSpec.INSET
        val x0 = x + inset
        val y0 = y + inset
        val x1 = x + cell - inset
        val y1 = y + cell - inset
        p.moveTo(x0, y1); p.lineTo(x1, y0)
        p.moveTo(x0, y0); p.lineTo(x1, y1)
    }
}

/** Рисува [batch]: кант ([outline], по желание), конецът ([thread]) и отблясъкът — същият бод като [crossStitch]. */
fun DrawScope.drawCrossStitches(batch: CrossStitchBatch, cell: Float, thread: (Int) -> Color, outline: ((Int) -> Color)? = null) {
    val w = cell * CrossStitchSpec.STROKE
    val paths = batch.paths
    batch.strokesFor(cell)
    if (outline != null) {
        for (i in paths.indices) if (!paths[i].isEmpty) drawPath(paths[i], outline(i), style = batch.edge)
    }
    for (i in paths.indices) if (!paths[i].isEmpty) drawPath(paths[i], thread(i), style = batch.body)
    translate(top = -w * CrossStitchSpec.LIFT) {
        for (i in paths.indices) if (!paths[i].isEmpty) drawPath(paths[i], Sheen, alpha = CrossStitchSpec.SHINE_ALPHA, style = batch.shine)
    }
}

private fun DrawScope.leg(
    x0: Float, y0: Float, x1: Float, y1: Float, t: Float,
    color: Color, stroke: Float, shine: Float, lift: Float, alpha: Float,
) {
    if (t <= 0f) return
    val ex = x0 + (x1 - x0) * t
    val ey = y0 + (y1 - y0) * t
    drawLine(color, Offset(x0, y0), Offset(ex, ey), strokeWidth = stroke, cap = StrokeCap.Round, alpha = alpha)
    // Отблясък по конеца — малко над средата му (светлината идва отгоре).
    if (shine > 0f) {
        drawLine(Sheen, Offset(x0, y0 - lift), Offset(ex, ey - lift), strokeWidth = shine, cap = StrokeCap.Round, alpha = CrossStitchSpec.SHINE_ALPHA * alpha)
    }
}
