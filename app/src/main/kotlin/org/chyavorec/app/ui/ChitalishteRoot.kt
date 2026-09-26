package org.chyavorec.app.ui

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
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
import org.chyavorec.app.ui.screens.site.SitePageScreen
import org.chyavorec.app.util.Intents
import org.chyavorec.core.Urls

@Composable
fun ChitalishteRoot(container: AppContainer, settings: AppSettings, deepLink: MutableStateFlow<String?>) {
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalAppContainer provides container) {
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

    LaunchedEffect(link) {
        when {
            link == null -> Unit
            link == "login" -> nav.navigate(Routes.LOGIN)
            link == "my/loans" -> nav.navigate(Routes.LOANS)
            link == "events" -> nav.navigate(Routes.EVENTS)
            link!!.startsWith("news") -> nav.navigateTab(Routes.NEWS)
        }
        deepLink.value = null
    }

    fun navigate(target: String) = if (target in topLevelRoutes) nav.navigateTab(target) else nav.navigate(target)

    /** Връзка от съдържанието на сайта: вътрешна страница → native екран; иначе Custom Tab. */
    fun openLink(url: String) {
        val base = container.config.siteBaseUrl
        if (url.startsWith("https://") && Urls.sameSite(url, base)) {
            val path = Uri.parse(url).path.orEmpty()
            when {
                path.startsWith("/index/elektronen_katalog") -> return nav.navigateTab(Routes.CATALOG)
                path.trimEnd('/') == "/news" -> return nav.navigateTab(Routes.NEWS)
                path.trimEnd('/') == "/photo" -> return nav.navigate(Routes.GALLERY)
                path.startsWith("/index/") && !path.substringAfterLast('/').contains('.') -> return nav.navigate(Routes.page(url, ""))
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
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                        TopTab.entries.forEach { tab ->
                            val selected = route == tab.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { nav.navigateTab(tab.route) },
                                icon = { Icon(if (selected) tab.selectedIcon else tab.icon, contentDescription = null) },
                                label = { Text(stringResource(tab.labelRes), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primaryContainer),
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0),
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            AppNavHost(nav, ::navigate, back, ::openLink, openExternal)
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
    NavHost(
        navController = nav,
        startDestination = Routes.HOME,
        enterTransition = {
            if (targetState.destination.route in topLevelRoutes) fadeIn(tween(dur)) + scaleIn(tween(dur), initialScale = 0.98f)
            else slideInHorizontally(tween(dur)) { it / 5 } + fadeIn(tween(dur))
        },
        exitTransition = { fadeOut(tween(dur / 2)) },
        popEnterTransition = { fadeIn(tween(dur)) },
        popExitTransition = {
            if (initialState.destination.route in topLevelRoutes) fadeOut(tween(dur / 2)) + scaleOut(targetScale = 0.98f)
            else slideOutHorizontally(tween(dur)) { it / 5 } + fadeOut(tween(dur))
        },
    ) {
        composable(Routes.HOME) { HomeScreen(navigate) }
        composable(Routes.CATALOG) { CatalogScreen(navigate) }
        composable(Routes.MY) { MyHubScreen(navigate) }
        composable(Routes.NEWS) { NewsListScreen(navigate) }
        composable(Routes.MORE) { MoreScreen(navigate) }

        composable(Routes.ARTICLE, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            ArticleScreen(Uri.decode(it.arguments?.getString("id").orEmpty()), back, navigate, openLink)
        }
        composable(Routes.EVENTS) { EventsScreen(back, navigate) }
        composable(Routes.EVENT, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            EventDetailScreen(Uri.decode(it.arguments?.getString("id").orEmpty()), back, openLink)
        }
        composable(Routes.BOOK, arguments = listOf(navArgument("inv") { type = NavType.LongType })) {
            BookScreen(it.arguments?.getLong("inv") ?: 0L, back, navigate)
        }
        composable(Routes.SEARCH) { SearchScreen(back, navigate, openExternal) }
        composable(Routes.LOGIN) {
            LoginScreen(back, onLoggedIn = { nav.popBackStack(); nav.navigateTab(Routes.MY) }, onAddCard = { nav.popBackStack(); nav.navigate(Routes.CARD) })
        }
        composable(Routes.PROFILE) { ProfileScreen(back) }
        composable(Routes.CARD) { CardScreen(back, onFullscreen = { nav.navigate(Routes.CARD_FULL) }, onLogin = { nav.navigate(Routes.LOGIN) }) }
        composable(Routes.CARD_FULL) { CardFullscreen(onClose = back) }
        composable(Routes.LOANS) { LoansScreen(back, onLogin = { nav.navigate(Routes.LOGIN) }) }
        composable(Routes.MEMBERSHIP) { MembershipScreen(back, onLogin = { nav.navigate(Routes.LOGIN) }) }
        composable(Routes.NOTIFICATIONS) { NotificationSettingsScreen(back) }
        composable(Routes.ACTIVITIES) { ActivitiesScreen(back, navigate, openExternal) }
        composable(Routes.ABOUT_CHITALISHTE) { AboutChitalishteScreen(back, navigate, openLink) }
        composable(
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
        composable(Routes.GALLERY) { GalleryScreen(back, navigate) }
        composable(Routes.ALBUM, arguments = listOf(navArgument("name") { type = NavType.StringType })) {
            AlbumScreen(Uri.decode(it.arguments?.getString("name").orEmpty()), back, navigate)
        }
        composable(
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
        composable(Routes.CONTACTS) { ContactsScreen(back) }
        composable(Routes.SETTINGS) { SettingsScreen(back, navigate) }
        composable(Routes.PRIVACY) { PrivacyScreen(back, navigate) }
        composable(Routes.TERMS) { TermsScreen(back, navigate) }
        composable(Routes.ABOUT) { AboutAppScreen(back) }
    }
}
