package org.chyavorec.app.ui.screens.timeline

import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.StitchSpec
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.LeafRefreshBox
import org.chyavorec.app.ui.components.MessageView
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchInOrnament
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.TimelineRepository
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.timeline.OnThisDay
import org.chyavorec.domain.timeline.TimelineEntry
import java.time.LocalDate

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

/** Лентата; без ViewModel — за тестовете със снимки. [staticFrame] — всичко вече е избродирано. */
@Composable
fun Timeline(
    entries: List<TimelineEntry>,
    fromCache: Boolean = false,
    syncedAt: java.time.Instant? = null,
    refreshError: AppError? = null,
    staticFrame: Boolean = false,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 32.dp)) {
        item("head") { TimelineHeader() }
        item("sync") { SyncBanner(fromCache, syncedAt, refreshError) }
        itemsIndexed(entries, key = { i, e -> "$i-${e.label}" }) { i, e ->
            TimelineItem("$i-${e.label}", e, last = i == entries.lastIndex, staticFrame = staticFrame)
        }
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

/** Една веха: медалия с годината върху конеца и картата ѝ. Конецът надолу към следващата веха се избродира веднъж. */
@Composable
private fun TimelineItem(key: String, e: TimelineEntry, last: Boolean, staticFrame: Boolean) {
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
    val railX = with(LocalDensity.current) { (RailWidth / 2).toPx() }
    val top = with(LocalDensity.current) { MedallionSize.toPx() }
    val description = listOf(e.label, e.title, e.description).filter { it.isNotBlank() }.joinToString(". ")
    Row(
        Modifier.fillMaxWidth().height(IntrinsicSize.Min)
            .semantics(mergeDescendants = true) { contentDescription = description }
            .drawBehind {
                // Конецът: бодове надолу от медалията до края на реда (и през него — до следващата).
                if (last) return@drawBehind
                val start = top + bod.width * 2
                val end = top + (size.height - top) * progress.value
                if (end > start) drawLine(thread, Offset(railX, start), Offset(railX, end), strokeWidth = bod.width, cap = StrokeCap.Round, pathEffect = bod.pathEffect)
            },
    ) {
        Box(Modifier.width(RailWidth), contentAlignment = Alignment.TopCenter) {
            Medallion(e.label, Modifier.graphicsLayer {
                val p = progress.value
                scaleX = 0.6f + 0.4f * p; scaleY = scaleX; alpha = (p * 3f).coerceAtMost(1f)
            })
        }
        EntryCard(
            e,
            Modifier.weight(1f).padding(end = ScreenGutter, top = 6.dp, bottom = 22.dp)
                .graphicsLayer { alpha = progress.value; translationX = (1f - progress.value) * 24.dp.toPx() },
        )
    }
}

/** Златна медалия с годината: двоен пръстен, Cormorant; размерът на текста е ограничен (украса, а текстът е и в картата). */
@Composable
private fun Medallion(label: String, modifier: Modifier = Modifier) {
    val ext = LocalExtendedColors.current
    val d = LocalDensity.current
    Box(
        modifier.padding(top = 2.dp).size(MedallionSize - 4.dp).clearAndSetSemantics { }
            .background(medallionBrush(ext.isDark), CircleShape)
            .border(2.dp, if (ext.isDark) Brand.GoldLight else Brand.GoldDark, CircleShape)
            .padding(5.dp).border(1.dp, Brand.Ink.copy(alpha = 0.35f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        // Украсният надпис не расте с шрифта над ~1.15: иначе годината не се събира в кръга.
        CompositionLocalProvider(LocalDensity provides Density(d.density, d.fontScale.coerceAtMost(1.15f))) {
            // Периодът („1912–1918“) се чупи след тирето, а не насред годината.
            Text(
                label.replace(Regex("\\s*([–-])\\s*"), "$1\n"),
                style = TextStyle(fontFamily = Cormorant, fontSize = if (label.length > 6) 15.sp else 22.sp, lineHeight = if (label.length > 6) 17.sp else 24.sp),
                color = Brand.Ink, textAlign = TextAlign.Center, maxLines = 3, modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

private fun medallionBrush(dark: Boolean) = androidx.compose.ui.graphics.Brush.linearGradient(
    listOf(if (dark) Color(0xFFEBD59A) else Color(0xFFE3C46F), Brand.Gold, if (dark) Color(0xFFB8923A) else Color(0xFFC9A84C)),
)

@Composable
private fun EntryCard(e: TimelineEntry, modifier: Modifier) {
    AppCard(modifier.fillMaxWidth(), accent = true) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(e.title, style = MaterialTheme.typography.titleLarge)
            if (e.description.isNotBlank()) Text(e.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
