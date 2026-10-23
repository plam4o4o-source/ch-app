package org.chyavorec.app.ui

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.saveable.rememberSaveable
import org.chyavorec.app.notifications.Notifier
import androidx.activity.BackEventCompat
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import org.chyavorec.app.ui.components.LocalSeasonalDecor
import org.chyavorec.app.ui.components.LocalSharedTransitionsEnabled
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.ui.graphics.Color
import org.chyavorec.app.ui.components.Emblem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import org.chyavorec.app.ui.components.BrandIntro
import org.chyavorec.app.ui.components.LocalNavAnimatedScope
import org.chyavorec.app.ui.components.LocalOnlineCovers
import org.chyavorec.app.ui.components.LocalCoverNetwork
import org.chyavorec.app.ui.components.observeCoverNetwork
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.observeReducedMotion
import org.chyavorec.app.ui.components.LocalSharedScope
import org.chyavorec.app.ui.components.LimitFontScale
import org.chyavorec.app.ui.components.LocalTabReselect
import org.chyavorec.app.ui.components.TabReselectBus
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.messages.MessagesOnLaunch
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.LinkTarget
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.navigation.TopTab
import org.chyavorec.app.ui.navigation.navigateTab
import org.chyavorec.app.ui.navigation.openLinkTarget
import org.chyavorec.app.ui.screens.catalog.BookScreen
import org.chyavorec.app.ui.screens.catalog.CatalogScreen
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.ui.screens.events.EventDetailScreen
import org.chyavorec.app.ui.screens.events.EventsScreen
import org.chyavorec.app.ui.screens.gallery.AlbumScreen
import org.chyavorec.app.ui.screens.gallery.GalleryScreen
import org.chyavorec.app.ui.screens.gallery.PhotoViewerScreen
import org.chyavorec.app.ui.screens.home.HomeScreen
import org.chyavorec.app.ui.screens.more.MoreScreen
import org.chyavorec.app.ui.screens.my.CardFullscreen
import org.chyavorec.app.ui.screens.my.CardScreen
import org.chyavorec.app.ui.screens.my.HistoryScreen
import org.chyavorec.app.ui.screens.my.LoansScreen
import org.chyavorec.app.ui.screens.my.LoginScreen
import org.chyavorec.app.ui.screens.my.MembershipScreen
import org.chyavorec.app.ui.screens.my.MyHubScreen
import org.chyavorec.app.ui.screens.my.ProfileScreen
import org.chyavorec.app.ui.screens.news.ArticleScreen
import org.chyavorec.app.ui.screens.news.NewsListScreen
import org.chyavorec.app.ui.screens.onboarding.OnboardingScreen
import org.chyavorec.app.ui.screens.search.SearchScreen
import org.chyavorec.app.ui.screens.scan.ScanScreen
import org.chyavorec.app.ui.screens.settings.AboutAppScreen
import org.chyavorec.app.ui.screens.settings.DiagnosticsScreen
import org.chyavorec.app.ui.screens.settings.NotificationSettingsScreen
import org.chyavorec.app.ui.screens.settings.PrivacyScreen
import org.chyavorec.app.ui.screens.settings.SettingsScreen
import org.chyavorec.app.ui.screens.settings.TermsScreen
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.logFailure
import org.chyavorec.app.ui.screens.site.AboutChitalishteScreen
import org.chyavorec.app.ui.screens.site.ActivitiesScreen
import org.chyavorec.app.ui.screens.site.ContactsScreen
import org.chyavorec.app.ui.screens.site.DocumentsScreen
import org.chyavorec.app.ui.screens.site.SitePageScreen
import org.chyavorec.app.util.Intents
import org.chyavorec.app.ui.components.UpdateDialog
import org.chyavorec.app.ui.components.OfflineBar
import org.chyavorec.app.ui.screens.messages.MessagesScreen
import kotlinx.coroutines.flow.first

/** Анимираното въведение се показва веднъж на процес (студен старт), не при всяко завъртане. */
private object IntroState { var shown = false }

