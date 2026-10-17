package org.chyavorec.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.chyavorec.TestUtil
import org.chyavorec.data.catalog.CatalogIndexColumns
import org.chyavorec.data.catalog.CatalogIndexQueries
import org.chyavorec.data.catalog.CatalogIndexQuery
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Пълнотекстовото търсене (FTS кандидати + точно съвпадение/точкуване в Kotlin) дава
 * същите резултати в същия ред като индекса в паметта за типичните заявки.
 */
class CatalogFtsSearchTest {
    private val extra = listOf(
        CatalogBook(1, "Вазов, Иван", "Под игото", subtitle = "роман из живота на българите", year = "1894", docType = "книга", language = "български", udc = "886.7", department = "за възрастни", available = true, registeredOn = "2020-01-01", keywords = "роман, възраждане", publisher = "Народна култура", callNumber = "ЦБ/886.7/В 12"),
        CatalogBook(2, "Вазов, Иван", "Чичовци", year = "1885", docType = "книга", language = "български", udc = "886.7", available = false, registeredOn = "2024-05-01"),
        CatalogBook(3, "Елин Пелин", "Под манастирската лоза", year = "[1936]", docType = "книга", language = "български", udc = "886.7", department = "за деца", available = true, registeredOn = "2023-01-01", annotation = "Разкази за живота в манастира."),
        CatalogBook(4, "Smith, John", "Atlas of Europe", year = "2010", docType = "картографско издание", language = "английски", udc = "912", available = true, isbn = "978-0-00-000000-2"),
        CatalogBook(1234, "", "Вестник Яворец", year = "", docType = "продължаващо издание", udc = "", available = true),
        CatalogBook(5, "Йовков, Йордан", "Старопланински легенди", year = "1927 г.", docType = "книга", language = "български", udc = "886.7", available = true, registeredOn = "2025-02-02", isbn = "9789540901234"),
        CatalogBook(6, "Café, Émile", "Pâté à la française", year = "1999", docType = "книга", language = "френски", udc = "64", available = false, keywords = "кулинария; рецепти"),
        CatalogBook(7, "Ботев, Христо", "Стихотворения", subtitle = "Пълно събрание", year = "2001", docType = "книга", language = "български", udc = "886.7", available = true, keywords = "поезия, ботев"),
        CatalogBook(8, "Димитрова, Блага", "Лавина", year = "1971", docType = "книга", language = "български", udc = "886.7", available = true, annotation = "Роман за една ски-експедиция.", publisher = "Български писател"),
        CatalogBook(9, "Ёлкин, Пётр", "Ёжик в тумане", year = "1975", docType = "книга", language = "руски", udc = "8", available = true),
        CatalogBook(10, "Ѝванов, Ѝ.", "C++ за начинаещи", year = "2019", docType = "книга", language = "български", udc = "004", available = true, keywords = "програмиране"),
        CatalogBook(11, "Вазов, Иван", "Под игото", year = "2005", docType = "книга", language = "български", udc = "886.7", available = true, registeredOn = "2025-02-02"),
    )
    private val sample = KatalogParser.parse(TestUtil.resource("katalog-sample.json"))
    private val snapshot = CatalogSnapshot("Б", "Яворец", "2026-09-19", extra + sample.books, emptyList())
    private val engine = CatalogSearchEngine(snapshot)
    private val store = FakeFtsIndexStore().apply {
        rows = CatalogIndexRows.build(snapshot)
        state = CatalogIndexState("h", snapshot.generatedOn, snapshot.books.size, CatalogSearchEngine.facetsOf(snapshot))
    }
    private val fts = FtsCatalogSearcher(store, store.state!!, fallback = { engine }, work = Dispatchers.Unconfined)

    private val typical = listOf(
        "", "под", "Под игото", "ПОД ИГОТО", "„под“", "под  игото ", "вазов", "Вазов игото", "иван вазов", "Джиан", "джиан ох",
        "роман", "възраждане", "лоза", "манастир", "елин пелин", "пелин", "йовков", "йордан", "легенди", "ёжик", "ежик", "елкин",
        "cafe", "café", "pate", "PÂTÉ", "atlas", "europe", "c++", "ботев", "стихотворения пълно", "лавина", "експедиция",
        "български писател", "народна", "1894", "399666", "9789540901234", "цб", "886", "вятъра", "6 дни",
        "ал араф", "книга", "за деца", "ѝванов",
    )
    private val filters = listOf(
        CatalogQuery(),
        CatalogQuery(onlyAvailable = true),
        CatalogQuery(docType = "книга", yearFrom = 1900, yearTo = 2015),
        CatalogQuery(language = "български", udcSection = 8),
        CatalogQuery(department = "за деца"),
    )

