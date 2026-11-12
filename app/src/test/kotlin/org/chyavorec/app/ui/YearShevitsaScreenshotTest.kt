package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.data.local.TextScale
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.screens.my.PersonalBand
import org.chyavorec.app.ui.screens.my.ReadingStatsCard
import org.chyavorec.app.ui.screens.my.YearSlideKind
import org.chyavorec.app.ui.screens.my.YearStory
import org.chyavorec.app.ui.screens.my.yearSlides
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareCardContent
import org.chyavorec.app.ui.share.ShareFixtures
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.catalog.ReadingYear.Archetype
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.YearMonth
import kotlin.test.assertTrue

/**
 * „Твоята шевица“ на последния слайд и на картата за споделяне, бродираните графики
 * („Моето четене“, месечният слайд) — светла и тъмна тема, едър шрифт, хоризонтално.
 * Снимките се гледат в клона „screenshots“; логиката (намалено движение) се проверява с пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class YearShevitsaScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val large = appFontScale(1.3f, TextScale.XLARGE.factor)
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun save(bmp: Bitmap, name: String) = File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun shown(text: String) =
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(text, substring = true) or hasContentDescription(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun storySlide(kind: YearSlideKind, name: String, text: String, mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f, summary: ReadingYear.Summary = ShareFixtures.year) {
        val index = yearSlides(summary).indexOf(kind)
        compose.mainClock.autoAdvance = false
        compose.setContent {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
                ChitalishteTheme(mode = mode) {
                    CompositionLocalProvider(LocalReducedMotion provides false) {
                        YearStory(summary, onClose = {}, siteUrl = "https://chyavorec.org", initialSlide = index)
                    }
                }
            }
        }
        // Влизането е 1.8 s, лентата — 2.5 s; снимката е в края на бродирането.
        compose.mainClock.advanceTimeBy(3_200)
        shown(text)
        save(compose.onRoot().captureToImage().asAndroidBitmap(), name)
    }

    /** Лентата наполовина: бродирането върви от средата навън. */
    @Test fun outroMidStitching() {
        val index = yearSlides(ShareFixtures.year).indexOf(YearSlideKind.OUTRO)
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                CompositionLocalProvider(LocalReducedMotion provides false) {
                    YearStory(ShareFixtures.year, onClose = {}, siteUrl = "https://chyavorec.org", initialSlide = index)
                }
            }
        }
        compose.mainClock.advanceTimeBy(1_500)
        shown("Благодарим ти")
        save(compose.onRoot().captureToImage().asAndroidBitmap(), "w21ys-00-outro-stitching")
    }

    @Test fun outroLight() = storySlide(YearSlideKind.OUTRO, "w21ys-01-outro-light", "Благодарим ти")

    @Test fun outroDark() = storySlide(YearSlideKind.OUTRO, "w21ys-02-outro-dark", "Благодарим ти", mode = ThemeMode.DARK)

    @Test fun outroLargeFont() = storySlide(YearSlideKind.OUTRO, "w21ys-03-outro-large-font", "Благодарим ти", fontScale = large)

    @Config(qualifiers = "bg-w891dp-h411dp-land-xhdpi")
    @Test fun outroLandscape() = storySlide(YearSlideKind.OUTRO, "w21ys-04-outro-landscape", "Благодарим ти")

    @Config(qualifiers = "bg-w891dp-h411dp-land-xhdpi")
    @Test fun monthLandscape() = storySlide(YearSlideKind.MONTH, "w21ys-05-month-landscape", "Септември")

    @Test fun monthLargeFont() = storySlide(YearSlideKind.MONTH, "w21ys-06-month-large-font", "Септември", fontScale = large)

    @Test fun monthDark() = storySlide(YearSlideKind.MONTH, "w21ys-07-month-dark", "Септември", mode = ThemeMode.DARK)

    /** Месец с много книги: клетката се смалява и колоната се побира. */
    @Test fun monthHugeCounts() {
        val s = ShareFixtures.year.copy(months = listOf(2, 4, 3, 14, 6, 1, 0, 3, 22, 5, 2, 9), books = 71)
        storySlide(YearSlideKind.MONTH, "w21ys-08-month-huge", "Септември", summary = s)
    }

    // ───────────── картата за споделяне (1080×1920) ─────────────

    private fun card(card: ShareCard, name: String) {
        compose.setContent {
            Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp).testTag("card")) { ShareCardContent(card, null) }
        }
        compose.waitForIdle()
        save(compose.onNodeWithTag("card").captureToImage().asAndroidBitmap(), name)
    }

    @Config(qualifiers = "bg-w360dp-h640dp-xxhdpi")
    @Test fun shareOutro() = card(ShareCard.Year(ShareFixtures.year, YearSlideKind.OUTRO, "https://chyavorec.org"), "w21ys-10-share-outro")

    @Config(qualifiers = "bg-w360dp-h640dp-xxhdpi")
    @Test fun shareBooks() = card(ShareCard.Year(ShareFixtures.year, YearSlideKind.BOOKS, "https://chyavorec.org"), "w21ys-11-share-books")

    @Config(qualifiers = "bg-w360dp-h640dp-xxhdpi")
    @Test fun shareMonth() = card(ShareCard.Year(ShareFixtures.year, YearSlideKind.MONTH, "https://chyavorec.org"), "w21ys-12-share-month")

    // ───────────── лентите на различни читатели ─────────────

    private fun summary(months: List<Int>, archetype: Archetype, sections: List<Pair<Int, Int>> = emptyList(), author: String? = null) =
        ReadingYear.Summary(
            2026, months.sum(), months, author, if (author != null) 3 else 0, 6,
            sections.map { (s, n) -> ReadingStats.SectionCount(s, n) }, 4, archetype,
        )

    @Composable
    private fun Gallery(items: List<Pair<String, ReadingYear.Summary>>) {
        Column(Modifier.fillMaxSize().background(Brand.Ink).padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { (label, s) ->
                Text(label, fontSize = 11.sp, color = Brand.GoldLight)
                PersonalBand(s, Modifier.width(380.dp), still = true)
            }
        }
    }

    @Test fun bandGallery() {
        val secs = listOf(8 to 14, 9 to 5, 3 to 3, 1 to 2, 7 to 1)
        val items = listOf(
            "книжен дракон, 28 книги" to summary(listOf(1, 2, 0, 3, 4, 2, 1, 0, 5, 3, 2, 5), Archetype.BOOKWORM, secs, "Вазов, Иван"),
            "верен почитател" to summary(listOf(0, 1, 2, 1, 0, 0, 3, 1, 0, 0, 5, 2), Archetype.LOYAL, secs.take(3), "Вазов, Иван"),
            "изследовател" to summary(listOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), Archetype.EXPLORER, secs),
            "маратонец" to summary(listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), Archetype.STEADY, secs),
            "една книга" to summary(listOf(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), Archetype.CURIOUS),
            "без книги (подразбираща се)" to summary(List(12) { 0 }, Archetype.CURIOUS),
            "огромни числа" to summary(List(12) { (it + 1) * 90 }, Archetype.BOOKWORM, secs),
        )
        compose.setContent { ChitalishteTheme(mode = ThemeMode.DARK) { Gallery(items) } }
        compose.waitForIdle()
        save(compose.onRoot().captureToImage().asAndroidBitmap(), "w21ys-20-band-gallery")
    }

    // ───────────── бродираните графики в „Моето четене“ ─────────────

    private fun readingStats(months: List<Int>, sections: List<Pair<Int?, Int>>): ReadingStats.Summary =
        ReadingStats.Summary(
            months = months.mapIndexed { i, n -> ReadingStats.MonthCount(YearMonth.of(2025, 11).plusMonths(i.toLong()), n) },
            sections = sections.map { (s, n) -> ReadingStats.SectionCount(s, n) },
            thisYear = 22, favouriteAuthor = "Вазов, Иван", favouriteAuthorCount = 5, longestStreak = 6, total = months.sum(),
        )

    private fun charts(mode: ThemeMode, name: String, stats: ReadingStats.Summary, fontScale: Float = 1f) {
        compose.setContent {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
                ChitalishteTheme(mode = mode) {
                    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(vertical = 16.dp)) {
                        ReadingStatsCard(stats, onOpenHistory = {})
                    }
                }
            }
        }
        shown("Книги по месеци")
        compose.waitForIdle()
        save(compose.onRoot().captureToImage().asAndroidBitmap(), name)
    }

    private val usual = listOf(1, 2, 0, 3, 4, 2, 1, 0, 2, 3, 5, 2)
    private val usualSections = listOf(8 to 14, 9 to 5, 3 to 3, 1 to 2, 7 to 1, null to 2)

    @Test fun chartsLight() = charts(ThemeMode.LIGHT, "w21ys-30-charts-light", readingStats(usual, usualSections))

    @Test fun chartsDark() = charts(ThemeMode.DARK, "w21ys-31-charts-dark", readingStats(usual, usualSections))

    @Test fun chartsLargeFont() = charts(ThemeMode.LIGHT, "w21ys-32-charts-large-font", readingStats(usual, usualSections), fontScale = large)

    /** Месец над 8 книги: клетката се смалява; един раздел: поясът е едноцветен. */
    @Test fun chartsHuge() = charts(
        ThemeMode.LIGHT, "w21ys-33-charts-huge",
        readingStats(listOf(3, 12, 0, 2, 16, 2, 1, 0, 2, 3, 5, 24), listOf(8 to 60)),
    )

    /** Много книги в тъмна тема и едър шрифт: колоните остават под буквите на месеците. */
    @Test fun chartsHugeDarkLargeFont() = charts(
        ThemeMode.DARK, "w21ys-35-charts-huge-dark-large-font",
        readingStats(listOf(3, 12, 0, 2, 16, 2, 1, 0, 2, 3, 5, 24), listOf(8 to 30, 9 to 20, 3 to 10)), fontScale = large,
    )

    @Test fun chartsEmptyMonths() = charts(ThemeMode.DARK, "w21ys-34-charts-empty", readingStats(List(12) { 0 }.toMutableList().also { it[11] = 1 }, listOf(null to 1)))

    // ───────────── намалено движение (Robolectric) ─────────────

    @Composable
    private fun TwoBands(reduced: Boolean) {
        CompositionLocalProvider(LocalReducedMotion provides reduced) {
            ChitalishteTheme(mode = ThemeMode.DARK) {
                Column(Modifier.background(Brand.Ink)) {
                    Box(Modifier.size(380.dp, 140.dp).testTag("live")) { PersonalBand(ShareFixtures.year, Modifier.width(380.dp)) }
                    Box(Modifier.size(380.dp, 140.dp).testTag("still")) { PersonalBand(ShareFixtures.year, Modifier.width(380.dp), still = true) }
                }
            }
        }
    }

    private fun frame(tag: String): Bitmap = compose.onNodeWithTag(tag).captureToImage().asAndroidBitmap()

    /** Дял на различаващите се пиксели (над прага на канал) — изглаждането по ръбовете може да се разминава с пиксел-два. */
    private fun Bitmap.diff(other: Bitmap): Double {
        if (width != other.width || height != other.height) return 1.0
        val a = IntArray(width * height).also { getPixels(it, 0, width, 0, 0, width, height) }
        val b = IntArray(width * height).also { other.getPixels(it, 0, width, 0, 0, width, height) }
        var n = 0
        for (i in a.indices) {
            val x = a[i]; val y = b[i]
            if (kotlin.math.abs((x shr 16 and 255) - (y shr 16 and 255)) > 24 || kotlin.math.abs((x shr 8 and 255) - (y shr 8 and 255)) > 24 ||
                kotlin.math.abs((x and 255) - (y and 255)) > 24) n++
        }
        return n.toDouble() / a.size
    }

    private fun Bitmap.sameAs2(other: Bitmap): Boolean = diff(other).also { println("diff=$it") } < 0.002

    /** При намалено движение лентата е готова от първия кадър (и е същата като неподвижната). */
    @Test fun reducedMotionShowsTheFinishedBandImmediately() {
        compose.mainClock.autoAdvance = false
        compose.setContent { TwoBands(reduced = true) }
        compose.mainClock.advanceTimeBy(0)
        compose.waitForIdle()
        assertTrue(frame("live").sameAs2(frame("still")), "намалено движение — готовата лента от първия кадър")
        compose.mainClock.advanceTimeBy(3_000)
        assertTrue(frame("live").sameAs2(frame("still")), "и след време — без промяна")
    }

    /** Без намалено движение: тръгва празна, бродира се и накрая е същата лента като неподвижната. */
    @Test fun animatedBandStartsEmptyAndEndsAsTheStillOne() {
        compose.mainClock.autoAdvance = false
        compose.setContent { TwoBands(reduced = false) }
        compose.mainClock.advanceTimeBy(16)
        val start = frame("live")
        assertTrue(!start.sameAs2(frame("still")), "в началото още няма бодове")
        compose.mainClock.advanceTimeBy(1_200)
        val mid = frame("live")
        assertTrue(!mid.sameAs2(frame("still")) && !mid.sameAs2(start), "по средата — частично бродирана")
        compose.mainClock.advanceTimeBy(2_000)
        assertTrue(frame("live").sameAs2(frame("still")), "краят на анимацията е същата лента")
    }
}
