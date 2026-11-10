package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeDown
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.share.ShareFixtures
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.data.catalog.ReadingYear
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** „Моята читателска година“: докосвания, автоматично превъртане, пауза с TalkBack, затваряне с плъзгане. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class YearStoryTest {
    @get:Rule val compose = createComposeRule()
    private val year = ShareFixtures.year
    private val total = yearSlides(year).size
    private var closed = false

    private fun show(reduced: Boolean = true, initial: Int = 0) {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                CompositionLocalProvider(LocalReducedMotion provides reduced) {
                    YearStory(year, onClose = { closed = true }, siteUrl = "https://chyavorec.org", initialSlide = initial)
                }
            }
        }
    }

    private fun onSlide(n: Int): Boolean =
        compose.onAllNodes(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Слайд $n от $total")).fetchSemanticsNodes().isNotEmpty()

    private fun tap(right: Boolean) = compose.onRoot().performTouchInput {
        click(Offset(if (right) width * 0.8f else width * 0.1f, height * 0.5f))
    }

    @Test fun demoYearHasEverySlide() {
        assertEquals(ReadingYear.Archetype.BOOKWORM, year.archetype)
        assertEquals(YearSlideKind.entries.toList(), yearSlides(year), "демо годината показва всички слайдове")
    }

    @Test fun slidesWithoutDataAreSkipped() {
        val one = ReadingYear.compute(listOf(ShareFixtures.history.first()), emptyMap(), ShareFixtures.today.year)!!
        assertEquals(listOf(YearSlideKind.INTRO, YearSlideKind.BOOKS, YearSlideKind.ARCHETYPE, YearSlideKind.OUTRO), yearSlides(one))
    }

    @Test fun tapsMoveBetweenSlidesAndTheLastOneCloses() {
        show()
        assertTrue(onSlide(1))
        tap(right = true)
        compose.waitForIdle()
        assertTrue(onSlide(2), "докосване вдясно — напред")
        tap(right = false)
        compose.waitForIdle()
        assertTrue(onSlide(1), "докосване вляво — назад")
        repeat(total) { tap(right = true); compose.waitForIdle() }
        assertTrue(closed, "след последния слайд историята се затваря")
    }

    @Test fun eachSlideIsOneNodeWithASummary() {
        show(initial = 1)
        // Целият слайд е един възел с описание — числото и месеците заедно.
        assertTrue(compose.onAllNodes(hasContentDescription("През ${year.year} г. взе ${year.books} книги", substring = true)).fetchSemanticsNodes().isNotEmpty())
    }

    @Test fun advancesByItselfAfterAboutFiveSeconds() {
        compose.mainClock.autoAdvance = false
        show(reduced = false)
        compose.mainClock.advanceTimeBy(3_000)
        assertTrue(onSlide(1))
        compose.mainClock.advanceTimeBy(3_500)
        assertTrue(onSlide(2), "след ~5.5 s — следващият слайд")
    }

    @Test fun reducedMotionDoesNotAdvance() {
        compose.mainClock.autoAdvance = false
        show(reduced = true)
        compose.mainClock.advanceTimeBy(12_000)
        assertTrue(onSlide(1))
    }

    @Test fun talkBackPausesAndOffersButtons() {
        val am = ApplicationProvider.getApplicationContext<Context>().getSystemService(AccessibilityManager::class.java)
        shadowOf(am).setTouchExplorationEnabled(true)
        compose.mainClock.autoAdvance = false
        show(reduced = false)
        compose.mainClock.advanceTimeBy(12_000)
        assertTrue(onSlide(1), "с TalkBack не превърта само")
        assertTrue(compose.onAllNodes(hasContentDescription("Следващ слайд")).fetchSemanticsNodes().isNotEmpty())
    }

    @Test fun swipeDownCloses() {
        show()
        compose.onRoot().performTouchInput { swipeDown(startY = height * 0.3f, endY = height * 0.95f) }
        compose.waitForIdle()
        assertTrue(closed)
    }

    /** Плъзгане надолу с [dp] за [ms] милисекунди от 40% височина. */
    private fun drag(dp: Float, ms: Long) = compose.onRoot().performTouchInput {
        val start = Offset(width / 2f, height * 0.4f)
        swipe(start, start + Offset(0f, dp * density), ms)
    }

    /** Плъзва и казва дали историята се затвори. */
    private fun swipeCloses(dp: Float, ms: Long): Boolean {
        show()
        drag(dp, ms)
        compose.waitForIdle()
        return closed
    }

    // Като в изпитанието на емулатора: 22% от екрана (~200 dp) за 90, 150, 300 и 1200 ms — винаги затваря.
    @Test fun aFifthOfTheScreenIn90msCloses() = assertTrue(swipeCloses(196f, 90L))

    @Test fun aFifthOfTheScreenIn150msCloses() = assertTrue(swipeCloses(196f, 150L))

    @Test fun aFifthOfTheScreenIn300msCloses() = assertTrue(swipeCloses(196f, 300L))

    @Test fun aFifthOfTheScreenSlowlyCloses() = assertTrue(swipeCloses(196f, 1_200L))

    @Test fun aShortQuickFlickCloses() = assertTrue(swipeCloses(100f, 80L), "кратко, но бързо (≈1250 dp/s) — затваря")

    @Test fun aShortSlowDragDoesNotClose() {
        assertTrue(!swipeCloses(100f, 1_000L), "кратко и бавно — историята се връща")
        assertTrue(onSlide(1))
    }

    /** Годините от „Моето“ — само за същата история; при смяна на читателя изчезват. */
    @Test fun theMemoServesOnlyTheSameHistory() {
        val memo = ReadingYearMemo()
        memo.put(3, listOf(year))
        assertEquals(year, memo.get(3, year.year))
        assertEquals(null, memo.get(4, year.year), "историята е сменена")
        assertEquals(null, memo.get(3, year.year - 1))
        memo.clear()
        assertEquals(null, memo.get(3, year.year))
    }

    @Test fun theRuleUsesTheWholeDistanceOrTheSpeed() {
        val d = StoryDismiss(androidx.compose.ui.unit.Density(2.625f))
        val dp = 2.625f
        assertTrue(d.shouldClose(141 * dp, 0f), "от 140 dp — и съвсем бавно")
        assertTrue(!d.shouldClose(120 * dp, 300 * dp), "под 140 dp и бавно — не")
        assertTrue(d.shouldClose(60 * dp, 900 * dp), "бързо пускане след 32 dp")
        assertTrue(!d.shouldClose(20 * dp, 3_000 * dp), "само трепване — не")
        assertTrue(!d.shouldClose(60 * dp, -900 * dp), "пуснато нагоре — не")
    }

    /** Само показаният слайд е „учтив“ регион и само той се чете — при преливането не два пъти. */
    @Test fun onlyTheCurrentSlideIsALiveRegion() {
        compose.mainClock.autoAdvance = false
        show(reduced = false)
        compose.mainClock.advanceTimeBy(500)
        tap(right = true)
        // По средата на преливането: и двата слайда са на екрана.
        compose.mainClock.advanceTimeBy(100)
        val live = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.LiveRegion)).fetchSemanticsNodes()
        assertEquals(1, live.size, "един „учтив“ регион")
        assertEquals("Слайд 2 от $total", live.single().config[SemanticsProperties.StateDescription])
        assertTrue(!onSlide(1), "излизащият слайд не се чете")
    }
}
