package org.chyavorec.app.ui.components

import androidx.activity.compose.LocalFullyDrawnReporterOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.ui.screens.my.CardFoilLayer
import org.chyavorec.app.ui.screens.my.Tilt
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaPattern
import org.chyavorec.app.ui.theme.ShevitsaShaders
import kotlin.math.abs
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

    /**
     * Снопът е извън кадъра (начало или край на цикъл): шейдърът рисува същото като неподвижната
     * плочка — при „намалено движение“ / пестене на батерията не му трябва шейдър.
     */
    val atRest get() = seconds <= 0f || exhausted

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
 * Режимът за пестене на батерията от корена на интерфейса ([ProvidePowerSaveMode]) — един
 * приемник за всички шевици, а не по един на екземпляр. Без доставчик (тестове, отделни
 * екрани) [rememberPowerSaveMode] си слуша сам.
 */
private val LocalPowerSaveMode = compositionLocalOf<Boolean?> { null }

/** Включва общия наблюдател на режима за пестене на батерията за съдържанието. */
@Composable
fun ProvidePowerSaveMode(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalPowerSaveMode provides rememberPowerSaveReceiver(), content = content)
}

/**
 * Режимът за пестене на батерията — следи се (ACTION_POWER_SAVE_MODE_CHANGED), не се
 * чете веднъж: включен по средата на тъкането, го спира; изключен — го пуска пак.
 */
@Composable
internal fun rememberPowerSaveMode(): Boolean = LocalPowerSaveMode.current ?: rememberPowerSaveReceiver()

@Composable
private fun rememberPowerSaveReceiver(): Boolean {
    val context = LocalContext.current
    val power = remember(context) { context.getSystemService(PowerManager::class.java) }
    var on by remember(power) { mutableStateOf(power?.isPowerSaveMode == true) }
    DisposableEffect(context, power) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                on = power?.isPowerSaveMode == true
            }
        }
        val registered = runCatching {
            ContextCompat.registerReceiver(
                context, receiver, IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED), ContextCompat.RECEIVER_NOT_EXPORTED,
            )
        }.isSuccess
        // Промяна между първото четене и регистрацията не се губи.
        on = power?.isPowerSaveMode == true
        onDispose { if (registered) runCatching { context.unregisterReceiver(receiver) } }
    }
    return on
}

/** Времето на тъкането (s) и дали тече в момента — докато тече, слоят на шевицата не се пази готов. */
@Stable
internal class WeaveClock(start: Float) {
    val time = mutableFloatStateOf(start)
    var running by mutableStateOf(false)
}

/**
 * Времето на тъкането: тече само докато шевицата е в композицията, екранът е
 * RESUMED, няма „намалено движение“ и режим за пестене на батерията, а бюджетът
 * не е изчерпан; [speed] ускорява (въведението). Чете се само при рисуване.
 */
@Composable
private fun rememberWeaveClock(budget: WeaveBudget, speed: Float, still: Boolean, stopWhen: () -> Boolean): WeaveClock {
    val lifecycle by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val resumed = lifecycle.isAtLeast(Lifecycle.State.RESUMED)
    val clock = remember(budget) { WeaveClock(budget.seconds) }
    val stop by rememberUpdatedState(stopWhen)
    LaunchedEffect(budget, still, resumed) {
        if (still || !resumed || budget.exhausted) return@LaunchedEffect
        clock.running = true
        try {
            var last = withFrameNanos { it }
            // На бавен телефон (кадри над 0.5 s) бюджетът не бива да се разтяга: край и по часовник.
            val wallLimit = last + (2 * (budget.totalSeconds - budget.seconds) / speed * 1e9f).toLong()
            while (!budget.exhausted) {
                if (stop() || last > wallLimit) {
                    budget.finish()
                    clock.time.floatValue = budget.seconds
                    break
                }
                delay(WEAVE_FRAME_DELAY_MS)
                val now = withFrameNanos { it }
                budget.advance(((now - last) / 1e9f).coerceIn(0f, 0.5f) * speed)
                last = now
                clock.time.floatValue = budget.seconds
            }
        } finally {
            clock.running = false
        }
    }
    return clock
}

