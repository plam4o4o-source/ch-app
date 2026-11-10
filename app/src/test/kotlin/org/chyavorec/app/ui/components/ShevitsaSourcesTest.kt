package org.chyavorec.app.ui.components

import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.ShevitsaPattern
import org.junit.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Мотивите на шевицата са един речник ([ShevitsaMotif]), „преведен“ от няколко
 * рисувачи: векторните пътища (Decor.kt), схемите за бродиране ([StitchCharts]) и
 * тъканата шевица ([ShevitsaPattern] = AGSL, виж ShevitsaParityTest). Мащабът и
 * цветовете им се различават нарочно; тук се пази общото — формата на мотивите.
 */
class ShevitsaSourcesTest {
    private val weavePeriod = 18f

    private fun weave(x: Int, y: Int) = ShevitsaPattern.kind(x.toFloat(), y.toFloat(), weavePeriod, ribbon = false)

    private fun chartCells(chart: StitchChart): Map<Pair<Int, Int>, Byte> {
        val cx = chart.columns / 2
        val cy = chart.rows / 2
        return (0 until chart.size).associate { (chart.col[it] - cx to chart.row[it] - cy) to chart.thread[it] }
    }

    /** Клетките на L1 разстояние [d] от средата. */
    private fun ring(d: Int): List<Pair<Int, Int>> =
        if (d == 0) listOf(0 to 0) else (-d..d).flatMap { x -> val y = d - abs(x); if (y == 0) listOf(x to 0) else listOf(x to y, x to -y) }

    /** „Ромб в ромба с точка“: и в тъканата шевица, и в схемата — концентрични ромбове с точка в средата. */
    @Test fun rhombusIsConcentricRingsInEverySource() {
        // Тъканата: ромбът е в средата на клетката на мрежата, напр. (0, 9) при период 18.
        val woven = (0..7).map { d -> ring(d).map { (x, y) -> weave(x, 9 + y) }.toSet() }
        woven.forEachIndexed { d, kinds -> assertEquals(1, kinds.size, "тъкан ромб, пръстен $d: $kinds") }
        assertTrue(woven[0].single() != ShevitsaPattern.EMPTY, "точка в средата")
        assertTrue(woven.count { it.single() != ShevitsaPattern.EMPTY } >= 3, "поне два ромба и точка")

        val r = 7
        val cells = chartCells(StitchCharts.rhombus(r))
        val stitched = (0..r).map { d -> ring(d).map { cells[it] }.toSet() }
        stitched.forEachIndexed { d, threads -> assertEquals(1, threads.size, "схема, пръстен $d: $threads") }
        assertTrue(stitched[0].single() != null, "точка в средата")
        assertTrue(stitched.count { it.single() != null } >= 3, "поне два ромба и точка")
        // Схемата е само пръстени (нищо извън ромба).
        assertTrue(cells.keys.all { (x, y) -> abs(x) + abs(y) <= r })
    }

    private fun d4(x: Int, y: Int) = listOf(y to x, -x to y, x to -y, -y to -x, -x to -y)

    /** Осморъката е осмолъчна (симетрична по осите и диагоналите) навсякъде. */
    @Test fun rosetteIsEightFoldInEverySource() {
        // Тъканата: в средата на другите клетки на мрежата, напр. (9, 0).
        for (x in -8..8) for (y in -8..8) {
            if (abs(x) + abs(y) >= 9) continue
            val k = weave(9 + x, y)
            for ((a, b) in d4(x, y)) assertEquals(k, weave(9 + a, b), "тъкана осморъка ($x, $y)")
        }
        assertTrue(weave(9, 0) != ShevitsaPattern.EMPTY)

        val cells = chartCells(StitchCharts.rosette(6))
        for ((p, t) in cells) for (q in d4(p.first, p.second)) assertEquals(t, cells[q], "схема на осморъката $p → $q")
        assertTrue((0 to 0) in cells)
    }

    /** Всеки мотив от речника има схема за бродиране (ромбът — своя, другите — осморъката). */
    @Test fun everyMotifHasAChart() {
        for (m in ShevitsaMotif.entries) assertTrue(StitchCharts.forMotif(m).size > 0, "$m")
    }
}
