package org.chyavorec.app.ui.components

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** Резервът на преведените шейдъри: повторна употреба, без дублирано създаване, изчакване на текущото. */
class InstancePoolTest {
    private class Item(val id: Int)

    private val created = AtomicInteger()
    private val direct = Executor { it.run() }

    private fun pool(
        executor: Executor = direct,
        maxIdle: Int = 2,
        waitMs: Long = 2_000,
        create: () -> Item? = { Item(created.incrementAndGet()) },
    ) = InstancePool(create, executor, maxIdle, waitMs)

    @Test fun prepareFillsOnceAndTakeUsesIt() {
        val p = pool()
        p.prepare()
        p.prepare()
        assertEquals(1, created.get(), "второто prepare не създава, щом има готов")
        assertEquals(1, p.idleCount)
        val a = assertNotNull(p.take())
        assertEquals(1, a.id)
        // След взимането резервът се допълва във фона (тук — веднага).
        assertEquals(2, created.get())
        assertEquals(1, p.idleCount)
    }

    @Test fun releasedItemIsReusedWithoutCreatingAgain() {
        val p = pool(maxIdle = 4)
        val a = assertNotNull(p.take())
        // Празен резерв: a е създаден на място, още един е тръгнал във фона за следващия.
        assertEquals(2, created.get())
        p.release(a)
        // Връщането на картата / лентата на „Начало“: следващите отваряния не превеждат наново.
        val first = p.take()
        assertEquals(2, created.get(), "взето от резерва — без ново създаване на пътя")
        val second = p.take()
        assertNotSame(first, second)
        assertSame(a, second, "върнатият шейдър е използван отново")
    }

    @Test fun releaseBeyondMaxIdleIsDropped() {
        val p = pool(maxIdle = 1)
        val a = Item(100)
        val b = Item(101)
        p.release(a)
        p.release(b)
        p.release(a)
        assertEquals(1, p.idleCount)
        assertSame(a, p.take())
    }

    @Test fun releasingTheSameItemTwiceKeepsOneCopy() {
        val p = pool()
        val a = Item(100)
        p.release(a)
        p.release(a)
        assertEquals(1, p.idleCount)
    }

    @Test fun emptyPoolCreatesInPlaceAndRefillsInBackground() {
        val p = pool()
        val a = assertNotNull(p.take())
        assertEquals(1, a.id)
        assertEquals(2, created.get(), "един на място + един резерв за следващия")
        assertEquals(1, p.idleCount)
    }

    private fun named(name: String) = Executors.newSingleThreadExecutor { r -> Thread(r, name).apply { isDaemon = true } }

    @Test fun takeWaitsForTheCompileInFlightInsteadOfCompilingASecondCopy() {
        val started = CountDownLatch(1)
        val finish = CountDownLatch(1)
        val createdOn = java.util.Collections.synchronizedList(ArrayList<String>())
        val background = named("bg")
        val taker = named("taker")
        try {
            val p = pool(background) {
                createdOn += Thread.currentThread().name
                started.countDown()
                finish.await(5, TimeUnit.SECONDS)
                Item(created.incrementAndGet())
            }
            p.prepare()
            assertTrue(started.await(5, TimeUnit.SECONDS))
            assertTrue(p.filling)

            val result = taker.submit<Item?> { p.take() }
            Thread.sleep(150)
            assertTrue(!result.isDone, "take чака започнатия превод")
            finish.countDown()
            val got = assertNotNull(result.get(5, TimeUnit.SECONDS))
            assertEquals(1, got.id, "взет е именно превеждания във фона")
            assertTrue("taker" !in createdOn, "главната нишка не прави дубликат: $createdOn")
        } finally {
            finish.countDown()
            background.shutdownNow()
            taker.shutdownNow()
        }
    }

    @Test fun takeGivesUpWaitingAndCreatesInPlaceWhenTheBackgroundIsTooSlow() {
        val gate = CountDownLatch(1)
        val createdOn = java.util.Collections.synchronizedList(ArrayList<String>())
        val background = named("bg")
        val taker = named("taker")
        try {
            val p = pool(background, waitMs = 50) {
                val me = Thread.currentThread().name
                createdOn += me
                if (me == "bg") gate.await(5, TimeUnit.SECONDS)
                Item(created.incrementAndGet())
            }
            p.prepare()
            val got = taker.submit<Item?> { p.take() }.get(5, TimeUnit.SECONDS)
            assertNotNull(got)
            assertTrue("taker" in createdOn, "след waitMs се създава на място: $createdOn")
        } finally {
            gate.countDown()
            background.shutdownNow()
            taker.shutdownNow()
        }
    }

    @Test fun failedCreateGivesNullAndDoesNotJamThePool() {
        var fail = true
        val p = pool { if (fail) null else Item(created.incrementAndGet()) }
        p.prepare()
        assertEquals(0, p.idleCount)
        assertEquals(false, p.filling)
        assertEquals(null, p.take())
        fail = false
        assertNotNull(p.take())
    }
}
