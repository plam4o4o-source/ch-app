package org.chyavorec.app.ui.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import kotlin.math.floor
import androidx.compose.animation.core.tween
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import org.chyavorec.app.ui.theme.LocalExtendedColors

/**
 * Отброяването на „Читалището в числа“ — веднъж на процес се решава дали е за днес.
 * SharedPreferences се четат извън главната нишка ([load]); денят се отбелязва
 * едва когато броячите наистина се покажат ([take]).
 */
private object CounterDay {
    private const val PREFS = "stats_anim"
    private const val KEY = "stats_anim_day"
    @Volatile private var prefs: SharedPreferences? = null
    @Volatile private var firstToday: Boolean? = null
    private var consumed = false

    /** Чете дали днес още не е имало отброяване. Блокиращо — само извън главната нишка. */
    @Synchronized
    fun load(context: Context): Boolean {
        firstToday?.let { return it }
        val first = runCatching {
            val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            prefs = p
            p.getString(KEY, null) != LocalDate.now().toString()
        }.getOrDefault(false)
        firstToday = first
        return first
    }

    /** true само при първото показване за деня (и само веднъж на процес); `null` — още не е прочетено. */
    @Synchronized
    fun take(): Boolean? {
        val first = firstToday ?: return null
        if (consumed) return false
        consumed = true
        // Файлът вече е в паметта — apply() записва на диска във фон.
        if (first) runCatching { prefs?.edit()?.putString(KEY, LocalDate.now().toString())?.apply() }
        return first
    }
}

/** Прочита отрано (извън главната нишка) дали броячите ще отброяват днес — напр. при отваряне на „Начало“. */
suspend fun prepareCountUpToday(context: Context) {
    withContext(Dispatchers.IO) { CounterDay.load(context) }
}

/**
 * Дали броячите да „отброят“ от 0: само при първото показване за календарния ден
 * (запомня се в SharedPreferences), иначе крайните стойности се показват веднага.
 * `null`, докато настройката се чете (извън главната нишка) — обикновено вече е
 * прочетена от [prepareCountUpToday].
 */
@Composable
fun rememberCountUpToday(): Boolean? {
    val context = LocalContext.current
    var result by remember { mutableStateOf(CounterDay.take()) }
    if (result == null) {
        LaunchedEffect(Unit) {
            prepareCountUpToday(context)
            result = CounterDay.take() ?: false
        }
    }
    return result
}

/**
 * Чистата логика на брояча-„одометър“ (тества се без Compose): цифрите на числото
 * и докъде да се завърти всяко колелце.
 */
internal object Odometer {
    /** Цифрите на |[value]| от най-старшата към най-младшата; 0 → [0]. */
    fun digits(value: Int): IntArray {
        // Long — за да е коректно и за Int.MIN_VALUE.
        var n = kotlin.math.abs(value.toLong())
        if (n == 0L) return intArrayOf(0)
        val out = ArrayList<Int>(10)
        while (n > 0) {
            out.add((n % 10).toInt())
            n /= 10
        }
        return out.asReversed().toIntArray()
    }

    /**
     * Целевата позиция на колелце, което сега е на [current] (позиция = цифра + 10 × обиколки),
     * за да покаже [digit]: при растящо число върти напред (9 → 0 → 1…), при намаляващо — назад.
     */
    fun rollTarget(current: Float, digit: Int, up: Boolean): Float {
        val c = kotlin.math.round(current).toInt()
        val shown = Math.floorMod(c, 10)
        val delta = if (up) Math.floorMod(digit - shown, 10) else -Math.floorMod(shown - digit, 10)
        return (c + delta).toFloat()
    }

    /**
     * Позицията, до която колелцето се завърта при първото показване (от 0):
     * единиците правят две пълни обиколки, десетиците — една, по-старшите — само до цифрата.
     */
    fun introTarget(digit: Int, place: Int): Float = (digit + 10 * (2 - place).coerceAtLeast(0)).toFloat()
}

/**
 * Число като брояч-„одометър“: всяка цифра е колелце, което се превърта вертикално —
 * при първото показване (ако [animate]) от 0, а при промяна на стойността — до
 * новата цифра. При намалено движение — направо крайната стойност.
 */
