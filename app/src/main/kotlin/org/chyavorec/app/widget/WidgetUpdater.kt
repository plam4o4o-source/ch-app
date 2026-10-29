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
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.BuildConfig
import org.chyavorec.core.logFailure
import java.io.File

/**
 * Опресняването на уиджета (AppWidgetManager.updateAppWidget): след синхронизация
 * (PeriodicSyncWorker), след зареждане на заеманията, при вход/изход и при старт на
 * приложението. Грешките се пренебрегват — уиджетът е допълнение.
 */
object WidgetUpdater {
    /** Фоновата работа на уиджета (приемникът, стартът на приложението). */
    internal val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** Едно опресняване наведнъж — последното прочетено състояние е последното показано. */
    private val mutex = Mutex()

    /** Под ~10-те секунди, които системата дава на приемника (goAsync). */
    private const val LOAD_TIMEOUT_MS = 8_000L

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
     * Чете данните и обновява копията [ids]. [fallback] — при грешка празно състояние
     * (ново копие, обновено приложение: старият изглед може вече да не съществува).
     */
    internal suspend fun update(context: Context, ids: IntArray, fallback: Boolean) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val manager = manager(context) ?: return@withLock
            val state = withTimeoutOrNull(LOAD_TIMEOUT_MS) {
                runCatching { loadWidgetState(context) }.logFailure("widget.load").getOrNull()
            } ?: if (fallback) EmptyWidgetState else return@withLock
            ids.forEach { id ->
                runCatching {
                    manager.updateAppWidget(id, WidgetViews.forWidget(context, state, manager.getAppWidgetOptions(id)))
                }.logFailure("widget.refresh")
            }
        }
    }

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

    /** Изчаква фоновата работа на уиджета (за тестовете). */
    @VisibleForTesting
    internal suspend fun awaitIdle() {
        scope.coroutineContext.job.children.toList().joinAll()
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
            val views = WidgetViews.build(context, previewWidgetState(context), WidgetViews.WideSize)
            manager.setWidgetPreview(provider(context), AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN, views)
        }.logFailure("widget.preview").getOrDefault(false)
        if (ok) prefs.edit { putString(KEY_STAMP, stamp) }
        return ok
    }
}
