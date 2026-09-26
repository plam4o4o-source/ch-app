package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Събитие на читалището. Датата и часът се извличат от текста на публикацията
 * на сайта — затова [dateIsExplicit] казва дали датата е намерена в текста
 * (true) или е взета датата на публикуване (false).
 */
@Serializable
data class Event(
    val id: String,
    val title: String,
    /** ISO дата (yyyy-MM-dd). */
    val date: String?,
    /** HH:mm или null. */
    val time: String? = null,
    val place: String? = null,
    val organizer: String? = null,
    val description: String = "",
    val imageUrl: String? = null,
    val sourceUrl: String,
    val category: String? = null,
    val dateIsExplicit: Boolean = true,
    val source: EventSource = EventSource.NEWS,
)

@Serializable
enum class EventSource { NEWS, EVENTS_PAGE }
