package org.chyavorec.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas as BitmapCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlin.math.ceil
import kotlin.math.min
import org.chyavorec.app.ui.theme.LocalExtendedColors

/**
 * Двете форми на плочката с икона ([FolkIconPlate]) — всяка със свой смисъл, за да не са
 * украса „на сляпо“: осморъката е читалището и общността (събития, дейности, контакти),
 * ромбът — библиотеката и „моето“ (карта, книги, списъци).
 */
enum class FolkMotif { ROSETTE, RHOMBUS }

/**
 * Контурите на трите форми като [RoundedPolygon] с радиус 1 (в единици = радиусът на плочката).
 * Строят се веднъж за процеса; пътят за конкретен размер — веднъж в drawWithCache.
 */
internal object FolkShapes {
    /**
     * Осморъка: 8 заоблени лъча с голям вътрешен радиус — мека розетка, не бодлива звезда.
     * Единствената в приложението: и плочката, и индикаторът на разделите ([TabRosetteShapes]),
     * и оценката в дневника, и медальонът на иконата на приложението (tools/gen_icon_rosette.py).
     */
    val rosette: RoundedPolygon = RoundedPolygon.star(
        numVerticesPerRadius = 8, radius = 1f, innerRadius = 0.8f,
        rounding = CornerRounding(0.2f, smoothing = 0.5f), innerRounding = CornerRounding(0.1f),
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
 * Плочката с икона — една и съща навсякъде, където икона стои над етикет (бързите действия на
 * „Начало“, плочките на „Още“ и „Моето“, действията на събитието, въведението): народна форма
 * ([motif]) с лек градиент и иконата в средата. Без пунктир — бодът е в самите икони („Бод“,
 * [FolkIcons]). В тъмна тема (и с полупрозрачен [container]) формата е плътна и с тънък ръб в
 * цвета на иконата: тъмнозлатното върху мастило иначе се слива и осморъката не се различава от
 * ромба. Самата плочка е готова картинка от общ кеш ([FolkPlateBitmaps]) — в списък всички еднакви
 * плочки са една и съща картинка, без пътища при рисуване; в покой — нищо по кадри.
 * [iconSize] — изрично (напр. [FolkListPlate]); иначе по размера на плочката.
 */
@Composable
fun FolkIconPlate(
    icon: ImageVector,
    motif: FolkMotif,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconSize: Dp = Dp.Unspecified,
) {
    val dark = LocalExtendedColors.current.isDark
    val rim = if (dark || container.alpha < 1f) content.copy(alpha = FolkPlateBitmaps.RIM_ALPHA) else Color.Unspecified
    val plate = remember(motif, container, rim, dark) {
        Modifier.drawWithCache {
            val bitmap = FolkPlateBitmaps.get(motif, this.size, container, rim, solid = dark, density = this)
            onDrawBehind { drawImage(bitmap) }
        }
    }
    // Ромбът е по-тесен от осморъката — иконата в него е малко по-малка, за да не опира в ръбовете.
    val glyph = if (iconSize != Dp.Unspecified) iconSize else size * if (motif == FolkMotif.RHOMBUS) 0.46f else 0.5f
    Box(modifier.size(size).then(plate), contentAlignment = Alignment.Center) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(glyph))
    }
}

/** Малката плочка в списъци (търсене, съобщения): 36 dp, икона 20 dp (в ромба — 18 dp). */
@Composable
fun FolkListPlate(
    icon: ImageVector,
    motif: FolkMotif,
    modifier: Modifier = Modifier,
    container: Color = MaterialTheme.colorScheme.primaryContainer,
    content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) = FolkIconPlate(
    icon, motif, modifier, size = 36.dp, container = container, content = content,
    iconSize = if (motif == FolkMotif.RHOMBUS) 18.dp else 20.dp,
)

/**
 * Готовите картинки на плочките — по форма, размер в px и цветове; общи за процеса (малък LRU).
 * В списък с 20 плочки формата се рисува веднъж, после е само drawImage (без запълване на път).
 */
internal object FolkPlateBitmaps {
    /** Непрозрачността на ръба (цветът на иконата) — около 3:1 спрямо мастилото в тъмна тема. */
    const val RIM_ALPHA = 0.5f

    private const val MAX = 16

    private data class Key(val motif: FolkMotif, val w: Int, val h: Int, val fill: Color, val rim: Color, val solid: Boolean, val rimPx: Float)

