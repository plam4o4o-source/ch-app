package org.chyavorec.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.chyavorec.app.ui.theme.ThemeMode

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** Несекретни настройки (DataStore). Лични данни тук НЕ се пазят. */
data class AppSettings(
    val onboardingDone: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = false,
    val notifyNews: Boolean = false,
    val notifyEvents: Boolean = true,
    val notifyLoans: Boolean = true,
    val notifyLibrary: Boolean = true,
    val lastNewsId: String? = null,
)

class SettingsStore(private val context: Context) {
    private object Keys {
        val onboarding = booleanPreferencesKey("onboarding_done")
        val theme = stringPreferencesKey("theme_mode")
        val dynamic = booleanPreferencesKey("dynamic_color")
        val notifyNews = booleanPreferencesKey("notify_news")
        val notifyEvents = booleanPreferencesKey("notify_events")
        val notifyLoans = booleanPreferencesKey("notify_loans")
        val notifyLibrary = booleanPreferencesKey("notify_library")
        val lastNewsId = stringPreferencesKey("last_news_id")
        val notifiedLoans = stringSetPreferencesKey("notified_loans")
        val recentSearches = stringPreferencesKey("recent_searches")
        val lastBackgroundSync = longPreferencesKey("last_bg_sync")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { p ->
        AppSettings(
            onboardingDone = p[Keys.onboarding] ?: false,
            themeMode = p[Keys.theme]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM,
            dynamicColor = p[Keys.dynamic] ?: false,
            notifyNews = p[Keys.notifyNews] ?: false,
            notifyEvents = p[Keys.notifyEvents] ?: true,
            notifyLoans = p[Keys.notifyLoans] ?: true,
            notifyLibrary = p[Keys.notifyLibrary] ?: true,
            lastNewsId = p[Keys.lastNewsId],
        )
    }

    suspend fun current(): AppSettings = settings.first()

    suspend fun setOnboardingDone() = context.dataStore.edit { it[Keys.onboarding] = true }
    suspend fun setTheme(mode: ThemeMode) = context.dataStore.edit { it[Keys.theme] = mode.name }
    suspend fun setDynamicColor(on: Boolean) = context.dataStore.edit { it[Keys.dynamic] = on }
    suspend fun setNotifyNews(on: Boolean) = context.dataStore.edit { it[Keys.notifyNews] = on }
    suspend fun setNotifyEvents(on: Boolean) = context.dataStore.edit { it[Keys.notifyEvents] = on }
    suspend fun setNotifyLoans(on: Boolean) = context.dataStore.edit { it[Keys.notifyLoans] = on }
    suspend fun setNotifyLibrary(on: Boolean) = context.dataStore.edit { it[Keys.notifyLibrary] = on }
    suspend fun setLastNewsId(id: String) = context.dataStore.edit { it[Keys.lastNewsId] = id }
    suspend fun setLastBackgroundSync(millis: Long) = context.dataStore.edit { it[Keys.lastBackgroundSync] = millis }

    /** Кои заемания вече са получили известие за даден статус (ключ „loanId:STATUS“). */
    suspend fun notifiedLoans(): Set<String> = context.dataStore.data.first()[Keys.notifiedLoans].orEmpty()
    suspend fun setNotifiedLoans(keys: Set<String>) = context.dataStore.edit { it[Keys.notifiedLoans] = keys }

    val recentSearches: Flow<List<String>> = context.dataStore.data.map { p ->
        p[Keys.recentSearches]?.split('\u001F')?.filter { it.isNotBlank() }.orEmpty()
    }

    suspend fun addRecentSearch(query: String) {
        val q = query.trim().take(80)
        if (q.length < 2) return
        context.dataStore.edit { p ->
            val list = p[Keys.recentSearches]?.split('\u001F')?.filter { it.isNotBlank() }.orEmpty()
            p[Keys.recentSearches] = (listOf(q) + list.filterNot { it.equals(q, ignoreCase = true) }).take(10).joinToString("\u001F")
        }
    }

    suspend fun clearRecentSearches() = context.dataStore.edit { it.remove(Keys.recentSearches) }

    /** Изчистване на личните следи (история на търсенето, известия за заемания). */
    suspend fun clearPersonal() = context.dataStore.edit {
        it.remove(Keys.recentSearches)
        it.remove(Keys.notifiedLoans)
    }
}
