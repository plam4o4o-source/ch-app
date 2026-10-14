package org.chyavorec.domain

import org.chyavorec.domain.model.LoanNotificationLog
import org.chyavorec.domain.model.LoanReminder
import org.chyavorec.domain.model.LoanReminderKind.BEFORE
import org.chyavorec.domain.model.LoanReminderKind.DUE_DAY
import org.chyavorec.domain.model.LoanReminderPlan
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoanRemindersTest {
    private val zone = ZoneId.of("Europe/Sofia")
    private val due = LocalDate.of(2026, 10, 20)

    private fun sofia(date: LocalDate, h: Int, m: Int = 0): Instant = LocalDateTime.of(date, java.time.LocalTime.of(h, m)).atZone(zone).toInstant()
    private fun plan(now: Instant) = LoanReminderPlan.plan(due, now, zone)

    @Test fun bothRemindersAtTenSofiaTime() {
        val now = sofia(due.minusDays(5), 15)
        assertEquals(
            listOf(LoanReminder(BEFORE, sofia(due.minusDays(2), 10)), LoanReminder(DUE_DAY, sofia(due, 10))),
            plan(now),
        )
        // 10:00 в София през лятното часово време е 07:00 UTC.
        assertEquals(Instant.parse("2026-10-18T07:00:00Z"), plan(now).first().at)
    }

    @Test fun winterTimeStillTenLocal() {
        val winterDue = LocalDate.of(2026, 11, 10)
        val r = LoanReminderPlan.plan(winterDue, sofia(winterDue.minusDays(10), 12), zone)
        assertEquals(Instant.parse("2026-11-10T08:00:00Z"), r.single { it.kind == DUE_DAY }.at)
    }

    @Test fun pastReminderSkipped() {
        // Ден преди срока: напомнянето „2 дни преди“ е минало, остава само денят на срока.
        assertEquals(listOf(LoanReminder(DUE_DAY, sofia(due, 10))), plan(sofia(due.minusDays(1), 9)))
    }

    @Test fun beforeTenOnDueDaySchedulesAtTen() {
        assertEquals(listOf(LoanReminder(DUE_DAY, sofia(due, 10))), plan(sofia(due, 9, 59)))
    }

    @Test fun dueTodayAfterTenFiresSoon() {
        val now = sofia(due, 14, 30)
        assertEquals(listOf(LoanReminder(DUE_DAY, now.plus(LoanReminderPlan.SOON))), plan(now))
    }

    @Test fun beforeDayAfterTenAlsoCatchesUp() {
        val now = sofia(due.minusDays(2), 12)
        assertEquals(
            listOf(LoanReminder(BEFORE, now.plus(LoanReminderPlan.SOON)), LoanReminder(DUE_DAY, sofia(due, 10))),
            plan(now),
        )
    }

    @Test fun dueTodayInTheEveningNothing() {
        assertEquals(emptyList(), plan(sofia(due, 20, 0)))
        assertEquals(emptyList(), plan(sofia(due, 23, 30)))
    }

    @Test fun overdueNothing() {
        assertEquals(emptyList(), plan(sofia(due.plusDays(1), 9)))
    }

    @Test fun neverInQuietHours() {
        // Пълно обхождане на 4 денонощия през 15 минути: нито един момент не е в 21:00–08:00.
        var t = sofia(due.minusDays(4), 0)
        val end = sofia(due.plusDays(1), 0)
        while (t < end) {
            plan(t).forEach { r ->
                assertFalse(LoanReminderPlan.isQuiet(r.at, zone), "$t → ${r.at}")
                assertTrue(r.at > t)
            }
            t = t.plusSeconds(15 * 60)
        }
    }

    @Test fun quietHoursBoundaries() {
        assertTrue(LoanReminderPlan.isQuiet(sofia(due, 21, 0), zone))
        assertTrue(LoanReminderPlan.isQuiet(sofia(due, 3, 0), zone))
        assertTrue(LoanReminderPlan.isQuiet(sofia(due, 7, 59), zone))
        assertFalse(LoanReminderPlan.isQuiet(sofia(due, 8, 0), zone))
        assertFalse(LoanReminderPlan.isQuiet(sofia(due, 20, 59), zone))
    }

    @Test fun deliverableOnlyOnItsDayAndDaytime() {
        assertTrue(LoanReminderPlan.deliverable(DUE_DAY, due, sofia(due, 10, 5), zone))
        // Закъснял WorkManager: вечерта или на следващия ден — без известие.
        assertFalse(LoanReminderPlan.deliverable(DUE_DAY, due, sofia(due, 22), zone))
        assertFalse(LoanReminderPlan.deliverable(DUE_DAY, due, sofia(due.plusDays(1), 10), zone))
        // Срокът е променен (удължаване): старото „2 дни преди“ вече не е за днес.
        assertFalse(LoanReminderPlan.deliverable(BEFORE, due.plusDays(21), sofia(due.minusDays(2), 10), zone))
        assertTrue(LoanReminderPlan.deliverable(BEFORE, due, sofia(due.minusDays(2), 10), zone))
    }

    @Test fun atForSingleKind() {
        assertNull(LoanReminderPlan.at(BEFORE, due, sofia(due, 9), zone))
        assertEquals(sofia(due, 10), LoanReminderPlan.at(DUE_DAY, due, sofia(due, 9), zone))
    }

    // --- дневник на известията ---

    @Test fun logRemembersSentReminderPerDueDate() {
        val today = LocalDate.of(2026, 10, 18)
        val log = LoanNotificationLog.add(emptySet(), LoanNotificationLog.entry("L|1", due, BEFORE.name, today), today)
        assertTrue(LoanNotificationLog.wasSent(log, "L|1", due, BEFORE.name))
        assertFalse(LoanNotificationLog.wasSent(log, "L|1", due, DUE_DAY.name))
        // Удължен срок → ново напомняне.
        assertFalse(LoanNotificationLog.wasSent(log, "L|1", due.plusDays(21), BEFORE.name))
        assertFalse(LoanNotificationLog.wasSent(log, "L", due, BEFORE.name))
        assertTrue(LoanNotificationLog.sentOn(log, "L|1", today))
        assertFalse(LoanNotificationLog.sentOn(log, "L|1", today.plusDays(1)))
    }

    @Test fun logDropsOldAndBrokenEntries() {
        val today = LocalDate.of(2026, 10, 18)
        val old = LoanNotificationLog.entry("A", due, BEFORE.name, today.minusDays(90))
        val log = LoanNotificationLog.add(setOf(old, "счупен"), LoanNotificationLog.entry("B", due, LoanNotificationLog.OVERDUE, today), today)
        assertEquals(setOf(LoanNotificationLog.entry("B", due, LoanNotificationLog.OVERDUE, today)), log)
    }
}
