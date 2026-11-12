package org.chyavorec.app.ui.share

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.EasterEggs
import org.chyavorec.app.ui.components.HolidayOrnament
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.occasion.GreetingCards
import org.chyavorec.domain.occasion.Occasion
import org.chyavorec.domain.occasion.OccasionOrnament

/** Бордо картата на честитката — по-светло горе, по-тъмно долу (фирменият фон на другите карти е мастилен). */
private val GreetingBackground = Brush.verticalGradient(listOf(Color(0xFF7D2A37), Brand.Burgundy, Color(0xFF3B1219)))
private val Paper = Brand.Parchment

/** Текстовете на честитката за повода: (заглавие, пожелание) на езика на интерфейса; `null` — няма готов текст. */
internal object GreetingTexts {
    fun res(id: String): Pair<Int, Int>? = when (id) {
        "spring" -> R.string.greeting_title_spring to R.string.greeting_wish_spring
        "summer" -> R.string.greeting_title_summer to R.string.greeting_wish_summer
        "autumn" -> R.string.greeting_title_autumn to R.string.greeting_wish_autumn
        "winter" -> R.string.greeting_title_winter to R.string.greeting_wish_winter
        "new_year" -> R.string.greeting_title_new_year to R.string.greeting_wish_new_year
        "liberation" -> R.string.greeting_title_liberation to R.string.greeting_wish_liberation
        "labour" -> R.string.greeting_title_labour to R.string.greeting_wish_labour
        "gergyovden" -> R.string.greeting_title_gergyovden to R.string.greeting_wish_gergyovden
        "may24" -> R.string.greeting_title_may24 to R.string.greeting_wish_may24
        "unification" -> R.string.greeting_title_unification to R.string.greeting_wish_unification
        "independence" -> R.string.greeting_title_independence to R.string.greeting_wish_independence
        "easter" -> R.string.greeting_title_easter to R.string.greeting_wish_easter
        "christmas_eve" -> R.string.greeting_title_christmas_eve to R.string.greeting_wish_christmas_eve
        "christmas" -> R.string.greeting_title_christmas to R.string.greeting_wish_christmas
        "levski_birth" -> R.string.greeting_title_levski_birth to R.string.greeting_wish_levski_birth
        "enlighteners" -> R.string.greeting_title_enlighteners to R.string.greeting_wish_enlighteners
        "children_book" -> R.string.greeting_title_children_book to R.string.greeting_wish_children_book
        "book_day" -> R.string.greeting_title_book_day to R.string.greeting_wish_book_day
        "children_day" -> R.string.greeting_title_children_day to R.string.greeting_wish_children_day
        "jordan" -> R.string.greeting_title_jordan to R.string.greeting_wish_jordan
        "babinden" -> R.string.greeting_title_babinden to R.string.greeting_wish_babinden
        "trifon" -> R.string.greeting_title_trifon to R.string.greeting_wish_trifon
        "baba_marta" -> R.string.greeting_title_baba_marta to R.string.greeting_wish_baba_marta
        "lazarovden" -> R.string.greeting_title_lazarovden to R.string.greeting_wish_lazarovden
        "tsvetnitsa" -> R.string.greeting_title_tsvetnitsa to R.string.greeting_wish_tsvetnitsa
        "forgiveness" -> R.string.greeting_title_forgiveness to R.string.greeting_wish_forgiveness
        "ascension" -> R.string.greeting_title_ascension to R.string.greeting_wish_ascension
        "enyovden" -> R.string.greeting_title_enyovden to R.string.greeting_wish_enyovden
        "ilinden" -> R.string.greeting_title_ilinden to R.string.greeting_wish_ilinden
        "assumption" -> R.string.greeting_title_assumption to R.string.greeting_wish_assumption
        "dimitrovden" -> R.string.greeting_title_dimitrovden to R.string.greeting_wish_dimitrovden
        "nikulden" -> R.string.greeting_title_nikulden to R.string.greeting_wish_nikulden
        "ignazhden" -> R.string.greeting_title_ignazhden to R.string.greeting_wish_ignazhden
        else -> null
    }

