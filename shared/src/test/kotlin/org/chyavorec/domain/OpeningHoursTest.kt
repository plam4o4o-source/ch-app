package org.chyavorec.domain

import org.chyavorec.domain.model.OpenInterval
import org.chyavorec.domain.model.OpenStatus
import org.chyavorec.domain.model.OpeningHours
import java.io.File
import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OpeningHoursTest {
    private val zone = ZoneId.of("Europe/Sofia")

    /** Вграденото работно време (contacts_fallback_hours, values/). */
    private val fallbackBg = listOf(
        "Понеделник – четвъртък: 08:00 – 16:30",
        "Петък: 08:00 – 16:00",
        "Събота и неделя: почивни дни",
    )
    private val fallbackEn = listOf(
        "Monday – Thursday: 08:00 – 16:30",
        "Friday: 08:00 – 16:00",
        "Saturday and Sunday: closed",
    )
    private val hours = OpeningHours.parse(fallbackBg)!!

    private fun t(h: Int, m: Int = 0): LocalTime = LocalTime.of(h, m)
    private fun iv(h1: Int, m1: Int, h2: Int, m2: Int) = OpenInterval(t(h1, m1), t(h2, m2))

    /** 23.11.2026 е понеделник. */
    private fun at(day: Int, h: Int, m: Int = 0, s: Int = 0): ZonedDateTime =
        ZonedDateTime.of(LocalDate.of(2026, 11, day), LocalTime.of(h, m, s), zone)

    private fun week(vararg days: Pair<DayOfWeek, List<OpenInterval>>) = OpeningHours(mapOf(*days))

    // --- Разчитане -------------------------------------------------------------------------

    @Test fun fallbackTextParses() {
        val full = listOf(iv(8, 0, 16, 30))
        assertEquals(
            week(MONDAY to full, TUESDAY to full, WEDNESDAY to full, THURSDAY to full, FRIDAY to listOf(iv(8, 0, 16, 0))),
            hours,
        )
        assertTrue(hours.intervalsOn(SATURDAY).isEmpty())
        assertTrue(hours.intervalsOn(SUNDAY).isEmpty())
    }

    @Test fun englishFallbackGivesTheSameHours() {
        assertEquals(hours, OpeningHours.parse(fallbackEn))
    }

    @Test fun fallbackInResourcesStaysParseable() {
        // Вграденият текст в ресурсите (bg и en) — ако някой го промени, тук се вижда.
        for (dir in listOf("values", "values-en")) {
            val file = File("../app/src/main/res/$dir/strings.xml")
            if (!file.exists()) continue
            val raw = Regex("<string name=\"contacts_fallback_hours\">(.*?)</string>").find(file.readText())!!.groupValues[1]
            assertEquals(hours, OpeningHours.parse(raw.split("\\n")), dir)
        }
    }

    @Test fun abbreviatedRangeWithDots() {
        val h = OpeningHours.parse(listOf("Пон.–Пет.: 9:00 – 17:00"))!!
        for (d in listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)) assertEquals(listOf(iv(9, 0, 17, 0)), h.intervalsOn(d))
        assertTrue(h.intervalsOn(SATURDAY).isEmpty())
    }

    @Test fun dashVariants() {
        val expected = OpeningHours.parse(listOf("Понеделник – петък: 08:00 – 12:00"))
        assertNotNull(expected)
        assertEquals(expected, OpeningHours.parse(listOf("Понеделник — петък: 08:00 — 12:00")))
        assertEquals(expected, OpeningHours.parse(listOf("Понеделник - петък: 08:00-12:00")))
        assertEquals(expected, OpeningHours.parse(listOf("Понеделник\u00A0–\u00A0петък 08.00 ч. – 12.00 ч.")))
        assertEquals(expected, OpeningHours.parse(listOf("От понеделник до петък: от 8:00 до 12:00")))
    }

    @Test fun twoIntervalsInOneLine() {
        val h = OpeningHours.parse(listOf("Вторник: 8:00-12:00 и 13:00-17:00"))!!
        assertEquals(listOf(iv(8, 0, 12, 0), iv(13, 0, 17, 0)), h.intervalsOn(TUESDAY))
        assertTrue(h.intervalsOn(MONDAY).isEmpty())
    }

    @Test fun singleDaysAndEnumerations() {
        val h = OpeningHours.parse(listOf("Сряда: 10:00 – 18:00", "Понеделник, четвъртък и петък: 08:30 – 12:30"))!!
        assertEquals(listOf(iv(10, 0, 18, 0)), h.intervalsOn(WEDNESDAY))
        for (d in listOf(MONDAY, THURSDAY, FRIDAY)) assertEquals(listOf(iv(8, 30, 12, 30)), h.intervalsOn(d))
        assertTrue(h.intervalsOn(TUESDAY).isEmpty())
    }

    @Test fun workdaysKeyword() {
        val h = OpeningHours.parse(listOf("Делнични дни: 9:00 – 17:30", "Събота и неделя: почивни дни"))!!
        for (d in listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)) assertEquals(listOf(iv(9, 0, 17, 30)), h.intervalsOn(d))
        assertTrue(h.intervalsOn(SATURDAY).isEmpty() && h.intervalsOn(SUNDAY).isEmpty())
    }

    @Test fun specificDayWinsOverRange() {
        val h = OpeningHours.parse(listOf("Делнични дни: 08:00 – 17:00", "Петък: 08:00 – 13:00", "Неделя: почивен ден"))!!
        assertEquals(listOf(iv(8, 0, 13, 0)), h.intervalsOn(FRIDAY))
        assertEquals(listOf(iv(8, 0, 17, 0)), h.intervalsOn(MONDAY))
        assertTrue(h.intervalsOn(SUNDAY).isEmpty())
    }

    @Test fun sameDaysOnTwoLinesAreMerged() {
        val h = OpeningHours.parse(listOf("Понеделник – петък: 8:00 – 12:00", "Понеделник – петък: 13:00 – 17:00"))!!
        assertEquals(listOf(iv(8, 0, 12, 0), iv(13, 0, 17, 0)), h.intervalsOn(WEDNESDAY))
    }

    @Test fun closedDayWithTimesInAnotherLineIgnoresUnknownLines() {
        val h = OpeningHours.parse(
            listOf("Работно време", "Библиотека", "Понеделник: 08:00 – 16:30", "тел. 0888 123 456", "Последвайте ни"),
        )!!
        assertEquals(week(MONDAY to listOf(iv(8, 0, 16, 30))), h)
    }

    @Test fun lunchBreakIsNotAClosedDay() {
        // „почивка“ (обедна) не прави деня почивен — часовете остават.
        val h = OpeningHours.parse(listOf("Понеделник: 08:00 – 12:00 и 13:00 – 17:00 (обедна почивка)"))!!
        assertEquals(listOf(iv(8, 0, 12, 0), iv(13, 0, 17, 0)), h.intervalsOn(MONDAY))
    }

    @Test fun garbageGivesNull() {
        assertNull(OpeningHours.parse(emptyList()))
        assertNull(OpeningHours.parse(listOf("Добре дошли в читалището!", "12:00 – 13:00", "лорем ипсум 42")))
        // Само почивни дни — нито един ден с часове.
        assertNull(OpeningHours.parse(listOf("Събота и неделя: почивни дни")))
        // Обърнат или невъзможен час.
        assertNull(OpeningHours.parse(listOf("Понеделник: 17:00 – 08:00", "Вторник: 25:00 – 26:00")))
    }

    @Test fun resolvePrefersSiteThenFallback() {
        val site = listOf("Понеделник – петък: 09:00 – 18:00")
        assertEquals(OpeningHours.parse(site), OpeningHours.resolve(site, fallbackBg))
        assertEquals(hours, OpeningHours.resolve(listOf("Пишете ни"), fallbackBg))
        assertEquals(hours, OpeningHours.resolve(emptyList(), fallbackBg))
        assertNull(OpeningHours.resolve(emptyList(), emptyList()))
    }

    // --- Състояние --------------------------------------------------------------------------

    @Test fun openUntilClose() {
        assertEquals(OpenStatus.Open(t(16, 30)), hours.status(at(23, 11)))
    }

    @Test fun exactOpeningMinuteIsOpen() {
        assertEquals(OpenStatus.Open(t(16, 30)), hours.status(at(23, 8)))
        // Минута преди отваряне — затворено, отваря днес в 08:00.
        assertEquals(OpenStatus.Closed(LocalDateTime.of(2026, 11, 23, 8, 0)), hours.status(at(23, 7, 59)))
    }

    @Test fun closingSoonFromThirtyMinutesBefore() {
        assertEquals(OpenStatus.Open(t(16, 30)), hours.status(at(23, 15, 59, 59)))
        assertEquals(OpenStatus.ClosingSoon(t(16, 30)), hours.status(at(23, 16, 0)))
        assertEquals(OpenStatus.ClosingSoon(t(16, 30)), hours.status(at(23, 16, 29, 59)))
    }

    @Test fun exactClosingMinuteIsClosed() {
        assertEquals(OpenStatus.Closed(LocalDateTime.of(2026, 11, 24, 8, 0)), hours.status(at(23, 16, 30)))
    }

    @Test fun fridayEveningOpensOnMonday() {
        val monday = LocalDateTime.of(2026, 11, 30, 8, 0)
        assertEquals(OpenStatus.Closed(monday), hours.status(at(27, 16, 0)))
        assertEquals(OpenStatus.Closed(monday), hours.status(at(28, 12)))
        assertEquals(OpenStatus.Closed(monday), hours.status(at(29, 23, 59)))
    }

    @Test fun lunchBreakOpensLaterToday() {
        val h = OpeningHours.parse(listOf("Понеделник – петък: 8:00-12:00 и 13:00-17:00"))!!
        assertEquals(OpenStatus.Closed(LocalDateTime.of(2026, 11, 23, 13, 0)), h.status(at(23, 12, 15)))
        assertEquals(OpenStatus.ClosingSoon(t(12, 0)), h.status(at(23, 11, 45)))
        assertEquals(OpenStatus.Open(t(17, 0)), h.status(at(23, 13, 0)))
    }

    @Test fun statusUsesSofiaTimeWhateverTheZoneOfNow() {
        // 06:30 UTC = 08:30 в София (зимно време).
        val utc = ZonedDateTime.ofInstant(Instant.parse("2026-11-23T06:30:00Z"), ZoneId.of("UTC"))
        assertEquals(OpenStatus.Open(t(16, 30)), hours.status(utc))
    }

    @Test fun nextOpeningWrapsAroundTheWeek() {
        // Само понеделник: в понеделник след затваряне — следващият понеделник.
        val h = week(MONDAY to listOf(iv(9, 0, 12, 0)))
        assertEquals(OpenStatus.Closed(LocalDateTime.of(2026, 11, 30, 9, 0)), h.status(at(23, 12, 0)))
        assertEquals(OpenStatus.Unknown, OpeningHours(emptyMap()).status(at(23, 12)))
    }

    @Test fun nextChangeInstants() {
        fun next(z: ZonedDateTime) = hours.nextChange(z).atZone(zone).toLocalDateTime()
        assertEquals(LocalDateTime.of(2026, 11, 23, 8, 0), next(at(23, 6)))
        assertEquals(LocalDateTime.of(2026, 11, 23, 16, 0), next(at(23, 8)))
        assertEquals(LocalDateTime.of(2026, 11, 23, 16, 30), next(at(23, 16, 0)))
        // След затваряне — полунощ („утре“ става „днес“).
        assertEquals(LocalDateTime.of(2026, 11, 24, 0, 0), next(at(23, 16, 30)))
        assertEquals(LocalDateTime.of(2026, 11, 29, 0, 0), next(at(28, 10)))
    }

    @Test fun lastClosingBeforeSkipsClosedDays() {
        assertEquals(LocalDateTime.of(2026, 11, 27, 16, 0), hours.lastClosingBefore(LocalDate.of(2026, 11, 29)))
        assertEquals(LocalDateTime.of(2026, 11, 26, 16, 30), hours.lastClosingBefore(LocalDate.of(2026, 11, 27)))
    }
}
