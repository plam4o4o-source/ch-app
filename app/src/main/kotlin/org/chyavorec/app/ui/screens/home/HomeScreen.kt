package org.chyavorec.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.PressableCard
import org.chyavorec.app.ui.components.RemoteImage
import org.chyavorec.app.ui.components.SectionHeader
import org.chyavorec.app.ui.components.SkeletonBox
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.shimmer
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.events.EventCard
import org.chyavorec.app.ui.screens.news.NewsRow
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.NewsArticle

private data class QuickAction(val labelRes: Int, val icon: ImageVector, val route: String)

private val quickActions = listOf(
    QuickAction(R.string.qa_catalog, Icons.AutoMirrored.Outlined.MenuBook, Routes.CATALOG),
    QuickAction(R.string.qa_my_library, Icons.Outlined.CollectionsBookmark, Routes.LOANS),
    QuickAction(R.string.qa_card, Icons.Outlined.Badge, Routes.CARD),
    QuickAction(R.string.qa_news, Icons.Outlined.Newspaper, Routes.NEWS),
    QuickAction(R.string.qa_events, Icons.Outlined.Event, Routes.EVENTS),
    QuickAction(R.string.qa_activities, Icons.Outlined.TheaterComedy, Routes.ACTIVITIES),
    QuickAction(R.string.qa_about, Icons.Outlined.AccountBalance, Routes.ABOUT_CHITALISHTE),
    QuickAction(R.string.qa_contacts, Icons.Outlined.Place, Routes.CONTACTS),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigate: (String) -> Unit) {
    val vm = appViewModel { HomeViewModel(it.newsRepository, it.eventsRepository, it.catalogRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    PullToRefreshBox(isRefreshing = state.news.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 24.dp), modifier = Modifier.fillMaxSize()) {
            item("header") { HomeHeader(onSearch = { navigate(Routes.SEARCH) }) }
            item("sync") { SyncBanner(state.news.fromCache, state.news.syncedAt, state.news.refreshError) }
            item("hero") {
                val hero = state.news.data?.firstOrNull()
                when {
                    hero != null -> {
                        // Лек parallax: изображението се движи по-бавно от списъка.
                        val offset = if (listState.firstVisibleItemIndex <= 2) listState.firstVisibleItemScrollOffset else 0
                        HeroCard(hero, parallax = offset.toFloat(), onClick = { navigate(Routes.article(hero.id)) })
                    }
                    state.news.showSkeleton -> Box(
                        Modifier.padding(16.dp).fillMaxWidth().height(260.dp).clip(RoundedCornerShape(28.dp)).shimmer(),
                    )
                    state.news.error != null -> ErrorView(state.news.error!!, onRetry = { vm.refresh() }, subject = stringResource(R.string.subject_news))
                }
            }
            item("actions") { QuickActionsGrid(navigate) }
            if (state.upcoming.isNotEmpty()) {
                item("events-h") {
                    SectionHeader(
                        stringResource(R.string.home_upcoming), label = stringResource(R.string.label_calendar),
                        actionLabel = stringResource(R.string.action_all), onAction = { navigate(Routes.EVENTS) },
                        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                    )
                }
                item("events") {
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.upcoming, key = { it.id }) { e ->
                            EventCard(e, onClick = { navigate(Routes.event(e.id)) }, modifier = Modifier.width(280.dp))
                        }
                    }
                }
            }
            if (state.newBooks.isNotEmpty()) {
                item("new-h") {
                    SectionHeader(
                        stringResource(R.string.home_new_books), label = stringResource(R.string.label_library),
                        actionLabel = stringResource(R.string.action_catalog), onAction = { navigate(Routes.CATALOG) },
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    )
                }
                item("new") { BookRow(state.newBooks) { navigate(Routes.book(it.inv)) } }
            }
            state.shelves.forEach { (name, books) ->
                item("shelf-$name") {
                    SectionHeader(name.replaceFirstChar { it.uppercase() }, label = stringResource(R.string.label_shelf), modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
                    BookRow(books) { navigate(Routes.book(it.inv)) }
                }
            }
            val more = state.news.data?.drop(1)?.take(4).orEmpty()
            if (more.isNotEmpty()) {
                item("news-h") {
                    SectionHeader(
                        stringResource(R.string.home_latest_news), label = stringResource(R.string.label_news),
                        actionLabel = stringResource(R.string.action_all), onAction = { navigate(Routes.NEWS) },
                        modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                    )
                }
                items(more, key = { "n-" + it.id }) { a -> NewsRow(a, onClick = { navigate(Routes.article(a.id)) }) }
            }
            if (state.catalogCount != null) {
                item("footer") {
                    Text(
                        stringResource(R.string.home_catalog_footer, state.catalogCount ?: 0, Formatters.shortDate(state.catalogGenerated) ?: "—"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(onSearch: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().statusBarsPadding().padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Emblem(46.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).semantics(mergeDescendants = true) { heading() }) {
            Text(stringResource(R.string.org_short), style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(stringResource(R.string.org_place), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onSearch, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.action_search))
        }
    }
}

@Composable
private fun HeroCard(article: NewsArticle, parallax: Float, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val context = LocalContext.current
    AnimatedVisibility(visible, enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 8 }) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.padding(16.dp).fillMaxWidth().aspectRatio(0.95f).widthIn(max = 720.dp),
        ) {
            Box {
                RemoteImage(
                    url = article.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().graphicsLayer { translationY = parallax * 0.35f; scaleX = 1.08f; scaleY = 1.08f },
                )
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(0f to Color.Transparent, 0.35f to Color.Transparent, 1f to Brand.Ink.copy(alpha = 0.92f)),
                    ),
                )
                Column(Modifier.align(Alignment.BottomStart).padding(22.dp)) {
                    Text(
                        (article.category ?: stringResource(R.string.label_news)).uppercase(),
                        style = MaterialTheme.typography.labelSmall, color = Brand.GoldLight,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        article.title, style = MaterialTheme.typography.headlineMedium, color = Brand.Parchment,
                        maxLines = 3, overflow = TextOverflow.Ellipsis,
                    )
                    Formatters.millisDate(context, article.publishedAtMillis)?.let {
                        Spacer(Modifier.height(6.dp))
                        Text(it, style = MaterialTheme.typography.labelMedium, color = Brand.Parchment.copy(alpha = 0.75f))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(navigate: (String) -> Unit) {
    Column(Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        quickActions.chunked(4).forEach { row ->
            Row(Modifier.fillMaxWidth()) {
                row.forEach { action ->
                    val label = stringResource(action.labelRes)
                    Column(
                        Modifier.weight(1f).clip(RoundedCornerShape(18.dp))
                            .clickable { navigate(action.route) }
                            .semantics { role = Role.Button }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            Modifier.size(54.dp).clip(RoundedCornerShape(18.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(action.icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 2, overflow = TextOverflow.Ellipsis,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun BookRow(books: List<CatalogBook>, onClick: (CatalogBook) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(books, key = { it.inv }) { b ->
            PressableCard(onClick = { onClick(b) }, modifier = Modifier.width(128.dp)) {
                Column(Modifier.padding(10.dp)) {
                    BookCover(b, width = 108.dp)
                    Spacer(Modifier.height(8.dp))
                    Text(b.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(b.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Suppress("unused")
@Composable
private fun HeroSkeleton() = SkeletonBox(height = 200.dp)
