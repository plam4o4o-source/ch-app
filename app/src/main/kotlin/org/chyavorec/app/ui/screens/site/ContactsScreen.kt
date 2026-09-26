package org.chyavorec.app.ui.screens.site

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Intents

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContactsScreen(onBack: () -> Unit) {
    val vm = appViewModel { ContactsViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(topBar = { BackTopBar(stringResource(R.string.contacts_title), onBack) }) { padding ->
        PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(state = state, onRetry = { vm.refresh() }, isEmpty = { false }, skeleton = { SkeletonCards() }, empty = {},
                errorSubject = stringResource(R.string.contacts_title)) { c ->
                Column(Modifier.verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
                    SyncBanner(state.fromCache, state.syncedAt, state.refreshError)
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
                        ContactCard(Icons.Outlined.Schedule, stringResource(R.string.contacts_hours), c.workingHours.ifEmpty { listOf(stringResource(R.string.contacts_hours_unknown)) })
                        ContactCard(Icons.Outlined.Public, stringResource(R.string.contacts_web), listOfNotNull(c.website, c.facebook)) { Intents.openUrl(context, it) }
                        if (!c.fromSite) {
                            Text(stringResource(R.string.contacts_fallback_note), style = MaterialTheme.typography.bodySmall, color = LocalExtendedColors.current.warn)
                        } else {
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
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, null, tint = LocalExtendedColors.current.gold)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                lines.forEach { line ->
                    if (onClick != null) {
                        androidx.compose.material3.TextButton(onClick = { onClick(line) }, contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                            Text(line, style = MaterialTheme.typography.titleMedium)
                        }
                    } else {
                        Text(line, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }
    }
}
