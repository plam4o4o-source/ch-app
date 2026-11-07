package org.chyavorec.app.ui.components

import org.chyavorec.app.ui.screens.news.ArticleReading
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.junit.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Бродираната шевица, конфетито и четенето на статия — чистата математика (без рисуване). */
class StitchingTest {

    @Test fun eachStitchTakesItsShareOfTheProgress() {
        val n = 10
        // Нищо в началото, всичко накрая; извън 0..1 — подрязано.
        assertEquals(0f, StitchMath.stitchFraction(0f, 0, n))
        assertEquals(1f, StitchMath.stitchFraction(1f, n - 1, n))
        assertEquals(0f, StitchMath.stitchFraction(-0.5f, 0, n))
        assertEquals(1f, StitchMath.stitchFraction(2f, n - 1, n))
        // При 35 %: три бода са готови, четвъртият е наполовина, петият не е започнат.
        assertEquals(1f, StitchMath.stitchFraction(0.35f, 2, n))
        assertEquals(0.5f, StitchMath.stitchFraction(0.35f, 3, n), 1e-4f)
        assertEquals(0f, StitchMath.stitchFraction(0.35f, 4, n))
        assertEquals(3, StitchMath.completed(0.35f, n))
        assertEquals(n, StitchMath.completed(1f, n))
        assertEquals(0f, StitchMath.stitchFraction(0.5f, 0, 0))
    }

    @Test fun firstLegIsStitchedBeforeTheSecond() {
        // „/“ през първата половина на бода, „\“ — през втората.
        assertEquals(0.5f, StitchMath.firstLeg(0.25f))
        assertEquals(0f, StitchMath.secondLeg(0.25f))
        assertEquals(1f, StitchMath.firstLeg(0.5f))
        assertEquals(0f, StitchMath.secondLeg(0.5f))
        assertEquals(1f, StitchMath.firstLeg(0.75f))
        assertEquals(0.5f, StitchMath.secondLeg(0.75f))
        assertEquals(1f, StitchMath.secondLeg(1f))
    }

    @Test fun anglesGoClockwiseFromTheTop() {
        assertEquals(0f, StitchMath.angleOf(0, -3)) // горе
        assertEquals(0.25f, StitchMath.angleOf(3, 0), 1e-4f) // дясно
        assertEquals(0.5f, StitchMath.angleOf(0, 3), 1e-4f) // долу
        assertEquals(0.75f, StitchMath.angleOf(-3, 0), 1e-4f) // ляво
        assertEquals(0f, StitchMath.angleOf(0, 0))
    }

    @Test fun runningThreadGlowsBehindItsHead() {
        assertEquals(1f, StitchMath.runGlow(0.5f, 0.5f))
        // Опашката — назад от главата, затихва; пред главата — тъмно.
        assertTrue(StitchMath.runGlow(0.4f, 0.5f) in 0.5f..0.7f)
        assertEquals(0f, StitchMath.runGlow(0.6f, 0.5f))
        assertEquals(0f, StitchMath.runGlow(0.1f, 0.5f))
        // През върха (фазата се е превъртяла).
        assertTrue(StitchMath.runGlow(0.95f, 0.05f) > 0.5f)
    }

    @Test fun rosetteIsAnEightfoldSymmetricChartStitchedFromTheCentre() {
        for (r in 4..7) {
            val c = StitchCharts.rosette(r)
            assertEquals(2 * r + 1, c.columns)
            assertEquals(2 * r + 1, c.rows)
            val cells = (0 until c.size).map { (c.col[it] - r) to (c.row[it] - r) }
            assertEquals(cells.size, cells.toSet().size, "бод два пъти в една клетка (r=$r)")
            val set = cells.toSet()
            // Завъртане на 90° и огледално — същата схема (осморъката е симетрична).
            for ((x, y) in cells) {
                assertTrue((-y to x) in set, "завъртане ($x,$y), r=$r")
                assertTrue((-x to y) in set, "огледало ($x,$y), r=$r")
            }
            // Иглата започва от средата и обикаля по часовника.
            assertEquals(0 to 0, cells.first())
            val angles = (1 until c.size).map { c.angle[it] }
            assertEquals(angles.sorted(), angles)
            // И двата конеца участват.
            assertTrue(c.thread.any { it == StitchCharts.GOLD } && c.thread.any { it == StitchCharts.BURGUNDY })
        }
    }

