package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.onAllNodesWithContentDescription
import org.chyavorec.app.ui.components.ConfettiState
import org.chyavorec.app.ui.components.LocalConfetti
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.HistoryItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Панелът на печата: намалено движение — статичен ред ~3 s; обикновено — цялата последователност и край. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class ReturnStampPanelTest {
    @get:Rule val compose = createComposeRule()

    private val book = HistoryItem("L1", 1, "Под игото", "Иван Вазов", "2026-09-20", "2026-11-11")
    private val other = HistoryItem("L2", 2, "Тютюн", "Димитър Димов", "2026-09-22", "2026-11-11")
    private val third = HistoryItem("L3", 3, "Нощем с белите коне", "Павел Вежинов", "2026-09-25", "2026-11-11")

    /** Опашката е „във ViewModel“: живее извън композицията и се пази при завъртане/излизане. */
    private var queue by mutableStateOf<StampShow?>(null)
    private var present by mutableStateOf(true)
    private var done = 0
    private val shownIds = mutableListOf<String>()
    private val confetti = ConfettiState(seed = 1L)

    @Composable private fun Host(reduced: Boolean) {
        val q = queue
        if (!present || q == null) return
        CompositionLocalProvider(LocalReducedMotion provides reduced, LocalConfetti provides confetti) {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                ReturnStampPanel(
                    q, onOpenHistory = {},
                    onBookShown = { id -> shownIds += id; queue = queue?.afterShown(id) },
                    onDone = { done++; queue = null },
                )
            }
        }
    }

    private fun show(reduced: Boolean, books: List<HistoryItem>) {
        queue = StampShow(books, 2026, 4)
        compose.mainClock.autoAdvance = false
        compose.setContent { Host(reduced) }
    }

    private fun leaveAndComeBack() {
        compose.runOnUiThread { present = false }
        compose.mainClock.advanceTimeBy(50)
        compose.runOnUiThread { present = true }
    }

    private fun assertNoStamp(title: String) =
        assertEquals(0, compose.onAllNodesWithContentDescription("Върната: $title").fetchSemanticsNodes().size, "$title не се играе пак")

    @Test fun reducedMotionShowsAStaticStampedRowForAboutThreeSeconds() {
        show(reduced = true, books = listOf(book))
        compose.mainClock.advanceTimeBy(100)
        // Веднага крайният кадър: книгата, печатът и броячът със завъртяната стойност (4 + 1).
        compose.onNodeWithContentDescription("Върната: Под игото").assertIsDisplayed()
        compose.onNodeWithContentDescription("5 върнати книги през 2026 г.").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(2700)
        assertEquals(0, done, "още е на екрана")
        compose.mainClock.advanceTimeBy(500)
        assertEquals(1, done)
    }

    @Test fun announcesTheReturnedBookOnceAsAPoliteLiveRegion() {
        show(reduced = true, books = listOf(book))
        compose.mainClock.advanceTimeBy(100)
        compose.onNodeWithContentDescription("Върната: Под игото")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
        // Декоративният печат не се чете отделно.
        assertEquals(0, compose.onAllNodesWithTextCount("ВЪРНАТА"))
    }

    @Test fun fullSequenceFinishesAndQueuesSecondBook() {
        show(reduced = false, books = listOf(book, other))
        compose.mainClock.advanceTimeBy(1500)
        compose.onNodeWithContentDescription("Върната: Под игото").assertIsDisplayed()
        assertEquals(0, done)
        compose.mainClock.advanceTimeBy(1800)
        // Втората книга от опашката.
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(6000)
        assertEquals(1, done, "краят е веднъж")
    }


    @Test fun queueOfThreePlaysEachOnceAndEndsOnce() {
        show(reduced = false, books = listOf(book, other, third))
        compose.mainClock.advanceTimeBy(1800)
        assertEquals(listOf("L1"), shownIds)
        compose.onNodeWithContentDescription("5 върнати книги през 2026 г.").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(2300)
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        assertEquals(listOf("L1", "L2"), shownIds)
        compose.mainClock.advanceTimeBy(2300)
        compose.onNodeWithContentDescription("Върната: Нощем с белите коне").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(3000)
        assertEquals(listOf("L1", "L2", "L3"), shownIds)
        assertEquals(1, done)
        assertEquals(null, queue)
    }

    @Test fun leavingMidStampRestartsOnlyThatBook() {
        show(reduced = false, books = listOf(book, other))
        compose.mainClock.advanceTimeBy(800)
        assertEquals(emptyList<String>(), shownIds)
        leaveAndComeBack()
        compose.mainClock.advanceTimeBy(100)
        // Недовършената книга почва отначало, броячът е на старото място.
        compose.onNodeWithContentDescription("Върната: Под игото").assertIsDisplayed()
        compose.onNodeWithContentDescription("4 върнати книги през 2026 г.").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(6000)
        assertEquals(listOf("L1", "L2"), shownIds)
        assertEquals(1, done)
    }

    @Test fun finishedBooksAreNotReplayedAfterLeavingAndComingBack() {
        show(reduced = false, books = listOf(book, other, third))
        compose.mainClock.advanceTimeBy(1800) // първата е готова, втората още не е почнала
        assertEquals(listOf("L1"), shownIds)
        val burstsBefore = confetti.bursts
        leaveAndComeBack()
        compose.mainClock.advanceTimeBy(100)
        assertNoStamp("Под игото")
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        // Броячът не се върти пак за първата: стои на 5 и тръгва към 6 едва с втората.
        compose.onNodeWithContentDescription("5 върнати книги през 2026 г.").assertIsDisplayed()
        assertTrue(confetti.bursts <= burstsBefore + 1, "най-много един повторен печат — на недовършената книга")
        compose.mainClock.advanceTimeBy(8000)
        assertEquals(listOf("L1", "L2", "L3"), shownIds)
        assertEquals(1, done)
    }

    @Test fun everythingAlreadyShownEndsAtOnceWithoutAStamp() {
        show(reduced = false, books = listOf(book))
        compose.mainClock.advanceTimeBy(1800)
        assertEquals(listOf("L1"), shownIds)
        val burstsBefore = confetti.bursts
        leaveAndComeBack()
        compose.mainClock.advanceTimeBy(100)
        assertNoStamp("Под игото")
        assertEquals(1, done)
        assertTrue(confetti.bursts <= burstsBefore, "печатът не се повтаря")
    }

    @Test fun rotationResumesWithTheQueueNotFromTheStart() {
        // Завъртането унищожава композицията (remember се губи), а опашката във ViewModel остава.
        show(reduced = false, books = listOf(book, other, third))
        compose.mainClock.advanceTimeBy(2400) // първата е готова, втората е в ход
        assertEquals(listOf("L1"), shownIds)
        leaveAndComeBack()
        compose.mainClock.advanceTimeBy(100)
        assertNoStamp("Под игото")
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        compose.onNodeWithContentDescription("5 върнати книги през 2026 г.").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(8000)
        assertEquals(listOf("L1", "L2", "L3"), shownIds, "всяка книга е изиграна докрай точно веднъж")
        assertEquals(1, done)
    }

    @Test fun reducedMotionRotationKeepsFinishedBooksOut() {
        show(reduced = true, books = listOf(book, other))
        compose.mainClock.advanceTimeBy(3100) // първият статичен ред мина, втората започва
        assertEquals(listOf("L1"), shownIds)
        leaveAndComeBack()
        compose.mainClock.advanceTimeBy(100)
        assertNoStamp("Под игото")
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(2500)
        assertEquals(listOf("L1", "L2"), shownIds)
        assertEquals(1, done)
    }

    @Test fun afterShownMovesTheQueueAndTheCounterTogether() {
        val s = StampShow(listOf(book, other), 2026, 4)
        val next = s.afterShown("L1")
        assertEquals(listOf("L2"), next.books.map { it.loanId })
        assertEquals(5, next.yearBefore)
        assertEquals(next, next.afterShown("missing"))
        assertEquals(emptyList<String>(), next.afterShown("L2").books.map { it.loanId })
    }

    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.onAllNodesWithTextCount(text: String): Int =
        onAllNodes(androidx.compose.ui.test.hasText(text)).fetchSemanticsNodes().size
}
