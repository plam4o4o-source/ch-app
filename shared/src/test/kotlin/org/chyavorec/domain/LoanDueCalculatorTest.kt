package org.chyavorec.domain

import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.MembershipEvaluator
import org.chyavorec.domain.model.MembershipStatus
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoanDueCalculatorTest {
    private val calc = LoanDueCalculator(dueSoonDays = 3)
    private val today = LocalDate.of(2026, 9, 26)

    @Test fun plentyOfTime() = assertEquals(DueStatus.PLENTY_OF_TIME, calc.status(LocalDate.of(2026, 10, 3), today))
    @Test fun dueSoonBoundary() = assertEquals(DueStatus.DUE_SOON, calc.status(LocalDate.of(2026, 9, 29), today))
    @Test fun dueToday() = assertEquals(DueStatus.DUE_SOON, calc.status(today, today))
    @Test fun overdue() = assertEquals(DueStatus.OVERDUE, calc.status(LocalDate.of(2026, 9, 25), today))
    @Test fun daysLeftNegativeWhenOverdue() = assertEquals(-6, calc.daysLeft(LocalDate.of(2026, 9, 20), today))

    @Test fun fromLoanWithIsoDateTime() {
        val loan = Loan("1", 1, "Под игото", "Иван Вазов", null, "2026-09-12", "2026-10-03T00:00:00")
        assertEquals(DueStatus.PLENTY_OF_TIME, calc.status(loan, today))
    }

    @Test fun invalidDueDate() {
        val loan = Loan("1", 1, "x", "y", null, "2026-09-12", "не е дата")
        assertNull(calc.status(loan, today))
    }

    @Test fun missingDueDate() {
        val loan = Loan("1", 1, "x", "y", null, borrowedOn = null, dueOn = null)
        assertNull(calc.status(loan, today))
    }

    @Test fun elapsedFraction() {
        assertEquals(0.5f, calc.elapsedFraction(LocalDate.of(2026, 9, 16), LocalDate.of(2026, 10, 6), today))
    }

    @Test fun membershipExpiresByDate() {
        val m = Membership("7", "Читател", validUntil = "2026-09-01", status = MembershipStatus.ACTIVE)
        assertEquals(MembershipStatus.EXPIRED, MembershipEvaluator.effectiveStatus(m, today))
        assertEquals(MembershipStatus.ACTIVE, MembershipEvaluator.effectiveStatus(m.copy(validUntil = "2027-01-01"), today))
        assertEquals(MembershipStatus.SUSPENDED, MembershipEvaluator.effectiveStatus(m.copy(status = MembershipStatus.SUSPENDED), today))
    }
}
