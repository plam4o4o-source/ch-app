package org.chyavorec.app.ui.screens.my

import kotlin.math.abs
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.ScrollState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.heading
import androidx.compose.material3.TextButton
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.components.rememberIsTouchExplorationEnabled
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.share.CardEmblem
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareImageHost
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.rememberShareImageState
import org.chyavorec.app.ui.share.sans
import org.chyavorec.app.ui.share.serif
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.SystemBarIcons
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.repository.AuthState

/** Колко трае един слайд при автоматично превъртане. */
internal const val SLIDE_MS = 5_500

/**
 * Състоянието на историята: още се изчислява, готова, празна (няма книги през годината) или
 * грешка (историята на заеманията не е на телефона и не се изтегли — напр. без връзка).
 */
sealed interface YearStoryState {
    data object Loading : YearStoryState
    data object Empty : YearStoryState
    data class Error(val error: AppError) : YearStoryState
    data class Ready(val summary: ReadingYear.Summary) : YearStoryState
}

/**
 * Изчислените в „Моето“ години ([ReadingStatsViewModel]) — историята се отваря от тях веднага,
 * без нови справки в каталога. Валидни са само за същата кеширана история
 * ([org.chyavorec.data.repository.LibraryRepository.historyVersion]); при смяна на читателя — изчистват се.
 */
class ReadingYearMemo {
    private class Entry(val version: Long, val years: Map<Int, ReadingYear.Summary>)

    @Volatile private var entry: Entry? = null

    fun put(version: Long, years: List<ReadingYear.Summary>) {
        val old = entry?.takeIf { it.version == version }?.years.orEmpty()
        entry = Entry(version, old + years.associateBy { it.year })
    }

    fun get(version: Long, year: Int): ReadingYear.Summary? = entry?.takeIf { it.version == version }?.years?.get(year)

    fun clear() {
        entry = null
    }
}

/**
 * „Моята читателска година“ за [year] — от паметта на „Моето“ ([ReadingYearMemo]) или от
 * кешираната история (иначе веднъж от сървъра) и каталога в базата. Справките в каталога
 * (десетки заявки към базата) вървят извън главната нишка: иначе всяко връщане от базата
 * чака кадър на анимацията за зареждане и историята се отваря за секунди.
 */
class ReadingYearViewModel(private val year: Int, private val c: AppContainer) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow<YearStoryState>(YearStoryState.Loading)
    val state: StateFlow<YearStoryState> = _state.asStateFlow()
    private var job: Job? = null

    override val reconnectState: ScreenState<*>
        get() = when (val s = _state.value) {
            YearStoryState.Loading -> ScreenState<Unit>(loading = true)
            is YearStoryState.Error -> ScreenState<Unit>(loading = false, error = s.error)
            else -> ScreenState<Unit>(data = Unit, loading = false)
        }

    init {
        load()
    }

    override fun refreshOnReconnect() {
        load()
    }

    /** „Опитай отново“ и връщането на връзката. */
    fun load() {
        if (job?.isActive == true) return
        _state.value = YearStoryState.Loading
        job = viewModelScope.launch { _state.value = compute() }
    }

    private suspend fun compute(): YearStoryState = withContext(Dispatchers.Default) {
        if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
        if (c.authRepository.state.value !is AuthState.SignedIn) return@withContext YearStoryState.Empty
        val version = c.libraryRepository.historyVersion.value
        c.readingYears.get(version, year)?.let { return@withContext YearStoryState.Ready(it) }
        val history = c.libraryRepository.cachedHistory()?.data ?: when (val r = c.libraryRepository.history(force = false)) {
            is Outcome.Success -> r.value.data
            // Без връзка (или сървърът не отговаря) — грешка с „Опитай отново“, не „няма книги“.
            is Outcome.Failure -> return@withContext when (r.error) {
                AppError.Unauthorized, AppError.NotFound, is AppError.NotAvailable -> YearStoryState.Empty
                else -> YearStoryState.Error(r.error)
            }
        }
        if (history.isEmpty()) return@withContext YearStoryState.Empty
        val books = readingBooks(c, history)
        ReadingYear.compute(history, books, year, c.clock.zone())
            ?.also { c.readingYears.put(version, listOf(it)) }
            ?.let { YearStoryState.Ready(it) }
            ?: YearStoryState.Empty
    }
}

