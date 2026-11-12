package org.chyavorec.app.ui.screens.quiz

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.chyavorec.app.data.local.resetOnCorruption
import org.chyavorec.domain.quiz.Quiz
import org.chyavorec.domain.quiz.QuizStreak

/** Викторината на деня с дадените досега отговори (индекси) — оцелява след затваряне на приложението. */
@Serializable
data class QuizProgress(val date: String, val quiz: Quiz, val answers: List<Int> = emptyList())

/** Отделен малък DataStore: серията и тази на деня — само на телефона, ограничени по размер. */
private val Context.quizDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "quiz",
    corruptionHandler = resetOnCorruption("quiz"),
)

class QuizStore(private val context: Context) : QuizPersistence {
    private val json = Json { ignoreUnknownKeys = true }
    private val streakKey = stringPreferencesKey("streak")
    private val todayKey = stringPreferencesKey("today")

    val streak: Flow<QuizStreak> = context.quizDataStore.data.map { decodeStreak(it[streakKey]) }

    override suspend fun streakNow(): QuizStreak = streak.first()

    override suspend fun progress(): QuizProgress? =
        context.quizDataStore.data.first()[todayKey]?.let { runCatching { json.decodeFromString(QuizProgress.serializer(), it) }.getOrNull() }

    override suspend fun saveProgress(p: QuizProgress) {
        context.quizDataStore.edit { it[todayKey] = json.encodeToString(QuizProgress.serializer(), p.copy(answers = p.answers.take(MAX_QUESTIONS))) }
    }

    override suspend fun saveStreak(s: QuizStreak) {
        context.quizDataStore.edit { it[streakKey] = json.encodeToString(QuizStreak.serializer(), s) }
    }

    private fun decodeStreak(raw: String?): QuizStreak =
        raw?.let { runCatching { json.decodeFromString(QuizStreak.serializer(), it) }.getOrNull() }
            ?.let { it.copy(length = it.length.coerceIn(0, QuizStreak.MAX), best = it.best.coerceIn(0, QuizStreak.MAX), played = it.played.coerceIn(0, QuizStreak.MAX)) }
            ?: QuizStreak()

    private companion object {
        const val MAX_QUESTIONS = 10
    }
}
