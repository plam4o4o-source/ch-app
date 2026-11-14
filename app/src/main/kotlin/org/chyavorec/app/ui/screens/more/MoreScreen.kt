package org.chyavorec.app.ui.screens.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.AppTopBar
import org.chyavorec.app.ui.components.PinSectionsSheet
import org.chyavorec.app.ui.navigation.PinnedShortcuts
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.TabReselectEffect
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.TileGrid
import org.chyavorec.app.ui.components.TileItem
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection

@Composable
private fun AppItem(icon: ImageVector, title: String, onClick: () -> Unit) = ListItem(
    headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium) },
    leadingContent = { Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
    trailingContent = { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null) },
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.clickable(onClick = onClick),
)

/** „Още“: читалището и заниманията като плочки, настройките на приложението като списък. */
@Composable
fun MoreScreen(navigate: (String) -> Unit) {
    val c = LocalAppContainer.current
    var digitalClub by remember { mutableStateOf<SiteLink?>(null) }
    val scrollState = rememberScrollState()
    TabReselectEffect(Routes.MORE) { scrollState.animateScrollTo(0) }
    LaunchedEffect(Unit) {
        // „Дигитален клуб“ се показва само ако страницата съществува на сайта.
        digitalClub = (c.siteRepository.links(false) as? Outcome.Success)?.value?.data?.firstOrNull { it.kind == SiteSection.DIGITAL_CLUB }
    }
    // „Добави на началния екран“ — само ако launcher-ът поддържа закачане.
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val canPin = PinnedShortcuts.rememberSupported(context)
    var pinSheet by remember { mutableStateOf(false) }
    val pinFailed = stringResource(R.string.pin_failed)
    if (pinSheet) PinSectionsSheet(onFailed = { scope.launch { snackbar.showSnackbar(pinFailed) } }, onDismiss = { pinSheet = false })
    Scaffold(topBar = { AppTopBar(stringResource(R.string.tab_more), onSearch = { navigate(Routes.SEARCH) }) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(scrollState)) {
            // Две групи: читалището (осморъка — общността) и заниманията в приложението.
            SectionLabel(stringResource(R.string.more_group_chitalishte), Modifier.padding(start = ScreenGutter, top = 8.dp, bottom = 12.dp))
            val centre = buildList {
                add(TileItem(FolkIcons.Messages, stringResource(R.string.messages_title), stringResource(R.string.more_messages_desc)) { navigate(Routes.MESSAGES) })
                add(TileItem(FolkIcons.Events, stringResource(R.string.events_title), stringResource(R.string.more_events_desc)) { navigate(Routes.EVENTS) })
                add(TileItem(FolkIcons.Activities, stringResource(R.string.activities_title), stringResource(R.string.more_activities_desc)) { navigate(Routes.ACTIVITIES) })
                add(TileItem(FolkIcons.Chitalishte, stringResource(R.string.qa_about), stringResource(R.string.more_about_desc)) { navigate(Routes.ABOUT_CHITALISHTE) })
                digitalClub?.let { l ->
                    add(TileItem(FolkIcons.Computer, l.title, stringResource(R.string.more_digital_desc)) { navigate(Routes.page(l.url, l.title)) })
                }
                add(TileItem(FolkIcons.Gallery, stringResource(R.string.gallery_title), stringResource(R.string.more_gallery_desc)) { navigate(Routes.GALLERY) })
                add(TileItem(FolkIcons.Documents, stringResource(R.string.documents_title), stringResource(R.string.more_documents_desc)) { navigate(Routes.DOCUMENTS) })
                add(TileItem(FolkIcons.Pin, stringResource(R.string.contacts_title), stringResource(R.string.more_contacts_desc)) { navigate(Routes.CONTACTS) })
            }
            TileGrid(centre)
            SectionLabel(stringResource(R.string.more_group_pastimes), Modifier.padding(start = ScreenGutter, top = 28.dp, bottom = 12.dp))
            val pastimes = listOf(
                TileItem(FolkIcons.Embroider, stringResource(R.string.embroidery_title), stringResource(R.string.more_embroidery_desc)) { navigate(Routes.EMBROIDERY) },
                TileItem(FolkIcons.Newspaper, stringResource(R.string.newspaper_title), stringResource(R.string.more_newspaper_desc)) { navigate(Routes.NEWSPAPER) },
                TileItem(FolkIcons.Timeline, stringResource(R.string.timeline_title), stringResource(R.string.more_timeline_desc)) { navigate(Routes.TIMELINE) },
                TileItem(FolkIcons.Quiz, stringResource(R.string.quiz_title), stringResource(R.string.more_quiz_desc)) { navigate(Routes.QUIZ) },
            )
            TileGrid(pastimes, firstIndex = centre.size)
            SectionLabel(stringResource(R.string.more_group_app), Modifier.padding(start = ScreenGutter, top = 28.dp, bottom = 4.dp))
            AppItem(Icons.Outlined.Settings, stringResource(R.string.settings_title)) { navigate(Routes.SETTINGS) }
            AppItem(Icons.Outlined.PrivacyTip, stringResource(R.string.privacy_title)) { navigate(Routes.PRIVACY) }
            if (canPin) AppItem(Icons.Outlined.AddHome, stringResource(R.string.pin_add)) { pinSheet = true }
            AppItem(Icons.Outlined.Info, stringResource(R.string.about_title)) { navigate(Routes.ABOUT) }
            Spacer(Modifier.height(24.dp))
        }
    }
}
