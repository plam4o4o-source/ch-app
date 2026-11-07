package org.chyavorec.app.ui.screens.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors

/*
 * Четенето на статия: „конец“ на прочетеното (кръстат бод по горния ръб), паралакс на
 * снимката и плаваща лента с действията (скрива се при четене надолу, връща се нагоре).
 * Превъртането се чете само в слоя/при рисуване — без рекомпозиция на кадър.
 */

/** Математиката на четенето — чисти функции. */
internal object ArticleReading {
    /** Снимката се движи с половината от скоростта на текста. */
    const val PARALLAX = 0.5f

    /** Прочетената част: превъртането спрямо цялото превъртане; без превъртане (кратка статия) — 0. */
    fun fraction(scroll: Int, max: Int): Float =
        if (max <= 0 || max == Int.MAX_VALUE) 0f else (scroll.toFloat() / max).coerceIn(0f, 1f)

    /** Колко от снимката е „изминала“ (0 — в покой, 1 — превъртяна с цялата си височина). */
    fun heroProgress(scroll: Float, heroHeight: Float): Float =
        if (heroHeight <= 0f) 0f else (scroll / heroHeight).coerceIn(0f, 1f)

    /** Лентата с действията след превъртане с [consumedDy] (< 0 — към края): над [slop] се скрива/показва. */
    fun toolbarVisible(current: Boolean, consumedDy: Float, slop: Float): Boolean = when {
        consumedDy < -slop -> false
        consumedDy > slop -> true
        else -> current
    }
}

/** Следи превъртането на статията и казва дали лентата с действията да се вижда. */
internal class ToolbarScrollConnection(private val slopPx: Float) : NestedScrollConnection {
    var visible by mutableStateOf(true)
        private set

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        visible = ArticleReading.toolbarVisible(visible, consumed.y, slopPx)
        return Offset.Zero
    }
}

/**
 * Паралакс на заглавната снимка: при превъртане тя се движи с [ArticleReading.PARALLAX] от
 * скоростта на текста, леко се уголемява и избледнява. Слага се след `clip` — изрязва се в рамката.
 */
internal fun Modifier.heroParallax(scroll: ScrollState): Modifier = graphicsLayer {
    val s = scroll.value.toFloat()
    val f = ArticleReading.heroProgress(s, size.height)
    translationY = s * ArticleReading.PARALLAX
    val k = 1f + 0.06f * f
    scaleX = k
    scaleY = k
    alpha = 1f - 0.4f * f
}

/**
 * „Конецът“ на прочетеното: ред кръстати бодове по горния ръб, избродиран до [progress] (0..1),
 * със светла точка-игла в края. При намалено движение — обикновена тънка линия. Декоративен.
 */
@Composable
internal fun ReadingThread(progress: () -> Float, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val gold = LocalExtendedColors.current.gold
    Spacer(
        modifier.clearAndSetSemantics { }.drawWithCache {
            val h = size.height
            val inset = h * 0.16f
            val stitches = Path()
            if (!reduced) {
                // Всеки бод е „X“ с ширина колкото височината — допират се с ъглите, като ред по канава.
                var x = 0f
                while (x < size.width) {
                    stitches.moveTo(x + inset, h - inset); stitches.lineTo(x + h - inset, inset)
                    stitches.moveTo(x + inset, inset); stitches.lineTo(x + h - inset, h - inset)
                    x += h
                }
            }
            val thread = Stroke(width = h * 0.26f, cap = StrokeCap.Round)
            onDrawBehind {
                val end = size.width * progress().coerceIn(0f, 1f)
                if (end <= 0f) return@onDrawBehind
                if (reduced) {
                    drawRect(gold, Offset(0f, h * 0.3f), Size(end, h * 0.4f))
                } else {
                    clipRect(right = end) { drawPath(stitches, gold, style = thread) }
                    drawCircle(Brand.GoldLight, h * 0.3f, Offset(end, h / 2f))
                }
            }
        },
    )
}

/**
 * Плаващата лента с действията на статията — по образеца на плаващата лента на Material 3
 * Expressive (хапче 64 dp, бутони 48 dp), с тънък златен кант. [visible] — скрива се надолу
 * (при намалено движение — само избледнява).
 */
@Composable
internal fun ArticleToolbar(visible: Boolean, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    val reduced = rememberReducedMotion()
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = if (reduced) fadeIn() else slideInVertically { it } + fadeIn(),
        exit = if (reduced) fadeOut() else slideOutVertically { it } + fadeOut(),
        label = "article-toolbar",
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 6.dp,
            border = BorderStroke(1.dp, LocalExtendedColors.current.gold.copy(alpha = 0.35f)),
            modifier = Modifier.semantics { isTraversalGroup = true },
        ) {
            Row(
                Modifier.heightIn(min = 64.dp).padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}
