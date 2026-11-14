package org.chyavorec.app.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.appViewModelWithState
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.SearchInput
import org.chyavorec.app.ui.components.SearchShortcutEffect
import org.chyavorec.app.ui.components.appFilterChipColors
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.BookListItem
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.util.Formatters
import androidx.compose.ui.platform.LocalContext
import org.chyavorec.app.ui.screens.news.NewsRow
import org.chyavorec.core.Outcome
import org.chyavorec.data.SearchAggregator
import org.chyavorec.data.SearchCorpus
import org.chyavorec.data.SearchResults
import org.chyavorec.data.site.SiteLinkClassifier
import org.chyavorec.domain.model.SiteSection

enum class SearchScope { ALL, BOOKS, NEWS, EVENTS, PAGES }

@Immutable
data class SearchUi(val query: String = "", val scope: SearchScope = SearchScope.ALL, val results: SearchResults = SearchResults(), val searching: Boolean = false)

/**
 * Глобално търсене: каталог, автори, новини, събития и страници на сайта.
 * Работи върху вече заредените (и кеширани) данни — мигновено и офлайн.
 */
@OptIn(FlowPreview::class)
class GlobalSearchViewModel(
    private val c: AppContainer,
    /** Текстът и обхватът — оцеляват след спиране на процеса във фонов режим (търси се наново). */
    private val savedState: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    val ui = MutableStateFlow(
        savedState.get<String>(QUERY_KEY).orEmpty().let { q ->
            val scope = savedState.get<String>(SCOPE_KEY)?.let { s -> SearchScope.entries.firstOrNull { it.name == s } } ?: SearchScope.ALL
            SearchUi(query = q, scope = scope, searching = q.isNotBlank())
        },
    )
    val recent = c.settings.recentSearches.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val query = MutableStateFlow(ui.value.query)
    // Новините, събитията и страниците се четат/декодират и нормализират веднъж, а не при всеки клавиш.
    @Volatile private var corpus: SearchCorpus = SearchCorpus.EMPTY

    init {
        viewModelScope.launch {
            // Зареждаме данните за търсене (каталогът — през индекса в базата; без него — от кеша, при нужда от мрежата).
            c.catalogRepository.lookupSearcher()
            val news = c.newsRepository.cached()?.data
                ?: (c.newsRepository.latest(false) as? Outcome.Success)?.value?.data.orEmpty()
            val events = c.eventsRepository.cachedRolled()?.data
                ?: (c.eventsRepository.events(false) as? Outcome.Success)?.value?.data.orEmpty()
            val siteDocs = c.siteRepository.cachedSearchIndex()
                ?: (c.siteRepository.searchIndex(false) as? Outcome.Success)?.value?.data.orEmpty()
            corpus = withContext(Dispatchers.Default) { SearchAggregator.prepare(news, events, siteDocs) }
            run(query.value)
        }
        // Нова заявка отменя още незавършеното търсене на старата.
        viewModelScope.launch(Dispatchers.Default) { query.debounce(180).collectLatest { run(it) } }
    }

    fun setQuery(q: String) {
        ui.update { it.copy(query = q, searching = q.isNotBlank()) }
        query.value = q
        savedState[QUERY_KEY] = q
    }

    fun setScope(s: SearchScope) {
        ui.update { it.copy(scope = s) }
        savedState[SCOPE_KEY] = s.name
    }
    fun commit() = viewModelScope.launch { c.settings.addRecentSearch(ui.value.query) }
    /** Изчиства последните търсения; връща изчистените — за „Отмени“. */
    fun clearRecent(): List<String> {
        val cleared = recent.value
        viewModelScope.launch { c.settings.clearRecentSearches() }
        return cleared
    }

    fun restoreRecent(cleared: List<String>) = viewModelScope.launch { c.settings.restoreRecentSearches(cleared) }

    private suspend fun run(q: String) {
        if (q.isBlank()) {
            ui.update { if (it.query == q) it.copy(results = SearchResults(), searching = false) else it }
            return
        }
        // През индекса в базата; без него — индексът в паметта (освободен ли е — пак от диска), без мрежа.
        val catalog = c.catalogRepository.lookupSearcher(network = false)?.data
        val r = withContext(Dispatchers.Default) { SearchAggregator.search(q, catalog, corpus) }
        ui.update { if (it.query == q) it.copy(results = r, searching = false) else it }
    }

    private companion object {
        const val QUERY_KEY = "search.query"
        const val SCOPE_KEY = "search.scope"
    }
}

