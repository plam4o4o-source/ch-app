package org.chyavorec.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.ReaderFontSize
import org.chyavorec.app.ui.LocalAppContainer

/**
 * Режим за четене (статии и страници от сайта): размер на шрифта S/M/L/XL и
 * ширина на колоната. Настройките са в DataStore и важат за всички статии.
 */

/** Бутон „Аа“ за лентата на екрана. */
@Composable
fun ReaderModeButton(onClick: () -> Unit) {
    val label = stringResource(R.string.reader_mode)
    IconButton(onClick = onClick, modifier = Modifier.semantics { contentDescription = label }) {
        Text("Аа", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

/** Обвива съдържание с мащаба на шрифта и максималната ширина от настройките. */
@Composable
fun ReaderContent(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val settings by LocalAppContainer.current.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val scale = settings?.readerFontSize?.scale ?: 1f
    val maxWidth = if (settings?.readerNarrow == true) 480.dp else 600.dp
    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = density.fontScale * scale)) {
        androidx.compose.foundation.layout.Box(modifier.widthIn(max = maxWidth)) { content() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsSheet(onDismiss: () -> Unit) {
    val store = LocalAppContainer.current.settings
    val settings by store.settings.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()
    val s = settings ?: return
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
            Text(stringResource(R.string.reader_mode), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.reader_font_size), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                val sizes = ReaderFontSize.entries
                sizes.forEachIndexed { i, size ->
                    SegmentedButton(
                        selected = s.readerFontSize == size,
                        onClick = { scope.launch { store.setReaderFontSize(size) } },
                        shape = SegmentedButtonDefaults.itemShape(i, sizes.size),
                        label = { Text(size.name, fontSize = (13 * size.scale).sp, maxLines = 1) },
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(stringResource(R.string.reader_width), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = !s.readerNarrow,
                    onClick = { scope.launch { store.setReaderNarrow(false) } },
                    shape = SegmentedButtonDefaults.itemShape(0, 2),
                    label = { Text(stringResource(R.string.reader_width_normal), maxLines = 1) },
                )
                SegmentedButton(
                    selected = s.readerNarrow,
                    onClick = { scope.launch { store.setReaderNarrow(true) } },
                    shape = SegmentedButtonDefaults.itemShape(1, 2),
                    label = { Text(stringResource(R.string.reader_width_narrow), maxLines = 1) },
                )
            }
        }
    }
}
