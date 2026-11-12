package org.chyavorec.app.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ShaderBrush
import org.chyavorec.core.Diagnostics
import java.util.concurrent.ConcurrentHashMap
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

    /** Връща шейдъра в [ShaderPool] (екранът го е спрял да го рисува) — следващият не го превежда наново. */
    fun release() {
        if (!broken) ShaderPool.release(this)
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
 * Готови шейдъри, създадени във фонов режим ([prepare]): екранът ги взима наготово и първият
 * му кадър не чака превода на AGSL кода. Взетият се връща с [GuardedShader.release] при
 * изчезването на екрана — отварянето на картата, обръщането ѝ и връщането към „Начало“ не
 * превеждат шейдър наново. Ако резервът е празен, а превод във фона тече — [take] изчаква него
 * ([InstancePool]); иначе се създава на място, както преди.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal object ShaderPool {
    const val WEAVE = ShaderTags.WEAVE
    const val FOIL = ShaderTags.FOIL

    // Нормален приоритет: главната нишка може да чака този превод (фонов приоритет го бави много).
    private val executor = Executors.newSingleThreadExecutor { r ->
        Thread(r, "shader-pool").apply { isDaemon = true }
    }
    private val pools = mapOf(
        WEAVE to InstancePool({ GuardedShader.create(org.chyavorec.app.ui.theme.ShevitsaShaders.WEAVE, WEAVE) }, executor),
        FOIL to InstancePool({ GuardedShader.create(org.chyavorec.app.ui.theme.ShevitsaShaders.FOIL, FOIL) }, executor),
    )

    /** Един шейдър от вида [tag] в резерва (или на път към него) — във фонова нишка. */
    fun prepare(tag: String) {
        pools[tag]?.prepare()
    }

    fun take(tag: String): GuardedShader? = pools[tag]?.take()

    fun release(shader: GuardedShader) {
        pools[shader.tag]?.release(shader)
    }
}

/**
 * Колко пъти е рисуван шейдър от даден вид в този процес (без връзка с Android API —
 * чете се и от кода за по-стари версии). Нарастването показва, че нещо наистина е нарисувано:
 * подгряването ([ShaderWarmup]) се брои за свършено едва тогава, а шевицата започва „жива“
 * веднага, щом видеокартата вече е превела шейдъра.
 */
internal object ShaderDraws {
    @Volatile private var weave = 0
    @Volatile private var foil = 0

    fun note(tag: String) {
        if (tag == ShaderTags.WEAVE) weave++ else foil++
    }

    fun count(tag: String): Int = if (tag == ShaderTags.WEAVE) weave else foil

    fun total(): Int = weave + foil

    fun hasDrawn(tag: String): Boolean = count(tag) > 0
}

/** Видовете шейдъри (без зависимост от API). */
internal object ShaderTags {
    const val WEAVE = "shevitsa-shader"
    const val FOIL = "foil-shader"
}
