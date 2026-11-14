package org.chyavorec.app.ui.screens.quiz

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.quiz.QuizDefaults
import org.chyavorec.domain.quiz.QuizGenerator
import org.chyavorec.domain.quiz.QuizSources
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** Резултатът на викторината: TalkBack чете „Назад“ веднъж (стрелката), а бутонът отдолу е „Затвори“. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class QuizResultSemanticsTest {
    @get:Rule val compose = createComposeRule()

    @Test fun resultHasOneBackAndACloseButton() {
        val quiz = assertNotNull(QuizGenerator.generate(LocalDate.parse("2026-11-14"), QuizSources(timeline = QuizDefaults.timeline)))
        var closed = 0
        compose.setContent {
            ChitalishteTheme {
                Scaffold(topBar = { BackTopBar("Познай Яворец", {}) }) { p ->
                    Box(Modifier.padding(p)) {
                        QuizContent(QuizUi.Finished(quiz, quiz.questions.map { true }, 2, 4), {}, {}, {}, {}, onBack = { closed++ })
                    }
                }
            }
        }
        val back = compose.onAllNodes(hasContentDescription("Назад") or hasText("Назад"), useUnmergedTree = true).fetchSemanticsNodes()
        assertEquals(1, back.size, "„Назад“ — само стрелката горе")
        compose.onNode(hasText("Затвори") and hasClickAction()).performClick()
        assertEquals(1, closed)
    }
}
