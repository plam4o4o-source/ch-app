package org.chyavorec.domain.model

import org.chyavorec.core.BulgarianDates
import org.chyavorec.data.catalog.ReadingRecommender
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/** Езикът на текста на прегледа — известията се съставят във фонов режим, на езика на приложението. */
enum class DigestLanguage { BG, EN }

/** Какво отваря докосването на прегледа: най-важното, което има в него. */
enum class DigestTarget { EVENTS, CATALOG, NEWS }

/**
 * Готовият преглед: заглавие, кратък ред и редове за разгънатото известие. [targets] — разделите, в
 * които има нещо, по важност (събития, нови книги, новини); докосването отваря [target], останалите
 * са бутони.
 */
data class Digest(val title: String, val summary: String, val lines: List<String>, val targets: List<DigestTarget>) {
    val target: DigestTarget get() = targets.first()
}

/**
 * „Любими раздели“ за реда „Нови на рафта“ в седмичния преглед: раздел по УДК (`u8`) или отдел
 * (`d:за деца`). Празен избор = всички книги. Пази се в настройките като множество от низове.
 */
object FavouriteSections {
    fun udc(section: Int): String = "u$section"
    fun department(name: String): String = "d:" + name.trim()

    /** Книгата е от някой от избраните раздели (празен избор — от всякакъв). */
    fun matches(selected: Set<String>, book: CatalogBook): Boolean {
        if (selected.isEmpty()) return true
        val section = book.udcSection
        if (section != null && udc(section) in selected) return true
        val dep = book.department.trim()
        return dep.isNotEmpty() && department(dep) in selected
    }
}

/**
 * Новите книги спрямо началото на периода. [keys] — ключовете ([WeeklyDigest.bookKey]) на всички
 * годни книги (преди да се махнат повторените заглавия) — за „вече показаното“.
 */
data class NewBooks(val count: Int, val titles: List<String>, val capped: Boolean, val keys: List<String> = emptyList()) {
    companion object {
        /** Над толкова не се брои — „над 99 нови книги“. */
        const val MAX = 99
        val NONE = NewBooks(0, emptyList(), false)

        private const val TITLES = 3

        /**
         * Книгите, постъпили СЛЕД [baseline] (датата на предишния преглед или на включването му),
         * от [sections] (празно — всички). Един екземпляр на заглавие. Каталогът е предимно тестови
         * данни със стари дати на постъпване — затова се гледа само „след“, а не „последните“.
         *
         * [shown] `null` — първият преглед след включване: само книги СЛЕД деня на началната точка.
         * Иначе (следващите прегледи) — от самия ден на началната точка включително, защото дата без
         * час не казва дали книгата е дошла преди или след предишния преглед; вече показаните
         * ([shown], ключове от [WeeklyDigest.bookKey]) не се броят пак.
         */
        fun since(books: List<CatalogBook>, baseline: LocalDate, sections: Set<String> = emptySet(), shown: Set<String>? = null): NewBooks {
            val titles = HashSet<String>()
            val candidates = books.filter { b ->
                val date = registered(b) ?: return@filter false
                val inPeriod = if (shown == null) date.isAfter(baseline) else !date.isBefore(baseline) && WeeklyDigest.bookKey(b) !in shown
                inPeriod && FavouriteSections.matches(sections, b)
            }
            val fresh = candidates
                .sortedWith(compareByDescending<CatalogBook> { it.registeredOn }.thenByDescending { it.inv })
                .filter { titles.add(ReadingRecommender.titleKey(it.title, it.author)) }
            if (fresh.isEmpty()) return NONE
            return NewBooks(
                minOf(fresh.size, MAX),
                fresh.take(TITLES).map { it.title.trim() }.filter { it.isNotEmpty() },
                fresh.size > MAX,
                candidates.map { WeeklyDigest.bookKey(it) },
            )
        }

        private fun registered(b: CatalogBook): LocalDate? =
            b.registeredOn.takeIf { it.length >= 10 }?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() }
    }
}

