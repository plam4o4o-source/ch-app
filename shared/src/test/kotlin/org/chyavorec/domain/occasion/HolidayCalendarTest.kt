package org.chyavorec.domain.occasion

import org.chyavorec.data.site.SiteOccasionEntry
import org.chyavorec.data.site.SiteOccasionsParser
import org.chyavorec.domain.occasion.OccasionEffect as E
import org.chyavorec.domain.occasion.OccasionOrnament as O
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HolidayCalendarTest {
    private fun decor(y: Int, m: Int, d: Int, site: List<SiteOccasion> = emptyList()) = HolidayCalendar.decorFor(LocalDate.of(y, m, d), site)

    @Test fun newYearHasFireworksAndSurvachka() {
        val d = decor(2026, 1, 1)
        assertEquals("new_year", d.greeting?.id)
        assertEquals(E.FIREWORKS, d.effect)
        assertEquals(O.SURVACHKA, d.ornament)
    }

    @Test fun martenitsaAllMarchButNationalDayWins() {
        assertEquals("baba_marta", decor(2026, 3, 1).greeting?.id)
        assertEquals(O.MARTENITSA, decor(2026, 3, 1).ornament)
        val liberation = decor(2026, 3, 3)
        assertEquals("liberation", liberation.greeting?.id)
        assertEquals(O.TRICOLOR, liberation.ornament)
        val plain = decor(2026, 3, 10)
        assertEquals(O.MARTENITSA, plain.ornament)
        assertNull(plain.greeting)
        assertEquals(O.NONE, decor(2026, 4, 1).ornament)
    }

    @Test fun memorialDaysAreQuiet() {
        val levski = decor(2026, 2, 19)
        assertTrue(levski.quiet)
        assertEquals("levski_memorial", levski.greeting?.id)
        assertEquals(E.NONE, levski.effect)
        assertEquals(O.MEMORIAL_WREATH, levski.ornament)
        assertEquals("Левски", levski.greeting?.link?.text)
        val botev = decor(2026, 6, 2)
        assertTrue(botev.quiet)
        assertEquals("botev", botev.greeting?.id)
    }

    @Test fun easterCycle2026() {
        // Великден 2026 е на 12 април.
        assertEquals("lazarovden", decor(2026, 4, 4).greeting?.id)
        assertEquals(O.FLOWER_WREATH, decor(2026, 4, 4).ornament)
        assertEquals("tsvetnitsa", decor(2026, 4, 5).greeting?.id)
        assertEquals(O.WILLOW, decor(2026, 4, 5).ornament)
        val friday = decor(2026, 4, 10)
        assertTrue(friday.quiet)
        assertEquals("good_friday", friday.greeting?.id)
        assertEquals(O.NONE, friday.ornament) // без весели яйца на Разпети петък
        assertEquals(O.EASTER_EGGS, decor(2026, 4, 11).ornament)
        assertNull(decor(2026, 4, 11).greeting)
        assertEquals("easter", decor(2026, 4, 12).greeting?.id)
        assertEquals("easter", decor(2026, 4, 13).greeting?.id)
        assertEquals(O.EASTER_EGGS, decor(2026, 4, 13).ornament)
        assertEquals("forgiveness", decor(2026, 2, 22).greeting?.id)
        assertEquals("ascension", decor(2026, 5, 21).greeting?.id)
    }

    @Test fun easterWinsOverGergyovden() {
        // 2024: Великден на 5 май, Гергьовден е Велики понеделник.
        assertEquals("easter", decor(2024, 5, 6).greeting?.id)
        assertEquals(O.EASTER_EGGS, decor(2024, 5, 6).ornament)
        val g = decor(2026, 5, 6)
        assertEquals("gergyovden", g.greeting?.id)
        assertEquals(O.GREEN_WREATH, g.ornament)
        assertEquals(E.PETALS, g.effect) // цветчетата на периода остават
    }

    @Test fun cultureDays() {
        val may24 = decor(2026, 5, 24)
        assertEquals("may24", may24.greeting?.id)
        assertEquals(E.LETTERS, may24.effect)
        assertEquals(O.LAUREL_WREATH, may24.ornament)
        val nov1 = decor(2026, 11, 1)
        assertEquals("enlighteners", nov1.greeting?.id)
        assertEquals(O.BOOK_FLAME, nov1.ornament)
        assertEquals(E.LEAVES, nov1.effect)
        assertTrue(decor(2026, 4, 23).greeting?.link?.newest == true)
        assertEquals("детск", decor(2026, 4, 2).greeting?.link?.departmentHint)
        assertEquals(E.BALLOONS, decor(2026, 6, 1).effect)
    }

    @Test fun seasonsStartOnAstronomicalDates() {
        assertEquals("spring", decor(2026, 3, 20).greeting?.id)
        assertEquals(E.PETALS_BUTTERFLIES, decor(2026, 3, 20).effect)
        assertEquals(O.MARTENITSA, decor(2026, 3, 20).ornament)
        assertEquals("summer", decor(2026, 6, 21).greeting?.id)
        assertEquals(O.SUN, decor(2026, 6, 21).ornament)
        assertEquals("independence", decor(2026, 9, 22).greeting?.id)
        assertEquals("autumn", decor(2026, 9, 23).greeting?.id)
        assertEquals(E.LEAVES, decor(2026, 9, 23).effect)
        val winter = decor(2026, 12, 21)
        assertEquals("winter", winter.greeting?.id)
        assertEquals(E.SNOW_STARS, winter.effect)
    }

    @Test fun christmas() {
        val eve = decor(2026, 12, 24)
        assertEquals("christmas_eve", eve.greeting?.id)
        assertEquals(O.RITUAL_BREAD, eve.ornament)
        assertEquals(E.SNOW_STARS, eve.effect)
        assertEquals(O.CHRISTMAS_STAR, decor(2026, 12, 25).ornament)
        assertEquals(E.SNOW, decor(2026, 12, 16).effect)
        assertEquals(O.CROSS_WATER, decor(2027, 1, 6).ornament)
        assertEquals(E.SNOW_STARS, decor(2027, 1, 6).effect)
    }

    @Test fun ordinaryDayIsEmpty() {
        assertTrue(decor(2026, 10, 12).isEmpty)
        assertEquals(E.LEAVES, decor(2026, 10, 15).effect)
        assertNull(decor(2026, 10, 15).greeting)
    }

    @Test fun seasonalEffectsByDate() {
        fun on(m: Int, d: Int, y: Int = 2026) = decor(y, m, d).effect
        assertEquals(E.NONE, on(10, 14))
        assertEquals(E.LEAVES, on(10, 15))
        assertEquals(E.LEAVES, on(11, 30))
        assertEquals(E.NONE, on(12, 1))
        assertEquals(E.NONE, on(12, 14))
        assertEquals(E.SNOW, on(12, 15))
        assertEquals(E.SNOW, on(12, 19))
        assertEquals(E.SNOW_STARS, on(12, 20))
        assertEquals(E.SNOW_STARS, on(12, 31))
        assertEquals(E.FIREWORKS, on(1, 1, 2027)) // Нова година е по-важна от снега
        assertEquals(E.SNOW_STARS, on(1, 2, 2027))
        assertEquals(E.SNOW_STARS, on(1, 7, 2027))
        assertEquals(E.NONE, on(1, 8, 2027))
        assertEquals(E.NONE, on(4, 30))
        assertEquals(E.PETALS, on(5, 1))
        assertEquals(E.PETALS, on(5, 10))
        assertEquals(E.NONE, on(5, 11))
        assertEquals(E.NONE, on(9, 14))
        assertEquals(E.NONE, on(2, 29, 2028))
    }

    @Test fun everyGreetingIdIsKnown() {
        val ids = HashSet<String>()
        var d = LocalDate.of(2024, 1, 1)
        while (d.year < 2031) {
            HolidayCalendar.occasionsOn(d).filter { it.greeting }.mapTo(ids) { it.id }
            d = d.plusDays(1)
        }
        assertEquals(GREETING_IDS, ids)
    }

    @Test fun siteOccasionsJoinTheCalendar() {
        val entries = SiteOccasionsParser.parse(
            """{"occasions":[
              {"id":"sabor","date":"08-28","until":"08-29","title":"Събор на Яворец","text":"Заповядайте!","effect":"fireworks","ornament":"laurel-wreath","search":"Яворец"},
              {"id":"vazpomenanie","date":"2026-07-01","title":"Ден на памет","quiet":true,"ornament":"memorial_wreath"},
              {"id":"zima","date":"12-30","until":"01-02","title":"Зимни празници"},
              {"id":"bad","date":"13-45","title":"x"},
              {"date":"01-01","title":"без id"}
            ]}""",
        )!!
        assertEquals(listOf("sabor", "vazpomenanie", "zima"), entries.map { it.id })
        val site = entries.mapNotNull { SiteOccasionsParser.toSiteOccasion(it) }
        val sabor = decor(2026, 8, 29, site)
        assertEquals("site_sabor", sabor.greeting?.id)
        assertEquals("Събор на Яворец", sabor.greeting?.custom?.titleBg)
        assertEquals(E.FIREWORKS, sabor.effect)
        assertEquals(O.LAUREL_WREATH, sabor.ornament)
        assertEquals("Яворец", sabor.greeting?.link?.text)
        assertTrue(decor(2026, 8, 30, site).isEmpty)
        val quiet = decor(2026, 7, 1, site)
        assertTrue(quiet.quiet)
        assertFalse(decor(2027, 7, 1, site).quiet) // само през 2026
        assertEquals("site_zima", decor(2027, 1, 2, site).greeting?.id)
        // Новата година остава по-важна от местния празник.
        assertEquals("new_year", decor(2027, 1, 1, site).greeting?.id)
    }

    @Test fun oneYearSiteOccasionAcrossNewYearMatchesOnlyItsOwnDays() {
        val entry = SiteOccasionsParser.parse(
            """[{"id":"zima26","date":"2026-12-30","until":"2027-01-02","title":"Зимни празници 2026"}]""",
        )!!.single()
        val site = SiteOccasionsParser.toSiteOccasion(entry)!!
        assertEquals(2026, site.year)
        // Януари на същата година е преди периода — не бива да съвпада.
        assertFalse(site.matches(LocalDate.of(2026, 1, 1)))
        assertFalse(site.matches(LocalDate.of(2026, 1, 2)))
        assertFalse(site.matches(LocalDate.of(2026, 12, 29)))
        assertTrue(site.matches(LocalDate.of(2026, 12, 30)))
        assertTrue(site.matches(LocalDate.of(2026, 12, 31)))
        assertTrue(site.matches(LocalDate.of(2027, 1, 1)))
        assertTrue(site.matches(LocalDate.of(2027, 1, 2)))
        assertFalse(site.matches(LocalDate.of(2027, 1, 3)))
        // Декември на следващата година вече е извън периода.
        assertFalse(site.matches(LocalDate.of(2027, 12, 31)))
        assertFalse(site.matches(LocalDate.of(2028, 1, 1)))
        assertEquals("site_zima26", decor(2026, 12, 31, listOf(site)).greeting?.id)
        assertTrue(HolidayCalendar.occasionsOn(LocalDate.of(2026, 1, 2), listOf(site)).none { it.id == "site_zima26" })
    }

    @Test fun oneYearSiteOccasionWithEndBeforeStartIsRejected() {
        // Краят е преди началото в същата година; периодът над две години също е невалиден.
        val entries = listOf(
            SiteOccasionEntry(id = "a", date = "2026-12-30", until = "2026-01-02", title = "x"),
            SiteOccasionEntry(id = "b", date = "2026-12-30", until = "2028-01-02", title = "x"),
            SiteOccasionEntry(id = "c", date = "2026-03-01", until = "2027-03-05", title = "x"),
            SiteOccasionEntry(id = "d", date = "2026-12-30", until = "01-02", title = "x"),
        )
        entries.forEach { assertNull(SiteOccasionsParser.toSiteOccasion(it), it.id) }
        // Едногодишен период в рамките на годината съвпада само в нея.
        val summer = SiteOccasionsParser.toSiteOccasion(SiteOccasionEntry(id = "s", date = "2026-07-01", until = "2026-07-03", title = "x"))!!
        assertTrue(summer.matches(LocalDate.of(2026, 7, 2)))
        assertFalse(summer.matches(LocalDate.of(2027, 7, 2)))
        assertFalse(summer.matches(LocalDate.of(2025, 7, 2)))
    }

    companion object {
        /** Всички поздрави — за всеки има текст `occ_<id>_title`/`occ_<id>_text` в приложението. */
        val GREETING_IDS = setOf(
            "spring", "summer", "autumn", "winter",
            "new_year", "liberation", "labour", "gergyovden", "may24", "unification", "independence",
            "good_friday", "easter", "christmas_eve", "christmas",
            "levski_memorial", "levski_birth", "botev", "enlighteners", "children_book", "book_day", "children_day",
            "jordan", "babinden", "trifon", "baba_marta", "lazarovden", "tsvetnitsa", "forgiveness", "ascension",
            "enyovden", "ilinden", "assumption", "dimitrovden", "nikulden", "ignazhden",
        )
    }
}
