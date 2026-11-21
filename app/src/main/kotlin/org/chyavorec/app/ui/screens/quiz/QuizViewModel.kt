package org.chyavorec.app.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.QuizQuestionsRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.data.repository.TimelineRepository
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.quiz.Quiz
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizGenerator
import org.chyavorec.domain.quiz.QuizPhoto
import org.chyavorec.domain.quiz.QuizSources
import org.chyavorec.domain.quiz.QuizStreak
import java.util.concurrent.TimeUnit

/** Къде се пазят напредъкът и серията (в приложението — [QuizStore]). */
interface QuizPersistence {
    suspend fun progress(): QuizProgress?
    suspend fun saveProgress(p: QuizProgress)
    suspend fun streakNow(): QuizStreak
    suspend fun saveStreak(s: QuizStreak)
}

/** Събира данните, от които се съставя викторината (без да чака мрежа, когато има кеш). */
fun interface QuizSourceLoader {
    suspend fun load(): QuizSources

    /**
     * Дали снимката на [url] със сигурност я няма (сайтът връща 404/410) — тогава въпросът с нея
     * се сменя още преди да се покаже. Без мрежа или при друга грешка — `false` (може да е в кеша).
     */
    suspend fun photoMissing(url: String): Boolean = false
}

/** Данните от приложението: хронология (кеш → мрежа → вградената), каталог и снимки само от кеша, ръчни въпроси. */
class AppQuizSources(
    private val timeline: TimelineRepository,
    private val catalog: CatalogRepository,
    private val site: SiteRepository,
    private val custom: QuizQuestionsRepository,
    private val http: OkHttpClient? = null,
) : QuizSourceLoader {
    override suspend fun load(): QuizSources {
        val entries = (timeline.timeline(false) as? Outcome.Success)?.value?.data
            ?: timeline.cached()?.data
            ?: QuizDefaults.timeline
        val books = runCatching { catalog.lookupSearcher(network = false)?.data?.search(CatalogQuery()) }.getOrNull().orEmpty()
        // Снимките на изложбата — само ако галерията вече е в кеша (десет страници не се теглят заради викторина).
        val photos = runCatching {
            site.cachedGallery()?.data.orEmpty().filter { it.name == EXHIBITION_ALBUM }.flatMap { a -> a.photos }.map { QuizPhoto(it.title, it.fullUrl) }
        }.getOrNull().orEmpty()
        val questions = (custom.questions(false) as? Outcome.Success)?.value?.data ?: custom.cached()?.data.orEmpty()
        return QuizSources(entries, books, photos, questions)
    }

    override suspend fun photoMissing(url: String): Boolean {
        val client = http ?: return false
        return withContext(Dispatchers.IO) {
            runCatching {
                client.newBuilder().callTimeout(PROBE_SECONDS, TimeUnit.SECONDS).build()
                    .newCall(Request.Builder().url(url).head().build()).execute()
                    .use { it.code == 404 || it.code == 410 }
            }.getOrDefault(false)
        }
    }

    private companion object {
        /** Албумът на фотодокументалната изложба (името е от chyavorec.org — виж ChyavorecSiteService.galleryPages). */
        const val EXHIBITION_ALBUM = "Фотодокументална изложба"

        /** Проверката дали снимката я има — кратка, за да не задържа зареждането на викторината. */
        const val PROBE_SECONDS = 4L
    }
}

sealed interface QuizUi {
    data object Loading : QuizUi

    /** Няма от какво да се съставят поне три въпроса — приятелско съобщение, не празна викторина. */
    data object Unavailable : QuizUi

    /** [index] — текущият въпрос; [chosen] — избраният отговор (`null` — още не е избран); [results] — за предишните. */
    data class Playing(val quiz: Quiz, val index: Int, val chosen: Int?, val results: List<Boolean>) : QuizUi

    data class Finished(val quiz: Quiz, val results: List<Boolean>, val streak: Int, val best: Int) : QuizUi
}

