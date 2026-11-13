package org.chyavorec.app.ui.screens

import android.app.Application
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.ui.screens.newspaper.NewspaperListViewModel
import org.chyavorec.app.ui.screens.newspaper.NewspaperReaderViewModel
import org.chyavorec.app.ui.screens.newspaper.PageGeometry
import org.chyavorec.app.ui.screens.newspaper.PdfPages
import org.chyavorec.app.ui.screens.newspaper.PdfProblem
import org.chyavorec.app.ui.screens.newspaper.PdfReadException
import org.chyavorec.app.ui.screens.newspaper.PdfRenderPlan
import org.chyavorec.app.ui.screens.newspaper.PlatformPdfPages
import org.chyavorec.app.ui.screens.newspaper.ReaderProblem
import org.chyavorec.app.ui.screens.newspaper.ReaderState
import org.chyavorec.app.ui.screens.quiz.QuizPersistence
import org.chyavorec.app.ui.screens.quiz.QuizProgress
import org.chyavorec.app.ui.screens.quiz.QuizSourceLoader
import org.chyavorec.app.ui.screens.quiz.QuizUi
import org.chyavorec.app.ui.screens.quiz.QuizViewModel
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.DocumentKind
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizSources
import org.chyavorec.domain.quiz.QuizStreak
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Логиката на „Старият вестник“ (четец на PDF, геометрия на щипването) и на „Познай Яворец“ (състояние, серия). */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ContentScreensLogicTest {
    private val dispatcher = StandardTestDispatcher()
    @get:Rule val main = MainDispatcherRule(dispatcher)

    // --- PdfRenderer: размери и грешки ---

    @Test fun renderPlanKeepsTheAspectAndTheBudgets() {
        assertEquals(1000 to 1415, PdfRenderPlan.size(595, 842, 1000, 6_000_000))
        // По пиксели: 3000×4243 = 12.7 MP → смалява се до ~6 MP със същата пропорция.
        val (w, h) = PdfRenderPlan.size(595, 842, 3000, 6_000_000)!!
        assertTrue(w * h <= 6_000_000 && kotlin.math.abs(h.toDouble() / w - 842.0 / 595) < 0.01, "$w×$h")
        // Много висока страница — не над 4096 по страна.
        val (tw, th) = PdfRenderPlan.size(100, 5000, 2000, 50_000_000)!!
        assertTrue(th <= PdfRenderPlan.MAX_SIDE && tw >= 1, "$tw×$th")
        // Много широка.
        val (ww, wh) = PdfRenderPlan.size(20000, 100, 9000, 50_000_000)!!
        assertTrue(ww <= PdfRenderPlan.MAX_SIDE && wh >= 1, "$ww×$wh")
        assertNull(PdfRenderPlan.size(0, 100, 1000, 1_000_000))
        assertNull(PdfRenderPlan.size(100, 100, 0, 1_000_000))
    }

    @Test fun missingFileIsAFriendlyProblemNotACrash(): Unit = runBlocking {
        val e = assertFailsWith<PdfReadException> { PlatformPdfPages.open(File("/nonexistent/vestnik/x.pdf")) }
        assertEquals(PdfProblem.MISSING, e.problem)
    }

    @Test fun garbageFileIsCorrupt(): Unit = runBlocking {
        val f = File.createTempFile("vestnik", ".pdf").apply { writeText("това не е pdf"); deleteOnExit() }
        val e = assertFailsWith<PdfReadException> { PlatformPdfPages.open(f) }
        assertEquals(PdfProblem.CORRUPT, e.problem)
    }

    // --- геометрия на щипването ---

    @Test fun zoomAndPanStayInsideTheSheet() {
        // Лист по-малък от прозореца — не се мести; по-голям — до половината от излишъка (плюс полетата).
        assertEquals(0f, PageGeometry.maxOffset(800f, 1000f))
        assertEquals(500f, PageGeometry.maxOffset(2000f, 1000f))
        assertEquals(512f, PageGeometry.maxOffset(2000f, 1000f, margin = 12f))
        assertEquals(500f, PageGeometry.clampOffset(900f, 2000f, 1000f))
        assertEquals(-500f, PageGeometry.clampOffset(-900f, 2000f, 1000f))
        assertEquals(0f, PageGeometry.clampOffset(40f, 800f, 1000f))
        assertEquals(1f, PageGeometry.clampScale(0.2f)); assertEquals(5f, PageGeometry.clampScale(9f))
        // Щипване около средата не мести листа; около десния край го измества наляво.
        assertEquals(0f, PageGeometry.zoomAbout(0f, 0f, 1f, 2f))
        assertEquals(-200f, PageGeometry.zoomAbout(0f, 200f, 1f, 2f))
        // Горният ръб на висок лист.
        assertEquals(300f, PageGeometry.topOffset(1600f, 1000f))
        assertEquals(0f, PageGeometry.topOffset(900f, 1000f))
    }

    // --- четецът ---

    private class FakePages(val count: Int = 2, var failRender: Boolean = false) : PdfPages {
        var closed = false
        /** Всички нарисувани картинки — за проверка кои са освободени. */
        val made = mutableListOf<Bitmap>()
        /** Ако е зададено, рисуването чака тук (и не се отменя — като платформената страница, която вече е готова). */
        var gate: kotlinx.coroutines.CompletableDeferred<Unit>? = null
        override val pageCount = count
        override suspend fun render(index: Int, targetWidthPx: Int, maxPixels: Int): Bitmap {
            if (failRender) throw PdfReadException(PdfProblem.MEMORY)
            gate?.let { g -> kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) { g.await() } }
            return Bitmap.createBitmap(10, 14, Bitmap.Config.ARGB_8888).also { made += it }
        }
        override fun close() { closed = true }
    }

    private fun reader(
        fetch: suspend ((Float?) -> Unit) -> Outcome<File>,
        open: suspend (File) -> PdfPages,
        discard: (File) -> Unit = {},
    ) = NewspaperReaderViewModel("https://chyavorec.org/vestnik/a.pdf", "Баба Дамяна разказва", fetch, open, discard)

    @Test fun readerOpensAndRendersPages() = runTest(dispatcher) {
        val pages = FakePages()
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { pages })
        advanceUntilIdle()
        assertEquals(ReaderState.Ready(2), vm.state.value)
        vm.render(1, 1000)
        advanceUntilIdle()
        assertEquals(1, vm.page.value?.index)
        // Същата заявка не рисува наново.
        val first = vm.page.value
        vm.render(1, 1000)
        advanceUntilIdle()
        assertTrue(first === vm.page.value)
        vm.render(0, 1000)
        advanceUntilIdle()
        assertEquals(0, vm.page.value?.index)
    }

    @Test fun downloadFailureThenRetry() = runTest(dispatcher) {
        var online = false
        val vm = reader({ if (online) Outcome.Success(File("a.pdf")) else Outcome.Failure(AppError.Network) }, { FakePages(1) })
        advanceUntilIdle()
        assertEquals(ReaderState.Failed(ReaderProblem.NETWORK), vm.state.value)
        online = true
        vm.retry()
        advanceUntilIdle()
        assertEquals(ReaderState.Ready(1), vm.state.value)
    }

    @Test fun corruptCachedFileIsDiscardedAndFetchedOnce() = runTest(dispatcher) {
        var fetches = 0
        var discarded = 0
        val vm = reader(
            { fetches++; Outcome.Success(File("a.pdf")) },
            { if (fetches == 1) throw PdfReadException(PdfProblem.CORRUPT) else FakePages() },
            discard = { discarded++ },
        )
        advanceUntilIdle()
        assertEquals(2, fetches); assertEquals(1, discarded)
        assertIs<ReaderState.Ready>(vm.state.value)

        // Повреден и след новото сваляне — приятелска грешка, не цикъл.
        fetches = 0
        val bad = reader({ fetches++; Outcome.Success(File("a.pdf")) }, { throw PdfReadException(PdfProblem.CORRUPT) })
        advanceUntilIdle()
        assertEquals(2, fetches)
        assertEquals(ReaderState.Failed(ReaderProblem.CORRUPT), bad.state.value)
    }

    @Test fun passwordProtectedFileGetsItsOwnMessage() = runTest(dispatcher) {
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { throw PdfReadException(PdfProblem.PASSWORD) })
        advanceUntilIdle()
        assertEquals(ReaderState.Failed(ReaderProblem.PASSWORD), vm.state.value)
    }


    @Test fun aRenderCancelledWhileDrawingRecyclesItsBitmap() = runTest(dispatcher) {
        val pages = FakePages()
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { pages })
        advanceUntilIdle()
        val gate = kotlinx.coroutines.CompletableDeferred<Unit>()
        pages.gate = gate
        vm.render(0, 1000)
        runCurrent()
        pages.gate = null
        vm.render(1, 1000) // отменя първата, която още чака
        advanceUntilIdle()
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(2, pages.made.size)
        // made[0] е на втората заявка (текущата), made[1] — на отменената, довършена след това.
        assertTrue(pages.made[1].isRecycled, "картинката на отменената заявка е освободена")
        assertEquals(1, vm.page.value?.index)
        assertTrue(!pages.made[0].isRecycled)
    }

    @Test fun quickPageTurnsStillRecycleEveryReplacedBitmapAfterTheDelay() = runTest(dispatcher) {
        val pages = FakePages(4)
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { pages })
        advanceUntilIdle()
        vm.render(0, 1000); runCurrent()
        vm.render(1, 1000); runCurrent()
        // Следващото прелистване идва, преди да е минало закъснението на първото освобождаване.
        vm.render(2, 1000); runCurrent()
        vm.render(3, 1000); runCurrent()
        assertEquals(4, pages.made.size)
        assertTrue(pages.made.take(3).none { it.isRecycled }, "още може да са в кадъра, който се рисува")
        advanceTimeBy(NewspaperReaderViewModel.RECYCLE_DELAY_MS + 50); runCurrent()
        assertTrue(pages.made.take(3).all { it.isRecycled }, "и трите сменени са освободени")
        assertTrue(!pages.made[3].isRecycled, "текущата не се пипа")
    }

    @Test fun clearingTheReaderRecyclesPendingAndCurrentBitmaps() = runTest(dispatcher) {
        val pages = FakePages(3)
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { pages })
        advanceUntilIdle()
        vm.render(0, 1000); runCurrent()
        vm.render(1, 1000); runCurrent()
        vm.render(2, 1000); runCurrent()
        assertTrue(pages.made.none { it.isRecycled })
        val store = androidx.lifecycle.ViewModelStore()
        @Suppress("UNCHECKED_CAST")
        androidx.lifecycle.ViewModelProvider(store, object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T = vm as T
        })[NewspaperReaderViewModel::class.java]
        store.clear()
        assertTrue(pages.made.all { it.isRecycled })
    }

    @Test fun aPageThatCannotBeDrawnKeepsTheReaderAndOffersRetry() = runTest(dispatcher) {
        val pages = FakePages(failRender = true)
        val vm = reader({ Outcome.Success(File("a.pdf")) }, { pages })
        advanceUntilIdle()
        vm.render(0, 1000)
        advanceUntilIdle()
        assertTrue(vm.pageFailed.value)
        assertIs<ReaderState.Ready>(vm.state.value)
        pages.failRender = false
        vm.retryPage()
        advanceUntilIdle()
        assertTrue(!vm.pageFailed.value)
        assertNotNull(vm.page.value)
    }

    @Test fun listKeepsOnlySitePdfPublications() {
        fun doc(id: String, url: String, kind: DocumentKind) = SiteDocument(id, id, url, kind = kind)
        val list = NewspaperListViewModel.articles(
            listOf(
                doc("a", "https://chyavorec.org/vestnik/a.pdf", DocumentKind.PUBLICATION),
                doc("b", "https://chyavorec.org/istoricheski-publikacii", DocumentKind.PUBLICATION),
                doc("c", "https://chyavorec.org/files/ustav.pdf", DocumentKind.DOCUMENT),
                doc("d", "https://chyavorec.org/vestnik/d.PDF?x=1", DocumentKind.PUBLICATION),
            ),
        )
        assertEquals(listOf("a", "d"), list.map { it.id })
    }

    // --- викторината ---

    private class FakeStore(var progress: QuizProgress? = null, var streak: QuizStreak = QuizStreak()) : QuizPersistence {
        override suspend fun progress() = progress
        override suspend fun saveProgress(p: QuizProgress) { progress = p }
        override suspend fun streakNow() = streak
        override suspend fun saveStreak(s: QuizStreak) { streak = s }
    }

    private val clock = FixedClock(Instant.parse("2026-11-12T09:00:00Z"))
    private val sources = QuizSourceLoader { QuizSources(timeline = QuizDefaults.timeline) }

    @Test fun quizPlaysThroughRecordsTheStreakAndResumes() = runTest(dispatcher) {
        val store = FakeStore(streak = QuizStreak("2026-11-11", 2, 2, 2))
        val vm = QuizViewModel(sources, store, clock)
        advanceUntilIdle()
        var s = assertIs<QuizUi.Playing>(vm.state.value)
        assertEquals(5, s.quiz.questions.size)
        repeat(5) { i ->
            s = assertIs<QuizUi.Playing>(vm.state.value)
            assertEquals(i, s.index)
            vm.choose(s.quiz.questions[i].correctIndex)
            // Втори отговор на същия въпрос не се брои.
            vm.choose((s.quiz.questions[i].correctIndex + 1) % 4)
            advanceUntilIdle()
            assertEquals(i + 1, store.progress!!.answers.size)
            vm.next()
            advanceUntilIdle()
        }
        val done = assertIs<QuizUi.Finished>(vm.state.value)
        assertEquals(List(5) { true }, done.results)
        assertEquals(3, done.streak)
        assertEquals("2026-11-12", store.streak.lastDay)

        // Ново отваряне през деня — същият резултат, серията не расте повторно.
        val again = QuizViewModel(sources, store, clock)
        advanceUntilIdle()
        assertEquals(3, assertIs<QuizUi.Finished>(again.state.value).streak)
        assertEquals(3, store.streak.length)
    }

    @Test fun unfinishedQuizResumesAtTheNextQuestionWithTheSameQuestions() = runTest(dispatcher) {
        val store = FakeStore()
        val vm = QuizViewModel(sources, store, clock)
        advanceUntilIdle()
        val q = assertIs<QuizUi.Playing>(vm.state.value).quiz
        vm.choose(0); advanceUntilIdle(); vm.next(); vm.choose(1); advanceUntilIdle()
        // Сменени данни през деня не сменят въпросите — те са запазени.
        val resumed = QuizViewModel({ QuizSources() }, store, clock)
        advanceUntilIdle()
        val p = assertIs<QuizUi.Playing>(resumed.state.value)
        assertEquals(q, p.quiz)
        assertEquals(2, p.index)
        assertEquals(2, p.results.size)
    }

    @Test fun noDataMeansAFriendlyUnavailableStateNotAnEmptyQuiz() = runTest(dispatcher) {
        val vm = QuizViewModel({ QuizSources() }, FakeStore(), clock)
        advanceUntilIdle()
        assertEquals(QuizUi.Unavailable, vm.state.value)
    }

    @Test fun aNewDayStartsANewQuiz() = runTest(dispatcher) {
        val store = FakeStore()
        QuizViewModel(sources, store, clock); advanceUntilIdle()
        val yesterday = store.progress!!
        val tomorrow = FixedClock(Instant.parse("2026-11-13T09:00:00Z"))
        val vm = QuizViewModel(sources, store, tomorrow)
        advanceUntilIdle()
        val p = assertIs<QuizUi.Playing>(vm.state.value)
        assertEquals("2026-11-13", p.quiz.date)
        assertTrue(p.quiz.questions.map { it.id } != yesterday.quiz.questions.map { it.id })
    }
}
