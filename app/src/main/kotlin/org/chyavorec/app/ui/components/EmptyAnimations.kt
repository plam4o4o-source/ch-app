package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 * Анимирани илюстрации на празните състояния — в линейния стил на ill_*.xml
 * (един златист цвят с различна плътност, поле 160×120). Canvas вместо Lottie/AVD:
 * пътищата се строят веднъж, а в кадъра се сменят само трансформации и прозрачност.
 * Цикълът се изиграва ~3 пъти и спира в покой; при намалено движение — само кадърът
 * в покой. Извън екрана (композицията е напуснала) корутината се отменя.
 */

/** Вид анимация според значката на празното състояние. */
enum class EmptyMotion(val periodMs: Int) {
    /** Няма резултати от търсене: страниците на отворена книга се разлистват. */
    PAGES(3200),

    /** Няма съобщения: плик, който леко се полюшва нагоре-надолу. */
    ENVELOPE(2800),

    /** Празна история: купчина книги — горната се повдига и пак „сяда“ на мястото си. */
    STACK(3000),

    /** Няма запазени: лентичката-разделител се люлее под книгата. */
    BOOKMARK(2400),

    /** Няма връзка: облак с пулсиращ (зачеркнат) сигнал. */
    OFFLINE(3000),
}

/** Анимацията за значката [icon]; `null` — статичната илюстрация (както досега). */
fun emptyMotionFor(icon: ImageVector): EmptyMotion? = when (icon) {
    Icons.Outlined.SearchOff -> EmptyMotion.PAGES
    Icons.Outlined.Campaign, Icons.Outlined.Mail -> EmptyMotion.ENVELOPE
    Icons.Outlined.AutoStories, Icons.Outlined.CollectionsBookmark -> EmptyMotion.STACK
    Icons.Outlined.BookmarkBorder, Icons.Outlined.Bookmarks -> EmptyMotion.BOOKMARK
    Icons.Outlined.CloudOff -> EmptyMotion.OFFLINE
    else -> null
}

private const val VIEW_W = 160f
private const val VIEW_H = 120f
private const val HALO = 0.12f
private const val FILL = 0.25f
private const val SOFT = 0.7f
private val Line = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
private val Thin = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)

private fun path(d: String): Path = PathParser().parsePathString(d).toPath()

/**
 * Пътищата на една илюстрация (в координати 160×120) — строят се веднъж: [a] — основната
 * форма, [b] и [c] — контури и детайли, [c]/[d] — подвижната част (според вида).
 */
private class EmptyArt(motion: EmptyMotion) {
    val a: Path
    val b: Path
    val c: Path
    val d: Path

    init {
        when (motion) {
            EmptyMotion.PAGES -> {
                a = path("M80,30C66,21 46,18 26,20L26,86C46,84 66,87 80,96ZM80,30C94,21 114,18 134,20L134,86C114,84 94,87 80,96Z")
                b = path(
                    "M80,30C66,21 46,18 26,20L26,86C46,84 66,87 80,96C94,87 114,84 134,86L134,20C114,18 94,21 80,30ZM80,30L80,96" +
                        "M18,28L18,94C42,91 64,94 80,104C96,94 118,91 142,94L142,28",
                )
                c = path("M38,38L66,41M38,50L66,53M38,62L60,64M94,41L122,38M94,53L122,50M100,64L122,62")
                // Разлистваната страница = дясната (огледално спрямо гръбчето е лявата).
                d = path("M80,30C94,21 114,18 134,20L134,86C114,84 94,87 80,96Z")
            }
            EmptyMotion.ENVELOPE -> {
                a = path("M49,34H111A5,5 0 0 1 116,39V79A5,5 0 0 1 111,84H49A5,5 0 0 1 44,79V39A5,5 0 0 1 49,34Z")
                b = path("M46,37L80,62L114,37")
                c = path("M47,82L70,61M113,82L90,61")
                d = Path()
            }
            EmptyMotion.STACK -> {
                a = path("M39,80H121A3,3 0 0 1 124,83V93A3,3 0 0 1 121,96H39A3,3 0 0 1 36,93V83A3,3 0 0 1 39,80ZM47,64H115A3,3 0 0 1 118,67V77A3,3 0 0 1 115,80H47A3,3 0 0 1 44,77V67A3,3 0 0 1 47,64Z")
                b = path("M48,81V95M112,81V95M56,65V79M106,65V79M24,100L136,100")
                // Горната книга, центрирана в (0,0) — местим/накланяме я цялата.
                c = path("M-33,-8H33A3,3 0 0 1 36,-5V5A3,3 0 0 1 33,8H-33A3,3 0 0 1 -36,5V-5A3,3 0 0 1 -33,-8Z")
                d = path("M-24,-7V7M24,-7V7M-14,0H14")
            }
            EmptyMotion.BOOKMARK -> {
                a = path("M58,14H102A4,4 0 0 1 106,18V80A4,4 0 0 1 102,84H58A4,4 0 0 1 54,80V18A4,4 0 0 1 58,14Z")
                b = path("M62,16V82M72,32L96,32M72,40L90,40")
                // Лентичката виси от (92,82) — оттам се люлее.
                c = path("M87,78L97,78L97,108L92,102L87,108Z")
                d = Path()
            }
            EmptyMotion.OFFLINE -> {
                a = path("M50,86C36,86 30,72 40,64C38,50 54,42 64,50C68,34 94,32 100,48C112,42 128,52 122,66C132,70 130,86 116,86Z")
                b = path("M66,58L96,88")
                c = Path()
                d = Path()
            }
        }
    }
}

