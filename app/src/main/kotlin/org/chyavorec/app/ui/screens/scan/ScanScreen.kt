package org.chyavorec.app.ui.screens.scan

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.CopyStatus
import org.chyavorec.app.ui.components.InlineNotice
import org.chyavorec.app.ui.components.limitedInput
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequest
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.domain.model.SearchField

/**
 * Скенер: скенерът на Google (Google Play услуги) се отваря веднага над приложението, а
 * разчетеният код се търси тук — лист с книгата или „няма я“. След „Назад“ от скенера
 * екранът остава с „Сканирай баркод“ и „Въведи номера на ръка“; без Play услуги (или
 * докато модулът на скенера се изтегля) — направо ръчното въвеждане с обяснение.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel(key = "scan") { ScanViewModel(it.catalogRepository, it.scanner) }
    val result by vm.result.collectAsStateWithLifecycle()
    val catalogStatus by vm.catalogStatus.collectAsStateWithLifecycle()
    val waiting by vm.waiting.collectAsStateWithLifecycle()
    val scanner by vm.scannerState.collectAsStateWithLifecycle()
    // Прозорецът на скенера се показва над activity-то.
    val activity = LocalActivity.current ?: LocalContext.current
    val haptic = LocalHapticFeedback.current

    // „Въведи номера на ръка“ — етикет, който скенерът не чете, или без скенер.
    var manual by rememberSaveable { mutableStateOf(false) }
    val openManual = { manual = true }
    LaunchedEffect(Unit) { vm.scan(activity, auto = true) }
    // Скенерът не е достъпен — ръчното въвеждане се отваря само (веднъж; затвореното остава затворено).
    var offeredManual by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(scanner) {
        if (scanner == ScannerState.UNAVAILABLE && !offeredManual) {
            offeredManual = true
            manual = true
        }
    }

    // Тактилен отговор: потвърждение при намерена книга (CONFIRM от Android 11, иначе
    // „дълго натискане“), отказ при „няма я“.
    val view = LocalView.current
    LaunchedEffect(result) {
        when (result) {
            is ScanResult.Hit ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                else haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            is ScanResult.Miss -> haptic.performHapticFeedback(HapticFeedbackType.Reject)
            null -> Unit
        }
    }

    Scaffold(topBar = { BackTopBar(stringResource(R.string.scan_title), onBack) }) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            // Докато панелът „на ръка“ е отворен, съдържанието под него е скрито за TalkBack.
            Box(Modifier.fillMaxSize().then(if (manual) Modifier.clearAndSetSemantics {} else Modifier)) {
                ScanIntro(
                    unavailable = scanner == ScannerState.UNAVAILABLE,
                    opening = scanner == ScannerState.OPEN,
                    catalogStatus = catalogStatus,
                    waiting = waiting,
                    onScan = { vm.scan(activity) },
                    onManual = openManual,
                    onRetryCatalog = vm::retry,
                )
            }
            // Панел над екрана (не отделен прозорец) — „Назад“ го затваря,
            // с предварителен изглед на жеста (както системните листове).
            var backProgress by remember { mutableFloatStateOf(0f) }
            PredictiveBackHandler(enabled = manual) { events ->
                try {
                    events.collect { backProgress = it.progress }
                    manual = false
                } finally {
                    backProgress = 0f
                }
            }
            if (manual) {
                ManualEntryPanel(
                    backProgress = backProgress,
                    note = if (scanner == ScannerState.UNAVAILABLE) stringResource(R.string.scan_unavailable) else null,
                    onSubmit = { code -> manual = false; vm.onManual(code) },
                    onDismiss = { manual = false },
                )
            }
        }
    }

    result?.let { r ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(onDismissRequest = { vm.dismiss() }, sheetState = sheetState) {
            when (r) {
                is ScanResult.Hit -> HitSheet(
                    r,
                    onOpen = { vm.dismiss(); navigate(Routes.book(r.book.inv)) },
                    onClose = { vm.dismiss() },
                )
                is ScanResult.Miss -> MissSheet(
                    r,
                    onSearch = {
                        val (text, field) = when (val t = r.target) {
                            is ScanTarget.Isbn -> t.isbn to SearchField.ISBN
                            is ScanTarget.Inventory -> t.inv.toString() to SearchField.INVENTORY
                            is ScanTarget.Unknown -> t.text to SearchField.ALL
                        }
                        CatalogSearchRequests.pending.value = CatalogSearchRequest(text, field)
                        vm.dismiss()
                        navigate(Routes.CATALOG)
                    },
                    onClose = { vm.dismiss() },
                )
            }
        }
    }
}

/**
 * Екранът под скенера: какво се сканира и бутон за нов опит (или защо скенерът не е
 * достъпен), „Въведи номера на ръка“ и състоянието на каталога.
 */
