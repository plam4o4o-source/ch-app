package org.chyavorec.app.ui.components

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Rect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Флагът „намалено движение“, прочетен веднъж в корена ([observeReducedMotion]) и
 * подаден надолу — елементите на списъците не четат системните настройки поотделно.
 * `null` = не е подаден (preview, екран извън корена) → чете се на място.
 */
val LocalReducedMotion = staticCompositionLocalOf<Boolean?> { null }

private fun readReducedMotion(context: android.content.Context): Boolean =
    Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

/**
 * Чете системната настройка и я опреснява при всяко връщане към приложението
 * (ON_RESUME) — за подаване в [LocalReducedMotion] веднъж близо до корена.
 */
@Composable
fun observeReducedMotion(): Boolean {
    val context = LocalContext.current
    var reduced by remember { mutableStateOf(readReducedMotion(context)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { reduced = readReducedMotion(context) }
    return reduced
}

/** Дали потребителят е изключил анимациите (Достъпност → „Премахване на анимациите“). */
@Composable
fun rememberReducedMotion(): Boolean {
    val provided = LocalReducedMotion.current
    if (provided != null) return provided
    val context = LocalContext.current
    return remember { readReducedMotion(context) }
}

/**
 * Колко дълго вървят декоративните анимации (въртележката, анимациите в празните
 * екрани): ~30 s, после спират — иначе екранът, оставен отворен, рисува кадри без край
 * и харчи батерията. При намалено движение изобщо не тръгват.
 */
object MotionBudget {
    const val DECOR_MS = 30_000L
    /** Смяна на страницата във въртележката на „Начало“. */
    const val CAROUSEL_INTERVAL_MS = 6_000L

    /** Времето за [periodMs]-цикъл: цели цикли (поне един), колкото се побират в [budgetMs] — спира в края на цикъл. */
    fun loopMs(periodMs: Int, budgetMs: Long = DECOR_MS): Long {
        if (periodMs <= 0) return 0L
        return (budgetMs / periodMs).coerceAtLeast(1L) * periodMs
    }

    /** Колко пъти въртележката сменя страницата сама (5 по 6 s ≈ 30 s); с по-малко от 2 страници — нито веднъж. */
    fun carouselAdvances(itemCount: Int, intervalMs: Long = CAROUSEL_INTERVAL_MS, budgetMs: Long = DECOR_MS): Int =
        if (itemCount < 2 || intervalMs <= 0) 0 else (budgetMs / intervalMs).toInt()

    /**
     * Дали въртележката да превърти сама следващата страница: не при намалено движение,
     * докато потребителят я докосва/плъзга, когато приложението не е на преден план
     * (не е RESUMED), и не след като бюджетът ([carouselAdvances]) е изчерпан.
     */
    fun carouselAutoAdvance(reduced: Boolean, touched: Boolean, resumed: Boolean, advancesDone: Int, itemCount: Int): Boolean =
        !reduced && !touched && resumed && advancesDone < carouselAdvances(itemCount)
}

/**
 * Плавно появяване на елемент от списък (fade + лек slide) с малко закъснение по
 * индекс. Изпълнява се веднъж за елемента (състоянието оцелява при скролиране) и
 * само за първите [maxStaggered] елемента — по-нататъшните се показват веднага.
 *
 * Евтино за списъците: при намалено движение не прави нищо; щом елементът се
 * е появил (или е извън първите), не добавя нищо към модификатора. Самата
 * анимация е [Modifier.Node] — стойността се чете само в слоя (без рекомпозиция
 * на всеки кадър). Променен [maxStaggered] след началото не прекъсва вече
 * започнала анимация.
 */
@Composable
fun Modifier.animateEntrance(index: Int, maxStaggered: Int = 10): Modifier {
    if (rememberReducedMotion()) return this
    var shown by rememberSaveable { mutableStateOf(index >= maxStaggered) }
    if (shown) return this
    return this then EntranceElement(delayMillis = index * 45L, onFinished = { shown = true })
}

private class EntranceElement(
    private val delayMillis: Long,
    private val onFinished: () -> Unit,
) : ModifierNodeElement<EntranceNode>() {
    override fun create() = EntranceNode(delayMillis, onFinished)
    override fun update(node: EntranceNode) {
        node.onFinished = onFinished
    }
    // Ламбдата пише в едно и също (запомнено) състояние — не участва в сравнението.
    override fun equals(other: Any?) = other is EntranceElement && other.delayMillis == delayMillis
    override fun hashCode() = delayMillis.hashCode()
    override fun InspectorInfo.inspectableProperties() {
        name = "animateEntrance"
        properties["delayMillis"] = delayMillis
    }
}

private class EntranceNode(private val delayMillis: Long, var onFinished: () -> Unit) : Modifier.Node(), LayoutModifierNode {
    private val progress = Animatable(0f)
    private val layer: GraphicsLayerScope.() -> Unit = {
        val p = progress.value
        alpha = p
        translationY = (1f - p) * 22.dp.toPx()
    }

    override fun onAttach() {
        coroutineScope.launch {
            delay(delayMillis)
            progress.animateTo(1f, tween(420, easing = FastOutSlowInEasing))
            onFinished()
        }
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) { placeable.placeWithLayer(0, 0, layerBlock = layer) }
    }
}

/* ---- Shared element преходи между списък и детайли (Navigation Compose) ---- */

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalNavAnimatedScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * false по време на смяна на раздел от долната лента: тогава екраните се сменят
 * по споделената ос X и „прелитащи“ корици/снимки между два раздела биха се
 * засекли с плъзгането (напр. една и съща книга в „Начало“ и „Каталог“).
 */
val LocalSharedTransitionsEnabled = compositionLocalOf { true }

/**
 * Пружинен преход на границите на shared element-а (леко „подскачане“ в края) —
 * при прекъснат жест „назад“ пружината естествено се връща без скок.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
private val SpringBounds = BoundsTransform { _: Rect, _: Rect ->
    spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow, visibilityThreshold = Rect.VisibilityThreshold)
}

/**
 * Дали shared element преходите са активни тук: в навигацията, без „намалено
 * движение“ и не при смяна на раздел. Извън навигация (preview, тестове) — false.
 */
@Composable
fun sharedTransitionsActive(): Boolean {
    val reduced = rememberReducedMotion()
    return LocalSharedScope.current != null && LocalNavAnimatedScope.current != null &&
        LocalSharedTransitionsEnabled.current && !reduced
}

/**
 * Свързва елемент с общ [key] между два екрана (напр. корицата на книга в
 * списъка и в детайлите), така че да „прелети“ при навигация. Ключовете са
 * стабилни идентификатори: `book-<инв. №>`, `news-<id>`, `event-title-<id>`…
 * Извън навигация (preview, тестове) и при намалено движение е no-op.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedElementKey(key: String): Modifier {
    val shared = LocalSharedScope.current ?: return this
    val nav = LocalNavAnimatedScope.current ?: return this
    val reduced = rememberReducedMotion()
    if (reduced || !LocalSharedTransitionsEnabled.current) return this
    return with(shared) {
        this@sharedElementKey.sharedElement(rememberSharedContentState(key), animatedVisibilityScope = nav, boundsTransform = SpringBounds)
    }
}

/**
 * Като [sharedElementKey], но за елементи с различно съдържание в двата екрана
 * (заглавие с различен размер на шрифта, картата в списъка и на цял екран):
 * общи са само границите, а съдържанието се мащабира и преминава с fade.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedBoundsKey(key: String): Modifier {
    val shared = LocalSharedScope.current ?: return this
    val nav = LocalNavAnimatedScope.current ?: return this
    val reduced = rememberReducedMotion()
    if (reduced || !LocalSharedTransitionsEnabled.current) return this
    return with(shared) {
        this@sharedBoundsKey.sharedBounds(rememberSharedContentState(key), animatedVisibilityScope = nav, boundsTransform = SpringBounds)
    }
}
