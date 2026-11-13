package org.chyavorec.domain

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Digest
import org.chyavorec.domain.model.DigestLanguage
import org.chyavorec.domain.model.DigestTarget
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.EventSource
import org.chyavorec.domain.model.FavouriteSections
import org.chyavorec.domain.model.NewBooks
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.TomorrowReminder
import org.chyavorec.domain.model.WeeklyDigest
import org.chyavorec.domain.model.WeeklyDigest.Decision
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Тази седмица в Яворец“: график, първо включване, нови книги, текст и множествено число. */
class WeeklyDigestTest {
    private val zone = ZoneId.of("Europe/Sofia")

    /** Местно време в София → момент. */
    private fun at(date: String, time: String): Instant = LocalDateTime.parse("${date}T$time").atZone(zone).toInstant()

    private fun event(id: String, date: String, time: String? = null, title: String = "Събитие $id", place: String? = null, source: EventSource = EventSource.CALENDAR) =
        Event(id = id, title = title, date = date, time = time, place = place, sourceUrl = "https://chyavorec.org/events/$id", source = source)

    private fun book(inv: Long, title: String, registered: String, udc: String = "886.7", department: String = "за възрастни", author: String = "Автор $inv") =
        CatalogBook(inv = inv, author = author, title = title, udc = udc, department = department, registeredOn = registered)

    private fun article(id: String, millis: Long?) = NewsArticle(id = id, title = "Новина $id", url = "https://chyavorec.org/news/$id", publishedAtMillis = millis)

    // 2026-11-15 е неделя.
    @Test fun slotIsTheLatestSundayEvening() {
        assertEquals(at("2026-11-15", "18:00"), WeeklyDigest.slotStart(at("2026-11-15", "18:00"), zone))
        assertEquals(at("2026-11-15", "18:00"), WeeklyDigest.slotStart(at("2026-11-17", "10:00"), zone))
        // Неделя преди 18:00 — още предишната седмица.
        assertEquals(at("2026-11-08", "18:00"), WeeklyDigest.slotStart(at("2026-11-15", "17:59"), zone))
        // Смяна на часовото време не мести слота от 18:00 местно (25.10.2026 — краят на лятното часово време).
        assertEquals(at("2026-10-25", "18:00"), WeeklyDigest.slotStart(at("2026-10-28", "09:00"), zone))
    }

    @Test fun firstEnableOnlyRecordsABaseline() {
        val now = at("2026-11-12", "20:00")
        val d = WeeklyDigest.decide(now, zone, WeeklyDigest.State(null, null))
        assertIs<Decision.Baseline>(d)
        assertEquals(WeeklyDigest.slotStart(now, zone), d.slot)
    }

    @Test fun sendsOncePerSlotInTheEvening() {
        val baseline = at("2026-11-02", "12:00").toEpochMilli()
        val slot = at("2026-11-15", "18:00")
        val state = WeeklyDigest.State(baseline, at("2026-11-08", "18:00").toEpochMilli())
        // Преди слота — нищо.
        assertEquals(Decision.None, WeeklyDigest.decide(at("2026-11-15", "17:00"), zone, state))
        // В слота — да.
        assertEquals(Decision.Send(slot), WeeklyDigest.decide(at("2026-11-15", "18:20"), zone, state))
        // След като е обработен — не втори път (нито час по-късно, нито в понеделник).
        val done = state.copy(lastSlotMillis = slot.toEpochMilli())
        assertEquals(Decision.None, WeeklyDigest.decide(at("2026-11-15", "19:00"), zone, done))
        assertEquals(Decision.None, WeeklyDigest.decide(at("2026-11-16", "10:00"), zone, done))
        // Върнат назад часовник — пак не.
        assertEquals(Decision.None, WeeklyDigest.decide(at("2026-11-15", "18:30"), zone, state.copy(lastSlotMillis = slot.toEpochMilli() + 1_000)))
    }

    @Test fun quietHoursWaitAndMondayMorningStillDelivers() {
        val state = WeeklyDigest.State(at("2026-11-02", "12:00").toEpochMilli(), at("2026-11-08", "18:00").toEpochMilli())
        // 22:30 — тихо; чака.
        assertEquals(Decision.None, WeeklyDigest.decide(at("2026-11-15", "22:30"), zone, state))
        // Понеделник сутрин след 08:00 — закъснелият преглед се показва.
        assertEquals(Decision.Send(at("2026-11-15", "18:00")), WeeklyDigest.decide(at("2026-11-16", "09:10"), zone, state))
    }

    @Test fun aMissedWeekIsSkippedNotSentLate() {
        val state = WeeklyDigest.State(at("2026-11-02", "12:00").toEpochMilli(), at("2026-11-08", "18:00").toEpochMilli())
        // Вторник — вече твърде късно за слота от неделя.
        assertEquals(Decision.Skip(at("2026-11-15", "18:00")), WeeklyDigest.decide(at("2026-11-17", "12:00"), zone, state))
    }

