package org.chyavorec.app.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.Brand
import kotlin.math.PI
import kotlin.math.sin

/** Най-голямото разтваряне на корицата (°): над 90° се вижда вътрешната ѝ страна (форзацът). */
internal const val BOOK_OPEN_MAX_DEG = 110f

/** Ъгълът на корицата при напредък на прехода [fold] (0 — затворена в началото, 1 — в края). */
internal fun bookOpenAngle(fold: Float): Float = BOOK_OPEN_MAX_DEG * sin(PI.toFloat() * fold.coerceIn(0f, 1f))

private val PageColor = Brand.Parchment
private val EndpaperColor = lerp(Brand.Cream, Brand.GoldLight, 0.35f)
private val PageLine = Brand.Muted.copy(alpha = 0.35f)
private val PageText = Brand.Muted.copy(alpha = 0.22f)
private val Frame = Brand.Gold.copy(alpha = 0.6f)

/**
 * Напредъкът на прехода на екрана за „разгръщането“ ([bookCoverShared]): 0 преди и след прехода,
 * 1 когато екранът е видим. Една стойност на дестинация (подава се от навигацията) — не по една
 * анимация за всяка корица в списъка.
 */
val LocalBookFold = compositionLocalOf<State<Float>?> { null }

/** Стойността за [LocalBookFold] от прехода на дестинацията ([scope]) — пружина без подскачане. */
@Composable
fun rememberBookFold(scope: AnimatedVisibilityScope): State<Float> =
    scope.transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow) },
        label = "book-open",
    ) { if (it == EnterExitState.Visible) 1f else 0f }

/**
 * „Разгръщане на книга“: корицата като [sharedElementKey] („прелита“ между списъка и детайлите
 * с ключ `book-<инв. №>`), но по време на прехода се отваря около гръбчето (лявия ръб) като
 * истинска предна корица — под нея се показва пергаментовата страница, а над 90° и вътрешната
 * страна на корицата, — и се затваря, докато застане на мястото си. Назад — обратно; жестът
 * „назад“ (predictive back) превърта същия преход, защото напредъкът е част от прехода на
 * екрана ([LocalBookFold]). Само трансформации на слоя и рисуване — без рекомпозиция по кадри.
 *
 * Слоят с отварянето се добавя само към корицата, чийто двойник е на другия екран, и само
 * докато преходът тече; останалите корици в списъка са само споделени елементи. При намалено
 * движение и в двата панела (таблет) — без споделен елемент, както [sharedElementKey].
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.bookCoverShared(inv: Long): Modifier {
    val shared = LocalSharedScope.current ?: return this
    val nav = LocalNavAnimatedScope.current ?: return this
    val reduced = rememberReducedMotion()
    if (reduced || !LocalSharedTransitionsEnabled.current) return this
    val state = with(shared) { rememberSharedContentState("book-$inv") }
    val base = with(shared) { this@bookCoverShared.sharedElement(state, animatedVisibilityScope = nav, boundsTransform = SpringBounds) }
    val fold = LocalBookFold.current ?: return base
    // Съвпадението се сменя два пъти на преход (начало и край) — не при всеки кадър.
    val matched by remember(state, shared) { derivedStateOf { state.isMatchFound && shared.isTransitionActive } }
    if (!matched) return base
    // 0 преди и след прехода на екрана, 1 когато е видим: ъгълът е sin(π·fold) — 0 в двата края.
    return base.bookOpening { bookOpenAngle(fold.value) }
}

/**
 * Самото отваряне при ъгъл [angle] (°): страницата отдолу, завъртането около лявия ръб и
 * форзацът/сянката върху корицата. Отделно — за снимката на междинен кадър в тестовете.
 */
internal fun Modifier.bookOpening(angle: () -> Float): Modifier =
    drawWithCache {
        val page = PageDrawing(this)
        onDrawBehind {
            val a = angle()
            if (a > 0.5f) page.draw(this, a)
        }
    }.graphicsLayer {
        val a = angle()
        // Свободният ръб идва към читателя (по-близо — по-голям) и минава наляво.
        rotationY = -a
        transformOrigin = TransformOrigin(0f, 0.5f)
        cameraDistance = 12f * density
    }.drawWithCache {
        val r = CornerRadius(3.dp.toPx())
        val inset = size.width * 0.1f
        val frame = Stroke(1.dp.toPx())
        onDrawWithContent {
            val a = angle()
            if (a > 90f) {
                // Вътрешната страна на корицата (форзац): кремава хартия със златна рамка.
                drawRoundRect(EndpaperColor, cornerRadius = r)
                drawRect(Frame, Offset(inset, inset), Size(size.width - 2 * inset, size.height - 2 * inset), style = frame)
                drawRect(Color.Black, alpha = 0.08f)
            } else {
                drawContent()
                // Обърнатата от светлината корица потъмнява.
                if (a > 0.5f) drawRect(Color.Black, alpha = 0.3f * sin(a / 180f * PI.toFloat()))
            }
        }
    }

/**
 * Първата страница под корицата: пергамент, ръбовете на листата вдясно, тънка златна рамка,
 * няколко реда „текст“ и сянката на корицата откъм гръбчето. Четките и линиите — веднъж за размера.
 */
private class PageDrawing(scope: CacheDrawScope) {
    private val radius: CornerRadius
    private val edges: List<Float>
    private val edgeStroke: Float
    private val edgeOutline: Stroke
    private val edgeTop: Float
    private val frameTopLeft: Offset
    private val frameSize: Size
    private val frameStroke: Stroke
    private val lines: List<Pair<Offset, Size>>
    private val shadeSize: Size
    private val shade: Brush

    init {
        with(scope) {
            val w = size.width
            val h = size.height
            val gap = 1.5.dp.toPx()
            radius = CornerRadius(3.dp.toPx())
            edges = (1..3).map { w - it * gap }
            edgeStroke = 0.6.dp.toPx()
            edgeOutline = Stroke(0.8.dp.toPx())
            edgeTop = gap * 2
            val inset = w * 0.14f
            frameTopLeft = Offset(inset, inset)
            frameSize = Size(w - 2 * inset - 3 * gap, h - 2 * inset)
            frameStroke = Stroke(0.8.dp.toPx())
            val lineH = 1.5.dp.toPx().coerceAtLeast(1f)
            val left = inset * 1.6f
            val right = w - inset * 1.6f - 3 * gap
            lines = (0 until 5).map { i ->
                val end = if (i == 4) left + (right - left) * 0.55f else right
                Offset(left, h * 0.3f + i * h * 0.075f) to Size(end - left, lineH)
            }
            shadeSize = Size(w * 0.35f, h)
            shade = Brush.horizontalGradient(listOf(Color.Black, Color.Transparent), 0f, w * 0.35f)
        }
    }

    fun draw(scope: DrawScope, angleDeg: Float) = with(scope) {
        drawRoundRect(PageColor, cornerRadius = radius)
        drawRoundRect(PageLine, cornerRadius = radius, style = edgeOutline)
        for (x in edges) drawLine(PageLine, Offset(x, edgeTop), Offset(x, size.height - edgeTop), strokeWidth = edgeStroke)
        drawRect(Frame, frameTopLeft, frameSize, style = frameStroke)
        for ((o, s) in lines) drawRect(PageText, o, s)
        // Сянката на корицата — най-гъста, когато корицата е изправена (90°).
        drawRect(shade, size = shadeSize, alpha = 0.28f * sin(angleDeg.coerceAtMost(90f) / 180f * PI.toFloat()))
    }
}
