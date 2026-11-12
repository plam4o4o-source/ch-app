package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.BuildConfig
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.ui.theme.ThemePalette
import org.chyavorec.app.util.AppLanguage
import org.chyavorec.core.Outcome
import org.chyavorec.core.logFailure
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Опресняването на уиджетите „Читалище Яворец“ и „Книга на деня“ (AppWidgetManager.updateAppWidget): след синхронизация
 * (PeriodicSyncWorker), след зареждане на заеманията, при вход/изход, при старт на
 * приложението и при смяна на езика (и от системните настройки, при затворено приложение). Грешките се пренебрегват — уиджетът е допълнение.
 */
object WidgetUpdater {
    /** Фоновата работа на уиджета (приемникът, стартът на приложението). */
    internal val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** Едно опресняване наведнъж — последното прочетено състояние е последното показано. */
    private val mutex = Mutex()

    /**
     * Събитията (кеш) и заеманията се четат успоредно, всяко със свой срок — под ~10-те
     * секунди, които системата дава на приемника (goAsync). Бавното едно не отнема другото.
     */
    private const val EVENTS_TIMEOUT_MS = 4_000L
    private const val LOANS_TIMEOUT_MS = 7_000L
    private const val SETTINGS_TIMEOUT_MS = 2_000L
    private const val BOOK_TIMEOUT_MS = 6_000L

    /** Събитията се теглят от мрежата във фон (без кеш) — едно теглене наведнъж. */
    private val fetchingEvents = AtomicBoolean(false)

    private fun provider(context: Context) = ComponentName(context, ChitalishteWidgetReceiver::class.java)

    private fun bookProvider(context: Context) = ComponentName(context, BookOfTheDayWidgetReceiver::class.java)

    private fun manager(context: Context): AppWidgetManager? = runCatching { AppWidgetManager.getInstance(context) }.getOrNull()

    private fun ids(context: Context, provider: ComponentName = provider(context)): IntArray =
        runCatching { manager(context)?.getAppWidgetIds(provider) }.getOrNull() ?: IntArray(0)

    private fun bookIds(context: Context): IntArray = ids(context, bookProvider(context))

    /** Има ли поне едно копие на някой от уиджетите на началния екран (иначе фоновата задача не тегли данни за тях). */
    fun hasInstances(context: Context): Boolean = ids(context).isNotEmpty() || bookIds(context).isNotEmpty()

    /** Опреснява всички копия на двата уиджета; ако данните не се прочетат, остава показаното досега. */
    suspend fun refresh(context: Context) {
        val ids = ids(context)
        if (ids.isNotEmpty()) update(context, ids, fallback = false)
        val books = bookIds(context)
        if (books.isNotEmpty()) updateBooks(context, books, fallback = false)
    }

    /** „Цветове от тапета“ са включени (и системата ги има — Android 12+); при грешка — фирмените. */
    private suspend fun dynamicColors(c: AppContainer): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            load(SETTINGS_TIMEOUT_MS, "widget.settings") { c.settings.settings.first().dynamicColor } == true

