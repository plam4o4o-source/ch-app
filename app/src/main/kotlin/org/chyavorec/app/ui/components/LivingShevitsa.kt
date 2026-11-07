package org.chyavorec.app.ui.components

import android.graphics.Bitmap
import android.graphics.RuntimeShader
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import kotlinx.coroutines.delay
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaPattern
import org.chyavorec.app.ui.theme.ShevitsaShaders
import org.chyavorec.core.Diagnostics
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Дали AGSL шейдърите са позволени (API 33+). В тестовете — false, за снимка на
 * неподвижния вариант, който виждат телефоните до Android 12L.
 */
val LocalAgslEnabled = staticCompositionLocalOf { true }

/** Цветовете на шевицата: канава ([fabric], често прозрачна) и конците. */
@Immutable
data class ShevitsaColors(
    val fabric: Color,
    val red: Color,
    val ink: Color,
    val gold: Color,
    val glow: Color = GoldGlow,
    /** 0 = само конците (фонът отдолу се вижда). */
    val fabricAlpha: Float = 0f,
    val threadAlpha: Float = 1f,
)

/** Топлата златна светлина на снопа. */
private val GoldGlow = Color(0xFFEED183)

object ShevitsaPalettes {
    /** Лентата на „Начало“: конците върху фона на екрана. */
    fun ribbon(dark: Boolean) = if (dark) {
        ShevitsaColors(fabric = Brand.Ink, red = Color(0xFFC0566A), ink = Brand.GoldLight, gold = Brand.Gold)
    } else {
        ShevitsaColors(fabric = Brand.Cream, red = Brand.Burgundy, ink = Brand.Ink, gold = Brand.Gold)
    }

    /** Читателската карта: приглушени конци (20 %) върху мастилено-бордо — текстът се чете. */
    val card = ShevitsaColors(fabric = Brand.Ink, red = Color(0xFFB04A5C), ink = Brand.GoldLight, gold = Brand.Gold, threadAlpha = 0.2f)

    /** Въведението с логото: бледа шевица по краищата на крема. */
    val intro = ShevitsaColors(fabric = Brand.Parchment, red = Brand.Burgundy, ink = Brand.Ink, gold = Brand.Gold, threadAlpha = 0.2f)
}

/**
 * Колко „тъкане“ остава: снопът светлина минава цели цикли (≈30 s, [MotionBudget]),
 * после шевицата застива — в края на цикъл, когато снопът е извън кадъра.
 * Общ обект (напр. [WeaveSession.home]) = веднъж на процес.
 */
class WeaveBudget(cycles: Int = (MotionBudget.DECOR_MS / (ShevitsaShaders.SWEEP_SECONDS * 1000).toLong()).toInt().coerceAtLeast(1)) {
    val totalSeconds = cycles * ShevitsaShaders.SWEEP_SECONDS
    var seconds = 0f
        private set
    val exhausted get() = seconds >= totalSeconds

    /** Добавя [dt] секунди (не над бюджета). */
    fun advance(dt: Float) {
        seconds = min(seconds + dt, totalSeconds)
    }

    /** Спира веднага в края на последния цикъл — снопът е извън кадъра, не застива по средата. */
    fun finish() {
        seconds = totalSeconds
    }
}

/** Бюджетите, които са веднъж на процес. */
object WeaveSession {
    val home = WeaveBudget()
}

/** ~30 кадъра/s — снопът е бавен, повече не се вижда. */
private const val WEAVE_FRAME_DELAY_MS = 30L

/**
 * Времето на тъкането (s): тече само докато шевицата е в композицията, екранът е
 * RESUMED, няма „намалено движение“ и режим за пестене на батерията, а бюджетът
 * не е изчерпан; [speed] ускорява (въведението). Чете се само при рисуване.
 */
