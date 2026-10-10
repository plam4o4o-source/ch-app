package org.chyavorec.data.catalog

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogShelf
import org.chyavorec.domain.model.CatalogSnapshot
import java.io.ByteArrayInputStream

/**
 * Разчита `katalog.json`, публикуван от InvLib (buildCatalogPayload/publicBookFields
 * в electron-app/main.js на plam4o4o-source/yavorec-katalog).
 *
 * Формат (кратки ключове):
 * ```
 * { "library": "...", "place": "...", "generated": "2026-09-19",
 *   "items": [ {"inv":399666,"a":"автор","t":"заглавие","s":"подзаглавие","c":"град",
 *               "p":"издател","y":"2016","v":"вид","l":"език","u":"УДК","g":"сигнатура",
 *               "o":"отдел","k":"ключови думи","n":"анотация","cv":"корица","av":1,
 *               "d":"2023-07-13"} ],
 *   "shelves": [ {"name":"...","items":[156,187]} ] }
 * ```
 * Парсерът е толерантен: непознати ключове се пренебрегват, липсващите стават празни,
 * а `inv` и `av` се приемат и като низ/булева стойност. Ключ `i` (ISBN) още не се
 * публикува от InvLib, но се чете, ако бъде добавен.
 */
object KatalogParser {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Serializable
    private data class Payload(
        val library: String = "",
        val place: String = "",
        val generated: String = "",
        val items: List<Item> = emptyList(),
        val shelves: List<Shelf> = emptyList(),
    )

    @Serializable
    private data class Item(
        val inv: JsonElement? = null,
        val a: String? = null,
        val t: String? = null,
        val s: String? = null,
        val c: String? = null,
        val p: String? = null,
        val y: JsonElement? = null,
        val v: String? = null,
        val l: String? = null,
        val u: JsonElement? = null,
        val g: String? = null,
        val o: String? = null,
        val k: String? = null,
        val n: String? = null,
        val cv: String? = null,
        val av: JsonElement? = null,
        val d: String? = null,
        @SerialName("i") val isbn: String? = null,
    )

    @Serializable
    private data class Shelf(val name: String = "", val items: List<JsonElement> = emptyList())

    fun parse(text: String): CatalogSnapshot = build(json.decodeFromString(Payload.serializer(), text))

    /** Направо от суровите байтове (UTF-8) — без междинен низ от няколко MB. */
    @OptIn(ExperimentalSerializationApi::class)
    fun parse(bytes: ByteArray): CatalogSnapshot {
        // UTF-8 BOM (ако файлът е записан с него) не е валиден JSON.
        val skip = if (bytes.size >= 3 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()) 3 else 0
        return build(json.decodeFromStream(Payload.serializer(), ByteArrayInputStream(bytes, skip, bytes.size - skip)))
    }

    private fun build(payload: Payload): CatalogSnapshot {
        val books = payload.items.mapNotNull { it.toBook() }
        return CatalogSnapshot(
            library = payload.library.cleanLibraryName(),
            place = payload.place.trim().trimEnd(',').trim(),
            generatedOn = payload.generated,
            books = books,
            shelves = payload.shelves
                .map { s -> CatalogShelf(s.name.trim(), s.items.mapNotNull { it.asLong() }) }
                .filter { it.name.isNotBlank() && it.invNumbers.isNotEmpty() },
        )
    }

    private fun Item.toBook(): CatalogBook? {
        val invNo = inv.asLong() ?: return null
        val title = t?.trim().orEmpty()
        if (title.isEmpty()) return null
        return CatalogBook(
            inv = invNo,
            author = a?.trim().orEmpty(),
            title = title,
            subtitle = s?.trim().orEmpty(),
            city = c?.trim().orEmpty(),
            publisher = p?.trim().orEmpty(),
            year = y.asText(),
            docType = v?.trim().orEmpty(),
            language = l?.trim().orEmpty(),
            udc = u.asText(),
            callNumber = g?.trim().orEmpty(),
            department = o?.trim().orEmpty(),
            keywords = k?.trim().orEmpty(),
            annotation = n?.trim().orEmpty(),
            coverUrl = cv?.trim()?.takeIf { it.startsWith("https://") || it.startsWith("http://") }
                ?.replaceFirst("http://", "https://").orEmpty(),
            available = av.asBool(),
            registeredOn = d?.trim().orEmpty(),
            isbn = isbn?.trim().orEmpty(),
        )
    }

    private fun JsonElement?.asLong(): Long? {
        val p = this as? JsonPrimitive ?: return null
        return p.longOrNull ?: p.contentOrNull?.trim()?.toLongOrNull()
    }

    private fun JsonElement?.asText(): String {
        val p = this as? JsonPrimitive ?: return ""
        return p.contentOrNull?.trim().orEmpty()
    }

    private fun JsonElement?.asBool(): Boolean {
        val p = this as? JsonPrimitive ?: return false
        return p.intOrNull?.let { it > 0 } ?: p.booleanOrNull ?: (p.contentOrNull == "1")
    }

    /** InvLib оставя „Библиотека при НЧ “ с висящ интервал, ако името не е попълнено. */
    private fun String.cleanLibraryName(): String = trim().trimEnd(',', '-', '–').trim()
}
