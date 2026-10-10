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
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.MembershipStatus
import org.chyavorec.domain.model.ReaderBundle
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.RenewResult
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.repository.PayloadCache
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
 *
 * Възможностите се питат най-много веднъж едновременно и се помнят [CAPS_TTL_MILLIS]
 * (в паметта и, ако е подаден [capsCache], в публичния кеш — те не са лични данни).
 * Неуспешно питане не се помни.
 */
class RemoteInvLibClient(
    private val http: HttpFetcher,
    baseUrl: String,
    private val clock: AppClock,
    private val deviceName: String,
    /** Публичен (нешифрован) кеш за възможностите; `null` = само в паметта. */
    private val capsCache: PayloadCache? = null,
) : AuthenticationService, ReaderService, MembershipService {

    private val api = baseUrl.trimEnd('/')
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    private class CapsEntry(val caps: ServiceCapabilities, val savedAtMillis: Long)

    @Volatile private var cachedCaps: CapsEntry? = null
    private val capsFlight = SingleFlight<Outcome<ServiceCapabilities>>()

    override suspend fun capabilities(): Outcome<ServiceCapabilities> {
        freshCaps(cachedCaps)?.let { return Outcome.Success(it) }
        return capsFlight.run {
            freshCaps(cachedCaps)?.let { return@run Outcome.Success(it) }
            readPersistedCaps()?.let { entry ->
                cachedCaps = entry
                return@run Outcome.Success(entry.caps)
            }
            when (val r = send(get("/v1/capabilities", null), CapabilitiesDto.serializer())) {
                // Неуспехът не се помни — следващото питане опитва отново (AuthRepository → NONE).
                is Outcome.Failure -> r
                is Outcome.Success -> {
                    val caps = r.value.toCapabilities()
                    cachedCaps = CapsEntry(caps, clock.now().toEpochMilli())
                    persistCaps(r.value)
                    Outcome.Success(caps)
                }
            }
        }
    }

    private fun freshCaps(entry: CapsEntry?): ServiceCapabilities? {
        entry ?: return null
        val age = clock.now().toEpochMilli() - entry.savedAtMillis
        return if (age in 0 until CAPS_TTL_MILLIS) entry.caps else null
    }

    private suspend fun readPersistedCaps(): CapsEntry? {
        val cache = capsCache ?: return null
        val payload = runCatching { cache.read(CAPS_CACHE_KEY) }.getOrNull() ?: return null
        val dto = runCatching { json.decodeFromString(CapabilitiesCacheDto.serializer(), payload.text) }.getOrNull() ?: return null
        // Запис за друг адрес на API (сменена настройка) не важи.
        if (dto.api != api) return null
        val entry = CapsEntry(dto.caps.toCapabilities(), payload.savedAtMillis)
        return if (freshCaps(entry) != null) entry else null
    }

    private suspend fun persistCaps(dto: CapabilitiesDto) {
        val cache = capsCache ?: return
        runCatching {
            cache.write(CAPS_CACHE_KEY, json.encodeToString(CapabilitiesCacheDto.serializer(), CapabilitiesCacheDto(api, dto)))
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
        val r = send(post("/v1/auth/refresh", null, body), TokenResponseDto.serializer())
        // 403 тук значи отнет/анулиран refresh токен → същото като 401 (нов вход).
        // Извън /v1/auth/refresh 403 остава Server(403) — „нямаш право“, не изход.
        return r.refreshForbiddenAsUnauthorized().toSession()
    }

    override suspend fun logout(session: AuthSession): Outcome<Unit> =
        sendUnit(post("/v1/auth/logout", session, "{}".toRequestBody(jsonType)))

    override suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit> {
        requireCap(Feature.PASSWORD_RESET) { it.passwordReset }?.let { return Outcome.Failure(it) }
        val body = json.encodeToString(PasswordResetRequestDto.serializer(), PasswordResetRequestDto(cardNumber.trim()))
            .toRequestBody(jsonType)
        return sendUnit(post("/v1/auth/password-reset", null, body))
    }

    /** Профилът; новите мостове слагат в него и `membership` (тогава втора заявка не трябва). */
    override suspend fun profile(session: AuthSession): Outcome<ReaderProfile> {
        requireCap(Feature.PROFILE) { it.profile }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me", session), ReaderDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.toProfile())
        }
    }

    /**
     * Всички читателски данни с една заявка (`GET /v1/me/all`, capability `all`).
     * Членството се слага и в профила (както при `/v1/me` с `membership`).
     */
    override suspend fun meAll(session: AuthSession): Outcome<ReaderBundle> {
        requireCap(Feature.PROFILE) { it.all }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me/all", session), MeAllResponseDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> r.value.let { d ->
                val membership = d.membership?.toMembership()
                Outcome.Success(
                    ReaderBundle(
                        profile = d.profile?.toProfile()?.let { p -> if (p.membership == null) p.copy(membership = membership) else p },
                        membership = membership,
                        loans = d.loans?.loans?.map { it.toLoan() },
                        history = d.history?.toHistory(),
                        messages = d.messages?.toMessages(),
                    ),
                )
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

    /**
     * Заявка за удължаване. Сървърът отговаря с `202` и заемането с
     * `renewPending = true` (самото удължаване става при следващата
     * синхронизация на InvLib); `409 {"error":"pending"|"not_allowed"}` →
     * [AppError.Conflict] със същия код; `404` → [AppError.NotFound]; `429` → [AppError.RateLimited].
     */
    override suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan> {
        requireCap(Feature.RENEW) { it.renew }?.let { return Outcome.Failure(it) }
        val path = "/v1/me/loans/" + java.net.URLEncoder.encode(loanId, "UTF-8") + "/renew"
        return when (val r = send(post(path, session, ByteArray(0).toRequestBody(null)), LoanDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.toLoan())
        }
    }

    override suspend fun history(session: AuthSession): Outcome<List<HistoryItem>> {
        requireCap(Feature.HISTORY) { it.history }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me/history", session), HistoryResponseDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.toHistory())
        }
    }

    /** Лични съобщения (`GET /v1/me/messages`), най-новите първо. Съдържанието не се логва. */
    override suspend fun messages(session: AuthSession): Outcome<List<ReaderMessage>> {
        requireCap(Feature.MESSAGES) { it.messages }?.let { return Outcome.Failure(it) }
        return when (val r = send(get("/v1/me/messages", session), ReaderMessagesResponseDto.serializer())) {
            is Outcome.Failure -> r
            is Outcome.Success -> Outcome.Success(r.value.toMessages())
        }
    }

    /**
     * „Прочетено“ (`POST /v1/me/messages/{id}/read`, празно тяло → 202). Идемпотентно на сървъра;
     * `404` → [AppError.NotFound] (репозиторият го приема като „няма какво да се прати“).
     */
    override suspend fun markMessageRead(session: AuthSession, messageId: String): Outcome<Unit> {
        requireCap(Feature.MESSAGES) { it.messages }?.let { return Outcome.Failure(it) }
        val path = "/v1/me/messages/" + java.net.URLEncoder.encode(messageId, "UTF-8") + "/read"
        return sendUnit(post(path, session, ByteArray(0).toRequestBody(null)))
    }

    /**
     * „Прочетено“ на пакет (`POST /v1/me/messages/read` с `{"ids":[…]}` → 202, capability
     * `messagesBatchRead`). Повече от [MessagesReadRequestDto.MAX_IDS] се пращат на части;
     * при първия неуспех се спира (идемпотентно е — повторният опит праща всичко пак).
     */
    override suspend fun markMessagesRead(session: AuthSession, messageIds: List<String>): Outcome<Unit> {
        requireCap(Feature.MESSAGES) { it.messages && it.messagesBatchRead }?.let { return Outcome.Failure(it) }
        val ids = messageIds.filter { it.isNotBlank() }.distinct()
        for (chunk in ids.chunked(MessagesReadRequestDto.MAX_IDS)) {
            val body = json.encodeToString(MessagesReadRequestDto.serializer(), MessagesReadRequestDto(chunk)).toRequestBody(jsonType)
            val r = sendUnit(post("/v1/me/messages/read", session, body))
            if (r is Outcome.Failure) return r
        }
        return Outcome.Success(Unit)
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
            is Outcome.Success -> Outcome.Success(r.value.toMembership())
        }
    }

    // --- помощни ---

    private fun CapabilitiesDto.toCapabilities() = ServiceCapabilities(
        login = login, profile = profile, loans = loans, membership = membership,
        holds = holds, renew = renew, passwordReset = passwordReset,
        accountDeletion = accountDeletion, push = push, availability = availability,
        history = history, messages = messages, all = all, messagesBatchRead = messagesBatchRead,
    )

    private fun ReaderDto.toProfile() =
        ReaderProfile(readerId, cardNumber, fullName, photoUrl, category, email, registeredOn, membership?.toMembership())

    private fun MembershipDto.toMembership() = Membership(
        memberNumber = memberNumber,
        holderName = holderName,
        since = since,
        validUntil = validUntil,
        status = when (status.lowercase()) {
            "active" -> MembershipStatus.ACTIVE
            "expired" -> MembershipStatus.EXPIRED
            "suspended" -> MembershipStatus.SUSPENDED
            else -> MembershipStatus.UNKNOWN
        },
        barcodePayload = barcode,
    )

    private fun HistoryResponseDto.toHistory() =
        items.map { HistoryItem(it.loanId, it.inv, it.title, it.author, it.dateOut, it.dateIn) }

    /** Без празни/повтарящи се id, най-новите първо. */
    private fun ReaderMessagesResponseDto.toMessages() = items
        .filter { it.id.isNotBlank() }
        .distinctBy { it.id }
        .map { ReaderMessage(it.id, it.title.orEmpty().trim(), it.text.orEmpty(), it.at.orEmpty(), it.read) }
        .sortedByDescending { m -> runCatching { java.time.Instant.parse(m.at) }.getOrDefault(java.time.Instant.EPOCH) }

    private fun LoanDto.toLoan() = Loan(
        loanId, inv, title, author, coverUrl, dateOut, dateDue, renewals, canRenew,
        renewPending = renewPending,
        renewResult = renewResult?.takeIf { it.status.isNotBlank() }?.let { RenewResult(it.status, it.reason, it.at) },
    )

    private fun <T> Outcome<T>.refreshForbiddenAsUnauthorized(): Outcome<T> =
        if (this is Outcome.Failure && error == AppError.Server(403)) Outcome.Failure(AppError.Unauthorized) else this

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

    companion object {
        /** Колко дълго важат запомнените възможности (после — пак от мрежата). */
        const val CAPS_TTL_MILLIS = 24 * 60 * 60 * 1000L
        internal const val CAPS_CACHE_KEY = "invlib:capabilities"
    }
}
