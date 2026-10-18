package org.chyavorec.app.data

import android.app.Application
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.TestResources
import org.chyavorec.app.data.local.CatalogDatabase
import org.chyavorec.app.data.local.RoomCatalogIndexStore
import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.CatalogIndexRows
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.FtsCatalogSearcher
import org.chyavorec.data.catalog.KatalogParser
import org.chyavorec.data.catalog.ReadingRecommender
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.SearchField
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Заявките на Room към индекса на каталога дават същото като индекса в паметта. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class CatalogIndexStoreTest {
    private lateinit var db: CatalogDatabase
    private val extra = listOf(
        CatalogBook(1, "Вазов, Иван", "Под игото", year = "1894", docType = "книга", udc = "886.7", available = true, annotation = "Роман.", isbn = "978-954-09-0123-4"),
        CatalogBook(2, "Вазов, Иван", "Чичовци", year = "1885", docType = "книга", udc = "886.7", available = false),
        CatalogBook(11, "Вазов, Иван", "Под игото", year = "2005", docType = "книга", udc = "886.7", available = true, isbn = "9789540901234"),
        CatalogBook(3, "Елин Пелин", "Гераците", udc = "886.7-32", available = true, registeredOn = "2023-01-01"),
        CatalogBook(4, "Smith, John", "Atlas of Europe", udc = "912", available = true, isbn = "978-0-00-000000-2"),
        CatalogBook(5, "", "Вестник Яворец", available = true),
    )
    private val sample = KatalogParser.parse(TestResources.text("katalog-sample.json"))
    private val snapshot = CatalogSnapshot("Б", "Яворец", "2026-09-19", extra + sample.books, emptyList())
    private val engine = CatalogSearchEngine(snapshot)
    private val state = CatalogIndexState("h1", snapshot.generatedOn, snapshot.books.size, CatalogSearchEngine.facetsOf(snapshot))

    @Before fun open() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), CatalogDatabase::class.java).build()
    }

    @After fun close() = db.close()

    @Test fun lookupsAndSearchMatchTheInMemoryEngine() = runBlocking {
        val store = RoomCatalogIndexStore { db }
        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals(state, store.state())
        // Без резервен път: грешка в SQL трябва да се види.
        val fts = FtsCatalogSearcher(store, state, fallback = { null }, work = Dispatchers.Default)

        for (b in snapshot.books) {
            assertEquals(engine.book(b.inv), fts.book(b.inv))
            assertEquals(engine.copiesOf(b), fts.copiesOf(b))
            assertEquals(engine.byAuthor(b.author, b.inv), fts.byAuthor(b.author, b.inv))
        }
        assertNull(fts.book(-1))
        val invs = listOf(1L, 11L, 4L, -1L) + sample.books.map { it.inv }
        assertEquals(engine.books(invs), fts.books(invs))
        for (raw in listOf("9789540901234", "978-954-09-0123-4", "9780000000002", "123", null)) {
            assertEquals(engine.findByIsbn(raw), fts.findByIsbn(raw), "ISBN $raw")
            assertEquals(engine.allByIsbn(raw), fts.allByIsbn(raw), "ISBN $raw")
        }
        for (text in listOf("вазов", "ва", "пелин", "smith", "zzz")) {
            val tokens = TextNormalizer.tokens(text)
            assertEquals(engine.authors(tokens, 8), fts.authors(tokens, 8), "автори $text")
        }
        val histories = listOf(
            listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван")),
            listOf(HistoryItem("h1", 4, "Atlas of Europe", "Smith, John"), HistoryItem("h2", sample.books.first().inv, "", "")),
            listOf(HistoryItem("h1", null, "Нещо", "Елин Пелин")),
        )
        // Кандидатите за препоръките се четат без анотацията — сравняват се книгите (инв. №) и редът.
        for (h in histories) assertEquals(ReadingRecommender.recommend(engine, h).map { it.inv }, fts.recommend(h).map { it.inv }, "история $h")
        assertTrue(fts.recommend(histories[0]).all { it.annotation.isEmpty() })

        for (text in listOf("", "под", "вазов игото", "джиан", "астир")) for (sort in CatalogSort.entries)
            assertEquals(engine.search(CatalogQuery(text, sort = sort)).map { it.inv }, fts.search(CatalogQuery(text, sort = sort)).map { it.inv }, "„$text“ $sort")
        assertEquals(engine.search(CatalogQuery("11", SearchField.INVENTORY)).map { it.inv }, fts.search(CatalogQuery("11", SearchField.INVENTORY)).map { it.inv })
        // Без текст — на страници от базата (ORDER BY/LIMIT), в същия ред като в паметта.
        for (q in listOf(CatalogQuery(), CatalogQuery(onlyAvailable = true), CatalogQuery(docType = "книга", yearFrom = 1900)))
            for (sort in CatalogSort.entries) {
                val query = q.copy(sort = sort)
                val expected = engine.search(query).map { it.inv }
                val first = fts.searchFirst(query, 7)
                assertEquals(expected.size, first.total, "брой $query")
                val all = first.books.map { it.inv }.toMutableList()
                while (all.size < first.total) all += fts.searchMore(query, all.size, 7).also { assertTrue(it.isNotEmpty()) }.map { it.inv }
                assertEquals(expected, all, "страници $query")
            }
        // Списъкът без текст не чете анотацията; книгата — да.
        assertEquals("", fts.search(CatalogQuery()).first { it.inv == 1L }.annotation)
        assertEquals("Роман.", fts.book(1)!!.annotation)
    }

    @Test fun replaceSwapsTheWholeIndex() = runBlocking {
        val store = RoomCatalogIndexStore { db }
        store.replace(state, CatalogIndexRows.build(snapshot))
        val smaller = snapshot.copy(books = extra.take(2))
        val next = CatalogIndexState("h2", "2026-10-01", 2, CatalogSearchEngine.facetsOf(smaller))
        store.replace(next, CatalogIndexRows.build(smaller))
        assertEquals(next, store.state())
        val fts = FtsCatalogSearcher(store, next, fallback = { null }, work = Dispatchers.Default)
        assertEquals(listOf(1L, 2L), fts.search(CatalogQuery()).map { it.inv }.sorted())
        assertEquals(listOf(1L), fts.search(CatalogQuery("игото")).map { it.inv })
        assertTrue(fts.search(CatalogQuery("джиан")).isEmpty(), "старите записи (и FTS) са изтрити")
        assertNull(fts.book(11))
    }

    /** „Изчисти кеша“: празен индекс (и FTS таблицата), после се попълва наново без грешка. */
    @Test fun clearEmptiesTheIndexAndItCanBeRefilled() = runBlocking {
        val store = RoomCatalogIndexStore { db }
        store.replace(state, CatalogIndexRows.build(snapshot))
        store.clear()
        assertNull(store.state())
        val fts = FtsCatalogSearcher(store, state, fallback = { null }, work = Dispatchers.Default)
        assertTrue(fts.search(CatalogQuery()).isEmpty())
        assertTrue(fts.search(CatalogQuery("вазов")).isEmpty(), "и FTS таблицата е празна")
        assertNull(store.book(1))
        assertTrue(store.authors().isEmpty())

        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals(state, store.state())
        assertEquals("Под игото", store.book(1)?.title)
        assertEquals(engine.search(CatalogQuery("вазов")).map { it.inv }.sorted(), fts.search(CatalogQuery("вазов")).map { it.inv }.sorted())
    }

    @Test fun brokenDatabaseIsRecreatedOnceAndTheIndexRebuilt() = runBlocking {
        var recreated = 0
        val store = RoomCatalogIndexStore(recreate = { recreated++; db }) { throw SQLiteException("file is not a database") }
        // Счупената база не стига до каталога — новата е празна и индексът се попълва отново.
        assertNull(store.state())
        assertEquals(1, recreated)
        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals(state, store.state())
        assertEquals(1, recreated)
    }

    @Test fun secondFailureReachesTheRepository() = runBlocking {
        var recreated = 0
        val store = RoomCatalogIndexStore(recreate = { recreated++; throw SQLiteException("пак") }) { throw SQLiteException("първо") }
        assertFailsWith<SQLiteException> { store.state() }
        // Само веднъж в процеса — иначе всяко търсене би изтривало базата наново.
        assertFailsWith<SQLiteException> { store.state() }
        assertEquals(1, recreated)
    }

    @Test fun corruptFileOnDiskDoesNotCrashAndIsRebuilt() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val file = context.getDatabasePath(CatalogDatabase.NAME)
        file.parentFile?.mkdirs()
        file.writeBytes(ByteArray(8192) { (it * 31 + 7).toByte() })
        val opened = ArrayList<CatalogDatabase>()
        val store = RoomCatalogIndexStore(recreate = { CatalogDatabase.recreate(context).also { opened += it } }) {
            CatalogDatabase.create(context).also { opened += it }
        }
        try {
            assertNull(store.state())
            store.replace(state, CatalogIndexRows.build(snapshot))
            assertEquals(state, store.state())
            assertEquals("Под игото", store.book(1)?.title)
        } finally {
            opened.forEach { runCatching { it.close() } }
            context.deleteDatabase(CatalogDatabase.NAME)
        }
    }
}
