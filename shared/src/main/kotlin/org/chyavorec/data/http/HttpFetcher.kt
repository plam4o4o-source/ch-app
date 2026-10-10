package org.chyavorec.data.http

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import java.io.IOException
import kotlin.coroutines.resume

/**
 * Суров HTTP отговор.
 *
 * [notModified] — съдържанието не е ново: сървърът е отговорил 304 (на условна заявка —
 * ръчна или през HTTP кеша на OkHttp) или отговорът е изцяло от кеша. При ръчна условна
 * заявка (суров 304) и при `skipBodyIfNotModified` [bytes] е празен — тялото не се чете.
 * [etag] — заглавката `ETag` (за следваща условна заявка с `If-None-Match`).
 */
class HttpBody(
    val bytes: ByteArray,
    val charset: String?,
    val finalUrl: String,
    val notModified: Boolean = false,
    val etag: String? = null,
) {
    fun text(): String = String(bytes, charset?.let { runCatching { charset(it) }.getOrNull() } ?: Charsets.UTF_8)
}

/**
 * Тънка обвивка над OkHttp: suspend извикване + превод на грешките в [AppError].
 * Никога не хвърля изключение нагоре и никога не записва съдържание в лога.
 * Отговорът (блокиращо четене на тялото) се обработва на [io], не на извикващата нишка.
 */
class HttpFetcher(
    private val client: OkHttpClient,
    private val userAgent: String,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * [skipBodyIfNotModified] — при непроменено съдържание ([HttpBody.notModified]) тялото
     * изобщо не се чете (напр. каталогът — няколко MB, които вече са на диска).
     */
    suspend fun get(
        url: String,
        headers: Map<String, String> = emptyMap(),
        skipBodyIfNotModified: Boolean = false,
    ): Outcome<HttpBody> {
        val request = runCatching {
            Request.Builder().url(url).header("User-Agent", userAgent).apply {
                headers.forEach { (k, v) -> header(k, v) }
            }.get().build()
        }.getOrElse { return Outcome.Failure(AppError.Unexpected("bad url")) }
        return execute(request, skipBodyIfNotModified)
    }

    suspend fun execute(request: Request): Outcome<HttpBody> = execute(request, skipBodyIfNotModified = false)

    private suspend fun execute(request: Request, skipBodyIfNotModified: Boolean): Outcome<HttpBody> {
        val call = client.newCall(request)
        return try {
            withContext(io) { call.await().use { response -> toOutcome(response, skipBodyIfNotModified) } }
        } catch (e: IOException) {
            Outcome.Failure(AppError.Network)
        }
    }

    /** Премахва адреса от HTTP кеша на OkHttp (ако има такъв) — за файлове, които се пазят другаде. */
    suspend fun evictFromHttpCache(url: String) {
        val cache = client.cache ?: return
        withContext(io) {
            runCatching {
                val urls = cache.urls()
                while (urls.hasNext()) if (urls.next() == url) urls.remove()
            }
        }
    }

    private fun toOutcome(response: Response, skipBodyIfNotModified: Boolean): Outcome<HttpBody> {
        val finalUrl = response.request.url.toString()
        val etag = response.header("ETag")
        if (response.code == 304) {
            // Ръчна условна заявка (If-None-Match), минала покрай HTTP кеша: тяло няма.
            return Outcome.Success(HttpBody(EMPTY, null, finalUrl, notModified = true, etag = etag))
        }
        if (!response.isSuccessful) {
            // При 409 тялото носи кода на конфликта ({"error":"pending"}) — четем само него.
            val errorCode = if (response.code == 409) runCatching { response.body.string().take(4096) }.getOrNull()?.let(::errorCode) else null
            return Outcome.Failure(mapHttpError(response.code, response.header("Retry-After"), errorCode))
        }
        val notModified = isNotModified(response.networkResponse?.code, response.cacheResponse != null)
        val body = response.body
        val charset = body.contentType()?.charset()?.name()
        if (notModified && skipBodyIfNotModified) {
            return Outcome.Success(HttpBody(EMPTY, charset, finalUrl, notModified = true, etag = etag))
        }
        return Outcome.Success(HttpBody(body.bytes(), charset, finalUrl, notModified, etag))
    }

    companion object {
        private val EMPTY = ByteArray(0)
        private val errorField = Regex("\"error\"\\s*:\\s*\"([A-Za-z0-9_.-]{1,64})\"")

        /** 304 от сървъра (условна заявка) или отговор изцяло от кеша, без мрежа. */
        internal fun isNotModified(networkCode: Int?, hasCacheResponse: Boolean): Boolean =
            networkCode == 304 || (hasCacheResponse && networkCode == null)

        /** Кодът от `{"error":"…"}` (или `null`, ако тялото не е в този вид). */
        fun errorCode(body: String?): String? = body?.let { errorField.find(it)?.groupValues?.get(1) }

        fun mapHttpError(code: Int, retryAfter: String?, errorCode: String? = null): AppError = when (code) {
            // 403 = „нямаш право на това“, а не невалидна сесия/парола: не води до
            // изход и не се брои като грешен опит за вход.
            401 -> AppError.Unauthorized
            404, 410 -> AppError.NotFound
            409 -> AppError.Conflict(errorCode ?: "conflict")
            429 -> AppError.RateLimited(retryAfter?.toLongOrNull() ?: 60)
            else -> AppError.Server(code)
        }
    }
}

private suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
    enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            cont.resume(response)
        }

        override fun onFailure(call: Call, e: IOException) {
            if (!cont.isCancelled) cont.resumeWith(Result.failure(e))
        }
    })
    cont.invokeOnCancellation { runCatching { cancel() } }
}