/** „Познай Яворец“: пет въпроса на ден, детерминирани по дата; състоянието се пази, докато денят не се смени. */
class QuizViewModel(
    private val sources: QuizSourceLoader,
    private val store: QuizPersistence,
    private val clock: AppClock,
) : ViewModel() {
    private val _state = MutableStateFlow<QuizUi>(QuizUi.Loading)
    val state: StateFlow<QuizUi> = _state.asStateFlow()
    private val answers = ArrayList<Int>()
    private var quiz: Quiz? = null
    private var loaded: QuizSources? = null

    private suspend fun allSources(): QuizSources = loaded ?: sources.load().also { loaded = it }

    init { load() }

    fun load() {
        _state.value = QuizUi.Loading
        viewModelScope.launch {
            val today = clock.today()
            val saved = store.progress()?.takeIf { it.date == today.toString() && it.quiz.questions.size >= QuizGenerator.MIN_QUESTIONS }
            val generated = saved?.quiz ?: QuizGenerator.generate(today, allSources())
            if (generated == null) {
                _state.value = QuizUi.Unavailable
                return@launch
            }
            answers.clear()
            saved?.answers?.take(generated.questions.size)?.let { answers.addAll(it) }
            // Снимки, изтрити от сайта: въпросите с тях се сменят, преди да се покажат (и в запазената викторина).
            val q = withoutMissingPhotos(generated, answers.size)
            quiz = q
            if (saved == null || q != generated) store.saveProgress(QuizProgress(q.date, q, answers.toList()))
            _state.value = if (answers.size >= q.questions.size) finished(q, record = false) else QuizUi.Playing(q, answers.size, null, results(q))
        }
    }

    /** Неотговорените въпроси със снимка, която я няма на сайта, се заменят (виж [QuizGenerator.withoutQuestion]). */
    private suspend fun withoutMissingPhotos(q: Quiz, answered: Int): Quiz {
        val withPhoto = q.questions.drop(answered).filter { it.imageUrl != null }
        if (withPhoto.isEmpty()) return q
        val missing = coroutineScope {
            withPhoto.map { question ->
                async { question.id.takeIf { sources.photoMissing(question.imageUrl.orEmpty()) } }
            }.awaitAll()
        }.filterNotNull()
        var out = q
        for (id in missing) {
            val i = out.questions.indexOfFirst { it.id == id }
            if (i < answered) continue
            out = QuizGenerator.withoutQuestion(out, i, allSources()) ?: break
        }
        return out
    }

    /**
     * Снимката на текущия въпрос не се зареди (няма я, няма мрежа) — въпрос „Какво е на снимката?“
     * без снимка не може да се отговори, затова се сменя с друг; отговорените не се пипат.
     */
    fun photoFailed(index: Int) {
        val s = _state.value as? QuizUi.Playing ?: return
        if (s.index != index || s.chosen != null || s.quiz.questions.getOrNull(index)?.imageUrl == null) return
        viewModelScope.launch {
            val next = QuizGenerator.withoutQuestion(s.quiz, index, allSources()) ?: return@launch
            // Междувременно е избран отговор или е сменен въпросът — нищо.
            if (_state.value != s) return@launch
            quiz = next
            store.saveProgress(QuizProgress(next.date, next, answers.toList()))
            _state.value = QuizUi.Playing(next, index, null, results(next))
        }
    }

    private fun results(q: Quiz): List<Boolean> = answers.mapIndexed { i, a -> a == q.questions[i].correctIndex }

    /** Избира отговор на текущия въпрос (само веднъж); записва се веднага. */
    fun choose(option: Int) {
        val s = _state.value as? QuizUi.Playing ?: return
        if (s.chosen != null || option !in s.quiz.questions[s.index].options.indices) return
        answers.add(option)
        _state.value = s.copy(chosen = option, results = results(s.quiz))
        viewModelScope.launch { store.saveProgress(QuizProgress(s.quiz.date, s.quiz, answers.toList())) }
    }

    /** „Напред“ след отговор: следващият въпрос или резултатът (и серията). */
    fun next() {
        val s = _state.value as? QuizUi.Playing ?: return
        if (s.chosen == null) return
        val q = s.quiz
        if (s.index + 1 >= q.questions.size) {
            viewModelScope.launch { _state.value = finished(q, record = true) }
        } else {
            _state.value = QuizUi.Playing(q, s.index + 1, null, results(q))
        }
    }

    private suspend fun finished(q: Quiz, record: Boolean): QuizUi.Finished {
        val today = clock.today()
        var streak = store.streakNow()
        if (record) {
            streak = streak.record(q.localDate)
            store.saveStreak(streak)
        }
        return QuizUi.Finished(q, results(q), streak.current(today), streak.best)
    }
}
