package org.chyavorec.app.ui.components

import android.app.Application
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.domain.model.OpenStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Надписите на „Отворено сега“ ([OpenNowBadge]): днес / утре / ден от седмицата. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class OpenNowTextTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    /** 27.11.2026 е петък. */
    private val friday = LocalDate.of(2026, 11, 27)

    @Test fun bulgarianTexts() {
        val res = app.resources
        assertEquals("Отворено до 16:30", openNowText(res, OpenStatus.Open(LocalTime.of(16, 30)), friday))
        assertEquals("Затваря скоро — до 16:00", openNowText(res, OpenStatus.ClosingSoon(LocalTime.of(16, 0)), friday))
        assertEquals("Затворено · отваря днес в 13:00", openNowText(res, OpenStatus.Closed(friday.atTime(13, 0)), friday))
        assertEquals("Затворено · отваря утре в 08:00", openNowText(res, OpenStatus.Closed(friday.plusDays(1).atTime(8, 0)), friday))
        assertEquals(
            "Затворено · отваря в понеделник в 08:00",
            openNowText(res, OpenStatus.Closed(LocalDateTime.of(2026, 11, 30, 8, 0)), friday),
        )
        assertEquals(
            "Затворено · отваря във вторник в 08:00",
            openNowText(res, OpenStatus.Closed(LocalDateTime.of(2026, 12, 1, 8, 0)), friday),
        )
        assertNull(openNowText(res, OpenStatus.Unknown, friday))
    }

    @Test fun englishTexts() {
        val res = app.createConfigurationContext(Configuration(app.resources.configuration).apply { setLocale(Locale.ENGLISH) }).resources
        assertEquals("Open until 16:30", openNowText(res, OpenStatus.Open(LocalTime.of(16, 30)), friday))
        assertEquals("Closing soon — until 16:00", openNowText(res, OpenStatus.ClosingSoon(LocalTime.of(16, 0)), friday))
        assertEquals("Closed · opens tomorrow at 08:00", openNowText(res, OpenStatus.Closed(friday.plusDays(1).atTime(8, 0)), friday))
        assertEquals(
            "Closed · opens on Monday at 08:00",
            openNowText(res, OpenStatus.Closed(LocalDateTime.of(2026, 11, 30, 8, 0)), friday),
        )
    }
}
