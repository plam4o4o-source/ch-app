package org.chyavorec.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlin.math.min

/** Народните форми за плочките и индикатора: осморъка, ромб и елбетица (четирилъчен „рог“). */
enum class FolkMotif { ROSETTE, RHOMBUS, ELBETITSA }

/**
 * Контурите на трите форми като [RoundedPolygon] с радиус 1 (в единици = радиусът на плочката).
 * Строят се веднъж за процеса; пътят за конкретен размер — веднъж в drawWithCache.
 */
internal object FolkShapes {
    /** Осморъка: пълна, заоблена розетка — иконата в средата има място. */
    val rosette: RoundedPolygon = RoundedPolygon.star(
        numVerticesPerRadius = 8, radius = 1f, innerRadius = 0.76f,
        rounding = CornerRounding(0.12f, smoothing = 0.4f), innerRounding = CornerRounding(0.08f),
    )

    /** Ромб — най-старият „пазещ“ знак, с меки върхове. */
    val rhombus: RoundedPolygon = RoundedPolygon(
        vertices = floatArrayOf(0f, -1f, 0.88f, 0f, 0f, 1f, -0.88f, 0f),
        rounding = CornerRounding(0.16f),
    )

    /** Елбетица: четирилъчна „кука“ — вдлъбнати страни, закръглени краища. */
    val elbetitsa: RoundedPolygon = RoundedPolygon.star(
        numVerticesPerRadius = 4, radius = 1f, innerRadius = 0.6f,
        rounding = CornerRounding(0.2f, smoothing = 0.5f), innerRounding = CornerRounding(0.12f),
    )

    /** Кръгът — началото на индикатора (осем върха като осморъката — за плавно превръщане). */
    val circle: RoundedPolygon = RoundedPolygon.circle(numVertices = 8, radius = 0.9f)

    fun of(motif: FolkMotif): RoundedPolygon = when (motif) {
        FolkMotif.ROSETTE -> rosette
        FolkMotif.RHOMBUS -> rhombus
        FolkMotif.ELBETITSA -> elbetitsa
    }
}

/** Пътят на формата с радиус [radius] и център ([cx], [cy]) — за кеширане в drawWithCache. */
internal fun RoundedPolygon.scaledPath(radius: Float, cx: Float, cy: Float): Path {
    val platform = toPath()
    val m = android.graphics.Matrix()
    m.setScale(radius, radius)
    m.postTranslate(cx, cy)
    platform.transform(m)
    return platform.asComposePath()
}

/**
 * Плочка с народна форма вместо заоблен квадрат: градиентен пълнеж и тънък пунктиран „бод“
 * малко навътре. Пътищата се строят веднъж за размера; в покой — нищо по кадри.
 */
internal fun Modifier.folkPlate(motif: FolkMotif, container: Color, stitch: Color): Modifier = drawWithCache {
    val c = size.minDimension / 2f
    val poly = FolkShapes.of(motif)
    val outer = poly.scaledPath(c * 0.98f, size.width / 2f, size.height / 2f)
    val inner = poly.scaledPath(c * 0.80f, size.width / 2f, size.height / 2f)
    val fill = Brush.linearGradient(listOf(container, container.copy(alpha = 0.55f)))
    val line = Stroke(
        width = 1.dp.toPx(), cap = StrokeCap.Round,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.5.dp.toPx(), 2.dp.toPx())),
    )
    onDrawBehind {
        drawPath(outer, fill)
        drawPath(inner, stitch.copy(alpha = 0.4f), style = line)
    }
}

/** [IconPlate] в народна форма ([motif]); без [motif] — както досега (заоблен квадрат). */
@Composable
fun FolkIconPlate(
    icon: ImageVector,
    motif: FolkMotif,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Box(modifier.size(size).folkPlate(motif, container, content), contentAlignment = Alignment.Center) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(size * 0.42f))
    }
}

/**
 * Пунктираният ъгъл на плочка (горе вдясно): два коротки „бода“ по заобляне на рамката.
 * При натискане [tighten] (0..1) ги „затяга“ — по-близо до ръба и по-гъсти. Само рисуване:
 * двата пътя са готови за размера, а в покой не се чете нищо по кадри.
 */
