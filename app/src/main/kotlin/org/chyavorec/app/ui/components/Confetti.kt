package org.chyavorec.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/*
 * „Шевичено“ конфети при истински успех (заявка, удължаване, „Искам да прочета“, напомняне):
 * ~36 ромбчета и кръстчета в цветовете на марката изскачат от мястото на действието и падат
 * за ~1 s. Частиците са в предварително заделени масиви; движението е формула от времето
 * (без състояние на кадър и без алокации). При намалено движение — без конфети (само снекбарът).
 */

/**
 * Състоянието на конфетито за един [ConfettiHost]: [burst] пуска ново избухване от
 * последното докосване (или от дадена точка). [seed] — фиксиран за тестовете (детерминирано).
 */
@Stable
class ConfettiState(internal val seed: Long? = null) {
    /** Колко избухвания са поискани (всяко ново рестартира анимацията). */
    var bursts by mutableIntStateOf(0)
        private set

    /** Дали в момента се рисува избухване. */
    var isPlaying by mutableStateOf(false)
        internal set

    /** Колко избухвания са изиграни (за тестовете). */
    internal var played = 0

    /** Откъде е последното избухване (в координатите на домакина); неуточнено — долу в средата. */
    internal var origin: Offset = Offset.Unspecified

    /** Последното докосване в домакина — „мястото на действието“ (не е състояние: не прекомпозира). */
    internal var lastTap: Offset = Offset.Unspecified

    fun burst(at: Offset = Offset.Unspecified) {
        origin = if (at.isSpecified) at else lastTap
        bursts++
    }
}

/** Конфетито на приложението (подава се от корена); извън него — `null` (без конфети). */
val LocalConfetti = staticCompositionLocalOf<ConfettiState?> { null }

/**
 * Обратната връзка за успех: тактилно потвърждение и (без намалено движение) конфети от мястото
 * на действието. Извиква се, когато действието наистина е успяло — не при натискането.
 * [signature] — подписът на вибрацията ([HapticSignature]); без него — обичайното потвърждение.
 */
@Composable
fun rememberSuccessFeedback(signature: HapticSignature? = null): () -> Unit {
    val confetti = LocalConfetti.current
    val haptic = LocalHapticFeedback.current
    val haptics = rememberAppHaptics()
    return remember(confetti, haptic, haptics, signature) {
        {
            if (signature != null) haptics.play(signature) else haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            confetti?.burst()
        }
    }
}

/** Продължителността на едно избухване. */
internal const val CONFETTI_MS = 1100

/** Брой частици в избухване. */
internal const val CONFETTI_PARTICLES = 36

/**
 * Кутия, над съдържанието на която може да изскочи конфети ([LocalConfetti]). Помни къде е
 * последното докосване (без да го „изяжда“); слоят с частиците съществува само докато трае избухването.
 */
@Composable
fun ConfettiHost(
    modifier: Modifier = Modifier,
    state: ConfettiState = remember { ConfettiState() },
    content: @Composable BoxScope.() -> Unit,
) {
    val reduced by rememberUpdatedState(rememberReducedMotion())
    val progress = remember { mutableFloatStateOf(0f) }
    val particles = remember { ConfettiParticles(CONFETTI_PARTICLES) }
    LaunchedEffect(state, state.bursts) {
        if (state.bursts == 0 || reduced) return@LaunchedEffect
        particles.reset(Random((state.seed ?: System.nanoTime()) + state.bursts))
        state.isPlaying = true
        state.played++
        try {
            // Собствен часовник от кадрите (не tween): физиката е във време, не в мащаба на анимациите.
            progress.floatValue = 0f
            val start = withFrameNanos { it }
            do {
                val elapsedMs = (withFrameNanos { it } - start) / 1_000_000f
                progress.floatValue = (elapsedMs / CONFETTI_MS).coerceAtMost(1f)
            } while (progress.floatValue < 1f)
        } finally {
            state.isPlaying = false
        }
    }
    Box(
        modifier.pointerInput(state) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    event.changes.firstOrNull { it.changedToDownIgnoreConsumed() }?.let { state.lastTap = it.position }
                }
            }
        },
    ) {
        CompositionLocalProvider(LocalConfetti provides state) { content() }
        if (state.isPlaying) {
            val colors = confettiColors()
            ConfettiLayer(particles, { state.origin }, { progress.floatValue }, colors, Modifier.matchParentSize())
        }
    }
}

/**
 * Неподвижен кадър от избухване: частиците от [seed] в момент [progress] (0..1) от [origin]
 * (неуточнено — долу в средата). За снимките в тестовете — детерминирано, без часовник.
 */
@Composable
fun ConfettiFrame(seed: Long, progress: Float, modifier: Modifier = Modifier, origin: Offset = Offset.Unspecified) {
    val particles = remember(seed) { ConfettiParticles(CONFETTI_PARTICLES).apply { reset(Random(seed)) } }
    ConfettiLayer(particles, { origin }, { progress }, confettiColors(), modifier)
}

