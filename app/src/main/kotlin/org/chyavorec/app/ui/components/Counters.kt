package org.chyavorec.app.ui.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
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

/** Число, което „отброява“ до стойността си (като броячите на сайта); [animate] = false → веднага. */
@Composable
fun AnimatedCounter(value: Int, label: String, modifier: Modifier = Modifier, animate: Boolean = true) {
    val reduced = rememberReducedMotion()
    val instant = reduced || !animate
    val anim = remember { Animatable(if (instant) value.toFloat() else 0f) }
    LaunchedEffect(value) {
        if (instant) anim.snapTo(value.toFloat())
        else anim.animateTo(value.toFloat(), tween(900, easing = LinearOutSlowInEasing))
    }
    Column(
        modifier.clearAndSetSemantics { contentDescription = "$value $label" },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(anim.value.toInt().toString(), style = MaterialTheme.typography.headlineLarge, color = LocalExtendedColors.current.gold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

/** „…**Празник**…“ (форматът на /api/calendar) → текст с удебелени части. */
fun boldMarkdown(line: String): AnnotatedString = buildAnnotatedString {
    val parts = line.split("**")
    parts.forEachIndexed { i, p ->
        if (i % 2 == 1) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(p) } else append(p)
    }
}
