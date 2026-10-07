package org.chyavorec.data.invlib

import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService

/**
 * Реализацията по подразбиране в production, докато InvLib няма онлайн API.
 *
 * InvLib е офлайн настолна програма (Electron + SQLite, Windows) — читателските
 * данни живеят само в локалната база на библиотеката и не се публикуват навън
 * (в `katalog.json` нарочно няма лични данни). Затова тук НЕ се симулира нищо:
 * всяка функция връща [AppError.NotAvailable], а UI обяснява защо.
 *
 * TODO(invlib-api): заменя се от [RemoteInvLibClient], щом бъде настроен
 *  INFLIB_API_URL и сървърът реализира договора в docs/API.md.
 */
class UnavailableInvLibServices : AuthenticationService, ReaderService, MembershipService {
    private fun <T> na(feature: Feature): Outcome<T> = Outcome.Failure(AppError.NotAvailable(feature))

    override suspend fun capabilities(): Outcome<ServiceCapabilities> = Outcome.Success(ServiceCapabilities.NONE)
    override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
        password.fill('\u0000')
        return na(Feature.LOGIN)
    }
    override suspend fun refresh(session: AuthSession): Outcome<AuthSession> = na(Feature.LOGIN)
    override suspend fun logout(session: AuthSession): Outcome<Unit> = Outcome.Success(Unit)
    override suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit> = na(Feature.PASSWORD_RESET)
    override suspend fun profile(session: AuthSession): Outcome<ReaderProfile> = na(Feature.PROFILE)
    override suspend fun loans(session: AuthSession): Outcome<List<Loan>> = na(Feature.LOANS)
    override suspend fun placeHold(session: AuthSession, inv: Long): Outcome<Unit> = na(Feature.HOLDS)
    override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> = na(Feature.RENEW)
    override suspend fun requestAccountDeletion(session: AuthSession): Outcome<Unit> = na(Feature.ACCOUNT_DELETION)
    override suspend fun availability(inv: Long): Outcome<BookStatus> = Outcome.Failure(AppError.NotAvailable(Feature.LOANS))
    override suspend fun history(session: AuthSession): Outcome<List<HistoryItem>> = na(Feature.HISTORY)
    override suspend fun membership(session: AuthSession): Outcome<Membership> = na(Feature.MEMBERSHIP)
}
