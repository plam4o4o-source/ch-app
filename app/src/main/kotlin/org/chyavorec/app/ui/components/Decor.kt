package org.chyavorec.app.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaCalendar
import org.chyavorec.app.ui.theme.ShevitsaMotif

/** Индикатор на страниците (лентата с новини, въведението) — един стил навсякъде. */
@Composable
fun PagerDots(
    count: Int,
    current: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = LocalExtendedColors.current.gold,
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    if (count < 2) return
    val reduced = rememberReducedMotion()
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        repeat(count) { i ->
            val dotSpec: AnimationSpec<Dp> = if (reduced) snap() else spring()
            val w by animateDpAsState(if (current == i) 22.dp else 8.dp, dotSpec, label = "dot")
            Box(
                Modifier.padding(4.dp).height(8.dp).width(w).clip(RoundedCornerShape(50))
                    .background(if (current == i) activeColor else inactiveColor),
            )
        }
    }
}

/** Плочка с икона (бързи действия, „Още“, въведението) — един стил навсякъде. */
@Composable
fun IconPlate(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Box(
        modifier.size(size).clip(MaterialTheme.shapes.medium)
            .background(Brush.linearGradient(listOf(container, container.copy(alpha = 0.55f)))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(size * 0.46f))
    }
}

/**
 * Поле за търсене (като на сайта); отваря глобалното търсене.
 * [trailing] — незадължителен елемент вдясно (напр. икона за скенера на баркодове).
 */
