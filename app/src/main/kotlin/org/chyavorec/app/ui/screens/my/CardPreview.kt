package org.chyavorec.app.ui.screens.my

import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.GoldSheen
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.components.ShaderPool
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.components.sharedBoundsKey
import org.chyavorec.app.ui.theme.Brand

/** Примерните данни на картата за читател без вход (същите като във въведението). */
@Composable
internal fun sampleCardData(): CardData = CardData(
    holder = stringResource(R.string.onb_sample_name), number = SAMPLE_CARD_NUMBER, barcode = SAMPLE_CARD_NUMBER,
    status = null, verified = false,
)

private const val SAMPLE_CARD_NUMBER = "19220001"

/** „Картата е издадена“: повдигането и отблясъкът след успешен вход. */
internal object CardIssue {
    /** Колко се повдига картата (dp) и колко расте (дял). */
    const val LIFT_DP = 10f
    const val GROW = 0.03f

    /** Един отблясък на фолиото — колкото трае и прелитането към „Моето“. */
    const val SWEEP_MS = 520

    /** Плътността на отблясъка върху картата (по-слаб от златото по буквите на заглавие). */
    const val SWEEP_ALPHA = 0.5f
}

/**
 * Примерната карта за читател без вход („Моето“ и входът): истинският [LibraryCard] със
 * същата шевица и фолио по наклона, както във въведението — сензорът е включен само докато
 * екранът е на преден план и заспива в покой; без шейдър (до Android 12L) фолиото е резервното,
 * при намалено движение картата е неподвижна. Отдолу — „Така ще изглежда картата ти“.
 *
 * [sharedKey] — картата прелита между „Моето“ и входа, а след входа — в плочката „Читателска
 * карта“ ([LIBRARY_CARD_KEY]; на Android 8 и при намалено движение преходите са без нея).
 * [issued] — входът е успешен: картата се повдига леко и по нея минава веднъж отблясък. Само
 * украса върху вече започналата навигация — нищо не я чака.
 */
@Composable
internal fun SampleCardPreview(modifier: Modifier = Modifier, sharedKey: String? = LIBRARY_CARD_KEY, issued: Boolean = false) {
    val description = stringResource(R.string.onb_sample_card_cd)
    val data = sampleCardData()
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.widthIn(max = 300.dp).fillMaxWidth()
                .then(if (sharedKey != null) Modifier.sharedBoundsKey(sharedKey) else Modifier)
                .issueMotion(issued)
                .semantics { contentDescription = description },
        ) {
            LibraryCard(data, Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(10.dp))
        Text(
            stringResource(R.string.guest_card_hint), style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
        )
    }
}

/**
 * Повдигането и отблясъкът при [issued]. До успешния вход — нищо (без слой и рисуване);
 * при намалено движение — също нищо (картата просто изчезва с екрана). API 33+: лентата е
 * AGSL шейдърът на [GoldSheen] от [ShaderPool]; по-старите и при грешка — градиент със
 * същото движение. Шейдърът се взима едва при успеха и се връща при изчезването на екрана.
 */
@Composable
private fun Modifier.issueMotion(issued: Boolean): Modifier {
    val reduced = rememberReducedMotion()
    if (!issued || reduced) return this
    val lift = remember { Animatable(0f) }
    val sweep = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch { lift.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)) }
        sweep.animateTo(1f, tween(CardIssue.SWEEP_MS, easing = FastOutSlowInEasing))
    }
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val shader = remember(agsl) {
        if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ShaderPool.take(ShaderPool.SHEEN) else null
    }
    DisposableEffect(shader) {
        onDispose { if (shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) shader.release() }
    }
    val shape: Shape = MaterialTheme.shapes.medium
    val gold = Brand.GoldLight
    val bandColors = remember { listOf(gold.copy(alpha = 0f), gold, Color.White.copy(alpha = 0.9f), gold, gold.copy(alpha = 0f)) }
    return this
        .graphicsLayer {
            val l = lift.value
            translationY = -CardIssue.LIFT_DP * l * density
            scaleX = 1f + CardIssue.GROW * l
            scaleY = 1f + CardIssue.GROW * l
        }
        .drawWithCache {
            val w = size.width
            val h = size.height
            // Отблясъкът — само в заоблената карта (сянката ѝ остава извън него).
            val clip = Path().apply { addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache)) }
            val ready = shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.guard {
                setFloatUniform("uResolution", w, h)
                setColorUniform("uGold", gold.toArgb())
            }
            val band = w * 0.45f
            onDrawWithContent {
                drawContent()
                val p = sweep.value
                if (p <= 0f || p >= 1f) return@onDrawWithContent
                clipPath(clip) {
                    if (ready && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader!!.guard { setFloatUniform("uProgress", p) }) {
                        drawRect(shader.brush, alpha = CardIssue.SWEEP_ALPHA)
                    } else {
                        // Резерва (API 26–32, счупен шейдър): градиентна ивица със същото движение.
                        val x0 = GoldSheen.center(p) * w - band / 2f
                        drawRect(Brush.linearGradient(bandColors, start = Offset(x0, 0f), end = Offset(x0 + band, band * 0.3f)), alpha = CardIssue.SWEEP_ALPHA)
                    }
                }
            }
        }
}
