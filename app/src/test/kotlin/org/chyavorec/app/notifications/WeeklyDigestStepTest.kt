package org.chyavorec.app.notifications

import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.core.FixedClock
import org.chyavorec.domain.model.FavouriteSections
import org.chyavorec.domain.model.MessagePollPolicy
import org.chyavorec.domain.model.WeeklyDigest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * „Тази седмица в Яворец“ във фоновата задача ([PeriodicSyncWorker] → [WeeklyDigestStep]): първото
 * включване само записва начална точка; после — един преглед на седмица, само с книги СЛЕД нея,
 * без нищо когато няма какво да се каже; не нощем; с изключена настройка — без заявки.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = WorkerTestApp::class, qualifiers = "bg")
class WeeklyDigestStepTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val nm get() = app.getSystemService(NotificationManager::class.java)
    private val backend = TestBackend()
    private val zone = ZoneId.of("Europe/Sofia")

    private fun at(date: String, time: String): Instant = LocalDateTime.parse("${date}T$time").atZone(zone).toInstant()

    // Неделя, 15.11.2026, 18:30 в София.
    private val clock = FixedClock(at("2026-11-15", "18:30"))
    private val slot = at("2026-11-15", "18:00").toEpochMilli()
    private val previousSlot = at("2026-11-08", "18:00").toEpochMilli()
    private val baseline = at("2026-11-02", "12:00").toEpochMilli()

    private fun item(inv: Int, title: String, registered: String, udc: String = "8", dep: String = "за възрастни") =
        """{"inv":$inv,"a":"Автор $inv","t":"$title","u":"$udc","o":"$dep","av":1,"d":"$registered"}"""

    private fun catalog(vararg items: String) = """{"library":"Б","place":"Я","generated":"2026-11-14","items":[${items.joinToString(",")}],"shelves":[]}"""

    private val settings get() = app.container.settings

    @Before fun setUp() = runBlocking<Unit> {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.json("/data/app-messages.json", "[]")
        backend.json("/data/news.json", "[]")
        backend.json("/rss.xml", """<?xml version="1.0"?><rss version="2.0"><channel><title>x</title></channel></rss>""")
        backend.json("/katalog.json", catalog(item(1, "Стара книга", "2023-07-13"), item(2, "Нова книга", "2026-11-10")))
        app.container = backend.container(app, clock)
        settings.setNotifyMessages(false)
        settings.setNotifyNews(false)
        settings.setNotifyLoans(false)
        settings.setNotifyLibrary(false)
        settings.setTomorrowReminder(false)
        settings.setDigestSections(emptySet())
        settings.setLastSyncStep(WeeklyDigestStep.STEP_CATALOG, 0)
        settings.markAppOpened(clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, 0)
        enable(baseline, previousSlot)
    }

    @After fun tearDown() = runBlocking<Unit> {
        settings.setWeeklyDigest(false, 0, 0)
        backend.shutdown()
    }

    private fun enable(baselineMillis: Long, lastSlot: Long) = runBlocking<Unit> { settings.setWeeklyDigest(true, baselineMillis, lastSlot) }

    private fun run(): ListenableWorker.Result = runBlocking { TestListenableWorkerBuilder<PeriodicSyncWorker>(app).build().doWork() }

    private fun digest() = nm.activeNotifications.filter { it.id == NotificationIds.DIGEST }

    @Test fun sendsOneQuietDigestWithTheNewBookAndTheCatalogueLink() = runBlocking<Unit> {
        assertIs<ListenableWorker.Result.Success>(run())
        val shown = digest().single()
        assertEquals(Channel.DIGEST.id, shown.notification.channelId)
        assertEquals(app.getString(R.string.channel_digest), nm.getNotificationChannel("digest").name.toString())
        assertEquals("Тази седмица в Яворец", shown.notification.extras.getString(android.app.Notification.EXTRA_TITLE))
        assertEquals("1 нова книга", shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT).toString())
        assertTrue("„Нова книга“" in shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_BIG_TEXT).toString())
        // Слотът е обработен, началната точка — сега.
        assertEquals(slot, settings.digestState().lastSlotMillis)
        assertEquals(clock.now().toEpochMilli(), settings.digestState().baselineMillis)
        // Общ преглед без лични данни — публично съдържание.
        assertEquals(android.app.Notification.VISIBILITY_PUBLIC, shown.notification.visibility)
        assertNotNull(shown.notification.contentIntent)

        // Повторно пускане в същия слот — нищо ново (не повече от един на седмица).
        nm.cancelAll()
        run()
        assertTrue(digest().isEmpty())
    }

    private fun text(shown: android.service.notification.StatusBarNotification) =
        shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT).toString()

    private fun bigText(shown: android.service.notification.StatusBarNotification) =
        shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_BIG_TEXT).toString()

    /** Дата без час: книга/новина от деня на предишния преглед, качена след него, влиза в следващия — веднъж. */
    @Test fun aSameDayItemAfterTheLastDigestIsCountedInTheNextOneAndNothingTwice() = runBlocking<Unit> {
        backend.json("/data/news.json", """[{"id":"n1","title":"Новина от неделя","date":"2026-11-15","content":"<p>x</p>"}]""")
        // Първо включване: нищо „показано“ още — първият преглед брои само СЛЕД началната точка.
        assertEquals(null, settings.digestShown())
        run()
        assertEquals("1 нова книга · 1 нова новина", text(digest().single()))
        val shownAfterFirst = assertNotNull(settings.digestShown())
        assertTrue("b:2" in shownAfterFirst && "n:n1" in shownAfterFirst, shownAfterFirst.toString())

        // След седмица: още една книга със същата дата като първия преглед; старите не се броят пак.
        nm.cancelAll()
        backend.json("/katalog.json", catalog(item(1, "Стара книга", "2023-07-13"), item(2, "Нова книга", "2026-11-10"), item(3, "Вечерна книга", "2026-11-15")))
        clock.advanceSeconds(7 * 24 * 3600)
        run()
        val second = digest().single()
        assertEquals("1 нова книга", text(second))
        assertTrue("Вечерна книга" in bigText(second) && "Нова книга" !in bigText(second) && "Новина" !in bigText(second), bigText(second))
        assertTrue("b:3" in assertNotNull(settings.digestShown()))

        // И още една седмица без нищо ново — тих слот, нищо не се повтаря.
        nm.cancelAll()
        clock.advanceSeconds(7 * 24 * 3600)
        run()
        assertTrue(digest().isEmpty())
    }

    @Test fun anEmptyFirstDigestStillSwitchesToTheSameDayRule() = runBlocking<Unit> {
        backend.json("/katalog.json", catalog(item(1, "Стара книга", "2023-07-13")))
        run()
        assertTrue(digest().isEmpty())
        assertEquals(emptyList(), settings.digestShown())
    }

    @Test fun reEnablingForgetsWhatWasShown() = runBlocking<Unit> {
        run()
        assertNotNull(settings.digestShown())
        settings.setWeeklyDigest(false, 0, 0)
        assertEquals(null, settings.digestShown())
        enable(baseline, previousSlot)
        assertEquals(null, settings.digestShown())
    }

    @Test fun enablingJustBeforeTheSlotNeverFloods() = runBlocking<Unit> {
        // Включен току-що (началната точка е сега): слотът е по-малко от денонощие след нея — прескача се.
        enable(clock.now().toEpochMilli(), previousSlot)
        run()
        assertTrue(digest().isEmpty())
        assertEquals(slot, settings.digestState().lastSlotMillis)
    }

    @Test fun nothingNewMeansNoNotificationButTheWeekIsConsumed() = runBlocking<Unit> {
        backend.json("/katalog.json", catalog(item(1, "Стара книга", "2023-07-13"), item(2, "Още по-стара", "2021-01-01")))
        run()
        assertTrue(digest().isEmpty())
        assertEquals(slot, settings.digestState().lastSlotMillis)
        assertEquals(clock.now().toEpochMilli(), settings.digestState().baselineMillis, "началната точка се мести напред")
    }

    @Test fun favouriteSectionsLimitTheNewBooksLine() = runBlocking<Unit> {
        backend.json("/katalog.json", catalog(item(2, "Нова книга", "2026-11-10", udc = "8"), item(3, "Нова физика", "2026-11-11", udc = "53")))
        settings.setDigestSections(setOf(FavouriteSections.udc(5)))
        run()
        val shown = digest().single()
        assertEquals("1 нова книга", shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT).toString())
        assertTrue("Нова физика" in shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_BIG_TEXT).toString())
        assertTrue("Нова книга" !in shown.notification.extras.getCharSequence(android.app.Notification.EXTRA_BIG_TEXT).toString())
    }

    @Test fun quietHoursWaitWithoutConsumingTheSlot() = runBlocking<Unit> {
        clock.advanceSeconds(4 * 3600) // 22:30
        run()
        assertTrue(digest().isEmpty())
        assertEquals(previousSlot, settings.digestState().lastSlotMillis)
    }

    @Test fun disabledDigestAsksNobodyAnything() = runBlocking<Unit> {
        settings.setWeeklyDigest(false, 0, 0)
        run()
        assertTrue(digest().isEmpty())
        assertEquals(0, backend.count("/katalog.json"))
    }

    @Test fun catalogueIsCheckedAtMostOncePerDay() = runBlocking<Unit> {
        run()
        assertEquals(1, backend.count("/katalog.json"))
        // Следващият слот е след седмица, но стъпката „каталог“ е запазена за денонощие.
        assertTrue(settings.lastSyncStep(WeeklyDigestStep.STEP_CATALOG) > 0)
        assertEquals(WeeklyDigest.Decision.None, WeeklyDigest.decide(clock.now(), zone, settings.digestState()))
    }
}
