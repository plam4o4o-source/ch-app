package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Отметката „пада“ само при запазване и без намалено движение; иначе веднага е на мястото си. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "w200dp-h200dp-xhdpi")
class BookmarkIconTest {
    @get:Rule val compose = createComposeRule()

    private fun frames(reduced: Boolean, initiallySaved: Boolean = false): Pair<android.graphics.Bitmap, android.graphics.Bitmap> {
        compose.mainClock.autoAdvance = false
        val saved = mutableStateOf(initiallySaved)
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides reduced) {
                Box(Modifier.size(80.dp).background(Color.White).testTag("box")) {
                    BookmarkIcon(saved.value, null, Color.Black, Modifier.size(48.dp))
                }
            }
        }
        compose.mainClock.advanceTimeBy(50)
        compose.runOnUiThread { saved.value = !initiallySaved }
        compose.mainClock.advanceTimeBy(100)
        compose.waitForIdle()
        val early = compose.onNodeWithTag("box").captureToImage().asAndroidBitmap().copy(android.graphics.Bitmap.Config.ARGB_8888, false)
        compose.mainClock.advanceTimeBy(600)
        compose.waitForIdle()
        val end = compose.onNodeWithTag("box").captureToImage().asAndroidBitmap().copy(android.graphics.Bitmap.Config.ARGB_8888, false)
        return early to end
    }

    @Test fun savingDropsTheRibbon() {
        val (early, end) = frames(reduced = false)
        assertFalse(early.sameAs(end), "по средата на падането изглежда различно")
    }

    @Test fun reducedMotionSnapsToTheFinalFrame() {
        val (early, end) = frames(reduced = true)
        assertTrue(early.sameAs(end), "без анимация е веднага на място")
    }

    @Test fun removingSnapsEvenWithMotion() {
        val (early, end) = frames(reduced = false, initiallySaved = true)
        assertTrue(early.sameAs(end), "отмяната не е анимирана")
    }
}
