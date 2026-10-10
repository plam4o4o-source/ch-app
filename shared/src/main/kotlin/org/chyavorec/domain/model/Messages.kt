package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Съобщение от читалището до потребителите на приложението — изпраща се от
 * админ панела на сайта (раздел „Съобщения до приложението“) и се публикува в
 * `/data/app-messages.json`.
 */
@Serializable
data class AppMessage(
    val id: String,
    val title: String,
    /** Обикновен текст (без HTML). */
    val body: String,
    val audience: MessageAudience = MessageAudience.ALL,
    val priority: MessagePriority = MessagePriority.NORMAL,
    /** ISO-8601 момент на създаване. */
    val createdAt: String,
    /** Последен ден на валидност (yyyy-MM-dd, включително) или null. */
    val expiresOn: String? = null,
    val url: String? = null,
    /**
     * Лично съобщение от библиотеката до влезлия читател (от InvLib, не от сайта).
     * Никога не идва от `/data/app-messages.json` — парсерът на сайта не чете това поле.
     */
    val personal: Boolean = false,
)

/**
 * Обща входяща кутия: съобщенията от сайта + личните от библиотеката.
 * Личните получават id с префикс [PERSONAL_PREFIX], за да не се бъркат с тези от сайта.
 */
object Inbox {
    const val PERSONAL_PREFIX = "p:"

    fun personalId(messageId: String): String = PERSONAL_PREFIX + messageId

    /** Id-то в InvLib за id от кутията, или `null`, ако съобщението е от сайта. */
    fun serverId(inboxId: String): String? =
        if (inboxId.startsWith(PERSONAL_PREFIX)) inboxId.removePrefix(PERSONAL_PREFIX).takeIf { it.isNotEmpty() } else null

    fun toAppMessage(m: ReaderMessage): AppMessage = AppMessage(
        id = personalId(m.id),
        title = m.title,
        body = m.text,
        createdAt = m.at,
        personal = true,
    )

    /** Слива двата списъка, най-новите първо (без дата → най-отдолу). */
    fun merge(site: List<AppMessage>, personal: List<ReaderMessage>): List<AppMessage> {
        if (personal.isEmpty()) return site
        return (site + personal.map(::toAppMessage))
            .distinctBy { it.id }
            .sortedByDescending { m -> runCatching { java.time.Instant.parse(m.createdAt) }.getOrDefault(java.time.Instant.EPOCH) }
    }
}

@Serializable
enum class MessageAudience {
    /** Всички потребители на приложението. */
    ALL,
    /** Само членове: влезли в профила си или въвели читателска карта. */
    MEMBERS,
}

@Serializable
enum class MessagePriority { NORMAL, HIGH }
