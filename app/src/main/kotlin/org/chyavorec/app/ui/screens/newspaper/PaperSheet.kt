package org.chyavorec.app.ui.screens.newspaper

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlin.math.max

/** Геометрията на щипването и влаченето — чисти функции (тестват се без екран). */
object PageGeometry {
    const val MIN_SCALE = 1f
    const val MAX_SCALE = 5f
    /** Двойното докосване увеличава до толкова. */
    const val DOUBLE_TAP_SCALE = 2.5f

    fun clampScale(s: Float): Float = s.coerceIn(MIN_SCALE, MAX_SCALE)

    /** Докъде може да се измести по една ос, ако листът (вече мащабиран) е [scaled] в прозорец [view] — с [margin] край листа. */
    fun maxOffset(scaled: Float, view: Float, margin: Float = 0f): Float = max(0f, (scaled + 2 * margin - view) / 2f)

    fun clampOffset(t: Float, scaled: Float, view: Float, margin: Float = 0f): Float {
        val m = maxOffset(scaled, view, margin)
        return t.coerceIn(-m, m)
    }

    /** Изместването след промяна на мащаба от [oldScale] на [newScale] около точката [c] (от центъра на прозореца). */
    fun zoomAbout(t: Float, c: Float, oldScale: Float, newScale: Float): Float = c - (c - t) * (newScale / oldScale)

    /** Началното изместване по вертикалата: горният ръб на листа (положително — листът е избутан надолу). */
    fun topOffset(sheetHeight: Float, viewHeight: Float, margin: Float = 0f): Float = maxOffset(sheetHeight, viewHeight, margin)
}

/** Цветовете на стария вестник: хартия, „бюро“ около нея и мастило при тъмна тема. */
object PaperColors {
    val Sheet = Color(0xFFF4ECD8)
    val Desk = Color(0xFFE6DAC0)
    val DeskDark = Color(0xFF15110A)
    val SheetDark = Color(0xFF211A10)
    private val InkDark = floatArrayOf(0.93f, 0.87f, 0.74f)
    private val PaperDark = floatArrayOf(0.13f, 0.105f, 0.07f)

    /**
     * Тъмна тема: „нежно обръщане“ — белите места стават тъмна хартия, черните — кремаво мастило
     * (не чисто бяло); Multiply не върши работа върху тъмна хартия.
     */
    val darkFilter: ColorFilter = ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                PaperDark[0] - InkDark[0], 0f, 0f, 0f, InkDark[0] * 255f,
                0f, PaperDark[1] - InkDark[1], 0f, 0f, InkDark[1] * 255f,
                0f, 0f, PaperDark[2] - InkDark[2], 0f, InkDark[2] * 255f,
                0f, 0f, 0f, 1f, 0f,
            ),
        ),
    )
}

/**
 * Листът от стария вестник: пергаментен лист с лека сянка по ръба и мек потъмнен край; страницата се
 * рисува върху него с Multiply (белият фон на PDF-а става хартия, шрифтът — мастило). Щипване и влачене
 * (до [PageGeometry.MAX_SCALE]×), двойно докосване — увеличение/връщане. [onZoomSettled] — мащабът ~¼ s
 * след последното движение (четецът прерисува по-едро). Без [page] — празен лист (докато се рисува).
 */
