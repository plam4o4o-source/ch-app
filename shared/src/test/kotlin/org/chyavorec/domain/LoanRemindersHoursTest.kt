package org.chyavorec.domain

import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanReminder
import org.chyavorec.domain.model.LoanReminderKind.BEFORE
import org.chyavorec.domain.model.LoanReminderKind.DUE_DAY
import org.chyavorec.domain.model.LoanReminderPlan
import org.chyavorec.domain.model.LoanReminderSync
import org.chyavorec.domain.model.OpeningHours
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Напомнянията за срок спрямо работното време на библиотеката. */
class LoanRemindersHoursTest {
    private val zone = ZoneId.of("Europe/Sofia")
    private val fallback = OpeningHours.parse(
        listOf("Понеделник – четвъртък: 08:00 – 16:30", "Петък: 08:00 – 16:00", "Събота и неделя: почивни дни"),
    )!!

    /** 23.11.2026 е понеделник. */
    private fun d(day: Int): LocalDate = LocalDate.of(2026, 11, day)
    private fun sofia(date: LocalDate, h: Int, m: Int = 0): Instant = LocalDateTime.of(date, LocalTime.of(h, m)).atZone(zone).toInstant()

    @Test fun usualHoursKeepTenOClock() {
        // В 10:00 библиотеката е отворена — същото като без работно време.
        val due = d(26)
        val now = sofia(d(20), 12)
        assertEquals(LoanReminderPlan.plan(due, now, zone), LoanReminderPlan.plan(due, now, zone, fallback))
        assertEquals("", LoanReminderPlan.hoursKey(fallback))
        assertEquals("", LoanReminderPlan.hoursKey(null))
    }

    @Test fun afternoonOpeningKeepsMorningReminder() {
        // Отваря в 13:00 — 10:00 е сутринта преди отваряне.
        val h = OpeningHours.parse(listOf("Понеделник – петък: 13:00 – 18:00"))!!
        assertEquals(LocalTime.of(10, 0), LoanReminderPlan.remindTime(DayOfWeek.WEDNESDAY, h))
        assertEquals("", LoanReminderPlan.hoursKey(h))
    }

    @Test fun closedBeforeTenMovesToOpening() {
        // В сряда работи само 08:30–09:30 — напомнянето е при отварянето, не след затварянето.
        val h = OpeningHours.parse(listOf("Сряда: 08:30 – 09:30", "Четвъртък: 07:00 – 09:00", "Петък: 08:00 – 16:00"))!!
        assertEquals(LocalTime.of(8, 30), LoanReminderPlan.remindTime(DayOfWeek.WEDNESDAY, h))
        // Не по-рано от края на тихите часове.
        assertEquals(LocalTime.of(8, 0), LoanReminderPlan.remindTime(DayOfWeek.THURSDAY, h))
        val due = d(27) // петък; „2 дни преди“ — сряда
        assertEquals(
            listOf(LoanReminder(BEFORE, sofia(d(25), 8, 30)), LoanReminder(DUE_DAY, sofia(due, 10))),
            LoanReminderPlan.plan(due, sofia(d(20), 12), zone, h),
        )
        assertEquals("3=08:30,4=08:00", LoanReminderPlan.hoursKey(h))
    }

    @Test fun closedDayKeepsTenOClock() {
        val due = d(28) // събота
        assertEquals(
            listOf(LoanReminder(BEFORE, sofia(d(26), 10)), LoanReminder(DUE_DAY, sofia(due, 10))),
            LoanReminderPlan.plan(due, sofia(d(20), 12), zone, fallback),
        )
    }

    @Test fun dueOnClosedDayGivesReturnByNote() {
        // Срок в събота: върни до петък 16:00 (напомнянето в четвъртък).
        val note = LoanReminderPlan.closedDue(d(28), sofia(d(26), 10), fallback, zone)
        assertEquals(LoanReminderPlan.ClosedDue(DayOfWeek.SATURDAY, LocalDateTime.of(2026, 11, 27, 16, 0)), note)
        // Срок в неделя — също до петък.
        assertEquals(LocalDateTime.of(2026, 11, 27, 16, 0), LoanReminderPlan.closedDue(d(29), sofia(d(27), 10), fallback, zone)?.returnBy)
        // В самата събота петък вече е минал — само че библиотеката не работи.
        assertEquals(LoanReminderPlan.ClosedDue(DayOfWeek.SATURDAY, null), LoanReminderPlan.closedDue(d(28), sofia(d(28), 10), fallback, zone))
    }

    @Test fun noNoteOnOpenDayOrUnknownHours() {
        assertNull(LoanReminderPlan.closedDue(d(27), sofia(d(25), 10), fallback, zone))
        assertNull(LoanReminderPlan.closedDue(d(28), sofia(d(26), 10), null, zone))
    }

    @Test fun changedHoursRescheduleOnlyWhenTheTimeChanges() {
        val loan = Loan("L1", 1, "Под игото", "Иван Вазов", dueOn = "2026-11-27")
        val plain = LoanReminderSync.entries(listOf(loan))
        // Обичайното работно време не мени отпечатъка — без излишно пренасрочване.
        assertEquals(plain, LoanReminderSync.entries(listOf(loan), LoanReminderPlan.hoursKey(fallback)))
        assertTrue(LoanReminderSync.diff(plain, listOf(loan), LoanReminderPlan.hoursKey(fallback)).isEmpty)
        val early = OpeningHours.parse(listOf("Сряда: 08:30 – 09:30"))!!
        assertEquals(listOf(loan), LoanReminderSync.diff(plain, listOf(loan), LoanReminderPlan.hoursKey(early)).schedule)
    }
}
