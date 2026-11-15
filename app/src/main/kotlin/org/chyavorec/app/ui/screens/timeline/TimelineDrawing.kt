package org.chyavorec.app.ui.screens.timeline

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt
import org.chyavorec.app.ui.components.DashStitch
import org.chyavorec.app.ui.components.FolkShapes
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.scaledPath

/*
 * Рисуването на лентата — евтино при превъртане: осморъката на медалията се рисува веднъж за
 * размера в картинка (пътят ѝ с пунктирния пръстен струваше ~50 ms/кадър на софтуерен GPU), а
 * конецът е поредица от къси заоблени черти вместо пунктирен ефект.
 */

/**
 * Готовите плочки-осморъки (същата форма и градиент като [org.chyavorec.app.ui.components.FolkIconPlate],
 * с пръстен от тичащ бод) — по една картинка за размер, обща за всички медалии и дръжката на плъзгача.
 */
internal class RosettePlates(private val container: Color, private val ring: Color, private val density: Float) {
    private val keys = LongArray(SLOTS) { -1L }
    private val images = arrayOfNulls<ImageBitmap>(SLOTS)
    private var next = 0

    fun plate(width: Int, height: Int): ImageBitmap {
        val w = width.coerceAtLeast(1)
        val h = height.coerceAtLeast(1)
        val key = (w.toLong() shl 32) or h.toLong()
        for (i in 0 until SLOTS) if (keys[i] == key) return images[i]!!
        val image = ImageBitmap(w, h)
        CanvasDrawScope().draw(Density(density), LayoutDirection.Ltr, Canvas(image), Size(w.toFloat(), h.toFloat())) {
            val r = size.minDimension / 2f
            val outer = FolkShapes.rosette.scaledPath(r * 0.98f, size.width / 2f, size.height / 2f)
            drawPath(outer, Brush.linearGradient(listOf(container, container.copy(alpha = container.alpha * 0.55f))))
            drawCircle(ring, radius = r * 0.74f, style = StitchSpec.Running.stroke(density))
        }
        keys[next] = key
        images[next] = image
        next = (next + 1) % SLOTS
        return image
    }

    private companion object {
        /** Медалията и дръжката (и един резервен размер). */
        const val SLOTS = 3
    }
}

/** Плочките за текущата тема и плътност. */
@Composable
internal fun rememberRosettePlates(): RosettePlates {
    val container = MaterialTheme.colorScheme.primaryContainer
    val ring = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
    val density = LocalDensity.current.density
    return remember(container, ring, density) { RosettePlates(container, ring, density) }
}

/** Фонът-осморъка: готовата картинка от [plates] (без пътища при рисуване). */
internal fun Modifier.rosettePlate(plates: RosettePlates): Modifier = drawWithCache {
    val image = plates.plate(size.width.roundToInt(), size.height.roundToInt())
    onDrawBehind { drawImage(image) }
}

/**
 * Отвесен конец от [top] до [bottom] в бода [spec] — същите черти и празнини като пунктирния му
 * ефект, но като отделни заоблени правоъгълници (бърз примитив; без алокации).
 */
internal fun DrawScope.drawStitchRun(spec: DashStitch, color: Color, x: Float, top: Float, bottom: Float) {
    val w = spec.widthDp * density
    val dash = spec.dashDp * density
    val period = (spec.dashDp + spec.gapDp) * density
    val corner = CornerRadius(w / 2f)
    var y = top
    while (y < bottom) {
        val len = minOf(dash, bottom - y)
        if (len >= w) drawRoundRect(color, Offset(x - w / 2f, y), Size(w, len), corner)
        y += period
    }
}
