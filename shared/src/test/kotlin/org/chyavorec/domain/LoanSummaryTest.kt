package org.chyavorec.domain

import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanSummary
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class LoanSummaryTest {
    private val today = LocalDate.of(2026, 10, 17)
    private fun loan(id: String, due: String?, title: String = "Книга $id") = Loan(id, null, title, "Автор", dueOn = due)

    @Test fun noLoansNoCard() {
        assertNull(LoanSummary.of(null, today))
        assertNull(LoanSummary.of(emptyList(), today))
    }

    @Test fun nextDueIsTheClosest() {
        val s = LoanSummary.of(listOf(loan("1", "2026-11-01"), loan("2", "2026-10-21", "Под игото"), loan("3", null)), today)
        val next = assertIs<LoanSummary.NextDue>(s)
        assertEquals("Под игото", next.loan.title)
        assertEquals(LocalDate.of(2026, 10, 21), next.dueOn)
        assertEquals(4, next.daysLeft)
        assertEquals(DueStatus.PLENTY_OF_TIME, next.status)
        assertEquals(3, next.total)
    }

    @Test fun dueTodayIsDueSoon() {
        val next = assertIs<LoanSummary.NextDue>(LoanSummary.of(listOf(loan("1", "2026-10-17")), today))
        assertEquals(0, next.daysLeft)
        assertEquals(DueStatus.DUE_SOON, next.status)
    }

    @Test fun overdueWinsAndCountsAll() {
        val s = LoanSummary.of(
            listOf(loan("1", "2026-10-20"), loan("2", "2026-10-15"), loan("3", "2026-10-10", "Най-старата")),
            today,
        )
        val o = assertIs<LoanSummary.Overdue>(s)
        assertEquals(2, o.count)
        assertEquals("Най-старата", o.first.title)
        assertEquals(7, o.daysOverdue)
        assertEquals(3, o.total)
    }

    @Test fun withoutDatesOnlyTheCount() {
        assertEquals(LoanSummary.NoDueDate(2), LoanSummary.of(listOf(loan("1", null), loan("2", "не е дата")), today))
    }
}
