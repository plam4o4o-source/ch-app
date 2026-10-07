package org.chyavorec.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.EventsService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class EventsRepositoryTest {
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))

    private fun ev(id: String, date: String?, recurring: Boolean = false, time: String? = null) =
        Event(id = id, title = id, date = date, time = time, sourceUrl = "https://example.org/$id", recurring = recurring)

    private class FakeEvents(val list: List<Event>) : EventsService {
        override suspend fun fetchEvents(): Outcome<List<Event>> = Outcome.Success(list)
    }

    @Test fun rolledRecurringEventMovesToItsChronologicalPlace() = runTest {
        // Годишен календар: януарският и мартенският празник са минали и се
        // превъртат към 2027 — трябва да застанат след декемврийското събитие.
        val source = listOf(
            ev("baba-marta", "2026-03-01", recurring = true),
            ev("new-year", "2026-01-01", recurring = true),
            ev("concert", "2026-11-05"),
            ev("christmas", "2026-12-24", recurring = true),
            ev("today-late", "2026-09-26", time = "18:00"),
            ev("today-early", "2026-09-26", time = "10:00"),
        )
        val repo = EventsRepository(FakeEvents(source), InMemoryPayloadCache(), clock, Dispatchers.Unconfined)

        val rolled = (repo.events(force = true) as Outcome.Success).value.data
        val upcoming = repo.upcoming(rolled)
        assertEquals(
            listOf("today-early", "today-late", "concert", "christmas", "new-year", "baba-marta"),
            upcoming.map { it.id },
        )
        assertEquals("2027-01-01", upcoming.first { it.id == "new-year" }.date)
        assertEquals(upcoming.map { it.id }, repo.upcoming(repo.cachedRolled()!!.data).map { it.id })
    }

    @Test fun upcomingSortsEvenUnsortedInput() {
        val repo = EventsRepository(FakeEvents(emptyList()), InMemoryPayloadCache(), clock, Dispatchers.Unconfined)
        val out = repo.upcoming(listOf(ev("b", "2026-12-01"), ev("past", "2026-01-01"), ev("a", "2026-10-01")))
        assertEquals(listOf("a", "b"), out.map { it.id })
    }
}
