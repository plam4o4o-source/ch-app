package org.chyavorec.app.ui.screens.my

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.components.rememberReducedMotion
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

/** ≈30 Hz — достатъчно за плавен отблясък. */
private const val SENSOR_PERIOD_US = 33_000
/** Най-много ~2 събития на порция: отблясъкът остава плавен, а системата може да ги групира. */
private const val SENSOR_MAX_LATENCY_US = 66_000
/** В покой: акселерометърът ≈10 Hz, на порции до 0.5 s — процесорът почти не се буди. */
private const val REST_PERIOD_US = 100_000
private const val REST_MAX_LATENCY_US = 500_000

/** Наклон на устройството (−1..1 по двете оси), нискочестотно филтриран; (0, 0) без сензор. */
@Stable
internal class Tilt {
    var x by mutableFloatStateOf(0f)
    var y by mutableFloatStateOf(0f)

    /** Фолиото и релефът за текущия наклон — пресмятат се веднъж на промяна, общо за всички слоеве. */
    val shine: CardFoil.Shine by derivedStateOf { CardFoil.shine(x, y) }

    /** Буди сензора (докосване на картата); задава го [rememberTilt], докато сензорът е включен. */
    var wake: () -> Unit = {}
}

/** Изглаждането, мъртвата зона и покоят на сензора — чисти функции (тестват се без сензор). */
internal object TiltLogic {
    /** Нискочестотният филтър: дял от разликата до новото показание на събитие. */
    const val SMOOTHING = 0.18f

    /** Промяна под това (нормализирано, ≈0.14°) не се записва — няма нов кадър за трептене на сензора. */
    const val DEAD_BAND = 0.003f

    /** „Истинско“ движение за покоя (≈1°): трептенето на сензора не държи бързия сензор буден. */
    const val MOVE = 0.02f

    /** След толкова време без движение бързият сензор спира. */
    const val REST_AFTER_MS = 15_000L

    /** Примерната карта (без вход): само след докосване и заспива по-рано. */
    const val PREVIEW_REST_AFTER_MS = 4_000L

    /** В покой: наклон над ≈2.5° спрямо положението при заспиването буди сензора. */
    val WAKE_COS: Float = cos(Math.toRadians(2.5)).toFloat()

    fun smooth(current: Float, target: Float): Float = current + (target - current) * SMOOTHING

    /** Дали изгладеният наклон ([x], [y]) се е отдалечил от показания ([shownX], [shownY]) достатъчно за кадър. */
    fun pastDeadBand(shownX: Float, shownY: Float, x: Float, y: Float): Boolean =
        abs(x - shownX) >= DEAD_BAND || abs(y - shownY) >= DEAD_BAND

    /**
     * Дали посоката на гравитацията (акселерометъра) се е сменила с повече от прага
     * спрямо [ref]. Безтегловност/невалидно показание (дължина под 2 m/s²) — не.
     */
    fun tilted(ref: FloatArray, x: Float, y: Float, z: Float): Boolean {
        val nr = sqrt(ref[0] * ref[0] + ref[1] * ref[1] + ref[2] * ref[2])
        val n = sqrt(x * x + y * y + z * z)
        if (!nr.isFinite() || !n.isFinite() || nr < 2f || n < 2f) return false
        return (ref[0] * x + ref[1] * y + ref[2] * z) / (nr * n) < WAKE_COS
    }
}

/** Таймерът на покоя: [resting] става true след [restAfterMs] без движение над [TiltLogic.MOVE]. */
internal class RestTimer(private val restAfterMs: Long = TiltLogic.REST_AFTER_MS) {
    private var anchorX = 0f
    private var anchorY = 0f
    private var since = 0L

    fun reset(now: Long, x: Float, y: Float) {
        anchorX = x
        anchorY = y
        since = now
    }

    fun resting(now: Long, x: Float, y: Float): Boolean {
        if (abs(x - anchorX) > TiltLogic.MOVE || abs(y - anchorY) > TiltLogic.MOVE) {
            reset(now, x, y)
            return false
        }
        return now - since >= restAfterMs
    }
}

/**
 * Сензорите на наклона. Докато картата се движи — векторът на завъртане (≈30 Hz, на
 * порции); наклонът се записва само над мъртвата зона. След [restAfterMs] покой той спира и
 * остава само рядка проверка с акселерометъра ([gravity]), която го буди при наклон. Будят го и
 * докосване на картата, и връщане към екрана. В покой: нула кадри; без [gravity] — и нула сензори.
 */
