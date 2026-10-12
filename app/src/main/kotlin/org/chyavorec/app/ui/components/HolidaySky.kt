package org.chyavorec.app.ui.components

import android.os.PowerManager
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.occasion.OccasionEffect
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Една частица — неизменни параметри; положението ѝ е функция на времето (без
 * състояние). Значението на полетата зависи малко от ефекта (виж генераторите).
 */
private class HolidayParticle(
    val x: Float, // 0..1 по ширината
    val speed: Float, // дял от височината в секунда (0 = неподвижна)
    val size: Float, // dp (при буквите — мащаб)
    val phase: Float,
    val sway: Float, // dp
    val offset: Float, // 0..1 начална височина
    val spin: Float, // градуса в секунда (или честота/амплитуда — виж ефекта)
    val tone: Int, // индекс в палитрата (при буквите — коя буква)
    val alt: Int = 0, // втори индекс (при буквите — цветът)
)

/** Един фойерверк: център (0..1), радиус (dp), закъснение и период (s), цвят и посоките на искрите. */
private class HolidayBurst(
    val x: Float,
    val y: Float,
    val radius: Float,
    val delay: Float,
    val period: Float,
    val tone: Int,
    val dx: FloatArray, // посока × скорост на всяка искра
    val dy: FloatArray,
)

/** Всичко, което се рисува за един ефект — генерира се веднъж (remember). */
private class HolidayScene(
    val main: List<HolidayParticle>,
    val stars: List<HolidayParticle> = emptyList(),
    val butterflies: List<HolidayParticle> = emptyList(),
    val bursts: List<HolidayBurst> = emptyList(),
)

/** ~30 кадъра в секунда: следващият кадър се иска ~25 ms след предишния (+ изчакване на vsync). */
private const val HOLIDAY_FRAME_DELAY_MS = 25L
/** Ефектът е кратък поздрав, не постоянна анимация: ~20 s, после избледнява. */
private const val HOLIDAY_RUN_NANOS = 20_000_000_000L
private const val HOLIDAY_FADE_NANOS = 1_200_000_000L

/** Ефектът е изигран в този процес — при връщане към „Начало“ не започва наново. */
private object HolidaySkySession { @Volatile var finished = false }

private const val HOLIDAY_TWO_PI = (2 * PI).toFloat()

/** Фойерверк: изкачване и разпръсване (s); 24 искри. */
private const val FIREWORK_RISE = 0.7f
private const val FIREWORK_BURST = 1.9f
private const val FIREWORK_SPARKS = 24

private fun holidaySceneFor(effect: OccasionEffect): HolidayScene = when (effect) {
    OccasionEffect.NONE -> HolidayScene(emptyList())
    OccasionEffect.SNOW -> HolidayScene(skySnow())
    OccasionEffect.SNOW_STARS -> HolidayScene(skySnow(), stars = skyStars())
    OccasionEffect.LEAVES -> HolidayScene(skyLeaves())
    OccasionEffect.PETALS -> HolidayScene(skyPetals())
    OccasionEffect.PETALS_BUTTERFLIES -> HolidayScene(skyPetals(), butterflies = skyButterflies())
    OccasionEffect.FIREWORKS -> HolidayScene(emptyList(), bursts = skyBursts())
    OccasionEffect.LETTERS -> HolidayScene(skyLetters())
    OccasionEffect.PAGES -> HolidayScene(skyPages())
    OccasionEffect.BALLOONS -> HolidayScene(skyBalloons())
    OccasionEffect.DEW -> HolidayScene(skyDew())
}

// Детерминирани частици (едни и същи при всяко стартиране); снегът, листата и
// цветчетата — със същото зърно и ред като досегашните сезонни частици.

private fun skySnow(): List<HolidayParticle> {
    val r = Random(1922)
    return List(40) {
        HolidayParticle(
            x = r.nextFloat(), speed = 0.025f + r.nextFloat() * 0.04f, size = 1.5f + r.nextFloat() * 2.5f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 4f + r.nextFloat() * 8f, offset = r.nextFloat(), spin = 0f, tone = 0,
        )
    }
}

private fun skyLeaves(): List<HolidayParticle> {
    val r = Random(1922)
    return List(14) {
        HolidayParticle(
            x = r.nextFloat(), speed = 0.035f + r.nextFloat() * 0.035f, size = 6f + r.nextFloat() * 5f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 10f + r.nextFloat() * 14f, offset = r.nextFloat(),
            spin = (if (r.nextBoolean()) 1f else -1f) * (40f + r.nextFloat() * 70f), tone = r.nextInt(SkyLeafColors.size),
        )
    }
}

