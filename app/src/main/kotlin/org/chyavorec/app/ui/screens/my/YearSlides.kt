package org.chyavorec.app.ui.screens.my

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.DarkBurgundyThread
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.share.BrandCardFrame
import org.chyavorec.app.ui.share.CardEmblem
import org.chyavorec.app.ui.share.CardLabel
import org.chyavorec.app.ui.share.ShareFormat
import org.chyavorec.app.ui.share.sans
import org.chyavorec.app.ui.share.serif
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.GeneratedCover
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.UdcLabels
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.catalog.ReadingYear.Archetype
import org.chyavorec.domain.model.ReadingDiary
import java.time.Month
import java.time.format.TextStyle as DateStyle
import kotlin.math.roundToInt

/** Слайдовете на „Моята читателска година“ — показват се само тези, за които има данни ([yearSlides]). */
enum class YearSlideKind { INTRO, BOOKS, AUTHOR, FAVOURITE, SECTION, MONTH, STREAK, ARCHETYPE, OUTRO }

/**
 * Слайдовете за [s]: любим автор — при поне 2 книги от него; най-харесана книга — ако в дневника
 * има оценена с поне 4 розетки; раздел — ако каталогът го знае; поредица — от 2 месеца.
 */
fun yearSlides(s: ReadingYear.Summary): List<YearSlideKind> = buildList {
    add(YearSlideKind.INTRO)
    add(YearSlideKind.BOOKS)
    if (s.favouriteAuthor != null) add(YearSlideKind.AUTHOR)
    if (s.favourite != null) add(YearSlideKind.FAVOURITE)
    if (s.topSection != null) add(YearSlideKind.SECTION)
    if (s.books >= 2 && s.peakMonth != null) add(YearSlideKind.MONTH)
    if (s.longestStreak >= 2) add(YearSlideKind.STREAK)
    add(YearSlideKind.ARCHETYPE)
    add(YearSlideKind.OUTRO)
}

/** Фонът и мотивът на слайда — злато върху бордо/мастило, всеки слайд в свой тон. */
internal class SlideLook(val top: Color, val bottom: Color, val motif: ShevitsaMotif) {
    val brush: Brush get() = Brush.verticalGradient(listOf(top, bottom))
}

internal fun lookOf(kind: YearSlideKind, s: ReadingYear.Summary): SlideLook = when (kind) {
    YearSlideKind.INTRO -> SlideLook(Brand.Burgundy, Brand.Ink, ShevitsaMotif.ROSETTE)
    YearSlideKind.BOOKS -> SlideLook(Color(0xFF2A1D10), Brand.Ink, ShevitsaMotif.RHOMBUS)
    YearSlideKind.AUTHOR -> SlideLook(Color(0xFF4A1520), Color(0xFF1A0C0A), ShevitsaMotif.TREE_OF_LIFE)
    // Най-харесаната книга: топло бордо към мастило, с розетката на оценките.
    YearSlideKind.FAVOURITE -> SlideLook(lerp(Brand.Burgundy, Brand.Gold, 0.12f), lerp(Brand.Ink, Brand.Burgundy, 0.18f), ShevitsaMotif.ROSETTE)
    YearSlideKind.SECTION -> {
        val section = s.topSection?.section
        val tint = section?.let { UdcCoverColors[it] } ?: Brand.Burgundy
        SlideLook(lerp(tint, Brand.Ink, 0.45f), Brand.Ink, GeneratedCover.motifFor(section, ""))
    }
    YearSlideKind.MONTH -> SlideLook(Brand.Ink, Color(0xFF33240F), ShevitsaMotif.ZIGZAG)
    YearSlideKind.STREAK -> SlideLook(Color(0xFF5A1A24), Color(0xFF1E0A0D), ShevitsaMotif.RHOMBUS)
    YearSlideKind.ARCHETYPE -> SlideLook(Color(0xFF1F160B), Color(0xFF45320F), archetypeMotif(s.archetype))
    YearSlideKind.OUTRO -> SlideLook(Brand.Burgundy, Brand.Ink, ShevitsaMotif.ROSETTE)
}

private fun archetypeMotif(a: Archetype) = when (a) {
    Archetype.BOOKWORM -> ShevitsaMotif.ROSETTE
    Archetype.LOYAL -> ShevitsaMotif.TREE_OF_LIFE
    Archetype.EXPLORER -> ShevitsaMotif.ZIGZAG
    Archetype.STEADY -> ShevitsaMotif.RHOMBUS
    Archetype.CURIOUS -> ShevitsaMotif.ROSETTE
}

