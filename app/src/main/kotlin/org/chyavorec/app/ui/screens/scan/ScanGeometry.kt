package org.chyavorec.app.ui.screens.scan

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/** Правоъгълник в пиксели на буфера на камерата (преди завъртане). */
data class CropRect(val left: Int, val top: Int, val width: Int, val height: Int)

/** Прозорецът на визьора в координатите на изгледа (px). */
data class ScanWindow(val left: Float, val top: Float, val width: Float, val height: Float)

/**
 * Прозорецът на визьора за изглед [viewWidth]×[viewHeight]: 78 % от ширината, височина
 * 62 % от неговата ширина (най-много половината изглед), центриран и леко над средата.
 * Същият прозорец рисува ViewfinderOverlay и в него анализаторът търси кода.
 */
fun scanWindow(viewWidth: Float, viewHeight: Float): ScanWindow {
    val w = viewWidth * 0.78f
    val h = (w * 0.62f).coerceAtMost(viewHeight * 0.5f)
    return ScanWindow((viewWidth - w) / 2f, (viewHeight - h) / 2f - viewHeight * 0.06f, w, h)
}

/**
 * Частта от буфера на камерата ([frameWidth]×[frameHeight], „легнал“ както идва от
 * сензора), която се вижда в [window] на изглед [viewWidth]×[viewHeight].
 *
 * Прегледът (PreviewView, FILL_CENTER) показва изправения кадър (завъртян на
 * [rotationDegrees] по часовниковата стрелка), мащабиран да запълни изгледа и
 * центриран — излишното се отрязва. Прегледът и анализът са с едно и също
 * съотношение на страните, така че показват едно и също зрително поле.
 *
 * Прозорецът се разширява с [margin] от размера си от всяка страна (кодът може леко
 * да излиза от него, а съпоставянето на двата потока не е до пиксел).
 *
 * @return областта в буфера или null, ако размерите са невалидни (тогава — целият кадър).
 */
fun cropForWindow(
    frameWidth: Int, frameHeight: Int, rotationDegrees: Int,
    viewWidth: Int, viewHeight: Int,
    window: ScanWindow = scanWindow(viewWidth.toFloat(), viewHeight.toFloat()),
    margin: Float = 0.1f,
): CropRect? {
    if (frameWidth <= 0 || frameHeight <= 0 || viewWidth <= 0 || viewHeight <= 0) return null
    val rotation = ((rotationDegrees % 360) + 360) % 360
    if (rotation % 90 != 0) return null
    val sideways = rotation == 90 || rotation == 270
    // Изправеният кадър (както го вижда потребителят).
    val uw = if (sideways) frameHeight else frameWidth
    val uh = if (sideways) frameWidth else frameHeight
    val scale = max(viewWidth.toFloat() / uw, viewHeight.toFloat() / uh)
    val offX = (viewWidth - uw * scale) / 2f
    val offY = (viewHeight - uh * scale) / 2f
    val mx = window.width * margin
    val my = window.height * margin
    // Прозорецът в координатите на изправения кадър, ограничен до кадъра.
    val ul = ((window.left - mx - offX) / scale).coerceIn(0f, uw.toFloat())
    val ut = ((window.top - my - offY) / scale).coerceIn(0f, uh.toFloat())
    val ur = ((window.left + window.width + mx - offX) / scale).coerceIn(0f, uw.toFloat())
    val ub = ((window.top + window.height + my - offY) / scale).coerceIn(0f, uh.toFloat())
    // Обратно към буфера: изправеният кадър е буферът, завъртян на rotation° по часовниковата стрелка.
    val (l, t, r, b) = when (rotation) {
        90 -> listOf(ut, frameHeight - ur, ub, frameHeight - ul)
        180 -> listOf(frameWidth - ur, frameHeight - ub, frameWidth - ul, frameHeight - ut)
        270 -> listOf(frameWidth - ub, ul, frameWidth - ut, ur)
        else -> listOf(ul, ut, ur, ub)
    }
    val left = floor(l).toInt().coerceIn(0, frameWidth)
    val top = floor(t).toInt().coerceIn(0, frameHeight)
    val right = ceil(r).toInt().coerceIn(0, frameWidth)
    val bottom = ceil(b).toInt().coerceIn(0, frameHeight)
    // Твърде малка област (изгледът още не е разположен, странни размери) — целият кадър.
    if (right - left < MIN_CROP || bottom - top < MIN_CROP) return null
    return CropRect(left, top, right - left, bottom - top)
}

private const val MIN_CROP = 32

/**
 * Ограничава колко често се разчита: следващ опит — не по-рано от [intervalMs] след
 * началото на предишния и не по-рано от [minGapMs] след края му (така и бавен
 * телефон не държи ядрото заето постоянно). Кадрите между опитите се пропускат.
 * Не е безопасен за няколко нишки (анализаторът е на една).
 */
class DecodeThrottle(private val intervalMs: Long, private val minGapMs: Long) {
    private var nextAt = Long.MIN_VALUE
    private var startedAt = 0L

    /** Може ли да се разчита кадър в момента [nowMs]; ако да — опитът започва. */
    fun tryStart(nowMs: Long): Boolean {
        if (nowMs < nextAt) return false
        startedAt = nowMs
        nextAt = Long.MAX_VALUE // до finish()
        return true
    }

    /** Опитът, започнат с [tryStart], е приключил в [nowMs]. */
    fun finish(nowMs: Long) {
        nextAt = max(startedAt + intervalMs, nowMs + minGapMs)
    }
}
