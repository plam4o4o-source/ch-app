package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.Raleway
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.CatalogBook

/** Изображение от мрежата с кеш, плавно появяване и неутрален фон при грешка. */
@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallbackUrl: String? = null,
) {
    var failed by remember(url) { mutableStateOf(false) }
    Box(modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)) {
        val target = if (failed) fallbackUrl else url
        if (target != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(target).crossfade(250).build(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                onError = { if (!failed && fallbackUrl != null) failed = true },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                Icons.Outlined.Image, contentDescription = null,
                tint = MaterialTheme.colorScheme.outline, modifier = Modifier.align(Alignment.Center).size(32.dp),
            )
        }
    }
}

/**
 * Корица на книга. Почти няма сканирани корици в каталога, затова (както на
 * уеб страницата на каталога) се рисува „издателска“ корица с цвета на раздела
 * по УДК, заглавието и автора; сканираната корица, ако има, ляга отгоре.
 */
@Composable
fun BookCover(book: CatalogBook, modifier: Modifier = Modifier, width: Dp = 72.dp, showStatusDot: Boolean = true) {
    val base = UdcCoverColors[book.udcSection] ?: Color(0xFF5A4D3D)
    val height = width * 1.42f
    val ext = LocalExtendedColors.current
    Box(
        modifier
            .size(width, height)
            .shadow(6.dp, RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
            .clip(RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
            .background(Brush.linearGradient(listOf(base.copy(alpha = 0.85f), base, base.darken()), Offset.Zero, Offset(0f, Float.POSITIVE_INFINITY)))
            .drawWithContent {
                drawContent()
                // гръбче
                drawRect(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.28f), Color.Transparent), 0f, 7.dp.toPx()), size = size.copy(width = 7.dp.toPx()))
            }
            .clearAndSetSemantics { contentDescription = book.title },
    ) {
        val scale = (width.value / 72f).coerceIn(0.6f, 3f)
        Column(Modifier.fillMaxSize().padding(start = (10 * scale).dp, end = (7 * scale).dp, top = (9 * scale).dp, bottom = (8 * scale).dp)) {
            Text(
                book.title,
                color = Color.White,
                fontFamily = Cormorant,
                fontWeight = FontWeight.SemiBold,
                fontSize = (11 * scale).sp,
                lineHeight = (12.5f * scale).sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )
            Box(Modifier.weight(1f))
            Text(
                book.author,
                color = Color.White.copy(alpha = 0.86f),
                fontFamily = Raleway,
                fontSize = (7.5f * scale).sp,
                lineHeight = (9 * scale).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (book.coverUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(book.coverUrl).crossfade(200).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (showStatusDot) {
            val dot = when (book.status) {
                BookStatus.AVAILABLE -> ext.ok
                BookStatus.ON_LOAN, BookStatus.NOT_ON_SHELF -> ext.warn
                BookStatus.UNAVAILABLE -> ext.bad
            }
            Box(
                Modifier.align(Alignment.TopEnd).padding((5 * scale).dp).size((9 * scale).dp)
                    .background(Color.White, CircleShape).padding(1.5.dp).background(dot, CircleShape),
            )
        }
    }
}

private fun Color.darken(f: Float = 0.78f) = Color(red * f, green * f, blue * f, alpha)

/**
 * Логото на НЧ „Васил Левски – 1922“ върху бял кръг — четимо и в тъмна тема
 * (самото лого е в сиви тонове върху прозрачен фон).
 */
@Composable
fun Emblem(size: Dp, modifier: Modifier = Modifier, description: String? = null) {
    Box(
        modifier.size(size).shadow(size * 0.08f, CircleShape).background(Color.White, CircleShape).padding(size * 0.06f),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(org.chyavorec.app.R.drawable.logo_chitalishte),
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

/** Произволно лого от ресурсите (каталог, InvLib, създател) със запазени пропорции. */
@Composable
fun BrandImage(res: Int, description: String?, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
        painter = androidx.compose.ui.res.painterResource(res),
        contentDescription = description,
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}