/** Подчертано забавяне (M3 emphasized decelerate) — за всички влизания. */
private val Emphasized = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

/** Частта от [p] между [from] и [to] (0..1), с [Emphasized]. */
private fun part(p: Float, from: Float, to: Float): Float = Emphasized.transform(((p - from) / (to - from)).coerceIn(0f, 1f))

/** Влизане: избледняване + изплуване отдолу — само в графичния слой (без прекомпозиране). */
private fun Modifier.rise(appear: () -> Float, from: Float, to: Float, dy: Dp = 22.dp): Modifier = graphicsLayer {
    val t = part(appear(), from, to)
    alpha = t
    translationY = (1f - t) * dy.toPx()
}

/** Шевица, която „се избродира“ от средата навън. */
private fun Modifier.stitchIn(appear: () -> Float, from: Float, to: Float): Modifier = graphicsLayer {
    val t = part(appear(), from, to)
    scaleX = t
    alpha = if (t > 0f) 1f else 0f
    transformOrigin = TransformOrigin.Center
}

/** Фонът на слайда: градиентът, мека светлина и огромен мотив „воден знак“, който бавно се завърта ([drift] 0..1). */
@Composable
internal fun SlideBackdrop(look: SlideLook, drift: () -> Float, modifier: Modifier = Modifier, fill: Boolean = true) {
    Spacer(
        modifier.fillMaxSize().clearAndSetSemantics { }.drawWithCache {
            val side = size.minDimension * 1.15f
            val motif = Path().apply {
                fillType = PathFillType.EvenOdd
                addShevitsaMotif(look.motif, 0f, 0f, side)
            }
            val glow = Brush.radialGradient(
                listOf(Brand.Gold.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(size.width / 2f, size.height * 0.42f), radius = size.maxDimension * 0.55f,
            )
            val bg = look.brush
            onDrawBehind {
                // В картата за споделяне фонът е на рамката — тук само светлината и мотивът.
                if (fill) {
                    drawRect(bg)
                    drawRect(glow)
                }
                rotate(degrees = 12f * drift(), pivot = Offset(size.width / 2f, size.height * 0.45f)) {
                    translate(size.width / 2f - side / 2f, size.height * 0.45f - side / 2f) {
                        drawPath(motif, Brand.Gold.copy(alpha = 0.055f))
                    }
                }
            }
        },
    )
}

/**
 * Съдържанието на слайда (без фона). [appear] — влизането 0..1 (при намалено движение и в
 * картинката за споделяне — 1). Текстът следва размера на шрифта в системата и се пренася;
 * големите цифри не растат с него, а се смаляват по наличната височина ([numeral]). С [scroll]
 * (историята) — ако не се побира (хоризонтално, таблет, едър шрифт), слайдът се превърта.
 */
@Composable
internal fun SlideBody(s: ReadingYear.Summary, kind: YearSlideKind, appear: () -> Float, modifier: Modifier = Modifier, scroll: ScrollState? = null) {
    BoxWithConstraints(modifier.fillMaxWidth().clearAndSetSemantics { }) {
        // Цифрите — в пълен размер от ~560 dp височина нагоре; в картата за споделяне — винаги.
        val scale = if (scroll == null) 1f else (maxHeight / SLIDE_DESIGN_HEIGHT).coerceIn(0.45f, 1f)
        // Широк и нисък слайд (телефон хоризонтално): текстът и графиката са един до друг.
        val landscape = scroll != null && maxWidth >= 480.dp && maxWidth > maxHeight * 1.6f
        CompositionLocalProvider(LocalNumeralScale provides scale, LocalSlideLandscape provides landscape, LocalSlideShare provides (scroll == null)) {
            Column(
                (if (scroll != null) Modifier.verticalScroll(scroll) else Modifier).fillMaxWidth().heightIn(min = maxHeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (kind) {
                    YearSlideKind.INTRO -> IntroSlide(s, appear)
                    YearSlideKind.BOOKS -> BooksSlide(s, appear)
                    YearSlideKind.AUTHOR -> AuthorSlide(s, appear)
                    YearSlideKind.FAVOURITE -> FavouriteSlide(s, appear)
                    YearSlideKind.SECTION -> SectionSlide(s, appear)
                    YearSlideKind.MONTH -> MonthSlide(s, appear)
                    YearSlideKind.STREAK -> StreakSlide(s, appear)
                    YearSlideKind.ARCHETYPE -> ArchetypeSlide(s, appear)
                    YearSlideKind.OUTRO -> OutroSlide(s, appear)
                }
            }
        }
    }
}

/** Височината, при която големите цифри са в пълния си размер. */
private val SLIDE_DESIGN_HEIGHT = 560.dp

/** Колко от пълния си размер са големите цифри (по наличната височина). */
private val LocalNumeralScale = staticCompositionLocalOf { 1f }

/** Слайдът е широк и нисък (хоризонтално) — подрежда се на две колони. */
private val LocalSlideLandscape = staticCompositionLocalOf { false }

/** Слайдът се рисува за картата за споделяне (без превъртане): емблемата е вече в рамката. */
private val LocalSlideShare = staticCompositionLocalOf { false }

/**
 * Размерът на голяма цифра (годината, броят книги, месецът): [size] при обикновен шрифт —
 * не расте с шрифта в системата (иначе не се побира), а се смалява по височината на слайда.
 */
@Composable
private fun numeral(size: Float): TextUnit = with(LocalDensity.current) { (size * LocalNumeralScale.current).dp.toSp() }

private val Paper = Brand.Parchment
private val GoldSoft = Brand.GoldLight

@Composable
private fun SlideLabel(text: String, appear: () -> Float) {
    // Като [CardLabel], но се пренася на втори ред (едър шрифт).
    Text(
        text.uppercase(), style = sans(11.sp, Brand.Gold, FontWeight.Bold, spacing = 0.2f), textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0f, 0.3f),
    )
    Spacer(Modifier.height(14.dp))
}

/** Голямо число, което „отброява“ до [target]; прекомпозира се само при смяна на цифрата. */
@Composable
private fun CountUp(target: Int, appear: () -> Float, from: Float, to: Float, size: Int = 150, color: Color = Brand.Gold) {
    val shown by remember(target) { derivedStateOf { (target * part(appear(), from, to)).roundToInt() } }
    Text(
        shown.toString(),
        style = serif(numeral(size.toFloat()), color, lineHeight = 1f).copy(fontFeatureSettings = "tnum"),
        maxLines = 1,
        modifier = Modifier.graphicsLayer {
            val t = part(appear(), from - 0.05f, from + 0.15f)
            alpha = t
            scaleX = 0.85f + 0.15f * t
            scaleY = scaleX
        },
    )
}

/** Мотив от шевица, изчертан в квадрат. */
@Composable
private fun MotifMark(motif: ShevitsaMotif, size: Dp, color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier.size(size).drawWithCache {
            val path = Path().apply {
                fillType = PathFillType.EvenOdd
                addShevitsaMotif(motif, 0f, 0f, this@drawWithCache.size.minDimension)
            }
            onDrawBehind { drawPath(path, color) }
        },
    )
}

