package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.screens.donate.DonateFormState
import org.chyavorec.app.ui.screens.donate.DonateMemoryContent
import org.chyavorec.app.ui.screens.donate.DonatePhoto
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals

/**
 * „Дари спомен“: празният екран и попълненият (четири снимки, полетата, съгласието) — светла и
 * тъмна тема, шрифт 1.3. Висок прозорец — целият екран в една снимка. Освен визуалния преглед:
 * „Изпрати“ е активен едва със снимка и съгласие, „×“ маха снимката.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1500dp-xhdpi")
class DonateScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** „Стара снимка“: сепия с тъмен кръг (лице/фигура) — различна за всяка. */
    private fun oldPhoto(seed: Int, width: Int = 320, height: Int = 240): DonatePhoto {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val light = android.graphics.Color.rgb(222 - seed * 8, 200 - seed * 6, 160 - seed * 5)
        val dark = android.graphics.Color.rgb(120 - seed * 6, 92 - seed * 4, 60)
        c.drawRect(0f, 0f, width.toFloat(), height.toFloat(), Paint().apply { shader = LinearGradient(0f, 0f, 0f, height.toFloat(), light, dark, Shader.TileMode.CLAMP) })
        c.drawCircle(width * (0.3f + seed * 0.12f), height * 0.45f, height * 0.22f, Paint().apply { color = android.graphics.Color.rgb(70, 52, 36) })
        c.drawRect(0f, height * 0.78f, width.toFloat(), height.toFloat(), Paint().apply { color = android.graphics.Color.rgb(96, 74, 50) })
        return DonatePhoto(File("spomen-$seed.jpg"), bmp.asImageBitmap())
    }

    private fun filledState() = DonateFormState().apply {
        repeat(4) { photos += oldPhoto(it) }
        who = "Баба Мара и дядо Иван на сбора"
        time = "около 1960 г."
        place = "пред читалището"
        contact = "Мария Петрова, 0888 123 456"
        consent = true
    }

    private fun show(state: DonateFormState, mode: ThemeMode = ThemeMode.LIGHT, fontScale: Float = 1f) {
        compose.setContent {
            FontScale(fontScale) {
                CompositionLocalProvider(LocalReducedMotion provides true) {
                    ChitalishteTheme(mode = mode) {
                        DonateMemoryContent(state, onBack = {}, onPick = {}, onRemove = { state.photos.remove(it) }, onSend = {})
                    }
                }
            }
        }
    }

    @Composable
    private fun FontScale(scale: Float, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = scale), content = content)
    }

    @Test fun emptyLight() {
        show(DonateFormState())
        compose.onNodeWithText("Избери снимки").assertExists()
        compose.onNode(hasText("Изпрати") and hasClickAction()).assertIsNotEnabled()
        compose.onNodeWithText("Избери поне една снимка.").assertExists()
        shot("w24-donate-01-empty-light")
    }

    @Test fun emptyDark() {
        show(DonateFormState(), ThemeMode.DARK)
        shot("w24-donate-02-empty-dark")
    }

    @Test fun filledLight() {
        val state = filledState()
        show(state)
        compose.onNode(hasText("Изпрати") and hasClickAction()).assertIsEnabled()
        compose.onNodeWithText("Съгласен/съгласна", substring = true).assertIsOn()
        shot("w24-donate-03-filled-light")
        // Без съгласие — неактивен; „×“ маха снимката.
        compose.onNodeWithText("Съгласен/съгласна", substring = true).performScrollTo().performClick()
        compose.onNode(hasText("Изпрати") and hasClickAction()).assertIsNotEnabled()
        compose.onNodeWithContentDescription("Махни снимка 2").performClick()
        compose.waitForIdle()
        assertEquals(3, state.photos.size)
    }

    @Test fun filledDark() {
        show(filledState(), ThemeMode.DARK)
        shot("w24-donate-04-filled-dark")
    }

    @Test fun filledFont13() {
        show(filledState(), fontScale = 1.3f)
        shot("w24-donate-05-filled-font13")
    }

    @Config(qualifiers = "bg-w411dp-h891dp-xhdpi")
    @Test fun filledPhoneHeight() {
        show(filledState().apply { failed = 1 })
        shot("w24-donate-06-filled-phone")
    }
}
