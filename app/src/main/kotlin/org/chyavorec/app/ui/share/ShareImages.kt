package org.chyavorec.app.ui.share

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Precision
import coil3.size.Scale
import coil3.toBitmap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.R
import org.chyavorec.app.util.Intents
import org.chyavorec.data.site.SiteImageUrls
import java.io.File

/**
 * Картинки за споделяне: карта (събитие, книга, новина, слайд от „Моята читателска година“),
 * нарисувана с Compose извън екрана в точен размер, записана като PNG в `cacheDir/share/` и
 * подадена на избраното приложение през FileProvider. Всичко става на телефона — картинката
 * излиза от него само когато читателят избере къде да я сподели.
 */
enum class ShareFormat(val widthPx: Int, val heightPx: Int) {
    /** Instagram/Facebook „история“ — 9:16. */
    STORY(1080, 1920),

    /** Публикация в лентата — 4:5. */
    FEED(1080, 1350);

    /** Картата се подрежда в „картови“ dp: 360 dp ширина при плътност [DENSITY] — еднаква на всеки телефон. */
    val widthDp get() = (widthPx / DENSITY).dp
    val heightDp get() = (heightPx / DENSITY).dp

    companion object {
        const val DENSITY = 3f
    }
}

/** Файловете за споделяне в кеша: пишат се тук и се чистят след ден (приложението-получател може да чете по-късно). */
object ShareFiles {
    const val DIR = "share"
    const val MAX_AGE_MS = 24L * 60 * 60 * 1000
    /** Най-много толкова картинки остават в кеша (най-новите). */
    const val KEEP = 6

    fun dir(context: Context): File = File(context.cacheDir, DIR)

    /** Записва [bitmap] като PNG; преди това маха старите. */
    fun write(context: Context, bitmap: Bitmap, name: String, nowMs: Long = System.currentTimeMillis()): File {
        val dir = dir(context).apply { mkdirs() }
        cleanup(dir, nowMs)
        val safe = name.filter { it.isLetterOrDigit() || it == '-' }.take(40).ifBlank { "card" }
        val file = File(dir, "chyavorec-$safe-$nowMs.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return file
    }

    /** Маха картинките, по-стари от [MAX_AGE_MS], и всички над [KEEP]-те най-нови. */
    fun cleanup(dir: File, nowMs: Long) {
        val files = dir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() } ?: return
        files.forEachIndexed { i, f -> if (i >= KEEP - 1 || nowMs - f.lastModified() > MAX_AGE_MS) f.delete() }
    }

    fun uri(context: Context, file: File): Uri = FileProvider.getUriForFile(context, Intents.fileProviderAuthority(context), file)

    /** ACTION_SEND с картинката ([uri]) и текста с връзката — с временно право за четене. */
    fun sendIntent(uri: Uri, subject: String, text: String): Intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
        // ClipData — за да стигне правото до избраното приложение и през системния избор.
        clipData = ClipData.newRawUri(subject, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    fun chooser(context: Context, uri: Uri, subject: String, text: String): Intent =
        Intent.createChooser(sendIntent(uri, subject, text), context.getString(R.string.action_share))
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}

/**
 * Снимката на картата — през общия ImageLoader (кешът на диска), софтуерна (рисува се и в
 * софтуерно платно). Сайтът я дава в ширина 1080; `null` (без връзка, грешка, изтекло време) —
 * картата е без снимка.
 */
suspend fun loadSharePhoto(context: Context, url: String?): ImageBitmap? {
    if (url.isNullOrBlank() || !url.startsWith("https://")) return null
    val loader = SingletonImageLoader.get(context)
    for (u in listOfNotNull(SiteImageUrls.optimized(url, 1080), url).distinct()) {
        try {
            val request = ImageRequest.Builder(context).data(u).size(1080).scale(Scale.FIT).precision(Precision.INEXACT)
                .allowHardware(false).build()
            val result = withTimeoutOrNull(8_000) { loader.execute(request) }
            (result as? SuccessResult)?.image?.toBitmap()?.let { return it.asImageBitmap() }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // следващият адрес
        }
    }
    return null
}

/**
 * Записаната карта като софтуерен bitmap с точния размер на [format]: първо през
 * [GraphicsLayer.toImageBitmap]; ако не стане — като се изрисува в софтуерно платно.
 */
internal suspend fun GraphicsLayer.toShareBitmap(format: ShareFormat): Bitmap? {
    val viaLayer = try {
        toImageBitmap().asAndroidBitmap().let { if (it.config == Bitmap.Config.HARDWARE) it.copy(Bitmap.Config.ARGB_8888, false) else it }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }
    if (viaLayer != null && viaLayer.width == format.widthPx && viaLayer.height == format.heightPx) return viaLayer
    return try {
        val bitmap = Bitmap.createBitmap(format.widthPx, format.heightPx, Bitmap.Config.ARGB_8888)
        CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(android.graphics.Canvas(bitmap)), Size(format.widthPx.toFloat(), format.heightPx.toFloat())) {
            drawLayer(this@toShareBitmap)
        }
        bitmap
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        viaLayer?.let { Bitmap.createScaledBitmap(it, format.widthPx, format.heightPx, true) }
    }
}

