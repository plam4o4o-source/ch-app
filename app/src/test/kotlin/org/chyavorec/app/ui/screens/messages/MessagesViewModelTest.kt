package org.chyavorec.app.ui.screens.messages

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.messages.MessageInbox
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.domain.model.AppMessage
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Съобщенията стават „прочетени“ само когато екранът наистина се вижда. */
@OptIn(ExperimentalCoroutinesApi::class)
class MessagesViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @get:Rule val main = MainDispatcherRule(dispatcher)

    private class FakeInbox : MessageInbox {
        var list = listOf(msg("a"), msg("b"))
        override val readIds = MutableStateFlow<Set<String>>(emptySet())
        val marked = mutableListOf<String>()
        override suspend fun isMember() = true
        override suspend fun refresh(force: Boolean, includePersonal: Boolean): Outcome<Synced<List<AppMessage>>> =
            Outcome.Success(Synced(list, Instant.parse("2026-10-01T10:00:00Z")))
        override suspend fun markRead(ids: Collection<String>) {
            marked += ids
            readIds.value = readIds.value + ids
        }
    }

    @Test fun loadedWhileHiddenStaysUnreadUntilShown() = runTest(dispatcher) {
        val inbox = FakeInbox()
        val vm = MessagesViewModel(inbox)
        advanceUntilIdle()
        // Зареждането (и опресняването при върната връзка) без видим екран не отбелязва нищо.
        assertTrue(inbox.marked.isEmpty())
        inbox.list = listOf(msg("c"), msg("a"), msg("b"))
        vm.refreshOnReconnect()
        advanceUntilIdle()
        assertTrue(inbox.marked.isEmpty(), "фоновото опресняване не чете съобщенията")
        assertEquals(setOf("a", "b", "c"), vm.fresh.value)

        // Екранът излиза на преден план — показаните стават прочетени (и остават „Ново“ на екрана).
        vm.setVisible(true)
        advanceUntilIdle()
        assertEquals(setOf("a", "b", "c"), inbox.readIds.value)
        assertEquals(setOf("a", "b", "c"), vm.fresh.value)
    }

    @Test fun refreshWhileVisibleMarksTheNewOnes() = runTest(dispatcher) {
        val inbox = FakeInbox()
        val vm = MessagesViewModel(inbox)
        vm.setVisible(true)
        advanceUntilIdle()
        assertEquals(setOf("a", "b"), inbox.readIds.value)

        // Изрично опресняване от читателя, докато гледа екрана.
        inbox.list = listOf(msg("d"), msg("a"), msg("b"))
        vm.refresh()
        advanceUntilIdle()
        assertTrue("d" in inbox.readIds.value)
    }

    @Test fun hiddenScreenDoesNotMarkLaterArrivals() = runTest(dispatcher) {
        val inbox = FakeInbox()
        val vm = MessagesViewModel(inbox)
        vm.setVisible(true)
        advanceUntilIdle()
        // Друг екран отгоре / приложението във фона.
        vm.setVisible(false)
        inbox.list = listOf(msg("e"), msg("a"), msg("b"))
        vm.refreshOnReconnect()
        advanceUntilIdle()
        assertTrue("e" !in inbox.readIds.value)
        assertTrue("e" in vm.fresh.value)
    }

    private companion object {
        fun msg(id: String) = AppMessage(id = id, title = "Съобщение $id", body = "", createdAt = "2026-10-01T08:00:00Z")
    }
}
