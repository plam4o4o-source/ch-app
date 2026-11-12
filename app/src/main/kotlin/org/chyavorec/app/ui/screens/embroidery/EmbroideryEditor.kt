package org.chyavorec.app.ui.screens.embroidery

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.domain.embroidery.EmbroideryTemplates
import org.chyavorec.domain.embroidery.StitchBoard
import org.chyavorec.domain.embroidery.StitchColor
import org.chyavorec.domain.embroidery.StitchPattern
import org.chyavorec.domain.embroidery.Symmetry

/** Мотивите за начало — от схемите на приложението (ромб, осморъка) и две нови (дърво, кривулица). */
internal enum class EmbroideryTemplate(val nameRes: Int, val motif: ShevitsaMotif) {
    RHOMBUS(R.string.embroidery_tpl_rhombus, ShevitsaMotif.RHOMBUS),
    ROSETTE(R.string.embroidery_tpl_rosette, ShevitsaMotif.ROSETTE),
    TREE(R.string.embroidery_tpl_tree, ShevitsaMotif.TREE_OF_LIFE),
    ZIGZAG(R.string.embroidery_tpl_zigzag, ShevitsaMotif.ZIGZAG);

    fun pattern(): StitchPattern = when (this) {
        RHOMBUS -> fromChart(StitchCharts.rhombus(7))
        ROSETTE -> fromChart(StitchCharts.rosette(7))
        TREE -> EmbroideryTemplates.treeOfLife()
        ZIGZAG -> EmbroideryTemplates.zigzag()
    }

    private fun fromChart(chart: org.chyavorec.app.ui.components.StitchChart): StitchPattern {
        val board = StitchBoard(chart.columns)
        board.beginStroke()
        for (i in 0 until chart.size) {
            val color = if (chart.thread[i] == StitchCharts.GOLD) StitchColor.GOLD else StitchColor.BURGUNDY
            board.paint(chart.col[i], chart.row[i], color.code)
        }
        board.endStroke()
        return board.snapshot()
    }
}

/**
 * Платното с избора на потребителя: цвят, гума, огледало, курсор. [drawVersion] расте при
 * всеки бод (чете се само при рисуване), [historyVersion] — при край на движение, „Върни“,
 * „Повтори“ и зареждане (чете се от бутоните и броя на бодовете).
 */
@Stable
class EmbroideryEditor(val board: StitchBoard = StitchBoard()) {
    var drawVersion by mutableIntStateOf(0)
        private set
    var historyVersion by mutableIntStateOf(0)
        private set
    var colour by mutableIntStateOf(StitchColor.BURGUNDY.code)
    var erase by mutableStateOf(false)
    var symmetry by mutableStateOf(Symmetry.NONE)
        private set
    var cursorMode by mutableStateOf(false)
    var cursorX by mutableIntStateOf(board.size / 2)
        private set
    var cursorY by mutableIntStateOf(board.size / 2)
        private set

    /** Извиква се след всяка завършена промяна (за черновата). */
    var onSettled: () -> Unit = {}

    private var strokeErase = false

    val canUndo: Boolean get() = historyVersion >= 0 && board.canUndo
    val canRedo: Boolean get() = historyVersion >= 0 && board.canRedo
    val stitchCount: Int get() = if (historyVersion >= 0) board.stitchCount else 0

    fun chooseSymmetry(value: Symmetry) {
        symmetry = value
        board.symmetry = value
        drawVersion++
    }

    /** Започва движение в (x, y): върху бод със същия цвят (или с гумата) движението маха. */
    fun beginStroke(x: Int, y: Int): Boolean {
        board.beginStroke()
        strokeErase = erase || board[x, y] == colour
        return paintAt(x, y)
    }

    /** Слага (или маха) бод; `true`, ако нещо се е променило. */
    fun paintAt(x: Int, y: Int): Boolean {
        val changed = board.paint(x, y, if (strokeErase) 0 else colour)
        if (changed) drawVersion++
        return changed
    }

    fun endStroke() {
        board.endStroke()
        settled()
    }

    fun undo() {
        if (board.undo()) { drawVersion++; settled() }
    }

    fun redo() {
        if (board.redo()) { drawVersion++; settled() }
    }

    fun clear() {
        board.clear()
        drawVersion++
        settled()
    }

    /** Мотив или запазена шевица върху платното — една стъпка, която се връща с „Върни“. */
    fun load(pattern: StitchPattern) {
        if (board.load(pattern)) { drawVersion++; settled() }
    }

    /** Черновата от предишното отваряне — без стъпка в историята. */
    fun restore(pattern: StitchPattern) {
        if (board.reset(pattern)) { drawVersion++; historyVersion++ }
    }

    fun moveCursor(dx: Int, dy: Int) {
        cursorX = (cursorX + dx).coerceIn(0, board.size - 1)
        cursorY = (cursorY + dy).coerceIn(0, board.size - 1)
        drawVersion++
    }

    /** Бод (или махане) на мястото на курсора — като докосване. */
    fun placeAtCursor(): Boolean {
        val changed = beginStroke(cursorX, cursorY)
        endStroke()
        return changed
    }

    /** Какво има под курсора: кодът на конеца (0 — празно). */
    fun cursorCell(): Int = if (historyVersion >= 0) board[cursorX, cursorY] else 0

    private fun settled() {
        historyVersion++
        onSettled()
    }
}
