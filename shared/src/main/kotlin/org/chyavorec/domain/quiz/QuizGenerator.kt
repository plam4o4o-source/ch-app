package org.chyavorec.domain.quiz

import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.catalog.RealRecords
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.timeline.TimelineEntry
import java.time.LocalDate
import kotlin.math.abs
import kotlin.random.Random

/**
 * „Познай Яворец“: пет въпроса на ден, съставени на телефона — детерминирано по дата (същата
 * дата и същите данни дават същите въпроси и същия ред на отговорите). Видове: от хронологията
 * на читалището, от каталога (само истински записи с автор), от подписите на снимките от
 * изложбата и ръчно написани от читалището. Липсващ вид просто не дава въпроси — другите
 * допълват; под [MIN_QUESTIONS] викторина няма ([generate] връща `null`).
 */
object QuizGenerator {
    const val COUNT = 5
    const val MIN_QUESTIONS = 3
    private const val MAX_CUSTOM = 2
    private const val MAX_CATALOG = 2
    private const val MAX_PHOTO = 1
    private const val MIN_YEAR = 1850

    fun generate(date: LocalDate, sources: QuizSources, count: Int = COUNT): Quiz? {
        val rng = Random(seedOf(date))
        val custom = customQuestions(sources.custom, rng)
        val timeline = timelineQuestions(sources.timeline, date, rng)
        val catalog = catalogQuestions(sources.books, rng)
        val photos = photoQuestions(sources.photos, rng)

        val chosen = ArrayList<QuizQuestion>()
        fun take(pool: List<QuizQuestion>, limit: Int) {
            var n = 0
            for (q in pool) {
                if (chosen.size >= count || n >= limit) return
                if (chosen.any { it.id == q.id || it.subject == q.subject }) continue
                chosen += q
                n++
            }
        }
        // Първо разнообразието (ръчни, снимка, каталог), после хронологията, после каквото остане.
        take(custom, MAX_CUSTOM)
        take(photos, MAX_PHOTO)
        take(catalog, MAX_CATALOG)
        take(timeline, count)
        take(catalog, count)
        take(photos, count)
        take(custom, count)
        if (chosen.size < MIN_QUESTIONS) return null
        return Quiz(date.toString(), chosen.shuffled(rng))
    }

    /** Зърното на деня — една и съща дата дава един и същ ред на нещата на всяко устройство. */
    internal fun seedOf(date: LocalDate): Long = date.toEpochDay() * -7046029254386353131L xor 0x5DEECE66DL

    // --- хронология ---

    private fun timelineQuestions(entries: List<TimelineEntry>, date: LocalDate, rng: Random): List<QuizQuestion> {
        val all = entries.filter { it.title.isNotBlank() }
        val exact = all.filter { e -> e.exactYear?.let { it in MIN_YEAR..date.year } == true }
        val perYear = exact.groupingBy { it.exactYear }.eachCount()
        val years = exact.mapNotNull { it.exactYear }.distinct()
        return exact.shuffled(rng).mapNotNull { e ->
            // „Какво се случва през 1934?“ — само ако годината е на една-единствена веха.
            val event = perYear[e.exactYear] == 1 && rng.nextBoolean()
            (if (event) eventQuestion(e, all, rng) else null) ?: yearQuestion(e, years, date, rng)
        }
    }

    private fun yearQuestion(e: TimelineEntry, years: List<Int>, date: LocalDate, rng: Random): QuizQuestion? {
        val year = e.exactYear ?: return null
        val picked = LinkedHashSet<Int>()
        // Годините на другите вехи са най-правдоподобни — „знаеш историята, но не точната година“.
        years.filter { it != year && abs(it - year) <= 40 }.shuffled(rng).take(2).forEach { picked += it }
        val near = (-12..12).filter { it != 0 }.map { year + it }.filter { it in MIN_YEAR..date.year }
        for (y in near.shuffled(rng)) {
            if (picked.size >= 3) break
            picked += y
        }
        if (picked.size < 3) return null
        // Годините се подреждат по големина — така се четат най-лесно.
        val options = (picked + year).sorted().map { it.toString() }
        return QuizQuestion(
            id = "ty-$year-${e.title.hashCode().toUInt()}", kind = QuizKind.TIMELINE_YEAR, subject = e.title,
            options = options, correctIndex = options.indexOf(year.toString()), note = e.description.ifBlank { null },
        )
    }

    private fun eventQuestion(e: TimelineEntry, all: List<TimelineEntry>, rng: Random): QuizQuestion? {
        val year = e.exactYear ?: return null
        val taken = hashSetOf(TextNormalizer.normalize(e.title))
        val others = all.filter { taken.add(TextNormalizer.normalize(it.title)) }.shuffled(rng).take(3).map { it.title }
        if (others.size < 3) return null
        val options = (others + e.title).shuffled(rng)
        return QuizQuestion(
            id = "te-$year-${e.title.hashCode().toUInt()}", kind = QuizKind.TIMELINE_EVENT, subject = year.toString(),
            options = options, correctIndex = options.indexOf(e.title), note = e.description.ifBlank { null },
        )
    }

    // --- каталог ---

    /** Запис, годен за въпрос: истински номер, един автор („Фамилия, Име“), смислено заглавие. */
    internal class Usable(val inv: Long, val title: String, val author: String)

