package org.chyavorec.app.ui.screens.quiz

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizGenerator
import org.chyavorec.domain.quiz.QuizSources
import org.chyavorec.domain.quiz.QuizStreak
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * „Познай Яворец“ с истинското хранилище (DataStore): напредъкът и серията оцеляват след
 * „рестарт“ (нов обект), викторината продължава от същия въпрос, а нов ден започва нова.
 * DataStore е общ за процеса — всеки тест сам поставя началното си състояние.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class QuizStoreTest {
    @get:Rule val main = MainDispatcherRule()
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val sources = QuizSourceLoader { QuizSources(timeline = QuizDefaults.timeline) }

    private suspend fun waitFor(what: String, check: suspend () -> Boolean) {
        withTimeout(10_000) { while (!check()) delay(10) }
        assertTrue(check(), what)
    }

    private suspend fun QuizViewModel.settled(): QuizUi = withTimeout(10_000) { state.first { it !is QuizUi.Loading } }

    @Test fun progressSurvivesANewStoreAndKeepsAtMostTenAnswers() = runBlocking {
        val quiz = assertNotNull(QuizGenerator.generate(LocalDate.parse("2026-11-14"), QuizSources(timeline = QuizDefaults.timeline)))
        QuizStore(context).saveProgress(QuizProgress(quiz.date, quiz, List(15) { it % 2 }))
        val back = assertNotNull(QuizStore(context).progress())
        assertEquals(quiz, back.quiz)
        assertEquals(List(10) { it % 2 }, back.answers)
    }

    @Test fun streakSurvivesAndAbsurdValuesAreClamped() = runBlocking {
        QuizStore(context).saveStreak(QuizStreak("2026-11-13", 4, 9, 12))
        assertEquals(QuizStreak("2026-11-13", 4, 9, 12), QuizStore(context).streakNow())
        QuizStore(context).saveStreak(QuizStreak("2026-11-13", Int.MAX_VALUE, -5, Int.MAX_VALUE))
        val s = QuizStore(context).streakNow()
        assertEquals(QuizStreak.MAX, s.length)
        assertEquals(0, s.best)
        assertEquals(QuizStreak.MAX, s.played)
    }

    @Test fun quizResumesFromTheStoreAndANewDayStartsOver() = runBlocking {
        val store = QuizStore(context)
        // Вчерашна (по-стара) викторина в хранилището — днес се съставя нова.
        val old = assertNotNull(QuizGenerator.generate(LocalDate.parse("2026-11-01"), QuizSources(timeline = QuizDefaults.timeline)))
        store.saveProgress(QuizProgress(old.date, old, listOf(0, 0, 0)))
        val today = FixedClock(Instant.parse("2026-11-14T09:00:00Z"))

        val vm = QuizViewModel(sources, store, today)
        val first = assertIs<QuizUi.Playing>(vm.settled())
        assertEquals("2026-11-14", first.quiz.date)
        assertEquals(0, first.index)
        vm.choose(1)
        waitFor("отговорът е записан") { store.progress()?.let { it.date == "2026-11-14" && it.answers == listOf(1) } == true }

        // „Рестарт“: нов обект за хранилището, без данни за съставяне — същите въпроси, следващият въпрос.
        val resumed = assertIs<QuizUi.Playing>(QuizViewModel({ QuizSources() }, QuizStore(context), today).settled())
        assertEquals(first.quiz, resumed.quiz)
        assertEquals(1, resumed.index)
        assertEquals(listOf(1 == first.quiz.questions[0].correctIndex), resumed.results)

        // Нов ден — нова викторина от началото, записана вместо вчерашната.
        val tomorrow = FixedClock(Instant.parse("2026-11-15T09:00:00Z"))
        val next = assertIs<QuizUi.Playing>(QuizViewModel(sources, QuizStore(context), tomorrow).settled())
        assertEquals("2026-11-15", next.quiz.date)
        assertEquals(0, next.index)
        assertTrue(next.quiz.questions.map { it.id } != first.quiz.questions.map { it.id })
        waitFor("новият ден е записан") { store.progress()?.date == "2026-11-15" }
    }
}
