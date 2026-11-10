package org.chyavorec.app.ui.screens.catalog

import android.app.Application
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.EngineCatalogSearcher
import org.chyavorec.data.catalog.SurprisePicker
import org.chyavorec.data.catalog.SurpriseTastes
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Duration
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Изненадай ме“: опит → предложение (само налични, без повторение), затваряне, спиране на процеса. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SurpriseViewModelTest {
    private val books = (1..8L).map { CatalogBook(it, "Автор $it", "Книга $it", available = it != 3L) }
    private val searcher = EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-10-01", books, emptyList())))

    @Before fun reset() { SurpriseSession.recent = emptyList() }

    private fun vm(tastes: SurpriseTastes = SurpriseTastes(), saved: SavedStateHandle = SavedStateHandle()) =
        SurpriseViewModel({ tastes }, Random(5), Dispatchers.Unconfined, saved)

    /** Както след спиране на процеса: само записаните стойности, в нов SavedStateHandle. */
    private fun restored(saved: SavedStateHandle) = SavedStateHandle(saved.keys().associateWith { saved.get<Any>(it) })

    private fun SurpriseViewModel.await(): SurpriseUi {
        val main = shadowOf(Looper.getMainLooper())
        val end = System.currentTimeMillis() + 10_000
        while (System.currentTimeMillis() < end) {
            main.idleFor(Duration.ofMillis(10))
            if (!ui.value.loading) return ui.value
            Thread.sleep(5)
        }
        error("няма предложение: ${ui.value}")
    }

    @Test fun rollsSevenDifferentAvailableBooks() {
        val vm = vm()
        val picked = (1..7).map { i ->
            vm.roll(searcher)
            val u = vm.await()
            assertTrue(u.open)
            assertEquals(i, u.round)
            assertNotNull(u.pick).also { assertTrue(it.available) }.inv
        }
        // Седемте налични — без повторение в сесията.
        assertEquals((1..8L).filter { it != 3L }.toSet(), picked.toSet())
        assertEquals(7, SurpriseSession.recent.size)
    }

    @Test fun closeKeepsTheRoundAndHides() {
        val vm = vm()
        vm.roll(searcher)
        vm.await()
        vm.close()
        assertFalse(vm.ui.value.open)
        assertNull(vm.ui.value.pick)
        assertEquals(1, vm.ui.value.round)
    }

    @Test fun withoutCatalogItIsEmpty() {
        val vm = vm()
        vm.roll(null)
        val u = vm.await()
        assertTrue(u.empty)
    }

    @Test fun tastesAreUsed() {
        val tastes = SurpriseTastes(authors = listOf("Автор 6"))
        // С фиксираното семе любимият автор излиза по-често от останалите.
        val counts = HashMap<Long, Int>()
        repeat(200) {
            SurpriseSession.recent = emptyList()
            val pick = SurprisePicker.pick(books, tastes, emptyList(), Random(it))!!
            counts[pick.inv] = (counts[pick.inv] ?: 0) + 1
        }
        assertTrue(counts.getValue(6L) > 2 * (counts[1L] ?: 0), "$counts")
    }

    /** Спиране на процеса при отворено предложение: същата книга, без ново теглене и разбъркване. */
    @Test fun suggestionSurvivesProcessDeath() {
        val saved = SavedStateHandle()
        val vm = vm(saved = saved)
        vm.roll(searcher)
        val first = vm.await()
        val pick = assertNotNull(first.pick)
        vm.markRevealed(first.round)
        val recent = SurpriseSession.recent
        val again = restored(saved)
        val vm2 = SurpriseViewModel({ error("без ново теглене") }, Random(99), Dispatchers.Unconfined, again)
        vm2.ui.value.let {
            assertTrue(it.open)
            assertTrue(it.revealed, "без разбъркване")
            assertTrue(it.loading)
        }
        // Каталогът още не е зареден — чака.
        vm2.restore(null)
        assertTrue(vm2.ui.value.loading)
        vm2.restore(searcher)
        val u = vm2.await()
        assertEquals(pick.inv, u.pick?.inv)
        assertEquals(first.round, u.round)
        assertTrue(u.revealed)
        assertEquals(recent, SurpriseSession.recent)
        // Затворено — при следващо спиране не се отваря наново.
        vm2.close()
        assertFalse(SurpriseViewModel({ SurpriseTastes() }, saved = restored(again)).ui.value.open)
    }

    /** Спряно по време на избора (още без предложение) — не се възстановява. */
    @Test fun processDeathWhileShufflingDoesNotReopen() {
        val saved = SavedStateHandle(mapOf("surprise.open" to true, "surprise.round" to 2))
        assertFalse(SurpriseViewModel({ SurpriseTastes() }, saved = saved).ui.value.open)
    }

    /** Книгата вече не е на рафта (или я няма) — изненадата се затваря. */
    @Test fun restoredBookNoLongerAvailableCloses() {
        val saved = SavedStateHandle(mapOf("surprise.open" to true, "surprise.round" to 1, "surprise.inv" to 3L))
        val vm = SurpriseViewModel({ SurpriseTastes() }, Random(1), Dispatchers.Unconfined, saved)
        vm.restore(searcher)
        val main = shadowOf(Looper.getMainLooper())
        val end = System.currentTimeMillis() + 10_000
        while (vm.ui.value.open && System.currentTimeMillis() < end) { main.idleFor(Duration.ofMillis(10)); Thread.sleep(5) }
        assertFalse(vm.ui.value.open)
    }

    /** Показаният резултат се помни (завъртане) — само за текущия опит. */
    @Test fun revealedIsPerRound() {
        val vm = vm()
        vm.roll(searcher)
        val u = vm.await()
        vm.markRevealed(u.round + 1)
        assertFalse(vm.ui.value.revealed)
        vm.markRevealed(u.round)
        assertTrue(vm.ui.value.revealed)
        vm.roll(searcher)
        assertFalse(vm.ui.value.revealed)
    }
}
