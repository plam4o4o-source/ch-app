package org.chyavorec.app.ui.components

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.navigation.DETAIL_KEY
import org.chyavorec.app.ui.navigation.DETAIL_STACK_KEY
import org.chyavorec.app.ui.navigation.FROM_DETAIL_KEY
import org.chyavorec.app.ui.navigation.detailStack
import org.chyavorec.app.ui.navigation.popDetail
import org.chyavorec.app.ui.navigation.showDetail
import org.chyavorec.app.ui.theme.AppMotion

/** Колко се измества детайлът към списъка при пълен жест „назад“. */
private val BACK_SHIFT = 64.dp

/** Смяна на детайла: новият влиза от толкова встрани (и избледнява). */
private val SWAP_SHIFT = 12.dp

/**
 * Прозорец за списък и детайл един до друг: поне 840 dp ширина (таблет, Chromebook
 * хоризонтално) и не нисък — телефонът хоризонтално (под 480 dp височина) остава с отделни екрани.
 */
@Composable
fun isTwoPaneWindow(): Boolean = windowWidth() >= 840.dp && windowHeight() >= 480.dp

/**
 * Детайлът отдясно — за навигацията от самия него. Връзка към друг екран на същия панел,
 * извикана през [fromDetail], отива над детайла („Назад“ се връща към него), а не на мястото му.
 */
class DetailPane internal constructor(private val state: SavedStateHandle) {
    fun fromDetail(block: () -> Unit) {
        state[FROM_DETAIL_KEY] = true
        try {
            block()
        } finally {
            state.remove<Boolean>(FROM_DETAIL_KEY)
        }
    }
}

/** Панелът, в който е съдържанието (`null` — не е отдясно на списък). */
val LocalDetailPane = staticCompositionLocalOf<DetailPane?> { null }

/**
 * Списък с детайл отдясно (таблет): избраният екран (маршрутът му) е в [state] — състоянието
 * на списъка в навигацията, затова се пази при завъртане, смяна на раздела и след спиране на
 * процеса. „Назад“ първо връща детайла назад (екраните под него — виж pushDetail) и го затваря,
 * после списъка. На тесен прозорец (завъртане, разделен екран) избраният детайл продължава на
 * цял екран над списъка ([navigate]). TalkBack чете първо списъка, после детайла. С
 * [placeholder] дясната част е винаги там (на широк прозорец) и без избран детайл показва него —
 * за разделите-хъбове, чиито плочки иначе биха се разтеглили по целия таблет.
 *
 * Всеки показан детайл има свои ViewModel-и ([DetailStores]): затворен или сменен — изчистват се
 * (иначе напр. викторината би показала вчерашния резултат, докато разделът е жив).
 */
