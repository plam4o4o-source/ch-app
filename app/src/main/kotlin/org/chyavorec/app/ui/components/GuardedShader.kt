package org.chyavorec.app.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ShaderBrush
import org.chyavorec.core.Diagnostics
import java.util.concurrent.ConcurrentHashMap

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
