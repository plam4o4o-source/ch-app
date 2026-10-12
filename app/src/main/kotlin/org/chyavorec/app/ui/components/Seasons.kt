package org.chyavorec.app.ui.components

import android.os.PowerManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.core.AppClock
import org.chyavorec.core.OrthodoxEaster
import java.time.LocalDate
import kotlinx.coroutines.delay
import java.time.Month
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Сезон за украсите на началния екран. */
enum class Season { NONE, CHRISTMAS, EASTER, HARVEST }

/**
 * Коледа: 15 декември – 7 януари (Ивановден); Великден: православният Великден ±7 дни;
 * жътва: септември (около Кръстовден). Иначе — без украса.
 */
fun currentSeason(today: LocalDate): Season {
    val m = today.month
    val d = today.dayOfMonth
    if ((m == Month.DECEMBER && d >= 15) || (m == Month.JANUARY && d <= 7)) return Season.CHRISTMAS
    val easter = OrthodoxEaster.of(today.year)
    if (!today.isBefore(easter.minusDays(7)) && !today.isAfter(easter.plusDays(7))) return Season.EASTER
    if (m == Month.SEPTEMBER) return Season.HARVEST
    return Season.NONE
}

fun currentSeason(clock: AppClock): Season = currentSeason(clock.today())

/** Текущият сезон, ако украсите са включени в настройките (иначе [Season.NONE]). */
@Composable
fun rememberSeason(): Season {
    val container = LocalAppContainer.current
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val enabled = settings?.seasonal ?: false
    return remember(enabled) { if (enabled) currentSeason(container.clock) else Season.NONE }
}

/**
 * Сезонни частици над заглавката на „Начало“ (отделно от [Season] — орнаментите):
 * сняг (Коледа, от 15 декември), сняг със звезди (20 декември – 7 януари), падащи
 * листа (15 октомври – 30 ноември) и цветчета около Гергьовден (1–10 май).
 */
enum class SkyEffect { NONE, SNOW, SNOW_STARS, LEAVES, PETALS }

fun skyEffect(today: LocalDate): SkyEffect {
    val m = today.month
    val d = today.dayOfMonth
    return when {
        (m == Month.DECEMBER && d >= 20) || (m == Month.JANUARY && d <= 7) -> SkyEffect.SNOW_STARS
        m == Month.DECEMBER && d >= 15 -> SkyEffect.SNOW
        (m == Month.OCTOBER && d >= 15) || m == Month.NOVEMBER -> SkyEffect.LEAVES
        m == Month.MAY && d <= 10 -> SkyEffect.PETALS
        else -> SkyEffect.NONE
    }
}

/** Текущите частици, ако украсите са включени в настройките (иначе [SkyEffect.NONE]). */
@Composable
fun rememberSkyEffect(): SkyEffect {
    val container = LocalAppContainer.current
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val enabled = settings?.seasonal ?: false
    return remember(enabled) { if (enabled) skyEffect(container.clock.today()) else SkyEffect.NONE }
}

/** Една частица — неизменни параметри; положението ѝ е функция на времето (без състояние). */
private class Particle(
    val x: Float, // 0..1 по ширината
    val speed: Float, // дял от височината в секунда (0 = неподвижна)
    val size: Float, // dp
    val phase: Float,
    val sway: Float, // dp
    val offset: Float, // 0..1 начална височина
    val spin: Float, // градуса в секунда
    val tone: Int, // индекс в палитрата
)

/** ~30 кадъра в секунда: следващият кадър се иска ~25 ms след предишния (+ изчакване на vsync). */
private const val SKY_FRAME_DELAY_MS = 25L
/** Ефектът е кратък поздрав, не постоянна анимация: ~20 s, после избледнява. */
private const val SKY_RUN_NANOS = 20_000_000_000L
private const val SKY_FADE_NANOS = 1_200_000_000L

/** Ефектът е изигран в този процес — при връщане към „Начало“ не започва наново. */
private object SkySession { @Volatile var finished = false }

/** Детерминирани частици за ефекта (едни и същи при всяко стартиране). */
private fun particlesFor(effect: SkyEffect, seed: Int): List<Particle> {
    val r = Random(seed)
    return when (effect) {
        SkyEffect.NONE -> emptyList()
        SkyEffect.SNOW, SkyEffect.SNOW_STARS -> List(40) {
            Particle(
                x = r.nextFloat(), speed = 0.025f + r.nextFloat() * 0.04f, size = 1.5f + r.nextFloat() * 2.5f,
                phase = r.nextFloat() * TWO_PI, sway = 4f + r.nextFloat() * 8f, offset = r.nextFloat(), spin = 0f, tone = 0,
            )
        }
        SkyEffect.LEAVES -> List(14) {
            Particle(
                x = r.nextFloat(), speed = 0.035f + r.nextFloat() * 0.035f, size = 6f + r.nextFloat() * 5f,
                phase = r.nextFloat() * TWO_PI, sway = 10f + r.nextFloat() * 14f, offset = r.nextFloat(),
                spin = (if (r.nextBoolean()) 1f else -1f) * (40f + r.nextFloat() * 70f), tone = r.nextInt(LeafColors.size),
            )
        }
        SkyEffect.PETALS -> List(24) {
            Particle(
                x = r.nextFloat(), speed = 0.03f + r.nextFloat() * 0.03f, size = 3f + r.nextFloat() * 3f,
                phase = r.nextFloat() * TWO_PI, sway = 8f + r.nextFloat() * 12f, offset = r.nextFloat(),
                spin = (if (r.nextBoolean()) 1f else -1f) * (60f + r.nextFloat() * 90f), tone = r.nextInt(PetalColors.size),
            )
        }
    }
}

