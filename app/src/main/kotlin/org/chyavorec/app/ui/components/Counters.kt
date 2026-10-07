package org.chyavorec.app.ui.components

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

/** Отброяването на „Читалището в числа“ — веднъж на процес се решава дали е за днес. */
private object CounterDay {
    private const val PREFS = "stats_anim"
    private const val KEY = "stats_anim_day"
    private var consumed = false

    /** true само при първото показване за деня (и само веднъж на процес). */
    fun shouldAnimate(context: Context): Boolean {
        if (consumed) return false
        consumed = true
        return runCatching {
            val today = LocalDate.now().toString()
            val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val first = prefs.getString(KEY, null) != today
            if (first) prefs.edit().putString(KEY, today).apply()
            first
        }.getOrDefault(false)
    }
}

/**
 * Дали броячите да „отброят“ от 0: само при първото показване за календарния ден
 * (запомня се в SharedPreferences), иначе крайните стойности се показват веднага.
 */
@Composable
fun rememberCountUpToday(): Boolean {
    val context = LocalContext.current
    return remember { CounterDay.shouldAnimate(context) }
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
