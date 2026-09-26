package org.chyavorec.app.ui.screens.site

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Diversity3
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.ContentBlocksView
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.util.Intents
import org.chyavorec.core.AppError
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection

fun sectionIcon(s: SiteSection): ImageVector = when (s) {
    SiteSection.ABOUT -> Icons.Outlined.AccountBalance
    SiteSection.HISTORY -> Icons.Outlined.History
    SiteSection.LIBRARY, SiteSection.CATALOG -> Icons.AutoMirrored.Outlined.MenuBook
    SiteSection.EVENTS -> Icons.Outlined.Event
    SiteSection.FOLKLORE -> Icons.Outlined.LibraryMusic
    SiteSection.DANCE -> Icons.Outlined.Diversity3
    SiteSection.CLUBS -> Icons.Outlined.Groups
    SiteSection.PROJECTS -> Icons.Outlined.Rocket
    SiteSection.DIGITAL_CLUB -> Icons.Outlined.Computer
    SiteSection.CONTACTS -> Icons.Outlined.Place
    SiteSection.GALLERY -> Icons.Outlined.Photo
    SiteSection.NEWS -> Icons.AutoMirrored.Outlined.Article
    SiteSection.DOCUMENTS, SiteSection.PUBLICATIONS -> Icons.Outlined.Folder
    SiteSection.DONATIONS -> Icons.Outlined.Favorite
    SiteSection.VILLAGE -> Icons.Outlined.Landscape
    SiteSection.PRIVACY, SiteSection.TERMS -> Icons.Outlined.Policy
    SiteSection.OTHER -> Icons.Outlined.TheaterComedy
}

/** Страниците, които имат собствени екрани в приложението, не се дублират като „дейности“. */
private val nativeSections = setOf(SiteSection.NEWS, SiteSection.EVENTS, SiteSection.GALLERY, SiteSection.CONTACTS, SiteSection.CATALOG, SiteSection.PRIVACY, SiteSection.TERMS)

/** Отваря връзка от сайта: вътрешните страници — native, останалото — в Custom Tab. */
fun openSiteLink(link: SiteLink, navigate: (String) -> Unit, openExternal: (String) -> Unit) {
    when (link.kind) {
        SiteSection.NEWS -> navigate(Routes.NEWS)
        SiteSection.EVENTS -> navigate(Routes.EVENTS)
        SiteSection.GALLERY -> navigate(Routes.GALLERY)
        SiteSection.CONTACTS -> navigate(Routes.CONTACTS)
        SiteSection.CATALOG -> navigate(Routes.CATALOG)
        SiteSection.DOCUMENTS -> openExternal(link.url)
        else -> navigate(Routes.page(link.url, link.title))
    }
}

@Composable
fun ActivitiesScreen(onBack: () -> Unit, navigate: (String) -> Unit, openExternal: (String) -> Unit) {
    val vm = appViewModel { LinksViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(topBar = { BackTopBar(stringResource(R.string.activities_title), onBack) }) { padding ->
        PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state.map { list -> list.filter { it.kind !in nativeSections } },
                onRetry = { vm.refresh() },
                isEmpty = { it.isEmpty() },
                skeleton = { SkeletonList(withImage = false) },
                empty = { EmptyView(stringResource(R.string.activities_empty)) },
            ) { links ->
                LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                    item {
                        Text(stringResource(R.string.activities_intro), style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                    }
                    items(links, key = { it.url }) { l ->
                        ListItem(
                            headlineContent = { Text(l.title, style = MaterialTheme.typography.titleMedium) },
                            leadingContent = { Icon(sectionIcon(l.kind), null, tint = MaterialTheme.colorScheme.primary) },
                            trailingContent = { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null) },
                            modifier = Modifier.clickable { openSiteLink(l, navigate, openExternal) }.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SitePageScreen(url: String, title: String, onBack: () -> Unit, navigate: (String) -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel(key = "page-$url") { PageViewModel(url, it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(topBar = {
        BackTopBar(title, onBack, actions = {
            IconButton(onClick = { Intents.openUrl(context, url) }) {
                Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = stringResource(R.string.action_open_site))
            }
        })
    }) { padding ->
        PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state, onRetry = { vm.refresh() }, isEmpty = { it.blocks.isEmpty() },
                skeleton = { SkeletonCards() },
                empty = { EmptyView(stringResource(R.string.page_empty)) },
                errorSubject = title,
            ) { page ->
                LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                    item { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                    item {
                        ContentBlocksView(
                            page.blocks,
                            onLink = openLink,
                            onImage = { img -> navigate(Routes.viewerUrls(page.images, page.images.indexOf(img))) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).widthIn(max = 760.dp),
                        )
                    }
                    item {
                        Text(stringResource(R.string.page_source, url), style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AboutChitalishteScreen(onBack: () -> Unit, navigate: (String) -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel { AboutChitalishteViewModel(it.siteRepository) }
    val link by vm.link.collectAsStateWithLifecycle()
    val failed by vm.failed.collectAsStateWithLifecycle()
    val l = link
    when {
        l != null -> SitePageScreen(l.url, l.title, onBack, navigate, openLink)
        failed -> Scaffold(topBar = { BackTopBar(stringResource(R.string.qa_about), onBack) }) { p ->
            Column(Modifier.padding(p)) {
                org.chyavorec.app.ui.components.ErrorView(AppError.NotFound, onRetry = null)
            }
        }
        else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}
