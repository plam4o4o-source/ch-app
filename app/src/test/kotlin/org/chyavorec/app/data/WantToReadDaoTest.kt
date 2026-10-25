package org.chyavorec.app.data

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.data.local.AppDatabase
import org.chyavorec.app.data.local.WantToReadEntity
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Искам да прочета“ в базата: ред, „Кажи ми“, наличността, махане и изчистване. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class WantToReadDaoTest {
    private val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Application>(), AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    private val dao = db.wantToRead()

    @After fun close() = db.close()

    private fun book(inv: Long, added: Long, notify: Boolean = false, available: Boolean = false) =
        WantToReadEntity(inv, "Книга $inv", "Автор $inv", added, notify, available)

    @Test fun newestFirstAndCounted() = runBlocking<Unit> {
        dao.upsert(book(1, added = 10))
        dao.upsert(book(2, added = 30))
        dao.upsert(book(3, added = 20))
        assertEquals(listOf(2L, 3L, 1L), dao.observeAll().first().map { it.inv })
        assertEquals(3, dao.observeCount().first())
        assertEquals("Книга 3", dao.observe(3).first()?.title)
        assertNull(dao.observe(99).first())
    }

    @Test fun notifyFlagDecidesTheBackgroundCheck() = runBlocking<Unit> {
        assertFalse(dao.anyNotify())
        dao.upsert(book(1, added = 1))
        assertFalse(dao.anyNotify(), "без „Кажи ми“ каталогът не се проверява във фонов режим")
        dao.upsert(book(1, added = 1).copy(notifyWhenAvailable = true))
        assertTrue(dao.anyNotify())
        assertEquals(1, dao.all().size, "същата книга — един запис")
    }

    @Test fun availabilityIsUpdatedInPlace() = runBlocking<Unit> {
        dao.upsert(book(1, added = 5, notify = true, available = false))
        dao.setAvailable(1, true)
        val saved = dao.get(1)!!
        assertTrue(saved.lastKnownAvailable)
        assertTrue(saved.notifyWhenAvailable)
        assertEquals(5L, saved.addedAtMillis)
    }

    @Test fun removeRestoreAndClear() = runBlocking<Unit> {
        val first = book(1, added = 5, notify = true)
        dao.upsert(first)
        dao.upsert(book(2, added = 6))
        dao.delete(1)
        assertNull(dao.get(1))
        // „Отмени“ — записът се връща точно какъвто беше.
        dao.upsert(first)
        assertEquals(first, dao.get(1))
        dao.clear()
        assertTrue(dao.all().isEmpty())
        assertEquals(0, dao.observeCount().first())
    }
}