@Composable
fun AnimatedCounter(value: Int, label: String, modifier: Modifier = Modifier, animate: Boolean = true) {
    val reduced = rememberReducedMotion()
    Column(
        modifier.clearAndSetSemantics { contentDescription = "$value $label" },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OdometerNumber(value, MaterialTheme.typography.headlineLarge, LocalExtendedColors.current.gold, animateIn = animate && !reduced, reduced = reduced)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

@Composable
private fun OdometerNumber(value: Int, style: TextStyle, color: Color, animateIn: Boolean, reduced: Boolean) {
    val measurer = rememberTextMeasurer()
    // Цифрите се измерват веднъж; клетката е с ширината на най-широката (без „подскачане“).
    val glyphs = remember(measurer, style) { List(10) { measurer.measure(text = it.toString(), style = style) } }
    val cellW = glyphs.maxOf { it.size.width }
    val cellH = glyphs.maxOf { it.size.height }
    val density = LocalDensity.current
    val cell = with(density) { DpSize(cellW.toDp(), cellH.toDp()) }
    // Посоката на последната промяна (растящо → колелцата напред). Не е състояние:
    // използва се само от ефекта, стартиран в същата композиция.
    val last = remember { intArrayOf(value) }
    val up = value >= last[0]
    last[0] = value
    // Колелца, появили се след първия кадър (напр. 99 → 100), тръгват от 0.
    val composed = remember { booleanArrayOf(false) }
    SideEffect { composed[0] = true }
    val digits = Odometer.digits(value)
    Row {
        if (value < 0) Text("−", style = style, color = color)
        digits.forEachIndexed { i, d ->
            val place = digits.size - 1 - i
            key(place) {
                OdometerDigit(d, place, up, fromZero = animateIn || composed[0], reduced = reduced, glyphs = glyphs, cell = cell, cellW = cellW, color = color)
            }
        }
    }
}

@Composable
private fun OdometerDigit(
    digit: Int,
    place: Int,
    up: Boolean,
    fromZero: Boolean,
    reduced: Boolean,
    glyphs: List<TextLayoutResult>,
    cell: DpSize,
    cellW: Int,
    color: Color,
) {
    val pos = remember { Animatable(if (fromZero && !reduced) 0f else digit.toFloat()) }
    val intro = remember { booleanArrayOf(fromZero && !reduced) }
    LaunchedEffect(digit, reduced) {
        if (reduced) {
            pos.snapTo(digit.toFloat())
            return@LaunchedEffect
        }
        if (intro[0]) {
            intro[0] = false
            pos.animateTo(Odometer.introTarget(digit, place), tween(1100, easing = FastOutSlowInEasing))
        } else {
            val target = Odometer.rollTarget(pos.value, digit, up)
            if (target != pos.value) pos.animateTo(target, tween(650, easing = FastOutSlowInEasing))
        }
        // Нормализиране (позицията остава малко число, цифрата — същата).
        pos.snapTo(digit.toFloat())
    }
    // Позицията се чете само при рисуване — без рекомпозиция на кадър.
    Canvas(Modifier.size(cell).clipToBounds()) {
        val p = pos.value
        val base = floor(p)
        val frac = p - base
        val a = Math.floorMod(base.toInt(), 10)
        val b = Math.floorMod(base.toInt() + 1, 10)
        val h = size.height
        fun drawDigit(d: Int, y: Float) {
            val g = glyphs[d]
            drawText(g, color = color, topLeft = Offset((cellW - g.size.width) / 2f, y))
        }
        drawDigit(a, -frac * h)
        if (frac > 0.001f) drawDigit(b, (1f - frac) * h)
    }
}

/** „…**Празник**…“ (форматът на /api/calendar) → текст с удебелени части. */
fun boldMarkdown(line: String): AnnotatedString = buildAnnotatedString {
    val parts = line.split("**")
    parts.forEachIndexed { i, p ->
        if (i % 2 == 1) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(p) } else append(p)
    }
}
