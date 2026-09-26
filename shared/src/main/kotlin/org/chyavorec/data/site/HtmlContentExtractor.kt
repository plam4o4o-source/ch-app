package org.chyavorec.data.site

import org.chyavorec.core.Urls
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.TextRun
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * Превръща HTML съдържание (страница от chyavorec.org или тялото на новина от
 * /data/news.json) в структурирани [ContentBlock]-ове. Сайтът е статичен (Vercel,
 * генериран от scripts/build-preview.py) и съдържанието на всяка страница стои в
 * `#hp .hp-inner`; парсерът маха навигация/скриптове/формуляри и пренася само
 * смислово съдържание — заглавия, параграфи, списъци, снимки, цитати, файлове.
 */
class HtmlContentExtractor(private val baseUrl: String) {

    /** Контейнери по приоритет: съдържанието на страницата, после общи резервни варианти. */
    private val containerSelectors = listOf("#hp .hp-inner", "#hp", "main", "article", "#content")

    /** Елементи, които никога не са съдържание. */
    private val junkSelector = listOf(
        "script", "style", "noscript", "iframe", "form", "input", "button", "select", "textarea",
        "nav", "header", "footer", "svg", "object", "embed",
        ".mobile-nav", ".scroll-progress", ".btm-bar", ".bti-fab", ".yv-lnk-bar", ".yv-lnk-net",
        "[aria-hidden=true]", "[style*=display:none]", "[style*=display: none]",
        ".share", ".breadcrumbs", ".lb-image", "#lbImg",
    ).joinToString(",")

    fun findContainer(doc: Document): Element {
        for (sel in containerSelectors) {
            val candidates = doc.select(sel)
            val best = candidates.maxByOrNull { it.text().length }
            if (best != null && best.text().length > 40) return best
        }
        return doc.body() ?: doc
    }

    /** Заглавието на страницата: `<title>` до „ | “ (сайтът го поддържа чисто), иначе h1. */
    fun title(doc: Document): String {
        val t = doc.title().substringBefore(" | ").substringBefore(" – НЧ").trim()
        if (t.isNotEmpty()) return t
        val og = doc.selectFirst("meta[property=og:title]")?.attr("content")?.substringBefore(" | ")?.trim()
        if (!og.isNullOrEmpty()) return og
        return findContainer(doc).selectFirst("h1")?.text()?.trim().orEmpty()
    }

    fun extract(doc: Document): List<ContentBlock> = extract(findContainer(doc))

    fun extract(root: Element): List<ContentBlock> {
        val copy = root.clone()
        copy.select(junkSelector).remove()
        // Анимираните броячи на сайта са „0“ в HTML-а; истинската стойност е в data-target.
        copy.select("[data-target]").forEach { el ->
            val target = el.attr("data-target")
            if (target.isNotBlank() && el.text().trim().let { it.isEmpty() || it == "0" }) el.text(target)
        }
        val blocks = ArrayList<ContentBlock>()
        walk(copy, blocks)
        return blocks.dedupe()
    }

    /** Всички снимки от съдържанието (за галерията на статията). */
    fun images(root: Element): List<String> =
        root.select("img").mapNotNull { imageUrl(it) }.distinct()

