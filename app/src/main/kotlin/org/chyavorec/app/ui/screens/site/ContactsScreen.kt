package org.chyavorec.app.ui.screens.site

import org.chyavorec.app.ui.components.AppCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
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
                        // Кратки надписи с икона; TalkBack чете пълното описание на действието.
                        ActionButtons(Modifier.fillMaxWidth()) {
                            c.phones.firstOrNull()?.let { ph ->
                                ActionButton(Icons.Outlined.Call, stringResource(R.string.contacts_call), stringResource(R.string.contacts_call_cd)) { Intents.dial(context, ph) }
                            }
                            c.emails.firstOrNull()?.let { em ->
                                ActionButton(Icons.Outlined.Email, stringResource(R.string.contacts_email_label), stringResource(R.string.contacts_email_cd)) { Intents.email(context, em) }
                            }
                            ActionButton(Icons.Outlined.Map, stringResource(R.string.contacts_map), stringResource(R.string.contacts_map_cd)) {
                                Intents.map(context, c.address?.let { "$it, ${c.organization}" } ?: c.mapQuery)
                            }
                            ActionButton(Icons.Outlined.Language, stringResource(R.string.contacts_site_short), stringResource(R.string.contacts_site_cd)) { Intents.openUrl(context, c.website) }
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
private fun ActionButton(icon: ImageVector, text: String, description: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick, modifier = Modifier.semantics { contentDescription = description },
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text(text)
    }
}

/**
 * Бутоните за действие — в мрежа с пълни редове: всички на един ред, ако надписите се събират на
 * един ред; иначе по два (при 4); иначе един под друг на цялата ширина (едър шрифт). В реда са
 * еднакво широки и високи — без „стъпала“ и без 2 + 1.
 */
@Composable
private fun ActionButtons(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content, modifier) { measurables, constraints ->
        val gap = 10.dp.roundToPx()
        val width = constraints.maxWidth
        val n = measurables.size
        fun cell(cols: Int) = (width - gap * (cols - 1)) / cols
        val cols = (n downTo 2).firstOrNull { c ->
            n % c == 0 && measurables.all { it.maxIntrinsicWidth(Constraints.Infinity) <= cell(c) }
        } ?: 1
        val w = cell(cols)
        val rows = measurables.chunked(cols).map { row ->
            val h = row.maxOf { it.maxIntrinsicHeight(w) }
            row.map { it.measure(Constraints.fixed(w, h)) }
        }
        val height = rows.sumOf { it.first().height } + gap * (rows.size - 1).coerceAtLeast(0)
        layout(width, height) {
            var y = 0
            rows.forEach { row ->
                row.forEachIndexed { i, p -> p.placeRelative(i * (w + gap), y) }
                y += row.first().height + gap
            }
        }
    }
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
