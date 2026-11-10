package org.chyavorec.app.ui.screens.catalog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.components.shevitsaStripPath
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif

internal val CardShape = RoundedCornerShape(10.dp)

/**
 * Гърбът на картата: бордо, тънка златна рамка, ромбът-шевица в средата и ленти с кривулица
 * горе и долу. Пътищата — веднъж за размера (drawWithCache).
 */
@Composable
internal fun CardBack(modifier: Modifier) {
    Spacer(
        modifier.drawWithCache {
            val w = size.width
            val h = size.height
            val inset = 7.dp.toPx()
            val band = 14.dp.toPx()
            val strip = shevitsaStripPath(ShevitsaMotif.ZIGZAG, w - 2 * inset - 8.dp.toPx(), band)
            val center = Path().apply {
                fillType = PathFillType.EvenOdd
                val s = w * 0.46f
                addShevitsaMotif(ShevitsaMotif.RHOMBUS, (w - s) / 2f, (h - s) / 2f, s)
            }
            val corners = Path().apply {
                fillType = PathFillType.EvenOdd
                val s = w * 0.12f
                addShevitsaMotif(ShevitsaMotif.ROSETTE, (w - s) / 2f, h / 2f - w * 0.23f - s * 1.3f, s)
                addShevitsaMotif(ShevitsaMotif.ROSETTE, (w - s) / 2f, h / 2f + w * 0.23f + s * 0.3f, s)
            }
            val line = Stroke(width = 1.2.dp.toPx())
            val radius = CornerRadius(6.dp.toPx())
            onDrawBehind {
                drawRect(Brand.Burgundy)
                drawRoundRect(Brand.Gold.copy(alpha = 0.8f), Offset(inset, inset), Size(w - 2 * inset, h - 2 * inset), radius, style = line)
                translate(inset + 4.dp.toPx(), inset + 5.dp.toPx()) { drawPath(strip, Brand.Gold.copy(alpha = 0.85f)) }
                translate(inset + 4.dp.toPx(), h - inset - 5.dp.toPx() - band) { drawPath(strip, Brand.Gold.copy(alpha = 0.85f)) }
                drawPath(center, Brand.Gold)
                drawPath(corners, Brand.GoldLight.copy(alpha = 0.7f))
            }
        },
    )
}

/**
 * Нарисувана мека сянка под картата (за [SurpriseMotion.LITE]): няколко полупрозрачни заоблени
 * правоъгълника, отместени надолу — без сянката на слоя, която старият HWUI смята при всеки кадър.
 */
internal fun Modifier.cardShadow(): Modifier = drawWithCache {
    val step = 1.6.dp.toPx()
    val dy = 4.dp.toPx()
    val radius = 10.dp.toPx()
    onDrawBehind {
        for (i in SHADOW_STEPS downTo 1) {
            val grow = i * step
            drawRoundRect(
                Color.Black.copy(alpha = 0.075f),
                topLeft = Offset(-grow, dy - grow * 0.5f),
                size = Size(size.width + 2 * grow, size.height + grow * 1.5f),
                cornerRadius = CornerRadius(radius + grow),
            )
        }
    }
}

private const val SHADOW_STEPS = 5
