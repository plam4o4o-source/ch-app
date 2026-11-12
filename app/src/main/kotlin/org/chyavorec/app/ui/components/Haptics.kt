package org.chyavorec.app.ui.components

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView

/*
 * Тактилният речник на приложението — три подписа вместо общо „тик“:
 *  - БОД (STITCH): два меки тика — запазване, отметка;
 *  - ПЕЧАТ (SEAL): глух удар и тик — удължаване, печатът „Върната“;
 *  - РАЗЛИСТВАНЕ (SHUFFLE): нарастващи тикове — картите на „Изненадай ме“.
 * От Android 11 — съставен ефект от примитиви (VibrationEffect.Composition), ако телефонът ги
 * поддържа; иначе (и на 8–10) — досегашните HapticFeedbackType през прозореца. Съставеният ефект
 * не минава през прозореца, затова сам се подчинява на системната настройка „Докосване“
 * (HAPTIC_FEEDBACK_ENABLED) — изключена, не вибрира нищо.
 */

/** Подписите на приложението. */
enum class HapticSignature { STITCH, SEAL, SHUFFLE }

/** Нотите на подписите — чисти данни (тестват се без Android). */
internal object HapticScores {
    /** Нота: примитив ([VibrationEffect.Composition]), сила 0..1 и пауза преди нея. */
    data class Note(val primitive: Int, val scale: Float, val delayMs: Int)

    // VibrationEffect.Composition.PRIMITIVE_THUD = 2, PRIMITIVE_TICK = 7 (API 30).
    const val THUD = 2
    const val TICK = 7

    val stitch = listOf(Note(TICK, 0.5f, 0), Note(TICK, 0.32f, 70))
    val seal = listOf(Note(THUD, 0.9f, 0), Note(TICK, 0.6f, 55))
    val shuffle = listOf(
        Note(TICK, 0.22f, 0), Note(TICK, 0.3f, 110), Note(TICK, 0.4f, 95),
        Note(TICK, 0.52f, 80), Note(TICK, 0.68f, 65), Note(TICK, 0.85f, 55),
    )

    fun of(signature: HapticSignature): List<Note> = when (signature) {
        HapticSignature.STITCH -> stitch
        HapticSignature.SEAL -> seal
        HapticSignature.SHUFFLE -> shuffle
    }

    /** Резервата за 8–10 (и телефони без примитиви): тип и пауза преди него. */
    data class Fallback(val type: HapticFeedbackType, val delayMs: Int)

    fun fallback(signature: HapticSignature): List<Fallback> = when (signature) {
        HapticSignature.STITCH -> listOf(Fallback(HapticFeedbackType.SegmentTick, 0), Fallback(HapticFeedbackType.SegmentTick, 70))
        HapticSignature.SEAL -> listOf(Fallback(HapticFeedbackType.Confirm, 0), Fallback(HapticFeedbackType.SegmentTick, 55))
        // Както досега: тик на всяко разместване (на ~160 ms).
        HapticSignature.SHUFFLE -> listOf(
            Fallback(HapticFeedbackType.SegmentTick, 0), Fallback(HapticFeedbackType.SegmentTick, 160), Fallback(HapticFeedbackType.SegmentTick, 160),
        )
    }
}

/** Изходът на съставените ефекти (Android 11+) — отделен, за да се тества без истински вибратор. */
internal interface CompositionOutput {
    /** Поддържа ли телефонът всички [primitives]. */
    fun supports(primitives: IntArray): Boolean
    /** Свири [notes]; `false` — не е успяло (тогава се ползва резервата). */
    fun play(notes: List<HapticScores.Note>): Boolean
}

@RequiresApi(Build.VERSION_CODES.R)
private class VibratorOutput(private val vibrator: Vibrator) : CompositionOutput {
    override fun supports(primitives: IntArray): Boolean =
        runCatching { vibrator.hasVibrator() && primitives.all { supported(it) } }.getOrDefault(false)

