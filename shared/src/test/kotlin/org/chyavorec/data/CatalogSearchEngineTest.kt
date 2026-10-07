package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogShelf
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CatalogSearchEngineTest {
    private val books = listOf(
        CatalogBook(1, "Вазов, Иван", "Под игото", year = "1894", docType = "книга", language = "български", udc = "886.7", department = "за възрастни", available = true, registeredOn = "2020-01-01", keywords = "роман, възраждане"),
        CatalogBook(2, "Вазов, Иван", "Чичовци", year = "1885", docType = "книга", language = "български", udc = "886.7", available = false, registeredOn = "2024-05-01"),
        CatalogBook(3, "Елин Пелин", "Под манастирската лоза", year = "1936", docType = "книга", language = "български", udc = "886.7", department = "за деца", available = true, registeredOn = "2023-01-01"),
        CatalogBook(4, "Smith, John", "Atlas of Europe", year = "2010", docType = "картографско издание", language = "английски", udc = "912", available = true, isbn = "978-0-00-000000-2"),
        CatalogBook(1234, "", "Вестник Яворец", year = "", docType = "продължаващо издание", udc = "", available = true),
    )
    private val engine = CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-09-19", books, listOf(CatalogShelf("Витрина", listOf(3, 1, 999)))))

    private fun ids(q: CatalogQuery) = engine.search(q).map { it.inv }

    @Test fun titleSearchIgnoresCaseAndQuotes() = assertEquals(listOf(1L, 3L), ids(CatalogQuery("„под“", SearchField.TITLE)))
    @Test fun authorSearch() = assertEquals(setOf(1L, 2L), ids(CatalogQuery("вазов", SearchField.AUTHOR)).toSet())
    @Test fun keywordSearch() = assertEquals(listOf(1L), ids(CatalogQuery("възраждане", SearchField.KEYWORD)))
    @Test fun isbnSearch() = assertEquals(listOf(4L), ids(CatalogQuery("9780000", SearchField.ISBN)))
    @Test fun inventoryExact() = assertEquals(1234L, ids(CatalogQuery("1234", SearchField.INVENTORY)).first())
    @Test fun findByIsbnNormalisesAndConverts() {
        assertEquals(4L, engine.findByIsbn("9780000000002")?.inv)
        assertEquals(4L, engine.findByIsbn("978 0 00 000000 2")?.inv)
        // ISBN-10 формата на същия номер
        assertEquals(4L, engine.findByIsbn("0000000000")?.inv)
        assertEquals(null, engine.findByIsbn("9789540907055"))
        assertEquals(null, engine.findByIsbn("978"))
        assertEquals(1, engine.allByIsbn("978-0-00-000000-2").size)
    }
    @Test fun findByInv() {
        assertEquals("Под игото", engine.findByInv(1)?.title)
        assertEquals(null, engine.findByInv(42))
    }
    @Test fun allFieldsMultiToken() = assertEquals(listOf(1L), ids(CatalogQuery("вазов игото")))
    @Test fun titleMatchRanksFirst() = assertEquals(1L, ids(CatalogQuery("под")).first())

    @Test fun filters() {
        assertEquals(listOf(4L), ids(CatalogQuery(language = "английски")))
        assertEquals(listOf(3L), ids(CatalogQuery(department = "за деца")))
        assertEquals(setOf(1L, 3L, 4L, 1234L), ids(CatalogQuery(onlyAvailable = true)).toSet())
        assertEquals(listOf(4L), ids(CatalogQuery(udcSection = 9)))
        assertEquals(setOf(1L, 2L), ids(CatalogQuery(yearFrom = 1800, yearTo = 1900)).toSet())
    }

    @Test fun sorting() {
        assertEquals(listOf(4L, 3L, 1L, 2L), ids(CatalogQuery(sort = CatalogSort.YEAR_DESC)).take(4))
        assertEquals(2L, ids(CatalogQuery(sort = CatalogSort.NEWEST)).first())
    }

    @Test fun facetsFromRealData() {
        val f = engine.facets
        assertEquals("книга", f.docTypes.first())
        assertEquals(listOf(8, 9), f.udcSections)
        assertEquals(1885, f.minYear)
        assertEquals(2010, f.maxYear)
    }

    @Test fun shelvesSkipUnknown() {
        val shelves = engine.shelfBooks()
        assertEquals(listOf(3L, 1L), shelves.single().second.map { it.inv })
    }

    @Test fun suggestions() = assertTrue(engine.suggestions("ваз").contains("Вазов, Иван"))

    @Test fun newestUsesRegistrationDate() = assertEquals(listOf(2L, 3L), engine.newest(2).map { it.inv })

    @Test fun performanceAtRealisticScale() {
        val big = (1..15_000).map { i -> CatalogBook(i.toLong(), "Автор $i", "Заглавие номер $i", keywords = "ключ${i % 50}", available = i % 3 != 0) }
        val e = CatalogSearchEngine(CatalogSnapshot("Б", "Я", "2026-01-01", big, emptyList()))
        val start = System.nanoTime()
        repeat(20) { e.search(CatalogQuery("заглавие 14")) }
        val perSearchMs = (System.nanoTime() - start) / 20 / 1_000_000
        assertTrue(perSearchMs < 200, "търсенето отнема $perSearchMs ms")
    }
}
