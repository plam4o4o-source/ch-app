package org.chyavorec.app.ui.screens.embroidery

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.domain.embroidery.StitchColor
import org.chyavorec.domain.embroidery.StitchPattern

/**
 * Конците и платното на „Извези шевица“. Цветовете са еднакви при всяка тема на телефона
 * (като на картите за споделяне): платното е ленена кърпа, конците имат по-тъмен кант, за да
 * се виждат и кремавият, и златният върху нея.
 */
internal object StitchPalette {
    val fabric = Color(0xFFDFD0AC)
    val fabricGrid = Color(0xFF6A5A3A)

    private val burgundy = Color(0xFF86202F)
    private val gold = Brand.Gold
    private val black = Color(0xFF1E1812)
    private val cream = Color(0xFFFFFBF0)
    private val green = Color(0xFF3F6B45)

    private val threads = arrayOf(burgundy, gold, black, cream, green)
    private val edges = arrayOf(Color(0xFF4A0F19), Brand.GoldDark, Color(0xFF8C7A5E), Color(0xFF9A8860), Color(0xFF1E3A24))

    fun thread(color: StitchColor): Color = threads[color.ordinal]
    fun thread(code: Int): Color = threads[code - 1]
    fun edge(code: Int): Color = edges[code - 1]

    fun nameRes(color: StitchColor): Int = when (color) {
        StitchColor.BURGUNDY -> R.string.embroidery_colour_burgundy
        StitchColor.GOLD -> R.string.embroidery_colour_gold
        StitchColor.BLACK -> R.string.embroidery_colour_black
        StitchColor.CREAM -> R.string.embroidery_colour_cream
        StitchColor.GREEN -> R.string.embroidery_colour_green
    }
}

/** Петте пътя (по един на конец) — преизползват се между кадрите. */
internal class StitchPaths {
    val paths = Array(StitchColor.entries.size) { Path() }
}

/**
 * Рисува всички бодове с по два прохода на цвят: по-тъмен кант, после конецът. Бодът е „X“
 * в клетка; пътищата се строят наново при всяко рисуване (до 225 клетки — пренебрежимо).
 */
internal fun DrawScope.drawStitches(paths: StitchPaths, n: Int, origin: Offset, cell: Float, get: (Int, Int) -> Int) {
    for (p in paths.paths) p.rewind()
    val inset = cell * 0.17f
    for (y in 0 until n) for (x in 0 until n) {
        val code = get(x, y)
        if (code !in 1..paths.paths.size) continue
        val p = paths.paths[code - 1]
        val x0 = origin.x + x * cell + inset
        val y0 = origin.y + y * cell + inset
        val x1 = origin.x + (x + 1) * cell - inset
        val y1 = origin.y + (y + 1) * cell - inset
        p.moveTo(x0, y0); p.lineTo(x1, y1)
        p.moveTo(x1, y0); p.lineTo(x0, y1)
    }
    val thread = cell * 0.24f
    val edge = Stroke(thread * 1.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val body = Stroke(thread, cap = StrokeCap.Round, join = StrokeJoin.Round)
    for (i in paths.paths.indices) if (!paths.paths[i].isEmpty) drawPath(paths.paths[i], StitchPalette.edge(i + 1), style = edge)
    for (i in paths.paths.indices) if (!paths.paths[i].isEmpty) drawPath(paths.paths[i], StitchPalette.thread(i + 1), style = body)
}

/** Кърпата с мрежа: тънки линии между клетките, по-плътни на всеки пети ред/колона (за броене). */
internal fun DrawScope.drawFabric(n: Int, origin: Offset, cell: Float, corner: Float = 0f) {
    val side = cell * n
    drawRoundRect(StitchPalette.fabric, origin, Size(side, side), CornerRadius(corner))
    val thin = Path()
    val strong = Path()
    for (i in 1 until n) {
        val target = if (i % 5 == 0) strong else thin
        val o = i * cell
        target.moveTo(origin.x + o, origin.y); target.lineTo(origin.x + o, origin.y + side)
        target.moveTo(origin.x, origin.y + o); target.lineTo(origin.x + side, origin.y + o)
    }
    val w = (cell * 0.04f).coerceAtLeast(0.6f)
    drawPath(thin, StitchPalette.fabricGrid.copy(alpha = 0.16f), style = Stroke(w))
    drawPath(strong, StitchPalette.fabricGrid.copy(alpha = 0.34f), style = Stroke(w * 1.4f))
}

/**
 * Малка неподвижна картинка на шевицата (мотиви за начало, запазени шевици, картата за
 * споделяне). Декоративна — описанието е на мястото, където се ползва ([description]).
 */
@Composable
internal fun StitchPicture(pattern: StitchPattern, modifier: Modifier = Modifier, description: String? = null) {
    val paths = remember { StitchPaths() }
    Canvas(modifier.then(if (description != null) Modifier.semantics { contentDescription = description } else Modifier.clearAndSetSemantics { })) {
        val cell = size.minDimension / pattern.size
        val origin = Offset((size.width - cell * pattern.size) / 2f, (size.height - cell * pattern.size) / 2f)
        drawFabric(pattern.size, origin, cell, corner = cell * 0.25f)
        drawStitches(paths, pattern.size, origin, cell) { x, y -> pattern[x, y] }
    }
}
