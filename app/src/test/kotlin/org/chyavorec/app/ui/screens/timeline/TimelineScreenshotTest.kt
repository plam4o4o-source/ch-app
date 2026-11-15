package org.chyavorec.app.ui.screens.timeline

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.quiz.QuizDefaults
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * „Лента на времето“ след преработката: само годината в медалията (и при шрифт 1.3/2.0), цялата
 * дата в картата, десетилетието в лентата отгоре, дръжката в лявото поле и надписът при влачене.
 * Снимките са за преглед (клонът „screenshots“); проверките — какво чува TalkBack в лентата.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class TimelineScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val entries = QuizDefaults.timeline

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Screen(
        dark: Boolean = false,
        fontScale: Float = 1f,
        listState: LazyListState,
        scrubber: Boolean = false,
        pill: Boolean = false,
    ) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides true) {
            ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Scaffold(topBar = { TimelineTopBar(entries, listState, {}, thisYear = 2026) }) { p ->
                        Box(Modifier.padding(p)) {
                            Timeline(entries, staticFrame = true, listState = listState, showScrubber = scrubber, thisYear = 2026, showDecadePill = pill)
                        }
                    }
                }
            }
        }
    }

    private fun barSays(): List<String> = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription))
        .fetchSemanticsNodes().flatMap { it.config[SemanticsProperties.ContentDescription] }
        .filter { it.startsWith("Лента на времето, ") }

    @Test fun scrolledLight() {
        compose.setContent { Screen(listState = rememberLazyListState(7)) }
        shot("tl24-01-scrolled-light")
    }

    @Test fun scrolledDarkLargeFont() {
        compose.setContent { Screen(dark = true, fontScale = 1.3f, listState = rememberLazyListState(11), scrubber = true) }
        shot("tl24-02-scrolled-dark-font13")
    }

    @Test fun draggingShowsThePill() {
        compose.setContent { Screen(listState = rememberLazyListState(9), scrubber = true, pill = true) }
        shot("tl24-03-dragging-pill")
    }

    /** „17 март 1958“ — най-дългият етикет; при шрифт 2.0 медалията пак е само „1958“. */
    @Test fun hugeFontMedallions() {
        compose.setContent { Screen(fontScale = 2f, listState = rememberLazyListState(11)) }
        shot("tl24-04-font20")
    }

    @Config(qualifiers = "bg-w320dp-h640dp-xhdpi")
    @Test fun narrowDarkLargeFontWithPill() {
        compose.setContent { Screen(dark = true, fontScale = 1.3f, listState = rememberLazyListState(5), scrubber = true, pill = true) }
        shot("tl24-05-320-dark-font13-pill")
    }

    @Test fun barIsSilentAtTheTopAndNamesTheDecadeWhenScrolled() {
        compose.setContent { Screen(listState = rememberLazyListState(0)) }
        compose.waitForIdle()
        assertEquals(emptyList(), barSays(), "горе голямото заглавие е в списъка — лентата мълчи")
    }

    @Test fun barNamesTheDecadeBeingRead() {
        compose.setContent { Screen(listState = rememberLazyListState(9)) }
        compose.waitForIdle()
        val said = barSays()
        assertEquals(1, said.size, "лентата: $said")
        // Ред 9 = веха 7 (1936) горе; на третина от екрана е тя или следващата (1947).
        assertTrue(said.single() in listOf("Лента на времето, 1930-те години", "Лента на времето, 1940-те години"), said.single())
    }

    @Test fun atTheEndTheBarSaysToday() {
        compose.setContent { Screen(listState = rememberLazyListState(entries.size + 1)) }
        compose.waitForIdle()
        assertEquals(listOf("Лента на времето, 2020-те години"), barSays())
    }

    @Test fun platesAreDrawnOncePerSize() {
        val plates = RosettePlates(Color(0xFFE8D9A8), Color(0x80785A10), 2f)
        val a = plates.plate(128, 128)
        assertSame(a, plates.plate(128, 128))
        val b = plates.plate(48, 48)
        assertNotSame(a, b)
        assertSame(a, plates.plate(128, 128))
        assertEquals(128, a.width)
    }
}
