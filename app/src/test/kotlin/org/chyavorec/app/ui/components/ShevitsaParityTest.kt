package org.chyavorec.app.ui.components

import android.app.Application
import android.graphics.Bitmap
import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ShevitsaPattern
import org.chyavorec.app.ui.theme.ShevitsaShaders
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.math.abs
import kotlin.math.floor

/**
 * Шарката на шевицата има два източника — AGSL `stitchKind` и Kotlin копието
 * [ShevitsaPattern.kind] (за неподвижната плочка). Тук AGSL се изпълнява наистина
 * (Robolectric, NATIVE графика) и всяка клетка се сверява с Kotlin. И плочката на
 * неподвижния вариант ([ShevitsaStill]) — пиксел по пиксел с шейдъра, през ръбовете ѝ.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "w1000dp-h1400dp-mdpi")
class ShevitsaParityTest {
    @get:Rule val compose = createComposeRule()

    /** Чисти цветове: всеки конец — свой канал, без сноп и проблясване. */
    private val pure = ShevitsaColors(
        fabric = Color.Black, red = Color(0xFFFF0000), ink = Color(0xFF00FF00), gold = Color(0xFF0000FF), glow = Color.White,
    )

    private fun weave(w: Int, h: Int, cell: Float, period: Int, ribbon: Boolean, colors: ShevitsaColors): Bitmap {
        val s = RuntimeShader(ShevitsaShaders.WEAVE)
        val spec = WeaveSpec(w.toFloat(), h.toFloat(), cell, period.toFloat(), ribbon, 0f, 0f, colors)
        s.setFloatUniform("uResolution", w.toFloat(), h.toFloat())
        s.setFloatUniform("uOrigin", spec.originX, spec.originY)
        s.setFloatUniform("uTime", 0f)
        s.setFloatUniform("uCell", cell)
        s.setFloatUniform("uPeriod", period.toFloat())
        s.setFloatUniform("uRibbon", if (ribbon) 1f else 0f)
        s.setFloatUniform("uIntensity", 0f)
        s.setFloatUniform("uFabricAlpha", colors.fabricAlpha)
        s.setFloatUniform("uThreadAlpha", colors.threadAlpha)
        s.setFloatUniform("uClear", 0f)
        s.setColorUniform("uFabric", colors.fabric.toArgb())
        s.setColorUniform("uRed", colors.red.toArgb())
        s.setColorUniform("uInk", colors.ink.toArgb())
        s.setColorUniform("uGold", colors.gold.toArgb())
        s.setColorUniform("uGlow", colors.glow.toArgb())
        // Софтуерното платно не изпълнява RuntimeShader — рисува се през Compose (хардуерния път на
        // Robolectric), върху черно: снимката е премултиплицираният цвят на шейдъра.
        val brush = ShaderBrush(s)
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f)) {
                Spacer(
                    Modifier.size(w.dp, h.dp).testTag("weave").drawBehind {
                        drawRect(Color.Black)
                        drawRect(brush)
                    },
                )
            }
        }
        compose.waitForIdle()
        val bmp = compose.onNodeWithTag("weave").captureToImage().asAndroidBitmap()
        assertEquals(w, bmp.width)
        assertEquals(h, bmp.height)
        return bmp
    }

    private fun kindOf(argb: Int): Int {
        val r = (argb shr 16) and 0xFF
        val g = (argb shr 8) and 0xFF
        val b = argb and 0xFF
        return when {
            maxOf(r, g, b) < 100 -> ShevitsaPattern.EMPTY
            r >= g && r >= b -> ShevitsaPattern.RED
            g >= b -> ShevitsaPattern.INK
            else -> ShevitsaPattern.GOLD
        }
    }

    private fun checkKinds(cols: Int, rows: Int, period: Int, ribbon: Boolean) {
        val cell = 8f
        val bmp = weave((cols * cell).toInt(), (rows * cell).toInt(), cell, period, ribbon, pure)
        val ox = floor(cols * 0.5f)
        val oy = floor(rows * 0.5f)
        val bad = ArrayList<String>()
        var threads = 0
        for (r in 0 until rows) for (c in 0 until cols) {
            val agsl = kindOf(bmp.getPixel((c * cell + cell / 2).toInt(), (r * cell + cell / 2).toInt()))
            val kotlin = ShevitsaPattern.kind(c - ox, r - oy, period.toFloat(), ribbon)
            if (kotlin != ShevitsaPattern.EMPTY) threads++
            if (agsl != kotlin) bad += "(${c - ox}, ${r - oy}): AGSL $agsl, Kotlin $kotlin"
        }
        assertTrue("шарката е празна", threads > cols * rows / 8)
        assertTrue("период $period, лента $ribbon: ${bad.size} разлики, напр. ${bad.take(8)}", bad.isEmpty())
    }

    @Test fun fieldKindsMatchTheShader() = checkKinds(cols = 45, rows = 45, period = 18, ribbon = false)

    @Test fun smallFieldKindsMatchTheShader() = checkKinds(cols = 31, rows = 27, period = 10, ribbon = false)

    @Test fun ribbonKindsMatchTheShader() = checkKinds(cols = 61, rows = ShevitsaPattern.ribbonRows(10), period = 10, ribbon = true)

    @Test fun wideRibbonKindsMatchTheShader() = checkKinds(cols = 75, rows = ShevitsaPattern.ribbonRows(14), period = 14, ribbon = true)

    /** Периодът на плочката: поле — period по двете оси, лента — 2·period по дължина. */
    @Test fun patternRepeatsEveryTile() {
        for (p in listOf(10, 14, 18)) {
            val n = ShevitsaStill.tileCells(p, ribbon = false)
            for (x in -40..40) for (y in -40..40) {
                val k = ShevitsaPattern.kind(x.toFloat(), y.toFloat(), p.toFloat(), false)
                assertEquals("поле $p ($x, $y)", k, ShevitsaPattern.kind((x + n).toFloat(), y.toFloat(), p.toFloat(), false))
                assertEquals("поле $p ($x, $y)", k, ShevitsaPattern.kind(x.toFloat(), (y + n).toFloat(), p.toFloat(), false))
            }
            val m = ShevitsaStill.tileCells(p, ribbon = true)
            val half = ShevitsaPattern.ribbonRows(p) / 2
            for (x in -60..60) for (y in -half..half) {
                val k = ShevitsaPattern.kind(x.toFloat(), y.toFloat(), p.toFloat(), true)
                assertEquals("лента $p ($x, $y)", k, ShevitsaPattern.kind((x + m).toFloat(), y.toFloat(), p.toFloat(), true))
            }
        }
    }

    /**
     * Плочката, повторена от средата (както я рисува [ShevitsaStill.paint]), съвпада с
     * шейдъра без снопа — и през ръбовете си (периодът е верен, шевът не се вижда).
     */
    private fun checkTile(w: Int, h: Int, cell: Float, period: Int, ribbon: Boolean, colors: ShevitsaColors) {
        val spec = WeaveSpec(w.toFloat(), h.toFloat(), cell, period.toFloat(), ribbon, 0f, 0f, colors)
        val tile = ShevitsaStill.tile(spec)
        val img = tile.image.asAndroidBitmap()
        // Цяла клетка в пиксели: плочката е точно 1:1 с изгледа.
        assertEquals(tile.width, img.width.toFloat(), 0f)
        val shader = weave(w, h, cell, period, ribbon, colors)
        val ox = (spec.originX * cell).toInt()
        val oy = if (ribbon) 0 else (spec.originY * cell).toInt()
        var sum = 0L
        var max = 0
        var n = 0
        for (y in 0 until h) for (x in 0 until w) {
            val a = shader.getPixel(x, y)
            val b = img.getPixel(Math.floorMod(x - ox, img.width), if (ribbon) y else Math.floorMod(y - oy, img.height))
            for (sh in intArrayOf(16, 8, 0)) {
                // Шейдърът е върху черно, т.е. premultiplied; плочката се премултиплицира тук.
                val d = abs(((a ushr sh) and 0xFF) - premul(b, sh))
                sum += d
                if (d > max) max = d
                n++
            }
        }
        val mean = sum.toDouble() / n
        println("плочка ${img.width}×${img.height} (период $period, лента $ribbon): средна разлика $mean, най-голяма $max")
        assertTrue("средна разлика $mean", mean < 1.5)
        assertTrue("най-голяма разлика $max", max <= 48)
    }

    private fun premul(argb: Int, shift: Int): Int {
        val a = argb ushr 24
        val v = (argb ushr shift) and 0xFF
        return (v * a + 127) / 255
    }

    @Test fun fieldTileMatchesTheShader() = checkTile(300, 250, 6f, 18, ribbon = false, ShevitsaPalettes.ribbon(dark = false))

    @Test fun ribbonTileMatchesTheShader() =
        checkTile(400, 6 * ShevitsaPattern.ribbonRows(10), 6f, 10, ribbon = true, ShevitsaPalettes.ribbon(dark = true))

    /** Плочката е обща за процеса: друг размер на същия изглед (прехода на картата) не я рисува наново. */
    @Test fun tileIsCachedAcrossSizes() {
        val a = ShevitsaStill.tile(WeaveSpec(300f, 200f, 5.25f, 18f, false, 0.9f, 0f, ShevitsaPalettes.card))
        val b = ShevitsaStill.tile(WeaveSpec(612f, 387f, 5.25f, 18f, false, 0.9f, 0f, ShevitsaPalettes.card))
        assertSame(a, b)
        // Плочката е само един период (≈95 px), не целият изглед.
        assertEquals(95, a.image.width)
    }
}
