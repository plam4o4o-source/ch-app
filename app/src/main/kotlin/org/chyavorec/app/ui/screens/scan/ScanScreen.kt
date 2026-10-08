package org.chyavorec.app.ui.screens.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashlightOff
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.BookStatusPill
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequest
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.core.Outcome
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.data.catalog.CatalogSearchEngine
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.SearchField
import java.util.concurrent.Executors

/** Резултат от сканиране: книга от каталога или „няма я“. */
sealed interface ScanResult {
    data class Hit(val book: CatalogBook, val copies: List<CatalogBook>, val target: ScanTarget) : ScanResult
    data class Miss(val target: ScanTarget) : ScanResult
}

/**
 * Скенер на баркодове: разчита ISBN (EAN-13 с 978/979), инвентарен номер
 * (Code 39/128 етикет на библиотеката) или QR с адрес на каталога и търси в
 * каталога в паметта. Всичко става на устройството — кадрите не се записват.
 */
class ScanViewModel(private val c: AppContainer) : ViewModel() {
    private val _result = MutableStateFlow<ScanResult?>(null)
    val result: StateFlow<ScanResult?> = _result.asStateFlow()
    private val _catalogReady = MutableStateFlow(false)
    val catalogReady: StateFlow<Boolean> = _catalogReady.asStateFlow()

    @Volatile private var engine: CatalogSearchEngine? = null
    @Volatile private var lastCode: String? = null
    @Volatile private var lastAt: Long = 0L
    @Volatile private var busy = false

    init {
        viewModelScope.launch {
            val synced = c.catalogRepository.inMemory() ?: c.catalogRepository.cached()
                ?: (c.catalogRepository.catalog(false) as? Outcome.Success)?.value
            engine = synced?.data
            _catalogReady.value = engine != null
        }
    }

    /** Извиква се от анализатора (фонова нишка). Еднакъв код се приема най-много веднъж на 3 s. */
    fun onCode(raw: String) {
        if (raw.isBlank() || busy || _result.value != null) return
        val now = c.clock.now().toEpochMilli()
        if (raw == lastCode && now - lastAt < DEBOUNCE_MS) return
        lastCode = raw; lastAt = now
        val eng = engine ?: return
        busy = true
        try {
            val target = ScanCodes.resolve(raw)
            val book = when (target) {
                is ScanTarget.Isbn -> eng.findByIsbn(target.isbn)
                is ScanTarget.Inventory -> eng.findByInv(target.inv)
                is ScanTarget.Unknown -> null
            }
            _result.value = if (book != null) {
                val copies = when (target) {
                    is ScanTarget.Isbn -> eng.allByIsbn(target.isbn).ifEmpty { listOf(book) }
                    else -> listOf(book) + eng.copiesOf(book)
                }
                ScanResult.Hit(book, copies, target)
            } else {
                ScanResult.Miss(target)
            }
        } finally {
            busy = false
        }
    }

    fun dismiss() {
        _result.value = null
        // Същият код може да се сканира отново след кратка пауза (не веднага, докато листът се затваря).
        lastAt = c.clock.now().toEpochMilli()
    }

