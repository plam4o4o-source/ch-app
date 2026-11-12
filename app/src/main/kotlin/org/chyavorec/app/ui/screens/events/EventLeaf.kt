package org.chyavorec.app.ui.screens.events

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.rememberOccasionTitle
import org.chyavorec.app.ui.components.sharedBoundsKey
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaCalendar
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.model.Event
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Колко дни има до събитието — думите на листа („след 19 дни“, „днес“, „преди 3 дни“).
 * Чисти функции: тестват се с Robolectric (bg и en, множествено число) без рисуване.
 */
internal object EventCountdown {
    /** Дни от [today] до [date]: положително — предстои, 0 — днес, отрицателно — минало. */
    fun days(date: LocalDate, today: LocalDate): Long = ChronoUnit.DAYS.between(today, date)

    fun label(resources: Resources, days: Long): String {
        val n = kotlin.math.abs(days).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
        return when {
            days == 0L -> resources.getString(R.string.event_countdown_today)
            days > 0 -> resources.getQuantityString(R.plurals.event_in_days, n, n)
            else -> resources.getQuantityString(R.plurals.event_days_ago, n, n)
        }
    }
}

/** Часът за листа: нормализиран „18:30“, ако се чете; иначе — текстът от сайта; без час — null. */
internal fun eventClock(time: String?): String? {
    val raw = time?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val t = BulgarianDates.parseClock(raw) ?: return raw
    return "%02d:%02d".format(t.hour, t.minute)
}

/** Деня от седмицата („петък“ / „Friday“) — от низ в ресурсите, за да е в шрифтовия подмножество. */
internal fun weekdayName(resources: Resources, date: LocalDate): String =
    resources.getString(R.string.event_weekdays).split(',').getOrElse(date.dayOfWeek.value - 1) { "" }

/**
 * Мотивът на шевицата в заглавката на листа — по раздела на събитието, а без разпознат
 * раздел — по сезона и празника на датата ([ShevitsaCalendar]).
 */
internal fun leafMotif(category: String?, date: LocalDate?): ShevitsaMotif {
    val c = category?.lowercase().orEmpty()
    return when {
        listOf("концерт", "музик", "песен", "chor", "concert", "music").any { it in c } -> ShevitsaMotif.ROSETTE
        listOf("изложб", "изкуств", "книг", "лекци", "exhib", "art", "book").any { it in c } -> ShevitsaMotif.TREE_OF_LIFE
        listOf("празн", "събор", "фолк", "танц", "festival", "folk", "dance").any { it in c } -> ShevitsaMotif.ZIGZAG
        date != null -> ShevitsaCalendar.motifFor(date)
        else -> ShevitsaMotif.RHOMBUS
    }
}

/** Хартията на листа в светла тема — топла слонова кост (в тъмна — повърхността на темата). */
private val LeafPaper = Color(0xFFFFFCF4)

/** Радиусът на ъглите и на прорезите на перфорацията. */
private val LeafCorner = 18.dp
private val LeafNotch = 9.dp

/**
 * Едната половина на листа: горната (заглавка и ден) е със заоблени горни и „прегризани“ долни
 * ъгли, долната (отрязъкът) — обратно; заедно са един лист, скъсан по перфорацията.
 */
private class LeafHalfShape(private val top: Boolean, private val corner: Dp, private val notch: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val w = size.width
        val h = size.height
        val c = with(density) { corner.toPx() }.coerceAtMost(h / 2f)
        val n = with(density) { notch.toPx() }.coerceAtMost(h / 2f)
        val p = Path()
        if (top) {
            p.moveTo(0f, c)
            p.arcTo(Rect(0f, 0f, 2 * c, 2 * c), 180f, 90f, false)
            p.lineTo(w - c, 0f)
            p.arcTo(Rect(w - 2 * c, 0f, w, 2 * c), 270f, 90f, false)
            p.lineTo(w, h - n)
            p.arcTo(Rect(w - n, h - n, w + n, h + n), 270f, -90f, false)
            p.lineTo(n, h)
            p.arcTo(Rect(-n, h - n, n, h + n), 0f, -90f, false)
        } else {
            p.moveTo(n, 0f)
            p.lineTo(w - n, 0f)
            p.arcTo(Rect(w - n, -n, w + n, n), 180f, -90f, false)
            p.lineTo(w, h - c)
            p.arcTo(Rect(w - 2 * c, h - 2 * c, w, h), 0f, 90f, false)
            p.lineTo(c, h)
            p.arcTo(Rect(0f, h - 2 * c, 2 * c, h), 90f, 90f, false)
            p.lineTo(0f, n)
            p.arcTo(Rect(-n, -n, n, n), 90f, -90f, false)
        }
        p.close()
        return Outline.Generic(p)
    }
}

/**
 * Събитието като откъсващ се лист от стенен календар: плетена бордо заглавка с шевица по раздела,
 * огромна цифра на деня (Cormorant), денят от седмицата, празникът за деня, перфорация и отрязък
 * с „след 19 дни“ и часа (ако има). [sharedBoundsKey] „event-date-<id>“ свързва листа със значката
 * на датата в списъка — тя „прелита“ и израства в него (без движение при намалено движение).
 */
