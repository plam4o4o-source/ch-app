package org.chyavorec.app.ui.share

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.PersonalCovers
import org.chyavorec.app.ui.components.QrCodeView
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.screens.my.YearShareCard
import org.chyavorec.app.ui.screens.my.YearSlideKind
import org.chyavorec.app.ui.screens.my.shortAuthor
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.Raleway
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import java.time.LocalDate
import java.util.Locale

/** Картата за споделяне — само публични данни (или — при годината — данните на самия читател, по негов избор). */
@Immutable
sealed interface ShareCard {
    val format: ShareFormat
    /** Снимката (зарежда се преди рисуването); `null` — без снимка. */
    val photoUrl: String?
    /** Адресът в QR кода. */
    val url: String
    val fileName: String

    data class Event(
        val title: String,
        /** „събота, 26 септември 2026 · 18:00“. */
        val dateLine: String?,
        /** Денят и месецът за „листчето от календара“ (`null` — без дата). */
        val day: String?,
        val month: String?,
        val place: String?,
        override val photoUrl: String?,
        override val url: String,
    ) : ShareCard {
        override val format get() = ShareFormat.STORY
        override val fileName get() = "event"
    }

    data class Book(val book: CatalogBook, override val photoUrl: String?, override val url: String) : ShareCard {
        override val format get() = ShareFormat.STORY
        override val fileName get() = "book-${book.inv}"
    }

    /** Новината — за лентата (4:5): там най-често се споделят новини. */
    data class News(val title: String, val dateLine: String?, val summary: String, override val photoUrl: String?, override val url: String) : ShareCard {
        override val format get() = ShareFormat.FEED
        override val fileName get() = "news"
    }

    /** „Честитка от Яворец“ за празник: орнаментът на деня, пожеланието и личният ред (без лични данни). */
    data class Greeting(
        val occasionId: String,
        val title: String,
        val wish: String,
        /** Личният ред („За баба Мария“) — вече изчистен ([org.chyavorec.domain.occasion.GreetingCards.sanitizeLine]). */
        val line: String,
        val ornament: org.chyavorec.domain.occasion.OccasionOrnament,
        val motif: ShevitsaMotif,
        override val url: String,
    ) : ShareCard {
        override val format get() = ShareFormat.STORY
        override val photoUrl: String? get() = null
        override val fileName get() = "greeting-$occasionId"
    }

    /** Шевицата, извезана в „Извези шевица“, като картичка за лентата (4:5). */
    data class Embroidery(val pattern: org.chyavorec.domain.embroidery.StitchPattern, override val url: String) : ShareCard {
        override val format get() = ShareFormat.FEED
        override val photoUrl: String? get() = null
        override val fileName get() = "embroidery"
    }

    data class Year(val summary: ReadingYear.Summary, val slide: YearSlideKind, override val url: String) : ShareCard {
        override val format get() = ShareFormat.STORY
        override val photoUrl: String? get() = null
        override val fileName get() = "year-${summary.year}-${slide.name.lowercase()}"
    }
}

/** Картата в размера на формата си (в „картови“ dp — виж [ShareFormat.DENSITY]). */
@Composable
fun ShareCardContent(card: ShareCard, photo: ImageBitmap?) {
    when (card) {
        is ShareCard.Event -> EventShareCard(card, photo)
        is ShareCard.Book -> BookShareCard(card, photo)
        is ShareCard.News -> NewsShareCard(card, photo)
        is ShareCard.Greeting -> GreetingShareCard(card)
        is ShareCard.Embroidery -> EmbroideryShareCard(card)
        is ShareCard.Year -> YearShareCard(card.summary, card.slide, card.url)
        is NewspaperShareCard -> NewspaperShareCardContent(card)
        is QuizShareCard -> QuizShareCardContent(card)
    }
}

// Цветовете на картите са фирмените — еднакви при всяка тема на телефона.
private val CardTop = Color(0xFF241A10)
private val CardBottom = Color(0xFF3B1219)
private val GoldSoft = Brand.GoldLight
private val Paper = Brand.Parchment

internal fun serif(size: TextUnit, color: Color = Paper, weight: FontWeight = FontWeight.SemiBold, lineHeight: Float = 1.1f) =
    TextStyle(fontFamily = Cormorant, fontWeight = weight, fontSize = size, lineHeight = size * lineHeight, color = color,
        lineBreak = LineBreak.Heading, hyphens = Hyphens.None)

