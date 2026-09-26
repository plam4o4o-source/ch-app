package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Събитие от календара на читалището (chyavorec.org/events). Календарът на
 * сайта е годишен — [recurring] = true означава, че датата се повтаря всяка
 * година, а [date] е най-близкото предстоящо настъпване.
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
    val recurring: Boolean = false,
    val source: EventSource = EventSource.CALENDAR,
)

@Serializable
enum class EventSource { CALENDAR, NEWS }
