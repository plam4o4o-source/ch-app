package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.data.local.ReadingDiaryEntity
import org.chyavorec.app.ui.screens.my.DiaryActions
import org.chyavorec.app.ui.screens.my.DiaryPanel
import org.chyavorec.app.ui.screens.my.RosetteRating
import org.chyavorec.app.ui.screens.settings.DigestSettingsRows
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareCardContent
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals

/**
 * Седмичен преглед в настройките, „Читателски дневник“ (въпрос, оценка, бележка) и картата
 * „Препоръчай“ — светла и тъмна тема, обикновен и едър шрифт. За визуален преглед (клонът
 * „screenshots“) плюс проверка на регулируемата оценка за екранен четец.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class ReadingDiaryScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String, tag: String? = null) {
        compose.waitForIdle()
        val node = if (tag != null) compose.onNodeWithTag(tag) else compose.onRoot()
        val bmp = node.captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Themed(dark: Boolean, fontScale: Float = 1f, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
            ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { content() }
            }
        }
    }

    private val none = DiaryActions({}, {}, {}, {})

    private fun entry(rating: Int, note: String = "") =
        ReadingDiaryEntity("под игото вазов иван", "Под игото", "Вазов, Иван", 7L, rating, note, 2026, 1L)

    @Composable
    private fun Panels() = Column(Modifier.padding(16.dp)) {
        DiaryPanel(null, none, {})
        androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
        DiaryPanel(entry(4), none, {})
        androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
        DiaryPanel(entry(5, "Прочетох я на един дъх — задължително за лятото."), none, {})
    }

    @Test fun diaryPanelsLight() {
        compose.setContent { Themed(dark = false) { Panels() } }
        shot("w21-diary-01-panels-light")
    }

    @Test fun diaryPanelsDark() {
        compose.setContent { Themed(dark = true) { Panels() } }
        shot("w21-diary-02-panels-dark")
    }

    @Test fun diaryPanelsLargeFont() {
        compose.setContent { Themed(dark = false, fontScale = 2f) { Panels() } }
        shot("w21-diary-03-panels-font2")
    }

    @Composable
    private fun Settings(dark: Boolean) = Themed(dark, fontScale = 1f) {
        Column {
            DigestSettingsRows(
                digest = true, onDigest = {}, sections = setOf("u8", "d:за деца"),
                departments = listOf("за възрастни", "за деца"), onToggleSection = {}, tomorrow = false, onTomorrow = {},
            )
        }
    }

    @Test fun settingsRowsLight() {
        compose.setContent { Settings(dark = false) }
        shot("w21-digest-01-settings-light")
    }

    @Test fun settingsRowsDark() {
        compose.setContent { Settings(dark = true) }
        shot("w21-digest-02-settings-dark")
    }

    @Test fun settingsRowsLargeFont() {
        compose.setContent {
            Themed(dark = false, fontScale = 2f) {
                Column {
                    DigestSettingsRows(
                        digest = true, onDigest = {}, sections = setOf("u8"), departments = listOf("за деца"),
                        onToggleSection = {}, tomorrow = true, onTomorrow = {},
                    )
                }
            }
        }
        shot("w21-digest-03-settings-font2")
    }

    @Test fun settingsRowsOff() {
        compose.setContent {
            Themed(dark = false) {
                Column { DigestSettingsRows(false, {}, emptySet(), emptyList(), {}, false, {}) }
            }
        }
        shot("w21-digest-04-settings-off")
    }

    /** За екранния четец оценката е един регулируем елемент: „3 от 5“, плъзване сменя стойността. */
    @Test fun ratingIsOneAdjustableElement() {
        var rating by mutableIntStateOf(3)
        compose.setContent { Themed(dark = false) { RosetteRating(rating, { rating = it }, Modifier.testTag("rating")) } }
        val node = compose.onNodeWithTag("rating")
        val info = node.fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo]
        assertEquals(3f, info.current)
        assertEquals(0f..5f, info.range)
        assertEquals("3 от 5", node.fetchSemanticsNode().config[SemanticsProperties.StateDescription])
        node.performSemanticsAction(SemanticsActions.SetProgress) { it(4f) }
        compose.waitForIdle()
        assertEquals(4, rating)
        assertEquals("4 от 5", compose.onNodeWithTag("rating").fetchSemanticsNode().config[SemanticsProperties.StateDescription])
    }

    @Test fun ratingTouchTargetsAreAtLeast48dp() {
        compose.setContent { Themed(dark = false) { RosetteRating(2, {}, Modifier.testTag("rating")) } }
        val size = compose.onNodeWithTag("rating").fetchSemanticsNode().size
        val dp = size.height / density
        kotlin.test.assertTrue(dp >= 48f, "височина $dp dp")
        kotlin.test.assertTrue(size.width / density >= 5 * 48f)
    }

    private val density get() = androidx.test.core.app.ApplicationProvider.getApplicationContext<Application>().resources.displayMetrics.density

    private val book = CatalogBook(inv = 7, author = "Вазов, Иван", title = "Под игото")

    @Composable
    private fun Card(card: ShareCard) = Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp).testTag("card")) { ShareCardContent(card, null) }

    @Test fun recommendCardWithoutNote() {
        compose.setContent { Card(ShareCard.Recommend(book, 4, null, "https://chyavorec.org/elektronen-katalog#zapis=7")) }
        shot("w21-diary-04-card", tag = "card")
    }

    @Test fun recommendCardWithNote() {
        compose.setContent { Card(ShareCard.Recommend(book, 5, "Прочетох я на един дъх.", "https://chyavorec.org/elektronen-katalog#zapis=7")) }
        shot("w21-diary-05-card-note", tag = "card")
    }
}
