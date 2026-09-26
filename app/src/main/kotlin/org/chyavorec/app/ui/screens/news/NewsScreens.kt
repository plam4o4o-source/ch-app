package org.chyavorec.app.ui.screens.news

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.ContentBlocksView
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.RemoteImage
import org.chyavorec.app.ui.components.SkeletonBox
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.NewsArticle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(navigate: (String) -> Unit) {
    val vm = appViewModel { NewsListViewModel(it.newsRepository, it.database.favorites(), it.clock) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tab_news), style = MaterialTheme.typography.headlineMedium) },
                scrollBehavior = scroll,
                colors = TopAppBarDefaults.topAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer),
            )
        },
    ) { padding ->
        PullToRefreshBox(ui.state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = ui.filter.query,
                    onValueChange = vm::setQuery,
                    placeholder = { Text(stringResource(R.string.news_search_hint)) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                )
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(ui.filter.category == null && !ui.filter.favoritesOnly, onClick = { vm.setCategory(null); vm.setFavoritesOnly(false) },
                            label = { Text(stringResource(R.string.filter_all)) })
                    }
                    item {
                        FilterChip(ui.filter.favoritesOnly, onClick = { vm.setFavoritesOnly(!ui.filter.favoritesOnly) },
                            label = { Text(stringResource(R.string.news_saved)) },
                            leadingIcon = { Icon(Icons.Filled.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp)) })
                    }
                    items(ui.categories) { c ->
                        FilterChip(ui.filter.category == c, onClick = { vm.setCategory(if (ui.filter.category == c) null else c) }, label = { Text(c) })
                    }
                }
                SyncBanner(ui.state.fromCache, ui.state.syncedAt, ui.state.refreshError)
                StateContent(
                    state = ui.state.map { ui.visible },
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList() },
                    empty = {
                        EmptyView(
                            if (ui.filter.favoritesOnly) stringResource(R.string.news_no_saved) else stringResource(R.string.news_empty),
                            icon = Icons.Outlined.Newspaper,
                        )
                    },
                    errorSubject = stringResource(R.string.subject_news),
                ) { list ->
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        itemsIndexed(list, key = { _, a -> a.id }) { index, a ->
                            if (index == 0 && ui.filter.query.isBlank() && !ui.filter.favoritesOnly) {
                                FeaturedNews(a, onClick = { navigate(Routes.article(a.id)) })
                            } else {
                                NewsRow(
                                    a,
                                    onClick = { navigate(Routes.article(a.id)) },
                                    isFavorite = a.id in ui.favoriteIds,
                                    onToggleFavorite = { vm.toggleFavorite(a, a.id in ui.favoriteIds) },
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedNews(a: NewsArticle, onClick: () -> Unit) {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp)) {
        if (a.imageUrl != null) {
            RemoteImage(a.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f).clip(MaterialTheme.shapes.large))
            Spacer(Modifier.height(12.dp))
        }
        MetaLine(a.category, Formatters.millisDate(context, a.publishedAtMillis))
        Text(a.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 4.dp))
        if (a.summary.isNotBlank()) {
            Text(a.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

@Composable
private fun MetaLine(category: String?, date: String?) {
    val parts = listOfNotNull(category?.uppercase(), date)
    if (parts.isEmpty()) return
    Text(parts.joinToString("  ·  "), style = MaterialTheme.typography.labelSmall, color = LocalExtendedColors.current.gold)
}

@Composable
fun NewsRow(
    a: NewsArticle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean? = null,
    onToggleFavorite: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    Row(
        modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RemoteImage(a.imageUrl, contentDescription = null, modifier = Modifier.size(84.dp).clip(RoundedCornerShape(14.dp)))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            MetaLine(a.category, Formatters.millisDate(context, a.publishedAtMillis))
            Text(a.title, style = MaterialTheme.typography.titleMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
        if (isFavorite != null && onToggleFavorite != null) {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = stringResource(if (isFavorite) R.string.action_unsave else R.string.action_save),
                    tint = if (isFavorite) LocalExtendedColors.current.gold else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(id: String, onBack: () -> Unit, navigate: (String) -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel(key = "article-$id") { ArticleViewModel(id, it.newsRepository, it.database.favorites(), it.clock) }
    val state by vm.state.collectAsStateWithLifecycle()
    val fav by vm.isFavorite.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scroll = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            BackTopBar(
                title = "",
                onBack = onBack,
                scrollBehavior = scroll,
                actions = {
                    state.data?.article?.let { a ->
                        IconButton(onClick = { vm.toggleFavorite() }) {
                            Icon(
                                if (fav) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = stringResource(if (fav) R.string.action_unsave else R.string.action_save),
                            )
                        }
                        IconButton(onClick = { Intents.share(context, a.title, a.title + "\n" + a.url) }) {
                            Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.action_share))
                        }
                        IconButton(onClick = { Intents.openUrl(context, a.url) }) {
                            Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = stringResource(R.string.action_open_site))
                        }
                    }
                },
            )
        },
    ) { padding ->
        StateContent(
            state = state,
            onRetry = { vm.load() },
            isEmpty = { false },
            skeleton = { SkeletonCards() },
            empty = {},
            modifier = Modifier.padding(padding),
            errorSubject = stringResource(R.string.subject_article),
        ) { detail ->
            val a = detail.article
            val gallery = detail.gallery
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 32.dp)) {
                item { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                a.imageUrl?.let { hero ->
                    item {
                        RemoteImage(
                            hero, contentDescription = null,
                            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f).padding(horizontal = 16.dp).clip(MaterialTheme.shapes.large)
                                .clickable { navigate(Routes.viewerUrls(gallery, gallery.indexOf(hero))) },
                        )
                    }
                }
                item {
                    Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp).widthIn(max = 720.dp)) {
                        MetaLine(a.category, Formatters.millisDate(context, a.publishedAtMillis))
                        Spacer(Modifier.height(6.dp))
                        Text(a.title, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.semantics { heading() })
                        Spacer(Modifier.height(16.dp))
                        if (state.loading && detail.blocks.isEmpty()) {
                            Text(a.summary, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(12.dp))
                            SkeletonBox(height = 14.dp); Spacer(Modifier.height(8.dp)); SkeletonBox(height = 14.dp)
                        } else if (detail.blocks.isEmpty()) {
                            Text(a.summary, style = MaterialTheme.typography.bodyLarge)
                        } else {
                            ContentBlocksView(
                                detail.blocks.filterNot { it is org.chyavorec.domain.model.ContentBlock.Image && it.url == a.imageUrl },
                                onLink = openLink,
                                onImage = { url -> navigate(Routes.viewerUrls(listOf(url), 0)) },
                                modifier = Modifier.animateContentSize(),
                                skipImages = true,
                            )
                        }
                    }
                }
                val extra = gallery.filter { it != a.imageUrl }
                if (extra.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.article_gallery), style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp).semantics { heading() })
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            itemsIndexed(extra) { i, url ->
                                RemoteImage(url, contentDescription = stringResource(R.string.photo_n, i + 1),
                                    modifier = Modifier.size(150.dp, 112.dp).clip(RoundedCornerShape(14.dp)).clickable { navigate(Routes.viewerUrls(gallery, gallery.indexOf(url))) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Suppress("unused")
private val boxAlign = Box::class
