package org.chyavorec.data.update

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import java.io.File
import java.io.InputStream
import java.net.URI
import java.security.MessageDigest

/**
 * Описание на публикувана версия — файлът `update.json`, който release
 * workflow-ът качва към всяко GitHub Release заедно с APK файла.
 */
data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    /** SHA-256 на APK файла (64 шестнадесетични знака, малки букви). */
    val sha256: String,
    val sizeBytes: Long?,
    val minSdk: Int,
    val notes: String,
    val releaseUrl: String?,
)

object UpdateManifestParser {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val SHA256 = Regex("^[0-9a-f]{64}$")

    /**
     * Строга проверка: APK адресът трябва да е HTTPS и на същия хост като
     * манифеста (т.е. в същото хранилище в GitHub), а контролната сума — валидна.
     * Всичко друго се отхвърля — по-добре без обновяване, отколкото с чужд файл.
     */
    fun parse(text: String, manifestUrl: String): Outcome<UpdateInfo> {
        val o: JsonObject = runCatching { json.parseToJsonElement(text).jsonObject }
            .getOrElse { return Outcome.Failure(AppError.Parse("update.json: not an object")) }
        fun str(key: String): String? = runCatching { o[key]?.jsonPrimitive?.content }.getOrNull()?.trim()?.takeIf { it.isNotEmpty() }

        val code = runCatching { o["versionCode"]?.jsonPrimitive?.int }.getOrNull()
        val name = str("versionName")
        val apk = str("apkUrl")
        val sha = str("sha256")?.lowercase()
        if (code == null || code <= 0 || name == null || apk == null || sha == null) {
            return Outcome.Failure(AppError.Parse("update.json: missing fields"))
        }
        if (!SHA256.matches(sha)) return Outcome.Failure(AppError.Parse("update.json: bad sha256"))
        if (!trustedApkUrl(apk, manifestUrl)) return Outcome.Failure(AppError.Parse("update.json: untrusted apkUrl"))
        return Outcome.Success(
            UpdateInfo(
                versionCode = code,
                versionName = name,
                apkUrl = apk,
                sha256 = sha,
                sizeBytes = runCatching { o["size"]?.jsonPrimitive?.longOrNull }.getOrNull()?.takeIf { it > 0 },
                minSdk = runCatching { o["minSdk"]?.jsonPrimitive?.intOrNull }.getOrNull() ?: 1,
                notes = str("notes").orEmpty(),
                releaseUrl = str("releaseUrl")?.takeIf { it.startsWith("https://") },
            ),
        )
    }

    fun trustedApkUrl(apkUrl: String, manifestUrl: String): Boolean {
        val apk = runCatching { URI(apkUrl) }.getOrNull() ?: return false
        val manifest = runCatching { URI(manifestUrl) }.getOrNull() ?: return false
        if (apk.host.isNullOrEmpty() || !apk.host.equals(manifest.host, ignoreCase = true)) return false
        // HTTPS винаги; HTTP се допуска само ако и манифестът е на HTTP (локални тестове).
        return apk.scheme == "https" || (apk.scheme == "http" && manifest.scheme == "http")
    }
}

/** Проверява за нова версия. Не прави нищо друго — изтеглянето и инсталирането са в приложението. */
class UpdateChecker(private val http: HttpFetcher, private val manifestUrl: String) {

    /**
     * @return новата версия, или `null`, ако инсталираната е последна
     * (или новата изисква по-нов Android от [sdkInt]).
     */
    suspend fun check(installedVersionCode: Int, sdkInt: Int): Outcome<UpdateInfo?> {
        if (manifestUrl.isBlank()) return Outcome.Success(null)
        val body = when (val r = http.get(manifestUrl, mapOf("Cache-Control" to "no-cache"))) {
            is Outcome.Failure -> return r
            is Outcome.Success -> r.value
        }
        return when (val parsed = UpdateManifestParser.parse(body.text(), manifestUrl)) {
            is Outcome.Failure -> parsed
            is Outcome.Success -> Outcome.Success(parsed.value.takeIf { isNewer(it, installedVersionCode, sdkInt) })
        }
    }

    companion object {
        fun isNewer(info: UpdateInfo, installedVersionCode: Int, sdkInt: Int): Boolean =
            info.versionCode > installedVersionCode && info.minSdk <= sdkInt
    }
}

object Checksums {
    fun sha256(input: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val buf = ByteArray(64 * 1024)
        while (true) {
            val n = input.read(buf)
            if (n < 0) break
            digest.update(buf, 0, n)
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    fun sha256(file: File): String = file.inputStream().use { sha256(it) }
}