@Composable
fun SearchScreen(onBack: () -> Unit, navigate: (String) -> Unit, openExternal: (String) -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModelWithState { c, saved -> GlobalSearchViewModel(c, saved) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    SearchShortcutEffect(Routes.SEARCH) { runCatching { focus.requestFocus() } }

    Scaffold(topBar = {
        Column(Modifier.statusBarsPadding().padding(start = 8.dp, end = 8.dp, top = 6.dp)) {
            // Запълненото „хапче“ като на „Начало“ и в каталога (без златен контур).
            SearchInput(
                value = ui.query, onValueChange = vm::setQuery,
                placeholder = stringResource(R.string.search_hint),
                leadingIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.action_back)) } },
                trailingIcon = { if (ui.query.isNotEmpty()) IconButton(onClick = { vm.setQuery("") }) { Icon(Icons.Outlined.Clear, stringResource(R.string.action_clear)) } },
                keyboardActions = KeyboardActions(onSearch = { vm.commit() }),
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
            )
        }
    }) { padding ->
        val recent by vm.recent.collectAsStateWithLifecycle()
        GlobalSearchContent(
            ui, recent, vm::setScope, onCommit = { vm.commit() }, onClearRecent = vm::clearRecent, onRestoreRecent = { vm.restoreRecent(it) },
            navigate, openExternal, openLink, onRecent = vm::setQuery, modifier = Modifier.padding(padding),
        )
    }
}

/**
 * Обхватите и под тях последните търсения или резултатите — общи за екрана „Търсене“ и
 * разгънатото поле за търсене на „Начало“ ([org.chyavorec.app.ui.screens.home.HomeSearchBar]).
 * [onRecent] — избрано последно търсене (полето е на извикващия). Състоянието идва готово
 * (стабилни параметри — прескача се, докато не се промени), а не като ViewModel.
 */
