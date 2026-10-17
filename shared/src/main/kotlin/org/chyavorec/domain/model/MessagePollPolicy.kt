package org.chyavorec.domain.model

import org.chyavorec.core.Hashing

/**
 * Колко често фоновата проверка пита сайта за нови съобщения — чисти изчисления, без Android.
 *
 * - задачата се буди на ~[BASE] (WorkManager), но истинската проверка е само когато е
 *   „дължима“ ([isDue]) — иначе работникът излиза веднага, без мрежа;
 * - непроменен файл със съобщения → интервалът се удвоява до [MAX] ([next]);
 * - ново/променено съобщение или отваряне на приложението → отново [BASE];
 * - приложението не е отваряно [DORMANT_AFTER] → проверките спират ([isDormant]),
 *   докато потребителят не го отвори отново.
 */
object MessagePollPolicy {
    const val BASE_MILLIS = 60 * 60 * 1000L
    const val MAX_MILLIS = 6 * BASE_MILLIS
    const val DORMANT_AFTER_MILLIS = 14 * 24 * BASE_MILLIS
    /** WorkManager буди с отклонение (flex) — почти дължимата проверка се смята за дължима. */
    const val SLACK_MILLIS = 10 * 60 * 1000L

    /** Следващият интервал: при промяна — [BASE_MILLIS], иначе двойно (най-много [MAX_MILLIS]). */
    fun next(intervalMillis: Long, changed: Boolean): Long =
        if (changed) BASE_MILLIS else (normalize(intervalMillis) * 2).coerceAtMost(MAX_MILLIS)

    /** Запомненият интервал, ограничен в [BASE_MILLIS]..[MAX_MILLIS] (0/повреден → [BASE_MILLIS]). */
    fun normalize(intervalMillis: Long): Long = intervalMillis.coerceIn(BASE_MILLIS, MAX_MILLIS)

    /** Дали е време за проверка. Без предишна проверка или с върнат назад часовник — да. */
    fun isDue(nowMillis: Long, lastPollMillis: Long, intervalMillis: Long): Boolean =
        lastPollMillis <= 0L || lastPollMillis > nowMillis || nowMillis - lastPollMillis >= normalize(intervalMillis) - SLACK_MILLIS

    /** Приложението не е отваряно отдавна. Неизвестно време (0) или часовник назад — не. */
    fun isDormant(nowMillis: Long, lastOpenMillis: Long): Boolean =
        lastOpenMillis in 1..nowMillis && nowMillis - lastOpenMillis >= DORMANT_AFTER_MILLIS

    /**
     * Отпечатък на съобщенията от сайта — ако е същият като предишния, файлът не е
     * променен. Не зависи от реда във файла.
     */
    fun signature(messages: List<AppMessage>): String {
        val text = messages
            .map { m -> listOf(m.id, m.createdAt, m.title, m.body, m.priority.name, m.audience.name, m.expiresOn.orEmpty(), m.url.orEmpty()).joinToString("\u001F") }
            .sorted()
            .joinToString("\u001E")
        return Hashing.hex(Hashing.sha256(text.toByteArray()), 12)
    }
}
