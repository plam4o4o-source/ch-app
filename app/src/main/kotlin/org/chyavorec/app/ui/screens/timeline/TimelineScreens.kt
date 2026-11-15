package org.chyavorec.app.ui.screens.timeline

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import org.chyavorec.app.ui.components.EmptyView
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
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.AppMotion
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
    Scaffold(topBar = { TimelineTopBar(state.data, listState, onBack) }) { padding ->
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

/**
 * Лентата на екрана: „Лента на времето · 1940-те“. Голямото заглавие е в списъка — тук то се
 * показва, щом онова излезе (или няма списък). Десетилетието е на вехата, която се чете (на
 * третина от горния край), и се сменя с кратко плъзгане нагоре/надолу; за TalkBack — едно
 * изречение („Лента на времето, 1940-те години“), без обявяване при всяка смяна.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineTopBar(
    entries: List<TimelineEntry>?,
    listState: LazyListState,
    onBack: () -> Unit,
    thisYear: Int = remember { LocalDate.now().year },
) {
    val title = stringResource(R.string.timeline_title)
    val listShown = !entries.isNullOrEmpty()
    val years = remember(entries, thisYear) { if (entries.isNullOrEmpty()) IntArray(0) else TimelineScrub.years(entries, thisYear) }
    val headerGone by remember(listState) { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val decade by rememberReadingDecade(years, listState)
    val shown = !listShown || headerGone
    val reduced = rememberReducedMotion()
    val titleSpec: AnimationSpec<Float> = if (reduced) snap() else AppMotion.defaultEffects()
    val titleAlpha by animateFloatAsState(if (shown) 1f else 0f, titleSpec, label = "bar-title")
    val shownDecade = decade.takeIf { listShown }
    val spoken = shownDecade?.let { stringResource(R.string.timeline_title_decade, it) } ?: title
    TopAppBar(
        // Името на екрана не следва десетилетието — иначе TalkBack би го обявявал при превъртане.
        modifier = Modifier.semantics { paneTitle = title },
        title = {
            Row(
                Modifier
                    .graphicsLayer {
                        alpha = titleAlpha
                        translationY = (1f - titleAlpha) * 6.dp.toPx()
                    }
                    .clearAndSetSemantics { if (shown) contentDescription = spoken },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val style = MaterialTheme.typography.titleLarge
                Text(title, style = style, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                if (shownDecade != null) {
                    Text(" · ", style = style, color = MaterialTheme.colorScheme.primary, maxLines = 1)
                    DecadeTick(shownDecade, reduced, style)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.action_back))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}

/** Десетилетието в лентата: новото идва отдолу при превъртане надолу (отгоре — нагоре) и избледнява старото. */
@Composable
private fun DecadeTick(decade: Int, reduced: Boolean, style: TextStyle) {
    val color = MaterialTheme.colorScheme.primary
    if (reduced) {
        Text(stringResource(R.string.timeline_decade, decade), style = style, color = color, maxLines = 1)
        return
    }
    AnimatedContent(
        targetState = decade,
        transitionSpec = {
            val dir = if (targetState > initialState) 1 else -1
            (slideInVertically(tween(AppMotion.ShortMs)) { dir * it / 2 } + fadeIn(tween(AppMotion.ShortMs)))
                .togetherWith(slideOutVertically(tween(AppMotion.ShortMs)) { -dir * it / 2 } + fadeOut(tween(AppMotion.QuickMs)))
                .using(SizeTransform(clip = false))
        },
        label = "decade",
    ) { d ->
        Text(stringResource(R.string.timeline_decade, d), style = style, color = color, maxLines = 1)
    }
}

/** Десетилетието на вехата, която се чете — сменя се само при ново десетилетие (без рекомпозиция на кадър). */
@Composable
private fun rememberReadingDecade(years: IntArray, listState: LazyListState): State<Int?> = remember(years, listState) {
    derivedStateOf {
        if (years.isEmpty()) {
            null
        } else {
            val i = TimelineScrub.readingEntry(listState.readingItem(), years.size, listState.canScrollBackward, listState.canScrollForward)
            TimelineScrub.decade(years[i])
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
 * [showScrubber] — дръжката на плъзгача се вижда и в покой, а [showDecadePill] — и надписът с
 * десетилетието като при влачене (тестове); [thisYear] — „Днес“.
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
    showDecadePill: Boolean = false,
) {
    val years = remember(entries, thisYear) { TimelineScrub.years(entries, thisYear) }
    val plates = rememberRosettePlates()
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
                TimelineItem("$i-${e.label}", e, last = i == entries.lastIndex, staticFrame = staticFrame, side = side, plates = plates)
            }
        }
        DecadeScrubber(years, listState, plates, showScrubber, showDecadePill, Modifier.align(Alignment.TopStart))
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

/** Лявото поле на телефона: там се плъзга дръжката (малка осморъка), без да закрива медалиите. */
private val ScrubGutter = 28.dp
/** Медалията: годината е в нея, а цялата дата — в картата. */
private val MedallionSize = 64.dp
private val MedallionTop = 4.dp
/** Колоната на медалията на телефона (след полето); на широк екран — по средата. */
private val MedallionRail = 68.dp
private val WideRail = 92.dp

/** От коя страна на конеца е картата на широк екран. */
private enum class Side { START, END }

/**
 * Една веха: медалия с годината върху конеца и картата ѝ. Конецът надолу към следващата веха се
 * избродира веднъж. На широк екран ([side] ≠ null) конецът е по средата, а картите се редуват.
 */
@Composable
private fun TimelineItem(key: String, e: TimelineEntry, last: Boolean, staticFrame: Boolean, side: Side?, plates: RosettePlates) {
    val reduced = rememberReducedMotion()
    // Показана веднъж — оцелява при превъртане и при връщане към екрана (rememberSaveable по ключа на реда).
    var shown by rememberSaveable(key) { mutableStateOf(false) }
    val progress = remember { Animatable(if (shown || reduced || staticFrame) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!shown && !reduced && !staticFrame) progress.animateTo(1f, tween(AppMotion.MediumMs, easing = FastOutSlowInEasing))
        shown = true
    }
    val ext = LocalExtendedColors.current
    val thread = if (ext.isDark) Brand.Gold.copy(alpha = 0.75f) else Brand.GoldDark.copy(alpha = 0.8f)
    val d = LocalDensity.current
    // Средата на конеца на телефона и долният край на медалията.
    val railX = with(d) { (ScrubGutter + MedallionRail / 2).toPx() }
    val top = with(d) { (MedallionTop + MedallionSize).toPx() }
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
                // Конецът (тропосване, [StitchSpec.Basting]): бодове надолу от медалията до края на реда.
                if (last) return@drawBehind
                val x = if (side == null) railX else size.width / 2f
                val start = top + StitchSpec.Basting.widthDp * density * 2
                val end = top + (size.height - top) * progress.value
                if (end > start) drawStitchRun(StitchSpec.Basting, thread, x, start, end)
            },
    ) {
        val medallion = Modifier.graphicsLayer {
            val p = progress.value
            scaleX = 0.6f + 0.4f * p; scaleY = scaleX; alpha = (p * 3f).coerceAtMost(1f)
        }
        if (side == null) {
            Spacer(Modifier.width(ScrubGutter))
            Box(Modifier.width(MedallionRail), contentAlignment = Alignment.TopCenter) { Medallion(e, plates, medallion) }
            EntryCard(e, Modifier.weight(1f).padding(end = ScreenGutter, top = 6.dp, bottom = 22.dp).then(cardMotion))
        } else {
            // Отляво остава поле за дръжката на плъзгача.
            Box(Modifier.weight(1f).padding(start = ScrubGutter + 4.dp, top = 6.dp, bottom = 22.dp), contentAlignment = Alignment.TopEnd) {
                if (side == Side.START) EntryCard(e, Modifier.widthIn(max = WideCardMax).then(cardMotion))
            }
            Box(Modifier.width(WideRail), contentAlignment = Alignment.TopCenter) { Medallion(e, plates, medallion) }
            Box(Modifier.weight(1f).padding(end = ScreenGutter, top = 6.dp, bottom = 22.dp), contentAlignment = Alignment.TopStart) {
                if (side == Side.END) EntryCard(e, Modifier.widthIn(max = WideCardMax).then(cardMotion))
            }
        }
    }
}

