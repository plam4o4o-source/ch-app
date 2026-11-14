package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.screens.embroidery.EmbroideryEditor
import org.chyavorec.app.ui.screens.embroidery.EmbroideryScaffold
import org.chyavorec.app.ui.screens.embroidery.EmbroideryTemplate
import org.chyavorec.app.ui.share.GreetingComposerBody
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareCardContent
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.embroidery.Symmetry
import org.chyavorec.domain.occasion.GreetingCards
import org.chyavorec.domain.occasion.OccasionOrnament
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Честитката (картата и редакторът) и „Извези шевица“: снимки за преглед (клонът „screenshots“). */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w360dp-h640dp-xxhdpi")
class GreetingCardScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val url = "https://chyavorec.org"

    private fun save(name: String) {
        compose.waitForIdle()
        val bmp = compose.onNodeWithTag("card").captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private fun card(card: ShareCard, name: String) {
        compose.setContent {
            Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp).testTag("card")) { ShareCardContent(card, null) }
        }
        save(name)
    }

    private val babaMarta = ShareCard.Greeting(
        "baba_marta", "Честита Баба Марта!", "Бъдете здрави и честити! Нека мартеницата ви донесе здраве, късмет и усмивки.",
        "За баба Мария", OccasionOrnament.MARTENITSA, ShevitsaMotif.RHOMBUS, url,
    )

    @Test fun greetingMartenitsa() = card(babaMarta, "greet-01-baba-marta")

    @Test fun greetingWithoutOrnamentUsesTheMotif() = card(
        ShareCard.Greeting(
            "spring", "Честита пролет!", "Нека новата пролет донесе светлина, здраве и радост във всеки дом.", "", OccasionOrnament.NONE,
            ShevitsaMotif.TREE_OF_LIFE, url,
        ),
        "greet-02-spring-no-line",
    )

    /** Най-дългите входове: 60 букви без интервал, дълго заглавие и пожелание — не излизат от рамката. */
    @Test fun greetingWithOddInput() = card(
        babaMarta.copy(
            occasionId = "assumption", title = "Честита Голяма Богородица!", ornament = OccasionOrnament.EASTER_EGGS,
            wish = "Успение на Пресвета Богородица. Здраве и благодат! Честит имен ден на Мария, Мариана и всички, които празнуват.",
            line = GreetingCards.sanitizeLine("Ш".repeat(80) + " 🎉"),
        ),
        "greet-03-odd-input",
    )

    @Test fun greetingEnglishChristmas() = card(
        babaMarta.copy(
            occasionId = "christmas", title = "Merry Christmas!", ornament = OccasionOrnament.CHRISTMAS_STAR, motif = ShevitsaMotif.ROSETTE,
            wish = "Merry Christmas! Peace, health and joy to you and your loved ones.", line = "For Grandma Maria and Grandpa Ivan",
        ),
        "greet-04-christmas-en",
    )

    @Composable
    private fun Composer(dark: Boolean, scale: Float, text: String) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, scale)) {
                androidx.compose.material3.Surface(Modifier.fillMaxSize().testTag("card"), color = MaterialTheme.colorScheme.surfaceContainerLow) {
                    val line = rememberTextFieldState(text)
                    GreetingComposerBody(
                        babaMarta.title, babaMarta.wish, "baba_marta", OccasionOrnament.MARTENITSA, ShevitsaMotif.RHOMBUS, url,
                        line, busy = false, onShare = {},
                    )
                }
            }
        }
    }

    @Test fun composerLight() {
        compose.setContent { Composer(dark = false, scale = 1f, text = "За баба Мария") }
        save("greet-05-composer-light")
    }

    @Test fun composerDarkLargeFont() {
        compose.setContent { Composer(dark = true, scale = 1.6f, text = "За баба Мария и дядо Иван") }
        save("greet-06-composer-dark-large")
    }
}

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class EmbroideryScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Screen(editor: EmbroideryEditor, dark: Boolean, scale: Float) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, scale)) {
                EmbroideryScaffold(editor, remember { SnackbarHostState() }, onBack = {}, onSave = {}, onOpenMine = {}, onShare = {})
            }
        }
    }

    private fun withTemplate(t: EmbroideryTemplate, symmetry: Symmetry = Symmetry.NONE) = EmbroideryEditor().apply {
        chooseSymmetry(symmetry)
        load(t.pattern())
    }

    @Test fun treeOfLifeLight() {
        compose.setContent { Screen(withTemplate(EmbroideryTemplate.TREE), dark = false, scale = 1f) }
        shot("embroidery-01-tree-light")
    }

    @Test fun rosetteDarkMirrored() {
        compose.setContent { Screen(withTemplate(EmbroideryTemplate.ROSETTE, Symmetry.QUAD), dark = true, scale = 1f) }
        shot("embroidery-02-rosette-dark")
    }

    @Test fun zigzagLargeFont() {
        compose.setContent { Screen(withTemplate(EmbroideryTemplate.ZIGZAG, Symmetry.MIRROR), dark = false, scale = 1.6f) }
        shot("embroidery-03-zigzag-large")
    }

    @Test fun cursorModeShowsThePad() {
        val editor = withTemplate(EmbroideryTemplate.RHOMBUS).apply { cursorMode = true; moveCursor(-3, -2) }
        compose.setContent { Screen(editor, dark = false, scale = 1f) }
        shot("embroidery-04-cursor")
    }

    /**
     * Тесен телефон (320 dp) и шрифт 1.3: имената на мотивите се пренасят само между думите
     * („Осморъка“ — на един ред, „Дърво на / живота“), а действията са мрежа от хапчета ≥ 48 dp.
     */
    @Config(qualifiers = "bg-w320dp-h1400dp-xhdpi")
    @Test fun narrowFont13TemplateNamesKeepWords() {
        compose.setContent { Screen(withTemplate(EmbroideryTemplate.TREE), dark = false, scale = 1.3f) }
        compose.assertNoMidWordBreak("Осморъка", maxLines = 1)
        compose.assertNoMidWordBreak("Дърво на живота", maxLines = 2)
        compose.assertNoMidWordBreak("Кривулица", maxLines = 1)
        compose.onNode(hasText("Изчисти платното") and hasClickAction()).assertHeightIsAtLeast(48.dp)
        compose.onNode(hasText("Курсор") and hasClickAction()).assertHeightIsAtLeast(48.dp)
        compose.onNode(hasText("Осморъка")).performScrollTo()
        shot("embroidery-05-narrow-font13")
    }

    /** Курсорът слага и маха бод; „Върни“ работи през бутона в лентата. */
    @Test fun cursorPlacesAndRemovesAStitchAndUndoWorks() {
        val editor = EmbroideryEditor().apply { cursorMode = true }
        compose.setContent { Screen(editor, dark = false, scale = 1f) }
        assertEquals(0, editor.stitchCount)
        editor.placeAtCursor()
        compose.waitForIdle()
        assertEquals(1, editor.stitchCount)
        editor.placeAtCursor()
        assertEquals(0, editor.stitchCount)
        editor.undo()
        assertEquals(1, editor.stitchCount)
        compose.onNode(hasContentDescription("Върни")).performClick()
        compose.waitForIdle()
        assertEquals(0, editor.stitchCount)
        assertTrue(editor.canRedo)
    }

    @Test fun templatesAre15x15AndNotEmpty() {
        for (t in EmbroideryTemplate.entries) {
            val p = t.pattern()
            assertEquals(15, p.size, t.name)
            assertTrue(p.stitchCount > 20, t.name)
        }
    }
}