/**
 * „Жива шевица“ — шевица на кръстат бод, по която бавно минава златна светлина
 * (AGSL, Android 13+). До Android 12L, при грешка в шейдъра или с
 * [LocalAgslEnabled] = false — същата шарка без анимация: една плочка (период на
 * шарката), нарисувана веднъж за процеса и повторена ([ShevitsaStill]). Декоративна: без семантика.
 *
 * Шевицата е в свой слой: тиктакането ѝ не прерисува съседите, а когато не тече
 * (бюджетът е изчерпан, намалено движение…), слоят се пази готов — рисуване над нея
 * (фолиото на картата) не пуска шейдъра наново.
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
    val reduced = rememberReducedMotion()
    val powerSave = rememberPowerSaveMode()
    val quiet = reduced || powerSave
    val clock = rememberWeaveClock(budget, speed, quiet, stopWhen)
    // Първият кадър — с готовата плочка, живата шевица — от следващия: видеокартата превежда
    // шейдъра при първото му рисуване и това не бива да е в първия кадър на екрана. Щом шейдърът
    // вече е рисуван в този процес (връщане към „Начало“ по средата на снопа), е жива веднага —
    // иначе един кадър би бил без снопа.
    val primed = agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ShaderDraws.hasDrawn(ShaderTags.WEAVE)
    var live by remember { mutableStateOf(!still || primed) }
    LaunchedEffect(Unit) {
        if (!live) {
            withFrameNanos { }
            live = true
        }
    }
    // Шейдър само когато може да се движи: без движение и със сноп извън кадъра (начало/край на цикъла)
    // той би нарисувал същото като плочката — нито превод, нито слой за него.
    val wantsShader = live && agsl && !(still && quiet && budget.atRest)
    val shader = remember(wantsShader) { if (wantsShader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) WeaveShader.createOrNull() else null }
    // Шейдърът се връща в резерва, когато шевицата изчезне — не се превежда наново при следващото появяване.
    DisposableEffect(shader) {
        onDispose { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) shader?.release() }
    }
    val currentAlpha by rememberUpdatedState(alpha)
    // Пресмята се веднъж за размера и параметрите; времето и плътността се четат при рисуване.
    val draw = remember(shader, clock, cell, period, ribbon, intensity, clearCenter, colors, still) {
        Modifier.drawWithCache {
            val cellPx = if (ribbon) size.height / ShevitsaPattern.ribbonRows(period) else cell.toPx()
            val spec = WeaveSpec(size.width, size.height, cellPx.coerceAtLeast(1f), period.toFloat(), ribbon, intensity, clearCenter, colors)
            val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.configure(spec)
            // Резервата се взима чак когато е нужна (без шейдър или при грешка по-късно); плочката е от общия
            // кеш — смяна на размера (прехода на картата) не я рисува наново.
            val fallback = if (still) lazy(LazyThreadSafetyMode.NONE) { ShevitsaStill.paint(spec) } else null
            var noted = false
            onDrawBehind {
                if (size.width < 1f || size.height < 1f) return@onDrawBehind
                val a = currentAlpha().coerceIn(0f, 1f)
                if (a <= 0f) return@onDrawBehind
                if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.setTime(clock.time.floatValue)) {
                    drawRect(shader.brush, alpha = a)
                    if (!noted) {
                        noted = true
                        ShaderDraws.note(ShaderTags.WEAVE)
                    }
                } else if (fallback != null) {
                    drawStill(fallback.value, a)
                }
            }
        }
    }
    val layer = Modifier.graphicsLayer {
        compositingStrategy = if (shader != null && !clock.running) CompositingStrategy.Offscreen else CompositingStrategy.Auto
    }
    Spacer(modifier.clearAndSetSemantics { }.then(layer).then(draw))
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

/** RuntimeShader-ът на шевицата (API 33+); всяка грешка → неподвижният вариант. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class WeaveShader private constructor(private val guarded: GuardedShader) {
    val brush get() = guarded.brush

    fun configure(s: WeaveSpec): Boolean = guarded.guard {
        setFloatUniform("uResolution", s.width, s.height)
        setFloatUniform("uOrigin", s.originX, s.originY)
        setFloatUniform("uTime", 0f)
        setFloatUniform("uCell", s.cell)
        setFloatUniform("uPeriod", s.period)
        setFloatUniform("uRibbon", if (s.ribbon) 1f else 0f)
        setFloatUniform("uIntensity", s.intensity)
        setFloatUniform("uFabricAlpha", s.colors.fabricAlpha)
        setFloatUniform("uThreadAlpha", s.colors.threadAlpha)
        setFloatUniform("uClear", s.clear)
        setColorUniform("uFabric", s.colors.fabric.toArgb())
        setColorUniform("uRed", s.colors.red.toArgb())
        setColorUniform("uInk", s.colors.ink.toArgb())
        setColorUniform("uGold", s.colors.gold.toArgb())
        setColorUniform("uGlow", s.colors.glow.toArgb())
    }

    fun setTime(t: Float): Boolean = guarded.guard { setFloatUniform("uTime", t) }

    /** Шевицата изчезва — шейдърът се връща в [ShaderPool]. */
    fun release() = guarded.release()

    companion object {
        fun createOrNull(): WeaveShader? = ShaderPool.take(ShaderPool.WEAVE)?.let(::WeaveShader)
    }
}

