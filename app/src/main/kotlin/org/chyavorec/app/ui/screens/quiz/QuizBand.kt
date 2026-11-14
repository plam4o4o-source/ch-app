package org.chyavorec.app.ui.screens.quiz

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.clearAndSetSemantics
import org.chyavorec.app.ui.components.CrossStitchSpec
import org.chyavorec.app.ui.components.HapticSignature
import org.chyavorec.app.ui.components.LocalConfetti
import org.chyavorec.app.ui.components.StitchChart
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.drawStitches
import org.chyavorec.app.ui.components.rememberAppHaptics
import org.chyavorec.app.ui.components.rememberReducedMotion

/*
 * „Избродирай отговорите“: лента от по един мотив за въпрос (схемата на [StitchCharts.band],
 * разрязана на мотиви). Верният отговор избродира своя мотив веднъж, грешният оставя празно
 * място с увиснал конец, а при всички верни последният бод затваря лентата (конфети и вибрация).
 * Лентата се изчислява от отговорите — затова се пази заедно с викторината на деня.
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
private const val STITCH_MS = 900
private const val CLOSE_MS = 260

/**
 * Лентата на екрана: новият верен отговор се избродира веднъж (при връщане към викторината и при
 * намалено движение — направо готов); пълната лента — вибрация „печат“ и конфети от лентата.
 */
@Composable
internal fun QuizBand(results: List<Boolean>, total: Int, current: Int?, colors: QuizBandColors, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    // Видените досега отговори не се бродират наново (връщане към екрана, завъртане, нов процес).
    var seen by rememberSaveable { mutableIntStateOf(results.size) }
    var stitching by remember { mutableIntStateOf(-1) }
    val stitch = remember { Animatable(1f) }
    val close = remember { Animatable(if (QuizBandCharts.closed(results, total)) 1f else 0f) }
    val confetti = LocalConfetti.current
    val haptics = rememberAppHaptics()
    val bounds = remember { arrayOfNulls<Rect>(1) }
    LaunchedEffect(results.size, total) {
        if (results.size <= seen) {
            seen = results.size
            return@LaunchedEffect
        }
        val i = results.size - 1
        seen = results.size
        if (results.getOrNull(i) == true && !reduced) {
            stitching = i
            stitch.snapTo(0f)
            stitch.animateTo(1f, tween(STITCH_MS, easing = LinearOutSlowInEasing))
        }
        if (QuizBandCharts.closed(results, total)) {
            if (reduced) close.snapTo(1f) else close.animateTo(1f, tween(CLOSE_MS))
            haptics.play(HapticSignature.SEAL)
            if (!reduced) confetti?.burst(bounds[0]?.center ?: Offset.Unspecified)
        }
    }
    QuizBandCanvas(
        results, total, current, colors,
        modifier.onGloballyPositioned { bounds[0] = it.boundsInRoot() },
        stitchingIndex = { stitching }, stitchProgress = { stitch.value }, closeProgress = { close.value },
    )
}

/**
 * Неподвижната лента (и основата на [QuizBand]): прогресът се чете само при рисуване — без
 * рекомпозиция на кадър. Пътищата и точките се строят веднъж за размера. Декоративна — без семантика.
 */
@Composable
internal fun QuizBandCanvas(
    results: List<Boolean>,
    total: Int,
    current: Int?,
    colors: QuizBandColors,
    modifier: Modifier = Modifier,
    stitchingIndex: () -> Int = { -1 },
    stitchProgress: () -> Float = { 1f },
    closeProgress: () -> Float = { if (QuizBandCharts.closed(results, total)) 1f else 0f },
) {
    val n = total.coerceAtLeast(1)
    val (segments, closing) = remember(n) { QuizBandCharts.of(n) }
    Spacer(
        modifier.clearAndSetSemantics { }.drawWithCache {
            val cell = minOf(size.width / QuizBandCharts.columns(n), size.height / QuizBandCharts.ROWS)
            val ox = (size.width - cell * QuizBandCharts.columns(n)) / 2f
            val oy = (size.height - cell * QuizBandCharts.ROWS) / 2f
            val holes = List(n) { i -> holePoints(segments[i], ox + i * QuizBandCharts.PERIOD * cell, oy + cell, cell) }
            val closingHole = holePoints(closing, ox + n * QuizBandCharts.PERIOD * cell, oy + cell, cell)
            val threads = List(n) { i -> looseThread(ox + i * QuizBandCharts.PERIOD * cell, oy, cell) }
            val threadStroke = Stroke(cell * CrossStitchSpec.STROKE * 0.8f, cap = StrokeCap.Round)
            val hem = StitchSpec.Running.stroke(density)
            val clothTop = oy + cell * 0.45f
            val clothSize = Size(cell * QuizBandCharts.columns(n), cell * (QuizBandCharts.ROWS - 0.9f))
            onDrawBehind {
                drawRoundRect(colors.cloth, Offset(ox, clothTop), clothSize, CornerRadius(cell * 0.6f))
                // Подгъвите на плата — тичащ бод.
                for (y in floatArrayOf(oy + cell * 0.95f, oy + cell * (QuizBandCharts.ROWS - 0.95f))) {
                    drawLine(colors.hem, Offset(ox + cell * 0.6f, y), Offset(ox + clothSize.width - cell * 0.6f, y), strokeWidth = hem.width, cap = StrokeCap.Round, pathEffect = hem.pathEffect)
                }
                val anim = stitchingIndex()
                for (i in 0 until n) {
                    val left = ox + i * QuizBandCharts.PERIOD * cell
                    val answer = results.getOrNull(i)
                    when {
                        answer == true -> stitchesIn(segments[i], left, oy + cell, cell, if (i == anim) stitchProgress() else 1f, colors)
                        answer == false -> {
                            drawPoints(holes[i], PointMode.Points, colors.holes, strokeWidth = cell * 0.24f, cap = StrokeCap.Round)
                            drawPath(threads[i], colors.gold, alpha = 0.9f, style = threadStroke)
                        }
                        else -> drawPoints(holes[i], PointMode.Points, if (i == current) colors.current else colors.holes, strokeWidth = cell * 0.24f, cap = StrokeCap.Round)
                    }
                }
                val c = closeProgress()
                if (c > 0f) stitchesIn(closing, ox + n * QuizBandCharts.PERIOD * cell, oy + cell, cell, c, colors)
                else drawPoints(closingHole, PointMode.Points, colors.holes, strokeWidth = cell * 0.24f, cap = StrokeCap.Round)
            }
        },
    )
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
