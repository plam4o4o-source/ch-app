package org.chyavorec.app.ui.screens.catalog

import android.app.Application
import android.os.Looper
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

/** „Изненадай ме“: опит → предложение (само налични, без повторение), затваряне; разклащането. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SurpriseViewModelTest {
    private val books = (1..8L).map { CatalogBook(it, "Автор $it", "Книга $it", available = it != 3L) }
    private val searcher = EngineCatalogSearcher(CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-10-01", books, emptyList())))

    @Before fun reset() { SurpriseSession.recent = emptyList() }

    private fun vm(tastes: SurpriseTastes = SurpriseTastes()) =
        SurpriseViewModel({ tastes }, Random(5), Dispatchers.Unconfined)

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
}

/** Разклащането: два удара над прага в рамките на 0,7 s; после пауза; един удар или леко движение — нищо. */
class ShakeDetectorTest {
    private val g = 9.80665f

    @Test fun twoStrongPeaksAreAShake() {
        val d = ShakeDetector()
        assertFalse(d.onSample(0f, 0f, g, 0))
        assertFalse(d.onSample(2.6f * g, 0f, 0f, 100))
        // Същият удар (следващото отчитане) не се брои втори път.
        assertFalse(d.onSample(2.6f * g, 0f, 0f, 160))
        assertTrue(d.onSample(-2.8f * g, 0f, 0f, 400))
        // Пауза след разклащане.
        assertFalse(d.onSample(3f * g, 0f, 0f, 700))
        assertFalse(d.onSample(3f * g, 0f, 0f, 1_000))
        // След паузата — пак.
        assertFalse(d.onSample(3f * g, 0f, 0f, 2_100))
        assertTrue(d.onSample(3f * g, 0f, 0f, 2_400))
    }

    @Test fun walkingOrASingleBumpIsNot() {
        val d = ShakeDetector()
        // Ходене, телефон в ръка: до ~1.5 g.
        for (t in 0L until 3_000L step 50) assertFalse(d.onSample(0f, 1.5f * g, 0f, t))
        // Два удара, но твърде далеч един от друг.
        assertFalse(d.onSample(3f * g, 0f, 0f, 4_000))
        assertFalse(d.onSample(3f * g, 0f, 0f, 5_000))
    }
}
