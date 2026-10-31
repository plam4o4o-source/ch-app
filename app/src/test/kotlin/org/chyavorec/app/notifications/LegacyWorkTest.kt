package org.chyavorec.app.notifications

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Задачите на стария уиджет (Glance) се отменят и изчистват; собствените задачи остават. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LegacyWorkTest {
    class NoopWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork() = Result.success()
    }

    private val context get() = ApplicationProvider.getApplicationContext<Application>()

    /** Отложена (не тръгва в теста) — като SessionWorker-а на Glance, отложен ~10 години. */
    private fun delayed(vararg tags: String): OneTimeWorkRequest =
        OneTimeWorkRequestBuilder<NoopWorker>().setInitialDelay(3650, TimeUnit.DAYS).apply { tags.forEach { addTag(it) } }.build()

    @Test fun glanceLeftoversAreCancelledAndPruned() {
        WorkManagerTestInitHelper.initializeTestWorkManager(context, Configuration.Builder().setExecutor(SynchronousExecutor()).build())
        val wm = WorkManager.getInstance(context)
        val keepEnabled = delayed()
        val session = delayed(LegacyWork.GLANCE_SESSION_WORKER)
        val taggedSession = delayed("appWidget-43")
        val ours = delayed(LoanReminderScheduler.TAG)
        wm.enqueueUniqueWork(LegacyWork.GLANCE_KEEP_ENABLED, ExistingWorkPolicy.KEEP, keepEnabled).result.get()
        wm.enqueueUniqueWork("appWidget-42", ExistingWorkPolicy.REPLACE, session).result.get()
        wm.enqueue(taggedSession).result.get()
        wm.enqueueUniqueWork("loan-due:L1:BEFORE", ExistingWorkPolicy.REPLACE, ours).result.get()

        LegacyWork.cancelGlance(context)

        for (request in listOf(keepEnabled, session, taggedSession)) {
            val state = wm.getWorkInfoById(request.id).get()?.state
            // Отменена (или вече изчистена от базата) — не чака да тръгне.
            assertTrue(state == null || state == WorkInfo.State.CANCELLED, "$state")
        }
        assertEquals(WorkInfo.State.ENQUEUED, wm.getWorkInfoById(ours.id).get()?.state)
        // Второ извикване (следващо обновяване) е безвредно.
        LegacyWork.cancelGlance(context)
        assertEquals(WorkInfo.State.ENQUEUED, wm.getWorkInfoById(ours.id).get()?.state)
    }
}
