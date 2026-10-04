package org.chyavorec.data.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.service.CatalogService
import java.security.MessageDigest

/** SHA-256 (hex) на суровия текст на каталога — за откриване на непроменено съдържание. */
internal fun katalogHash(text: String): String =
    MessageDigest.getInstance("SHA-256").digest(text.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }

/**
 * Зарежда каталога от същите два адреса, които ползва и уеб страницата
 * „Електронен каталог“ на chyavorec.org (site/page-katalog.html в InvLib):
 * 1. raw.githubusercontent.com — основен път;
 * 2. cdn.jsdelivr.net — независим резервен път (друга CDN инфраструктура).
 *
 * Разчитането (няколко MB JSON) върви на [work], не на извикващата (главна) нишка.
 */
class GitHubCatalogService(
    private val http: HttpFetcher,
    private val urls: List<String>,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) : CatalogService {

    override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> =
        when (val r = fetchCatalogIfChanged(null)) {
            is Outcome.Failure -> r
            is Outcome.Success -> r.value?.let { Outcome.Success(it) } ?: Outcome.Failure(AppError.Parse("katalog.json"))
        }

    override suspend fun fetchCatalogIfChanged(knownHash: String?): Outcome<Pair<String, CatalogSnapshot>?> {
        var lastError: AppError = AppError.Network
        for (url in urls) {
            // no-cache = повторна проверка (условна заявка през HTTP кеша на OkHttp); при
            // непроменен файл тялото идва от кеша, а тук се пропуска и разчитането.
            when (val result = http.get(url, mapOf("Cache-Control" to "no-cache"))) {
                is Outcome.Success -> {
                    val body = result.value
                    val parsed: Result<Pair<String, CatalogSnapshot>?> = withContext(work) {
                        runCatching {
                            val text = body.text()
                            if (knownHash != null && katalogHash(text) == knownHash) null
                            else text to KatalogParser.parse(text)
                        }
                    }
                    parsed.onSuccess { return Outcome.Success(it) }
                    lastError = AppError.Parse("katalog.json")
                }
                is Outcome.Failure -> lastError = result.error
            }
        }
        return Outcome.Failure(lastError)
    }
}
