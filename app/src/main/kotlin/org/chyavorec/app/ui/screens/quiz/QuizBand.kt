package org.chyavorec.app.ui.screens.quiz

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.components.CrossStitchSpec
import org.chyavorec.app.ui.components.HapticSignature
import org.chyavorec.app.ui.components.LocalConfetti
import org.chyavorec.app.ui.components.StitchChart
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.drawStitches
import org.chyavorec.app.ui.components.goldSheen
import org.chyavorec.app.ui.components.rememberAppHaptics
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.AppMotion

/*
 * „Избродирай отговорите“: лента от по един мотив за въпрос (схемата на [StitchCharts.band],
 * разрязана на мотиви). Верният отговор избродира своя мотив веднъж, грешният оставя празно
 * място с увиснал конец, а при всички верни последният бод затваря лентата (конфети, вибрация
 * и златен отблясък). Лентата се изчислява от отговорите — затова се пази заедно с викторината на деня.
 */

/** Мотивите на лентата и геометрията ѝ — чисти функции (тестват се без рисуване). */
internal object QuizBandCharts {
    /** Колони на мотив (бодът-връзка вляво + ромбът). */
    const val PERIOD = 8

    /** Редове на схемата; лентата е с по един ред „плат“ над и под нея. */
    const val CHART_ROWS = 7
    const val ROWS = CHART_ROWS + 2

    private val cache = HashMap<Int, Pair<List<StitchChart>, StitchChart>>()

    /** Колоните на цялата лента от [n] мотива (и затварящия бод вдясно). */
    fun columns(n: Int): Int = PERIOD * n.coerceAtLeast(1) + 1

    /** Отношението ширина/височина на лентата. */
    fun aspect(n: Int): Float = columns(n).toFloat() / ROWS

    /** [n] мотива и затварящият бод — строят се веднъж за броя. */
    fun of(n: Int): Pair<List<StitchChart>, StitchChart> = synchronized(cache) {
        cache.getOrPut(n.coerceAtLeast(1)) {
            val m = n.coerceAtLeast(1)
            val band = StitchCharts.band(m)
            List(m) { slice(band, it * PERIOD, PERIOD) } to slice(band, m * PERIOD, 1)
        }
    }

    /** Колоните [from]..[from]+[width] от схемата — в същия ред на иглата. */
    private fun slice(band: StitchChart, from: Int, width: Int): StitchChart {
        val idx = (0 until band.size).filter { band.col[it] in from until from + width }
        return StitchChart(
            width, band.rows,
            IntArray(idx.size) { band.col[idx[it]] - from },
            IntArray(idx.size) { band.row[idx[it]] },
            ByteArray(idx.size) { band.thread[idx[it]] },
            FloatArray(idx.size),
        )
    }

    /** Затворена лента: отговорени са всички въпроси и всички — вярно. */
    fun closed(results: List<Boolean>, total: Int): Boolean = total > 0 && results.size >= total && results.take(total).all { it }

    /**
     * Началото на конфетито: над средата на лентата [band], [lift] (px) над горния ѝ край — така
     * ветрилото остава над бутона под лентата. Без размери — неуточнено (долу в средата на екрана).
     */
    fun confettiOrigin(band: Rect?, lift: Float): Offset =
        if (band == null) Offset.Unspecified else Offset(band.center.x, (band.top - lift).coerceAtLeast(0f))

    /** Колко мотива са избродирани. */
    fun stitched(results: List<Boolean>, total: Int): Int = results.take(total).count { it }
}

/** Цветовете на лентата: конците, „дупките“ на схемата, конецът на текущия въпрос, платът и подгъвът. */
internal class QuizBandColors(
    val gold: Color,
    val burgundy: Color,
    val holes: Color,
    val current: Color,
    val cloth: Color,
    val hem: Color,
)

/** Продължителност на бродирането на един мотив и на затварящия бод. */
private const val STITCH_MS = AppMotion.StitchMs
private const val CLOSE_MS = AppMotion.ShortMs