@Composable
private fun ScanIntro(
    unavailable: Boolean,
    opening: Boolean,
    catalogStatus: ScanCatalogStatus,
    waiting: ScanTarget?,
    onScan: () -> Unit,
    onManual: () -> Unit,
    onRetryCatalog: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(Icons.Outlined.QrCodeScanner, contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(if (unavailable) R.string.scan_unavailable_title else R.string.scan_intro_title),
            style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(if (unavailable) R.string.scan_unavailable else R.string.scan_intro_text),
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onScan, enabled = !opening, modifier = Modifier.heightIn(min = 48.dp)) {
            Icon(Icons.Outlined.QrCodeScanner, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(if (unavailable) R.string.action_retry else R.string.scan_action))
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onManual, modifier = Modifier.heightIn(min = 48.dp)) {
            Icon(Icons.Outlined.Keyboard, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.scan_manual))
        }
        Spacer(Modifier.height(16.dp))
        CatalogStatusNote(catalogStatus, waiting, onRetry = onRetryCatalog)
    }
}

/**
 * Каталогът се зарежда; не е достъпен (без връзка и без кеш) — с „Опитай отново“ (при
 * връщане на връзката се опитва и сам); запазеният код, който чака каталога.
 */
@Composable
private fun CatalogStatusNote(status: ScanCatalogStatus, waiting: ScanTarget?, onRetry: () -> Unit) {
    when (status) {
        ScanCatalogStatus.READY -> Unit
        ScanCatalogStatus.LOADING -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChLoadingIndicator(Modifier.size(22.dp), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scan_catalog_loading), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(8.dp))
        }
        ScanCatalogStatus.UNAVAILABLE -> {
            InlineNotice(
                Icons.Outlined.CloudOff, stringResource(R.string.scan_catalog_unavailable),
                actionLabel = stringResource(R.string.action_retry), onAction = onRetry,
            )
            Spacer(Modifier.height(8.dp))
        }
    }
    if (waiting != null && status != ScanCatalogStatus.READY) {
        Text(
            stringResource(R.string.scan_code_waiting, codeLabel(waiting)),
            color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center,
        )
    }
}

/**
 * „Въведи номера на ръка“: ISBN (под баркода) или инвентарният номер от етикета — със
 * цифрова клавиатура; справката е същата като при сканиране. Нещо, което не е нито
 * едното, нито другото, се казва още тук (без лист „няма я“). Панел отдолу върху
 * затъмнения екран; докосване извън него го затваря. [note] — защо скенерът не е достъпен.
 */