@Composable
private fun IntroSlide(s: ReadingYear.Summary, appear: () -> Float) {
    MotifMark(
        ShevitsaMotif.ROSETTE, 64.dp, Brand.Gold,
        Modifier.graphicsLayer {
            val t = part(appear(), 0f, 0.45f)
            alpha = t
            scaleX = 0.3f + 0.7f * t
            scaleY = scaleX
            rotationZ = -90f * (1f - t)
        },
    )
    Spacer(Modifier.height(22.dp))
    SlideLabel(stringResource(R.string.year_title), appear)
    // Цифрите на годината „падат“ една след друга.
    Row {
        s.year.toString().forEachIndexed { i, digit ->
            val start = 0.12f + i * 0.08f
            Text(
                digit.toString(),
                style = serif(numeral(124f), Brand.Gold, lineHeight = 1f).copy(fontFeatureSettings = "tnum"),
                modifier = Modifier.graphicsLayer {
                    val t = part(appear(), start, start + 0.38f)
                    alpha = t
                    translationY = -(1f - t) * 60.dp.toPx()
                },
            )
        }
    }
    Spacer(Modifier.height(6.dp))
    ShevitsaStrip(ShevitsaMotif.ZIGZAG, Brand.Gold, Modifier.width(210.dp).height(12.dp).stitchIn(appear, 0.45f, 0.85f))
    Spacer(Modifier.height(18.dp))
    Text(
        stringResource(R.string.year_intro_title),
        style = serif(32.sp, Paper, FontWeight.Normal).copy(fontStyle = FontStyle.Italic),
        textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.55f, 0.95f),
    )
}

