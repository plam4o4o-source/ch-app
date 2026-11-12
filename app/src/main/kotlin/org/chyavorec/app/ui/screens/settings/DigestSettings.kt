package org.chyavorec.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.appFilterChipColors
import org.chyavorec.app.util.udcSectionName
import org.chyavorec.domain.model.FavouriteSections
import org.chyavorec.domain.model.UdcSections

/**
 * „Седмичен преглед“ и „Утре“ в Настройки › Известия: превключвател, а при включен преглед —
 * „Любими раздели“ (чипове) и превключвател за напомнянето „Утре“. Без състояние — отварянето на
 * разрешението за известия (само при включване) и записът са в [NotificationSettingsScreen].
 */
@Composable
internal fun DigestSettingsRows(
    digest: Boolean,
    onDigest: (Boolean) -> Unit,
    sections: Set<String>,
    departments: List<String>,
    onToggleSection: (String) -> Unit,
    tomorrow: Boolean,
    onTomorrow: (Boolean) -> Unit,
) {
    SwitchItem(stringResource(R.string.digest_switch_title), stringResource(R.string.digest_switch_desc), digest, onChange = onDigest)
    if (digest) {
        Text(
            stringResource(R.string.digest_sections_title), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp).semantics { heading() },
        )
        Text(
            stringResource(R.string.digest_sections_desc), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 8.dp),
        )
        DigestSectionChips(sections, departments, onToggleSection, Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
    SwitchItem(stringResource(R.string.digest_tomorrow_title), stringResource(R.string.digest_tomorrow_desc), tomorrow, onChange = onTomorrow)
}

/** Разделите на УДК и отделите от каталога като чипове; избраните ограничават „Нови на рафта“. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DigestSectionChips(selected: Set<String>, departments: List<String>, onToggle: (String) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        UdcSections.names.keys.forEach { section ->
            val key = FavouriteSections.udc(section)
            FilterChip(key in selected, onClick = { onToggle(key) }, label = { Text(udcSectionName(section)) }, colors = appFilterChipColors())
        }
        departments.forEach { dep ->
            val key = FavouriteSections.department(dep)
            FilterChip(key in selected, onClick = { onToggle(key) }, label = { Text(dep) }, colors = appFilterChipColors())
        }
    }
}
