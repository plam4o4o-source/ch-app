package org.chyavorec.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.diagnostics.AppDiagnostics
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.util.Intents
import org.chyavorec.core.DiagnosticEntry
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * „Диагностика“: последните записи от локалния дневник на грешките. „Сподели“ праща
 * обикновен текст през системното меню — само по избор на читателя; „Изчисти“ ги изтрива.
 * Дневникът не съдържа лични данни (виж DiagnosticSanitizer).
 */
@Composable
fun DiagnosticsScreen(onBack: () -> Unit) {
    val c = LocalAppContainer.current
    val context = LocalContext.current
    val log = remember { AppDiagnostics.log(context) }
    // Най-новият запис отгоре.
    var entries by remember { mutableStateOf(log.entries().asReversed()) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clearedMsg = stringResource(R.string.diagnostics_cleared)
    val subject = stringResource(R.string.diagnostics_share_subject)

    Scaffold(
        topBar = { BackTopBar(stringResource(R.string.settings_diagnostics), onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).fillMaxSize().widthIn(max = 760.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Text(
                    stringResource(R.string.diagnostics_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        enabled = entries.isNotEmpty(),
                        onClick = {
                            val text = log.export(AppDiagnostics.header(c.config.versionName, c.config.versionCode), c.clock.zone())
                            Intents.share(context, subject, text)
                        },
                    ) {
                        Icon(Icons.Outlined.Share, null, Modifier.padding(end = ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.action_share))
                    }
                    OutlinedButton(
                        enabled = entries.isNotEmpty(),
                        onClick = {
                            log.clear()
                            entries = emptyList()
                            scope.launch { snackbar.showSnackbar(clearedMsg) }
                        },
                    ) {
                        Icon(Icons.Outlined.DeleteSweep, null, Modifier.padding(end = ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.action_clear))
                    }
                }
            }
            if (entries.isEmpty()) {
                item {
                    Text(
                        stringResource(R.string.diagnostics_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            } else {
                item {
                    Text(stringResource(R.string.diagnostics_count, entries.size), style = MaterialTheme.typography.labelLarge)
                }
                items(entries) { e -> EntryCard(e, c.clock.zone()) }
            }
        }
    }
}

private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

@Composable
private fun EntryCard(e: DiagnosticEntry, zone: java.time.ZoneId) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val repeated = if (e.count > 1) "  ×${e.count}" else ""
            Text(
                TIME.format(Instant.ofEpochMilli(e.at).atZone(zone)) + "  ·  " + e.tag + repeated,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(e.error, style = MaterialTheme.typography.bodyMedium)
            if (e.stack.isNotEmpty()) {
                Text(
                    e.stack.take(STACK_LINES).joinToString("\n"),
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = STACK_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** На екрана — само началото на стека (целият е в споделения текст). */
private const val STACK_LINES = 3