/** Екранът (маршрут, не диалог): тъмен, на цял екран, със светли икони в системните ленти. */
@Composable
fun YearStoryScreen(year: Int, onClose: () -> Unit) {
    val vm = appViewModel(key = "reading-year-$year") { ReadingYearViewModel(year, it) }
    val state by vm.state.collectAsStateWithLifecycle()
    SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
    Box(Modifier.fillMaxSize().background(Brand.Ink), contentAlignment = Alignment.Center) {
        when (val s = state) {
            YearStoryState.Loading -> DelayedLoader()
            YearStoryState.Empty -> StoryMessage(stringResource(R.string.year_empty), text = null, onClose = onClose)
            is YearStoryState.Error -> StoryMessage(
                stringResource(if (s.error == AppError.Network) R.string.error_offline_title else R.string.year_error_title),
                text = stringResource(if (s.error == AppError.Network) R.string.year_offline_text else R.string.error_temporary),
                onClose = onClose,
                onRetry = vm::load,
            )
            is YearStoryState.Ready -> YearStory(s.summary, onClose, siteUrl = LocalAppContainer.current.config.siteBaseUrl)
        }
    }
}

/** Индикаторът — едва ако изчисляването трае (обикновено историята е готова за миг и не мигва). */
@Composable
private fun DelayedLoader() {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(LOADER_DELAY_MS)
        shown = true
    }
    if (shown) ChLoadingIndicator(color = Brand.Gold)
}

private const val LOADER_DELAY_MS = 350L

/** Празна година или грешка: заглавие, обяснение, „Опитай отново“ (при грешка) и „Затвори“. */
@Composable
internal fun StoryMessage(title: String, text: String?, onClose: () -> Unit, onRetry: (() -> Unit)? = null) {
    Column(
        Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).verticalScroll(rememberScrollState()).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title, style = serif(26.sp, Brand.Parchment), textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
        if (text != null) {
            Spacer(Modifier.height(10.dp))
            Text(text, style = sans(15.sp, Brand.GoldLight), textAlign = TextAlign.Center)
        }
        Spacer(Modifier.height(20.dp))
        if (onRetry != null) {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink),
                modifier = Modifier.heightIn(min = 48.dp),
            ) { Text(stringResource(R.string.action_retry)) }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onClose, colors = ButtonDefaults.textButtonColors(contentColor = Brand.Parchment), modifier = Modifier.heightIn(min = 48.dp)) {
                Text(stringResource(R.string.action_close))
            }
        } else {
            Button(onClick = onClose, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.action_close)) }
        }
    }
}

/**
 * Историята като в Instagram: лентички горе, които се пълнят (по ~5.5 s на слайд); докосване
 * вдясно — напред, вляво — назад; задържане — пауза; плъзгане надолу — затваряне. При
 * намалено движение — без автоматично превъртане и без ефекти; с TalkBack — паузирано,
 * всеки слайд е един възел с описание, а „Напред“/„Назад“ са бутони и действия на възела.
 */
