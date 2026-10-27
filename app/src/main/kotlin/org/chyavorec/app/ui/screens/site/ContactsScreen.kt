package org.chyavorec.app.ui.screens.site

import org.chyavorec.app.ui.components.AppCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
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
import org.chyavorec.app.ui.components.InfoBanner
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Intents

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContactsScreen(onBack: () -> Unit) {
    val vm = appViewModel { ContactsViewModel(it.siteRepository, it.fallbackContacts) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(topBar = { BackTopBar(stringResource(R.string.contacts_title), onBack) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(state = state, onRetry = { vm.refresh() }, isEmpty = { false }, skeleton = { SkeletonCards() }, empty = {},
                errorSubject = stringResource(R.string.subject_contacts)) { c ->
                Column(Modifier.verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
                    SyncBanner(state.fromCache, state.syncedAt, state.refreshError)
                    // Вградените основни данни (без връзка и без запазени от сайта) — бележката е най-отгоре.
                    if (!c.fromSite) InfoBanner(
                        Icons.Outlined.CloudOff, stringResource(R.string.contacts_fallback_note),
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                    Column(Modifier.padding(20.dp).widthIn(max = 720.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(c.organization, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            c.phones.firstOrNull()?.let { ph ->
                                FilledTonalButton(onClick = { Intents.dial(context, ph) }) { ActionContent(Icons.Outlined.Call, stringResource(R.string.contacts_call)) }
                            }
                            c.emails.firstOrNull()?.let { em ->
                                FilledTonalButton(onClick = { Intents.email(context, em) }) { ActionContent(Icons.Outlined.Email, stringResource(R.string.contacts_email)) }
                            }
                            FilledTonalButton(onClick = { Intents.map(context, c.address?.let { "$it, ${c.organization}" } ?: c.mapQuery) }) {
                                ActionContent(Icons.Outlined.Map, stringResource(R.string.contacts_map))
                            }
                            FilledTonalButton(onClick = { Intents.openUrl(context, c.website) }) { ActionContent(Icons.Outlined.Language, stringResource(R.string.contacts_site)) }
                        }
                        ContactCard(Icons.Outlined.Place, stringResource(R.string.contacts_address), listOfNotNull(c.address).ifEmpty { listOf(stringResource(R.string.contacts_see_site)) })
                        if (c.persons.isNotEmpty()) {
                            ContactCard(Icons.Outlined.Groups, stringResource(R.string.contacts_persons), c.persons.map { "${it.role}: ${it.name}" })
                        }
                        if (c.phones.isNotEmpty()) ContactCard(Icons.Outlined.Call, stringResource(R.string.contacts_phone), c.phones) { Intents.dial(context, it) }
                        if (c.emails.isNotEmpty()) ContactCard(Icons.Outlined.Email, stringResource(R.string.contacts_email_label), c.emails) { Intents.email(context, it) }
                        // Вградените данни — работното време от ресурсите (на езика на интерфейса).
                        val hours = if (c.fromSite) c.workingHours else stringResource(R.string.contacts_fallback_hours).lines()
                        ContactCard(Icons.Outlined.Schedule, stringResource(R.string.contacts_hours), hours.ifEmpty { listOf(stringResource(R.string.contacts_hours_unknown)) })
                        ContactCard(Icons.Outlined.Public, stringResource(R.string.contacts_web), listOfNotNull(c.website, c.facebook)) { Intents.openUrl(context, it) }
                        if (c.fromSite) {
                            c.sourceUrl?.let { AssistChip(onClick = { Intents.openUrl(context, it) }, label = { Text(stringResource(R.string.contacts_source)) }) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionContent(icon: ImageVector, text: String) {
    Icon(icon, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text(text)
}

@Composable
private fun ContactCard(icon: ImageVector, title: String, lines: List<String>, onClick: ((String) -> Unit)? = null) {
    AppCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, null, tint = LocalExtendedColors.current.gold)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                lines.forEach { line ->
                    if (onClick != null) {
                        // Обикновен текст, а не TextButton: заобленият клип на бутона рязал
                        // първите букви на пренесените на няколко реда връзки.
                        Text(
                            line, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                .clickable(role = Role.Button) { onClick(line) }
                                .wrapContentHeight(Alignment.CenterVertically).padding(vertical = 4.dp),
                        )
                    } else {
                        Text(line, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }
    }
}
