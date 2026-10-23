package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.ReadingRecommender
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.HistoryItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReadingRecommenderTest {
    private val books = listOf(
        CatalogBook(1, "Вазов, Иван", "Под игото", udc = "886.7-31", available = true),
        CatalogBook(2, "Вазов, Иван", "Чичовци", udc = "886.7-31", available = true, registeredOn = "2024-01-01"),
        CatalogBook(3, "Елин Пелин", "Гераците", udc = "886.7-32", available = true, registeredOn = "2023-01-01"),
        CatalogBook(4, "Елин Пелин", "Ян Бибиян", udc = "886.7-93", available = false),
        CatalogBook(5, "Smith, John", "Atlas", udc = "912", available = true),
        CatalogBook(6, "Йовков, Йордан", "Старопланински легенди", udc = "886.7-32", available = true),
        CatalogBook(7, "Вазов, Иван", "Под игото", udc = "886.7-31", available = true), // втори екземпляр
    )
    private val engine = CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-09-19", books, emptyList()))

    @Test fun sameAuthorAndUdcScoreHighestAndReadBooksAreSkipped() {
        val history = listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван", "2026-01-10", "2026-02-01"))
        val r = ReadingRecommender.recommend(engine, history).map { it.inv }
        assertEquals(2L, r.first(), "същият автор е първи")
        assertFalse(1L in r, "прочетената не се предлага")
        assertFalse(7L in r, "друг екземпляр на прочетеното заглавие не се предлага")
        assertFalse(4L in r, "неналичните се пропускат")
        assertFalse(5L in r, "без общ УДК/автор — не")
        assertTrue(3L in r && 6L in r, "същият раздел по УДК")
    }

    @Test fun emptyHistoryGivesNothing() = assertTrue(ReadingRecommender.recommend(engine, emptyList()).isEmpty())

    @Test fun historyWithoutInvStillMatchesAuthor() {
        val history = listOf(HistoryItem("h1", null, "Гераците", "Елин Пелин"))
        val r = ReadingRecommender.recommend(engine, history).map { it.inv }
        assertEquals(emptyList(), r.filter { it == 3L }, "прочетеното заглавие (без инв.) се пропуска")
        assertTrue(r.isEmpty() || r.all { it != 3L })
    }

    @Test fun limitIsRespected() {
        val history = listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван"))
        assertEquals(1, ReadingRecommender.recommend(engine, history, limit = 1).size)
    }

    @Test fun udcPrefix() {
        assertEquals("886", ReadingRecommender.udcPrefix("886.7-31", 3))
        assertEquals("88", ReadingRecommender.udcPrefix("886.7", 2))
        assertEquals(null, ReadingRecommender.udcPrefix("", 2))
        assertEquals(null, ReadingRecommender.udcPrefix("9", 2))
    }

    /** Ключовете без Regex съвпадат с досегашните (`lowercase().replace(Regex("[^\p{L}\p{N}]+"), " ").trim()`). */
    @Test fun keysMatchTheRegexVersion() {
        val regex = Regex("[^\\p{L}\\p{N}]+")
        fun old(s: String) = s.lowercase().replace(regex, " ").trim()
        val samples = mutableListOf(
            "", " ", "Под игото | Вазов, Иван", "  --Café, Émile--  ", "Ёжик в тумане|Ёлкин, Пётр", "и др.", "И ДР",
            "Том 2½ — ²³ Ⅻ", "\uD835\uDC00bc 😀 x", "a\tb\nc", "ǅemal", "İstanbul", "ß-straße", "١٢٣ عربى", "日本語のタイトル",
        )
        val alphabet = "aZ09 ,.-|;:!?()[]Ёйъ\u00A0\u2007\t½Ⅻ²İß😀\uD800"
        val random = java.util.Random(7)
        repeat(2000) { samples += String(CharArray(random.nextInt(16)) { alphabet[random.nextInt(alphabet.length)] }) }
        for (s in samples) {
            assertEquals(old(s), ReadingRecommender.alnumKey(s), "„$s“")
            assertEquals(old("$s|$s"), ReadingRecommender.titleKey(s, s))
            assertEquals(old(s).takeIf { it.length >= 3 && it != "и др" }, ReadingRecommender.authorKey(s))
        }
    }

    @Test fun recommendCallsTheCheckpointSoItCanBeCancelled() {
        val many = (1..1000L).map { CatalogBook(100 + it, "Вазов, Иван", "Книга $it", udc = "886.7-31", available = true) }
        val history = listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван"))
        var calls = 0
        ReadingRecommender.recommend(many, history, engine::book) { calls++ }
        assertEquals(4, calls, "на всеки 256 записа")
        val cancelled = runCatching { ReadingRecommender.recommend(many, history, engine::book) { throw java.util.concurrent.CancellationException() } }
        assertTrue(cancelled.exceptionOrNull() is java.util.concurrent.CancellationException)
    }

    @Test fun prefilterIsDroppedWhenItsSectionsCoverMostBooks() {
        val counts = mapOf(8 to 80, 9 to 10, 3 to 5, ReadingRecommender.NO_SECTION to 5)
        assertTrue(ReadingRecommender.coversMost(setOf(8), counts, emptyList()))
        assertFalse(ReadingRecommender.coversMost(setOf(9, 3), counts, emptyList()))
        assertFalse(ReadingRecommender.coversMost(emptySet(), counts, emptyList()))
        // Индекс от по-стара версия (без броя) — по броя на разделите.
        assertTrue(ReadingRecommender.coversMost(setOf(1, 2, 3, 8, 9), emptyMap(), (0..9).toList()))
        assertFalse(ReadingRecommender.coversMost(setOf(8, 9), emptyMap(), (0..9).toList()))
        assertFalse(ReadingRecommender.coversMost(setOf(8), emptyMap(), emptyList()))
        assertEquals(mapOf(8 to 5, 9 to 1), ReadingRecommender.availableBySection(books))
    }
}
