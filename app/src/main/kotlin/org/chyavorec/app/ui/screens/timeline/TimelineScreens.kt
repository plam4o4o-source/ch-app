package org.chyavorec.app.ui.screens.timeline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.FolkShapes
import org.chyavorec.app.ui.components.LeafRefreshBox
import org.chyavorec.app.ui.components.MessageView
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchInOrnament
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.rememberIsTouchExplorationEnabled
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.components.scaledPath
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.FolkTokens
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.TimelineRepository
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.timeline.OnThisDay
import org.chyavorec.domain.timeline.TimelineEntry

class TimelineViewModel(private val repo: TimelineRepository) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<TimelineEntry>>())
    val state: StateFlow<ScreenState<List<TimelineEntry>>> = _state.asStateFlow()

    init { refresh(false) }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.startRefresh() }
            _state.update { it.with(repo.timeline(force)) }
        }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() = refresh(false)
}

/**
 * „Лента на времето“: историята на читалището като шевица-конец, по който „пораства“ златна
 * медалия за всяка година. Конецът се избродира веднъж за веха, когато тя се появи на екрана.
 * Ако страницата „История“ на сайта е сменила вида си и не се разчита — сочи към обикновената страница.
 */
@Composable
fun TimelineScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { TimelineViewModel(it.timelineRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val historyUrl = LocalAppContainer.current.config.siteBaseUrl.trimEnd('/') + "/history"
    val pageTitle = stringResource(R.string.timeline_page_title)
    val listState = rememberLazyListState()
    // Голямото заглавие е в списъка; в лентата заглавието се показва, щом то излезе (или няма списък).
    val headerGone by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val listShown = state.data?.isNotEmpty() == true
    Scaffold(topBar = { BackTopBar(stringResource(R.string.timeline_title), onBack, titleShown = !listShown || headerGone) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.error is AppError.Parse && state.data == null) {
                MessageView(
                    Icons.Outlined.HistoryEdu, stringResource(R.string.timeline_fallback_title), stringResource(R.string.timeline_fallback),
                    actionLabel = stringResource(R.string.timeline_open_page), onAction = { navigate(Routes.page(historyUrl, pageTitle)) },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                StateContent(
                    state = state, onRetry = { vm.refresh() }, isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList(withImage = false) },
                    empty = { EmptyView(stringResource(R.string.timeline_empty), icon = Icons.Outlined.HistoryEdu) },
                    errorSubject = stringResource(R.string.subject_history),
                ) { entries ->
                    Timeline(entries, state.fromCache, state.syncedAt, state.refreshError, listState = listState)
                }
            }
        }
    }
}

/** От тази ширина вехите са от двете страни на конеца (таблет, хоризонтално). */
private val WideTimeline = 720.dp
private val WideCardMax = 440.dp

/** Редовете пред вехите в списъка: заглавието и лентата за синхронизация. */
private const val HEAD_ITEMS = 2

/**
 * Лентата; без ViewModel — за тестовете със снимки. [staticFrame] — всичко вече е избродирано;
 * [showScrubber] — плъзгачът с десетилетието се вижда и в покой (тестове); [thisYear] — „Днес“.
 */
@Composable
fun Timeline(
    entries: List<TimelineEntry>,
    fromCache: Boolean = false,
    syncedAt: java.time.Instant? = null,
    refreshError: AppError? = null,
    staticFrame: Boolean = false,
    listState: LazyListState = rememberLazyListState(),
    showScrubber: Boolean = false,
    thisYear: Int = remember { LocalDate.now().year },
) {
    val years = remember(entries, thisYear) { TimelineScrub.years(entries, thisYear) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= WideTimeline
        LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(bottom = 32.dp)) {
            item("head") { TimelineHeader() }
            item("sync") { SyncBanner(fromCache, syncedAt, refreshError) }
            itemsIndexed(entries, key = { i, e -> "$i-${e.label}" }) { i, e ->
                val side = when {
                    !wide -> null
                    i % 2 == 0 -> Side.START
                    else -> Side.END
                }
                TimelineItem("$i-${e.label}", e, last = i == entries.lastIndex, staticFrame = staticFrame, side = side)
            }
        }
        DecadeScrubber(years, listState, showScrubber, Modifier.align(Alignment.TopEnd))
    }
}

@Composable
private fun TimelineHeader() {
    Column(Modifier.fillMaxWidth().padding(horizontal = ScreenGutter, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.timeline_title), style = MaterialTheme.typography.displaySmall, modifier = Modifier.semantics { heading() })
        Text(
            stringResource(R.string.timeline_subtitle), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
        )
        StitchInOrnament(StitchCharts.band(4), Modifier.size(width = 132.dp, height = 28.dp))
    }
}