@Composable
fun ChitalishteRoot(container: AppContainer, settings: AppSettings, deepLink: MutableStateFlow<String?>, showIntro: Boolean = true) {
    val scope = rememberCoroutineScope()
    // Въведението с логото — само при първото стартиране (после splash екранът е достатъчен).
    var intro by remember { mutableStateOf(showIntro && !IntroState.shown && !settings.introShown) }
    CompositionLocalProvider(
        LocalAppContainer provides container,
        LocalReducedMotion provides observeReducedMotion(),
        LocalOnlineCovers provides settings.coversOnline,
        LocalCoverNetwork provides observeCoverNetwork(container.connectivity, settings.coversWifiOnly),
        // Украсите — от вече прочетените настройки: вярни още в първия кадър на началния екран.
        LocalSeasonalDecor provides settings.seasonal,
    ) {
        Box(Modifier.fillMaxSize()) {
            if (!settings.onboardingDone) {
                OnboardingScreen(onFinish = { login ->
                    scope.launch {
                        container.settings.setOnboardingDone()
                        if (login) deepLink.value = "login"
                    }
                })
            } else {
                // Въпросът за известията — едва след въведението с логото.
                MainScaffold(container, deepLink, canAskNotifications = !intro)
            }
            if (intro) BrandIntro(onFinished = {
                IntroState.shown = true
                intro = false
                scope.launch { container.settings.setIntroShown() }
            })
        }
    }
}

private val topLevelRoutes = TopTab.entries.map { it.route }.toSet()

/** Лентата е с фиксирана височина: над 1.3× етикетите на разделите биха се рязали („Нача…“). */
private const val NAV_LABEL_MAX_FONT_SCALE = 1.3f

