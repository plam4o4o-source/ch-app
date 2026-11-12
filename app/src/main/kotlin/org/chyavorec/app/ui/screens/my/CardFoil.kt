package org.chyavorec.app.ui.screens.my

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import org.chyavorec.app.ui.components.GuardedShader
import org.chyavorec.app.ui.components.ShaderDraws
import org.chyavorec.app.ui.components.ShaderPool
import org.chyavorec.app.ui.components.ShaderTags
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.theme.Brand
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
 * него на бял фон и остава контрастен. Наклонът се чете само при рисуване, а
 * фолиото е в свой слой: наклонът прерисува само него (не и шевицата отдолу).
 */
@Composable
internal fun CardFoilLayer(tilt: Tilt, modifier: Modifier = Modifier) {
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val shader = remember(agsl) { if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) FoilShader.createOrNull() else null }
    // Връща се в резерва при изчезване на картата (отваряне на цял екран, обръщане) — без нов превод.
    DisposableEffect(shader) {
        onDispose { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) shader?.release() }
    }
    val draw = remember(shader, tilt) {
        Modifier.drawWithCache {
            val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.resize(size.width, size.height)
            // Градиентите на резервата — веднъж за размера (и чак когато са нужни); наклонът само ги мести.
            val fallback = lazy(LazyThreadSafetyMode.NONE) { FallbackFoil(size.width, size.height) }
            var noted = false
            onDrawBehind {
                val s = tilt.shine
                if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.update(tilt.x, tilt.y, s)) {
                    drawRect(shader.brush, blendMode = BlendMode.Screen)
                    if (!noted) {
                        noted = true
                        ShaderDraws.note(ShaderTags.FOIL)
                    }
                } else {
                    drawFallbackFoil(fallback.value, s)
                }
            }
        }
    }
    // Слоят е Auto (не Offscreen) — Screen се смесва с шевицата и градиента под него.
    Spacer(modifier.clearAndSetSemantics { }.graphicsLayer { }.then(draw))
}

/** Пастелната дъга на резервното фолио (злато → роза → люляк → мента → небе → злато). */
private val FoilColors = listOf(
    Color(0xFFE8D5A3), Color(0xFFE8A6B8), Color(0xFFC6A8E2), Color(0xFFA6DCC8), Color(0xFFA8C6EA), Color(0xFFE8D5A3),
)

/** Градиентите на резервното фолио за размер [w]×[h] — в покой; при рисуване се местят. */
private class FallbackFoil(w: Float, h: Float) {
    val rainbow = Brush.linearGradient(FoilColors, start = Offset(-w * 0.7f, 0f), end = Offset(w * 0.7f, h), tileMode = TileMode.Mirror)
    val sheen = Brush.linearGradient(
        colorStops = arrayOf(
            (0.5f - 0.18f) to Color.Transparent,
            0.5f to Brand.GoldLight.copy(alpha = 0.14f),
            (0.5f + 0.18f) to Color.Transparent,
        ),
        start = Offset(0f, 0f), end = Offset(w, h * 0.45f),
    )
    val glintRadius = h * 0.32f
    val glint = Brush.radialGradient(listOf(Color(0xFFFFF5E0), Color.Transparent), center = Offset.Zero, radius = glintRadius.coerceAtLeast(1f))
}

/** Без шейдър: дъга, която се плъзга с наклона, ивица блясък и мек отблясък (без нови четки на кадър). */
private fun DrawScope.drawFallbackFoil(f: FallbackFoil, s: CardFoil.Shine) {
    val w = size.width
    val h = size.height
    val shift = s.hue * w * 1.4f
    translate(left = shift) {
        drawRect(f.rainbow, topLeft = Offset(-shift, 0f), size = this@drawFallbackFoil.size, alpha = CardFoil.STRENGTH * 0.7f, blendMode = BlendMode.Screen)
    }
    // Ивицата е по диагонала (0,0)→(w, 0.45h); местенето на градиента = местене на точките му.
    val d = s.sheen.coerceIn(-0.2f, 1.2f) - 0.5f
    val dx = d * w
    val dy = d * h * 0.45f
    translate(dx, dy) {
        drawRect(f.sheen, topLeft = Offset(-dx, -dy), size = this@drawFallbackFoil.size, blendMode = BlendMode.Screen)
    }
    translate(s.glintX * w, s.glintY * h) {
        drawCircle(f.glint, radius = f.glintRadius, center = Offset.Zero, alpha = s.glintStrength, blendMode = BlendMode.Screen)
    }
}

/** RuntimeShader-ът на фолиото (API 33+); при грешка — резервните градиенти. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class FoilShader private constructor(private val guarded: GuardedShader) {
    val brush get() = guarded.brush

    fun resize(w: Float, h: Float): Boolean = guarded.guard {
        setFloatUniform("uResolution", w.coerceAtLeast(1f), h.coerceAtLeast(1f))
        setFloatUniform("uStrength", CardFoil.STRENGTH)
        setColorUniform("uGold", Brand.Gold.toArgb())
    }

    fun update(tx: Float, ty: Float, s: CardFoil.Shine): Boolean = guarded.guard {
        setFloatUniform("uTilt", tx, ty)
        setFloatUniform("uHue", s.hue)
        setFloatUniform("uSheen", s.sheen)
        setFloatUniform("uGlint", s.glintX, s.glintY)
        setFloatUniform("uGlintStrength", s.glintStrength)
    }

    /** Картата изчезва — шейдърът се връща в [ShaderPool]. */
    fun release() = guarded.release()

    companion object {
        fun createOrNull(): FoilShader? = ShaderPool.take(ShaderPool.FOIL)?.let(::FoilShader)
    }
}

/**
 * Текст с релеф („щампован“ в картата): тъмна сянка и светъл ръб, отместени по
 * наклона ([LocalCardTilt]). Копията са без семантика и се местят само в слоя —
 * без прекомпозиция при всяко събитие от сензора; отместването ([Tilt.shine]) се
 * пресмята веднъж на промяна на наклона, общо за двете копия и фолиото.
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
                    val s = tilt.shine
                    translationX = -s.embossX * 1.2f * density
                    translationY = -s.embossY * 1.2f * density
                },
            )
            Text(
                text, color = Color.White.copy(alpha = 0.24f), style = merged, maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clearAndSetSemantics { }.graphicsLayer {
                    val s = tilt.shine
                    translationX = s.embossX * density
                    translationY = s.embossY * density
                },
            )
        }
        Text(text, color = color, style = merged, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
