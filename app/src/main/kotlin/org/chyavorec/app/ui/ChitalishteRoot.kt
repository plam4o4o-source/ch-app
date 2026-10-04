package org.chyavorec.app.ui

import android.net.Uri
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
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.observeReducedMotion
import org.chyavorec.app.ui.components.LocalSharedScope
import org.chyavorec.app.ui.components.LocalTabReselect
import org.chyavorec.app.ui.components.TabReselectBus
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.navigation.TopTab
import org.chyavorec.app.ui.screens.catalog.BookScreen
import org.chyavorec.app.ui.screens.catalog.CatalogScreen
import org.chyavorec.app.ui.screens.events.EventDetailScreen
import org.chyavorec.app.ui.screens.events.EventsScreen
import org.chyavorec.app.ui.screens.gallery.AlbumScreen
import org.chyavorec.app.ui.screens.gallery.GalleryScreen
import org.chyavorec.app.ui.screens.gallery.PhotoViewerScreen
import org.chyavorec.app.ui.screens.home.HomeScreen
import org.chyavorec.app.ui.screens.more.MoreScreen
import org.chyavorec.app.ui.screens.my.CardFullscreen
import org.chyavorec.app.ui.screens.my.CardScreen
import org.chyavorec.app.ui.screens.my.LoansScreen
import org.chyavorec.app.ui.screens.my.LoginScreen
import org.chyavorec.app.ui.screens.my.MembershipScreen
import org.chyavorec.app.ui.screens.my.MyHubScreen
import org.chyavorec.app.ui.screens.my.ProfileScreen
import org.chyavorec.app.ui.screens.news.ArticleScreen
import org.chyavorec.app.ui.screens.news.NewsListScreen
import org.chyavorec.app.ui.screens.onboarding.OnboardingScreen
import org.chyavorec.app.ui.screens.search.SearchScreen
import org.chyavorec.app.ui.screens.settings.AboutAppScreen
import org.chyavorec.app.ui.screens.settings.NotificationSettingsScreen
import org.chyavorec.app.ui.screens.settings.PrivacyScreen
import org.chyavorec.app.ui.screens.settings.SettingsScreen
import org.chyavorec.app.ui.screens.settings.TermsScreen
import org.chyavorec.app.ui.screens.site.AboutChitalishteScreen
import org.chyavorec.app.ui.screens.site.ActivitiesScreen
import org.chyavorec.app.ui.screens.site.ContactsScreen
import org.chyavorec.app.ui.screens.site.DocumentsScreen
import org.chyavorec.app.ui.screens.site.SitePageScreen
import org.chyavorec.app.util.Intents
import org.chyavorec.app.ui.components.UpdateDialog
import org.chyavorec.app.ui.screens.messages.MessagesScreen
import org.chyavorec.core.Urls
import kotlinx.coroutines.flow.first

/** Анимираното въведение се показва веднъж на процес (студен старт), не при всяко завъртане. */
private object IntroState { var shown = false }

