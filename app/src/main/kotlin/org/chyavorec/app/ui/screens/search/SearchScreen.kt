package org.chyavorec.app.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.BookListItem
import org.chyavorec.app.ui.screens.news.NewsRow
import org.chyavorec.app.ui.screens.site.openSiteLink
import org.chyavorec.app.ui.screens.site.sectionIcon
import org.chyavorec.core.Outcome
import org.chyavorec.data.SearchAggregator
import org.chyavorec.data.SearchResults
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage

enum class SearchScope { ALL, BOOKS, NEWS, EVENTS, PAGES }

data class SearchUi(val query: String = "", val scope: SearchScope = SearchScope.ALL, val results: SearchResults = SearchResults(), val searching: Boolean = false)

/**
 * Глобално търсене: каталог, автори, новини, събития и страници на сайта.
 * Работи върху вече заредените (и кеширани) данни — мигновено и офлайн.
 */
@OptIn(FlowPreview::class)
class GlobalSearchViewModel(private val c: AppContainer) : ViewModel() {
    val ui = MutableStateFlow(SearchUi())
    val recent = c.settings.recentSearches.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val query = MutableStateFlow("")
    private var pages: List<Pair<SiteLink, SitePage?>> = emptyList()

    init {
        viewModelScope.launch {
            // Зареждаме данните за търсене (от кеша, при нужда — от мрежата).
            c.catalogRepository.cached() ?: c.catalogRepository.catalog(false)
            if (c.newsRepository.cached() == null) c.newsRepository.latest(false)
            val links = (c.siteRepository.links(false) as? Outcome.Success)?.value?.data.orEmpty()
            pages = links.map { it to c.siteRepository.cachedPage(it.url) }
            run(query.value)
        }
        viewModelScope.launch { query.debounce(180).collect { run(it) } }
    }

    fun setQuery(q: String) { ui.update { it.copy(query = q, searching = q.isNotBlank()) }; query.value = q }
    fun setScope(s: SearchScope) = ui.update { it.copy(scope = s) }
    fun commit() = viewModelScope.launch { c.settings.addRecentSearch(ui.value.query) }
    fun clearRecent() = viewModelScope.launch { c.settings.clearRecentSearches() }

    private suspend fun run(q: String) {
        val engine = c.catalogRepository.inMemory()?.data
        val news = c.newsRepository.cached()?.data.orEmpty()
        val events = c.eventsRepository.cached()?.data.orEmpty()
        val r = withContext(Dispatchers.Default) { SearchAggregator.search(q, engine, news, events, pages) }
        ui.update { if (it.query == q) it.copy(results = r, searching = false) else it }
    }
}

@Composable
fun SearchScreen(onBack: () -> Unit, navigate: (String) -> Unit, openExternal: (String) -> Unit) {
    val vm = appViewModel { GlobalSearchViewModel(it) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val recent by vm.recent.collectAsStateWithLifecycle()
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }

    Scaffold(topBar = {
        Column(Modifier.statusBarsPadding().padding(horizontal = 8.dp, vertical = 6.dp)) {
            OutlinedTextField(
                value = ui.query, onValueChange = vm::setQuery,
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.action_back)) } },
                trailingIcon = { if (ui.query.isNotEmpty()) IconButton(onClick = { vm.setQuery("") }) { Icon(Icons.Outlined.Clear, stringResource(R.string.action_clear)) } },
                singleLine = true,
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { vm.commit() }),
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
            )
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SearchScope.entries) { s ->
                    FilterChip(ui.scope == s, onClick = { vm.setScope(s) }, label = {
                        Text(stringResource(when (s) {
                            SearchScope.ALL -> R.string.filter_all
                            SearchScope.BOOKS -> R.string.search_books
                            SearchScope.NEWS -> R.string.tab_news
                            SearchScope.EVENTS -> R.string.events_title
                            SearchScope.PAGES -> R.string.search_pages
                        }))
                    })
                }
            }
        }
    }) { padding ->
        AnimatedContent(ui.query.isBlank(), transitionSpec = { fadeIn() togetherWith fadeOut() }, modifier = Modifier.padding(padding), label = "search") { blank ->
            if (blank) {
                LazyColumn(Modifier.fillMaxSize()) {
                    if (recent.isNotEmpty()) {
                        item {
                            ListItem(
                                headlineContent = { Text(stringResource(R.string.search_recent), style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { heading() }) },
                                trailingContent = { TextButton(onClick = { vm.clearRecent() }) { Text(stringResource(R.string.action_clear)) } },
                            )
                        }
                        items(recent) { q ->
                            ListItem(headlineContent = { Text(q) }, leadingContent = { Icon(Icons.Outlined.History, null) },
                                modifier = Modifier.clickable { vm.setQuery(q) })
                        }
                    } else {
                        item { EmptyView(stringResource(R.string.search_start), stringResource(R.string.search_start_hint)) }
                    }
                }
            } else {
                Results(ui, navigate, openExternal, onOpen = { vm.commit() })
            }
        }
    }
}

