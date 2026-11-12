package org.chyavorec.app.ui

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.ShaderDraws
import org.chyavorec.app.ui.components.ShaderTags
import org.chyavorec.app.ui.components.ShevitsaPalettes
import org.chyavorec.app.ui.components.ShevitsaStill
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.components.WeaveSpec
import org.chyavorec.app.ui.screens.home.prewarmHomeBand
import org.chyavorec.app.ui.theme.ShevitsaPattern
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** Подготовката на старта и правилата за шейдъра на шевицата (чистата логика, без видеокарта). */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ShevitsaWarmupTest {
    @Test fun budgetIsAtRestAtTheStartAndAtTheEndOfTheSweepOnly() {
        val b = WeaveBudget()
        assertTrue(b.atRest, "начало: снопът е извън кадъра")
        b.advance(3.6f)
        assertFalse(b.atRest, "по средата на снопа шейдърът рисува нещо различно от плочката")
        b.finish()
        assertTrue(b.atRest && b.exhausted, "край: застива с изчерпан бюджет")
    }

    /** Предварително нарисуваната плочка на лентата е точно тази, която рисуването намира в кеша. */
    @Test fun prewarmedBandTileIsTheOneTheRibbonDrawsWith() {
        val density = 2.75f
        val heightPx = Math.round(26 * density)
        val dark = false
        prewarmHomeBand(density, dark)
        // Както в LivingShevitsa.drawWithCache: клетка = височина / редове, широчината на изгледа е друга.
        val period = 10
        val cell = heightPx.toFloat() / ShevitsaPattern.ribbonRows(period)
        val spec = WeaveSpec(1080f, heightPx.toFloat(), cell, period.toFloat(), true, 1f, 0f, ShevitsaPalettes.ribbon(dark))
        val first = ShevitsaStill.tile(spec)
        val again = ShevitsaStill.tile(spec.copy(width = 720f))
        assertSame(first, again, "широчината не влиза в ключа")
        // Тъмната лента е друга плочка (други цветове).
        prewarmHomeBand(density, dark = true)
        val darkSpec = spec.copy(colors = ShevitsaPalettes.ribbon(true))
        assertEquals(ShevitsaStill.tile(darkSpec), ShevitsaStill.tile(darkSpec))
    }

    @Test fun drawCountersOnlyGrowWhenSomethingIsDrawn() {
        val before = ShaderDraws.total()
        val weave = ShaderDraws.count(ShaderTags.WEAVE)
        assertEquals(before, ShaderDraws.total())
        ShaderDraws.note(ShaderTags.WEAVE)
        assertEquals(before + 1, ShaderDraws.total())
        assertEquals(weave + 1, ShaderDraws.count(ShaderTags.WEAVE))
        assertTrue(ShaderDraws.hasDrawn(ShaderTags.WEAVE))
    }
}
