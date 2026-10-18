package org.chyavorec.core

/** Малки помощни функции за URL адреси. */
object Urls {
    /** Прави абсолютен адрес спрямо [base] и винаги предпочита HTTPS за познатите домейни. */
    fun absolutize(url: String?, base: String): String? {
        if (url.isNullOrBlank()) return null
        val trimmed = url.trim()
        if (trimmed.startsWith("data:") || trimmed.startsWith("javascript:")) return null
        val abs = when {
            trimmed.startsWith("//") -> "https:$trimmed"
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.startsWith("mailto:") || trimmed.startsWith("tel:") -> return trimmed
            trimmed.startsWith("/") -> base.trimEnd('/') + trimmed
            else -> base.trimEnd('/') + "/" + trimmed
        }
        // Адресите на самия сайт следват неговата схема (в production — https);
        // всички външни http:// адреси се повдигат до https.
        return if (base.startsWith("http://") && sameSite(abs, base)) abs else upgradeToHttps(abs)
    }

    /** Приложението позволява само HTTPS; http:// адреси се повдигат. */
    fun upgradeToHttps(url: String): String =
        if (url.startsWith("http://")) "https://" + url.removePrefix("http://") else url

    /** Адресът е от сайта (с или без www) — строгата проверка е в [TrustedLinks.siteLink]. */
    fun sameSite(url: String, base: String): Boolean = TrustedLinks.isSiteUrl(url, base)
}
