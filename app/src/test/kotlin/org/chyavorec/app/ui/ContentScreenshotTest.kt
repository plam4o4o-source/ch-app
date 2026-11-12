package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.screens.newspaper.NewspaperList
import org.chyavorec.app.ui.screens.newspaper.PageBar
import org.chyavorec.app.ui.screens.newspaper.RenderedPage
import org.chyavorec.app.ui.screens.newspaper.ZoomablePaper
import org.chyavorec.app.ui.screens.quiz.QuizContent
import org.chyavorec.app.ui.screens.quiz.QuizUi
import org.chyavorec.app.ui.screens.timeline.OnThisDayContent
import org.chyavorec.app.ui.screens.timeline.Timeline
import org.chyavorec.app.ui.share.ContentShareCards
import org.chyavorec.app.ui.share.ShareCardContent
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.DocumentKind
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.quiz.Quiz
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizKind
import org.chyavorec.domain.quiz.QuizQuestion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate

/**
 * „Старият вестник“ (четец върху нарисувана страница-образец — PdfRenderer няма в Robolectric),
 * „Лента на времето“, „Познай Яворец“ и картите за споделяне — светла/тъмна тема и едър шрифт.
 * Снимки за преглед (клонът „screenshots“); не сравняват пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class ContentScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String, tag: String? = null) {
        compose.waitForIdle()
        val node = if (tag == null) compose.onNodeWithTag("root") else compose.onNodeWithTag(tag)
        val bmp = node.captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Frame(dark: Boolean, fontScale: Float = 1f, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides true) {
            ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).testTag("root")) { content() }
            }
        }
    }

    /** Страница от стар вестник: заглавие и колони „текст“ (чер на бял фон — както в PDF-а). */
    private fun samplePage(): RenderedPage {
        val w = 1200; val h = 1700
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        val ink = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK; typeface = Typeface.SERIF; textAlign = Paint.Align.CENTER; textSize = 96f }
        c.drawText("Баба Дамяна", w / 2f, 190f, ink)
        c.drawText("разказва", w / 2f, 300f, ink)
        val line = Paint().apply { color = Color.rgb(40, 40, 40); strokeWidth = 7f }
        var rnd = 7
        for (col in 0 until 2) {
            var y = 420f
            while (y < h - 140f) {
                rnd = (rnd * 1103515245 + 12345) and 0x7fffffff
                val len = 430f - (rnd % 90)
                val x = 90f + col * 540f
                c.drawLine(x, y, x + len, y, line)
                y += 36f
            }
        }
        return RenderedPage(0, bmp.asImageBitmap(), w, h, bmp)
    }

    @Test fun readerLight() {
        val page = samplePage()
        compose.setContent {
            Frame(dark = false) {
                Scaffold(topBar = { BackTopBar("Баба Дамяна разказва", {}) }) { p ->
                    Column(Modifier.padding(p)) {
                        Box(Modifier.weight(1f)) { ZoomablePaper(page, 0, "Страница 1 от 2") }
                        PageBar(0, 2, {}, {})
                    }
                }
            }
        }
        shot("c21-01-reader-light")
    }

    @Test fun readerDark() {
        val page = samplePage()
        compose.setContent {
            Frame(dark = true) {
                Scaffold(topBar = { BackTopBar("Баба Дамяна разказва", {}) }) { p ->
                    Column(Modifier.padding(p)) {
                        Box(Modifier.weight(1f)) { ZoomablePaper(page, 0, "Страница 1 от 2") }
                        PageBar(0, 2, {}, {})
                    }
                }
            }
        }
        shot("c21-02-reader-dark")
    }

    private val articles = listOf(
        SiteDocument("a", "Баба Дамяна разказва", "https://chyavorec.org/vestnik/a.pdf", category = "Статия 01 · Спомени", description = "Спомен от Освобождението — десетгодишно дете преживява войната, страха и радостта от края на робството.", kind = DocumentKind.PUBLICATION),
        SiteDocument("b", "Първа копка", "https://chyavorec.org/vestnik/b.pdf", category = "Статия 06 · Строеж", description = "Как започна строежът на читалищната сграда.", kind = DocumentKind.PUBLICATION),
        SiteDocument("c", "Нашият театър", "https://chyavorec.org/vestnik/c.pdf", category = "Статия 09 · Култура", kind = DocumentKind.PUBLICATION),
    )

    @Test fun newspaperListLight() {
        compose.setContent { Frame(dark = false) { Scaffold(topBar = { BackTopBar("Старият вестник", {}) }) { p -> Box(Modifier.padding(p)) { NewspaperList(articles) {} } } } }
        shot("c21-03-newspaper-list-light")
    }

    @Test fun newspaperListDarkLargeFont() {
        compose.setContent { Frame(dark = true, fontScale = 1.3f) { Scaffold(topBar = { BackTopBar("Старият вестник", {}) }) { p -> Box(Modifier.padding(p)) { NewspaperList(articles) {} } } } }
        shot("c21-04-newspaper-list-dark-font13")
    }

    @Test fun timelineLight() {
        compose.setContent { Frame(dark = false) { Scaffold(topBar = { BackTopBar("Лента на времето", {}) }) { p -> Box(Modifier.padding(p)) { Timeline(QuizDefaults.timeline, staticFrame = true) } } } }
        shot("c21-05-timeline-light")
    }

    @Test fun timelineDarkLargeFont() {
        compose.setContent { Frame(dark = true, fontScale = 1.3f) { Scaffold(topBar = { BackTopBar("Лента на времето", {}) }) { p -> Box(Modifier.padding(p)) { Timeline(QuizDefaults.timeline, staticFrame = true) } } } }
        shot("c21-06-timeline-dark-font13")
    }

    @Test fun onThisDayCard() {
        val today = LocalDate.of(2026, 12, 17)
        val entry = QuizDefaults.timeline.first { it.day == 17 && it.month == 12 }
        compose.setContent {
            Column {
                Frame(dark = false) { Box { OnThisDayContent(entry, today, {}) } }
            }
        }
        shot("c21-07-on-this-day")
    }

    private val quiz = Quiz(
        "2026-11-12",
        listOf(
            QuizQuestion("1", QuizKind.TIMELINE_YEAR, "Открита читалищната сграда", listOf("1955", "1958", "1961", "1967"), 2, note = "Нова сграда с библиотека, голям салон и два малки салона."),
            QuizQuestion("2", QuizKind.CATALOG_AUTHOR, "Високо под земята", listOf("Съни Сънински", "Вера Мутафчиева", "Хелън Полард", "Линдзи Келк"), 0),
            QuizQuestion("3", QuizKind.TIMELINE_EVENT, "1934", listOf("Дружество „Воля“", "Първият радиоапарат в селото", "Първа копка", "Проект „Глобални библиотеки“"), 1),
            QuizQuestion("4", QuizKind.CUSTOM, "Кога е съборът на Яворец?", listOf("6 май", "28 август", "1 юни"), 1),
            QuizQuestion("5", QuizKind.CATALOG_TITLE, "Вера Мутафчиева", listOf("Бомбите", "Анатомът", "Алтъна", "Безсрамна добродетел"), 0),
        ),
    )

    @Test fun quizQuestionFresh() {
        compose.setContent { Frame(dark = false) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Playing(quiz, 0, null, emptyList()), {}, {}, {}, {}, {}) } } } }
        shot("c21-08-quiz-question")
    }

    @Test fun quizAnsweredRightDark() {
        compose.setContent { Frame(dark = true) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Playing(quiz, 1, 0, listOf(true, true)), {}, {}, {}, {}, {}) } } } }
        shot("c21-09-quiz-right-dark")
    }

    @Test fun quizAnsweredWrongLargeFont() {
        compose.setContent { Frame(dark = false, fontScale = 1.3f) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Playing(quiz, 0, 3, listOf(false)), {}, {}, {}, {}, {}) } } } }
        shot("c21-10-quiz-wrong-font13")
    }

    @Test fun quizResultLight() {
        compose.setContent { Frame(dark = false) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Finished(quiz, listOf(true, true, false, true, true), 3, 5), {}, {}, {}, {}, {}) } } } }
        shot("c21-11-quiz-result-light")
    }

    @Test fun quizResultDarkLargeFont() {
        compose.setContent { Frame(dark = true, fontScale = 1.3f) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Finished(quiz, listOf(false, true, false, true, false), 1, 1), {}, {}, {}, {}, {}) } } } }
        shot("c21-12-quiz-result-dark-font13")
    }

    @Test fun quizUnavailable() {
        compose.setContent { Frame(dark = false) { Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(QuizUi.Unavailable, {}, {}, {}, {}, {}) } } } }
        shot("c21-13-quiz-unavailable")
    }

    // --- карти за споделяне (1080×1350 и 1080×1920 при плътност 3) ---

    private fun card(card: org.chyavorec.app.ui.share.ShareCard, name: String) {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(3f, 1f)) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp).testTag("card")) { ShareCardContent(card, null) }
                }
            }
        }
        shot(name, "card")
    }

    @Test fun newspaperShareCard() = card(ContentShareCards.newspaper("Баба Дамяна разказва", "Статия 01 · Спомени", "https://chyavorec.org/vestnik/baba_damjana_razkazva.pdf"), "c21-14-share-newspaper")

    @Test fun quizShareCard() = card(ContentShareCards.quiz("12 ноември 2026", listOf(true, true, false, true, true), 3, "https://chyavorec.org"), "c21-15-share-quiz")
}
