package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.screens.my.YearSlideKind
import org.chyavorec.app.ui.screens.my.YearStory
import org.chyavorec.app.ui.screens.my.YearStoryCard
import org.chyavorec.app.ui.screens.my.yearSlides
import org.chyavorec.app.ui.share.ShareActionButton
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareCardContent
import org.chyavorec.app.ui.share.ShareFixtures
import org.chyavorec.app.ui.share.ShareFormat
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * „Моята читателска година“ (слайдовете, картата в „Моето“) и картите за споделяне — с
 * примерните данни на демо входа ([ShareFixtures]). Записват се до другите снимки.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class YearStoryScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun save(bmp: Bitmap, name: String) = File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun shown(text: String) =
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(text, substring = true) or hasContentDescription(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    /** Слайдът [kind] в средата на своя таймер: влизането е завършило, лентичката — наполовина. */
    private fun slide(kind: YearSlideKind, name: String, summaryText: String) {
        val index = yearSlides(ShareFixtures.year).indexOf(kind)
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                CompositionLocalProvider(LocalReducedMotion provides false) {
                    YearStory(ShareFixtures.year, onClose = {}, siteUrl = "https://chyavorec.org", initialSlide = index)
                }
            }
        }
        compose.mainClock.advanceTimeBy(2_600)
        shown(summaryText)
        save(compose.onRoot().captureToImage().asAndroidBitmap(), name)
    }

    @Test fun intro() = slide(YearSlideKind.INTRO, "year-01-intro", "2026")

    @Test fun books() = slide(YearSlideKind.BOOKS, "year-02-books", "28 книги")

    @Test fun author() = slide(YearSlideKind.AUTHOR, "year-03-author", "Иван Вазов")

    @Test fun section() = slide(YearSlideKind.SECTION, "year-04-section", "Език. Литература")

    @Test fun month() = slide(YearSlideKind.MONTH, "year-05-month", "Септември")

    @Test fun streak() = slide(YearSlideKind.STREAK, "year-06-streak", "11 месеца")

    @Test fun archetype() = slide(YearSlideKind.ARCHETYPE, "year-07-archetype", "Книжен дракон")

    @Test fun outro() = slide(YearSlideKind.OUTRO, "year-08-outro", "Благодарим ти")

    /** Картата в „Моето“: голяма (декември–януари) и малка (през годината), светла и тъмна тема. */
    private fun hubCards(mode: ThemeMode, name: String) {
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(vertical = 16.dp)) {
                    YearStoryCard(ShareFixtures.year, prominent = true, onOpen = {})
                    Spacer(Modifier.height(24.dp))
                    SectionLabel("Моето четене", Modifier.padding(start = 20.dp, bottom = 12.dp))
                    YearStoryCard(ShareFixtures.year, prominent = false, onOpen = {})
                }
            }
        }
        shown("Виж историята")
        save(compose.onRoot().captureToImage().asAndroidBitmap(), name)
    }

    @Test fun hubCardsLight() = hubCards(ThemeMode.LIGHT, "year-09-hub-cards-light")

    @Test fun hubCardsDark() = hubCards(ThemeMode.DARK, "year-10-hub-cards-dark")

    /** Листът „Сподели“ — картинка (с умалена карта) или връзка. */
    @OptIn(ExperimentalMaterial3Api::class)
    @Test fun shareChooser() {
        val card = ShareCard.Event("Празник на селото", "събота, 26 септември 2026 · 18:00", "26", "септември", "Читалищен салон", null, "https://chyavorec.org/events/praznik")
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
                    TopAppBar(title = { Text("Събитие") }, actions = {
                        ShareActionButton(onShareLink = {}, image = { ShareImageRequest(card, card.title, card.url) })
                    })
                }
            }
        }
        compose.onAllNodes(hasContentDescription("Сподели") and hasClickAction()).onFirst().performClick()
        shown("Сподели като картинка")
        save(compose.captureScreen(), "share-06-chooser")
    }
}

/**
 * Картите за споделяне в истинския си размер: екран 360×640 dp при плътност 3 = 1080×1920 px
 * (историята); новината е 1080×1350 (лентата).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w360dp-h640dp-xxhdpi")
class ShareCardsScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun card(card: ShareCard, name: String, photo: Boolean) {
        val image = if (photo) ShareFixtures.photo() else null
        compose.setContent {
            Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp).testTag("card")) { ShareCardContent(card, image) }
        }
        compose.waitForIdle()
        val bmp = compose.onNodeWithTag("card").captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private val event = ShareCard.Event(
        "Празник на селото и 45-ти събор на народното творчество", "събота, 26 септември 2026 · 18:00", "26", "септември",
        "Читалищен салон", "https://chyavorec.org/pictures/sabor.jpg", "https://chyavorec.org/events/praznik-na-selo-2026",
    )

    @Test fun eventWithPhoto() = card(event, "share-01-event", photo = true)

    @Test fun eventWithoutPhoto() = card(event.copy(title = "Коледен концерт на децата от школите"), "share-02-event-no-photo", photo = false)

    @Test fun book() = card(ShareCard.Book(ShareFixtures.book, null, "https://chyavorec.org/elektronen-katalog#zapis=1234"), "share-03-book", photo = false)

    @Test fun news() = card(
        ShareCard.News(
            "Читалището получи дарение от 120 нови книги за детския отдел", "4 ноември 2026",
            "Благодарим на всички дарители!", "https://chyavorec.org/pictures/darenie.jpg", "https://chyavorec.org/news/darenie-120-knigi",
        ),
        "share-04-news", photo = true,
    )

    @Test fun yearSlide() = card(ShareCard.Year(ShareFixtures.year, YearSlideKind.MONTH, "https://chyavorec.org"), "share-05-year-month", photo = false)

    @Test fun formatsMatchTheirPixels() {
        kotlin.test.assertEquals(360f to 640f, ShareFormat.STORY.widthDp.value to ShareFormat.STORY.heightDp.value)
        kotlin.test.assertEquals(450f, ShareFormat.FEED.heightDp.value)
    }
}
