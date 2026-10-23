package org.chyavorec.domain

import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.RenewOutcomes
import org.chyavorec.domain.model.RenewOutcomes.Kind
import org.chyavorec.domain.model.RenewResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RenewOutcomesTest {
    private fun loan(
        id: String = "L1",
        due: String? = "2026-10-21",
        pending: Boolean = false,
        result: RenewResult? = null,
    ) = Loan(id, 7, "Под игото", "Иван Вазов", dueOn = due, canRenew = true, renewPending = pending, renewResult = result)

    private val rejected = RenewResult(RenewResult.STATUS_REJECTED, "Книгата е запазена от друг читател.", "2026-10-18T09:00:00Z")
    private val done = RenewResult(RenewResult.STATUS_DONE, null, "2026-10-18T09:00:00Z")

    /** Заявено удължаване, видяно от предишната проверка. */
    private val watching = RenewOutcomes.diff(emptySet(), emptySet(), listOf(loan(pending = true)))

    @Test fun pendingLoanIsWatchedWithDueAtRequest() {
        assertEquals(setOf("L1|2026-10-21"), watching.watch)
        assertTrue(watching.outcomes.isEmpty())
    }

    @Test fun approvedWhenDueDateMoves() {
        val r = RenewOutcomes.diff(watching.watch, watching.notified, listOf(loan(due = "2026-11-12", result = done)))
        val o = r.outcomes.single()
        assertEquals(Kind.APPROVED, o.kind)
        assertEquals("2026-11-12", o.loan.dueOn)
        assertTrue(r.watch.isEmpty())
        assertTrue(o.key in r.notified)
    }

    @Test fun approvedWithoutResultWhenDueDateMoves() {
        val r = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(due = "2026-11-12")))
        assertEquals(Kind.APPROVED, r.outcomes.single().kind)
    }

    @Test fun rejectedWithReason() {
        val r = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(result = rejected)))
        val o = r.outcomes.single()
        assertEquals(Kind.REJECTED, o.kind)
        assertEquals("Книгата е запазена от друг читател.", o.loan.renewResult?.reason)
        assertTrue(r.watch.isEmpty())
    }

    @Test fun doneButOldDueWaitsForTheNewDate() {
        val r = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(result = done)))
        assertTrue(r.outcomes.isEmpty())
        assertEquals(watching.watch, r.watch)
        // Новият срок идва с по-късната снимка → едва тогава известие.
        val later = RenewOutcomes.diff(r.watch, r.notified, listOf(loan(due = "2026-11-12", result = done)))
        assertEquals(Kind.APPROVED, later.outcomes.single().kind)
    }

    @Test fun eachOutcomeOnlyOnce() {
        val first = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(result = rejected)))
        assertEquals(1, first.outcomes.size)
        // Следващата проверка (заемането вече не е наблюдавано) — нищо.
        val again = RenewOutcomes.diff(first.watch, first.notified, listOf(loan(result = rejected)))
        assertTrue(again.outcomes.isEmpty())
        // Едновременна проверка от старото състояние (напр. екранът и фоновата задача) — пак нищо.
        val race = RenewOutcomes.diff(watching.watch, first.notified, listOf(loan(result = rejected)))
        assertTrue(race.outcomes.isEmpty())
    }

    @Test fun unknownWatchOnlyRemembersPending() {
        // Първо пускане/нов вход: стар отговор (до 7 дни на сървъра) не е известие.
        val r = RenewOutcomes.diff(null, emptySet(), listOf(loan(result = rejected), loan(id = "L2", pending = true)))
        assertTrue(r.outcomes.isEmpty())
        assertEquals(setOf("L2|2026-10-21"), r.watch)
    }

    @Test fun notWatchedLoanWithResultIsIgnored() {
        val r = RenewOutcomes.diff(emptySet(), emptySet(), listOf(loan(result = rejected)))
        assertTrue(r.outcomes.isEmpty())
    }

    @Test fun expiredRequestIsForgottenSilently() {
        val r = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan()))
        assertTrue(r.outcomes.isEmpty())
        assertTrue(r.watch.isEmpty())
    }

    @Test fun returnedBookIsForgotten() {
        val r = RenewOutcomes.diff(watching.watch, setOf("L1|rejected|x"), emptyList())
        assertTrue(r.outcomes.isEmpty())
        assertTrue(r.watch.isEmpty())
        assertTrue(r.notified.isEmpty())
    }

    @Test fun deferKeepsTheOutcomeForLater() {
        val night = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(result = rejected)), defer = true)
        assertTrue(night.outcomes.isEmpty())
        assertEquals(watching.watch, night.watch)
        val morning = RenewOutcomes.diff(night.watch, night.notified, listOf(loan(result = rejected)))
        assertEquals(Kind.REJECTED, morning.outcomes.single().kind)
    }

    @Test fun loanIdWithSeparator() {
        val w = RenewOutcomes.diff(emptySet(), emptySet(), listOf(loan(id = "A|B", pending = true)))
        val r = RenewOutcomes.diff(w.watch, emptySet(), listOf(loan(id = "A|B", due = "2026-11-12")))
        assertEquals("A|B", r.outcomes.single().loan.loanId)
        assertTrue(r.notified.single().startsWith("A|B|"))
    }

    @Test fun dueAtRequestSurvivesWhilePending() {
        // Срокът се смени, докато заявката още чака (напр. удължено на гишето) — сравнява се с първоначалния.
        val still = RenewOutcomes.diff(watching.watch, emptySet(), listOf(loan(due = "2026-11-01", pending = true)))
        assertEquals(setOf("L1|2026-10-21"), still.watch)
    }
}