/** Неподвижната шевица, готова за рисуване: повторената плочка и (за логото) маската на празния кръг. */
internal class StillPaint(val brush: ShaderBrush, val clearMask: Brush?) {
    val layerPaint = Paint()
}

private fun DrawScope.drawStill(p: StillPaint, alpha: Float) {
    val mask = p.clearMask
    if (mask == null) {
        drawRect(p.brush, alpha = alpha)
        return
    }
    // Празният кръг: плочката в отделен слой, после маската я „изрязва“ (DstOut).
    drawIntoCanvas { it.saveLayer(Rect(Offset.Zero, size), p.layerPaint) }
    drawRect(p.brush, alpha = alpha)
    drawRect(mask, blendMode = BlendMode.DstOut)
    drawIntoCanvas { it.restore() }
}

/**
 * Неподвижната шевица — същата шарка и бод като шейдъра (без снопа). Шарката е
 * периодична: полето се повтаря през [period][WeaveSpec.period] бода и по двете оси,
 * лентата — през 2·period по дължина. Затова се рисува само една плочка (единственото
 * копие на осветлението на шейдъра на процесора), пази се за процеса и се повтаря с
 * ImageShader — без значение колко е голям изгледът и колко пъти сменя размера си.
 */
internal object ShevitsaStill {
    /** Колко плочки се пазят (картата, цял екран, лентата на „Начало“, въведението). */
    private const val MAX_TILES = 8

    /**
     * Една плочка. При лентата плочката е цялата височина ([height] px, [originY] — редът
     * на средата), при полето тези са 0.
     */
    data class TileKey(val cell: Float, val period: Int, val ribbon: Boolean, val height: Int, val originY: Int, val colors: ShevitsaColors)

    /** Картинката на плочката и размерът ѝ в пиксели на изгледа (клетката може да е дробна). */
    class Tile(val image: ImageBitmap, val width: Float, val height: Float)

    /** Ширината на плочката в бодове (полето е квадратно). */
    fun tileCells(period: Int, ribbon: Boolean): Int = if (ribbon) 2 * period else period

    fun key(s: WeaveSpec): TileKey =
        if (s.ribbon) TileKey(s.cell, s.period.toInt(), true, s.height.roundToInt(), s.originY.toInt(), s.colors)
        else TileKey(s.cell, s.period.toInt(), false, 0, 0, s.colors)

