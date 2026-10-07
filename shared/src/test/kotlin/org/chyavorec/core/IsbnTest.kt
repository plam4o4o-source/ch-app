package org.chyavorec.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IsbnTest {
    @Test fun normalizeDropsHyphensAndSpaces() = assertEquals("9789540907055", Isbn.normalize("978-954-09-0705-5"))
    @Test fun normalizeKeepsCheckX() = assertEquals("080442957X", Isbn.normalize("0-8044-2957-x"))

    @Test fun validIsbn13() {
        assertTrue(Isbn.isValid13("9789540907055"))
        assertTrue(Isbn.isValid13("9780306406157"))
        assertFalse(Isbn.isValid13("9780306406158"))
        assertFalse(Isbn.isValid13("978030640615"))
    }

    @Test fun validIsbn10() {
        assertTrue(Isbn.isValid10("0306406152"))
        assertTrue(Isbn.isValid10("080442957X"))
        assertFalse(Isbn.isValid10("0306406153"))
    }

    @Test fun isbn10ToIsbn13() {
        assertEquals("9780306406157", Isbn.toIsbn13("0-306-40615-2"))
        assertEquals("9780804429573", Isbn.toIsbn13("080442957X"))
        assertNull(Isbn.toIsbn13("0306406153"))
        assertNull(Isbn.toIsbn13("9780306406157"))
    }

    @Test fun isbn13ToIsbn10() {
        assertEquals("0306406152", Isbn.toIsbn10("978-0-306-40615-7"))
        assertEquals("080442957X", Isbn.toIsbn10("9780804429573"))
        // 979-… няма форма ISBN-10.
        assertNull(Isbn.toIsbn10("9791234567896"))
    }

    @Test fun candidatesHoldBothForms() {
        assertEquals(setOf("9780306406157", "0306406152"), Isbn.candidates("978-0-306-40615-7"))
        assertEquals(setOf("0306406152", "9780306406157"), Isbn.candidates("0306406152"))
        assertEquals(setOf("12345"), Isbn.candidates("123-45"))
        assertEquals(emptySet(), Isbn.candidates(" - "))
    }

    @Test fun bookEanRequiresBooklandPrefixAndChecksum() {
        assertTrue(Isbn.isBookEan("9789540907055"))
        assertFalse(Isbn.isBookEan("4006381333931")) // обикновен EAN на стока
        assertFalse(Isbn.isBookEan("9789540907056"))
    }

    @Test fun scanResolvesIsbnEan() = assertEquals(ScanTarget.Isbn("9789540907055"), ScanCodes.resolve("9789540907055"))
    @Test fun scanResolvesHyphenatedIsbn10() = assertEquals(ScanTarget.Isbn("0306406152"), ScanCodes.resolve("0-306-40615-2"))
    @Test fun scanResolvesInventoryLabel() {
        assertEquals(ScanTarget.Inventory(156), ScanCodes.resolve("0000156"))
        assertEquals(ScanTarget.Inventory(156), ScanCodes.resolve("156"))
    }
    @Test fun scanNonBookEanIsUnknown() { assertIs<ScanTarget.Unknown>(ScanCodes.resolve("4006381333931")) }
    @Test fun scanCatalogUrl() {
        assertEquals(ScanTarget.Inventory(156), ScanCodes.resolve("https://chyavorec.org/elektronen-katalog?inv=156"))
        assertEquals(ScanTarget.Inventory(157), ScanCodes.resolve("https://chyavorec.org/elektronen-katalog#inv=157"))
        assertEquals(ScanTarget.Isbn("9789540907055"), ScanCodes.resolve("https://chyavorec.org/elektronen-katalog?isbn=978-954-09-0705-5"))
        assertEquals(ScanTarget.Isbn("9789540907055"), ScanCodes.resolve("https://chyavorec.org/elektronen-katalog?q=9789540907055"))
        assertEquals(ScanTarget.Unknown("https://example.org/x"), ScanCodes.resolve("https://example.org/x"))
    }
    @Test fun scanTextIsUnknown() = assertEquals(ScanTarget.Unknown("ABC-1"), ScanCodes.resolve("ABC-1"))
}
