package org.chyavorec.core

import org.chyavorec.core.Equinoxes.Kind
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EquinoxesTest {
    private fun near(expectedUtc: String, year: Int, kind: Kind) {
        val diff = Duration.between(Instant.parse(expectedUtc), Equinoxes.instant(year, kind)).abs()
        assertTrue(diff.toMinutes() <= 5, "$year $kind: ${Equinoxes.instant(year, kind)} vs $expectedUtc")
    }

    @Test fun matchesPublishedTimes() {
        near("2024-03-20T03:06:00Z", 2024, Kind.MARCH)
        near("2024-06-20T20:51:00Z", 2024, Kind.JUNE)
        near("2024-09-22T12:44:00Z", 2024, Kind.SEPTEMBER)
        near("2024-12-21T09:21:00Z", 2024, Kind.DECEMBER)
        near("2025-03-20T09:01:00Z", 2025, Kind.MARCH)
        near("2025-06-21T02:42:00Z", 2025, Kind.JUNE)
        near("2025-09-22T18:19:00Z", 2025, Kind.SEPTEMBER)
        near("2025-12-21T15:03:00Z", 2025, Kind.DECEMBER)
    }

    @Test fun datesAreInSofiaTime() {
        // 2024: 20 юни 20:51 UTC = 23:51 в София — още 20 юни.
        assertEquals(LocalDate.of(2024, 6, 20), Equinoxes.date(2024, Kind.JUNE))
        // 2026: есенното равноденствие е в 00:05 UTC = 03:05 в София на 23 септември.
        assertEquals(LocalDate.of(2026, 9, 23), Equinoxes.date(2026, Kind.SEPTEMBER))
        assertEquals(LocalDate.of(2026, 3, 20), Equinoxes.date(2026, Kind.MARCH))
        assertEquals(LocalDate.of(2026, 12, 21), Equinoxes.date(2026, Kind.DECEMBER))
    }
}
