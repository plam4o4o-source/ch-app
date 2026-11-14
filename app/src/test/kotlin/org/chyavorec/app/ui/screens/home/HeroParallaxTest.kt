package org.chyavorec.app.ui.screens.home

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.CompositionLocalProvider
import androidx.test.core.app.ApplicationProvider
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.core.FixedClock
import java.time.Instant
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.onRoot
import org.chyavorec.app.ui.components.OrnamentFrame
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import java.io.File
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** Дълбочината на снимката във въртележката: чистата математика и слоят (с и без намалено движение). */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class HeroParallaxTest {
    @get:Rule val compose = createComposeRule()

    @Test fun photoLagsEightPercentBehindTheCard() {
        // Картата е на 200 px вдясно от центъра → снимката се връща с 8 % от това (16 px наляво).
        assertEquals(-16f, HeroParallax.swipeShift(200f, margin = 100f))
        assertEquals(16f, HeroParallax.swipeShift(-200f, margin = 100f))
        assertEquals(0f, HeroParallax.swipeShift(0f, margin = 100f))
        // Никога повече от полето, което мащабът оставя — краищата на снимката не се показват.
        assertEquals(-10f, HeroParallax.swipeShift(5_000f, margin = 10f))
    }

    @Test fun scrollDriftIsBoundedByTwelveDpAndTheMargin() {
        assertEquals(0f, HeroParallax.scrollShift(0f, drift = 36f, margin = 40f))
        assertEquals(18f, HeroParallax.scrollShift(0.5f, drift = 36f, margin = 40f))
        assertEquals(36f, HeroParallax.scrollShift(3f, drift = 36f, margin = 40f))
        assertEquals(-36f, HeroParallax.scrollShift(-3f, drift = 36f, margin = 40f))
        assertEquals(20f, HeroParallax.scrollShift(1f, drift = 36f, margin = 20f))
    }

    @Test fun scaleCoversTheDriftOnLowCards() {
        assertEquals(1.08f, HeroParallax.scale(height = 1000f, drift = 10f))
        // Ниска карта: 12 dp плъзгане на 224 dp височина иска повече от 1.08, иначе ще се покаже ръб.
        val s = HeroParallax.scale(height = 224f, drift = 12f)
        assertTrue(s > 1.08f)
        assertTrue((s - 1f) * 224f / 2f >= 11.99f)
        assertEquals(1.08f, HeroParallax.scale(height = 0f, drift = 12f))
    }

    @Test fun tinyJitterDoesNotCostAFrame() {
        assertFalse(HeroParallax.moved(10f, 10.2f, 0.25f))
        assertTrue(HeroParallax.moved(10f, 10.4f, 0.25f))
    }

    @Test fun reducedMotionAddsNoModifiersAtAll() {
        val shift = HeroShift()
        assertSame(Modifier, Modifier.heroPhotoLayer(shift, enabled = false))
        assertSame(Modifier, Modifier.heroTrack(HeroAnchor(), shift, enabled = false))
        assertSame(Modifier, Modifier.heroContainer(HeroAnchor(), enabled = false))
    }

    @Test fun anchorRestIsExactWhileTheFirstRowIsVisibleAndKeptOtherwise() {
        var top: Int? = 120
        val a = HeroAnchor().also { it.topScroll = { top } }
        // Списъкът е превъртян с 120 px, картата е на 380 px → покоят е 500, превъртането — 120.
        assertEquals(120f, a.scrolledBy(380f))
        top = null
        // Първият ред вече не се вижда: покоят се пази (500), картата е на −100 → 600 px превъртане.
        assertEquals(600f, a.scrolledBy(-100f))
        // Запазеният покой (смяна на раздела, превъртян „Начало“) — без първия ред пак е точен.
        val b = HeroAnchor(a.rest)
        assertEquals(500f - 260f, b.scrolledBy(260f))
        // Непознат покой и непознат списък — първото видяно място (както преди).
        assertEquals(0f, HeroAnchor().scrolledBy(42f))
    }

    /** Въртележката излиза от списъка и се връща / „Начало“ се отваря превъртяно: най-горе снимката е в покой. */
    @Test fun photoRestsAtZeroAfterTheCarouselReentersOrStartsScrolled() {
        val shift = HeroShift()
        lateinit var list: LazyListState
        compose.setContent {
            // Като при връщане към раздела: списъкът е вече превъртян, когато въртележката се появи за първи път.
            list = rememberLazyListState(0, 600)
            val anchor = rememberHeroAnchor(list)
            LazyColumn(Modifier.size(300.dp, 500.dp).heroList(anchor), state = list) {
                item("header") { Box(Modifier.fillMaxWidth().height(320.dp)) }
                item("hero") { Box(Modifier.fillMaxWidth().height(160.dp).heroTrack(anchor, shift, enabled = true)) }
                items(20) { Box(Modifier.fillMaxWidth().height(200.dp)) }
            }
        }
        fun scrollTo(index: Int, offset: Int = 0) {
            compose.runOnIdle { runBlocking { list.scrollToItem(index, offset) } }
            compose.waitForIdle()
        }
        compose.waitForIdle()
        assertTrue(shift.scroll > 0f, "превъртяно: снимката е отместена (${shift.scroll})")
        scrollTo(0)
        assertEquals(0f, shift.scroll, 0.003f, "най-горе — покой")
        // Далеч надолу (редът на въртележката излиза от композицията) и обратно.
        scrollTo(15)
        scrollTo(1, 40)
        assertTrue(shift.scroll > 0f, "въртележката е горе на екрана: снимката е отместена (${shift.scroll})")
        scrollTo(0)
        assertEquals(0f, shift.scroll, 0.003f, "след връщане — пак покой")
    }

    private val enabled = mutableStateOf(false)
    private val cardOffset = mutableFloatStateOf(0f)

    private fun showPhoto() {
        compose.setContent {
            val shift = remember { HeroShift() }
            shift.cardOffset = cardOffset.floatValue
            Box(Modifier.size(100.dp).testTag("photo")) {
                Box(
                    Modifier.fillMaxSize().heroPhotoLayer(shift, enabled.value)
                        .background(Brush.horizontalGradient(listOf(Color.Red, Color.Blue))),
                )
            }
        }
    }

    private fun pixels(enabledNow: Boolean, dx: Float): List<Int> {
        compose.runOnIdle { enabled.value = enabledNow; cardOffset.floatValue = dx }
        compose.waitForIdle()
        val map = compose.onNodeWithTag("photo").captureToImage().toPixelMap()
        return (0 until map.width step 7).map { map[it, map.height / 2].toArgb() }
    }

    @Test fun reducedMotionKeepsThePhotoWhereItWas() {
        showPhoto()
        val plain = pixels(enabledNow = false, dx = 0f)
        // Намалено движение: картата може да е където и да е — снимката не се мести и не се мащабира.
        assertEquals(plain, pixels(enabledNow = false, dx = 4_000f))
    }

    @Test fun layerScalesAndShiftsWhenEnabled() {
        showPhoto()
        val plain = pixels(enabledNow = false, dx = 0f)
        val scaled = pixels(enabledNow = true, dx = 0f)
        // В покой снимката е само по-едра (мащаб ≈1.08–1.11), без отместване — различава се от плоската.
        assertNotEquals(plain, scaled)
        // Плъзгане: картата е далеч вдясно → снимката се връща наляво спрямо покоя.
        assertNotEquals(scaled, pixels(enabledNow = true, dx = 300f))
    }

    /** Снимка за преглед: рамката се изрисува (20 %, 60 %, 100 %), а снимката зад нея е изместена като при плъзгане. */
    @Test fun frameDrawsInOverShiftedPhoto() {
        val config = AppConfig(
            siteBaseUrl = "http://localhost", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = true, versionName = "test", versionCode = 1,
        )
        // Рамката чете мотива за днес от часовника на контейнера.
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config, FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            CompositionLocalProvider(LocalAppContainer provides container) {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(Modifier.background(Brand.Cream).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(0.2f to 0f, 0.6f to 120f, 1f to -120f).forEach { (p, dx) ->
                        val shift = remember { HeroShift().also { it.cardOffset = dx; it.scroll = dx / 240f } }
                        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(24.dp))) {
                            Box(
                                Modifier.fillMaxSize().heroPhotoLayer(shift, true)
                                    .background(Brush.linearGradient(listOf(Brand.Burgundy, Brand.Gold, Brand.Ink))),
                            )
                            OrnamentFrame(Modifier.matchParentSize(), alpha = 0.8f, progress = { p })
                        }
                    }
                }
            }
            }
        }
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File("build/reports/screenshots").apply { mkdirs() }.resolve("w21-hero-frame-draw-in.png")
            .outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
