package org.chyavorec.app.messages

import android.content.Context
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.notifications.PeriodicSyncWorker
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.core.Outcome
import org.chyavorec.data.site.AppMessagesParser
import org.chyavorec.domain.model.Inbox
import org.chyavorec.domain.model.MessagePriority

/**
 * Известия за нови съобщения от читалището — общите от сайта и, при влязъл
 * читател, личните от библиотеката. Общите (важни и обикновени) се проверяват на
 * ~30 мин. ([MessagePollWorker]); личните — от общата фонова задача
 * ([PeriodicSyncWorker]) и при отваряне на приложението. Push сървър няма (без
 * Firebase/проследяване), затова приложението само пита сайта. Всяко съобщение
 * дава известие най-много веднъж; при първото пускане старите съобщения не се
 * показват като известия. Докосването на известие отваря съобщенията, превъртени до
 * самото съобщение ([DeepLinks.message]).
 */
object MessageWorker {
    private const val MAX_PER_RUN = 3
    /** Известие само за съобщения от последните 7 дни. */
    private const val RECENT_SECONDS = 7 * 24 * 60 * 60L

    /**
     * Опреснява съобщенията (общите и, при вход, личните) и показва известия за
     * новите. Без включена настройка „Съобщения“ не прави нищо (и не пита сървъра).
     */
    suspend fun refreshAndNotify(context: Context, c: AppContainer, includePersonal: Boolean = true) {
        if (!c.settings.current().notifyMessages) return
        val result = c.messages.refresh(force = true, includePersonal = includePersonal)
        // Само съобщенията от сайта — личните имат собствено правило по-долу.
        val list = (result as? Outcome.Success)?.value?.takeIf { !it.fromCache }?.data?.filter { !it.personal }
        if (list != null) {
            val notified = c.settings.notifiedMessageIds()
            val read = c.settings.readMessageIdsNow()
            val firstRun = !c.settings.messagesInitialized()
            val cutoff = c.clock.now().minusSeconds(RECENT_SECONDS)
            list.filter { it.id !in notified && it.id !in read }
                .filter { !firstRun && AppMessagesParser.createdAt(it).isAfter(cutoff) }
                .take(MAX_PER_RUN)
                .forEach { m ->
                    Notifier.show(
                        context,
                        if (m.priority == MessagePriority.HIGH) Channel.MESSAGES_IMPORTANT else Channel.MESSAGES,
                        NotificationIds.message(m.id),
                        m.title, m.body.ifBlank { m.title }, DeepLinks.message(m.id),
                    )
                }
            c.settings.setNotifiedMessages(list.map { it.id }.toSet())
            c.settings.setMessagesInitialized()
        }
        if (includePersonal) notifyPersonal(context, c)
    }

    /**
     * Известия за нови лични съобщения от библиотеката (само при влязъл читател и
     * сървър с възможност `messages`; [AppContainer.messages] трябва вече да е опреснен).
     * Всяко съобщение — най-много едно известие; на заключен екран съдържанието е скрито.
     */
    suspend fun notifyPersonal(context: Context, c: AppContainer) {
        if (!c.settings.current().notifyMessages) return
        val fresh = c.messages.takePersonalToNotify(c.clock.now().minusSeconds(RECENT_SECONDS), MAX_PER_RUN)
        val fallbackTitle = context.getString(R.string.messages_personal_notification_title)
        fresh.forEach { m ->
            val title = m.title.ifBlank { fallbackTitle }
            Notifier.show(
                context,
                Channel.MESSAGES,
                NotificationIds.message(Inbox.personalId(m.id)),
                title, m.text.ifBlank { title }, DeepLinks.message(Inbox.personalId(m.id)),
                sensitive = true,
            )
        }
    }
}
