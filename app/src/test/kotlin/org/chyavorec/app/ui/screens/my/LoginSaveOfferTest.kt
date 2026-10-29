package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
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
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * [LoginViewModel] и мениджърът на пароли: предложение за запазване — само след успешен вход
 * със „Запомни ме“ и въведени на ръка карта и ПИН; запазеният вход минава през същия вход.
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
        assertEquals("", vm.ui.value.password)
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
    }
}
