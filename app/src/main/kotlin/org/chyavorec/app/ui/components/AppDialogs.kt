package org.chyavorec.app.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

/*
 * Диалогът, листът и менюто са отделни прозорци: композицията им започва с плътността на
 * прозореца (системния мащаб) и губи „Размер на текста“ от ChitalishteRoot. Обвивките
 * по-долу връщат плътността от мястото, откъдето са отворени (с LimitFontScale, ако има).
 */

/** Съдържанието — с [density] (взета извън прозореца). */
@Composable
fun KeepDensity(density: Density, content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalDensity provides density, content = content)

private fun (@Composable () -> Unit).withDensity(density: Density): @Composable () -> Unit = { KeepDensity(density, this) }

/** [AlertDialog] с мащаба на текста от приложението. */
@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
) {
    val density = LocalDensity.current
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton.withDensity(density),
        modifier = modifier,
        dismissButton = dismissButton?.withDensity(density),
        icon = icon?.withDensity(density),
        title = title?.withDensity(density),
        text = text?.withDensity(density),
    )
}

/** [ModalBottomSheet] с мащаба на текста от приложението. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        KeepDensity(density) { content() }
    }
}

/** [DropdownMenu] с мащаба на текста от приложението. */
@Composable
fun AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest, modifier = modifier) {
        KeepDensity(density) { content() }
    }
}
