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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors

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
        modifier = modifier.fillMaxWidth().height(52.dp).semantics { role = Role.Button },
    ) {
        Row(Modifier.padding(start = 18.dp, end = if (trailing != null) 6.dp else 18.dp), verticalAlignment = Alignment.CenterVertically) {
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
 * от 600 dp и 4 от 840 dp (таблет, хоризонтално).
 */
@Composable
fun TileGrid(items: List<TileItem>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val columns = when {
            maxWidth >= 840.dp -> 4
            maxWidth >= 600.dp -> 3
            else -> 2
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
                            Column(Modifier.padding(14.dp)) {
                                IconPlate(item.icon, size = 44.dp)
                                Spacer(Modifier.height(10.dp))
                                Text(item.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                item.subtitle?.let {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                                    )
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
 * Тънка златна рамка с шевица по ъглите (drawable/ornament_frame) върху карта —
 * злато при ~35 % плътност, за да е ненатрапчива. Поставя се като последно дете на
 * Box с `Modifier.matchParentSize()`; не носи семантика.
 */
@Composable
fun OrnamentFrame(modifier: Modifier = Modifier, alpha: Float = 0.35f) {
    androidx.compose.foundation.Image(
        painter = androidx.compose.ui.res.painterResource(org.chyavorec.app.R.drawable.ornament_frame),
        contentDescription = null,
        contentScale = androidx.compose.ui.layout.ContentScale.FillBounds,
        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Brand.Gold.copy(alpha = alpha)),
        modifier = modifier.clearAndSetSemantics { },
    )
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