@Composable
private fun Results(ui: SearchUi, navigate: (String) -> Unit, openExternal: (String) -> Unit, onOpen: () -> Unit) {
    val r = ui.results
    val show = { s: SearchScope -> ui.scope == SearchScope.ALL || ui.scope == s }
    val limit = if (ui.scope == SearchScope.ALL) 5 else Int.MAX_VALUE
    if (r.isEmpty && !ui.searching) {
        EmptyView(stringResource(R.string.search_no_results), stringResource(R.string.catalog_no_results_hint), icon = Icons.Outlined.SearchOff)
        return
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        if (show(SearchScope.BOOKS) && r.authors.isNotEmpty()) {
            item { Header(stringResource(R.string.search_authors)) }
            items(r.authors.take(limit), key = { "a-$it" }) { a ->
                ListItem(headlineContent = { Text(a) }, leadingContent = { Icon(Icons.Outlined.Person, null) },
                    modifier = Modifier.clickable { onOpen(); navigate(Routes.CATALOG) }.animateItem())
            }
        }
        if (show(SearchScope.BOOKS) && r.books.isNotEmpty()) {
            item { Header(stringResource(R.string.search_books)) }
            items(r.books.take(limit), key = { "b-" + it.book.inv }) { hit ->
                BookListItem(hit.book, onClick = { onOpen(); navigate(Routes.book(hit.book.inv)) }, modifier = Modifier.animateItem())
            }
        }
        if (show(SearchScope.NEWS) && r.news.isNotEmpty()) {
            item { Header(stringResource(R.string.tab_news)) }
            items(r.news.take(limit), key = { "n-" + it.article.id }) { hit ->
                NewsRow(hit.article, onClick = { onOpen(); navigate(Routes.article(hit.article.id)) }, modifier = Modifier.animateItem())
            }
        }
        if (show(SearchScope.EVENTS) && r.events.isNotEmpty()) {
            item { Header(stringResource(R.string.events_title)) }
            items(r.events.take(limit), key = { "e-" + it.event.id }) { hit ->
                ListItem(
                    headlineContent = { Text(hit.event.title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    supportingContent = hit.event.date?.let { { Text(it) } },
                    leadingContent = { Icon(Icons.Outlined.Event, null) },
                    modifier = Modifier.clickable { onOpen(); navigate(Routes.event(hit.event.id)) }.animateItem(),
                )
            }
        }
        if (show(SearchScope.PAGES) && r.pages.isNotEmpty()) {
            item { Header(stringResource(R.string.search_pages)) }
            items(r.pages.take(limit), key = { "p-" + it.link.url }) { hit ->
                ListItem(
                    headlineContent = { Text(hit.link.title) },
                    supportingContent = { Text(hit.snippet, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    leadingContent = { Icon(sectionIcon(hit.link.kind), null) },
                    modifier = Modifier.clickable { onOpen(); openSiteLink(hit.link, navigate, openExternal) }.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun Header(text: String) = Text(
    text, style = MaterialTheme.typography.titleLarge,
    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp).semantics { heading() },
)

@Suppress("unused")
private val align = Alignment.Center
