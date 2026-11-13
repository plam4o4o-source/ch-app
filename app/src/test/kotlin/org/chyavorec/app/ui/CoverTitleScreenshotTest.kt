package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Генерираните корици с дълги и къси български думи в заглавието (в малки и в голяма корица, при
 * обикновен и едър шрифт): думите се пренасят със сричкопренасяне („Арме-ния“), не се късат
 * насред без тире („Армени|я“). За визуален преглед (клонът „screenshots“); не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class CoverTitleScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private val books = listOf(
        CatalogBook(1, "Иван Вазов", "Армения", udc = "9"),
        CatalogBook(2, "Димчо Дебелянов", "Останеш", udc = "821"),
        CatalogBook(3, "Йордан Радичков", "Суматоха", udc = "821"),
        CatalogBook(4, "Йордан Йовков", "Старопланински легенди", udc = "821"),
        CatalogBook(5, "Елин Пелин", "Непрокопсаниците", udc = "821"),
    )

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Page(dark: Boolean, fontScale: Float, content: @Composable () -> Unit) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides true) {
                Column(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) { content() }
            }
        }
    }

    private fun covers(name: String, dark: Boolean = false, fontScale: Float = 1f) {
        compose.setContent {
            Page(dark, fontScale) {
                for (w in listOf(56, 72, 96)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        books.forEach { BookCover(it, width = w.dp, showStatusDot = false, decorative = true) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    books.take(3).forEach { BookCover(it, width = 112.dp, showStatusDot = false, decorative = true) }
                }
            }
        }
        shot(name)
    }

    @Test fun coverTitlesLight() = covers("w22-cover-titles-light")

    @Test fun coverTitlesDark() = covers("w22-cover-titles-dark", dark = true)

    @Test fun coverTitlesFont13() = covers("w22-cover-titles-font13", fontScale = 1.3f)

    @Test fun coverTitlesFont2() = covers("w22-cover-titles-font2", fontScale = 2f)
}
