package org.chyavorec.domain

import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.ReadingRecommender
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.DiaryEntry
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.ReadingDiary
import java.time.Instant
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Читателски дневник“: правилата, любимата книга на годината и тежестите на оценките в препоръките. */
class ReadingDiaryTest {
    private val zone = ZoneId.of("Europe/Sofia")

    private fun entry(title: String, rating: Int, readYear: Int? = 2026, ratedAt: Long = 1_000, author: String = "Автор", note: String = "") =
        DiaryEntry(ReadingDiary.bookKey(title, author), title, author, null, rating, note, readYear, ratedAt)

    @Test fun ratingAndNoteAreBounded() {
        assertEquals(0, ReadingDiary.clampRating(-3))
        assertEquals(5, ReadingDiary.clampRating(9))
        assertEquals(3, ReadingDiary.clampRating(3))
        assertEquals("Много хубава книга", ReadingDiary.cleanNote("  Много  хубава\nкнига  "))
        assertEquals(ReadingDiary.MAX_NOTE, ReadingDiary.cleanNote("а".repeat(500)).length)
    }

    @Test fun sameTitleAndAuthorShareAKeyIgnoringPunctuation() {
        assertEquals(ReadingDiary.bookKey("Под игото!", "Вазов, Иван"), ReadingDiary.bookKey("Под игото", "вазов иван"))
        assertFalse(ReadingDiary.bookKey("Под игото", "Вазов") == ReadingDiary.bookKey("Чичовци", "Вазов"))
    }

    @Test fun ratingsSkipDismissedEntries() {
        val r = ReadingDiary.ratings(listOf(entry("А", 5), entry("Б", 0), entry("В", 2)))
        assertEquals(setOf(ReadingDiary.bookKey("А", "Автор"), ReadingDiary.bookKey("В", "Автор")), r.keys)
        assertEquals(5, r[ReadingDiary.bookKey("А", "Автор")])
    }

    @Test fun favouriteOfTheYearIsTheBestLikedOneThenTheLatest() {
        val entries = listOf(
            entry("Три", 3, readYear = 2026),
            entry("Четири", 4, readYear = 2026, ratedAt = 10),
            entry("Пет стара", 5, readYear = 2026, ratedAt = 20),
            entry("Пет нова", 5, readYear = 2026, ratedAt = 30),
            entry("Друга година", 5, readYear = 2025, ratedAt = 40),
            entry("Пропусната", 0, readYear = 2026, ratedAt = 50),
        )
        assertEquals("Пет нова", ReadingDiary.favorite(entries, 2026)?.title)
        assertEquals("Друга година", ReadingDiary.favorite(entries, 2025)?.title)
        assertNull(ReadingDiary.favorite(entries, 2024))
    }

    @Test fun favouriteNeedsAtLeastALikedRating() {
        assertNull(ReadingDiary.favorite(listOf(entry("Средна", 3), entry("Лоша", 1)), 2026))
    }

    @Test fun yearFallsBackToTheRatingDateInSofia() {
        // 31.12.2026 22:30 UTC е вече 2027 в София.
        val ratedAt = Instant.parse("2026-12-31T22:30:00Z").toEpochMilli()
        val e = entry("Без година", 5, readYear = null, ratedAt = ratedAt)
        assertEquals("Без година", ReadingDiary.favorite(listOf(e), 2027, zone)?.title)
        assertNull(ReadingDiary.favorite(listOf(e), 2026, zone))
    }

    @Test fun weightsAreSmallAndDeterministic() {
        assertEquals(1, ReadingDiary.weight(null))
        assertEquals(listOf(-1, 0, 1, 2, 3), (1..5).map { ReadingDiary.weight(it) })
    }

    @Test fun shareTextNeverCarriesTheNoteUnlessIncluded() {
        val without = ReadingDiary.recommendation("Под игото", 5, "Лична бележка", includeNote = false, bg = true)
        assertTrue("Под игото — препоръчва читател на НЧ „Васил Левски – 1922“" in without)
        assertTrue("Оценка: 5 от 5" in without)
        assertFalse("Лична бележка" in without)
        val with = ReadingDiary.recommendation("Под игото", 5, "Лична бележка", includeNote = true, bg = true)
        assertTrue("„Лична бележка“" in with)
        // Включена, но няма бележка — нищо излишно.
        assertFalse("„" in ReadingDiary.recommendation("Книга", 4, "  ", includeNote = true, bg = true).substringAfter("Оценка"))
        val en = ReadingDiary.recommendation("Atlas", 4, "", includeNote = false, bg = false)
        assertTrue("recommended by a reader" in en && "Rating: 4 of 5" in en)
    }