@Composable
private fun rememberWeaveTime(budget: WeaveBudget, speed: Float, stopWhen: () -> Boolean): FloatState {
    val context = LocalContext.current
    val powerSave = remember { context.getSystemService(PowerManager::class.java)?.isPowerSaveMode == true }
    val still = rememberReducedMotion() || powerSave
    val lifecycle by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val resumed = lifecycle.isAtLeast(Lifecycle.State.RESUMED)
    val time = remember(budget) { mutableFloatStateOf(budget.seconds) }
    val stop by rememberUpdatedState(stopWhen)
    LaunchedEffect(budget, still, resumed) {
        if (still || !resumed) return@LaunchedEffect
        var last = withFrameNanos { it }
        while (!budget.exhausted) {
            if (stop()) {
                budget.finish()
                time.floatValue = budget.seconds
                break
            }
            delay(WEAVE_FRAME_DELAY_MS)
            val now = withFrameNanos { it }
            budget.advance(((now - last) / 1e9f).coerceIn(0f, 0.1f) * speed)
            last = now
            time.floatValue = budget.seconds
        }
    }
    return time
}

/**
 * „Жива шевица“ — шевица на кръстат бод, по която бавно минава златна светлина
 * (AGSL, Android 13+). До Android 12L, при грешка в шейдъра или с
 * [LocalAgslEnabled] = false — същата шарка, нарисувана веднъж в картинка (без
 * анимация). Декоративна: без семантика.
 *
 * @param cell размерът на един бод (гъстотата); при [ribbon] се смята от височината.
 * @param period бодове между два възела на ромбовата мрежа (четно).
 * @param ribbon лента (верига от ромбове с кант) вместо поле.
 * @param intensity силата на снопа и проблясването (0..1).
 * @param clearCenter > 0: празен кръг в средата (дял от по-малката страна) — за логото.
 * @param alpha общата плътност, чете се при рисуване (за появяване).
 * @param still дали без шейдър да рисува неподвижната шарка (иначе — нищо).
 */
@Composable
fun LivingShevitsa(
    colors: ShevitsaColors,
    modifier: Modifier = Modifier,
    cell: Dp = 2.6.dp,
    period: Int = 18,
    ribbon: Boolean = false,
    intensity: Float = 1f,
    clearCenter: Float = 0f,
    speed: Float = 1f,
    budget: WeaveBudget = remember { WeaveBudget() },
    stopWhen: () -> Boolean = { false },
    alpha: () -> Float = { 1f },
    still: Boolean = true,
) {
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val time = rememberWeaveTime(budget, speed, stopWhen)
    val shader = remember(agsl) { if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) WeaveShader.createOrNull() else null }
    val currentAlpha by rememberUpdatedState(alpha)
    // Пресмята се веднъж за размера и параметрите; времето и плътността се четат при рисуване.
    val draw = remember(shader, time, cell, period, ribbon, intensity, clearCenter, colors, still) {
        Modifier.drawWithCache {
            val cellPx = if (ribbon) size.height / ShevitsaPattern.ribbonRows(period) else cell.toPx()
            val spec = WeaveSpec(size.width, size.height, cellPx.coerceAtLeast(1f), period.toFloat(), ribbon, intensity, clearCenter, colors)
            val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.configure(spec)
            // Резервата се рисува чак когато е нужна (без шейдър или при грешка по-късно).
            val image by lazy { ShevitsaStill.render(spec) }
            onDrawBehind {
                if (size.width < 1f || size.height < 1f) return@onDrawBehind
                val a = currentAlpha().coerceIn(0f, 1f)
                if (a <= 0f) return@onDrawBehind
                if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.setTime(time.floatValue)) {
                    drawRect(shader.brush, alpha = a)
                } else if (still) {
                    drawImage(image, dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()), alpha = a, filterQuality = FilterQuality.Low)
                }
            }
        }
    }
    Spacer(modifier.clearAndSetSemantics { }.then(draw))
}

