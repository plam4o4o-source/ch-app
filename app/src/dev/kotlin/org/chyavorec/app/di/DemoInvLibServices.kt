package org.chyavorec.app.di

import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.MembershipStatus
import org.chyavorec.domain.model.ReaderBundle
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.RenewResult
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
        ServiceCapabilities(
            login = true, profile = true, loans = true, membership = true, holds = true, renew = true, history = true, messages = true,
            all = true, messagesBatchRead = true,
        ),
    )

    override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
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
        val t = today()
        return Outcome.Success(
            listOf(
                Loan("demo-1", null, "Под игото", "Иван Вазов", null, t.minusDays(14).toString(), t.plusDays(7).toString(), 0, true),
                Loan("demo-2", null, "Демо заглавие (наближава срок)", "Демо автор", null, t.minusDays(28).toString(), t.plusDays(2).toString(), 1, true,
                    renewPending = pendingRenew.contains("demo-2")),
                Loan("demo-3", null, "Демо заглавие (просрочено)", "Демо автор", null, t.minusDays(40).toString(), t.minusDays(3).toString(), 2, false,
                    renewResult = RenewResult(RenewResult.STATUS_REJECTED, "Книгата е заявена от друг читател.", clock.now().toString())),
            ),
        )
    }

    /** Заявените удължавания в демо режим — остават „чакащи“ (както при истинския мост до синхронизацията на InvLib). */
    private val pendingRenew = java.util.concurrent.CopyOnWriteArraySet<String>()

    override suspend fun placeHold(session: AuthSession, inv: Long) = Outcome.Success(Unit)
    override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> {
        if (loanId in pendingRenew) return Outcome.Failure(AppError.Conflict(AppError.Conflict.PENDING))
        val loan = (loans(session) as Outcome.Success).value.firstOrNull { it.loanId == loanId }
            ?: return Outcome.Failure(AppError.NotFound)
        if (!loan.canRenew) return Outcome.Failure(AppError.Conflict(AppError.Conflict.NOT_ALLOWED))
        pendingRenew += loanId
        return Outcome.Success(loan.copy(renewPending = true))
    }

    override suspend fun history(session: AuthSession): Outcome<List<HistoryItem>> {
        val t = today()
        return Outcome.Success(
            listOf(
                HistoryItem("demo-h1", 1, "Под игото", "Иван Вазов", t.minusDays(60).toString(), t.minusDays(30).toString()),
                HistoryItem("demo-h2", 2, "Демо заглавие (прочетено)", "Демо автор", t.minusMonths(8).toString(), t.minusMonths(7).toString()),
                HistoryItem("demo-h3", null, "Демо заглавие (миналата година)", "Друг автор", t.minusYears(1).toString(), t.minusYears(1).plusDays(20).toString()),
            ),
        )
    }

    /** Прочетените в демо режим лични съобщения (до рестарт на приложението). */
    private val readMessages = java.util.concurrent.CopyOnWriteArraySet<String>()

    override suspend fun messages(session: AuthSession): Outcome<List<ReaderMessage>> {
        val now = clock.now()
        return Outcome.Success(
            listOf(
                ReaderMessage("demo-m2", "Запазената книга пристигна", "Демо: книгата, която запазихте, ви чака на гишето до петък.",
                    now.minusSeconds(2 * 3600).toString(), read = "demo-m2" in readMessages),
                ReaderMessage("demo-m1", "", "Демо: лично съобщение без заглавие.",
                    now.minusSeconds(5 * 24 * 3600).toString(), read = true),
            ),
        )
    }

    override suspend fun markMessageRead(session: AuthSession, messageId: String): Outcome<Unit> {
        readMessages += messageId
        return Outcome.Success(Unit)
    }

    override suspend fun markMessagesRead(session: AuthSession, messageIds: List<String>): Outcome<Unit> {
        readMessages += messageIds
        return Outcome.Success(Unit)
    }

    /** Като `GET /v1/me/all`: всичко с едно извикване (членството — и в профила). */
    override suspend fun meAll(session: AuthSession): Outcome<ReaderBundle> {
        val membership = membership(session).value
        return Outcome.Success(
            ReaderBundle(
                profile = profile(session).value.copy(membership = membership),
                membership = membership,
                loans = (loans(session) as Outcome.Success).value,
                history = (history(session) as Outcome.Success).value,
                messages = (messages(session) as Outcome.Success).value,
            ),
        )
    }

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