/**
 * „Тази седмица в Яворец“: един тих преглед на седмица. График, решение кога да се покаже и
 * сглобяване на текста — чисти изчисления без Android (виж `WeeklyDigestStep` в приложението).
 *
 * - Слотът е неделя 18:00 (по българско време); показва се най-късно до понеделник 20:00 и не в
 *   тихите часове (21:00–08:00). Не по-често от веднъж на слот (седмица).
 * - Първото включване само записва начална точка ([Decision.Baseline]) — нищо не се показва; и
 *   слот, който е по-малко от денонощие след нея, се прескача, за да не дойде преглед веднага.
 * - „Нови“ е винаги СЛЕД началната точка, а не „последните N“ — не залива при първо включване.
 */
object WeeklyDigest {
    val DAY: DayOfWeek = DayOfWeek.SUNDAY
    val TIME: LocalTime = LocalTime.of(18, 0)

    /** Най-късно след слота, до когато закъснелият преглед още се показва (иначе седмицата се прескача). */
    val WINDOW: Duration = Duration.ofHours(26)

    /** Слот по-близо от толкова до включването се прескача. */
    val MIN_AFTER_ENABLE: Duration = Duration.ofHours(24)

    /** Колко дни напред се гледат събитията (включително днес). */
    const val EVENT_DAYS = 7

    /** Най-много събития с име в разгънатия текст. */
    private const val LISTED = 3

    /** Последното начало на слот (неделя 18:00) не по-късно от [now]. */
    fun slotStart(now: Instant, zone: ZoneId): Instant {
        val local = now.atZone(zone)
        var slot = local.toLocalDate().with(TemporalAdjusters.previousOrSame(DAY)).atTime(TIME).atZone(zone)
        if (slot.toInstant().isAfter(now)) slot = slot.toLocalDate().minusWeeks(1).atTime(TIME).atZone(zone)
        return slot.toInstant()
    }

    /** Запомненото състояние. */
    data class State(
        /** Моментът на последния преглед (или на включването); `null` — още не е включен. */
        val baselineMillis: Long?,
        /** Началото на последния обработен слот; `null` — никой. */
        val lastSlotMillis: Long?,
    )

    sealed interface Decision {
        /** Нищо за правене сега. */
        data object None : Decision

        /** Първо включване: само се записва начална точка и слотът се смята за обработен. */
        data class Baseline(val slot: Instant) : Decision

        /** Слотът се пропуска (закъснял, или твърде скоро след включването) — отбелязва се като обработен. */
        data class Skip(val slot: Instant) : Decision

        /** Време е: състави и покажи (ако има какво да се каже), после отбележи слота. */
        data class Send(val slot: Instant) : Decision
    }

    fun decide(now: Instant, zone: ZoneId, state: State): Decision {
        val slot = slotStart(now, zone)
        val baseline = state.baselineMillis ?: return Decision.Baseline(slot)
        val last = state.lastSlotMillis
        // Един преглед на слот; върнат назад часовник — също нищо (не повече от един на седмица).
        if (last != null && last >= slot.toEpochMilli()) return Decision.None
        if (Duration.between(slot, now) > WINDOW) return Decision.Skip(slot)
        if (slot.toEpochMilli() - baseline < MIN_AFTER_ENABLE.toMillis()) return Decision.Skip(slot)
        if (LoanReminderPlan.isQuiet(now, zone)) return Decision.None
        return Decision.Send(slot)
    }

