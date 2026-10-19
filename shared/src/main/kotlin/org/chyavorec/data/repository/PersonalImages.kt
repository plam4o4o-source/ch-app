package org.chyavorec.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.chyavorec.core.logFailure
import org.chyavorec.domain.repository.CachedBytes
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.PayloadCache

/**
 * Адресите на личните изображения в читателските данни: кориците на заетите книги
 * (`coverUrl`) и снимката в профила (`photoUrl`) — на всяко ниво на JSON-а от моста.
 */
object PersonalImages {
    private val FIELDS = setOf("coverUrl", "photoUrl")
    private val json = Json { ignoreUnknownKeys = true }

    /** Адресите (само http/https) в [text]; не-JSON или без такива полета — празно. */
    fun urlsIn(text: String): Set<String> {
        if (FIELDS.none { it in text }) return emptySet()
        val root = runCatching { json.parseToJsonElement(text) }.getOrNull() ?: return emptySet()
        return buildSet { collect(root, this) }
    }

    private fun collect(e: JsonElement, out: MutableSet<String>) {
        when (e) {
            is JsonObject -> e.forEach { (k, v) ->
                val url = (v as? JsonPrimitive)?.takeIf { it.isString }?.content?.trim()
                if (k in FIELDS && url != null && (url.startsWith("https://") || url.startsWith("http://"))) out += url
                else collect(v, out)
            }
            is JsonArray -> e.forEach { collect(it, out) }
            else -> Unit
        }
    }
}

/**
 * Читателският кеш, който помни адресите на личните изображения ([PersonalImages]) от всеки
 * запис — и на вече върнати книги (списъкът се пази отделно, шифрован заедно с останалото).
 * При [clear] (изход, нов читател, старт без сесия след „не ме помни“) първо се вика
 * [evict] с тях — само те се махат от кеша на изображенията, а снимките от сайта и
 * кориците от каталога остават (иначе всичко се тегли наново, до 80 MB).
 */
class PersonalImageTrackingCache(
    private val base: PayloadCache,
    private val evict: suspend (Set<String>) -> Unit,
) : PayloadCache by base {
    private val lock = Mutex()
    /** Запомнените в този процес (без четене от диска при всеки запис). */
    @Volatile private var known: Set<String>? = null

    override suspend fun write(key: String, text: String) {
        base.write(key, text)
        remember(text)
    }

    override suspend fun writeBytes(key: String, bytes: ByteArray) {
        base.writeBytes(key, bytes)
        remember(String(bytes, Charsets.UTF_8))
    }

    // И при четене: данните, записани от версия без този списък, също се запомнят.
    override suspend fun read(key: String): CachedPayload? = base.read(key)?.also { if (key != KEY) remember(it.text) }

    override suspend fun readBytes(key: String): CachedBytes? =
        base.readBytes(key)?.also { if (key != KEY) remember(String(it.bytes, Charsets.UTF_8)) }

    override suspend fun clear() {
        val urls = lock.withLock {
            val all = runCatching { stored() }.logFailure("cache.personal-images").getOrDefault(emptySet())
            base.clear()
            known = emptySet()
            all
        }
        if (urls.isNotEmpty()) runCatching { evict(urls) }.logFailure("cache.personal-images")
    }

    private suspend fun remember(text: String) {
        val found = PersonalImages.urlsIn(text)
        if (found.isEmpty()) return
        lock.withLock {
            val current = known ?: stored()
            if (current.containsAll(found)) {
                known = current
                return
            }
            val next = current + found
            base.write(KEY, json.encodeToString(LIST, next.toList()))
            known = next
        }
    }

    private suspend fun stored(): Set<String> =
        base.read(KEY)?.text?.let { t -> runCatching { json.decodeFromString(LIST, t).toSet() }.getOrNull() }.orEmpty()

    private companion object {
        /** Извън пространствата на читателите (`r…/ключ`) — не се вижда през [ReaderScopedCache]. */
        const val KEY = "personal-images"
        val json = Json
        val LIST = ListSerializer(String.serializer())
    }
}
