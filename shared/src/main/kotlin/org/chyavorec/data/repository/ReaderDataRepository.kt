package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.builtins.ListSerializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.core.getOrNull
import org.chyavorec.data.invlib.SingleFlight
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderBundle
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService

/**
 * Общото място за читателските данни (профил, членство, заемания, история, лични
 * съобщения): един шифрован кеш на вид данни и една инстанция за цялото приложение.
 *
 * - Ако сървърът има capability `all`, една заявка `GET /v1/me/all` пълни всички кешове
 *   наведнъж; едновременните извиквания споделят една заявка. Без нея (по-стар мост)
 *   или ако отговорът не става — поотделни заявки, както преди.
 * - Свежест (без мрежа, освен при изрично опресняване): профил [PROFILE_FRESH_MILLIS],
 *   членство [MEMBERSHIP_FRESH_MILLIS], заемания [LOANS_FRESH_MILLIS],
 *   история [HISTORY_FRESH_MILLIS], съобщения [MESSAGES_FRESH_MILLIS].
 * - Профилът се държи и в [profile] (на ниво приложение), за да го споделят всички
 *   екрани, без всеки да го тегли сам. Изчиства се при изход/нов вход.
 */
class ReaderDataRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    private val clock: AppClock,
    /** Ако е зададен, членството се добавя към профила, когато `/v1/me` не го връща. */
    private val membershipService: MembershipService? = null,
    /**
     * Вика се след ВСЕКИ запис на заеманията в кеша — от екраните, от фоновата задача,
     * от `/v1/me/all` и след удължаване (напр. за локалните напомняния за срок).
     */
    onLoansStored: ((List<Loan>) -> Unit)? = null,
) {
    private class Part<T>(
        val res: CachedResource<T>,
        val freshMillis: Long,
        val pick: (ReaderBundle) -> T?,
        val single: suspend (AuthSession) -> Outcome<T>,
    ) {
        val flight = SingleFlight<Outcome<Synced<T>>>()
    }

    /** Ключовете са в пространството на текущия читател ([ReaderScopedCache]). */
    private val cache: PayloadCache = auth.scopedCache(secureCache)

    private val profilePart = Part<ReaderProfile>(
        CachedResource(cache, KEY_PROFILE, ReaderProfile.serializer(), clock, PROFILE_FRESH_MILLIS),
        PROFILE_FRESH_MILLIS, { it.profile },
    ) { session ->
        when (val p = service.profile(session)) {
            is Outcome.Failure -> p
            is Outcome.Success -> {
                val ms = membershipService
                if (p.value.membership != null || ms == null) {
                    p
                } else {
                    // Неуспех при членството не проваля профила — просто без него.
                    Outcome.Success(p.value.copy(membership = ms.membership(session).getOrNull()))
                }
            }
        }
    }

    private val membershipPart = Part<Membership>(
        CachedResource(cache, KEY_MEMBERSHIP, Membership.serializer(), clock, MEMBERSHIP_FRESH_MILLIS),
        MEMBERSHIP_FRESH_MILLIS, { it.membership },
    ) { session -> membershipService?.membership(session) ?: Outcome.Failure(AppError.NotAvailable(Feature.MEMBERSHIP)) }

    private val loansPart = Part<List<Loan>>(
        CachedResource(cache, KEY_LOANS, ListSerializer(Loan.serializer()), clock, LOANS_FRESH_MILLIS, onStored = onLoansStored),
        LOANS_FRESH_MILLIS, { it.loans },
    ) { session -> service.loans(session) }

    private val _historyVersion = MutableStateFlow(0L)
    /**
     * Сменя се при всеки запис на историята в кеша (от „Какво съм чел“, `/v1/me/all`) и при
     * изход/нов вход — за да се преизчислят „За теб“ и „Моето четене“ без собствени заявки.
     */
    val historyVersion: StateFlow<Long> = _historyVersion.asStateFlow()

    private val historyPart = Part<List<HistoryItem>>(
        CachedResource(
            cache, KEY_HISTORY, ListSerializer(HistoryItem.serializer()), clock, HISTORY_FRESH_MILLIS,
            onStored = { _historyVersion.update { it + 1 } },
        ),
        HISTORY_FRESH_MILLIS, { it.history },
    ) { session -> service.history(session) }

    private val messagesPart = Part<List<ReaderMessage>>(
        CachedResource(cache, KEY_MESSAGES, ListSerializer(ReaderMessage.serializer()), clock, MESSAGES_FRESH_MILLIS),
        MESSAGES_FRESH_MILLIS, { it.messages },
    ) { session -> service.messages(session) }

    private val allFlight = SingleFlight<Outcome<ReaderBundle>>()

    private val _profile = MutableStateFlow<Synced<ReaderProfile>?>(null)
    /** Последно известният профил на влезлия читател (`null` = няма/още не е зареден). */
    val profile: StateFlow<Synced<ReaderProfile>?> = _profile.asStateFlow()

    /** Сменя се при изход/нов вход: закъснели отговори за предишния читател не се показват. */
    @Volatile private var generation = 0L

    init {
        auth.addReaderResetListener {
            generation++
            _profile.value = null
            _historyVersion.update { it + 1 }
        }
    }

    // --- профил ---

    suspend fun profile(force: Boolean): Outcome<Synced<ReaderProfile>> {
        // Пресен профил в паметта — без четене от диска (всеки екран го иска при отваряне).
        if (!force) {
            _profile.value?.takeIf { it.refreshError == null && isFresh(it, PROFILE_FRESH_MILLIS) }?.let {
                return Outcome.Success(it.copy(fromCache = false))
            }
        }
        val gen = generation
        val r = load(profilePart, force)
        if (r is Outcome.Success) publishProfile(gen, r.value)
        return r
    }

    /** Кешираният профил (може и остарял) — показва се веднага, докато тече опресняването. */
    suspend fun cachedProfile(): Synced<ReaderProfile>? {
        _profile.value?.let { return it }
        val gen = generation
        val c = profilePart.res.cached() ?: return null
        if (gen != generation) return null // междувременно изход/нов вход
        _profile.compareAndSet(null, c)
        return _profile.value ?: c
    }

    private fun publishProfile(gen: Long, value: Synced<ReaderProfile>) {
        if (gen == generation) _profile.value = value
    }

    // --- останалите ---

    suspend fun membership(force: Boolean): Outcome<Synced<Membership>> = load(membershipPart, force)
    suspend fun cachedMembership(): Synced<Membership>? = membershipPart.res.cached()

    suspend fun loans(force: Boolean): Outcome<Synced<List<Loan>>> = load(loansPart, force)
    suspend fun cachedLoans(): Synced<List<Loan>>? = loansPart.res.cached()
    internal suspend fun updateCachedLoans(transform: (List<Loan>) -> List<Loan>) = loansPart.res.updateCached(transform)

    suspend fun history(force: Boolean): Outcome<Synced<List<HistoryItem>>> = load(historyPart, force)
    suspend fun cachedHistory(): Synced<List<HistoryItem>>? = historyPart.res.cached()

    suspend fun messages(force: Boolean): Outcome<Synced<List<ReaderMessage>>> = load(messagesPart, force)
    suspend fun cachedMessages(): Synced<List<ReaderMessage>>? = messagesPart.res.cached()
    internal suspend fun updateCachedMessages(transform: (List<ReaderMessage>) -> List<ReaderMessage>) =
        messagesPart.res.updateCached(transform)

    // --- общото ---

    /** Без изрично опресняване едновременните извиквания за едни и същи данни споделят една заявка. */
    private suspend fun <T> load(part: Part<T>, force: Boolean): Outcome<Synced<T>> =
        if (force) loadNow(part, force = true) else part.flight.run { loadNow(part, force = false) }

    private suspend fun <T> loadNow(part: Part<T>, force: Boolean): Outcome<Synced<T>> {
        if (!force) {
            part.res.cached()?.let { c -> if (isFresh(c, part.freshMillis)) return Outcome.Success(c.copy(fromCache = false)) }
        }
        // Без сесия — без заявки (и без питане за възможностите); при грешка — кешът, както винаги.
        val sessionError = (auth.validSession() as? Outcome.Failure)?.error
        if (sessionError != null) return part.res.load(force = true) { Outcome.Failure(sessionError) }
        if (!auth.capabilities().all) return part.res.load(force = true) { auth.withSession(part.single) }
        return when (val b = fetchAll()) {
            is Outcome.Success -> {
                val value = part.pick(b.value)
                if (value != null) {
                    Outcome.Success(Synced(value, clock.now(), fromCache = false))
                } else {
                    // Сървърът не е върнал тази част — поотделно (ако изобщо се поддържа).
                    part.res.load(force = true) { auth.withSession(part.single) }
                }
            }
            is Outcome.Failure -> {
                val error = b.error
                if (error.allowsSingleFallback()) part.res.load(force = true) { auth.withSession(part.single) }
                else part.res.load(force = true) { Outcome.Failure(error) }
            }
        }
    }

    /**
     * `GET /v1/me/all` (една заявка за всички едновременни извиквания) и запис на всяка
     * върната част в нейния кеш. Профилът се показва веднага в [profile].
     */
    private suspend fun fetchAll(): Outcome<ReaderBundle> = allFlight.run {
        val gen = generation
        val r = auth.withSession { service.meAll(it) }
        if (r is Outcome.Success && gen == generation) store(r.value, gen)
        r
    }

    private suspend fun store(b: ReaderBundle, gen: Long) {
        b.profile?.let { p ->
            write(profilePart.res, p)
            publishProfile(gen, Synced(p, clock.now()))
        }
        b.membership?.let { write(membershipPart.res, it) }
        b.loans?.let { write(loansPart.res, it) }
        b.history?.let { write(historyPart.res, it) }
        b.messages?.let { write(messagesPart.res, it) }
    }

    /** Запис в кеша чрез обичайния път на [CachedResource] (със сегашния момент като време на синхронизация). */
    private suspend fun <T> write(res: CachedResource<T>, value: T) {
        res.load(force = true) { Outcome.Success(value) }
    }

    /** Същото правило като в [CachedResource]: по-пресни от [freshMillis] — без мрежа. */
    private fun isFresh(value: Synced<*>, freshMillis: Long): Boolean =
        clock.now().toEpochMilli() - value.syncedAt.toEpochMilli() in 0 until freshMillis

    /** Грешки, при които комбинираната заявка не става, но поотделните може да станат. */
    private fun AppError.allowsSingleFallback(): Boolean =
        this is AppError.Parse || this == AppError.NotFound || this is AppError.NotAvailable ||
            (this is AppError.Server && httpCode in setOf(404, 405, 501))

    companion object {
        const val PROFILE_FRESH_MILLIS = 12 * 60 * 60_000L
        const val MEMBERSHIP_FRESH_MILLIS = 6 * 60 * 60_000L
        const val LOANS_FRESH_MILLIS = 3 * 60_000L
        const val HISTORY_FRESH_MILLIS = 10 * 60_000L
        const val MESSAGES_FRESH_MILLIS = 5 * 60_000L

        internal const val KEY_PROFILE = "reader:profile"
        internal const val KEY_MEMBERSHIP = "reader:membership"
        internal const val KEY_LOANS = "reader:loans"
        internal const val KEY_HISTORY = "reader:history"
        internal const val KEY_MESSAGES = "reader:messages"
    }
}