@OptIn(FlowPreview::class)
@Composable
fun ZoomablePaper(
    page: RenderedPage?,
    pageKey: Int,
    description: String,
    modifier: Modifier = Modifier,
    /** Пропорцията (височина/ширина), докато няма картинка. */
    placeholderAspect: Float = 1.41f,
    onZoomSettled: (Float) -> Unit = {},
) {
    val dark = LocalExtendedColors.current.isDark
    val reduced = rememberReducedMotion()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var scale by remember(pageKey) { mutableFloatStateOf(1f) }
    var tx by remember(pageKey) { mutableFloatStateOf(0f) }
    var ty by remember(pageKey) { mutableFloatStateOf(0f) }
    val margin = with(density) { 12.dp.toPx() }
    val zoomInLabel = stringResource(R.string.newspaper_zoom_in)
    val zoomOutLabel = stringResource(R.string.newspaper_zoom_out)
    val aspect = page?.aspect ?: placeholderAspect

    BoxWithConstraints(modifier.fillMaxSize().background(if (dark) PaperColors.DeskDark else PaperColors.Desk).clipToBounds()) {
        val viewW = constraints.maxWidth.toFloat()
        val viewH = constraints.maxHeight.toFloat()
        val sheetW = (viewW - 2 * margin).coerceAtLeast(1f)
        val sheetH = sheetW * aspect

        fun clampAll() {
            tx = PageGeometry.clampOffset(tx, sheetW * scale, viewW, margin)
            ty = PageGeometry.clampOffset(ty, sheetH * scale, viewH, margin)
        }
        // Нова страница или нов размер: отначало, с горния край на листа на екрана.
        LaunchedEffect(pageKey, viewW, viewH, page != null) {
            scale = 1f
            tx = 0f
            ty = PageGeometry.topOffset(sheetH, viewH, margin)
        }
        LaunchedEffect(Unit) { snapshotFlow { scale }.debounce(250).collect { onZoomSettled(it) } }

        fun zoomTo(target: Float, focus: Offset) {
            val s0 = scale; val x0 = tx; val y0 = ty
            val s1 = PageGeometry.clampScale(target)
            val cx = focus.x - viewW / 2f
            val cy = focus.y - viewH / 2f
            val x1 = PageGeometry.clampOffset(PageGeometry.zoomAbout(x0, cx, s0, s1), sheetW * s1, viewW, margin)
            val y1 = PageGeometry.clampOffset(PageGeometry.zoomAbout(y0, cy, s0, s1), sheetH * s1, viewH, margin)
            if (reduced) {
                scale = s1; tx = x1; ty = y1
            } else {
                scope.launch {
                    animate(0f, 1f, animationSpec = tween(240)) { f, _ ->
                        scale = s0 + (s1 - s0) * f
                        tx = x0 + (x1 - x0) * f
                        ty = y0 + (y1 - y0) * f
                    }
                }
            }
        }

        // Жестовете са по ключ от страницата и прозореца; пропорцията идва от картинката и при по-едро
        // прерисуване се променя с пиксел — ключ по нея би рестартирал детекторите под пръста.
        val clampNow by rememberUpdatedState { clampAll() }
        val zoomToNow by rememberUpdatedState<(Float, Offset) -> Unit> { target, focus -> zoomTo(target, focus) }

        val w = with(density) { sheetW.toDp() }
        val h = with(density) { sheetH.toDp() }
        Box(
            Modifier.fillMaxSize()
                .pointerInput(pageKey, viewW, viewH) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        val s0 = scale
                        val s1 = PageGeometry.clampScale(s0 * zoom)
                        val cx = centroid.x - viewW / 2f
                        val cy = centroid.y - viewH / 2f
                        tx = PageGeometry.zoomAbout(tx, cx, s0, s1) + pan.x
                        ty = PageGeometry.zoomAbout(ty, cy, s0, s1) + pan.y
                        scale = s1
                        clampNow()
                    }
                }
                .pointerInput(pageKey, viewW, viewH) {
                    detectTapGestures(onDoubleTap = { at -> zoomToNow(if (scale > 1.2f) 1f else PageGeometry.DOUBLE_TAP_SCALE, at) })
                }
                .semantics {
                    contentDescription = description
                    customActions = listOf(
                        CustomAccessibilityAction(zoomInLabel) { zoomTo(scale * 1.6f, Offset(viewW / 2f, viewH / 2f)); true },
                        CustomAccessibilityAction(zoomOutLabel) { zoomTo(scale / 1.6f, Offset(viewW / 2f, viewH / 2f)); true },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            val sheet = if (dark) PaperColors.SheetDark else PaperColors.Sheet
            Canvas(
                Modifier.requiredSize(w, h)
                    .graphicsLayer {
                        scaleX = scale; scaleY = scale
                        translationX = tx; translationY = ty
                    }
                    .shadow(if (dark) 3.dp else 5.dp, clip = false, ambientColor = Color(0xFF3A2A10), spotColor = Color(0xFF3A2A10)),
            ) {
                drawRect(sheet)
                if (page != null) {
                    drawImage(
                        page.image, dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                        colorFilter = if (dark) PaperColors.darkFilter else null,
                        blendMode = if (dark) BlendMode.SrcOver else BlendMode.Multiply,
                        filterQuality = FilterQuality.High,
                    )
                }
                // Мек потъмнен край — хартията е по-тъмна към ръбовете и в ъглите.
                val edge = if (dark) Color(0x66000000) else Color(0x2E5A3E12)
                drawRect(
                    Brush.radialGradient(
                        0.62f to Color.Transparent, 1f to edge,
                        center = center, radius = size.maxDimension * 0.78f,
                    ),
                )
            }
        }
    }
}

/** Описанието на страницата за екранния четец. */
@Composable
fun pageDescription(title: String, index: Int, count: Int): String =
    stringResource(R.string.newspaper_page_desc, title, index + 1, count)