/** Какво се споделя като картинка: картата, текстът с връзката и името на файла. */
data class ShareImageRequest(val card: ShareCard, val subject: String, val text: String)

/** Състоянието на споделянето като картинка за един екран ([ShareImageHost] го изпълнява). */
@Stable
class ShareImageState {
    var pending by mutableStateOf<ShareImageRequest?>(null)
        internal set

    val busy: Boolean get() = pending != null

    fun share(request: ShareImageRequest) {
        if (pending == null) pending = request
    }
}

@Composable
fun rememberShareImageState(): ShareImageState = remember { ShareImageState() }

/**
 * Рисува чакащата карта извън екрана (0×0 в оформлението, нищо не се вижда), взима я като
 * картинка, записва я и отваря системното „Сподели“. Без семантика — екранният четец не я
 * вижда. [onRendered] — за тестовете (вместо „Сподели“).
 */
@Composable
fun ShareImageHost(state: ShareImageState, onRendered: ((Bitmap, ShareImageRequest) -> Unit)? = null) {
    val request = state.pending ?: return
    val context = LocalContext.current
    // Снимката — преди картата: картата се рисува веднъж, вече със снимката (или без нея).
    var photo by remember(request) { mutableStateOf<Loaded?>(null) }
    LaunchedEffect(request) { photo = Loaded(loadSharePhoto(context, request.card.photoUrl)) }
    val loaded = photo ?: return
    val format = request.card.format
    val layer = rememberGraphicsLayer()
    // Не е състояние: записът в рисуването не бива да предизвиква ново композиране.
    val recorded = remember(request) { java.util.concurrent.atomic.AtomicBoolean(false) }
    Box(
        Modifier
            .clearAndSetSemantics { }
            .layout { measurable, _ ->
                val p = measurable.measure(Constraints.fixed(format.widthPx, format.heightPx))
                layout(0, 0) { p.place(0, 0) }
            }
            .drawWithContent {
                // Само се записва — на екрана не се рисува нищо.
                layer.record { this@drawWithContent.drawContent() }
                recorded.set(true)
            },
    ) {
        CompositionLocalProvider(LocalDensity provides Density(ShareFormat.DENSITY, 1f), LocalLayoutDirection provides LayoutDirection.Ltr) {
            ShareCardContent(request.card, loaded.image)
        }
    }
    LaunchedEffect(request, loaded) {
        // Няколко кадъра — шрифтовете и снимката вече са в паметта, картата е записана.
        var frames = 0
        while (!recorded.get() && frames++ < 60) withFrameNanos { }
        withFrameNanos { }
        val bitmap = if (recorded.get()) layer.toShareBitmap(format) else null
        try {
            when {
                bitmap == null -> Toast.makeText(context, R.string.share_image_failed, Toast.LENGTH_SHORT).show()
                onRendered != null -> onRendered(bitmap, request)
                else -> {
                    val file = withContext(Dispatchers.IO) { ShareFiles.write(context, bitmap, request.card.fileName) }
                    val uri = ShareFiles.uri(context, file)
                    context.startActivity(ShareFiles.chooser(context, uri, request.subject, request.text).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            Toast.makeText(context, R.string.share_image_failed, Toast.LENGTH_SHORT).show()
        } finally {
            state.pending = null
        }
    }
}

/** Заредената (или липсващата) снимка — различно от „още се зарежда“. */
private class Loaded(val image: ImageBitmap?)
