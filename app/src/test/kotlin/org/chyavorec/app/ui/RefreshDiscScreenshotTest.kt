package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.ConnectedButtonGroup
import org.chyavorec.app.ui.components.LeafRefreshBox
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.SearchInput
import org.chyavorec.app.ui.components.appFilterChipColors
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Розетката на „дръпни за опресняване“ върху своя диск — над сегментите, търсачката и
 * чиповете на „Новини“ (там без подложка се губеше): 60 %, прагът и опресняване; светла и тъмна.
 * За визуален преглед (клонът „screenshots“); не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1240dp-xhdpi")
class RefreshDiscScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Горната част на „Новини“: сегменти, търсачка, чипове — под индикатора, дръпнат до [fraction]. */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun NewsHeader(fraction: Float, refreshing: Boolean = false) {
        val state = rememberPullToRefreshState()
        LaunchedEffect(Unit) { state.snapTo(fraction) }
        LeafRefreshBox(refreshing, onRefresh = {}, state = state, modifier = Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.background)) {
            Column(Modifier.fillMaxSize()) {
                ConnectedButtonGroup(listOf(false, true), false, {}, Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) { cal, _ ->
                    Text(if (cal) "Календар" else "Новини")
                }
                SearchInput("", {}, "Търси в новините", Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp))
                Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(true, {}, label = { Text("Всички") }, colors = appFilterChipColors())
                    FilterChip(false, {}, label = { Text("Запазени") }, colors = appFilterChipColors())
                    FilterChip(false, {}, label = { Text("Събитие") }, colors = appFilterChipColors())
                }
            }
        }
    }

    @Test fun refreshDiscOverNewsHeader() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Column {
                    listOf(false, true).forEach { dark ->
                        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                            NewsHeader(0.6f)
                            NewsHeader(1f)
                            NewsHeader(1f, refreshing = true)
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(400)
        shot("w20m-01-refresh-disc")
    }
}
