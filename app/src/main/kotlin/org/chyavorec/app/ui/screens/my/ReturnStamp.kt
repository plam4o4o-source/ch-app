package org.chyavorec.app.ui.screens.my

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.AnimatedCounter
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.GuardedShader
import org.chyavorec.app.ui.components.HapticSignature
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.components.LocalConfetti
import org.chyavorec.app.ui.components.rememberAppHaptics
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem

/*
 * Печатът „Върната“: при първото отваряне на „Моите книги“ след връщане картата на книгата
 * получава кръгъл печат „ВЪРНАТА · Благодарим“ (пада, ударът е с вибрация „печат“, мастилото
 * се разлива), после корицата се плъзва към „Прочетени книги“, а годишният брояч се завърта +1.
 * Логиката кой се празнува и само веднъж е в ReturnStamps (shared); тук е само изгледът.
 * Намалено движение: статичен печатан ред за ~3 s. Един кадър на анимация, нищо в покой.
 */

/** Какво се празнува при това отваряне: книгите, годината и числото на брояча преди първия печат. */
data class StampShow(val books: List<HistoryItem>, val year: Int, val yearBefore: Int)

/** Времената на анимацията (ms) — на едно място за изгледа и тестовете. */
internal object StampTiming {
    const val ENTER = 220
    const val PRESS = 130
    const val INK = 520
    const val HOLD = 550L
    const val SLIDE = 560
    const val ROLL = 700L
    const val OUT = 320L

    /** Статичният ред при намалено движение: първата книга по-дълго, следващите — по-кратко. */
    fun staticMs(index: Int): Long = if (index == 0) 3000L else 2000L
}

/** Анимираните стойности на един печат. Четат се само при рисуване (без рекомпозиция на кадър). */
@Stable
internal class StampAnim {
    /** 0 — печатът е вдигнат, 1 — докосва картата. */
    val press = Animatable(0f)
    /** Кратко „смачкване“ след удара. */
    val squash = Animatable(1f)
    /** Мастилото: 0 — няма, 1 — напълно разляно. */
    val ink = Animatable(0f)
    /** Появата на реда с книгата. */
    val enter = Animatable(0f)
    /** Плъзгането на корицата към „Прочетени книги“. */
    val slide = Animatable(0f)

    suspend fun reset() {
        enter.snapTo(0f); press.snapTo(0f); squash.snapTo(1f); ink.snapTo(0f); slide.snapTo(0f)
    }

    /** Крайният кадър: поставен печат, корицата на мястото си. */
    suspend fun settle() {
        enter.snapTo(1f); press.snapTo(1f); squash.snapTo(1f); ink.snapTo(1f); slide.snapTo(0f)
    }
}

/** Къде са корицата и целта ѝ (в координатите на прозореца) — за плъзгането; не е състояние. */
@Stable
internal class StampGeometry {
    var cover: Rect? = null
    var target: Rect? = null
    var seal: Rect? = null
}

@Composable
internal fun ReturnStampPanel(
    show: StampShow,
    onOpenHistory: (() -> Unit)?,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val reduced = rememberReducedMotion()
    val haptics = rememberAppHaptics()
    val confetti = LocalConfetti.current
    val anim = remember { StampAnim() }
    val geo = remember { StampGeometry() }
    var index by remember { mutableIntStateOf(0) }
    var counter by remember { mutableIntStateOf(show.yearBefore) }
    var visible by remember { mutableStateOf(false) }
    val latest by rememberUpdatedState(show)
    val currentOnDone by rememberUpdatedState(onDone)

    LaunchedEffect(Unit) {
        visible = true
        var i = 0
        // Книгите могат да се допълнят, докато върви (ново опресняване) — цикълът ги догонва.
        while (i < latest.books.size) {
            index = i
            anim.reset()
            if (reduced) {
                anim.settle()
                counter = latest.yearBefore + i + 1
                haptics.play(HapticSignature.SEAL)
                delay(StampTiming.staticMs(i))
            } else {
                anim.enter.animateTo(1f, tween(StampTiming.ENTER))
                delay(80)
                anim.press.animateTo(1f, tween(StampTiming.PRESS, easing = FastOutLinearInEasing))
                // Ударът: вибрация „печат“ и (за първата книга) шевичено конфети от печата.
                haptics.play(HapticSignature.SEAL)
                if (i == 0) geo.seal?.center?.let { confetti?.burst(it) }
                coroutineScope {
                    launch { anim.ink.animateTo(1f, tween(StampTiming.INK)) }
                    launch {
                        anim.squash.animateTo(0.93f, tween(60))
                        anim.squash.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
                    }
                    delay(StampTiming.HOLD)
                }
                anim.slide.animateTo(1f, tween(StampTiming.SLIDE, easing = FastOutSlowInEasing))
                counter = latest.yearBefore + i + 1
                delay(StampTiming.ROLL)
            }
            i++
        }
        visible = false
        if (!reduced) delay(StampTiming.OUT)
        currentOnDone()
    }

    val book = latest.books.getOrNull(index) ?: return
    AnimatedVisibility(
        visible,
        modifier,
        enter = if (reduced) EnterTransition.None else expandVertically(tween(260)) + fadeIn(tween(200)),
        exit = if (reduced) ExitTransition.None else shrinkVertically(tween(300)) + fadeOut(tween(200)),
    ) {
        ReturnStampCard(book, anim, geo, counter, latest.year, onOpenHistory)
    }
}

