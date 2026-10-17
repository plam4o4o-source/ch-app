package org.chyavorec.core

import org.chyavorec.core.WaitDuration.Unit.HOURS
import org.chyavorec.core.WaitDuration.Unit.MINUTES
import org.chyavorec.core.WaitDuration.Unit.SECONDS
import org.chyavorec.data.repository.LoginThrottle
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class WaitDurationTest {
    @Test fun underAMinuteInSeconds() {
        assertEquals(WaitDuration(42, SECONDS), WaitDuration.of(42))
        assertEquals(WaitDuration(0, SECONDS), WaitDuration.of(-5))
    }

    @Test fun firstLockoutIsOneMinute() {
        // Броячът на заключването закръгля нагоре (61 s за 60-секундно заключване).
        assertEquals(WaitDuration(1, MINUTES), WaitDuration.of(61))
        assertEquals(WaitDuration(2, MINUTES), WaitDuration.of(121))
        assertEquals(WaitDuration(5, MINUTES), WaitDuration.of(300))
    }

    @Test fun neverZeroOrSixtyMinutes() {
        assertEquals(WaitDuration(1, MINUTES), WaitDuration.of(60))
        assertEquals(WaitDuration(59, MINUTES), WaitDuration.of(3599))
    }

    @Test fun longestLockoutIsAboutAnHour() {
        // 3840 s = 64 мин — „около 1 час“, а не „3840 секунди“.
        assertEquals(WaitDuration(1, HOURS, approximate = true), WaitDuration.of(3841))
        assertEquals(WaitDuration(2, HOURS, approximate = false), WaitDuration.of(7200))
    }

    @Test fun throttleExposesAttemptsWithoutChangingLockout() {
        val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
        val t = LoginThrottle(clock)
        assertEquals(5, t.attemptsLeft())
        repeat(3) { t.onFailure() }
        assertEquals(3, t.failedAttempts())
        assertEquals(2, t.attemptsLeft())
        repeat(2) { t.onFailure() }
        // Заключено за 60 s; броячът на опитите започва отначало.
        assertEquals(61, t.remainingLockSeconds())
        assertEquals(0, t.failedAttempts())
        assertEquals(5, t.attemptsLeft())
        t.onSuccess()
        assertEquals(0, t.remainingLockSeconds())
    }
}
