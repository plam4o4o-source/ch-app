package org.chyavorec.domain

import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.quiz.CustomQuestion
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizFile
import org.chyavorec.domain.quiz.QuizGenerator
import org.chyavorec.domain.quiz.QuizKind
import org.chyavorec.domain.quiz.QuizPhoto
import org.chyavorec.domain.quiz.QuizQuestion
import org.chyavorec.domain.quiz.QuizSources
import org.chyavorec.domain.quiz.QuizStreak
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class QuizGeneratorTest {
    private val timeline = QuizDefaults.timeline
    private val today = LocalDate.of(2026, 11, 12)

    private fun book(inv: Long, author: String, title: String) = CatalogBook(inv = inv, author = author, title = title)

    /** Истински записи (шестцифрени номера) + тестови редове, които не бива да попадат във въпроси. */
    private val books = listOf(
        book(399666, "Джиан, Филип", "\"Ох...\""),
        book(399685, "Келк, Линдзи", "Аз обичам Ню Йорк"),
        book(399549, "Келк, Линдзи", "Аз обичам Париж"),
        book(399581, "Коул, Кортни", "Ако останеш"),
        book(399600, "Андахази, Федерико", "Анатомът"),
        book(399659, "Мутафчиева, Вера", "Бомбите"),
        book(399662, "Кринчев, Страшимир", "В страната на палмите : Пътепис"),
        book(399519, "Полард, Хелън", "Вила в Прованс"),
        book(399572, "Сънински, Съни", "Високо под земята"),
        book(399560, "Гарфилд, Джудит и др.", "12 -те вятъра"),
        book(312, "Симеонов, Николай", "Белият дома"),
        book(624, "Симеонов, Николай", "Белият дома"),
        book(399999, "", "Без автор"),
        book(399998, "Тестов Автор", "Тестова книга"),
    )

    private val photos = listOf("Жътва", "Харманът", "Бригадите", "Пастири и стада", "Складове и хамбари", "Площадът")
        .map { QuizPhoto(it, "https://chyavorec.org/pictures/${it.hashCode()}.webp") }

    private fun full(custom: List<CustomQuestion> = emptyList()) = QuizSources(timeline, books, photos, custom)

    private fun assertWellFormed(q: QuizQuestion) {
        assertTrue(q.options.size in 2..4, "${q.id}: ${q.options}")
        assertEquals(q.options.size, q.options.map { TextNormalizer.normalize(it) }.toSet().size, "отговорите са уникални: ${q.options}")
        assertTrue(q.correctIndex in q.options.indices)
        assertTrue(q.subject.isNotBlank())
        if (q.kind != QuizKind.CUSTOM) assertEquals(4, q.options.size, q.id)
    }

    @Test fun sameDateAndDataGiveTheSameQuiz() {
        val a = QuizGenerator.generate(today, full())
        val b = QuizGenerator.generate(today, full())
        assertNotNull(a)
        assertEquals(a, b)
        assertEquals(QuizGenerator.COUNT, a.questions.size)
    }

    @Test fun differentDatesGiveDifferentQuizzes() {
        val days = (0L until 10L).map { QuizGenerator.generate(today.plusDays(it), full())!!.questions.map { q -> q.id } }
        assertTrue(days.toSet().size >= 9, "десет дни — почти все различни комплекти")
    }

    @Test fun noRepeatsInADayAndEveryQuestionIsWellFormed() {
        for (d in 0L until 60L) {
            val quiz = QuizGenerator.generate(today.plusDays(d), full())!!
            assertEquals(quiz.questions.size, quiz.questions.map { it.id }.toSet().size)
            assertEquals(quiz.questions.size, quiz.questions.map { it.subject }.toSet().size)
            quiz.questions.forEach(::assertWellFormed)
        }
    }

    @Test fun yearOptionsArePlausibleSortedAndNotFromTheFuture() {
        val years = (0L until 120L).flatMap { d -> QuizGenerator.generate(today.plusDays(d), QuizSources(timeline = timeline))!!.questions }
            .filter { it.kind == QuizKind.TIMELINE_YEAR }
        assertTrue(years.isNotEmpty())
        for (q in years) {
            val values = q.options.map { it.toInt() }
            assertEquals(values.sorted(), values, "годините са подредени")
            val correct = q.correct.toInt()
            assertTrue(values.all { kotlin.math.abs(it - correct) <= 40 && it <= 2026 && it >= 1850 }, "${q.subject}: $values")
        }
    }

    @Test fun theBuildingQuestionHasTheRealAnswer() {
        val all = (0L until 200L).flatMap { d -> QuizGenerator.generate(today.plusDays(d), QuizSources(timeline = timeline))!!.questions }
        val building = all.first { it.kind == QuizKind.TIMELINE_YEAR && it.subject == "Открита читалищната сграда" }
        assertEquals("1961", building.correct)
        val event = all.first { it.kind == QuizKind.TIMELINE_EVENT && it.subject == "1934" }
        assertEquals("Първият радиоапарат в селото", event.correct)
    }

    @Test fun catalogQuestionsUseOnlyRealRecordsWithAnAuthor() {
        val quizzes = (0L until 120L).map { QuizGenerator.generate(today.plusDays(it), QuizSources(timeline, books))!! }
        val catalog = quizzes.flatMap { it.questions }.filter { it.kind == QuizKind.CATALOG_AUTHOR || it.kind == QuizKind.CATALOG_TITLE }
        assertTrue(catalog.isNotEmpty())
        val junk = listOf("Белият дома", "Без автор", "Тестова", "Тестов", "Симеонов")
        for (q in catalog) {
            val text = q.subject + " " + q.options.joinToString(" ")
            junk.forEach { assertTrue(!text.contains(it), "$it в ${q.id}") }
            assertTrue(q.options.none { it.contains("и др") || it.contains(';') || it.contains(',') && q.kind == QuizKind.CATALOG_AUTHOR })
            assertTrue(q.options.none { it.contains(" : ") }, "подзаглавието е махнато")
        }
        // „Фамилия, Име“ се показва като „Име Фамилия“.
        val authorQ = catalog.first { it.kind == QuizKind.CATALOG_AUTHOR }
        assertTrue(authorQ.options.all { it.contains(' ') && !it.contains(',') })
        // Авторът на въпроса „Коя книга е написана от …“ не е автор на грешните отговори.
        val titleQ = catalog.filter { it.kind == QuizKind.CATALOG_TITLE }
        titleQ.forEach { q ->
            if (q.subject == "Линдзи Келк") assertEquals(1, q.options.count { it.startsWith("Аз обичам") })
        }
    }

    @Test fun thinCatalogDataFallsBackToTheTimeline() {
        val thin = QuizSources(timeline, books.take(2) + books.takeLast(3), photos.take(2))
        repeat(30) { d ->
            val quiz = QuizGenerator.generate(today.plusDays(d.toLong()), thin)!!
            assertEquals(QuizGenerator.COUNT, quiz.questions.size)
            assertTrue(quiz.questions.all { it.kind == QuizKind.TIMELINE_YEAR || it.kind == QuizKind.TIMELINE_EVENT })
        }
    }

    @Test fun neverAnEmptyOrBrokenQuiz() {
        assertNull(QuizGenerator.generate(today, QuizSources()))
        // Две вехи — под минимума и без разнообразие за отговорите.
        assertNull(QuizGenerator.generate(today, QuizSources(timeline = timeline.take(2))))
        // Вехи без точна година не дават въпроси.
        assertNull(QuizGenerator.generate(today, QuizSources(timeline = timeline.filter { it.exactYear == null })))
        // Само вградените данни са достатъчни за пълна викторина.
        assertEquals(QuizGenerator.COUNT, QuizGenerator.generate(today, QuizSources(timeline = QuizDefaults.timeline))!!.questions.size)
    }

    @Test fun photosAreAskedAboutCaptionsAndNeedFourDistinctOnes() {
        val q = (0L until 60L).flatMap { QuizGenerator.generate(today.plusDays(it), QuizSources(timeline, photos = photos))!!.questions }
            .filter { it.kind == QuizKind.PHOTO }
        assertTrue(q.isNotEmpty())
        q.forEach {
            assertTrue(it.imageUrl!!.startsWith("https://"))
            assertEquals(it.subject, it.correct)
            assertTrue(it.options.all { o -> o in photos.map { p -> p.caption } })
        }
        val dup = List(6) { QuizPhoto("Жътва", "https://chyavorec.org/pictures/$it.webp") }
        assertTrue(QuizGenerator.generate(today, QuizSources(timeline, photos = dup))!!.questions.none { it.kind == QuizKind.PHOTO })
        assertTrue(QuizGenerator.generate(today, QuizSources(timeline, photos = photos.map { it.copy(url = "http://x/y.jpg") }))!!.questions.none { it.kind == QuizKind.PHOTO })
    }

    @Test fun handWrittenQuestionsComeFirstAndKeepTheirOrderUnlessAsked() {
        val custom = (1..4).map {
            CustomQuestion("k$it", "Въпрос номер $it?", listOf("А$it", "Б$it", "В$it"), answer = it % 3, note = "Бележка $it")
        } + CustomQuestion("sh", "Разбъркан въпрос?", listOf("едно", "две", "три", "четири"), 3, shuffle = true)
        val quizzes = (0L until 30L).map { QuizGenerator.generate(today.plusDays(it), full(custom))!! }
        for (quiz in quizzes) {
            val cu = quiz.questions.filter { it.kind == QuizKind.CUSTOM }
            assertTrue(cu.size in 1..2, "най-много два ръчни на ден")
            cu.forEach { q ->
                assertWellFormed(q)
                if (q.id != "cu-sh") assertEquals(listOf("А", "Б", "В").map { it + q.id.removePrefix("cu-k") }, q.options)
                else assertEquals("четири", q.correct)
            }
        }
    }

    @Test fun invalidHandWrittenQuestionsAreDropped() {
        val bad = listOf(
            CustomQuestion("a", "Един отговор?", listOf("само"), 0),
            CustomQuestion("b", "Повтарящи се?", listOf("да", "ДА"), 0),
            CustomQuestion("c", "Извън обхвата?", listOf("а", "б"), 2),
            CustomQuestion("", "Без номер?", listOf("а", "б"), 0),
            CustomQuestion("d", "Пет отговора?", listOf("1", "2", "3", "4", "5"), 0),
            CustomQuestion("e", "Лошо изображение?", listOf("а", "б"), 0, image = "http://evil/x.png"),
            CustomQuestion("f", "Празен отговор?", listOf("а", " "), 0),
        )
        assertTrue(QuizGenerator.generate(today, QuizSources(timeline, custom = bad))!!.questions.none { it.kind == QuizKind.CUSTOM })
    }

    @Test fun theQuizFileIsToleratedWhateverItContains() {
        val good = """{"questions":[{"id":"x1","text":"Колко?","options":["едно","две"],"answer":1,"note":" бележка ","image":"/pictures/a.webp","extra":5},
            {"id":"x2","text":"Повреден","options":["само"],"answer":0},{"id":"x1","text":"Дубликат?","options":["а","б"],"answer":0},"низ",{"text":"без id"}]}"""
        val parsed = QuizFile.parse(good)!!
        assertEquals(listOf("x1"), parsed.map { it.id })
        assertEquals("бележка", parsed.single().note)
        assertEquals(1, QuizFile.parse("""[{"id":"a","text":"Кой?","options":["а","б"],"answer":0}]""")!!.size)
        assertNull(QuizFile.parse("<html>404</html>"))
        assertNull(QuizFile.parse("""{"other":[]}"""))
        assertNull(QuizFile.parse(""))
        assertEquals(emptyList(), QuizFile.parse("""{"questions":[]}"""))
    }

    @Test fun seedIsStableAcrossRuns() {
        // Фиксирани стойности: промяната на зърното би променила въпросите на всички при обновяване.
        assertEquals(QuizGenerator.seedOf(LocalDate.of(2026, 1, 1)), QuizGenerator.seedOf(LocalDate.of(2026, 1, 1)))
        assertNotEquals(QuizGenerator.seedOf(LocalDate.of(2026, 1, 1)), QuizGenerator.seedOf(LocalDate.of(2026, 1, 2)))
        val first = QuizGenerator.generate(LocalDate.of(2026, 1, 1), full())!!.questions.map { it.id }
        assertEquals(first, QuizGenerator.generate(LocalDate.of(2026, 1, 1), full())!!.questions.map { it.id })
    }

    @Test fun streakCountsConsecutiveDays() {
        var s = QuizStreak()
        assertEquals(0, s.current(today))
        s = s.record(today)
        assertEquals(1, s.current(today))
        assertEquals(s, s.record(today), "същия ден — без промяна")
        s = s.record(today.plusDays(1)).record(today.plusDays(2))
        assertEquals(3, s.current(today.plusDays(2)))
        assertEquals(3, s.current(today.plusDays(3)), "вчера е довършена — редицата още върви")
        assertEquals(0, s.current(today.plusDays(4)), "пропуснат ден — редицата е прекъсната")
        s = s.record(today.plusDays(5))
        assertEquals(1, s.length)
        assertEquals(3, s.best)
        assertEquals(4, s.played)
        assertEquals(9_999, QuizStreak("2026-01-01", 9_999, 9_999, 9_999).record(LocalDate.of(2026, 1, 2)).length)
        assertEquals(0, QuizStreak("не-дата", 5, 5, 5).current(today))
    }
}
