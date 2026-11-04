package org.chyavorec.app.diagnostics

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.concurrent.Executor
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Бързодействие“: медианите, границата на стартовете и сливането на кадрите по маршрут. */
class PerfStatsTest {
    @get:Rule val tmp = TemporaryFolder()

    private fun cold(first: Long, drawn: Long? = null) = StartupSample(cold = true, firstFrameMs = first, fullyDrawnMs = drawn)
    private fun warm(first: Long, drawn: Long? = null) = StartupSample(cold = false, firstFrameMs = first, fullyDrawnMs = drawn)

    @Test fun medianOfOddEvenAndEmpty() {
        assertNull(PerfStats.median(emptyList()))
        assertEquals(7L, PerfStats.median(listOf(7)))
        assertEquals(300L, PerfStats.median(listOf(900, 100, 300)))
        // Четен брой — средното на двете средни.
        assertEquals(250L, PerfStats.median(listOf(400, 100, 200, 300)))
    }

    @Test fun keepsOnlyTheLastStarts() {
        var d = PerfData()
        repeat(25) { i -> d = PerfStats.addStart(d, cold(i.toLong())) }
        assertEquals(PerfStats.MAX_STARTS, d.starts.size)
        assertEquals((5L..24L).toList(), d.starts.map { it.firstFrameMs })
    }

    @Test fun summarySplitsColdAndWarm() {
        var d = PerfData()
        listOf(cold(1000, 1500), warm(300, 400), cold(1200), cold(900, 1300), warm(250, 330)).forEach { d = PerfStats.addStart(d, it) }
        val s = PerfStats.summary(d)
        assertEquals(StartStats(3, MedianLast(1000, 900), MedianLast(1400, 1300)), s.cold)
        assertEquals(StartStats(2, MedianLast(275, 250), MedianLast(365, 330)), s.warm)
    }

    @Test fun startHiddenBeforeFullyDrawnHasNoFullyDrawnStats() {
        val s = PerfStats.summary(PerfStats.addStart(PerfData(), warm(320)))
        assertNull(s.cold)
        assertEquals(MedianLast(320, 320), s.warm!!.firstFrame)
        assertNull(s.warm!!.fullyDrawn)
    }

    @Test fun framesAreMergedPerRoute() {
        var d = PerfStats.addFrames(PerfData(), listOf(RouteFrames("home", 100, 5, 40), RouteFrames("catalog", 50, 1, 20)))
        d = PerfStats.addFrames(d, listOf(RouteFrames("home", 100, 15, 90), RouteFrames("book", 40, 4, 33)))
        val home = d.routes.single { it.route == "home" }
        assertEquals(RouteFrames("home", 200, 20, 90), home)
        assertEquals(10.0, home.jankyPercent, 1e-9)
        assertEquals(setOf("home", "catalog", "book"), d.routes.map { it.route }.toSet())
    }

    @Test fun routesAreCapped() {
        val delta = (1..10).map { RouteFrames("r$it", frames = it * 10L) }
        val d = PerfStats.addFrames(PerfData(), delta, cap = 4)
        // Остават тези с най-много кадри.
        assertEquals(setOf("r10", "r9", "r8", "r7"), d.routes.map { it.route }.toSet())
    }

    @Test fun topRoutesByJankyShareWithMinimumFrames() {
        val d = PerfData(
            routes = listOf(
                RouteFrames("home", 1000, 30, 80), // 3 %
                RouteFrames("catalog", 500, 50, 120), // 10 %
                RouteFrames("viewer", 10, 9, 300), // твърде малко кадри
                RouteFrames("book", 200, 10, 60), // 5 %
                RouteFrames("news", 400, 0, 16),
                RouteFrames("search", 300, 3, 25), // 1 %
                RouteFrames("my", 100, 2, 20), // 2 %
            ),
        )
        val top = PerfStats.summary(d).topRoutes
        assertEquals(listOf("catalog", "book", "home", "my", "search"), top.map { it.route })
    }

    @Test fun routeTagHasNoArguments() {
        assertEquals("book", PerfStats.routeTag("book/{inv}?personal={personal}"))
        assertEquals("messages", PerfStats.routeTag("messages/{id}"))
        assertEquals("viewer", PerfStats.routeTag("viewer?album={album}&index={index}&url={url}"))
        assertEquals("card/full", PerfStats.routeTag("card/full"))
        assertEquals("home", PerfStats.routeTag("home"))
        assertEquals("start", PerfStats.routeTag(null))
    }

    @Test fun frameCounterAggregatesAndDrains() {
        val c = FrameCounter()
        c.add("home", 8_000_000, jank = false)
        c.add("home", 45_000_000, jank = true)
        c.add("book", 12_000_000, jank = false)
        assertEquals(setOf(RouteFrames("home", 2, 1, 45), RouteFrames("book", 1, 0, 12)), c.drain().toSet())
        assertTrue(c.drain().isEmpty())
    }

    @Test fun storeSurvivesReopenAndClears() {
        val file = File(tmp.root, "diagnostics/perf.json")
        val now = Executor { it.run() }
        val store = PerfStore(file, now)
        store.update { PerfStats.addStart(it, cold(1000, 1400)) }
        store.update { PerfStats.addFrames(it, listOf(RouteFrames("home", 60, 3, 50))) }
        val reopened = PerfStore(file, now).read()
        assertEquals(listOf(cold(1000, 1400)), reopened.starts)
        assertEquals(listOf(RouteFrames("home", 60, 3, 50)), reopened.routes)
        store.clear()
        assertTrue(store.read().isEmpty)
        assertTrue(PerfStore(file, now).read().isEmpty)
    }

    @Test fun brokenFileIsIgnored() {
        val file = tmp.newFile("perf.json").apply { writeText("{broken") }
        assertTrue(PerfStore(file, Executor { it.run() }).read().isEmpty)
    }

    @Test fun exportHasNoRouteArguments() {
        val text = PerfStats.export(PerfStats.summary(PerfData(listOf(cold(1000, 1500)), listOf(RouteFrames("book", 100, 7, 64)))))
        assertTrue("startup cold n=1: first frame median 1000 ms, last 1000 ms; fully drawn median 1500 ms, last 1500 ms" in text, text)
        assertTrue("frames book: 7/100 janky (7.0 %), worst 64 ms" in text, text)
    }
}
