package org.chyavorec.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.domain.occasion.OccasionOrnament
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/*
 * Празничните орнаменти се рисуват в „чертожна“ мрежа с височина 28 единици и
 * ширина според орнамента; Canvas-ът я мащабира към реалния си размер, така че
 * 1 единица = 1 dp при номиналния размер (и дебелините на линиите следват density).
 * Пътищата са в единици на мрежата и се създават веднъж (by lazy).
 */

/** Височината на мрежата (= номиналната височина в dp). */
private const val ART_H = 28f

private const val RAD = (PI / 180).toFloat()
private const val DEG = (180 / PI).toFloat()

/** Малка празнична рисунка за заглавката на „Начало“ (декоративна — без семантика). */
@Composable
fun HolidayOrnament(ornament: OccasionOrnament, modifier: Modifier = Modifier, quiet: Boolean = false) {
    if (ornament == OccasionOrnament.NONE) return
    if (ornament == OccasionOrnament.EASTER_EGGS) {
        EasterEggs(modifier)
        return
    }
    val w = artWidth(ornament)
    val ink = if (quiet) QuietInk else FestiveInk
    Canvas(modifier.size(w.dp, ART_H.dp).clearAndSetSemantics { }) {
        // Вписваме мрежата w×28 в наличното място, центрирана.
        val k = minOf(size.width / w, size.height / ART_H)
        withTransform({
            translate((size.width - w * k) / 2f, (size.height - ART_H * k) / 2f)
            scale(k, k, Offset.Zero)
        }) {
            drawOrnament(ornament, ink)
        }
    }
}

/** Ширината на мрежата за орнамента (в dp при номинален размер). */
private fun artWidth(o: OccasionOrnament): Float = when (o) {
    OccasionOrnament.TRICOLOR, OccasionOrnament.FISH -> 40f
    OccasionOrnament.BOOK_FLAME, OccasionOrnament.VINE -> 36f
    OccasionOrnament.LAUREL_WREATH -> 34f
    OccasionOrnament.CROSS_WATER, OccasionOrnament.MARTENITSA -> 32f
    else -> 30f
}

private fun DrawScope.drawOrnament(o: OccasionOrnament, ink: OrnamentInk) {
    when (o) {
        OccasionOrnament.NONE, OccasionOrnament.EASTER_EGGS -> Unit
        OccasionOrnament.SUN -> drawSun(ink)
        OccasionOrnament.TRICOLOR -> drawTricolor(ink)
        OccasionOrnament.GREEN_WREATH -> drawGreenWreath(ink)
        OccasionOrnament.LAUREL_WREATH -> drawLaurelWreath(ink)
        OccasionOrnament.RITUAL_BREAD -> drawRitualBread(ink)
        OccasionOrnament.CHRISTMAS_STAR -> drawChristmasStar(ink)
        OccasionOrnament.MEMORIAL_WREATH -> drawMemorialWreath(ink)
        OccasionOrnament.BOOK_FLAME -> drawBookFlame(ink)
        OccasionOrnament.SURVACHKA -> drawSurvachka(ink)
        OccasionOrnament.CROSS_WATER -> drawCrossWater(ink)
        OccasionOrnament.VINE -> drawVine(ink)
        OccasionOrnament.MARTENITSA -> drawMartenitsa(ink)
        OccasionOrnament.FLOWER_WREATH -> drawFlowerWreath(ink)
        OccasionOrnament.WILLOW -> drawWillow(ink)
        OccasionOrnament.HERB_WREATH -> drawHerbWreath(ink)
        OccasionOrnament.FISH -> drawFish(ink)
    }
}

// ——— Цветове ———

/** Приглушен цвят: [amount] от пътя към сивото със същата яркост (тихите, паметни дни). */
private fun Color.muted(amount: Float = 0.4f): Color {
    val grey = 0.299f * red + 0.587f * green + 0.114f * blue
    return Color(
        red = red + (grey - red) * amount,
        green = green + (grey - green) * amount,
        blue = blue + (grey - blue) * amount,
        alpha = alpha,
    )
}

/**
 * Палитрата на орнаментите — наситени средни тонове, които се четат и върху
 * светлия (#FAF7F2), и върху тъмния (#1A1208) фон на заглавката. При [quiet]
 * всичко е приглушено. Градиентите са в единици на мрежата (създават се веднъж).
 */
private class OrnamentInk(private val quiet: Boolean) {
    private fun c(argb: Long): Color = c(Color(argb))
    private fun c(color: Color): Color = if (quiet) color.muted() else color

    // Общи
    val gold = c(Brand.Gold)
    val goldDark = c(Brand.GoldDark)
    val red = c(0xFFC62828)
    val redDark = c(0xFF8E1B1B)
    val white = c(0xFFFFFFFF)
    val whiteEdge = c(0xFF9C8F7A)
    val leafGreen = c(0xFF3F8A4A)
    val leafLight = c(0xFF6AAE4E)
    val stemGreen = c(0xFF2E6B3A)

    // Слънце
    val ray = c(0xFFE0A526)
    val rust = c(0xFFB5541C)
    val sunDisc = Brush.radialGradient(listOf(c(0xFFFFDB6E), c(0xFFF2B53A), c(0xFFE08A1C)), Offset.Zero, 6.6f)

    // Трикольор
    val flagWhite = c(0xFFFFFFFF)
    val flagGreen = c(0xFF00966E)
    val flagRed = c(0xFFD62612)
    val flagEdge = c(Brand.Muted).copy(alpha = 0.8f)
    val pole = c(0xFF6E5A44)
    val flagShade = Brush.horizontalGradient(
        listOf(Color.Transparent, Color(0x29000000), Color.Transparent, Color(0x1F000000), Color.Transparent),
        startX = 7f,
        endX = 36f,
    )

    // Лавров венец
    val laurel = c(0xFF7A9A3C)
    val ribbon = c(0xFF8E2A35)

    // Пита
    val dough = c(0xFFF6DDA8)
    val doughShade = c(0xFFB97A3A)
    val crustEdge = c(0xFF8A4F1E)
    val crust = Brush.radialGradient(listOf(c(0xFFE7B467), c(0xFFC98838), c(0xFFA8652A)), Offset.Zero, 12.4f)