private fun skyPetals(): List<HolidayParticle> {
    val r = Random(1922)
    return List(24) {
        HolidayParticle(
            x = r.nextFloat(), speed = 0.03f + r.nextFloat() * 0.03f, size = 3f + r.nextFloat() * 3f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 8f + r.nextFloat() * 12f, offset = r.nextFloat(),
            spin = (if (r.nextBoolean()) 1f else -1f) * (60f + r.nextFloat() * 90f), tone = r.nextInt(SkyPetalColors.size),
        )
    }
}

/** Звездите — неподвижни, в горните 60 % от заглавката; трептят (spin = честота, rad/s). */
private fun skyStars(): List<HolidayParticle> {
    val r = Random(2512)
    return List(12) {
        HolidayParticle(
            x = 0.04f + r.nextFloat() * 0.92f, speed = 0f, size = 2.5f + r.nextFloat() * 2.5f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 0f, offset = r.nextFloat() * 0.6f,
            spin = 1.2f + r.nextFloat() * 2f, tone = 0,
        )
    }
}

/** Пеперуди — бавно нагоре, с криволичене; spin = честота на пърхане (rad/s). */
private fun skyButterflies(): List<HolidayParticle> {
    val r = Random(321)
    return List(5) { i ->
        HolidayParticle(
            x = (i + 0.2f + r.nextFloat() * 0.6f) / 5f, speed = 0.03f + r.nextFloat() * 0.02f, size = 10f + r.nextFloat() * 4f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 14f + r.nextFloat() * 12f, offset = r.nextFloat(),
            spin = 7f + r.nextFloat() * 4f, tone = i % SkyButterflyColors.size,
        )
    }
}

/** Фойерверки на различни места, с разминати закъснения и периоди. */
private fun skyBursts(): List<HolidayBurst> {
    val r = Random(2026)
    val xs = floatArrayOf(0.22f, 0.74f, 0.47f, 0.88f)
    return List(xs.size) { i ->
        val dx = FloatArray(FIREWORK_SPARKS)
        val dy = FloatArray(FIREWORK_SPARKS)
        for (j in 0 until FIREWORK_SPARKS) {
            val a = j * HOLIDAY_TWO_PI / FIREWORK_SPARKS + (r.nextFloat() - 0.5f) * 0.16f
            val v = 0.75f + r.nextFloat() * 0.25f
            dx[j] = cos(a) * v
            dy[j] = sin(a) * v
        }
        HolidayBurst(
            x = xs[i] + (r.nextFloat() - 0.5f) * 0.08f, y = 0.3f + r.nextFloat() * 0.15f, radius = 30f + r.nextFloat() * 10f,
            delay = i * 0.95f + r.nextFloat() * 0.3f, period = 3.6f + r.nextFloat() * 0.8f, tone = i % SkyFireworkColors.size,
            dx = dx, dy = dy,
        )
    }
}

/** Букви — tone е буквата, alt — цветът (златно/бордо); spin = амплитуда на люлеене (°). */
private fun skyLetters(): List<HolidayParticle> {
    val r = Random(2405)
    return List(18) {
        HolidayParticle(
            x = r.nextFloat(), speed = 0.03f + r.nextFloat() * 0.025f, size = 0.75f + r.nextFloat() * 0.5f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 8f + r.nextFloat() * 10f, offset = r.nextFloat(),
            spin = 15f + r.nextFloat() * 20f, tone = r.nextInt(SKY_ALPHABET.length), alt = r.nextInt(2),
        )
    }
}

/** Страници — sway е хоризонталната скорост (dp/s, alt = посоката); spin = честота на прелистване. */
private fun skyPages(): List<HolidayParticle> {
    val r = Random(423)
    return List(8) {
        HolidayParticle(
            x = r.nextFloat(), speed = 0.03f + r.nextFloat() * 0.025f, size = 16f + r.nextFloat() * 6f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 6f + r.nextFloat() * 8f, offset = r.nextFloat(),
            spin = 1.4f + r.nextFloat() * 1.4f, tone = 0, alt = if (r.nextBoolean()) 1 else -1,
        )
    }
}

