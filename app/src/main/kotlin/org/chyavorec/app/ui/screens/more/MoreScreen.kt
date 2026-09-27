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
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.AppTopBar
import org.chyavorec.app.ui.components.SectionLabel
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

/** „Още“: разделите на читалището като плочки и настройките на приложението като списък. */
@Composable
fun MoreScreen(navigate: (String) -> Unit) {
    val c = LocalAppContainer.current
    var digitalClub by remember { mutableStateOf<SiteLink?>(null) }
    LaunchedEffect(Unit) {
        // „Дигитален клуб“ се показва само ако страницата съществува на сайта.
        digitalClub = (c.siteRepository.links(false) as? Outcome.Success)?.value?.data?.firstOrNull { it.kind == SiteSection.DIGITAL_CLUB }
    }
    Scaffold(topBar = { AppTopBar(stringResource(R.string.tab_more), onSearch = { navigate(Routes.SEARCH) }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            SectionLabel(stringResource(R.string.more_group_chitalishte), Modifier.padding(start = 20.dp, top = 8.dp, bottom = 12.dp))
            val tiles = buildList {
                add(TileItem(Icons.Outlined.Campaign, stringResource(R.string.messages_title), stringResource(R.string.more_messages_desc)) { navigate(Routes.MESSAGES) })
                add(TileItem(Icons.Outlined.Event, stringResource(R.string.events_title), stringResource(R.string.more_events_desc)) { navigate(Routes.EVENTS) })
                add(TileItem(Icons.Outlined.TheaterComedy, stringResource(R.string.activities_title), stringResource(R.string.more_activities_desc)) { navigate(Routes.ACTIVITIES) })
                add(TileItem(Icons.Outlined.AccountBalance, stringResource(R.string.qa_about), stringResource(R.string.more_about_desc)) { navigate(Routes.ABOUT_CHITALISHTE) })
                digitalClub?.let { l ->
                    add(TileItem(Icons.Outlined.Computer, l.title, stringResource(R.string.more_digital_desc)) { navigate(Routes.page(l.url, l.title)) })
                }
                add(TileItem(Icons.Outlined.Photo, stringResource(R.string.gallery_title), stringResource(R.string.more_gallery_desc)) { navigate(Routes.GALLERY) })
                add(TileItem(Icons.Outlined.Folder, stringResource(R.string.documents_title), stringResource(R.string.more_documents_desc)) { navigate(Routes.DOCUMENTS) })
                add(TileItem(Icons.Outlined.Place, stringResource(R.string.contacts_title), stringResource(R.string.more_contacts_desc)) { navigate(Routes.CONTACTS) })
            }
            TileGrid(tiles)
            SectionLabel(stringResource(R.string.more_group_app), Modifier.padding(start = 20.dp, top = 28.dp, bottom = 4.dp))
            AppItem(Icons.Outlined.Settings, stringResource(R.string.settings_title)) { navigate(Routes.SETTINGS) }
            AppItem(Icons.Outlined.PrivacyTip, stringResource(R.string.privacy_title)) { navigate(Routes.PRIVACY) }
            AppItem(Icons.Outlined.Info, stringResource(R.string.about_title)) { navigate(Routes.ABOUT) }
            Spacer(Modifier.height(24.dp))
        }
    }
}
