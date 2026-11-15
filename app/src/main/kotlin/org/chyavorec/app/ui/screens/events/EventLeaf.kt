package org.chyavorec.app.ui.screens.events

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import org.chyavorec.app.ui.components.rememberReducedMotion
import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
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
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.rememberOccasionTitle
import org.chyavorec.app.ui.components.LocalNavAnimatedScope
import org.chyavorec.app.ui.theme.AppMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaCalendar
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.model.Event

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

/** Хартията на листа в светла тема — топла слонова кост (в тъмна — повърхността на темата). Общ и с „Нов лист“ при удължаване. */
internal val LeafPaper = Color(0xFFFFFCF4)

/** Радиусът на ъглите и на прорезите на перфорацията. */
private val LeafCorner = 18.dp
private val LeafNotch = 9.dp

/**
 * Едната половина на листа: горната (заглавка и ден) е със заоблени горни и „прегризани“ долни
 * ъгли, долната (отрязъкът) — обратно; заедно са един лист, скъсан по перфорацията.
 */
internal class LeafHalfShape(private val top: Boolean, private val corner: Dp, private val notch: Dp) : Shape {
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
 * с „след 19 дни“ и часа (ако има). При отваряне листът „пада“ отгоре около горния си ръб
 * ([rememberLeafTurn]); значката в списъка не се разтяга в него.
 * [torn] — събитието е отворено в календара: отрязъкът се „откъсва“ (виж [rememberStubTear]).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EventLeaf(event: Event, date: LocalDate, today: LocalDate, modifier: Modifier = Modifier, torn: Boolean = false) {
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
    // Календарът не казва дали събитието е запазено — само че е било отворено там.
    val added = if (torn) resources.getString(R.string.event_opened_calendar) else null
    val description = listOfNotNull(
        weekday.replaceFirstChar { it.uppercase() }, Formatters.date(context, date), feast, countdown, clock, added,
    ).joinToString(", ")
    // Цифрата е огромна, но не расте с шрифта над 1.3× (иначе не се събира); текстът до нея — расте.
    val fontScale = LocalDensity.current.fontScale
    val numeralSp = (116f * fontScale.coerceAtMost(1.3f) / fontScale).sp
    val topShape = remember { LeafHalfShape(top = true, LeafCorner, LeafNotch) }
    val stubShape = remember { LeafHalfShape(top = false, LeafCorner, LeafNotch) }
    val dash = ink.copy(alpha = 0.35f)
    val turn = rememberLeafTurn()
    val turnReduced = rememberReducedMotion()

    Column(
        modifier.widthIn(max = 360.dp).fillMaxWidth()
            .then(
                if (turn == null) Modifier
                else Modifier.graphicsLayer {
                    // Листът се „обръща надолу“ около горния ръб; при намалено движение — само проява.
                    val p = turn.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    cameraDistance = 12f * density
                    if (turnReduced) alpha = p.coerceIn(0f, 1f)
                    else {
                        rotationX = leafTurnDegrees(p)
                        alpha = (p / 0.35f).coerceIn(0f, 1f)
                    }
                },
            )
            .clearAndSetSemantics { contentDescription = description },
    ) {
        Column(Modifier.shadow(4.dp, topShape).background(paper)) {
            // Заглавката: плетена бордо лента — шевица, месецът и годината, шевица.
            Column(Modifier.fillMaxWidth().background(Brand.Burgundy).padding(vertical = 6.dp)) {
                ShevitsaStrip(motif, Brand.Gold, Modifier.fillMaxWidth().height(20.dp))
                // Месецът и разделът — на един ред, ако се събират; иначе разделът слиза под месеца
                // (тесен екран 320 dp, едър шрифт). Месецът никога не се реже.
                FlowRow(
                    Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        monthYear, color = Brand.Parchment, fontFamily = Cormorant, fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp, lineHeight = 26.sp, modifier = Modifier.padding(end = 12.dp),
                    )
                    event.category?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            it.uppercase(), color = Brand.GoldLight, style = MaterialTheme.typography.labelSmall,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                        )
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
        val tear = rememberStubTear(torn)
        val reduced = rememberReducedMotion()
        Row(
            Modifier.fillMaxWidth()
                // Мястото под откъснатия отрязък е запазено — не ляга върху заглавието под листа.
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val reserve = (STUB_RESERVE.toPx() * tear.value.coerceIn(0f, 1f)).toInt()
                    layout(placeable.width, placeable.height + reserve) { placeable.place(0, 0) }
                }
                .graphicsLayer {
                    // Откъснат: слиза с 4 dp и леко се накланя (−1,5°) около средата на горния ръб.
                    val t = tear.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    translationY = 4.dp.toPx() * t
                    rotationZ = STUB_TILT_DEG * t
                }
                .shadow(4.dp, stubShape).background(paper)
                .drawBehind {
                    val n = LeafNotch.toPx()
                    // Перфорацията — тропосване ([StitchSpec.Basting]).
                    drawBasting(dash, 0f, n + 6.dp.toPx(), size.width - n - 6.dp.toPx())
                }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // При намалено движение листът не се откъсва — отметка на отрязъка.
            if (torn && reduced) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
            }
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

/** Наклонът на откъснатия отрязък (°) и мястото, запазено под него. */
private const val STUB_TILT_DEG = -1.5f
private val STUB_RESERVE = 12.dp

/**
 * Перфорацията като отделни бодове на [StitchSpec.Basting] (същата дебелина и стъпка) —
 * обикновени черти, без ефект на четката при всяко рисуване.
 */
private fun DrawScope.drawBasting(color: Color, y: Float, from: Float, to: Float) {
    val b = StitchSpec.Basting
    val w = b.widthDp * density
    // С кръглите краища видимата черта е dashDp, а празнината — gapDp.
    val on = (b.dashDp * density - w).coerceAtLeast(0.01f * density)
    val period = (b.dashDp + b.gapDp) * density
    var x = from
    while (x < to) {
        drawLine(color, Offset(x, y), Offset(minOf(x + on, to), y), strokeWidth = w, cap = StrokeCap.Round)
        x += period
    }
}

/** Наклонът на листа (°) при напредък [p] на отварянето: −75° в началото (обърнат нагоре), 0° — на място. */
internal const val LEAF_TURN_DEG = -75f

internal fun leafTurnDegrees(p: Float): Float = LEAF_TURN_DEG * (1f - p)

/**
 * Напредъкът на „падането“ на листа (0 — обърнат нагоре, 1 — на място) от прехода на екрана:
 * при отваряне — пружина, при „назад“ — обратно (и жестът го превърта, защото е част от
 * прехода). При намалено движение — еднократна кратка проява само при отваряне. Извън
 * навигация (тестове, preview) — null: листът е неподвижен.
 */
@Composable
internal fun rememberLeafTurn(): State<Float>? {
    val nav = LocalNavAnimatedScope.current ?: return null
    if (rememberReducedMotion()) {
        // Само при влизане в екрана (не при завъртане или връщане към него).
        val fade = remember { Animatable(if (nav.transition.currentState == EnterExitState.PreEnter) 0f else 1f) }
        LaunchedEffect(Unit) { fade.animateTo(1f, tween(AppMotion.ShortMs)) }
        return fade.asState()
    }
    return nav.transition.animateFloat(
        transitionSpec = { if (targetState == EnterExitState.Visible) AppMotion.defaultSpatial() else tween(AppMotion.ShortMs) },
        label = "event-leaf-turn",
    ) { if (it == EnterExitState.Visible) 1f else 0f }
}

/**
 * Откъсването на отрязъка (0 — на място, 1 — откъснат): веднъж, когато [torn] стане вярно
 * (след връщане от календара), на пружина и с лек тик. Вече откъснат при първа композиция
 * (завъртане, връщане към екрана) — направо в крайно положение, без повторение; при намалено
 * движение не се движи (отметката е на отрязъка).
 */
@Composable
private fun rememberStubTear(torn: Boolean): Animatable<Float, AnimationVector1D> {
    val reduced = rememberReducedMotion()
    val haptic = LocalHapticFeedback.current
    val tear = remember { Animatable(if (torn && !reduced) 1f else 0f) }
    LaunchedEffect(torn, reduced) {
        when {
            !torn || reduced -> tear.snapTo(0f)
            tear.value < 1f -> {
                haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                tear.animateTo(1f, AppMotion.defaultSpatial())
            }
        }
    }
    return tear
}

/**
 * „Бродиран“ ромб — прекъснат контур с ромбче в средата (най-старият пазещ знак, [ShevitsaMotif.RHOMBUS]).
 * Заменя точката под деня с проява в месечния календар и разделя празника в листа.
 */
@Composable
internal fun StitchedRhombus(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val s = size.minDimension
        // Тичащ бод ([StitchSpec.Running]) — като контурите на розетките и печата.
        val bod = StitchSpec.Running.stroke(density)
        val stroke = bod.width
        val half = s / 2f - stroke
        val c = Offset(size.width / 2f, size.height / 2f)
        val outline = Path().apply {
            moveTo(c.x, c.y - half); lineTo(c.x + half, c.y); lineTo(c.x, c.y + half); lineTo(c.x - half, c.y); close()
        }
        drawPath(outline, color, style = bod)
        val dot = Path()
        val r = s * 0.14f
        dot.moveTo(c.x, c.y - r); dot.lineTo(c.x + r, c.y); dot.lineTo(c.x, c.y + r); dot.lineTo(c.x - r, c.y); dot.close()
        drawPath(dot, color)
    }
}
