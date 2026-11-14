package org.chyavorec.app.ui.screens.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/**
 * Дълбочина на снимката във въртележката на „Начало“ — като прозорец: златната рамка стои, а
 * снимката зад нея е малко по-едра и се движи по-бавно от картата.
 * - плъзгане встрани: снимката изостава с [SWIPE_SHARE] от изместването на картата;
 * - превъртане на страницата: снимката се плъзга до [DRIFT_DP] спрямо картата.
 *
 * Всичко е в слоя на снимката (graphicsLayer) и се чете само при рисуване: без прекомпозиция и
 * без работа в покой. Изместванията ги пише `onGloballyPositioned` (след подредбата, само при
 * промяна на положението); нулата е мястото на картата, когато списъкът е най-горе ([HeroAnchor]).
 * При намалено движение няма нито слой, нито наблюдател (снимката е както досега).
 */
internal object HeroParallax {
    /** Дял от изместването на картата, с който снимката изостава (≈8 %). */
    const val SWIPE_SHARE = 0.08f

    /** Най-голямото плъзгане на снимката при превъртане на страницата (dp). */
    const val DRIFT_DP = 12f

    /** Най-малкият мащаб на снимката — полето, зад което се движи, без да се показват краища. */
    const val MIN_SCALE = 1.08f

    /** Мащабът, при който плъзгането по височина ([drift] px) не оголва краищата на снимка с височина [height] px. */
    fun scale(height: Float, drift: Float): Float =
        if (height <= 0f) MIN_SCALE else maxOf(MIN_SCALE, 1f + 2f * drift / height)

    /** Хоризонталното изместване на снимката (px), когато картата е на [cardOffset] px от центъра; не излиза от [margin]. */
    fun swipeShift(cardOffset: Float, margin: Float): Float = (0f - cardOffset * SWIPE_SHARE).coerceIn(-margin, margin)

    /** Вертикалното изместване (px) при превъртане: [scroll] е изминатото като дял от половин прозорец (−1..1). */
    fun scrollShift(scroll: Float, drift: Float, margin: Float): Float = (scroll.coerceIn(-1f, 1f) * drift).coerceIn(-margin, margin)

    /** Дали наблюдаваната стойност се е променила достатъчно, за да струва нов кадър. */
    fun moved(old: Float, new: Float, eps: Float): Boolean = abs(new - old) > eps
}

/**
 * Общите за въртележката мерки: центърът на лентата и покоят на картата (нулата на превъртането).
 * Живее над списъка ([rememberHeroAnchor]) — не се губи, когато редът на въртележката излезе от
 * екрана и се върне, нито при смяна на раздела (пази се в състоянието на екрана).
 */
@Stable
internal class HeroAnchor(rest: Float = Float.NaN) {
    var centerX = Float.NaN

    /** Горният ръб на картата спрямо списъка (px), когато списъкът е най-горе. */
    var rest = rest
        private set

    /** Списъкът — превъртането се мери спрямо него, не спрямо прозореца (преходите местят и двата). */
    var list: LayoutCoordinates? = null

    /** Точното превъртане на списъка (px), докато първият му ред се вижда; иначе null. */
    var topScroll: () -> Int? = { null }

    /**
     * Колко е превъртяна картата от покоя (px), когато е на [y] px под горния ръб на списъка.
     * Покоят се (пре)смята точно, докато първият ред се вижда; иначе — запазеният; ако го няма
     * (непознат списък), е първото видяно място, както преди.
     */
    fun scrolledBy(y: Float): Float {
        topScroll()?.let { rest = y + it }
        if (rest.isNaN()) rest = y
        return rest - y
    }

    companion object {
        val Saver: Saver<HeroAnchor, Float> = Saver(save = { it.rest }, restore = { HeroAnchor(it) })
    }
}

/** [HeroAnchor] за въртележката в списъка [list] — създава се над него (вж. [heroList]). */
@Composable
internal fun rememberHeroAnchor(list: LazyListState): HeroAnchor {
    val anchor = rememberSaveable(saver = HeroAnchor.Saver) { HeroAnchor() }
    anchor.topScroll = remember(list) { { if (list.firstVisibleItemIndex == 0) list.firstVisibleItemScrollOffset else null } }
    return anchor
}

/** Списъкът, в който е въртележката — еталонът на превъртането за [heroTrack]. */
internal fun Modifier.heroList(anchor: HeroAnchor): Modifier = onPlaced { anchor.list = it }

/** Изместването на една карта спрямо покой — пише се от [heroTrack], чете се в слоя на снимката. */
@Stable
internal class HeroShift {
    /** Центърът на картата спрямо центъра на лентата (px): 0 в покой, ± при плъзгане. */
    var cardOffset by mutableFloatStateOf(0f)

    /** Превъртането на страницата от покоя, като дял от половин прозорец. */
    var scroll by mutableFloatStateOf(0f)
}

/** Центърът на лентата с карти — еталонът за [HeroShift.cardOffset]; без [enabled] не прави нищо. */
internal fun Modifier.heroContainer(anchor: HeroAnchor, enabled: Boolean): Modifier =
    if (!enabled) this else onGloballyPositioned { c ->
        anchor.centerX = c.positionInWindow().x + c.size.width / 2f
    }

/** Следи положението на картата (плъзгане и превъртане); без [enabled] не прави нищо. */
internal fun Modifier.heroTrack(anchor: HeroAnchor, shift: HeroShift, enabled: Boolean): Modifier =
    if (!enabled) this else onGloballyPositioned { c ->
        val p = c.positionInWindow()
        val list = anchor.list?.takeIf { it.isAttached }
        val y = if (list != null) list.localPositionOf(c, Offset.Zero).y else p.y
        val half = (c.findRootCoordinates().size.height / 2f).coerceAtLeast(1f)
        val offset = if (anchor.centerX.isNaN()) 0f else p.x + c.size.width / 2f - anchor.centerX
        val scroll = anchor.scrolledBy(y) / half
        if (HeroParallax.moved(shift.cardOffset, offset, 0.25f)) shift.cardOffset = offset
        if (HeroParallax.moved(shift.scroll, scroll, 0.002f)) shift.scroll = scroll
    }

/** Слоят на снимката: мащаб и изместване от [HeroShift], прочетени само при рисуване; без [enabled] — нищо. */
internal fun Modifier.heroPhotoLayer(shift: HeroShift, enabled: Boolean): Modifier =
    if (!enabled) this else graphicsLayer {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@graphicsLayer
        val drift = HeroParallax.DRIFT_DP.dp.toPx()
        val s = HeroParallax.scale(h, drift)
        scaleX = s
        scaleY = s
        translationX = HeroParallax.swipeShift(shift.cardOffset, (s - 1f) * w / 2f)
        translationY = HeroParallax.scrollShift(shift.scroll, drift, (s - 1f) * h / 2f)
    }

/** Рамката на въртележката се изрисува само при първото показване за процеса. */
internal object HeroFrameReveal {
    @Volatile var played = false
}
