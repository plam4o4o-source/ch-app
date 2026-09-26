package org.chyavorec.data.catalog

import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.service.CatalogService

/**
 * Зарежда каталога от същите два адреса, които ползва и уеб страницата
 * „Електронен каталог“ на chyavorec.org (site/page-katalog.html в InvLib):
 * 1. raw.githubusercontent.com — основен път;
 * 2. cdn.jsdelivr.net — независим резервен път (друга CDN инфраструктура).
 */
class GitHubCatalogService(
    private val http: HttpFetcher,
    private val urls: List<String>,
) : CatalogService {

    override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> {
        var lastError: AppError = AppError.Network
        for (url in urls) {
            when (val result = http.get(url, mapOf("Cache-Control" to "no-cache"))) {
                is Outcome.Success -> {
                    val text = result.value.text()
                    val parsed = runCatching { KatalogParser.parse(text) }
                    parsed.onSuccess { return Outcome.Success(text to it) }
                    lastError = AppError.Parse("katalog.json")
                }
                is Outcome.Failure -> lastError = result.error
            }
        }
        return Outcome.Failure(lastError)
    }
}
