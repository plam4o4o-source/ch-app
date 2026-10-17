package org.chyavorec.domain

import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanReminderSync
import org.chyavorec.domain.model.MessagePollPolicy
import org.chyavorec.domain.model.MessagePollPolicy.BASE_MILLIS
import org.chyavorec.domain.model.MessagePollPolicy.MAX_MILLIS
import org.chyavorec.domain.model.MessagePriority
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** Пренасрочване на напомнянията само при промяна и адаптивната проверка за съобщения. */
class BackgroundPoliciesTest {
    private fun loan(id: String, due: String?, pending: Boolean = false) =
        Loan(id, 1, "Под игото", "Иван Вазов", dueOn = due, renewPending = pending)

    // --- LoanReminderSync ---

    @Test fun unknownPlanSchedulesEverything() {
        val loans = listOf(loan("L1", "2026-10-20"), loan("L2", "2026-10-25"))
        val changes = LoanReminderSync.diff(null, loans)
        assertEquals(loans, changes.schedule)
        assertTrue(changes.cancel.isEmpty())
    }

    @Test fun unchangedPlanDoesNothing() {
        val loans = listOf(loan("L1", "2026-10-20"), loan("L2", "2026-10-25"))
        val stored = LoanReminderSync.entries(loans)
        // Другият ред на списъка не е промяна.
        assertTrue(LoanReminderSync.diff(stored, loans.reversed()).isEmpty)
    }

    @Test fun renewalAndPendingRescheduleOnlyThatLoan() {
        val stored = LoanReminderSync.entries(listOf(loan("L1", "2026-10-20"), loan("L2", "2026-10-25")))
        val renewed = LoanReminderSync.diff(stored, listOf(loan("L1", "2026-11-10"), loan("L2", "2026-10-25")))
        assertEquals(listOf("L1"), renewed.schedule.map { it.loanId })
        assertTrue(renewed.cancel.isEmpty())
        val pending = LoanReminderSync.diff(stored, listOf(loan("L1", "2026-10-20"), loan("L2", "2026-10-25", pending = true)))
        assertEquals(listOf("L2"), pending.schedule.map { it.loanId })
    }

    @Test fun returnedLoanIsCancelledNewLoanScheduled() {
        val stored = LoanReminderSync.entries(listOf(loan("L|1", "2026-10-20"), loan("L2", "2026-10-25")))
        val changes = LoanReminderSync.diff(stored, listOf(loan("L2", "2026-10-25"), loan("L3", "2026-11-01")))
        assertEquals(listOf("L3"), changes.schedule.map { it.loanId })
        // loanId с разделителя в него се разпознава правилно.
        assertEquals(setOf("L|1"), changes.cancel)
    }

    @Test fun emptyPlanAfterSignOutOrDisabledSchedulesAll() {
        val loans = listOf(loan("L1", "2026-10-20"))
        assertEquals(loans, LoanReminderSync.diff(emptySet(), loans).schedule)
        assertTrue(LoanReminderSync.diff(emptySet(), emptyList()).isEmpty)
    }

    @Test fun duplicateLoanIdsCountOnce() {
        val loans = listOf(loan("L1", "2026-10-20"), loan("L1", "2026-10-20"))
        assertEquals(1, LoanReminderSync.diff(null, loans).schedule.size)
        assertEquals(1, LoanReminderSync.entries(loans).size)
    }

    // --- MessagePollPolicy ---

    @Test fun intervalDoublesUpToSixHoursAndResetsOnChange() {
        var i = BASE_MILLIS
        val seen = mutableListOf<Long>()
        repeat(5) { i = MessagePollPolicy.next(i, changed = false); seen += i / BASE_MILLIS }
        assertEquals(listOf(2L, 4L, 6L, 6L, 6L), seen)
        assertEquals(BASE_MILLIS, MessagePollPolicy.next(MAX_MILLIS, changed = true))
        // Повредена/липсваща стойност → от началото.
        assertEquals(2 * BASE_MILLIS, MessagePollPolicy.next(0L, changed = false))
    }

    @Test fun dueOnlyAfterIntervalWithSlack() {
        val now = 100 * BASE_MILLIS
        assertTrue(MessagePollPolicy.isDue(now, 0L, BASE_MILLIS))
        assertTrue(MessagePollPolicy.isDue(now, now - 55 * 60_000L, BASE_MILLIS)) // в отклонението на WorkManager
        assertFalse(MessagePollPolicy.isDue(now, now - 30 * 60_000L, BASE_MILLIS))
        assertFalse(MessagePollPolicy.isDue(now, now - 2 * BASE_MILLIS, 4 * BASE_MILLIS))
        assertTrue(MessagePollPolicy.isDue(now, now - 4 * BASE_MILLIS, 4 * BASE_MILLIS))
        // Часовникът е върнат назад.
        assertTrue(MessagePollPolicy.isDue(now, now + BASE_MILLIS, MAX_MILLIS))
    }

    @Test fun dormantAfterFourteenDaysWithoutOpening() {
        val day = 24 * BASE_MILLIS
        val now = 1000 * day
        assertFalse(MessagePollPolicy.isDormant(now, 0L))
        assertFalse(MessagePollPolicy.isDormant(now, now - 13 * day))
        assertTrue(MessagePollPolicy.isDormant(now, now - 14 * day))
        assertFalse(MessagePollPolicy.isDormant(now, now + day))
    }

    @Test fun signatureIgnoresOrderButSeesEdits() {
        val a = AppMessage("1", "Покана", "Текст", createdAt = "2026-10-01T10:00:00Z")
        val b = AppMessage("2", "Важно", "Друг", priority = MessagePriority.HIGH, createdAt = "2026-10-02T10:00:00Z")
        assertEquals(MessagePollPolicy.signature(listOf(a, b)), MessagePollPolicy.signature(listOf(b, a)))
        assertNotEquals(MessagePollPolicy.signature(listOf(a, b)), MessagePollPolicy.signature(listOf(a, b.copy(body = "Поправен"))))
        assertNotEquals(MessagePollPolicy.signature(listOf(a)), MessagePollPolicy.signature(listOf(a, b)))
    }
}