@Composable
fun ChitalishteRoot(container: AppContainer, settings: AppSettings, deepLink: MutableStateFlow<String?>, showIntro: Boolean = true) {
    val scope = rememberCoroutineScope()
    // Въведението с логото — само при първото стартиране (после splash екранът е достатъчен).
    var intro by remember { mutableStateOf(showIntro && !IntroState.shown && !settings.introShown) }
    CompositionLocalProvider(LocalAppContainer provides container, LocalReducedMotion provides observeReducedMotion()) {
        Box(Modifier.fillMaxSize()) {
            if (!settings.onboardingDone) {
                OnboardingScreen(onFinish = { login ->
                    scope.launch {
                        container.settings.setOnboardingDone()
                        if (login) deepLink.value = "login"
                    }
                })
            } else {
                MainScaffold(container, deepLink)
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

@Composable
private fun MainScaffold(container: AppContainer, deepLink: MutableStateFlow<String?>) {
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
        if (link == null) return@LaunchedEffect
        // При студен старт (докосване на известие) графът на навигацията още не е
        // зададен в първия кадър — изчакваме първия екран, преди да навигираме.
        nav.currentBackStackEntryFlow.first()
        when {
            link == null -> Unit
            link == "login" -> nav.navigate(Routes.LOGIN)
            link == "my/loans" -> nav.navigate(Routes.LOANS)
            link == "events" -> nav.navigate(Routes.EVENTS)
            link == "update" -> container.updater.showPrompt()
            link == "messages" -> nav.navigate(Routes.MESSAGES)
            link!!.startsWith("news") -> nav.navigateTab(Routes.NEWS)
        }
        deepLink.value = null
    }

    LaunchedEffect(Unit) { container.updater.checkOnLaunch() }
    UpdateDialog(container.updater)

    fun navigate(target: String) = if (target in topLevelRoutes) nav.navigateTab(target) else nav.navigate(target)

    /** Връзка от съдържанието на сайта: вътрешна страница → native екран; файл/външен адрес → Custom Tab. */
    fun openLink(url: String) {
        val base = container.config.siteBaseUrl
        if (Urls.sameSite(url, base)) {
            val path = Uri.parse(url).path.orEmpty().trimEnd('/').ifEmpty { "/" }
            val isFile = path.substringAfterLast('/').contains('.')
            when {
                isFile -> Unit
                path == "/" -> return nav.navigateTab(Routes.HOME)
                path == "/elektronen-katalog" -> return nav.navigateTab(Routes.CATALOG)
                path == "/news" -> return nav.navigateTab(Routes.NEWS)
                path.startsWith("/news/") -> return nav.navigate(Routes.article(url))
                path == "/events" -> return nav.navigate(Routes.EVENTS)
                path == "/kontakti" -> return nav.navigate(Routes.CONTACTS)
                path == "/load" || path == "/istoricheski-publikacii" -> return nav.navigate(Routes.DOCUMENTS)
                path == "/policy" -> return nav.navigate(Routes.PRIVACY)
                path.count { it == '/' } == 1 -> return nav.navigate(Routes.page(url, ""))
            }
        }
        Intents.openUrl(context, url)
    }
    val openExternal: (String) -> Unit = { Intents.openUrl(context, it) }
    val back: () -> Unit = { if (!nav.popBackStack()) nav.navigateTab(Routes.HOME) }

    Scaffold(
        bottomBar = {
            Column {
                val topLevel = route in topLevelRoutes
                AnimatedVisibility(!online) {
                    Surface(color = MaterialTheme.colorScheme.inverseSurface, contentColor = MaterialTheme.colorScheme.inverseOnSurface) {
                        Text(
                            stringResource(R.string.offline_global),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.fillMaxWidth()
                                .then(if (topLevel) Modifier else Modifier.navigationBarsPadding())
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
                AnimatedVisibility(
                    visible = topLevel,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    // Избраният раздел — в златно/мастило от фирмената палитра (не розово в тъмна тема).
                    val navColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = if (LocalExtendedColors.current.isDark) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                        TopTab.entries.forEach { tab ->
                            val selected = route == tab.route
                            val iconSpec: AnimationSpec<Float> = if (reduced) snap()
                            else spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                            val iconScale by animateFloatAsState(if (selected) 1.15f else 1f, iconSpec, label = "tab-icon")
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (selected) {
                                        // Повторно докосване на текущия раздел — към началото на списъка.
                                        haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                        reselect.emit(tab.route)
                                    } else {
                                        nav.navigateTab(tab.route)
                                    }
                                },
                                icon = {
                                    Icon(
                                        if (selected) tab.selectedIcon else tab.icon, contentDescription = null,
                                        modifier = Modifier.graphicsLayer { scaleX = iconScale; scaleY = iconScale },
                                    )
                                },
                                label = { Text(stringResource(tab.labelRes), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                colors = navColors,
                            )
                        }
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

private fun NavHostController.navigateTab(route: String) = navigate(route) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
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
    SharedTransitionLayout {
    CompositionLocalProvider(LocalSharedScope provides this) {
    NavHost(
        navController = nav,
        startDestination = Routes.HOME,
        enterTransition = {
            if (reduced) EnterTransition.None
            else if (targetState.destination.route in topLevelRoutes) fadeIn(tween(dur)) + scaleIn(tween(dur), initialScale = 0.98f)
            else slideInHorizontally(tween(dur)) { it / 5 } + fadeIn(tween(dur))
        },
        exitTransition = { if (reduced) ExitTransition.None else fadeOut(tween(dur / 2)) },
        popEnterTransition = { if (reduced) EnterTransition.None else fadeIn(tween(dur)) },
        popExitTransition = {
            if (reduced) ExitTransition.None
            else if (initialState.destination.route in topLevelRoutes) fadeOut(tween(dur / 2)) + scaleOut(targetScale = 0.98f)
            else slideOutHorizontally(tween(dur)) { it / 5 } + fadeOut(tween(dur))
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
        screen(Routes.BOOK, arguments = listOf(navArgument("inv") { type = NavType.LongType })) {
            BookScreen(it.arguments?.getLong("inv") ?: 0L, back, navigate)
        }
        screen(Routes.SEARCH) { SearchScreen(back, navigate, openExternal, openLink) }
        screen(Routes.LOGIN) {
            LoginScreen(back, onLoggedIn = { nav.popBackStack(); nav.navigateTab(Routes.MY) }, onAddCard = { nav.popBackStack(); nav.navigate(Routes.CARD) })
        }
        screen(Routes.PROFILE) { ProfileScreen(back) }
        screen(Routes.CARD) { CardScreen(back, onFullscreen = { nav.navigate(Routes.CARD_FULL) }, onLogin = { nav.navigate(Routes.LOGIN) }) }
        screen(Routes.CARD_FULL) { CardFullscreen(onClose = back) }
        screen(Routes.LOANS) { LoansScreen(back, onLogin = { nav.navigate(Routes.LOGIN) }) }
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
        screen(Routes.SETTINGS) { SettingsScreen(back, navigate) }
        screen(Routes.PRIVACY) { PrivacyScreen(back, navigate) }
        screen(Routes.TERMS) { TermsScreen(back, navigate) }
        screen(Routes.ABOUT) { AboutAppScreen(back) }
    }
    }
    }
}

/** Пренася AnimatedContentScope на дестинацията към shared element преходите. */
private fun NavGraphBuilder.screen(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(route, arguments) { entry ->
    CompositionLocalProvider(LocalNavAnimatedScope provides this) { content(entry) }
}