/** Балончета — равномерно по ширината, за да не се застъпват. */
private fun skyBalloons(): List<HolidayParticle> {
    val r = Random(601)
    return List(10) { i ->
        HolidayParticle(
            x = (i + 0.2f + r.nextFloat() * 0.6f) / 10f, speed = 0.045f + r.nextFloat() * 0.03f, size = 14f + r.nextFloat() * 6f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 6f + r.nextFloat() * 6f, offset = r.nextFloat(),
            spin = 0f, tone = r.nextInt(SkyBalloonColors.size),
        )
    }
}

/** Капки роса — на място; spin = честота на трептене (rad/s). */
private fun skyDew(): List<HolidayParticle> {
    val r = Random(624)
    return List(30) {
        HolidayParticle(
            x = 0.03f + r.nextFloat() * 0.94f, speed = 0f, size = 5f + r.nextFloat() * 4f,
            phase = r.nextFloat() * HOLIDAY_TWO_PI, sway = 0f, offset = 0.08f + r.nextFloat() * 0.84f,
            spin = 0.8f + r.nextFloat() * 1f, tone = r.nextInt(3),
        )
    }
}

private val SkyLeafColors = listOf(Color(0xFFB5541C), Color(0xFFC98A1B), Color(0xFF8E2A1E), Color(0xFFA36B1F))
// Достатъчно наситени, за да се виждат и върху светлия фон на заглавката.
private val SkyPetalColors = listOf(Color(0xFFE8A0B8), Color(0xFFF2BFD0), Color(0xFFD98AA6))
// Пастелни: лавандула, небесно, масло, мента, праскова.
private val SkyButterflyColors = listOf(
    Color(0xFFB9A4E0), Color(0xFF8FC1E6), Color(0xFFF0CB6A), Color(0xFF8ED3AA), Color(0xFFF2A88F),
)
private val SkyButterflyBody = Color(0xFF5B4A3F)
// Златно, червено, зелено; четвъртият цвят („бяло“) е snowColor — вижда се и на светъл фон.
private val SkyFireworkColors = listOf(Color(0xFFE8C04A), Color(0xFFE0474F), Color(0xFF4DB86A), Color.White)
private val SkyBalloonColors = listOf(
    Color(0xFFE53950), Color(0xFF3D8BE0), Color(0xFFF5C02E), Color(0xFF3FB86B), Color(0xFF9B5DE5), Color(0xFFF58A3D),
)
private val SkyBalloonString = Color(0xFF8A8A8A)
// Пергамент: лице, гръб (по-тъмен), подгънат ъгъл, ръб и „редове“.
private val SkyPageFront = Color(0xFFF7EEDA)
private val SkyPageBack = Color(0xFFEADBB8)
private val SkyPageCurl = Color(0xFFD9C49A)
private val SkyPageEdge = Color(0xFFB59A68)
private val SkyPageInk = Color(0xFF7A6644)
// Бордо, по-светло на тъмен фон.
private val SkyBurgundyLight = Color(0xFFC0566A)
// Роса: свежо зелено, (сребристо = snowColor), бледо зелено.
private val SkyDewGreen = Color(0xFF6CC47A)
private val SkyDewPale = Color(0xFFB8E6C1)

/** Българската азбука (24 май). */
private const val SKY_ALPHABET = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЬЮЯ"

/** Редовете „текст“ по страницата (в единичния квадрат): височина и край. */
private val SkyPageLineY = floatArrayOf(-0.32f, -0.2f, -0.08f, 0.04f, 0.16f)
private val SkyPageLineEnd = floatArrayOf(0.26f, 0.2f, 0.26f, 0.22f, 0.06f)
private val SkyPageStroke = Stroke(width = 0.03f)

/** Лист в единичен квадрат около (0, 0): два дъгови ръба от дръжката до върха. */
private val SkyLeafPath by lazy { Path().apply {
    moveTo(0f, 0.5f)
    quadraticTo(0.45f, 0.1f, 0f, -0.5f)
    quadraticTo(-0.45f, 0.1f, 0f, 0.5f)
    close()
} }

/** Венчелистче — капка, по-широка навън. */
private val SkyPetalPath by lazy { Path().apply {
    moveTo(0f, 0.5f)
    cubicTo(0.55f, 0.2f, 0.4f, -0.5f, 0f, -0.45f)
    cubicTo(-0.4f, -0.5f, -0.55f, 0.2f, 0f, 0.5f)
    close()
} }

