package org.chyavorec.app.ui.screens.quiz

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.ConfettiHost
import org.chyavorec.app.ui.components.ConfettiState
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.quiz.Quiz
import org.chyavorec.domain.quiz.QuizKind
import org.chyavorec.domain.quiz.QuizQuestion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * По-спокойната викторина (вълна 24): без реда с кръгчета, неподвижен знак на бележката, ✕ в ромб
 * на грешния избор, рамки в цвета на контура; пълната лента — кадър от конфетито (тръгва със
 * затварящия бод, над бутона) и кадри от златния отблясък. За преглед — не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class QuizScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

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

    private fun save(bmp: Bitmap, name: String) =
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }

    private fun capture(): Bitmap = compose.onRoot().captureToImage().asAndroidBitmap()

    @Composable
    private fun Page(state: QuizUi, dark: Boolean = false, reduced: Boolean = true, confetti: ConfettiState? = null) {
        CompositionLocalProvider(LocalReducedMotion provides reduced) {
            ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
                val content = @Composable {
                    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                        Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p -> Box(Modifier.padding(p)) { QuizContent(state, {}, {}, {}, {}, {}) } }
                    }
                }
                if (confetti != null) ConfettiHost(Modifier.fillMaxSize(), state = confetti) { content() } else content()
            }
        }
    }

    /** Грешен избор: ✕ в ромб, верният — ✓; знакът „Не точно“ е готов; горе — само „Въпрос 3 от 5“. */
    @Test fun calmWrongAnswer() {
        compose.setContent { Page(QuizUi.Playing(quiz, 2, 3, listOf(true, false, false))) }
        compose.waitForIdle()
        save(capture(), "c24-01-quiz-calm-wrong")
    }

    /** Нов въпрос в тъмна тема: рамките на отговорите — в цвета на контура. */
    @Test fun calmFreshDark() {
        compose.setContent { Page(QuizUi.Playing(quiz, 1, null, listOf(true)), dark = true) }
        compose.waitForIdle()
        save(capture(), "c24-02-quiz-calm-dark")
    }

    /** Таблет: въпросът вляво, вдясно — бележката, лентата и бутонът (без отделен ред с кръгчета). */
    @Config(qualifiers = "bg-w1280dp-h800dp-land-xhdpi")
    @Test fun calmTabletWrongDark() {
        compose.setContent { Page(QuizUi.Playing(quiz, 2, 3, listOf(true, false, false)), dark = true) }
        compose.waitForIdle()
        save(capture(), "c24-03-quiz-tablet-wrong-dark")
    }

    /** 5/5 в истинския домакин: кадър ~0.4 s след затварящия бод — ветрилото е над „Виж резултата“. */
    @Test fun closedBandBurst() {
        val confetti = ConfettiState(seed = 3)
        var ui by mutableStateOf<QuizUi>(QuizUi.Playing(quiz, 4, null, List(4) { true }))
        compose.setContent { Page(ui, reduced = false, confetti = confetti) }
        compose.waitForIdle()
        compose.mainClock.autoAdvance = false
        compose.runOnUiThread { ui = QuizUi.Playing(quiz, 4, 0, List(5) { true }); Snapshot.sendApplyNotifications() }
        compose.mainClock.advanceTimeBy(CLOSE_LEAD_MS + 260)
        val early = capture()
        compose.mainClock.advanceTimeBy(260)
        val late = capture()
        save(sideBySide(early, late), "c24-04-quiz-closed-burst")
        compose.mainClock.autoAdvance = true
    }

    /** Отблясъкът по готовата лента (без конфети): три кадъра от долната част на екрана. */
    @Test fun closedBandSheen() {
        var ui by mutableStateOf<QuizUi>(QuizUi.Playing(quiz, 4, null, List(4) { true }))
        compose.setContent { Page(ui, reduced = false) }
        compose.waitForIdle()
        compose.mainClock.autoAdvance = false
        compose.runOnUiThread { ui = QuizUi.Playing(quiz, 4, 0, List(5) { true }); Snapshot.sendApplyNotifications() }
        compose.mainClock.advanceTimeBy(1_120)
        val frames = listOf(0L, 110L, 110L, 110L, 600L).map { t ->
            compose.mainClock.advanceTimeBy(t)
            val full = capture()
            val h = (full.height * 0.24f).toInt()
            Bitmap.createBitmap(full, 0, full.height - h, full.width, h)
        }
        save(stacked(frames), "c24-05-quiz-closed-sheen-frames")
        compose.mainClock.autoAdvance = true
    }

    private fun sideBySide(a: Bitmap, b: Bitmap): Bitmap {
        val out = Bitmap.createBitmap(a.width + b.width, maxOf(a.height, b.height), Bitmap.Config.ARGB_8888)
        Canvas(out).apply { drawBitmap(a, 0f, 0f, null); drawBitmap(b, a.width.toFloat(), 0f, null) }
        return out
    }

    private fun stacked(frames: List<Bitmap>): Bitmap {
        val out = Bitmap.createBitmap(frames.first().width, frames.sumOf { it.height }, Bitmap.Config.ARGB_8888)
        val c = Canvas(out)
        var y = 0f
        for (f in frames) {
            c.drawBitmap(f, 0f, y, null)
            y += f.height
        }
        return out
    }
}