    @Test fun aSlotRightAfterEnablingIsSkipped() {
        // Включено в събота вечер — неделният слот е след по-малко от денонощие: прескача се.
        val state = WeeklyDigest.State(at("2026-11-14", "20:00").toEpochMilli(), at("2026-11-08", "18:00").toEpochMilli())
        assertEquals(Decision.Skip(at("2026-11-15", "18:00")), WeeklyDigest.decide(at("2026-11-15", "18:30"), zone, state))
        // Включено в петък вечер — след 48 часа вече идва.
        val friday = state.copy(baselineMillis = at("2026-11-13", "19:00").toEpochMilli())
        assertEquals(Decision.Send(at("2026-11-15", "18:00")), WeeklyDigest.decide(at("2026-11-15", "18:30"), zone, friday))
    }

    @Test fun eventsOfTheNextSevenDaysAreSortedAndTodayPastOnesDropped() {
        val now = LocalDateTime.parse("2026-11-15T18:30")
        val events = listOf(
            event("late", "2026-11-21", "19:00"),
            event("past", "2026-11-14", "10:00"),
            event("today-over", "2026-11-15", "10:00"),
            event("today-later", "2026-11-15", "20:00"),
            event("far", "2026-11-22", "10:00"),
            event("no-time", "2026-11-16"),
            event("early", "2026-11-16", "09:00"),
            Event(id = "nodate", title = "?", date = null, sourceUrl = "u"),
        )
        assertEquals(listOf("today-later", "early", "no-time", "late"), WeeklyDigest.upcomingEvents(events, now).map { it.id })
    }

    @Test fun newBooksAreOnlyThoseAfterTheBaselineOnePerTitle() {
        val books = listOf(
            book(1, "Стара", "2020-01-05"),
            book(2, "На деня", "2026-11-02"),
            book(3, "Нова", "2026-11-10", author = "Общ"),
            book(4, "Нова", "2026-11-11", author = "Общ"), // втори екземпляр
            book(5, "По-нова", "2026-11-12", author = "Друг"),
            book(6, "Без дата", ""),
        )
        val r = NewBooks.since(books, LocalDate.parse("2026-11-02"))
        assertEquals(2, r.count)
        assertEquals(listOf("По-нова", "Нова"), r.titles)
        assertFalse(r.capped)
        assertEquals(NewBooks.NONE, NewBooks.since(books, LocalDate.parse("2026-11-12")))
    }

    @Test fun fakePastDatesNeverFloodTheFirstDigest() {
        // Каталог с тестови данни: всички дати са в миналото; началната точка е „днес“ — нищо не е ново.
        val test = (1L..500).map { book(it, "Заглавие $it", "2023-0${1 + (it % 9).toInt()}-1${(it % 9).toInt()}", author = "А$it") }
        assertEquals(NewBooks.NONE, NewBooks.since(test, LocalDate.parse("2026-11-12")))
    }

    @Test fun newBooksCapAtNinetyNine() {
        val many = (1L..150).map { book(it, "Заглавие $it", "2026-11-10", author = "А$it") }
        val r = NewBooks.since(many, LocalDate.parse("2026-11-01"))
        assertEquals(NewBooks.MAX, r.count)
        assertTrue(r.capped)
        assertEquals("над 99 нови книги", WeeklyDigest.booksCount(r.count, r.capped, bg = true))
    }

    @Test fun favouriteSectionsLimitTheNewBooks() {
        val books = listOf(
            book(1, "Роман", "2026-11-10", udc = "821.162.3"),
            book(2, "Приказки", "2026-11-10", udc = "398", department = "за деца", author = "Б"),
            book(3, "Физика", "2026-11-10", udc = "53", department = "за възрастни", author = "В"),
        )
        val since = LocalDate.parse("2026-11-01")
        assertEquals(3, NewBooks.since(books, since, emptySet()).count)
        assertEquals(1, NewBooks.since(books, since, setOf(FavouriteSections.udc(8))).count)
        assertEquals(1, NewBooks.since(books, since, setOf(FavouriteSections.department("за деца"))).count)
        assertEquals(2, NewBooks.since(books, since, setOf(FavouriteSections.udc(5), FavouriteSections.department("за деца"))).count)
        assertEquals(0, NewBooks.since(books, since, setOf(FavouriteSections.udc(9))).count)
    }

