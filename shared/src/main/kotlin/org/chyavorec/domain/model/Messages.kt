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
)

@Serializable
enum class MessageAudience {
    /** Всички потребители на приложението. */
    ALL,
    /** Само членове: влезли в профила си или въвели читателска карта. */
    MEMBERS,
}

@Serializable
enum class MessagePriority { NORMAL, HIGH }