@Composable
fun YearStory(summary: ReadingYear.Summary, onClose: () -> Unit, siteUrl: String, initialSlide: Int = 0) {
    val slides = remember(summary) { yearSlides(summary) }
    var index by rememberSaveable { mutableStateOf(initialSlide.coerceIn(0, slides.lastIndex)) }
    val reduced = rememberReducedMotion()
    val talkBack = rememberIsTouchExplorationEnabled()
    val auto = !reduced && !talkBack
    var held by remember { mutableStateOf(false) }
    val resumed by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val share = rememberShareImageState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Таймерът и влизането са на слайд — нов слайд, нови стойности.
    // „Назад“ на първия слайд го пуска отначало ([restart]).
    var restart by remember { mutableIntStateOf(0) }
    val timer = remember(index, restart) { Animatable(0f) }
    val appear = remember(index) { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(index, reduced) {
        if (reduced) appear.snapTo(1f) else appear.animateTo(1f, tween(1_800, easing = LinearEasing))
    }
    var scrolling by remember { mutableStateOf(false) }
    val paused = held || scrolling || share.busy || !resumed.isAtLeast(Lifecycle.State.RESUMED)
    LaunchedEffect(index, restart, auto, paused) {
        if (!auto || paused) return@LaunchedEffect
        timer.animateTo(1f, tween(((1f - timer.value) * SLIDE_MS).toInt().coerceAtLeast(1), easing = LinearEasing))
        if (index < slides.lastIndex) index++
    }
    fun next() {
        if (index < slides.lastIndex) index++ else onClose()
    }
    fun previous() {
        if (index > 0) index-- else restart++
    }

    // Плъзгане надолу — затваря (историята се смалява след пръста). Решава цялото изминато
    // разстояние ([dragY]) или скоростта при пускане. Жестът се хваща ИЗВЪН слоя, който се
    // мести и смалява, и в първата фаза (преди превъртането на слайда): иначе превъртаният
    // текст го взима, а изминатото и скоростта излизат по-малки (координатите вървят с пръста).
    var dragY by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val dismiss = remember(density) { StoryDismiss(density) }
    var settling by remember { mutableStateOf<Job?>(null) }
    val close by rememberUpdatedState(onClose)
    // Превъртането на текущия слайд: надолу от средата на текста — превърта, не затваря.
    var currentScroll by remember { mutableStateOf<ScrollState?>(null) }
    fun release(velocity: Float) {
        held = false
        if (dismiss.shouldClose(dragY, velocity)) {
            close()
        } else if (dragY > 0f) {
            settling = scope.launch { animate(dragY, 0f) { v, _ -> dragY = v } }
        }
    }
    fun pull(delta: Float) {
        settling?.cancel()
        dragY = (dragY + delta).coerceAtLeast(0f)
    }
    val nextLabel = stringResource(R.string.year_next)
    val previousLabel = stringResource(R.string.year_previous)
    val kind = slides[index]
    val shareSubject = stringResource(R.string.year_title_with, summary.year)
    val shareText = stringResource(R.string.year_share_text, summary.year, siteUrl)

    Box(
        Modifier.fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                    val tracker = VelocityTracker().apply { addPosition(down.uptimeMillis, down.position) }
                    var pulling = false
                    var last = down.position.y
                    while (true) {
                        val change = awaitPointerEvent(PointerEventPass.Initial).changes.firstOrNull { it.id == down.id } ?: break
                        tracker.addPosition(change.uptimeMillis, change.position)
                        if (!change.pressed) {
                            if (pulling) release(tracker.calculateVelocity().y)
                            break
                        }
                        if (!pulling) {
                            val dy = change.position.y - down.position.y
                            val dx = change.position.x - down.position.x
                            val atTop = currentScroll?.canScrollBackward != true
                            if (atTop && dy > viewConfiguration.touchSlop && dy > abs(dx)) {
                                pulling = true
                                held = true
                                last = change.position.y
                            }
                        }
                        if (pulling) {
                            pull(change.position.y - last)
                            last = change.position.y
                            change.consume()
                        }
                    }
                }
            }
            .pointerInput(slides.size) {
                detectTapGestures(
                    onPress = {
                        held = true
                        tryAwaitRelease()
                        held = false
                    },
                    onLongPress = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                    onTap = { o -> if (o.x < size.width * 0.3f) previous() else next() },
                )
            }
            .graphicsLayer {
                val f = (dragY / size.height).coerceIn(0f, 1f)
                translationY = dragY
                scaleX = 1f - 0.12f * f
                scaleY = scaleX
                if (dragY > 0f) {
                    shape = RoundedCornerShape(28.dp)
                    clip = true
                }
            },
    ) {
        AnimatedContent(
            targetState = index,
            transitionSpec = { if (reduced) fadeIn(tween(0)) togetherWith fadeOut(tween(0)) else fadeIn(tween(260)) togetherWith fadeOut(tween(200)) },
            label = "year-slide",
        ) { i ->
            val k = slides[i]
            val look = remember(k, summary) { lookOf(k, summary) }
            val current = i == index
            // Описанието — на самия слайд (не на текущия): при смяната и двата са на екрана.
            val description = slideDescription(summary, k)
            val stateText = stringResource(R.string.year_slide_state, i + 1, slides.size)
            val scroll = rememberScrollState()
            if (current) {
                SideEffect { currentScroll = scroll }
                // Докато читателят превърта текста — слайдът стои.
                LaunchedEffect(scroll) {
                    try {
                        snapshotFlow { scroll.isScrollInProgress }.collect { scrolling = it }
                    } finally {
                        scrolling = false
                    }
                }
            }
            BoxWithConstraints(Modifier.fillMaxSize()) {
                // Ниският екран (хоризонтално) — по-тесни отстъпи над и под слайда.
                val padding = if (maxHeight < 600.dp) StoryContentPaddingCompact else StoryContentPadding
                // Мотивът във фона бавно се завърта с таймера (само докато слайдът тече).
                SlideBackdrop(look, drift = { if (current) timer.value else 1f })
                SlideBody(
                    summary, k,
                    appear = { if (current) appear.value else 1f },
                    scroll = scroll,
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(padding)
                        .clearAndSetSemantics {
                            // Само текущият слайд: излизащият не се чете, а „учтивото“ съобщение е
                            // едно (иначе при преливането TalkBack чете два пъти).
                            if (!current) return@clearAndSetSemantics
                            contentDescription = description
                            stateDescription = stateText
                            liveRegion = LiveRegionMode.Polite
                            customActions = listOf(
                                CustomAccessibilityAction(nextLabel) { next(); true },
                                CustomAccessibilityAction(previousLabel) { previous(); true },
                            )
                        },
                )
            }
        }

        // Горе: лентичките на слайдовете, емблемата и „Затвори“.
        Column(Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 12.dp, vertical = 8.dp)) {
            StoryProgress(slides.size, index, progress = { if (auto) timer.value else 1f })
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CardEmblem(32.dp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.year_title), style = sans(13.sp, Brand.Parchment, FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(summary.year.toString(), style = sans(12.sp, Brand.GoldLight), maxLines = 1)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Brand.Parchment)
                }
            }
        }

        // Долу: „Сподели“ (картинка на текущия слайд), а с TalkBack — и „Назад“/„Напред“.
        Row(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (talkBack) {
                IconButton(onClick = ::previous, enabled = index > 0) {
                    Icon(Icons.Outlined.ChevronLeft, contentDescription = previousLabel, tint = Brand.Parchment)
                }
                Spacer(Modifier.width(12.dp))
            }
            Button(
                onClick = { share.share(ShareImageRequest(ShareCard.Year(summary, kind, siteUrl), shareSubject, shareText)) },
                enabled = !share.busy,
                colors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                if (share.busy) {
                    CircularProgressIndicator(Modifier.size(18.dp), color = Brand.Ink, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Share, null, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.action_share))
            }
            if (talkBack) {
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = ::next) {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = nextLabel, tint = Brand.Parchment)
                }
            }
        }
        ShareImageHost(share)
    }
}

