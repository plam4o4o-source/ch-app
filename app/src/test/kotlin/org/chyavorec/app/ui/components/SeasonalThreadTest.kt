package org.chyavorec.app.ui.components

import org.chyavorec.app.ui.screens.my.cardBandYear
import org.chyavorec.app.ui.theme.Brand
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Сезонният конец по дата (мартеница, Коледа, Цветница/Великден) и месеците на лентата върху картата. */
class SeasonalThreadTest {
    private fun d(s: String) = LocalDate.parse(s)

    @Test fun martenitsaFromTheFirstToTheNinthOfMarch() {
        for (day in 1..9) assertEquals(SeasonalThread.MARTENITSA, SeasonalThread.forDate(LocalDate.of(2027, 3, day)), "$day март")
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2027-02-28")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2027-03-10")))
    }

    @Test fun christmasStarFromTheTwentiethOfDecemberToIvanovden() {
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-12-19")))
        assertEquals(SeasonalThread.CHRISTMAS_STAR, SeasonalThread.forDate(d("2026-12-20")))
        assertEquals(SeasonalThread.CHRISTMAS_STAR, SeasonalThread.forDate(d("2026-12-25")))
        assertEquals(SeasonalThread.CHRISTMAS_STAR, SeasonalThread.forDate(d("2027-01-07")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2027-01-08")))
    }

    @Test fun greenFromTsvetnitsaThroughEasterWeekAndOnGergyovden() {
        // Православен Великден 2026 — 12 април; Цветница — 5 април.
        assertEquals(SeasonalThread.SPRING_GREEN, SeasonalThread.forDate(d("2026-04-05")))
        assertEquals(SeasonalThread.SPRING_GREEN, SeasonalThread.forDate(d("2026-04-12")))
        assertEquals(SeasonalThread.SPRING_GREEN, SeasonalThread.forDate(d("2026-04-19")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-04-20")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-04-04")))
        // 2027: Великден — 2 май, Цветница — 25 април.
        assertEquals(SeasonalThread.SPRING_GREEN, SeasonalThread.forDate(d("2027-04-25")))
        // Гергьовден.
        assertEquals(SeasonalThread.SPRING_GREEN, SeasonalThread.forDate(d("2026-05-06")))
    }

    @Test fun ordinaryDaysAndBuditeliKeepTheGold() {
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-11-14")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-11-01")))
        assertEquals(SeasonalThread.GOLD, SeasonalThread.forDate(d("2026-07-15")))
    }

    @Test fun coloursPerTheme() {
        val gold = Brand.Gold
        assertEquals(ThreadColors(gold), threadColors(SeasonalThread.GOLD, gold, dark = false))
        val light = threadColors(SeasonalThread.MARTENITSA, gold, dark = false)
        assertNotNull(light.second)
        assertNotNull(light.secondEdge, "бялото на светъл фон е с кант")
        assertNull(threadColors(SeasonalThread.MARTENITSA, gold, dark = true).secondEdge)
        assertTrue(threadColors(SeasonalThread.CHRISTMAS_STAR, gold, dark = false).star)
        assertFalse(threadColors(SeasonalThread.SPRING_GREEN, gold, dark = true).star)
    }

    @Test fun personalBandIsOnTheCardInDecemberAndJanuary() {
        assertEquals(2026, cardBandYear(d("2026-12-01")))
        assertEquals(2026, cardBandYear(d("2027-01-31")))
        assertNull(cardBandYear(d("2027-02-01")))
        assertNull(cardBandYear(d("2026-11-30")))
    }
}