    private suspend fun assertSame(q: CatalogQuery) =
        assertEquals(engine.search(q).map { it.inv }, fts.search(q).map { it.inv }, "заявка $q")

    @Test fun typicalQueriesMatchInMemoryEngineForAllFieldsAndSorts() = runTest {
        for (text in typical) for (field in listOf(SearchField.ALL, SearchField.TITLE, SearchField.AUTHOR, SearchField.KEYWORD))
            for (sort in CatalogSort.entries) assertSame(CatalogQuery(text, field, sort = sort))
    }

    @Test fun filtersMatchInMemoryEngine() = runTest {
        for (base in filters) for (text in listOf("", "под", "вазов", "роман", "книга")) for (sort in CatalogSort.entries)
            assertSame(base.copy(text = text, sort = sort))
    }

    @Test fun isbnAndInventoryMatchInMemoryEngine() = runTest {
        val inv = sample.books.first().inv.toString()
        for (text in listOf("978-0-00", "9780000000002", "0000", "954090", "97", "", inv, "00$inv", inv.take(3), "1234", "12", "x"))
            for (field in listOf(SearchField.ISBN, SearchField.INVENTORY)) assertSame(CatalogQuery(text, field))
        for (text in listOf("978-0-00", "9780000000002", "", inv)) assertSame(CatalogQuery(text, SearchField.ALL))
    }

    @Test fun ftsCandidatesComeFromTheIndexNotAFullScan() = runTest {
        store.queries.clear()
        assertEquals(listOf(1L, 11L), fts.search(CatalogQuery("вазов игото", sort = CatalogSort.YEAR_ASC)).map { it.inv })
        val q = store.queries.single()
        assertEquals(listOf("вазов", "игото"), q.matchTokens)
        assertTrue(q.contains.isEmpty())
    }

    @Test fun middleOfWordFallsBackToSubstringSearch() = runTest {
        // „астир“ не е начало на дума — FTS не намира нищо, търси се като подниз (както в паметта).
        assertSame(CatalogQuery("астир"))
        store.queries.clear()
        assertEquals(listOf(3L), fts.search(CatalogQuery("астир")).map { it.inv })
        assertEquals(2, store.queries.size)
        assertEquals(listOf("астир"), store.queries.last().contains)
    }

    @Test fun middleOfWordIsNotAddedWhenWholeWordsMatch() = runTest {
        // „гото“ — няма дума, започваща така → резервното търсене намира „игото“.
        assertSame(CatalogQuery("гото"))
        // Умишлена разлика: думите се търсят като начало на дума (FTS префикс). Подниз по
        // средата на дума се търси само ако няма нито едно такова съвпадение.
        assertEquals(listOf(10L, 6L), engine.search(CatalogQuery("c", SearchField.TITLE)).map { it.inv }) // „française“
        assertEquals(listOf(10L), fts.search(CatalogQuery("c", SearchField.TITLE)).map { it.inv })
    }

    @Test fun ftsResultsAreAlwaysAnOrderedSubsetOfTheEngine() = runTest {
        val texts = typical + listOf("c", "ва", "97", "а", "о", "ст", "ние", "99", "ск", "ки")
        for (text in texts) for (field in SearchField.entries) for (sort in CatalogSort.entries) {
            val q = CatalogQuery(text, field, sort = sort)
            val expected = engine.search(q).map { it.inv }
            val actual = fts.search(q).map { it.inv }
            assertTrue(expected.isEmpty() == actual.isEmpty(), "празен резултат само заедно: $q")
            var i = 0
            for (inv in expected) if (i < actual.size && actual[i] == inv) i++
            assertEquals(actual.size, i, "подредица на резултата в паметта: $q")
        }
    }

    @Test fun operatorsAndQuotesAreInert() = runTest {
        for (text in listOf("\"под\"", "под OR вазов", "под AND игото", "NOT вазов", "-вазов", "под*", "^под", "title:под", "NEAR", "под NEAR/3 игото", "&", "№ 5", "' OR 1=1 --"))
            for (field in listOf(SearchField.ALL, SearchField.TITLE)) assertSame(CatalogQuery(text, field))
    }

    @Test fun suggestionsMatchInMemoryEngine() = runTest {
        for (text in listOf("вазов", "под", "под иг", "елин пелин", "ёж", "c+", "zzz", "лав")) for (limit in listOf(1, 6, 8))
            assertEquals(engine.suggestions(text, limit), fts.suggestions(text, limit), "предложения за $text")
    }

