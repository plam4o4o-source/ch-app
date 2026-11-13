package org.chyavorec.app.ui.screens.newspaper

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

/** Листът от стария вестник: по-едро прерисуване (малко различна пропорция) не прекъсва щипването. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class PaperSheetGestureTest {
    @get:Rule val compose = createComposeRule()

    private fun page(w: Int, h: Int): RenderedPage {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        return RenderedPage(0, bmp.asImageBitmap(), w, h, bmp)
    }

    @Test fun aBoostedRerenderWithADifferentAspectDoesNotRestartThePinch() {
        var shown by mutableStateOf(page(1200, 1700))
        var settled = 1f
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    ZoomablePaper(shown, 0, "Страница", onZoomSettled = { settled = it })
                }
            }
        }
        val paper = compose.onNodeWithContentDescription("Страница")
        // Два пръста са долу и вече се раздалечават...
        paper.performTouchInput {
            val c = center
            down(0, Offset(c.x - 60f, c.y))
            down(1, Offset(c.x + 60f, c.y))
            repeat(4) { i ->
                updatePointerTo(0, Offset(c.x - 80f - 20f * i, c.y))
                updatePointerTo(1, Offset(c.x + 80f + 20f * i, c.y))
                move()
            }
        }
        // ...когато по-едрата картинка пристига с пропорция, закръглена другояче...
        compose.runOnUiThread { shown = page(3600, 5101) }
        compose.waitForIdle()
        // ...и жестът продължава със същите пръсти.
        paper.performTouchInput {
            val c = center
            repeat(6) { i ->
                updatePointerTo(0, Offset(c.x - 160f - 25f * i, c.y))
                updatePointerTo(1, Offset(c.x + 160f + 25f * i, c.y))
                move()
            }
            up(0)
            up(1)
        }
        compose.mainClock.advanceTimeBy(400)
        assertTrue(settled > 1.5f, "щипването продължава след смяната на картинката (мащаб $settled)")
    }
}
