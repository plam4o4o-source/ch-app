package org.chyavorec.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.navigation.PinSection
import org.chyavorec.app.ui.navigation.PinnedShortcuts

/**
 * „⋮“ в лентата на книга/събитие с едно действие — „Добави на началния екран“. Не се показва,
 * ако launcher-ът не поддържа закачане (Android < 8 или друга начална страница). Ако launcher-ът
 * откаже — тиха бележка в [snackbar].
 */
@Composable
fun PinShortcutMenu(info: () -> ShortcutInfoCompat?, snackbar: SnackbarHostState) {
    val context = LocalContext.current
    if (!remember(context) { PinnedShortcuts.supported(context) }) return
    var open by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val failed = stringResource(R.string.pin_failed)
    Box {
        IconButton(onClick = { open = true }) { Icon(Icons.Outlined.MoreVert, contentDescription = stringResource(R.string.pin_more_cd)) }
        AppDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pin_add)) },
                leadingIcon = { Icon(Icons.Outlined.AddHome, null) },
                onClick = {
                    open = false
                    if (!PinnedShortcuts.request(context, info())) scope.launch { snackbar.showSnackbar(failed) }
                },
            )
        }
    }
}

/** Лист с разделите, които се закачат на началния екран („Още“ → „Добави на началния екран“); [onFailed] — launcher-ът отказа. */
@Composable
fun PinSectionsSheet(onFailed: () -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AppBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.pin_add), style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp).semantics { heading() },
            )
            Text(
                stringResource(R.string.pin_sheet_text), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 8.dp),
            )
            for (section in PinSection.entries) {
                ListItem(
                    headlineContent = { Text(stringResource(section.labelRes)) },
                    leadingContent = { Icon(painterResource(section.iconRes), null, tint = Color.Unspecified, modifier = Modifier.padding(0.dp)) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier.clickable {
                        onDismiss()
                        if (!PinnedShortcuts.request(context, PinnedShortcuts.section(context, section))) onFailed()
                    }.padding(horizontal = 8.dp),
                )
            }
        }
    }
}
