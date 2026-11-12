package org.chyavorec.domain.quiz

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Файлът `data/app-quiz.json` на сайта (по избор): въпроси, написани от читалището.
 *
 * ```json
 * { "questions": [
 *   { "id": "sabor-1", "text": "Кога е съборът на Яворец?",
 *     "options": ["Първата неделя на юни", "28 август", "6 май"], "answer": 1,
 *     "note": "Съборът е на Голяма Богородица.", "image": "/pictures/sabor.webp", "shuffle": true }
 * ] }
 * ```
 * Приема се и гол масив. `id` — до 60 знака; `text` — 3–240; `options` — 2–4 различни отговора до
 * 120 знака; `answer` — индекс (от 0); `note`, `image` (адрес в сайта или https), `shuffle` — по
 * избор. Грешните записи се пропускат, непознатите ключове — също; липсващ или повреден файл не
 * е грешка (просто няма ръчни въпроси).
 */
object QuizFile {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    /** Разчита файла; `null` — не е JSON с очаквания вид. Записите, които не минават [validate], се пропускат. */
    fun parse(text: String): List<CustomQuestion>? {
        val root = runCatching { json.parseToJsonElement(text) }.getOrNull() ?: return null
        val array: JsonArray = when (root) {
            is JsonArray -> root
            is JsonObject -> (root["questions"] as? JsonArray) ?: return null
            else -> return null
        }
        // Всеки запис поотделно: един повреден не губи останалите.
        val out = ArrayList<CustomQuestion>()
        val seen = HashSet<String>()
        for (el in array) {
            val q = runCatching { json.decodeFromJsonElement(CustomQuestion.serializer(), el.jsonObject) }.getOrNull() ?: continue
            val ok = validate(q) ?: continue
            if (seen.add(ok.id)) out += ok
        }
        return out
    }

    /** Подрязан и проверен въпрос или `null`. */
    fun validate(q: CustomQuestion): CustomQuestion? {
        val id = q.id.trim()
        val text = q.text.trim()
        val options = q.options.map { it.trim() }
        if (id.isEmpty() || id.length > 60) return null
        if (text.length !in 3..240) return null
        if (options.size !in 2..4 || options.any { it.isEmpty() || it.length > 120 }) return null
        if (options.map { it.lowercase() }.toSet().size != options.size) return null
        if (q.answer !in options.indices) return null
        val image = q.image?.trim()?.takeIf { it.isNotEmpty() }
        if (image != null && !(image.startsWith("/") && !image.startsWith("//")) && !image.startsWith("https://")) return null
        return q.copy(id = id, text = text, options = options, note = q.note?.trim()?.take(300)?.takeIf { it.isNotEmpty() }, image = image)
    }

    /** Записът за кеша (същият списък). */
    val listSerializer = ListSerializer(CustomQuestion.serializer())
}
