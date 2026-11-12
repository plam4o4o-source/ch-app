package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BookmarkIcon
import org.chyavorec.app.ui.components.HemState
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.stitchedHem
import org.chyavorec.app.ui.screens.my.ReturnStampCard
import org.chyavorec.app.ui.screens.my.StampAnim
import org.chyavorec.app.ui.screens.my.StampGeometry
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.HistoryItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Печатът „Върната“ (миг от удара, поставен, тъмна тема, едър шрифт), подгъвът на жеста „назад“
 * и падащата отметка. За визуален преглед (клонът „screenshots“); не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class ReturnStampScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private val book = HistoryItem("L1", 1, "Под игото", "Иван Вазов", "2026-09-20", "2026-11-11")

    /** Картата в зададена фаза на анимацията. */
    @Composable
    private fun Frame(dark: Boolean, press: Float, ink: Float, slide: Float, fontScale: Float = 1f, counter: Int = 5) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = fontScale)) {
                val anim = remember { StampAnim() }
                val geo = remember { StampGeometry() }
                LaunchedEffect(Unit) {
                    anim.enter.snapTo(1f); anim.press.snapTo(press); anim.ink.snapTo(ink); anim.slide.snapTo(slide)
                }
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(vertical = 8.dp)) {
                    ReturnStampCard(book, anim, geo, counter, 2026, onOpenHistory = {})
                }
            }
        }
    }

    @Test fun stampFrames() {
        compose.setContent {
            Column {
                Frame(dark = false, press = 0.55f, ink = 0.45f, slide = 0f, counter = 4)
                Frame(dark = false, press = 1f, ink = 1f, slide = 0f, counter = 4)
                Frame(dark = false, press = 1f, ink = 1f, slide = 0.6f, counter = 4)
                Frame(dark = true, press = 1f, ink = 1f, slide = 0f)
            }
        }
        shot("w21-stamp-01-frames")
    }

    @Test fun stampLargeFontAndStaticReducedMotion() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Column {
                    Frame(dark = false, press = 1f, ink = 1f, slide = 0f, fontScale = 1.6f)
                    Frame(dark = true, press = 1f, ink = 1f, slide = 0f, fontScale = 1.6f)
                }
            }
        }
        shot("w21-stamp-02-large-font")
    }

    /** Подгъвът при ~35 % и ~75 % от жеста — екранът е „смален“ с кутия за ориентир. */
    @Test fun hemFrames() {
        compose.setContent {
            Row {
                listOf(false to 0.35f, true to 0.35f, false to 0.75f).forEach { (dark, p) ->
                    ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                        val hem = remember { HemState() }
                        Box(
                            Modifier.size(130.dp, 280.dp).background(MaterialTheme.colorScheme.background)
                                .stitchedHem(hem, progress = { p }, leaving = { true }, seeking = { true }),
                            contentAlignment = Alignment.Center,
                        ) { Text("p=$p", color = MaterialTheme.colorScheme.onBackground) }
                    }
                }
            }
        }
        shot("w21-hem-01-gesture")
    }

    /** Отметката по средата на падането (~60 ms) и поставена, в двете теми. */
    @Test fun bookmarkDrop() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                var saved by remember { mutableStateOf(false) }
                // Превключването е в композицията (по виртуалния часовник), иначе кадърът го пропуска.
                LaunchedEffect(Unit) { delay(50); saved = true }
                Column {
                    listOf(false, true).forEach { dark ->
                        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                            Row(Modifier.fillMaxWidth().height(80.dp).background(MaterialTheme.colorScheme.background).padding(16.dp)) {
                                BookmarkIcon(saved, "Запази", LocalExtendedColors.current.gold, Modifier.size(48.dp))
                            }
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(120)
        shot("w21-bookmark-01-mid-drop")
        compose.mainClock.advanceTimeBy(500)
        shot("w21-bookmark-02-settled")
    }
}