@Composable
private fun BooksSlide(s: ReadingYear.Summary, appear: () -> Float) {
    SlideLabel(stringResource(R.string.year_books_label), appear)
    CountUp(s.books, appear, 0.1f, 0.7f)
    Text(
        pluralStringResource(R.plurals.year_books_unit, s.books),
        style = serif(38.sp, Paper),
        modifier = Modifier.rise(appear, 0.5f, 0.8f),
    )
    Spacer(Modifier.height(22.dp))
    // По един бод за всяка книга — „избродирват се“ заедно с броенето.
    val stitches = s.books.coerceAtMost(40)
    val columns = 10
    val rows = (stitches + columns - 1) / columns
    Canvas(Modifier.width(250.dp).height((rows * 25).dp)) {
        val cell = size.width / columns
        val r = cell * 0.32f
        val lit = (stitches * part(appear(), 0.1f, 0.7f)).roundToInt()
        for (i in 0 until lit) {
            val cx = (i % columns) * cell + cell / 2f
            val cy = (i / columns) * 25.dp.toPx() + 25.dp.toPx() / 2f
            val p = Path().apply {
                moveTo(cx, cy - r); lineTo(cx + r, cy); lineTo(cx, cy + r); lineTo(cx - r, cy); close()
            }
            drawPath(p, if (i % 2 == 0) Brand.Gold else GoldSoft.copy(alpha = 0.75f))
        }
    }
    Spacer(Modifier.height(18.dp))
    Text(
        pluralStringResource(R.plurals.year_books_months, s.activeMonths, s.activeMonths),
        style = sans(17.sp, GoldSoft),
        textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.65f, 1f),
    )
}

@Composable
private fun AuthorSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val name = shortAuthor(s.favouriteAuthor.orEmpty())
    SlideLabel(stringResource(R.string.year_author_label), appear)
    Box(contentAlignment = Alignment.Center) {
        Text(
            name,
            style = serif(if (name.length > 22) 44.sp else 56.sp, Paper, lineHeight = 1.02f),
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            // Името се „разкрива“ отляво надясно.
            modifier = Modifier.padding(vertical = 30.dp).drawWithContent {
                val t = part(appear(), 0.15f, 0.65f)
                clipRect(right = size.width * t) { this@drawWithContent.drawContent() }
            },
        )
    }
    ShevitsaStrip(ShevitsaMotif.TREE_OF_LIFE, Brand.Gold, Modifier.width(180.dp).height(12.dp).stitchIn(appear, 0.5f, 0.85f))
    Spacer(Modifier.height(22.dp))
    // Гръбчета на книгите от автора — „изникват“ на рафта.
    val spines = s.favouriteAuthorCount.coerceIn(1, 9)
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(spines) { i ->
            val h = listOf(64, 54, 70, 58, 66, 52, 62, 68, 56)[i]
            Box(
                Modifier.width(16.dp).height(h.dp)
                    .rise(appear, 0.45f + i * 0.04f, 0.75f + i * 0.04f, dy = 30.dp)
                    .background(if (i % 3 == 1) Brand.Burgundy else Brand.Gold, RoundedCornerShape(3.dp))
                    .border(1.dp, GoldSoft.copy(alpha = 0.6f), RoundedCornerShape(3.dp)),
            )
        }
    }
    Box(Modifier.width((spines * 21 + 24).dp).height(3.dp).background(GoldSoft.copy(alpha = 0.5f)))
    Spacer(Modifier.height(16.dp))
    Text(
        pluralStringResource(R.plurals.history_year_count, s.favouriteAuthorCount, s.favouriteAuthorCount),
        style = sans(18.sp, GoldSoft, FontWeight.SemiBold),
        modifier = Modifier.rise(appear, 0.7f, 1f),
    )
}

/**
 * Най-харесаната книга от „Читателския дневник“: заглавието, авторът и оценката в розетки, които
 * „разцъфват“ една след друга. Само заглавие, автор и оценка — бележката е лична и не стига дотук.
 */
