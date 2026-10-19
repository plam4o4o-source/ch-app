package org.chyavorec.app.ui.components

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Пауза за кориците от Open Library след 403/429 — без нова заявка при всяко превъртане. */
class CoverRateLimitTest {
    private val minute = 60_000L

    @Test fun forbiddenAndTooManyRequestsStartTenMinutePause() {
        val limit = CoverRateLimit()
        assertFalse(limit.active(0))
        assertTrue(limit.onHttpError(403, now = 1_000))
        assertTrue(limit.active(1_000))
        assertTrue(limit.active(1_000 + 9 * minute))
        assertFalse(limit.active(1_000 + 10 * minute))

        assertTrue(limit.onHttpError(429, now = 20 * minute))
        assertTrue(limit.active(25 * minute))
        assertFalse(limit.active(31 * minute))
    }

    @Test fun otherErrorsAreNotRateLimits() {
        val limit = CoverRateLimit()
        // 504 — „няма в кеша“ (only-if-cached) при „Корици само по Wi-Fi“; 404 — няма корица; 5xx — сървърът.
        listOf(504, 404, 500, 503, 401).forEach { code ->
            assertFalse(limit.onHttpError(code, now = 0), "$code")
            assertFalse(limit.active(0), "$code")
        }
    }

    @Test fun newLimitExtendsPauseFromNow() {
        val limit = CoverRateLimit()
        limit.onHttpError(403, now = 0)
        limit.onHttpError(403, now = 8 * minute)
        assertTrue(limit.active(15 * minute))
        assertFalse(limit.active(18 * minute))
    }

    @Test fun clockMovedBackDoesNotStretchPause() {
        val limit = CoverRateLimit()
        limit.onHttpError(403, now = 100 * minute)
        // Часовникът е върнат с час — оставащото би било 70 минути; паузата не важи.
        assertFalse(limit.active(40 * minute))
    }
}