@Composable
private fun BoxScope.ManualEntryPanel(backProgress: Float, note: String?, onSubmit: (String) -> Unit, onDismiss: () -> Unit) {
    val text = rememberTextFieldState()
    // Цифри, тирета и интервали (както е отпечатан ISBN-ът); X — последната цифра на стар ISBN.
    val input = remember { limitedInput(24, keep = { it.isDigit() || it == '-' || it == ' ' || it == 'X' || it == 'x' }) }
    // При намалено движение жестът „назад“ не мащабира панела — само го затваря.
    val p = if (rememberReducedMotion()) 0f else backProgress
    // Отхвърленият номер: грешката се вижда, докато не бъде поправен.
    var invalidFor by rememberSaveable { mutableStateOf<String?>(null) }
    val invalid = invalidFor != null && invalidFor == text.text.toString()
    val focus = remember { FocusRequester() }
    val submit: () -> Unit = {
        val value = text.text.toString()
        if (ScanCodes.resolve(value) is ScanTarget.Unknown) {
            invalidFor = value
        } else {
            onSubmit(value.trim())
        }
    }
    // Затъмнението е само за докосване — TalkBack не го вижда (затваря се с „Отказ“): семантиката
    // се изчиства ПРЕДИ clickable, иначе остава безименна област с действие „докосни“.
    Box(
        Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.6f * (1f - p * 0.5f)))
            .clearAndSetSemantics {}
            .clickable(interactionSource = null, indication = null, onClick = onDismiss),
    )
    val title = stringResource(R.string.scan_manual)
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().widthIn(max = 640.dp)
            // Като ModalBottomSheet при жест „назад“: свива се леко към долния ръб.
            .graphicsLayer {
                if (p > 0f) {
                    scaleX = 1f - p * (48.dp.toPx() / size.width).coerceAtMost(0.1f)
                    scaleY = 1f - p * (24.dp.toPx() / size.height).coerceAtMost(0.1f)
                    transformOrigin = TransformOrigin(0.5f, 1f)
                }
            }
            .semantics { paneTitle = title; isTraversalGroup = true },
    ) {
        Column(Modifier.navigationBarsPadding().imePadding().padding(horizontal = 24.dp, vertical = 20.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            if (note != null) {
                Spacer(Modifier.height(6.dp))
                Row {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = LocalExtendedColors.current.gold)
                    Spacer(Modifier.width(12.dp))
                    Text(note, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(stringResource(R.string.scan_manual_text), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                state = text,
                inputTransformation = input,
                label = { Text(stringResource(R.string.scan_manual_label)) },
                lineLimits = TextFieldLineLimits.SingleLine,
                isError = invalid,
                supportingText = if (invalid) { { Text(stringResource(R.string.scan_manual_invalid)) } } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Search),
                onKeyboardAction = { submit() },
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
                Button(onClick = submit, enabled = text.text.isNotBlank()) { Text(stringResource(R.string.scan_manual_search)) }
            }
        }
    }
    // Клавиатурата — веднага.
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
}

@Composable
private fun HitSheet(hit: ScanResult.Hit, onOpen: () -> Unit, onClose: () -> Unit) {
    val b = hit.book
    val availableCopies = hit.copies.count { it.available }
    // Този екземпляр не е на рафта, а други са — както на страницата на книгата.
    val othersOnShelf = if (b.available) 0 else hit.copies.count { it.available && it.inv != b.inv }
    Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            // Без sharedElementKey: листът е отделен прозорец и се затваря преди прехода —
            // корицата няма откъде да „прелети“.
            BookCover(b, width = 84.dp, decorative = true)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(b.title, style = MaterialTheme.typography.titleLarge, maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.semantics { heading() })
                if (b.author.isNotBlank()) {
                    Text(b.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Text(
                    listOf(b.year, b.publisher).filter { it.isNotBlank() }.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1,
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CopyStatus(b.status, othersOnShelf)
                    if (othersOnShelf == 0 && hit.copies.size > 1) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            pluralStringResource(R.plurals.book_copies, hit.copies.size, hit.copies.size) + ", " +
                                pluralStringResource(R.plurals.book_available_copies, availableCopies, availableCopies),
                            style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(codeLabel(hit.target), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.action_close)) }
            Button(onClick = onOpen, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.scan_open_book)) }
        }
    }
}

@Composable
private fun MissSheet(miss: ScanResult.Miss, onSearch: () -> Unit, onClose: () -> Unit) {
    Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Outlined.SearchOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(10.dp))
        Text(stringResource(R.string.scan_not_found_title), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.scan_not_found_text, codeLabel(miss.target)),
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.action_close)) }
            FilledTonalButton(onClick = onSearch, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.scan_search_catalog)) }
        }
    }
}

@Composable
private fun codeLabel(target: ScanTarget): String = when (target) {
    is ScanTarget.Isbn -> stringResource(R.string.scan_code_isbn, target.isbn)
    is ScanTarget.Inventory -> stringResource(R.string.scan_code_inv, target.inv.toString())
    is ScanTarget.Unknown -> "„" + target.text.take(60) + "“"
}
