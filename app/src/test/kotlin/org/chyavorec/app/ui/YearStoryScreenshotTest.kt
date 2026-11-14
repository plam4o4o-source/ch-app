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
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.screens.my.StoryMessage
import org.chyavorec.app.data.local.TextScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.platform.LocalDensity
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
import org.chyavorec.data.catalog.ReadingYear
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
    private val large = appFontScale(1.3f, TextScale.XLARGE.factor)
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun save(bmp: Bitmap, name: String) = File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun shown(text: String) =
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(text, substring = true) or hasContentDescription(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    /** Слайдът [kind] в средата на своя таймер: влизането е завършило, лентичката — наполовина. */
    private fun slide(kind: YearSlideKind, name: String, summaryText: String, fontScale: Float = 1f, summary: ReadingYear.Summary = ShareFixtures.year) {
        val index = yearSlides(summary).indexOf(kind)
        compose.mainClock.autoAdvance = false
        compose.setContent {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    CompositionLocalProvider(LocalReducedMotion provides false) {
                        YearStory(summary, onClose = {}, siteUrl = "https://chyavorec.org", initialSlide = index)
                    }
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

    /** Най-харесаната книга от дневника (4+ розетки) — след любимия автор. */
    @Test fun favourite() = slide(YearSlideKind.FAVOURITE, "w23-year-favourite", "Под игото", summary = ShareFixtures.yearWithFavourite)

    @Test fun favouriteLargeFont() =
        slide(YearSlideKind.FAVOURITE, "w23-year-favourite-large-font", "Под игото", fontScale = large, summary = ShareFixtures.yearWithFavourite)

    @Config(qualifiers = "bg-w891dp-h411dp-land-xhdpi")
    @Test fun favouriteLandscape() = slide(YearSlideKind.FAVOURITE, "w23-year-favourite-landscape", "Под игото", summary = ShareFixtures.yearWithFavourite)

    /** Хоризонтално: по-тесни отстъпи, по-малки цифри, а каквото не се побира — превърта се. */
    @Config(qualifiers = "bg-w891dp-h411dp-land-xhdpi")
    @Test fun landscapeBooks() = slide(YearSlideKind.BOOKS, "year-11-landscape-books", "28 книги")

    @Config(qualifiers = "bg-w891dp-h411dp-land-xhdpi")
    @Test fun landscapeMonth() = slide(YearSlideKind.MONTH, "year-12-landscape-month", "Септември")

    /** „Много голям“ в приложението и 1.3 в системата: текстът расте и се пренася, цифрите — не. */
    @Test fun largeFontBooks() = slide(YearSlideKind.BOOKS, "year-13-large-font-books", "28 книги", fontScale = large)

    @Test fun largeFontArchetype() = slide(YearSlideKind.ARCHETYPE, "year-14-large-font-archetype", "Книжен дракон", fontScale = large)

    /** Без връзка и без изтеглена история: „Няма връзка“ и „Опитай отново“, не „няма книги“. */
    @Test fun offline() {
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                Box(Modifier.fillMaxSize().background(Brand.Ink)) {
                    StoryMessage(
                        "Няма връзка", "Историята на четенето ти още не е на телефона. Провери връзката и опитай отново.",
                        onClose = {}, onRetry = {},
                    )
                }
            }
        }
        shown("Опитай отново")
        save(compose.onRoot().captureToImage().asAndroidBitmap(), "year-15-offline")
    }

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

    /** Картата за споделяне на най-харесаната книга — без бележката от дневника. */
    @Test fun yearFavourite() =
        card(ShareCard.Year(ShareFixtures.yearWithFavourite, YearSlideKind.FAVOURITE, "https://chyavorec.org"), "w23-share-year-favourite", photo = false)

    @Test fun formatsMatchTheirPixels() {
        kotlin.test.assertEquals(360f to 640f, ShareFormat.STORY.widthDp.value to ShareFormat.STORY.heightDp.value)
        kotlin.test.assertEquals(450f, ShareFormat.FEED.heightDp.value)
    }
}
