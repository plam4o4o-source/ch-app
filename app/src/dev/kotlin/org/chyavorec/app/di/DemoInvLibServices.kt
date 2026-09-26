package org.chyavorec.app.di

import kotlinx.coroutines.delay
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.MembershipStatus
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService

/**
 * ⚠ ДЕМО ДАННИ — САМО ЗА РАЗРАБОТКА (dev flavor, USE_MOCK_DATA=true).
 *
 * Тези данни са измислени и служат единствено за разработка на UI и тестове,
 * докато InvLib няма онлайн API. Класът съществува само в src/dev и не може да
 * попадне в production build. Всеки екран, който ги показва, има лента „ДЕМО ДАННИ“.
 *
 * Вход: карта DEMO-0001, парола demo.
 */
class DemoInvLibServices(private val clock: AppClock) : AuthenticationService, ReaderService, MembershipService {

    private fun today() = clock.today()

    override suspend fun capabilities() = Outcome.Success(
        ServiceCapabilities(login = true, profile = true, loans = true, membership = true, holds = true, renew = true),
    )

    override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
        delay(400)
        val ok = cardNumber.trim().equals("DEMO-0001", ignoreCase = true) && String(password) == "demo"
        password.fill('\u0000')
        return if (ok) Outcome.Success(AuthSession("demo-token", "demo-refresh", clock.now().toEpochMilli() + 3_600_000, "demo-reader"))
        else Outcome.Failure(AppError.Unauthorized)
    }

    override suspend fun refresh(session: AuthSession) =
        Outcome.Success(session.copy(expiresAtMillis = clock.now().toEpochMilli() + 3_600_000))

    override suspend fun logout(session: AuthSession) = Outcome.Success(Unit)
    override suspend fun requestPasswordReset(cardNumber: String) = Outcome.Success(Unit)

    override suspend fun profile(session: AuthSession) = Outcome.Success(
        ReaderProfile(
            readerId = "demo-reader",
            cardNumber = "DEMO-0001",
            fullName = "Демо Читател",
            category = "възрастен",
            registeredOn = today().minusYears(2).toString(),
        ),
    )

    override suspend fun loans(session: AuthSession): Outcome<List<Loan>> {
        delay(300)
        val t = today()
        return Outcome.Success(
            listOf(
                Loan("demo-1", null, "Под игото", "Иван Вазов", null, t.minusDays(14).toString(), t.plusDays(7).toString(), 0, true),
                Loan("demo-2", null, "Демо заглавие (наближава срок)", "Демо автор", null, t.minusDays(28).toString(), t.plusDays(2).toString(), 1, true),
                Loan("demo-3", null, "Демо заглавие (просрочено)", "Демо автор", null, t.minusDays(40).toString(), t.minusDays(3).toString(), 2, false),
            ),
        )
    }

    override suspend fun placeHold(session: AuthSession, inv: Long) = Outcome.Success(Unit)
    override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> =
        Outcome.Failure(AppError.Server(409))
    override suspend fun requestAccountDeletion(session: AuthSession) = Outcome.Success(Unit)
    override suspend fun availability(inv: Long): Outcome<BookStatus> = Outcome.Failure(AppError.NotFound)

    override suspend fun membership(session: AuthSession) = Outcome.Success(
        Membership(
            memberNumber = "DEMO-0001",
            holderName = "Демо Читател",
            since = today().minusYears(2).toString(),
            validUntil = today().withDayOfYear(1).plusYears(1).minusDays(1).toString(),
            status = MembershipStatus.ACTIVE,
            barcodePayload = "DEMO-0001",
        ),
    )
}