    /** Празниците от сайта носят собствен текст (на английски — ако е даден, иначе на български). */
    fun texts(context: Context, o: Occasion): Pair<String, String>? {
        o.custom?.let { c ->
            val bg = Formatters.isBulgarian(context)
            val en = c.titleEn
            return if (bg || en == null) c.titleBg to c.textBg else en to (c.textEn ?: "")
        }
        val (title, wish) = res(o.id) ?: return null
        return context.getString(title) to context.getString(wish)
    }
}

/**
 * Честитката (9:16): орнаментът на деня на златно сияние, заглавието, личният ред и пожеланието
 * на бордо фон със златна рамка. Личният ред се свива според дължината и е най-много на два реда.
 */
@Composable
internal fun GreetingShareCard(card: ShareCard.Greeting) {
    BrandCardFrame(card.format, card.url, background = GreetingBackground, motif = card.motif) {
        Spacer(Modifier.weight(0.35f))
        Box(
            Modifier.fillMaxWidth().height(138.dp).drawWithCache {
                val r = size.height / 2f
                val glow = Brush.radialGradient(
                    listOf(Brand.Gold.copy(alpha = 0.30f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height / 2f), radius = r * 1.15f,
                )
                onDrawBehind { drawCircle(glow, radius = r * 1.15f) }
            },
            contentAlignment = Alignment.Center,
        ) { GreetingArt(card.ornament, card.motif) }
        Spacer(Modifier.height(12.dp))
        CardLabel(stringResource(R.string.greeting_card_label), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(8.dp))
        Text(
            card.title, style = serif(titleSize(card.title, 38f)), maxLines = 3, overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
        if (card.line.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(
                card.line,
                style = serif(GreetingCards.lineSizeSp(card.line).sp, Brand.GoldLight, FontWeight.Normal).copy(fontStyle = FontStyle.Italic),
                maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(14.dp))
        GreetingDivider(Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(14.dp))
        Text(
            card.wish, style = sans(15.sp, Paper.copy(alpha = 0.92f)).copy(lineHeight = 22.sp), maxLines = 7, overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.weight(0.65f))
    }
}

/** Орнаментът на деня в голям размер (плътността се увеличава — линиите остават пропорционални); без орнамент — мотив от шевица. */
@Composable
private fun GreetingArt(ornament: OccasionOrnament, motif: org.chyavorec.app.ui.theme.ShevitsaMotif) {
    val base = LocalDensity.current
    val scaled = Density(base.density * ART_SCALE, 1f)
    when (ornament) {
        OccasionOrnament.NONE -> Canvas(Modifier.size(104.dp).clearAndSetSemantics { }) {
            val s = size.minDimension
            val ring = Stroke(1.2.dp.toPx())
            drawCircle(Brand.Gold.copy(alpha = 0.7f), radius = s / 2f, style = ring)
            drawCircle(Brand.Gold.copy(alpha = 0.3f), radius = s / 2f - 5.dp.toPx(), style = Stroke(0.7.dp.toPx()))
            val inner = s * 0.62f
            val path = Path().apply {
                fillType = PathFillType.EvenOdd
                addShevitsaMotif(motif, (s - inner) / 2f, (s - inner) / 2f, inner)
            }
            drawPath(path, Brand.Gold)
        }
        OccasionOrnament.EASTER_EGGS -> CompositionLocalProvider(LocalDensity provides scaled) { EasterEggs() }
        else -> CompositionLocalProvider(LocalDensity provides scaled) { HolidayOrnament(ornament) }
    }
}

/** Тънка златна линия с ромбче по средата. */
@Composable
private fun GreetingDivider(modifier: Modifier = Modifier) {
    Canvas(modifier.width(150.dp).height(10.dp).clearAndSetSemantics { }) {
        val y = size.height / 2f
        val gap = 9.dp.toPx()
        val c = size.width / 2f
        val line = Brand.Gold.copy(alpha = 0.75f)
        val w = 1.dp.toPx()
        drawLine(line, Offset(0f, y), Offset(c - gap, y), w)
        drawLine(line, Offset(c + gap, y), Offset(size.width, y), w)
        val r = 4.dp.toPx()
        val d = Path().apply { moveTo(c, y - r); lineTo(c + r, y); lineTo(c, y + r); lineTo(c - r, y); close() }
        drawPath(d, Brand.Gold)
    }
}

private const val ART_SCALE = 4.3f
