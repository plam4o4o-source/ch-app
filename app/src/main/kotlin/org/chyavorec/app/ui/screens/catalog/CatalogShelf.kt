package org.chyavorec.app.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.components.coverColor
import org.chyavorec.app.ui.components.sharedElementKey
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.Raleway
import org.chyavorec.domain.model.CatalogBook

/* ---- Размери на гръбчетата: детерминирани от книгата, без допълнително състояние ---- */

private const val MIN_SPINE = 28f
private const val MAX_SPINE = 44f
private const val MIN_HEIGHT = 120f
private const val MAX_HEIGHT = 160f
private val SPINE_GAP = 3.dp
private val SHELF_PADDING = 16.dp

/** Ширина на гръбчето (28–44 dp): няма „страници“ в каталога, затова по дължината на заглавието. */
private fun spineWidth(b: CatalogBook): Dp {
    val len = (b.title.length + b.subtitle.length / 2).coerceIn(6, 60)
    return (MIN_SPINE + (len - 6) * (MAX_SPINE - MIN_SPINE) / 54f).dp
}

/** Височина (120–160 dp) — псевдослучайна по инвентарния номер, за да не са еднакви. */
private fun spineHeight(b: CatalogBook): Dp {
    val h = (b.inv * 2654435761L).toInt() and Int.MAX_VALUE
    return (MIN_HEIGHT + (h % 9) * (MAX_HEIGHT - MIN_HEIGHT) / 8f).dp
}

/** Нарежда книгите по полици: докато се събират в наличната ширина. */
private fun shelves(books: List<CatalogBook>, rowWidth: Dp): List<List<CatalogBook>> {
    val rows = mutableListOf<List<CatalogBook>>()
    var row = mutableListOf<CatalogBook>()
    var used = 0.dp
    books.forEach { b ->
        val w = spineWidth(b)
        if (row.isNotEmpty() && used + SPINE_GAP + w > rowWidth) {
            rows += row
            row = mutableListOf()
            used = 0.dp
        }
        row += b
        used += (if (row.size > 1) SPINE_GAP else 0.dp) + w
    }
    if (row.isNotEmpty()) rows += row
    return rows
}

/**
 * „Полица“: книгите като гръбчета, подредени върху дървени дъски. Всяко гръбче е
 * обикновен Box с градиент и завъртян текст — без bitmap-и; редовете са елементи
 * на LazyColumn, така че дългите списъци остават леки.
 */
@Composable
fun CatalogShelfView(
    books: List<CatalogBook>,
    listState: LazyListState,
    header: @Composable () -> Unit,
    canLoadMore: Boolean,
    onLoadMore: () -> Unit,
    onOpen: (CatalogBook) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val rowWidth = maxWidth - SHELF_PADDING * 2
        val rows = remember(books, rowWidth) { shelves(books, rowWidth) }
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 24.dp)) {
            item(contentType = "header") { header() }
            rows.forEachIndexed { r, row ->
                item(key = "shelf-" + row.first().inv, contentType = "shelf") {
                    Column(Modifier.fillMaxWidth().padding(horizontal = SHELF_PADDING).animateEntrance(r)) {
                        Row(
                            Modifier.fillMaxWidth().height(MAX_HEIGHT.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(SPINE_GAP),
                        ) {
                            row.forEach { b -> BookSpine(b, onClick = { onOpen(b) }) }
                        }
                        ShelfBoard()
                        Spacer(Modifier.height(14.dp))
                    }
                }
            }
            if (canLoadMore) {
                item(contentType = "more") {
                    OutlinedButton(onClick = onLoadMore, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(stringResource(R.string.action_load_more))
                    }
                }
            }
        }
    }
}

