package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.EmptyAnimation
import org.chyavorec.app.ui.components.EmptyMotion
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.AppError
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Индикаторът за зареждане, анимираните празни състояния и монохромната икона —
 * за визуален преглед (клонът „screenshots“). Не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1600dp-xhdpi")
class VisualsScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Label(text: String, dark: Boolean = false) =
        Text(text, fontSize = 11.sp, color = if (dark) Color(0xFFBBBBBB) else Color(0xFF555555))

    @Composable
    private fun LoadingRow(dark: Boolean) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChLoadingIndicator()
                ChLoadingIndicator(Modifier.size(28.dp))
                Box(Modifier.background(Color.Black).padding(6.dp)) { ChLoadingIndicator(Modifier.size(22.dp), null, color = Brand.GoldLight) }
                CompositionLocalProvider(LocalReducedMotion provides true) { ChLoadingIndicator() }
                Label("48 / 28 / 22 dp, неподвижен", dark)
            }
        }
    }

    @Test fun loadingIndicator() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                Column { LoadingRow(dark = false); LoadingRow(dark = true) }
            }
        }
        compose.mainClock.advanceTimeBy(700)
        shot("40-loading-indicator")
    }

    /** Всеки вид в няколко кадъра от цикъла (0 = покой). */
    @Test fun emptyAnimationFrames() {
        val frames = listOf(0f, 0.25f, 0.45f, 0.65f)
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(Modifier.background(MaterialTheme.colorScheme.background).padding(8.dp)) {
                    EmptyMotion.entries.forEach { m ->
                        Label(m.name.lowercase())
                        Row {
                            frames.forEach { f ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    EmptyAnimation(m, Modifier.size(96.dp, 72.dp), stillFrame = f)
                                    Label("t=$f")
                                }
                            }
                        }
                    }
                }
            }
        }
        shot("41-empty-animation-frames")
    }

    /** Истинските празни състояния (в покой) — светла и тъмна тема. */
    private fun emptyStates(name: String, dark: Boolean) {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Column {
                            Row {
                                EmptyView("Няма резултати", icon = Icons.Outlined.SearchOff, modifier = Modifier.width(205.dp))
                                EmptyView("Няма съобщения", icon = Icons.Outlined.Campaign, modifier = Modifier.width(205.dp))
                            }
                            Row {
                                EmptyView("Няма история", icon = Icons.Outlined.AutoStories, modifier = Modifier.width(205.dp))
                                EmptyView("Няма запазени", icon = Icons.Outlined.BookmarkBorder, modifier = Modifier.width(205.dp))
                            }
                            Row {
                                Box(Modifier.width(205.dp)) { ErrorView(AppError.Network, onRetry = {}) }
                                EmptyView("Статично (новини)", icon = Icons.Outlined.Newspaper, modifier = Modifier.width(205.dp))
                            }
                        }
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun emptyStatesLight() = emptyStates("42-empty-states-light", dark = false)

    @Test fun emptyStatesDark() = emptyStates("43-empty-states-dark", dark = true)

    /** Монохромният слой на иконата, оцветен като тематична икона (с кръга на безопасната зона). */
    @Test fun themedLauncherIcon() {
        val themes = listOf(Color(0xFFD8E2F7) to Color(0xFF26446E), Color(0xFF1E2A38) to Color(0xFFC3D6F0), Color(0xFFEFE3CF) to Color(0xFF5B4314))
        compose.setContent {
            Column(Modifier.background(Color.White).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    themes.forEach { (bg, fg) ->
                        // 108 dp слой, видимата част е кръг 72 dp; безопасната зона — 66 dp (червеният кръг).
                        Box(Modifier.size(144.dp).clip(CircleShape).background(bg), contentAlignment = Alignment.Center) {
                            Image(
                                painterResource(R.drawable.ic_launcher_monochrome), null,
                                colorFilter = ColorFilter.tint(fg), modifier = Modifier.size(216.dp),
                            )
                            Box(Modifier.size(132.dp).border(1.dp, Color.Red.copy(alpha = 0.4f), CircleShape))
                        }
                    }
                }
                // В реален размер (48 dp).
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    themes.forEach { (bg, fg) ->
                        Box(Modifier.size(48.dp).clip(CircleShape).background(bg), contentAlignment = Alignment.Center) {
                            Image(painterResource(R.drawable.ic_launcher_monochrome), null, colorFilter = ColorFilter.tint(fg), modifier = Modifier.size(72.dp))
                        }
                    }
                }
            }
        }
        shot("44-themed-launcher-icon")
    }
}
