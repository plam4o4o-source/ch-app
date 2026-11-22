package org.chyavorec.app.ui.screens.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.AppAlertDialog

/** Изборът в потвърждението на изхода. */
internal object SignOutChoice {
    /**
     * Остава ли читателският дневник: само ако в него има нещо ([hasDiary]) и читателят е махнал
     * отметката „Изтрий и бележките…“ ([deleteDiary]). Без отметка на екрана — трие се.
     */
    fun keepDiary(hasDiary: Boolean, deleteDiary: Boolean): Boolean = hasDiary && !deleteDiary
}

/**
 * „Изход от профила?“. С поне една оценка или бележка в читателския дневник ([hasDiary]) —
 * отметка „Изтрий и бележките от читателския дневник“, сложена по подразбиране (телефонът може
 * да е общ). Махната — дневникът остава на телефона за следващия вход ([onConfirm] с `true`).
 */
@Composable
internal fun LogoutDialog(hasDiary: Boolean, onConfirm: (keepDiary: Boolean) -> Unit, onDismiss: () -> Unit) {
    var deleteDiary by rememberSaveable { mutableStateOf(true) }
    AppAlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.logout_title)) },
        text = {
            Column {
                Text(stringResource(R.string.logout_text))
                if (hasDiary) {
                    Row(
                        Modifier.fillMaxWidth().padding(top = 12.dp).heightIn(min = 48.dp)
                            .toggleable(deleteDiary, role = Role.Checkbox) { deleteDiary = it },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(deleteDiary, onCheckedChange = null)
                        Spacer(Modifier.width(12.dp))
                        Text(stringResource(R.string.logout_delete_diary), style = MaterialTheme.typography.bodyLarge)
                    }
                    if (!deleteDiary) {
                        Text(
                            stringResource(R.string.logout_diary_kept_note), style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp).semantics { liveRegion = LiveRegionMode.Polite },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(SignOutChoice.keepDiary(hasDiary, deleteDiary)) }) { Text(stringResource(R.string.action_logout)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) } },
    )
}