    // Звезда
    val starGlow = Brush.radialGradient(listOf(c(Brand.Gold).copy(alpha = 0.5f), Color.Transparent), Offset(15f, 14f), 13.5f)
    val starFill = Brush.radialGradient(listOf(c(0xFFFFF0B8), c(0xFFF0C350), c(0xFFD9A22E)), Offset(15f, 14f), 12f)

    // Паметен венец — винаги приглушен
    val mourn = c(0xFF3E5A44)
    val mournLight = c(0xFF56715B)
    val mournStem = c(0xFF34453A)
    val mournWhite = c(0xFFE4E0D6)
    val mournGreen = c(0xFF4C7F68)
    val mournRed = c(0xFF9B4038)

    // Книга и пламък
    val page = c(0xFFF8F1E0)
    val pageEdge = c(0xFF7A5634)
    val cover = c(0xFF8E2A35)
    val flameGlow = Brush.radialGradient(listOf(c(Brand.Gold).copy(alpha = 0.4f), Color.Transparent), Offset(18f, 7f), 7.5f)
    val flame = Brush.verticalGradient(listOf(c(0xFFD9431E), c(0xFFEE7A1E), c(0xFFF5A623)), startY = 1.6f, endY = 11.2f)
    val flameCore = c(0xFFFFE07A)

    // Сурвачка
    val bark = c(0xFFA0673A)
    val barkDark = c(0xFF5A3518)
    val popcorn = c(0xFFFBF3DD)
    val popcornEdge = c(0xFFB89A6A)
    val fruitOrange = c(0xFFE0842E)
    val fruitPlum = c(0xFF5E2A4E)
    val threadGreen = c(0xFF2F8F5B)
    val threadYellow = c(0xFFE8B923)
    val shine = Color.White.copy(alpha = 0.45f)

    // Кръст и вода
    val crossGlow = Brush.radialGradient(listOf(c(Brand.Gold).copy(alpha = 0.35f), Color.Transparent), Offset(16f, 9f), 8.5f)
    val water1 = c(0xFF5B9BD5)
    val water2 = c(0xFF3D7FBF)
    val water3 = c(0xFF2F6AA3)

    // Лоза
    val cane = c(0xFF7A5234)
    val vineLeaf = Brush.verticalGradient(listOf(c(0xFF5C9A3E), c(0xFF2F6B2E)), startY = 5f, endY = 20f)
    val vineEdge = c(0xFF1F4D22)
    val tendril = c(0xFF4E8A3A)
    val grape = c(0xFF6A3D8C)
    val grapeDark = c(0xFF3E2056)

    // Мартеница
    val cordEdge = c(0xFF9C8F7A).copy(alpha = 0.7f)

    // Венче от цветя (пастелни, но достатъчно наситени за светлия фон)
    val springLeaf = c(0xFF5FA05A)
    val springLeafLight = c(0xFF8CC06A)
    val flowerCenter = c(0xFFF2A93B)
    val petalEdge = Color(0x40000000)
    val petals = arrayOf(c(0xFFE27BA0), c(0xFFA98BD9), c(0xFFF0C23B), c(0xFFF8F4EC), c(0xFFF09A6B), c(0xFF7DB4E0), c(0xFFE27BA0))

    // Върба
    val twig = c(0xFF8A4B2E)
    val catkin = c(0xFFE9EBE3)
    val catkinEdge = c(0xFF8E9C8C)
    val willowLeaf = c(0xFF8FAE8A)

    // Билки
    val herbA = c(0xFF4F8A3C)
    val herbB = c(0xFF7BA44A)
    val herbC = c(0xFF6E9A7A)
    val herbYellow = c(0xFFF2C230)
    val herbPurple = c(0xFF8E5BB0)

    // Риба и чиния
    val fishBody = Brush.verticalGradient(listOf(c(0xFF3F6E96), c(0xFF8FB0C8), c(0xFFC9D7E2)), startY = 5f, endY = 19f)
    val fishEdge = c(0xFF2B4A63)
    val fishFin = c(0xFF2F5878)
    val fishEye = c(0xFF1A2633)
    val plate = c(0xFFF1E8D6)
    val plateEdge = c(0xFF9C7A4A)
    val shadow = Color(0x1F000000)
}

private val FestiveInk by lazy { OrnamentInk(quiet = false) }
private val QuietInk by lazy { OrnamentInk(quiet = true) }

// ——— Дебелини (в единици на мрежата ≈ dp) ———

