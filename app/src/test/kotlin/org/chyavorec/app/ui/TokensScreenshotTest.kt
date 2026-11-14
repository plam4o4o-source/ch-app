package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.AppTopBar
import org.chyavorec.app.ui.components.ConnectedButtonGroup
import org.chyavorec.app.ui.components.CrossStitchBatch
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.PillToggle
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.StitchedMotif
import org.chyavorec.app.ui.components.appTonalButtonColors
import org.chyavorec.app.ui.components.drawCrossStitches
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Общият речник (вълна 23): трите бода ([StitchSpec]), етикетът с бодове, избраният сегмент
 * (един цвят в двете теми), хапчето-превключвател, тоналният бутон, картата и картата-акцент,
 * горната лента без „нощна черга“ — светла и тъмна тема и шрифт 1.3. За визуален преглед.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class TokensScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Caption(text: String) = Text(text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Sheet(dark: Boolean, fontScale: Float) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides true) {
                Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    AppTopBar("Читалището", onSearch = {})
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionLabel("Бодовете")
                        val gold = LocalExtendedColors.current.gold
                        Caption("тичащ бод · тропосване")
                        Canvas(Modifier.fillMaxWidth().height(24.dp)) {
                            val run = StitchSpec.Running.stroke(density)
                            val bast = StitchSpec.Basting.stroke(density)
                            drawLine(gold, Offset(0f, 6.dp.toPx()), Offset(size.width, 6.dp.toPx()), run.width, run.cap, run.pathEffect)
                            drawLine(gold, Offset(0f, 18.dp.toPx()), Offset(size.width, 18.dp.toPx()), bast.width, bast.cap, bast.pathEffect)
                        }
                        Caption("кръстат бод · с кант (платното)")
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            StitchedMotif(StitchCharts.band(3), progress = { 1f }, modifier = Modifier.size(150.dp, 28.dp))
                            val batch = remember { CrossStitchBatch(2) }
                            Canvas(Modifier.size(150.dp, 28.dp).background(Brand.Parchment)) {
                                val cell = size.height / 2f
                                batch.reset()
                                for (i in 0 until 10) batch.add(i % 2, i * cell, (i % 2) * cell, cell)
                                drawCrossStitches(batch, cell, thread = { if (it == 0) Brand.Burgundy else Brand.Gold }, outline = { if (it == 0) Brand.Ink else Brand.GoldDark })
                            }
                        }
                        ConnectedButtonGroup(listOf("Новини", "Календар"), "Календар", {}, Modifier.fillMaxWidth()) { o, _ -> Text(o) }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PillToggle(false, {}, label = { Text("Курсор") }, leadingIcon = { Icon(Icons.Outlined.TouchApp, null) })
                            PillToggle(true, {}, label = { Text("Искам да прочета") }, leadingIcon = { Icon(Icons.Outlined.BookmarkBorder, null) })
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton({}, colors = appTonalButtonColors()) {
                                Icon(Icons.Outlined.Email, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Имейл")
                            }
                            Button({}) { Text("Напред") }
                        }
                        AppCard(Modifier.fillMaxWidth()) { Text("Карта", Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium) }
                        AppCard(Modifier.fillMaxWidth(), accent = true, onClick = {}, onClickLabel = "Отвори") {
                            Text("Карта-акцент (златна рамка)", Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }

    @Test fun tokensLight() {
        compose.setContent { Sheet(dark = false, fontScale = 1f) }
        shot("w23-tokens-light")
    }

    @Test fun tokensDark() {
        compose.setContent { Sheet(dark = true, fontScale = 1f) }
        shot("w23-tokens-dark")
    }

    @Test fun tokensDarkFont13() {
        compose.setContent { Sheet(dark = true, fontScale = 1.3f) }
        shot("w23-tokens-dark-font13")
    }

    /** Хапчето-превключвател е избираемо за TalkBack (като чипа преди него) и сменя състоянието си. */
    @Test fun pillToggleIsSelectable() {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                val on = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
                PillToggle(on.value, { on.value = !on.value }, label = { Text("Курсор") })
            }
        }
        val node = compose.onNode(hasText("Курсор"), useUnmergedTree = false)
        node.assertIsNotSelected().performClick()
        node.assertIsSelected()
    }
}