/** Картата: реда с книгата и печата, отдолу — „Прочетени книги“ с годишния брояч. */
@Composable
internal fun ReturnStampCard(
    book: HistoryItem,
    anim: StampAnim,
    geo: StampGeometry,
    counter: Int,
    year: Int,
    onOpenHistory: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val announce = stringResource(R.string.stamp_a11y, book.title)
    val historyTitle = stringResource(R.string.history_title)
    AppCard(modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        // Екранният четец чете „Върната: заглавие“ веднъж, при появата и при смяна на книгата.
        Box(
            Modifier.fillMaxWidth().clearAndSetSemantics {
                contentDescription = announce
                liveRegion = LiveRegionMode.Polite
            },
        ) {
            Row(
                Modifier.fillMaxWidth().padding(14.dp).graphicsLayer {
                    val e = anim.enter.value
                    alpha = e
                    translationY = (1f - e) * 12.dp.toPx()
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .onGloballyPositioned { geo.cover = it.boundsInRoot() }
                        .graphicsLayer {
                            // Плъзга се към иконата на „Прочетени книги“, смалява се и чезне на края.
                            val s = anim.slide.value
                            val c = geo.cover
                            val t = geo.target
                            if (c != null && t != null) {
                                translationX = (t.center.x - c.center.x) * s
                                translationY = (t.center.y - c.center.y) * s
                            }
                            val k = 1f - 0.72f * s
                            scaleX = k
                            scaleY = k
                            alpha = 1f - ((s - 0.75f) / 0.25f).coerceIn(0f, 1f)
                        },
                ) {
                    BookCover(CatalogBook(inv = book.inv ?: 0L, author = book.author, title = book.title), width = 64.dp, showStatusDot = false, decorative = true)
                }
                Spacer(Modifier.width(14.dp))
                Column(
                    Modifier.weight(1f).graphicsLayer { alpha = 1f - (anim.slide.value * 1.6f).coerceAtMost(1f) },
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(book.title, style = MaterialTheme.typography.titleMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    if (book.author.isNotBlank()) {
                        Text(book.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    Formatters.shortDate(context, book.dateIn)?.let {
                        Text(stringResource(R.string.stamp_returned_on, it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.width(84.dp))
            }
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .onGloballyPositioned { geo.seal = it.boundsInRoot() }
                    .graphicsLayer { alpha = 1f - (anim.slide.value * 1.6f).coerceAtMost(1f) },
            ) {
                ReturnSeal(anim, Modifier.size(100.dp))
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            Modifier
                .fillMaxWidth()
                .then(if (onOpenHistory != null) Modifier.clickable(role = Role.Button, onClick = onOpenHistory) else Modifier)
                .heightIn(min = 56.dp)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.AutoStories, contentDescription = null,
                tint = LocalExtendedColors.current.gold,
                modifier = Modifier.size(28.dp).onGloballyPositioned { geo.target = it.boundsInRoot() },
            )
            Spacer(Modifier.width(12.dp))
            Text(historyTitle, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            AnimatedCounter(counter, pluralStringResource(R.plurals.stamp_year_label, counter, year), animate = false)
            if (onOpenHistory != null) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * Кръглият печат. Пада от x1,8 до x1 с лек наклон (−16° → −8°), след удара се „смачква“ за миг;
 * мастилото се разлива с шум (AGSL, Android 13+, [InkShader]) или — на 8–12 и при счупен шейдър —
 * с проста прозрачност. Цветът е като на истински печат: бордо върху светло, светло злато върху тъмно.
 */
@Composable
internal fun ReturnSeal(anim: StampAnim, modifier: Modifier = Modifier) {
    val ink = if (LocalExtendedColors.current.isDark) Brand.GoldLight else Brand.Burgundy
    val agsl = LocalAgslEnabled.current && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val shader = remember(agsl) { if (agsl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) InkShader.createOrNull() else null }
    val density = LocalDensity.current
    // Текстът на печата е част от рисунката — не расте с големия шрифт (иначе излиза от кръга).
    fun fixed(size: Float) = with(density) { size.dp.toSp() }
    val top = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = fixed(15f), letterSpacing = fixed(1.1f), color = ink)
    val bottom = TextStyle(fontFamily = Cormorant, fontStyle = FontStyle.Italic, fontSize = fixed(13f), color = ink)
    Box(
        modifier
            .graphicsLayer {
                val p = anim.press.value
                val s = (1.8f - 0.8f * p) * anim.squash.value
                scaleX = s
                scaleY = s
                rotationZ = -16f + 8f * p
                // Без шейдър мастилото е само прозрачност (по-светло, докато се „попива“).
                alpha = (p * 4f).coerceAtMost(1f) * (if (shader == null) 0.45f + 0.55f * anim.ink.value else 1f)
                compositingStrategy = if (anim.ink.value < 1f && shader != null) CompositingStrategy.Offscreen else CompositingStrategy.Auto
            }
            .drawWithContent {
                drawContent()
                val p = anim.ink.value
                if (p < 1f && shader != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shader.update(size.width, size.height, p)) {
                    drawRect(shader.brush, blendMode = BlendMode.DstIn)
                }
            }
            .drawWithCache {
                val ring = Stroke(2.6.dp.toPx())
                val thin = Stroke(1.1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 3.dp.toPx())))
                val r = size.minDimension / 2f
                onDrawBehind {
                    drawCircle(ink, radius = r - ring.width / 2f, style = ring)
                    drawCircle(ink.copy(alpha = 0.85f), radius = r - 8.dp.toPx(), style = thin)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.stamp_seal_top), style = top, textAlign = TextAlign.Center, maxLines = 1)
            Spacer(
                Modifier.padding(vertical = 3.dp).width(44.dp).height(7.dp).drawWithCache {
                    val diamond = Path().apply {
                        val c = Offset(size.width / 2f, size.height / 2f)
                        val d = 3.dp.toPx()
                        moveTo(c.x, c.y - d); lineTo(c.x + d, c.y); lineTo(c.x, c.y + d); lineTo(c.x - d, c.y); close()
                    }
                    val line = 1.dp.toPx()
                    onDrawBehind {
                        val y = size.height / 2f
                        drawLine(ink, Offset(0f, y), Offset(size.width / 2f - 6.dp.toPx(), y), strokeWidth = line)
                        drawLine(ink, Offset(size.width / 2f + 6.dp.toPx(), y), Offset(size.width, y), strokeWidth = line)
                        drawPath(diamond, ink)
                    }
                },
            )
            Text(stringResource(R.string.stamp_seal_bottom), style = bottom, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}

/**
 * AGSL маската на мастилото: на всяка точка праг от шум (и лек наклон към средата); с [uProgress]
 * от 0 към 1 мастилото „попива“ на петна, докато покрие целия печат. Ползва се с DstIn —
 * от рисунката остава само част, равна на маската. При 1 е изцяло плътна.
 */
internal object InkShaders {
    const val BLEED: String = """
uniform float2 uSize;
uniform float uProgress;

float hash(float2 p) {
    return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453);
}

float vnoise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash(i);
    float b = hash(i + float2(1.0, 0.0));
    float c = hash(i + float2(0.0, 1.0));
    float d = hash(i + float2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

half4 main(float2 p) {
    float scale = max(max(uSize.x, uSize.y), 1.0);
    float2 q = p / scale * 14.0;
    float n = vnoise(q) * 0.6 + vnoise(q * 2.3) * 0.4;
    float2 c = (p - uSize * 0.5) / (0.5 * scale);
    float r = clamp(length(c), 0.0, 1.0);
    float threshold = n * 0.75 + r * 0.25;
    float a = smoothstep(threshold, threshold + 0.35, uProgress * 1.35);
    return half4(a, a, a, a);
}
"""
}

/** RuntimeShader-ът на мастилото (API 33+); при грешка — проста прозрачност. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class InkShader private constructor(private val guarded: GuardedShader) {
    val brush get() = guarded.brush

    fun update(w: Float, h: Float, progress: Float): Boolean = guarded.guard {
        setFloatUniform("uSize", w.coerceAtLeast(1f), h.coerceAtLeast(1f))
        setFloatUniform("uProgress", progress)
    }

    companion object {
        const val TAG = "ink-shader"
        fun createOrNull(): InkShader? = GuardedShader.create(InkShaders.BLEED, TAG)?.let(::InkShader)
    }
}
