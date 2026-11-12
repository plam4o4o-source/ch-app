package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertTrue

/** Намалено движение: skeleton-ът е неподвижен (без отблясък), а не само по-бавен. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg")
class ReducedMotionTest {
    @get:Rule val compose = createComposeRule()

    @Test fun shimmerIsStillWithReducedMotion() {
        compose.mainClock.autoAdvance = false
        var base = Color.Unspecified
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                CompositionLocalProvider(LocalReducedMotion provides true) {
                    base = MaterialTheme.colorScheme.surfaceContainerHigh
                    Box(Modifier.size(120.dp).testTag("skeleton").shimmer())
                }
            }
        }
        // Отблясъкът минава за 1.3 s — в няколко момента от цикъла кадърът е един и същ.
        // Фонът е „канава“ (бледа шевица при ~6%): близо до основния цвят, но не точно той.
        var first: List<Int>? = null
        repeat(4) { step ->
            compose.mainClock.advanceTimeBy(350)
            val pixels = compose.onNodeWithTag("skeleton").captureToImage().toPixelMap()
            val all = (0 until pixels.width).flatMap { x -> (0 until pixels.height).map { y -> pixels[x, y].toArgb() } }
            val b = base.toArgb()
            val far = all.count { c ->
                (0..2).any { k -> kotlin.math.abs(((c shr (16 - 8 * k)) and 0xFF) - ((b shr (16 - 8 * k)) and 0xFF)) > 24 }
            }
            assertTrue(far == 0, "стъпка $step: $far точки далеч от основния цвят (отблясък)")
            if (first == null) first = all else assertTrue(first == all, "стъпка $step: кадърът се е променил")
        }
    }
}
