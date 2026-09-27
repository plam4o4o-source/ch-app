package org.chyavorec.app.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        repeat(count) { i ->
            val w by animateDpAsState(if (current == i) 22.dp else 8.dp, label = "dot")
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

/** Поле за търсене (като на сайта); отваря глобалното търсене. */
@Composable
fun SearchPill(hint: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier.fillMaxWidth().height(52.dp).semantics { role = Role.Button },
    ) {
        Row(Modifier.padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Text(
                hint, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Елемент от мрежата с плочки („Още“, „Моето“). */
data class TileItem(val icon: ImageVector, val title: String, val subtitle: String?, val onClick: () -> Unit)

/** Мрежа от плочки в 2 колони с еднаква височина на реда. */
@Composable
fun TileGrid(items: List<TileItem>, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.chunked(2).forEachIndexed { r, row ->
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
                        modifier = Modifier.weight(1f).fillMaxHeight().animateEntrance(r * 2 + c),
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
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