    private val cache = object : LinkedHashMap<TileKey, Tile>(MAX_TILES, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<TileKey, Tile>?) = size > MAX_TILES
    }

    /** Плочката за [s] — от кеша или нарисувана сега (веднъж). */
    fun tile(s: WeaveSpec): Tile {
        val k = key(s)
        synchronized(cache) { cache[k] }?.let { return it }
        val t = render(k)
        synchronized(cache) { cache[k] = t }
        return t
    }

    /**
     * Плочката на лентата с височина [heightPx] (px) — нарисувана предварително (от фоновата
     * подготовка на старта), за да не се рисува в първия кадър. Същата ключова пресметка като
     * при рисуването: клетката е височина / редове, широчината не влиза в ключа.
     */
    fun prewarmRibbon(heightPx: Int, period: Int, colors: ShevitsaColors) {
        val h = heightPx.toFloat().coerceAtLeast(1f)
        val cell = (h / ShevitsaPattern.ribbonRows(period)).coerceAtLeast(1f)
        tile(WeaveSpec(h, h, cell, period.toFloat(), true, 1f, 0f, colors))
    }

    /** Четката за изгледа [s]: плочката, повторена от клетката в средата (както uOrigin на шейдъра). */
    fun paint(s: WeaveSpec): StillPaint {
        val t = tile(s)
        val shader = ImageShader(t.image, TileMode.Repeated, if (s.ribbon) TileMode.Clamp else TileMode.Repeated)
        shader.setLocalMatrix(
            Matrix().apply {
                setScale(t.width / t.image.width, t.height / t.image.height)
                // Полето: плочката започва от клетка (0, 0); лентата — от горния ред на изгледа.
                postTranslate(s.originX * s.cell, if (s.ribbon) 0f else s.originY * s.cell)
            },
        )
        return StillPaint(ShaderBrush(shader), if (s.clear > 0f) clearMask(s) else null)
    }

    /** Маската на празния кръг: 1 в средата, 0 навън — като smoothstep(clear·0.55, clear, r) в шейдъра. */
    private fun clearMask(s: WeaveSpec): Brush {
        val steps = 6
        val stops = ArrayList<Pair<Float, Color>>(steps + 2)
        stops += 0f to Color.Black
        for (i in 0..steps) {
            val f = i / steps.toFloat()
            stops += (0.55f + 0.45f * f) to Color.Black.copy(alpha = 1f - smooth(0f, 1f, f))
        }
        return Brush.radialGradient(
            *stops.toTypedArray(),
            center = Offset(s.width * 0.5f, s.height * 0.5f),
            radius = (s.clear * min(s.width, s.height)).coerceAtLeast(1f),
        )
    }

    /** Рисува плочката: всеки пиксел — като шейдъра в средата на пиксела (без снопа и проблясването). */
    fun render(k: TileKey): Tile {
        val tw = tileCells(k.period, k.ribbon) * k.cell
        val th = if (k.ribbon) k.height.toFloat().coerceAtLeast(1f) else tw
        val w = max(1, tw.roundToInt())
        val h = max(1, th.roundToInt())
        // Пиксели на изгледа на пиксел от плочката (≈1; клетката може да е дробна).
        val sx = tw / w
        val sy = th / h
        val cell = k.cell
        val c = k.colors
        val threads = arrayOf(c.red, c.ink, c.gold)
        val fabric = c.fabric
        val period = k.period.toFloat()
        val cols = tileCells(k.period, k.ribbon)
        val pixels = IntArray(w * h)
        // Видът бод по клетки — веднъж на клетка, не на пиксел.
        val rowCells = IntArray(cols)
        var cachedRow = Int.MIN_VALUE
        val aa = 1f / cell
        val tw2 = 0.21f
        for (y in 0 until h) {
            val gy = (y + 0.5f) * sy / cell
            val cyRaw = floor(gy)
            val qy = gy - cyRaw
            val cy = cyRaw.toInt() - k.originY
            if (cy != cachedRow) {
                cachedRow = cy
                for (i in 0 until cols) rowCells[i] = ShevitsaPattern.kind(i.toFloat(), cy.toFloat(), period, k.ribbon)
            }
            for (x in 0 until w) {
                val gx = (x + 0.5f) * sx / cell
                val cxi = floor(gx).toInt().coerceIn(0, cols - 1)
                val qx = gx - floor(gx)
                val kind = rowCells[cxi]
                // Кръстатият бод: долна нишка „\“ (A) и горна „/“ (B), както в шейдъра.
                var cov = 0f
                var lit = 1f
                if (kind != ShevitsaPattern.EMPTY) {
                    val dA = abs(qx - qy) * 0.7071f
                    val tA = (qx + qy) * 0.5f
                    val dB = abs(qx + qy - 1f) * 0.7071f
                    val tB = (qx - qy) * 0.5f + 0.5f
                    val endA = smooth(0.03f, 0.1f, tA) * (1f - smooth(0.9f, 0.97f, tA))
                    val endB = smooth(0.03f, 0.1f, tB) * (1f - smooth(0.9f, 0.97f, tB))
                    val covA = (1f - smooth(tw2 - aa, tw2 + aa, dA)) * endA
                    val covB = (1f - smooth(tw2 - aa, tw2 + aa, dB)) * endB
                    val nA = dA / tw2
                    val nB = dB / tw2
                    val shA = sqrt((1f - nA * nA).coerceIn(0f, 1f))
                    val shB = sqrt((1f - nB * nB).coerceIn(0f, 1f))
                    val twA = 0.9f + 0.1f * sin((tA * 7f + dA * 9f) * 6.2832f)
                    val twB = 0.9f + 0.1f * sin((tB * 7f + dB * 9f) * 6.2832f)
                    val shd = 1f - 0.35f * (1f - smooth(tw2, tw2 + 0.12f, dB)) * endB
                    val litA = (0.5f + 0.62f * shA) * twA * shd
                    val litB = (0.5f + 0.62f * shB) * twB
                    lit = litB * covB + litA * (1f - covB)
                    cov = (covA + covB - covA * covB).coerceIn(0f, 1f)
                }
                val ca = cov * c.threadAlpha
                val fa = c.fabricAlpha * (1f - ca)
                val mx = min(qx, 1f - qx)
                val my = min(qy, 1f - qy)
                val hole = 1f - smooth(0.05f, 0.2f, sqrt(mx * mx + my * my))
                val fk = 0.97f - 0.1f * hole
                val t = if (kind != ShevitsaPattern.EMPTY) threads[kind - 1] else fabric
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
        return Tile(Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888).asImageBitmap(), tw, th)
    }

    /** Един канал: конецът (осветен) върху канавата, без premultiply. */
    private fun channel(thread: Float, fab: Float, lit: Float, ca: Float, fa: Float, fk: Float): Int =
        (((thread * lit).coerceIn(0f, 1f) * ca + (fab * fk).coerceIn(0f, 1f) * fa) / (ca + fa) * 255f).roundToInt().coerceIn(0, 255)

    private fun smooth(e0: Float, e1: Float, x: Float): Float {
        val t = ((x - e0) / (e1 - e0)).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }
}

