package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery

/**
 * Задачите, останали от стария уиджет (Glance 1.1, до 1.2.28): Glance поставя уникалната
 * `sessionWorkerKeepEnabled` (SessionWorker, отложен ~10 години — да не се изключва
 * WorkManager) и по една сесия на копие на уиджета (`appWidget-<id>`). Класът вече го няма —
 * ако някоя тръгне, пада още при създаването. Отменят се еднократно след обновяване
 * (ChitalishteApp.setUpBackground), после завършените записи се изчистват от базата.
 */
object LegacyWork {
    const val GLANCE_KEEP_ENABLED = "sessionWorkerKeepEnabled"
    /** Етикетът, който WorkManager слага на всяка задача — името на класа. */
    const val GLANCE_SESSION_WORKER = "androidx.glance.session.SessionWorker"
    const val GLANCE_SESSION_PREFIX = "appWidget-"

    /** Блокиращо (WorkManager чете базата си) — само извън главната нишка. */
    fun cancelGlance(context: Context) {
        val wm = WorkManager.getInstance(context)
        wm.cancelUniqueWork(GLANCE_KEEP_ENABLED).result.get()
        wm.cancelAllWorkByTag(GLANCE_SESSION_WORKER).result.get()
        // И по името на сесията, ако е и етикет.
        val pending = WorkQuery.fromStates(WorkInfo.State.ENQUEUED, WorkInfo.State.BLOCKED, WorkInfo.State.RUNNING)
        wm.getWorkInfos(pending).get()
            .filter { info -> info.tags.any { it.startsWith(GLANCE_SESSION_PREFIX) || it == GLANCE_SESSION_WORKER } }
            .forEach { wm.cancelWorkById(it.id).result.get() }
        wm.pruneWork().result.get()
    }
}
