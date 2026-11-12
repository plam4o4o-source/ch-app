package org.chyavorec.app.ui.screens.events

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Думите на листа на събитието: броят дни, множественото число (bg и en), часът и мотивът в заглавката. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class EventLeafTest {
    private val resources get() = ApplicationProvider.getApplicationContext<Application>().resources

    @Test fun daysAreCountedFromToday() {
        val today = LocalDate.of(2026, 11, 12)
        assertEquals(19, EventCountdown.days(LocalDate.of(2026, 12, 1), today))
        assertEquals(0, EventCountdown.days(today, today))
        assertEquals(-3, EventCountdown.days(LocalDate.of(2026, 11, 9), today))
    }

    @Test fun bulgarianCountdown() {
        assertEquals("днес", EventCountdown.label(resources, 0))
        assertEquals("след 1 ден", EventCountdown.label(resources, 1))
        assertEquals("след 2 дни", EventCountdown.label(resources, 2))
        assertEquals("след 19 дни", EventCountdown.label(resources, 19))
        assertEquals("след 21 дни", EventCountdown.label(resources, 21))
        assertEquals("преди 1 ден", EventCountdown.label(resources, -1))
        assertEquals("преди 5 дни", EventCountdown.label(resources, -5))
        assertEquals("след 365 дни", EventCountdown.label(resources, 365))
    }

    @Config(qualifiers = "en")
    @Test fun englishCountdown() {
        assertEquals("today", EventCountdown.label(resources, 0))
        assertEquals("in 1 day", EventCountdown.label(resources, 1))
        assertEquals("in 19 days", EventCountdown.label(resources, 19))
        assertEquals("1 day ago", EventCountdown.label(resources, -1))
        assertEquals("3 days ago", EventCountdown.label(resources, -3))
    }

    @Test fun weekdayNamesFollowTheLanguage() {
        // 12 ноември 2026 е четвъртък.
        assertEquals("четвъртък", weekdayName(resources, LocalDate.of(2026, 11, 12)))
        assertEquals("неделя", weekdayName(resources, LocalDate.of(2026, 11, 15)))
    }

    @Config(qualifiers = "en")
    @Test fun weekdayNamesEnglish() {
        assertEquals("Thursday", weekdayName(resources, LocalDate.of(2026, 11, 12)))
        assertEquals("Monday", weekdayName(resources, LocalDate.of(2026, 11, 9)))
    }

    @Test fun clockIsNormalisedOrKeptOrAbsent() {
        assertEquals("18:30", eventClock("18:30"))
        assertEquals("18:30", eventClock("18.30"))
        assertEquals("09:05", eventClock(" 9:05 "))
        assertEquals("вечерта", eventClock("вечерта"))
        assertNull(eventClock(null))
        assertNull(eventClock("  "))
    }

    @Test fun categoryChoosesTheHeaderMotifElseTheSeason() {
        val d = LocalDate.of(2026, 7, 10)
        assertEquals(ShevitsaMotif.ROSETTE, leafMotif("Концерт", d))
        assertEquals(ShevitsaMotif.TREE_OF_LIFE, leafMotif("Изложба", d))
        assertEquals(ShevitsaMotif.ZIGZAG, leafMotif("Празник", d))
        // Непознат раздел — шевицата на сезона (лято — кривулица), без дата — ромб.
        assertEquals(ShevitsaMotif.ZIGZAG, leafMotif("Читалище", d))
        assertEquals(ShevitsaMotif.RHOMBUS, leafMotif(null, null))
    }
}
