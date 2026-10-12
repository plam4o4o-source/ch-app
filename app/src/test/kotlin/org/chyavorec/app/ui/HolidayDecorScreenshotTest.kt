package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.HolidayOrnament
import org.chyavorec.app.ui.components.HolidaySky
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.occasion.OccasionEffect
import org.chyavorec.domain.occasion.OccasionOrnament
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Празничните орнаменти и ефекти — всички на едно място, за визуален преглед
 * (клонът „screenshots“). Не сравнява пиксели; проверява, че всичко се рисува.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1600dp-xhdpi")
class HolidayDecorScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun OrnamentRow(o: OccasionOrnament, quiet: Boolean = false) {
        Row(Modifier.fillMaxWidth().height(44.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(o.name.lowercase() + if (quiet) " (тихо)" else "", fontSize = 11.sp, color = Color(0xFF555555), modifier = Modifier.width(150.dp))
            // Светъл и тъмен фон на заглавката, в реален размер и уголемено ×1.6.
            Box(Modifier.width(60.dp).height(44.dp).background(Color(0xFFFAF7F2)), contentAlignment = Alignment.Center) { HolidayOrnament(o, quiet = quiet) }
            Box(Modifier.width(60.dp).height(44.dp).background(Color(0xFF1A1208)), contentAlignment = Alignment.Center) { HolidayOrnament(o, quiet = quiet) }
            Spacer(Modifier.width(12.dp))
            Box(Modifier.width(100.dp).height(44.dp).background(Color(0xFFFAF7F2)), contentAlignment = Alignment.Center) {
                HolidayOrnament(o, Modifier.scale(1.5f), quiet = quiet)
            }
        }
    }

    @Test fun ornaments() {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(Modifier.background(Color.White).padding(8.dp)) {
                    OccasionOrnament.entries.filter { it != OccasionOrnament.NONE }.forEach { OrnamentRow(it) }
                    OrnamentRow(OccasionOrnament.MEMORIAL_WREATH, quiet = true)
                    OrnamentRow(OccasionOrnament.TRICOLOR, quiet = true)
                }
            }
        }
        shot("30-ornaments")
    }

    private fun effects(name: String, still: Boolean, dark: Boolean, advanceMs: Long) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides still) {
                ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                    Column(Modifier.background(if (dark) Color(0xFF1A1208) else Color(0xFFFAF7F2))) {
                        OccasionEffect.entries.filter { it != OccasionEffect.NONE }.forEach { e ->
                            Box(Modifier.fillMaxWidth().height(110.dp)) {
                                Text(e.name.lowercase(), fontSize = 11.sp, color = Color(0xFF888888), modifier = Modifier.padding(6.dp))
                                HolidaySky(e, Modifier.matchParentSize(), snowColor = if (dark) Color.White else Color(0xFF7A93AB))
                            }
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(advanceMs)
        shot(name)
    }

    @Test fun effectsStill() = effects("31-effects-still", still = true, dark = false, advanceMs = 100)

    @Test fun effectsMoving() = effects("32-effects-moving-3s", still = false, dark = false, advanceMs = 3_000)

    @Test fun effectsMovingDark() = effects("33-effects-moving-dark-6s", still = false, dark = true, advanceMs = 6_000)
}
