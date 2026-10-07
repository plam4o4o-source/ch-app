package org.chyavorec.core

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class OrthodoxEasterTest {
    @Test fun easter2026() = assertEquals(LocalDate.of(2026, 4, 12), OrthodoxEaster.of(2026))
    @Test fun easter2027() = assertEquals(LocalDate.of(2027, 5, 2), OrthodoxEaster.of(2027))
    @Test fun easter2028() = assertEquals(LocalDate.of(2028, 4, 16), OrthodoxEaster.of(2028))
    @Test fun easter2025() = assertEquals(LocalDate.of(2025, 4, 20), OrthodoxEaster.of(2025))
    @Test fun easter2024() = assertEquals(LocalDate.of(2024, 5, 5), OrthodoxEaster.of(2024))
}
