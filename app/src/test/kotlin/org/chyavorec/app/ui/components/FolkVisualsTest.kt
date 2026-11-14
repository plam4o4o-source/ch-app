package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.GeneratedCover
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.ui.theme.ThemePalette
import org.chyavorec.app.ui.theme.ThemeStyle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Народният вид отвъд „Начало“: трите бода се бродират веднъж (флагът „показан“ оцелява),
 * намаленото движение дава крайното състояние веднага, ленената подвързия никога не изсветлява
 * корицата, а „нощната черга“ е само в тъмна тема без OLED.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg")
class FolkVisualsTest {
    @get:Rule val compose = createComposeRule()

    private fun pixels(tag: String): List<Int> {
        val p = compose.onNodeWithTag(tag).captureToImage().toPixelMap()
        return (0 until p.height).flatMap { y -> (0 until p.width).map { x -> p[x, y].toArgb() } }
    }

    private fun inked(tag: String, background: Color = Color.White) = pixels(tag).count { it != background.toArgb() }

    @Composable
    private fun Mark(reduced: Boolean) {
        CompositionLocalProvider(LocalReducedMotion provides reduced) {
            Box(Modifier.background(Color.White)) { StitchMark(Color.Black, Modifier.testTag("mark")) }
        }
    }

    @Test fun stitchFractionsRunOneAfterAnother() {
        assertEquals(0f, CrossStitches.fraction(0f, 0))
        assertEquals(1f, CrossStitches.fraction(1f, 2))
        // На третинката първият е готов, вторият още не е започнал.
        assertEquals(1f, CrossStitches.fraction(1f / 3f, 0), 1e-5f)
        assertEquals(0f, CrossStitches.fraction(1f / 3f, 1), 1e-5f)
        assertEquals(0.5f, CrossStitches.fraction(0.5f, 1), 1e-5f)
    }

    @Test fun stitchesEmbroiderOnceAndStayDoneAfterRestore() {
        compose.mainClock.autoAdvance = false
        val tester = StateRestorationTester(compose)
        tester.setContent { Mark(reduced = false) }
        // Преди бродирането — празно.
        assertEquals(0, inked("mark"))
        compose.mainClock.advanceTimeBy(250)
        val mid = inked("mark")
        assertTrue(mid > 0, "по средата вече има бодове")
        compose.mainClock.advanceTimeBy(600)
        val done = inked("mark")
        assertTrue(done > mid, "готовите бодове са повече от половината")
        // Завъртане/връщане: флагът е запазен — веднага готово, без ново бродиране.
        tester.emulateSavedInstanceStateRestore()
        assertEquals(done, inked("mark"))
    }

    @Test fun reducedMotionShowsTheFinishedStitchesAtOnce() {
        compose.mainClock.autoAdvance = false
        compose.setContent { Mark(reduced = true) }
        val first = inked("mark")
        assertTrue(first > 0)
        compose.mainClock.advanceTimeBy(700)
        assertEquals(first, inked("mark"))
    }

