package org.chyavorec.app.data

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    @Test fun noteThenRatingKeepsBoth() = runBlocking<Unit> {
        // Бележка върху книга, която още няма запис: оценката е „пропусната“ (0), после се дописва.
        store.setNote(book, "Хубава")
        assertEquals(0, db.readingDiary().all().single().rating)
        store.rate(book, 3)
        val e = db.readingDiary().all().single()
        assertEquals(3, e.rating)
        assertEquals("Хубава", e.note)
        assertEquals(7L, e.inv)
        assertEquals(2026, e.readYear)
    }

    /** Оценка и бележка, пуснати едновременно от различни нишки, не си пренаписват колоните. */
    @Test fun ratingAndNoteSavedTogetherNeverOverwriteEachOther() = runBlocking<Unit> {
        repeat(40) { round ->
            val b = DiaryBook("Книга $round", "Автор", round + 100L, 2026)
            val jobs = listOf(
                async(Dispatchers.Default) { store.rate(b, 1 + round % 5) },
                async(Dispatchers.Default) { store.setNote(b, "бележка $round") },
                async(Dispatchers.Default) { store.setNote(b, "бележка $round") },
                async(Dispatchers.Default) { store.rate(b, 1 + round % 5) },
            )
            jobs.awaitAll()
            val e = db.readingDiary().get(b.key)!!
            assertEquals(1 + round % 5, e.rating, "оценка, кръг $round")
            assertEquals("бележка $round", e.note, "бележка, кръг $round")
        }
    }

    @Test fun writesTouchOnlyTheirOwnColumn() = runBlocking<Unit> {
        store.rate(book, 4)
        store.setNote(book, "Ред")
        // Нова оценка не пипа бележката; нова бележка не пипа оценката.
        store.rate(book, 2)
        assertEquals("Ред", db.readingDiary().all().single().note)
        store.setNote(book, "Друг ред")
        assertEquals(2, db.readingDiary().all().single().rating)
        // Без инв. № и година в новото повикване — старите се пазят.
        store.rate(DiaryBook(book.title, book.author, null, null), 5)
        val e = db.readingDiary().all().single()
        assertEquals(7L, e.inv)
        assertEquals(2026, e.readYear)
        assertEquals("Друг ред", e.note)
    }

    @Test fun dismissingAnAlreadyRatedBookKeepsRatingAndNote() = runBlocking<Unit> {
        store.rate(book, 5)
        store.setNote(book, "Любима")
        store.dismiss(book)
        val e = db.readingDiary().all().single()
        assertEquals(5, e.rating)
        assertEquals("Любима", e.note)
    }

    @Test fun aMissingRowIsCreatedByTheFirstWriteAndRemovedBooksStartOver() = runBlocking<Unit> {
        assertTrue(db.readingDiary().all().isEmpty())
        store.rate(book, 4)
        assertEquals(1, db.readingDiary().all().size)
        store.remove(book)
        assertTrue(db.readingDiary().all().isEmpty())
        store.setNote(book, "Пак")
        val e = db.readingDiary().all().single()
        assertEquals(0, e.rating)
        assertEquals("Пак", e.note)
        // Целенасочените заявки върху липсващ ред не създават нищо.
        db.readingDiary().updateRating("няма-такъв", 3, null, null, 1L)
        db.readingDiary().updateNote("няма-такъв", "x", null, null, 1L)
        assertEquals(1, db.readingDiary().all().size)
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
