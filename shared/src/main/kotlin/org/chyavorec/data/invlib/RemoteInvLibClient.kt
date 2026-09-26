package org.chyavorec.data.invlib

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
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
 * Клиент за ПРЕДЛОЖЕНИЯ онлайн API на InvLib (docs/API.md).
 *
 * Активира се само когато е зададен INFLIB_API_URL. Използва Bearer токени,
 * никога не пази паролата и я зачиства от паметта веднага след заявката.
 * Всяка функция първо проверява /v1/capabilities — ако сървърът не я
 * поддържа, връща [AppError.NotAvailable], а не симулира отговор.
 */
class RemoteInvLibClient(
    private val http: HttpFetcher,
    baseUrl: String,
    private val clock: AppClock,
    private val deviceName: String,
) : AuthenticationService, ReaderService, MembershipService {

    private val api = baseUrl.trimEnd('/')
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    @Volatile private var cachedCaps: ServiceCapabilities? = null

    override suspend fun capabilities(): Outcome<ServiceCapabilities> {
        cachedCaps?.let { return Outcome.Success(it) }
        return when (val r = send(get("/v1/capabilities", null), CapabilitiesDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> {
                val d = r.value
                val caps = ServiceCapabilities(
                    login = d.login, profile = d.profile, loans = d.loans, membership = d.membership,
                    holds = d.holds, renew = d.renew, passwordReset = d.passwordReset,
                    accountDeletion = d.accountDeletion, push = d.push, availability = d.availability,
                )
                cachedCaps = caps
                Outcome.Success(caps)
            }
        }
    }

    private suspend fun requireCap(feature: Feature, check: (ServiceCapabilities) -> Boolean): AppError? =
        when (val c = capabilities()) {
            is Outcome.Failure -> c.error
            is Outcome.Success -> if (check(c.value)) null else AppError.NotAvailable(feature)
        }

    override suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession> {
        try {
            requireCap(Feature.LOGIN) { it.login }?.let { return Outcome.Failure(it) }
            val body = json.encodeToString(
                LoginRequestDto.serializer(),
                LoginRequestDto(cardNumber.trim(), String(password), deviceName),
            ).toRequestBody(jsonType)
            return send(post("/v1/auth/login", null, body), TokenResponseDto.serializer()).toSession()
        } finally {
            password.fill('\u0000')
        }
    }

    override suspend fun refresh(session: AuthSession): Outcome<AuthSession> {
        val token = session.refreshToken ?: return Outcome.Failure(AppError.Unauthorized)
        val body = json.encodeToString(RefreshRequestDto.serializer(), RefreshRequestDto(token)).toRequestBody(jsonType)
        return send(post("/v1/auth/refresh", null, body), TokenResponseDto.serializer()).toSession()
    }

    override suspend fun logout(session: AuthSession): Outcome<Unit> =
        sendUnit(post("/v1/auth/logout", session, "{}".toRequestBody(jsonType)))

    override suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit> {
        requireCap(Feature.PASSWORD_RESET) { it.passwordReset }?.let { return Outcome.Failure(it) }
        val body = json.encodeToString(PasswordResetRequestDto.serializer(), PasswordResetRequestDto(cardNumber.trim()))
            .toRequestBody(jsonType)
        return sendUnit(post("/v1/auth/password-reset", null, body))
    }

    override suspend fun profile(session: AuthSession): Outcome<ReaderProfile> {
        requireCap(Feature.PROFILE) { it.profile }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me", session), ReaderDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> r.value.let {
                Outcome.Success(ReaderProfile(it.readerId, it.cardNumber, it.fullName, it.photoUrl, it.category, it.email, it.registeredOn))
            }
        }
    }

    override suspend fun loans(session: AuthSession): Outcome<List<Loan>> {
        requireCap(Feature.LOANS) { it.loans }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me/loans", session), LoansResponseDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.loans.map { it.toLoan() })
        }
    }

    override suspend fun placeHold(session: AuthSession, inv: Long): Outcome<Unit> {
        requireCap(Feature.HOLDS) { it.holds }?.let { return Outcome.Failure(it) }
        val body = json.encodeToString(HoldRequestDto.serializer(), HoldRequestDto(inv)).toRequestBody(jsonType)
        return sendUnit(post("/v1/me/holds", session, body))
    }

    override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> {
        requireCap(Feature.RENEW) { it.renew }?.let { return Outcome.Failure(it) }
        val path = "/v1/me/loans/" + java.net.URLEncoder.encode(loanId, "UTF-8") + "/renew"
        return when (val r = send(post(path, session, "{}".toRequestBody(jsonType)), LoanDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.toLoan())
        }
    }

    override suspend fun requestAccountDeletion(session: AuthSession): Outcome<Unit> {
        requireCap(Feature.ACCOUNT_DELETION) { it.accountDeletion }?.let { return Outcome.Failure(it) }
        return sendUnit(Request.Builder().url("$api/v1/me").delete().auth(session).build())
    }

    override suspend fun availability(inv: Long): Outcome<BookStatus> {
        requireCap(Feature.LOANS) { it.availability }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/books/$inv/availability", null), AvailabilityDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(
                when (r.value.status.lowercase()) {
                    "available" -> BookStatus.AVAILABLE
                    "on_loan" -> BookStatus.ON_LOAN
                    else -> BookStatus.UNAVAILABLE
                },
            )
        }
    }

    override suspend fun membership(session: AuthSession): Outcome<Membership> {
        requireCap(Feature.MEMBERSHIP) { it.membership }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me/membership", session), MembershipDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> r.value.let {
                Outcome.Success(
                    Membership(
                        memberNumber = it.memberNumber,
                        holderName = it.holderName,
                        since = it.since,
                        validUntil = it.validUntil,
                        status = when (it.status.lowercase()) {
                            "active" -> MembershipStatus.ACTIVE
                            "expired" -> MembershipStatus.EXPIRED
                            "suspended" -> MembershipStatus.SUSPENDED
                            else -> MembershipStatus.UNKNOWN
                        },
                        barcodePayload = it.barcode,
                    ),
                )
            }
        }
    }

    // --- помощни ---

    private fun LoanDto.toLoan() = Loan(loanId, inv, title, author, coverUrl, dateOut, dateDue, renewals, canRenew)

    private fun Outcome<TokenResponseDto>.toSession(): Outcome<AuthSession> = when (this) {
        is Outcome.Failure -> this
        is Outcome.Success -> Outcome.Success(
            AuthSession(
                accessToken = value.accessToken,
                refreshToken = value.refreshToken,
                expiresAtMillis = clock.now().toEpochMilli() + value.expiresIn.coerceAtLeast(0) * 1000,
                readerId = value.readerId,
            ),
        )
    }

    private fun Request.Builder.auth(session: AuthSession?): Request.Builder =
        apply { if (session != null) header("Authorization", "Bearer " + session.accessToken) }
            .header("Accept", "application/json")

    private fun get(path: String, session: AuthSession?): Request =
        Request.Builder().url(api + path).get().auth(session).build()

    private fun post(path: String, session: AuthSession?, body: RequestBody): Request =
        Request.Builder().url(api + path).post(body).auth(session).build()

    private suspend fun <T> send(request: Request, serializer: KSerializer<T>): Outcome<T> =
        when (val r = http.execute(request)) {
            is Outcome.Failure -> r
            is Outcome.Success -> runCatching { json.decodeFromString(serializer, r.value.text()) }
                .fold({ Outcome.Success(it) }, { Outcome.Failure(AppError.Parse("invlib api")) })
        }

    private suspend fun sendUnit(request: Request): Outcome<Unit> =
        when (val r = http.execute(request)) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(Unit)
        }
}
