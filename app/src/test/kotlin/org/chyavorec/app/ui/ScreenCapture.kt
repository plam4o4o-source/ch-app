package org.chyavorec.app.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.PixelCopy
import android.view.Surface
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import org.robolectric.util.ReflectionHelpers
import kotlin.math.roundToInt

/**
 * Снимка на екрана. С отворен диалог или лист (отделни прозорци) — всички прозорци един
 * върху друг ([captureAllWindows]); captureToImage() рисува само прозореца на активността.
 */
fun ComposeTestRule.captureScreen(): Bitmap {
    waitForIdle()
    val roots = onAllNodes(isRoot()).fetchSemanticsNodes().size
    return if (roots <= 1) onRoot().captureToImage().asAndroidBitmap() else captureAllWindows()
}

/**
 * Всички прозорци на приложението, по реда им (като UiAutomation.takeScreenshot в Robolectric),
 * със затъмняването зад диалог (FLAG_DIM_BEHIND). Robolectric слага всеки прозорец горе вляво,
 * затова мястото му е по gravity (диалогът — в средата, както на телефона).
 */
fun captureAllWindows(): Bitmap {
    val global = Class.forName("android.view.WindowManagerGlobal").getMethod("getInstance").invoke(null)
    val roots = list<Any>(ReflectionHelpers.getField(global, "mRoots"))
    val params = list<WindowManager.LayoutParams>(ReflectionHelpers.getField(global, "mParams"))
    val windows = roots.zip(params)
        .map { (root, p) -> Triple(root, ReflectionHelpers.callInstanceMethod<View>(root, "getView"), p) }
        .filter { (_, view, _) -> view.isShown && view.width > 0 && view.height > 0 }
        .sortedBy { it.third.type }
    val base = (windows.firstOrNull { it.third.type == WindowManager.LayoutParams.TYPE_BASE_APPLICATION } ?: windows.first()).second
    val screen = Rect(0, 0, base.width, base.height)
    val out = Bitmap.createBitmap(screen.width(), screen.height(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(out)
    for ((root, view, p) in windows) {
        if (p.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND != 0) {
            canvas.drawColor(Color.argb((p.dimAmount * 255).roundToInt(), 0, 0, 0))
        }
        // Robolectric рисува прозореца синхронно (ShadowPixelCopy); слушателят не е нужен.
        val window = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val surface = ReflectionHelpers.getField<Surface>(root, "mSurface")
        PixelCopy.request(surface, null, window, {}, Handler(Looper.getMainLooper()))
        val at = Rect()
        Gravity.apply(p.gravity, view.width, view.height, screen, p.x, p.y, at)
        canvas.drawBitmap(window, at.left.toFloat(), at.top.toFloat(), null)
    }
    return out
}

@Suppress("UNCHECKED_CAST")
private fun <T> list(value: Any?): List<T> = when (value) {
    is Array<*> -> value.toList() as List<T>
    is List<*> -> value as List<T>
    else -> error("неочакван тип: ${value?.javaClass}")
}