@Composable
private fun MainScaffold(container: AppContainer, deepLink: MutableStateFlow<String?>, canAskNotifications: Boolean = true) {
    // Известията са включени по подразбиране, но на Android 13+ без разрешение не излизат —
    // питаме веднъж след въведението (и веднъж при обновяване за старите инсталации): първо
    // кратко обяснение в приложението, а системният прозорец — само при „Да“.
    val notificationPermission = org.chyavorec.app.ui.screens.settings.rememberNotificationPermission()
    val permissionContext = LocalContext.current
    val askScope = rememberCoroutineScope()
    var askNotifications by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(canAskNotifications) {
        if (!canAskNotifications || container.config.environment == "test" || container.settings.notificationPermissionAsked()) return@LaunchedEffect
        val needed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Notifier.canNotify(permissionContext)
        // Под Android 13 (или вече разрешени) — няма какво да се пита.
        if (needed) askNotifications = true else container.settings.setNotificationPermissionAsked()
    }
    if (askNotifications) {
        // Отговорът се помни (и „Не сега“) — по-късно известията се включват от Настройки › Известия.
        fun answered() {
            askNotifications = false
            askScope.launch { container.settings.setNotificationPermissionAsked() }
        }
        org.chyavorec.app.ui.screens.settings.NotificationRationaleDialog(
            onYes = { answered(); notificationPermission.requestWithResult { } },
            onLater = { answered() },
        )
    }
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val context = LocalContext.current
    val online by container.connectivity.online.collectAsStateWithLifecycle(initialValue = true)
    val link by deepLink.collectAsState()
    val reselect = remember { TabReselectBus() }
    val haptic = LocalHapticFeedback.current
    val reduced = rememberReducedMotion()

    LaunchedEffect(link) {
        val target = link ?: return@LaunchedEffect
        // При студен старт (докосване на известие) графът на навигацията още не е
        // зададен в първия кадър — изчакваме първия екран, преди да навигираме.
        nav.currentBackStackEntryFlow.first()
        // Известие, пряк път или адрес от сайта (App Links): първо разделът отдолу, после
        // екранът над него — „Назад“ води към раздела, а не извън приложението.
        when (val t = DeepLinks.target(target, container.config.siteBaseUrl)) {
            // Без стария подстек на раздела (иначе екранът би се повторил) — виж openLinkTarget.
            // Неуспешна навигация не срива приложението — причината остава в локалния дневник.
            is LinkTarget.Screen -> runCatching { nav.openLinkTarget(t) }.logFailure("deeplink")
            LinkTarget.UpdatePrompt -> container.updater.showPrompt()
            // Адрес от сайта без native екран — в браузъра; непознатите връзки се пренебрегват (без самата връзка в дневника).
            null -> if (target.startsWith("https://")) Intents.openUrl(context, target) else Diagnostics.note("deeplink", "unknown link")
        }
        deepLink.value = null
    }

    LaunchedEffect(Unit) { container.updater.checkOnLaunch() }
    // Съобщенията (и известие за нови лични) — веднъж на процес, не при всяко показване на „Начало“.
    LaunchedEffect(Unit) { MessagesOnLaunch.run(context, container) }
    UpdateDialog(container.updater)

    fun navigate(target: String) = when {
        // Търсене, поискано от друг екран: каталогът без стария си подстек (иначе би се
        // възстановил напр. екранът на книга над него, а не резултатите от търсенето).
        target == Routes.CATALOG && CatalogSearchRequests.pending.value != null -> nav.openLinkTarget(LinkTarget.Screen(Routes.CATALOG))
        target in topLevelRoutes -> nav.navigateTab(target)
        else -> nav.navigate(target)
    }

    /**
     * Връзка от съдържанието на сайта: вътрешна страница → native екран (над текущия,
     * а раздел — като смяна на раздела); файл/външен адрес → Custom Tab.
     */
    fun openLink(url: String) {
        val t = DeepLinks.forUrl(url, container.config.siteBaseUrl) ?: return Intents.openUrl(context, url)
        if (t.stack.isEmpty()) nav.navigateTab(t.tab) else t.stack.forEach { nav.navigate(it) }
    }
    val openExternal: (String) -> Unit = { Intents.openUrl(context, it) }
    val back: () -> Unit = { if (!nav.popBackStack()) nav.navigateTab(Routes.HOME) }

    val topLevel = route in topLevelRoutes
    val onTab: (TopTab, Boolean) -> Unit = { tab, selected ->
        if (selected) {
            // Повторно докосване на текущия раздел — към началото на списъка.
            haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
            reselect.emit(tab.route)
        } else {
            nav.navigateTab(tab.route)
        }
    }

    // Телефон (изправен): долна лента. От 600 dp ширина (таблет, хоризонтално) —
    // странична навигационна лента (rail), а съдържанието заема останалата ширина.
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp
        Row(Modifier.fillMaxSize()) {
            if (wide) {
                AnimatedVisibility(
                    visible = topLevel,
                    enter = if (reduced) EnterTransition.None else slideInHorizontally { -it } + fadeIn(),
                    exit = if (reduced) ExitTransition.None else slideOutHorizontally { -it } + fadeOut(),
                ) {
                    AppNavigationRail(route, reduced, onTab)
                }
            }
            Scaffold(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                bottomBar = {
                    Column {
                        // Без долна лента под нея лентата „няма връзка“ сама отстъпва от системната навигация.
                        val barBelow = topLevel && !wide
                        AnimatedVisibility(!online) { OfflineBar(insetNavigation = !barBelow) }
                        if (!wide) {
                            AnimatedVisibility(
                                visible = topLevel,
                                enter = if (reduced) EnterTransition.None else slideInVertically { it } + fadeIn(),
                                exit = if (reduced) ExitTransition.None else slideOutVertically { it } + fadeOut(),
                            ) {
                                AppNavigationBar(route, reduced, onTab)
                            }
                        }
                    }
                },
                contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0),
            ) { padding ->
                Box(Modifier.padding(padding).fillMaxSize()) {
                    CompositionLocalProvider(LocalTabReselect provides reselect) {
                        AppNavHost(nav, ::navigate, back, ::openLink, openExternal)
                    }
                }
            }
        }
    }
}

/** Иконата на раздела — „подскача“ при избор (без анимация при намалено движение). */
@Composable
private fun TabIcon(tab: TopTab, selected: Boolean, reduced: Boolean) {
    val iconSpec: AnimationSpec<Float> = if (reduced) snap()
    else spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
    val iconScale by animateFloatAsState(if (selected) 1.15f else 1f, iconSpec, label = "tab-icon")
    Icon(
        if (selected) tab.selectedIcon else tab.icon, contentDescription = null,
        modifier = Modifier.graphicsLayer { scaleX = iconScale; scaleY = iconScale },
    )
}