internal fun sans(size: TextUnit, color: Color = Paper, weight: FontWeight = FontWeight.Normal, spacing: Float = 0f) =
    TextStyle(fontFamily = Raleway, fontWeight = weight, fontSize = size, lineHeight = size * 1.4f, color = color, letterSpacing = spacing.em)

/** Заглавие: колкото по-дълго, толкова по-дребно (картата е с постоянен размер). */
internal fun titleSize(title: String, base: Float): TextUnit = when {
    title.length > 90 -> base * 0.68f
    title.length > 55 -> base * 0.8f
    title.length > 30 -> base * 0.9f
    else -> base
}.sp

/** Малък етикет с ГЛАВНИ букви и разредка (над заглавието). */
@Composable
internal fun CardLabel(text: String, color: Color = Brand.Gold, modifier: Modifier = Modifier) {
    Text(text.uppercase(), style = sans(11.sp, color, FontWeight.Bold, spacing = 0.2f), modifier = modifier, maxLines = 1)
}

/**
 * Рамката на всички карти: тъмен фон мастило → бордо със златиста светлина горе, двойна златна
 * линия с шевица по ъглите, читалището горе и сайтът с QR кода долу.
 */
@Composable
internal fun BrandCardFrame(
    format: ShareFormat,
    url: String,
    modifier: Modifier = Modifier,
    background: Brush = Brush.verticalGradient(listOf(CardTop, Brand.Ink, CardBottom)),
    motif: ShevitsaMotif = ShevitsaMotif.ROSETTE,
    content: @Composable ColumnScope.() -> Unit,
) {
    val feed = format == ShareFormat.FEED
    Box(modifier.size(format.widthDp, format.heightDp).background(background)) {
        CardOrnaments(motif, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(horizontal = 30.dp, vertical = if (feed) 26.dp else 32.dp)) {
            CardHeader()
            Spacer(Modifier.height(if (feed) 14.dp else 20.dp))
            Column(Modifier.weight(1f).fillMaxWidth(), content = content)
            CardFooter(url, motif, qrSize = if (feed) 62.dp else 74.dp)
        }
    }
}

/** Двойната рамка, шевиците по ъглите и мекото сияние горе — само рисуване, без семантика. */
@Composable
private fun CardOrnaments(motif: ShevitsaMotif, modifier: Modifier) {
    Spacer(
        modifier.drawWithCache {
            val outer = 12.dp.toPx()
            val inner = 17.dp.toPx()
            val corner = 24.dp.toPx()
            val gap = outer + corner + 4.dp.toPx()
            val w = size.width
            val h = size.height
            fun rule(inset: Float, from: Float) = Path().apply {
                moveTo(from, inset); lineTo(w - from, inset)
                moveTo(from, h - inset); lineTo(w - from, h - inset)
                moveTo(inset, from); lineTo(inset, h - from)
                moveTo(w - inset, from); lineTo(w - inset, h - from)
            }
            val outerRule = rule(outer, gap)
            val innerRule = rule(inner, gap)
            val o = outer - corner / 2f + 2.dp.toPx()
            val motifs = Path().apply {
                fillType = PathFillType.EvenOdd
                addShevitsaMotif(motif, o, o, corner)
                addShevitsaMotif(motif, w - o - corner, o, corner)
                addShevitsaMotif(motif, o, h - o - corner, corner)
                addShevitsaMotif(motif, w - o - corner, h - o - corner, corner)
            }
            val glow = Brush.radialGradient(
                listOf(Brand.Gold.copy(alpha = 0.16f), Color.Transparent),
                center = Offset(w / 2f, h * 0.12f), radius = w * 0.9f,
            )
            val thin = Stroke(1.2.dp.toPx())
            val hair = Stroke(0.7.dp.toPx())
            onDrawBehind {
                drawRect(glow)
                drawPath(outerRule, Brand.Gold.copy(alpha = 0.7f), style = thin)
                drawPath(innerRule, Brand.Gold.copy(alpha = 0.3f), style = hair)
                drawPath(motifs, Brand.Gold)
            }
        },
    )
}

/** Емблемата и името на читалището. */
@Composable
private fun CardHeader() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        CardEmblem(38.dp)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(stringResource(R.string.org_short), style = serif(17.sp), maxLines = 1)
            Text(stringResource(R.string.org_place), style = sans(10.5.sp, GoldSoft, spacing = 0.06f), maxLines = 1)
        }
    }
}

