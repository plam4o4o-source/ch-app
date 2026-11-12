package org.chyavorec.domain

import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.ReturnStamps
import org.chyavorec.domain.model.ReturnStamps.State
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Печатът „Върната“: празнува се веднъж, само потвърдено от историята, ограничено и без фалшиви печати. */
class ReturnStampsTest {
    private val today = LocalDate.of(2026, 11, 12)

    private fun loan(id: String, inv: Long? = id.removePrefix("L").toLongOrNull(), title: String = "Книга $id") =
        Loan(loanId = id, inv = inv, title = title, author = "Автор")

    private fun back(id: String, on: LocalDate = today.minusDays(1), inv: Long? = id.removePrefix("L").toLongOrNull()) =
        HistoryItem(loanId = id, inv = inv, title = "Книга $id", author = "Автор", dateOut = "2026-10-01", dateIn = on.toString())

    private fun open(id: String) = back(id).copy(dateIn = null)

    /** Състоянието след отваряне, на което са били заети [ids]. */
    private fun seen(vararg ids: String): State = ReturnStamps.step(State(), ids.map { loan(it) }, null, today).state

    @Test fun firstOpeningOnlyRemembers() {
        val r = ReturnStamps.step(State(), listOf(loan("L1")), listOf(back("L9")), today)
        assertTrue(r.celebrate.isEmpty())
        assertEquals(setOf("L1"), r.state.known)
    }

    @Test fun returnedBookConfirmedByHistoryIsCelebratedOnce() {
        val s0 = seen("L1", "L2")
        val r = ReturnStamps.step(s0, listOf(loan("L2")), listOf(back("L1"), open("L2")), today)
        assertEquals(listOf("L1"), r.celebrate.map { it.loanId })
        assertTrue(r.state.pending.isEmpty())
        assertTrue("L1" in r.state.done)
        // Следващото отваряне — нищо повече (дори историята още да я съдържа).
        val again = ReturnStamps.step(r.state, listOf(loan("L2")), listOf(back("L1")), today)
        assertTrue(again.celebrate.isEmpty())
        // …и дори състоянието „известни“ да е от преди (процесът е умрял преди запис на известните).
        val replay = ReturnStamps.step(r.state.copy(known = setOf("L1", "L2")), listOf(loan("L2")), listOf(back("L1")), today)
        assertTrue(replay.celebrate.isEmpty())
    }

    @Test fun failedBookFetchChangesNothing() {
        val s0 = seen("L1")
        val r = ReturnStamps.step(s0, null, listOf(back("L1")), today)
        assertEquals(s0, r.state)
        assertTrue(r.celebrate.isEmpty())
    }

    @Test fun missingOrUnconfirmedHistoryWaitsInQueue() {
        val s0 = seen("L1")
        // Историята не е достъпна → опашка, без печат.
        val waiting = ReturnStamps.step(s0, emptyList(), null, today)
        assertTrue(waiting.celebrate.isEmpty())
        assertEquals(setOf("L1"), waiting.state.pending.keys)
        // Историята още я води като отворена → още чака.
        val stillOpen = ReturnStamps.step(waiting.state, emptyList(), listOf(open("L1")), today)
        assertTrue(stillOpen.celebrate.isEmpty())
        assertEquals(setOf("L1"), stillOpen.state.pending.keys)
        // Потвърдена по-късно → печат.
        val later = ReturnStamps.step(stillOpen.state, emptyList(), listOf(back("L1")), today.plusDays(2))
        assertEquals(listOf("L1"), later.celebrate.map { it.loanId })
    }

    @Test fun unconfirmedEntryExpires() {
        val waiting = ReturnStamps.step(seen("L1"), emptyList(), null, today)
        val late = ReturnStamps.step(waiting.state, emptyList(), listOf(back("L1", on = today)), today.plusDays(ReturnStamps.PENDING_DAYS + 1))
        assertTrue(late.celebrate.isEmpty())
        assertTrue(late.state.pending.isEmpty())
    }

