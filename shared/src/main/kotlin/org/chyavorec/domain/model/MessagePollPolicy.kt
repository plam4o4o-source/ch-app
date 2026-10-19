package org.chyavorec.domain.model

import org.chyavorec.core.Hashing

/**
 * Колко често фоновата проверка пита сайта за нови съобщения — чисти изчисления, без Android.
 *
 * - задачата е еднократна и се планира точно за следващата дължима проверка ([nextRunDelay]),
 *   а в края на всяка проверка — отново (без будене на всеки час само за да излезе веднага);
 *   ако все пак се събуди рано ([isDue] = false) — излиза без мрежа;
 * - непроменен файл със съобщения → интервалът се удвоява до [MAX] ([next]);
 * - ново/променено съобщение или отваряне на приложението → отново [BASE];
 * - приложението не е отваряно [DORMANT_AFTER] → проверките спират ([isDormant]),
 *   докато потребителят не го отвори отново; задачата тогава изобщо не се планира.
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
     * След колко ms да тръгне следващата проверка (еднократна задача в WorkManager), или
     * `null` — да не се планира изобщо (известията за съобщения са изключени/забранени или
     * приложението „спи“ — [isDormant]). Дължимата проверка — веднага (0).
     * [minDelayMillis] — най-малко толкова: след неуспешна проверка (грешка/офлайн) новият
     * опит е след [BASE_MILLIS], не веднага.
     */
    fun nextRunDelay(
        enabled: Boolean,
        nowMillis: Long,
        lastOpenMillis: Long,
        lastPollMillis: Long,
        intervalMillis: Long,
        minDelayMillis: Long = 0,
    ): Long? {
        if (!enabled || isDormant(nowMillis, lastOpenMillis)) return null
        val wait = if (isDue(nowMillis, lastPollMillis, intervalMillis)) 0L else lastPollMillis + normalize(intervalMillis) - nowMillis
        return wait.coerceAtLeast(minDelayMillis)
    }

    /**
     * Интервалът, който се записва след успешна проверка — от ЗАПИСАНОТО в момента на записа
     * ([storedIntervalMillis], [storedOpenMillis]), не от прочетеното преди заявката
     * ([startIntervalMillis], [startOpenMillis]). Ако междувременно приложението е отворено
     * (записано е по-ново отваряне или интервалът вече е друг — markAppOpened го връща на
     * [BASE_MILLIS]), отварянето печели: [BASE_MILLIS], без удвояване.
     */
    fun intervalAfterPoll(
        startIntervalMillis: Long,
        startOpenMillis: Long,
        storedIntervalMillis: Long,
        storedOpenMillis: Long,
        changed: Boolean,
    ): Long {
        val openedMeanwhile = storedOpenMillis > startOpenMillis || normalize(storedIntervalMillis) != normalize(startIntervalMillis)
        return if (openedMeanwhile) BASE_MILLIS else next(storedIntervalMillis, changed)
    }

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