private val Hair = Stroke(width = 0.45f)
private val Fine = Stroke(width = 0.6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
private val StemStroke = Stroke(width = 0.8f, cap = StrokeCap.Round)
private val WaveStroke = Stroke(width = 1.3f, cap = StrokeCap.Round)
private val BarkOuter = Stroke(width = 2.3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
private val BarkInner = Stroke(width = 1.3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
private val TwigStroke = Stroke(width = 1f, cap = StrokeCap.Round)
private val CordOuter = Stroke(width = 2.4f, cap = StrokeCap.Round)
private val CordInner = Stroke(width = 1.8f, cap = StrokeCap.Round)

// ——— Листа и венци ———

/** Лист с дръжка в (0, 0) и връх в (0, -1). */
private val LeafShape by lazy { Path().apply {
    moveTo(0f, 0f)
    quadraticTo(0.6f, -0.45f, 0f, -1f)
    quadraticTo(-0.6f, -0.45f, 0f, 0f)
    close()
} }

/** Посоката (в градуси, 0 = нагоре, по часовниковата стрелка) на вектора (dx, dy). */
private fun headingOf(dx: Float, dy: Float): Float = atan2(dx, -dy) * DEG

/** Лист с дръжка в (x, y), дължина [len], относителна ширина [wk], насочен към [degrees]. */
private fun DrawScope.leaf(x: Float, y: Float, len: Float, wk: Float, degrees: Float, color: Color) {
    withTransform({
        translate(x, y)
        rotate(degrees, Offset.Zero)
        scale(len * wk, len, Offset.Zero)
    }) {
        drawPath(LeafShape, color)
    }
}

/**
 * Листа по дъга от [from] до [to] градуса (по часовниковата стрелка; 90 = долу),
 * с върховете напред по дъгата: навън [outer], навътре [inner]. При [pairs] — по
 * двойка на всяка стъпка, иначе редуващи се.
 */
private fun DrawScope.leafArc(
    cx: Float,
    cy: Float,
    r: Float,
    from: Float,
    to: Float,
    count: Int,
    len: Float,
    wk: Float,
    tilt: Float,
    outer: Color,
    inner: Color,
    pairs: Boolean = true,
) {
    for (i in 0 until count) {
        val a = (from + (to - from) * i / count) * RAD
        val s = sin(a)
        val c = cos(a)
        val along = headingOf(-s, c)
        if (pairs || i % 2 == 0) leaf(cx + r * c, cy + r * s, len, wk, along - tilt, outer)
        if (pairs || i % 2 == 1) leaf(cx + r * c, cy + r * s, len, wk, along + tilt, inner)
    }
}

/** Листо на върха на клонка, завършваща при ъгъл [at] от дъгата. */
private fun DrawScope.tipLeaf(cx: Float, cy: Float, r: Float, at: Float, len: Float, wk: Float, color: Color) {
    val a = at * RAD
    leaf(cx + r * cos(a), cy + r * sin(a), len, wk, headingOf(-sin(a), cos(a)), color)
}

/** Стъблото на венеца — дъга. */
private fun DrawScope.stemArc(cx: Float, cy: Float, r: Float, from: Float, to: Float, color: Color) {
    drawArc(color, from, to - from, false, Offset(cx - r, cy - r), Size(2 * r, 2 * r), style = StemStroke)
}

// ——— Слънце ———

/** Дълъг лъч, завършващ с ромбче (шевица); насочен нагоре от центъра. */
private val SunLongRay by lazy { Path().apply {
    moveTo(-1.1f, -6.2f); lineTo(0f, -9.8f); lineTo(1.1f, -6.2f); close()
    moveTo(0f, -12.75f); lineTo(0.95f, -11.5f); lineTo(0f, -10.25f); lineTo(-0.95f, -11.5f); close()
} }

private val SunShortRay by lazy { Path().apply {
    moveTo(-1.3f, -6.2f); lineTo(0f, -9.2f); lineTo(1.3f, -6.2f); close()
} }

/** Назъбен кръг (кривулица) върху диска. */
private val SunRing by lazy { Path().apply {
    for (i in 0..24) {
        val a = (i * 15f - 90f) * RAD
        val r = if (i % 2 == 0) 4.7f else 3.4f
        if (i == 0) moveTo(r * cos(a), r * sin(a)) else lineTo(r * cos(a), r * sin(a))
    }
    close()
} }

/** Слънце: диск с назъбен червен кръг и лъчи, които завършват с ромбчета. */
private fun DrawScope.drawSun(ink: OrnamentInk) {
    translate(15f, 14f) {
        for (i in 0 until 12) {
            rotate(i * 30f, Offset.Zero) {
                if (i % 2 == 0) drawPath(SunLongRay, ink.red) else drawPath(SunShortRay, ink.ray)
            }
        }
        drawCircle(ink.sunDisc, 6.6f, Offset.Zero)
        drawCircle(ink.rust, 6.6f, Offset.Zero, style = Fine)
        drawPath(SunRing, ink.red, style = Hair)
        drawCircle(ink.red, 1.5f, Offset.Zero)
    }
}

// ——— Трикольор ———

/** Ивица от развятото знаме: горният ръб е вълна, долният — същата вълна по-долу. */
private fun flagBand(top: Float, h: Float): Path = Path().apply {
    moveTo(6.2f, top)
    cubicTo(14f, top - 2.6f, 25f, top + 2.8f, 36f, top - 0.6f)
    lineTo(36f, top - 0.6f + h)
    cubicTo(25f, top + 2.8f + h, 14f, top - 2.6f + h, 6.2f, top + h)
    close()
}

private const val FLAG_TOP = 4.6f
private const val FLAG_STRIPE = 4.6f
private val FlagWhite by lazy { flagBand(FLAG_TOP, FLAG_STRIPE) }
private val FlagGreen by lazy { flagBand(FLAG_TOP + FLAG_STRIPE, FLAG_STRIPE) }
private val FlagRed by lazy { flagBand(FLAG_TOP + 2 * FLAG_STRIPE, FLAG_STRIPE) }
private val FlagWhole by lazy { flagBand(FLAG_TOP, 3 * FLAG_STRIPE) }

/** Развят трикольор на тънък прът; бялата ивица е с лек контур (за светлия фон). */
private fun DrawScope.drawTricolor(ink: OrnamentInk) {
    drawPath(FlagWhite, ink.flagWhite)
    drawPath(FlagGreen, ink.flagGreen)
    drawPath(FlagRed, ink.flagRed)
    drawPath(FlagWhole, ink.flagShade)
    drawPath(FlagWhole, ink.flagEdge, style = Hair)
    drawLine(ink.pole, Offset(5.5f, 3f), Offset(5.5f, 27f), strokeWidth = 1.3f, cap = StrokeCap.Round)
    drawCircle(ink.gold, 1.4f, Offset(5.5f, 2.2f))
}

// ——— Венци ———

/** Панделка: два клупа и две опашки около възела (0, 0). */
private val Bow by lazy { Path().apply {
    moveTo(0f, 0f); lineTo(-3.2f, -1.9f); lineTo(-3.2f, 1.9f); close()
    moveTo(0f, 0f); lineTo(3.2f, -1.9f); lineTo(3.2f, 1.9f); close()
    moveTo(-0.4f, 0.2f); lineTo(-2.2f, 4.6f); lineTo(-1f, 4.2f); lineTo(0.3f, 0.6f); close()
    moveTo(0.4f, 0.2f); lineTo(2.2f, 4.6f); lineTo(1f, 4.2f); lineTo(-0.3f, 0.6f); close()
} }

/** Зелен венец (Гергьовден) с червена панделка отдолу. */
private fun DrawScope.drawGreenWreath(ink: OrnamentInk) {
    stemArc(15f, 13.5f, 8.5f, 0f, 360f, ink.stemGreen)
    leafArc(15f, 13.5f, 8.5f, 0f, 360f, 14, 4.8f, 0.55f, 40f, ink.leafGreen, ink.leafLight)
    translate(15f, 22.2f) {
        drawPath(Bow, ink.red)
        drawCircle(ink.redDark, 0.9f, Offset.Zero)
    }
}

/** Лавров венец: две клонки, извити една към друга, отворени отгоре. */
private fun DrawScope.drawLaurelWreath(ink: OrnamentInk) {
    val cx = 17f
    for (side in 0..1) {
        scale(if (side == 0) 1f else -1f, 1f, Offset(cx, 0f)) {
            stemArc(cx, 13.5f, 10f, 112f, 238f, ink.goldDark)
            // Стъблата се кръстосват отдолу.
            drawLine(ink.goldDark, Offset(13.25f, 22.8f), Offset(19f, 25.6f), strokeWidth = 0.8f, cap = StrokeCap.Round)
            leafArc(cx, 13.5f, 10f, 112f, 238f, 7, 5.2f, 0.5f, 32f, ink.laurel, ink.gold)
            tipLeaf(cx, 13.5f, 10f, 238f, 5f, 0.5f, ink.laurel)
        }
    }
    drawCircle(ink.ribbon, 1.1f, Offset(cx, 24.2f))
}

/** Тих паметен венец: тъмнозелен лавър и малка кокарда с трикольорни ленти. */
private fun DrawScope.drawMemorialWreath(ink: OrnamentInk) {
    val cx = 15f
    for (side in 0..1) {
        scale(if (side == 0) 1f else -1f, 1f, Offset(cx, 0f)) {
            stemArc(cx, 13f, 9f, 100f, 262f, ink.mournStem)
            leafArc(cx, 13f, 9f, 100f, 262f, 9, 4.4f, 0.5f, 34f, ink.mourn, ink.mournLight)
            tipLeaf(cx, 13f, 9f, 262f, 4.2f, 0.5f, ink.mourn)
            // Лента: три успоредни ивици (бяло, зелено, червено) надолу и навън.
            drawLine(ink.mournWhite, Offset(cx - 0.64f, 22.6f - 0.39f), Offset(cx - 3.64f, 27.4f - 0.39f), strokeWidth = 0.8f)
            drawLine(ink.mournGreen, Offset(cx, 22.6f), Offset(cx - 3f, 27.4f), strokeWidth = 0.8f)
            drawLine(ink.mournRed, Offset(cx + 0.64f, 22.6f + 0.39f), Offset(cx - 2.36f, 27.4f + 0.39f), strokeWidth = 0.8f)
        }
    }
    // Кокарда.
    drawCircle(ink.mournRed, 1.7f, Offset(cx, 22.4f))
    drawCircle(ink.mournGreen, 1.15f, Offset(cx, 22.4f))
    drawCircle(ink.mournWhite, 0.6f, Offset(cx, 22.4f))
}

// ——— Пита ———

/** Обредна пита отгоре: плетен ръб, кръст от тесто и кръгчета в четирите полета. */
private fun DrawScope.drawRitualBread(ink: OrnamentInk) {
    translate(15f, 14f) {
        drawCircle(ink.crust, 12.4f, Offset.Zero)
        drawCircle(ink.crustEdge, 12.4f, Offset.Zero, style = Fine)
        // Плетеница по ръба — наклонени „зърна“ тесто.
        for (i in 0 until 18) {
            rotate(i * 20f, Offset.Zero) {
                rotate(28f, Offset(0f, -10.2f)) {
                    drawOval(ink.dough, Offset(-1.7f, -11.1f), Size(3.4f, 1.8f))
                }
            }
        }
        // Кръст: тъмен контур, после светло тесто.
        drawLine(ink.doughShade, Offset(-7f, 0f), Offset(7f, 0f), strokeWidth = 2.8f, cap = StrokeCap.Round)
        drawLine(ink.doughShade, Offset(0f, -7f), Offset(0f, 7f), strokeWidth = 2.8f, cap = StrokeCap.Round)
        drawLine(ink.dough, Offset(-7f, 0f), Offset(7f, 0f), strokeWidth = 1.9f, cap = StrokeCap.Round)
        drawLine(ink.dough, Offset(0f, -7f), Offset(0f, 7f), strokeWidth = 1.9f, cap = StrokeCap.Round)
        for (q in 0 until 4) {
            val p = Offset(if (q % 2 == 0) 4.4f else -4.4f, if (q < 2) 4.4f else -4.4f)
            drawCircle(ink.doughShade, 1.9f, p, style = BarkInner)
            drawCircle(ink.dough, 1.9f, p, style = Fine)
            drawCircle(ink.doughShade, 0.5f, p)
        }
        drawCircle(ink.doughShade, 2.2f, Offset.Zero)
        drawCircle(ink.dough, 1.7f, Offset.Zero)
        drawCircle(ink.doughShade, 0.6f, Offset.Zero)
    }
}

// ——— Коледна звезда ———

/** Осмолъчна звезда: четири дълги и четири по-къси лъча. */
private val StarShape by lazy { Path().apply {
    for (k in 0 until 16) {
        val a = (-90f + k * 22.5f) * RAD
        val r = when {
            k % 4 == 0 -> 12f
            k % 2 == 0 -> 7.5f
            else -> 3.4f
        }
        if (k == 0) moveTo(15f + r * cos(a), 14f + r * sin(a)) else lineTo(15f + r * cos(a), 14f + r * sin(a))
    }
    close()
} }

/** Ребрата на лъчите — от центъра до върховете. */
private val StarFacets by lazy { Path().apply {
    for (k in 0 until 8) {
        val a = (-90f + k * 45f) * RAD
        val r = if (k % 2 == 0) 12f else 7.5f
        moveTo(15f, 14f)
        lineTo(15f + r * cos(a), 14f + r * sin(a))
    }
} }

/** Златна осмолъчна звезда с меко сияние. */
private fun DrawScope.drawChristmasStar(ink: OrnamentInk) {
    drawCircle(ink.starGlow, 13.5f, Offset(15f, 14f))
    drawPath(StarShape, ink.starFill)
    drawPath(StarFacets, ink.goldDark.copy(alpha = 0.45f), style = Hair)
    drawPath(StarShape, ink.goldDark, style = Fine)
    drawCircle(ink.white.copy(alpha = 0.9f), 1.2f, Offset(15f, 14f))
}

// ——— Книга и пламък ———

/** Лявата страница (дясната е огледална). */
private val BookPage by lazy { Path().apply {
    moveTo(18f, 12.5f)
    cubicTo(14.5f, 10.2f, 9f, 10f, 4.5f, 11.5f)
    lineTo(4.5f, 23f)
    cubicTo(9f, 21.8f, 14.5f, 22.2f, 18f, 24.5f)
    close()
} }

private val BookCover by lazy { Path().apply {
    moveTo(18f, 14f)
    cubicTo(14.5f, 12f, 9f, 11.8f, 3f, 13f)
    lineTo(3f, 24.6f)
    cubicTo(9f, 23.4f, 14.5f, 23.8f, 18f, 26.2f)
    close()
} }

/** Дебелината на листата и редовете „текст“. */
private val BookLines by lazy { Path().apply {
    moveTo(4.5f, 23.8f)
    cubicTo(9f, 22.6f, 14.5f, 23f, 18f, 25.3f)
    for (k in 0 until 3) {
        val d = 2.6f * k
        moveTo(6.5f, 14f + d)
        cubicTo(9.5f, 13.2f + d, 13.5f, 13.4f + d, 16.3f, 15f + d)
    }
} }

private val FlameOuter by lazy { Path().apply {
    moveTo(18f, 11.2f)
    cubicTo(14.6f, 11.2f, 13.6f, 8.2f, 15.2f, 5.8f)
    cubicTo(16f, 4.6f, 16.6f, 3.6f, 17.2f, 1.6f)
    cubicTo(19.4f, 3.6f, 22.6f, 6.6f, 21.4f, 9f)
    cubicTo(20.8f, 10.4f, 19.6f, 11.2f, 18f, 11.2f)
    close()
} }

private val FlameInner by lazy { Path().apply {
    moveTo(18f, 10.6f)
    cubicTo(16.4f, 10.6f, 16f, 9f, 16.8f, 7.6f)
    lineTo(18f, 5.2f)
    cubicTo(19.2f, 7f, 20f, 8.4f, 19.4f, 9.6f)
    cubicTo(19.1f, 10.2f, 18.6f, 10.6f, 18f, 10.6f)
    close()
} }

/** Отворена книга с малко пламъче над гръбчето (будителите). */
private fun DrawScope.drawBookFlame(ink: OrnamentInk) {
    for (side in 0..1) {
        scale(if (side == 0) 1f else -1f, 1f, Offset(18f, 0f)) {
            drawPath(BookCover, ink.cover)
            drawPath(BookPage, ink.page)
            drawPath(BookPage, ink.pageEdge, style = Fine)
            drawPath(BookLines, ink.pageEdge.copy(alpha = 0.5f), style = Hair)
        }
    }
    drawCircle(ink.flameGlow, 7.5f, Offset(18f, 7f))
    drawPath(FlameOuter, ink.flame)
    drawPath(FlameInner, ink.flameCore)
}

// ——— Сурвачка ———

/** Дренова клонка: дръжка, извита в сърце, и права пръчица по средата. */
private val SurvachkaWood by lazy { Path().apply {
    moveTo(15f, 27f); lineTo(15f, 17.5f)
    cubicTo(6f, 12.5f, 4f, 3f, 10.5f, 3f)
    cubicTo(13.5f, 3f, 15f, 5.5f, 15f, 7.5f)
    moveTo(15f, 17.5f)
    cubicTo(24f, 12.5f, 26f, 3f, 19.5f, 3f)
    cubicTo(16.5f, 3f, 15f, 5.5f, 15f, 7.5f)
    lineTo(15f, 17.5f)
} }

/** Пуканка — няколко слепени топчета с тънък контур. */
private fun DrawScope.popcorn(x: Float, y: Float, ink: OrnamentInk) {
    drawCircle(ink.popcornEdge, 1.2f, Offset(x - 0.4f, y))
    drawCircle(ink.popcornEdge, 1.05f, Offset(x + 0.5f, y - 0.3f))
    drawCircle(ink.popcorn, 0.95f, Offset(x - 0.4f, y))
    drawCircle(ink.popcorn, 0.8f, Offset(x + 0.5f, y - 0.3f))
}

/** Сушен плод с отблясък. */
private fun DrawScope.fruit(x: Float, y: Float, color: Color, ink: OrnamentInk) {
    drawCircle(color, 1.25f, Offset(x, y))
    drawCircle(ink.shine, 0.4f, Offset(x - 0.4f, y - 0.4f))
}

/** Конец, увит напряко на клонката. */
private fun DrawScope.thread(x: Float, y: Float, color: Color) {
    drawLine(color, Offset(x - 0.3f, y - 1.4f), Offset(x + 0.3f, y + 1.4f), strokeWidth = 1f, cap = StrokeCap.Round)
}

/** Сурвачка: огъната дренова клонка с пуканки, сушени плодове и червено-бяла прежда. */
private fun DrawScope.drawSurvachka(ink: OrnamentInk) {
    drawPath(SurvachkaWood, ink.barkDark, style = BarkOuter)
    drawPath(SurvachkaWood, ink.bark, style = BarkInner)
    // Червено-бяла прежда по дръжката.
    var y = 19f
    var i = 0
    while (y < 26.5f) {
        drawLine(if (i % 2 == 0) ink.red else ink.white, Offset(13.9f, y + 0.6f), Offset(16.1f, y - 0.6f), strokeWidth = 0.9f)
        y += 1.3f
        i++
    }
    thread(10.5f, 3f, ink.threadGreen)
    thread(19.5f, 3f, ink.threadYellow)
    popcorn(9.6f, 12.7f, ink)
    fruit(6.9f, 8.1f, ink.red, ink)
    popcorn(7.3f, 4.6f, ink)
    fruit(13.8f, 4.5f, ink.fruitOrange, ink)
    fruit(20.4f, 12.7f, ink.fruitOrange, ink)
    popcorn(23.2f, 8.1f, ink)
    fruit(22.8f, 4.5f, ink.fruitPlum, ink)
    popcorn(16.4f, 4.4f, ink)
    // Розетка от прежда по средата.
    drawCircle(ink.red, 1.4f, Offset(15f, 11.5f))
    drawCircle(ink.white, 0.6f, Offset(15f, 11.5f))
}

// ——— Кръст и вода ———

/** Една вълна (4 периода) от x = 3 при y = 0. */
private val Wave by lazy { Path().apply {
    moveTo(3f, 0f)
    var x = 3f
    repeat(4) {
        quadraticTo(x + 1.625f, -1.4f, x + 3.25f, 0f)
        quadraticTo(x + 4.875f, 1.4f, x + 6.5f, 0f)
        x += 6.5f
    }
} }

/** Православен кръст (златен, с тъмен кант) над стилизирани вълни (Йордановден). */
private fun DrawScope.drawCrossWater(ink: OrnamentInk) {
    drawCircle(ink.crossGlow, 8.5f, Offset(16f, 9f))
    for (pass in 0..1) {
        val col = if (pass == 0) ink.goldDark else ink.gold
        val extra = if (pass == 0) 0.9f else 0f
        drawLine(col, Offset(16f, 2f), Offset(16f, 17.5f), strokeWidth = 1.7f + extra, cap = StrokeCap.Square)
        drawLine(col, Offset(14.4f, 4.4f), Offset(17.6f, 4.4f), strokeWidth = 1.1f + extra, cap = StrokeCap.Square)
        drawLine(col, Offset(11.4f, 7.7f), Offset(20.6f, 7.7f), strokeWidth = 1.5f + extra, cap = StrokeCap.Square)
        drawLine(col, Offset(13.7f, 12.6f), Offset(18.3f, 14.4f), strokeWidth = 1.1f + extra, cap = StrokeCap.Square)
    }
    translate(0f, 19.6f) { drawPath(Wave, ink.water1, style = WaveStroke) }
    translate(-1.6f, 22.8f) { drawPath(Wave, ink.water2, style = WaveStroke) }
    translate(0f, 26f) { drawPath(Wave, ink.water3, style = WaveStroke) }
}

// ——— Лоза ———

/** Петделен лозов лист, увиснал от лозата (дръжката е горе). */
private val VineLeafShape by lazy { lobedLeaf(cx = 11f, cy = 12f, r = 13f) }

private val VineAngles = floatArrayOf(270f, 330f, 0f, 30f, 60f, 90f, 120f, 150f, 180f, 210f)
private val VineRadii = floatArrayOf(0.12f, 0.48f, 0.3f, 0.5f, 0.3f, 0.55f, 0.3f, 0.5f, 0.3f, 0.48f)

private fun lobedLeaf(cx: Float, cy: Float, r: Float): Path = Path().apply {
    fun px(i: Int) = cx + r * VineRadii[i] * cos(VineAngles[i] * RAD)
    fun py(i: Int) = cy + r * VineRadii[i] * sin(VineAngles[i] * RAD)
    moveTo(px(0), py(0))
    for (step in 1..VineAngles.size) {
        val i = step % VineAngles.size
        val prev = step - 1
        val a0 = VineAngles[prev]
        var a1 = VineAngles[i]
        if (a1 < a0) a1 += 360f
        // Заобленост: контролната точка е на средния ъгъл, близо до радиуса на по-големия край.
        val mid = (a0 + a1) / 2f * RAD
        val cr = r * maxOf(VineRadii[prev], VineRadii[i]) * 0.92f
        quadraticTo(cx + cr * cos(mid), cy + cr * sin(mid), px(i), py(i))
    }
    close()
}

/** Жилките — от дръжката към петте дяла. */
private val VineVeins by lazy { Path().apply {
    val sx = 11f
    val sy = 12f - 13f * 0.12f
    for (a in floatArrayOf(330f, 30f, 90f, 150f, 210f)) {
        moveTo(sx, sy)
        lineTo(11f + 13f * 0.4f * cos(a * RAD), 12f + 13f * 0.4f * sin(a * RAD))
    }
} }

private val VineCane by lazy { Path().apply {
    moveTo(2f, 7f)
    quadraticTo(18f, 1f, 34f, 6f)
} }

private val VineTendril by lazy { Path().apply {
    moveTo(30.5f, 4.6f)
    cubicTo(32.5f, 1.5f, 35.5f, 3.2f, 34.2f, 5.2f)
    cubicTo(33.4f, 6.2f, 32.2f, 5.4f, 32.8f, 4.4f)
} }

/** Зърната на гроздето — (x, y) по двойки, отгоре надолу. */
private val GrapeXY = floatArrayOf(
    20f, 9.6f, 23.6f, 9.6f, 27.2f, 9.6f, 30.8f, 9.6f,
    21.8f, 12.7f, 25.4f, 12.7f, 29f, 12.7f,
    23.6f, 15.8f, 27.2f, 15.8f,
    25.4f, 18.9f,
)

/** Лозов лист и чепка грозде на лозова пръчка (Трифон Зарезан). */
private fun DrawScope.drawVine(ink: OrnamentInk) {
    drawPath(VineCane, ink.cane, style = StemStroke)
    drawPath(VineTendril, ink.tendril, style = Fine)
    drawLine(ink.cane, Offset(11f, 4.6f), Offset(11f, 10.6f), strokeWidth = 0.7f)
    drawLine(ink.cane, Offset(25.4f, 4.3f), Offset(25.4f, 8f), strokeWidth = 0.7f)
    drawPath(VineLeafShape, ink.vineLeaf)
    drawPath(VineVeins, ink.vineEdge.copy(alpha = 0.55f), style = Hair)
    drawPath(VineLeafShape, ink.vineEdge, style = Hair)
    var i = 0
    while (i < GrapeXY.size) {
        val p = Offset(GrapeXY[i], GrapeXY[i + 1])
        drawCircle(ink.grape, 1.9f, p)
        drawCircle(ink.grapeDark, 1.9f, p, style = Hair)
        drawCircle(ink.shine, 0.55f, Offset(p.x - 0.6f, p.y - 0.6f))
        i += 2
    }
}

// ——— Мартеница ———

/** Усуканият шнур — провиснала дъга. */
private val Cord by lazy { Path().apply {
    moveTo(1.5f, 4f)
    quadraticTo(16f, 9f, 30.5f, 4f)
} }

/** Червените наклонени „намотки“ по белия шнур. */
private val CordTwist by lazy {
    val stamp = Path().apply {
        moveTo(-0.9f, 0.9f); lineTo(0.1f, 0.9f); lineTo(0.9f, -0.9f); lineTo(-0.1f, -0.9f); close()
    }
    Stroke(width = 1f, pathEffect = PathEffect.stampedPathEffect(stamp, 2.6f, 0f, StampedPathEffectStyle.Rotate))
}

private val PendaSkirt by lazy { Path().apply {
    moveTo(20.6f, 18f); lineTo(24.4f, 18f); lineTo(26.3f, 25.4f); lineTo(18.7f, 25.4f); close()
} }

/** Кукличка от прежда: Пижо (с крачета) или Пенда (с поличка). */
private fun DrawScope.drawDoll(x: Float, body: Color, accent: Color, edge: Color, skirt: Boolean, ink: OrnamentInk) {
    drawLine(ink.red, Offset(x, 6f), Offset(x, 8.8f), strokeWidth = 0.5f)
    // Ръце — хоризонтален сноп с маншети.
    drawRoundRect(body, Offset(x - 3.9f, 12.9f), Size(7.8f, 1.5f), CornerRadius(0.75f))
    drawRoundRect(edge, Offset(x - 3.9f, 12.9f), Size(7.8f, 1.5f), CornerRadius(0.75f), style = Hair)
    drawLine(accent, Offset(x - 3.2f, 12.95f), Offset(x - 3.2f, 14.35f), strokeWidth = 0.6f)
    drawLine(accent, Offset(x + 3.2f, 12.95f), Offset(x + 3.2f, 14.35f), strokeWidth = 0.6f)
    // Тяло.
    drawRect(body, Offset(x - 1.9f, 12.2f), Size(3.8f, 6.2f))
    drawRect(edge, Offset(x - 1.9f, 12.2f), Size(3.8f, 6.2f), style = Hair)
    if (skirt) {
        drawPath(PendaSkirt, body)
        drawPath(PendaSkirt, edge, style = Hair)
        drawLine(accent, Offset(19.1f, 24.3f), Offset(25.9f, 24.3f), strokeWidth = 0.7f)
    } else {
        drawRect(body, Offset(x - 1.9f, 18.2f), Size(1.6f, 6.6f))
        drawRect(body, Offset(x + 0.3f, 18.2f), Size(1.6f, 6.6f))
        drawRect(edge, Offset(x - 1.9f, 18.2f), Size(1.6f, 6.6f), style = Hair)
        drawRect(edge, Offset(x + 0.3f, 18.2f), Size(1.6f, 6.6f), style = Hair)
        drawLine(accent, Offset(x - 1.9f, 24f), Offset(x - 0.3f, 24f), strokeWidth = 0.7f)
        drawLine(accent, Offset(x + 0.3f, 24f), Offset(x + 1.9f, 24f), strokeWidth = 0.7f)
    }
    // Нишките на преждата.
    drawLine(edge.copy(alpha = 0.35f), Offset(x - 0.7f, 12.6f), Offset(x - 0.7f, 16.8f), strokeWidth = 0.3f)
    drawLine(edge.copy(alpha = 0.35f), Offset(x + 0.7f, 12.6f), Offset(x + 0.7f, 16.8f), strokeWidth = 0.3f)
    // Колан.
    drawRect(accent, Offset(x - 2.1f, 17f), Size(4.2f, 1.2f))
    // Глава.
    drawCircle(body, 2f, Offset(x, 10.7f))
    drawCircle(edge, 2f, Offset(x, 10.7f), style = Hair)
}

/** Мартеница: Пижо (бял) и Пенда (червена) на усукан червено-бял шнур с пискюлчета. */
private fun DrawScope.drawMartenitsa(ink: OrnamentInk) {
    drawPath(Cord, ink.cordEdge, style = CordOuter)
    drawPath(Cord, ink.white, style = CordInner)
    drawPath(Cord, ink.red, style = CordTwist)
    for (side in 0..1) {
        val x = if (side == 0) 1.5f else 30.5f
        drawLine(ink.red, Offset(x, 4f), Offset(x - 0.7f, 7.6f), strokeWidth = 0.6f, cap = StrokeCap.Round)
        drawLine(ink.whiteEdge, Offset(x, 4f), Offset(x + 0.7f, 7.6f), strokeWidth = 0.6f, cap = StrokeCap.Round)
        drawCircle(ink.red, 0.9f, Offset(x, 4f))
    }
    drawDoll(9.5f, ink.white, ink.red, ink.whiteEdge, skirt = false, ink = ink)
    drawDoll(22.5f, ink.red, ink.white, ink.redDark, skirt = true, ink = ink)
}

// ——— Пролетни венци ———

/** Петте венчелистчета на цвете — отместванията им (dx, dy) от центъра. */
private val PetalOffsets by lazy {
    FloatArray(10).also { o ->
        for (k in 0 until 5) {
            val a = (-90f + 72f * k) * RAD
            o[2 * k] = 1.35f * cos(a)
            o[2 * k + 1] = 1.35f * sin(a)
        }
    }
}

private fun DrawScope.flower(x: Float, y: Float, petal: Color, ink: OrnamentInk) {
    val o = PetalOffsets
    for (k in 0 until 5) drawCircle(ink.petalEdge, 1.55f, Offset(x + o[2 * k], y + o[2 * k + 1]))
    for (k in 0 until 5) drawCircle(petal, 1.2f, Offset(x + o[2 * k], y + o[2 * k + 1]))
    drawCircle(ink.flowerCenter, 0.85f, Offset(x, y))
}

/** Венче от пролетни цветя (Лазаровден): зелени листенца и седем пастелни цветчета. */
private fun DrawScope.drawFlowerWreath(ink: OrnamentInk) {
    stemArc(15f, 13.5f, 8.5f, 0f, 360f, ink.stemGreen)
    leafArc(15f, 13.5f, 8.5f, 0f, 360f, 14, 4f, 0.55f, 45f, ink.springLeaf, ink.springLeafLight)
    for (i in 0 until 7) {
        val a = (-90f + i * 360f / 7f) * RAD
        flower(15f + 8.5f * cos(a), 13.5f + 8.5f * sin(a), ink.petals[i], ink)
    }
}

/** Венец от билки (Еньовден): тесни листа в различни зелени и жълти и лилави цветчета. */
private fun DrawScope.drawHerbWreath(ink: OrnamentInk) {
    stemArc(15f, 13.5f, 8.5f, 0f, 360f, ink.stemGreen)
    leafArc(15f, 13.5f, 8.5f, 0f, 360f, 16, 5f, 0.42f, 40f, ink.herbA, ink.herbB)
    leafArc(15f, 13.5f, 8.5f, 11f, 371f, 12, 3.6f, 0.7f, 70f, ink.herbC, ink.herbC, pairs = false)
    for (i in 0 until 6) {
        // Жълт кантарион навън, лилава мащерка навътре — редуват се.
        val a = (-60f + i * 60f) * RAD
        val c = cos(a)
        val s = sin(a)
        val y0 = 13.5f + 10.6f * s
        val x0 = 15f + 10.6f * c
        drawCircle(ink.herbYellow, 1.05f, Offset(x0, y0))
        drawCircle(ink.flowerCenter, 0.4f, Offset(x0, y0))
        val b = a + 30f * RAD
        val x1 = 15f + 6.6f * cos(b)
        val y1 = 13.5f + 6.6f * sin(b)
        drawCircle(ink.herbPurple, 0.6f, Offset(x1, y1 - 0.6f))
        drawCircle(ink.herbPurple, 0.6f, Offset(x1 - 0.55f, y1 + 0.35f))
        drawCircle(ink.herbPurple, 0.6f, Offset(x1 + 0.55f, y1 + 0.35f))
    }
}

// ——— Върба ———

/** Трите клонки — квадратични криви (P0, P1, P2) от общото снопче. */
private val WillowTwigs = arrayOf(
    floatArrayOf(15f, 27f, 13.5f, 15f, 6.5f, 3.5f),
    floatArrayOf(15f, 27f, 15.6f, 14f, 15.8f, 1.8f),
    floatArrayOf(15f, 27f, 17.5f, 15f, 24f, 4f),
)

private val WillowPath by lazy { Path().apply {
    for (t in WillowTwigs) {
        moveTo(t[0], t[1])
        quadraticTo(t[2], t[3], t[4], t[5])
    }
} }

/** Мъхестите реси — (x, y, посока) по тройки, редуващи се от двете страни на клонката. */
private val Catkins by lazy {
    val ts = floatArrayOf(0.42f, 0.56f, 0.7f, 0.84f, 1f)
    val out = FloatArray(WillowTwigs.size * ts.size * 3)
    var n = 0
    for ((j, p) in WillowTwigs.withIndex()) {
        for ((i, t) in ts.withIndex()) {
            val u = 1f - t
            val x = u * u * p[0] + 2 * u * t * p[2] + t * t * p[4]
            val y = u * u * p[1] + 2 * u * t * p[3] + t * t * p[5]
            val dx = 2 * u * (p[2] - p[0]) + 2 * t * (p[4] - p[2])
            val dy = 2 * u * (p[3] - p[1]) + 2 * t * (p[5] - p[3])
            val side = when {
                t == 1f -> 0f
                (i + j) % 2 == 0 -> -32f
                else -> 32f
            }
            out[n++] = x
            out[n++] = y
            out[n++] = headingOf(dx, dy) + side
        }
    }
    out
}

/** Върбови клонки с мъхести реси (Цветница), вързани с червен конец. */
private fun DrawScope.drawWillow(ink: OrnamentInk) {
    leaf(14.6f, 23f, 4.6f, 0.45f, -42f, ink.willowLeaf)
    leaf(15.4f, 23.4f, 4.4f, 0.45f, 46f, ink.willowLeaf)
    drawPath(WillowPath, ink.twig, style = TwigStroke)
    val c = Catkins
    var i = 0
    while (i < c.size) {
        withTransform({
            translate(c[i], c[i + 1])
            rotate(c[i + 2], Offset.Zero)
        }) {
            drawOval(ink.catkin, Offset(-0.95f, -3.4f), Size(1.9f, 3.3f))
            drawOval(ink.catkinEdge, Offset(-0.95f, -3.4f), Size(1.9f, 3.3f), style = Hair)
        }
        i += 3
    }
    drawLine(ink.red, Offset(13.6f, 24.6f), Offset(16.4f, 24.6f), strokeWidth = 1.1f, cap = StrokeCap.Round)
}

// ——— Риба ———

private val FishBody by lazy { Path().apply {
    moveTo(5f, 12f)
    cubicTo(9f, 4.8f, 21f, 4.4f, 29.5f, 11.2f)
    lineTo(35.5f, 6.8f)
    lineTo(33.6f, 12f)
    lineTo(35.5f, 17.2f)
    lineTo(29.5f, 12.8f)
    cubicTo(21f, 19.6f, 9f, 19.2f, 5f, 12f)
    close()
} }

private val FishFins by lazy { Path().apply {
    moveTo(15f, 6.8f); quadraticTo(19.5f, 2.6f, 24.5f, 7.8f); close()
    moveTo(17f, 17.2f); quadraticTo(19.5f, 21f, 22.5f, 16.8f); close()
} }

/** Люспи (дъгички „)“), хриле и ивиците на опашката. */
private val FishDetails by lazy { Path().apply {
    for (col in 0 until 4) {
        val x = 15f + col * 3f
        val shift = if (col % 2 == 0) 0f else 1.2f
        for (row in 0 until 3) {
            val y = 9.6f + row * 2.4f + shift
            if (y > 15.5f) continue
            arcTo(Rect(x - 1.5f, y - 1.5f, x + 1.5f, y + 1.5f), -65f, 130f, true)
        }
    }
    arcTo(Rect(3f, 7f, 13f, 17f), -50f, 100f, true)
    moveTo(30f, 12f); lineTo(34.4f, 8.4f)
    moveTo(30.2f, 12f); lineTo(33.4f, 12f)
    moveTo(30f, 12f); lineTo(34.4f, 15.6f)
} }

/** Червена кривулица по предния ръб на чинията. */
private val PlateZigzag by lazy { Path().apply {
    var k = 0
    var deg = 15f
    while (deg <= 165.01f) {
        val a = deg * RAD
        val out = k % 2 == 0
        val x = 20f + (if (out) 18.1f else 16.4f) * cos(a)
        val y = 21.5f + (if (out) 5.05f else 3.95f) * sin(a)
        if (k == 0) moveTo(x, y) else lineTo(x, y)
        deg += 7.5f
        k++
    }
} }

/** Сребристосиня риба с люспи върху чиния с шевица (Никулден). */
private fun DrawScope.drawFish(ink: OrnamentInk) {
    drawOval(ink.plate, Offset(1f, 16.2f), Size(38f, 10.6f))
    drawOval(ink.plateEdge, Offset(1f, 16.2f), Size(38f, 10.6f), style = Fine)
    drawOval(ink.plateEdge, Offset(4.5f, 17.8f), Size(31f, 7.4f), style = Hair)
    drawPath(PlateZigzag, ink.red, style = Hair)
    drawOval(ink.shadow, Offset(7f, 16.6f), Size(24f, 3.6f))
    drawPath(FishFins, ink.fishFin)
    drawPath(FishBody, ink.fishBody)
    drawPath(FishDetails, ink.fishEdge.copy(alpha = 0.5f), style = Hair)
    drawPath(FishBody, ink.fishEdge, style = Fine)
    drawCircle(ink.white, 1.35f, Offset(9.6f, 10.6f))
    drawCircle(ink.fishEye, 0.7f, Offset(9.8f, 10.6f))
}