    @Test fun databaseErrorFallsBackToEngine() = runTest {
        store.failing = true
        assertSame(CatalogQuery("вазов"))
        assertEquals(engine.suggestions("ва", 6), fts.suggestions("ва", 6))
        store.failing = false
    }

    @Test fun matchExpressionQuotesEveryTokenAsPrefixPhrase() {
        val q = CatalogIndexQueries.search(CatalogQuery("Вазов „Под“ ИГОТО"), listOf("вазов", "под", "игото"), "")!!
        assertEquals("\"вазов*\" \"под*\" \"игото*\"", q.match)
        assertNull(q.matchColumn)
        assertEquals(CatalogIndexColumns.TITLE, CatalogIndexQueries.search(CatalogQuery("x", SearchField.TITLE), listOf("x"), "")!!.matchColumn)
        assertEquals("a+b", CatalogIndexQueries.ftsToken("a\"+\"b"))
        assertNull(CatalogIndexQueries.ftsToken("&"))
        assertNull(CatalogIndexQueries.ftsToken("\"\""))
        // Дума без букви/цифри не влиза в MATCH (празна фраза в FTS4 не съвпада с нищо).
        assertEquals(listOf("под"), CatalogIndexQueries.search(CatalogQuery("под &"), listOf("под", "&"), "")!!.matchTokens)
        // Само такива думи → търсене като подниз.
        assertEquals(listOf("&"), CatalogIndexQueries.search(CatalogQuery("&"), listOf("&"), "")!!.contains)
        assertNull(CatalogIndexQueries.search(CatalogQuery("12", SearchField.ISBN), listOf("12"), "12"))
        assertNull(CatalogIndexQueries.search(CatalogQuery("x", SearchField.INVENTORY), listOf("x"), ""))
    }

    @Test fun sqlBindsEveryValueAsArgument() {
        val q = CatalogIndexQuery(
            matchTokens = listOf("под", "игото"), matchColumn = CatalogIndexColumns.TITLE,
            docType = "кни'га", department = "д", language = "л", udcSection = 8, yearFrom = 1900, yearTo = 2000, onlyAvailable = true,
        )
        assertEquals(
            "SELECT b.* FROM catalog_books AS b JOIN catalog_fts ON catalog_fts.rowid = b.ord WHERE 1 AND catalog_fts.n_title MATCH ?" +
                " AND b.available = 1 AND b.doc_type = ? AND b.department = ? AND b.language = ? AND b.udc_section = ?" +
                " AND b.year_num >= ? AND b.year_num <= ? ORDER BY b.ord",
            q.sql,
        )
        assertEquals(listOf<Any>("\"под*\" \"игото*\"", "кни'га", "д", "л", 8, 1900, 2000), q.args)
        assertEquals(q.sql.count { it == '?' }, q.args.size)

        val scan = CatalogIndexQueries.scan(CatalogQuery("а б", SearchField.AUTHOR), listOf("а", "б"))!!
        assertEquals("SELECT b.* FROM catalog_books AS b WHERE 1 AND instr(b.n_author, ?) > 0 AND instr(b.n_author, ?) > 0 ORDER BY b.ord", scan.sql)
        assertEquals(listOf<Any>("а", "б"), scan.args)

        val inv = CatalogIndexQuery(invDigits = "0012", limit = 5, offset = 10)
        assertEquals("SELECT b.* FROM catalog_books AS b WHERE 1 AND (b.n_inv = ? OR substr(b.n_inv, 1, ?) = ?) ORDER BY b.ord LIMIT 5 OFFSET 10", inv.sql)
        assertEquals(listOf<Any>("12", 4, "0012"), inv.args)

        val isbn = CatalogIndexQuery(isbnDigits = "978")
        assertEquals("SELECT b.* FROM catalog_books AS b WHERE 1 AND instr(b.isbn_digits, ?) > 0 ORDER BY b.ord", isbn.sql)

        val all = CatalogIndexQueries.scan(CatalogQuery("x"), listOf("x"))!!
        assertEquals(CatalogIndexColumns.FTS.size - 1, all.sql.split(" || ' ' || ").size - 1)
        assertEquals(1, all.args.size)
    }

