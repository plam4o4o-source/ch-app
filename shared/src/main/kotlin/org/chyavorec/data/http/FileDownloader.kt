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
 * Сваляне на файл (PDF от „Документи и архив“) в папка на устройството — за отваряне с
 * приложение на телефона. С напредък и отменимо; покрай HTTP кеша на OkHttp (файлът се пази
 * тук, не втори път там). Готовият файл се появява наведнъж (временен файл → преименуване):
 * прекъснато или неуспешно сваляне не оставя половин документ.
 */
class FileDownloader(
    private val client: OkHttpClient,
    private val dir: File,
    private val now: () -> Long = System::currentTimeMillis,
    private val io: CoroutineDispatcher = Dispatchers.IO,
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
            if (target.length() > 0 && now() - target.lastModified() in 0 until maxAgeMillis) return@withContext Outcome.Success(target)
            dir.mkdirs()
            val part = File(dir, target.name + ".part")
            try {
                val request = Request.Builder().url(url).cacheControl(CacheControl.Builder().noStore().build()).build()
                client.newCall(request).await().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext Outcome.Failure(HttpFetcher.mapHttpError(response.code, response.header("Retry-After")))
                    }
                    val body = response.body
                    val total = body.contentLength().takeIf { it > 0 }
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
                                out.write(buffer, 0, n)
                                read += n
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
                if (!part.renameTo(target)) {
                    target.delete()
                    if (!part.renameTo(target)) return@withContext Outcome.Failure(AppError.Unexpected("rename"))
                }
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

    private companion object {
        const val BUFFER = 64 * 1024
        const val DAY = 24 * 60 * 60 * 1000L
    }
}