    @Test fun loaderIsAStaticRosetteWithReducedMotion() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Box(Modifier.background(Color.White)) { ChLoadingIndicator(Modifier.testTag("loader"), color = Color.Black) }
            }
        }
        val a = pixels("loader")
        compose.mainClock.advanceTimeBy(900)
        assertEquals(a, pixels("loader"))
        assertTrue(a.any { it != Color.White.toArgb() })
    }

    @Test fun loaderMovesWithoutReducedMotion() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                Box(Modifier.background(Color.White)) { ChLoadingIndicator(Modifier.testTag("loader"), color = Color.Black) }
            }
        }
        compose.mainClock.advanceTimeBy(100)
        val a = pixels("loader")
        compose.mainClock.advanceTimeBy(500)
        assertNotEquals(a, pixels("loader"))
    }

    @Test fun linenNeverLightensTheCover() {
        val tile = ClothWeave.linen.toPixelMap()
        var max = 0
        for (y in 0 until tile.height) for (x in 0 until tile.width) {
            max = maxOf(max, (tile[x, y].red * 255f).toInt())
        }
        assertTrue(max <= ClothWeave.LINEN_MAX, "най-светлата стойност на плочката: $max")
        // Overlay със s ≤ 0.5 дава резултат ≤ основата във всеки канал — контрастът (LIGHTEST_OVERLAY) не пада.
        val s = ClothWeave.LINEN_MAX / 255f
        for (palette in ThemePalette.entries) for (dark in listOf(false, true)) for (seed in 0 until 40) {
            val base = GeneratedCover.backgroundArgb("автор $seed", palette.coverAnchors, dark)
            for (shift in intArrayOf(16, 8, 0)) {
                val c = ((base shr shift) and 0xFF) / 255f
                val o = if (c < 0.5f) 2f * c * s else 1f - 2f * (1f - c) * (1f - s)
                assertTrue(o <= c + 1e-6f, "канал $c → $o")
            }
            assertTrue(GeneratedCover.readableOn(base))
        }
    }

    @Test fun sheenShaderDeclaresItsUniforms() {
        val declared = Regex("""uniform\s+(?:layout\(color\)\s+)?\S+\s+(\w+);""").findAll(GoldSheen.SOURCE).map { it.groupValues[1] }.toList()
        assertEquals(GoldSheen.UNIFORMS.sorted(), declared.sorted())
        // Ивицата започва и свършва извън кадъра.
        assertTrue(GoldSheen.center(0f) < 0f && GoldSheen.center(1f) > 1f)
    }

    @Composable
    private fun Title(key: Long, reduced: Boolean) {
        CompositionLocalProvider(LocalReducedMotion provides reduced, LocalAgslEnabled provides false) {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Box(Modifier.background(Color.White)) {
                    Text("Под игото", modifier = Modifier.testTag("title").goldSheen(key))
                }
            }
        }
    }

    @Test fun titleSheenIsOnceAndSkippedWithReducedMotion() {
        compose.mainClock.autoAdvance = false
        compose.setContent { Title(1L, reduced = false) }
        val plain = pixels("title")
        compose.mainClock.advanceTimeBy(120 + 250)
        assertNotEquals(plain, pixels("title"), "по средата отблясъкът променя буквите")
        compose.mainClock.advanceTimeBy(600)
        // След отблясъка слоят изчезва — буквите са както преди (допуска се дребно разлика от изглаждането).
        val after = pixels("title")
        val differing = plain.indices.count { plain[it] != after[it] }
        assertTrue(differing * 50 < plain.size, "различни точки: $differing от ${plain.size}")
    }

    @Test fun reducedMotionHasNoSheen() {
        compose.mainClock.autoAdvance = false
        compose.setContent { Title(2L, reduced = true) }
        val plain = pixels("title")
        for (t in listOf(150L, 250L, 400L)) {
            compose.mainClock.advanceTimeBy(t)
            assertEquals(plain, pixels("title"))
        }
    }

    @Composable
    private fun Night(dark: Boolean, oled: Boolean) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT, style = ThemeStyle(oledBlack = oled)) {
            Box(Modifier.size(64.dp).background(Color(0xFF1A1208)).testTag("night").nightCloth())
        }
    }

    @Test fun nightClothIsOnlyInTheDarkThemeWithoutOled() {
        val bg = Color(0xFF1A1208)
        compose.setContent { Night(dark = true, oled = false) }
        assertTrue(inked("night", bg) > 0, "тъмна тема — има тъкан")
        // Едва забележима: всеки канал се отклонява с под 8/255.
        val maxDelta = pixels("night").maxOf { c -> (0..2).maxOf { k -> kotlin.math.abs(((c shr (16 - 8 * k)) and 0xFF) - ((bg.toArgb() shr (16 - 8 * k)) and 0xFF)) } }
        assertTrue(maxDelta in 1..10, "най-голямо отклонение: $maxDelta")
    }

    @Test fun nightClothIsOffInOled() {
        compose.setContent { Night(dark = true, oled = true) }
        assertEquals(0, inked("night", Color(0xFF1A1208)))
    }

    @Test fun nightClothIsOffInTheLightTheme() {
        compose.setContent { Night(dark = false, oled = false) }
        assertEquals(0, inked("night", Color(0xFF1A1208)))
    }

    @Test fun folkShapesFitTheirPlate() {
        for (m in FolkMotif.entries) {
            val b = FolkShapes.of(m).scaledPath(10f, 10f, 10f).getBounds()
            assertTrue(b.left >= -0.5f && b.top >= -0.5f && b.right <= 20.5f && b.bottom <= 20.5f, "$m: $b")
            assertTrue(b.width > 12f && b.height > 12f, "$m: $b")
        }
    }

    @Test fun cornerIsDrawnOnlyWhilePressed() {
        val t = androidx.compose.runtime.mutableFloatStateOf(0f)
        compose.setContent {
            Box(Modifier.size(96.dp, 64.dp).background(Color.White).testTag("tile").stitchedCorner(Color.Black) { t.floatValue })
        }
        val rest = pixels("tile").sumOf { 255 - (it and 0xFF) }
        t.floatValue = 1f
        compose.waitForIdle()
        val pressed = pixels("tile").sumOf { 255 - (it and 0xFF) }
        assertEquals(0, rest, "в покой ъгълът не се рисува")
        assertTrue(pressed > 0, "при натискане ъгълът се вижда")
    }

    /** Нечетната последна плочка не оставя дупка: на телефон (2 колони) е на цялата ширина. */
    @Test @Config(qualifiers = "bg-w411dp-h891dp")
    fun oddLastTileSpansTheRow() {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                TileGrid(
                    listOf(
                        TileItem(FolkIcons.Messages, "Първа", null) {},
                        TileItem(FolkIcons.Events, "Втора", null) {},
                        TileItem(FolkIcons.Pin, "Трета", null) {},
                    ),
                )
            }
        }
        fun width(text: String) = compose.onNodeWithText(text).fetchSemanticsNode().boundsInRoot.width
        val half = width("Първа")
        assertEquals(half, width("Втора"), 1f)
        val full = width("Трета")
        assertTrue(full > half * 1.8f, "последната е на целия ред: $full срещу $half")
    }
}