/** Четирилъчна звездичка в единичен квадрат около (0, 0). */
private val SkyStarPath by lazy { Path().apply {
    moveTo(0f, -0.5f)
    lineTo(0.1f, -0.1f); lineTo(0.5f, 0f); lineTo(0.1f, 0.1f)
    lineTo(0f, 0.5f)
    lineTo(-0.1f, 0.1f); lineTo(-0.5f, 0f); lineTo(-0.1f, -0.1f)
    close()
} }

/** Отблясък на роса — по-тънка четирилъчна звездичка. */
private val SkyGlintPath by lazy { Path().apply {
    moveTo(0f, -0.5f)
    lineTo(0.05f, -0.05f); lineTo(0.5f, 0f); lineTo(0.05f, 0.05f)
    lineTo(0f, 0.5f)
    lineTo(-0.05f, 0.05f); lineTo(-0.5f, 0f); lineTo(-0.05f, -0.05f)
    close()
} }

/** Пеперуда: две двойки криле (горни по-големи) около тялото в (0, 0). */
private val SkyButterflyPath by lazy { Path().apply {
    for (s in intArrayOf(1, -1)) {
        // горно крило
        moveTo(0f, -0.02f)
        cubicTo(0.12f * s, -0.48f, 0.52f * s, -0.52f, 0.48f * s, -0.18f)
        cubicTo(0.46f * s, -0.02f, 0.2f * s, 0.02f, 0f, -0.02f)
        close()
        // долно крило
        moveTo(0f, 0.02f)
        cubicTo(0.3f * s, 0.02f, 0.42f * s, 0.3f, 0.26f * s, 0.4f)
        cubicTo(0.14f * s, 0.46f, 0.04f * s, 0.25f, 0f, 0.02f)
        close()
    }
} }

/** Страница 0.72×1 около (0, 0) с подгънат долен десен ъгъл. */
private val SkyPagePath by lazy { Path().apply {
    moveTo(-0.36f, -0.5f)
    lineTo(0.36f, -0.5f)
    lineTo(0.36f, 0.28f)
    quadraticTo(0.3f, 0.44f, 0.16f, 0.5f)
    lineTo(-0.36f, 0.5f)
    close()
} }

/** Подгънатият ъгъл на страницата. */
private val SkyPageCurlPath by lazy { Path().apply {
    moveTo(0.36f, 0.28f)
    quadraticTo(0.24f, 0.3f, 0.16f, 0.5f)
    quadraticTo(0.3f, 0.44f, 0.36f, 0.28f)
    close()
} }

/** Възелче на балон: триъгълник с връх в (0, 0), надолу до y = 1. */
private val SkyKnotPath by lazy { Path().apply {
    moveTo(0f, 0f)
    lineTo(0.5f, 1f)
    lineTo(-0.5f, 1f)
    close()
} }

/** Всяка буква се измерва веднъж — в рисуването само се рисува готовото оформление. */
private fun measureSkyGlyphs(measurer: TextMeasurer): List<TextLayoutResult> {
    val style = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
    return List(SKY_ALPHABET.length) { measurer.measure(SKY_ALPHABET[it].toString(), style) }
}

/** Меко сияние на капка роса в единичен кръг (радиус 0.5) около (0, 0). */
private fun skyDewBrushes(silver: Color): List<Brush> = listOf(SkyDewGreen, silver, SkyDewPale).map { c ->
    Brush.radialGradient(
        0f to c.copy(alpha = 0.95f), 0.45f to c.copy(alpha = 0.45f), 1f to c.copy(alpha = 0f),
        center = Offset.Zero, radius = 0.5f,
    )
}

/**
 * Празнични частици върху областта на заглавката: сняг (и звезди), листа,
 * цветчета (и пеперуди), фойерверки, букви, страници, балончета или роса.
 * Рисуват се с Canvas по време от кадъра — без състояние за всяка частица и без
 * рекомпозиция (кадърът се чете само при рисуване); в рисуването нищо не се
 * създава (пътищата и частиците са готови). Пестят батерията: ~30 кадъра/s,
 * спират (с избледняване) след ~20 s или щом [stopWhen] стане true (напр.
 * потребителят е превъртял), веднъж на процес; при намалено движение или режим
 * за пестене на батерията са неподвижни.
 */