/**
 * Кога плъзгането надолу затваря историята: изминати поне 140 dp (колкото и бавно) или
 * бързо пускане (от 700 dp/s) след поне 32 dp — по цялото изминато разстояние, не по едно събитие.
 */
internal class StoryDismiss(density: Density) {
    private val closeAt = with(density) { 140.dp.toPx() }
    private val flingFrom = with(density) { 32.dp.toPx() }
    private val flingVelocity = with(density) { 700.dp.toPx() }

    fun shouldClose(distance: Float, velocity: Float): Boolean =
        distance >= closeAt || (distance >= flingFrom && velocity >= flingVelocity)
}

/** Лентичките горе: минали — пълни, текущата — според [progress], следващите — празни. */
@Composable
private fun StoryProgress(count: Int, index: Int, progress: () -> Float) {
    Canvas(Modifier.fillMaxWidth().height(3.dp).clearAndSetSemantics { }) {
        val gap = 4.dp.toPx()
        val w = (size.width - gap * (count - 1)) / count
        val r = CornerRadius(size.height / 2f)
        for (i in 0 until count) {
            val x = i * (w + gap)
            drawRoundRect(Color.White.copy(alpha = 0.28f), Offset(x, 0f), Size(w, size.height), r)
            val fill = when {
                i < index -> 1f
                i == index -> progress().coerceIn(0f, 1f)
                else -> 0f
            }
            if (fill > 0f) drawRoundRect(Brand.Gold, Offset(x, 0f), Size(w * fill, size.height), r)
        }
    }
}

