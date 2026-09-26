package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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

/** Число, което „отброява“ до стойността си (като броячите на сайта). */
@Composable
fun AnimatedCounter(value: Int, label: String, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val anim = remember { Animatable(if (reduced) value.toFloat() else 0f) }
    LaunchedEffect(value) { anim.animateTo(value.toFloat(), tween(if (reduced) 0 else 1400, easing = FastOutSlowInEasing)) }
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
