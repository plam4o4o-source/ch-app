package org.chyavorec.app.ui.screens.donate

import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.content.IntentCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.util.Intents
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer

/*
 * „Дари спомен“: стари снимки за фотоархива на читалището. Снимките се смаляват на телефона
 * (копие в кеша, без EXIF — и без мястото на снимане) и тръгват само с писмо от приложението
 * за имейл на читателя. Приложението само не качва нищо.
 */

/** Най-много снимки в едно писмо (и в избора). */
const val DONATE_MAX_PHOTOS = 10

/** Попълненото във формата. Полетата са по желание — задължителни са снимките и съгласието. */
data class DonationForm(
    val who: String = "",
    val time: String = "",
    val place: String = "",
    val contact: String = "",
    val consent: Boolean = false,
)

/** Откъде още се стига до „Дари спомен“: фотодокументалната изложба (албумът и страницата от сайта). */
object DonateEntry {
    /** Албумът на изложбата в галерията (името е от chyavorec.org — виж ChyavorecSiteService.galleryPages). */
    const val EXHIBITION_ALBUM = "Фотодокументална изложба"
    private const val EXHIBITION_PATH = "/fotodokumentalna-izlozhba"

    fun isExhibitionPage(url: String): Boolean = Uri.parse(url).path?.trimEnd('/') == EXHIBITION_PATH
}

/** Смалените копия на снимките в `cacheDir/donate/` (FileProvider ги подава на приложението за имейл). */
object DonationPhotos {
    const val DIR = "donate"
    /** Най-дългата страна на копието — достатъчно за архива, а писмото остава поносимо. */
    const val MAX_SIDE = 2560
    const val JPEG_QUALITY = 85
    /** Миниатюрата на екрана (в паметта, не на диска). */
    const val THUMB_SIDE = 320
    /** Приложението за имейл може да чете по-късно (чернова) — копията стоят ден. */
    const val MAX_AGE_MS = 24L * 60 * 60 * 1000
    /** По-голям файл не се чете (не е снимка за архив, а и не би се побрал в паметта). */
    const val MAX_INPUT_BYTES = 40 * 1024 * 1024

    fun dir(context: Context): File = File(context.cacheDir, DIR)

    /** При отваряне на екрана: копията, по-стари от ден. */
    fun cleanup(context: Context, nowMs: Long = System.currentTimeMillis()) {
        dir(context).listFiles()?.filter { it.isFile && nowMs - it.lastModified() > MAX_AGE_MS }?.forEach { it.delete() }
    }

    /** Всички — при „Изчисти временните данни“. */
    fun clear(context: Context) {
        dir(context).deleteRecursively()
    }

    /** Размерът след смаляване: най-дългата страна до [max]; по-малка снимка остава както е. */
    fun targetSize(width: Int, height: Int, max: Int = MAX_SIDE): Pair<Int, Int> {
        val long = maxOf(width, height)
        if (long <= max) return width to height
        val scale = max.toFloat() / long
        return (width * scale).toInt().coerceAtLeast(1) to (height * scale).toInt().coerceAtLeast(1)
    }

    /**
     * Чете снимката [uri], смалява я и я записва като JPEG; връща копието и миниатюра.
     * `null` — не е снимка, не се чете (изтекло право, твърде голям файл) или няма памет.
     */
    fun prepare(context: Context, uri: Uri, nowMs: Long = System.currentTimeMillis()): PreparedPhoto? = try {
        val bytes = context.contentResolver.openInputStream(uri)?.use { readCapped(it, MAX_INPUT_BYTES) }
        bytes?.let { prepare(context, it, nowMs) }
    } catch (_: OutOfMemoryError) {
        null
    } catch (_: Exception) {
        null // SecurityException, FileNotFoundException, повреден файл
    }

