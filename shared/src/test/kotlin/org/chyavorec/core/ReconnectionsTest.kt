package org.chyavorec.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** „Връзката се върна“: само преход офлайн → онлайн, стабилен поне 2 секунди, и само на преден план. */
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

    /**
     * Мрежата като системния NetworkCallback: при всеко ново наблюдение първо текущото
     * състояние; [subscribed] — дали в момента някой я наблюдава (т.е. callback-ът е записан).
     */
    private class Gated(scope: TestScope, visible: Boolean) {
        val network = MutableStateFlow(true)
        val foreground = MutableStateFlow(visible)
        var subscribed = 0
        var events = 0
        private val online = flow {
            subscribed++
            try { emitAll(network) } finally { subscribed-- }
        }

        init {
            scope.backgroundScope.launch { online.reconnections(2_000, foreground).collect { events++ } }
            scope.runCurrent()
        }
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

    @Test fun backgroundReconnectionWaitsForTheNextStartAndFiresOnce() = runTest {
        val g = Gated(this, visible = true)
        assertEquals(1, g.subscribed)
        g.network.value = false; runCurrent()
        // Скрито: мрежата не се наблюдава, връщането на връзката не опреснява нищо.
        g.foreground.value = false; runCurrent()
        assertEquals(0, g.subscribed, "във фонов режим callback-ът е отписан")
        g.network.value = true; advanceTimeBy(60_000); runCurrent()
        assertEquals(0, g.events)
        // Отново на преден план — едно опресняване (след стабилните 2 s).
        g.foreground.value = true; runCurrent()
        assertEquals(1, g.subscribed)
        advanceTimeBy(2_000); runCurrent()
        assertEquals(1, g.events)
        // Следващо скриване/показване без прекъсване — нищо ново.
        g.foreground.value = false; runCurrent()
        g.foreground.value = true; advanceTimeBy(10_000); runCurrent()
        assertEquals(1, g.events)
    }

    @Test fun hidingDuringTheDebounceDefersTheEvent() = runTest {
        val g = Gated(this, visible = true)
        g.network.value = false; runCurrent()
        g.network.value = true; advanceTimeBy(1_000); runCurrent()
        g.foreground.value = false; advanceTimeBy(10_000); runCurrent()
        assertEquals(0, g.events, "не и докато приложението е скрито")
        g.foreground.value = true; advanceTimeBy(2_000); runCurrent()
        assertEquals(1, g.events)
    }

    @Test fun startedInBackgroundObservesNothing() = runTest {
        val g = Gated(this, visible = false)
        assertEquals(0, g.subscribed)
        g.network.value = false; runCurrent()
        g.network.value = true; advanceTimeBy(10_000); runCurrent()
        assertEquals(0, g.events)
        // На преден план с налична връзка — началното „онлайн“ не е връщане.
        g.foreground.value = true; advanceTimeBy(10_000); runCurrent()
        assertEquals(0, g.events)
    }

    @Test fun startingOfflineFiresWhenOnline() = runTest {
        val h = Harness(this)
        h.emit(false); advanceTimeBy(60_000); runCurrent()
        assertEquals(0, h.events)
        h.emit(true); advanceTimeBy(2_000); runCurrent()
        assertEquals(1, h.events)
    }
}
