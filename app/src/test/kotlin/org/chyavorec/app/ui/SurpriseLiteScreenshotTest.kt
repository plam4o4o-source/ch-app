package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.screens.catalog.SurpriseMotion
import org.chyavorec.app.ui.screens.catalog.SurpriseOverlay
import org.chyavorec.app.ui.screens.catalog.SurpriseUi
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * „Изненадай ме“ на Android 8–11 ([SurpriseMotion.LITE]): същото разбъркване, но без 3D и без
 * сенки на слоя — обръщането е свиване по ширина, сянката е нарисувана. И жестът „назад“:
 * сцената се смалява и избледнява, отказан — връща се. Името на „прозореца“ за TalkBack.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class SurpriseLiteScreenshotTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val book = CatalogBook(7, "Иван Вазов", "Под игото", available = true, year = "1894")
    private var closed = 0

    private fun save(bmp: Bitmap, name: String) =
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun capture(): Bitmap = compose.onRoot().captureToImage().asAndroidBitmap()

    private fun strip(frames: List<Bitmap>, heightPx: Int): Bitmap {
        val w = frames.first().width
        val out = Bitmap.createBitmap(w * frames.size, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        frames.forEachIndexed { i, f -> canvas.drawBitmap(f, (i * w).toFloat(), 0f, null) }
        return out
    }

    private fun show(motion: SurpriseMotion, mode: ThemeMode = ThemeMode.LIGHT) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                SurpriseOverlay(
                    SurpriseUi(open = true, round = 1, loading = false, pick = book), motion,
                    onOpen = {}, onAgain = {}, onClose = { closed++ },
                )
            }
        }
    }

    private fun paneTitle(): String? = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.PaneTitle))
        .fetchSemanticsNodes().singleOrNull()?.config?.get(SemanticsProperties.PaneTitle)

    /** Кадри: ветрилото, разбъркването, обръщането (свиване и разгъване) и резултатът. */
    @Test fun liteDeckShufflesAndFlipsWithoutPerspective() {
        show(SurpriseMotion.LITE)
        assertEquals("Изненадай ме", paneTitle())
        val frames = listOf(420L, 580L, 60L, 60L, 60L, 820L).map { t ->
            compose.mainClock.advanceTimeBy(t)
            capture()
        }
        // Горната половина — там е тестето.
        save(strip(frames, heightPx = frames.first().height / 2), "96-surprise-lite-frames")
        compose.mainClock.autoAdvance = true
        compose.waitUntil(5_000) { compose.onAllNodes(hasText("Към книгата")).fetchSemanticsNodes().isNotEmpty() }
        compose.mainClock.advanceTimeBy(600)
        save(capture(), "96-surprise-lite-reveal")
        assertEquals("Предложение", paneTitle())
    }

    /** Жестът „назад“ на половина: сцената е смалена (~0,95) и избледняла; отказан — пак цяла, пуснат — затваря. */
    @Test fun predictiveBackScalesTheSceneAndCancelRestoresIt() {
        show(SurpriseMotion.FULL, ThemeMode.DARK)
        compose.mainClock.autoAdvance = true
        compose.waitUntil(5_000) { compose.onAllNodes(hasText("Към книгата")).fetchSemanticsNodes().isNotEmpty() }
        compose.mainClock.advanceTimeBy(600)
        val back = compose.activity.onBackPressedDispatcher
        compose.runOnUiThread {
            back.dispatchOnBackStarted(BackEventCompat(0f, 900f, 0f, BackEventCompat.EDGE_LEFT))
            back.dispatchOnBackProgressed(BackEventCompat(300f, 900f, 0.5f, BackEventCompat.EDGE_LEFT))
        }
        compose.waitForIdle()
        val half = capture()
        compose.runOnUiThread { back.dispatchOnBackCancelled() }
        compose.mainClock.advanceTimeBy(1_000)
        val restored = capture()
        save(strip(listOf(half, restored), heightPx = half.height), "97-surprise-back-gesture")
        assertEquals(0, closed)
        compose.runOnUiThread { back.onBackPressed() }
        compose.waitForIdle()
        assertTrue(closed == 1, "„назад“ не затвори")
    }
}
