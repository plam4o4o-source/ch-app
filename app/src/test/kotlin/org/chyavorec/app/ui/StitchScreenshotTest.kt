package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.ConfettiFrame
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.LeafRefreshBox
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchRefreshIndicator
import org.chyavorec.app.ui.components.StitchedMotif
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Бродираната шевица: кадри от бродирането, дърпането за опресняване по средата,
 * празно състояние и кадър от конфетито (детерминирано — фиксиран seed и спрян часовник).
 * За визуален преглед (клонът „screenshots“); не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class StitchScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Label(text: String) = Text(text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

    @Composable
    private fun MotifRow(dark: Boolean) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf(0.15f, 0.45f, 0.75f, 1f).forEach { p ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            StitchedMotif(StitchCharts.rosette(6), progress = { p }, modifier = Modifier.size(84.dp))
                            Label("p=$p")
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StitchedMotif(StitchCharts.rosette(6), progress = { 1f }, glowPhase = { 0.3f }, modifier = Modifier.size(84.dp))
                        Label("конец")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StitchedMotif(StitchCharts.rhombus(7), progress = { 0.6f }, modifier = Modifier.size(84.dp))
                        Label("ромб 0.6")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StitchedMotif(StitchCharts.rosette(5), progress = { 1f }, modifier = Modifier.size(32.dp))
                        Label("32 dp")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StitchedMotif(StitchCharts.band(4), progress = { 0.55f }, modifier = Modifier.size(112.dp, 24.dp))
                        Label("лента 0.55")
                    }
                }
            }
        }
    }

    @Test fun stitchedMotifs() {
        compose.setContent { Column { MotifRow(dark = false); MotifRow(dark = true) } }
        shot("s19-01-stitch-motifs")
    }

    /** Индикаторът при 30/60/100 % от прага и по време на опресняване; отдолу — списък по средата на дърпането. */
    @OptIn(ExperimentalMaterial3Api::class)
    @Test fun pullToRefreshMidPull() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                Column {
                    listOf(false, true).forEach { dark ->
                        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                            Row(
                                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                listOf(0.3f, 0.6f, 1f).forEach { f -> Pulled(f, refreshing = false) }
                                Pulled(1f, refreshing = true)
                            }
                        }
                    }
                    ChitalishteTheme(mode = ThemeMode.LIGHT) {
                        val state = rememberPullToRefreshState()
                        LaunchedEffect(Unit) { state.snapTo(0.7f) }
                        LeafRefreshBox(false, onRefresh = {}, state = state, modifier = Modifier.fillMaxWidth().height(360.dp).background(MaterialTheme.colorScheme.background)) {
                            LazyColumn(Modifier.fillMaxSize()) {
                                items((1..8).toList()) { i -> ListItem(headlineContent = { Text("Новина номер $i") }) }
                            }
                        }
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(400)
        shot("s19-02-refresh-pull")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Pulled(fraction: Float, refreshing: Boolean) {
        val state: PullToRefreshState = rememberPullToRefreshState()
        LaunchedEffect(Unit) { state.snapTo(fraction) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(84.dp, 140.dp)) { StitchRefreshIndicator(state, refreshing, Modifier.align(Alignment.TopCenter)) }
            Label(if (refreshing) "опресняване" else "${(fraction * 100).toInt()} %")
        }
    }

    /** Празно състояние: лентата под илюстрацията по средата на бродирането и готова (намалено движение). */
    @Test fun emptyStateStitchesIn() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Row(Modifier.background(MaterialTheme.colorScheme.background)) {
                    CompositionLocalProvider(LocalReducedMotion provides false) {
                        EmptyView("Няма запазени новини", "Докосни отметката до новина.", Modifier.weight(1f), icon = Icons.Outlined.BookmarkBorder)
                    }
                    CompositionLocalProvider(LocalReducedMotion provides true) {
                        EmptyView("Няма запазени новини", "Докосни отметката до новина.", Modifier.weight(1f), icon = Icons.Outlined.BookmarkBorder)
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(560)
        shot("s19-03-empty-state")
    }

    /** Кадри от конфетито (фиксиран seed) ~0.3 s и ~0.6 s след успеха — от „бутона“ в средата. */
    @Test fun confettiFrame() {
        var origin = Offset.Zero
        compose.setContent {
            origin = with(LocalDensity.current) { Offset(205.dp.toPx(), 230.dp.toPx()) }
            Column {
                listOf(ThemeMode.LIGHT to 0.27f, ThemeMode.DARK to 0.55f).forEach { (mode, p) ->
                    ChitalishteTheme(mode = mode) {
                        Box(Modifier.fillMaxWidth().height(400.dp).background(MaterialTheme.colorScheme.background)) {
                            Text(
                                "Заявката е изпратена",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.align(Alignment.Center).padding(top = 120.dp),
                            )
                            ConfettiFrame(seed = 19, progress = p, origin = origin, modifier = Modifier.fillMaxSize())
                            Label("t=${(p * 1100).toInt()} ms")
                        }
                    }
                }
            }
        }
        shot("s19-06-confetti")
    }
}
