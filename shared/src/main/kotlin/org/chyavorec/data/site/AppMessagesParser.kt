package org.chyavorec.data.site

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.MessageAudience
import org.chyavorec.domain.model.MessagePriority
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** Разчита `/data/app-messages.json`. Невалидните записи се пропускат, не провалят целия файл. */
object AppMessagesParser {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parse(text: String): List<AppMessage>? {
        val array = runCatching { json.parseToJsonElement(text) as? JsonArray }.getOrNull() ?: return null
        return array.mapNotNull { (it as? JsonObject)?.let(::item) }
            .distinctBy { it.id }
            .sortedByDescending { it.createdAt }
    }

    private fun item(o: JsonObject): AppMessage? {
        fun str(key: String) = runCatching { o[key]?.jsonPrimitive?.content }.getOrNull()?.trim()?.takeIf { it.isNotEmpty() }
        val id = str("id") ?: return null
        val title = str("title")?.take(200) ?: return null
        val body = str("body")?.take(4000).orEmpty()
        val created = str("createdAt")?.let { runCatching { Instant.parse(it) }.getOrNull() } ?: return null
        val expires = str("expiresAt")?.take(10)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        return AppMessage(
            id = id,
            title = title,
            body = body,
            audience = if (str("audience").equals("members", ignoreCase = true)) MessageAudience.MEMBERS else MessageAudience.ALL,
            priority = if (str("priority").equals("high", ignoreCase = true)) MessagePriority.HIGH else MessagePriority.NORMAL,
            createdAt = created.toString(),
            expiresOn = expires?.toString(),
            url = str("url")?.takeIf { it.startsWith("https://") },
        )
    }

    /** Какво вижда потребителят: без изтеклите и — ако не е член — без съобщенията само за членове. */
    fun visible(messages: List<AppMessage>, isMember: Boolean, today: LocalDate): List<AppMessage> =
        messages.filter { m ->
            (isMember || m.audience == MessageAudience.ALL) &&
                (m.expiresOn == null || runCatching { !LocalDate.parse(m.expiresOn).isBefore(today) }.getOrDefault(true))
        }

    fun createdAt(m: AppMessage): Instant = runCatching { Instant.parse(m.createdAt) }.getOrDefault(Instant.EPOCH)

    val SOFIA: ZoneId = ZoneId.of("Europe/Sofia")
}
