package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * „Нов лист“: чипът „остават N дни“ се сменя заедно с обръщането на листа (в средата му), а не
 * преди него; при намалено движение — веднага на новия срок.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class RenewedLeafPillTest {
    @get:Rule val compose = createComposeRule()

    private val today = LocalDate.of(2026, 10, 17)

    // Старият срок — след 4 дни, новият (след удължаването) — след 18.
    private val loan = Loan("1", 101, "Под игото", "Иван Вазов", borrowedOn = "2026-09-23", dueOn = "2026-11-04", renewals = 1, canRenew = true)
    private val leaf = DueLeaf(oldDue = "2026-10-21", newDue = "2026-11-04")
    private val oldDays = "остават 4 дни"
    private val newDays = "остават 18 дни"

    private fun show(reduced: Boolean) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalReducedMotion provides reduced) {
                    LoanCard(loan, LoanDueCalculator(), today, stale = false, serverRenews = true, renewing = false, onRenew = {}, onOpen = null, leaf = leaf)
                }
            }
        }
        compose.mainClock.advanceTimeByFrame()
    }

    private fun shows(text: String) = compose.onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    @Test fun pillTurnsWithTheLeaf() {
        show(reduced = false)
        // Листът още не е обърнат — чипът е на стария срок.
        assertTrue(shows(oldDays))
        assertFalse(shows(newDays))
        // Листът тръгва, но още не е стигнал средата — чипът чака.
        compose.mainClock.advanceTimeBy(DueLeafTiming.DELAY_MS + DueLeafTiming.FLIP_MS / 8)
        assertTrue(shows(oldDays), "чипът се смени преди листа")
        // Отвъд средата (листът още се обръща) — новият срок.
        compose.mainClock.advanceTimeBy(DueLeafTiming.FLIP_MS / 2L)
        assertTrue(shows(newDays))
        assertFalse(shows(oldDays))
        // И в покой — само новият.
        compose.mainClock.advanceTimeBy(DueLeafTiming.FLIP_MS.toLong())
        assertTrue(shows(newDays))
        assertFalse(shows(oldDays))
    }

    @Test fun reducedMotionShowsTheNewTermAtOnce() {
        show(reduced = true)
        assertTrue(shows(newDays))
        assertFalse(shows(oldDays))
    }

    /** Повторно показване на вече обърнат лист — направо новият срок. */
    @Test fun playedLeafShowsTheNewTerm() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalReducedMotion provides false) {
                    LoanCard(loan, LoanDueCalculator(), today, stale = false, serverRenews = true, renewing = false, onRenew = {}, onOpen = null, leaf = leaf.copy(played = true))
                }
            }
        }
        compose.mainClock.advanceTimeByFrame()
        assertTrue(shows(newDays))
        assertFalse(shows(oldDays))
    }
}
