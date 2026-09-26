package org.chyavorec.data

import org.chyavorec.TestUtil
import org.chyavorec.data.catalog.KatalogParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KatalogParserTest {

    @Test fun parsesRealInvLibSample() {
        val snap = KatalogParser.parse(TestUtil.resource("katalog-sample.json"))
        assertEquals(60, snap.books.size)
        assertEquals("с. Яворец", snap.place)
        assertEquals("Библиотека при НЧ", snap.library)
        val first = snap.books.first()
        assertEquals(399666L, first.inv)
        assertEquals("Джиан, Филип", first.author)
        assertEquals("ЦБ/840/Д 51", first.callNumber)
        assertTrue(first.available)
        assertEquals(8, first.udcSection)
        assertEquals(2016, first.yearNumber)
        // Витрина: несъществуващият инв. № остава в списъка, празната витрина отпада.
        assertEquals(1, snap.shelves.size)
        assertEquals(3, snap.shelves[0].invNumbers.size)
    }

    @Test fun tolerantToTypesAndMissingFields() {
        val json = """
            {"library":"Б","place":"с. Яворец, ","generated":"2026-01-01","extra":1,
             "items":[
               {"inv":"12","t":"Заглавие","av":true,"y":1999,"u":82,"cv":"http://x.org/c.jpg","i":"978-954"},
               {"inv":13,"t":"","av":1},
               {"t":"Без номер"},
               {"inv":14,"t":"Недостъпна","av":0}
             ]}
        """.trimIndent()
        val snap = KatalogParser.parse(json)
        assertEquals(listOf(12L, 14L), snap.books.map { it.inv })
        val b = snap.books[0]
        assertTrue(b.available)
        assertEquals("1999", b.year)
        assertEquals("82", b.udc)
        assertEquals("https://x.org/c.jpg", b.coverUrl)
        assertEquals("978-954", b.isbn)
        assertFalse(snap.books[1].available)
        assertTrue(snap.shelves.isEmpty())
    }

    @Test fun rejectsGarbage() {
        assertFailsWith<Exception> { KatalogParser.parse("<html>not json</html>") }
    }
}
