package org.chyavorec.data.http

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.chyavorec.core.AppError
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import java.io.File
import java.io.IOException

/**
 * Сваляне на PDF („Документи и архив“) в папка на устройството — за отваряне с приложение
 * на телефона. С напредък и отменимо; покрай HTTP кеша на OkHttp (файлът се пази тук, не
 * втори път там). Готовият файл се появява наведнъж (временен файл → преименуване):
 * прекъснато или неуспешно сваляне не оставя половин документ. Приема се само истински PDF
 * (`%PDF-` в началото — не страница за вход в Wi-Fi мрежа) до [maxBytes]. Папката се
 * поддържа малка ([prune]) и се изчиства с „Изчисти кеша“ ([clear]).
 *
 * Всеки документ е в своя папка (по адреса), а файлът в нея е с четимо име ([fileFor]) —
 * това име вижда приложението, което го отваря (заглавието в четеца на PDF).
 */
class FileDownloader(
    private val client: OkHttpClient,
    private val dir: File,
    private val now: () -> Long = System::currentTimeMillis,
    private val io: CoroutineDispatcher = Dispatchers.IO,
    /** Най-големият приет файл — и общият размер на папката след [prune]. */
    private val maxBytes: Long = MAX_BYTES,
    /** Откъде може да е файлът — и адресът, и крайният след пренасочванията (напр. само сайтът). */
    private val allowedUrl: (HttpUrl) -> Boolean = { true },
) {
    /**
     * Мястото на файла за [url]: папката е по адреса (един и същ документ не се тегли
     * повторно), а името — [name] (напр. заглавието на документа) или последната част от
     * пътя на адреса, сведени до безопасно име на файл с разширение [extension].
     */
    fun fileFor(url: String, name: String? = null, extension: String = "pdf"): File {
        val key = Hashing.sha256Hex(url.toByteArray(Charsets.UTF_8)).take(24)
        val raw = name?.takeIf { it.isNotBlank() } ?: url.toHttpUrlOrNull()?.pathSegments?.lastOrNull { it.isNotBlank() }
        return File(File(dir, key), safeFileName(raw, extension))
    }

    /**
     * Сваля [url] в [fileFor] (с име [name]). Вече свален файл, по-нов от [maxAgeMillis], се
     * връща веднага. [progress] получава дела 0..1 (или `null`, ако сървърът не е казал размера).
     */
    suspend fun download(url: String, name: String? = null, maxAgeMillis: Long = DAY, progress: (Float?) -> Unit = {}): Outcome<File> =
        withContext(io) {
            val target = fileFor(url, name)
            if (target.length() > 0 && now() - target.lastModified() in 0 until maxAgeMillis && isPdf(target)) {
                return@withContext Outcome.Success(target)
            }
            val folder = target.parentFile!!
            folder.mkdirs()
            val part = File(folder, target.name + PART)
            try {
                val request = Request.Builder().url(url).cacheControl(CacheControl.Builder().noStore().build()).build()
                if (!allowedUrl(request.url)) return@withContext Outcome.Failure(AppError.Unexpected("host"))
                client.newCall(request).await().use { response ->
                    // Пренасочен към чужд адрес — без сваляне.
                    if (!allowedUrl(response.request.url)) return@withContext Outcome.Failure(AppError.Unexpected("redirect"))
                    if (!response.isSuccessful) {
                        return@withContext Outcome.Failure(HttpFetcher.mapHttpError(response.code, response.header("Retry-After")))
                    }
                    val body = response.body
                    // HTML (напр. страница за вход в Wi-Fi) или твърде голям файл — без сваляне.
                    if (body.contentType()?.type == "text") return@withContext Outcome.Failure(AppError.Parse("not a pdf"))
                    val total = body.contentLength().takeIf { it > 0 }
                    if (total != null && total > maxBytes) return@withContext Outcome.Failure(AppError.Parse("too large"))
                    var read = 0L
                    var reported = -1
                    progress(if (total == null) null else 0f)
                    body.byteStream().use { input ->
                        part.outputStream().use { out ->
                            val buffer = ByteArray(BUFFER)
                            while (true) {
                                coroutineContext.ensureActive()
                                val n = input.read(buffer)
                                if (n < 0) break
                                read += n
                                // Без Content-Length (или с грешен) — границата е по прочетеното.
                                if (read > maxBytes) return@withContext Outcome.Failure(AppError.Parse("too large"))
                                out.write(buffer, 0, n)
                                // Напредъкът — на всеки процент (не за всеки буфер).
                                if (total != null) {
                                    val percent = (read * 100 / total).toInt().coerceAtMost(100)
                                    if (percent != reported) { reported = percent; progress(percent / 100f) }
                                }
                            }
                        }
                    }
                    if (read == 0L) return@withContext Outcome.Failure(AppError.Parse("empty file"))
                }
                if (!isPdf(part)) return@withContext Outcome.Failure(AppError.Parse("not a pdf"))
                if (!part.renameTo(target)) {
                    target.delete()
                    if (!part.renameTo(target)) return@withContext Outcome.Failure(AppError.Unexpected("rename"))
                }
                // Същият документ под старо име (сменено заглавие) — вън.
                folder.listFiles()?.forEach { if (it != target && it != part) it.delete() }
                runCatching { prune(keep = target) }
                Outcome.Success(target)
            } catch (e: CancellationException) {
                throw e
            } catch (_: IOException) {
                Outcome.Failure(AppError.Network)
            } catch (e: IllegalArgumentException) {
                // Неразчетим адрес.
                Outcome.Failure(AppError.Unexpected(e.javaClass.simpleName))
            } finally {
                part.delete()
                // Празната папка на неуспешно сваляне (непразна — с предишния файл — остава).
                folder.delete()
            }
        }

    /**
     * Изтрива файловете, по-стари от [maxAgeMillis], и после най-старите, докато общият размер
     * стане до [maxBytes] ([keep] — току-що сваленият — остава). Незавършените `.part` — само
     * по възраст, за да не се пипне текущо сваляне, а с [dropPartial] (при стартиране, когато
     * сваляне още няма) — всички. Блокиращо: извън главната нишка.
     */
    fun prune(maxAgeMillis: Long = WEEK, keep: File? = null, dropPartial: Boolean = false) {
        val files = dir.walk().filter { it.isFile && it != keep }.toList()
        val time = now()
        fun old(f: File) = time - f.lastModified() !in 0 until maxAgeMillis
        val (partial, done) = files.partition { it.name.endsWith(PART) }
        partial.filter { dropPartial || old(it) }.forEach(::remove)
        val (stale, recent) = done.partition(::old)
        stale.forEach(::remove)
        var total = recent.sumOf { it.length() } + (keep?.length() ?: 0L)
        for (f in recent.sortedBy { it.lastModified() }) {
            if (total <= maxBytes) break
            total -= f.length()
            remove(f)
        }
    }

    /** Файлът — и папката на документа, ако е останала празна. */
    private fun remove(file: File) {
        file.delete()
        file.parentFile?.takeIf { it != dir }?.delete()
    }

    /** „Изчисти кеша“: всички свалени документи. Блокиращо: извън главната нишка. */
    fun clear() {
        dir.deleteRecursively()
    }

    private companion object {
        const val BUFFER = 64 * 1024
        const val DAY = 24 * 60 * 60 * 1000L
        const val WEEK = 7 * DAY
        const val MAX_BYTES = 50L * 1024 * 1024
        const val PART = ".part"
        /** Най-дългото име (в знаци — кирилицата е по 2 байта, а границата на файловата система е 255). */
        const val MAX_NAME = 80
        private val UNSAFE = Regex("[\\p{Cntrl}/\\\\:*?\"<>|]+")
        private val SPACES = Regex("\\s+")

        /** Безопасно име на файл: без забранени знаци и водеща точка, до [MAX_NAME] знака, с [extension]. */
        fun safeFileName(raw: String?, extension: String): String {
            val suffix = ".$extension"
            val base = raw.orEmpty().replace(UNSAFE, " ").replace(SPACES, " ").trim()
                .let { if (it.endsWith(suffix, ignoreCase = true)) it.dropLast(suffix.length) else it }
                .trim('.', ' ').take(MAX_NAME).trimEnd('.', ' ')
            return base.ifEmpty { "document" } + suffix
        }
        /** Сигнатурата на PDF — до 1024 байта от началото (както я търсят и четците). */
        private val MAGIC = "%PDF-".toByteArray(Charsets.US_ASCII)
        const val MAGIC_WINDOW = 1024

        fun isPdf(file: File): Boolean = runCatching {
            val head = ByteArray(MAGIC_WINDOW)
            // Без readNBytes (Android 13+): докато се напълни или свърши файлът.
            val size = file.inputStream().use { input ->
                var n = 0
                while (n < head.size) {
                    val r = input.read(head, n, head.size - n)
                    if (r < 0) break
                    n += r
                }
                n
            }
            (0..size - MAGIC.size).any { i -> MAGIC.indices.all { head[i + it] == MAGIC[it] } }
        }.getOrDefault(false)
    }
}
