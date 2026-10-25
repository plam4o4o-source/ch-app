package org.chyavorec.app.ui.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.format.Formatter
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.update.AppUpdater
import org.chyavorec.app.update.UpdateFailure
import org.chyavorec.app.update.UpdateState
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.Intents
import org.chyavorec.data.update.UpdateInfo

/**
 * Диалог за нова версия: показва се при стартиране (ако има нова версия и не е
 * отложена), след „Провери за нова версия“ или след докосване на известието.
 */
@Composable
fun UpdateDialog(updater: AppUpdater) {
    if (!updater.enabled) return
    val visible by updater.prompt.collectAsStateWithLifecycle()
    val state by updater.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // След връщане от „Инсталиране на неизвестни приложения“ инсталирането продължава само.
    LifecycleResumeEffect(updater) {
        updater.onResume()
        onPauseOrDispose { }
    }
    if (!visible) {
        // „Какво ново“ след обновяване — не заедно с диалога за (още по-нова) версия.
        PendingWhatsNew(updater)
        return
    }

    val s = state
    val info = updater.currentInfo()
    val close = { updater.hidePrompt(); updater.dismissError() }
    val title = if (info != null) stringResource(R.string.update_dialog_title, info.versionName) else stringResource(R.string.settings_updates)

    AlertDialog(
        onDismissRequest = { if (s !is UpdateState.Downloading) close() },
        icon = { Icon(Icons.Outlined.SystemUpdate, null) },
        title = { Text(title) },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()).heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                when (s) {
                    is UpdateState.Checking -> {
                        Text(stringResource(R.string.update_status_checking))
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                    is UpdateState.UpToDate -> Text(stringResource(R.string.update_status_current, versionLabel(updater)))
                    is UpdateState.Available -> {
                        Text(stringResource(R.string.update_dialog_available))
                        s.info.sizeBytes?.let { Text(stringResource(R.string.update_size, Formatter.formatShortFileSize(context, it)), style = MaterialTheme.typography.bodySmall) }
                        Notes(s.info)
                    }
                    is UpdateState.Downloading -> {
                        Text(stringResource(R.string.update_status_downloading), Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                        val p = s.progress
                        if (p == null) LinearProgressIndicator(Modifier.fillMaxWidth())
                        else {
                            LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth())
                            Text(stringResource(R.string.update_progress, (p * 100).toInt()), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    is UpdateState.Ready -> {
                        Text(stringResource(R.string.update_ready))
                        Notes(s.info)
                    }
                    is UpdateState.NeedsPermission -> Text(stringResource(R.string.update_permission))
                    is UpdateState.Installing -> {
                        Text(stringResource(R.string.update_installing))
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                    is UpdateState.Failed -> Text(
                        stringResource(failureText(s.reason)),
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                    UpdateState.Idle -> Text(stringResource(R.string.update_status_current, versionLabel(updater)))
                }
            }
        },
        confirmButton = {
            when (s) {
                is UpdateState.Available -> TextButton(onClick = updater::startDownloadAndInstall) { Text(stringResource(R.string.update_download)) }
                is UpdateState.Ready -> TextButton(onClick = updater::startInstall) { Text(stringResource(R.string.update_install)) }
                is UpdateState.NeedsPermission -> TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}"))
                    runCatching { context.startActivity(intent) }
                }) { Text(stringResource(R.string.update_permission_action)) }
                is UpdateState.Failed -> {
                    val release = s.info?.releaseUrl
                    if (s.reason == UpdateFailure.SIGNATURE && release != null) {
                        TextButton(onClick = { Intents.openUrl(context, release) }) { Text(stringResource(R.string.update_open_release)) }
                    } else if (s.info != null && s.reason != UpdateFailure.SIGNATURE) {
                        TextButton(onClick = updater::startDownloadAndInstall) { Text(stringResource(R.string.action_retry)) }
                    } else {
                        TextButton(onClick = close) { Text(stringResource(R.string.action_close)) }
                    }
                }
                is UpdateState.Downloading, is UpdateState.Installing, is UpdateState.Checking ->
                    TextButton(onClick = { updater.hidePrompt() }) { Text(stringResource(R.string.update_hide)) }
                else -> TextButton(onClick = close) { Text(stringResource(R.string.action_ok)) }
            }
        },
        dismissButton = {
            when (s) {
                is UpdateState.Available, is UpdateState.Ready, is UpdateState.NeedsPermission ->
                    TextButton(onClick = updater::snooze) { Text(stringResource(R.string.update_later)) }
                is UpdateState.Failed -> if (s.info != null) TextButton(onClick = close) { Text(stringResource(R.string.action_close)) }
                else -> Unit
            }
        },
    )
}

@Composable
private fun Notes(info: UpdateInfo) {
    val notes = info.notesFor(english = !Formatters.isBulgarian(LocalContext.current))
    if (notes.isBlank()) return
    Text(stringResource(R.string.update_notes), style = MaterialTheme.typography.titleSmall)
    Text(notes.take(1500), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

/** Причината за неуспеха — един и същ текст в диалога и в настройките. */
@StringRes
private fun failureText(reason: UpdateFailure): Int = when (reason) {
    UpdateFailure.CHECK -> R.string.update_error_check
    UpdateFailure.DOWNLOAD -> R.string.update_error_network
    UpdateFailure.CHECKSUM -> R.string.update_error_checksum
    UpdateFailure.SIGNATURE -> R.string.update_error_signature
    UpdateFailure.INSTALL -> R.string.update_error_install
}

/**
 * „Какво ново във версия X“ — веднъж, при първото отваряне след обновяване (бележките
 * от update.json на инсталираната версия, на езика на интерфейса).
 */
@Composable
private fun PendingWhatsNew(updater: AppUpdater) {
    val whatsNew by updater.whatsNew.collectAsStateWithLifecycle()
    val w = whatsNew ?: return
    WhatsNewSheet(
        versionName = w.versionName,
        notes = w.notesFor(english = !Formatters.isBulgarian(LocalContext.current)),
        onDismiss = updater::dismissWhatsNew,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewSheet(versionName: String, notes: String, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                stringResource(R.string.whats_new_title, versionName),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            Text(notes.take(1500), style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(stringResource(R.string.action_ok))
            }
        }
    }
}

@Composable
fun versionLabel(updater: AppUpdater): String = updater.installedVersionName

/** Кратък текст за състоянието — в настройките. */
@Composable
fun updateStatusText(state: UpdateState, updater: AppUpdater): String = when (state) {
    is UpdateState.Checking -> stringResource(R.string.update_status_checking)
    is UpdateState.Available -> stringResource(R.string.update_status_available, state.info.versionName)
    is UpdateState.Downloading -> stringResource(R.string.update_status_downloading)
    is UpdateState.Ready, is UpdateState.NeedsPermission, is UpdateState.Installing ->
        stringResource(R.string.update_status_ready, updater.currentInfo()?.versionName.orEmpty())
    is UpdateState.Failed -> stringResource(failureText(state.reason))
    UpdateState.UpToDate -> stringResource(R.string.update_status_current, versionLabel(updater))
    UpdateState.Idle -> stringResource(R.string.about_version, versionLabel(updater))
}