@Composable
fun SearchPill(hint: String, onClick: () -> Unit, modifier: Modifier = Modifier, trailing: (@Composable () -> Unit)? = null) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier.fillMaxWidth().heightIn(min = 52.dp).semantics { role = Role.Button },
    ) {
        Row(Modifier.heightIn(min = 52.dp).padding(start = 18.dp, end = if (trailing != null) 6.dp else 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Text(
                hint, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (trailing != null) {
                Spacer(Modifier.width(8.dp))
                trailing()
            }
        }
    }
}

/** Елемент от мрежата с плочки („Още“, „Моето“). */
data class TileItem(val icon: ImageVector, val title: String, val subtitle: String?, val onClick: () -> Unit)

/**
 * Мрежа от плочки с еднаква височина на реда: 2 колони на телефон, 3 при ширина
 * от 600 dp и 4 от 840 dp (таблет, хоризонтално). Ширината се мери спрямо мащаба на
 * шрифта: при много едър системен шрифт на телефон плочките са в една колона (иконата
 * вляво от текста), за да не се пренасят думите по букви.
 */
@Composable
fun TileGrid(items: List<TileItem>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val textWidth = maxWidth / LocalDensity.current.fontScale.coerceAtLeast(1f)
        val columns = when {
            textWidth >= 840.dp -> 4
            textWidth >= 600.dp -> 3
            textWidth >= 300.dp -> 2
            else -> 1
        }
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items.chunked(columns).forEachIndexed { r, row ->
                Row(
                    Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEachIndexed { c, item ->
                        Surface(
                            onClick = item.onClick,
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier.weight(1f).fillMaxHeight().animateEntrance(r * columns + c)
                                .darkTopHighlight(MaterialTheme.shapes.medium),
                        ) {
                            val texts: @Composable () -> Unit = {
                                Text(item.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                item.subtitle?.let {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = if (columns == 1) 3 else 2, overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                            if (columns == 1) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    IconPlate(item.icon, size = 44.dp)
                                    Spacer(Modifier.width(14.dp))
                                    Column(Modifier.weight(1f)) { texts() }
                                }
                            } else {
                                Column(Modifier.padding(14.dp)) {
                                    IconPlate(item.icon, size = 44.dp)
                                    Spacer(Modifier.height(10.dp))
                                    texts()
                                }
                            }
                        }
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

/**
 * Тънка златна рамка с шевица по ъглите върху карта — злато при ~35 % плътност,
 * за да е ненатрапчива. Мотивът по ъглите следва празниците и сезона
 * ([rememberShevitsaMotif]). Поставя се като последно дете на Box с
 * `Modifier.matchParentSize()`; не носи семантика.
 */
@Composable
fun OrnamentFrame(modifier: Modifier = Modifier, alpha: Float = 0.35f) {
    val motif = rememberShevitsaMotif()
    val draw = remember(motif, alpha) {
        val color = Brand.Gold.copy(alpha = alpha)
        Modifier.drawWithCache {
            val inset = 8.dp.toPx()
            val corner = 18.dp.toPx()
            val origin = 2.dp.toPx()
            val gap = origin + corner + 4.dp.toPx()
            val w = size.width
            val h = size.height
            val frame = Path()
            if (w > 2 * gap && h > 2 * gap) {
                frame.moveTo(gap, inset); frame.lineTo(w - gap, inset)
                frame.moveTo(gap, h - inset); frame.lineTo(w - gap, h - inset)
                frame.moveTo(inset, gap); frame.lineTo(inset, h - gap)
                frame.moveTo(w - inset, gap); frame.lineTo(w - inset, h - gap)
            }
            val motifs = Path().apply {
                fillType = PathFillType.EvenOdd
                addShevitsaMotif(motif, origin, origin, corner)
                addShevitsaMotif(motif, w - origin - corner, origin, corner)
                addShevitsaMotif(motif, origin, h - origin - corner, corner)
                addShevitsaMotif(motif, w - origin - corner, h - origin - corner, corner)
            }
            val line = Stroke(width = 1.5.dp.toPx())
            onDrawBehind {
                drawPath(frame, color, style = line)
                drawPath(motifs, color)
            }
        }
    }
    Spacer(modifier.clearAndSetSemantics { }.then(draw))
}

/**
 * Мотивът на шевицата за днес — по празниците и сезона ([ShevitsaCalendar]);
 * часовникът е този на приложението (в тестовете — фиксиран).
 */
@Composable
fun rememberShevitsaMotif(): ShevitsaMotif {
    val clock = LocalAppContainer.current.clock
    return remember(clock) { ShevitsaCalendar.motifFor(clock.today()) }
}

/**
 * Тънка лента с шевица: две тънки линии по краищата и повтарящ се [motif] между
 * тях. Пътят се строи веднъж за размера (drawWithCache); цветът се чете при
 * рисуване — анимираната тема не пресъздава пътя.
 */
@Composable
fun ShevitsaStrip(motif: ShevitsaMotif, color: Color, modifier: Modifier = Modifier) {
    val currentColor by rememberUpdatedState(color)
    val draw = remember(motif) {
        Modifier.drawWithCache {
            val path = shevitsaStripPath(motif, size.width, size.height)
            onDrawBehind { drawPath(path, currentColor) }
        }
    }
    Spacer(modifier.clearAndSetSemantics { }.then(draw))
}

/** Пътят на лентата: линии отгоре и отдолу (8 % от височината) и мотивите в средата. */
internal fun shevitsaStripPath(motif: ShevitsaMotif, width: Float, height: Float): Path {
    val path = Path().apply { fillType = PathFillType.EvenOdd }
    if (width <= 0f || height <= 0f) return path
    val line = height * 0.08f
    path.addRect(Rect(0f, 0f, width, line))
    path.addRect(Rect(0f, height - line, width, height))
    val tile = height * 0.68f
    if (tile <= 0f) return path
    // Кривулицата е непрекъсната (без разстояние); другите мотиви — с въздух между тях.
    val step = if (motif == ShevitsaMotif.ZIGZAG) tile else tile * 1.35f
    val count = ((width + step - tile) / step).toInt().coerceAtLeast(1)
    val used = tile + step * (count - 1)
    var x = (width - used) / 2f
    val y = (height - tile) / 2f
    repeat(count) {
        path.addShevitsaMotif(motif, x, y, tile)
        x += step
    }
    return path
}

/**
 * Мотив от шевица в квадрат ([x], [y], страна [s]) — само прави линии, без
 * застъпвания между отделните части (рисува се с EvenOdd: вложените ромбове
 * дават „пръстен“ с точка).
 */
internal fun Path.addShevitsaMotif(motif: ShevitsaMotif, x: Float, y: Float, s: Float) {
    fun poly(vararg p: Float) {
        moveTo(x + p[0] * s, y + p[1] * s)
        var i = 2
        while (i < p.size) {
            lineTo(x + p[i] * s, y + p[i + 1] * s)
            i += 2
        }
        close()
    }
    fun rhomb(cx: Float, cy: Float, r: Float) = poly(cx, cy - r, cx + r, cy, cx, cy + r, cx - r, cy)
    when (motif) {
        ShevitsaMotif.RHOMBUS -> {
            rhomb(0.5f, 0.5f, 0.5f)
            rhomb(0.5f, 0.5f, 0.26f)
            rhomb(0.5f, 0.5f, 0.1f)
        }
        ShevitsaMotif.ROSETTE -> {
            // четири листенца по осите (допират се само по ръбовете) + точки по диагоналите
            poly(0.5f, 0.02f, 0.6f, 0.4f, 0.5f, 0.5f, 0.4f, 0.4f)
            poly(0.98f, 0.5f, 0.6f, 0.6f, 0.5f, 0.5f, 0.6f, 0.4f)
            poly(0.5f, 0.98f, 0.4f, 0.6f, 0.5f, 0.5f, 0.6f, 0.6f)
            poly(0.02f, 0.5f, 0.4f, 0.4f, 0.5f, 0.5f, 0.4f, 0.6f)
            rhomb(0.18f, 0.18f, 0.07f)
            rhomb(0.82f, 0.18f, 0.07f)
            rhomb(0.18f, 0.82f, 0.07f)
            rhomb(0.82f, 0.82f, 0.07f)
        }
        ShevitsaMotif.TREE_OF_LIFE -> {
            poly(0.46f, 0.3f, 0.54f, 0.3f, 0.54f, 1f, 0.46f, 1f) // стъбло
            rhomb(0.5f, 0.15f, 0.12f) // връх
            for ((base, w, h) in TreeBranches) {
                leaf(this, x, y, s, 0.46f, base, 0.46f - w, base - h)
                leaf(this, x, y, s, 0.54f, base, 0.54f + w, base - h)
            }
        }
        ShevitsaMotif.ZIGZAG -> {
            poly(
                0f, 0.62f, 0.25f, 0.22f, 0.5f, 0.62f, 0.75f, 0.22f, 1f, 0.62f,
                1f, 0.84f, 0.75f, 0.44f, 0.5f, 0.84f, 0.25f, 0.44f, 0f, 0.84f,
            )
            rhomb(0.5f, 0.18f, 0.08f)
            rhomb(0.25f, 0.84f, 0.08f)
            rhomb(0.75f, 0.84f, 0.08f)
        }
    }
}

/** Клоните на дървото на живота: (основа по височина, разпереност, издигане) — в дялове от страната. */
private val TreeBranches = listOf(Triple(0.45f, 0.30f, 0.16f), Triple(0.68f, 0.36f, 0.18f), Triple(0.9f, 0.40f, 0.18f))

/** Листо-ромб от (ax, ay) до (cx, cy) с ширина 0.12 от страната. */
private fun leaf(path: Path, x: Float, y: Float, s: Float, ax: Float, ay: Float, cx: Float, cy: Float) {
    val dx = cx - ax
    val dy = cy - ay
    val len = kotlin.math.sqrt(dx * dx + dy * dy)
    val nx = -dy / len * 0.06f
    val ny = dx / len * 0.06f
    val mx = (ax + cx) / 2f
    val my = (ay + cy) / 2f
    path.moveTo(x + ax * s, y + ay * s)
    path.lineTo(x + (mx + nx) * s, y + (my + ny) * s)
    path.lineTo(x + cx * s, y + cy * s)
    path.lineTo(x + (mx - nx) * s, y + (my - ny) * s)
    path.close()
}

/**
 * Тънка златиста светлина по горния ръб на карта — само в тъмна тема, където
 * сенките не се виждат върху мастиления фон. В светла тема не прави нищо.
 */
@Composable
fun Modifier.darkTopHighlight(shape: Shape): Modifier {
    if (MaterialTheme.colorScheme.background.luminance() >= 0.5f) return this
    return this.border(
        width = 1.dp,
        brush = Brush.verticalGradient(0f to Brand.Gold.copy(alpha = 0.12f), 0.35f to Color.Transparent),
        shape = shape,
    )
}
