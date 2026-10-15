package org.chyavorec.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** „Връзката се върна“: само преход офлайн → онлайн, стабилен поне 2 секунди. */
class ReconnectionsTest {

    private class Harness(scope: TestScope) {
        val online = MutableSharedFlow<Boolean>(extraBufferCapacity = 16)
        var events = 0

        init {
            scope.backgroundScope.launch { online.reconnections(2_000).collect { events++ } }
            scope.runCurrent()
        }

        fun emit(value: Boolean) { check(online.tryEmit(value)) }
    }

    @Test fun initialOnlineIsNotAReconnection() = runTest {
        val h = Harness(this)
        h.emit(true)
        advanceTimeBy(10_000); runCurrent()
        assertEquals(0, h.events)
    }

    @Test fun offlineThenOnlineFiresOnceAfterDebounce() = runTest {
        val h = Harness(this)
        h.emit(true); h.emit(false); runCurrent()
        h.emit(true); runCurrent()
        advanceTimeBy(1_999); runCurrent()
        assertEquals(0, h.events, "преди 2 s няма събитие")
        advanceTimeBy(1); runCurrent()
        assertEquals(1, h.events)
        // Повторено „онлайн“ (напр. смяна на мрежата без прекъсване) не е ново връщане.
        h.emit(true); advanceTimeBy(5_000); runCurrent()
        assertEquals(1, h.events)
    }

    @Test fun flappingConnectionWaitsForAStableOne() = runTest {
        val h = Harness(this)
        h.emit(false); runCurrent()
        h.emit(true); advanceTimeBy(500); runCurrent()
        h.emit(false); advanceTimeBy(500); runCurrent()
        h.emit(true); advanceTimeBy(1_500); runCurrent()
        assertEquals(0, h.events, "мигането отлага събитието")
        advanceTimeBy(500); runCurrent()
        assertEquals(1, h.events, "едно събитие за цялото прекъсване")
    }

    @Test fun everyReconnectionFiresAgain() = runTest {
        val h = Harness(this)
        repeat(3) {
            h.emit(false); runCurrent()
            h.emit(true); advanceTimeBy(2_000); runCurrent()
        }
        assertEquals(3, h.events)
    }

    @Test fun startingOfflineFiresWhenOnline() = runTest {
        val h = Harness(this)
        h.emit(false); advanceTimeBy(60_000); runCurrent()
        assertEquals(0, h.events)
        h.emit(true); advanceTimeBy(2_000); runCurrent()
        assertEquals(1, h.events)
    }
}