    /** Събитията в следващите [EVENT_DAYS] дни (от днес); днешните — само ако още не са започнали. */
    fun upcomingEvents(events: List<Event>, now: LocalDateTime): List<Event> {
        val today = now.toLocalDate()
        val last = today.plusDays(EVENT_DAYS - 1L)
        class Dated(val date: LocalDate, val time: LocalTime?, val event: Event)
        return events.mapNotNull { e ->
            val d = e.date?.takeIf { it.length >= 10 }?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() } ?: return@mapNotNull null
            if (d.isBefore(today) || d.isAfter(last)) return@mapNotNull null
            val t = BulgarianDates.parseClock(e.time)
            if (d == today && t != null && !t.isAfter(now.toLocalTime())) return@mapNotNull null
            Dated(d, t, e)
        }.sortedWith(compareBy<Dated> { it.date }.thenBy(nullsLast()) { it.time }).map { it.event }
    }

    /** Новините, публикувани СЛЕД [sinceMillis] (началото на периода). */
    fun newArticles(news: List<NewsArticle>, sinceMillis: Long): List<NewsArticle> =
        news.filter { (it.publishedAtMillis ?: 0L) > sinceMillis }.sortedByDescending { it.publishedAtMillis }

    /** Новинарските дати са без час — полунощ по Софийско време ([org.chyavorec.data.site.SiteJsonParser]). */
    private val NEWS_ZONE: ZoneId = ZoneId.of("Europe/Sofia")

    /**
     * Новините от следващите прегледи: от началото на деня [baselineDay] (деня на предишния преглед)
     * включително — новина с дата от същия ден, качена СЛЕД прегледа, иначе никога не влиза, защото
     * датата й е полунощ. Вече показаните ([shown], ключове от [articleKey]) не се броят пак.
     */
    fun newArticlesSinceDay(news: List<NewsArticle>, baselineDay: LocalDate, shown: Set<String>): List<NewsArticle> {
        val from = baselineDay.atStartOfDay(NEWS_ZONE).toInstant().toEpochMilli()
        return news.filter { (it.publishedAtMillis ?: 0L) >= from && articleKey(it) !in shown }.sortedByDescending { it.publishedAtMillis }
    }

    /** Ключовете в „вече показаното“ на прегледа. */
    fun articleKey(a: NewsArticle): String = "n:" + a.id
    fun bookKey(b: CatalogBook): String = "b:" + b.inv

    /** Най-много толкова ключове се помнят (старите отпадат): достатъчно за няколко седмици. */
    const val MAX_SHOWN = 200

    /** Предишните ключове плюс новите, най-много [MAX_SHOWN] — най-старите отпадат, без повторения. */
    fun remember(previous: List<String>, added: List<String>): List<String> =
        (previous + added).asReversed().distinct().take(MAX_SHOWN).asReversed()

    /** `null`, когато няма какво да се каже — тогава известие не се показва. */
    fun compose(events: List<Event>, books: NewBooks, news: List<NewsArticle>, lang: DigestLanguage): Digest? {
        if (events.isEmpty() && books.count == 0 && news.isEmpty()) return null
        val bg = lang == DigestLanguage.BG
        val parts = buildList {
            if (events.isNotEmpty()) add(eventsCount(events.size, bg))
            if (books.count > 0) add(booksCount(books.count, books.capped, bg))
            if (news.isNotEmpty()) add(newsCount(news.size, bg))
        }
        val lines = buildList {
            if (events.isNotEmpty()) {
                events.take(LISTED).forEach { add(eventLine(it, bg)) }
                if (events.size > LISTED) add(more(events.size - LISTED, bg))
            }
            if (books.titles.isNotEmpty()) {
                val rest = books.count - books.titles.size
                val names = books.titles.joinToString("; ") { quote(it, bg) }
                val tail = if (rest > 0) (if (bg) " и още $rest" else " and $rest more") else ""
                add((if (bg) "Нови на рафта: " else "New on the shelf: ") + names + tail)
            }
            news.take(2).forEach { add((if (bg) "Новина: " else "News: ") + it.title.trim()) }
        }
        val targets = buildList {
            if (events.isNotEmpty()) add(DigestTarget.EVENTS)
            if (books.count > 0) add(DigestTarget.CATALOG)
            if (news.isNotEmpty()) add(DigestTarget.NEWS)
        }
        return Digest(if (bg) "Тази седмица в Яворец" else "This week in Yavorets", parts.joinToString(" · "), lines, targets)
    }

    // Множествено число: български и английски — 1 / останалите.
    fun eventsCount(n: Int, bg: Boolean): String =
        if (bg) (if (n == 1) "1 събитие" else "$n събития") else (if (n == 1) "1 event" else "$n events")

    fun booksCount(n: Int, capped: Boolean, bg: Boolean): String = when {
        capped && bg -> "над $n нови книги"
        capped -> "over $n new books"
        bg -> if (n == 1) "1 нова книга" else "$n нови книги"
        else -> if (n == 1) "1 new book" else "$n new books"
    }

    fun newsCount(n: Int, bg: Boolean): String =
        if (bg) (if (n == 1) "1 нова новина" else "$n нови новини") else (if (n == 1) "1 new story" else "$n new stories")

    private fun more(n: Int, bg: Boolean) = if (bg) "… и още $n" else "… and $n more"

    private fun quote(title: String, bg: Boolean) = if (bg) "„$title“" else "“$title”"

    private val BG_DAYS = listOf("пн", "вт", "ср", "чт", "пт", "сб", "нд")
    private val EN_DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val EN_MONTHS = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    /** „сб, 26.09 · 18:00 — Заглавие“. */
    private fun eventLine(e: Event, bg: Boolean): String {
        val d = e.date?.take(10)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        val day = d?.let {
            val name = (if (bg) BG_DAYS else EN_DAYS)[it.dayOfWeek.value - 1]
            if (bg) name + ", " + two(it.dayOfMonth) + "." + two(it.monthValue) else "$name ${it.dayOfMonth} ${EN_MONTHS[it.monthValue - 1]}"
        }
        val time = BulgarianDates.parseClock(e.time)?.let { two(it.hour) + ":" + two(it.minute) }
        val whenText = listOfNotNull(day, time).joinToString(" · ")
        return if (whenText.isEmpty()) e.title.trim() else "$whenText — ${e.title.trim()}"
    }

    private fun two(n: Int) = if (n < 10) "0$n" else n.toString()
}

