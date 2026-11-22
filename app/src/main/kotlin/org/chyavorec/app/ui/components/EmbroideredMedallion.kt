package org.chyavorec.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Под тази височина на прозореца празните състояния са само с медальона (без илюстрацията и лентата). */
val CompactMessageHeight: Dp = 480.dp

/** Дали празното състояние/грешката е на нисък прозорец (хоризонтален телефон, разделен екран). */
fun isCompactMessageHeight(windowHeight: Dp): Boolean = windowHeight < CompactMessageHeight

/**
 * Бродиран медальон: кръгло поле в златния контейнер на темата, обиколено от венец кръстати
 * бодове (злато и бордо, през един — както в шевиците на приложението) с четири бордо ромбчета
 * по осите, и значката на ситуацията в средата. Рисува се с Canvas в цветовете на марката —
 * конците следват темата (бордото в тъмна тема е по-светло, [stitchBurgundy]).
 *
 * Декоративен: без семантика (заглавието на празното състояние казва всичко) и без анимация,
 * затова и при намалено движение е същият.
 */
@Composable
fun EmbroideredMedallion(icon: ImageVector, modifier: Modifier = Modifier, size: Dp = 56.dp) {
    val gold = LocalExtendedColors.current.gold
    val burgundy = stitchBurgundy()
    val field = MaterialTheme.colorScheme.primaryContainer
    Box(
        modifier.size(size).clearAndSetSemantics { }.drawBehind { drawMedallion(field, gold, burgundy) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(size * ICON))
    }
}

/** Колко бода има венецът (всеки четвърти — ромбче по оста). */
internal const val MEDALLION_STITCHES = 16
private const val FIELD = 0.66f
private const val RING = 0.85f
private const val CELL = 0.2f
private const val ICON = 0.42f

/**
 * Медальонът в [DrawScope.size]: поле с радиус [FIELD], венец по окръжност [RING] (части от
 * половината на по-малката страна) — кръстати бодове в клетка [CELL], а по осите — ромбчета.
 */
internal fun DrawScope.drawMedallion(field: Color, gold: Color, burgundy: Color) {
    val r = size.minDimension / 2f
    if (r <= 0f) return
    val c = center
    drawCircle(field, radius = r * FIELD, center = c)
    val cell = r * CELL
    val half = cell * (0.5f - CrossStitchSpec.INSET)
    val ring = r * RING
    for (i in 0 until MEDALLION_STITCHES) {
        val a = i * (2.0 * PI / MEDALLION_STITCHES) - PI / 2
        val p = Offset(c.x + ring * cos(a).toFloat(), c.y + ring * sin(a).toFloat())
        if (i % 4 == 0) {
            drawRhomb(p, cell * 0.55f, burgundy)
        } else {
            crossStitch(p.x - half, p.y - half, p.x + half, p.y + half, if (i % 2 == 0) burgundy else gold, cell)
        }
    }
}

/** Плътно ромбче (връх нагоре) с център [c] и „радиус“ [r] до върха. */
private fun DrawScope.drawRhomb(c: Offset, r: Float, color: Color) {
    val path = Path().apply {
        moveTo(c.x, c.y - r); lineTo(c.x + r, c.y); lineTo(c.x, c.y + r); lineTo(c.x - r, c.y); close()
    }
    drawPath(path, color)
}