    @Test fun pluralsInBulgarianAndEnglish() {
        assertEquals("1 събитие", WeeklyDigest.eventsCount(1, true))
        assertEquals("3 събития", WeeklyDigest.eventsCount(3, true))
        assertEquals("1 нова книга", WeeklyDigest.booksCount(1, false, true))
        assertEquals("5 нови книги", WeeklyDigest.booksCount(5, false, true))
        assertEquals("1 нова новина", WeeklyDigest.newsCount(1, true))
        assertEquals("2 нови новини", WeeklyDigest.newsCount(2, true))
        assertEquals("1 event", WeeklyDigest.eventsCount(1, false))
        assertEquals("4 events", WeeklyDigest.eventsCount(4, false))
        assertEquals("1 new book", WeeklyDigest.booksCount(1, false, false))
        assertEquals("5 new books", WeeklyDigest.booksCount(5, false, false))
        assertEquals("over 99 new books", WeeklyDigest.booksCount(99, true, false))
        assertEquals("1 new story", WeeklyDigest.newsCount(1, false))
        assertEquals("3 new stories", WeeklyDigest.newsCount(3, false))
    }

    @Test fun nothingToSayMeansNoDigest() {
        assertNull(WeeklyDigest.compose(emptyList(), NewBooks.NONE, emptyList(), DigestLanguage.BG))
        assertNull(WeeklyDigest.compose(emptyList(), NewBooks.NONE, emptyList(), DigestLanguage.EN))
    }

    @Test fun composesAllThreePartsInBulgarian() {
        val events = listOf(
            event("a", "2026-11-21", "18:00", title = "Събор"),
            event("b", "2026-11-17", title = "Клуб"),
        ).let { WeeklyDigest.upcomingEvents(it, LocalDateTime.parse("2026-11-15T18:30")) }
        val books = NewBooks(5, listOf("Нова", "Още една", "Трета"), false)
        val news = listOf(article("1", 5L))
        val d = assertNotNull(WeeklyDigest.compose(events, books, news, DigestLanguage.BG))
        assertEquals("Тази седмица в Яворец", d.title)
        assertEquals("2 събития · 5 нови книги · 1 нова новина", d.summary)
        assertEquals(DigestTarget.EVENTS, d.target)
        assertEquals(listOf(DigestTarget.EVENTS, DigestTarget.CATALOG, DigestTarget.NEWS), d.targets)
        assertEquals(
            listOf(
                "вт, 17.11 — Клуб",
                "сб, 21.11 · 18:00 — Събор",
                "Нови на рафта: „Нова“; „Още една“; „Трета“ и още 2",
                "Новина: Новина 1",
            ),
            d.lines,
        )
    }

    @Test fun composesInEnglishAndPicksTheMostImportantTarget() {
        val d: Digest = assertNotNull(WeeklyDigest.compose(emptyList(), NewBooks(1, listOf("Atlas"), false), listOf(article("1", 1L), article("2", 2L)), DigestLanguage.EN))
        assertEquals("This week in Yavorets", d.title)
        assertEquals("1 new book · 2 new stories", d.summary)
        assertEquals(DigestTarget.CATALOG, d.target)
        assertEquals("New on the shelf: “Atlas”", d.lines.first())
        assertEquals(DigestTarget.NEWS, WeeklyDigest.compose(emptyList(), NewBooks.NONE, listOf(article("1", 1L)), DigestLanguage.EN)!!.target)
    }

    @Test fun moreEventsThanListedAddAnAndMoreLine() {
        val events = (1..5).map { event("e$it", "2026-11-1${5 + it % 3}", "1$it:00") }
        val d = assertNotNull(WeeklyDigest.compose(events, NewBooks.NONE, emptyList(), DigestLanguage.BG))
        assertEquals("5 събития", d.summary)
        assertEquals("… и още 2", d.lines.last())
        assertEquals(4, d.lines.size)
    }

    @Test fun onlyArticlesAfterTheBaselineCount() {
        val news = listOf(article("old", 100L), article("new", 300L), article("newer", 400L), article("undated", null))
        assertEquals(listOf("newer", "new"), WeeklyDigest.newArticles(news, 200L).map { it.id })
    }

    /** Новина с дата (полунощ в София) от деня на предишния преглед, качена СЛЕД него — влиза в следващия. */
    @Test fun aSameDayArticlePostedAfterTheLastDigestIsCountedOnce() {
        val sent = at("2026-11-08", "18:00") // предишният преглед
        val midnight = at("2026-11-08", "00:00").toEpochMilli()
        val beforeTheDigest = article("morning", midnight)
        val afterTheDigest = article("evening", midnight)
        // Старото правило („след момента на прегледа“) губи и двете — полунощ е преди 18:00.
        assertTrue(WeeklyDigest.newArticles(listOf(beforeTheDigest, afterTheDigest), sent.toEpochMilli()).isEmpty())
        val day = LocalDate.parse("2026-11-08")
        // Сутрешната вече е била в прегледа; вечерната — не.
        val shown = setOf(WeeklyDigest.articleKey(beforeTheDigest))
        assertEquals(listOf("evening"), WeeklyDigest.newArticlesSinceDay(listOf(beforeTheDigest, afterTheDigest), day, shown).map { it.id })
        // Нищо не се брои два пъти: след запомняне и вечерната отпада.
        val shownNow = shown + WeeklyDigest.articleKey(afterTheDigest)
        assertTrue(WeeklyDigest.newArticlesSinceDay(listOf(beforeTheDigest, afterTheDigest), day, shownNow).isEmpty())
    }

