package org.chyavorec.app.diagnostics

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.SystemClock
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.FrameDataApi24
import androidx.profileinstaller.ProfileVerifier
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
import org.chyavorec.app.BuildConfig
import org.chyavorec.core.logFailure
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Измерванията за „Бързодействие“ (виж [PerfStats]) на ниво процес:
 * - старт: от началото на процеса (`Process.getStartElapsedRealtime`) или — при топъл старт —
 *   от създаването на активността до първия кадър и до пълното показване (`reportFullyDrawn`,
 *   от ReportDrawnWhen на началния екран); връщане към приложението (без пресъздаване) — също
 *   топъл старт: от показването до първия кадър;
 * - плавност: JankStats само докато активността се вижда; броячите се записват при скриване.
 */
object AppPerf {
    /** Ключът на маршрута в PerformanceMetricsState. */
    const val ROUTE_KEY = "route"

    /**
     * Процесът е стартиран за активността, ако тя е създадена до толкова след него; иначе
     * процесът е бил буден (фонова задача) и стартът се брои за топъл.
     */
    private const val COLD_GAP_MS = 5_000L

    /** Версията на този билд — всяка проба и кадър я носят, за да личи дали една версия е помогнала. */
    val versionCode: Int get() = BuildConfig.VERSION_CODE
    val versionName: String get() = BuildConfig.VERSION_NAME

    @Volatile private var store: PerfStore? = null
    private val frames = FrameCounter()
    @Volatile private var activityCreated = false

    fun store(context: Context): PerfStore = store ?: synchronized(this) {
        store ?: PerfStore(File(context.applicationContext.noBackupFilesDir, "diagnostics/perf.json")).also { store = it }
    }

    /** Веднъж от onCreate на активността (след setContent). */
    fun attach(activity: ComponentActivity, restored: Boolean) {
        runCatching { attachStartup(activity, restored) }.logFailure("perf.startup")
        val jank = runCatching { trackFrames(activity) }.logFailure("perf.jank").getOrNull()
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            /** Активността е била скрита — следващото показване е топъл старт (връщане към приложението). */
            private var stopped = false

            override fun onStart(owner: LifecycleOwner) {
                jank?.isTrackingEnabled = true
                if (stopped) {
                    stopped = false
                    runCatching { traceResume(activity) }.logFailure("perf.resume")
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                stopped = true
                jank?.isTrackingEnabled = false
                // Веднъж при скриване — не при всеки кадър.
                val delta = frames.drain(versionCode)
                if (delta.isNotEmpty()) store(activity).update { PerfStats.addFrames(PerfStats.noteVersion(it, versionCode, versionName), delta) }
            }
        })
    }

    private fun attachStartup(activity: ComponentActivity, restored: Boolean) {
        val now = SystemClock.elapsedRealtime()
        val first = !activityCreated
        activityCreated = true
        val processStart = Process.getStartElapsedRealtime()
        val cold = first && now - processStart < COLD_GAP_MS
        // Пресъздаване (завъртане, смяна на темата) не е старт.
        if (!cold && restored) return
        val trace = StartupTrace(cold, if (cold) processStart else now)
        // Първият истински кадър (splash екранът отлага рисуването, докато настройките се заредят).
        onFirstFrame(activity) { trace.firstFrameAt = it }
        activity.fullyDrawnReporter.addOnReportDrawnListener {
            trace.finish(activity, SystemClock.elapsedRealtime())
        }
        // Скрито преди пълното показване — записва се само първият кадър.
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                trace.finish(activity, null)
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    /**
     * Връщане към приложението (активността не е пресъздадена): от показването до първия кадър.
     * Пълно показване няма — reportFullyDrawn важи веднъж за активността.
     */
    private fun traceResume(activity: ComponentActivity) {
        val trace = StartupTrace(cold = false, startAt = SystemClock.elapsedRealtime())
        onFirstFrame(activity) { at ->
            trace.firstFrameAt = at
            trace.finish(activity, null)
        }
    }

    /** [block] с времето на първия кадър след извикването — в края му (след рисуването). */
    private fun onFirstFrame(activity: ComponentActivity, block: (Long) -> Unit) {
        val decor = activity.window.decorView
        val handler = Handler(Looper.getMainLooper())
        val onDraw = object : ViewTreeObserver.OnDrawListener {
            private var seen = false

            override fun onDraw() {
                if (seen) return
                seen = true
                // Слушателят не се маха по време на onDraw.
                handler.postAtFrontOfQueue { block(SystemClock.elapsedRealtime()) }
                handler.post { decor.viewTreeObserver.takeIf { it.isAlive }?.removeOnDrawListener(this) }
            }
        }
        decor.viewTreeObserver.addOnDrawListener(onDraw)
    }

    private class StartupTrace(val cold: Boolean, val startAt: Long) {
        @Volatile var firstFrameAt = 0L
        @Volatile private var done = false

        fun finish(context: Context, fullyDrawnAt: Long?) {
            if (done) return
            done = true
            val first = firstFrameAt.takeIf { it != 0L } ?: fullyDrawnAt ?: return
            val sample = StartupSample(cold, first - startAt, fullyDrawnAt?.let { maxOf(it, first) - startAt }, versionCode)
            store(context).update { PerfStats.addStart(PerfStats.noteVersion(it, versionCode, versionName), sample) }
        }
    }

    private fun trackFrames(activity: ComponentActivity): JankStats {
        val listener = object : JankStats.OnFrameListener {
            // Нишката на JankStats, всеки кадър: само броячи (без заделяне на памет).
            override fun onFrame(volatileFrameData: FrameData) {
                val states = volatileFrameData.states
                var route = "start"
                for (i in states.indices) {
                    val s = states[i]
                    if (s.key == ROUTE_KEY) route = s.value
                }
                // Целият кадър (с нишката за рендериране и видеокартата) — само от API 24; типова проверка, без заделяне.
                val total = (volatileFrameData as? FrameDataApi24)?.frameDurationCpuNanos ?: 0L
                frames.add(route, volatileFrameData.frameDurationUiNanos, volatileFrameData.isJank, total)
            }
        }
        val stats = JankStats.createAndTrack(activity.window, listener)
        // Включва се в onStart — само докато активността се вижда.
        stats.isTrackingEnabled = false
        return stats
    }

    /**
     * Какво казва ProfileVerifier за базовия профил на тази версия: компилиран, чака системната
     * компилация (dexopt във фонов режим) или го няма. Блокира — вика се от фонова нишка, и то само
     * при отваряне на „Диагностика“ (не на пътя на старта).
     */
    fun profileState(): ProfileState = runCatching {
        val status = ProfileVerifier.getCompilationStatusAsync().get(10, TimeUnit.SECONDS)
        ProfileState.of(status.isCompiledWithProfile, status.hasProfileEnqueuedForCompilation(), status.profileInstallResultCode)
    }.logFailure("perf.profile").getOrDefault(ProfileState.Unknown)

    /** Изчиства натрупаното („Изчисти“ в „Диагностика“). */
    fun clear(context: Context) {
        frames.drain()
        store(context).clear()
    }
}

/** Текущият маршрут (само шаблонът, без аргументи — виж [PerfStats.routeTag]) за JankStats. */
@Composable
fun TrackRoute(route: String?) {
    val view = LocalView.current
    val tag = PerfStats.routeTag(route)
    DisposableEffect(view, tag) {
        PerformanceMetricsState.getHolderForHierarchy(view).state?.putState(AppPerf.ROUTE_KEY, tag)
        onDispose { }
    }
}
