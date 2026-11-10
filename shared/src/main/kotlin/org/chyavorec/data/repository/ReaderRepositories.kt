package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.repository.SelfCardStore
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService
import java.time.Instant

/**
 * Профилът на читателя. [data] е общото хранилище — в приложението една инстанция
 * за всички читателски репозиторита (по подразбиране — собствено, за тестове).
 */
class ProfileRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    /** Ако е зададен, членството се добавя към профила (когато /v1/me не го връща). */
    membershipService: MembershipService? = null,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock, membershipService),
) {
    /** Споделеният профил (на ниво приложение) — всички екрани го четат оттук. */
    val state: StateFlow<Synced<ReaderProfile>?> get() = data.profile

    /** [force] = false → без мрежа, ако профилът е по-пресен от 12 часа. */
    suspend fun profile(force: Boolean = true): Outcome<Synced<ReaderProfile>> = data.profile(force)

    /** Кешираният профил (без мрежа) — веднага при отваряне на екран. */
    suspend fun cachedProfile(): Synced<ReaderProfile>? = data.cachedProfile()

    suspend fun requestAccountDeletion(): Outcome<Unit> = auth.withSession { service.requestAccountDeletion(it) }
}

/** Заемания („Моите книги“), запазване и подновяване. */
class LibraryRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock),
) {
    suspend fun cachedLoans(): Synced<List<Loan>>? = data.cachedLoans()

    /** [force] = false → без мрежа, ако списъкът е по-пресен от 3 минути. */
    suspend fun loans(force: Boolean = true): Outcome<Synced<List<Loan>>> = data.loans(force)

    /**
     * Заявка за удължаване. При успех (202) сървърът връща заемането с
     * `renewPending = true`; кешираният списък се обновява веднага (без нова заявка
     * за целия списък), за да не „изчезне“ чакащото състояние при следващо показване.
     */
    suspend fun renew(loanId: String): Outcome<Loan> {
        val r = auth.withSession { service.renew(it, loanId) }
        if (r is Outcome.Success) data.updateCachedLoans { list -> list.map { if (it.loanId == loanId) r.value else it } }
        if (r is Outcome.Failure) noteUnexpected("loans.renew", r.error)
        return r
    }

    /** Изпратените от телефона заявки (инв. № → кога) — в кеша на читателя, изтриват се при изход. */
    private val holdsCache: PayloadCache = auth.scopedCache(secureCache)
    private val holdsClock = clock
    private val holdsLock = Mutex()

    /** Заявка за книга; при успех се помни ([heldAt]) — сървърът не връща списък със заявките. */
    suspend fun placeHold(inv: Long): Outcome<Unit> {
        val r = auth.withSession { service.placeHold(it, inv) }
        if (r is Outcome.Success) rememberHold(inv)
        return r
    }

    /** Кога книгата е заявена от този телефон (в последните [HOLD_KEEP_MILLIS]); `null` — не е. */
    suspend fun heldAt(inv: Long): Instant? = readHolds()[inv]?.let(Instant::ofEpochMilli)

    private suspend fun readHolds(): Map<Long, Long> {
        val text = holdsCache.read(KEY_HOLDS)?.text ?: return emptyMap()
        val now = holdsClock.now().toEpochMilli()
        val all = runCatching { cacheJson.decodeFromString(holdsSerializer, text) }.getOrNull() ?: return emptyMap()
        return all.filterValues { now - it in 0..HOLD_KEEP_MILLIS }
    }

    private suspend fun rememberHold(inv: Long) = holdsLock.withLock {
        val updated = readHolds() + (inv to holdsClock.now().toEpochMilli())
        holdsCache.write(KEY_HOLDS, cacheJson.encodeToString(holdsSerializer, updated))
    }

    /** История на четенето — шифрован кеш (10 минути без мрежа), изтрива се при изход. */
    suspend fun history(force: Boolean = true): Outcome<Synced<List<HistoryItem>>> = data.history(force)

    suspend fun cachedHistory(): Synced<List<HistoryItem>>? = data.cachedHistory()

    /** Сменя се при всяка промяна на кешираната история ([ReaderDataRepository.historyVersion]). */
    val historyVersion: StateFlow<Long> get() = data.historyVersion

    companion object {
        /** Колко дълго книгата се показва като „Заявена“ (библиотеката я пази няколко дни). */
        const val HOLD_KEEP_MILLIS = 7 * 24 * 60 * 60_000L
        internal const val KEY_HOLDS = "reader:holds"
        private val holdsSerializer = MapSerializer(Long.serializer(), Long.serializer())
    }
}