/** 0→1 за [periodMs], [loops] пъти, после 0 (кадърът в покой). */
@Composable
private fun rememberLoopProgress(periodMs: Int, enabled: Boolean, loops: Int = 3): Animatable<Float, AnimationVector1D> {
    val progress = remember { Animatable(0f) }
    if (enabled) {
        LaunchedEffect(periodMs) {
            repeat(loops) {
                progress.snapTo(0f)
                progress.animateTo(1f, tween(periodMs, easing = LinearEasing))
            }
            progress.snapTo(0f)
        }
    }
    return progress
}

/**
 * Анимираната илюстрация [motion]. [stillFrame] (0..1) рисува един кадър без анимация —
 * за снимките в тестовете; 0 е кадърът в покой.
 */
@Composable
fun EmptyAnimation(motion: EmptyMotion, modifier: Modifier = Modifier, stillFrame: Float? = null) {
    val color = LocalExtendedColors.current.gold
    val art = remember(motion) { EmptyArt(motion) }
    // При намалено движение — само кадърът в покой (и ако настройката се смени по време на цикъла).
    val animate = stillFrame == null && !rememberReducedMotion()
    val progress = rememberLoopProgress(motion.periodMs, enabled = animate)
    Canvas(modifier) {
        val p = stillFrame ?: if (animate) progress.value else 0f // четене само във фазата на рисуване
        val s = min(size.width / VIEW_W, size.height / VIEW_H)
        withTransform({
            translate((size.width - VIEW_W * s) / 2f, (size.height - VIEW_H * s) / 2f)
            scale(s, s, Offset.Zero)
        }) {
            drawOval(color, Offset(22f, 10f), Size(116f, 100f), alpha = HALO)
            when (motion) {
                EmptyMotion.PAGES -> drawPages(art, color, p)
                EmptyMotion.ENVELOPE -> drawEnvelope(art, color, p)
                EmptyMotion.STACK -> drawStack(art, color, p)
                EmptyMotion.BOOKMARK -> drawBookmark(art, color, p)
                EmptyMotion.OFFLINE -> drawOffline(art, color, p)
            }
        }
    }
}

/** Гладка „гърбица“ 0→1→0 за t ∈ [0,1], извън — 0. */
private fun bump(t: Float): Float = if (t <= 0f || t >= 1f) 0f else sin(PI.toFloat() * t)

private fun DrawScope.drawPages(art: EmptyArt, color: Color, p: Float) {
    drawPath(art.a, color, alpha = FILL)
    drawPath(art.b, color, style = Line)
    drawPath(art.c, color, alpha = SOFT, style = Thin)
    // Две страници се обръщат една след друга; в покой съвпадат със страниците на книгата.
    for (i in 0..1) {
        val t = ((p - 0.12f - 0.22f * i) / 0.5f).coerceIn(0f, 1f)
        if (t <= 0f || t >= 1f) continue
        val angle = PI.toFloat() * FastOutSlowInEasing.transform(t)
        val sx = cos(angle)
        withTransform({
            translate(0f, -sin(angle) * 4f)
            scale(sx, 1f, Offset(80f, 63f))
        }) {
            drawPath(art.d, color, alpha = 0.35f)
            drawPath(art.d, color, style = Line)
        }
    }
}