@Composable
fun HolidaySky(effect: OccasionEffect, modifier: Modifier = Modifier, snowColor: Color = Color.White, stopWhen: () -> Boolean = { false }) {
    if (effect == OccasionEffect.NONE) return
    val context = LocalContext.current
    val powerSave = remember { context.getSystemService(PowerManager::class.java)?.isPowerSaveMode == true }
    val still = rememberReducedMotion() || powerSave
    val scene = remember(effect) { holidaySceneFor(effect) }
    val ext = LocalExtendedColors.current
    val gold = ext.gold
    val burgundy = if (ext.isDark) SkyBurgundyLight else Brand.Burgundy
    val measurer = rememberTextMeasurer()
    val glyphs = remember(effect, measurer) { if (effect == OccasionEffect.LETTERS) measureSkyGlyphs(measurer) else emptyList() }
    val dewBrushes = remember(effect, snowColor) { if (effect == OccasionEffect.DEW) skyDewBrushes(snowColor) else emptyList() }
    var frame by remember { mutableLongStateOf(0L) }
    // 1 → 0 при спиране; при 0 нищо не се рисува.
    var fade by remember { mutableFloatStateOf(if (still || !HolidaySkySession.finished) 1f else 0f) }
    val stop by rememberUpdatedState(stopWhen)
    LaunchedEffect(still) {
        if (still || HolidaySkySession.finished) return@LaunchedEffect
        val start = withFrameNanos { it }
        var fadeStart = -1L
        while (true) {
            val t = withFrameNanos { it } - start
            if (fadeStart < 0 && (t >= HOLIDAY_RUN_NANOS || stop())) fadeStart = t
            if (fadeStart >= 0) {
                fade = (1f - (t - fadeStart).toFloat() / HOLIDAY_FADE_NANOS).coerceAtLeast(0f)
                if (fade == 0f) {
                    HolidaySkySession.finished = true
                    break
                }
            }
            frame = t
            delay(HOLIDAY_FRAME_DELAY_MS)
        }
    }
    // Изрязано по заглавката — нищо не излиза върху съдържанието под нея.
    Canvas(modifier.clipToBounds().clearAndSetSemantics { }) {
        val k = if (still) 1f else fade
        if (k <= 0f) return@Canvas
        val t = frame / 1_000_000_000f
        when (effect) {
            OccasionEffect.SNOW, OccasionEffect.SNOW_STARS -> {
                drawSkyStars(scene.stars, t, k, still, gold)
                drawSkySnow(scene.main, t, k, snowColor)
            }
            OccasionEffect.LEAVES -> drawSkyFalling(scene.main, SkyLeafPath, SkyLeafColors, 0.35f, 1.3f, 0.55f, t, k)
            OccasionEffect.PETALS -> drawSkyFalling(scene.main, SkyPetalPath, SkyPetalColors, 0.3f, 2.1f, 0.7f, t, k)
            OccasionEffect.PETALS_BUTTERFLIES -> {
                drawSkyFalling(scene.main, SkyPetalPath, SkyPetalColors, 0.3f, 2.1f, 0.7f, t, k)
                drawSkyButterflies(scene.butterflies, t, k, still)
            }
            OccasionEffect.FIREWORKS -> drawSkyFireworks(scene.bursts, t, k, still, snowColor)
            OccasionEffect.LETTERS -> drawSkyLetters(scene.main, glyphs, t, k, gold, burgundy)
            OccasionEffect.PAGES -> drawSkyPages(scene.main, t, k, still)
            OccasionEffect.BALLOONS -> drawSkyBalloons(scene.main, t, k, still)
            OccasionEffect.DEW -> drawSkyDew(scene.main, dewBrushes, t, k, still, snowColor)
            OccasionEffect.NONE -> Unit
        }
    }
}

/** Трептящи звезди (0.25..1; неподвижни — средна яркост). */
private fun DrawScope.drawSkyStars(stars: List<HolidayParticle>, t: Float, k: Float, still: Boolean, color: Color) {
    val w = size.width
    val h = size.height
    for (i in stars.indices) {
        val s = stars[i]
        val tw = if (still) 0.6f else 0.25f + 0.75f * (0.5f + 0.5f * sin(t * s.spin + s.phase))
        drawSkyPath(SkyStarPath, Offset(s.x * w, s.offset * h), s.size * 2f * density, 0f, 1f, color.copy(alpha = 0.85f * tw * k))
    }
}