/** Затварящият бод (и конфетито с него) тръгва, докато иглата довършва последния мотив. */
internal const val CLOSE_LEAD_MS = STITCH_MS * 7L / 10

/**
 * Колко над лентата изскача конфетито: частиците първо се издигат до ~100 dp, после падат
 * ~100–200 dp, докато са плътни — с толкова отгоре ветрилото не минава през бутона.
 */
internal val ConfettiLift = 72.dp

/**
 * Лентата на екрана: новият верен отговор се избродира веднъж (при връщане към викторината и при
 * намалено движение — направо готов). Пълната лента: конфетито тръгва заедно със затварящия бод,
 * накрая — вибрация „печат“ и един златен отблясък по конците.
 */
@Composable
internal fun QuizBand(results: List<Boolean>, total: Int, current: Int?, colors: QuizBandColors, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    // Видените досега отговори не се бродират наново (връщане към екрана, завъртане, нов процес).
    var seen by rememberSaveable { mutableIntStateOf(results.size) }
    var stitching by remember { mutableIntStateOf(-1) }
    val stitch = remember { Animatable(1f) }
    // Затварящият бод: 0 — няма го, 1 — бродира се, 2 — готов.
    var closePhase by remember { mutableIntStateOf(if (QuizBandCharts.closed(results, total)) 2 else 0) }
    val close = remember { Animatable(0f) }
    var sheen by remember { mutableStateOf(false) }
    val confetti = LocalConfetti.current
    val haptics = rememberAppHaptics()
    val lift = with(LocalDensity.current) { ConfettiLift.toPx() }
    val bounds = remember { arrayOfNulls<Rect>(1) }
    LaunchedEffect(results.size, total) {
        if (results.size <= seen) {
            seen = results.size
            return@LaunchedEffect
        }
        val i = results.size - 1
        val closes = QuizBandCharts.closed(results, total)
        val stitches = results.getOrNull(i) == true && !reduced
        // Иглата е на мотива, преди той да спре да е „нов“ — без кадър с готовия мотив.
        if (stitches) {
            stitch.snapTo(0f)
            stitching = i
        }
        seen = results.size
        if (reduced) {
            if (closes) {
                closePhase = 2
                haptics.play(HapticSignature.SEAL)
            }
            return@LaunchedEffect
        }
        coroutineScope {
            if (stitches) {
                launch {
                    try {
                        stitch.animateTo(1f, tween(STITCH_MS, easing = LinearOutSlowInEasing))
                    } finally {
                        stitching = -1
                    }
                }
            }
            if (closes) {
                // По кадрите (не delay): иглата и без това рисува всеки кадър, а часовникът е този на анимациите.
                val start = withFrameMillis { it }
                var elapsed = 0L
                while (elapsed < CLOSE_LEAD_MS) elapsed = withFrameMillis { it } - start
                closePhase = 1
                confetti?.burst(QuizBandCharts.confettiOrigin(bounds[0], lift))
                try {
                    close.snapTo(0f)
                    close.animateTo(1f, tween(CLOSE_MS))
                } finally {
                    closePhase = 2
                }
                haptics.play(HapticSignature.SEAL)
                sheen = true
            }
        }
    }
    // Нов верен отговор, за който ефектът още не е тръгнал: мотивът е празен (не готов) до първия кадър.
    val fresh = !reduced && results.size > seen && results.lastOrNull() == true
    val animating = if (stitching >= 0) stitching else if (fresh) results.size - 1 else -1
    QuizBandCanvas(
        results, total, current, colors,
        modifier.onGloballyPositioned { bounds[0] = it.boundsInRoot() },
        animating = animating, stitchProgress = { if (stitching >= 0) stitch.value else 0f },
        closing = closePhase == 1, closeShown = closePhase == 2, closeProgress = { close.value },
        sheenKey = if (sheen) SHEEN_KEY else null,
    )
}

private const val SHEEN_KEY = "quiz-band"

