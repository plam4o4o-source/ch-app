package org.chyavorec.app.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ShaderBrush
import org.chyavorec.core.Diagnostics
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

/**
 * AGSL шейдър с предпазител (API 33+): грешка при създаването или при задаване на
 * униформа го „чупи“ (връща false — рисува се резервата), а самата грешка се записва
 * в Диагностика веднъж на процес за [tag]. Общо за шевицата и фолиото на картата.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class GuardedShader private constructor(val shader: RuntimeShader, val tag: String) {
    val brush = ShaderBrush(shader)

    /** След първата грешка шейдърът не се ползва повече. */
    var broken = false
        private set

    /** Изпълнява [block] (задаване на униформи); false = шейдърът е счупен (сега или преди). */
    inline fun guard(block: RuntimeShader.() -> Unit): Boolean {
        if (broken) return false
        return try {
            shader.block()
            true
        } catch (e: Throwable) {
            markBroken(e)
            false
        }
    }

    @PublishedApi internal fun markBroken(e: Throwable) {
        broken = true
        ShaderReport.record(tag, e)
    }

    companion object {
        fun create(source: String, tag: String): GuardedShader? =
            runCatching { GuardedShader(RuntimeShader(source), tag) }.onFailure { ShaderReport.record(tag, it) }.getOrNull()
    }
}

/** Грешките на шейдърите — по една на вид за процеса (Диагностика). */
internal object ShaderReport {
    private val reported = ConcurrentHashMap.newKeySet<String>()

    fun record(tag: String, error: Throwable) {
        if (reported.add(tag)) Diagnostics.record(tag, error)
    }
}

/**
 * Готови шейдъри, създадени във фонов режим (при старта, [prepare]): екранът ги взима
 * наготово и първият му кадър не чака превода на AGSL кода. Взетият се допълва отново
 * във фонова нишка; ако резервът е празен — създава се на място, както преди.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal object ShaderPool {
    const val WEAVE = "shevitsa-shader"
    const val FOIL = "foil-shader"

    private val ready = ConcurrentHashMap<String, ConcurrentLinkedQueue<GuardedShader>>()
    private val refill = Executors.newSingleThreadExecutor { r ->
        Thread(r, "shader-pool").apply {
            isDaemon = true
            priority = Thread.MIN_PRIORITY
        }
    }

    /** По един от всеки вид — във фонова нишка (ChitalishteApp.warmUp). */
    fun prepare() {
        fill(org.chyavorec.app.ui.theme.ShevitsaShaders.WEAVE, WEAVE)
        fill(org.chyavorec.app.ui.theme.ShevitsaShaders.FOIL, FOIL)
    }

    fun take(source: String, tag: String): GuardedShader? {
        val pooled = ready[tag]?.poll()
        runCatching { refill.execute { fill(source, tag) } }
        return pooled ?: GuardedShader.create(source, tag)
    }

    /** Готов шейдър от резерва (остава там) — само за подгряването на видеокартата. */
    fun peek(tag: String): GuardedShader? = ready[tag]?.peek()

    private fun fill(source: String, tag: String) {
        val queue = ready.getOrPut(tag) { ConcurrentLinkedQueue() }
        if (queue.isEmpty()) GuardedShader.create(source, tag)?.let(queue::add)
    }
}
