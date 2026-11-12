package org.chyavorec.app.diagnostics

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.captureScreen
import org.chyavorec.app.ui.screens.settings.PerfSection
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** „Бързодействие“ с версии, профил, първи старт след обновяване и сравнение — и при много едър шрифт. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class PerfSectionScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun fixture(): PerfSummary {
        var d = PerfStats.noteVersion(PerfData(), 35, "1.2.35")
        d = PerfStats.noteVersion(d, 36, "1.2.36")
        repeat(4) { i -> d = PerfStats.addStart(d, StartupSample(true, 240L + i, 310L + i, 35)) }
        d = PerfStats.addStart(d, StartupSample(false, 36, 80, 35))
        d = PerfStats.addFrames(d, listOf(RouteFrames("card/full", 400, 22, 159, 35), RouteFrames("home", 900, 8, 70, 35)))
        d = PerfStats.addStart(d, StartupSample(true, 420, 510, 36))
        repeat(3) { i -> d = PerfStats.addStart(d, StartupSample(true, 205L + i, 270L + i, 36)) }
        d = PerfStats.addStart(d, StartupSample(false, 33, 70, 36))
        d = PerfStats.addFrames(
            d,
            listOf(
                RouteFrames("card/full", 400, 6, 48, 36, worstTotalMs = 92, sumTotalMs = 5200),
                RouteFrames("home", 900, 7, 41, 36, worstTotalMs = 66, sumTotalMs = 9900),
            ),
        )
        return PerfStats.summary(d, 36, "1.2.36")
    }

    private fun shot(name: String, mode: ThemeMode, fontScale: Float) {
        val summary = fixture()
        compose.setContent {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
                ChitalishteTheme(mode = mode) {
                    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp).verticalScroll(rememberScrollState())) {
                        PerfSection(summary, ProfileState.of(compiled = false, enqueued = true, code = 2))
                    }
                }
            }
        }
        compose.waitForIdle()
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun perfCardLight() = shot("perf-card-versions", ThemeMode.LIGHT, 1f)

    @Test fun perfCardDark() = shot("perf-card-versions-dark", ThemeMode.DARK, 1f)

    @Test fun perfCardLargeFont() = shot("perf-card-versions-font2", ThemeMode.LIGHT, 2f)
}
