package org.chyavorec.data.update

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
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
    /** „Какво ново“ на български (`notes` — четат го и старите версии на приложението). */
    val notes: String,
    val releaseUrl: String?,
    /**
     * Обновяванията до тази версия са спрени (`"paused": true` — workflow-ът
     * update-control.yml, виж docs/RELEASE.md): приложението не я предлага и не я тегли.
     */
    val paused: Boolean = false,
    /** „Какво ново“ на английски (`notes_en`); празно — само българският текст. */
    val notesEn: String = "",
    /** Профилът за бърз старт като dex metadata (`dm`) — по един за всеки формат на ART; по желание. */
    val dexMetadata: List<DexMetadata> = emptyList(),
) {
    /** Бележките на езика на интерфейса; без английски текст — българският. */
    fun notesFor(english: Boolean): String = if (english && notesEn.isNotBlank()) notesEn else notes

    /** .dm файлът за този Android (първият, чиито граници го включват); под Android 9 — никой. */
    fun dexMetadataFor(sdkInt: Int): DexMetadata? =
        if (sdkInt < DexMetadata.MIN_SDK) null else dexMetadata.firstOrNull { it.matches(sdkInt) }
}

/**
 * Dex metadata (`.dm`) към APK-а на версията: профилът за бърз старт във формата на ART за
 * Android [minSdk]..[maxSdk] (`null` — и всички по-нови). Инсталира се заедно с APK-а и тогава
 * приложението е компилирано още при обновяването. Само подобрение: без него (или при грешка)
 * обновяването е същото.
 */
data class DexMetadata(val minSdk: Int, val maxSdk: Int?, val url: String, val sha256: String, val sizeBytes: Long?) {
    fun matches(sdkInt: Int): Boolean = sdkInt >= minSdk && (maxSdk == null || sdkInt <= maxSdk)

    companion object {
        /** Инсталиране на .dm през PackageInstaller — Android 9+. */
        const val MIN_SDK = 28
        /** Профилите са стотици KB; по-голям файл не е наш. */
        const val MAX_BYTES = 16L * 1024 * 1024
    }
}

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
                paused = flag(o["paused"]),
                notesEn = str("notes_en").orEmpty(),
                dexMetadata = dexMetadata(o["dm"], manifestUrl),
            ),
        )
    }

    /**
     * Списъкът `dm` — по желание: липсващ, повреден или с непознат вид — празен (старите
     * update.json и старите версии на приложението не го познават). Запис без валидни граници,
     * HTTPS адрес от същия хост или сума се пропуска; останалите остават.
     */
    private fun dexMetadata(e: JsonElement?, manifestUrl: String): List<DexMetadata> {
        val entries = runCatching { e?.jsonArray }.getOrNull() ?: return emptyList()
        return entries.mapNotNull { item ->
            runCatching {
                val o = item.jsonObject
                fun str(key: String): String? = o[key]?.jsonPrimitive?.takeIf { it.isString }?.content?.trim()?.takeIf { it.isNotEmpty() }
                val min = o["minSdk"]?.jsonPrimitive?.intOrNull ?: return@runCatching null
                val max = o["maxSdk"]?.jsonPrimitive?.intOrNull
                val url = str("url") ?: return@runCatching null
                val sha = str("sha256")?.lowercase() ?: return@runCatching null
                val size = o["size"]?.jsonPrimitive?.longOrNull
                when {
                    min < DexMetadata.MIN_SDK || (max != null && max < min) -> null
                    !SHA256.matches(sha) || !trustedApkUrl(url, manifestUrl) -> null
                    size != null && (size <= 0 || size > DexMetadata.MAX_BYTES) -> null
                    else -> DexMetadata(min, max, url, sha, size)
                }
            }.getOrNull()
        }
    }

    /** `true`, `"true"` или `1` → да; липсващо, `null` или каквото и да е друго → не. */
    private fun flag(e: JsonElement?): Boolean {
        val p = e as? JsonPrimitive ?: return false
        return p.booleanOrNull ?: (p.content.trim().lowercase() in setOf("true", "1"))
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
     * (или новата изисква по-нов Android от [sdkInt], или обновяванията са спрени).
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
        /** Спряната ([UpdateInfo.paused]) версия не се брои за нова — до „resume“ приложението е „актуално“. */
        fun isNewer(info: UpdateInfo, installedVersionCode: Int, sdkInt: Int): Boolean =
            !info.paused && info.versionCode > installedVersionCode && info.minSdk <= sdkInt
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
