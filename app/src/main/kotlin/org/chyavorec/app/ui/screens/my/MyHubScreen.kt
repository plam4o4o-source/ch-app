package org.chyavorec.app.ui.screens.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.AppTopBar
import org.chyavorec.app.ui.components.DemoBanner
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.TileGrid
import org.chyavorec.app.ui.components.TileItem
import org.chyavorec.app.ui.components.MembershipPill
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.data.repository.AuthState

@Composable
fun accountViewModel(): AccountViewModel = appViewModel(key = "account") { AccountViewModel(it) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHubScreen(navigate: (String) -> Unit) {
    val vm = accountViewModel()
    val auth by vm.authState.collectAsStateWithLifecycle()
    val caps by vm.capabilities.collectAsStateWithLifecycle()
    val profile by vm.profile.collectAsStateWithLifecycle()
    val selfCard by vm.selfCard.collectAsStateWithLifecycle()
    var confirmLogout by remember { mutableStateOf(false) }
    val signedIn = auth is AuthState.SignedIn

    Scaffold(topBar = {
        AppTopBar(stringResource(R.string.tab_my), onSearch = { navigate(Routes.SEARCH) })
    }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            DemoBanner(vm.isDemo)
            if (signedIn) {
                val p = profile.data
                Surface(
                    color = Brand.Ink, contentColor = Brand.Parchment, shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) {
                    Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Brand.Gold, contentColor = Brand.Ink, modifier = Modifier.size(56.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text(p?.fullName?.takeIf { it.isNotBlank() }?.split(' ')?.mapNotNull { it.firstOrNull() }?.take(2)?.joinToString("") ?: "·",
                                    style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            val name = p?.fullName?.takeIf { it.isNotBlank() }
                            when {
                                name != null -> Text(name, style = MaterialTheme.typography.headlineSmall)
                                p != null -> Text(stringResource(R.string.my_profile), style = MaterialTheme.typography.headlineSmall)
                                profile.error != null -> TextButton(onClick = { vm.loadProfile() }) {
                                    Text(stringResource(R.string.action_retry), color = Brand.GoldLight)
                                }
                                else -> Text(stringResource(R.string.loading), style = MaterialTheme.typography.headlineSmall)
                            }
                            p?.let { Text(stringResource(R.string.reader_number, it.cardNumber), style = MaterialTheme.typography.bodyMedium, color = Brand.GoldLight) }
                            p?.membership?.let { MembershipPill(it.status, Modifier.padding(top = 6.dp)) }
                        }
                    }
                }
            } else {
                GuestCard(loginSupported = caps?.login == true, onLogin = { navigate(Routes.LOGIN) })
            }

            SectionLabel(stringResource(R.string.my_section_library), Modifier.padding(start = 20.dp, top = 8.dp, bottom = 12.dp))
            val tiles = buildList {
                add(TileItem(Icons.Outlined.Badge, stringResource(R.string.my_card),
                    if (signedIn) stringResource(R.string.my_card_desc) else if (selfCard != null) stringResource(R.string.my_card_self_desc) else stringResource(R.string.my_card_add_desc)) {
                    navigate(Routes.CARD)
                })
                add(TileItem(Icons.Outlined.CollectionsBookmark, stringResource(R.string.my_books), stringResource(R.string.my_books_desc)) { navigate(Routes.LOANS) })
                add(TileItem(Icons.Outlined.CardMembership, stringResource(R.string.my_membership), stringResource(R.string.my_membership_desc)) { navigate(Routes.MEMBERSHIP) })
                if (signedIn) add(TileItem(Icons.Outlined.Person, stringResource(R.string.my_profile), stringResource(R.string.my_profile_desc)) { navigate(Routes.PROFILE) })
                add(TileItem(Icons.Outlined.NotificationsNone, stringResource(R.string.my_notifications), stringResource(R.string.my_notifications_desc)) { navigate(Routes.NOTIFICATIONS) })
            }
            TileGrid(tiles)
            Spacer(Modifier.height(8.dp))

            if (signedIn) {
                OutlinedButton(onClick = { confirmLogout = true }, modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.action_logout))
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            title = { Text(stringResource(R.string.logout_title)) },
            text = { Text(stringResource(R.string.logout_text)) },
            confirmButton = { TextButton(onClick = { confirmLogout = false; vm.logout() }) { Text(stringResource(R.string.action_logout)) } },
            dismissButton = { TextButton(onClick = { confirmLogout = false }) { Text(stringResource(R.string.action_cancel)) } },
        )
    }
}

@Composable
private fun GuestCard(loginSupported: Boolean, onLogin: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(stringResource(R.string.guest_title), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })
            Spacer(Modifier.height(6.dp))
            Text(stringResource(R.string.guest_text), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(14.dp))
            if (loginSupported) {
                Button(onClick = onLogin) {
                    Icon(Icons.AutoMirrored.Outlined.Login, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.action_login))
                }
            } else {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.Info, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.na_login), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
