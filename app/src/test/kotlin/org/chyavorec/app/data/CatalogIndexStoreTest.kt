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
import org.chyavorec.app.data.local.FTS_SYNC_TRIGGERS
import org.chyavorec.app.data.local.RoomCatalogIndexStore
import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.CatalogIndexLostException
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
                while (all.size < first.total) all += fts.searchMore(query, all.size, 7)!!.also { assertTrue(it.isNotEmpty()) }.map { it.inv }
                assertEquals(expected, all, "страници $query")
            }
        // С текст — точкуват се леките записи, книгите се четат само за страницата (същият ред).
        for (text in listOf("под", "вазов", "астир", "игото")) for (sort in CatalogSort.entries) {
            val query = CatalogQuery(text, sort = sort)
            val expected = engine.search(query)
            val first = fts.searchFirst(query, 2)
            assertEquals(expected.size, first.total, "брой $query")
            assertEquals(expected.take(2), first.books, "първа страница $query")
            assertEquals(expected.drop(2).take(2), fts.searchMore(query, 2, 2), "следваща страница $query")
        }
        assertEquals(listOf(11L, 1L), store.booksAt(listOf(2, 0, 99_999)).map { it.inv }, "в подадения ред, без липсващите")
        val hit = store.hits(org.chyavorec.data.catalog.CatalogIndexQueries.search(CatalogQuery("игото"), listOf("игото"), "")!!).first()
        assertEquals(CatalogIndexRows.build(snapshot)[hit.ord].toHit(), hit)
        // Списъкът без текст не чете анотацията; книгата — да.
        assertEquals("", fts.search(CatalogQuery()).first { it.inv == 1L }.annotation)
        assertEquals("Роман.", fts.book(1)!!.annotation)
    }

    private fun CatalogDatabase.plan(sql: String, args: List<Any>): String =
        openHelper.writableDatabase.query("EXPLAIN QUERY PLAN $sql", args.toTypedArray()).use { c ->
            buildList { while (c.moveToNext()) add(c.getString(c.getColumnIndexOrThrow("detail"))) }.joinToString(" | ")
        }

    /** Подредените страници без текст (заглавие, автор, „Най-нови“) четат индекс наред — без подреждане на целия каталог. */
    @Test fun sortedPagesUseAnIndexInsteadOfSortingTheCatalogue() = runBlocking {
        RoomCatalogIndexStore { db }.replace(state, CatalogIndexRows.build(snapshot))
        for (sort in listOf(CatalogSort.RELEVANCE, CatalogSort.TITLE, CatalogSort.AUTHOR, CatalogSort.NEWEST))
            for (q in listOf(CatalogQuery(sort = sort), CatalogQuery(sort = sort, onlyAvailable = true))) {
                val page = org.chyavorec.data.catalog.CatalogIndexQueries.page(q, offset = 40, limit = 40)
                val plan = db.plan(page.sql, page.args)
                assertTrue("USING INDEX" in plan && "TEMP B-TREE" !in plan, "$sort: $plan")
            }
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

    /**
     * Повредена база при справка: новата (празна) база не е празен каталог — справките
     * хвърлят, докато индексът не се попълни отново, а търсачката минава през индекса в паметта.
     */
    @Test fun lookupsOnTheRecreatedDatabaseReportTheLostIndex() = runBlocking {
        var recreated = 0
        val store = RoomCatalogIndexStore(recreate = { recreated++; db }) { throw SQLiteException("file is not a database") }
        assertFailsWith<CatalogIndexLostException> { store.book(1) }
        assertEquals(1, recreated)
        assertFailsWith<CatalogIndexLostException> { store.authors() }
        assertNull(store.state(), "индекс няма — попълва се наново")

        var lost = 0
        val fts = FtsCatalogSearcher(store, state, fallback = { engine }, work = Dispatchers.Default, onIndexLost = { lost++ })
        assertEquals(engine.search(CatalogQuery("вазов")).map { it.inv }, fts.search(CatalogQuery("вазов")).map { it.inv })
        assertEquals("Под игото", fts.book(1)?.title)
        assertEquals(2, lost)

        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals("Под игото", store.book(1)?.title)
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

    private fun CatalogDatabase.triggers(): Map<String, String> =
        openHelper.writableDatabase.query("SELECT name, sql FROM sqlite_master WHERE type = 'trigger' ORDER BY name").use { c ->
            buildMap { while (c.moveToNext()) put(c.getString(0), c.getString(1)) }
        }

    private fun CatalogDatabase.ftsCount(token: String): Int =
        openHelper.writableDatabase.query("SELECT COUNT(*) FROM catalog_fts WHERE catalog_fts MATCH ?", arrayOf<Any>(token)).use { c ->
            c.moveToFirst()
            c.getInt(0)
        }

    /**
     * Бързото попълване маха тригерите на Room за FTS таблицата само за времето на
     * транзакцията: после те са същите (SQL като в схемата) и пак синхронизират индекса,
     * а пълнотекстовият индекс (строен с 'rebuild') отговаря на съдържанието.
     */
    @Test fun replaceKeepsRoomTriggersAndTheFtsIndexConsistent() = runBlocking {
        val schema = org.json.JSONObject(java.io.File("schemas/org.chyavorec.app.data.local.CatalogDatabase/3.json").readText())
        val entities = schema.getJSONObject("database").getJSONArray("entities")
        val expected = (0 until entities.length()).map { entities.getJSONObject(it) }
            .first { it.getString("tableName") == "catalog_fts" }.getJSONArray("contentSyncTriggers")
            .let { a -> (0 until a.length()).map { a.getString(it) } }
        assertEquals(expected.sorted(), FTS_SYNC_TRIGGERS.values.sorted(), "тригерите — точно като в схемата на Room")

        val before = db.triggers()
        assertEquals(FTS_SYNC_TRIGGERS.keys.sorted(), before.keys.filter { it.startsWith("room_fts_") }.sorted())
        val store = RoomCatalogIndexStore { db }
        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals(before, db.triggers(), "тригерите са възстановени")
        store.replace(state, CatalogIndexRows.build(snapshot))
        assertEquals(before, db.triggers())
        db.openHelper.writableDatabase.execSQL("INSERT INTO catalog_fts(catalog_fts) VALUES('integrity-check')")
        assertEquals(2, db.ftsCount("игото*"), "„Под игото“ — инв. № 1 и 11")

        // Тригерите пак работят: нов ред влиза в пълнотекстовия индекс, изтритият — излиза.
        val extraRow = CatalogIndexRows.build(snapshot.copy(books = listOf(CatalogBook(777, "Тестов, Автор", "Уникалнозаглавие", available = true))))
            .single().copy(ord = 100_000)
        db.index().insertBooks(listOf(org.chyavorec.app.data.local.CatalogBookEntity.of(extraRow)))
        assertEquals(1, db.ftsCount("уникалнозаглавие"))
        db.openHelper.writableDatabase.execSQL("DELETE FROM catalog_books WHERE ord = 100000")
        assertEquals(0, db.ftsCount("уникалнозаглавие"))

        store.clear()
        assertEquals(before, db.triggers())
        assertEquals(0, db.ftsCount("игото*"), "изчистеният индекс е празен")
    }

    /** Базата на диска се отваря отново от Room (проверката на схемата/идентичността минава) — с целия индекс. */
    @Test fun replacedIndexReopensCleanly() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        context.deleteDatabase(CatalogDatabase.NAME)
        try {
            val first = CatalogDatabase.create(context)
            val triggers = first.triggers()
            RoomCatalogIndexStore { first }.replace(state, CatalogIndexRows.build(snapshot))
            first.close()

            val reopened = CatalogDatabase.create(context)
            try {
                val store = RoomCatalogIndexStore { reopened }
                assertEquals(state, store.state())
                assertEquals(triggers, reopened.triggers())
                val fts = FtsCatalogSearcher(store, state, fallback = { null }, work = Dispatchers.Default)
                assertEquals(engine.search(CatalogQuery("вазов")).map { it.inv }, fts.search(CatalogQuery("вазов")).map { it.inv })
                assertEquals("Под игото", fts.book(1)?.title)
            } finally {
                reopened.close()
            }
        } finally {
            context.deleteDatabase(CatalogDatabase.NAME)
        }
    }
}