/**
 * Лични съобщения от библиотеката до читателя. Шифрован кеш ([secureCache] = читателският),
 * който се изтрива при изход заедно със заеманията и историята. Съдържанието не се логва.
 */
class ReaderMessagesRepository(
    private val service: ReaderService,
    private val auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(service, auth, secureCache, clock),
) {
    suspend fun cached(): Synced<List<ReaderMessage>>? = data.cachedMessages()

    /** Кратък кеш: без мрежа, ако е по-пресен от 5 минути (освен при изрично опресняване). */
    suspend fun messages(force: Boolean = true): Outcome<Synced<List<ReaderMessage>>> = data.messages(force)

    /** Дали сървърът приема „прочетено“ на пакет ([markRead] с няколко id). */
    suspend fun supportsBatchRead(): Boolean = auth.capabilities().messagesBatchRead

    /**
     * Праща „прочетено“. `404` (няма такова съобщение при читателя) се приема за успех —
     * няма смисъл от повторни опити. При успех кешираният списък се обновява веднага.
     */
    suspend fun markRead(messageId: String): Outcome<Unit> {
        val r = auth.withSession { service.markMessageRead(it, messageId) }
        if (r is Outcome.Failure && r.error !is AppError.NotFound) return r
        data.updateCachedMessages { list -> list.map { if (it.id == messageId) it.copy(read = true) else it } }
        return Outcome.Success(Unit)
    }

    /**
     * „Прочетено“ за няколко съобщения с една заявка (само при [supportsBatchRead]).
     * Както при единичното: `404` се приема за успех; при успех кешът се обновява.
     */
    suspend fun markRead(messageIds: Collection<String>): Outcome<Unit> {
        val ids = messageIds.filter { it.isNotBlank() }.distinct()
        if (ids.isEmpty()) return Outcome.Success(Unit)
        val r = auth.withSession { service.markMessagesRead(it, ids) }
        if (r is Outcome.Failure && r.error !is AppError.NotFound) return r
        val set = ids.toSet()
        data.updateCachedMessages { list -> list.map { if (it.id in set) it.copy(read = true) else it } }
        return Outcome.Success(Unit)
    }
}

class MembershipRepository(
    service: MembershipService,
    auth: AuthRepository,
    secureCache: PayloadCache,
    clock: AppClock,
    private val data: ReaderDataRepository = ReaderDataRepository(
        service as? ReaderService ?: UnavailableInvLibServices(), auth, secureCache, clock, service,
    ),
) {
    /** [force] = false → без мрежа, ако членството е по-пресно от 6 часа. */
    suspend fun membership(force: Boolean = true): Outcome<Synced<Membership>> = data.membership(force)

    suspend fun cached(): Synced<Membership>? = data.cachedMembership()
}

/** Ръчно въведената карта — независима от онлайн API. */
class SelfCardRepository(private val store: SelfCardStore) {
    private val _card = MutableStateFlow<SelfDeclaredCard?>(null)
    val card: StateFlow<SelfDeclaredCard?> = _card.asStateFlow()

    suspend fun load() { _card.value = store.load() }

    /** Проверява формата: Code 39 допуска A–Z, 0–9 и „-. $/+%“. */
    fun validate(number: String): Boolean {
        val n = number.trim().uppercase()
        return n.length in 1..32 && n.all { it in CODE39_CHARS }
    }

    suspend fun save(number: String, name: String): Boolean {
        val n = number.trim().uppercase()
        if (!validate(n)) return false
        val card = SelfDeclaredCard(n, name.trim().take(80))
        store.save(card)
        _card.value = card
        return true
    }

    suspend fun clear() { store.clear(); _card.value = null }

    companion object {
        const val CODE39_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. \$/+%"
    }
}