@Composable
private fun FavouriteSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val fav = s.favourite ?: return
    SlideLabel(stringResource(R.string.year_favourite_label), appear)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(ReadingDiary.MAX_RATING) { i ->
            val start = 0.1f + i * 0.07f
            RosetteMark(
                filled = i < fav.rating, size = 34.dp, fill = Brand.Gold, empty = GoldSoft.copy(alpha = 0.4f), stitch = Brand.Ink.copy(alpha = 0.55f),
                modifier = Modifier.graphicsLayer {
                    val t = part(appear(), start, start + 0.3f)
                    alpha = t
                    scaleX = 0.4f + 0.6f * t
                    scaleY = scaleX
                    rotationZ = -45f * (1f - t)
                },
            )
        }
    }
    Spacer(Modifier.height(26.dp))
    Text(
        "„" + fav.title + "“",
        style = serif(if (fav.title.length > 28) 36.sp else 46.sp, Paper, lineHeight = 1.05f).copy(fontStyle = FontStyle.Italic),
        textAlign = TextAlign.Center,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.rise(appear, 0.35f, 0.7f),
    )
    if (fav.author.isNotBlank()) {
        Spacer(Modifier.height(10.dp))
        Text(
            shortAuthor(fav.author), style = sans(18.sp, GoldSoft, FontWeight.SemiBold), textAlign = TextAlign.Center,
            maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.rise(appear, 0.45f, 0.8f),
        )
    }
    Spacer(Modifier.height(20.dp))
    ShevitsaStrip(ShevitsaMotif.ROSETTE, Brand.Gold, Modifier.width(180.dp).height(12.dp).stitchIn(appear, 0.55f, 0.9f))
    Spacer(Modifier.height(16.dp))
    Text(
        stringResource(R.string.year_favourite_text, fav.rating, ReadingDiary.MAX_RATING),
        style = sans(16.sp, GoldSoft), textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.7f, 1f),
    )
}

@Composable
private fun SectionSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val top = s.topSection ?: return
    val look = lookOf(YearSlideKind.SECTION, s)
    SlideLabel(stringResource(R.string.year_section_label), appear)
    MotifMark(
        look.motif, 128.dp, Brand.Gold,
        Modifier.graphicsLayer {
            val t = part(appear(), 0.05f, 0.55f)
            alpha = t
            // Лек „отскок“: малко над 1 и обратно.
            val bounce = if (t < 1f) 1f + 0.12f * kotlin.math.sin(t * Math.PI).toFloat() else 1f
            scaleX = 0.2f + 0.8f * t * bounce
            scaleY = scaleX
            rotationZ = 45f * (1f - t)
        },
    )
    Spacer(Modifier.height(22.dp))
    Text(
        stringResource(UdcLabels.res(top.section)),
        style = serif(42.sp, Paper, lineHeight = 1.05f),
        textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.35f, 0.7f),
    )
    Spacer(Modifier.height(6.dp))
    Text(
        pluralStringResource(R.plurals.history_year_count, top.count, top.count),
        style = sans(18.sp, GoldSoft, FontWeight.SemiBold),
        modifier = Modifier.rise(appear, 0.45f, 0.8f),
    )
    if (s.sections.size >= 2) {
        Spacer(Modifier.height(26.dp))
        val rows = s.sections.take(3)
        val max = rows.maxOf { it.count }.toFloat()
        Column(Modifier.widthIn(max = 300.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rows.forEachIndexed { i, sc ->
                val color = lerp(sc.section?.let { UdcCoverColors[it] } ?: Brand.Muted, Color.White, 0.25f)
                Column(Modifier.rise(appear, 0.5f + i * 0.08f, 0.8f + i * 0.08f)) {
                    Row {
                        Text(stringResource(UdcLabels.res(sc.section)), style = sans(13.sp, Paper), maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        Text(sc.count.toString(), style = sans(13.sp, GoldSoft, FontWeight.Bold))
                    }
                    Spacer(Modifier.height(4.dp))
                    Canvas(Modifier.fillMaxWidth().height(8.dp)) {
                        val t = part(appear(), 0.55f + i * 0.08f, 0.95f)
                        drawRoundRect(Color.White.copy(alpha = 0.1f), cornerRadius = CornerRadius(size.height / 2f))
                        drawRoundRect(color, size = Size(size.width * sc.count / max * t, size.height), cornerRadius = CornerRadius(size.height / 2f))
                    }
                }
            }
        }
    }
}

/** Името на месеца с главна буква („Септември“). */
@Composable
private fun monthName(m: Month): String {
    val locale = Formatters.uiLocale(LocalContext.current)
    return m.getDisplayName(DateStyle.FULL_STANDALONE, locale).replaceFirstChar { it.titlecase(locale) }
}

@Composable
private fun monthInitials(): List<String> {
    val locale = Formatters.uiLocale(LocalContext.current)
    return remember(locale) { Month.entries.map { it.getDisplayName(DateStyle.NARROW_STANDALONE, locale).uppercase(locale) } }
}

@Composable
private fun MonthSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val peak = s.peakMonth ?: return
    val landscape = LocalSlideLandscape.current
    // Месеците като колони от кръстат бод — по един бод за книга; рекордният е в злато.
    val chart: @Composable (Modifier) -> Unit = { m ->
        EmbroideredMonths(
            counts = s.months,
            labels = monthInitials(),
            progress = { part(appear(), 0.25f, 0.9f) },
            thread = DarkBurgundyThread,
            peak = Brand.Gold,
            baseline = GoldSoft.copy(alpha = 0.35f),
            labelStyle = { i -> sans(12.sp, if (i == peak.value - 1) Brand.Gold else GoldSoft.copy(alpha = 0.7f), if (i == peak.value - 1) FontWeight.Bold else FontWeight.Normal) },
            modifier = m,
        )
    }
    if (landscape) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(Modifier.weight(0.8f), horizontalAlignment = Alignment.CenterHorizontally) { MonthHeader(s, peak, appear) }
            chart(Modifier.weight(1.2f))
        }
    } else {
        MonthHeader(s, peak, appear)
        Spacer(Modifier.height(26.dp))
        chart(Modifier.widthIn(max = 340.dp))
    }
}

