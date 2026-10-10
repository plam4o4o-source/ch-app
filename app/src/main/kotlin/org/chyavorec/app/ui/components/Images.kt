package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.network.HttpException
import coil3.request.ImageRequest
import org.chyavorec.app.R
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
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    onFailure: (() -> Unit)? = null,
) {
    var failed by remember(url) { mutableStateOf(false) }
    Box(modifier.background(containerColor)) {
        val target = if (failed) fallbackUrl else url
        if (target != null) {
            val context = LocalContext.current
            // Заявката се създава веднъж за адрес (не при всяка рекомпозиция); crossfade идва от ImageLoader-а.
            val request = remember(context, target) { ImageRequest.Builder(context).data(target).build() }
            AsyncImage(
                model = request,
                contentDescription = contentDescription,
                contentScale = contentScale,
                onError = {
                    if (!failed && fallbackUrl != null) failed = true else onFailure?.invoke()
                },
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
 * Фирмен фон вместо липсваща снимка: диагонален градиент бордо → мастило и
 * едва забележим воден знак с логото, изместен надясно. Текст върху него —
 * в Parchment/GoldLight (контраст над 4.5:1).
 */
@Composable
fun BrandedImageFallback(modifier: Modifier = Modifier) {
    Box(modifier.background(Brush.linearGradient(listOf(Brand.Burgundy, Color(0xFF3A1A18), Brand.Ink)))) {
        // Воден знак с 9% непрозрачност — малкият вариант (256 px) е достатъчен.
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(org.chyavorec.app.R.drawable.logo_chitalishte_small),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alpha = 0.09f,
            colorFilter = ColorFilter.tint(Brand.Parchment),
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight()
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .graphicsLayer {
                    translationX = size.width * 0.22f
                    scaleX = 1.15f
                    scaleY = 1.15f
                },
        )
    }
}

/**
 * Дали кориците може да се теглят от covers.openlibrary.org (настройка „Корици от
 * интернет“). Подава се от корена; по подразбиране включено.
 */
val LocalOnlineCovers = staticCompositionLocalOf { true }

/** ISBN само с цифри (и X) или `null`, ако не е 10- или 13-знаков. */
private fun cleanIsbn(isbn: String): String? {
    val clean = isbn.filter { it.isDigit() || it == 'X' || it == 'x' }.uppercase()
    return clean.takeIf { it.length == 10 || it.length == 13 }
}

/** Адрес на корица в Open Library по ISBN (само HTTPS; `default=false` → 404 вместо празен GIF, когато липсва). */
fun openLibraryCoverUrl(isbn: String, large: Boolean): String? {
    val clean = cleanIsbn(isbn) ?: return null
    return "https://covers.openlibrary.org/b/isbn/$clean-${if (large) "L" else "M"}.jpg?default=false"
}

/** Голяма (-L) корица само за детайлите на книгата; списъците и редовете ползват една и съща -M. */
private val LargeCoverWidth = 120.dp

/** Потъмняване към долния край на корицата — еднакво за всички, създава се веднъж. */
private val CoverBottomShade = Brush.verticalGradient(0.55f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.35f))

/** Цветът на „издателската“ корица — по раздела по УДК със засенчване според заглавието (общ за корици и гръбчета). */
fun coverColor(book: CatalogBook): Color = coverShade(UdcCoverColors[book.udcSection] ?: Color(0xFF5A4D3D), book.title)

/**
 * Корица на книга. Почти няма сканирани корици в каталога, затова (както на
 * уеб страницата на каталога) се рисува „издателска“ корица с цвета на раздела
 * по УДК, заглавието и автора; сканираната корица, ако има, ляга отгоре. Ако
 * книгата има ISBN и настройката позволява, се опитва корица от Open Library
 * (кешира се от Coil на диска); при липса/грешка остава рисуваната.
 */
@Composable
fun BookCover(book: CatalogBook, modifier: Modifier = Modifier, width: Dp = 72.dp, showStatusDot: Boolean = true) {
    val base = coverColor(book)
    val height = width * 1.42f
    val ext = LocalExtendedColors.current
    // Цветната лента сама не е достъпна за екранни четци и при цветна слепота —
    // наличността се казва и с текст.
    val statusText = if (showStatusDot) {
        when (book.status) {
            BookStatus.AVAILABLE -> stringResource(R.string.status_available)
            BookStatus.ON_LOAN -> stringResource(R.string.status_on_loan)
            BookStatus.NOT_ON_SHELF -> stringResource(R.string.status_not_on_shelf)
            BookStatus.UNAVAILABLE -> stringResource(R.string.status_unavailable)
        }
    } else null
    // Градиентът зависи само от цвета — не се създава наново при всяка рекомпозиция.
    val background = remember(base) {
        Brush.linearGradient(listOf(base.copy(alpha = 0.85f), base, base.darken()), Offset.Zero, Offset(0f, Float.POSITIVE_INFINITY))
    }

    val context = LocalContext.current
    val online = LocalOnlineCovers.current
    val large = width >= LargeCoverWidth
    val isbn = remember(book.isbn) { cleanIsbn(book.isbn) }
    val wantsOnline = online && book.coverUrl.isBlank() && isbn != null
    // ISBN без корица в Open Library (запомнени за 30 дни) не се питат отново.
    if (wantsOnline) CoverMisses.ensureLoaded(context)
    val missesReady = wantsOnline && CoverMisses.ready.value
    val onlineUrl = remember(isbn, wantsOnline, missesReady, large) {
        if (isbn != null && missesReady && !CoverMisses.isMiss(isbn)) openLibraryCoverUrl(isbn, large) else null
    }
    var onlineFailed by remember(onlineUrl) { mutableStateOf(false) }
    val url = when {
        book.coverUrl.isNotBlank() -> book.coverUrl
        onlineUrl != null && !onlineFailed -> onlineUrl
        else -> null
    }
    // Истинската корица е заредена → рисуваното заглавие/автор под нея не се изчертава.
    var imageShown by remember(url) { mutableStateOf(false) }

    Box(
        modifier
            .size(width, height)
            .shadow(6.dp, RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
            .clip(RoundedCornerShape(topStart = 3.dp, bottomStart = 3.dp, topEnd = 8.dp, bottomEnd = 8.dp))
            .background(background)
            .drawWithCache {
                // гръбче
                val spineWidth = 7.dp.toPx()
                val spine = Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.28f), Color.Transparent), 0f, spineWidth)
                onDrawWithContent {
                    drawContent()
                    drawRect(spine, size = size.copy(width = spineWidth))
                }
            }
            .clearAndSetSemantics {
                contentDescription = book.title
                if (statusText != null) stateDescription = statusText
            },
    ) {
        val scale = (width.value / 72f).coerceIn(0.6f, 3f)
        if (!imageShown) {
            // вътрешна „релефна“ рамка като на твърда корица
            Box(
                Modifier.fillMaxSize().padding(start = (9 * scale).dp, end = (4 * scale).dp, top = (4 * scale).dp, bottom = (4 * scale).dp)
                    .border(0.75.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(2.dp)),
            )
            // по-тъмно дъно — авторът се чете и върху светлите раздели
            Box(Modifier.fillMaxSize().background(CoverBottomShade))
            Column(Modifier.fillMaxSize().padding(start = (12 * scale).dp, end = (7 * scale).dp, top = (9 * scale).dp, bottom = (10 * scale).dp)) {
                // Малките корици (в списъци) получават по-дребен шрифт. Сричкопренасяне — само
                // при големите: в малките то само оскъпява подреждането на текста в списъка.
                Text(
                    book.title,
                    color = Color.White,
                    fontFamily = Raleway,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (11.5f * scale).coerceAtLeast(9f).sp,
                    lineHeight = (13.5f * scale).coerceAtLeast(11f).sp,
                    maxLines = if (width < 72.dp) 3 else 4,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTextStyle.current.copy(
                        hyphens = if (width >= 100.dp) Hyphens.Auto else Hyphens.None,
                        lineBreak = LineBreak.Heading,
                    ),
                )
                Box(Modifier.weight(1f))
                Text(
                    book.author,
                    color = Color.White.copy(alpha = 0.92f),
                    fontFamily = Raleway,
                    fontSize = maxOf(9f, 8.5f * scale).sp,
                    lineHeight = maxOf(11f, 10f * scale).sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (url != null) {
            val request = remember(context, url) {
                ImageRequest.Builder(context).data(url).apply {
                    // В детайлите: докато се тегли голямата корица, се показва вече заредената средна от списъка.
                    if (url == onlineUrl && large && isbn != null) placeholderMemoryCacheKey(openLibraryCoverUrl(isbn, large = false))
                }.build()
            }
            AsyncImage(
                model = request,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onError = { state ->
                    if (url == onlineUrl) {
                        onlineFailed = true
                        // Само „няма такава корица“ се помни; без мрежа/грешка на сървъра — нов опит следващия път.
                        val error = state.result.throwable
                        if (isbn != null && error is HttpException && error.response.code == 404) CoverMisses.record(isbn)
                    }
                },
                onSuccess = { state ->
                    // Open Library връща 1×1 GIF за липсваща корица (ако `default=false` бъде подминато).
                    val s = state.painter.intrinsicSize
                    if (url == onlineUrl && s.isSpecified && (s.width < 10f || s.height < 10f)) {
                        onlineFailed = true
                        if (isbn != null) CoverMisses.record(isbn)
                    } else {
                        imageShown = true
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (showStatusDot) {
            // лента в долния край: зелено — налична, жълто — не е на рафта, червено — недостъпна
            val color = when (book.status) {
                BookStatus.AVAILABLE -> ext.ok
                BookStatus.ON_LOAN, BookStatus.NOT_ON_SHELF -> ext.warn
                BookStatus.UNAVAILABLE -> ext.bad
            }
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height((4 * scale).dp).background(color))
        }
    }
}

private fun Color.darken(f: Float = 0.78f) = Color(red * f, green * f, blue * f, alpha)

/**
 * Вариация в рамките на цвета на раздела по УДК според заглавието, за да не е
 * списък от еднакви корици. Само затъмнява (контрастът на белия текст не пада)
 * и леко „затопля“ или „охлажда“ тона.
 */
private fun coverShade(base: Color, title: String): Color {
    val h = title.hashCode() and Int.MAX_VALUE
    val darkened = base.darken(floatArrayOf(1f, 0.92f, 0.85f, 0.78f)[h % 4])
    return when ((h / 4) % 3) {
        0 -> lerp(darkened, Brand.Burgundy, 0.12f)
        1 -> lerp(darkened, Brand.Ink, 0.12f)
        else -> darkened
    }
}

/**
 * Логото на НЧ „Васил Левски – 1922“ върху бял кръг — четимо и в тъмна тема
 * (самото лого е в сиви тонове върху прозрачен фон). До 64dp (заглавни ленти,
 * карта, меню) се ползва малкият вариант (256 px) — по-малко памет и по-бързо
 * декодиране; пълният (679×720) — само за големите емблеми.
 */
@Composable
fun Emblem(size: Dp, modifier: Modifier = Modifier, description: String? = null) {
    Box(
        modifier.size(size).shadow(size * 0.08f, CircleShape).background(Color.White, CircleShape).padding(size * 0.06f),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(
                if (size <= 64.dp) org.chyavorec.app.R.drawable.logo_chitalishte_small else org.chyavorec.app.R.drawable.logo_chitalishte,
            ),
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