private class TiltSensors(
    private val manager: SensorManager,
    private val rotation: Sensor,
    private val gravity: Sensor?,
    private val tilt: Tilt,
    restAfterMs: Long,
) {
    private val matrix = FloatArray(9)
    private val orientation = FloatArray(3)
    private val rest = RestTimer(restAfterMs)
    private val ref = FloatArray(3)
    private var hasRef = false
    private var sx = tilt.x
    private var sy = tilt.y
    private var fast = false
    private var slow = false

    private val fastListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            runCatching {
                SensorManager.getRotationMatrixFromVector(matrix, event.values)
                SensorManager.getOrientation(matrix, orientation)
                // pitch (напред/назад) и roll (наляво/надясно) в радиани → ±1 при ≈ ±45°.
                val ty = (orientation[1] / 0.8f).coerceIn(-1f, 1f)
                val tx = (orientation[2] / 0.8f).coerceIn(-1f, 1f)
                // Невалидно показание (NaN) би останало в наклона завинаги — пропуска се.
                if (!tx.isFinite() || !ty.isFinite()) return@runCatching
                sx = TiltLogic.smooth(sx, tx)
                sy = TiltLogic.smooth(sy, ty)
                if (TiltLogic.pastDeadBand(tilt.x, tilt.y, sx, sy)) {
                    tilt.x = sx
                    tilt.y = sy
                }
                if (rest.resting(SystemClock.elapsedRealtime(), sx, sy)) sleep()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    private val slowListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val v = event.values
            if (v.size < 3) return
            if (!hasRef) {
                v.copyInto(ref, endIndex = 3)
                hasRef = true
            } else if (TiltLogic.tilted(ref, v[0], v[1], v[2])) {
                wake()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    /** Бързият сензор — веднага (или, ако вече е включен, таймерът на покоя отначало). */
    fun wake() {
        rest.reset(SystemClock.elapsedRealtime(), sx, sy)
        if (fast) return
        if (slow) {
            manager.unregisterListener(slowListener)
            slow = false
        }
        fast = runCatching { manager.registerListener(fastListener, rotation, SENSOR_PERIOD_US, SENSOR_MAX_LATENCY_US) }.getOrDefault(false)
    }

    /** Покой: бързият сензор спира, наклонът остава; акселерометърът (ако го има) пази за движение. */
    private fun sleep() {
        if (fast) {
            manager.unregisterListener(fastListener)
            fast = false
        }
        hasRef = false
        if (gravity != null && !slow) {
            slow = runCatching { manager.registerListener(slowListener, gravity, REST_PERIOD_US, REST_MAX_LATENCY_US) }.getOrDefault(false)
        }
    }

    fun stop() {
        manager.unregisterListener(fastListener)
        manager.unregisterListener(slowListener)
        fast = false
        slow = false
    }
}

/**
 * Наклонът за „живия“ отблясък на картата. Не прави нищо без сензор или при намалено
 * движение. Сензорът е включен само докато екранът е на преден план (ON_RESUME …
 * ON_PAUSE) — не и при заключен екран или приложение във фона, — а събитията се
 * доставят на порции, за да не се буди процесорът за всяко поотделно.
 *
 * [preview] — примерната карта: сензорът тръгва чак при докосване, заспива след
 * [TiltLogic.PREVIEW_REST_AFTER_MS] и в покой не остава нищо включено. [paused] — наклонът
 * застива както е (напр. докато се пише във формата за вход).
 */
@Composable
private fun rememberTilt(preview: Boolean = false, paused: Boolean = false): Tilt {
    val context = LocalContext.current
    val reduced = rememberReducedMotion()
    val fixed = LocalFixedCardTilt.current
    val tilt = remember(fixed) { Tilt().apply { fixed?.let { x = it.x; y = it.y } } }
    LifecycleResumeEffect(tilt, reduced, fixed, preview, paused) {
        if (reduced || fixed != null || paused) return@LifecycleResumeEffect onPauseOrDispose {}
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotation = manager?.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) ?: manager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (manager == null || rotation == null) return@LifecycleResumeEffect onPauseOrDispose {}
        val sensors = if (preview) {
            TiltSensors(manager, rotation, gravity = null, tilt, TiltLogic.PREVIEW_REST_AFTER_MS)
        } else {
            TiltSensors(manager, rotation, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), tilt, TiltLogic.REST_AFTER_MS).also { it.wake() }
        }
        tilt.wake = sensors::wake
        onPauseOrDispose {
            tilt.wake = {}
            sensors.stop()
        }
    }
    return tilt
}

/** Неподвижен наклон вместо сензора — за снимките на фолиото в тестовете. */
internal val LocalFixedCardTilt = staticCompositionLocalOf<Offset?> { null }

/**
 * Наклонът и „тъкането“ на шевицата на картата. На цял екран са общи за лицето и
 * гърба — обръщането не включва сензора наново и не започва шевицата отначало.
 */
@Stable
internal class CardMotion(val tilt: Tilt, val budget: WeaveBudget)

@Composable
internal fun rememberCardMotion(): CardMotion {
    val tilt = rememberTilt()
    return remember(tilt) { CardMotion(tilt, WeaveBudget()) }
}

/** „Тъкането“ на примерната карта — един цикъл на процес, колкото и пъти да се покаже картата. */
private object PreviewWeave {
    val budget = WeaveBudget(cycles = 1)
}

/**
 * Наклонът и шевицата на примерната карта („Моето“ без вход, входът, въведението): шевицата
 * тъче веднъж на процес; сензорът — само след докосване на картата и заспива след ~4 s.
 * [live] — сензорът както на истинската карта (въведението кани да се наклони телефонът);
 * [paused] — наклонът застива (докато се пише във формата за вход).
 */
@Composable
internal fun rememberSampleCardMotion(live: Boolean = false, paused: Boolean = false): CardMotion {
    val tilt = rememberTilt(preview = !live, paused = paused)
    return remember(tilt) { CardMotion(tilt, PreviewWeave.budget) }
}

/** Всяко докосване на картата буди сензора (без да пречи на щракването — само гледа събитията). */
internal fun Modifier.wakesTilt(tilt: Tilt): Modifier = pointerInput(tilt) {
    awaitPointerEventScope {
        while (true) {
            awaitPointerEvent(PointerEventPass.Initial)
            tilt.wake()
        }
    }
}