    companion object { const val DEBOUNCE_MS = 3_000L }
}

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
    val vm = appViewModel(key = "scan") { ScanViewModel(it) }
    val result by vm.result.collectAsStateWithLifecycle()
    val catalogReady by vm.catalogReady.collectAsStateWithLifecycle()
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
    var hasTorch by remember { mutableStateOf(false) }
    var cameraError by remember { mutableStateOf(false) }

    // Тактилен отговор: потвърждение при намерена книга, отказ при „няма я“.
    LaunchedEffect(result) {
        when (result) {
            is ScanResult.Hit -> haptic.performHapticFeedback(HapticFeedbackType.Confirm)
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
            when {
                !hasCamera(context) || cameraError -> PermissionMessage(
                    title = stringResource(R.string.scan_camera_error), text = null,
                    primary = null, onPrimary = {}, secondary = null, onSecondary = {},
                )
                granted -> {
                    CameraPreview(
                        torch = torch,
                        paused = result != null,
                        onCode = vm::onCode,
                        onCamera = { cam -> hasTorch = cam.cameraInfo.hasFlashUnit() },
                        onError = { cameraError = true },
                    )
                    ViewfinderOverlay(Modifier.fillMaxSize())
                    Column(
                        Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = 24.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (!catalogReady) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(Modifier.size(16.dp), color = Brand.GoldLight, strokeWidth = 2.dp)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.scan_catalog_loading), color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium)
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        Surface(color = Color.Black.copy(alpha = 0.55f), contentColor = Brand.Parchment, shape = RoundedCornerShape(14.dp)) {
                            Text(
                                stringResource(R.string.scan_hint),
                                style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            )
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

@Composable
private fun PermissionMessage(
    title: String, text: String?,
    primary: String?, onPrimary: () -> Unit,
    secondary: String?, onSecondary: () -> Unit,
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
    }
}

/**
 * CameraX преглед + анализ на кадрите с ML Kit. Анализаторът подава всеки
 * разчетен баркод към [onCode]; докато [paused] е истина (отворен лист с
 * резултат), кадрите се пропускат. Скенерът и изпълнителят се затварят при
 * напускане на екрана.
 */
@Composable
private fun CameraPreview(
    torch: Boolean,
    paused: Boolean,
    onCode: (String) -> Unit,
    onCamera: (Camera) -> Unit,
    onError: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context).apply { implementationMode = PreviewView.ImplementationMode.COMPATIBLE } }
    // Ако разпознаването не може да се създаде (липсващ компонент, стар телефон),
    // показваме „камерата не е достъпна“ вместо срив на приложението.
    val scanner: BarcodeScanner? = remember {
        runCatching {
            BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_EAN_13, Barcode.FORMAT_EAN_8, Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_CODE_39, Barcode.FORMAT_QR_CODE,
                    )
                    .build(),
            )
        }.getOrNull()
    }
    if (scanner == null) {
        LaunchedEffect(Unit) { onError() }
        return
    }
    val executor = remember { Executors.newSingleThreadExecutor() }
    // Най-новите стойности за анализатора (той е създаден веднъж).
    val pausedRef = remember { arrayOf(paused) }
    pausedRef[0] = paused
    val onCodeRef = remember { arrayOf(onCode) }
    onCodeRef[0] = onCode
    var camera by remember { mutableStateOf<Camera?>(null) }

    DisposableEffect(lifecycleOwner) {
        var provider: ProcessCameraProvider? = null
        val future = runCatching { ProcessCameraProvider.getInstance(context) }.getOrNull()
        if (future == null) onError() else future.addListener({
            try {
                val p = future.get()
                provider = p
                val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                analysis.setAnalyzer(executor) { proxy -> analyze(proxy, scanner, pausedRef, onCodeRef) }
                p.unbindAll()
                val cam = p.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                camera = cam
                onCamera(cam)
            } catch (_: Throwable) {
                onError()
            }
        }, ContextCompat.getMainExecutor(context))
        onDispose {
            runCatching { provider?.unbindAll() }
            runCatching { scanner.close() }
            executor.shutdown()
        }
    }
    LaunchedEffect(torch, camera) { camera?.let { runCatching { it.cameraControl.enableTorch(torch) } } }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun analyze(proxy: ImageProxy, scanner: BarcodeScanner, pausedRef: Array<Boolean>, onCodeRef: Array<(String) -> Unit>) {
    val media = proxy.image
    if (media == null || pausedRef[0]) {
        proxy.close()
        return
    }
    val image = runCatching { InputImage.fromMediaImage(media, proxy.imageInfo.rotationDegrees) }.getOrNull()
    if (image == null) {
        proxy.close()
        return
    }
    runCatching { scanner.process(image) }.getOrElse {
        proxy.close()
        return
    }
        .addOnSuccessListener { codes ->
            if (pausedRef[0]) return@addOnSuccessListener
            // Най-голямото (най-близкото до центъра/най-четливото) първо.
            val value = codes.sortedByDescending { it.boundingBox?.let { b -> b.width() * b.height() } ?: 0 }
                .firstNotNullOfOrNull { it.rawValue?.takeIf { v -> v.isNotBlank() } }
            if (value != null) onCodeRef[0](value)
        }
        .addOnCompleteListener { proxy.close() }
}

/** Затъмнен фон с прозорец със заоблени ъгли и златни „скоби“ по ъглите. */
@Composable
private fun ViewfinderOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width * 0.78f
        val h = (w * 0.62f).coerceAtMost(size.height * 0.5f)
        val left = (size.width - w) / 2f
        val top = (size.height - h) / 2f - size.height * 0.06f
        val radius = 22.dp.toPx()
        val window = Path().apply { addRoundRect(androidx.compose.ui.geometry.RoundRect(left, top, left + w, top + h, CornerRadius(radius))) }
        clipPath(window, ClipOp.Difference) { drawRect(Color.Black.copy(alpha = 0.55f)) }
        drawRoundRect(
            Brand.Gold.copy(alpha = 0.35f), Offset(left, top), Size(w, h), CornerRadius(radius),
            style = Stroke(1.5.dp.toPx()),
        )
        // Златни ъгли
        val len = 28.dp.toPx()
        val stroke = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
        val corner = Path()
        fun arm(x0: Float, y0: Float, dx: Float, dy: Float) {
            corner.reset()
            corner.moveTo(x0 + dx * len, y0)
            corner.lineTo(x0 + dx * radius, y0)
            corner.quadraticTo(x0, y0, x0, y0 + dy * radius)
            corner.lineTo(x0, y0 + dy * len)
            drawPath(corner, Brand.Gold, style = stroke)
        }
        arm(left, top, 1f, 1f)
        arm(left + w, top, -1f, 1f)
        arm(left, top + h, 1f, -1f)
        arm(left + w, top + h, -1f, -1f)
    }
}

@Composable
private fun HitSheet(hit: ScanResult.Hit, onOpen: () -> Unit, onClose: () -> Unit) {
    val b = hit.book
    val availableCopies = hit.copies.count { it.available }
    Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp)) {
        Row(verticalAlignment = Alignment.Top) {
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
