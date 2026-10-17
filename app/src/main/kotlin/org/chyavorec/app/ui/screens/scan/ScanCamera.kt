package org.chyavorec.app.ui.screens.scan

import android.os.SystemClock
import android.hardware.display.DisplayManager
import android.view.View
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

/**
 * CameraX преглед + анализ на кадрите със ZXing ([BarcodeAnalyzer]). Анализаторът
 * подава всеки разчетен баркод към [onCode]. Докато [paused] е истина (отворен лист
 * с резултат) или екранът не е на преден план, анализаторът е откачен — камерата
 * само показва образа. Изпълнителят се спира при напускане на екрана.
 *
 * Прегледът и анализът са 4:3 (едно и също зрително поле — нужно за изрязването до
 * прозореца на визьора); анализът е около 1280×960 вместо най-голямата резолюция.
 */
@Composable
internal fun CameraPreview(
    torch: Boolean,
    paused: Boolean,
    onCode: (String) -> Unit,
    onCamera: (Camera) -> Unit,
    onError: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context).apply { implementationMode = PreviewView.ImplementationMode.COMPATIBLE } }
    val executor = remember { Executors.newSingleThreadExecutor() }
    // Най-новите стойности за анализатора (той е създаден веднъж).
    val pausedRef = remember { arrayOf(paused) }
    pausedRef[0] = paused
    val onCodeRef = remember { arrayOf(onCode) }
    onCodeRef[0] = onCode
    // Разпознаването е чист Kotlin/Java (ZXing) — без native библиотеки и модели.
    val analyzer = remember { BarcodeAnalyzer(BarcodeDecoder(), previewView, { pausedRef[0] }, { onCodeRef[0](it) }) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var analysis by remember { mutableStateOf<ImageAnalysis?>(null) }

    DisposableEffect(lifecycleOwner) {
        var provider: ProcessCameraProvider? = null
        var useCase: ImageAnalysis? = null
        // Екранът сам обработва завъртането (configChanges): завъртането на анализа се
        // обновява ръчно — по него кадърът се изрязва до прозореца на визьора.
        val displays = context.getSystemService(DisplayManager::class.java)
        val displayListener = object : DisplayManager.DisplayListener {
            override fun onDisplayAdded(displayId: Int) = Unit
            override fun onDisplayRemoved(displayId: Int) = Unit
            override fun onDisplayChanged(displayId: Int) {
                val display = previewView.display ?: return
                if (display.displayId == displayId) useCase?.targetRotation = display.rotation
            }
        }
        val future = runCatching { ProcessCameraProvider.getInstance(context) }.getOrNull()
        if (future == null) onError() else future.addListener({
            try {
                val p = future.get()
                provider = p
                val ratio = AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
                val preview = Preview.Builder()
                    .setResolutionSelector(ResolutionSelector.Builder().setAspectRatioStrategy(ratio).build())
                    .build()
                    .also { it.surfaceProvider = previewView.surfaceProvider }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setResolutionSelector(
                        ResolutionSelector.Builder()
                            .setAspectRatioStrategy(ratio)
                            .setResolutionStrategy(
                                ResolutionStrategy(
                                    android.util.Size(1280, 960),
                                    ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER,
                                ),
                            )
                            .build(),
                    )
                    // Докато се разчита кадър, идващите се изхвърлят (остава само последният).
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                previewView.display?.let { imageAnalysis.targetRotation = it.rotation }
                useCase = imageAnalysis
                p.unbindAll()
                val cam = p.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
                camera = cam
                analysis = imageAnalysis
                displays?.registerDisplayListener(displayListener, null)
                onCamera(cam)
            } catch (_: Throwable) {
                onError()
            }
        }, ContextCompat.getMainExecutor(context))
        onDispose {
            runCatching { displays?.unregisterDisplayListener(displayListener) }
            runCatching { useCase?.clearAnalyzer() }
            runCatching { provider?.unbindAll() }
            executor.shutdown()
        }
    }
    LaunchedEffect(torch, camera) { camera?.let { runCatching { it.cameraControl.enableTorch(torch) } } }
    // Анализ само докато се търси и екранът е на преден план (не при отворен резултат,
    // диалог или друго приложение отгоре) — иначе кадрите не стигат до анализатора.
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()
    val active = !paused && lifecycleState.isAtLeast(Lifecycle.State.RESUMED)
    LaunchedEffect(analysis, active) {
        val a = analysis ?: return@LaunchedEffect
        runCatching { if (active) a.setAnalyzer(executor, analyzer) else a.clearAnalyzer() }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

/**
 * Разчита кадрите: не повече от ~6 в секунда ([DecodeThrottle]), само областта на
 * прозореца на визьора ([cropForWindow]) — копира се само нейната част от яркостния
 * (Y) канал. Кадърът винаги се затваря (иначе CameraX спира да подава нови), а всяка
 * грешка при разчитането само пропуска кадъра.
 */
private class BarcodeAnalyzer(
    private val decoder: BarcodeDecoder,
    private val previewView: View,
    private val paused: () -> Boolean,
    private val onCode: (String) -> Unit,
) : ImageAnalysis.Analyzer {
    private val throttle = DecodeThrottle(intervalMs = 150, minGapMs = 50)
    // Преизползван буфер за яркостния канал (без нов масив за всеки кадър).
    private var frame = ByteArray(0)

    override fun analyze(proxy: ImageProxy) {
        try {
            if (paused() || !throttle.tryStart(SystemClock.elapsedRealtime())) return
            try {
                val value = decode(proxy)
                if (value != null && !paused()) onCode(value)
            } finally {
                throttle.finish(SystemClock.elapsedRealtime())
            }
        } catch (_: Throwable) {
            // Повреден/непълен кадър — пропуска се; следващият идва веднага.
        } finally {
            proxy.close()
        }
    }

    private fun decode(proxy: ImageProxy): String? {
        // YUV_420_888 (по подразбиране за ImageAnalysis): равнина 0 е Y, по 1 байт на пиксел.
        val plane = proxy.planes.firstOrNull() ?: return null
        val width = proxy.width
        val height = proxy.height
        val rowStride = plane.rowStride
        val rotation = proxy.imageInfo.rotationDegrees
        // Размерът на изгледа (прост int, четенето от тази нишка е безопасно).
        val area = cropForWindow(width, height, rotation, previewView.width, previewView.height)
            ?: CropRect(0, 0, width, height)
        // Само редовете на областта; последният — до десния ѝ край.
        val buffer = plane.buffer
        val start = area.top * rowStride
        val length = (area.height - 1) * rowStride + area.left + area.width
        if (start + length > buffer.limit()) return null
        buffer.position(start)
        if (frame.size < length) frame = ByteArray(length)
        buffer.get(frame, 0, length)
        return decoder.decode(frame, width, area.height, rowStride, rotation, area.copy(top = 0))
    }
}
