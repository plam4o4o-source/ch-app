package org.chyavorec.app.ui.components

import android.os.Build
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.chyavorec.app.ui.theme.Brand

/**
 * Златният отблясък по заглавието на книгата: един път (~500 ms), когато преходът „разгръщане
 * на книга“ е стигнал до края си, ивица светлина минава през буквите и ги „позлатява“.
 * API 33+: малък AGSL шейдър през [GuardedShader] (ивица с горещо ядро); API 26–32 и при грешка —
 * обикновен градиент със същото движение. При намалено движение — без отблясък. Слоят и
 * рисуването съществуват само докато отблясъкът тече; после модификаторът не добавя нищо.
 */
internal object GoldSheen {
    const val TAG = "sheen-shader"
    const val DURATION_MS = 500
    const val START_DELAY_MS = 120L

    /** Центърът на ивицата (дял от ширината, извън кадъра в двата края) при напредък [p]. */
    fun center(p: Float): Float = -0.3f + 1.6f * p

    /** Униформите на [SOURCE] — всички се задават от кода (тестът сверява с текста). */
    val UNIFORMS = listOf("uResolution", "uProgress", "uGold")

    /** Ивица светлина под лек наклон: широк златен ореол и тясно светло ядро; рисува се с SrcAtop върху буквите. */
    const val SOURCE: String = """
uniform float2 uResolution;
uniform float uProgress;
layout(color) uniform half4 uGold;

half4 main(float2 p) {
    float2 uv = p / uResolution;
    float t = uv.x * 0.9 + uv.y * 0.35;
    float c = -0.3 + 1.6 * uProgress;
    float e = (t - c) / 0.22;
    float halo = exp(-e * e);
    float core = exp(-e * e * 7.0);
    float a = clamp(halo * 0.95, 0.0, 1.0);
    float3 col = mix(float3(uGold.rgb), float3(1.0, 0.96, 0.8), core);
    return half4(half3(col * a), half(a));
}
"""
}

/**
 * [GoldSheen] върху съдържанието на модификатора (текст без фон). [key] — коя книга: веднъж на
 * отваряне на екрана (rememberSaveable — завъртането не го пуска пак).
 */
@Composable
fun Modifier.goldSheen(key: Any): Modifier {
    val reduced = rememberReducedMotion()
    var done by rememberSaveable(key) { mutableStateOf(false) }
    if (reduced || done) return this
    val progress = remember(key) { Animatable(0f) }
    val scope = LocalNavAnimatedScope.current
    LaunchedEffect(key) {
        // Чака края на прехода на екрана (корицата вече е на мястото си); без навигация — малко след показването.
        val transition = scope?.transition
        if (transition != null) snapshotFlow { transition.currentState == EnterExitState.Visible && !transition.isRunning }.first { it }
        delay(GoldSheen.START_DELAY_MS)
        progress.animateTo(1f, tween(GoldSheen.DURATION_MS, easing = FastOutSlowInEasing))
        done = true
    }
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val shader = remember(agsl) {
        if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) GuardedShader.create(GoldSheen.SOURCE, GoldSheen.TAG) else null
    }
    val gold = Brand.GoldLight
    val fadeColors = remember {
        listOf(gold.copy(alpha = 0f), gold.copy(alpha = 0.95f), Color(0xFFFFF3C4).copy(alpha = 0.95f), gold.copy(alpha = 0.95f), gold.copy(alpha = 0f))
    }
    return this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithCache {
            val w = size.width
            val h = size.height
            val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.guard {
                setFloatUniform("uResolution", w, h)
                setColorUniform("uGold", gold.toArgb())
            }
            val band = w * 0.45f
            onDrawWithContent {
                drawContent()
                val p = progress.value
                if (p <= 0f || p >= 1f) return@onDrawWithContent
                if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.guard { setFloatUniform("uProgress", p) }) {
                    drawRect(shader.brush, blendMode = BlendMode.SrcAtop)
                } else {
                    // Резерва: градиентна ивица със същото движение (API 26–32 и счупен шейдър).
                    val x0 = GoldSheen.center(p) * w - band / 2f
                    drawRect(
                        Brush.linearGradient(fadeColors, start = Offset(x0, 0f), end = Offset(x0 + band, band * 0.3f)),
                        blendMode = BlendMode.SrcAtop,
                    )
                }
            }
        }
}