    private val cache = object : LinkedHashMap<Key, ImageBitmap>(MAX, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Key, ImageBitmap>?) = size > MAX
    }

    /** Колко картинки има в кеша (за тестове). */
    internal val count: Int get() = synchronized(cache) { cache.size }

    fun get(motif: FolkMotif, size: Size, fill: Color, rim: Color, solid: Boolean, density: Density): ImageBitmap {
        val w = ceil(size.width).toInt().coerceAtLeast(1)
        val h = ceil(size.height).toInt().coerceAtLeast(1)
        val rimPx = if (rim != Color.Unspecified) with(density) { 1.dp.toPx() } else 0f
        val key = Key(motif, w, h, fill, rim, solid, rimPx)
        return synchronized(cache) { cache.getOrPut(key) { draw(key, density) } }
    }

    private fun draw(k: Key, density: Density): ImageBitmap {
        val bitmap = ImageBitmap(k.w, k.h)
        CanvasDrawScope().draw(density, LayoutDirection.Ltr, BitmapCanvas(bitmap), Size(k.w.toFloat(), k.h.toFloat())) {
            val c = min(k.w, k.h) / 2f
            // Ръбът е изцяло вътре в картинката.
            val outer = FolkShapes.of(k.motif).scaledPath(c * 0.98f - k.rimPx / 2f, k.w / 2f, k.h / 2f)
            if (k.solid) drawPath(outer, k.fill)
            else drawPath(outer, Brush.linearGradient(listOf(k.fill, k.fill.copy(alpha = k.fill.alpha * 0.55f))))
            if (k.rimPx > 0f) drawPath(outer, k.rim, style = Stroke(k.rimPx))
        }
        return bitmap
    }
}

/**
 * Пунктираният ъгъл на плочка (горе вдясно) — само докато плочката е натисната: [tighten]
 * (0..1, четено само при рисуване) го проявява и „затяга“ към ръба. В покой (0) не се рисува
 * нищо — 11 плочки не носят 11 пунктирни ъгъла. Пътят е готов за размера, а
 * модификаторът е запомнен — рекомпозицията не гради кеша наново.
 */
@Composable
internal fun Modifier.stitchedCorner(color: Color, tighten: () -> Float): Modifier = stitchedCorner(ThreadColors(color), tighten)

/** Същият ъгъл в сезонния конец ([rememberSeasonalThread]): усукан на две нишки или със звездичка. */
@Composable
internal fun Modifier.stitchedCorner(thread: ThreadColors, tighten: () -> Float): Modifier {
    val currentTighten = rememberUpdatedState(tighten)
    val corner = remember(thread) {
        Modifier.drawWithCache {
            val w = size.width
            val inset = 6.dp.toPx()
            val r = 14.dp.toPx()
            val arm = 18.dp.toPx()
            val path = Path().apply {
                moveTo(w - inset - r - arm, inset)
                lineTo(w - inset - r, inset)
                arcTo(Rect(w - inset - 2 * r, inset, w - inset, inset + 2 * r), -90f, 90f, false)
                lineTo(w - inset, inset + r + arm)
            }
            // Тичащ бод ([StitchSpec.Running]) — като пунктирите навсякъде другаде; в сезонния конец —
            // усукан на две нишки ([StitchSpec.RunningTwisted]) или със звезден бод в началото (Коледа).
            val starAt = Offset(w - inset - r - arm, inset)
            val starR = 2.6.dp.toPx()
            onDrawWithContent {
                drawContent()
                val t = currentTighten.value().coerceIn(0f, 1f)
                if (t > 0f) {
                    drawThread(path, thread.primary, thread.second, thread.secondEdge, alpha = 0.75f * t)
                    if (thread.star) drawStarStitch(starAt, starR, thread.primary, 1.dp.toPx(), alpha = 0.85f * t)
                }
            }
        }
    }
    return this.then(corner)
}

/* ---- Индикаторът „зарежда се“: кръг → осморъка → ромб → елбетица ---- */

/** Формите на индикатора и преходите между тях (по кръга). */
internal object FolkLoaderShapes {
    val polygons: List<RoundedPolygon> = listOf(FolkShapes.circle, FolkShapes.rosette, FolkShapes.rhombus, FolkShapes.elbetitsa)
    val morphs: List<Morph> = List(polygons.size) { Morph(polygons[it], polygons[(it + 1) % polygons.size]) }
    val count get() = polygons.size
}

/** Пътят на междинната форма — един и същ обект, строи се наново само при нов прогрес. */
internal class FolkLoaderCache {
    private val platformPath = android.graphics.Path()
    private val path = platformPath.asComposePath()
    private var lastIndex = -1
    private var lastProgress = -1f

    fun pathAt(index: Int, progress: Float): Path {
        if (index != lastIndex || progress != lastProgress) {
            FolkLoaderShapes.morphs[index].toPath(progress, platformPath)
            lastIndex = index
            lastProgress = progress
        }
        return path
    }
}

/**
 * Кадър на индикатора при фаза [phase] (0 .. [FolkLoaderShapes.count], цяло число = форма в покой):
 * пълната форма и около нея — тичащ бод ([StitchSpec.Running]), който се върти обратно. Без
 * заделяне на памет (освен пътя в [cache] при нов прогрес; бодът е готов за мащаба).
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
                // Пътят е в единици на формата — бодът се мащабира обратно (1 dp = 1.dp.toPx() / ring).
                drawPath(path, color, alpha = 0.6f, style = StitchSpec.Running.stroke(1.dp.toPx() / ring))
            }
        }
    }
}

/** Неподвижен кадър на индикатора (за тестове и намалено движение) — [phase] както в [drawFolkLoader]. */
@Composable
internal fun FolkLoaderFrame(phase: Float, color: Color, modifier: Modifier = Modifier) {
    val cache = remember { FolkLoaderCache() }
    Canvas(modifier) { drawFolkLoader(cache, phase, color, spin = false) }
}
