package org.chyavorec.core

import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BulgarianDatesTest {
    @Test fun textualDateWithYear() =
        assertEquals(LocalDate.of(2026, 10, 12), BulgarianDates.findDate("Концерт на 12 октомври 2026 г. в салона", 2025))

    @Test fun textualDateWithoutYearUsesReference() =
        assertEquals(LocalDate.of(2026, 3, 1), BulgarianDates.findDate("Баба Марта — 1 март", 2026))

    @Test fun abbreviatedMonth() =
        assertEquals(LocalDate.of(2026, 12, 24), BulgarianDates.findDate("на 24 дек. 2026", 2026))

    @Test fun numericDate() =
        assertEquals(LocalDate.of(2026, 9, 12), BulgarianDates.findDate("Заета: 12.09.2026", 2020))

    @Test fun firstDateWins() =
        assertEquals(LocalDate.of(2026, 5, 2), BulgarianDates.findDate("От 2 май 2026 до 10.05.2026", 2026))

    @Test fun invalidDateIgnored() = assertNull(BulgarianDates.findDate("31.02.2026 и нищо друго", 2026))

    @Test fun noDate() = assertNull(BulgarianDates.findDate("Без дата тук", 2026))

    @Test fun timeWithSuffix() = assertEquals(LocalTime.of(18, 30), BulgarianDates.findTime("Начало 18.30 ч. в салона"))

    @Test fun timeWithColon() = assertEquals(LocalTime.of(9, 0), BulgarianDates.findTime("от 9:00 до обяд"))

    @Test fun dateIsNotMistakenForTime() = assertNull(BulgarianDates.findTime("на 12.10.2026 г."))

    @Test fun formatting() {
        assertEquals("3 октомври 2026", BulgarianDates.formatLong(LocalDate.of(2026, 10, 3)))
        assertEquals("03.10.2026", BulgarianDates.formatShort(LocalDate.of(2026, 10, 3)))
    }
}
