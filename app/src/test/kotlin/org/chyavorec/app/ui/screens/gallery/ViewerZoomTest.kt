package org.chyavorec.app.ui.screens.gallery

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import org.junit.Test
import kotlin.test.assertEquals

/** Увеличението в прегледа: около пръстите, в границите на екрана, от 1× до 5×. */
class ViewerZoomTest {
    private val bounds = IntSize(1000, 2000)
    private val center = Offset(500f, 1000f)
    private val identity = ViewerTransform(1f, Offset.Zero)

    /** Къде на екрана е точката [p] от снимката (в координатите ѝ без увеличение). */
    private fun screenOf(p: Offset, t: ViewerTransform) = center + (p - center) * t.scale + t.offset

    /** Коя точка от снимката е под [c] на екрана. */
    private fun contentAt(c: Offset, t: ViewerTransform) = center + (c - center - t.offset) / t.scale

    private fun assertNear(expected: Offset, actual: Offset) {
        assertEquals(expected.x, actual.x, 0.01f, "x")
        assertEquals(expected.y, actual.y, 0.01f, "y")
    }

    @Test fun thePointBetweenTheFingersStaysUnderThem() {
        val centroid = Offset(800f, 1200f)
        val zoomed = zoomAround(identity, 2f, Offset.Zero, centroid, bounds)
        assertEquals(2f, zoomed.scale)
        assertNear(centroid, screenOf(contentAt(centroid, identity), zoomed))
        // И от вече увеличена и преместена снимка.
        val start = ViewerTransform(2f, Offset(-100f, 50f))
        val c = Offset(300f, 700f)
        val more = zoomAround(start, 1.5f, Offset.Zero, c, bounds)
        assertEquals(3f, more.scale, 0.0001f)
        assertNear(contentAt(c, start), contentAt(c, more))
    }

    @Test fun panMovesThePointUnderTheFingersWithThem() {
        val start = ViewerTransform(2f, Offset(-100f, 50f))
        val c = Offset(300f, 700f)
        val pan = Offset(40f, -25f)
        val next = zoomAround(start, 1.2f, pan, c, bounds)
        assertNear(c + pan, screenOf(contentAt(c, start), next))
    }

    @Test fun zoomIsLimitedAndTheImageStaysOnScreen() {
        assertEquals(identity, zoomAround(ViewerTransform(1.5f, Offset(100f, 0f)), 0.5f, Offset(30f, 30f), Offset(10f, 10f), bounds))
        assertEquals(5f, zoomAround(ViewerTransform(4f, Offset.Zero), 2f, Offset.Zero, center, bounds).scale)
        // При 2× снимката се мести най-много с половината от размера си на всяка страна.
        val far = zoomAround(identity, 2f, Offset(5000f, -5000f), center, bounds)
        assertNear(Offset(500f, -1000f), far.offset)
        assertNear(Offset(500f, -1000f), clampToBounds(Offset(9999f, -9999f), 2f, bounds))
    }

    @Test fun withoutACentroidZoomIsAroundTheCentre() {
        val t = zoomAround(ViewerTransform(2f, Offset(100f, 0f)), 1.5f, Offset.Zero, Offset.Unspecified, bounds)
        assertEquals(3f, t.scale, 0.0001f)
        assertNear(Offset(150f, 0f), t.offset)
    }

    @Test fun doubleTapZoomsAroundTheTap() {
        // Същото като досегашното (center - tap) * (2 - 1).
        val tap = Offset(200f, 600f)
        val t = zoomAround(identity, 2f, Offset.Zero, tap, bounds)
        assertEquals(2f, t.scale)
        assertNear(center - tap, t.offset)
        assertNear(tap, screenOf(tap, t))
    }
}
