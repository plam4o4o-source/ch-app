package org.chyavorec.app.ui.screens.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.ui.platform.LocalView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.clickable
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashlightOff
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import org.chyavorec.app.ui.components.ChLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.BookStatusPill
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequest
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.domain.model.SearchField

private fun hasCameraPermission(context: Context) =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

private fun hasCamera(context: Context) = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

private fun openAppSettings(context: Context) {
    runCatching {
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel(key = "scan") { ScanViewModel(it.catalogRepository, it.clock) }
    val result by vm.result.collectAsStateWithLifecycle()
    val catalogStatus by vm.catalogStatus.collectAsStateWithLifecycle()
    val waiting by vm.waiting.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var granted by remember { mutableStateOf(hasCameraPermission(context)) }
    var asked by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        granted = ok
        asked = true
    }
    LaunchedEffect(Unit) { if (!granted) launcher.launch(Manifest.permission.CAMERA) }

    var torch by remember { mutableStateOf(false) }
    // „Въведи номера на ръка“ — без камера (отказано разрешение, няма камера) или етикет, който не се чете.
    var manual by rememberSaveable { mutableStateOf(false) }
    val openManual = { manual = true }
    var hasTorch by remember { mutableStateOf(false) }
    var cameraError by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.scan_title), onBack, actions = {
                if (granted && hasTorch) {
                    IconButton(onClick = { torch = !torch }) {
                        Icon(
                            if (torch) Icons.Outlined.FlashlightOff else Icons.Outlined.FlashlightOn,
                            contentDescription = stringResource(if (torch) R.string.scan_torch_off else R.string.scan_torch_on),
                        )
                    }
                }
            })
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize().background(Color.Black)) {
            // Докато панелът „на ръка“ е отворен, съдържанието под него е скрито за TalkBack.
            Box(Modifier.fillMaxSize().then(if (manual) Modifier.clearAndSetSemantics {} else Modifier)) {
                when {
                    !hasCamera(context) || cameraError -> PermissionMessage(
                        title = stringResource(R.string.scan_camera_error), text = null,
                        primary = null, onPrimary = {}, secondary = null, onSecondary = {},
                        onManual = openManual,
                    )
                    granted -> {
                        CameraPreview(
                            torch = torch,
                            paused = result != null || manual,
                            onCode = vm::onCode,
                            onCamera = { cam -> hasTorch = cam.cameraInfo.hasFlashUnit() },
                            onError = { cameraError = true },
                        )
                        ViewfinderOverlay(
                            when (result) {
                                is ScanResult.Hit -> ViewfinderMode.HIT
                                is ScanResult.Miss -> ViewfinderMode.MISS
                                null -> ViewfinderMode.SCANNING
                            },
                            Modifier.fillMaxSize(),
                        )
                        Column(
                            Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = 24.dp, vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CatalogStatusNote(catalogStatus, waiting, onRetry = vm::retry)
                            Surface(color = Color.Black.copy(alpha = 0.55f), contentColor = Brand.Parchment, shape = RoundedCornerShape(14.dp)) {
                                Text(
                                    stringResource(R.string.scan_hint),
                                    style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            // Вторично действие: етикет, който камерата не чете (смачкан, избелял).
                            Surface(color = Color.Black.copy(alpha = 0.55f), contentColor = Brand.GoldLight, shape = RoundedCornerShape(50)) {
                                ManualEntryButton(openManual)
                            }
                        }
                    }
                    else -> PermissionMessage(
                        title = stringResource(R.string.scan_permission_title),
                        text = stringResource(R.string.scan_permission_text),
                        primary = stringResource(R.string.scan_permission_grant),
                        onPrimary = { launcher.launch(Manifest.permission.CAMERA) },
                        // След отказ системата може да не покаже диалога отново — пътят е през настройките.
                        secondary = if (asked) stringResource(R.string.scan_permission_settings) else null,
                        onSecondary = { openAppSettings(context) },
                        onManual = openManual,
                    )
                }
                // Без камера: номерът, въведен на ръка, чака каталога (без връзка и без кеш) — както сканираният.
                if (!granted || !hasCamera(context) || cameraError) {
                    if (waiting != null) {
                        Column(
                            Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = 24.dp, vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) { CatalogStatusNote(catalogStatus, waiting, onRetry = vm::retry) }
                    }
                }
            }
            // Панел над камерата/съобщението (не отделен прозорец) — „Назад“ го затваря,
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
 * Над подсказката: каталогът се зарежда; не е достъпен (без връзка и без кеш) — с
 * „Опитай отново“ (при връщане на връзката се опитва и сам); запазеният код, който чака каталога.
 */
@Composable
private fun CatalogStatusNote(status: ScanCatalogStatus, waiting: ScanTarget?, onRetry: () -> Unit) {
    when (status) {
        ScanCatalogStatus.READY -> Unit
        ScanCatalogStatus.LOADING -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChLoadingIndicator(Modifier.size(22.dp), contentDescription = null, color = Brand.GoldLight)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scan_catalog_loading), color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(8.dp))
        }
        ScanCatalogStatus.UNAVAILABLE -> {
            Surface(color = Color.Black.copy(alpha = 0.7f), contentColor = Brand.Parchment, shape = RoundedCornerShape(14.dp)) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(R.string.scan_catalog_unavailable),
                        style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedButton(onClick = onRetry) { Text(stringResource(R.string.action_retry), color = Brand.GoldLight) }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
    if (waiting != null && status != ScanCatalogStatus.READY) {
        Text(
            stringResource(R.string.scan_code_waiting, codeLabel(waiting)),
            color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PermissionMessage(
    title: String, text: String?,
    primary: String?, onPrimary: () -> Unit,
    secondary: String?, onSecondary: () -> Unit,
    onManual: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = Brand.GoldLight, modifier = Modifier.size(56.dp))
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, color = Brand.Parchment, textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
        if (text != null) {
            Spacer(Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium, color = Brand.Parchment.copy(alpha = 0.85f), textAlign = TextAlign.Center)
        }
        if (primary != null) {
            Spacer(Modifier.height(20.dp))
            Button(onClick = onPrimary) { Text(primary) }
        }
        if (secondary != null) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onSecondary) { Text(secondary, color = Brand.GoldLight) }
        }
        // Без камера — пак може да се потърси книга: номерът от етикета или ISBN-ът, на ръка.
        Spacer(Modifier.height(if (primary == null) 20.dp else 8.dp))
        if (primary == null) {
            Button(onClick = onManual) {
                Icon(Icons.Outlined.Keyboard, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scan_manual))
            }
        } else {
            CompositionLocalProvider(LocalContentColor provides Brand.GoldLight) { ManualEntryButton(onManual) }
        }
    }
}