    @Test fun rhombusAndBandCharts() {
        val rh = StitchCharts.rhombus(7)
        assertEquals(15, rh.columns)
        for (i in 0 until rh.size) {
            val d = abs(rh.col[i] - 7) + abs(rh.row[i] - 7)
            assertTrue(d == 7 || d == 4 || d <= 1, "клетка извън ромбовете: $d")
        }
        assertEquals(StitchCharts.rhombus(6).size, StitchCharts.forMotif(ShevitsaMotif.RHOMBUS, 6).size)
        val band = StitchCharts.band(4)
        assertEquals(33, band.columns)
        assertEquals(7, band.rows)
        // Отляво надясно — колона по колона.
        val cols = (0 until band.size).map { band.col[it] }
        assertEquals(cols.sorted(), cols)
    }

    @Test fun pullToRefreshEmbroidersUpToTheThreshold() {
        assertEquals(0f, refreshProgress(0f))
        assertEquals(0.5f, refreshProgress(0.5f))
        // На прага (и след него) осморъката е цяла.
        assertEquals(1f, refreshProgress(1f))
        assertEquals(1f, refreshProgress(1.4f))
    }

    @Test fun confettiRisesThenFalls() {
        assertEquals(0f, ConfettiPhysics.dx(500f, 0f))
        assertEquals(0f, ConfettiPhysics.dy(-500f, 0f))
        // Изхвърлена нагоре: първо се издига, накрая е под точката на изскачане.
        assertTrue(ConfettiPhysics.dy(-600f, 0.2f) < -40f)
        assertTrue(ConfettiPhysics.dy(-600f, CONFETTI_MS / 1000f) > 0f)
        // Съпротивлението спира хоризонталното движение — не отлита неограничено.
        assertTrue(abs(ConfettiPhysics.dx(700f, 10f)) <= 700f / ConfettiPhysics.DRAG + 1f)
        assertEquals(1f, ConfettiPhysics.alpha(0.3f))
        assertEquals(0f, ConfettiPhysics.alpha(1f))
        assertTrue(ConfettiPhysics.alpha(0.85f) in 0.1f..0.9f)
    }

    @Test fun confettiIsDeterministicForASeed() {
        val a = ConfettiParticles(CONFETTI_PARTICLES).apply { reset(kotlin.random.Random(42)) }
        val b = ConfettiParticles(CONFETTI_PARTICLES).apply { reset(kotlin.random.Random(42)) }
        assertTrue(a.vx.contentEquals(b.vx) && a.vy.contentEquals(b.vy) && a.color.contentEquals(b.color))
        // Всички излитат нагоре (ветрило от -160° до -20°).
        assertTrue(a.vy.all { it < 0f })
        assertTrue(CONFETTI_PARTICLES in 30..40)
    }

    @Test fun articleReadingFractionAndToolbar() {
        assertEquals(0f, ArticleReading.fraction(0, 1000))
        assertEquals(0.25f, ArticleReading.fraction(250, 1000))
        assertEquals(1f, ArticleReading.fraction(1000, 1000))
        // Кратка статия (без превъртане) и още неизмерена — 0.
        assertEquals(0f, ArticleReading.fraction(0, 0))
        assertEquals(0f, ArticleReading.fraction(0, Int.MAX_VALUE))
        assertEquals(0.5f, ArticleReading.heroProgress(100f, 200f))
        assertEquals(1f, ArticleReading.heroProgress(900f, 200f))
        // Надолу (към края) — скрива се; нагоре — показва се; дребни движения не я местят.
        assertFalse(ArticleReading.toolbarVisible(true, -20f, 6f))
        assertTrue(ArticleReading.toolbarVisible(false, 20f, 6f))
        assertTrue(ArticleReading.toolbarVisible(true, -3f, 6f))
        assertFalse(ArticleReading.toolbarVisible(false, 3f, 6f))
    }
}