/** Всичко, от което зависи кадърът (без времето). */
internal data class WeaveSpec(
    val width: Float,
    val height: Float,
    val cell: Float,
    val period: Float,
    val ribbon: Boolean,
    val intensity: Float,
    val clear: Float,
    val colors: ShevitsaColors,
) {
    val cols get() = floor(width / cell)
    val rows get() = floor(height / cell)

    /** Клетката в средата е (0, 0) — шарката е симетрична спрямо изгледа. */
    val originX get() = floor(cols * 0.5f)
    val originY get() = floor(rows * 0.5f)
}

/** Грешката на шейдъра се записва веднъж на процес (Диагностика). */
private object ShaderReport {
    @Volatile private var reported = false

    fun record(error: Throwable) {
        if (reported) return
        reported = true
        Diagnostics.record("shevitsa-shader", error)
    }
}

/** RuntimeShader-ът на шевицата (API 33+); всяка грешка → неподвижният вариант. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class WeaveShader private constructor(private val shader: RuntimeShader) {
    val brush = ShaderBrush(shader)
    private var broken = false

    fun configure(s: WeaveSpec): Boolean {
        if (broken) return false
        return runCatching {
            shader.setFloatUniform("uResolution", s.width, s.height)
            shader.setFloatUniform("uOrigin", s.originX, s.originY)
            shader.setFloatUniform("uTime", 0f)
            shader.setFloatUniform("uCell", s.cell)
            shader.setFloatUniform("uPeriod", s.period)
            shader.setFloatUniform("uRibbon", if (s.ribbon) 1f else 0f)
            shader.setFloatUniform("uIntensity", s.intensity)
            shader.setFloatUniform("uFabricAlpha", s.colors.fabricAlpha)
            shader.setFloatUniform("uThreadAlpha", s.colors.threadAlpha)
            shader.setFloatUniform("uClear", s.clear)
            shader.setColorUniform("uFabric", s.colors.fabric.toArgb())
            shader.setColorUniform("uRed", s.colors.red.toArgb())
            shader.setColorUniform("uInk", s.colors.ink.toArgb())
            shader.setColorUniform("uGold", s.colors.gold.toArgb())
            shader.setColorUniform("uGlow", s.colors.glow.toArgb())
        }.onFailure { broken = true; ShaderReport.record(it) }.isSuccess
    }

    fun setTime(t: Float): Boolean {
        if (broken) return false
        return runCatching { shader.setFloatUniform("uTime", t) }.onFailure { broken = true; ShaderReport.record(it) }.isSuccess
    }

    companion object {
        fun createOrNull(): WeaveShader? =
            runCatching { WeaveShader(RuntimeShader(ShevitsaShaders.WEAVE)) }.onFailure { ShaderReport.record(it) }.getOrNull()
    }
}

/**
 * Неподвижната шевица — същата шарка и бод като шейдъра (без снопа), нарисувана
 * веднъж в картинка. Голям изглед се рисува в по-малка картинка (до [MAX_PIXELS]) и
 * се разтяга — пресмятането остава няколко милисекунди и на стар телефон.
 */
internal object ShevitsaStill {
    private const val MAX_PIXELS = 240_000f

