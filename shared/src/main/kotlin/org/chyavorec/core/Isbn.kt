package org.chyavorec.core

/**
 * ISBN: нормализиране (без тирета и интервали), проверка на контролната цифра и
 * преобразуване ISBN-10 ↔ ISBN-13. Каталогът пази ISBN както е въведен в
 * InvLib (с тирета, понякога ISBN-10), а баркодът на книгата е винаги EAN-13.
 */
object Isbn {
    /** Само цифри и „X“ (контролна цифра на ISBN-10). */
    fun normalize(raw: String?): String = TextNormalizer.digits(raw)

    /** EAN-13 от баркода на книга: започва с 978 или 979 (Bookland). */
    fun isBookEan(code: String): Boolean {
        val n = normalize(code)
        return n.length == 13 && (n.startsWith("978") || n.startsWith("979")) && isValid13(n)
    }

    fun isValid13(n: String): Boolean {
        if (n.length != 13 || !n.all { it.isDigit() }) return false
        return checksum13(n.substring(0, 12)) == n[12]
    }

    fun isValid10(n: String): Boolean {
        if (n.length != 10 || !n.substring(0, 9).all { it.isDigit() }) return false
        return checksum10(n.substring(0, 9)) == n[9]
    }

    /** ISBN-10 → ISBN-13 (префикс 978); `null`, ако входът не е валиден ISBN-10. */
    fun toIsbn13(isbn10: String?): String? {
        val n = normalize(isbn10)
        if (!isValid10(n)) return null
        val body = "978" + n.substring(0, 9)
        return body + checksum13(body)
    }

    /** ISBN-13 с префикс 978 → ISBN-10; `null` за 979-… (няма ISBN-10 форма) или невалиден вход. */
    fun toIsbn10(isbn13: String?): String? {
        val n = normalize(isbn13)
        if (!isValid13(n) || !n.startsWith("978")) return null
        val body = n.substring(3, 12)
        return body + checksum10(body)
    }

    /**
     * Всички еквивалентни форми на един ISBN (нормализирани): самият той плюс
     * другата дължина, ако съществува. За невалиден/непълен вход — само
     * нормализираният низ (ако не е празен), за да работи и частично търсене.
     */
    fun candidates(raw: String?): Set<String> {
        val n = normalize(raw)
        if (n.isEmpty()) return emptySet()
        val out = LinkedHashSet<String>()
        out += n
        toIsbn13(n)?.let { out += it }
        toIsbn10(n)?.let { out += it }
        return out
    }

    private fun checksum13(body12: String): Char {
        var sum = 0
        for (i in body12.indices) sum += body12[i].digitToInt() * if (i % 2 == 0) 1 else 3
        return ((10 - sum % 10) % 10).digitToChar()
    }

    private fun checksum10(body9: String): Char {
        var sum = 0
        for (i in body9.indices) sum += body9[i].digitToInt() * (10 - i)
        val check = (11 - sum % 11) % 11
        return if (check == 10) 'X' else check.digitToChar()
    }
}

/** Какво е разчел скенерът: ISBN на книга, инвентарен номер или нещо непознато. */
sealed interface ScanTarget {
    data class Isbn(val isbn: String) : ScanTarget
    data class Inventory(val inv: Long) : ScanTarget
    data class Unknown(val text: String) : ScanTarget
}

/**
 * Превод на стойността от баркод/QR към цел за търсене в каталога:
 * - EAN-13 с префикс 978/979 → ISBN;
 * - само цифри (до 9) → инвентарен номер (етикетът на библиотеката);
 * - адрес на електронния каталог (chyavorec.org) с `inv=`/`isbn=`/`q=` в
 *   заявката или фрагмента → извлича стойността;
 * - друго → [ScanTarget.Unknown].
 */
object ScanCodes {
    private val param = Regex("(?:^|[?#&;])(inv|isbn|q|book)=([^&#;]+)", RegexOption.IGNORE_CASE)

    fun resolve(raw: String): ScanTarget {
        val value = raw.trim()
        if (value.isEmpty()) return ScanTarget.Unknown(value)
        val digits = Isbn.normalize(value)
        if (digits.length == value.length || value.all { it.isDigit() || it == '-' || it == ' ' }) {
            if (Isbn.isBookEan(digits)) return ScanTarget.Isbn(digits)
            if (Isbn.isValid10(digits)) return ScanTarget.Isbn(digits)
            if (digits.isNotEmpty() && digits.length <= 9 && digits.all { it.isDigit() }) {
                digits.trimStart('0').toLongOrNull()?.takeIf { it > 0 }?.let { return ScanTarget.Inventory(it) }
            }
            return ScanTarget.Unknown(value)
        }
        if (isCatalogUrl(value)) {
            for (m in param.findAll(value)) {
                val key = m.groupValues[1].lowercase()
                val v = decode(m.groupValues[2])
                when (key) {
                    "inv", "book" -> v.filter { it.isDigit() }.trimStart('0').toLongOrNull()?.takeIf { it > 0 }?.let { return ScanTarget.Inventory(it) }
                    "isbn" -> if (Isbn.normalize(v).length >= 10) return ScanTarget.Isbn(Isbn.normalize(v))
                    "q" -> return resolve(v).let { if (it is ScanTarget.Unknown) ScanTarget.Unknown(v) else it }
                }
            }
            // /elektronen-katalog/156 или …/book/156
            Regex("/(?:book|kniga|inv)/(\\d{1,9})").find(value)?.groupValues?.get(1)?.toLongOrNull()?.let { return ScanTarget.Inventory(it) }
        }
        return ScanTarget.Unknown(value)
    }

    private fun isCatalogUrl(value: String): Boolean {
        val lower = value.lowercase()
        return (lower.startsWith("http://") || lower.startsWith("https://")) &&
            (lower.contains("chyavorec.org") || lower.contains("katalog") || lower.contains("catalog"))
    }

    private fun decode(s: String): String = runCatching { java.net.URLDecoder.decode(s, "UTF-8") }.getOrDefault(s)
}