    private fun walk(el: Element, out: MutableList<ContentBlock>) {
        val inline = ArrayList<TextRun>()
        fun flush() {
            val runs = inline.normalizeRuns()
            if (runs.isNotEmpty()) out += ContentBlock.Paragraph(runs)
            inline.clear()
        }
        for (child in el.childNodes()) {
            when (child) {
                is TextNode -> {
                    val t = child.text()
                    if (t.isNotBlank() || inline.isNotEmpty()) inline += TextRun(t)
                }
                is Element -> {
                    val tag = child.normalName()
                    when {
                        tag in HEADINGS -> {
                            flush()
                            val text = child.text().trim()
                            if (text.isNotEmpty()) out += ContentBlock.Heading(tag[1].digitToInt().coerceIn(1, 4), text)
                        }
                        tag == "p" && isSoleFileLink(child) -> {
                            flush()
                            val a = child.selectFirst("a")!!
                            Urls.absolutize(a.attr("href"), baseUrl)?.let { out += ContentBlock.LinkButton(a.text().trim(), it) }
                        }
                        tag == "p" -> {
                            flush()
                            child.select("img").forEach { img -> imageUrl(img)?.let { out += ContentBlock.Image(it, img.attr("alt").ifBlank { null }) } }
                            val runs = inlineRuns(child).normalizeRuns()
                            if (runs.isNotEmpty()) out += ContentBlock.Paragraph(runs)
                        }
                        tag == "ul" || tag == "ol" -> {
                            flush()
                            val items = child.children().filter { it.normalName() == "li" }
                                .map { inlineRuns(it).normalizeRuns() }.filter { it.isNotEmpty() }
                            if (items.isNotEmpty()) out += ContentBlock.BulletList(items, ordered = tag == "ol")
                        }
                        tag == "blockquote" -> {
                            flush()
                            val t = child.text().trim()
                            if (t.isNotEmpty()) out += ContentBlock.Quote(t)
                        }
                        tag == "img" -> {
                            flush()
                            imageUrl(child)?.let { out += ContentBlock.Image(it, child.attr("alt").ifBlank { null }) }
                        }
                        tag == "figure" -> {
                            flush()
                            val img = child.selectFirst("img")
                            val caption = child.selectFirst("figcaption")?.text()?.trim()?.ifBlank { null }
                            img?.let { imageUrl(it) }?.let { out += ContentBlock.Image(it, caption) }
                        }
                        tag == "a" && isFileLink(child) -> {
                            flush()
                            val url = Urls.absolutize(child.attr("href"), baseUrl)
                            val text = child.text().trim().ifBlank { "Отвори файла" }
                            if (url != null) out += ContentBlock.LinkButton(text, url)
                        }
                        tag == "br" -> inline += TextRun("\n")
                        tag == "table" -> {
                            flush()
                            child.select("tr").forEach { tr ->
                                val cells = tr.select("th,td").map { it.text().trim() }.filter { it.isNotEmpty() }
                                if (cells.isNotEmpty()) out += ContentBlock.Paragraph(listOf(TextRun(cells.joinToString(" · "))))
                            }
                        }
                        tag in INLINE -> inline += inlineRuns(child, bold = tag in BOLD, italic = tag in ITALIC)
                        else -> {
                            // Блоков контейнер (div, section, …): ако съдържа само inline
                            // съдържание — един параграф; иначе — рекурсия.
                            if (hasBlockChildren(child)) {
                                flush()
                                walk(child, out)
                            } else {
                                flush()
                                child.select("img").forEach { img -> imageUrl(img)?.let { out += ContentBlock.Image(it) } }
                                val runs = inlineRuns(child).normalizeRuns()
                                if (runs.isNotEmpty()) out += ContentBlock.Paragraph(runs)
                            }
                        }
                    }
                }
            }
        }
        flush()
    }

    private fun hasBlockChildren(el: Element): Boolean =
        el.children().any { it.normalName() !in INLINE && it.normalName() != "br" && it.normalName() != "img" }

    private fun inlineRuns(el: Element, bold: Boolean = false, italic: Boolean = false, url: String? = null): List<TextRun> {
        val runs = ArrayList<TextRun>()
        fun visit(node: Node, b: Boolean, i: Boolean, link: String?) {
            when (node) {
                is TextNode -> if (node.text().isNotEmpty()) runs += TextRun(node.text(), b, i, link)
                is Element -> {
                    val tag = node.normalName()
                    if (tag == "br") { runs += TextRun("\n", b, i, link); return }
                    if (tag == "img" || tag == "script" || tag == "style") return
                    val nb = b || tag in BOLD
                    val ni = i || tag in ITALIC
                    val nl = if (tag == "a") Urls.absolutize(node.attr("href"), baseUrl)?.takeIf { isSafeLink(it) } ?: link else link
                    node.childNodes().forEach { visit(it, nb, ni, nl) }
                }
            }
        }
        visit(el, bold, italic, url)
        return runs
    }