    // Препоръки: харесаното се качва, нехаресаното пада; празните оценки — както досега.
    private val books = listOf(
        CatalogBook(1, "Вазов, Иван", "Под игото", udc = "821.1", available = true),
        CatalogBook(2, "Йовков, Йордан", "Чифликът", udc = "821.2", available = true),
        CatalogBook(3, "Вазов, Иван", "Чичовци", udc = "821.1", available = true),
        CatalogBook(4, "Дейвис, Пол", "Физика", udc = "530.1", available = true),
        CatalogBook(5, "Дейвис, Пол", "Космос", udc = "524.8", available = true),
        CatalogBook(6, "Йовков, Йордан", "Старопланински легенди", udc = "821.2", available = true),
        CatalogBook(7, "Гео, Милев", "Септември", udc = "821.1", available = true),
        CatalogBook(8, "Елин Пелин", "Гераците", udc = "821.2", available = true),
        CatalogBook(9, "Дейвис, Пол", "Часът на звездите", udc = "524.8", available = true),
    )
    private val engine = CatalogSearchEngine(CatalogSnapshot("Б", "Яворец", "2026-09-19", books, emptyList()))

    private fun recs(history: List<HistoryItem>, ratings: Map<String, Int> = emptyMap()) =
        ReadingRecommender.recommend(engine, history, 20, ratings).map { it.inv }

    private val historyBoth = listOf(
        HistoryItem("h1", 1, "Под игото", "Вазов, Иван", "2026-01-10", "2026-02-01"),
        HistoryItem("h2", 4, "Физика", "Дейвис, Пол", "2026-03-10", "2026-04-01"),
    )

    @Test fun withoutRatingsTheOrderIsUnchanged() {
        assertEquals(recs(historyBoth), recs(historyBoth, emptyMap()))
    }

    @Test fun likedAuthorAndSectionGoUpAndDislikedGoDown() {
        val vazov = ReadingDiary.bookKey("Под игото", "Вазов, Иван")
        val physics = ReadingDiary.bookKey("Физика", "Дейвис, Пол")
        // Харесан Вазов, нехаресан Дейвис: книги на Вазов/раздел 821.1 водят, на Дейвис/524 падат под тях.
        val likesVazov = recs(historyBoth, mapOf(vazov to 5, physics to 1))
        assertEquals(3L, likesVazov.first(), "същият автор и раздел като харесаното")
        val i3 = likesVazov.indexOf(3L)
        assertTrue(likesVazov.filter { it in setOf(5L, 9L) }.all { likesVazov.indexOf(it) > i3 }, "нехаресаният автор е след харесания")
        // Обратното: харесан Дейвис, нехаресан Вазов.
        val likesDavis = recs(historyBoth, mapOf(vazov to 1, physics to 5))
        assertTrue(likesDavis.indexOf(5L) < likesDavis.indexOf(3L) || 3L !in likesDavis, "Дейвис води при обратни оценки")
        assertEquals(likesDavis, recs(historyBoth, mapOf(vazov to 1, physics to 5)), "детерминирано")
    }

    @Test fun aSingleDislikedReadGivesNothingToRecommend() {
        val vazov = ReadingDiary.bookKey("Под игото", "Вазов, Иван")
        val history = listOf(HistoryItem("h1", 1, "Под игото", "Вазов, Иван"))
        assertTrue(recs(history).isNotEmpty())
        assertTrue(recs(history, mapOf(vazov to 1)).isEmpty())
    }

    @Test fun ratingsOfOtherBooksChangeNothing() {
        val other = ReadingDiary.bookKey("Нещо друго", "Някой")
        assertEquals(recs(historyBoth), recs(historyBoth, mapOf(other to 1)))
    }

    @Test fun prefilterIgnoresNonPositiveSections() {
        val vazov = ReadingDiary.bookKey("Под игото", "Вазов, Иван")
        val physics = ReadingDiary.bookKey("Физика", "Дейвис, Пол")
        val plain = assertNotNull(ReadingRecommender.prefilter(historyBoth, engine::book))
        assertTrue(plain.udcSections.containsAll(setOf(8, 5)))
        val rated = assertNotNull(ReadingRecommender.prefilter(historyBoth, engine::book, mapOf(vazov to 5, physics to 1)))
        assertEquals(setOf(8), rated.udcSections, "нехаресаният раздел не е кандидат")
    }
}
