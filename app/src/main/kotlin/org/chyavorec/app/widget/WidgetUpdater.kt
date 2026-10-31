package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
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
import org.chyavorec.app.util.AppLanguage
import org.chyavorec.core.Outcome
import org.chyavorec.core.logFailure
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Опресняването на уиджета (AppWidgetManager.updateAppWidget): след синхронизация
 * (PeriodicSyncWorker), след зареждане на заеманията, при вход/изход, при старт на
 * приложението и при смяна на езика му. Грешките се пренебрегват — уиджетът е допълнение.
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

    /** Събитията се теглят от мрежата във фон (без кеш) — едно теглене наведнъж. */
    private val fetchingEvents = AtomicBoolean(false)

    private fun provider(context: Context) = ComponentName(context, ChitalishteWidgetReceiver::class.java)

    private fun manager(context: Context): AppWidgetManager? = runCatching { AppWidgetManager.getInstance(context) }.getOrNull()

    private fun ids(context: Context): IntArray =
        runCatching { manager(context)?.getAppWidgetIds(provider(context)) }.getOrNull() ?: IntArray(0)

    /** Има ли поне едно копие на уиджета на началния екран (иначе фоновата задача не тегли данни за него). */
    fun hasInstances(context: Context): Boolean = ids(context).isNotEmpty()

    /** Опреснява всички копия; ако данните не се прочетат, остава показаното досега. */
    suspend fun refresh(context: Context) {
        val ids = ids(context)
        if (ids.isNotEmpty()) update(context, ids, fallback = false)
    }

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
                val (events, loans) = coroutineScope {
                    val e = async { load(EVENTS_TIMEOUT_MS, "widget.events") { loadWidgetEvents(c) } }
                    val l = async { load(LOANS_TIMEOUT_MS, "widget.loans") { loadWidgetLoans(c) } }
                    e.await() to l.await()
                }
                if (events == null && fetchEvents) fetchEventsInBackground(context.applicationContext ?: context, c)
                val today = c.clock.today()
                when {
                    loans != null -> widgetState(text, events, loans, today)
                    fallback -> widgetState(text, events, emptyList(), today)
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
     * старт на активността (MainActivity.onStart — и след пресъздаването ѝ за новия език).
     */
    fun refreshIfLanguageChanged(context: Context) {
        val app = context.applicationContext
        scope.launch {
            val shown = runCatching { prefs(app).getString(KEY_LANGUAGE, null) }.getOrNull() ?: return@launch
            if (shown == AppLanguage.localized(app).getString(R.string.ui_language)) return@launch
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) publishPreview(app)
            refresh(app)
        }
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
     * ([previewWidgetState]); цветовете се четат при показването (динамичните на системата).
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
            manager.setWidgetPreview(provider(context), AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN, views)
        }.logFailure("widget.preview").getOrDefault(false)
        if (ok) prefs.edit { putString(KEY_STAMP, stamp) }
        return ok
    }
}