    /** Същото от вече прочетените байтове (и за тестовете). */
    internal fun prepare(context: Context, bytes: ByteArray, nowMs: Long): PreparedPhoto? {
        val full = decode(bytes, MAX_SIDE) ?: return null
        try {
            val dir = dir(context).apply { mkdirs() }
            val file = File(dir, "spomen-$nowMs-${System.nanoTime() % 100_000}.jpg")
            file.outputStream().use { full.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, it) }
            val (tw, th) = targetSize(full.width, full.height, THUMB_SIDE)
            // Малка снимка: миниатюрата може да е самата тя — тогава не се освобождава.
            val thumb = Bitmap.createScaledBitmap(full, tw, th, true)
            if (thumb !== full) full.recycle()
            return PreparedPhoto(file, thumb)
        } catch (e: Throwable) {
            full.recycle()
            throw e
        }
    }

    /** Декодира с най-дълга страна до [maxSide], завъртяна според EXIF. */
    internal fun decode(bytes: ByteArray, maxSide: Int): Bitmap? {
        // ImageDecoder не успя (рядък формат) — и BitmapFactory, той също завърта по EXIF.
        val decoded = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) decodeModern(bytes, maxSide) else null) ?: decodeLegacy(bytes, maxSide)
        return decoded?.let { scaleDown(it, maxSide) }
    }

    /** Android 9+: ImageDecoder смалява още при декодирането и сам прилага EXIF ориентацията. */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun decodeModern(bytes: ByteArray, maxSide: Int): Bitmap? = runCatching {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(ByteBuffer.wrap(bytes))) { decoder, info, _ ->
            val (w, h) = targetSize(info.size.width, info.size.height, maxSide)
            if (w != info.size.width || h != info.size.height) decoder.setTargetSize(w, h)
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
    }.getOrNull()

    /** Android 8: BitmapFactory (с inSampleSize) и завъртане според EXIF. */
    private fun decodeLegacy(bytes: ByteArray, maxSide: Int): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / (sample * 2) >= maxSide) sample *= 2
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options().apply { inSampleSize = sample }) ?: return null
        val degrees = runCatching {
            when (ExifInterface(ByteArrayInputStream(bytes)).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        }.getOrDefault(0f)
        if (degrees == 0f) return bitmap
        val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(degrees) }, true)
        if (rotated !== bitmap) bitmap.recycle()
        return rotated
    }

    private fun scaleDown(bitmap: Bitmap, maxSide: Int): Bitmap {
        val (w, h) = targetSize(bitmap.width, bitmap.height, maxSide)
        if (w == bitmap.width && h == bitmap.height) return bitmap
        return Bitmap.createScaledBitmap(bitmap, w, h, true).also { if (it !== bitmap) bitmap.recycle() }
    }

    /** Целият поток, ако е до [cap] байта; иначе `null`. */
    private fun readCapped(input: InputStream, cap: Int): ByteArray? {
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(64 * 1024)
        var total = 0
        while (true) {
            val n = input.read(buffer)
            if (n < 0) break
            total += n
            if (total > cap) return null
            out.write(buffer, 0, n)
        }
        return out.toByteArray()
    }

    fun uri(context: Context, file: File): Uri = FileProvider.getUriForFile(context, Intents.fileProviderAuthority(context), file)
}

/** Смаленото копие и миниатюрата му за екрана. */
class PreparedPhoto(val file: File, val thumb: Bitmap)

/**
 * „Сподели → Читалище Яворец“ от галерията: снимките чакат тук, докато екранът „Дари спомен“
 * ги вземе ([take]). Приемат се само `content://` адреси на други приложения — не файлове и не
 * собствените ни (кешът, PDF-ите): чуждо приложение не може да „подаде“ наш файл за изпращане.
 */
object DonateIntake {
    private val pending = MutableStateFlow<List<Uri>>(emptyList())

    /** Споделяне на снимки → връзката към екрана (и снимките чакат); друго намерение → `null`. */
    fun receive(context: Context, intent: Intent): String? {
        if (intent.action != Intent.ACTION_SEND && intent.action != Intent.ACTION_SEND_MULTIPLE) return null
        pending.value = sharedImages(context.packageName, intent)
        return DeepLinks.DONATE
    }