private val RailWidth = 92.dp
private val MedallionSize = 76.dp

/** От коя страна на конеца е картата на широк екран. */
private enum class Side { START, END }

/**
 * Една веха: медалия с годината върху конеца и картата ѝ. Конецът надолу към следващата веха се
 * избродира веднъж. На широк екран ([side] ≠ null) конецът е по средата, а картите се редуват.
 */
@Composable
private fun TimelineItem(key: String, e: TimelineEntry, last: Boolean, staticFrame: Boolean, side: Side?) {
    val reduced = rememberReducedMotion()
    // Показана веднъж — оцелява при превъртане и при връщане към екрана (rememberSaveable по ключа на реда).
    var shown by rememberSaveable(key) { mutableStateOf(false) }
    val progress = remember { Animatable(if (shown || reduced || staticFrame) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!shown && !reduced && !staticFrame) progress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
        shown = true
    }
    val ext = LocalExtendedColors.current
    val thread = if (ext.isDark) Brand.Gold.copy(alpha = 0.75f) else Brand.GoldDark.copy(alpha = 0.8f)
    // Конецът между вехите — тропосване ([StitchSpec.Basting]), готовият бод от общия речник.
    val bod = StitchSpec.Basting.stroke(LocalDensity.current.density)
    val railHalf = with(LocalDensity.current) { (RailWidth / 2).toPx() }
    val top = with(LocalDensity.current) { MedallionSize.toPx() }
    val description = listOf(e.label, e.title, e.description).filter { it.isNotBlank() }.joinToString(". ")
    // Картата „влиза“ откъм своята страна.
    val enterFrom = if (side == Side.START) -1f else 1f
    val cardMotion = Modifier.graphicsLayer { alpha = progress.value; translationX = enterFrom * (1f - progress.value) * 24.dp.toPx() }
    Row(
        // На широк екран — без собствени размери (IntrinsicSize): картата с widthIn се мери по
        // по-голямата ширина и текстът ѝ се реже; редът и така е висок колкото картата.
        Modifier.fillMaxWidth().then(if (side == null) Modifier.height(IntrinsicSize.Min) else Modifier)
            .semantics(mergeDescendants = true) { contentDescription = description }
            .drawBehind {
                // Конецът: бодове надолу от медалията до края на реда (и през него — до следващата).
                if (last) return@drawBehind
                val railX = if (side == null) railHalf else size.width / 2f
                val start = top + bod.width * 2
                val end = top + (size.height - top) * progress.value
                if (end > start) drawLine(thread, Offset(railX, start), Offset(railX, end), strokeWidth = bod.width, cap = StrokeCap.Round, pathEffect = bod.pathEffect)
            },
    ) {
        val medallion = Modifier.graphicsLayer {
            val p = progress.value
            scaleX = 0.6f + 0.4f * p; scaleY = scaleX; alpha = (p * 3f).coerceAtMost(1f)
        }
        if (side == null) {
            Box(Modifier.width(RailWidth), contentAlignment = Alignment.TopCenter) { Medallion(e.label, medallion) }
            EntryCard(e, Modifier.weight(1f).padding(end = ScreenGutter, top = 6.dp, bottom = 22.dp).then(cardMotion))
        } else {
            Box(Modifier.weight(1f).padding(start = ScreenGutter, top = 6.dp, bottom = 22.dp), contentAlignment = Alignment.TopEnd) {
                if (side == Side.START) EntryCard(e, Modifier.widthIn(max = WideCardMax).then(cardMotion))
            }
            Box(Modifier.width(RailWidth), contentAlignment = Alignment.TopCenter) { Medallion(e.label, medallion) }
            Box(Modifier.weight(1f).padding(end = ScreenGutter, top = 6.dp, bottom = 22.dp), contentAlignment = Alignment.TopStart) {
                if (side == Side.END) EntryCard(e, Modifier.widthIn(max = WideCardMax).then(cardMotion))
            }
        }
    }
}

/**
 * Плочка-осморъка като [org.chyavorec.app.ui.components.FolkIconPlate] (същата форма и градиент) с
 * пръстен от тичащ бод — медалията с годината и дръжката на плъзгача. Пътят — веднъж за размера.
 */
@Composable
private fun Modifier.rosettePlate(): Modifier {
    val container = MaterialTheme.colorScheme.primaryContainer
    val ring = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
    return this.then(
        remember(container, ring) {
            Modifier.drawWithCache {
                val r = size.minDimension / 2f
                val outer = FolkShapes.rosette.scaledPath(r * 0.98f, size.width / 2f, size.height / 2f)
                val fill = Brush.linearGradient(listOf(container, container.copy(alpha = container.alpha * 0.55f)))
                val bod = StitchSpec.Running.stroke(density)
                onDrawBehind {
                    drawPath(outer, fill)
                    drawCircle(ring, radius = r * 0.74f, style = bod)
                }
            }
        },
    )
}