/** Звездите (само при [SkyEffect.SNOW_STARS]) — неподвижни, в горните 60 % от заглавката; трептят. */
private fun starsFor(effect: SkyEffect): List<Particle> {
    if (effect != SkyEffect.SNOW_STARS) return emptyList()
    val r = Random(2512)
    return List(12) {
        Particle(
            x = 0.04f + r.nextFloat() * 0.92f, speed = 0f, size = 2.5f + r.nextFloat() * 2.5f,
            phase = r.nextFloat() * TWO_PI, sway = 0f, offset = r.nextFloat() * 0.6f,
            spin = 1.2f + r.nextFloat() * 2f, tone = 0, // spin = честота на трептене (rad/s)
        )
    }
}

private const val TWO_PI = (2 * PI).toFloat()

private val LeafColors = listOf(Color(0xFFB5541C), Color(0xFFC98A1B), Color(0xFF8E2A1E), Color(0xFFA36B1F))
// Достатъчно наситени, за да се виждат и върху светлия фон на заглавката.
private val PetalColors = listOf(Color(0xFFE8A0B8), Color(0xFFF2BFD0), Color(0xFFD98AA6))

/** Лист в единичен квадрат около (0, 0): два дъгови ръба от дръжката до върха. */
private val LeafPath by lazy { Path().apply {
    moveTo(0f, 0.5f)
    quadraticTo(0.45f, 0.1f, 0f, -0.5f)
    quadraticTo(-0.45f, 0.1f, 0f, 0.5f)
    close()
} }

/** Венчелистче — капка, по-широка навън. */
private val PetalPath by lazy { Path().apply {
    moveTo(0f, 0.5f)
    cubicTo(0.55f, 0.2f, 0.4f, -0.5f, 0f, -0.45f)
    cubicTo(-0.4f, -0.5f, -0.55f, 0.2f, 0f, 0.5f)
    close()
} }

/** Четирилъчна звездичка в единичен квадрат около (0, 0). */
private val StarPath by lazy { Path().apply {
    moveTo(0f, -0.5f)
    lineTo(0.1f, -0.1f); lineTo(0.5f, 0f); lineTo(0.1f, 0.1f)
    lineTo(0f, 0.5f)
    lineTo(-0.1f, 0.1f); lineTo(-0.5f, 0f); lineTo(-0.1f, -0.1f)
    close()
} }

/**
 * Сезонни частици върху областта на заглавката: сняг (и трептящи звезди), листа
 * или цветчета. Рисуват се с Canvas по време от кадъра — без състояние за всяка
 * частица и без рекомпозиция (кадърът се чете само при рисуване). Пестят
 * батерията: ~30 кадъра/s, спират (с избледняване) след ~20 s или щом [stopWhen]
 * стане true (напр. потребителят е превъртял), веднъж на процес; при намалено
 * движение или режим за пестене на батерията са неподвижни.
 */
