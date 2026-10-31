package org.chyavorec.app.notifications

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.LoanReminderKind
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

/**
 * След обновяване на приложението напомнянията за срок се поставят наново (веднъж): вече
 * поставените задачи пазят настройките на старата версия (напр. паузата между опитите).
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class)
class LoanReminderUpgradeTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val backend = TestBackend()
    // 15.10.2026, 09:00 в София — напомнянията за срока 20.10 са напред.
    private val clock = FixedClock(Instant.parse("2026-10-15T06:00:00Z"))
    /** Уникално за теста — DataStore (планът) се споделя между тестовете. */
    private val loanId = "L-" + UUID.randomUUID()
    private lateinit var wm: WorkManager

    @Before fun setUp() {
        WorkManagerTestInitHelper.initializeTestWorkManager(app, Configuration.Builder().setExecutor(SynchronousExecutor()).build())
        wm = WorkManager.getInstance(app)
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.loans(
            """{"loanId":"$loanId","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-09-20","dateDue":"2026-10-20","canRenew":true}""",
        )
    }

    @After fun tearDown() = backend.shutdown()

    /** ID-тата на поставените напомняния (едно на вид — без дубликати). */
    private fun reminders() = LoanReminderKind.entries.map { kind ->
        wm.getWorkInfosForUniqueWork(LoanReminderScheduler.uniqueName(loanId, kind)).get().single { it.state == WorkInfo.State.ENQUEUED }.id
    }

    /** Нов процес с [versionCode]: сесията — от хранилището, наблюдателите — както в ChitalishteApp. */
    private suspend fun process(versionCode: Int): AppContainer = backend.container(app, clock, versionCode = versionCode).also {
        app.container = it
        it.watchReader()
        it.authRepository.restore()
    }

    private suspend fun awaitPlan(c: AppContainer, versionCode: Int) = withTimeout(5_000) {
        while (c.settings.loanReminderPlanVersion() != versionCode || c.settings.loanReminderPlan().orEmpty().none { it.startsWith("$loanId|") }) delay(20)
    }

    @Test fun upgradeReschedulesEveryReminderOnce() = runBlocking<Unit> {
        val first = backend.container(app, clock, versionCode = 1)
        app.container = first
        first.settings.setNotifyLoans(true)
        first.watchReader()
        assertIs<Outcome.Success<Unit>>(first.authRepository.login("0042", "1234".toCharArray(), remember = true))
        first.libraryRepository.loans(force = true)
        awaitPlan(first, 1)
        val original = reminders()

        try {
            // Нов процес на същата версия — нищо не се пренасрочва.
            process(versionCode = 1)
            delay(500)
            assertEquals(original, reminders())

            // Обновено приложение — всяко напомняне е поставено наново, по веднъж.
            val upgraded = process(versionCode = 2)
            awaitPlan(upgraded, 2)
            withTimeout(5_000) { while (runCatching { reminders().any { it in original } }.getOrDefault(true)) delay(20) }
            val again = reminders()
            original.zip(again).forEach { (old, new) -> assertNotEquals(old, new) }

            // Следващият старт на новата версия — пак нищо.
            process(versionCode = 2)
            delay(500)
            assertEquals(again, reminders())
        } finally {
            // Другите тестове са с версия 1 (DataStore се споделя).
            app.container.settings.setLoanReminderPlan(emptySet(), versionCode = 1)
        }
    }
}
