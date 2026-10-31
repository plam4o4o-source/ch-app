package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.TestBackend
import org.chyavorec.app.data.security.SavedLogin
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.data.repository.AuthState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * [LoginViewModel] и мениджърът на пароли: предложение за запазване — само след успешен вход
 * със „Запомни ме“ и въведени на ръка карта и ПИН; запазеният вход минава през същия вход.
 * Мениджърът се пита при отваряне само ако от приложението е запазван вход, извън
 * заключването и с ограничено време; ПИН-ът не остава в полето при заключване.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LoginSaveOfferTest {
    @get:Rule val main = MainDispatcherRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val backend = TestBackend()
    private lateinit var c: AppContainer
    private val login = "${TestBackend.API}/v1/auth/login"

    @Before fun setUp() {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        c = backend.container(app, FixedClock(Instant.parse("2026-10-17T10:00:00Z")))
        runBlocking { c.settings.setPasswordManagerUsed(false) }
    }

    @After fun tearDown() = backend.shutdown()

    private fun until(done: () -> Boolean) = runBlocking<Unit> {
        withTimeout(10_000) { while (!done()) { yield(); Thread.sleep(10) } }
    }

    private fun vm() = LoginViewModel(c).also { vm -> until { vm.capabilitiesStatus.value is org.chyavorec.data.repository.CapabilitiesStatus.Known } }

    @Test fun manualSuccessfulLoginOffersSaveOnce() {
        val vm = vm()
        vm.setCard(" 0042 ")
        vm.setPassword("1234")
        vm.submit()
        until { vm.ui.value.success }
        val offer = assertNotNull(vm.takeSaveOffer())
        assertEquals("0042", offer.cardNumber)
        assertEquals("1234", offer.pin)
        // Взима се веднъж; ПИН-ът не остава в екрана.
        assertNull(vm.takeSaveOffer())
        assertEquals("", vm.pin.text.toString())
    }

    @Test fun withoutRememberMeNothingIsOffered() {
        val vm = vm()
        vm.setRemember(false)
        vm.setCard("0042")
        vm.setPassword("1234")
        vm.submit()
        until { vm.ui.value.success }
        assertNull(vm.takeSaveOffer())
    }

    @Test fun failedLoginOffersNothing() {
        val vm = vm()
        vm.setCard("0042")
        vm.setPassword("9999")
        vm.submit()
        until { vm.ui.value.error != null }
        assertNull(vm.takeSaveOffer())
        assertTrue(!vm.ui.value.success)
    }

    @Test fun savedLoginFillsAndSubmitsWithoutOfferingToSaveAgain() {
        val vm = vm()
        vm.signInWithSaved(SavedLogin("0042", "1234"))
        until { vm.ui.value.success }
        assertIs<AuthState.SignedIn>(c.authRepository.state.value)
        assertEquals(1, backend.count(login))
        assertTrue(backend.requests.single { it.path == login }.body.clone().readUtf8().contains("\"0042\""))
        assertNull(vm.takeSaveOffer())
    }

    /** Заключването след грешни опити важи и за запазения вход — без заявка. */
    @Test fun savedLoginRespectsTheLock() {
        val vm = vm()
        repeat(5) { c.authRepository.throttle.onFailure() }
        val locked = LoginViewModel(c)
        until { locked.ui.value.lockedUntilMillis > 0 }
        locked.signInWithSaved(SavedLogin("0042", "1234"))
        Thread.sleep(200)
        assertEquals(0, backend.count(login))
        assertTrue(!locked.ui.value.success)
        assertNull(vm.takeSaveOffer())
        // Запазеният ПИН не влиза в полето (иначе „окото“ би го показало по време на заключването).
        assertEquals("", locked.pin.text.toString())
        assertEquals("", locked.card.text.toString())
    }

    /** Опит по време на заключването (напр. „Готово“ от клавиатурата): без заявка и без ПИН в полето. */
    @Test fun blockedAttemptDoesNotKeepThePin() {
        repeat(5) { c.authRepository.throttle.onFailure() }
        val vm = LoginViewModel(c)
        until { vm.ui.value.lockedUntilMillis > 0 }
        vm.setCard("0042")
        vm.setPassword("1234")
        vm.submit()
        assertEquals("", vm.pin.text.toString())
        assertEquals(0, backend.count(login))
    }

    /** Без запазван от приложението вход мениджърът не се пита (без невидима activity при всяко отваряне). */
    @Test fun passwordManagerIsAskedOnlyAfterALoginWasSavedThere() {
        val vm = vm()
        var asked = 0
        runBlocking { vm.askSavedLogin { asked++; SavedLogin("0042", "1234") } }
        assertEquals(0, asked)
        assertEquals(0, backend.count(login))

        vm.setCard("0042")
        vm.setPassword("1234")
        vm.submit()
        until { vm.ui.value.success }
        // Отказ („Не сега“) — не се отбелязва; запазено — отбелязва се.
        runBlocking { vm.offerSave { false } }
        assertFalse(runBlocking { c.settings.passwordManagerUsed() })
        val again = vm()
        again.setCard("0042")
        again.setPassword("1234")
        again.submit()
        until { again.ui.value.success }
        runBlocking { again.offerSave { true } }
        assertTrue(runBlocking { c.settings.passwordManagerUsed() })

        val next = vm()
        runBlocking { next.askSavedLogin { asked++; SavedLogin("0042", "1234") } }
        assertEquals(1, asked)
        until { next.ui.value.success }
        assertEquals(3, backend.count(login))
    }

    /** Мениджър, който не отговаря: при отваряне — до 5 s, при запазване — до минута; после формата/екранът продължава. */
    @Test fun aSilentPasswordManagerDoesNotBlock() = runTest {
        c.settings.setPasswordManagerUsed(true)
        val vm = vm()
        vm.askSavedLogin { awaitCancellation() }
        assertEquals("", vm.card.text.toString())
        assertEquals(0, backend.count(login))

        c.settings.setPasswordManagerUsed(false)
        vm.setCard("0042")
        vm.setPassword("1234")
        vm.submit()
        until { vm.ui.value.success }
        vm.offerSave { awaitCancellation() }
        assertFalse(c.settings.passwordManagerUsed())
    }
}