    @Test fun bookLookupsMatchInMemoryEngineWithoutSearching() = runTest {
        store.queries.clear()
        for (b in snapshot.books) {
            assertEquals(engine.book(b.inv), fts.book(b.inv))
            assertEquals(engine.copiesOf(b), fts.copiesOf(b), "екземпляри на ${b.inv}")
            for (limit in listOf(1, 10)) assertEquals(engine.byAuthor(b.author, b.inv, limit), fts.byAuthor(b.author, b.inv, limit), "автор на ${b.inv}")
        }
        assertEquals(listOf(11L), fts.copiesOf(extra[0]).map { it.inv }, "същото заглавие и автор")
        assertEquals(listOf(2L, 11L), fts.byAuthor("Вазов, Иван", 1, 10).map { it.inv }, "без самата книга")
        assertEquals(listOf(1L, 2L), fts.byAuthor("Вазов, Иван", 99, 10).map { it.inv }, "по едно заглавие")
        assertNull(fts.book(-1))
        val invs = listOf(1L, 11L, 5L, -1L, sample.books.first().inv)
        assertEquals(engine.books(invs), fts.books(invs))
        assertTrue(store.queries.isEmpty(), "справките не минават през търсенето")
    }

    @Test fun isbnLookupsMatchInMemoryEngine() = runTest {
        for (raw in listOf("9789540901234", "978-954-09-0123-4", "9780000000002", "0000000002", "978-0-00", "", null, "x")) {
            assertEquals(engine.findByIsbn(raw), fts.findByIsbn(raw), "ISBN $raw")
            assertEquals(engine.allByIsbn(raw), fts.allByIsbn(raw), "ISBN $raw")
        }
        assertEquals(5L, fts.findByIsbn("9789540901234")?.inv)
    }

    @Test fun authorsMatchInMemoryEngine() = runTest {
        for (text in listOf("вазов", "ва", "ив", "елин пелин", "ёлкин", "smith", "zzz", "а"))
            for (limit in listOf(1, 8)) {
                val tokens = org.chyavorec.core.TextNormalizer.tokens(text)
                assertEquals(engine.authors(tokens, limit), fts.authors(tokens, limit), "автори за $text")
            }
    }

    @Test fun globalSearchThroughTheIndexMatchesTheEngine() = runTest {
        for (q in listOf("вазов", "под", "пелин", "ёлкин", "астир", "zzz", ""))
            assertEquals(SearchAggregator.search(q, engine, SearchCorpus.EMPTY), SearchAggregator.search(q, fts, SearchCorpus.EMPTY), "„$q“")
        // Както в екрана „Каталог“: книгите — думите като начало на дума (подниз по средата —
        // само ако няма такива); авторите — като подниз, както досега.
        val engineHits = SearchAggregator.search("ва", engine, SearchCorpus.EMPTY)
        val ftsHits = SearchAggregator.search("ва", fts, SearchCorpus.EMPTY)
        assertEquals(engineHits.authors, ftsHits.authors)
        assertEquals(listOf(1L, 11L, 2L), ftsHits.books.map { it.book.inv })
    }

    @Test fun recommendationsMatchInMemoryEngineFromPrefilteredCandidates() = runTest {
        val histories = listOf(
            listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван")),
            listOf(HistoryItem("h1", 4, "Atlas of Europe", "Smith, John"), HistoryItem("h2", 9, "Ёжик в тумане", "Ёлкин, Пётр")),
            listOf(HistoryItem("h1", null, "Нещо", "Йовков, Йордан")),
            listOf(HistoryItem("h1", sample.books.first().inv, "", ""), HistoryItem("h2", 6, "Pâté", "Café, Émile")),
            listOf(HistoryItem("h1", 1234, "Вестник Яворец", "")),
            emptyList(),
        )
        // Кандидатите идват само с колоните за списъка — сравняват се книгите (инв. №) и редът.
        for (h in histories) for (limit in listOf(1, 12))
            assertEquals(ReadingRecommender.recommend(engine, h, limit).map { it.inv }, fts.recommend(h, limit).map { it.inv }, "история $h")
        store.lookups.clear()
        val recs = fts.recommend(histories[0], 12)
        assertEquals(listOf("books", "recommendationCandidates"), store.lookups)
        assertTrue(recs.isNotEmpty() && recs.all { it.annotation.isEmpty() && it.keywords.isEmpty() }, "анотацията не се чете за препоръките")
    }

    @Test fun recommendationPrefilterIsASupersetOfScoredBooks() {
        val history = listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван"), HistoryItem("h2", null, "", "Café, Émile"))
        val pre = ReadingRecommender.prefilter(history, engine::book)!!
        assertEquals(setOf(8), pre.udcSections)
        assertEquals(setOf("вазов", "emile"), pre.authorWords)
        assertNull(ReadingRecommender.prefilter(emptyList(), engine::book))
    }

