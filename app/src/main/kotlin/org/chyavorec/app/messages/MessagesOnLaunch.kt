package org.chyavorec.app.messages

import android.content.Context
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.chyavorec.app.di.AppContainer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Опресняване на съобщенията при отваряне на приложението — веднъж на процес.
 * Доскоро ставаше в заглавката на началния екран при всяко нейно показване
 * (връщане към „Начало“, превъртане обратно нагоре), т.е. с излишни заявки.
 * Върви извън екрана, затова не се прекъсва, ако потребителят веднага отвори друг.
 */
object MessagesOnLaunch {
    private val started = AtomicBoolean(false)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { _, _ -> })

    fun run(context: Context, c: AppContainer) {
        if (!started.compareAndSet(false, true)) return
        val app = context.applicationContext
        scope.launch {
            runCatching {
                c.messages.refresh(force = false)
                // Нови лични съобщения от библиотеката → известие и при отваряне (всяко само веднъж).
                MessageWorker.notifyPersonal(app, c)
            }
        }
    }
}
