package org.chyavorec.data.site

import java.net.URI
import java.net.URLEncoder

/**
 * Оптимизирани снимки от chyavorec.org (Vercel Image Optimization, договор „B“ от вълна 3).
 *
 * Сайтът разрешава преоразмеряване само за локални пътища под `/assets/` и `/pictures/`
 * и само за ширините в [WIDTHS]. За такава снимка [optimized] връща
 * `https://chyavorec.org/_vercel/image?url=<URL-кодиран път>&w=<ширина>&q=75`, където
 * ширината е най-малката от [WIDTHS], която е ≥ нужната (таван [MAX_WIDTH]).
 * Всичко друго (чужди адреси, други пътища, адреси със заявка, SVG) се връща без промяна.
 *
 * Ако оптимизираният адрес върне грешка (напр. оптимизацията е изключена на сървъра),
 * мястото, което зарежда снимката, еднократно опитва оригиналния адрес.
 */
object SiteImageUrls {

    /** Разрешените ширини (`images.sizes` във vercel.json на сайта), във възходящ ред. */
    val WIDTHS: List<Int> = listOf(128, 256, 384, 640, 828, 1080, 1600)
    val MAX_WIDTH: Int = WIDTHS.last()
    const val QUALITY: Int = 75

    private const val ENDPOINT = "https://chyavorec.org/_vercel/image"
    private val HOSTS = setOf("chyavorec.org", "www.chyavorec.org")
    private val PREFIXES = listOf("/assets/", "/pictures/")

    /**
     * Адрес за показване на [url] с ширина [widthPx] физически пиксела. `null` → `null`;
     * неподходящ адрес или ширина ≤ 0 → [url] без промяна.
     */
    fun optimized(url: String?, widthPx: Int): String? {
        if (url == null || widthPx <= 0) return url
        val path = optimizablePath(url) ?: return url
        // encode(String, String): вариантът с Charset е от API 33 на Android (minSdk 26).
        val encoded = URLEncoder.encode(path, "UTF-8").replace("+", "%20")
        return "$ENDPOINT?url=$encoded&w=${widthFor(widthPx)}&q=$QUALITY"
    }

    /** Най-малката разрешена ширина ≥ [widthPx]; над тавана — [MAX_WIDTH]. */
    fun widthFor(widthPx: Int): Int = WIDTHS.firstOrNull { it >= widthPx } ?: MAX_WIDTH

    /** Декодираният път на снимка от сайта, която може да се оптимизира, иначе `null`. */
    private fun optimizablePath(url: String): String? {
        val uri = runCatching { URI(url.trim()) }.getOrNull() ?: return null
        val scheme = uri.scheme?.lowercase()
        if (scheme != "https" && scheme != "http") return null
        if (uri.host?.lowercase() !in HOSTS) return null
        if (uri.port != -1 && uri.port != if (scheme == "https") 443 else 80) return null
        if (uri.rawUserInfo != null) return null
        // Адрес със заявка (напр. ?v=2) не съвпада с разрешения шаблон — оставя се оригиналът.
        if (uri.rawQuery != null) return null
        val path = uri.path ?: return null
        if (PREFIXES.none { path.startsWith(it) && path.length > it.length }) return null
        if (path.split('/').any { it == ".." || it == "." }) return null
        // SVG не се преоразмерява от оптимизатора (връща грешка) — направо оригиналът.
        if (path.lowercase().endsWith(".svg")) return null
        return path
    }
}
