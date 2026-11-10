package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LivingShevitsa
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.ShevitsaPalettes
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.components.WeaveShader
import org.chyavorec.app.ui.components.WeaveSpec
import org.chyavorec.app.ui.screens.my.CardData
import org.chyavorec.app.ui.screens.my.FoilShader
import org.chyavorec.app.ui.screens.my.LibraryCard
import org.chyavorec.app.ui.screens.my.LibraryCardBack
import org.chyavorec.app.ui.screens.my.LocalFixedCardTilt
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ShevitsaShaders
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.MembershipStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * „Живата шевица“ и холографското фолио на картата — за визуален преглед (клонът
 * „screenshots“). AGSL вървят само ако Robolectric може да ги компилира; иначе
 * снимката показва неподвижния вариант. Отделните снимки „без AGSL“ са това, което
 * виждат телефоните до Android 12L.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1600dp-xhdpi")
class ShevitsaScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Label(text: String, dark: Boolean = false) =
        Text(text, fontSize = 11.sp, color = if (dark) Color(0xFFBBBBBB) else Color(0xFF555555), modifier = Modifier.padding(4.dp))

    /** Кадър със снопа по средата: неподвижен (намалено движение) при време [t]. */
    private fun frameAt(t: Float) = WeaveBudget().apply { advance(t) }

    /**
     * AGSL текстът се компилира, ако Robolectric поддържа RuntimeShader (иначе
     * конструкторът не е IllegalArgumentException — тя е грешката при компилиране),
     * и приема всички униформи, които кодът задава.
     */
    @Test fun shadersCompileAndTakeTheirUniforms() {
        for (src in listOf(ShevitsaShaders.WEAVE, ShevitsaShaders.FOIL)) {
            val error = runCatching { RuntimeShader(src) }.exceptionOrNull()
            println("AGSL: ${error?.let { it::class.simpleName + ": " + it.message } ?: "компилиран"}")
            assertFalse("AGSL: ${error?.message}", error is IllegalArgumentException)
        }
        if (runCatching { RuntimeShader(ShevitsaShaders.WEAVE) }.isSuccess) {
            val weave = WeaveShader.createOrNull()
            assertNotNull(weave)
            assertTrue(weave!!.configure(WeaveSpec(400f, 60f, 4f, 10f, true, 1f, 0f, ShevitsaPalettes.ribbon(false))))
            assertTrue(weave.setTime(2f))
            val foil = FoilShader.createOrNull()
            assertNotNull(foil)
            assertTrue(foil!!.resize(600f, 380f))
        }
    }

    @Composable
    private fun Ribbons(dark: Boolean) {
        val bg = if (dark) Brand.Ink else Brand.Cream
        Column(Modifier.fillMaxWidth().background(bg).padding(vertical = 6.dp)) {
            for ((agsl, t) in listOf(true to 0f, true to 3.6f, false to 0f)) {
                Label((if (agsl) "AGSL t=$t" else "без AGSL") + if (dark) ", тъмна" else ", светла", dark)
                CompositionLocalProvider(LocalAgslEnabled provides agsl) {
                    LivingShevitsa(ShevitsaPalettes.ribbon(dark), Modifier.fillMaxWidth().height(26.dp), period = 10, ribbon = true, budget = frameAt(t))
                }
            }
        }
    }

    @Test fun ribbon() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Column { Ribbons(dark = false); Ribbons(dark = true) }
            }
        }
        shot("shevitsa-1-ribbon")
    }

    /** Полето (картата, въведението) със и без AGSL. */
    @Test fun field() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Column(Modifier.background(Color.White).padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (agsl in listOf(true, false)) {
                        Label(if (agsl) "AGSL (карта, въведение), t=3.6" else "без AGSL")
                        CompositionLocalProvider(LocalAgslEnabled provides agsl) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(Modifier.size(196.dp, 300.dp).background(cardBackground)) {
                                    LivingShevitsa(ShevitsaPalettes.card, Modifier.size(196.dp, 300.dp), cell = 2.4.dp, intensity = 0.9f, budget = frameAt(3.6f))
                                }
                                Box(Modifier.size(196.dp, 300.dp).background(Brand.Cream)) {
                                    LivingShevitsa(ShevitsaPalettes.intro, Modifier.size(196.dp, 300.dp), cell = 3.dp, clearCenter = 0.55f, budget = frameAt(3.6f))
                                }
                            }
                        }
                    }
                }
            }
        }
        shot("shevitsa-2-field")
    }

    /**
     * Неподвижната плочка (вляво — шейдърът без снопа, вдясно — повторената плочка): картата
     * (конците на 100 %, за да се види разлика) и лентата в светла и тъмна тема.
     */
    @Test fun tileVsShader() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Column(Modifier.background(Color.White).padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Label("AGSL без сноп | плочка (без AGSL)")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (agsl in listOf(true, false)) {
                            CompositionLocalProvider(LocalAgslEnabled provides agsl) {
                                Box(Modifier.size(190.dp, 120.dp).background(cardBackground)) {
                                    LivingShevitsa(
                                        ShevitsaPalettes.card.copy(threadAlpha = 1f), Modifier.size(190.dp, 120.dp),
                                        cell = 2.4.dp, intensity = 0f,
                                    )
                                }
                            }
                        }
                    }
                    for (dark in listOf(false, true)) {
                        for (agsl in listOf(true, false)) {
                            CompositionLocalProvider(LocalAgslEnabled provides agsl) {
                                Box(Modifier.fillMaxWidth().background(if (dark) Brand.Ink else Brand.Cream).padding(vertical = 4.dp)) {
                                    LivingShevitsa(
                                        ShevitsaPalettes.ribbon(dark), Modifier.fillMaxWidth().height(26.dp),
                                        period = 10, ribbon = true, intensity = 0f,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        shot("shevitsa-6-tile-vs-shader")
    }

    private val data = CardData(holder = "Мария Петрова", number = "1024-5678", barcode = "10245678", status = MembershipStatus.ACTIVE, verified = true)

    private fun cards(name: String, dark: Boolean, agsl: Boolean) {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true, LocalAgslEnabled provides agsl) {
                ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                    Column(
                        Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp).width(379.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        for (tilt in listOf(Offset.Zero, Offset(0.75f, -0.45f))) {
                            Label("наклон ${tilt.x}, ${tilt.y}" + if (agsl) "" else ", без AGSL", dark)
                            CompositionLocalProvider(LocalFixedCardTilt provides tilt) {
                                LibraryCard(data)
                                LibraryCardBack(data, Modifier.padding(top = 12.dp))
                            }
                        }
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun cardLight() = cards("shevitsa-3-card-light", dark = false, agsl = true)

    @Test fun cardDark() = cards("shevitsa-4-card-dark", dark = true, agsl = true)

    @Test fun cardFallback() = cards("shevitsa-5-card-fallback", dark = false, agsl = false)

    private val cardBackground = Brush.linearGradient(listOf(Brand.Ink, Color(0xFF3A1A18), Brand.Burgundy))
}
