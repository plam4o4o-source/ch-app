package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
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

/** Панелът на печата: намалено движение — статичен ред ~3 s; обикновено — цялата последователност и край. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class ReturnStampPanelTest {
    @get:Rule val compose = createComposeRule()

    private val book = HistoryItem("L1", 1, "Под игото", "Иван Вазов", "2026-09-20", "2026-11-11")
    private val other = HistoryItem("L2", 2, "Тютюн", "Димитър Димов", "2026-09-22", "2026-11-11")

    private fun show(reduced: Boolean, books: List<HistoryItem>, done: () -> Unit) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides reduced) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    ReturnStampPanel(StampShow(books, 2026, 4), onOpenHistory = {}, onDone = done)
                }
            }
        }
    }

    @Test fun reducedMotionShowsAStaticStampedRowForAboutThreeSeconds() {
        var done = 0
        show(reduced = true, books = listOf(book)) { done++ }
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
        show(reduced = true, books = listOf(book)) {}
        compose.mainClock.advanceTimeBy(100)
        compose.onNodeWithContentDescription("Върната: Под игото")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite))
        // Декоративният печат не се чете отделно.
        assertEquals(0, compose.onAllNodesWithTextCount("ВЪРНАТА"))
    }

    @Test fun fullSequenceFinishesAndQueuesSecondBook() {
        var done = 0
        show(reduced = false, books = listOf(book, other)) { done++ }
        compose.mainClock.advanceTimeBy(1500)
        compose.onNodeWithContentDescription("Върната: Под игото").assertIsDisplayed()
        assertEquals(0, done)
        compose.mainClock.advanceTimeBy(1800)
        // Втората книга от опашката.
        compose.onNodeWithContentDescription("Върната: Тютюн").assertIsDisplayed()
        compose.mainClock.advanceTimeBy(6000)
        assertEquals(1, done, "краят е веднъж")
    }

    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.onAllNodesWithTextCount(text: String): Int =
        onAllNodes(androidx.compose.ui.test.hasText(text)).fetchSemanticsNodes().size
}
