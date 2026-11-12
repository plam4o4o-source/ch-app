package org.chyavorec.app.data

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.data.local.AppDatabase
import org.chyavorec.app.data.local.DiaryBook
import org.chyavorec.app.data.local.ReadingDiaryStore
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.ReadingDiary
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Читателски дневник“ в базата: оценка, бележка, „Не сега“, търсене по ключ или инв. №, изчистване. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ReadingDiaryDaoTest {
    private val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Application>(), AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    private val clock = FixedClock(Instant.parse("2026-11-12T10:00:00Z"))
    private val store = ReadingDiaryStore(db.readingDiary(), clock)
    private val book = DiaryBook("Под игото", "Вазов, Иван", 7L, 2026)

    @After fun close() = db.close()

    @Test fun ratingKeepsTheNoteAndNoteKeepsTheRating() = runBlocking<Unit> {
        store.rate(book, 4)
        store.setNote(book, "  Много  хубава\nкнига ")
        val e = db.readingDiary().all().single()
        assertEquals(4, e.rating)
        assertEquals("Много хубава книга", e.note)
        assertEquals(clock.now().toEpochMilli(), e.ratedAtMillis)
        store.rate(book, 5)
        assertEquals("Много хубава книга", db.readingDiary().all().single().note, "нова оценка не пипа бележката")
        assertEquals(5, db.readingDiary().all().single().rating)
    }

    @Test fun noteIsCutAt140Characters() = runBlocking<Unit> {
        store.rate(book, 3)
        store.setNote(book, "я".repeat(300))
        assertEquals(ReadingDiary.MAX_NOTE, db.readingDiary().all().single().note.length)
    }

    @Test fun dismissingStoresNoRatingAndOnlyOnce() = runBlocking<Unit> {
        store.dismiss(book)
        val e = db.readingDiary().all().single()
        assertEquals(0, e.rating)
        assertTrue(store.ratings().isEmpty(), "„Не сега“ не е оценка")
        // Оценка след „Не сега“ е възможна (от страницата на книгата).
        store.rate(book, 2)
        assertEquals(mapOf(book.key to 2), store.ratings())
    }

    @Test fun anEntryIsFoundByKeyOrByInventoryNumber() = runBlocking<Unit> {
        store.rate(book, 5)
        assertEquals(5, store.observeFor(book).first()?.rating)
        // Друго заглавие в историята, но същият екземпляр.
        assertEquals(5, store.observeFor(DiaryBook("Под игото (изд. 2)", "Вазов", 7L, null)).first()?.rating)
        assertNull(store.observeFor(DiaryBook("Чичовци", "Вазов", 8L, null)).first())
    }

    @Test fun removeAndSignOutClearTheDiary() = runBlocking<Unit> {
        store.rate(book, 5)
        store.rate(DiaryBook("Чичовци", "Вазов", 8L, 2026), 3)
        store.remove(book)
        assertEquals(1, db.readingDiary().all().size)
        // Изходът (AppContainer.clearReaderTraces) вика clear().
        db.readingDiary().clear()
        assertTrue(db.readingDiary().all().isEmpty())
    }
}