/** Видеокартата е подгрята веднъж за процеса ([ShaderWarmup]). */
@Volatile private var shadersWarmed = false

/**
 * Видеокартата превежда шейдъра при първото му рисуване (десетки ms) — и поотделно за всяка
 * комбинация от изрязване и смесване. Веднъж за процеса, два кадъра след пълното показване
 * на началния екран (на спокойствие), се рисуват същите [LivingShevitsa] и [CardFoilLayer] като
 * в картата, в малка кутия със същото изрязване със заоблени ъгли и почти прозрачна — първото
 * отваряне на картата ги намира готови; също и отблясъкът по заглавието на книгата ([GoldSheenWarmup]). Фолиото се превежда във фонов режим едва след първия кадър.
 * Подгряването се брои за свършено само ако нещо наистина е нарисувано с шейдър.
 */
@Composable
fun ShaderWarmup(modifier: Modifier = Modifier) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || !LocalAgslEnabled.current) return
    var show by remember { mutableStateOf(false) }
    val reporter = LocalFullyDrawnReporterOwner.current?.fullyDrawnReporter
    LaunchedEffect(Unit) {
        if (shadersWarmed) return@LaunchedEffect
        // Първият кадър е зад гърба ни: фолиото се превежда във фона, не в него.
        withFrameNanos { }
        // Фолиото (картата) и отблясъкът по заглавието на книгата (преходът към книга).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ShaderPool.prepare(ShaderPool.FOIL)
            ShaderPool.prepare(ShaderPool.SHEEN)
        }
        if (reporter != null) {
            val drawn = CompletableDeferred<Unit>()
            reporter.addOnReportDrawnListener { drawn.complete(Unit) }
            // Ако пълното показване се забави (или го няма) — след 8 s подгряваме така или иначе.
            withTimeoutOrNull(8_000) { drawn.await() }
        } else {
            delay(2_000)
        }
        withFrameNanos { }
        withFrameNanos { }
        val before = ShaderDraws.total()
        show = true
        repeat(3) { withFrameNanos { } }
        show = false
        if (ShaderDraws.total() > before) shadersWarmed = true
    }
    if (show) WarmupBox(modifier)
}

/** Като картата: шевица и фолио в кутия с изрязване 20 dp; почти прозрачна (без отделен слой за прозрачността). */
@Composable
private fun WarmupBox(modifier: Modifier) {
    val tilt = remember { Tilt() }
    Box(
        modifier.size(72.dp, 44.dp).clearAndSetSemantics { }.graphicsLayer {
            alpha = 0.01f
            compositingStrategy = CompositingStrategy.ModulateAlpha
            clip = true
            shape = RoundedCornerShape(20.dp)
        },
        propagateMinConstraints = true,
    ) {
        LivingShevitsa(ShevitsaPalettes.card, Modifier.matchParentSize(), cell = 2.4.dp, intensity = 0.9f, still = false)
        CardFoilLayer(tilt, Modifier.matchParentSize())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) GoldSheenWarmup(Modifier.matchParentSize())
    }
}
