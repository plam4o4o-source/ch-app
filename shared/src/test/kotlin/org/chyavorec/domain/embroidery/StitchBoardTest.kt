package org.chyavorec.domain.embroidery

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StitchBoardTest {
    private val red = StitchColor.BURGUNDY.code
    private val gold = StitchColor.GOLD.code

    private fun StitchBoard.stitch(x: Int, y: Int, code: Int = red) {
        beginStroke(); paint(x, y, code); endStroke()
    }

    @Test fun paintPlacesAStitchAndErases() {
        val b = StitchBoard()
        b.stitch(3, 4)
        assertEquals(red, b[3, 4])
        assertEquals(1, b.stitchCount)
        b.stitch(3, 4, 0)
        assertEquals(0, b[3, 4])
        assertEquals(0, b.stitchCount)
    }

    @Test fun outOfBoundsAndBadCodesAreIgnored() {
        val b = StitchBoard()
        b.beginStroke()
        assertFalse(b.paint(-1, 0, red))
        assertFalse(b.paint(0, -1, red))
        assertFalse(b.paint(b.size, 0, red))
        assertFalse(b.paint(0, b.size, red))
        assertFalse(b.paint(1, 1, 6))
        assertFalse(b.paint(1, 1, -1))
        b.endStroke()
        assertEquals(0, b.stitchCount)
        assertFalse(b.canUndo)
        assertEquals(0, b[-5, 99])
    }

    @Test fun samePaintTwiceReportsNoChange() {
        val b = StitchBoard()
        b.beginStroke()
        assertTrue(b.paint(2, 2, red))
        assertFalse(b.paint(2, 2, red))
        b.endStroke()
    }

    @Test fun mirrorTwoWayReflectsLeftRight() {
        val b = StitchBoard(15).apply { symmetry = Symmetry.MIRROR }
        b.stitch(2, 5)
        assertEquals(red, b[2, 5])
        assertEquals(red, b[12, 5])
        assertEquals(2, b.stitchCount)
        // На оста — една клетка, не две.
        b.stitch(7, 1)
        assertEquals(3, b.stitchCount)
    }

    @Test fun mirrorFourWayReflectsBothAxes() {
        val b = StitchBoard(15).apply { symmetry = Symmetry.QUAD }
        b.stitch(1, 3)
        for ((x, y) in listOf(1 to 3, 13 to 3, 1 to 11, 13 to 11)) assertEquals(red, b[x, y], "$x,$y")
        assertEquals(4, b.stitchCount)
        // В центъра — една; на оста — две.
        b.stitch(7, 7)
        assertEquals(5, b.stitchCount)
        b.stitch(7, 2)
        assertEquals(7, b.stitchCount)
    }

    @Test fun evenSizedBoardMirrorsWithoutAnAxisCell() {
        val b = StitchBoard(10).apply { symmetry = Symmetry.QUAD }
        b.stitch(4, 4)
        assertEquals(4, b.stitchCount)
        assertEquals(red, b[5, 5])
    }

    @Test fun undoRedoWholeStrokeAtOnce() {
        val b = StitchBoard().apply { symmetry = Symmetry.MIRROR }
        b.beginStroke()
        b.paint(1, 1, red); b.paint(2, 1, red); b.paint(3, 1, red)
        b.endStroke()
        assertEquals(6, b.stitchCount)
        assertTrue(b.undo())
        assertEquals(0, b.stitchCount)
        assertFalse(b.canUndo)
        assertTrue(b.canRedo)
        assertTrue(b.redo())
        assertEquals(6, b.stitchCount)
        assertFalse(b.canRedo)
        assertFalse(b.redo())
    }

    @Test fun undoRestoresOverwrittenColours() {
        val b = StitchBoard()
        b.stitch(4, 4, red)
        b.stitch(4, 4, gold)
        assertEquals(gold, b[4, 4])
        b.undo()
        assertEquals(red, b[4, 4])
        b.undo()
        assertEquals(0, b[4, 4])
        assertFalse(b.undo())
    }

    @Test fun newEditClearsRedo() {
        val b = StitchBoard()
        b.stitch(1, 1)
        b.undo()
        assertTrue(b.canRedo)
        b.stitch(2, 2)
        assertFalse(b.canRedo)
    }

    @Test fun strokeThatChangesNothingIsNotAStep() {
        val b = StitchBoard()
        b.stitch(1, 1)
        b.beginStroke(); b.paint(1, 1, red); b.endStroke()
        // Една стъпка в историята, не две.
        assertTrue(b.undo())
        assertFalse(b.canUndo)
    }

    @Test fun strokeThatPaintsAndRepaintsBackToOriginalIsNotAStep() {
        val b = StitchBoard()
        b.beginStroke(); b.paint(1, 1, red); b.paint(1, 1, 0); b.endStroke()
        assertFalse(b.canUndo)
    }

    @Test fun undoWhileAStrokeIsOpenClosesItFirst() {
        val b = StitchBoard()
        b.beginStroke(); b.paint(1, 1, red)
        assertTrue(b.undo())
        assertEquals(0, b.stitchCount)
    }

    @Test fun historyIsBounded() {
        val b = StitchBoard()
        repeat(StitchBoard.MAX_UNDO + 30) { i -> b.stitch(i % b.size, (i / b.size) % b.size, 1 + i % 5) }
        var steps = 0
        while (b.undo()) steps++
        assertEquals(StitchBoard.MAX_UNDO, steps)
    }

    @Test fun clearIsOneUndoableStep() {
        val b = StitchBoard()
        b.stitch(1, 1); b.stitch(5, 5, gold)
        b.clear()
        assertEquals(0, b.stitchCount)
        b.undo()
        assertEquals(2, b.stitchCount)
        assertEquals(gold, b[5, 5])
        // Празно платно — clear не добавя стъпка.
        val empty = StitchBoard()
        empty.clear()
        assertFalse(empty.canUndo)
    }

    @Test fun loadReplacesAndUndoes() {
        val b = StitchBoard()
        b.stitch(0, 0)
        assertTrue(b.load(EmbroideryTemplates.treeOfLife()))
        assertEquals(EmbroideryTemplates.treeOfLife(), b.snapshot())
        b.undo()
        assertEquals(red, b[0, 0])
        assertEquals(1, b.stitchCount)
        assertFalse(b.load(StitchPattern.empty(9)), "друг размер")
    }

    @Test fun resetReplacesTheGridAndDropsTheHistory() {
        val b = StitchBoard()
        b.stitch(0, 0); b.stitch(1, 1)
        b.undo()
        assertTrue(b.reset(EmbroideryTemplates.zigzag()))
        assertEquals(EmbroideryTemplates.zigzag(), b.snapshot())
        assertFalse(b.canUndo)
        assertFalse(b.canRedo)
        assertFalse(b.reset(StitchPattern.empty(9)))
    }

    @Test fun snapshotIsIndependentOfTheBoard() {
        val b = StitchBoard()
        b.stitch(2, 2)
        val s = b.snapshot()
        b.stitch(3, 3)
        assertEquals(1, s.stitchCount)
        assertEquals(red, s[2, 2])
    }

    @Test fun encodeDecodeRoundTrip() {
        for (p in listOf(StitchPattern.empty(), EmbroideryTemplates.treeOfLife(), EmbroideryTemplates.zigzag(), StitchPattern.empty(9))) {
            assertEquals(p, StitchPattern.decode(p.encode()))
        }
        assertEquals(15 * 15 + 3, EmbroideryTemplates.treeOfLife().encode().length)
    }

    @Test fun decodeRejectsAnythingMalformed() {
        val ok = EmbroideryTemplates.treeOfLife().encode()
        assertNotNull(StitchPattern.decode(ok))
        for (bad in listOf(
            "", ":", "15", "15:", "x:000", "4:" + "0".repeat(16), "32:" + "0".repeat(32 * 32), "15:" + "0".repeat(224),
            "15:" + "0".repeat(226), ok.dropLast(1) + "6", ok.dropLast(1) + "x", ok.dropLast(1) + "-", "-15:" + "0".repeat(225),
            "015:" + "0".repeat(225), "15 " + "0".repeat(225),
        )) assertNull(StitchPattern.decode(bad), bad.take(20))
    }

    @Test fun templatesAreSquareSymmetricAndNotEmpty() {
        for (p in listOf(EmbroideryTemplates.treeOfLife(), EmbroideryTemplates.zigzag())) {
            assertEquals(15, p.size)
            assertTrue(p.stitchCount > 20)
            for (y in 0 until p.size) for (x in 0 until p.size) assertEquals(p[x, y], p[p.size - 1 - x, y], "ляво-дясно $x,$y")
        }
    }

    @Test fun zigzagUsesThreeThreads() {
        val p = EmbroideryTemplates.zigzag()
        val colours = (0 until 15).flatMap { y -> (0 until 15).map { x -> p[x, y] } }.filter { it != 0 }.toSet()
        assertEquals(setOf(StitchColor.GOLD.code, StitchColor.BURGUNDY.code, StitchColor.BLACK.code), colours)
    }

    @Test fun ofRowsRejectsNonSquareAndUnknownLetters() {
        assertTrue(runCatching { StitchPattern.ofRows(listOf("....", "....")) }.isFailure)
        assertTrue(runCatching { StitchPattern.ofRows(List(5) { "....Z" }) }.isFailure)
        assertEquals(0, StitchPattern.ofRows(List(5) { "....." }).stitchCount)
    }

    @Test fun lineCoversEveryCellWithoutGaps() {
        val l = StitchGeometry.line(0, 0, 5, 2)
        assertEquals(0 to 0, l.first())
        assertEquals(5 to 2, l.last())
        assertEquals(6, l.size)
        l.zipWithNext().forEach { (a, b) -> assertTrue(kotlin.math.abs(a.first - b.first) <= 1 && kotlin.math.abs(a.second - b.second) <= 1) }
        assertEquals(listOf(3 to 3), StitchGeometry.line(3, 3, 3, 3))
        assertEquals(listOf(4 to 1, 3 to 1, 2 to 1), StitchGeometry.line(4, 1, 2, 1))
    }

    @Test fun colourCodes() {
        assertEquals(StitchColor.GREEN, StitchColor.of(5))
        assertNull(StitchColor.of(0))
        assertNull(StitchColor.of(6))
        assertEquals(5, StitchColor.entries.size)
    }
}