    /**
     * Чете данните и обновява копията [ids]. Ако заеманията не се прочетат (сесията временно
     * не се чете, срокът изтече) — остава показаното досега; само при [fallback] (ново копие,
     * обновено приложение, рестарт — старият изглед може да липсва или да не е валиден) се
     * показва каквото е известно, без заемания. Без кеш на събитията те се теглят във фон
     * ([fetchEvents]) и уиджетът се опреснява след това; дотогава вместо тях няма нищо, а не
     * „Няма предстоящи събития“.
     */
    internal suspend fun update(context: Context, ids: IntArray, fallback: Boolean, fetchEvents: Boolean = true) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val manager = manager(context) ?: return@withLock
            val c = (context.applicationContext as? AppContainerOwner)?.appContainer
            // Един контекст с езика на приложението — за надписите и за данните (дати, бройки).
            val text = AppLanguage.localized(context.applicationContext ?: context)
            val state = if (c == null) {
                if (fallback) EmptyWidgetState else return@withLock
            } else {
                val dynamic = dynamicColors(c)
                val (events, loans) = coroutineScope {
                    val e = async { load(EVENTS_TIMEOUT_MS, "widget.events") { loadWidgetEvents(c) } }
                    val l = async { load(LOANS_TIMEOUT_MS, "widget.loans") { loadWidgetLoans(c) } }
                    e.await() to l.await()
                }
                if (events == null && fetchEvents) fetchEventsInBackground(context.applicationContext ?: context, c)
                val today = c.clock.today()
                when {
                    loans != null -> widgetState(text, events, loans, today, dynamic)
                    fallback -> widgetState(text, events, emptyList(), today, dynamic)
                    else -> return@withLock
                }
            }
            var shown = false
            ids.forEach { id ->
                runCatching {
                    manager.updateAppWidget(id, WidgetViews.forWidget(text, state, manager.getAppWidgetOptions(id)))
                    shown = true
                }.logFailure("widget.refresh")
            }
            if (shown) runCatching { prefs(context).edit { putString(KEY_LANGUAGE, text.getString(R.string.ui_language)) } }
        }
    }

    /**
     * Опреснява копията [ids] на „Книга на деня“: книгата на деня от кеша на каталога (без мрежа,
     * [loadBookOfTheDay]). Без каталог в кеша — приятелска бележка; ако четенето не успее (срок,
     * грешка), остава показаното досега, освен при [fallback] (ново копие — старият изглед може да липсва).
     */
    internal suspend fun updateBooks(context: Context, ids: IntArray, fallback: Boolean) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val manager = manager(context) ?: return@withLock
            val c = (context.applicationContext as? AppContainerOwner)?.appContainer
            val text = AppLanguage.localized(context.applicationContext ?: context)
            val state = if (c == null) {
                if (fallback) BookWidgetState(null) else return@withLock
            } else {
                val dynamic = dynamicColors(c)
                val palette = load(SETTINGS_TIMEOUT_MS, "widget.settings") { c.settings.settings.first().themeStyle.palette }
                val known = loadKnown(BOOK_TIMEOUT_MS, "widget.book") { loadBookOfTheDay(c, bookPrefs(context), c.clock.today()) }
                when {
                    known != null -> BookWidgetState(known.value?.toPick(), dynamic, palette ?: ThemePalette.YAVORETS)
                    fallback -> BookWidgetState(null, dynamic, palette ?: ThemePalette.YAVORETS)
                    else -> return@withLock
                }
            }
            ids.forEach { id ->
                runCatching { manager.updateAppWidget(id, BookWidgetViews.build(text, state)) }.logFailure("widget.book")
            }
        }
    }

    /** Прочетена стойност, която може да е `null` („няма“) — различава се от неуспешното четене. */
    private class Known<T>(val value: T?)

    private suspend fun <T> loadKnown(timeoutMs: Long, tag: String, block: suspend () -> T?): Known<T>? =
        withTimeoutOrNull(timeoutMs) { runCatching { Known(block()) }.logFailure(tag).getOrNull() }

    /** [block] до [timeoutMs]; грешка или изтекъл срок — null (неизвестно). */
    private suspend fun <T> load(timeoutMs: Long, tag: String, block: suspend () -> T?): T? =
        withTimeoutOrNull(timeoutMs) { runCatching { block() }.logFailure(tag).getOrNull() }

    /** Събитията от мрежата (бавна заявка не бави уиджета); при успех — опресняване, без ново теглене. */
    private fun fetchEventsInBackground(context: Context, c: AppContainer) {
        if (!fetchingEvents.compareAndSet(false, true)) return
        scope.launch {
            try {
                val ok = runCatching { c.eventsRepository.events(force = false) is Outcome.Success }
                    .logFailure("widget.events-fetch").getOrDefault(false)
                if (ok) {
                    val ids = ids(context)
                    if (ids.isNotEmpty()) update(context, ids, fallback = false, fetchEvents = false)
                }
            } finally {
                fetchingEvents.set(false)
            }
        }
    }

    /**
     * Езикът на приложението е сменен (настройките; от Android 13 и системните настройки на
     * приложението) — уиджетът и генерираният преглед се опресняват. Извиква се при всеки
     * старт на активността (MainActivity.onStart — и след пресъздаването ѝ за новия език) и
     * при смяна на конфигурацията на живия процес (ChitalishteApp.onConfigurationChanged).
     */
    fun refreshIfLanguageChanged(context: Context) {
        val app = context.applicationContext
        scope.launch { refreshForLanguage(app) }
    }

    /**
     * Опреснява, ако уиджетът е показан на друг език. Системата праща ACTION_LOCALE_CHANGED
     * (ChitalishteWidgetReceiver) и при затворено приложение — при смяна на езика на телефона
     * и, от Android 13, на езика само на приложението от системните му настройки.
     */
    internal suspend fun refreshForLanguage(context: Context) {
        val app = context.applicationContext ?: context
        val shown = runCatching { prefs(app).getString(KEY_LANGUAGE, null) }.getOrNull() ?: return
        if (shown == AppLanguage.localized(app).getString(R.string.ui_language)) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) publishPreview(app)
        refresh(app)
    }

    private const val PREFS = "widget"
    private const val KEY_LANGUAGE = "language"

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /**
     * При старт на интерфейса (ChitalishteApp.onUiStart), във фон: остатъците от Glance,
     * генерираният преглед (Android 15+) и опресняване на копията.
     */
    fun onUiStart(context: Context) {
        val app = context.applicationContext
        scope.launch {
            runCatching { deleteGlanceState(app) }.logFailure("widget.cleanup")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) publishPreview(app)
            refresh(app)
        }
    }

    /** Изчаква фоновата работа на уиджета, и започнатата междувременно (за тестовете). */
    @VisibleForTesting
    internal suspend fun awaitIdle() {
        while (true) {
            val running = scope.coroutineContext.job.children.toList()
            if (running.isEmpty()) return
            running.joinAll()
        }
    }

    /** Състоянието на копията от Glance (до v1.2.27) — files/datastore/appWidget-<id>…, вече не трябва. */
    private fun deleteGlanceState(context: Context) {
        File(context.filesDir, "datastore").listFiles { f -> f.name.startsWith("appWidget") }?.forEach { it.delete() }
    }

    private const val PREVIEW_PREFS = "widget_preview"
    private const val KEY_STAMP = "stamp"
    private const val KEY_ATTEMPT = "attempt"

    /** Неуспешен опит се повтаря най-рано след ден (системата ограничава честотата). */
    private const val PREVIEW_RETRY_MS = 24 * 60 * 60 * 1000L

    /**
     * Дали да се публикува генерираният преглед: веднъж за версия и език ([stamp] —
     * текстовете са преведени), а след неуспех — най-рано след [PREVIEW_RETRY_MS].
     */
    internal fun shouldPublishPreview(stamp: String, published: String?, lastAttempt: Long, now: Long): Boolean =
        stamp != published && (now - lastAttempt !in 0 until PREVIEW_RETRY_MS)

    /**
     * Android 15+: генериран преглед в избора на уиджети — широкият изглед с примерните данни
     * ([previewWidgetState]) и „Книга на деня“ с примерна корица; винаги във фирмените цветове.
     * setWidgetPreview може да върне false (ограничение на честотата) или да хвърли — тогава
     * остава previewLayout от widget_info.xml и се опитва пак по-късно.
     */
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    internal fun publishPreview(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREVIEW_PREFS, Context.MODE_PRIVATE)
        val stamp = "${BuildConfig.VERSION_CODE}:${context.resources.configuration.locales[0].toLanguageTag()}"
        val now = System.currentTimeMillis()
        if (!shouldPublishPreview(stamp, prefs.getString(KEY_STAMP, null), prefs.getLong(KEY_ATTEMPT, 0L), now)) return false
        prefs.edit { putLong(KEY_ATTEMPT, now) }
        val ok = runCatching {
            val manager = manager(context) ?: return false
            val views = WidgetViews.build(context, previewWidgetState(context), WidgetViews.FullWideSize)
            val book = BookWidgetViews.build(context, previewBookState(context))
            val category = AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN
            // И двата опита се правят; успехът е само ако са успели и двата (иначе — нов опит утре).
            val main = manager.setWidgetPreview(provider(context), category, views)
            val second = manager.setWidgetPreview(bookProvider(context), category, book)
            main && second
        }.logFailure("widget.preview").getOrDefault(false)
        if (ok) prefs.edit { putString(KEY_STAMP, stamp) }
        return ok
    }
}

/**
 * Извън главната нишка; приемникът живее, докато [block] свърши (goAsync — до ~10 с).
 * Извън истинско съобщение (напр. в тестовете) goAsync връща null.
 */
internal fun BroadcastReceiver.inWidgetScope(block: suspend () -> Unit) {
    val pending: BroadcastReceiver.PendingResult? = goAsync()
    WidgetUpdater.scope.launch {
        try {
            block()
        } finally {
            pending?.finish()
        }
    }
}