/** Дървена дъска: градиент орех → тъмно, светла ивица отгоре, сянка отдолу. */
@Composable
private fun ShelfBoard() {
    val dark = LocalExtendedColors.current.isDark
    val top = if (dark) Color(0xFF7A5230) else Color(0xFFA9774A)
    val bottom = if (dark) Color(0xFF3E2A16) else Color(0xFF6E4A2A)
    Box(
        Modifier.fillMaxWidth().height(14.dp)
            .drawBehind {
                // сянка под дъската
                drawRect(
                    Brush.verticalGradient(0f to Color.Black.copy(alpha = 0.28f), 1f to Color.Transparent, startY = size.height, endY = size.height + 12.dp.toPx()),
                    topLeft = Offset(0f, size.height), size = size.copy(height = 12.dp.toPx()),
                )
            }
            .clip(RoundedCornerShape(2.dp))
            .background(Brush.verticalGradient(listOf(top, bottom)))
            .drawBehind {
                drawRect(Brand.Parchment.copy(alpha = 0.35f), size = size.copy(height = 1.5.dp.toPx()))
                // жилки на дървото
                val step = 46.dp.toPx()
                var x = 12.dp.toPx()
                while (x < size.width) {
                    drawLine(Color.Black.copy(alpha = 0.08f), Offset(x, 3.dp.toPx()), Offset(x + 18.dp.toPx(), size.height - 2.dp.toPx()), strokeWidth = 1f)
                    x += step
                }
            },
    )
}

/**
 * Завърта съдържанието на 90° и разменя ширина/височина при измерването, така че
 * текстът да „лежи“ по гръбчето (чете се отдолу нагоре, както по европейските корици).
 */
private fun Modifier.verticalText(): Modifier = this
    .layout { measurable, constraints ->
        val placeable = measurable.measure(
            Constraints(minWidth = 0, maxWidth = constraints.maxHeight, minHeight = 0, maxHeight = constraints.maxWidth),
        )
        layout(placeable.height, placeable.width) {
            placeable.place(x = -(placeable.width - placeable.height) / 2, y = (placeable.width - placeable.height) / 2)
        }
    }
    .rotate(-90f)

/** Гръбче на книга: цвят по раздела по УДК (като корицата), заглавие по дължината, автор дребно. */
@Composable
private fun BookSpine(b: CatalogBook, onClick: () -> Unit) {
    val base = coverColor(b)
    val w = spineWidth(b)
    val h = spineHeight(b)
    val description = listOf(b.title, b.author).filter { it.isNotBlank() }.joinToString(", ")
    Box(
        Modifier
            .size(w, h)
            .sharedElementKey("book-${b.inv}")
            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
            .background(Brush.horizontalGradient(listOf(base.copy(alpha = 0.8f), base, Color(base.red * 0.7f, base.green * 0.7f, base.blue * 0.7f))))
            .drawBehind {
                // ръб на страниците отгоре и релефни ивици в двата края на гръбчето
                drawRect(Brand.Parchment.copy(alpha = 0.55f), size = size.copy(height = 2.dp.toPx()))
                val band = Color.White.copy(alpha = 0.22f)
                drawRect(band, topLeft = Offset(0f, 8.dp.toPx()), size = size.copy(height = 1.5.dp.toPx()))
                drawRect(band, topLeft = Offset(0f, size.height - 10.dp.toPx()), size = size.copy(height = 1.5.dp.toPx()))
                drawRect(Color.Black.copy(alpha = 0.18f), topLeft = Offset(size.width - 2.dp.toPx(), 0f), size = size.copy(width = 2.dp.toPx()))
            }
            .clickable(onClick = onClick)
            .semantics(mergeDescendants = true) { role = Role.Button; contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            Modifier.padding(vertical = 12.dp, horizontal = 3.dp).verticalText(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                b.title,
                color = Color.White,
                fontFamily = Raleway,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (w >= 36.dp) 11.sp else 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            if (b.author.isNotBlank() && w >= 34.dp) {
                Spacer(Modifier.width(6.dp))
                Text(
                    b.author.substringBefore(',').substringBefore(';'),
                    color = Color.White.copy(alpha = 0.85f),
                    fontFamily = Raleway,
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(44.dp),
                )
            }
        }
    }
}
