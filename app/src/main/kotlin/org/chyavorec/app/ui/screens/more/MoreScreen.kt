package org.chyavorec.app.ui.screens.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection

@Composable
private fun MoreItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) = ListItem(
    headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium) },
    supportingContent = subtitle?.let { { Text(it) } },
    leadingContent = { Icon(icon, null, tint = MaterialTheme.colorScheme.primary) },
    trailingContent = { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null) },
    modifier = Modifier.clickable(onClick = onClick),
)

@Composable
fun MoreScreen(navigate: (String) -> Unit) {
    val c = LocalAppContainer.current
    var digitalClub by remember { mutableStateOf<SiteLink?>(null) }
    LaunchedEffect(Unit) {
        // „Дигитален клуб“ се показва само ако страницата съществува на сайта.
        digitalClub = (c.siteRepository.links(false) as? Outcome.Success)?.value?.data?.firstOrNull { it.kind == SiteSection.DIGITAL_CLUB }
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.tab_more), style = MaterialTheme.typography.headlineMedium) },
            actions = { Emblem(36.dp, Modifier.padding(end = 16.dp)) })
    }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            MoreItem(Icons.Outlined.Event, stringResource(R.string.events_title), stringResource(R.string.more_events_desc)) { navigate(Routes.EVENTS) }
            MoreItem(Icons.Outlined.TheaterComedy, stringResource(R.string.activities_title), stringResource(R.string.more_activities_desc)) { navigate(Routes.ACTIVITIES) }
            MoreItem(Icons.Outlined.AccountBalance, stringResource(R.string.qa_about), stringResource(R.string.more_about_desc)) { navigate(Routes.ABOUT_CHITALISHTE) }
            digitalClub?.let { l ->
                MoreItem(Icons.Outlined.Computer, l.title, stringResource(R.string.more_digital_desc)) { navigate(Routes.page(l.url, l.title)) }
            }
            MoreItem(Icons.Outlined.Photo, stringResource(R.string.gallery_title), stringResource(R.string.more_gallery_desc)) { navigate(Routes.GALLERY) }
            MoreItem(Icons.Outlined.Folder, stringResource(R.string.documents_title), stringResource(R.string.more_documents_desc)) { navigate(Routes.DOCUMENTS) }
            MoreItem(Icons.Outlined.Place, stringResource(R.string.contacts_title), stringResource(R.string.more_contacts_desc)) { navigate(Routes.CONTACTS) }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            MoreItem(Icons.Outlined.Settings, stringResource(R.string.settings_title)) { navigate(Routes.SETTINGS) }
            MoreItem(Icons.Outlined.PrivacyTip, stringResource(R.string.privacy_title)) { navigate(Routes.PRIVACY) }
            MoreItem(Icons.Outlined.Info, stringResource(R.string.about_title)) { navigate(Routes.ABOUT) }
        }
    }
}
