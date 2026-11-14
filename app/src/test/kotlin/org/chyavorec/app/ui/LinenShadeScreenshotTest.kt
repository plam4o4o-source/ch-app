package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.ClothWeave
import org.chyavorec.app.ui.components.linenBrush
import org.chyavorec.app.ui.components.linenTintFilter
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.GeneratedCover
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.ui.theme.ThemePalette
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Ленената подвързия: предишният Overlay (вляво) и сянката на лена със SrcOver (вдясно) върху
 * фонове на корици от светлата и тъмната тема и върху светли цветове; отдолу — истински корици.
 * За визуален преглед (клонът „screenshots“); равенството на цветовете проверява FolkVisualsTest.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class LinenShadeScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun overlayVersusShade() {
        val bases = buildList {
            add(GeneratedCover.backgroundArgb("Вазов, Иван", ThemePalette.entries.first().coverAnchors, false))
            add(GeneratedCover.backgroundArgb("Йовков, Йордан", ThemePalette.entries.first().coverAnchors, false))
            add(GeneratedCover.backgroundArgb("Вазов, Иван", ThemePalette.entries.first().coverAnchors, true))
            add(GeneratedCover.backgroundArgb("Радичков, Йордан", ThemePalette.entries.last().coverAnchors, true))
            add(0xFFC9A84C.toInt())
            add(0xFFEDE3CC.toInt())
        }.map { Color(it) }
        val overlay = ShaderBrush(ImageShader(ClothWeave.linen, TileMode.Repeated, TileMode.Repeated))
        val shade = linenBrush()
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(Modifier.background(Color.White).padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Overlay (преди)  |  сянка SrcOver (сега)", fontSize = 12.sp, color = Color.Black)
                    bases.forEach { base ->
                        val tint = linenTintFilter(base)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(
                                Modifier.size(150.dp, 52.dp).background(base).drawBehind {
                                    drawRect(overlay, alpha = GeneratedCover.CLOTH_ALPHA, blendMode = BlendMode.Overlay)
                                },
                            )
                            Spacer(
                                Modifier.size(150.dp, 52.dp).background(base).drawBehind {
                                    drawRect(shade, alpha = GeneratedCover.CLOTH_ALPHA, colorFilter = tint)
                                },
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf(
                            CatalogBook(1, "Вазов, Иван", "Под игото", udc = "821"),
                            CatalogBook(2, "Йовков, Йордан", "Старопланински легенди", udc = "821"),
                            CatalogBook(3, "Колев, Симеон", "Белият детството", udc = "3"),
                        ).forEach { BookCover(it, width = 96.dp, showStatusDot = false) }
                    }
                }
            }
        }
        shot("w23-linen-overlay-vs-shade")
    }
}
