package org.chyavorec.core

import java.text.Normalizer

/**
 * Нормализиране на текст за търсене: малки букви, без ударения/диакритика,
 * без кавички и излишни интервали. „Под игото“ и под  ИГОТО съвпадат.
 */
object TextNormalizer {
    private val diacritics = Regex("\\p{Mn}+")
    private val punctuation = Regex("[\"'„“”«»‚‘’`´.,:;!?()\\[\\]{}/\\\\|*_#~-]+")
    private val spaces = Regex("\\s+")

    fun normalize(input: String?): String {
        if (input.isNullOrBlank()) return ""
        val lower = input.lowercase()
            .replace('ё', 'е')
            .replace('ѝ', 'и')
        // NFD разделя „й“ на „и“ + знак, затова го пазим преди премахването на диакритиката.
        val protected = lower.replace('й', '\u0001')
        val stripped = Normalizer.normalize(protected, Normalizer.Form.NFD)
            .replace(diacritics, "")
            .replace('\u0001', 'й')
        return stripped.replace(punctuation, " ").replace(spaces, " ").trim()
    }

    /** Думите от заявката, всяка от които трябва да се съдържа в текста. */
    fun tokens(query: String?): List<String> =
        normalize(query).split(' ').filter { it.isNotBlank() }

    /** Цифрите от низ — за търсене по ISBN/инвентарен номер. */
    fun digits(input: String?): String = input?.filter { it.isDigit() || it == 'X' || it == 'x' }?.uppercase() ?: ""
}
