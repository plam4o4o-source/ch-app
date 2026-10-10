package org.chyavorec.app.ui.screens.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.DemoBanner
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.SectionHeader
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.SyncStamp
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.home.BookRow
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.data.catalog.ReadingRecommender
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem

/**
 * „Какво съм чел“: историята идва от библиотечния сървър (само за съгласили се
 * читатели); „Подобни книги“ се изчисляват изцяло на телефона от каталога.
 */
class HistoryViewModel(private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<HistoryItem>>())
    val state: StateFlow<ScreenState<List<HistoryItem>>> = _state.asStateFlow()
    private val _recommendations = MutableStateFlow<List<CatalogBook>>(emptyList())
    val recommendations: StateFlow<List<CatalogBook>> = _recommendations.asStateFlow()
    /** Каталожният запис по инвентарен номер — за истинската корица в списъка. */
    private val _catalog = MutableStateFlow<Map<Long, CatalogBook>>(emptyMap())
    val catalog: StateFlow<Map<Long, CatalogBook>> = _catalog.asStateFlow()
    val selectedYear = MutableStateFlow<Int?>(null)
    val isDemo = c.readerServices.isDemo

    @Volatile private var engine: CatalogSearchEngine? = null

    init {
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            c.authRepository.state.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (_state.value.data == null) load(force = false)
                } else if (s is AuthState.SignedOut) {
                    _state.value = ScreenState(loading = false, error = AppError.Unauthorized)
                    _recommendations.value = emptyList()
                }
            }
        }
    }

    fun refresh() = load(force = true)

    private fun load(force: Boolean) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        val r = c.libraryRepository.history(force)
        _state.update { it.with(r) }
        (r as? Outcome.Success)?.value?.data?.let { recommend(it) }
    }

    private suspend fun recommend(history: List<HistoryItem>) {
        val eng = engine ?: run {
            val synced = c.catalogRepository.inMemory() ?: c.catalogRepository.cached()
                ?: (c.catalogRepository.catalog(false) as? Outcome.Success)?.value
            synced?.data?.also { engine = it }
        } ?: return
        val (books, recs) = withContext(Dispatchers.Default) {
            val byInv = history.mapNotNull { it.inv }.distinct().mapNotNull { inv -> eng.book(inv)?.let { inv to it } }.toMap()
            byInv to ReadingRecommender.recommend(eng, history, limit = 12)
        }
        _catalog.value = books
        _recommendations.value = recs
    }
}

@Composable
fun HistoryScreen(onBack: () -> Unit, navigate: (String) -> Unit, onLogin: () -> Unit) {
    val vm = appViewModel(key = "history") { HistoryViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val recs by vm.recommendations.collectAsStateWithLifecycle()
    val catalog by vm.catalog.collectAsStateWithLifecycle()
    val year by vm.selectedYear.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Scaffold(topBar = { BackTopBar(stringResource(R.string.history_title), onBack) }) { padding ->
        PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column {
                DemoBanner(vm.isDemo)
                val err = state.error
                if (state.data == null && err != null && (err == AppError.Unauthorized || err is AppError.NotAvailable)) {
                    NeedsLogin(err, onLogin)
                    return@Column
                }
                SyncBanner(state.fromCache, state.syncedAt, state.refreshError)
                StateContent(
                    state = state,
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList() },
                    empty = { EmptyView(stringResource(R.string.history_empty), stringResource(R.string.history_empty_hint), icon = Icons.Outlined.AutoStories) },
                    errorSubject = stringResource(R.string.subject_history),
                ) { items ->
                    // Групиране по година на заемане (най-новите първо); без дата — най-отдолу.
                    // Пресмята се наново само при нови данни или друга избрана година (не при всяка рекомпозиция).
                    val byYear = remember(items) { items.groupBy { it.year }.toSortedMap(compareByDescending<Int?> { it ?: Int.MIN_VALUE }) }
                    val years = remember(byYear) { byYear.keys.toList() }
                    val shown = remember(byYear, year) { if (year == null) byYear else byYear.filterKeys { it == year } }
                    LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                        if (years.size > 1) {
                            item("years") {
                                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    item {
                                        FilterChip(year == null, onClick = { vm.selectedYear.value = null }, label = { Text(stringResource(R.string.history_year_all)) })
                                    }
                                    items(years) { y ->
                                        FilterChip(
                                            year == y,
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                                vm.selectedYear.value = if (year == y) null else y
                                            },
                                            label = { Text(y?.toString() ?: "—") },
                                        )
                                    }
                                }
                            }
                        }
                        shown.forEach { (y, list) ->
                            item("year-$y") {
                                Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.Bottom) {
                                    Text(y?.toString() ?: "—", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f).semantics { heading() })
                                    Text(
                                        pluralStringResource(R.plurals.history_year_count, list.size, list.size),
                                        style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            itemsIndexedKeyed(list) { i, item ->
                                HistoryRow(item, catalog[item.inv], onOpen = { inv -> navigate(Routes.book(inv)) }, modifier = Modifier.animateEntrance(i))
                            }
                        }
                        item("source") {
                            if (!state.fromCache) SyncStamp(state.syncedAt)
                            Text(
                                stringResource(R.string.history_source_note),
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                            )
                        }
                        if (recs.isNotEmpty()) {
                            item("similar-h") {
                                SectionHeader(
                                    stringResource(R.string.history_similar), label = stringResource(R.string.label_library),
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                                )
                            }
                            item("similar") { BookRow(recs) { navigate(Routes.book(it.inv)) } }
                            item("similar-note") {
                                Row(Modifier.padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Outlined.Info, null, modifier = Modifier.size(16.dp).padding(top = 1.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.history_similar_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Елементи с ключ по loanId (стабилни при филтриране по година). */
private fun androidx.compose.foundation.lazy.LazyListScope.itemsIndexedKeyed(list: List<HistoryItem>, content: @Composable (Int, HistoryItem) -> Unit) {
    list.forEachIndexed { i, item -> item(key = "h-" + item.loanId, contentType = "history") { content(i, item) } }
}

@Composable
private fun HistoryRow(item: HistoryItem, book: CatalogBook?, onOpen: (Long) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Корица: от каталога, ако екземплярът е в него; иначе „издателска“ корица само със заглавието.
    val cover = book ?: CatalogBook(inv = item.inv ?: 0L, author = item.author, title = item.title)
    val inv = book?.inv
    Row(
        modifier.fillMaxWidth()
            .then(if (inv != null) Modifier.clickable { onOpen(inv) } else Modifier)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BookCover(cover, width = 48.dp, showStatusDot = book != null)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (item.author.isNotBlank()) {
                Text(item.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            val out = Formatters.shortDate(context, item.dateOut) ?: "—"
            val back = Formatters.shortDate(context, item.dateIn)
            Text(
                if (back != null) stringResource(R.string.history_range, out, back)
                else stringResource(R.string.history_range, out, stringResource(R.string.history_not_returned)),
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    Spacer(Modifier.height(2.dp))
}
