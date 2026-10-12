package org.chyavorec.app.ui.theme

import org.chyavorec.core.OrthodoxEaster
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Мотивите на шевицата по дата (празници, Великден) и генерираните корици. */
class OrnamentsTest {
    private fun d(y: Int, m: Int, day: Int) = LocalDate.of(y, m, day)

    @Test fun orthodoxEasterDates() {
        assertEquals(d(2021, 5, 2), OrthodoxEaster.of(2021))
        assertEquals(d(2023, 4, 16), OrthodoxEaster.of(2023))
        assertEquals(d(2026, 4, 12), OrthodoxEaster.of(2026))
        assertEquals(d(2027, 5, 2), OrthodoxEaster.of(2027))
        assertEquals(d(2029, 4, 8), OrthodoxEaster.of(2029))
        assertEquals(d(2030, 4, 28), OrthodoxEaster.of(2030))
        // Винаги неделя.
        for (y in 2000..2100) assertEquals(java.time.DayOfWeek.SUNDAY, OrthodoxEaster.of(y).dayOfWeek, "$y")
    }

    @Test fun holidays() {
        assertEquals(BgHoliday.CHRISTMAS, ShevitsaCalendar.holiday(d(2026, 12, 25)))
        assertEquals(BgHoliday.CHRISTMAS, ShevitsaCalendar.holiday(d(2027, 1, 7)))
        assertNull(ShevitsaCalendar.holiday(d(2027, 1, 8)))
        assertEquals(BgHoliday.LIBERATION_DAY, ShevitsaCalendar.holiday(d(2026, 3, 3)))
        assertEquals(BgHoliday.GERGYOVDEN, ShevitsaCalendar.holiday(d(2026, 5, 6)))
        assertEquals(BgHoliday.BUDITELI, ShevitsaCalendar.holiday(d(2026, 11, 1)))
        assertNull(ShevitsaCalendar.holiday(d(2026, 11, 2)))
        // Великден 2026 — 12 април, прозорец ±7 дни.
        assertEquals(BgHoliday.EASTER, ShevitsaCalendar.holiday(d(2026, 4, 12)))
        assertEquals(BgHoliday.EASTER, ShevitsaCalendar.holiday(d(2026, 4, 5)))
        assertEquals(BgHoliday.EASTER, ShevitsaCalendar.holiday(d(2026, 4, 19)))
        assertNull(ShevitsaCalendar.holiday(d(2026, 4, 20)))
    }

    @Test fun fixedHolidaysWinOverEasterWindow() {
        // 2021: Великден на 2 май — Гергьовден (6 май) е в прозореца му, но има предимство.
        assertEquals(BgHoliday.GERGYOVDEN, ShevitsaCalendar.holiday(d(2021, 5, 6)))
        assertEquals(BgHoliday.EASTER, ShevitsaCalendar.holiday(d(2021, 5, 4)))
    }

    @Test fun motifByHolidayAndSeason() {
        assertEquals(ShevitsaMotif.ROSETTE, ShevitsaCalendar.motifFor(d(2026, 12, 24)))
        assertEquals(ShevitsaMotif.TREE_OF_LIFE, ShevitsaCalendar.motifFor(d(2026, 4, 12)))
        assertEquals(ShevitsaMotif.RHOMBUS, ShevitsaCalendar.motifFor(d(2026, 3, 3)))
        assertEquals(ShevitsaMotif.ZIGZAG, ShevitsaCalendar.motifFor(d(2026, 5, 6)))
        assertEquals(ShevitsaMotif.TREE_OF_LIFE, ShevitsaCalendar.motifFor(d(2026, 11, 1)))
        // Сезони извън празниците.
        assertEquals(ShevitsaMotif.ROSETTE, ShevitsaCalendar.motifFor(d(2026, 2, 10)))
        assertEquals(ShevitsaMotif.TREE_OF_LIFE, ShevitsaCalendar.motifFor(d(2026, 5, 20)))
        assertEquals(ShevitsaMotif.ZIGZAG, ShevitsaCalendar.motifFor(d(2026, 7, 15)))
        assertEquals(ShevitsaMotif.RHOMBUS, ShevitsaCalendar.motifFor(d(2026, 10, 12)))
    }

    @Test fun motifIsDeterministic() {
        var day = d(2026, 1, 1)
        while (day.year == 2026) {
            assertEquals(ShevitsaCalendar.motifFor(day), ShevitsaCalendar.motifFor(LocalDate.of(day.year, day.month, day.dayOfMonth)))
            day = day.plusDays(1)
        }
    }

    @Test fun bookMotif() {
        assertEquals(ShevitsaMotif.ROSETTE, GeneratedCover.motifFor(8, "Книга"))
        assertEquals(ShevitsaMotif.RHOMBUS, GeneratedCover.motifFor(9, ""))
        assertEquals(ShevitsaMotif.ZIGZAG, GeneratedCover.motifFor(5, "Книга"))
        assertEquals(ShevitsaMotif.TREE_OF_LIFE, GeneratedCover.motifFor(1, "Книга"))
        assertEquals(ShevitsaMotif.RHOMBUS, GeneratedCover.motifFor(null, ""))
        assertEquals(ShevitsaMotif.ZIGZAG, GeneratedCover.motifFor(8, "Видеодокумент"))
    }

    @Test fun stableHashIsFnv1a() {
        assertEquals(0x811C9DC5.toInt(), GeneratedCover.stableHash(""))
        assertEquals(0xE40C292C.toInt(), GeneratedCover.stableHash("a"))
        assertEquals(GeneratedCover.stableHash("Иван Вазов"), GeneratedCover.stableHash("Иван Вазов"))
    }

    @Test fun coverBackgroundIsStableAndReadable() {
        val authors = listOf("", "Вазов, Иван", "Йовков, Йордан", "Елин Пелин", "Христо Ботев", "Agatha Christie", "Толкин", "Ран Босилек") +
            (1..300).map { "Автор $it" }
        for (palette in ThemePalette.entries) {
            for (dark in listOf(false, true)) {
                for (a in authors) {
                    val c = GeneratedCover.backgroundArgb(a, palette.coverAnchors, dark)
                    assertEquals(c, GeneratedCover.backgroundArgb(a, palette.coverAnchors, dark), "стабилен")
                    // Регистърът и интервалите в краищата не сменят цвета.
                    assertEquals(c, GeneratedCover.backgroundArgb("  " + a.uppercase() + " ", palette.coverAnchors, dark))
                    assertTrue(GeneratedCover.readableOn(c), "$palette dark=$dark '$a'")
                    assertTrue(ColorMath.contrast(GeneratedCover.INK, c) >= 4.5)
                }
            }
        }
    }

    @Test fun differentAuthorsGetDifferentColours() {
        val colours = (1..50).map { GeneratedCover.backgroundArgb("Автор $it", ThemePalette.YAVORETS.coverAnchors, false) }.toSet()
        assertTrue(colours.size >= 20, "${colours.size}")
    }

    @Test fun themeStyleRoundTrip() {
        for (p in ThemePalette.entries) {
            for (oled in listOf(false, true)) {
                val s = ThemeStyle(p, oled)
                assertEquals(s, ThemeStyle.decode(s.encode()))
            }
        }
        assertEquals(ThemeStyle(), ThemeStyle.decode(null))
        assertEquals(ThemeStyle(), ThemeStyle.decode(""))
        assertEquals(ThemeStyle(ThemePalette.YAVORETS, oledBlack = true), ThemeStyle.decode("UNKNOWN+oled"))
        assertEquals("LEVSKI+oled", ThemeStyle(ThemePalette.LEVSKI, true).encode())
    }
}