    /** Споделените снимки, които приемаме — до [DONATE_MAX_PHOTOS]. */
    fun sharedImages(ownPackage: String, intent: Intent): List<Uri> {
        if (intent.type?.startsWith("image/") != true) return emptyList()
        val raw = when (intent.action) {
            Intent.ACTION_SEND -> listOfNotNull(IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java))
            Intent.ACTION_SEND_MULTIPLE -> IntentCompat.getParcelableArrayListExtra(intent, Intent.EXTRA_STREAM, Uri::class.java).orEmpty()
            else -> emptyList()
        }
        return raw.filter { accepts(it, ownPackage) }.distinct().take(DONATE_MAX_PHOTOS)
    }

    internal fun accepts(uri: Uri, ownPackage: String): Boolean {
        val authority = uri.authority ?: return false
        return uri.scheme == "content" && !authority.startsWith(ownPackage)
    }

    /** Чакащите снимки (веднъж — после списъкът е празен). */
    fun take(): List<Uri> = pending.getAndUpdate { emptyList() }
}

/** Писмото до читалището: тема, текст и снимките като прикачени файлове. */
object DonationMail {
    fun canSend(photos: Int, form: DonationForm): Boolean = form.consent && photos in 1..DONATE_MAX_PHOTOS

    /** „Дари спомен — <кой / къде / колко снимки>“. */
    fun subject(context: Context, form: DonationForm, photos: Int): String {
        val about = form.who.ifBlank { form.place }.trim().replace(Regex("\\s+"), " ")
            .ifBlank { context.resources.getQuantityString(R.plurals.photos_count, photos, photos) }
        val short = if (about.length > SUBJECT_MAX) about.take(SUBJECT_MAX - 1).trimEnd() + "…" else about
        return context.getString(R.string.donate_mail_subject, short)
    }

    fun body(context: Context, form: DonationForm, photos: Int): String = buildString {
        appendLine(context.getString(R.string.donate_mail_greeting))
        appendLine(context.resources.getQuantityString(R.plurals.donate_mail_intro, photos, photos))
        appendLine()
        // Само попълнените полета (без празни редове от празните).
        val fields = listOf(
            R.string.donate_who to form.who, R.string.donate_when to form.time,
            R.string.donate_where to form.place, R.string.donate_mail_from to form.contact,
        ).filter { it.second.isNotBlank() }
        fields.forEach { (label, value) -> appendLine(context.getString(label) + ": " + value.trim()) }
        if (fields.isNotEmpty()) appendLine()
        appendLine(context.getString(R.string.donate_consent))
        appendLine()
        append(context.getString(R.string.donate_mail_footer))
    }

    /**
     * ACTION_SEND_MULTIPLE до [to] със снимките [uris] (FileProvider) — с временно право за четене,
     * и през ClipData (за да стигне до избраното приложение през системния избор).
     * `null` — няма съгласие или снимките не са 1…[DONATE_MAX_PHOTOS].
     */
    fun sendIntent(context: Context, to: String, form: DonationForm, uris: List<Uri>): Intent? {
        if (!canSend(uris.size, form)) return null
        return Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject(context, form, uris.size))
            putExtra(Intent.EXTRA_TEXT, body(context, form, uris.size))
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
            clipData = ClipData.newRawUri(null, uris.first()).apply { uris.drop(1).forEach { addItem(ClipData.Item(it)) } }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    /**
     * Системният избор. Ако на телефона има приложение за имейл, изборът е само между тях
     * (selector `mailto:` — писмото е до читалището, не в чат); иначе — всички, които приемат снимки.
     */
    fun chooser(context: Context, send: Intent): Intent {
        val mailto = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        if (mailto.resolveActivity(context.packageManager) != null) send.selector = mailto
        return Intent.createChooser(send, context.getString(R.string.donate_chooser)).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Без самото приложение (то също приема снимки).
            .putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, arrayOf(ComponentName(context, MainActivity::class.java)))
    }

    private const val SUBJECT_MAX = 60
}