@Composable
internal fun EventLeaf(event: Event, date: LocalDate, today: LocalDate, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val dark = LocalExtendedColors.current.isDark
    val scheme = MaterialTheme.colorScheme
    val paper = if (dark) scheme.surfaceContainerHigh else LeafPaper
    val ink = if (dark) scheme.onSurface else Brand.Ink
    val accent = if (dark) Brand.GoldLight else Brand.GoldText
    val weekday = remember(date, resources) { weekdayName(resources, date) }
    val monthYear = remember(date, context) {
        val m = if (Formatters.isBulgarian(context)) BulgarianDates.monthName(date.monthValue)
        else date.month.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, java.util.Locale.ENGLISH)
        m.replaceFirstChar { it.uppercase() } + " " + date.year
    }
    val feast = rememberOccasionTitle(date)
    val countdown = EventCountdown.label(resources, EventCountdown.days(date, today))
    val clock = eventClock(event.time)
    val motif = leafMotif(event.category, date)
    val description = listOfNotNull(
        weekday.replaceFirstChar { it.uppercase() }, Formatters.date(context, date), feast, countdown, clock,
    ).joinToString(", ")
    // Цифрата е огромна, но не расте с шрифта над 1.3× (иначе не се събира); текстът до нея — расте.
    val fontScale = LocalDensity.current.fontScale
    val numeralSp = (116f * fontScale.coerceAtMost(1.3f) / fontScale).sp
    val topShape = remember { LeafHalfShape(top = true, LeafCorner, LeafNotch) }
    val stubShape = remember { LeafHalfShape(top = false, LeafCorner, LeafNotch) }
    val dash = ink.copy(alpha = 0.35f)

    Column(
        modifier.widthIn(max = 360.dp).fillMaxWidth().sharedBoundsKey("event-date-${event.id}")
            .clearAndSetSemantics { contentDescription = description },
    ) {
        Column(Modifier.shadow(4.dp, topShape).background(paper)) {
            // Заглавката: плетена бордо лента — шевица, месецът и годината, шевица.
            Column(Modifier.fillMaxWidth().background(Brand.Burgundy).padding(vertical = 6.dp)) {
                ShevitsaStrip(motif, Brand.Gold, Modifier.fillMaxWidth().height(20.dp))
                val monthText: @Composable (Modifier) -> Unit = { m ->
                    Text(
                        monthYear, color = Brand.Parchment, fontFamily = Cormorant, fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = m,
                    )
                }
                val categoryText: @Composable (Modifier, TextAlign) -> Unit = { m, align ->
                    event.category?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            it.uppercase(), color = Brand.GoldLight, style = MaterialTheme.typography.labelSmall,
                            maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = align, modifier = m,
                        )
                    }
                }
                // При едър шрифт месецът и разделът не се събират един до друг — един под друг (месецът не се реже).
                if (fontScale >= 1.3f) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp)) {
                        monthText(Modifier)
                        categoryText(Modifier, TextAlign.Start)
                    }
                } else {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        monthText(Modifier.weight(1f, fill = false))
                        Spacer(Modifier.width(12.dp))
                        categoryText(Modifier.weight(1f), TextAlign.End)
                    }
                }
                ShevitsaStrip(motif, Brand.Gold, Modifier.fillMaxWidth().height(20.dp))
            }
            Box(Modifier.fillMaxWidth().height(2.dp).background(Brand.Gold))
            Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    weekday.uppercase(), color = accent, style = MaterialTheme.typography.labelLarge, letterSpacing = 3.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
                Text(
                    date.dayOfMonth.toString(), color = ink, fontFamily = Cormorant, fontWeight = FontWeight.SemiBold,
                    fontSize = numeralSp, lineHeight = numeralSp * 0.95f, maxLines = 1,
                )
                if (feast != null) {
                    Spacer(Modifier.height(4.dp))
                    StitchedRhombus(accent, Modifier.size(14.dp))
                    Spacer(Modifier.height(6.dp))
                    Text(
                        feast, color = ink, fontFamily = Cormorant, fontStyle = FontStyle.Italic, fontSize = 20.sp,
                        textAlign = TextAlign.Center, maxLines = 3, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        // Отрязъкът: перфорацията е горният му ръб, а прорезите — в ъглите на двете половини.
        Row(
            Modifier.fillMaxWidth().shadow(4.dp, stubShape).background(paper)
                .drawBehind {
                    val n = LeafNotch.toPx()
                    drawLine(
                        dash, Offset(n + 6.dp.toPx(), 0f), Offset(size.width - n - 6.dp.toPx(), 0f), strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx())),
                    )
                }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                countdown, color = accent, fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = 24.sp,
                modifier = Modifier.weight(1f),
            )
            if (clock != null) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(clock, color = ink, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

/**
 * „Бродиран“ ромб — прекъснат контур с ромбче в средата (най-старият пазещ знак, [ShevitsaMotif.RHOMBUS]).
 * Заменя точката под деня с проява в месечния календар и разделя празника в листа.
 */
@Composable
internal fun StitchedRhombus(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val s = size.minDimension
        val stroke = (s * 0.11f).coerceAtLeast(1f)
        val half = s / 2f - stroke
        val c = Offset(size.width / 2f, size.height / 2f)
        val outline = Path().apply {
            moveTo(c.x, c.y - half); lineTo(c.x + half, c.y); lineTo(c.x, c.y + half); lineTo(c.x - half, c.y); close()
        }
        drawPath(
            outline, color,
            style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(stroke * 1.8f, stroke * 1.2f))),
        )
        val dot = Path()
        val r = s * 0.14f
        dot.moveTo(c.x, c.y - r); dot.lineTo(c.x + r, c.y); dot.lineTo(c.x, c.y + r); dot.lineTo(c.x - r, c.y); dot.close()
        drawPath(dot, color)
    }
}
