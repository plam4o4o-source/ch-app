package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector

/** Движението на панделката на отметката — чиста логика (тества се без Compose). */
internal object BookmarkDrop {
    /** Колко трае „падането“ с превишаване. */
    const val MS = 260
    /** Кога панделката стига най-ниската си точка (превишаването). */
    const val OVERSHOOT_AT_MS = 150
    const val OVERSHOOT = 1.12f

    /** Вертикално отместване (дял от височината на иконата) при прогрес [p]: 0 → издърпана нагоре, 1 → на място. */
    fun offset(p: Float): Float = (p - 1f) * 0.45f

    /** Разтягане по височина (от горния край) при прогрес [p]: панделката се „разгъва“ надолу. */
    fun stretch(p: Float): Float = 0.7f + 0.3f * p
}

/**
 * Отметка, която „пада“ като панделка: при запазване иконата идва отгоре, разтегната, и
 * се закрепва с кратко превишаване (~150 ms); при отмяна — веднага. Без анимация при първото
 * показване (списъкът не трябва да „пада“ при скролиране) и при намалено движение.
 */
@Composable
fun BookmarkIcon(
    saved: Boolean,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier = Modifier,
    savedIcon: ImageVector = Icons.Filled.Bookmark,
    unsavedIcon: ImageVector = Icons.Outlined.BookmarkBorder,
) {
    val reduced = rememberReducedMotion()
    val drop = remember { Animatable(1f) }
    var seen by remember { mutableStateOf(saved) }
    LaunchedEffect(saved, reduced) {
        if (saved == seen) return@LaunchedEffect
        seen = saved
        if (!saved || reduced) {
            drop.snapTo(1f)
            return@LaunchedEffect
        }
        drop.snapTo(0f)
        drop.animateTo(
            1f,
            keyframes {
                durationMillis = BookmarkDrop.MS
                0f at 0
                BookmarkDrop.OVERSHOOT at BookmarkDrop.OVERSHOOT_AT_MS using FastOutSlowInEasing
                1f at BookmarkDrop.MS using FastOutSlowInEasing
            },
        )
    }
    Icon(
        if (saved) savedIcon else unsavedIcon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.graphicsLayer {
            // Позицията се чете само при рисуване — без рекомпозиция на кадър.
            val p = drop.value
            transformOrigin = TransformOrigin(0.5f, 0f)
            translationY = BookmarkDrop.offset(p) * size.height
            scaleY = BookmarkDrop.stretch(p)
        },
    )
}
