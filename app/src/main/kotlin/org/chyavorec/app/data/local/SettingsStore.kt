package org.chyavorec.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
    /** Автоматично обновяване (само в APK извън Google Play). */
    val autoUpdate: Boolean = true,
    /** Известия за съобщения от читалището. */
    val notifyMessages: Boolean = true,
    /** Анимираното въведение с логото е показано (показва се само при първото стартиране). */
    val introShown: Boolean = false,
    /** Корици от covers.openlibrary.org по ISBN (изкл. = никакви заявки към този сървър). */
    val coversOnline: Boolean = true,
    /** Сезонни украси на началния екран (снежинки, яйца, житни класове). */
    val seasonal: Boolean = true,
    /** Каталогът като „полица“ (гръбчета на книги) вместо списък. */
    val catalogShelf: Boolean = false,
    /** Режим за четене: размер на шрифта в статиите. */
    val readerFontSize: ReaderFontSize = ReaderFontSize.M,
    /** Режим за четене: по-тясна колона за текста. */
    val readerNarrow: Boolean = false,
)

/** Размер на шрифта в режим за четене (S/M/L/XL → мащаб на шрифта). */
enum class ReaderFontSize(val scale: Float) { S(0.9f), M(1f), L(1.15f), XL(1.3f) }

private const val MAX_MESSAGE_IDS = 300

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
        val autoUpdate = booleanPreferencesKey("auto_update")
        val lastUpdateCheck = longPreferencesKey("last_update_check")
        val notifiedUpdate = intPreferencesKey("notified_update_code")
        val snoozedUpdate = intPreferencesKey("snoozed_update_code")
        val snoozedUntil = longPreferencesKey("snoozed_update_until")
        val notifyMessages = booleanPreferencesKey("notify_messages")
        val readMessages = stringSetPreferencesKey("read_messages")
        val notifiedMessages = stringSetPreferencesKey("notified_messages")
        val messagesInitialized = booleanPreferencesKey("messages_initialized")
        val introShown = booleanPreferencesKey("intro_shown")
        val coversOnline = booleanPreferencesKey("covers_online")
        val seasonal = booleanPreferencesKey("seasonal_decor")
        val catalogShelf = booleanPreferencesKey("catalog_shelf")
        val readerFontSize = stringPreferencesKey("reader_font_size")
        val readerNarrow = booleanPreferencesKey("reader_narrow")
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
            autoUpdate = p[Keys.autoUpdate] ?: true,
            notifyMessages = p[Keys.notifyMessages] ?: true,
            introShown = p[Keys.introShown] ?: false,
            coversOnline = p[Keys.coversOnline] ?: true,
            seasonal = p[Keys.seasonal] ?: true,
            catalogShelf = p[Keys.catalogShelf] ?: false,
            readerFontSize = p[Keys.readerFontSize]?.let { runCatching { ReaderFontSize.valueOf(it) }.getOrNull() } ?: ReaderFontSize.M,
            readerNarrow = p[Keys.readerNarrow] ?: false,
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

    suspend fun setAutoUpdate(on: Boolean) = context.dataStore.edit { it[Keys.autoUpdate] = on }
    val lastUpdateCheck: Flow<Long> = context.dataStore.data.map { it[Keys.lastUpdateCheck] ?: 0L }
    suspend fun setLastUpdateCheck(millis: Long) = context.dataStore.edit { it[Keys.lastUpdateCheck] = millis }
    suspend fun notifiedUpdateCode(): Int = context.dataStore.data.first()[Keys.notifiedUpdate] ?: 0
    suspend fun setNotifiedUpdateCode(code: Int) = context.dataStore.edit { it[Keys.notifiedUpdate] = code }
    suspend fun snoozeUpdate(code: Int, untilMillis: Long) = context.dataStore.edit {
        it[Keys.snoozedUpdate] = code
        it[Keys.snoozedUntil] = untilMillis
    }
    suspend fun updateSnoozed(code: Int, nowMillis: Long): Boolean {
        val p = context.dataStore.data.first()
        return p[Keys.snoozedUpdate] == code && (p[Keys.snoozedUntil] ?: 0L) > nowMillis
    }

    suspend fun setIntroShown() = context.dataStore.edit { it[Keys.introShown] = true }
    suspend fun setCoversOnline(on: Boolean) = context.dataStore.edit { it[Keys.coversOnline] = on }
    suspend fun setSeasonal(on: Boolean) = context.dataStore.edit { it[Keys.seasonal] = on }
    suspend fun setCatalogShelf(on: Boolean) = context.dataStore.edit { it[Keys.catalogShelf] = on }
    suspend fun setReaderFontSize(size: ReaderFontSize) = context.dataStore.edit { it[Keys.readerFontSize] = size.name }
    suspend fun setReaderNarrow(on: Boolean) = context.dataStore.edit { it[Keys.readerNarrow] = on }
    suspend fun setNotifyMessages(on: Boolean) = context.dataStore.edit { it[Keys.notifyMessages] = on }
    val readMessageIds: Flow<Set<String>> = context.dataStore.data.map { it[Keys.readMessages].orEmpty() }
    suspend fun readMessageIdsNow(): Set<String> = readMessageIds.first()
    /** Пазят се най-много [MAX_MESSAGE_IDS] идентификатора (старите отпадат). */
    suspend fun addReadMessages(ids: Collection<String>) = context.dataStore.edit {
        it[Keys.readMessages] = (it[Keys.readMessages].orEmpty() + ids).toList().takeLast(MAX_MESSAGE_IDS).toSet()
    }
    suspend fun notifiedMessageIds(): Set<String> = context.dataStore.data.first()[Keys.notifiedMessages].orEmpty()
    suspend fun setNotifiedMessages(ids: Set<String>) = context.dataStore.edit {
        it[Keys.notifiedMessages] = (it[Keys.notifiedMessages].orEmpty() + ids).toList().takeLast(MAX_MESSAGE_IDS).toSet()
    }
    suspend fun messagesInitialized(): Boolean = context.dataStore.data.first()[Keys.messagesInitialized] ?: false
    suspend fun setMessagesInitialized() = context.dataStore.edit { it[Keys.messagesInitialized] = true }

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
