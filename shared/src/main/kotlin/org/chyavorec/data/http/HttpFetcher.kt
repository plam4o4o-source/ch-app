package org.chyavorec.data.http

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import java.io.IOException
import kotlin.coroutines.resume

/** Суров HTTP отговор. */
class HttpBody(val bytes: ByteArray, val charset: String?, val finalUrl: String) {
    fun text(): String = String(bytes, charset?.let { runCatching { charset(it) }.getOrNull() } ?: Charsets.UTF_8)
}

/**
 * Тънка обвивка над OkHttp: suspend извикване + превод на грешките в [AppError].
 * Никога не хвърля изключение нагоре и никога не записва съдържание в лога.
 */
class HttpFetcher(private val client: OkHttpClient, private val userAgent: String) {

    suspend fun get(url: String, headers: Map<String, String> = emptyMap()): Outcome<HttpBody> {
        val request = runCatching {
            Request.Builder().url(url).header("User-Agent", userAgent).apply {
                headers.forEach { (k, v) -> header(k, v) }
            }.get().build()
        }.getOrElse { return Outcome.Failure(AppError.Unexpected("bad url")) }
        return execute(request)
    }

    suspend fun execute(request: Request): Outcome<HttpBody> {
        val call = client.newCall(request)
        return try {
            call.await().use { response -> toOutcome(response) }
        } catch (e: IOException) {
            Outcome.Failure(AppError.Network)
        }
    }

    private fun toOutcome(response: Response): Outcome<HttpBody> {
        if (!response.isSuccessful) {
            // При 409 тялото носи кода на конфликта ({"error":"pending"}) — четем само него.
            val errorCode = if (response.code == 409) runCatching { response.body?.string()?.take(4096) }.getOrNull()?.let(::errorCode) else null
            return Outcome.Failure(mapHttpError(response.code, response.header("Retry-After"), errorCode))
        }
        val body = response.body ?: return Outcome.Failure(AppError.Parse("empty body"))
        val bytes = body.bytes()
        val charset = body.contentType()?.charset()?.name()
        return Outcome.Success(HttpBody(bytes, charset, response.request.url.toString()))
    }

    companion object {
        private val errorField = Regex("\"error\"\\s*:\\s*\"([A-Za-z0-9_.-]{1,64})\"")

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