@Composable
internal fun GlobalSearchContent(
    ui: SearchUi,
    recent: List<String>,
    onScope: (SearchScope) -> Unit,
    onCommit: () -> Unit,
    /** Изчиства последните търсения; връща изчистените — за „Отмени“. */
    onClearRecent: () -> List<String>,
    onRestoreRecent: (List<String>) -> Unit,
    navigate: (String) -> Unit,
    openExternal: (String) -> Unit,
    openLink: (String) -> Unit,
    onRecent: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clearedMsg = stringResource(R.string.search_recent_cleared)
    val undoLabel = stringResource(R.string.action_undo)
    // Изчистването е с „Отмени“ вместо потвърждение — списъкът се връща с едно докосване.
    val clearRecent: () -> Unit = {
        val cleared = onClearRecent()
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            if (snackbar.showSnackbar(clearedMsg, actionLabel = undoLabel, duration = SnackbarDuration.Short) == SnackbarResult.ActionPerformed) {
                onRestoreRecent(cleared)
            }
        }
    }
    // Редовете — върху фона на контейнера (и в разгънатото поле на „Начало“ — обичайният фон),
    // а не със свой цвят surface.
    val itemColors = ListItemDefaults.colors(containerColor = Color.Transparent)
    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SearchScope.entries) { s ->
                    FilterChip(ui.scope == s, onClick = { onScope(s) }, label = {
                        Text(stringResource(when (s) {
                            SearchScope.ALL -> R.string.filter_all
                            SearchScope.BOOKS -> R.string.search_books
                            SearchScope.NEWS -> R.string.tab_news
                            SearchScope.EVENTS -> R.string.events_title
                            SearchScope.PAGES -> R.string.search_pages
                        }))
                    }, colors = appFilterChipColors())
                }
            }
            AnimatedContent(ui.query.isBlank(), transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "search") { blank ->
                if (blank) {
                    LazyColumn(Modifier.fillMaxSize()) {
                        if (recent.isNotEmpty()) {
                            item {
                                Row(
                                    Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(start = 16.dp, end = 4.dp, top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    SectionLabel(stringResource(R.string.search_recent), Modifier.weight(1f).semantics { heading() })
                                    TextButton(onClick = clearRecent) { Text(stringResource(R.string.action_clear)) }
                                }
                            }
                            items(recent) { q ->
                                ListItem(headlineContent = { Text(q) }, leadingContent = { Icon(FolkIcons.History, null, Modifier.size(22.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    modifier = Modifier.clickable { onRecent(q) }, colors = itemColors)
                            }
                        } else {
                            item { EmptyView(stringResource(R.string.search_start), stringResource(R.string.search_start_hint), icon = Icons.Outlined.Search) }
                        }
                    }
                } else {
                    Results(ui, navigate, openExternal, openLink, onOpen = onCommit, onScope = onScope, itemColors = itemColors)
                }
            }
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Results(
    ui: SearchUi,
    navigate: (String) -> Unit,
    openExternal: (String) -> Unit,
    openLink: (String) -> Unit,
    onOpen: () -> Unit,
    onScope: (SearchScope) -> Unit,
    itemColors: ListItemColors,
) {
    val r = ui.results
    val show = { s: SearchScope -> ui.scope == SearchScope.ALL || ui.scope == s }
    val limit = if (ui.scope == SearchScope.ALL) ALL_SCOPE_LIMIT else Int.MAX_VALUE
    // Скритите в „Всички“ — „Покажи всички (N)“ в заглавката на раздела отваря обхвата му.
    val more = { total: Int, s: SearchScope -> if (total > limit) ({ onScope(s) } to total) else null }
    val context = LocalContext.current
    if (r.isEmpty) {
        // Още се търси (напр. първото зареждане на каталога) — индикатор, не празен екран.
        if (ui.searching) {
            Box(Modifier.fillMaxSize().padding(top = 48.dp), contentAlignment = Alignment.TopCenter) { ChLoadingIndicator() }
        } else {
            EmptyView(stringResource(R.string.search_no_results), stringResource(R.string.search_no_results_hint), icon = Icons.Outlined.SearchOff)
        }
        return
    }
    val list = rememberLazyListState()
    // Към друг обхват (чип или „Покажи всички“) — от началото на списъка.
    LaunchedEffect(ui.scope) { list.scrollToItem(0) }
    LazyColumn(Modifier.fillMaxSize(), state = list, contentPadding = PaddingValues(bottom = 24.dp)) {
        if (show(SearchScope.BOOKS) && r.authors.isNotEmpty()) {
            item { Header(stringResource(R.string.search_authors), more(r.authors.size, SearchScope.BOOKS)) }
            items(r.authors.take(limit), key = { "a-$it" }) { a ->
                ListItem(headlineContent = { Text(a) }, leadingContent = { ResultPlate(FolkIcons.Profile, FolkMotif.RHOMBUS) },
                    // Книгите на автора — каталогът с филтър „Автор“.
                    modifier = Modifier.clickable { onOpen(); CatalogSearchRequests.byAuthor(a, navigate) }.animateItem(), colors = itemColors)
            }
        }
        if (show(SearchScope.BOOKS) && r.books.isNotEmpty()) {
            item { Header(stringResource(R.string.search_books), more(r.books.size, SearchScope.BOOKS)) }
            items(r.books.take(limit), key = { "b-" + it.book.inv }) { hit ->
                BookListItem(hit.book, onClick = { onOpen(); navigate(Routes.book(hit.book.inv)) }, modifier = Modifier.animateItem())
            }
        }
        if (show(SearchScope.NEWS) && r.news.isNotEmpty()) {
            item { Header(stringResource(R.string.tab_news), more(r.news.size, SearchScope.NEWS)) }
            items(r.news.take(limit), key = { "n-" + it.article.id }) { hit ->
                NewsRow(hit.article, onClick = { onOpen(); navigate(Routes.article(hit.article.id)) }, modifier = Modifier.animateItem())
            }
        }
        if (show(SearchScope.EVENTS) && r.events.isNotEmpty()) {
            item { Header(stringResource(R.string.events_title), more(r.events.size, SearchScope.EVENTS)) }
            items(r.events.take(limit), key = { "e-" + it.event.id }) { hit ->
                ListItem(
                    headlineContent = { Text(hit.event.title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    // Датата в езика на приложението („17 октомври 2026 г.“ / „October 17, 2026“), не ISO.
                    supportingContent = Formatters.date(context, hit.event.date)?.let { d -> { Text(listOfNotNull(d, hit.event.time).joinToString(" · ")) } },
                    leadingContent = { ResultPlate(FolkIcons.Events, FolkMotif.ROSETTE) },
                    modifier = Modifier.clickable { onOpen(); navigate(Routes.event(hit.event.id)) }.animateItem(),
                    colors = itemColors,
                )
            }
        }
        if (show(SearchScope.PAGES) && r.pages.isNotEmpty()) {
            item { Header(stringResource(R.string.search_pages), more(r.pages.size, SearchScope.PAGES)) }
            items(r.pages.take(limit), key = { "p-" + it.doc.id }) { hit ->
                val link = SiteLinkClassifier.link(hit.doc.title, hit.doc.url, "")
                ListItem(
                    overlineContent = hit.doc.category?.let { { Text(it) } },
                    headlineContent = { Text(hit.doc.title) },
                    supportingContent = hit.snippet.takeIf { it.isNotBlank() }?.let { snip -> { Text(snip, maxLines = 2, overflow = TextOverflow.Ellipsis) } },
                    // Страниците на библиотеката — ромб (както книгите); всичко друго от сайта — розетка.
                    leadingContent = { ResultPlate(pageIcon(link.kind), if (link.kind.isLibrary()) FolkMotif.RHOMBUS else FolkMotif.ROSETTE) },
                    modifier = Modifier.clickable {
                        onOpen()
                        // Документ/публикация → PDF файлът; иначе страницата (или котвата в нея).
                        val file = hit.doc.fileUrl
                        if (file != null) openExternal(file) else openLink(hit.doc.url)
                    }.animateItem(),
                    colors = itemColors,
                )
            }
        }
    }
}

/** Колко резултата от раздел се показват в обхвата „Всички“. */
internal const val ALL_SCOPE_LIMIT = 5

/** Заглавката на раздел — стебният етикет като навсякъде; [more] — („Покажи всички“, общият брой), ако не всички са показани. */
@Composable
private fun Header(text: String, more: Pair<() -> Unit, Int>? = null) = Row(
    Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(start = 16.dp, end = 4.dp, top = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    SectionLabel(text, Modifier.weight(1f).semantics { heading() })
    more?.let { (open, total) -> TextButton(onClick = open) { Text(stringResource(R.string.search_show_all, total)) } }
}

/** Малката плочка пред резултат (28 dp): ромб — библиотеката, розетка — читалището и сайтът. */
@Composable
private fun ResultPlate(icon: ImageVector, motif: FolkMotif) = FolkIconPlate(icon, motif, size = 28.dp)

private fun SiteSection.isLibrary() = this == SiteSection.LIBRARY || this == SiteSection.CATALOG

/** Иконата („Бод“) за страница от сайта — най-близката от набора; останалите — читалището. */
internal fun pageIcon(kind: SiteSection): ImageVector = when (kind) {
    SiteSection.LIBRARY, SiteSection.CATALOG -> FolkIcons.Books
    SiteSection.HISTORY -> FolkIcons.Timeline
    SiteSection.EVENTS -> FolkIcons.Events
    SiteSection.NEWS -> FolkIcons.News
    SiteSection.GALLERY, SiteSection.EXHIBITION -> FolkIcons.Gallery
    SiteSection.CONTACTS -> FolkIcons.Pin
    SiteSection.DOCUMENTS -> FolkIcons.Documents
    SiteSection.PUBLICATIONS -> FolkIcons.Newspaper
    SiteSection.DIGITAL_CLUB -> FolkIcons.Computer
    SiteSection.FOLKLORE, SiteSection.ENSEMBLE, SiteSection.DANCE, SiteSection.CLUBS, SiteSection.PROJECTS -> FolkIcons.Activities
    else -> FolkIcons.Chitalishte
}