    // Само известните примитиви, със самите константи на рамката (проверка WrongConstant).
    private fun supported(primitive: Int): Boolean = when (primitive) {
        HapticScores.THUD -> vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_THUD)
        HapticScores.TICK -> vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_TICK)
        else -> false
    }

    private fun VibrationEffect.Composition.add(n: HapticScores.Note) {
        when (n.primitive) {
            HapticScores.THUD -> addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, n.scale, n.delayMs)
            else -> addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, n.scale, n.delayMs)
        }
    }

    override fun play(notes: List<HapticScores.Note>): Boolean = runCatching {
        val composition = VibrationEffect.startComposition()
        for (n in notes) composition.add(n)
        val effect = composition.compose()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) vibrator.vibrate(effect, VibrationAttributes.createForUsage(VibrationAttributes.USAGE_TOUCH))
        else vibrator.vibrate(effect)
        true
    }.getOrDefault(false)
}

/**
 * Свири подписите. [output] и [touchFeedbackOn] са отделени за тестовете; на устройство
 * идват от системата ([rememberAppHaptics]).
 */
class AppHaptics internal constructor(
    private val view: View,
    private val compose: HapticFeedback,
    private val sdk: Int = Build.VERSION.SDK_INT,
    private val output: () -> CompositionOutput? = { systemOutput(view.context, Build.VERSION.SDK_INT) },
    private val touchFeedbackOn: () -> Boolean = { systemTouchFeedbackOn(view) },
    private val post: (Long, () -> Unit) -> Unit = { delay, block -> Handler(Looper.getMainLooper()).postDelayed(block, delay) },
) {
    private fun compositionOutput(signature: HapticSignature): CompositionOutput? {
        if (sdk < Build.VERSION_CODES.R) return null
        val out = output() ?: return null
        return out.takeIf { it.supports(HapticScores.of(signature).map { n -> n.primitive }.distinct().toIntArray()) }
    }

    /** Дали [play] ще ползва съставен ефект (за тестовете). */
    internal fun usesComposition(signature: HapticSignature): Boolean = compositionOutput(signature) != null

    fun play(signature: HapticSignature) {
        val out = compositionOutput(signature)
        if (out != null) {
            // Настройката „Докосване“ е изключена — нищо; иначе съставеният ефект.
            if (!touchFeedbackOn()) return
            if (out.play(HapticScores.of(signature))) return
        }
        // Резерва: стандартните типове (прозорецът сам спазва системната настройка). Паузите са
        // една след друга, затова се събират.
        var at = 0L
        for (step in HapticScores.fallback(signature)) {
            at += step.delayMs
            if (at == 0L) compose.performHapticFeedback(step.type)
            else post(at) { compose.performHapticFeedback(step.type) }
        }
    }

    /** Запазване (отметка): включване — БОД, изключване — обичайното „изключено“. */
    fun save(on: Boolean) {
        if (on) play(HapticSignature.STITCH) else compose.performHapticFeedback(HapticFeedbackType.ToggleOff)
    }

    private companion object {
        fun systemOutput(context: Context, sdk: Int): CompositionOutput? {
            if (sdk < Build.VERSION_CODES.R || Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return null
            val v = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) context.getSystemService(VibratorManager::class.java)?.defaultVibrator
            else context.getSystemService(Vibrator::class.java)
            return v?.let { VibratorOutput(it) }
        }

        /** „Докосване“ в системните настройки и разрешението на самия прозорец. */
        fun systemTouchFeedbackOn(view: View): Boolean =
            view.isHapticFeedbackEnabled &&
                runCatching { Settings.System.getInt(view.context.contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED, 1) != 0 }
                    .getOrDefault(true)
    }
}

/** Подписите за текущия прозорец. */
@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    val compose = LocalHapticFeedback.current
    return remember(view, compose) { AppHaptics(view, compose) }
}