/**
 * „Утре: …“ — по желание: напомняне за събитие на читалището с начален час, на следващия ден.
 * Пази се от дубли (по събитие и дата) и не се показва за събитие, за което вече има звънче.
 */
object TomorrowReminder {
    /** Събитията от календара на читалището, утре и с начален час; без вече напомнените и със звънче. */
    fun pick(events: List<Event>, today: LocalDate, reminded: Set<String>, bell: Set<String>): List<Event> {
        val tomorrow = today.plusDays(1).toString()
        return events.filter { e ->
            e.source == EventSource.CALENDAR && e.date?.take(10) == tomorrow &&
                BulgarianDates.parseClock(e.time) != null && key(e, tomorrow) !in reminded && e.id !in bell
        }.sortedBy { BulgarianDates.parseClock(it.time) }
    }

    /** Ключът за „вече напомнено“. */
    fun key(e: Event, date: String): String = "${e.id}|$date"

    fun title(e: Event, lang: DigestLanguage): String = (if (lang == DigestLanguage.BG) "Утре: " else "Tomorrow: ") + e.title.trim()

    fun text(e: Event, lang: DigestLanguage): String {
        val t = BulgarianDates.parseClock(e.time)?.let { (if (it.hour < 10) "0" else "") + it.hour + ":" + (if (it.minute < 10) "0" else "") + it.minute }
        val at = t?.let { if (lang == DigestLanguage.BG) "от $it ч." else "at $it" }
        return listOfNotNull(at, e.place?.trim()?.takeIf { it.isNotEmpty() }).joinToString(" · ")
    }
}
