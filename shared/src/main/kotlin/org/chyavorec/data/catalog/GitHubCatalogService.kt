package org.chyavorec.data.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.core.AppError
import org.chyavorec.core.Hashing
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.service.CatalogFetch
import org.chyavorec.domain.service.CatalogService
import org.chyavorec.domain.service.CatalogValidators

/** SHA-256 (hex) на суровия текст на каталога (UTF-8) — за откриване на непроменено съдържание. */
internal fun katalogHash(text: String): String = katalogHash(text.toByteArray(Charsets.UTF_8))

/** SHA-256 (hex) на суровите байтове на каталога. */
internal fun katalogHash(bytes: ByteArray): String = Hashing.sha256Hex(bytes)

/**
 * Зарежда каталога от същите два адреса, които ползва и уеб страницата
 * „Електронен каталог“ на chyavorec.org (site/page-katalog.html в InvLib):
 * 1. raw.githubusercontent.com — основен път;
 * 2. cdn.jsdelivr.net — независим резервен път (друга CDN инфраструктура).
 *
 * Файлът (няколко MB) се пази само веднъж — в кеша на хранилището. Затова заявката
 * заобикаля HTTP кеша на OkHttp (`no-store`) и е ръчно условна (`If-None-Match` със
 * запазения ETag): при 304 тялото не се чете, а при същия SHA-256 — не се разчита.
 * Разчитането върви на [work], не на извикващата (главна) нишка.
 */
class GitHubCatalogService(
    private val http: HttpFetcher,
    private val urls: List<String>,
    private val work: CoroutineDispatcher = Dispatchers.Default,
    /**
     * Старото място (клон `main`), където InvLib обновява файла най-много веднъж на ден.
     * Ползва се САМО ако основният адрес отговори 404 (клонът `katalog` още не е създаден
     * от по-стар InvLib) — иначе мрежова грешка би върнала по-стар каталог върху по-новия.
     */
    private val legacyUrls: List<String> = emptyList(),
) : CatalogService {

    /** Копията от по-старите версии (в HTTP кеша на OkHttp) се изтриват веднъж на процес. */
    @Volatile private var httpCacheEvicted = false

    override suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>> =
        when (val r = fetchCatalogConditional(null)) {
            is Outcome.Failure -> r
            is Outcome.Success -> when (val f = r.value) {
                is CatalogFetch.Changed -> Outcome.Success(String(f.raw, Charsets.UTF_8) to f.snapshot)
                is CatalogFetch.Unchanged -> Outcome.Failure(AppError.Parse("katalog.json"))
            }
        }

    override suspend fun fetchCatalogIfChanged(knownHash: String?): Outcome<Pair<String, CatalogSnapshot>?> =
        when (val r = fetchCatalogConditional(knownHash?.let { CatalogValidators(it) })) {
            is Outcome.Failure -> r
            is Outcome.Success -> when (val f = r.value) {
                is CatalogFetch.Changed -> Outcome.Success(String(f.raw, Charsets.UTF_8) to f.snapshot)
                is CatalogFetch.Unchanged -> Outcome.Success(null)
            }
        }

    override suspend fun fetchCatalogConditional(known: CatalogValidators?): Outcome<CatalogFetch> {
        if (!httpCacheEvicted) {
            httpCacheEvicted = true
            (urls + legacyUrls).forEach { http.evictFromHttpCache(it) }
        }
        val (primary, notFound) = fetchFrom(urls, known)
        return if (primary is Outcome.Failure && notFound && legacyUrls.isNotEmpty()) fetchFrom(legacyUrls, known).first else primary
    }

    /** Резултатът и дали някой от адресите е отговорил 404 (клонът/файлът липсва). */
    private suspend fun fetchFrom(list: List<String>, known: CatalogValidators?): Pair<Outcome<CatalogFetch>, Boolean> {
        var notFound = false
        var lastError: AppError = AppError.Network
        for (url in list) {
            val headers = buildMap {
                // no-store: без копие в HTTP кеша (файлът вече е на диска); заявката отива до сървъра.
                put("Cache-Control", "no-store")
                val etag = known?.etag
                if (known?.hash != null && etag != null && known.etagUrl == url) put("If-None-Match", etag)
            }
            when (val result = http.get(url, headers, skipBodyIfNotModified = true)) {
                is Outcome.Success -> {
                    val body = result.value
                    if (body.notModified) {
                        if (known?.hash != null) return Outcome.Success(CatalogFetch.Unchanged(body.etag ?: known.etag, url)) to false
                        lastError = AppError.Parse("katalog.json")
                        continue
                    }
                    val parsed: Result<CatalogFetch> = withContext(work) {
                        runCatching {
                            val hash = katalogHash(body.bytes)
                            if (known?.hash != null && hash == known.hash) CatalogFetch.Unchanged(body.etag, url)
                            else CatalogFetch.Changed(body.bytes, KatalogParser.parse(body.bytes), hash, body.etag, url)
                        }
                    }
                    parsed.onSuccess { return Outcome.Success(it) to false }
                    lastError = AppError.Parse("katalog.json")
                }
                is Outcome.Failure -> {
                    lastError = result.error
                    if (result.error == AppError.NotFound) notFound = true
                }
            }
        }
        return Outcome.Failure(lastError) to notFound
    }
}