@Composable
private fun MonthHeader(s: ReadingYear.Summary, peak: Month, appear: () -> Float) {
    SlideLabel(stringResource(R.string.year_month_label), appear)
    val name = monthName(peak)
    // Дългите имена („Септември“) — по-дребно, за да са на един ред и в картата за споделяне.
    Text(name, style = serif(numeral(if (name.length > 6) 54f else 64f), Brand.Gold, lineHeight = 1f), maxLines = 1, modifier = Modifier.rise(appear, 0.05f, 0.4f))
    Spacer(Modifier.height(6.dp))
    Text(
        pluralStringResource(R.plurals.history_year_count, s.peakCount, s.peakCount),
        style = serif(26.sp, Paper),
        modifier = Modifier.rise(appear, 0.15f, 0.5f),
    )
}

@Composable
private fun StreakSlide(s: ReadingYear.Summary, appear: () -> Float) {
    SlideLabel(stringResource(R.string.year_streak_label), appear)
    CountUp(s.longestStreak, appear, 0.1f, 0.5f, size = 140)
    Text(
        pluralStringResource(R.plurals.year_streak_unit, s.longestStreak),
        style = serif(32.sp, Paper),
        modifier = Modifier.rise(appear, 0.35f, 0.65f),
    )
    Spacer(Modifier.height(30.dp))
    // Дванадесет бода — месеците; поредицата светва бод по бод.
    val initials = monthInitials()
    val streak = s.streakMonths
    Column(Modifier.widthIn(max = 320.dp).fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(30.dp)) {
            val slot = size.width / 12f
            val r = minOf(slot * 0.42f, size.height / 2f)
            val cy = size.height / 2f
            val stroke = Stroke(1.5.dp.toPx())
            s.months.forEachIndexed { i, n ->
                val cx = i * slot + slot / 2f
                val p = Path().apply { moveTo(cx, cy - r); lineTo(cx + r, cy); lineTo(cx, cy + r); lineTo(cx - r, cy); close() }
                val inStreak = i in streak
                val lit = if (inStreak) part(appear(), 0.3f + (i - streak.first) * 0.05f, 0.45f + (i - streak.first) * 0.05f) else 0f
                when {
                    inStreak && lit > 0f -> drawPath(p, Brand.Gold, alpha = lit)
                    n > 0 -> drawPath(p, GoldSoft.copy(alpha = 0.45f))
                    else -> drawPath(p, GoldSoft.copy(alpha = 0.3f), style = stroke)
                }
                if (inStreak && lit < 1f) drawPath(p, GoldSoft.copy(alpha = 0.3f), style = stroke)
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth()) {
            initials.forEachIndexed { i, label ->
                Text(label, style = sans(12.sp, if (i in streak) Brand.Gold else GoldSoft.copy(alpha = 0.6f)), textAlign = TextAlign.Center, maxLines = 1, modifier = Modifier.weight(1f))
            }
        }
    }
    Spacer(Modifier.height(24.dp))
    Text(stringResource(R.string.year_streak_text), style = sans(17.sp, GoldSoft), textAlign = TextAlign.Center, modifier = Modifier.rise(appear, 0.6f, 1f))
}