    private fun isSafeLink(url: String) =
        url.startsWith("https://") || url.startsWith("mailto:") || url.startsWith("tel:")

    private fun isSoleFileLink(p: Element): Boolean {
        val links = p.select("a[href]")
        return links.size == 1 && isFileLink(links[0]) && p.text().trim() == links[0].text().trim()
    }

    private fun isFileLink(a: Element): Boolean {
        val href = a.attr("href").lowercase()
        return FILE_EXT.any { href.substringBefore('?').endsWith(it) } && a.text().isNotBlank()
    }

    fun imageUrl(img: Element): String? {
        val src = img.attr("data-src").ifBlank { img.attr("src") }
        val url = Urls.absolutize(src, baseUrl) ?: return null
        val lower = url.lowercase()
        // Иконки, логото, подписът и изображения без истински адрес не са съдържание.
        if (IGNORED_IMAGE_PARTS.any { lower.contains(it) }) return null
        if (IMAGE_EXT.none { lower.substringBefore('?').endsWith(it) }) return null
        val w = img.attr("width").toIntOrNull()
        val h = img.attr("height").toIntOrNull()
        if ((w != null && w < 48) || (h != null && h < 48)) return null
        return url
    }

    private fun List<TextRun>.normalizeRuns(): List<TextRun> = normalizeRunsRaw().let { runs ->
        if (runs.joinToString("") { it.text }.any { it.isLetterOrDigit() }) runs else emptyList()
    }

    private fun List<TextRun>.normalizeRunsRaw(): List<TextRun> {
        // Слепваме съседни еднакво форматирани парчета и нормализираме интервалите.
        val merged = ArrayList<TextRun>()
        for (r in this) {
            val text = r.text.replace(' ', ' ').replace(Regex("[ \\t\\r\\f]+"), " ")
            val last = merged.lastOrNull()
            if (last != null && last.bold == r.bold && last.italic == r.italic && last.url == r.url) {
                merged[merged.lastIndex] = last.copy(text = last.text + text)
            } else merged += r.copy(text = text)
        }
        if (merged.isEmpty()) return merged
        merged[0] = merged[0].copy(text = merged[0].text.trimStart())
        merged[merged.lastIndex] = merged.last().copy(text = merged.last().text.trimEnd())
        val cleaned = merged.map { it.copy(text = it.text.replace(Regex(" *\\n *"), "\n").replace(Regex("\\n{3,}"), "\n\n")) }
            .filter { it.text.isNotEmpty() }
        return if (cleaned.joinToString("") { it.text }.isBlank()) emptyList() else cleaned
    }

    private fun List<ContentBlock>.dedupe(): List<ContentBlock> {
        val out = ArrayList<ContentBlock>()
        val seenImages = HashSet<String>()
        for (b in this) {
            if (b is ContentBlock.Image && !seenImages.add(b.url)) continue
            if (out.lastOrNull() == b) continue
            out += b
        }
        return out
    }

    companion object {
        private val HEADINGS = setOf("h1", "h2", "h3", "h4", "h5", "h6")
        private val BOLD = setOf("b", "strong")
        private val ITALIC = setOf("i", "em")
        private val INLINE = setOf("a", "b", "strong", "i", "em", "span", "u", "small", "sup", "sub", "font", "mark", "abbr", "time", "label", "cite", "q")
        private val FILE_EXT = listOf(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".zip")
        private val IGNORED_IMAGE_PARTS = listOf(
            "logo-256", "logo.", "signature", "emoji", "pixel", "spacer", "/icons/", "facebook.com/tr",
        )
        private val IMAGE_EXT = listOf(".webp", ".jpg", ".jpeg", ".png", ".gif", ".avif")
    }
}