/** Логото на бял кръг — без сянка (картата се рисува и в софтуерно платно). */
@Composable
internal fun CardEmblem(size: Dp, modifier: Modifier = Modifier) {
    Box(modifier.size(size).background(Color.White, CircleShape).border(1.dp, Brand.Gold, CircleShape).padding(size * 0.08f)) {
        Image(painterResource(if (size <= 64.dp) R.drawable.logo_chitalishte_small else R.drawable.logo_chitalishte), null, Modifier.fillMaxSize())
    }
}

/** Лента шевица, сайтът и QR кодът към страницата. */
@Composable
private fun CardFooter(url: String, motif: ShevitsaMotif, qrSize: Dp) {
    ShevitsaStrip(motif, Brand.Gold.copy(alpha = 0.85f), Modifier.fillMaxWidth().height(9.dp))
    Spacer(Modifier.height(14.dp))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("chyavorec.org", style = serif(21.sp, Brand.Gold))
            Text(stringResource(R.string.share_card_scan), style = sans(11.sp, GoldSoft))
        }
        Box(Modifier.size(qrSize).background(Brand.Cream, RoundedCornerShape(8.dp)).padding(5.dp)) {
            QrCodeView(url, "", Modifier.fillMaxSize(), color = Brand.Ink)
        }
    }
}

/** Снимката със златна рамка. */
@Composable
private fun CardPhoto(photo: ImageBitmap, modifier: Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Image(photo, null, contentScale = ContentScale.Crop, modifier = modifier.clip(shape).border(1.dp, Brand.Gold.copy(alpha = 0.7f), shape))
}

