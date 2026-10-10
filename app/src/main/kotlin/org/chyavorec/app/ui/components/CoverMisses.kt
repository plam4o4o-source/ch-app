package org.chyavorec.app.ui.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Постоянен „отрицателен“ кеш за кориците от Open Library: ISBN, за които там
 * няма корица (404 или празно изображение), не се питат отново [TTL_MS] (30 дни).
 * Coil не кешира неуспешните заявки, затова без това всяко превъртане на
 * каталога би пращало едни и същи напразни заявки.
 *
 * Списъкът се чете от SharedPreferences веднъж, извън главната нишка; дотогава
 * ([ready] = false) онлайн корици не се теглят. В паметта е за целия процес.
 */
internal object CoverMisses {
    private const val PREFS = "cover_misses"
    private const val TTL_MS = 30L * 24 * 60 * 60 * 1000
    /** Горна граница на записаните ISBN (каталогът е няколко хиляди книги). */
    private const val MAX_ENTRIES = 10_000

    private val misses = ConcurrentHashMap<String, Long>()
    private val started = AtomicBoolean(false)
    @Volatile private var prefs: SharedPreferences? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> })

    private val _ready = mutableStateOf(false)
    /** Списъкът е зареден от диска (чете се в композицията — кориците се прекомпозират веднъж). */
    val ready: State<Boolean> get() = _ready

    /** Започва зареждането (еднократно; безопасно за извикване от всяка корица). */
    fun ensureLoaded(context: Context) {
        if (!started.compareAndSet(false, true)) return
        val app = context.applicationContext
        scope.launch {
            runCatching {
                val p = app.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                val now = System.currentTimeMillis()
                val expired = mutableListOf<String>()
                p.all.forEach { (isbn, value) ->
                    val at = value as? Long
                    if (at == null || now - at > TTL_MS || at > now + TTL_MS) expired += isbn else misses[isbn] = at
                }
                if (expired.isNotEmpty()) {
                    val editor = p.edit()
                    expired.forEach { editor.remove(it) }
                    editor.apply()
                }
                prefs = p
            }
            withContext(Dispatchers.Main) { _ready.value = true }
        }
    }

    // containsKey, а не „in“: ConcurrentHashMap.contains() търси сред стойностите.
    fun isMiss(isbn: String): Boolean = misses.containsKey(isbn)

    /** Записва липсваща корица (повторенията се пренебрегват). */
    fun record(isbn: String) {
        val now = System.currentTimeMillis()
        if (misses.putIfAbsent(isbn, now) != null) return
        if (misses.size > MAX_ENTRIES) return
        val p = prefs ?: return
        scope.launch { p.edit().putLong(isbn, now).apply() }
    }
}