private fun DrawScope.drawSkySnow(flakes: List<HolidayParticle>, t: Float, k: Float, color: Color) {
    val w = size.width
    val h = size.height
    val dp = density
    val c = color.copy(alpha = 0.3f * k)
    for (i in flakes.indices) {
        val p = flakes[i]
        val y = ((p.offset + t * p.speed) % 1f) * (h + 16 * dp) - 8 * dp
        val x = p.x * w + sin(t * 0.9f + p.phase) * p.sway * dp
        drawCircle(c, radius = p.size * dp, center = Offset(x, y))
    }
}

/** Листа/цветчета: падат, люлеят се, въртят се и се „преобръщат“ (свиване по ширина). */
private fun DrawScope.drawSkyFalling(
    list: List<HolidayParticle>, path: Path, colors: List<Color>, squeezeMin: Float, flipRate: Float, alpha: Float, t: Float, k: Float,
) {
    val w = size.width
    val h = size.height
    val dp = density
    for (i in list.indices) {
        val p = list[i]
        val y = ((p.offset + t * p.speed) % 1f) * (h + 16 * dp) - 8 * dp
        val x = p.x * w + sin(t * 0.9f + p.phase) * p.sway * dp
        drawSkyPath(
            path, Offset(x, y), p.size * 2f * dp, p.phase * 57f + t * p.spin,
            squeezeMin + (1f - squeezeMin) * abs(cos(t * flipRate + p.phase)),
            colors[p.tone].copy(alpha = alpha * k),
        )
    }
}

/** Пеперуди: пърхат (свиване по X с |sin|) и криволичат нагоре. */
private fun DrawScope.drawSkyButterflies(list: List<HolidayParticle>, t: Float, k: Float, still: Boolean) {
    val w = size.width
    val h = size.height
    val dp = density
    val body = SkyButterflyBody.copy(alpha = 0.75f * k)
    for (i in list.indices) {
        val b = list[i]
        val s = b.size * dp
        val y = h + s - ((b.offset + t * b.speed) % 1f) * (h + 2 * s)
        val x = b.x * w + sin(t * 0.55f + b.phase) * b.sway * dp + sin(t * 1.3f + b.phase * 2f) * b.sway * 0.3f * dp
        // Накланя се по посоката на движение.
        val tilt = cos(t * 0.55f + b.phase) * 22f
        val flap = if (still) 0.85f else 0.2f + 0.8f * abs(sin(t * b.spin + b.phase))
        withTransform({
            translate(x, y)
            rotate(tilt, pivot = Offset.Zero)
        }) {
            scale(s * flap, s, pivot = Offset.Zero) {
                drawPath(SkyButterflyPath, SkyButterflyColors[b.tone].copy(alpha = 0.85f * k))
            }
            drawLine(body, Offset(0f, -0.28f * s), Offset(0f, 0.32f * s), strokeWidth = 1.3f * dp, cap = StrokeCap.Round)
        }
    }
}

/** Фойерверки: искра нагоре, после 24 искри навън, с гравитация и избледняване; неподвижно — един застинал взрив. */
private fun DrawScope.drawSkyFireworks(bursts: List<HolidayBurst>, t: Float, k: Float, still: Boolean, white: Color) {
    if (bursts.isEmpty()) return
    if (still) {
        drawSkyBurst(bursts[0], 0.35f, k, white)
        return
    }
    val h = size.height
    val dp = density
    for (i in bursts.indices) {
        val b = bursts[i]
        if (t < b.delay) continue
        val u = (t - b.delay) % b.period
        if (u < FIREWORK_RISE) {
            val q = u / FIREWORK_RISE
            val ease = 1f - (1f - q) * (1f - q)
            val y0 = h + 4 * dp
            val y = y0 + (b.y * h - y0) * ease
            val x = b.x * size.width + sin(q * 6f) * 1.5f * dp
            drawLine(
                SkyFireworkColors[0].copy(alpha = 0.55f * k), Offset(x, y + 12 * dp * (1f - 0.5f * ease)), Offset(x, y),
                strokeWidth = 1.5f * dp, cap = StrokeCap.Round,
            )
            drawCircle(white.copy(alpha = 0.95f * k), radius = 1.8f * dp, center = Offset(x, y))
        } else {
            val e = (u - FIREWORK_RISE) / FIREWORK_BURST
            if (e < 1f) drawSkyBurst(b, e, k, white)
        }
    }
}

