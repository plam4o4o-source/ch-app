package org.chyavorec.app.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.ui.semantics.contentDescription
import kotlinx.coroutines.delay
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.AnimatedCounter
import org.chyavorec.app.ui.components.IconPlate
import org.chyavorec.app.ui.components.PagerDots
import org.chyavorec.app.ui.components.SearchPill
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.components.boldMarkdown
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.model.DailyFeast
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
import androidx.compose.material3.Badge as CountBadge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.BrandedImageFallback
import org.chyavorec.app.ui.components.TabReselectEffect
import org.chyavorec.app.ui.components.sharedElementKey
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

/** Четири основни действия — останалите са в долната лента или в „Още“. */
private val quickActions = listOf(
    QuickAction(R.string.qa_catalog, Icons.AutoMirrored.Outlined.MenuBook, Routes.CATALOG),
    QuickAction(R.string.qa_card, Icons.Outlined.Badge, Routes.CARD),
    QuickAction(R.string.qa_events, Icons.Outlined.Event, Routes.EVENTS),
    QuickAction(R.string.qa_contacts, Icons.Outlined.Place, Routes.CONTACTS),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigate: (String) -> Unit) {
    val vm = appViewModel { HomeViewModel(it.newsRepository, it.eventsRepository, it.catalogRepository, it.siteRepository, it.clock) }
    val state by vm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    TabReselectEffect(Routes.HOME) { listState.animateScrollToItem(0) }

    PullToRefreshBox(isRefreshing = state.news.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 24.dp), modifier = Modifier.fillMaxSize()) {
            item("header") { HomeHeader(onMessages = { navigate(Routes.MESSAGES) }) }
            item("search") {
                SearchPill(
                    stringResource(R.string.home_search_hint), onClick = { navigate(Routes.SEARCH) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
            state.feast?.let { f -> item("feast") { FeastLine(f) } }
            item("sync") { SyncBanner(state.news.fromCache, state.news.syncedAt, state.news.refreshError) }
            item("hero") {
                val top = state.news.data?.take(5).orEmpty()
                when {
                    top.isNotEmpty() -> HeroCarousel(top, onOpen = { navigate(Routes.article(it.id)) })
                    state.news.showSkeleton -> Box(
                        Modifier.padding(16.dp).fillMaxWidth().aspectRatio(1.6f).clip(MaterialTheme.shapes.large).shimmer(),
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
            val more = state.news.data?.drop(5)?.take(4).orEmpty()
            if (more.isNotEmpty()) {
                item("news-h") {
                    SectionHeader(
                        stringResource(R.string.home_latest_news), label = stringResource(R.string.label_news),
                        actionLabel = stringResource(R.string.action_all), onAction = { navigate(Routes.NEWS) },
                        modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                    )
                }
                itemsIndexed(more, key = { _, a -> "n-" + a.id }) { i, a ->
                    NewsRow(a, onClick = { navigate(Routes.article(a.id)) }, modifier = Modifier.animateEntrance(i))
                }
            }
            item("stats") { StatsRow(state) }
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
private fun HomeHeader(onMessages: () -> Unit) {
    val center = LocalAppContainer.current.messages
    val unread by center.unreadCount.collectAsStateWithLifecycle(initialValue = 0)
    LaunchedEffect(Unit) { runCatching { center.refresh(force = false) } }
    // Значката „подскача“ при поява или нов брой непрочетени (без анимация при намалено движение).
    val reduced = rememberReducedMotion()
    val badgeScale = remember { Animatable(1f) }
    var lastUnread by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(unread) {
        val grew = unread > lastUnread
        lastUnread = unread
        if (grew && !reduced) {
            badgeScale.snapTo(0.3f)
            badgeScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
        } else {
            badgeScale.snapTo(1f)
        }
    }
    Row(
        Modifier.fillMaxWidth().statusBarsPadding().padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Emblem(50.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).semantics(mergeDescendants = true) { heading() }) {
            // Пълното име трябва да се чете: до два реда, малко по-дребен шрифт.
            Text(
                stringResource(R.string.org_short),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, lineHeight = 23.sp, lineBreak = LineBreak.Heading),
                maxLines = 2, overflow = TextOverflow.Ellipsis,
            )
            Text(stringResource(R.string.org_place), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onMessages, modifier = Modifier.size(48.dp)) {
            val label = if (unread > 0) pluralStringResource(R.plurals.messages_unread, unread, unread) else stringResource(R.string.messages_title)
            BadgedBox(
                badge = {
                    if (unread > 0) {
                        CountBadge(Modifier.graphicsLayer { scaleX = badgeScale.value; scaleY = badgeScale.value }) {
                            Text(if (unread > 9) "9+" else unread.toString())
                        }
                    }
                },
            ) {
                Icon(Icons.Outlined.NotificationsNone, contentDescription = label)
            }
        }
    }
}

/**
 * Въртяща се лента с последните новини: автоматично превъртане на 6 s (спира,
 * докато потребителят плъзга), parallax на снимката и индикатор на страниците.
 */
@Composable
private fun HeroCarousel(items: List<NewsArticle>, onOpen: (NewsArticle) -> Unit) {
    val pager = rememberPagerState { items.size }
    val reduced = rememberReducedMotion()
    val dragged by pager.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(dragged, items.size) {
        if (reduced || dragged || items.size < 2) return@LaunchedEffect
        while (true) {
            delay(6_000)
            pager.animateScrollToPage((pager.currentPage + 1) % items.size, animationSpec = tween(700))
        }
    }
    val context = LocalContext.current
    val carouselLabel = stringResource(R.string.home_carousel)
    Column {
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.padding(top = 12.dp).semantics { contentDescription = carouselLabel },
        ) { page ->
            val a = items[page]
            val offset = (pager.currentPage - page) + pager.currentPageOffsetFraction
            Surface(
                onClick = { onOpen(a) },
                shape = MaterialTheme.shapes.large,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.6f).widthIn(max = 720.dp)
                    .graphicsLayer {
                        val scale = 1f - 0.06f * kotlin.math.abs(offset).coerceIn(0f, 1f)
                        scaleX = scale; scaleY = scale
                    },
            ) {
                // Без снимка (или при грешка) — фирмен фон с воден знак, без тъмния воал.
                var imageFailed by remember(a.imageUrl) { mutableStateOf(false) }
                val branded = a.imageUrl.isNullOrBlank() || imageFailed
                Box {
                    if (branded) {
                        BrandedImageFallback(Modifier.fillMaxSize())
                    } else {
                        RemoteImage(
                            url = a.imageUrl,
                            contentDescription = null,
                            containerColor = Brand.InkSoft,
                            onFailure = { imageFailed = true },
                            modifier = Modifier.sharedElementKey("news-${a.id}").fillMaxSize().graphicsLayer {
                                translationX = offset * size.width * 0.35f
                                scaleX = 1.15f; scaleY = 1.15f
                            },
                        )
                        Box(
                            Modifier.fillMaxSize().background(
                                Brush.verticalGradient(
                                    0f to Color.Transparent, 0.3f to Color.Transparent,
                                    0.65f to Brand.Ink.copy(alpha = 0.6f), 1f to Brand.Ink.copy(alpha = 0.96f),
                                ),
                            ),
                        )
                    }
                    Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
                        Text(
                            (a.category ?: stringResource(R.string.label_news)).uppercase(),
                            style = MaterialTheme.typography.labelSmall, color = Brand.GoldLight,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(a.title, style = MaterialTheme.typography.headlineSmall, color = Brand.Parchment, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Formatters.millisDate(context, a.publishedAtMillis)?.let {
                            Spacer(Modifier.height(6.dp))
                            Text(it, style = MaterialTheme.typography.labelMedium, color = Brand.Parchment.copy(alpha = 0.78f))
                        }
                    }
                }
            }
        }
        PagerDots(items.size, pager.currentPage, Modifier.padding(top = 8.dp))
    }
}

/** Православният празник за деня — един ред под търсенето (от /api/calendar на сайта). */
@Composable
private fun FeastLine(feast: DailyFeast) {
    val context = LocalContext.current
    val date = Formatters.date(context, LocalAppContainer.current.clock.today())
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 6.dp).animateEntrance(0),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(Icons.Outlined.AutoAwesome, null, tint = LocalExtendedColors.current.gold, modifier = Modifier.padding(top = 2.dp).size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            boldMarkdown("$date · ${feast.line}"),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2, overflow = TextOverflow.Ellipsis,
        )
    }
}

/** „Читалището в числа“ — анимирани броячи (като на сайта). */
@Composable
private fun StatsRow(state: HomeUiState) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 24.dp),
    ) {
        Row(Modifier.padding(vertical = 18.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            AnimatedCounter(state.yearsSinceFounding, stringResource(R.string.stat_years), Modifier.weight(1f))
            state.catalogCount?.let { AnimatedCounter(it, stringResource(R.string.stat_books), Modifier.weight(1f)) }
            state.eventsThisMonth?.let { AnimatedCounter(it, stringResource(R.string.stat_events), Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun QuickActionsGrid(navigate: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
        quickActions.forEachIndexed { i, action ->
            Column(
                Modifier.weight(1f).animateEntrance(i).clip(MaterialTheme.shapes.medium)
                    .clickable { navigate(action.route) }
                    .semantics { role = Role.Button }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconPlate(action.icon)
                Spacer(Modifier.height(6.dp))
                Text(stringResource(action.labelRes), style = MaterialTheme.typography.labelMedium, maxLines = 2, overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