    fun render(s: WeaveSpec): ImageBitmap {
        val scale = min(1f, sqrt(MAX_PIXELS / max(1f, s.width * s.height)))
        val w = max(1, (s.width * scale).roundToInt())
        val h = max(1, (s.height * scale).roundToInt())
        val cell = s.cell * scale
        val c = s.colors
        val threads = arrayOf(c.red, c.ink, c.gold)
        val fabric = c.fabric
        val pixels = IntArray(w * h)
        val cols = (w / cell).toInt() + 2
        // Видът бод по клетки — веднъж на клетка, не на пиксел.
        var rowCells = IntArray(cols)
        var cachedRow = Int.MIN_VALUE
        val aa = 1f / cell
        val tw = 0.21f
        for (y in 0 until h) {
            val gy = (y + 0.5f) / cell
            val cyRaw = floor(gy)
            val qy = gy - cyRaw
            if (cyRaw.toInt() != cachedRow) {
                cachedRow = cyRaw.toInt()
                rowCells = IntArray(cols) { i -> ShevitsaPattern.kind(i - s.originX, cyRaw - s.originY, s.period, s.ribbon) }
            }
            for (x in 0 until w) {
                val gx = (x + 0.5f) / cell
                val cxi = floor(gx).toInt().coerceIn(0, cols - 1)
                val qx = gx - floor(gx)
                val k = rowCells[cxi]
                // Кръстатият бод: долна нишка „\“ (A) и горна „/“ (B), както в шейдъра.
                var cov = 0f
                var lit = 1f
                if (k != ShevitsaPattern.EMPTY) {
                    val dA = kotlin.math.abs(qx - qy) * 0.7071f
                    val tA = (qx + qy) * 0.5f
                    val dB = kotlin.math.abs(qx + qy - 1f) * 0.7071f
                    val tB = (qx - qy) * 0.5f + 0.5f
                    val endA = smooth(0.03f, 0.1f, tA) * (1f - smooth(0.9f, 0.97f, tA))
                    val endB = smooth(0.03f, 0.1f, tB) * (1f - smooth(0.9f, 0.97f, tB))
                    val covA = (1f - smooth(tw - aa, tw + aa, dA)) * endA
                    val covB = (1f - smooth(tw - aa, tw + aa, dB)) * endB
                    val nA = dA / tw
                    val nB = dB / tw
                    val shA = sqrt((1f - nA * nA).coerceIn(0f, 1f))
                    val shB = sqrt((1f - nB * nB).coerceIn(0f, 1f))
                    val twA = 0.9f + 0.1f * sin((tA * 7f + dA * 9f) * 6.2832f)
                    val twB = 0.9f + 0.1f * sin((tB * 7f + dB * 9f) * 6.2832f)
                    val shd = 1f - 0.35f * (1f - smooth(tw, tw + 0.12f, dB)) * endB
                    val litA = (0.5f + 0.62f * shA) * twA * shd
                    val litB = (0.5f + 0.62f * shB) * twB
                    lit = litB * covB + litA * (1f - covB)
                    cov = (covA + covB - covA * covB).coerceIn(0f, 1f)
                }
                var ta = c.threadAlpha
                if (s.clear > 0f) {
                    val dx = (x + 0.5f) / scale - s.width * 0.5f
                    val dy = (y + 0.5f) / scale - s.height * 0.5f
                    ta *= smooth(s.clear * 0.55f, s.clear, sqrt(dx * dx + dy * dy) / min(s.width, s.height))
                }
                val ca = cov * ta
                val fa = c.fabricAlpha * (1f - ca)
                val mx = min(qx, 1f - qx)
                val my = min(qy, 1f - qy)
                val hole = 1f - smooth(0.05f, 0.2f, sqrt(mx * mx + my * my))
                val fk = 0.97f - 0.1f * hole
                val t = if (k != ShevitsaPattern.EMPTY) threads[k - 1] else fabric
                val a = ca + fa
                pixels[y * w + x] = if (a <= 0.001f) {
                    0
                } else {
                    // Без premultiply: ARGB на Bitmap.createBitmap(int[]) е „прав“.
                    (((a * 255f).roundToInt().coerceIn(0, 255)) shl 24) or
                        (channel(t.red, fabric.red, lit, ca, fa, fk) shl 16) or
                        (channel(t.green, fabric.green, lit, ca, fa, fk) shl 8) or
                        channel(t.blue, fabric.blue, lit, ca, fa, fk)
                }
            }
        }
        return Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888).asImageBitmap()
    }

    /** Един канал: конецът (осветен) върху канавата, без premultiply. */
    private fun channel(thread: Float, fab: Float, lit: Float, ca: Float, fa: Float, fk: Float): Int =
        (((thread * lit).coerceIn(0f, 1f) * ca + (fab * fk).coerceIn(0f, 1f) * fa) / (ca + fa) * 255f).roundToInt().coerceIn(0, 255)

    private fun smooth(e0: Float, e1: Float, x: Float): Float {
        val t = ((x - e0) / (e1 - e0)).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }
}