    @Test fun listWithoutTextReadsOnlyTheListColumns() = runTest {
        val q = CatalogIndexQueries.search(CatalogQuery(onlyAvailable = true), emptyList(), "")!!
        assertTrue(q.textless)
        assertTrue(q.sql.startsWith("SELECT b.ord, b.inv, b.author, b.title"), q.sql)
        assertTrue(q.sql.contains("'' AS annotation") && q.sql.contains("'' AS n_annotation") && !q.sql.contains("b.*"), q.sql)
        assertEquals(q.sql.count { it == '?' }, q.args.size)
        val listed = fts.search(CatalogQuery()).first { it.inv == 3L }
        assertEquals("", listed.annotation, "анотацията не се чете за списъка")
        assertEquals("Разкази за живота в манастира.", fts.book(3)!!.annotation)
        assertTrue(!CatalogIndexQueries.search(CatalogQuery("под"), listOf("под"), "")!!.textless)
    }

    @Test fun emptySearchIsPagedFromTheIndexInTheEngineOrder() = runTest {
        for (base in filters) for (sort in CatalogSort.entries) for (text in listOf("", "  ", "„“")) for (page in listOf(1, 3, 40)) {
            val q = base.copy(text = text, sort = sort)
            val expected = engine.search(q).map { it.inv }
            store.queries.clear()
            val first = fts.searchFirst(q, page)
            assertEquals(expected.size, first.total, "брой $q")
            assertEquals(expected.take(page), first.books.map { it.inv }, "първа страница $q / $page")
            // Само две заявки: страницата (с LIMIT) и броят — не целият списък.
            assertEquals(2, store.queries.size, "$q")
            assertTrue(store.queries.all { it.textless && it.limit == page && it.sort == sort }, "$q")
            val all = first.books.map { it.inv }.toMutableList()
            while (all.size < first.total) {
                val more = fts.searchMore(q, all.size, page)
                assertTrue(more.isNotEmpty() && more.size <= page)
                all += more.map { it.inv }
            }
            assertEquals(expected, all, "всички страници $q / $page")
            assertTrue(fts.searchMore(q, all.size, page).isEmpty())
        }
    }

    @Test fun searchWithTextReturnsAllResultsAtOnce() = runTest {
        for (text in listOf("под", "вазов", "астир")) {
            val q = CatalogQuery(text)
            val r = fts.searchFirst(q, 1)
            assertEquals(engine.search(q).map { it.inv }, r.books.map { it.inv })
            assertEquals(r.books.size, r.total)
            assertEquals(r.books.drop(1).take(1), fts.searchMore(q, 1, 1))
        }
    }

    @Test fun emptySearchPagingFallsBackToEngineOnDatabaseError() = runTest {
        store.failing = true
        val q = CatalogQuery(sort = CatalogSort.AUTHOR)
        val expected = engine.search(q)
        val first = fts.searchFirst(q, 5)
        assertEquals(expected.size, first.total)
        assertEquals(expected.take(5), first.books)
        assertEquals(expected.drop(5).take(5), fts.searchMore(q, 5, 5))
    }

    @Test fun pageQueryOrdersAndLimitsInTheDatabase() {
        val q = CatalogIndexQueries.page(CatalogQuery(onlyAvailable = true, yearFrom = 1900, sort = CatalogSort.YEAR_DESC), offset = 40, limit = 20)
        assertTrue(q.textless)
        assertTrue(q.sql.endsWith(" ORDER BY b.year_num IS NULL, b.year_num DESC, b.ord LIMIT 20 OFFSET 40"), q.sql)
        assertTrue(q.sql.startsWith("SELECT " + CatalogIndexColumns.LIST_SELECT + " FROM"), q.sql)
        assertEquals("SELECT COUNT(*) FROM catalog_books AS b WHERE 1 AND b.available = 1 AND b.year_num >= ?", q.countSql)
        assertEquals(q.sql.count { it == '?' }, q.args.size)
        assertEquals(q.countSql.count { it == '?' }, q.args.size)
        for (sort in CatalogSort.entries) assertTrue(CatalogIndexQueries.page(CatalogQuery(sort = sort), 0, 1).sql.contains(" ORDER BY b."))
    }

    @Test fun rowsKeepNormalisedFieldsAndRanks() {
        val rows = CatalogIndexRows.build(snapshot)
        val r = rows.first { it.book.inv == 1L }
        assertEquals("под игото роман из живота на българите", r.title)
        assertEquals("цб/886.7/в 12", r.callNumber)
        assertEquals(1894, r.year)
        assertEquals(8, r.udcSection)
        val byTitle = rows.sortedWith(compareBy({ it.titleRank }, { it.ord })).map { it.book.inv }
        assertEquals(engine.search(CatalogQuery(sort = CatalogSort.TITLE)).map { it.inv }, byTitle)
    }
}