    @Test fun renewalIsNeverCelebrated() {
        // Същият идентификатор още е в списъка (удължена книга).
        val s0 = seen("L1")
        val same = ReturnStamps.step(s0, listOf(loan("L1")), listOf(back("L1")), today)
        assertTrue(same.celebrate.isEmpty())
        // Удължаването е сменило идентификатора, но книгата (инвентарният номер) е същата.
        val renewed = ReturnStamps.step(s0, listOf(loan("L1b", inv = 1)), listOf(back("L1", inv = 1)), today)
        assertTrue(renewed.celebrate.isEmpty())
        assertTrue(renewed.state.pending.isEmpty())
        // Без инвентарен номер — по заглавие и автор.
        val noInv = ReturnStamps.step(
            State(known = setOf("X")), listOf(loan("Y", inv = null, title = "Книга X")),
            listOf(back("X", inv = null).copy(title = "Книга X")), today,
        )
        assertTrue(noInv.celebrate.isEmpty())
    }

    @Test fun idThatComesBackLeavesTheQueue() {
        val waiting = ReturnStamps.step(seen("L1"), emptyList(), null, today)
        val back = ReturnStamps.step(waiting.state, listOf(loan("L1")), listOf(back("L1")), today)
        assertTrue(back.celebrate.isEmpty())
        assertTrue(back.state.pending.isEmpty())
    }

    @Test fun atMostThreePerOpeningTheRestQueue() {
        val ids = (1..5).map { "L$it" }
        val s0 = seen(*ids.toTypedArray())
        val history = ids.map { back(it, on = today.minusDays(6L - it.removePrefix("L").toLong())) }
        val first = ReturnStamps.step(s0, emptyList(), history, today)
        assertEquals(ReturnStamps.MAX_PER_OPENING, first.celebrate.size)
        // Най-старото връщане първо.
        assertEquals(listOf("L1", "L2", "L3"), first.celebrate.map { it.loanId })
        assertEquals(setOf("L4", "L5"), first.state.pending.keys)
        val second = ReturnStamps.step(first.state, emptyList(), history, today)
        assertEquals(listOf("L4", "L5"), second.celebrate.map { it.loanId })
        assertTrue(second.state.pending.isEmpty())
    }

    @Test fun oldReturnIsSkippedSilently() {
        val r = ReturnStamps.step(seen("L1"), emptyList(), listOf(back("L1", on = today.minusDays(ReturnStamps.RECENT_DAYS + 1))), today)
        assertTrue(r.celebrate.isEmpty())
        assertTrue(r.state.pending.isEmpty())
    }

    @Test fun doneSetIsBoundedByCountAndAge() {
        var state = State()
        // 130 отпразнувани за 130 различни дни → пази се най-много 100, а по-старите от 120 дни падат.
        for (i in 0 until 130) {
            val day = today.minusDays(130L - i)
            val id = "B$i"
            state = ReturnStamps.step(state, listOf(loan(id)), null, day).state
            state = ReturnStamps.step(state, emptyList(), listOf(back(id, on = day)), day).state
        }
        assertTrue(state.done.size <= 100, "done=${state.done.size}")
        assertNotNull(state.done["B129"])
        assertTrue(state.done.values.all { today.toEpochDay() - it <= 121 })
    }

    @Test fun encodingRoundTripsEvenWithSeparatorInId() {
        val map = mapOf("a|b" to 20_000L, "L2" to 20_001L)
        assertEquals(map, ReturnStamps.decode(ReturnStamps.encode(map)))
        // Повредените записи се пропускат.
        assertEquals(mapOf("ok" to 5L), ReturnStamps.decode(setOf("ok|5", "bad", "x|y", "|7")))
    }

    @Test fun yearCounterCountsReturnsOfThatYearOncePerLoan() {
        val history = listOf(
            back("L1", on = LocalDate.of(2026, 3, 1)), back("L1", on = LocalDate.of(2026, 3, 1)),
            back("L2", on = LocalDate.of(2025, 12, 30)), open("L3"), back("L4", on = LocalDate.of(2026, 11, 11)),
        )
        assertEquals(2, ReturnStamps.returnedInYear(history, 2026))
        assertEquals(1, ReturnStamps.returnedInYear(history, 2025))
    }
}