/** Името и описанието на читателския тип. */
internal fun archetypeTexts(a: Archetype): Pair<Int, Int> = when (a) {
    Archetype.BOOKWORM -> R.string.year_type_bookworm to R.string.year_type_bookworm_text
    Archetype.LOYAL -> R.string.year_type_loyal to R.string.year_type_loyal_text
    Archetype.EXPLORER -> R.string.year_type_explorer to R.string.year_type_explorer_text
    Archetype.STEADY -> R.string.year_type_steady to R.string.year_type_steady_text
    Archetype.CURIOUS -> R.string.year_type_curious to R.string.year_type_curious_text
}

@Composable
private fun ArchetypeSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val (title, text) = archetypeTexts(s.archetype)
    SlideLabel(stringResource(R.string.year_type_label), appear)
    val shape = RoundedCornerShape(26.dp)
    // Картата се „обръща“ към читателя (като карта за игра).
    Column(
        Modifier.widthIn(max = 290.dp).fillMaxWidth()
            .graphicsLayer {
                val t = part(appear(), 0.05f, 0.6f)
                rotationY = 90f * (1f - t)
                alpha = if (t > 0f) 1f else 0f
                cameraDistance = 14f * density
            }
            .background(Brush.verticalGradient(listOf(Color(0xFF2B1E0E), Brand.Ink)), shape)
            .border(1.5.dp, Brand.Gold, shape)
            .padding(horizontal = 22.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MotifMark(archetypeMotif(s.archetype), 84.dp, Brand.Gold)
        Spacer(Modifier.height(18.dp))
        Text(stringResource(title), style = serif(40.sp, Brand.Gold, lineHeight = 1.05f), textAlign = TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        ShevitsaStrip(archetypeMotif(s.archetype), Brand.Gold.copy(alpha = 0.8f), Modifier.width(150.dp).height(10.dp))
        Spacer(Modifier.height(16.dp))
        Text(stringResource(text), style = sans(17.sp, Paper), textAlign = TextAlign.Center, modifier = Modifier.rise(appear, 0.55f, 0.9f))
    }
}

@Composable
private fun OutroSlide(s: ReadingYear.Summary, appear: () -> Float) {
    val landscape = LocalSlideLandscape.current
    // Лентата е „готова“ веднага, ако слайдът не се появява сега (картата за споделяне, излизащ слайд).
    val still = remember { Snapshot.withoutReadObservation { appear() } >= 1f }
    val band: @Composable (Modifier) -> Unit = { m ->
        Column(m, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(R.string.year_shevitsa_label).uppercase(),
                style = sans(11.sp, Brand.Gold, FontWeight.Bold, spacing = 0.2f), textAlign = TextAlign.Center,
                modifier = Modifier.rise(appear, 0.2f, 0.5f),
            )
            Spacer(Modifier.height(8.dp))
            PersonalBand(s, Modifier.widthIn(max = 340.dp), still = still)
        }
    }
    if (landscape) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            band(Modifier.weight(1f))
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) { OutroTexts(s, appear, compact = true) }
        }
        return
    }
    if (!LocalSlideShare.current) Box(contentAlignment = Alignment.Center) {
        // Меко сияние зад емблемата.
        Spacer(
            Modifier.size(150.dp).graphicsLayer { alpha = part(appear(), 0.1f, 0.6f) }.drawWithCache {
                val glow = Brush.radialGradient(listOf(Brand.Gold.copy(alpha = 0.45f), Color.Transparent))
                onDrawBehind { drawCircle(glow) }
            },
        )
        CardEmblem(
            84.dp,
            Modifier.graphicsLayer {
                val t = part(appear(), 0f, 0.5f)
                alpha = t
                scaleX = 0.6f + 0.4f * t
                scaleY = scaleX
            },
        )
    }
    if (!LocalSlideShare.current) Spacer(Modifier.height(18.dp))
    OutroTexts(s, appear, compact = false)
    Spacer(Modifier.height(22.dp))
    band(Modifier.fillMaxWidth())
}

