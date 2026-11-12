package org.chyavorec.data.site

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.domain.quiz.CustomQuestion
import org.chyavorec.domain.quiz.QuizFile
import org.chyavorec.domain.timeline.TimelineEntry

/**
 * Данните за „Лента на времето“ и „Познай Яворец“ от chyavorec.org:
 * страницата `/history` (хронологията е в `.hist-tl-item`) и по избор `/data/app-quiz.json`.
 * Разчитането върви на [work], не на главната нишка.
 */
class SiteExtrasService(
    private val http: HttpFetcher,
    private val baseUrl: String,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) {
    private fun url(path: String) = baseUrl.trimEnd('/') + path

    /** Хронологията; страница без разпознаваеми вехи (сменени класове) е [AppError.Parse]. */
    suspend fun fetchHistory(): Outcome<List<TimelineEntry>> =
        when (val r = http.get(url(HISTORY_PATH))) {
            is Outcome.Failure -> r
            is Outcome.Success -> {
                val entries = withContext(work) { runCatching { HistoryTimelineParser.parse(r.value.text(), baseUrl) }.getOrDefault(emptyList()) }
                if (entries.size >= MIN_ENTRIES) Outcome.Success(entries) else Outcome.Failure(AppError.Parse("history"))
            }
        }

    /** Ръчните въпроси; няма файл (404) = няма въпроси. Повреден файл е [AppError.Parse]. */
    suspend fun fetchQuizQuestions(): Outcome<List<CustomQuestion>> =
        when (val r = http.get(url(QUIZ_PATH), mapOf("Cache-Control" to "no-cache"))) {
            is Outcome.Failure -> if (r.error == AppError.NotFound) Outcome.Success(emptyList()) else r
            is Outcome.Success -> withContext(work) { QuizFile.parse(r.value.text()) }?.let { Outcome.Success(it) }
                ?: Outcome.Failure(AppError.Parse("app-quiz.json"))
        }

    companion object {
        const val HISTORY_PATH = "/history"
        const val QUIZ_PATH = "/data/app-quiz.json"

        /** Под толкова вехи страницата не е „хронологията“ — по-безопасно е да се покаже страницата. */
        const val MIN_ENTRIES = 3
    }
}
