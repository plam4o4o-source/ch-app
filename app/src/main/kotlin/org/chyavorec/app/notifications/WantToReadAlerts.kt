package org.chyavorec.app.notifications

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.chyavorec.app.R
import org.chyavorec.app.data.local.WantToReadEntity
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.domain.model.LoanReminderPlan
import org.chyavorec.domain.model.WatchTransitions
import org.chyavorec.domain.model.WatchedBook

/**
 * „Кажи ми, когато книгата е на рафта“. Мостът няма API за наличност и запазване, затова
 * всичко е на телефона: при всяко ново опресняване на каталога (на преден план — [watch],
 * във фонов режим — [refreshAndCheck] от [PeriodicSyncWorker]) наличността на книгите от
 * „Искам да прочета“ се сравнява със запомнената ([WatchTransitions]) и за върнатите на
 * рафта книги с „Кажи ми“ идва известие в канала „Библиотека“ — веднъж на преход.
 */
object WantToReadAlerts {
    /** Една проверка наведнъж — наблюдателят и фоновата задача не показват един преход два пъти. */
    private val lock = Mutex()

    /**
     * Сравнява наличността в [searcher] със запомнената и записва новата. Известие — само при
     * включени и разрешени известия „Библиотека“ (иначе само се обновява състоянието, за да не
     * дойдат стари известия след включването им). Нощем, без отворено приложение, известието
     * изчаква сутринта (преходът не се записва).
     */
    suspend fun check(context: Context, c: AppContainer, searcher: CatalogSearcher) = lock.withLock {
        val dao = c.database.wantToRead()
        val saved = dao.all()
        if (saved.isEmpty()) return@withLock
        val current = searcher.books(saved.map { it.inv }).mapValues { it.value.available }
        val enabled = c.settings.current().notifyLibrary && Notifier.canNotify(context)
        val defer = enabled && LoanReminderPlan.isQuiet(c.clock.now()) && !inForeground()
        val result = WatchTransitions.check(saved.map { WatchedBook(it.inv, it.notifyWhenAvailable, it.lastKnownAvailable) }, current, defer)
        // Първо записът, после известието: прекъсване между двете губи едно известие, но не го повтаря.
        result.changed.forEach { (inv, available) -> dao.setAvailable(inv, available) }
        if (!enabled) return@withLock
        val byInv = saved.associateBy { it.inv }
        result.becameAvailable.forEach { inv -> byInv[inv]?.let { show(context, it) } }
    }

    /**
     * Фоновата стъпка: само ако в списъка има книга с „Кажи ми“. Каталогът се опреснява през
     * общото хранилище — условна заявка (ETag; 304 без тяло при непроменен файл), а в рамките на
     * 30 минути след последното сваляне — без мрежа. Сравнява се само със свежи данни.
     * @return true — проверено (или няма какво); false — каталогът не е опреснен.
     */
    suspend fun refreshAndCheck(context: Context, c: AppContainer): Boolean {
        if (!c.database.wantToRead().anyNotify()) return true
        val refreshed = (c.catalogRepository.refreshHomeSummary(force = false) as? Outcome.Success)?.value ?: return false
        if (refreshed.fromCache) return false
        // Новият каталог се индексира във фонов режим — справката чака новия индекс, не стария.
        val lookup = c.catalogRepository.latestLookup() ?: return false
        if (lookup.fromCache) return false
        check(context, c, lookup.data)
        return true
    }

    /**
     * На преден план: всяка нова търсачка на каталога (нов каталог, приет от който и да е екран)
     * → проверка. Остарелите (кешът, старият индекс докато се попълва новият) се прескачат —
     * сравнението е само със свежи данни. Тече, докато е жив процесът (ChitalishteApp).
     */
    suspend fun watch(context: Context, c: AppContainer) {
        c.catalogRepository.searcherUpdates.collect { s ->
            if (s.fromCache) return@collect
            try {
                check(context, c, s.data)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Diagnostics.record("want-to-read.check", e)
            }
        }
    }

    private fun inForeground(): Boolean =
        runCatching { ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) }.getOrDefault(false)

    private fun show(context: Context, book: WantToReadEntity) {
        Notifier.show(
            context, Channel.LIBRARY, NotificationIds.wantToRead(book.inv),
            context.getString(R.string.notif_want_available, book.title),
            context.getString(R.string.notif_want_available_text),
            DeepLinks.book(book.inv),
            // Личният списък не се вижда на заключен екран — там само общ текст.
            sensitive = true,
            lockScreenTitle = context.getString(R.string.notif_public_want),
        )
    }
}