/**
 * Лентата на три пласта: платът с подгъвите и дупките (прости черти и точки); готовите мотиви —
 * в собствен слой извън екрана, който кадрите на бродирането не рисуват наново; и само мотивът
 * под иглата ([animating], [closing]) — прогресът се чете при рисуване, без рекомпозиция.
 * [sheenKey] — един златен отблясък по конците (само по тях, не по плата). Декоративна — без семантика.
 */
@Composable
internal fun QuizBandCanvas(
    results: List<Boolean>,
    total: Int,
    current: Int?,
    colors: QuizBandColors,
    modifier: Modifier = Modifier,
    animating: Int = -1,
    stitchProgress: () -> Float = { 1f },
    closing: Boolean = false,
    closeShown: Boolean = QuizBandCharts.closed(results, total),
    closeProgress: () -> Float = { 1f },
    sheenKey: Any? = null,
) {
    val n = total.coerceAtLeast(1)
    val (segments, closingChart) = remember(n) { QuizBandCharts.of(n) }
    Box(modifier.clearAndSetSemantics { }) {
        // Платът, подгъвите (тичащ бод като отделни черти — без пунктирен ефект), дупките и увисналите конци.
        Spacer(
            Modifier.matchParentSize().drawWithCache {
                val g = BandGeometry(size, n)
                val cell = g.cell
                val holes = List(n) { i -> holePoints(segments[i], g.left(i), g.oy + cell, cell) }
                val closingHole = holePoints(closingChart, g.left(n), g.oy + cell, cell)
                val threads = List(n) { i -> looseThread(g.left(i), g.oy, cell) }
                val threadStroke = Stroke(cell * CrossStitchSpec.STROKE * 0.8f, cap = StrokeCap.Round)
                val hemWidth = StitchSpec.Running.widthDp * density
                val hemX0 = g.ox + cell * 0.6f
                val hemX1 = g.ox + g.clothSize.width - cell * 0.6f
                val hems = runningStitchSegments(hemX0, hemX1, g.oy + cell * 0.95f, density) +
                    runningStitchSegments(hemX0, hemX1, g.oy + cell * (QuizBandCharts.ROWS - 0.95f), density)
                val dot = cell * 0.24f
                onDrawBehind {
                    drawRoundRect(colors.cloth, Offset(g.ox, g.clothTop), g.clothSize, CornerRadius(cell * 0.6f))
                    drawPoints(hems, PointMode.Lines, colors.hem, strokeWidth = hemWidth, cap = StrokeCap.Round)
                    for (i in 0 until n) {
                        when (results.getOrNull(i)) {
                            true -> Unit
                            false -> {
                                drawPoints(holes[i], PointMode.Points, colors.holes, strokeWidth = dot, cap = StrokeCap.Round)
                                drawPath(threads[i], colors.gold, alpha = 0.9f, style = threadStroke)
                            }
                            null -> drawPoints(holes[i], PointMode.Points, if (i == current) colors.current else colors.holes, strokeWidth = dot, cap = StrokeCap.Round)
                        }
                    }
                    if (!closing && !closeShown) drawPoints(closingHole, PointMode.Points, colors.holes, strokeWidth = dot, cap = StrokeCap.Round)
                }
            },
        )
        // Готовите мотиви — рисуват се веднъж в слоя; отблясъкът минава само по тях.
        Spacer(
            Modifier.matchParentSize()
                .then(if (sheenKey != null) Modifier.goldSheen(sheenKey) else Modifier)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    val g = BandGeometry(size, n)
                    onDrawBehind {
                        for (i in 0 until n) {
                            if (results.getOrNull(i) == true && i != animating) stitchesIn(segments[i], g.left(i), g.oy + g.cell, g.cell, 1f, colors)
                        }
                        if (closeShown) stitchesIn(closingChart, g.left(n), g.oy + g.cell, g.cell, 1f, colors)
                    }
                },
        )
        // Иглата — само докато бродира; в свой слой, за да не прерисува плата и готовите мотиви.
        if (animating in 0 until n || closing) {
            Spacer(
                Modifier.matchParentSize().graphicsLayer { }.drawWithCache {
                    val g = BandGeometry(size, n)
                    onDrawBehind {
                        if (animating in 0 until n) stitchesIn(segments[animating], g.left(animating), g.oy + g.cell, g.cell, stitchProgress(), colors)
                        if (closing) stitchesIn(closingChart, g.left(n), g.oy + g.cell, g.cell, closeProgress(), colors)
                    }
                },
            )
        }
    }
}