    internal fun usableBooks(books: List<CatalogBook>): List<Usable> = books.mapNotNull { b ->
        if (!RealRecords.isRealInv(b.inv)) return@mapNotNull null
        val author = displayAuthor(b.author) ?: return@mapNotNull null
        val title = cleanTitle(b.title) ?: return@mapNotNull null
        if (RealRecords.looksLikeTest(title, author)) return@mapNotNull null
        Usable(b.inv, title, author)
    }

    /** „Джиан, Филип“ → „Филип Джиан“; няколко автора, „и др.“, цифри, странни знаци — без име. */
    internal fun displayAuthor(raw: String): String? {
        val a = raw.trim()
        if (a.isEmpty() || ';' in a || a.contains("и др", ignoreCase = true)) return null
        if (!a.all { it.isLetter() || it == ' ' || it == ',' || it == '.' || it == '-' || it == '\'' }) return null
        val parts = a.split(',').map { it.trim() }
        val name = when (parts.size) {
            1 -> parts[0]
            2 -> if (parts[0].isNotEmpty() && parts[1].isNotEmpty()) parts[1] + " " + parts[0] else return null
            else -> return null
        }
        return name.takeIf { it.length in 3..50 && it.count(Char::isLetter) >= 3 }
    }

    /** Заглавието без подзаглавие/паралелно заглавие и кавички; твърде кратко, дълго или странно — без. */
    internal fun cleanTitle(raw: String): String? {
        val t = raw.substringBefore(" : ").substringBefore(" ; ").substringBefore(" / ").trim().trim('"', '„', '“', '”').trim()
        if (t.length !in 3..70 || t.count(Char::isLetter) < 3) return null
        if (!t.first().isLetter()) return null
        return t
    }

    private fun catalogQuestions(books: List<CatalogBook>, rng: Random): List<QuizQuestion> {
        val usable = usableBooks(books)
        // Заглавие с няколко различни автора е двусмислено — не става за въпрос.
        val byTitle = usable.groupBy { TextNormalizer.normalize(it.title) }.filterValues { g -> g.map { TextNormalizer.normalize(it.author) }.distinct().size == 1 }
        val titles = byTitle.values.map { it.first() }
        val authors = titles.map { it.author }.distinctBy { TextNormalizer.normalize(it) }
        if (authors.size < 4 || titles.size < 4) return emptyList()
        val out = ArrayList<QuizQuestion>()
        for (u in titles.shuffled(rng)) {
            if (out.size >= 8) break
            val q = if (rng.nextBoolean()) authorQuestion(u, authors, rng) else titleQuestion(u, titles, rng)
            if (q != null) out += q
        }
        return out
    }

    private fun authorQuestion(u: Usable, authors: List<String>, rng: Random): QuizQuestion? {
        val me = TextNormalizer.normalize(u.author)
        val others = authors.filter { TextNormalizer.normalize(it) != me }.shuffled(rng).take(3)
        if (others.size < 3) return null
        val options = (others + u.author).shuffled(rng)
        return QuizQuestion("ca-${u.inv}", QuizKind.CATALOG_AUTHOR, u.title, options, options.indexOf(u.author))
    }

    private fun titleQuestion(u: Usable, titles: List<Usable>, rng: Random): QuizQuestion? {
        val me = TextNormalizer.normalize(u.author)
        val seen = hashSetOf(TextNormalizer.normalize(u.title))
        // Заглавията на същия автор не са грешни отговори.
        val others = titles.filter { TextNormalizer.normalize(it.author) != me && seen.add(TextNormalizer.normalize(it.title)) }
            .shuffled(rng).take(3).map { it.title }
        if (others.size < 3) return null
        val options = (others + u.title).shuffled(rng)
        return QuizQuestion("ct-${u.inv}", QuizKind.CATALOG_TITLE, u.author, options, options.indexOf(u.title))
    }

    // --- снимки ---

    private fun photoQuestions(photos: List<QuizPhoto>, rng: Random): List<QuizQuestion> {
        val usable = photos.filter { it.url.startsWith("https://") && it.caption.trim().length in 3..60 }
            .map { it.copy(caption = it.caption.trim()) }
            .distinctBy { TextNormalizer.normalize(it.caption) }
        if (usable.size < 4) return emptyList()
        return usable.shuffled(rng).take(6).map { p ->
            val others = usable.filter { it !== p }.shuffled(rng).take(3).map { it.caption }
            val options = (others + p.caption).shuffled(rng)
            QuizQuestion("ph-${p.caption.hashCode().toUInt()}", QuizKind.PHOTO, p.caption, options, options.indexOf(p.caption), imageUrl = p.url)
        }
    }

    // --- ръчни въпроси ---

    private fun customQuestions(custom: List<CustomQuestion>, rng: Random): List<QuizQuestion> =
        custom.mapNotNull { c ->
            val q = QuizFile.validate(c) ?: return@mapNotNull null
            val options = if (q.shuffle) q.options.shuffled(rng) else q.options
            QuizQuestion(
                "cu-${q.id}", QuizKind.CUSTOM, q.text, options, options.indexOf(q.options[q.answer]),
                note = q.note, imageUrl = q.image,
            )
        }.shuffled(rng)
}
