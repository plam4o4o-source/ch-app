package org.chyavorec.app.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.derivedStateOf
import org.chyavorec.app.ui.components.PersonalCovers
import org.chyavorec.app.ui.components.sharedElementKey
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
import org.chyavorec.app.ui.components.prepareCountUpToday
import org.chyavorec.app.ui.components.rememberCountUpToday
import org.chyavorec.app.ui.components.IconPlate
import org.chyavorec.app.ui.components.OrnamentFrame
import org.chyavorec.app.ui.components.Season
import org.chyavorec.app.ui.components.HolidayGreeting
import org.chyavorec.app.ui.components.HolidayOrnament
import org.chyavorec.app.ui.components.HolidaySky
import org.chyavorec.app.ui.components.rememberDayDecor
import org.chyavorec.app.ui.components.rememberToday
import kotlinx.coroutines.flow.map
import org.chyavorec.domain.occasion.OccasionEffect
import org.chyavorec.domain.occasion.OccasionOrnament
import org.chyavorec.app.ui.components.WheatEdge
import org.chyavorec.app.ui.components.rememberSeason
import org.chyavorec.app.ui.components.PagerDots
import org.chyavorec.app.ui.components.SearchPill
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.components.darkTopHighlight
import org.chyavorec.app.ui.components.boldMarkdown
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.components.MotionBudget
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.model.DailyFeast
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Badge as CountBadge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.platform.LocalDensity
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.BrandedImageFallback
import org.chyavorec.app.ui.components.TabReselectEffect
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.PressableCard
import org.chyavorec.app.ui.components.SiteImage
import org.chyavorec.app.ui.components.SectionHeader
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.shimmer
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.events.EventCard
import org.chyavorec.app.ui.screens.news.NewsRow
import org.chyavorec.app.ui.screens.my.LoanDueCard
import org.chyavorec.app.ui.screens.my.rememberLoanSummary
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
    val vm = appViewModel {
        HomeViewModel(
            it.newsRepository, it.eventsRepository, it.catalogRepository, it.siteRepository, it.clock,
            it.libraryRepository, it.authRepository,
            seasonal = it.settings.settings.map { s -> s.seasonal }, dismissedOccasion = it.settings.dismissedOccasion,
        )
    }
    val state by vm.state.collectAsStateWithLifecycle()
    val dismissedGreeting by vm.dismissed.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    TabReselectEffect(Routes.HOME) { listState.animateScrollToItem(0) }
    // Дали броячите „отброяват“ днес — прочита се отрано и извън главната нишка.
    val appContext = LocalContext.current.applicationContext
    LaunchedEffect(Unit) { prepareCountUpToday(appContext) }
    // Празници, сезони и обичаи: ефект, орнамент и поздрав за днес. Денят се опреснява при
    // връщане към приложението и в полунощ; местните празници са в ViewModel-а (без „скок“).
    val today = rememberToday()
    LaunchedEffect(today) { vm.onDay(today) }
    val decor = rememberDayDecor(state.occasions, today)
    val loanSummary = rememberLoanSummary()

    LeafRefreshBox(isRefreshing = state.news.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.fillMaxSize()) {
        // На таблет/хоризонтално съдържанието е центрирано и не по-широко от 840 dp.
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 24.dp),
            // Хоризонтално: отдясно е системната навигация (3 бутона) или изрязването за камерата —
            // отляво ги поема страничната лента.
            modifier = Modifier.align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.End))
                .widthIn(max = 840.dp).fillMaxSize(),
        ) {
            item("header") {
                Box {
                    HomeHeader(onMessages = { navigate(Routes.MESSAGES) }, ornament = decor.ornament, quiet = decor.quiet)
                    // Празничен/сезонен ефект (сняг, листа, фойерверки, букви…) само върху заглавката.
                    if (decor.effect != OccasionEffect.NONE) {
                        HolidaySky(
                            decor.effect,
                            Modifier.matchParentSize(),
                            snowColor = if (LocalExtendedColors.current.isDark) Color.White else Color(0xFF7A93AB),
                            // Първото превъртане спира ефекта (чете се в анимацията, не в композицията).
                            stopWhen = { listState.isScrollInProgress || listState.firstVisibleItemIndex > 0 },
                        )
                    }
                }
            }
            // Ако поздравът дойде след първия кадър (местен празник от сайта), се появява плавно.
            if (decor.greeting != null) {
                item("greeting") { HolidayGreeting(decor, navigate, Modifier.animateItem(), dismissed = dismissedGreeting, today = today) }
            }
            item("search") {
                SearchPill(
                    stringResource(R.string.home_search_hint), onClick = { navigate(Routes.SEARCH) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    trailing = {
                        // Скенер на баркодове: ISBN на книга или етикет с инвентарен номер.
                        IconButton(onClick = { navigate(Routes.SCAN) }, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Outlined.QrCodeScanner, contentDescription = stringResource(R.string.scan_action), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                )
            }
            // „Следващ срок / Просрочено“ — само за влязъл читател със заети книги (от кеша, без мрежа).
            loanSummary?.let { s ->
                item("loans") {
                    LoanDueCard(s, onClick = { navigate(Routes.LOANS) }, modifier = Modifier.animateItem().padding(horizontal = 16.dp, vertical = 6.dp))
                }
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
            // „За теб“: по историята на влезлия читател, иначе „Най-четени и нови“. Изчислява се
            // във ViewModel-а след първия кадър — затова се появява плавно (animateItem).
            if (state.forYou.isNotEmpty()) {
                item("for-you-h") {
                    SectionHeader(
                        stringResource(R.string.home_for_you),
                        label = stringResource(if (state.forYouPersonal) R.string.home_for_you_personal else R.string.home_for_you_popular),
                        modifier = Modifier.animateItem().padding(top = 24.dp, bottom = 8.dp),
                    )
                }
                // Подбрано по историята на читателя — без корици от Open Library (PersonalCovers).
                item("for-you") { PersonalCovers { BookRow(state.forYou, Modifier.animateItem()) { navigate(Routes.book(it.inv, personal = true)) } } }
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
            // Само числата — новини/рафтове не прекомпозират реда с броячите.
            item("stats") { StatsRow(state.yearsSinceFounding, state.catalogCount, state.eventsThisMonth) }
            if (state.catalogCount != null) {
                item("footer") {
                    Text(
                        pluralStringResource(R.plurals.home_catalog_footer, state.catalogCount ?: 0, state.catalogCount ?: 0, Formatters.shortDate(LocalContext.current, state.catalogGenerated) ?: "—"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    )
                }
            }
        }
        // Началото няма горна лента: превъртяното съдържание минава под прозрачната лента на
        // състоянието — щом е превъртяно, под часовника и иконите се появява фон, за да се четат.
        val scrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }
        val scrim by animateFloatAsState(if (scrolled) 0.94f else 0f, label = "status-scrim")
        Box(
            Modifier.align(Alignment.TopCenter).fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .graphicsLayer { alpha = scrim }
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@Composable
private fun HomeHeader(onMessages: () -> Unit, ornament: OccasionOrnament = OccasionOrnament.NONE, quiet: Boolean = false) {
    val center = LocalAppContainer.current.messages
    // Самото опресняване на съобщенията е веднъж на процес (MessagesOnLaunch в корена), не тук:
    // заглавката се показва наново при всяко връщане към „Начало“ и превъртане нагоре.
    val unread by center.unreadCount.collectAsStateWithLifecycle(initialValue = 0)
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
        if (ornament != OccasionOrnament.NONE) HolidayOrnament(ornament, Modifier.padding(end = 6.dp), quiet = quiet)
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

/** Тъмен воал под текста на въртележката — еднакъв за всички страници, създава се веднъж. */
private val HeroScrim = Brush.verticalGradient(
    0f to Color.Transparent, 0.3f to Color.Transparent,
    0.65f to Brand.Ink.copy(alpha = 0.6f), 1f to Brand.Ink.copy(alpha = 0.96f),
)

/**
 * Въртяща се лента с последните новини: автоматично превъртане на 6 s, parallax на
 * снимката и индикатор на страниците. Само превъртане спира след ~30 s
 * ([MotionBudget.carouselAdvances]), изчаква, докато потребителят я докосва/плъзга или
 * приложението не е на преден план, и изобщо не тръгва при намалено движение.
 */
@Composable
private fun HeroCarousel(items: List<NewsArticle>, onOpen: (NewsArticle) -> Unit) {
    val pager = rememberPagerState { items.size }
    val reduced = rememberReducedMotion()
    val dragged by pager.interactionSource.collectIsDraggedAsState()
    // Пръст върху лентата (и без плъзгане) — следващата смяна изчаква пускането.
    var touched by remember { mutableStateOf(false) }
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    // Колко пъти е превъртяла сама — пази се и при връщане към „Начало“.
    var advances by rememberSaveable { mutableIntStateOf(0) }
    val auto = MotionBudget.carouselAutoAdvance(
        reduced = reduced, touched = dragged || touched,
        resumed = lifecycleState.isAtLeast(Lifecycle.State.RESUMED),
        advancesDone = advances, itemCount = items.size,
    )
    // Всяка пауза (докосване, фон) започва интервала отначало.
    LaunchedEffect(auto, advances, items.size) {
        if (!auto) return@LaunchedEffect
        delay(MotionBudget.CAROUSEL_INTERVAL_MS)
        pager.animateScrollToPage((pager.currentPage + 1) % items.size, animationSpec = tween(700))
        advances++
    }
    val context = LocalContext.current
    val carouselLabel = stringResource(R.string.home_carousel)
    val season = rememberSeason()
    Column {
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.padding(top = 12.dp).semantics { contentDescription = carouselLabel }
                .pointerInput(Unit) {
                    // Само наблюдава (Initial, без consume) — докосването и плъзгането стигат до страниците.
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                        touched = true
                        try {
                            do {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                            } while (event.changes.any { it.pressed })
                        } finally {
                            touched = false
                        }
                    }
                },
        ) { page ->
            val a = items[page]
            // Отместването се чете само в graphicsLayer — при плъзгане страницата не се прекомпозира на всеки кадър.
            val offset = { (pager.currentPage - page) + pager.currentPageOffsetFraction }
            // Пропорция 1.6:1 като минимум — при едър шрифт картата расте на височина,
            // вместо заглавието да се отреже.
            BoxWithConstraints {
            val minHeight = maxWidth.coerceAtMost(720.dp) / 1.6f
            // Ширината за снимката — maxWidth се чете тук, в обхвата на BoxWithConstraints. Без мащаба
            // 1.15 на паралакса: същият адрес (ширина) като в статията — една заявка и един запис в кеша.
            val imageWidth = maxWidth.coerceAtMost(720.dp)
            Surface(
                onClick = { onOpen(a) },
                shape = MaterialTheme.shapes.large,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().widthIn(max = 720.dp).heightIn(min = minHeight)
                    .graphicsLayer {
                        val scale = 1f - 0.06f * kotlin.math.abs(offset()).coerceIn(0f, 1f)
                        scaleX = scale; scaleY = scale
                    }
                    .darkTopHighlight(MaterialTheme.shapes.large),
            ) {
                // Без снимка (или при грешка) — фирмен фон с воден знак, без тъмния воал.
                var imageFailed by remember(a.imageUrl) { mutableStateOf(false) }
                val branded = a.imageUrl.isNullOrBlank() || imageFailed
                Box {
                    Box(Modifier.matchParentSize()) {
                    if (branded) {
                        BrandedImageFallback(Modifier.fillMaxSize())
                    } else {
                        SiteImage(
                            url = a.imageUrl,
                            width = imageWidth,
                            contentDescription = null,
                            containerColor = Brand.InkSoft,
                            onFailure = { imageFailed = true },
                            // Без shared element: същият ключ има и списъкът „Новини“, а при смяна на
                            // таб двата източника биха се засекли (въртележката е и с паралакс/мащаб).
                            modifier = Modifier.fillMaxSize().graphicsLayer {
                                translationX = offset() * size.width * 0.35f
                                scaleX = 1.15f; scaleY = 1.15f
                            },
                        )
                        Box(Modifier.fillMaxSize().background(HeroScrim))
                    }
                    }
                    Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
                        Text(
                            (a.category ?: stringResource(R.string.label_news)).uppercase(),
                            style = MaterialTheme.typography.labelSmall, color = Brand.GoldLight,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(a.title, style = MaterialTheme.typography.headlineSmall, color = Brand.Parchment, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        val date = remember(a.publishedAtMillis) { Formatters.millisDate(context, a.publishedAtMillis) }
                        date?.let {
                            Spacer(Modifier.height(6.dp))
                            Text(it, style = MaterialTheme.typography.labelMedium, color = Brand.Parchment.copy(alpha = 0.78f))
                        }
                    }
                    // Житни класове по горния ръб (септември, Кръстовден) — текстът е долу и
                    // не се застъпва с тях; златна шевица по ъглите.
                    if (season == Season.HARVEST) WheatEdge(Modifier.align(Alignment.TopCenter).padding(top = 10.dp))
                    OrnamentFrame(Modifier.matchParentSize())
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
    val today = LocalAppContainer.current.clock.today()
    val date = remember(today) { Formatters.date(context, today) }
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
private fun StatsRow(yearsSinceFounding: Int, catalogCount: Int?, eventsThisMonth: Int?) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 24.dp)
            .darkTopHighlight(MaterialTheme.shapes.medium),
    ) {
        // Отброяване от 0 само при първото показване за деня; после — направо крайните стойности.
        // Докато настройката се чете (обикновено вече е прочетена), редът заема мястото си, но е невидим.
        val countUp = rememberCountUpToday()
        key(countUp) {
            Row(
                Modifier.padding(vertical = 18.dp, horizontal = 8.dp).graphicsLayer { alpha = if (countUp == null) 0f else 1f },
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                val animate = countUp == true
                AnimatedCounter(yearsSinceFounding, stringResource(R.string.stat_years), Modifier.weight(1f), animate = animate)
                catalogCount?.let { AnimatedCounter(it, stringResource(R.string.stat_books), Modifier.weight(1f), animate = animate) }
                eventsThisMonth?.let { AnimatedCounter(it, stringResource(R.string.stat_events), Modifier.weight(1f), animate = animate) }
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(navigate: (String) -> Unit) {
    // При много едър шрифт четирите в ред не се събират (думите се пренасят по букви) — 2 × 2.
    BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
        val perRow = if (maxWidth / 4 / LocalDensity.current.fontScale.coerceAtLeast(1f) < 72.dp) 2 else 4
        Column {
            quickActions.chunked(perRow).forEachIndexed { r, row ->
                Row(Modifier.fillMaxWidth()) {
                    row.forEachIndexed { c, action -> QuickActionCell(action, r * perRow + c, navigate) }
                }
            }
        }
    }
}

@Composable
private fun RowScope.QuickActionCell(action: QuickAction, i: Int, navigate: (String) -> Unit) {
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

@Composable
fun BookRow(books: List<CatalogBook>, modifier: Modifier = Modifier, onClick: (CatalogBook) -> Unit) {
    LazyRow(modifier = modifier, contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(books, key = { it.inv }) { b ->
            PressableCard(onClick = { onClick(b) }, modifier = Modifier.width(128.dp)) {
                Column(Modifier.padding(10.dp)) {
                    BookCover(b, width = 108.dp, modifier = Modifier.sharedElementKey("book-${b.inv}"))
                    Spacer(Modifier.height(8.dp))
                    Text(b.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(b.author, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
