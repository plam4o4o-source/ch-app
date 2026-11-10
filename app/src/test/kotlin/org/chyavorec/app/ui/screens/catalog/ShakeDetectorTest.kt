package org.chyavorec.app.ui.screens.catalog

import android.os.Build
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Разклащането: три удара над прага за секунда и после покой — да; два удара, ходене, тичане
 * (ударите не спират) — не; пауза след разклащане. Плюс настройката и TalkBack.
 */
class ShakeDetectorTest {
    private val g = 9.80665f

    /** Телефонът лежи спокойно в ръката от [from] до [to] (отчитане на [step] ms); кога е разпознато разклащане. */
    private fun ShakeDetector.still(from: Long, to: Long, step: Long = 60): List<Long> =
        (from..to step step).filter { onSample(0f, 0f, g, it) }

    private fun ShakeDetector.peak(t: Long, sign: Float = 1f) = onSample(sign * 2.8f * g, 0f, 0f, t)

    @Test fun threePeaksThenStillIsAShake() {
        val d = ShakeDetector()
        assertEquals(emptyList(), d.still(0, 300))
        assertFalse(d.peak(400))
        // Същият удар (следващото отчитане) не се брои втори път.
        assertFalse(d.peak(460))
        assertFalse(d.peak(650, -1f))
        assertFalse(d.peak(900))
        // Ръката още се движи — не.
        assertFalse(d.onSample(0f, 0f, 1.8f * g, 960))
        // Застава: след ~0,25 s покой — разклащане, веднъж.
        val shakes = d.still(1_020, 1_800)
        assertEquals(1, shakes.size, "$shakes")
        assertTrue(shakes.single() in 1_270L..1_400L, "$shakes")
    }

    @Test fun twoPeaksAreNotEnough() {
        val d = ShakeDetector()
        assertFalse(d.peak(0))
        assertFalse(d.peak(250, -1f))
        assertEquals(emptyList(), d.still(300, 2_000))
        // Три удара, но разпилени в повече от секунда — също не.
        assertFalse(d.peak(3_000))
        assertFalse(d.peak(3_600))
        assertFalse(d.peak(4_200))
        assertEquals(emptyList(), d.still(4_260, 5_000))
    }

    @Test fun pauseAfterAShake() {
        val d = ShakeDetector()
        listOf(0L, 200L, 400L).forEach { d.peak(it) }
        val first = d.still(460, 1_000).single()
        // Веднага пак — в паузата нищо не се брои.
        listOf(first + 100, first + 300, first + 500).forEach { assertFalse(d.peak(it)) }
        assertEquals(emptyList(), d.still(first + 560, first + 1_400))
        // След паузата — пак.
        listOf(3_000L, 3_200L, 3_400L).forEach { d.peak(it) }
        assertEquals(1, d.still(3_460, 4_000).size)
    }

    @Test fun walkingIsNot() {
        val d = ShakeDetector()
        // Ходене с телефон в ръка: 1 ± 0,5 g, две крачки в секунда.
        for (t in 0L until 6_000L step 60) {
            val a = 1f + 0.5f * sin(2 * PI.toFloat() * t / 500f)
            assertFalse(d.onSample(0f, a * g, 0f, t), "t=$t")
        }
    }

    @Test fun runningIsNot() {
        val d = ShakeDetector()
        // Тичане: удари до ~2,8 g почти три пъти в секунда, но телефонът не застава.
        for (t in 0L until 8_000L step 60) {
            val a = 1f + 1.8f * sin(2 * PI.toFloat() * t / 350f)
            assertFalse(d.onSample(0f, a * g, 0f, t), "t=$t")
        }
    }

    @Test fun shakesThatDoNotSettleAreDropped() {
        val d = ShakeDetector()
        listOf(0L, 200L, 400L).forEach { d.peak(it) }
        // Продължава да се движи (без нови удари) повече от 1,2 s — ударите се забравят.
        for (t in 460L until 2_000L step 60) assertFalse(d.onSample(0f, 0f, 1.7f * g, t))
        assertEquals(emptyList(), d.still(2_000, 3_000))
    }

    @Test fun settingAndTalkBack() {
        // Неизбрано — включено; с TalkBack — никога, дори включено.
        assertTrue(shakeToSurpriseEnabled(setting = null, screenReader = false))
        assertTrue(shakeToSurpriseEnabled(setting = true, screenReader = false))
        assertFalse(shakeToSurpriseEnabled(setting = false, screenReader = false))
        assertFalse(shakeToSurpriseEnabled(setting = null, screenReader = true))
        assertFalse(shakeToSurpriseEnabled(setting = true, screenReader = true))
        // Подразбиращата се стойност при първото ползване: изкл., ако TalkBack е включен.
        assertTrue(shakeToSurpriseDefault(screenReader = false))
        assertFalse(shakeToSurpriseDefault(screenReader = true))
    }
}

/** Кой вид „Изненадай ме“ на кой Android и с кои настройки на достъпността. */
class SurpriseMotionTest {
    @Test fun androidTwelveAndNewerGetTheFullDeck() {
        for (sdk in Build.VERSION_CODES.S..37) assertEquals(SurpriseMotion.FULL, surpriseMotion(sdk, reducedMotion = false, screenReader = false))
    }

    /** Android 8–11: разбъркване без 3D и без сенки на слоя (паметта на API 28 — проверена на емулатор). */
    @Test fun olderAndroidGetsTheLiteDeck() {
        for (sdk in MIN_SHUFFLE_SDK until Build.VERSION_CODES.S) {
            assertEquals(SurpriseMotion.LITE, surpriseMotion(sdk, reducedMotion = false, screenReader = false), "API $sdk")
        }
        assertEquals(SurpriseMotion.STILL, surpriseMotion(MIN_SHUFFLE_SDK - 1, reducedMotion = false, screenReader = false))
    }

    @Test fun reducedMotionOrScreenReaderIsAlwaysStill() {
        for (sdk in Build.VERSION_CODES.O..37) {
            assertEquals(SurpriseMotion.STILL, surpriseMotion(sdk, reducedMotion = true, screenReader = false))
            assertEquals(SurpriseMotion.STILL, surpriseMotion(sdk, reducedMotion = false, screenReader = true))
        }
    }
}
