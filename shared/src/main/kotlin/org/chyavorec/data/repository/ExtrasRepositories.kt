package org.chyavorec.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.site.SiteExtrasService
import org.chyavorec.domain.quiz.CustomQuestion
import org.chyavorec.domain.timeline.TimelineEntry
import org.chyavorec.domain.repository.PayloadCache

private const val SIX_HOURS = 6 * 60 * 60 * 1000L

/** Хронологията на читалището (страницата „История“) — кеширана, офлайн от кеша. */
class TimelineRepository(
    private val service: SiteExtrasService,
    cache: PayloadCache,
    clock: AppClock,
    work: CoroutineDispatcher = Dispatchers.Default,
) {
    private val list = CachedResource(cache, "site:history", ListSerializer(TimelineEntry.serializer()), clock, SIX_HOURS, work)

    suspend fun timeline(force: Boolean = false): Outcome<Synced<List<TimelineEntry>>> = list.load(force) { service.fetchHistory() }

    /** Само от кеша (без мрежа) или `null`. */
    suspend fun cached(): Synced<List<TimelineEntry>>? = list.cached()
}

/** Ръчните въпроси на викторината (`data/app-quiz.json`, по избор) — кеширани. */
class QuizQuestionsRepository(
    private val service: SiteExtrasService,
    cache: PayloadCache,
    clock: AppClock,
    work: CoroutineDispatcher = Dispatchers.Default,
) {
    private val list = CachedResource(cache, "site:quiz", ListSerializer(CustomQuestion.serializer()), clock, SIX_HOURS, work)

    suspend fun questions(force: Boolean = false): Outcome<Synced<List<CustomQuestion>>> = list.load(force) { service.fetchQuizQuestions() }

    suspend fun cached(): Synced<List<CustomQuestion>>? = list.cached()
}
