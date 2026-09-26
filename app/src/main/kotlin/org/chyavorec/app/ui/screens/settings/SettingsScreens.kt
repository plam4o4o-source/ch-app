package org.chyavorec.app.ui.screens.settings

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.util.Intents

@Composable
private fun GroupTitle(text: String) = Text(
    text, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 4.dp).semantics { heading() },
)

@Composable
private fun NavItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) = ListItem(
    headlineContent = { Text(title) },
    supportingContent = subtitle?.let { { Text(it) } },
    leadingContent = { Icon(icon, null) },
    trailingContent = { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null) },
    modifier = Modifier.clickable(onClick = onClick),
)

@Composable
private fun SwitchItem(title: String, subtitle: String?, checked: Boolean, onChange: (Boolean) -> Unit) = ListItem(
    headlineContent = { Text(title) },
    supportingContent = subtitle?.let { { Text(it) } },
    trailingContent = { Switch(checked, onCheckedChange = null) },
    modifier = Modifier.selectable(checked, role = Role.Switch, onClick = { onChange(!checked) }),
)

@Composable
fun SettingsScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val c = LocalAppContainer.current
    val settings by c.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var themeDialog by remember { mutableStateOf(false) }
    var langDialog by remember { mutableStateOf(false) }
    var clearDialog by remember { mutableStateOf(false) }
    val clearedMsg = stringResource(R.string.settings_cache_cleared)
    val s = settings ?: return

    Scaffold(topBar = { BackTopBar(stringResource(R.string.settings_title), onBack) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState()).widthIn(max = 720.dp)) {
            GroupTitle(stringResource(R.string.settings_appearance))
            NavItem(Icons.Outlined.DarkMode, stringResource(R.string.settings_theme), themeLabel(s.themeMode)) { themeDialog = true }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                SwitchItem(stringResource(R.string.settings_dynamic), stringResource(R.string.settings_dynamic_desc), s.dynamicColor) {
                    scope.launch { c.settings.setDynamicColor(it) }
                }
            }
            NavItem(Icons.Outlined.Language, stringResource(R.string.settings_language), currentLanguageLabel()) { langDialog = true }

            GroupTitle(stringResource(R.string.settings_notifications))
            NavItem(Icons.Outlined.NotificationsNone, stringResource(R.string.my_notifications), stringResource(R.string.settings_notifications_desc)) {
                navigate(Routes.NOTIFICATIONS)
            }

            GroupTitle(stringResource(R.string.settings_data))
            NavItem(Icons.Outlined.DeleteSweep, stringResource(R.string.settings_clear_cache), stringResource(R.string.settings_clear_cache_desc)) { clearDialog = true }

            GroupTitle(stringResource(R.string.settings_legal))
            NavItem(Icons.Outlined.PrivacyTip, stringResource(R.string.privacy_title)) { navigate(Routes.PRIVACY) }
            NavItem(Icons.Outlined.Description, stringResource(R.string.terms_title)) { navigate(Routes.TERMS) }
            NavItem(Icons.Outlined.Info, stringResource(R.string.about_title), stringResource(R.string.about_version, c.config.versionName)) { navigate(Routes.ABOUT) }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (themeDialog) {
        ChoiceDialog(
            title = stringResource(R.string.settings_theme),
            options = ThemeMode.entries.map { it to themeLabel(it) },
            selected = s.themeMode,
            onSelect = { mode -> themeDialog = false; scope.launch { c.settings.setTheme(mode) } },
            onDismiss = { themeDialog = false },
        )
    }
    if (langDialog) {
        val current = AppCompatDelegate.getApplicationLocales().toLanguageTags().ifBlank { "" }
        ChoiceDialog(
            title = stringResource(R.string.settings_language),
            options = listOf("" to stringResource(R.string.language_system), "bg" to "Български", "en" to "English"),
            selected = current.substringBefore('-'),
            onSelect = { tag ->
                langDialog = false
                AppCompatDelegate.setApplicationLocales(if (tag.isEmpty()) LocaleListCompat.getEmptyLocaleList() else LocaleListCompat.forLanguageTags(tag))
            },
            onDismiss = { langDialog = false },
        )
    }
    if (clearDialog) {
        AlertDialog(
            onDismissRequest = { clearDialog = false },
            title = { Text(stringResource(R.string.settings_clear_cache)) },
            text = { Text(stringResource(R.string.settings_clear_cache_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    clearDialog = false
                    scope.launch { c.clearPublicCache(); snackbar.showSnackbar(clearedMsg) }
                }) { Text(stringResource(R.string.action_clear)) }
            },
            dismissButton = { TextButton(onClick = { clearDialog = false }) { Text(stringResource(R.string.action_cancel)) } },
        )
    }
}

@Composable
private fun currentLanguageLabel(): String {
    val tags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    return when {
        tags.startsWith("bg") -> "Български"
        tags.startsWith("en") -> "English"
        else -> stringResource(R.string.language_system)
    }
}

@Composable
fun themeLabel(mode: ThemeMode): String = stringResource(
    when (mode) {
        ThemeMode.SYSTEM -> R.string.theme_system
        ThemeMode.LIGHT -> R.string.theme_light
        ThemeMode.DARK -> R.string.theme_dark
    },
)

@Composable
private fun <T> ChoiceDialog(title: String, options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { (value, label) ->
                    Row(
                        Modifier.fillMaxWidth().selectable(value == selected, role = Role.RadioButton) { onSelect(value) }.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(value == selected, onClick = null)
                        Text(label, modifier = Modifier.padding(start = 12.dp))
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_close)) } },
    )
}

@Composable
fun NotificationSettingsScreen(onBack: () -> Unit) {
    val c = LocalAppContainer.current
    val settings by c.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()
    val permission = rememberNotificationPermission()
    val context = LocalContext.current
    val s = settings ?: return
    val loansSupported = remember { mutableStateOf<Boolean?>(null) }
    androidx.compose.runtime.LaunchedEffect(Unit) { loansSupported.value = c.authRepository.capabilities().loans }

    Scaffold(topBar = { BackTopBar(stringResource(R.string.my_notifications), onBack) }) { padding ->
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState())) {
            Text(stringResource(R.string.notifications_intro), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(20.dp))
            fun enable(on: Boolean, save: suspend (Boolean) -> Unit) {
                if (on) permission.request { scope.launch { save(true) } } else scope.launch { save(false) }
            }
            SwitchItem(stringResource(R.string.channel_news), stringResource(R.string.channel_news_desc), s.notifyNews) { enable(it, { v -> c.settings.setNotifyNews(v) }) }
            SwitchItem(stringResource(R.string.channel_events), stringResource(R.string.channel_events_desc), s.notifyEvents) { enable(it, { v -> c.settings.setNotifyEvents(v) }) }
            SwitchItem(
                stringResource(R.string.channel_loans),
                if (loansSupported.value == false) stringResource(R.string.notifications_loans_na) else stringResource(R.string.channel_loans_desc),
                s.notifyLoans,
            ) { enable(it, { v -> c.settings.setNotifyLoans(v) }) }
            SwitchItem(stringResource(R.string.channel_library), stringResource(R.string.notifications_library_desc), s.notifyLibrary) { enable(it, { v -> c.settings.setNotifyLibrary(v) }) }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            NavItem(Icons.Outlined.Palette, stringResource(R.string.notifications_system)) {
                context.startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            }
            Text(stringResource(R.string.notifications_push_note), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(20.dp))
        }
    }
}

@Suppress("unused")
private val arrangement = Arrangement.Top

@Suppress("unused")
private fun openSite(context: android.content.Context, url: String) = Intents.openUrl(context, url)
