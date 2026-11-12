package org.chyavorec.app.ui.screens.onboarding

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Въведението показва истинската идентичност: печат в тъкана рамка, възли вместо плочки, нишка
 * вместо точки и примерна читателска карта. Страниците на въведението са в снимките 07*, 54*, 55.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class OnboardingDecorTest {
    @get:Rule val compose = createComposeRule()

    @Test fun threadDotsNameThePage() {
        compose.setContent { ChitalishteTheme(mode = ThemeMode.DARK) { ThreadDots(3, position = { 1f }, current = 1) } }
        compose.onNodeWithContentDescription("Страница 2 от 3").assertIsDisplayed()
    }

    /** При намалено движение картата е същата, но неподвижна (без сензор и без цикъл от кадри в покой). */
    @Test fun sampleCardIsTheRealCardAndSettlesWithReducedMotion() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme(mode = ThemeMode.DARK) { SampleCard(large = false) }
            }
        }
        // Стига се до покой (waitForIdle не виси на безкрайна анимация) и данните са примерните.
        compose.waitForIdle()
        compose.onNodeWithContentDescription("Примерна читателска карта").assertIsDisplayed()
        compose.onNodeWithText("Мария Иванова", useUnmergedTree = true).assertIsDisplayed()
    }

    /** Снимка за преглед: печатът, възлите, нишката в три положения и примерната карта — тъмен фон като във въведението. */
    @Test fun decorPreview() {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(
                    Modifier.fillMaxWidth().background(Brand.Ink).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OnboardingRibbon()
                    SealMedallion(200.dp, remember { WeaveBudget(cycles = 1) })
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        KnotPlate(Icons.Outlined.FormatSize, 52.dp)
                        KnotPlate(Icons.Outlined.FormatSize, 104.dp)
                    }
                    ThreadDots(3, position = { 0f }, current = 0)
                    ThreadDots(3, position = { 0.5f }, current = 0)
                    ThreadDots(3, position = { 2f }, current = 2)
                    SampleCard(large = false)
                }
            }
        }
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File("build/reports/screenshots").apply { mkdirs() }.resolve("w21-onboarding-decor.png")
            .outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
