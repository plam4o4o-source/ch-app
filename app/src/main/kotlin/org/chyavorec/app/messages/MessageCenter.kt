package org.chyavorec.app.messages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.MessagesRepository
import org.chyavorec.data.repository.ReaderMessagesRepository
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.Inbox
import org.chyavorec.domain.model.ReaderMessage
import org.chyavorec.domain.model.ServiceCapabilities
import java.time.Instant

/**
 * Съобщенията от читалището в приложението: кой е „член“ (влязъл в профила
 * си или въвел читателска карта), кои съобщения са прочетени, брой непрочетени.
 *
 * Освен общите съобщения от сайта, при влязъл читател и сървър с възможност
 * `messages` тук идват и личните съобщения от библиотеката (id с префикс „p:“).
 * Без вход или без тази възможност всичко е както преди — само съобщенията от сайта.
 * Съдържанието на личните съобщения не се логва никъде.
 */
class MessageCenter(
    private val repository: MessagesRepository,
    private val auth: AuthRepository,
    private val selfCard: SelfCardRepository,
    private val settings: SettingsStore,
    private val personalRepository: ReaderMessagesRepository,
    private val clock: AppClock,
) {
    private val _site = MutableStateFlow<List<AppMessage>>(emptyList())
    private val _personal = MutableStateFlow<List<ReaderMessage>>(emptyList())
    private val _messages = MutableStateFlow<List<AppMessage>>(emptyList())
    val messages: StateFlow<List<AppMessage>> = _messages.asStateFlow()

    /** Последното опресняване на личните съобщения е минало (иначе не се пращат известия). */
    @Volatile private var personalSynced = false
    private val flushLock = Mutex()
    private val notifyLock = Mutex()

    val unreadCount: Flow<Int> = combine(_messages, settings.readMessageIds, auth.state) { list, read, state ->
        val signedIn = state is AuthState.SignedIn
        list.count { it.id !in read && (signedIn || !it.personal) }
    }
    val readIds: Flow<Set<String>> = settings.readMessageIds

    suspend fun isMember(): Boolean {
        if (auth.state.value is AuthState.Unknown) auth.restore()
        if (auth.state.value is AuthState.SignedIn) return true
        if (selfCard.card.value == null) selfCard.load()
        return selfCard.card.value != null
    }

    suspend fun refresh(force: Boolean): Outcome<Synced<List<AppMessage>>> {
        val member = isMember()
        val signedIn = auth.state.value is AuthState.SignedIn
        if (!signedIn) clearPersonal()
        if (_site.value.isEmpty()) _site.value = repository.cached(member)
        if (signedIn && _personal.value.isEmpty()) _personal.value = personalRepository.cached()?.data.orEmpty()
        publish()
        val r = repository.messages(member, force)
        if (r is Outcome.Success) _site.value = r.value.data
        if (signedIn) refreshPersonal(force)
        publish()
        val personal = _personal.value
        return when (r) {
            is Outcome.Success -> Outcome.Success(r.value.map { Inbox.merge(it, personal) })
            // Сайтът не отговаря и няма кеш, но има лични съобщения — показват се те (с причината).
            is Outcome.Failure ->
                if (personal.isEmpty()) r
                else Outcome.Success(Synced(Inbox.merge(emptyList(), personal), clock.now(), fromCache = true, refreshError = r.error))
        }
    }

    /** Отбелязва показаните съобщения като прочетени; личните — и на сървъра (с повторен опит при неуспех). */
    suspend fun markRead(ids: Collection<String>) {
        settings.addReadMessages(ids)
        val unreadOnServer = _personal.value.filter { !it.read }.map { it.id }.toSet()
        val toSend = ids.mapNotNull { Inbox.serverId(it) }.filter { it in unreadOnServer }
        if (toSend.isNotEmpty()) settings.addPendingPersonalReads(toSend)
        flushPendingReads()
    }

    /**
     * Нови непрочетени лични съобщения за известие — всяко най-много веднъж.
     * Първия път след вход само запомня наличните (без „порой“ от стари известия),
     * както при съобщенията от сайта. Известие само за съобщения след [cutoff].
     */
    suspend fun takePersonalToNotify(cutoff: Instant, max: Int): List<ReaderMessage> {
        if (auth.state.value !is AuthState.SignedIn || !personalSynced) return emptyList()
        return notifyLock.withLock {
            val list = _personal.value
            val notified = settings.notifiedMessageIds()
            val read = settings.readMessageIdsNow()
            val firstRun = !settings.personalMessagesInitialized()
            val fresh: List<ReaderMessage> = if (firstRun) {
                emptyList<ReaderMessage>()
            } else {
                list.filter { m ->
                    val id = Inbox.personalId(m.id)
                    !m.read && id !in notified && id !in read && sentAt(m).isAfter(cutoff)
                }.take(max)
            }
            if (list.isNotEmpty()) settings.setNotifiedMessages(list.map { Inbox.personalId(it.id) }.toSet())
            settings.setPersonalMessagesInitialized()
            fresh
        }
    }

    /** При изход: личните съобщения изчезват веднага от паметта (кешът и настройките се чистят отделно). */
    fun clearPersonal() {
        personalSynced = false
        _personal.value = emptyList()
        publish()
    }

    private suspend fun refreshPersonal(force: Boolean) {
        // Без възможността `messages` (по-стар мост) — без заявки и без нищо лично, както преди.
        // NONE = и „не е разчетено“ (без мрежа): тогава последно наличните остават на екрана.
        val caps = auth.capabilities()
        if (!caps.messages) {
            personalSynced = false
            if (caps != ServiceCapabilities.NONE) _personal.value = emptyList()
            return
        }
        when (val p = personalRepository.messages(force)) {
            is Outcome.Success -> {
                _personal.value = p.value.data
                personalSynced = true
                // Прочетените на сървъра (напр. от друго устройство) не се броят за непрочетени.
                val read = settings.readMessageIdsNow()
                val serverRead = p.value.data.filter { it.read }.map { Inbox.personalId(it.id) }.filter { it !in read }
                if (serverRead.isNotEmpty()) settings.addReadMessages(serverRead)
            }
            is Outcome.Failure -> {
                personalSynced = false
                // Без вход или без възможността — нищо лично (както преди).
                if (p.error is AppError.NotAvailable || p.error is AppError.Unauthorized) _personal.value = emptyList()
            }
        }
        flushPendingReads()
    }

    /** Праща чакащите „прочетено“. Неуспелите остават за следващото опресняване. */
    private suspend fun flushPendingReads() {
        if (auth.state.value !is AuthState.SignedIn) return
        flushLock.withLock {
            val pending = settings.pendingPersonalReads()
            if (pending.isNotEmpty()) {
                val alreadyRead = _personal.value.filter { it.read }.map { it.id }.toSet()
                val done = pending.filter { it in alreadyRead }.toMutableSet()
                val toSend = pending.filter { it !in alreadyRead }
                if (toSend.size > 1 && personalRepository.supportsBatchRead()) {
                    // Сървър с „прочетено“ на пакет — една заявка за всички.
                    when (val r = personalRepository.markRead(toSend)) {
                        is Outcome.Success -> {
                            done += toSend
                        }
                        is Outcome.Failure -> {
                            // По-стар мост без съобщения — няма къде да се праща. Иначе (без мрежа/сесия,
                            // твърде много заявки) — опит при следващото опресняване.
                            if (r.error is AppError.NotAvailable) done += pending
                        }
                    }
                } else {
                    // Едно по едно, както преди.
                    for (id in toSend) {
                        val r = personalRepository.markRead(id)
                        if (r is Outcome.Success) {
                            done += id
                        } else if (r is Outcome.Failure && r.error is AppError.NotAvailable) {
                            // По-стар мост без съобщения — няма къде да се праща.
                            done += pending
                            break
                        } else {
                            // Без мрежа/сесия или твърде много заявки — опит при следващото опресняване.
                            break
                        }
                    }
                }
                if (done.isNotEmpty()) {
                    settings.removePendingPersonalReads(done)
                    _personal.update { list -> list.map { if (it.id in done) it.copy(read = true) else it } }
                }
            }
        }
    }

    private fun publish() {
        _messages.value = Inbox.merge(_site.value, _personal.value)
    }

    private fun sentAt(m: ReaderMessage): Instant = runCatching { Instant.parse(m.at) }.getOrDefault(Instant.EPOCH)
}