/** Медалията с годината: осморъка с пръстен от бод, Cormorant; размерът на текста е ограничен (украса, а текстът е и в картата). */
@Composable
private fun Medallion(label: String, modifier: Modifier = Modifier) {
    val d = LocalDensity.current
    Box(
        modifier.padding(top = 2.dp).size(MedallionSize - 4.dp).clearAndSetSemantics { }.rosettePlate(),
        contentAlignment = Alignment.Center,
    ) {
        // Украсният надпис не расте с шрифта над ~1.15: иначе годината не се събира в пръстена.
        CompositionLocalProvider(LocalDensity provides Density(d.density, d.fontScale.coerceAtMost(1.15f))) {
            // Периодът („1912–1918“) се чупи след тирето, а не насред годината.
            Text(
                label.replace(Regex("\\s*([–-])\\s*"), "$1\n"),
                style = TextStyle(fontFamily = Cormorant, fontSize = if (label.length > 6) 14.sp else 21.sp, lineHeight = if (label.length > 6) 16.sp else 23.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer, textAlign = TextAlign.Center, maxLines = 3, modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun EntryCard(e: TimelineEntry, modifier: Modifier) {
    AppCard(modifier.fillMaxWidth(), accent = true) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(e.title, style = MaterialTheme.typography.titleLarge)
            if (e.description.isNotBlank()) Text(e.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Плъзгачът „Десетилетие“: години на вехите и положения — чисти функции (тестват се без рисуване). */
internal object TimelineScrub {
    /** Началото на лентата. */
    const val START = 1900

    /** Под толкова вехи плъзгач не трябва. */
    const val MIN_ENTRIES = 6

    /** Годината на всяка веха: без година — предишната (в началото — [START]); последната без година („Днес“) — [thisYear]. */
    fun years(entries: List<TimelineEntry>, thisYear: Int): IntArray {
        var prev = START
        return IntArray(entries.size) { i ->
            val y = entries[i].year ?: if (i == entries.lastIndex && i > 0) maxOf(thisYear, prev) else prev
            prev = y
            y
        }
    }

    fun decade(year: Int): Int = Math.floorDiv(year, 10) * 10

    /** Вехата при положение [fraction] (0 — горе, 1 — долу) от [count] вехи. */
    fun indexAt(fraction: Float, count: Int): Int = if (count <= 0) 0 else (fraction.coerceIn(0f, 1f) * (count - 1)).roundToInt()

    /**
     * Колко е превъртян списъкът (0..1): по средната височина на видимите редове — оценка като на
     * обикновена лента за превъртане; в двата края — точно 0 и 1.
     */
    fun fraction(
        firstIndex: Int, firstOffset: Int, totalItems: Int, averageSize: Float, viewport: Int,
        canBack: Boolean, canForward: Boolean,
    ): Float {
        if (!canBack) return 0f
        if (!canForward) return 1f
        val range = averageSize * totalItems - viewport
        if (range <= 0f) return 0f
        return ((firstIndex * averageSize + firstOffset) / range).coerceIn(0f, 1f)
    }

    /** Обратното: редът и отместването за положение [fraction] (за плъзгането). */
    fun target(fraction: Float, totalItems: Int, averageSize: Float, viewport: Int): Pair<Int, Int> {
        if (totalItems <= 0 || averageSize <= 0f) return 0 to 0
        val px = fraction.coerceIn(0f, 1f) * (averageSize * totalItems - viewport).coerceAtLeast(0f)
        val index = (px / averageSize).toInt().coerceIn(0, totalItems - 1)
        return index to (px - index * averageSize).roundToInt()
    }
}

/** Средната височина на видимите редове (без алокации — чете се при всяко превъртане). */
private fun LazyListState.averageItemSize(): Float {
    val items = layoutInfo.visibleItemsInfo
    if (items.isEmpty()) return 0f
    var sum = 0
    for (i in items.indices) sum += items[i].size
    return sum.toFloat() / items.size
}

private fun LazyListState.viewportSize(): Int = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset

private fun LazyListState.scrollFraction(): Float = TimelineScrub.fraction(
    firstVisibleItemIndex, firstVisibleItemScrollOffset, layoutInfo.totalItemsCount, averageItemSize(), viewportSize(),
    canScrollBackward, canScrollForward,
)

/** Колко трае плъзгачът след края на превъртането. */
private const val SCRUBBER_HIDE_MS = 1200L
private val ScrubberThumb = 48.dp

/**
 * Малка осморъка вдясно, която показва десетилетието, докато списъкът се превърта, и може да се
 * влачи от 1900 г. до днес. В покой я няма. Положението се чете при подреждането (без
 * рекомпозиция на кадър), а надписът се сменя само при ново десетилетие. С TalkBack не се
 * показва — списъкът се обхожда сам, ред по ред.
 */
@Composable
private fun DecadeScrubber(years: IntArray, listState: LazyListState, alwaysShown: Boolean, modifier: Modifier = Modifier) {
    if (years.size < TimelineScrub.MIN_ENTRIES || rememberIsTouchExplorationEnabled()) return
    val reduced = rememberReducedMotion()
    var dragging by remember { mutableStateOf(false) }
    val drag = remember { mutableFloatStateOf(0f) }
    val scrollable by remember { derivedStateOf { listState.canScrollForward || listState.canScrollBackward } }
    val active = alwaysShown || dragging || listState.isScrollInProgress
    var visible by remember { mutableStateOf(alwaysShown) }
    LaunchedEffect(active) {
        if (active) visible = true
        else {
            delay(SCRUBBER_HIDE_MS)
            visible = false
        }
    }
    val fraction = remember(listState) { { if (dragging) drag.floatValue else listState.scrollFraction() } }
    AnimatedVisibility(
        visible && scrollable, modifier = modifier.fillMaxHeight(),
        enter = if (reduced) EnterTransition.None else fadeIn(), exit = if (reduced) ExitTransition.None else fadeOut(),
    ) {
        val decade by remember(years) { derivedStateOf { TimelineScrub.decade(years[TimelineScrub.indexAt(fraction(), years.size)]) } }
        val thumbPx = with(LocalDensity.current) { ScrubberThumb.roundToPx() }
        val padPx = with(LocalDensity.current) { 8.dp.roundToPx() }
        BoxWithConstraints(Modifier.fillMaxHeight().clearAndSetSemantics { }) {
            val track = (constraints.maxHeight - thumbPx - 2 * padPx).coerceAtLeast(1)
            Row(
                Modifier.offset { IntOffset(0, padPx + (fraction() * track).roundToInt()) }
                    .height(ScrubberThumb).padding(end = 6.dp)
                    .pointerInput(listState, track) {
                        detectVerticalDragGestures(
                            onDragStart = { drag.floatValue = listState.scrollFraction(); dragging = true },
                            onDragEnd = { dragging = false },
                            onDragCancel = { dragging = false },
                        ) { change, dy ->
                            change.consume()
                            drag.floatValue = (drag.floatValue + dy / track).coerceIn(0f, 1f)
                            val (index, offset) = TimelineScrub.target(
                                drag.floatValue, listState.layoutInfo.totalItemsCount, listState.averageItemSize(), listState.viewportSize(),
                            )
                            listState.requestScrollToItem(index, offset)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.timeline_decade, decade),
                    style = TextStyle(fontFamily = Cormorant, fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface, maxLines = 1,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                        .border(FolkTokens.goldBorder(), CircleShape).padding(horizontal = 12.dp, vertical = 2.dp),
                )
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(34.dp).rosettePlate())
            }
        }
    }
}

/**
 * „На този ден“ за „Начало“: на 17 декември и 17 март (датите на две вехи от страницата „История“).
 * Другите дни не прави нищо — не чете кеша. Хронологията — от кеша/мрежата, а без тях — вградената.
 */
@Composable
fun OnThisDayCard(today: LocalDate, navigate: (String) -> Unit, modifier: Modifier = Modifier) {
    if (!OnThisDay.mayHaveEntry(today)) return
    val repo = LocalAppContainer.current.timelineRepository
    val entry by produceState<TimelineEntry?>(null, today) {
        val entries = (repo.timeline(false) as? Outcome.Success)?.value?.data ?: repo.cached()?.data ?: QuizDefaults.timeline
        value = OnThisDay.entryFor(today, entries)
    }
    entry?.let { OnThisDayContent(it, today, onClick = { navigate(Routes.TIMELINE) }, modifier) }
}

@Composable
fun OnThisDayContent(entry: TimelineEntry, today: LocalDate, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val years = OnThisDay.yearsAgo(entry, today)
    val open = stringResource(R.string.timeline_open)
    AppCard(
        modifier.fillMaxWidth().padding(horizontal = ScreenGutter, vertical = 6.dp).heightIn(min = 56.dp).semantics(mergeDescendants = true) {},
        onClick = onClick, onClickLabel = open, accent = true,
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            StitchInOrnament(StitchCharts.rosette(5), Modifier.size(44.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(stringResource(R.string.timeline_on_this_day, entry.year ?: 0), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(entry.title, style = MaterialTheme.typography.titleMedium)
                years?.let { Text(pluralStringResource(R.plurals.timeline_years_ago, it, it), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
