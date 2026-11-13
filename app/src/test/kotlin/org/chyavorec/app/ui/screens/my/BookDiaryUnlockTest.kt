package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.FakeDeviceUnlock
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.LocalRequireUnlock
import org.chyavorec.app.ui.share.ShareImageState
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.CatalogBook
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Дневникът на страницата на книгата е публичен екран (без UnlockGate): при „Искай отключване“ и
 * заключено лично оценката и бележката не се показват; отключено или с изключена настройка — да.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class BookDiaryUnlockTest {
    @get:Rule val compose = createComposeRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val backend = TestBackend()
    private lateinit var c: AppContainer
    private val api = TestBackend.API

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.json(
            "$api/v1/capabilities",
            """{"apiVersion":1,"login":true,"profile":true,"loans":true,"membership":false,"renew":true,"history":true,"messages":false}""",
        )
        backend.json(
            "$api/v1/me/history",
            """{"items":[{"loanId":"L1","inv":123456,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-20","dateIn":"2026-10-10"}]}""",
        )
        c = backend.container(app, FixedClock(Instant.parse("2026-11-01T10:00:00Z")), unlock = FakeDeviceUnlock(true, true))
        assertIs<Outcome.Success<Unit>>(c.authRepository.login("0042", "1234".toCharArray(), remember = false))
        assertIs<Outcome.Success<*>>(c.libraryRepository.history(force = true))
    }

    @After fun tearDown() = backend.shutdown()

    private fun show(requireUnlock: Boolean) {
        compose.setContent {
            CompositionLocalProvider(LocalAppContainer provides c, LocalRequireUnlock provides requireUnlock) {
                ChitalishteTheme(mode = ThemeMode.LIGHT) {
                    BookDiarySection(CatalogBook(inv = 123456L, author = "Иван Вазов", title = "Под игото"), ShareImageState())
                }
            }
        }
    }

    private fun question() = compose.onAllNodes(hasText(app.getString(R.string.diary_question))).fetchSemanticsNodes().isNotEmpty()

    @Test fun lockedPersonalAreaHidesTheDiary() {
        show(requireUnlock = true)
        compose.waitForIdle()
        // Историята се чете асинхронно; на заключено лично панелът така или иначе не трябва да се появи.
        Thread.sleep(500)
        compose.waitForIdle()
        assertTrue(!question())
    }

    @Test fun unlockedShowsTheDiary() {
        c.personalUnlock.unlock()
        show(requireUnlock = true)
        compose.waitUntil(10_000) { question() }
    }

    @Test fun settingOffShowsTheDiary() {
        show(requireUnlock = false)
        compose.waitUntil(10_000) { question() }
    }

    @Test fun lockingAgainHidesTheDiary() {
        c.personalUnlock.unlock()
        show(requireUnlock = true)
        compose.waitUntil(10_000) { question() }
        compose.runOnIdle { c.personalUnlock.lock() }
        compose.waitUntil(10_000) { !question() }
    }
}
