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
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.diagnostics.AppDiagnostics
import org.chyavorec.app.diagnostics.AppPerf
import org.chyavorec.app.diagnostics.PerfData
import org.chyavorec.app.diagnostics.PerfStats
import org.chyavorec.app.diagnostics.PerfSummary
import org.chyavorec.app.diagnostics.StartStats
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.util.Intents
import org.chyavorec.core.DiagnosticEntry
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * „Диагностика“: последните записи от локалния дневник на грешките. „Сподели“ праща
 * обикновен текст през системното меню — само по избор на читателя; „Изчисти“ ги изтрива.
 * Дневникът не съдържа лични данни (виж DiagnosticSanitizer). Още „Бързодействие“
 * (стартове и плавност, виж diagnostics/AppPerf) — над записите, също само на устройството.
 */
@Composable
fun DiagnosticsScreen(onBack: () -> Unit) {
    val c = LocalAppContainer.current
    val context = LocalContext.current
    val log = remember { AppDiagnostics.log(context) }
    // Най-новият запис отгоре.
    var entries by remember { mutableStateOf(log.entries().asReversed()) }
    // „Бързодействие“: стартовете и плавността (малък файл, вече в паметта след първия път).
    var perf by remember { mutableStateOf(PerfStats.summary(AppPerf.store(context).read())) }
    val hasData = entries.isNotEmpty() || !perf.isEmpty
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
                        enabled = hasData,
                        onClick = {
                            val text = log.export(AppDiagnostics.header(c.config.versionName, c.config.versionCode), c.clock.zone()) +
                                PerfStats.export(perf)
                            Intents.share(context, subject, text)
                        },
                    ) {
                        Icon(Icons.Outlined.Share, null, Modifier.padding(end = ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.action_share))
                    }
                    OutlinedButton(
                        enabled = hasData,
                        onClick = {
                            log.clear()
                            entries = emptyList()
                            // И времената на стартиране, и плавността.
                            AppPerf.clear(context)
                            perf = PerfStats.summary(PerfData())
                            scope.launch { snackbar.showSnackbar(clearedMsg) }
                        },
                    ) {
                        Icon(Icons.Outlined.DeleteSweep, null, Modifier.padding(end = ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.action_clear))
                    }
                }
            }
            item { PerfSection(perf) }
            if (entries.isEmpty()) {
                // Празното състояние — като на всички екрани (илюстрация + заглавие).
                item { EmptyView(stringResource(R.string.diagnostics_empty), icon = Icons.Outlined.TaskAlt) }
            } else {
                item {
                    Text(stringResource(R.string.diagnostics_count, entries.size), style = MaterialTheme.typography.labelLarge)
                }
                items(entries) { e -> EntryCard(e, c.clock.zone()) }
            }
        }
    }
}

/** „Бързодействие“: медианата и последният старт (студен/топъл) и най-насечените екрани. */
@Composable
internal fun PerfSection(summary: PerfSummary, modifier: Modifier = Modifier) {
    val locale = LocalConfiguration.current.locales[0]
    AppCard(modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                stringResource(R.string.perf_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                stringResource(R.string.perf_intro),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (summary.isEmpty) {
                Text(stringResource(R.string.perf_empty), style = MaterialTheme.typography.bodyMedium)
            }
            summary.cold?.let { StartLines(stringResource(R.string.perf_cold, it.count), it) }
            summary.warm?.let { StartLines(stringResource(R.string.perf_warm, it.count), it) }
            if (summary.topRoutes.isNotEmpty()) {
                Text(
                    stringResource(R.string.perf_frames),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
                for (r in summary.topRoutes) {
                    Text(
                        stringResource(
                            R.string.perf_route, r.route, String.format(locale, "%.1f %%", r.jankyPercent), r.frames,
                            stringResource(R.string.perf_ms, r.worstMs),
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    )
                }
            }
        }
    }
}

@Composable
private fun StartLines(title: String, s: StartStats) {
    Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
    Text(
        stringResource(R.string.perf_first_frame, stringResource(R.string.perf_ms, s.firstFrame.median), stringResource(R.string.perf_ms, s.firstFrame.last)),
        style = MaterialTheme.typography.bodyMedium,
    )
    s.fullyDrawn?.let {
        Text(
            stringResource(R.string.perf_fully_drawn, stringResource(R.string.perf_ms, it.median), stringResource(R.string.perf_ms, it.last)),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

@Composable
private fun EntryCard(e: DiagnosticEntry, zone: java.time.ZoneId) {
    AppCard(Modifier.fillMaxWidth()) {
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