/** Един взрив в момента [e] (0..1): разширяване (ease-out), падане и избледняване. */
private fun DrawScope.drawSkyBurst(b: HolidayBurst, e: Float, k: Float, white: Color) {
    val dp = density
    val cx = b.x * size.width
    val cy = b.y * size.height
    val radius = min(b.radius * dp, 0.36f * size.height)
    val inv = 1f - e
    val reach = 1f - inv * inv * inv
    val drop = radius * 0.55f * e * e
    val main = if (b.tone == 3) white else SkyFireworkColors[b.tone]
    val second = if (b.tone == 0) white else SkyFireworkColors[0]
    // Кратък блясък в центъра.
    if (e < 0.15f) drawCircle(white.copy(alpha = 0.6f * (1f - e / 0.15f) * k), radius = radius * 0.25f, center = Offset(cx, cy))
    for (j in 0 until FIREWORK_SPARKS) {
        // Към края искрите премигват.
        val twinkle = if (e > 0.6f) 0.6f + 0.4f * sin(j * 7f + e * 40f) else 1f
        val a = inv * (1f - 0.3f * e) * twinkle * k
        val c = (if (j % 3 == 0) second else main).copy(alpha = a.coerceIn(0f, 1f))
        val px = cx + b.dx[j] * radius * reach
        val py = cy + b.dy[j] * radius * reach + drop
        val tail = 0.82f
        drawLine(
            c, Offset(cx + b.dx[j] * radius * reach * tail, cy + b.dy[j] * radius * reach * tail + drop * tail), Offset(px, py),
            strokeWidth = 1.4f * dp, cap = StrokeCap.Round,
        )
        drawCircle(c, radius = 1.4f * dp, center = Offset(px, py))
    }
}

/** Падащи букви: люлеят се и леко се въртят (оформленията са измерени предварително). */
private fun DrawScope.drawSkyLetters(
    list: List<HolidayParticle>, glyphs: List<TextLayoutResult>, t: Float, k: Float, gold: Color, burgundy: Color,
) {
    if (glyphs.isEmpty()) return
    val w = size.width
    val h = size.height
    val dp = density
    for (i in list.indices) {
        val p = list[i]
        val g = glyphs[p.tone]
        val gw = g.size.width.toFloat()
        val gh = g.size.height.toFloat()
        val margin = gh * p.size * 0.6f
        val y = ((p.offset + t * p.speed) % 1f) * (h + 2 * margin) - margin
        val x = p.x * w + sin(t * 0.8f + p.phase) * p.sway * dp
        val deg = sin(t * 0.6f + p.phase) * p.spin
        val c = if (p.alt == 0) gold else burgundy
        withTransform({
            translate(x, y)
            rotate(deg, pivot = Offset.Zero)
            scale(p.size, p.size, pivot = Offset.Zero)
        }) {
            drawText(g, color = c.copy(alpha = 0.75f * k), topLeft = Offset(-gw / 2f, -gh / 2f))
        }
    }
}

/** Страници: летят напреко и надолу, полюшват се и се прелистват (scaleX = cos); гърбът е по-тъмен. */
private fun DrawScope.drawSkyPages(list: List<HolidayParticle>, t: Float, k: Float, still: Boolean) {
    val w = size.width
    val h = size.height
    val dp = density
    for (i in list.indices) {
        val p = list[i]
        val s = p.size * dp
        val y = ((p.offset + t * p.speed) % 1f) * (h + 2 * s) - s
        val span = w + 2 * s
        val across = (p.x * span + t * p.sway * dp * p.alt) % span
        val x = (if (across < 0f) across + span else across) - s + sin(t * 1.1f + p.phase) * 8 * dp
        val deg = (p.phase * 57f) % 50f - 25f + sin(t * 1.4f + p.phase) * 25f
        val turn = if (still) 0.6f + 0.4f * abs(cos(p.phase)) else cos(t * p.spin + p.phase)
        val back = turn < 0f
        // Ребром — тънка ивица, не нулев мащаб.
        val flip = if (abs(turn) < 0.05f) (if (back) -0.05f else 0.05f) else turn
        withTransform({
            translate(x, y)
            rotate(deg, pivot = Offset.Zero)
            scale(s * flip, s, pivot = Offset.Zero)
        }) {
            drawPath(SkyPagePath, (if (back) SkyPageBack else SkyPageFront).copy(alpha = 0.92f * k))
            drawPath(SkyPagePath, SkyPageEdge.copy(alpha = 0.6f * k), style = SkyPageStroke)
            if (!back) {
                val ink = SkyPageInk.copy(alpha = 0.35f * k)
                for (l in SkyPageLineY.indices) {
                    drawLine(ink, Offset(-0.26f, SkyPageLineY[l]), Offset(SkyPageLineEnd[l], SkyPageLineY[l]), strokeWidth = 0.035f)
                }
            }
            drawPath(SkyPageCurlPath, SkyPageCurl.copy(alpha = 0.92f * k))
        }
    }
}

