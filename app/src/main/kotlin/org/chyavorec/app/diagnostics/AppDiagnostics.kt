package org.chyavorec.app.diagnostics

import android.content.Context
import android.os.Build
import org.chyavorec.core.DiagnosticLog
import org.chyavorec.core.DiagnosticStore
import org.chyavorec.core.Diagnostics
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

/**
 * Файлът на локалния дневник на грешките ([DiagnosticLog]) — в личната папка на приложението
 * (noBackup: не се архивира в облака). Записът е във фонова нишка (последното състояние
 * печели), а при срив — веднага ([flush]), преди процесът да спре.
 */
class FileDiagnosticStore(private val file: File) : DiagnosticStore {
    private val pending = AtomicReference<String?>(null)
    private val writer = Executors.newSingleThreadExecutor { r -> Thread(r, "diagnostics").apply { isDaemon = true } }

    override fun read(): String? = if (file.exists()) file.readText() else null

    override fun write(text: String) {
        if (pending.getAndSet(text) == null) writer.execute { flush() }
    }

    /** Записва чакащото състояние в текущата нишка. */
    fun flush() = synchronized(this) {
        val text = pending.getAndSet(null) ?: return@synchronized
        runCatching {
            file.parentFile?.mkdirs()
            val tmp = File(file.path + ".tmp")
            tmp.writeText(text)
            if (!tmp.renameTo(file)) {
                file.writeText(text)
                tmp.delete()
            }
        }
        Unit
    }
}

/** Дневникът на ниво процес: създава се при старта на приложението (или при първото отваряне на „Диагностика“). */
object AppDiagnostics {
    private const val CRASH_TAG = "crash"
    @Volatile private var store: FileDiagnosticStore? = null

    /** Дневникът (създава го, ако още го няма). */
    fun log(context: Context): DiagnosticLog = Diagnostics.log ?: synchronized(this) {
        Diagnostics.log ?: run {
            val s = FileDiagnosticStore(File(context.applicationContext.noBackupFilesDir, "diagnostics/log.jsonl"))
            store = s
            DiagnosticLog(s).also { Diagnostics.install(it) }
        }
    }

    /**
     * Веднъж от Application: инсталира дневника и записва и неприхванатите грешки (сривове),
     * след което предава на досегашния обработчик — системата показва срива както винаги.
     */
    fun install(context: Context) {
        log(context)
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            runCatching {
                Diagnostics.record(CRASH_TAG, error)
                store?.flush()
            }
            previous?.uncaughtException(thread, error)
        }
    }

    /** Заглавката на текста за „Сподели“: версия и Android — без модел на телефона и без лични данни. */
    fun header(versionName: String, versionCode: Int): List<String> = listOf(
        "Читалище Яворец $versionName ($versionCode)",
        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
    )
}