@Composable
fun SeasonalSky(effect: SkyEffect, modifier: Modifier = Modifier, snowColor: Color = Color.White, stopWhen: () -> Boolean = { false }) {
    if (effect == SkyEffect.NONE) return
    val context = LocalContext.current
    val powerSave = remember { context.getSystemService(PowerManager::class.java)?.isPowerSaveMode == true }
    val still = rememberReducedMotion() || powerSave
    val particles = remember(effect) { particlesFor(effect, 1922) }
    val stars = remember(effect) { starsFor(effect) }
    val starColor = LocalExtendedColors.current.gold
    var frame by remember { mutableLongStateOf(0L) }
    // 1 → 0 при спиране; при 0 нищо не се рисува.
    var fade by remember { mutableFloatStateOf(if (still || !SkySession.finished) 1f else 0f) }
    val stop by rememberUpdatedState(stopWhen)
    LaunchedEffect(still) {
        if (still || SkySession.finished) return@LaunchedEffect
        val start = withFrameNanos { it }
        var fadeStart = -1L
        while (true) {
            val t = withFrameNanos { it } - start
            if (fadeStart < 0 && (t >= SKY_RUN_NANOS || stop())) fadeStart = t
            if (fadeStart >= 0) {
                fade = (1f - (t - fadeStart).toFloat() / SKY_FADE_NANOS).coerceAtLeast(0f)
                if (fade == 0f) {
                    SkySession.finished = true
                    break
                }
            }
            frame = t
            delay(SKY_FRAME_DELAY_MS)
        }
    }
    Canvas(modifier.clearAndSetSemantics { }) {
        val k = if (still) 1f else fade
        if (k <= 0f) return@Canvas
        val t = frame / 1_000_000_000f
        val h = size.height
        val w = size.width
        val dp = density
        stars.forEach { s ->
            // Трептене 0.25..1 (неподвижно — средна яркост).
            val tw = if (still) 0.6f else 0.25f + 0.75f * (0.5f + 0.5f * sin(t * s.spin + s.phase))
            drawUnitPath(StarPath, Offset(s.x * w, s.offset * h), s.size * 2f * dp, 0f, 1f, starColor.copy(alpha = 0.85f * tw * k))
        }
        particles.forEach { p ->
            val y = ((p.offset + t * p.speed) % 1f) * (h + 16 * dp) - 8 * dp
            val x = p.x * w + sin(t * 0.9f + p.phase) * p.sway * dp
            when (effect) {
                SkyEffect.SNOW, SkyEffect.SNOW_STARS ->
                    drawCircle(snowColor.copy(alpha = 0.3f * k), radius = p.size * dp, center = Offset(x, y))
                SkyEffect.LEAVES -> drawUnitPath(
                    LeafPath, Offset(x, y), p.size * 2f * dp, p.phase * 57f + t * p.spin,
                    // „Преобръщане“ във въздуха: листът се свива по ширина.
                    0.35f + 0.65f * abs(cos(t * 1.3f + p.phase)),
                    LeafColors[p.tone].copy(alpha = 0.55f * k),
                )
                SkyEffect.PETALS -> drawUnitPath(
                    PetalPath, Offset(x, y), p.size * 2f * dp, p.phase * 57f + t * p.spin,
                    0.3f + 0.7f * abs(cos(t * 2.1f + p.phase)),
                    PetalColors[p.tone].copy(alpha = 0.7f * k),
                )
                SkyEffect.NONE -> Unit
            }
        }
    }
}

/** Рисува [path] (в единичен квадрат около 0,0) с център [center], размер [sizePx], завъртане и свиване по X. */
private fun DrawScope.drawUnitPath(path: Path, center: Offset, sizePx: Float, degrees: Float, squeezeX: Float, color: Color) {
    withTransform({
        translate(center.x, center.y)
        rotate(degrees, pivot = Offset.Zero)
        scale(sizePx * squeezeX, sizePx, pivot = Offset.Zero)
    }) {
        drawPath(path, color)
    }
}

/** Две писани яйца (великденска украса) — малък орнамент за заглавката. */
@Composable
fun EasterEggs(modifier: Modifier = Modifier) {
    Row(modifier.clearAndSetSemantics { }, verticalAlignment = Alignment.Bottom) {
        Canvas(Modifier.size(18.dp, 24.dp)) { drawEgg(Brand.Burgundy, Brand.Gold) }
        Spacer(Modifier.width(2.dp))
        Canvas(Modifier.size(18.dp, 24.dp)) { rotate(14f) { drawEgg(Color(0xFF3F5E4A), Brand.GoldLight) } }
    }
}

private fun DrawScope.drawEgg(base: Color, pattern: Color) {
    val w = size.width
    val h = size.height
    // Яйцето — овал, малко по-тесен отгоре.
    drawOval(Brush.verticalGradient(listOf(base.copy(alpha = 0.85f), base)), topLeft = Offset(w * 0.08f, 0f), size = Size(w * 0.84f, h))
    // Шевица: пояс с ромбчета през средата.
    val band = h * 0.5f
    drawLine(pattern, Offset(w * 0.12f, band), Offset(w * 0.88f, band), strokeWidth = 1.2f * density)
    val step = w * 0.22f
    var x = w * 0.2f
    while (x < w * 0.82f) {
        val r = w * 0.08f
        drawLine(pattern, Offset(x, band - r), Offset(x + r, band), strokeWidth = density)
        drawLine(pattern, Offset(x + r, band), Offset(x, band + r), strokeWidth = density)
        drawLine(pattern, Offset(x, band + r), Offset(x - r, band), strokeWidth = density)
        drawLine(pattern, Offset(x - r, band), Offset(x, band - r), strokeWidth = density)
        x += step
    }
    // отблясък
    drawOval(pattern.copy(alpha = 0.25f), topLeft = Offset(w * 0.2f, h * 0.1f), size = Size(w * 0.25f, h * 0.2f))
    drawOval(pattern.copy(alpha = 0.5f), topLeft = Offset(w * 0.08f, 0f), size = Size(w * 0.84f, h), style = Stroke(density * 0.8f))
}

/** Житни класове по долния ръб на карта (Кръстовден) — векторен орнамент, златен. */
@Composable
fun WheatEdge(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ornament_wheat),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.BottomCenter,
        colorFilter = ColorFilter.tint(Brand.Gold.copy(alpha = 0.55f)),
        modifier = modifier.fillMaxWidth().height(22.dp).clearAndSetSemantics { },
    )
}