/** Балончета: издигат се отдолу нагоре с полюшване; неподвижно — във въздуха, по средата. */
private fun DrawScope.drawSkyBalloons(list: List<HolidayParticle>, t: Float, k: Float, still: Boolean) {
    val w = size.width
    val h = size.height
    val dp = density
    val string = SkyBalloonString.copy(alpha = 0.7f * k)
    val shine = Color.White.copy(alpha = 0.45f * k)
    for (i in list.indices) {
        val p = list[i]
        val bw = p.size * dp
        val bh = bw * 1.22f
        val len = bh * 1.3f
        val prog = if (still) 0.15f + 0.6f * p.offset else (p.offset + t * p.speed) % 1f
        // Центърът: от под долния ръб (с конеца) до над горния.
        val y = h + bh * 0.5f - prog * (h + bh + len)
        val x = p.x * w + sin(t * 0.7f + p.phase) * p.sway * dp
        val tilt = sin(t * 0.7f + p.phase + 0.6f) * 6f
        val c = SkyBalloonColors[p.tone].copy(alpha = 0.88f * k)
        withTransform({
            translate(x, y)
            rotate(tilt, pivot = Offset.Zero)
        }) {
            drawOval(c, topLeft = Offset(-bw / 2f, -bh / 2f), size = Size(bw, bh))
            drawOval(shine, topLeft = Offset(-bw * 0.3f, -bh * 0.36f), size = Size(bw * 0.22f, bh * 0.3f))
            val knot = bw * 0.18f
            withTransform({
                translate(0f, bh * 0.5f - knot * 0.2f)
                scale(knot, knot, pivot = Offset.Zero)
            }) {
                drawPath(SkyKnotPath, c)
            }
            // Вълнист конец — четири къси отсечки.
            var sx = 0f
            var sy = bh * 0.5f + knot * 0.8f
            for (seg in 1..4) {
                val ny = sy + len / 4f
                val nx = sin(t * 3f + p.phase + seg * 1.3f) * 2.5f * dp * (seg / 4f)
                drawLine(string, Offset(sx, sy), Offset(nx, ny), strokeWidth = dp)
                sx = nx
                sy = ny
            }
        }
    }
}

/** Роса: меки сияния с четирилъчен отблясък, проблясват на място с лек дрейф. */
private fun DrawScope.drawSkyDew(list: List<HolidayParticle>, brushes: List<Brush>, t: Float, k: Float, still: Boolean, glint: Color) {
    if (brushes.isEmpty()) return
    val w = size.width
    val h = size.height
    val dp = density
    for (i in list.indices) {
        val p = list[i]
        val a = if (still) {
            0.35f + 0.5f * abs(sin(p.phase))
        } else {
            val v = 0.5f + 0.5f * sin(t * p.spin + p.phase)
            v * v
        }
        if (a < 0.02f) continue
        val s = p.size * dp
        val x = p.x * w + sin(t * 0.3f + p.phase) * 3 * dp
        val y = p.offset * h + cos(t * 0.25f + p.phase) * 2 * dp
        withTransform({
            translate(x, y)
            scale(s, s, pivot = Offset.Zero)
        }) {
            drawCircle(brushes[p.tone], radius = 0.5f, center = Offset.Zero, alpha = a * k)
        }
        drawSkyPath(SkyGlintPath, Offset(x, y), s * 1.6f * (0.6f + 0.4f * a), t * 25f + p.phase * 57f, 1f, glint.copy(alpha = 0.9f * a * k))
    }
}

/** Рисува [path] (в единичен квадрат около 0,0) с център [center], размер [sizePx], завъртане и свиване по X. */
private fun DrawScope.drawSkyPath(path: Path, center: Offset, sizePx: Float, degrees: Float, squeezeX: Float, color: Color) {
    withTransform({
        translate(center.x, center.y)
        rotate(degrees, pivot = Offset.Zero)
        scale(sizePx * squeezeX, sizePx, pivot = Offset.Zero)
    }) {
        drawPath(path, color)
    }
}
