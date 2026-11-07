package org.chyavorec.app.ui.screens.my

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaShaders
import org.chyavorec.core.Diagnostics
import kotlin.math.hypot

/**
 * Холографското фолио на картата като чиста функция на наклона (−1..1 по двете
 * оси): тонът на дъгата, ивицата блясък, отблясъкът и релефът на текста. При
 * (0, 0) — спокойното положение (и при „намалено движение“, без сензор).
 */
internal object CardFoil {
    /** Силата на фолиото (плътност на дъгата). */
    const val STRENGTH = 0.13f

    /**
     * @property hue изместване на тона на тънкия филм (0..1).
     * @property sheen къде по диагонала е ивицата блясък (0 = горе вляво, 1 = долу вдясно).
     * @property glintX/glintY отблясъкът — дял от ширината/височината (0.05..0.95).
     * @property glintStrength яркостта на отблясъка — по-силен при по-голям наклон.
     * @property embossX/embossY отместването на светлия ръб на буквите (dp); тъмният е срещу него.
     */
    data class Shine(
        val hue: Float,
        val sheen: Float,
        val glintX: Float,
        val glintY: Float,
        val glintStrength: Float,
        val embossX: Float,
        val embossY: Float,
    )

    fun shine(tiltX: Float, tiltY: Float): Shine {
        val x = if (tiltX.isNaN()) 0f else tiltX.coerceIn(-1f, 1f)
        val y = if (tiltY.isNaN()) 0f else tiltY.coerceIn(-1f, 1f)
        val h = (0.08f + 0.22f * x + 0.14f * y) % 1f
        return Shine(
            hue = (if (h < 0f) h + 1f else h).let { if (it >= 1f) 0f else it },
            sheen = 0.5f + 0.38f * x - 0.18f * y,
            glintX = (0.72f + 0.28f * x).coerceIn(0.05f, 0.95f),
            glintY = (0.22f + 0.3f * y).coerceIn(0.05f, 0.95f),
            glintStrength = 0.22f + 0.18f * hypot(x, y).coerceAtMost(1f),
            // Светлината е горе вляво; при наклон надясно/надолу ръбът „тръгва“ натам.
            embossX = -0.7f - 0.6f * x,
            embossY = -0.7f - 0.6f * y,
        )
    }
}

/** Наклонът на картата, подаден на релефния текст вътре в нея. */
internal val LocalCardTilt = staticCompositionLocalOf<Tilt?> { null }

/**
 * Фолиото под съдържанието на картата: AGSL тънък филм (Android 13+) или
 * резервни градиенти. Смесва се със Screen — само изсветлява; баркодът е над
 * него на бял фон и остава контрастен. Наклонът се чете само при рисуване.
 */
@Composable
internal fun CardFoilLayer(tilt: Tilt, modifier: Modifier = Modifier) {
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val shader = remember(agsl) { if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) FoilShader.createOrNull() else null }
    val draw = Modifier.drawWithCache {
        val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.resize(size.width, size.height)
        onDrawBehind {
            val s = CardFoil.shine(tilt.x, tilt.y)
            if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.update(tilt.x, tilt.y, s)) {
                drawRect(shader.brush, blendMode = BlendMode.Screen)
            } else {
                drawFallbackFoil(s)
            }
        }
    }
    Spacer(modifier.clearAndSetSemantics { }.then(draw))
}

/** Пастелната дъга на резервното фолио (злато → роза → люляк → мента → небе → злато). */
private val FoilColors = listOf(
    Color(0xFFE8D5A3), Color(0xFFE8A6B8), Color(0xFFC6A8E2), Color(0xFFA6DCC8), Color(0xFFA8C6EA), Color(0xFFE8D5A3),
)

/** Без шейдър: дъга, която се плъзга с наклона, ивица блясък и мек отблясък. */
private fun DrawScope.drawFallbackFoil(s: CardFoil.Shine) {
    val w = size.width
    val h = size.height
    val shift = s.hue * w * 1.4f
    drawRect(
        Brush.linearGradient(FoilColors, start = Offset(shift - w * 0.7f, 0f), end = Offset(shift + w * 0.7f, h), tileMode = TileMode.Mirror),
        alpha = CardFoil.STRENGTH * 0.7f, blendMode = BlendMode.Screen,
    )
    val c = s.sheen.coerceIn(-0.2f, 1.2f)
    drawRect(
        Brush.linearGradient(
            colorStops = arrayOf(
                (c - 0.18f) to Color.Transparent,
                c to Brand.GoldLight.copy(alpha = 0.14f),
                (c + 0.18f) to Color.Transparent,
            ),
            start = Offset(0f, 0f), end = Offset(w, h * 0.45f),
        ),
        blendMode = BlendMode.Screen,
    )
    val center = Offset(s.glintX * w, s.glintY * h)
    val radius = h * 0.32f
    drawCircle(
        Brush.radialGradient(listOf(Color(0xFFFFF5E0).copy(alpha = s.glintStrength), Color.Transparent), center = center, radius = radius),
        radius = radius, center = center, blendMode = BlendMode.Screen,
    )
}

/** RuntimeShader-ът на фолиото (API 33+); при грешка — резервните градиенти. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class FoilShader private constructor(private val shader: RuntimeShader) {
    val brush = ShaderBrush(shader)
    private var broken = false

    fun resize(w: Float, h: Float): Boolean = guard {
        shader.setFloatUniform("uResolution", w.coerceAtLeast(1f), h.coerceAtLeast(1f))
        shader.setFloatUniform("uStrength", CardFoil.STRENGTH)
        shader.setColorUniform("uGold", Brand.Gold.toArgb())
    }

    fun update(tx: Float, ty: Float, s: CardFoil.Shine): Boolean = guard {
        shader.setFloatUniform("uTilt", tx, ty)
        shader.setFloatUniform("uHue", s.hue)
        shader.setFloatUniform("uSheen", s.sheen)
        shader.setFloatUniform("uGlint", s.glintX, s.glintY)
        shader.setFloatUniform("uGlintStrength", s.glintStrength)
    }

    private inline fun guard(block: () -> Unit): Boolean {
        if (broken) return false
        return runCatching(block).onFailure { broken = true; Diagnostics.record("foil-shader", it) }.isSuccess
    }

    companion object {
        fun createOrNull(): FoilShader? =
            runCatching { FoilShader(RuntimeShader(ShevitsaShaders.FOIL)) }.onFailure { Diagnostics.record("foil-shader", it) }.getOrNull()
    }
}

/**
 * Текст с релеф („щампован“ в картата): тъмна сянка и светъл ръб, отместени по
 * наклона ([LocalCardTilt]). Копията са без семантика и се местят само в слоя —
 * без прекомпозиция при всяко събитие от сензора.
 */
@Composable
internal fun EmbossedText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    val tilt = LocalCardTilt.current
    // Като Text(fontSize = …): върху стила по подразбиране (височината на реда остава същата).
    val merged = LocalTextStyle.current.merge(style)
    Box(modifier) {
        if (tilt != null) {
            Text(
                text, color = Color.Black.copy(alpha = 0.55f), style = merged, maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }.graphicsLayer {
                    val s = CardFoil.shine(tilt.x, tilt.y)
                    translationX = -s.embossX * 1.2f * density
                    translationY = -s.embossY * 1.2f * density
                },
            )
            Text(
                text, color = Color.White.copy(alpha = 0.24f), style = merged, maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }.graphicsLayer {
                    val s = CardFoil.shine(tilt.x, tilt.y)
                    translationX = s.embossX * density
                    translationY = s.embossY * density
                },
            )
        }
        Text(text, color = color, style = merged, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
