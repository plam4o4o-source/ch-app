package org.chyavorec.app.diagnostics

import android.app.Application
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.screens.settings.PerfSection
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** „Диагностика › Бързодействие“: медианата и последният старт и най-насечените екрани. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class PerfSectionTest {
    @get:Rule val compose = createComposeRule()

    private fun show(summary: PerfSummary) {
        compose.setContent { ChitalishteTheme { PerfSection(summary) } }
        compose.waitForIdle()
    }

    private fun count(text: String, n: Int = 1) = compose.onAllNodes(hasText(text, substring = true)).assertCountEquals(n)

    @Test fun showsStartsAndJankyRoutes() {
        val data = PerfData(
            starts = listOf(
                StartupSample(true, 1000, 1500), StartupSample(true, 1200, 1300), StartupSample(true, 900, null),
                StartupSample(false, 280, 350),
            ),
            routes = listOf(RouteFrames("catalog", 500, 50, 120), RouteFrames("home", 1000, 30, 80)),
        )
        show(PerfStats.summary(data))
        count("Бързодействие")
        count("Студен старт · 3")
        count("Първи кадър: медиана 1000 мс, последен 900 мс")
        count("Пълно показване: медиана 1400 мс, последно 1300 мс")
        count("Топъл старт · 1")
        count("Първи кадър: медиана 280 мс, последен 280 мс")
        count("Насечени кадри по екрани")
        count("catalog: 10,0 % от 500 кадъра · най-бавен 120 мс")
        count("home: 3,0 % от 1000 кадъра · най-бавен 80 мс")
        count("Още няма измервания.", 0)
    }

    @Test fun emptyShowsNoMeasurements() {
        show(PerfStats.summary(PerfData()))
        count("Бързодействие")
        count("Още няма измервания.")
        count("Студен старт", 0)
        count("Насечени кадри", 0)
    }
}