@Composable
fun ListDetailPane(
    state: SavedStateHandle,
    navigate: (String) -> Unit,
    detail: @Composable (route: String, close: () -> Unit) -> Unit,
    twoPane: Boolean = isTwoPaneWindow(),
    placeholder: (@Composable () -> Unit)? = null,
    list: @Composable () -> Unit,
) {
    val selectedFlow = remember(state) { state.getStateFlow<String?>(DETAIL_KEY, null) }
    val selected by selectedFlow.collectAsStateWithLifecycle()
    val stackFlow = remember(state) { state.getStateFlow<List<String>>(DETAIL_STACK_KEY, ArrayList()) }
    val stack by stackFlow.collectAsStateWithLifecycle()
    val close: () -> Unit = { state.popDetail() }
    LaunchedEffect(twoPane, selected) {
        val route = selected
        if (!twoPane && route != null) {
            // На цял екран — и екраните под детайла, за да води „Назад“ към тях, както в панела.
            val below = state.detailStack()
            state.showDetail(null)
            (below + route).forEach(navigate)
        }
    }
    val shown = selected?.takeIf { twoPane }
    val parent = checkNotNull(LocalViewModelStoreOwner.current) { "ListDetailPane без ViewModelStoreOwner" }
    val stores = viewModel(parent, key = "list-detail-stores") { DetailStores() }
    // Състоянието на екраните под детайла (превъртане и др.) — до връщането към тях.
    val saveable = rememberSaveableStateHolder()
    SideEffect {
        val keep = if (shown != null) (stack + shown).toSet() else emptySet()
        stores.keep(keep).forEach(saveable::removeState)
    }
    val pane = remember(state) { DetailPane(state) }
    // Предсказуемо „назад“: детайлът се плъзга към списъка и избледнява с жеста; пуснат —
    // затваря се, отказан — пружината го връща. При намалено движение — само избледнява.
    val reduced = rememberReducedMotion()
    val scope = rememberCoroutineScope()
    val back = remember { Animatable(0f) }
    // Нов (или затворен) детайл — без остатък от предишния жест.
    LaunchedEffect(shown) { back.snapTo(0f) }
    PredictiveBackHandler(enabled = shown != null) { events ->
        try {
            events.collect { back.snapTo(it.progress) }
            close()
        } catch (e: CancellationException) {
            scope.launch { back.animateTo(0f, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) }
            throw e
        }
    }
    val shift = with(LocalDensity.current) { SWAP_SHIFT.roundToPx() }
    // Списъкът е винаги първото дете — при избор не губи превъртането и състоянието си.
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxHeight().semantics { isTraversalGroup = true; traversalIndex = 0f }) { list() }
        if (shown != null || (twoPane && placeholder != null)) {
            VerticalDivider()
            Box(
                Modifier
                    .weight(1.4f)
                    .fillMaxHeight()
                    .graphicsLayer {
                        val p = back.value
                        alpha = 1f - 0.6f * p
                        if (!reduced) translationX = -p * BACK_SHIFT.toPx()
                    }
                    .semantics { isTraversalGroup = true; traversalIndex = 1f },
            ) {
                // Смяна на раздела: новият избледнява навътре с леко плъзгане (назад в панела — от
                // другата страна); старият само избледнява. След жест „назад“ — без втори изход.
                AnimatedContent(
                    targetState = PaneTarget(shown, stack.size),
                    contentKey = { it.route },
                    transitionSpec = {
                        val dir = if (targetState.depth < initialState.depth) -1 else 1
                        val enter = if (reduced) EnterTransition.None
                        else fadeIn(tween(AppMotion.ShortMs)) + slideInHorizontally(tween(AppMotion.ShortMs)) { dir * shift }
                        val exit = if (reduced || back.value > 0f) ExitTransition.None else fadeOut(tween(AppMotion.QuickMs))
                        (enter togetherWith exit).using(null)
                    },
                    label = "detail",
                ) { target ->
                    val route = target.route
                    if (route == null) {
                        placeholder?.invoke()
                    } else {
                        val owner = remember(route) { DetailOwner(stores.store(route), parent) }
                        DisposableEffect(route) {
                            stores.attach(route)
                            onDispose { stores.detach(route).forEach(saveable::removeState) }
                        }
                        // Корицата е и в списъка, и в детайла едновременно — без shared element.
                        CompositionLocalProvider(
                            LocalSharedTransitionsEnabled provides false,
                            LocalViewModelStoreOwner provides owner,
                            LocalDetailPane provides pane,
                        ) {
                            saveable.SaveableStateProvider(route) { detail(route, close) }
                        }
                    }
                }
            }
        }
    }
}

/** Какво е отдясно: маршрутът (`null` — заместителят) и колко екрана има под него. */
private data class PaneTarget(val route: String?, val depth: Int)

/**
 * Хранилищата на ViewModel-ите на детайлите отдясно — по едно на маршрут. Хранилището живее,
 * докато маршрутът е показан, под показания (стекът на панела) или още излиза с анимацията;
 * после се изчиства. Самият държател е ViewModel на списъка — преживява завъртането (и
 * хранилищата с него), а с изчистването на списъка изчиства и всички тях.
 */
internal class DetailStores : ViewModel() {
    private val stores = HashMap<String, ViewModelStore>()
    private val composed = HashMap<String, Int>()
    private var keep: Set<String> = emptySet()

    /** Маршрутите с хранилище (за тестовете). */
    val routes: Set<String> get() = stores.keys.toSet()

    fun store(route: String): ViewModelStore = stores.getOrPut(route) { ViewModelStore() }

    /** Нужните маршрути (показаният и тези под него); връща изчистените. */
    fun keep(routes: Set<String>): List<String> {
        keep = routes
        return trim()
    }

    fun attach(route: String) {
        composed[route] = (composed[route] ?: 0) + 1
    }

    /** Съдържанието на [route] излезе от композицията; връща изчистените маршрути. */
    fun detach(route: String): List<String> {
        val n = (composed[route] ?: 1) - 1
        if (n <= 0) composed.remove(route) else composed[route] = n
        return trim()
    }

    private fun trim(): List<String> {
        val gone = stores.keys.filter { it !in keep && it !in composed }
        gone.forEach { stores.remove(it)?.clear() }
        return gone
    }

    override fun onCleared() {
        stores.values.forEach(ViewModelStore::clear)
        stores.clear()
    }
}

/**
 * Собственикът на ViewModel-ите на един детайл. Фабриката и CreationExtras са тези на списъка
 * (приложението, аргументите, запазеното състояние), но SavedStateHandle на детайла е в неговото
 * хранилище — изчиства се с него.
 */
private class DetailOwner(override val viewModelStore: ViewModelStore, private val parent: ViewModelStoreOwner) :
    ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    private val parentDefaults get() = parent as? HasDefaultViewModelProviderFactory

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = parentDefaults?.defaultViewModelProviderFactory ?: object : ViewModelProvider.Factory {}

    override val defaultViewModelCreationExtras: CreationExtras
        get() = MutableCreationExtras(parentDefaults?.defaultViewModelCreationExtras ?: CreationExtras.Empty).apply {
            this[VIEW_MODEL_STORE_OWNER_KEY] = this@DetailOwner
        }
}