@Composable
private fun ManualEntryButton(onClick: () -> Unit) {
    TextButton(onClick = onClick, colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)) {
        Icon(Icons.Outlined.Keyboard, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.scan_manual))
    }
}

/**
 * „Въведи номера на ръка“: ISBN (под баркода) или инвентарният номер от етикета — със
 * цифрова клавиатура; справката е същата като при сканиране. Нещо, което не е нито
 * едното, нито другото, се казва още тук (без лист „няма я“). Панел отдолу върху
 * затъмнения екран; докосване извън него го затваря.
 */
@Composable
private fun BoxScope.ManualEntryPanel(backProgress: Float, onSubmit: (String) -> Unit, onDismiss: () -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    // При намалено движение жестът „назад“ не мащабира панела — само го затваря.
    val p = if (rememberReducedMotion()) 0f else backProgress
    var invalid by rememberSaveable { mutableStateOf(false) }
    val focus = remember { FocusRequester() }
    val submit: () -> Unit = {
        if (ScanCodes.resolve(text) is ScanTarget.Unknown) {
            invalid = true
        } else {
            onSubmit(text.trim())
        }
    }
    // Затъмнението е само за докосване — TalkBack не го вижда (затваря се с „Отказ“).
    Box(
        Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.6f * (1f - p * 0.5f)))
            .clickable(interactionSource = null, indication = null, onClick = onDismiss)
            .clearAndSetSemantics {},
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
            Spacer(Modifier.height(6.dp))
            Text(stringResource(R.string.scan_manual_text), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = text,
                onValueChange = { v ->
                    // Цифри, тирета и интервали (както е отпечатан ISBN-ът); X — последната цифра на стар ISBN.
                    text = v.filter { it.isDigit() || it == '-' || it == ' ' || it == 'X' || it == 'x' }.take(24)
                    invalid = false
                },
                label = { Text(stringResource(R.string.scan_manual_label)) },
                singleLine = true,
                isError = invalid,
                supportingText = if (invalid) { { Text(stringResource(R.string.scan_manual_invalid)) } } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { submit() }),
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
                Button(onClick = submit, enabled = text.isNotBlank()) { Text(stringResource(R.string.scan_manual_search)) }
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
    Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            // Без sharedElementKey: листът е отделен прозорец и се затваря преди прехода —
            // корицата няма откъде да „прелети“.
            BookCover(b, width = 84.dp)
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
                    BookStatusPill(b.status)
                    if (hit.copies.size > 1) {
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