@Composable
private fun ConfettiLayer(particles: ConfettiParticles, origin: () -> Offset, progress: () -> Float, colors: Array<Color>, modifier: Modifier) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val o = origin()
        val at = if (o.isSpecified) o else Offset(size.width / 2f, size.height * 0.8f)
        particles.draw(this, at, progress(), colors)
    }
}

/**
 * Цветовете на конфетито: злато, бордо и още два тона злато — светлото само в тъмна тема
 * (върху кремавия фон не се вижда); бордото е по-светло в тъмна тема.
 */
@Composable
private fun confettiColors(): Array<Color> {
    val gold = LocalExtendedColors.current.gold
    val burgundy = stitchBurgundy()
    val dark = LocalExtendedColors.current.isDark
    return remember(gold, burgundy, dark) {
        arrayOf(gold, if (dark) Brand.GoldLight else Brand.Gold, burgundy, Brand.GoldDark)
    }
}

/** Движението на частица — чисти функции (dp, секунди; y расте надолу). */
internal object ConfettiPhysics {
    /** Земното ускорение на конфетито (dp/s²). */
    const val GRAVITY = 1500f

    /** Въздушното съпротивление (1/s) — хартийките „плуват“, а не падат като камък. */
    const val DRAG = 2.2f

    /** Отместване по x след [t] s при начална скорост [vx]: съпротивлението го спира плавно. */
    fun dx(vx: Float, t: Float): Float = vx * (1f - exp(-DRAG * t)) / DRAG

    /** Отместване по y: нагоре с [vy] (< 0), после надолу към крайната скорост GRAVITY/DRAG. */
    fun dy(vy: Float, t: Float): Float {
        val terminal = GRAVITY / DRAG
        return terminal * t + (vy - terminal) * (1f - exp(-DRAG * t)) / DRAG
    }

    /** Плътността по прогреса 0..1: цели до 65 %, после избледняват до 0. */
    fun alpha(progress: Float): Float = if (progress <= 0.65f) 1f else ((1f - progress) / 0.35f).coerceIn(0f, 1f)
}

/** Частиците на едно избухване — масиви, заделени веднъж; [reset] ги пълни наново при избухване. */
internal class ConfettiParticles(val count: Int) {
    val vx = FloatArray(count)
    val vy = FloatArray(count)
    val rotation = FloatArray(count)
    val spin = FloatArray(count)
    val sizeDp = FloatArray(count)
    val flutter = FloatArray(count)
    val color = IntArray(count)
    val cross = BooleanArray(count)

    fun reset(random: Random) {
        for (i in 0 until count) {
            // Ветрило нагоре: от -160° до -20° (0° — надясно).
            val angle = (-160f + 140f * random.nextFloat()) * (PI.toFloat() / 180f)
            val speed = 300f + 420f * random.nextFloat()
            vx[i] = cos(angle) * speed
            vy[i] = sin(angle) * speed
            rotation[i] = 360f * random.nextFloat()
            spin[i] = (if (random.nextBoolean()) 1f else -1f) * (180f + 540f * random.nextFloat())
            sizeDp[i] = 6f + 4f * random.nextFloat()
            flutter[i] = 2f * PI.toFloat() * random.nextFloat()
            color[i] = random.nextInt(4)
            cross[i] = random.nextInt(3) == 0
        }
    }

    fun draw(scope: DrawScope, origin: Offset, progress: Float, colors: Array<Color>) = with(scope) {
        val t = progress * CONFETTI_MS / 1000f
        val alpha = ConfettiPhysics.alpha(progress)
        if (alpha <= 0f) return@with
        val d = density
        for (i in 0 until count) {
            val x = origin.x + ConfettiPhysics.dx(vx[i], t) * d
            val y = origin.y + ConfettiPhysics.dy(vy[i], t) * d
            val s = sizeDp[i] * d
            // „Пърхане“: частицата се обръща във въздуха (сплесква се по ширина).
            val flip = cos(flutter[i] + t * 9f)
            val c = colors[color[i] % colors.size]
            withTransform({
                translate(x, y)
                rotate(rotation[i] + spin[i] * t, Offset.Zero)
                scale(flip, 1f, Offset.Zero)
            }) {
                if (cross[i]) {
                    // Кръстче (кръстат бод).
                    val h = s * 0.42f
                    drawLine(c, Offset(-h, -h), Offset(h, h), strokeWidth = s * 0.26f, cap = StrokeCap.Round, alpha = alpha)
                    drawLine(c, Offset(-h, h), Offset(h, -h), strokeWidth = s * 0.26f, cap = StrokeCap.Round, alpha = alpha)
                } else {
                    // Ромбче — пътят е единичен, мащабира се (без нов път на кадър).
                    withTransform({ scale(s, s, Offset.Zero) }) { drawPath(UnitRhombus, c, alpha = alpha) }
                }
            }
        }
    }
}

/** Ромб с височина 1 и ширина 0.66, центриран в (0,0). */
private val UnitRhombus: Path = Path().apply {
    moveTo(0f, -0.5f)
    lineTo(0.33f, 0f)
    lineTo(0f, 0.5f)
    lineTo(-0.33f, 0f)
    close()
}
