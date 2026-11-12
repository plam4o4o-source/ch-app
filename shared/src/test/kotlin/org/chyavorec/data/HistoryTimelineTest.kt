package org.chyavorec.data

import org.chyavorec.TestUtil
import org.chyavorec.data.site.HistoryTimelineParser
import org.chyavorec.domain.timeline.OnThisDay
import org.chyavorec.domain.timeline.TimelineEntry
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Хронологията от РЕАЛНАТА страница „История“ (снимка на `.hist-timeline-wrap` от chyavorec.org). */
class HistoryTimelineTest {
    private val entries = HistoryTimelineParser.parse(TestUtil.resource("site-history.html"), "https://chyavorec.org")

    @Test fun parsesAllFourteenMilestonesInOrder() {
        assertEquals(14, entries.size)
        assertEquals("Театрален кръжец и зараждане на идеята", entries.first().title)
        assertEquals("Днес", entries.last().label)
        assertTrue(entries.all { it.title.isNotBlank() && it.description.isNotBlank() })
        // Годините растат (периодът и „След 1989“ — по началната година; „Днес“ няма година).
        assertTrue(entries.dropLast(1).mapNotNull { it.year }.zipWithNext().all { (a, b) -> a <= b })
    }

    @Test fun understandsTheYearLabels() {
        val byLabel = entries.associateBy { it.label }
        assertEquals(1900, byLabel.getValue("1900").exactYear)
        val range = byLabel.getValue("1912–1918")
        assertEquals(1912, range.year); assertEquals(1918, range.endYear); assertNull(range.exactYear)
        val founded = byLabel.getValue("17 дек. 1922")
        assertEquals(1922, founded.exactYear); assertEquals(17, founded.day); assertEquals(12, founded.month)
        val spade = byLabel.getValue("17 март 1958")
        assertEquals(1958, spade.exactYear); assertEquals(17, spade.day); assertEquals(3, spade.month)
        val after = byLabel.getValue("След 1989")
        assertEquals(1989, after.year); assertTrue(after.approximate); assertNull(after.exactYear)
        val today = byLabel.getValue("Днес")
        assertNull(today.year); assertNull(today.exactYear)
    }

    @Test fun exactlyTwoMilestonesCarryADate() {
        assertEquals(setOf(12 to 17, 3 to 17), entries.filter { it.day != null }.map { it.month!! to it.day!! }.toSet())
    }

    @Test fun onThisDayFindsTheFoundingAndTheFirstSpade() {
        val dec = OnThisDay.entryFor(LocalDate.of(2026, 12, 17), entries)
        assertEquals("Възстановяване и официално учредяване", dec?.title)
        assertEquals(104, OnThisDay.yearsAgo(dec!!, LocalDate.of(2026, 12, 17)))
        val mar = OnThisDay.entryFor(LocalDate.of(2027, 3, 17), entries)
        assertEquals("Първа копка", mar?.title)
        assertNull(OnThisDay.entryFor(LocalDate.of(2026, 11, 12), entries))
        assertNull(OnThisDay.entryFor(LocalDate.of(2026, 12, 18), entries))
    }

    @Test fun cheapGateCoversEveryDatedMilestone() {
        // Home не чете кеша в другите дни; ако сайтът добави нова дата, този тест ще го покаже.
        for (e in entries.filter { it.day != null }) assertTrue(OnThisDay.mayHaveEntry(LocalDate.of(2026, e.month!!, e.day!!)), e.label)
        assertFalse(OnThisDay.mayHaveEntry(LocalDate.of(2026, 11, 12)))
        assertFalse(OnThisDay.mayHaveEntry(LocalDate.of(2026, 12, 16)))
    }

    @Test fun changedMarkupGivesAnEmptyListNotAnException() {
        assertTrue(HistoryTimelineParser.parse("<div class=\"timeline\"><div class=\"item\">1900 Нещо</div></div>").isEmpty())
        assertTrue(HistoryTimelineParser.parse("").isEmpty())
        // Запис без година или без заглавие се пропуска, останалите оцеляват.
        val partial = HistoryTimelineParser.parse(
            "<div class=\"hist-tl-item\"><div class=\"hist-tl-year\"></div><div class=\"hist-tl-event\">Без година</div></div>" +
                "<div class=\"hist-tl-item\"><div class=\"hist-tl-year\">1930</div><div class=\"hist-tl-event\">Добро</div></div>",
        )
        assertEquals(listOf("Добро"), partial.map { it.title })
        assertEquals("", partial.single().description)
    }

    @Test fun labelsWithDatesAndRangesAreRobust() {
        fun p(l: String) = HistoryTimelineParser.parseLabel(l, "т", "")
        assertEquals(3, p("5 март 1950").month)
        assertEquals(12, p("17 декември 1922").month)
        assertNull(p("31 февруари").exactYear)
        assertEquals(1990, p("1990-1995").year)
        assertEquals(1995, p("1990-1995").endYear)
        assertTrue(p("Около 1930").approximate)
        assertNull(p("Лятото").year)
    }

    @Test fun entriesSurviveTheCacheRoundTrip() {
        val json = kotlinx.serialization.json.Json
        val list = kotlinx.serialization.builtins.ListSerializer(TimelineEntry.serializer())
        assertEquals(entries, json.decodeFromString(list, json.encodeToString(list, entries)))
        assertNotNull(entries.firstOrNull())
    }
}
