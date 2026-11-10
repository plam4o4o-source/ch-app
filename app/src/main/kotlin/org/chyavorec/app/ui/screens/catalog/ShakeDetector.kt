package org.chyavorec.app.ui.screens.catalog

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Разклащане на телефона: поне [peaks] удара над [thresholdG] g за [windowMs] (напред-назад),
 * а после телефонът застава спокойно (около 1 g) за [stillMs] — до [settleMs] след последния
 * удар. При ходене и тичане ударите са редовни, но телефонът не застава — не е разклащане.
 * След разклащане — пауза [cooldownMs]. Чиста логика — сензорът е в [ShakeEffect].
 */
class ShakeDetector(
    private val thresholdG: Float = 2.5f,
    private val windowMs: Long = 1_000,
    private val peaks: Int = 3,
    private val stillMs: Long = 250,
    private val stillToleranceG: Float = 0.3f,
    private val settleMs: Long = 1_200,
    private val cooldownMs: Long = 1_500,
) {
    /** Времената на последните [peaks] удара (кръгово, без заделяне на памет при всяко отчитане). */
    private val peakTimes = LongArray(peaks) { NEVER }
    private var next = 0
    private var lastPeakAt = NEVER
    private var lastShakeAt = NEVER
    private var stillSince = NONE

    /** Ускорението (m/s², с гравитацията) в момента [timeMs]; `true` — разклащане. */
    fun onSample(x: Float, y: Float, z: Float, timeMs: Long): Boolean {
        if (timeMs - lastShakeAt < cooldownMs) return false
        val g = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
        if (g >= thresholdG) {
            stillSince = NONE
            // Един и същ удар (няколко отчитания подред) се брои веднъж.
            if (timeMs - lastPeakAt >= MIN_PEAK_GAP_MS) {
                lastPeakAt = timeMs
                peakTimes[next] = timeMs
                next = (next + 1) % peakTimes.size
            }
            return false
        }
        // Достатъчно удари в прозореца, завършващ с последния?
        val oldest = peakTimes[next]
        if (oldest == NEVER || lastPeakAt - oldest > windowMs) return false
        if (timeMs - lastPeakAt > settleMs) {
            // Движението не спира (ходене, тичане) — ударите не се броят.
            reset()
            return false
        }
        if (abs(g - 1f) > stillToleranceG) {
            stillSince = NONE
            return false
        }
        if (stillSince == NONE) stillSince = timeMs
        if (timeMs - stillSince < stillMs) return false
        reset()
        lastShakeAt = timeMs
        return true
    }

    private fun reset() {
        peakTimes.fill(NEVER)
        stillSince = NONE
    }

    private companion object {
        const val MIN_PEAK_GAP_MS = 120L
        const val NONE = Long.MIN_VALUE
        const val NEVER = Long.MIN_VALUE / 2
    }
}

/**
 * Дали разклащането пуска „Изненадай ме“: настройката ([setting]; `null` — още не е избрана:
 * включена, освен ако е включен екранен четец) и никога докато TalkBack е активен
 * ([screenReader]) — движенията с телефона не бива да отварят неочаквано сцена под фокуса.
 */
fun shakeToSurpriseEnabled(setting: Boolean?, screenReader: Boolean): Boolean =
    !screenReader && (setting ?: true)

/** Подразбиращата се стойност на настройката при първото ползване. */
fun shakeToSurpriseDefault(screenReader: Boolean): Boolean = !screenReader

/**
 * Акселерометърът — само докато екранът е отпред (RESUMED) и [enabled]; при пауза, смяна на
 * екрана или изход се спира веднага. Без сензор (емулатор, таблет) — нищо. Отчитанията идват
 * на пакети (до 0,2 s): процесорът се буди по-рядко, а времената им остават точни.
 */
@Composable
fun ShakeEffect(enabled: Boolean, onShake: () -> Unit) {
    if (!enabled) return
    val context = LocalContext.current
    val current by rememberUpdatedState(onShake)
    LifecycleResumeEffect(context) {
        val sm = context.getSystemService(SensorManager::class.java)
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val detector = ShakeDetector()
        val listener = object : SensorEventListener {
            override fun onSensorChanged(e: SensorEvent) {
                if (detector.onSample(e.values[0], e.values[1], e.values[2], e.timestamp / 1_000_000L)) current()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (sm != null && sensor != null) sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI, MAX_REPORT_LATENCY_US)
        onPauseOrDispose { sm?.unregisterListener(listener) }
    }
}

/** Пакетите от акселерометъра: до 0,2 s закъснение (вж. [ShakeEffect]). */
private const val MAX_REPORT_LATENCY_US = 200_000