/** „Листче от календара“ — денят и месецът. */
@Composable
private fun DateBadge(day: String, month: String, modifier: Modifier = Modifier, large: Boolean = false) {
    Column(
        modifier.background(Brand.Cream, RoundedCornerShape(if (large) 18.dp else 12.dp)).border(1.dp, Brand.Gold, RoundedCornerShape(if (large) 18.dp else 12.dp))
            .padding(horizontal = if (large) 26.dp else 14.dp, vertical = if (large) 12.dp else 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(day, style = serif(if (large) 76.sp else 34.sp, Brand.Burgundy, lineHeight = 1f))
        Text((if (large) month else month.take(3)).uppercase(), style = sans(if (large) 14.sp else 10.sp, Brand.Burgundy, FontWeight.Bold, spacing = 0.16f))
    }
}

@Composable
private fun InfoLine(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(top = 6.dp)) {
        Icon(icon, null, tint = Brand.Gold, modifier = Modifier.padding(top = 1.dp).size(17.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = sans(14.sp, GoldSoft), maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun EventShareCard(card: ShareCard.Event, photo: ImageBitmap?) {
    BrandCardFrame(card.format, card.url) {
        if (photo != null) {
            Box(Modifier.fillMaxWidth()) {
                CardPhoto(photo, Modifier.fillMaxWidth().aspectRatio(16f / 10f))
                if (card.day != null && card.month != null) {
                    DateBadge(card.day, card.month, Modifier.align(Alignment.BottomStart).offset(x = 14.dp, y = 18.dp))
                }
            }
            Spacer(Modifier.height(30.dp))
        } else {
            Spacer(Modifier.weight(0.6f))
            if (card.day != null && card.month != null) {
                DateBadge(card.day, card.month, Modifier.align(Alignment.CenterHorizontally), large = true)
                Spacer(Modifier.height(30.dp))
            }
        }
        CardLabel(stringResource(R.string.share_card_event))
        Spacer(Modifier.height(8.dp))
        Text(card.title, style = serif(titleSize(card.title, if (photo != null) 32f else 38f)), maxLines = if (photo != null) 3 else 5, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(8.dp))
        card.dateLine?.let { InfoLine(Icons.Outlined.Event, it) }
        card.place?.let { InfoLine(Icons.Outlined.Place, it) }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun BookShareCard(card: ShareCard.Book, photo: ImageBitmap?) {
    val b = card.book
    BrandCardFrame(card.format, card.url, motif = ShevitsaMotif.TREE_OF_LIFE) {
        Spacer(Modifier.weight(0.4f))
        Box(
            Modifier.fillMaxWidth().drawWithCache {
                // Кръг (не правоъгълник) — сиянието няма ръбове.
                val r = size.minDimension / 2f
                val glow = Brush.radialGradient(listOf(Brand.Gold.copy(alpha = 0.28f), Color.Transparent), center = Offset(size.width / 2f, size.height / 2f), radius = r)
                onDrawBehind { drawCircle(glow, radius = r) }
            }.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            val coverWidth = 128.dp
            if (photo != null) {
                val shape = RoundedCornerShape(4.dp)
                Image(photo, null, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(coverWidth, coverWidth * 1.42f).clip(shape).border(1.dp, Brand.Gold.copy(alpha = 0.6f), shape))
            } else {
                // Генерираната корица — в светлата фирмена тема (еднаква при всяка тема на телефона).
                ChitalishteTheme(mode = ThemeMode.LIGHT) { PersonalCovers { BookCover(b, width = coverWidth, showStatusDot = false, decorative = true) } }
            }
        }
        Spacer(Modifier.height(20.dp))
        CardLabel(stringResource(R.string.share_card_book), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(8.dp))
        Text(b.title, style = serif(titleSize(b.title, 34f)), maxLines = 3, overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        if (b.author.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(b.authors.take(2).joinToString(", ") { shortAuthor(it) }, style = sans(15.sp, GoldSoft, FontWeight.SemiBold),
                maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
        val imprint = listOf(b.publisher, b.year).filter { it.isNotBlank() }.joinToString(", ")
        if (imprint.isNotBlank()) {
            Text(imprint, style = sans(12.sp, Paper.copy(alpha = 0.7f)), maxLines = 1, overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
        }
        Spacer(Modifier.weight(1f))
        Text(stringResource(R.string.share_card_book_cta), style = serif(18.sp, Paper, FontWeight.Normal), textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp))
    }
}

@Composable
private fun NewsShareCard(card: ShareCard.News, photo: ImageBitmap?) {
    BrandCardFrame(card.format, card.url, motif = ShevitsaMotif.RHOMBUS) {
        if (photo != null) {
            CardPhoto(photo, Modifier.fillMaxWidth().height(128.dp))
            Spacer(Modifier.height(14.dp))
        } else {
            Spacer(Modifier.weight(0.3f))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            CardLabel(stringResource(R.string.share_card_news))
            card.dateLine?.let {
                Text("  ·  ", style = sans(11.sp, Brand.Gold, FontWeight.Bold))
                Text(it, style = sans(12.sp, GoldSoft), maxLines = 1)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(card.title, style = serif(titleSize(card.title, if (photo != null) 27f else 34f)), maxLines = if (photo != null) 3 else 4, overflow = TextOverflow.Ellipsis)
        if (photo == null && card.summary.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(card.summary, style = sans(14.sp, Paper.copy(alpha = 0.85f)), maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
        Spacer(Modifier.weight(1f))
    }
}

/** Картите от данните на екраните (датите — на езика на интерфейса). */
object ShareCards {
    fun event(context: Context, e: Event): ShareCard.Event {
        val date = e.date?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() }
        val dateLine = listOfNotNull(Formatters.date(context, e.date), e.time?.takeIf { it.isNotBlank() }).joinToString(" · ").ifBlank { null }
        return ShareCard.Event(
            title = e.title,
            dateLine = dateLine,
            day = date?.dayOfMonth?.toString(),
            month = date?.let { monthName(context, it) },
            place = e.place?.takeIf { it.isNotBlank() },
            photoUrl = e.imageUrl,
            url = e.sourceUrl,
        )
    }

    fun news(context: Context, a: NewsArticle, photoUrl: String? = a.imageUrl): ShareCard.News = ShareCard.News(
        title = a.title,
        dateLine = Formatters.millisDate(context, a.publishedAtMillis),
        summary = a.summary,
        photoUrl = photoUrl,
        url = a.url,
    )

    /** [coverUrl] — истинската корица, ако може да се тегли (не от личен списък); иначе генерираната. */
    fun book(book: CatalogBook, url: String, coverUrl: String?): ShareCard.Book = ShareCard.Book(book, coverUrl, url)

    private fun monthName(context: Context, d: LocalDate): String =
        if (Formatters.isBulgarian(context)) BulgarianDates.monthName(d.monthValue)
        else d.month.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, Locale.ENGLISH)
}
