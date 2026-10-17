package org.chyavorec.app.notifications

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanReminderKind
import org.chyavorec.domain.model.LoanReminderSync
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Уникалните задачи за напомняне: имена, закъснения, REPLACE при удължаване, отмяна. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LoanReminderSchedulerTest {
    private lateinit var wm: WorkManager
    // 15.10.2026, 09:00 в София (UTC+3).
    private val clock = FixedClock(Instant.parse("2026-10-15T06:00:00Z"))
    private lateinit var scheduler: LoanReminderScheduler

    @Before fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        WorkManagerTestInitHelper.initializeTestWorkManager(
            context,
            Configuration.Builder().setExecutor(SynchronousExecutor()).build(),
        )
        wm = WorkManager.getInstance(context)
        scheduler = LoanReminderScheduler(context, clock)
    }

    private fun loan(id: String, due: String?) = Loan(id, 1, "Под игото", "Иван Вазов", dueOn = due)

    private fun enqueued(id: String, kind: LoanReminderKind): List<WorkInfo> =
        wm.getWorkInfosForUniqueWork(LoanReminderScheduler.uniqueName(id, kind)).get().filter { it.state == WorkInfo.State.ENQUEUED }

    private fun hours(h: Long) = Duration.ofHours(h).toMillis()

    @Test fun schedulesBothRemindersAtTen() {
        scheduler.schedule(listOf(loan("L1", "2026-10-20")))
        val before = enqueued("L1", LoanReminderKind.BEFORE).single()
        // 18.10 10:00 − 15.10 09:00 = 3 дни и 1 час.
        assertEquals(hours(3 * 24 + 1), before.initialDelayMillis)
        assertTrue(LoanReminderScheduler.TAG in before.tags)
        assertEquals(hours(5 * 24 + 1), enqueued("L1", LoanReminderKind.DUE_DAY).single().initialDelayMillis)
    }

    @Test fun renewalReplacesWithNewDueDate() {
        scheduler.schedule(listOf(loan("L1", "2026-10-20")))
        scheduler.schedule(listOf(loan("L1", "2026-11-10")))
        val due = enqueued("L1", LoanReminderKind.DUE_DAY).single()
        // 10.11 10:00 (UTC+2) − 15.10 09:00 (UTC+3) = 26 дни и 2 часа.
        assertEquals(hours(26 * 24 + 2), due.initialDelayMillis)
        assertEquals(1, enqueued("L1", LoanReminderKind.BEFORE).size)
    }

    @Test fun pastOrMissingDueDateSchedulesNothing() {
        scheduler.schedule(listOf(loan("L1", "2026-10-20")))
        // Срокът вече е минал (напр. променен) → старите задачи се отменят.
        scheduler.schedule(listOf(loan("L1", "2026-10-14"), loan("L2", null)))
        assertTrue(enqueued("L1", LoanReminderKind.BEFORE).isEmpty())
        assertTrue(enqueued("L1", LoanReminderKind.DUE_DAY).isEmpty())
        assertTrue(enqueued("L2", LoanReminderKind.DUE_DAY).isEmpty())
    }

    @Test fun dueTodayAfterTenFiresSoon() {
        clock.advanceSeconds(4 * 3600) // 13:00
        scheduler.schedule(listOf(loan("L1", "2026-10-15")))
        assertEquals(Duration.ofMinutes(1).toMillis(), enqueued("L1", LoanReminderKind.DUE_DAY).single().initialDelayMillis)
        assertTrue(enqueued("L1", LoanReminderKind.BEFORE).isEmpty())
    }

    @Test fun applyTouchesOnlyChangedLoans() {
        val l1 = loan("L1", "2026-10-20")
        val l2 = loan("L2", "2026-10-25")
        scheduler.applyChanges(LoanReminderSync.diff(null, listOf(l1, l2)))
        val plan = LoanReminderSync.entries(listOf(l1, l2))
        val l2Work = enqueued("L2", LoanReminderKind.DUE_DAY).single().id

        // Непроменен списък (напр. нов старт на процеса) — нищо не се пренасрочва.
        assertTrue(LoanReminderSync.diff(plan, listOf(l1, l2)).isEmpty)

        // L1 е върната, L2 е същата, L3 е нова.
        scheduler.applyChanges(LoanReminderSync.diff(plan, listOf(l2, loan("L3", "2026-11-01"))))
        assertTrue(enqueued("L1", LoanReminderKind.BEFORE).isEmpty())
        assertTrue(enqueued("L1", LoanReminderKind.DUE_DAY).isEmpty())
        assertEquals(l2Work, enqueued("L2", LoanReminderKind.DUE_DAY).single().id)
        assertEquals(1, enqueued("L3", LoanReminderKind.DUE_DAY).size)
    }

    @Test fun reschedulingWaitsForRunningReminder() = runTest {
        LoanReminderScheduler.workerStarted()
        val waiting = async { LoanReminderScheduler.awaitIdle() }
        runCurrent()
        assertFalse(waiting.isCompleted)
        LoanReminderScheduler.workerFinished()
        waiting.await()
    }

    @Test fun cancelAllRemovesEveryReminder() {
        scheduler.schedule(listOf(loan("L1", "2026-10-20"), loan("L2", "2026-10-25")))
        scheduler.cancelAll()
        val infos = wm.getWorkInfosByTag(LoanReminderScheduler.TAG).get()
        assertEquals(4, infos.size)
        assertTrue(infos.all { it.state == WorkInfo.State.CANCELLED })
    }
}