private fun DrawScope.drawEnvelope(art: EmptyArt, color: Color, p: Float) {
    val phase = 2f * PI.toFloat() * p
    val lift = (1f - cos(phase)) / 2f // 0 в покой, 1 най-горе
    // Сянката се свива, докато пликът се издига.
    val shadowW = 68f - 16f * lift
    drawOval(color, Offset(80f - shadowW / 2f, 95f), Size(shadowW, 7f), alpha = 0.22f - 0.1f * lift)
    withTransform({
        translate(0f, -7f * lift)
        rotate(2.5f * sin(phase), Offset(80f, 59f))
    }) {
        drawPath(art.a, color, alpha = FILL)
        drawPath(art.c, color, alpha = SOFT, style = Thin)
        drawPath(art.a, color, style = Line)
        drawPath(art.b, color, style = Line)
        drawCircle(color, 4.5f, Offset(80f, 62f)) // печатът
    }
}

private fun DrawScope.drawStack(art: EmptyArt, color: Color, p: Float) {
    drawPath(art.a, color, alpha = FILL)
    drawPath(art.a, color, style = Line)
    drawPath(art.b, color, alpha = SOFT, style = Thin)
    // Горната книга: повдига се (0.10–0.35), пада (0.35–0.60) и леко отскача (0.60–0.78).
    val height = when {
        p < 0.10f -> 0f
        p < 0.35f -> 16f * FastOutSlowInEasing.transform((p - 0.10f) / 0.25f)
        p < 0.60f -> { val t = (p - 0.35f) / 0.25f; 16f * (1f - t * t) }
        else -> 3f * bump((p - 0.60f) / 0.18f)
    }
    val tilt = -7f * height / 16f
    withTransform({
        translate(76f, 56f - height)
        rotate(tilt, Offset.Zero)
    }) {
        drawPath(art.c, color, alpha = FILL)
        drawPath(art.c, color, style = Line)
        drawPath(art.d, color, alpha = SOFT, style = Thin)
    }
    // Прашинки при „сядането“.
    val dust = bump((p - 0.58f) / 0.3f)
    if (dust > 0f) {
        val spread = 6f * ((p - 0.58f) / 0.3f).coerceIn(0f, 1f)
        drawCircle(color, 2f, Offset(38f - spread, 61f - spread / 2f), alpha = 0.6f * dust)
        drawCircle(color, 1.5f, Offset(114f + spread, 60f - spread / 2f), alpha = 0.6f * dust)
    }
}

private fun DrawScope.drawBookmark(art: EmptyArt, color: Color, p: Float) {
    // Махало: най-силно в средата на цикъла, в покой — отвесно.
    val angle = 13f * sin(2f * PI.toFloat() * p) * (0.35f + 0.65f * bump(p))
    withTransform({ rotate(angle, Offset(92f, 82f)) }) {
        drawPath(art.c, color, alpha = 0.85f)
    }
    drawPath(art.a, color, alpha = FILL)
    drawPath(art.a, color, style = Line)
    drawPath(art.b, color, alpha = SOFT, style = Thin)
    drawCircle(color, 6f, Offset(84f, 58f), alpha = SOFT, style = Thin)
}

private fun DrawScope.drawOffline(art: EmptyArt, color: Color, p: Float) {
    val drift = 2f * sin(2f * PI.toFloat() * p)
    withTransform({ translate(drift, 0f) }) {
        drawPath(art.a, color, alpha = FILL)
        drawPath(art.a, color, style = Line)
    }
    // Сигналът: вълна на избледняване от точката навън, после всичко отново светва.
    val center = Offset(80f, 80f)
    drawCircle(color, 3f, Offset(80f, 77f), alpha = 1f - 0.7f * bump((p - 0.12f) / 0.35f))
    for (i in 0..2) {
        val r = 9f + 7f * i
        val a = SOFT + 0.3f - 0.75f * bump((p - 0.22f - 0.12f * i) / 0.35f)
        drawArc(
            color, startAngle = 225f, sweepAngle = 90f, useCenter = false,
            topLeft = Offset(center.x - r, center.y - r), size = Size(2f * r, 2f * r),
            alpha = a, style = Line,
        )
    }
    drawPath(art.b, color, style = Line)
}
