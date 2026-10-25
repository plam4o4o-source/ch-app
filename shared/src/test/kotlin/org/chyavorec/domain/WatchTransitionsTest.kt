package org.chyavorec.domain

import org.chyavorec.domain.model.WatchTransitions
import org.chyavorec.domain.model.WatchedBook
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** „Кажи ми, когато е на рафта“: известие само при преход към „на рафта“, веднъж на преход. */
class WatchTransitionsTest {
    private fun w(inv: Long, notify: Boolean = true, last: Boolean = false) = WatchedBook(inv, notify, last)

    @Test fun returnedBookWithNotifyFlagIsAnnounced() {
        val r = WatchTransitions.check(listOf(w(1), w(2)), mapOf(1L to true, 2L to false))
        assertEquals(mapOf(1L to true), r.changed)
        assertEquals(listOf(1L), r.becameAvailable)
    }

    @Test fun withoutNotifyFlagOnlyTheStateIsUpdated() {
        val r = WatchTransitions.check(listOf(w(1, notify = false)), mapOf(1L to true))
        assertEquals(mapOf(1L to true), r.changed)
        assertTrue(r.becameAvailable.isEmpty())
    }

    @Test fun sameStateTwiceIsAnnouncedOnce() {
        val first = WatchTransitions.check(listOf(w(1)), mapOf(1L to true))
        assertEquals(listOf(1L), first.becameAvailable)
        // Записаната нова наличност → следващата проверка със същия каталог не известява.
        val second = WatchTransitions.check(listOf(w(1, last = true)), mapOf(1L to true))
        assertTrue(second.isEmpty)
        assertTrue(second.becameAvailable.isEmpty())
    }

    @Test fun borrowedAgainAndReturnedIsANewTransition() {
        val out = WatchTransitions.check(listOf(w(1, last = true)), mapOf(1L to false))
        assertEquals(mapOf(1L to false), out.changed)
        assertTrue(out.becameAvailable.isEmpty())
        val back = WatchTransitions.check(listOf(w(1, last = false)), mapOf(1L to true))
        assertEquals(listOf(1L), back.becameAvailable)
    }

    @Test fun bookMissingFromTheCatalogueKeepsItsState() {
        val r = WatchTransitions.check(listOf(w(1), w(2, last = true)), emptyMap())
        assertTrue(r.isEmpty)
        assertTrue(r.becameAvailable.isEmpty())
    }

    @Test fun quietHoursPostponeOnlyTheAnnouncements() {
        val watched = listOf(w(1), w(2, notify = false), w(3, last = true))
        val night = WatchTransitions.check(watched, mapOf(1L to true, 2L to true, 3L to false), defer = true)
        // Книгата с „Кажи ми“ остава „не е на рафта“ — известието е за сутринта; другите се записват.
        assertEquals(mapOf(2L to true, 3L to false), night.changed)
        assertTrue(night.becameAvailable.isEmpty())
        val morning = WatchTransitions.check(listOf(w(1)), mapOf(1L to true))
        assertEquals(listOf(1L), morning.becameAvailable)
    }
}
