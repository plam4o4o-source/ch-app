package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Process
import android.util.SizeF
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.Instant
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Уиджетът (RemoteViews): сроковете — само брой книги и дати, без заглавия; следващото
 * събитие или „Няма предстоящи събития“; размерите; докосванията (изрични PendingIntent-и
 * през собствения вход); опресняването през ChitalishteWidgetReceiver и изходът от профила;
 * генерираният преглед. Снимките (90–93) — до тези на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = WorkerTestApp::class, qualifiers = "bg-xhdpi")
class ChitalishteWidgetTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val today = LocalDate.parse("2026-10-17")
    private var backend: TestBackend? = null

    @After fun tearDown() {
        backend?.shutdown()
    }

    private fun loan(id: String, due: String?) = Loan(loanId = id, inv = 1, title = "Под игото", author = "Иван Вазов", dueOn = due)

    /** Просрочена, две за 20.10, една за 29.10 и една без срок. */
    private fun signedInState(): WidgetState {
        val loans = listOf(
            loan("a", "2026-10-20"), loan("b", "2026-10-20"), loan("c", "2026-10-10"), loan("d", null), loan("e", "2026-10-29"),
        )
        val total = app.resources.getQuantityString(R.plurals.widget_books_to_return, loans.size, loans.size)
        return WidgetState("Литературна вечер", "18 октомври, 18:00", total, dueGroups(app, loans, today))
    }

    private fun apply(views: RemoteViews): View = views.apply(app, FrameLayout(app))

    private fun View.text(id: Int): String = findViewById<TextView>(id).text.toString()
    private fun View.visible(id: Int): Boolean = findViewById<View>(id).visibility == View.VISIBLE

    /** Всички видими текстове в изгледа. */
    private fun View.texts(): List<String> = when {
        visibility != View.VISIBLE -> emptyList()
        this is TextView -> listOf(text.toString())
        this is ViewGroup -> (0 until childCount).flatMap { getChildAt(it).texts() }
        else -> emptyList()
    }

    /** Докосване на [id] — връзката, с която се отваря приложението (или null за обикновено отваряне). */
    private fun View.click(id: Int): android.content.Intent {
        assertTrue(findViewById<View>(id).performClick())
        return assertNotNull(shadowOf(app).nextStartedActivity)
    }

    private fun assertLink(expected: String, intent: android.content.Intent) {
        assertEquals(LaunchIntents.ACTION_OPEN_LINK, intent.action)
        assertEquals(LaunchIntents.OPEN_LINK_ALIAS, intent.component?.className)
        assertEquals(expected, intent.getStringExtra(Notifier.EXTRA_DEEP_LINK))
    }

    @Test fun dueGroupsHaveOnlyCountsAndDates() {
        val groups = signedInState().groups
        assertEquals(listOf("просрочена", "след 3 дни", "след 12 дни", "без срок"), groups.map { it.countdown })
        assertEquals(listOf(DueStatus.OVERDUE, DueStatus.DUE_SOON, DueStatus.PLENTY_OF_TIME, null), groups.map { it.status })
        assertEquals(listOf("1 книга · 10.10", "2 книги · 20.10", "1 книга · 29.10", "1 книга"), groups.map { it.line })
        // В широкия: само датата за една книга, иначе броят и датата.
        assertEquals(listOf("10.10", "2 книги · 20.10", "29.10", "1 книга"), groups.map { it.short })
    }

    @Test fun smallShowsNearestDueAndOpensLoans() {
        val state = signedInState()
        val v = apply(WidgetViews.build(app, state, WidgetViews.SmallSize))
        assertTrue(v.visible(R.id.widget_loans))
        assertFalse(v.visible(R.id.widget_event_column))
        assertEquals("просрочена", v.text(R.id.widget_countdown))
        assertEquals(app.getColor(R.color.widget_overdue_text), v.findViewById<TextView>(R.id.widget_countdown).currentTextColor)
        assertEquals("1 книга · 10.10", v.text(R.id.widget_due_line))
        assertEquals("5 книги за връщане", v.text(R.id.widget_loans_total))
        assertTrue(v.texts().none { "Под игото" in it || "Вазов" in it })

        assertLink("my/loans", v.click(R.id.widget_loans))
        val open = v.click(android.R.id.background)
        assertEquals(MainActivity::class.java.name, open.component?.className)
        assertEquals(android.content.Intent.ACTION_MAIN, open.action)
    }

    @Test fun smallWithoutLoansShowsNextEvent() {
        val state = signedInState().copy(loansTotal = null, groups = emptyList())
        val v = apply(WidgetViews.build(app, state, WidgetViews.SmallSize))
        assertFalse(v.visible(R.id.widget_loans))
        assertTrue(v.visible(R.id.widget_event))
        assertEquals("Литературна вечер", v.text(R.id.widget_event_title))
        assertEquals("18 октомври, 18:00", v.text(R.id.widget_event_date))
        assertLink("events", v.click(R.id.widget_event))
    }

    @Test fun wideShowsGroupsByHeightEventAndButtons() {
        val state = signedInState()
        val v = apply(WidgetViews.build(app, state, WidgetViews.WideSize))
        assertEquals("5 книги за връщане", v.text(R.id.widget_loans_total))
        assertEquals("просрочена", v.text(R.id.widget_group1_chip))
        assertEquals("10.10", v.text(R.id.widget_group1_text))
        assertEquals("след 3 дни", v.text(R.id.widget_group2_chip))
        assertEquals(app.getColor(R.color.widget_soon_text), v.findViewById<TextView>(R.id.widget_group2_chip).currentTextColor)
        assertEquals("2 книги · 20.10", v.text(R.id.widget_group2_text))
        // 4×2 — два срока; по-високият — три.
        assertFalse(v.visible(R.id.widget_group3))
        val tall = apply(WidgetViews.build(app, state, WidgetViews.WideTallSize))
        assertTrue(tall.visible(R.id.widget_group3))
        assertEquals("след 12 дни", tall.text(R.id.widget_group3_chip))
        assertEquals("Литературна вечер", v.text(R.id.widget_event_title))

        assertLink("scan", v.click(R.id.widget_scan))
        assertLink("catalog", v.click(R.id.widget_catalog))
        assertLink("my/loans", v.click(R.id.widget_loans))
        assertLink("events", v.click(R.id.widget_event))
    }

    @Test fun signedOutWithoutEventsShowsNoEvents() {
        for (size in listOf(WidgetViews.SmallSize, WidgetViews.WideSize)) {
            val v = apply(WidgetViews.build(app, EmptyWidgetState, size))
            assertFalse(v.visible(R.id.widget_loans))
            assertFalse(v.visible(R.id.widget_event))
            assertTrue(v.visible(R.id.widget_no_events))
            assertEquals(app.getString(R.string.widget_no_events), v.text(R.id.widget_no_events))
        }
    }

    @Test fun sizesPickLargestThatFits() {
        assertEquals(WidgetViews.SmallSize, WidgetViews.pick(SizeF(180f, 180f)))
        assertEquals(WidgetViews.WideSize, WidgetViews.pick(SizeF(320f, 120f)))
        assertEquals(WidgetViews.WideTallSize, WidgetViews.pick(SizeF(320f, 200f)))
        // По-малко от най-малкия — най-малкият.
        assertEquals(WidgetViews.SmallSize, WidgetViews.pick(SizeF(60f, 60f)))
    }

    @Test fun receiverRefreshesAndSignOutClearsLoans() = runBlocking<Unit> {
        val backend = TestBackend().start().also { backend = it }
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        app.container = backend.container(app, FixedClock(Instant.parse("2026-10-17T10:00:00Z")))
        val manager = AppWidgetManager.getInstance(app)
        val id = 7
        shadowOf(manager).bindAppWidgetId(id, ComponentName(app, ChitalishteWidgetReceiver::class.java))
        assertTrue(WidgetUpdater.hasInstances(app))

        // Ново копие: без вход и без събития.
        ChitalishteWidgetReceiver().onUpdate(app, manager, intArrayOf(id))
        WidgetUpdater.awaitIdle()
        assertTrue(app.getString(R.string.widget_no_events) in shadowOf(manager).getViewFor(id).texts())

        assertIs<Outcome.Success<Unit>>(app.container.authRepository.login("0042", "1234".toCharArray(), remember = false))
        backend.loans(
            """{"loanId":"L1","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-10-01","dateDue":"2026-10-20"}""",
        )
        assertIs<Outcome.Success<*>>(app.container.libraryRepository.loans(force = true))
        WidgetUpdater.refresh(app)
        val signedIn = shadowOf(manager).getViewFor(id).texts()
        assertTrue("след 3 дни" in signedIn, signedIn.toString())
        assertTrue(signedIn.none { "Под игото" in it || "Вазов" in it })

        app.container.authRepository.logout()
        WidgetUpdater.refresh(app)
        val signedOut = shadowOf(manager).getViewFor(id).texts()
        assertTrue(signedOut.none { "след" in it }, signedOut.toString())
    }

    @Test fun previewIsPublishedOncePerVersionWithSampleTexts() {
        val manager = AppWidgetManager.getInstance(app)
        val provider = ComponentName(app, ChitalishteWidgetReceiver::class.java)
        assertTrue(WidgetUpdater.publishPreview(app))
        // Вече публикуван за тази версия и език — не отново.
        assertFalse(WidgetUpdater.publishPreview(app))
        val preview = manager.getWidgetPreview(provider, Process.myUserHandle(), AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN)
        val texts = apply(assertNotNull(preview)).texts()
        listOf(
            R.string.widget_preview_books, R.string.widget_preview_due, R.string.widget_preview_date,
            R.string.widget_preview_event, R.string.widget_preview_event_date, R.string.shortcut_scan,
        ).forEach { assertTrue(app.getString(it) in texts, texts.toString()) }
    }

    @Test fun previewPolicyRetriesFailuresAfterADay() {
        val day = 24 * 60 * 60 * 1000L
        assertTrue(WidgetUpdater.shouldPublishPreview("2:bg", null, 0, 10 * day))
        assertFalse(WidgetUpdater.shouldPublishPreview("2:bg", "2:bg", 0, 10 * day))
        // Неуспешен опит преди час — не; след ден — да; нова версия — пак само след ден от опита.
        assertFalse(WidgetUpdater.shouldPublishPreview("2:bg", "1:bg", 10 * day - 3_600_000, 10 * day))
        assertTrue(WidgetUpdater.shouldPublishPreview("2:bg", "1:bg", 9 * day, 10 * day))
        assertTrue(WidgetUpdater.shouldPublishPreview("2:en", "2:bg", 0, 10 * day))
    }

    // Снимки за визуален преглед (не сравняват пиксели).

    @Test fun shotWideLight() = shot("90-widget-wide", WidgetViews.build(app, signedInState(), WidgetViews.WideTallSize), 320, 170)

    @Config(qualifiers = "+night")
    @Test fun shotWideDark() = shot("91-widget-wide-dark", WidgetViews.build(app, signedInState(), WidgetViews.WideSize), 320, 130)

    @Test fun shotSmall() = shot("92-widget-small", WidgetViews.build(app, signedInState(), WidgetViews.SmallSize), 160, 150)

    @Test fun shotSignedOut() {
        val state = signedInState().copy(loansTotal = null, groups = emptyList())
        shot("93-widget-wide-signed-out", WidgetViews.build(app, state, WidgetViews.WideSize), 320, 130)
    }

    /** Изгледът [wDp]×[hDp] върху „тапет“, с поле около уиджета. */
    private fun shot(name: String, views: RemoteViews, wDp: Int, hDp: Int) {
        val density = app.resources.displayMetrics.density
        val w = (wDp * density).roundToInt()
        val h = (hDp * density).roundToInt()
        val pad = (16 * density).roundToInt()
        val v = apply(views)
        v.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY))
        v.layout(0, 0, w, h)
        val bmp = Bitmap.createBitmap(w + 2 * pad, h + 2 * pad, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.rgb(0x5F, 0x76, 0x8A))
        canvas.translate(pad.toFloat(), pad.toFloat())
        v.draw(canvas)
        val outDir = File("build/reports/screenshots").apply { mkdirs() }
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
