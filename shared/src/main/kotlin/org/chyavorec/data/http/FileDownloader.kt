package org.chyavorec.data.http

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
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
 */
class FileDownloader(
    private val client: OkHttpClient,
    private val dir: File,
    private val now: () -> Long = System::currentTimeMillis,
    private val io: CoroutineDispatcher = Dispatchers.IO,
    /** Най-големият приет файл — и общият размер на папката след [prune]. */
    private val maxBytes: Long = MAX_BYTES,
) {
    /** Мястото на файла за [url] (по адреса — един и същ документ не се тегли повторно). */
    fun fileFor(url: String, extension: String = "pdf"): File =
        File(dir, Hashing.sha256Hex(url.toByteArray(Charsets.UTF_8)).take(24) + "." + extension)

    /**
     * Сваля [url] в [fileFor]. Вече свален файл, по-нов от [maxAgeMillis], се връща веднага.
     * [progress] получава дела 0..1 (или `null`, ако сървърът не е казал размера).
     */
    suspend fun download(url: String, maxAgeMillis: Long = DAY, progress: (Float?) -> Unit = {}): Outcome<File> =
        withContext(io) {
            val target = fileFor(url)
            if (target.length() > 0 && now() - target.lastModified() in 0 until maxAgeMillis && isPdf(target)) {
                return@withContext Outcome.Success(target)
            }
            dir.mkdirs()
            val part = File(dir, target.name + PART)
            try {
                val request = Request.Builder().url(url).cacheControl(CacheControl.Builder().noStore().build()).build()
                client.newCall(request).await().use { response ->
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
            }
        }

    /**
     * Изтрива файловете, по-стари от [maxAgeMillis], и после най-старите, докато общият размер
     * стане до [maxBytes] ([keep] — току-що сваленият — остава; незавършените `.part` — само по
     * възраст, за да не се пипне текущо сваляне). Блокиращо: извън главната нишка.
     */
    fun prune(maxAgeMillis: Long = WEEK, keep: File? = null) {
        val files = dir.listFiles()?.filter { it.isFile && it != keep } ?: return
        val time = now()
        val (old, recent) = files.partition { time - it.lastModified() !in 0 until maxAgeMillis }
        old.forEach { it.delete() }
        val done = recent.filterNot { it.name.endsWith(PART) }
        var total = done.sumOf { it.length() } + (keep?.length() ?: 0L)
        for (f in done.sortedBy { it.lastModified() }) {
            if (total <= maxBytes) break
            total -= f.length()
            f.delete()
        }
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
