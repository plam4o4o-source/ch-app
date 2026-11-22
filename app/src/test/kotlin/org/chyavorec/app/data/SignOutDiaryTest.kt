package org.chyavorec.app.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.DiaryBook
import org.chyavorec.app.data.local.KeptDiary
import org.chyavorec.app.data.local.ReadingDiaryStore
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.AuthSession
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Читателският дневник при „Изход“: по подразбиране се трие с останалите следи; ако читателят
 * махне отметката в потвърждението — остава за следващия му вход, а вход на друг читател го
 * трие. Всеки друг изход (принудителен) го трие винаги.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SignOutDiaryTest {
    private val context = ApplicationProvider.getApplicationContext<Application>()
    private val clock = FixedClock(Instant.parse("2026-11-22T10:00:00Z"))
    private val config = AppConfig(
        siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "",
        environment = "test", useMockData = false, versionName = "test", versionCode = 1,
    )
    private val book = DiaryBook("Под игото", "Иван Вазов", 1L, 2026)

    /** Нов „процес“ със запазена сесия на [reader]: наблюдателите тръгват, сесията се чете. */
    private suspend fun signedIn(reader: String): AppContainer {
        SecureSessionStore(File(context.noBackupFilesDir, "secure/session.bin"), XorTestCipher())
            .save(AuthSession("a", "r", clock.now().toEpochMilli() + 3_600_000, reader), persist = true)
        val c = AppContainer(context, config, clock, XorTestCipher(), OkHttpClient())
        c.watchReader()
        c.authRepository.restore()
        assertEquals(AuthState.SignedIn(reader), c.authRepository.state.value)
        // Наблюдателят е видял входа.
        withTimeout(5_000) { while (!c.settings.readerTraces()) delay(20) }
        return c
    }

    private suspend fun AppContainer.diary() = database.readingDiary().all()

    /** Изчистването на следите е минало, когато флагът е свален. */
    private suspend fun AppContainer.awaitTracesCleared() = withTimeout(5_000) { while (settings.readerTraces()) delay(20) }

    private suspend fun rated(c: AppContainer) {
        ReadingDiaryStore(c.database.readingDiary(), clock).rate(book, 5)
        assertEquals(1, c.diary().size)
    }

    @Test fun defaultSignOutDeletesTheDiary(): Unit = runBlocking {
        val c = signedIn("R1")
        rated(c)
        c.signOut(keepDiary = false)
        c.awaitTracesCleared()
        assertTrue(c.diary().isEmpty())
        assertNull(c.settings.keptDiaryOwner())
    }

    @Test fun keptDiaryStaysAndComesBackForTheSameReader(): Unit = runBlocking {
        val c = signedIn("R1")
        rated(c)
        c.signOut(keepDiary = true)
        c.awaitTracesCleared()
        assertEquals(AuthState.SignedOut, c.authRepository.state.value)
        assertEquals(listOf(5), c.diary().map { it.rating }, "дневникът остава на телефона")
        assertEquals(KeptDiary.ownerKey("R1"), c.settings.keptDiaryOwner(), "помни се чий е (отпечатък, не номерът)")
        c.database.close()

        // Следващ вход на същия читател: дневникът е пак там, белегът „оставен“ — свален.
        val again = signedIn("R1")
        withTimeout(5_000) { while (again.settings.keptDiaryOwner() != null) delay(20) }
        assertEquals(listOf(5), again.diary().map { it.rating })
    }

    @Test fun keptDiaryIsDeletedWhenAnotherReaderSignsIn(): Unit = runBlocking {
        val c = signedIn("R1")
        rated(c)
        c.signOut(keepDiary = true)
        c.awaitTracesCleared()
        assertEquals(1, c.diary().size)
        c.database.close()

        val other = signedIn("R2")
        withTimeout(5_000) { while (other.settings.keptDiaryOwner() != null) delay(20) }
        assertTrue(other.diary().isEmpty(), "чуждият дневник не се показва на следващия читател")
    }

    /** Изтекла/отказана сесия, премахнат профил — изходът не е от потвърждението: дневникът се трие. */
    @Test fun forcedSignOutAlwaysDeletesTheDiary(): Unit = runBlocking {
        val c = signedIn("R1")
        rated(c)
        c.authRepository.signOutLocally()
        c.awaitTracesCleared()
        assertTrue(c.diary().isEmpty())
        assertNull(c.settings.keptDiaryOwner())
    }
}