    @Test fun articlesBeforeTheBaselineDayStayOutAndDatesAreSofiaMidnight() {
        val day = LocalDate.parse("2026-11-08")
        val dayBefore = article("before", at("2026-11-07", "00:00").toEpochMilli())
        val onDay = article("on-day", at("2026-11-08", "00:00").toEpochMilli())
        val later = article("later", at("2026-11-12", "00:00").toEpochMilli())
        val undated = article("undated", null)
        assertEquals(listOf("later", "on-day"), WeeklyDigest.newArticlesSinceDay(listOf(dayBefore, onDay, later, undated), day, emptySet()).map { it.id })
    }

    @Test fun aSameDayBookRegisteredAfterTheLastDigestIsCountedOnce() {
        val baseline = LocalDate.parse("2026-11-08")
        val early = book(1, "Сутрешна", "2026-11-08", author = "А")
        val late = book(2, "Вечерна", "2026-11-08", author = "Б")
        val older = book(3, "Стара", "2026-11-07", author = "В")
        val all = listOf(early, late, older)
        // Първо включване / първи преглед (без „показано“): само СЛЕД деня — нищо от същия ден.
        assertEquals(NewBooks.NONE, NewBooks.since(all, baseline))
        // Следващ преглед: от деня включително, без вече показаните.
        val first = NewBooks.since(all, baseline, shown = emptySet())
        assertEquals(2, first.count)
        assertEquals(listOf(WeeklyDigest.bookKey(early), WeeklyDigest.bookKey(late)).toSet(), first.keys.toSet())
        val second = NewBooks.since(all, baseline, shown = setOf(WeeklyDigest.bookKey(early)))
        assertEquals(1, second.count)
        assertEquals(listOf("Вечерна"), second.titles)
        assertEquals(NewBooks.NONE, NewBooks.since(all, baseline, shown = first.keys.toSet()))
    }

    @Test fun shownKeysAreBoundedAndKeepTheNewest() {
        val many = (1..WeeklyDigest.MAX_SHOWN + 50).map { "b:$it" }
        val kept = WeeklyDigest.remember(emptyList(), many)
        assertEquals(WeeklyDigest.MAX_SHOWN, kept.size)
        assertEquals("b:${WeeklyDigest.MAX_SHOWN + 50}", kept.last())
        assertFalse("b:1" in kept)
        // Без повторения; повторено се премества като най-ново.
        assertEquals(listOf("b:2", "b:3", "b:1"), WeeklyDigest.remember(listOf("b:1", "b:2", "b:3"), listOf("b:1")))
    }

    @Test fun fakePastDatesNeverFloodASecondDigestEither() {
        val test = (1L..500).map { book(it, "Заглавие $it", "2023-0${1 + (it % 9).toInt()}-1${(it % 9).toInt()}", author = "А$it") }
        assertEquals(NewBooks.NONE, NewBooks.since(test, LocalDate.parse("2026-11-08"), shown = emptySet()))
    }

    @Test fun tomorrowReminderPicksCentreEventsWithATimeOnly() {
        val today = LocalDate.parse("2026-11-15")
        val events = listOf(
            event("ok", "2026-11-16", "18:00", title = "Концерт", place = "Салон"),
            event("no-time", "2026-11-16"),
            event("day-after", "2026-11-17", "18:00"),
            event("news", "2026-11-16", "18:00", source = EventSource.NEWS),
            event("bell", "2026-11-16", "17:00"),
            event("done", "2026-11-16", "19:00"),
        )
        val picked = TomorrowReminder.pick(events, today, reminded = setOf("done|2026-11-16"), bell = setOf("bell"))
        assertEquals(listOf("ok"), picked.map { it.id })
        assertEquals("Утре: Концерт", TomorrowReminder.title(picked.single(), DigestLanguage.BG))
        assertEquals("от 18:00 ч. · Салон", TomorrowReminder.text(picked.single(), DigestLanguage.BG))
        assertEquals("Tomorrow: Концерт", TomorrowReminder.title(picked.single(), DigestLanguage.EN))
        assertEquals("at 18:00 · Салон", TomorrowReminder.text(picked.single(), DigestLanguage.EN))
    }
}
