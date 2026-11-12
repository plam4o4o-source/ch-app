package org.chyavorec.domain.occasion

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GreetingCardsTest {
    @Test fun everyFestiveGreetingOfTheCalendarHasACard() {
        val missing = mutableSetOf<String>()
        var d = LocalDate.of(2026, 1, 1)
        while (d.year < 2030) {
            val decor = HolidayCalendar.decorFor(d)
            val o = decor.greeting
            if (o != null && !decor.quiet && o.custom == null && o.id !in GreetingCards.IDS) missing += o.id
            if (o != null && !decor.quiet) assertTrue(GreetingCards.available(decor), "${o.id} на $d")
            d = d.plusDays(1)
        }
        assertEquals(emptySet(), missing)
    }

    @Test fun quietDaysAndEmptyDaysHaveNoCard() {
        assertFalse(GreetingCards.available(HolidayCalendar.decorFor(LocalDate.of(2026, 2, 19))), "Левски")
        assertFalse(GreetingCards.available(HolidayCalendar.decorFor(LocalDate.of(2026, 6, 2))), "Ботев")
        // Разпети петък 2026 е 10 април (православен Великден 12 април).
        assertFalse(GreetingCards.available(HolidayCalendar.decorFor(LocalDate.of(2026, 4, 10))), "Разпети петък")
        assertFalse(GreetingCards.available(HolidayCalendar.decorFor(LocalDate.of(2026, 8, 3))), "обикновен ден")
        assertFalse(GreetingCards.available(DayDecor.NONE))
        assertTrue(GreetingCards.available(HolidayCalendar.decorFor(LocalDate.of(2026, 3, 1))), "Баба Марта")
    }

    @Test fun siteOccasionsUseTheirOwnText() {
        val custom = Occasion("village_feast", HolidayCalendar.SITE, custom = CustomGreeting("Събор на селото", "Честит събор!", null, null))
        assertTrue(GreetingCards.available(DayDecor(greeting = custom)))
        val quiet = Occasion("village_memorial", HolidayCalendar.SITE, OccasionMood.QUIET, custom = CustomGreeting("Помен", "Тих ден", null, null))
        assertFalse(GreetingCards.available(DayDecor(greeting = quiet, quiet = true)))
    }

    @Test fun cleanLineKeepsOrdinaryBulgarianAndLatinText() {
        assertEquals("За баба Мария", GreetingCards.sanitizeLine("За баба Мария"))
        assertEquals("Für Grandma Zoë – with love…", GreetingCards.sanitizeLine("Für Grandma Zoë – with love…"))
        assertEquals("„Честит“ рожден ден, Ґрета!", GreetingCards.sanitizeLine("„Честит“ рожден ден, Ґрета!"))
        assertEquals("№ 5 (и 7/8)", GreetingCards.sanitizeLine("№ 5 (и 7/8)"))
    }

    @Test fun cleanLineDropsEmojiSymbolsAndInvisibleMarks() {
        assertEquals("Честит празник", GreetingCards.sanitizeLine("Честит 🎉 празник 💐"))
        assertEquals("Ма", GreetingCards.sanitizeLine("М‍‎а‮⁦"))
        assertEquals("ab", GreetingCards.sanitizeLine("a\u0000b\u0007"))
        assertEquals("A B", GreetingCards.sanitizeLine("A❤️ B"))
        assertEquals("", GreetingCards.sanitizeLine("🎉🎉"))
        // Лоши (непарни) суррогати не стигат до картата.
        assertEquals("ab", GreetingCards.sanitizeLine("a\uD83Db\uDE00"))
        // Арабски, китайски, разширена кирилица — вън от шрифтовете.
        assertEquals("x", GreetingCards.sanitizeLine("xم中҂"))
    }

    @Test fun cleanLineCollapsesWhitespaceAndTrims() {
        assertEquals("баба Мария и дядо Иван", GreetingCards.sanitizeLine("  баба   Мария\n\tи дядо\r\nИван  \n"))
        assertEquals("", GreetingCards.sanitizeLine(" \n\t "))
        assertEquals("", GreetingCards.sanitizeLine(""))
    }

    @Test fun cleanLineIsAtMostSixtyCharsAndNeverEndsInASpace() {
        val long = "Много скъпа и обичана баба Мария от цялото семейство с любов и здраве"
        val cut = GreetingCards.sanitizeLine(long)
        assertTrue(cut.length <= GreetingCards.MAX_LINE, cut.length.toString())
        assertFalse(cut.endsWith(" "))
        assertEquals(long.take(cut.length), cut)
        val spaceAt60 = "а".repeat(59) + " " + "б".repeat(10)
        assertEquals("а".repeat(59), GreetingCards.sanitizeLine(spaceAt60))
        assertEquals(GreetingCards.MAX_LINE, GreetingCards.sanitizeLine("Ш".repeat(500)).length)
    }

    @Test fun sanitizeIsIdempotent() {
        for (s in listOf("За баба Мария", "x 🎉 y", "  много   интервали ", "Ш".repeat(100), "‮evil")) {
            val once = GreetingCards.sanitizeLine(s)
            assertEquals(once, GreetingCards.sanitizeLine(once))
        }
    }

    @Test fun lineGetsSmallerAsItGetsLonger() {
        val sizes = listOf(5, 20, 40, 60).map { GreetingCards.lineSizeSp("а".repeat(it)) }
        assertEquals(sizes.sortedDescending(), sizes)
        assertTrue(sizes.first() > sizes.last())
    }

    @Test fun allowedCharsAreInsideTheFontRanges() {
        for (c in 0..0xFFFF) {
            val ch = c.toChar()
            if (!GreetingCards.isAllowed(ch)) continue
            val ok = c in 0x20..0x7E || c in 0xA1..0x17F || c in 0x400..0x45F || c == 0x490 || c == 0x491 || c in 0x2010..0x2026 || c == 0x2116
            assertTrue(ok, "U+${c.toString(16)}")
        }
    }
}
