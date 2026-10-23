package org.chyavorec.app.ui.components

import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable

/**
 * Цветовете на филтрите (FilterChip): избраният е в златния контейнер — като
 * индикатора в долната лента, а не в розовия secondaryContainer по подразбиране.
 */
@Composable
fun appFilterChipColors(): SelectableChipColors {
    val c = MaterialTheme.colorScheme
    return FilterChipDefaults.filterChipColors(
        selectedContainerColor = c.primaryContainer,
        selectedLabelColor = c.onPrimaryContainer,
        selectedLeadingIconColor = c.onPrimaryContainer,
        selectedTrailingIconColor = c.onPrimaryContainer,
    )
}