/** Цветът на текста на избрания раздел — златно в тъмна тема, бордо в светла (не розово). */
@Composable
private fun selectedTabTextColor(): Color =
    if (LocalExtendedColors.current.isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

@Composable
private fun AppNavigationBar(route: String?, reduced: Boolean, onTab: (TopTab, Boolean) -> Unit) {
    // Избраният раздел — в златно/мастило от фирмената палитра (не розово в тъмна тема).
    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedTextColor = selectedTabTextColor(),
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        TopTab.entries.forEach { tab ->
            val selected = route == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { onTab(tab, selected) },
                icon = { TabIcon(tab, selected, reduced) },
                label = { LimitFontScale(NAV_LABEL_MAX_FONT_SCALE) { Text(stringResource(tab.labelRes), maxLines = 1, overflow = TextOverflow.Ellipsis) } },
                colors = navColors,
            )
        }
    }
}

@Composable
private fun AppNavigationRail(route: String?, reduced: Boolean, onTab: (TopTab, Boolean) -> Unit) {
    val railColors = NavigationRailItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedTextColor = selectedTabTextColor(),
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    )
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        header = { Emblem(40.dp, Modifier.padding(vertical = 8.dp)) },
    ) {
        Spacer(Modifier.weight(1f))
        TopTab.entries.forEach { tab ->
            val selected = route == tab.route
            NavigationRailItem(
                selected = selected,
                onClick = { onTab(tab, selected) },
                icon = { TabIcon(tab, selected, reduced) },
                label = { LimitFontScale(NAV_LABEL_MAX_FONT_SCALE) { Text(stringResource(tab.labelRes), maxLines = 1, overflow = TextOverflow.Ellipsis) } },
                colors = railColors,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

/** Индексът на раздела от долната лента (−1 — не е раздел). */
private fun tabIndex(route: String?): Int = TopTab.entries.indexOfFirst { it.route == route }

/**
 * Посоката при смяна на раздел (+1 — към раздел вдясно, −1 — вляво), или null,
 * ако преходът не е между два раздела (тогава важат push/pop преходите).
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabDirection(): Int? {
    val from = tabIndex(initialState.destination.route)
    val to = tabIndex(targetState.destination.route)
    if (from < 0 || to < 0 || from == to) return null
    return if (to > from) 1 else -1
}

@Composable
private fun AppNavHost(
    nav: NavHostController,
    navigate: (String) -> Unit,
    back: () -> Unit,
    openLink: (String) -> Unit,
    openExternal: (String) -> Unit,
) {
    val dur = 280
    // При изключени анимации (Достъпност) екраните се сменят без преход.
    val reduced = rememberReducedMotion()
    // Детайлните екрани с shared element (корица/снимка) влизат с пружина — в
    // синхрон с „прелитащия“ елемент (виж Modifier.sharedElementKey).
    val detailRoutes = setOf(Routes.BOOK, Routes.ARTICLE, Routes.EVENT)
    val detailSpring = spring<Float>(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)

    // Material „споделена ос X“ между разделите: 30 dp плъзгане по посока на индекса
    // на раздела; излизащият изчезва бързо, влизащият се появява след него.
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val axisPx = with(LocalDensity.current) { 30.dp.roundToPx() } * (if (rtl) -1 else 1)
    val axisSlide = tween<IntOffset>(300, easing = FastOutSlowInEasing)
    fun axisEnter(dir: Int): EnterTransition =
        slideInHorizontally(axisSlide) { dir * axisPx } + fadeIn(tween(210, delayMillis = 90, easing = LinearOutSlowInEasing))
    fun axisExit(dir: Int): ExitTransition =
        slideOutHorizontally(axisSlide) { -dir * axisPx } + fadeOut(tween(90, easing = FastOutLinearInEasing))

    // Смяна на раздел → без shared element (виж LocalSharedTransitionsEnabled).
    var tabSwitch by remember { mutableStateOf(false) }
    DisposableEffect(nav) {
        var last: String? = null
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            tabSwitch = last in topLevelRoutes && destination.route in topLevelRoutes
            last = destination.route
        }
        nav.addOnDestinationChangedListener(listener)
        onDispose { nav.removeOnDestinationChangedListener(listener) }
    }

    SharedTransitionLayout {
    CompositionLocalProvider(LocalSharedScope provides this, LocalSharedTransitionsEnabled provides !tabSwitch) {
    NavHost(
        navController = nav,
        startDestination = Routes.HOME,
        enterTransition = {
            val dir = tabDirection()
            if (reduced) EnterTransition.None
            else if (dir != null) axisEnter(dir)
            else if (targetState.destination.route in topLevelRoutes) fadeIn(tween(dur)) + scaleIn(tween(dur), initialScale = 0.98f)
            else if (targetState.destination.route in detailRoutes) fadeIn(detailSpring) + scaleIn(detailSpring, initialScale = 0.96f)
            else slideInHorizontally(tween(dur)) { it / 5 } + fadeIn(tween(dur))
        },
        exitTransition = {
            val dir = tabDirection()
            if (reduced) ExitTransition.None
            else if (dir != null) axisExit(dir)
            else fadeOut(tween(dur / 2))
        },
        // Връщане с бутон (без жест): предишният екран „израства“ от 0.9 с леко
        // плъзгане, а текущият се смалява и излиза надясно. Между раздели — оста X.
        popEnterTransition = {
            val dir = tabDirection()
            if (reduced) EnterTransition.None
            else if (dir != null) axisEnter(dir)
            else if (initialState.destination.route in topLevelRoutes) fadeIn(tween(dur))
            else fadeIn(tween(dur)) + scaleIn(tween(dur), initialScale = 0.9f) + slideInHorizontally(tween(dur)) { -it / 10 }
        },
        popExitTransition = {
            val dir = tabDirection()
            if (reduced) ExitTransition.None
            else if (dir != null) axisExit(dir)
            else if (initialState.destination.route in topLevelRoutes) fadeOut(tween(dur / 2)) + scaleOut(targetScale = 0.98f)
            else slideOutHorizontally(tween(dur)) { it / 10 } + scaleOut(tween(dur), targetScale = 0.9f) + fadeOut(tween(dur))
        },
        // Предсказуемо „назад“ (жест, Android 14+): NavHost следва прогреса на жеста.
        // Както в системата (Android 15): текущият екран се смалява до 90 % със
        // заоблени ъгли (виж screen()) и леко се измества към посоката на жеста;
        // предишният се плъзга отдолу. Избледняването е в края — след потвърждаване.
        predictivePopEnterTransition = { edge ->
            val dir = if (edge == BackEventCompat.EDGE_RIGHT) -1 else 1
            if (reduced) EnterTransition.None
            else slideInHorizontally(tween(dur, easing = LinearEasing)) { -dir * it / 8 } + fadeIn(tween(dur / 2, easing = LinearEasing), initialAlpha = 0.6f)
        },
        predictivePopExitTransition = { edge ->
            val dir = if (edge == BackEventCompat.EDGE_RIGHT) -1 else 1
            if (reduced) ExitTransition.None
            else scaleOut(tween(dur, easing = LinearEasing), targetScale = 0.9f) +
                slideOutHorizontally(tween(dur, easing = LinearEasing)) { dir * it / 20 } +
                fadeOut(tween(dur / 3, delayMillis = dur * 2 / 3, easing = LinearEasing))
        },
    ) {
        screen(Routes.HOME) { HomeScreen(navigate) }
        screen(Routes.CATALOG) { CatalogScreen(navigate) }
        screen(Routes.MY) { MyHubScreen(navigate) }
        screen(Routes.NEWS) { NewsListScreen(navigate) }
        screen(Routes.MORE) { MoreScreen(navigate) }

        screen(Routes.ARTICLE, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            ArticleScreen(Uri.decode(it.arguments?.getString("id").orEmpty()), back, navigate, openLink)
        }
        screen(Routes.EVENTS) { EventsScreen(back, navigate) }
        screen(Routes.EVENT, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            EventDetailScreen(Uri.decode(it.arguments?.getString("id").orEmpty()), back, openLink)
        }
        screen(
            Routes.BOOK,
            arguments = listOf(
                navArgument("inv") { type = NavType.LongType },
                navArgument("personal") { type = NavType.BoolType; defaultValue = false },
            ),
        ) {
            BookScreen(it.arguments?.getLong("inv") ?: 0L, back, navigate, personal = it.arguments?.getBoolean("personal") == true)
        }
        screen(Routes.SEARCH) { SearchScreen(back, navigate, openExternal, openLink) }
        screen(Routes.SCAN) { ScanScreen(back, navigate) }
        screen(Routes.LOGIN) {
            LoginScreen(back, onLoggedIn = { nav.popBackStack(); nav.navigateTab(Routes.MY) }, onAddCard = { nav.popBackStack(); nav.navigate(Routes.CARD) })
        }
        screen(Routes.PROFILE) { ProfileScreen(back) }
        screen(Routes.CARD) { CardScreen(back, onFullscreen = { nav.navigate(Routes.CARD_FULL) }, onLogin = { nav.navigate(Routes.LOGIN) }) }
        screen(Routes.CARD_FULL) { CardFullscreen(onClose = back) }
        screen(Routes.LOANS) { LoansScreen(back, onLogin = { nav.navigate(Routes.LOGIN) }, onOpenBook = { navigate(Routes.book(it, personal = true)) }) }
        screen(Routes.HISTORY) { HistoryScreen(back, navigate, onLogin = { nav.navigate(Routes.LOGIN) }) }
        screen(Routes.MEMBERSHIP) { MembershipScreen(back, onLogin = { nav.navigate(Routes.LOGIN) }) }
        screen(Routes.NOTIFICATIONS) { NotificationSettingsScreen(back) }
        screen(Routes.ACTIVITIES) { ActivitiesScreen(back, navigate, openExternal) }
        screen(Routes.ABOUT_CHITALISHTE) { AboutChitalishteScreen(back, navigate, openLink) }
        screen(
            Routes.PAGE,
            arguments = listOf(
                navArgument("url") { type = NavType.StringType; defaultValue = "" },
                navArgument("title") { type = NavType.StringType; defaultValue = "" },
            ),
        ) {
            val url = Uri.decode(it.arguments?.getString("url").orEmpty())
            val title = Uri.decode(it.arguments?.getString("title").orEmpty())
            SitePageScreen(url, title, back, navigate, openLink)
        }
        screen(Routes.GALLERY) { GalleryScreen(back, navigate) }
        screen(Routes.ALBUM, arguments = listOf(navArgument("name") { type = NavType.StringType })) {
            AlbumScreen(Uri.decode(it.arguments?.getString("name").orEmpty()), back, navigate)
        }
        screen(
            Routes.VIEWER,
            arguments = listOf(
                navArgument("album") { type = NavType.StringType; defaultValue = "" },
                navArgument("index") { type = NavType.IntType; defaultValue = 0 },
                navArgument("url") { type = NavType.StringType; defaultValue = "" },
            ),
        ) {
            val urls = Uri.decode(it.arguments?.getString("url").orEmpty()).split('\n').filter { u -> u.isNotBlank() }
            PhotoViewerScreen(Uri.decode(it.arguments?.getString("album").orEmpty()), it.arguments?.getInt("index") ?: 0, urls, back)
        }
        screen(Routes.CONTACTS) { ContactsScreen(back) }
        screen(Routes.DOCUMENTS) { DocumentsScreen(back, openLink) }
        screen(Routes.MESSAGES) { MessagesScreen(back, openLink) }
        screen(Routes.MESSAGE, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            MessagesScreen(back, openLink, focusId = Uri.decode(it.arguments?.getString("id").orEmpty()).ifBlank { null })
        }
        screen(Routes.SETTINGS) { SettingsScreen(back, navigate) }
        screen(Routes.PRIVACY) { PrivacyScreen(back, navigate) }
        screen(Routes.TERMS) { TermsScreen(back, navigate) }
        screen(Routes.ABOUT) { AboutAppScreen(back) }
        screen(Routes.DIAGNOSTICS) { DiagnosticsScreen(back) }
    }
    }
    }
}

/**
 * Пренася AnimatedContentScope на дестинацията към shared element преходите и
 * заобля ъглите на екрана, докато той влиза/излиза (най-видимо при предсказуемо
 * „назад“ — смаленият екран е „карта“ като в системната анимация). Радиусът е
 * част от прехода на дестинацията — следва жеста и се чете само в слоя.
 */
private fun NavGraphBuilder.screen(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(route, arguments) { entry ->
    val reduced = rememberReducedMotion()
    val corner = transition.animateDp(
        transitionSpec = { if (reduced) snap() else tween(280) },
        label = "screen-corner",
    ) { state -> if (state == EnterExitState.Visible || reduced) 0.dp else 28.dp }
    Box(
        Modifier.fillMaxSize().graphicsLayer {
            val r = corner.value.toPx()
            if (r > 0.5f) {
                shape = RoundedCornerShape(r)
                clip = true
            } else {
                clip = false
            }
        },
    ) {
        CompositionLocalProvider(LocalNavAnimatedScope provides this@composable) { content(entry) }
    }
}