/**
 * Медалията: само годината, едро (Cormorant ~21 sp), върху готовата осморъка. Украса — TalkBack не я
 * чете, а надписът не расте с шрифта (цялата дата е в картата и расте там): така не прелива никога.
 */
@Composable
private fun Medallion(e: TimelineEntry, plates: RosettePlates, modifier: Modifier = Modifier) {
    val d = LocalDensity.current
    val text = TimelineScrub.medallionText(e)
    Box(
        modifier.padding(top = MedallionTop).size(MedallionSize).clearAndSetSemantics { }.rosettePlate(plates),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalDensity provides Density(d.density, 1f)) {
            val (fontSize, lineHeight) = when {
                text.length <= 4 -> 21.sp to 23.sp
                text.length <= 6 -> 16.sp to 18.sp
                else -> 12.sp to 14.sp
            }
            Text(
                text, style = TextStyle(fontFamily = Cormorant, fontSize = fontSize, lineHeight = lineHeight),
                color = MaterialTheme.colorScheme.onPrimaryContainer, textAlign = TextAlign.Center,
                maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(horizontal = 9.dp),
            )
        }
    }
}

/** Картата на вехата: цялата дата („17 март 1958“, „1912–1918“) като златен надред, заглавие и текст. */
@Composable
private fun EntryCard(e: TimelineEntry, modifier: Modifier) {
    AppCard(modifier.fillMaxWidth(), accent = true) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(e.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
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

    /** Надписът в медалията: само годината (началната при период); без година — етикетът („Днес“). */
    fun medallionText(e: TimelineEntry): String = e.year?.toString() ?: e.label.trim()

    /**
     * Вехата, която се чете: тази на реда [item] от списъка (заглавните редове — първата); в
     * началото — първата, а в края на списъка — последната (иначе „Днес“ може и да не дойде).
     */
    fun readingEntry(item: Int, count: Int, canBack: Boolean, canForward: Boolean): Int = when {
        count <= 0 || !canBack -> 0
        !canForward -> count - 1
        else -> (item - HEAD_ITEMS).coerceIn(0, count - 1)
    }

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

/** Редът, който пресича линията на третина от горния край на списъка (без алокации). */
private fun LazyListState.readingItem(): Int {
    val info = layoutInfo
    val items = info.visibleItemsInfo
    if (items.isEmpty()) return firstVisibleItemIndex
    val line = info.viewportStartOffset + (info.viewportEndOffset - info.viewportStartOffset) / 3
    for (i in items.indices) {
        val item = items[i]
        if (item.offset + item.size > line) return item.index
    }
    return items[items.lastIndex].index
}

private fun LazyListState.scrollFraction(): Float = TimelineScrub.fraction(
    firstVisibleItemIndex, firstVisibleItemScrollOffset, layoutInfo.totalItemsCount, averageItemSize(), viewportSize(),
    canScrollBackward, canScrollForward,
)

/** Колко трае дръжката след края на превъртането. */
private const val SCRUBBER_HIDE_MS = 1200L
/** Докосваемата дръжка (48 dp) и видимата осморъка в нея. */
private val ThumbTouch = 48.dp
private val ThumbSize = 24.dp
/** Надписът с десетилетието не расте над това (за да не стигне текста на картите). */
private const val PILL_MAX_FONT_SCALE = 1.3f

/**
 * Плъзгачът в лявото поле: малка осморъка, която се появява, докато списъкът се превърта, и може
 * да се влачи от 1900 г. до днес. Надписът с десетилетието („1940-те“) излиза само докато се влачи
 * — над пръста, в колоната на медалиите, без да закрива текста на картите; при превъртане
 * десетилетието е в лентата отгоре. При нов десетилетие под пръста — лек тик. В покой не се
 * рисува нищо; положението се чете при подреждането (без рекомпозиция на кадър), а появата е
 * само прозрачност на малките слоеве. С TalkBack го няма — списъкът се обхожда сам, ред по ред.
 */
@Composable
private fun DecadeScrubber(
    years: IntArray,
    listState: LazyListState,
    plates: RosettePlates,
    alwaysShown: Boolean,
    pillAlwaysShown: Boolean,
    modifier: Modifier = Modifier,
) {
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
    val shown = visible && scrollable
    val thumbAlpha = animateFloatAsState(if (shown) 1f else 0f, if (reduced) snap() else tween(AppMotion.ShortMs), label = "scrub-thumb")
    val pillAlpha = animateFloatAsState(
        if (shown && (dragging || pillAlwaysShown)) 1f else 0f, if (reduced) snap() else tween(AppMotion.QuickMs), label = "scrub-pill",
    )
    val pillComposed by remember { derivedStateOf { pillAlpha.value > 0f } }
    val decadeState = rememberReadingDecade(years, listState)
    // Тик при ново десетилетие — само докато се влачи (при обикновено превъртане — тишина).
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(decadeState) {
        var last: Int? = null
        snapshotFlow { decadeState.value }.collect { d ->
            if (last != null && d != last && dragging) haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
            last = d
        }
    }
    val fraction = remember(listState) { { if (dragging) drag.floatValue else listState.scrollFraction() } }
    val d = LocalDensity.current
    val touchPx = with(d) { ThumbTouch.roundToPx() }
    val thumbPx = with(d) { ThumbSize.roundToPx() }
    val padPx = with(d) { 8.dp.roundToPx() }
    val gapPx = with(d) { 6.dp.roundToPx() }
    val startPx = with(d) { 2.dp.roundToPx() }
    BoxWithConstraints(modifier.fillMaxHeight().clearAndSetSemantics { }) {
        val track = (constraints.maxHeight - touchPx - 2 * padPx).coerceAtLeast(1)
        val thumbY = { padPx + (fraction() * track).roundToInt() }
        Box(
            Modifier.offset { IntOffset(0, thumbY()) }.size(ThumbTouch)
                .graphicsLayer { alpha = thumbAlpha.value }
                .then(
                    if (!shown) Modifier else Modifier.pointerInput(listState, track) {
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
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(Modifier.padding(start = 2.dp).size(ThumbSize).rosettePlate(plates))
        }
        if (pillComposed) {
            val decade = decadeState.value
            if (decade != null) {
                CompositionLocalProvider(LocalDensity provides Density(d.density, d.fontScale.coerceAtMost(PILL_MAX_FONT_SCALE))) {
                    Text(
                        stringResource(R.string.timeline_decade, decade),
                        style = TextStyle(fontFamily = Cormorant, fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface, maxLines = 1,
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                // Над осморъката (над пръста); съвсем горе — под нея.
                                val p = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
                                layout(p.width, p.height) {
                                    val y0 = thumbY()
                                    val above = y0 + (touchPx - thumbPx) / 2 - gapPx - p.height
                                    p.place(startPx, if (above >= 0) above else y0 + (touchPx + thumbPx) / 2 + gapPx)
                                }
                            }
                            .graphicsLayer { alpha = pillAlpha.value }
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                            .border(FolkTokens.goldBorder(), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 1.dp),
                    )
                }
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
