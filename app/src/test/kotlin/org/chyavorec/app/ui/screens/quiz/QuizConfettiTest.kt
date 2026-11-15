package org.chyavorec.app.ui.screens.quiz

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.CONFETTI_MS
import org.chyavorec.app.ui.components.CONFETTI_PARTICLES
import org.chyavorec.app.ui.components.ConfettiHost
import org.chyavorec.app.ui.components.ConfettiParticles
import org.chyavorec.app.ui.components.ConfettiPhysics
import org.chyavorec.app.ui.components.ConfettiState
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.AppMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.quiz.Quiz
import org.chyavorec.domain.quiz.QuizKind
import org.chyavorec.domain.quiz.QuizQuestion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Викторината в истинския [ConfettiHost] (както под корена на приложението): петият верен отговор
 * наистина изиграва конфетито, рано (със затварящия бод) и така, че ветрилото е над бутона.
 * Отговорите са бутони със състояние за TalkBack.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class QuizConfettiTest {
    @get:Rule val compose = createComposeRule()

    private val quiz = Quiz("2026-11-12", List(5) { QuizQuestion("q$it", QuizKind.CUSTOM, "Въпрос ${it + 1}?", listOf("Ала", "Бала", "Ница"), 1) })

    @Test fun theFifthRightAnswerPlaysTheRealHostAboveTheButton() {
        val confetti = ConfettiState(seed = 7)
        var ui by mutableStateOf<QuizUi>(QuizUi.Playing(quiz, 4, null, List(4) { true }))
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                ChitalishteTheme {
                    ConfettiHost(Modifier.fillMaxSize(), state = confetti) { QuizContent(ui, {}, {}, {}, {}, {}) }
                }
            }
        }
        compose.waitForIdle()
        assertEquals(0, confetti.played, "вече отговорените не пускат конфети")

        compose.mainClock.autoAdvance = false
        compose.runOnIdle { ui = QuizUi.Playing(quiz, 4, 1, List(5) { true }) }
        // Още докато иглата довършва петия мотив — конфетито вече лети (не 1.5–3 s по-късно).
        compose.mainClock.advanceTimeBy(AppMotion.StitchMs.toLong())
        assertEquals(1, confetti.bursts)
        assertTrue(confetti.isPlaying, "домакинът не рисува конфетито")
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
        assertEquals(1, confetti.played, "домакинът не изигра избухването")
        assertFalse(confetti.isPlaying, "конфетито не спря")

        // Откъде: над лентата и над бутона под нея.
        val origin = confetti.origin
        assertTrue(origin.isSpecified)
        val button = compose.onNode(hasText("Виж резултата")).fetchSemanticsNode().boundsInRoot
        val screen = compose.onRoot().fetchSemanticsNode().boundsInRoot
        assertTrue(origin.y < button.top)
        assertEquals(screen.center.x, origin.x, screen.width * 0.05f)

        // Същото ветрило като в домакина (зърното + номера на избухването): плътните частици
        // остават над бутона, а под долния ръб изчезват по-малко от досегашните ~22 %.
        val particles = ConfettiParticles(CONFETTI_PARTICLES).apply { reset(Random(7L + confetti.bursts)) }
        val d = compose.density.density
        var opaque = 0
        var overButton = 0
        var shown = 0
        var belowEdge = 0
        for (f in 0..60) {
            val p = f / 60f
            val t = p * CONFETTI_MS / 1000f
            val a = ConfettiPhysics.alpha(p)
            for (i in 0 until particles.count) {
                val y = origin.y + ConfettiPhysics.dy(particles.vy[i], t) * d
                if (a >= 1f) {
                    opaque++
                    if (y >= button.top) overButton++
                }
                if (a > 0f) {
                    shown++
                    if (y > screen.bottom) belowEdge++
                }
            }
        }
        assertTrue(overButton <= opaque * 0.05f, "плътни частици над бутона: $overButton от $opaque")
        assertTrue(belowEdge <= shown * 0.18f, "под ръба на екрана: $belowEdge от $shown")
    }

    @Test fun reducedMotionClosesWithoutConfetti() {
        val confetti = ConfettiState(seed = 7)
        var ui by mutableStateOf<QuizUi>(QuizUi.Playing(quiz, 4, null, List(4) { true }))
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme {
                    ConfettiHost(Modifier.fillMaxSize(), state = confetti) { QuizContent(ui, {}, {}, {}, {}, {}) }
                }
            }
        }
        compose.runOnIdle { ui = QuizUi.Playing(quiz, 4, 1, List(5) { true }) }
        compose.waitForIdle()
        assertEquals(0, confetti.played)
        assertFalse(confetti.isPlaying)
    }

    /** TalkBack: всеки отговор е „бутон“; след избора — „Верен отговор“ / „Грешен избор“. */
    @Test fun answersAreButtonsWithState() {
        var ui by mutableStateOf<QuizUi>(QuizUi.Playing(quiz, 0, null, emptyList()))
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme { QuizContent(ui, {}, {}, {}, {}, {}) }
            }
        }
        fun node(text: String) = compose.onNode(hasText(text)).fetchSemanticsNode().config
        for (t in listOf("Ала", "Бала", "Ница")) {
            assertEquals(Role.Button, node(t).getOrNull(SemanticsProperties.Role), "„$t“ не е бутон")
            assertEquals(null, node(t).getOrNull(SemanticsProperties.StateDescription))
        }
        compose.runOnIdle { ui = QuizUi.Playing(quiz, 0, 2, listOf(false)) }
        compose.waitForIdle()
        assertEquals(Role.Button, node("Ница").getOrNull(SemanticsProperties.Role))
        assertEquals("Грешен избор", node("Ница").getOrNull(SemanticsProperties.StateDescription))
        assertEquals("Верен отговор", node("Бала").getOrNull(SemanticsProperties.StateDescription))
        assertEquals(null, node("Ала").getOrNull(SemanticsProperties.StateDescription))
    }
}
