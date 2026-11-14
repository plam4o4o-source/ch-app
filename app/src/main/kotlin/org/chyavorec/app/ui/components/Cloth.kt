package org.chyavorec.app.ui.components

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.LocalThemeStyle
import kotlin.math.roundToInt

/**
 * Тъканта — обща основа за двете „платове“ в приложението: ленената подвързия на
 * генерираните корици и нощната тъкан на тъмната тема. Една височинна карта (платно
 * бод: нишките се редуват отгоре/отдолу), от която се прави по една плочка 64 px на процес.
 */
internal object ClothWeave {
    /** Страната на плочката (px); кратна на [PITCH]·2, за да е безшевна. */
    const val TILE = 64
    const val PITCH = 4

    /** Най-светлата стойност на ленената плочка (0..255) — над 127 би изсветлявало с Overlay. */
    const val LINEN_MAX = 127

    /** Височината на нишката в точка (0 = процеп, 1 = гребен на нишката); периодична през [TILE]. */
    fun height(x: Int, y: Int): Float {
        val cx = x / PITCH
        val cy = y / PITCH
        val u = ((x % PITCH) + 0.5f) / PITCH
        val v = ((y % PITCH) + 0.5f) / PITCH
        // Основа (вертикална нишка) е отгоре при четна сума, вътък (хоризонтална) — при нечетна.
        val warpOver = (cx + cy) % 2 == 0
        val across = if (warpOver) u else v
        val along = if (warpOver) v else u
        val a = 2f * across - 1f
        val b = 2f * along - 1f
        val crown = 1f - 0.75f * a * a
        val dive = 1f - 0.3f * b * b * b * b
        // Едва забележима неравност на нишката (детерминирана).
        val noise = ((x * 73856093) xor (y * 19349663)).let { (it ushr 3) and 0xFF } / 255f
        return (crown * dive - 0.08f * noise).coerceIn(0f, 1f)
    }

    /**
     * Ленената плочка: непрозрачни сиви стойности 70..127 — Overlay с тях само затъмнява
     * (нищо не изсветлява), затова контрастът на текста върху корицата ([GeneratedCover.LIGHTEST_OVERLAY])
     * не пада. Строи се веднъж за процеса.
     */
    val linen: ImageBitmap by lazy {
        val px = IntArray(TILE * TILE)
        for (y in 0 until TILE) for (x in 0 until TILE) {
            val g = (0.275f + 0.225f * height(x, y)) * 255f
            val v = g.toInt().coerceIn(0, LINEN_MAX)
            px[y * TILE + x] = (0xFF shl 24) or (v shl 16) or (v shl 8) or v
        }
        Bitmap.createBitmap(px, TILE, TILE, Bitmap.Config.ARGB_8888).asImageBitmap()
    }

    /** Нощната плочка: златист плат, чиято плътност (алфа) следва височината — рисува се при ~4%. */
    val night: ImageBitmap by lazy {
        val size = TILE / 2
        val px = IntArray(size * size)
        for (y in 0 until size) for (x in 0 until size) {
            val a = (height(x, y) * 255f).roundToInt().coerceIn(0, 255)
            px[y * size + x] = (a shl 24) or NIGHT_THREAD
        }
        Bitmap.createBitmap(px, size, size, Bitmap.Config.ARGB_8888).asImageBitmap()
    }

    /** Топло злато (RGB) на нощната тъкан. */
    private const val NIGHT_THREAD = 0xE6C97A
}

/** Четката с повтаряща се ленена плочка — за корицата (за рисуване с BlendMode.Overlay). */
internal fun linenBrush(): ShaderBrush = ShaderBrush(ImageShader(ClothWeave.linen, TileMode.Repeated, TileMode.Repeated))

/** Плътността на нощната тъкан (дял от плочката) — „едва забележима“, 3–4%. */
internal const val NIGHT_CLOTH_ALPHA = 0.04f

/**
 * „Нощна черга“: едва забележима тъкана текстура върху картите в тъмната тема (не и върху
 * горната лента — там изглеждаше като залепена по-светла ивица с твърд ръб). Една плочка като [ImageShader] (без шейдър програма на нито една версия);
 * при „Черно (OLED)“ и в светла тема — нищо. [shape] е заоблянето на картата (за да не
 * излиза текстурата над ъглите).
 */
@Composable
fun Modifier.nightCloth(shape: Shape = RectangleShape): Modifier {
    if (!LocalExtendedColors.current.isDark || LocalThemeStyle.current.oledBlack) return this
    return this.then(nightClothDraw(shape))
}

private fun nightClothDraw(shape: Shape): Modifier = Modifier.drawWithCache {
    val brush = ShaderBrush(ImageShader(ClothWeave.night, TileMode.Repeated, TileMode.Repeated))
    val path = if (shape === RectangleShape) null else Path().apply { addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache)) }
    onDrawWithContent {
        drawContent()
        if (path == null) drawRect(brush, alpha = NIGHT_CLOTH_ALPHA) else drawPath(path, brush, alpha = NIGHT_CLOTH_ALPHA)
    }
}