/** Клетката и отстъпите на лентата от [n] мотива в [size] (центрирана). */
private class BandGeometry(size: Size, n: Int) {
    val cell = minOf(size.width / QuizBandCharts.columns(n), size.height / QuizBandCharts.ROWS)
    val ox = (size.width - cell * QuizBandCharts.columns(n)) / 2f
    val oy = (size.height - cell * QuizBandCharts.ROWS) / 2f
    val clothTop = oy + cell * 0.45f
    val clothSize = Size(cell * QuizBandCharts.columns(n), cell * (QuizBandCharts.ROWS - 0.9f))

    /** Левият край на мотив [i] ([n] — затварящият бод). */
    fun left(i: Int): Float = ox + i * QuizBandCharts.PERIOD * cell
}

/**
 * Тичащият бод ([StitchSpec.Running]) по хоризонталата [x0]..[x1] на височина [y] — двойки точки
 * за `drawPoints(PointMode.Lines)`: същите черти и стъпка като пунктира, но без пунктирен ефект
 * (той се смята наново при всяко рисуване). [pxPerDp] — обикновено `density`.
 */
internal fun runningStitchSegments(x0: Float, x1: Float, y: Float, pxPerDp: Float): List<Offset> {
    val spec = StitchSpec.Running
    val w = spec.widthDp * pxPerDp
    // Видимата черта е dashDp заедно с кръглите краища — както в DashStitch.stroke.
    val on = (spec.dashDp * pxPerDp - w).coerceAtLeast(0.01f * pxPerDp)
    val period = (spec.dashDp + spec.gapDp) * pxPerDp
    if (x1 <= x0 || period <= 0f) return emptyList()
    val out = ArrayList<Offset>(2 * ((x1 - x0) / period).toInt() + 2)
    var x = x0
    while (x < x1) {
        out += Offset(x, y)
        out += Offset(minOf(x + on, x1), y)
        x += period
    }
    return out
}

/** Мотивът [chart] в клетките от ([left], [top]) — общото бродиране ([drawStitches]). */
private fun DrawScope.stitchesIn(chart: StitchChart, left: Float, top: Float, cell: Float, progress: Float, colors: QuizBandColors) {
    inset(left, top, size.width - left - chart.columns * cell, size.height - top - chart.rows * cell) {
        drawStitches(chart, progress, colors.gold, colors.burgundy)
    }
}

/** „Дупките“ на схемата — средите на клетките на мотива, които още не са бродирани. */
private fun holePoints(chart: StitchChart, left: Float, top: Float, cell: Float): List<Offset> =
    List(chart.size) { Offset(left + (chart.col[it] + 0.5f) * cell, top + (chart.row[it] + 0.5f) * cell) }

/** Увисналият конец на празния мотив: излиза от бода-връзка и пада свободно с разнищен край. */
private fun looseThread(left: Float, oy: Float, cell: Float): Path {
    val y = oy + cell * (1 + QuizBandCharts.CHART_ROWS / 2f)
    val endX = left + cell * 6.2f
    val endY = y + cell * 2.4f
    return Path().apply {
        moveTo(left + cell * 0.5f, y)
        cubicTo(left + cell * 2.4f, y + cell * 3.6f, left + cell * 4.4f, y + cell * 0.2f, endX, endY)
        // Разнищеният край — две къси нишки.
        moveTo(endX, endY); lineTo(endX + cell * 0.7f, endY + cell * 0.25f)
        moveTo(endX, endY); lineTo(endX + cell * 0.45f, endY + cell * 0.65f)
    }
}
