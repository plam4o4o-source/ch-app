package org.chyavorec.domain

import org.chyavorec.domain.model.DayStatus
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.MonthCalendar
import java.time.LocalDate
import java.time.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MonthCalendarTest {
    // Годишният календар е „превъртян“ спрямо 27.09.2026: минали дни → следващата година.
    private fun ev(id: String, date: String) = Event(id, "Събитие $id", date, sourceUrl = "/events", recurring = true)
    private val events = listOf(
        ev("a", "2027-09-06"),   // 6 септември — вече минал тази година
        ev("b", "2026-09-27"),   // днес
        ev("c", "2026-09-30"),
        ev("d", "2026-10-01"),
        ev("e", "2027-01-06"),
        ev("x", "bad-date"),
    )

    @Test fun currentMonthWithStatuses() {
        val cal = MonthCalendar.of(events, LocalDate.parse("2026-09-27"))
        assertEquals(YearMonth.of(2026, 9), cal.month)
        assertEquals(listOf("a" to DayStatus.PAST, "b" to DayStatus.TODAY, "c" to DayStatus.UPCOMING), cal.items.map { it.event.id to it.status })
        assertEquals(listOf(6, 27, 30), cal.items.map { it.day })
        assertEquals(2, cal.upcomingCount)
        assertEquals("d", cal.next?.id)
    }

    @Test fun switchesAutomaticallyWithTheMonth() {
        val cal = MonthCalendar.of(events, LocalDate.parse("2026-10-01"))
        assertEquals(YearMonth.of(2026, 10), cal.month)
        assertEquals(listOf("d"), cal.items.map { it.event.id })
        assertEquals(DayStatus.TODAY, cal.items.single().status)
    }

    @Test fun emptyMonthStillPointsToNextDate() {
        val cal = MonthCalendar.of(events, LocalDate.parse("2026-11-15"))
        assertTrue(cal.items.isEmpty())
        assertEquals("e", cal.next?.id)
    }
}
