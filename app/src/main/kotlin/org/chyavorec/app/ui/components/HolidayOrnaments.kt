package org.chyavorec.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.domain.occasion.OccasionOrnament

/*
 * Празничните орнаменти се рисуват в „чертожна“ мрежа с височина 28 единици и
 * ширина според орнамента; Canvas-ът я мащабира към реалния си размер, така че
 * 1 единица = 1 dp при номиналния размер (и дебелините на линиите следват density).
 * Пътищата са в единици на мрежата и се създават веднъж (by lazy).
 */

/** Височината на мрежата (= номиналната височина в dp). */
private const val ART_H = 28f

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
internal class OrnamentInk(private val quiet: Boolean) {
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
