package org.chyavorec.app.ui.components

import org.chyavorec.app.ui.screens.my.InkShader
import org.chyavorec.app.ui.screens.my.InkShaders
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

/** Отблясъкът и мастилото на печата минават през [ShaderPool] като шевицата и фолиото. */
class ShaderPoolTagsTest {
    @Test fun sheenAndInkArePooledWithTheirOwnSources() {
        assertEquals(listOf(ShaderTags.WEAVE, ShaderTags.FOIL, ShaderTags.SHEEN, ShaderTags.INK), ShaderTags.ALL)
        assertEquals(ShaderTags.ALL.size, ShaderTags.ALL.toSet().size, "видовете са различни")
        assertEquals(GoldSheen.TAG, ShaderPool.SHEEN)
        assertEquals(InkShader.TAG, ShaderPool.INK)
        assertSame(GoldSheen.SOURCE, ShaderTags.source(ShaderTags.SHEEN))
        assertSame(InkShaders.BLEED, ShaderTags.source(ShaderTags.INK))
        ShaderTags.ALL.forEach { assertNotNull(ShaderTags.source(it), it) }
        assertNull(ShaderTags.source("unknown"))
    }

    /** InkShader.update задава точно униформите, обявени в кода на мастилото. */
    @Test fun inkUniformsMatchTheSource() {
        val declared = Regex("""uniform\s+\S+\s+(\w+);""").findAll(InkShaders.BLEED).map { it.groupValues[1] }.toSet()
        assertEquals(setOf("uSize", "uProgress"), declared)
    }

    /** Отблясъкът и печатът не се броят като шевица/фолио (подгряването чака именно тях). */
    @Test fun drawCountersIgnoreSheenAndInk() {
        val total = ShaderDraws.total()
        ShaderDraws.note(ShaderTags.SHEEN)
        ShaderDraws.note(ShaderTags.INK)
        assertEquals(total, ShaderDraws.total())
        assertEquals(0, ShaderDraws.count(ShaderTags.SHEEN))
    }

    /** Отваряне на книга след книга (взимане при прехода, връщане след отблясъка) не превежда наново. */
    @Test fun openingBookAfterBookCompilesOnlyTheWarmupAndOneSpare() {
        val created = AtomicInteger()
        val pool = InstancePool({ Any().also { created.incrementAndGet() } }, Executor { it.run() })
        pool.prepare() // подгряването след първия кадър
        assertEquals(1, created.get())
        repeat(10) {
            val sheen = assertNotNull(pool.take())
            pool.release(sheen)
        }
        assertEquals(2, created.get(), "един от подгряването + един резерв")
    }
}