internal fun Modifier.stitchedCorner(color: Color, tighten: () -> Float): Modifier = drawWithCache {
    fun corner(inset: Float, r: Float, arm: Float): Path {
        val w = size.width
        return Path().apply {
            moveTo(w - inset - r - arm, inset)
            lineTo(w - inset - r, inset)
            arcTo(Rect(w - inset - 2 * r, inset, w - inset, inset + 2 * r), -90f, 90f, false)
            lineTo(w - inset, inset + r + arm)
        }
    }
    val wide = corner(7.dp.toPx(), 13.dp.toPx(), 14.dp.toPx())
    val tight = corner(6.dp.toPx(), 14.dp.toPx(), 18.dp.toPx())
    val loose = Stroke(1.dp.toPx(), cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 3.5.dp.toPx())))
    val dense = Stroke(1.dp.toPx(), cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 1.6.dp.toPx())))
    onDrawWithContent {
        drawContent()
        val t = tighten().coerceIn(0f, 1f)
        if (t < 1f) drawPath(wide, color, alpha = 0.4f * (1f - t), style = loose)
        if (t > 0f) drawPath(tight, color, alpha = 0.75f * t, style = dense)
    }
}

/* ---- Индикаторът „зарежда се“: кръг → осморъка → ромб → елбетица ---- */

/** Формите на индикатора и преходите между тях (по кръга). */
internal object FolkLoaderShapes {
    val polygons: List<RoundedPolygon> = listOf(FolkShapes.circle, FolkShapes.rosette, FolkShapes.rhombus, FolkShapes.elbetitsa)
    val morphs: List<Morph> = List(polygons.size) { Morph(polygons[it], polygons[(it + 1) % polygons.size]) }
    val count get() = polygons.size
}

/** Пътят на междинната форма и пунктирът — един и същ обект, строи се наново само при нов прогрес. */
internal class FolkLoaderCache {
    private val platformPath = android.graphics.Path()
    private val path = platformPath.asComposePath()
    private var lastIndex = -1
    private var lastProgress = -1f
    private var strokeWidth = -1f
    private var stroke: Stroke? = null

    fun pathAt(index: Int, progress: Float): Path {
        if (index != lastIndex || progress != lastProgress) {
            FolkLoaderShapes.morphs[index].toPath(progress, platformPath)
            lastIndex = index
            lastProgress = progress
        }
        return path
    }

    /** Пунктир в единиците на формата, създава се веднъж за дебелината. */
    fun dashed(width: Float, dash: Float, gap: Float): Stroke {
        val s = stroke
        if (s != null && width == strokeWidth) return s
        strokeWidth = width
        return Stroke(width, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap))).also { stroke = it }
    }
}

/**
 * Кадър на индикатора при фаза [phase] (0 .. [FolkLoaderShapes.count], цяло число = форма в покой):
 * пълната форма и около нея — пунктиран „бод“, който се върти обратно. Без заделяне на памет
 * (освен пътя в [cache] при нов прогрес).
 */
internal fun DrawScope.drawFolkLoader(cache: FolkLoaderCache, phase: Float, color: Color, spin: Boolean = true) {
    val count = FolkLoaderShapes.count
    val i = min(phase.toInt(), count - 1)
    val f = phase - i
    // Превръщането заема първите 70% от стъпката, после формата „почива“.
    val m = FastOutSlowInEasing.transform(min(f / 0.7f, 1f))
    val path = cache.pathAt(i, m)
    val half = min(size.width, size.height) / 2f
    val core = half * 0.66f
    val ring = half * 0.93f
    val turn = if (spin) phase * (360f / count) else 0f
    translate(size.width / 2f, size.height / 2f) {
        rotate(turn, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            scale(core, core, pivot = androidx.compose.ui.geometry.Offset.Zero) { drawPath(path, color) }
        }
        rotate(-turn, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            scale(ring, ring, pivot = androidx.compose.ui.geometry.Offset.Zero) {
                val w = 1.4.dp.toPx() / ring
                drawPath(path, color, alpha = 0.6f, style = cache.dashed(w, 2.6.dp.toPx() / ring, 2.4.dp.toPx() / ring))
            }
        }
    }
}

/** Неподвижен кадър на индикатора (за тестове и намалено движение) — [phase] както в [drawFolkLoader]. */
@Composable
internal fun FolkLoaderFrame(phase: Float, color: Color, modifier: Modifier = Modifier) {
    val cache = androidx.compose.runtime.remember { FolkLoaderCache() }
    Canvas(modifier) { drawFolkLoader(cache, phase, color, spin = false) }
}