/**
 * Картата „Моята читателска година“ в „Моето“: през декември и януари — голяма, тъмна, със
 * златната година и шевица; през останалото време — скромен ред.
 */
@Composable
fun YearStoryCard(summary: ReadingYear.Summary, prominent: Boolean, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    val title = stringResource(R.string.year_title_with, summary.year)
    if (prominent) {
        val shape = MaterialTheme.shapes.large
        Surface(
            onClick = onOpen,
            shape = shape,
            color = Brand.Ink,
            contentColor = Brand.Parchment,
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp).border(1.dp, Brand.Gold.copy(alpha = 0.55f), shape),
        ) {
            Box(
                Modifier.drawWithCache {
                    val bg = Brush.linearGradient(listOf(Brand.Burgundy, Color(0xFF3A1A18), Brand.Ink), Offset.Zero, Offset(size.width, size.height))
                    val side = size.height * 1.3f
                    val motif = Path().apply {
                        fillType = PathFillType.EvenOdd
                        addShevitsaMotif(ShevitsaMotif.ROSETTE, size.width - side * 0.62f, -side * 0.15f, side)
                    }
                    onDrawBehind {
                        drawRect(bg)
                        drawPath(motif, Brand.Gold.copy(alpha = 0.10f))
                    }
                },
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(summary.year.toString(), style = serif(56.sp, Brand.Gold, lineHeight = 1f))
                    Text(stringResource(R.string.year_title), style = serif(26.sp, Brand.Parchment))
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.year_card_text), style = sans(14.sp, Brand.GoldLight))
                    Spacer(Modifier.height(14.dp))
                    ShevitsaStrip(ShevitsaMotif.ROSETTE, Brand.Gold.copy(alpha = 0.7f), Modifier.fillMaxWidth().height(8.dp))
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            pluralStringResource(R.plurals.history_year_count, summary.books, summary.books),
                            style = sans(14.sp, Brand.Parchment, FontWeight.SemiBold), modifier = Modifier.weight(1f),
                        )
                        Surface(shape = RoundedCornerShape(50), color = Brand.Gold, contentColor = Brand.Ink) {
                            Row(Modifier.heightIn(min = 40.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.year_card_open), style = MaterialTheme.typography.labelLarge)
                                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    } else {
        Surface(
            onClick = onOpen,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(Modifier.heightIn(min = 72.dp).padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(48.dp).clip(MaterialTheme.shapes.small).background(Brush.linearGradient(listOf(Brand.Burgundy, Brand.Ink))),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(summary.year.toString(), style = serif(17.sp, Brand.Gold))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        pluralStringResource(R.plurals.year_card_books, summary.books, summary.books),
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