@Composable
private fun OutroTexts(s: ReadingYear.Summary, appear: () -> Float, compact: Boolean) {
    Text(
        stringResource(R.string.year_outro_title),
        style = serif(if (compact) 30.sp else 40.sp, Paper, lineHeight = 1.05f),
        textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.3f, 0.65f),
    )
    Spacer(Modifier.height(if (compact) 8.dp else 14.dp))
    Text(
        stringResource(R.string.year_outro_text, s.year + 1),
        style = sans(if (compact) 15.sp else 17.sp, GoldSoft),
        textAlign = TextAlign.Center,
        modifier = Modifier.rise(appear, 0.6f, 0.95f),
    )
    Spacer(Modifier.height(if (compact) 4.dp else 10.dp))
    Text("chyavorec.org", style = serif(if (compact) 20.sp else 22.sp, Brand.Gold), modifier = Modifier.rise(appear, 0.7f, 1f))
}

/** Описанието на слайда за екранния четец — целият слайд е един възел. */
@Composable
internal fun slideDescription(s: ReadingYear.Summary, kind: YearSlideKind): String = when (kind) {
    YearSlideKind.INTRO -> stringResource(R.string.year_intro_cd, s.year)
    YearSlideKind.BOOKS -> pluralStringResource(R.plurals.year_books_cd, s.books, s.books, s.year) + " " +
        pluralStringResource(R.plurals.year_books_months, s.activeMonths, s.activeMonths)
    YearSlideKind.AUTHOR -> stringResource(R.string.year_author_cd, shortAuthor(s.favouriteAuthor.orEmpty()), s.favouriteAuthorCount)
    YearSlideKind.FAVOURITE -> s.favourite?.let { f ->
        val book = if (f.author.isBlank()) f.title else f.title + ", " + shortAuthor(f.author)
        stringResource(R.string.year_favourite_cd, book, f.rating, ReadingDiary.MAX_RATING)
    }.orEmpty()
    YearSlideKind.SECTION -> s.topSection?.let {
        pluralStringResource(R.plurals.year_section_cd, it.count, it.count, stringResource(UdcLabels.res(it.section)))
    }.orEmpty()
    YearSlideKind.MONTH -> s.peakMonth?.let { pluralStringResource(R.plurals.year_month_cd, s.peakCount, s.peakCount, monthName(it)) }.orEmpty()
    YearSlideKind.STREAK -> pluralStringResource(R.plurals.year_streak_cd, s.longestStreak, s.longestStreak)
    YearSlideKind.ARCHETYPE -> archetypeTexts(s.archetype).let { (t, d) -> stringResource(R.string.year_type_cd, stringResource(t), stringResource(d)) }
    YearSlideKind.OUTRO -> stringResource(R.string.year_outro_title) + " " + stringResource(R.string.year_outro_text, s.year + 1) + " " +
        pluralStringResource(R.plurals.year_shevitsa_cd, s.books, s.books)
}

/**
 * Слайдът като картинка за споделяне (9:16): същото съдържание в крайния си вид, във фирмената
 * рамка с читалището и QR кода към сайта.
 */
@Composable
fun YearShareCard(s: ReadingYear.Summary, kind: YearSlideKind, url: String) {
    val look = lookOf(kind, s)
    BrandCardFrame(ShareFormat.STORY, url, background = look.brush, motif = look.motif, footerBand = if (kind == YearSlideKind.OUTRO) null else ({ PersonalBandStrip(s) })) {
        Box(Modifier.fillMaxWidth().weight(1f)) {
            SlideBackdrop(look, drift = { 0f }, fill = false)
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                if (kind != YearSlideKind.INTRO) {
                    CardLabel(stringResource(R.string.year_title_with, s.year), color = GoldSoft.copy(alpha = 0.8f))
                }
                SlideBody(s, kind, appear = { 1f }, modifier = Modifier.weight(1f).fillMaxHeight())
            }
        }
    }
}

/** Вътрешни отстъпи на слайда в историята — под лентите горе и над бутона долу. */
internal val StoryContentPadding = PaddingValues(start = 28.dp, end = 28.dp, top = 96.dp, bottom = 104.dp)

/** Същото на нисък екран (хоризонтално) — само колкото лентите горе и бутона долу. */
internal val StoryContentPaddingCompact = PaddingValues(start = 28.dp, end = 28.dp, top = 76.dp, bottom = 84.dp)
