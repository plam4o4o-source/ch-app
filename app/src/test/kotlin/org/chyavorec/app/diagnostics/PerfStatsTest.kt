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

    @Test fun frequentWarmStartsDoNotPushOutColdOnes() {
        var d = PerfData()
        repeat(3) { i -> d = PerfStats.addStart(d, cold(1000L + i)) }
        repeat(30) { i -> d = PerfStats.addStart(d, warm(200L + i)) }
        assertEquals(listOf(1000L, 1001L, 1002L), d.starts.filter { it.cold }.map { it.firstFrameMs })
        assertEquals((210L..229L).toList(), d.starts.filter { !it.cold }.map { it.firstFrameMs })
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

    private fun at(v: Int, first: Long, drawn: Long? = null, cold: Boolean = true) = StartupSample(cold, first, drawn, versionCode = v)

    @Test fun versionsAreNotMixedAndFirstStartAfterUpdateIsFlagged() {
        var d = PerfData()
        repeat(3) { i -> d = PerfStats.addStart(d, at(35, 245L + i, 317)) }
        // Нова версия: първият студен старт е „след обновяване“ и не влиза в медианата.
        d = PerfStats.addStart(d, at(36, 400, 500))
        d = PerfStats.addStart(d, at(36, 200, 260))
        d = PerfStats.addStart(d, at(36, 210, 270))
        assertEquals(listOf(false, false, false, true, false, false), d.starts.map { it.afterUpdate })
        val s = PerfStats.summary(d, currentVersion = 36)
        assertEquals(36, s.versionCode)
        assertEquals(StartupSample(true, 400, 500, 36, afterUpdate = true), s.afterUpdate)
        assertEquals(StartStats(2, MedianLast(205, 210), MedianLast(265, 270)), s.cold)
        // Предишната версия — за сравнение, със своите числа.
        assertEquals(35, s.earlier!!.versionCode)
        assertEquals(246L, s.earlier!!.cold!!.firstFrame.median)
    }

    @Test fun legacyDataWithoutVersionIsMigratedAsUnknown() {
        val old = """{"starts":[{"cold":true,"firstFrameMs":900,"fullyDrawnMs":1200}],"routes":[{"route":"card/full","frames":100,"janky":5,"worstMs":159}]}"""
        val file = tmp.newFile("old.json").apply { writeText(old) }
        val d = PerfStore(file, Executor { it.run() }).read()
        assertEquals(0, d.starts.single().versionCode)
        assertEquals(0, d.routes.single().versionCode)
        // Нова версия не наследява 159 ms; старото остава като „неизвестна версия“ за сравнение.
        val after = PerfStats.addStart(d, at(36, 250, 300))
        assertTrue(after.starts.last().afterUpdate)
        val s = PerfStats.summary(after, 36)
        assertTrue(s.topRoutes.isEmpty())
        assertEquals(0, s.earlier!!.versionCode)
        assertEquals(159L, s.earlier!!.topRoutes.single().worstMs)
    }

    @Test fun worstFrameResetsPerVersion() {
        var d = PerfStats.addFrames(PerfData(), listOf(RouteFrames("card/full", 100, 5, 159, versionCode = 35)))
        d = PerfStats.addFrames(d, listOf(RouteFrames("card/full", 100, 1, 40, versionCode = 36, worstTotalMs = 70, sumTotalMs = 1200)))
        d = PerfStats.addFrames(d, listOf(RouteFrames("card/full", 100, 1, 30, versionCode = 36, worstTotalMs = 90, sumTotalMs = 1300)))
        val cur = PerfStats.summary(d, 36).topRoutes.single()
        assertEquals(RouteFrames("card/full", 200, 2, 40, 36, 90, 2500), cur)
        assertEquals(12L, cur.meanTotalMs)
        assertEquals(159L, PerfStats.summary(d, 36).earlier!!.topRoutes.single().worstMs)
    }

    @Test fun onlyTheNewestVersionsAreKept() {
        var d = PerfData()
        for (v in 30..35) d = PerfStats.addStart(d, at(v, 200L + v))
        assertEquals(listOf(33, 34, 35), d.starts.map { it.versionCode })
        for (v in 30..35) d = PerfStats.addFrames(d, listOf(RouteFrames("home", 40, 0, 10, versionCode = v)))
        assertEquals(listOf(34, 35), d.routes.map { it.versionCode })
    }

    @Test fun freshDataIsNotFlaggedAsAfterUpdate() {
        val d = PerfStats.addStart(PerfData(), at(36, 250, 300))
        assertTrue(d.starts.none { it.afterUpdate })
    }

    @Test fun summaryOfNewVersionWithoutDataIsEmptyButShowsEarlier() {
        val d = PerfStats.addStart(PerfData(), at(35, 250, 300))
        val s = PerfStats.summary(d, 36, "1.2.36")
        assertTrue(s.currentEmpty)
        assertTrue(!s.isEmpty)
        assertEquals("1.2.36", s.versionName)
        assertEquals(35, s.earlier!!.versionCode)
    }

    @Test fun frameCounterKeepsTotalsAndVersion() {
        val c = FrameCounter()
        c.add("home", 8_000_000, jank = false, totalNanos = 20_000_000)
        c.add("home", 9_000_000, jank = true, totalNanos = 40_000_000)
        assertEquals(listOf(RouteFrames("home", 2, 1, 9, 36, worstTotalMs = 40, sumTotalMs = 60)), c.drain(36))
    }

    @Test fun exportCarriesVersionProfileAndEarlier() {
        var d = PerfData()
        d = PerfStats.noteVersion(d, 35, "1.2.35")
        d = PerfStats.noteVersion(d, 36, "1.2.36")
        d = PerfStats.addStart(d, at(35, 245, 317))
        d = PerfStats.addStart(d, at(36, 300, 400))
        d = PerfStats.addStart(d, at(36, 210, 270))
        d = PerfStats.addFrames(d, listOf(RouteFrames("card/full", 100, 4, 30, 36, 80, 1500)))
        val text = PerfStats.export(PerfStats.summary(d, 36), ProfileState.of(compiled = false, enqueued = true, code = 2))
        assertTrue(text.startsWith("performance 1.2.36 (36)\n"), text)
        assertTrue("profile: waiting for compilation (code 2)" in text, text)
        assertTrue("startup cold first after update: first frame 300 ms; fully drawn 400 ms" in text, text)
        assertTrue("startup cold n=1: first frame median 210 ms" in text, text)
        assertTrue("total with render thread and GPU mean 15 ms, worst 80 ms" in text, text)
        assertTrue("earlier 1.2.35 (35): startup cold n=1: first frame median 245 ms" in text, text)
    }

    @Test fun profileStateMapping() {
        assertEquals(ProfileState.Kind.COMPILED, ProfileState.of(true, false, 1).kind)
        assertEquals(ProfileState.Kind.PENDING, ProfileState.of(false, true, 2).kind)
        assertEquals(ProfileState.Kind.NONE, ProfileState.of(false, false, 0).kind)
    }
}
