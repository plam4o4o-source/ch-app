package org.chyavorec.app.messages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.MessagesRepository
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.domain.model.AppMessage

/**
 * Съобщенията от читалището в приложението: кой е „член“ (влязъл в профила
 * си или въвел читателска карта), кои съобщения са прочетени, брой непрочетени.
 */
class MessageCenter(
    private val repository: MessagesRepository,
    private val auth: AuthRepository,
    private val selfCard: SelfCardRepository,
    private val settings: SettingsStore,
) {
    private val _messages = MutableStateFlow<List<AppMessage>>(emptyList())
    val messages: StateFlow<List<AppMessage>> = _messages.asStateFlow()

    val unreadCount: Flow<Int> = combine(_messages, settings.readMessageIds) { list, read -> list.count { it.id !in read } }
    val readIds: Flow<Set<String>> = settings.readMessageIds

    suspend fun isMember(): Boolean {
        if (auth.state.value is AuthState.Unknown) auth.restore()
        if (auth.state.value is AuthState.SignedIn) return true
        if (selfCard.card.value == null) selfCard.load()
        return selfCard.card.value != null
    }

    suspend fun refresh(force: Boolean): Outcome<Synced<List<AppMessage>>> {
        val member = isMember()
        if (_messages.value.isEmpty()) _messages.value = repository.cached(member)
        val r = repository.messages(member, force)
        if (r is Outcome.Success) _messages.value = r.value.data
        return r
    }

    suspend fun markRead(ids: Collection<String>) = settings.addReadMessages(ids)
}
