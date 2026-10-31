package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.LocaleList
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
import okhttp3.mockwebserver.MockResponse
import org.chyavorec.app.FlakyKeystoreCipher
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.TestBackend
import org.chyavorec.app.WorkerTestApp
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.app.util.AppLanguage
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Event
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
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Уиджетът (RemoteViews): сроковете — само брой книги и дати, без заглавия; следващото
 * събитие или „Няма предстоящи събития“; размерите и името по ширината; бутоните (48 dp);
 * докосванията (изрични PendingIntent-и през собствения вход); опресняването през
 * ChitalishteWidgetReceiver, изходът от профила, временно нечетимата сесия и бавните събития;
 * езикът на приложението; генерираният преглед. Снимките (90–95) — до тези на ScreenshotTest
 * (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = WorkerTestApp::class, qualifiers = "bg-xhdpi")
class ChitalishteWidgetTest {
    private val app get() = ApplicationProvider.getApplicationContext<WorkerTestApp>()
    private val today = LocalDate.parse("2026-10-17")
    private var backend: TestBackend? = null

    @After fun tearDown() {
        runBlocking { WidgetUpdater.awaitIdle() }
        backend?.shutdown()
    }

    private fun loan(id: String, due: String?) = Loan(loanId = id, inv = 1, title = "Под игото", author = "Иван Вазов", dueOn = due)

    /** Просрочена, две за 20.10, една за 29.10 и една без срок. */
    private fun signedInState(): WidgetState {
        val loans = listOf(
            loan("a", "2026-10-20"), loan("b", "2026-10-20"), loan("c", "2026-10-10"), loan("d", null), loan("e", "2026-10-29"),
        )
        val total = app.resources.getQuantityString(R.plurals.widget_books_to_return, loans.size, loans.size)
        return WidgetState(listOf(WidgetEvent("Литературна вечер", "18 октомври, 18:00")), total, dueGroups(app, loans, today))
    }

    /** Без вход: две предстоящи събития. */
    private fun signedOutState() = WidgetState(
        listOf(WidgetEvent("Литературна вечер", "18 октомври, 18:00"), WidgetEvent("Бенджамин Франклин", "17 януари 2027")),
        null,
        emptyList(),
    )

    private fun apply(views: RemoteViews): View = views.apply(app, FrameLayout(app))

    /** Изгледът, измерен и разположен в [wDp]×[hDp]. */
    private fun layOut(views: RemoteViews, wDp: Int, hDp: Int): View {
        val density = app.resources.displayMetrics.density
        val w = (wDp * density).roundToInt()
        val h = (hDp * density).roundToInt()
        val v = apply(views)
        v.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY))
        v.layout(0, 0, w, h)
        return v
    }

    /** Отместването (x, y) на изгледа спрямо [root]. */
    private fun View.offsetIn(root: View): Pair<Int, Int> {
        var x = 0
        var y = 0
        var v: View = this
        while (v !== root) {
            x += v.left
            y += v.top
            v = v.parent as View
        }
        return x to y
    }

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
        // Датата е първа: в тясна колона „…“ реже броя, не датата.
        assertEquals(listOf("10.10 · 1 книга", "20.10 · 2 книги", "29.10 · 1 книга", "1 книга"), groups.map { it.line })
        // В широкия: само датата за една книга, иначе датата и броят.
        assertEquals(listOf("10.10", "20.10 · 2 книги", "29.10", "1 книга"), groups.map { it.short })
    }

    @Test fun smallShowsNearestDueAndOpensLoans() {
        val state = signedInState()
        val v = apply(WidgetViews.build(app, state, WidgetViews.SmallSize))
        assertTrue(v.visible(R.id.widget_loans))
        assertFalse(v.visible(R.id.widget_event_column))
        assertEquals("просрочена", v.text(R.id.widget_countdown))
        assertEquals(app.getColor(R.color.widget_overdue_text), v.findViewById<TextView>(R.id.widget_countdown).currentTextColor)
        assertEquals("10.10 · 1 книга", v.text(R.id.widget_due_line))
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
        assertEquals("20.10 · 2 книги", v.text(R.id.widget_group2_text))
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

    @Test fun unknownEventsShowNeitherEventNorNoEvents() {
        // Без кеш на събитията (теглят се във фон) — не „Няма предстоящи събития“.
        val state = WidgetState(null, null, emptyList())
        for (size in listOf(WidgetViews.SmallSize, WidgetViews.FullWideSize)) {
            val v = apply(WidgetViews.build(app, state, size))
            assertFalse(v.visible(R.id.widget_event))
            assertFalse(v.visible(R.id.widget_no_events))
        }
    }

    @Test fun tallSignedOutShowsTwoEvents() {
        val state = signedOutState()
        for (size in listOf(WidgetViews.SmallTallSize, WidgetViews.WideTallSize, WidgetViews.FullWideTallSize)) {
            val v = apply(WidgetViews.build(app, state, size))
            assertEquals("Предстоящи събития", v.text(R.id.widget_next_event_label))
            assertEquals("Литературна вечер", v.text(R.id.widget_event_title))
            assertTrue(v.visible(R.id.widget_event2))
            assertEquals("Бенджамин Франклин", v.text(R.id.widget_event2_title))
            assertEquals("17 януари 2027", v.text(R.id.widget_event2_date))
            assertLink("events", v.click(R.id.widget_event2))
        }
        // Ниските — едно; високият със заемания — също едно (колоната е половината).
        for (v in listOf(
            apply(WidgetViews.build(app, state, WidgetViews.SmallSize)),
            apply(WidgetViews.build(app, state, WidgetViews.FullWideSize)),
            apply(WidgetViews.build(app, signedInState().copy(events = state.events), WidgetViews.FullWideTallSize)),
        )) {
            assertFalse(v.visible(R.id.widget_event2))
            assertEquals("Следващо събитие", v.text(R.id.widget_next_event_label))
        }
    }

    @Test fun titleFollowsWidth() {
        // Малкият и тесният широк — краткото име; най-тесният широк (3×2) — само емблемата, с описание.
        val small = apply(WidgetViews.build(app, EmptyWidgetState, WidgetViews.SmallSize))
        assertEquals("Читалище", small.text(R.id.widget_title))
        val narrow = apply(WidgetViews.build(app, EmptyWidgetState, WidgetViews.WideSize))
        assertFalse(narrow.visible(R.id.widget_title))
        assertEquals("Читалище Яворец", narrow.findViewById<View>(R.id.widget_logo).contentDescription)
        val medium = apply(WidgetViews.build(app, EmptyWidgetState, SizeF(300f, 100f)))
        assertEquals("Читалище", medium.text(R.id.widget_title))
        val full = apply(WidgetViews.build(app, EmptyWidgetState, WidgetViews.FullWideSize))
        assertEquals("Читалище Яворец", full.text(R.id.widget_title))
        assertEquals(null, full.findViewById<View>(R.id.widget_logo).contentDescription)
        // Където е избрано, името се събира до бутоните (без „…“) — на най-тясната ширина на размера.
        for ((size, wDp) in listOf(WidgetViews.FullWideSize to 345, SizeF(300f, 100f) to 300, WidgetViews.SmallSize to 150)) {
            val title = layOut(WidgetViews.build(app, signedInState(), size), wDp, 130).findViewById<TextView>(R.id.widget_title)
            assertEquals(0, title.layout.getEllipsisCount(0), "${title.text} в $wDp dp")
        }
    }

    @Test fun narrowWithLoansShowsOnlyDueDates() {
        // 3×2: в половин ширина датите не се събират до етикетите — само сроковете, на цяла ширина.
        val narrow = layOut(WidgetViews.build(app, signedInState(), WidgetViews.WideSize), 270, 140)
        assertFalse(narrow.visible(R.id.widget_event_column))
        val date = narrow.findViewById<TextView>(R.id.widget_group1_text)
        assertEquals("10.10", date.text.toString())
        assertTrue(date.layout.getEllipsisCount(0) == 0, "датата е отрязана")
        // Без заемания — събитието; по-широкият — и двете колони.
        assertTrue(apply(WidgetViews.build(app, signedOutState(), WidgetViews.WideSize)).visible(R.id.widget_event))
        assertTrue(apply(WidgetViews.build(app, signedInState(), SizeF(300f, 100f))).visible(R.id.widget_event_column))
    }

    @Test fun buttonsAreAtLeast48dpApart() {
        val density = app.resources.displayMetrics.density
        val v = layOut(WidgetViews.build(app, signedInState(), WidgetViews.FullWideSize), 360, 140)
        val scan = v.findViewById<View>(R.id.widget_scan)
        val catalog = v.findViewById<View>(R.id.widget_catalog)
        for (b in listOf(scan, catalog)) {
            assertTrue(b.height >= (48 * density).roundToInt(), "${b.height}")
            assertTrue(b.width >= (48 * density).roundToInt(), "${b.width}")
        }
        val gap = catalog.offsetIn(v).first - scan.offsetIn(v).first - scan.width
        assertTrue(gap >= (8 * density).roundToInt(), "$gap")
        // Полето за докосване започва от горния ръб; съдържанието е под него, не върху него.
        assertEquals(0, scan.offsetIn(v).second)
        assertTrue(v.findViewById<View>(R.id.widget_loans).offsetIn(v).second >= scan.height)
    }

    @Test fun sizesPickLargestThatFits() {
        assertEquals(WidgetViews.SmallTallSize, WidgetViews.pick(SizeF(180f, 180f)))
        assertEquals(WidgetViews.SmallSize, WidgetViews.pick(SizeF(180f, 120f)))
        assertEquals(WidgetViews.WideSize, WidgetViews.pick(SizeF(260f, 120f)))
        assertEquals(WidgetViews.WideTallSize, WidgetViews.pick(SizeF(260f, 200f)))
        assertEquals(WidgetViews.FullWideSize, WidgetViews.pick(SizeF(360f, 120f)))
        assertEquals(WidgetViews.FullWideTallSize, WidgetViews.pick(SizeF(360f, 200f)))
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

        // Ново копие: без вход и без събития (сайтът не отговаря) — не „Няма предстоящи събития“.
        ChitalishteWidgetReceiver().onUpdate(app, manager, intArrayOf(id))
        WidgetUpdater.awaitIdle()
        val empty = shadowOf(manager).getViewFor(id).texts()
        assertTrue(empty.none { "Няма предстоящи" in it }, empty.toString())
        backend.json("/javora/index.json", INDEX)
        assertIs<Outcome.Success<*>>(app.container.eventsRepository.events(force = true))
        WidgetUpdater.refresh(app)
        assertTrue("Литературна вечер" in shadowOf(manager).getViewFor(id).texts())

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
        assertTrue("Литературна вечер" in signedOut)
    }

    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))

    /** Влязъл читател със заемания в кеша и копие на уиджета (още непоказано). */
    private suspend fun signedInWidget(cipher: FlakyKeystoreCipher? = null): Int {
        val backend = TestBackend().start().also { backend = it }
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.loans(
            """{"loanId":"L1","inv":7,"title":"Под игото","author":"Иван Вазов","dateOut":"2026-10-01","dateDue":"2026-10-20"}""",
        )
        app.container = if (cipher != null) backend.container(app, clock, cipher) else backend.container(app, clock)
        assertIs<Outcome.Success<Unit>>(app.container.authRepository.login("0042", "1234".toCharArray(), remember = cipher != null))
        assertIs<Outcome.Success<*>>(app.container.libraryRepository.loans(force = true))
        val id = 7
        shadowOf(AppWidgetManager.getInstance(app)).bindAppWidgetId(id, ComponentName(app, ChitalishteWidgetReceiver::class.java))
        return id
    }

    private fun shown(id: Int) = shadowOf(AppWidgetManager.getInstance(app)).getViewFor(id).texts()

    @Test fun unreadableSessionKeepsShownLoans() = runBlocking<Unit> {
        val cipher = FlakyKeystoreCipher(broken = false)
        val id = signedInWidget(cipher)
        WidgetUpdater.refresh(app)
        assertTrue("след 3 дни" in shown(id))

        // Нов процес: запазената сесия временно не се чете (Keystore) — това не е изход.
        cipher.broken = true
        app.container = backend!!.container(app, clock, cipher)
        WidgetUpdater.refresh(app)
        WidgetUpdater.awaitIdle()
        assertIs<AuthState.Unavailable>(app.container.authRepository.state.value)
        assertTrue("след 3 дни" in shown(id), shown(id).toString())

        // Ново копие (няма какво да се запази): известното, без заемания — и не „Няма предстоящи събития“.
        ChitalishteWidgetReceiver().onUpdate(app, AppWidgetManager.getInstance(app), intArrayOf(id))
        WidgetUpdater.awaitIdle()
        val fresh = shown(id)
        assertTrue(fresh.none { "след" in it || "Няма предстоящи" in it }, fresh.toString())
    }

    @Test fun slowEventsDoNotHideLoans() = runBlocking<Unit> {
        val id = signedInWidget()
        // Събитията ги няма в кеша, а сайтът отговаря бавно — теглят се във фон.
        backend!!.routes["/javora/index.json"] = { MockResponse().setBody(INDEX).setBodyDelay(3, TimeUnit.SECONDS) }
        val started = System.nanoTime()
        WidgetUpdater.update(app, intArrayOf(id), fallback = true)
        assertTrue(System.nanoTime() - started < TimeUnit.SECONDS.toNanos(3))
        val first = shown(id)
        assertTrue("след 3 дни" in first, first.toString())
        assertTrue(first.none { "Няма предстоящи" in it }, first.toString())

        // Изтеглени във фон — веднъж (опресняването след тях не тегли отново); сроковете остават.
        WidgetUpdater.awaitIdle()
        assertEquals(listOf("Литературна вечер"), app.container.eventsRepository.cachedRolled()?.data?.map { it.title })
        assertEquals(1, backend!!.count("/javora/index.json"))
        assertTrue("след 3 дни" in shown(id), shown(id).toString())
    }

    /** Android 8–12: езикът на приложението е в SharedPreferences (AppLanguage). */
    private fun storeAppLanguage(tag: String) {
        app.getSharedPreferences("app_language", Context.MODE_PRIVATE).edit().putString("tag", tag).commit()
    }

    @Config(sdk = [30])
    @Test fun appLanguageAppliesToLabelsAndData() {
        storeAppLanguage("en")
        val defaults = LocaleList.getDefault()
        val text = AppLanguage.localized(app)
        // Без странични ефекти за процеса (за разлика от AppLanguage.wrap).
        assertEquals(defaults, LocaleList.getDefault())
        val loans = listOf(loan("a", "2026-10-20"), loan("b", "2026-10-20"), loan("c", "2026-10-10"))
        val events = listOf(Event(id = "e1", title = "Literary evening", date = "2026-10-18", time = "18:00", sourceUrl = "https://x/events"))
        val state = widgetState(text, events, loans, today)
        assertEquals("3 books to return", state.loansTotal)
        assertEquals(listOf("overdue", "in 3 days"), state.groups.map { it.countdown })
        assertEquals("Oct 20 · 2 books", state.groups[1].short)
        val texts = apply(WidgetViews.build(text, state, WidgetViews.FullWideSize)).texts()
        listOf("Chitalishte Yavorets", "Scan", "Next event", "October 18, 2026, 18:00", "in 3 days").forEach {
            assertTrue(it in texts, texts.toString())
        }
        assertTrue(texts.none { t -> t.any { it in 'а'..'я' } }, texts.toString())
    }

    @Config(sdk = [30])
    @Test fun languageChangeRefreshesWidget() = runBlocking<Unit> {
        val id = signedInWidget()
        backend!!.json("/javora/index.json", INDEX)
        WidgetUpdater.refresh(app)
        WidgetUpdater.awaitIdle()
        assertTrue("след 3 дни" in shown(id))

        // Същият език — нищо; сменен в настройките — уиджетът се опреснява на новия.
        WidgetUpdater.refreshIfLanguageChanged(app)
        WidgetUpdater.awaitIdle()
        assertTrue("след 3 дни" in shown(id))
        storeAppLanguage("en")
        WidgetUpdater.refreshIfLanguageChanged(app)
        WidgetUpdater.awaitIdle()
        val english = shown(id)
        assertTrue("in 3 days" in english, english.toString())
        assertTrue("Next event" in english, english.toString())
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

    @Test fun shotWideLight() = shot("90-widget-wide", WidgetViews.build(app, signedInState(), WidgetViews.FullWideTallSize), 360, 180)

    @Config(qualifiers = "+night")
    @Test fun shotWideDark() = shot("91-widget-wide-dark", WidgetViews.build(app, signedInState(), WidgetViews.FullWideSize), 360, 140)

    @Test fun shotSmall() = shot("92-widget-small", WidgetViews.build(app, signedInState(), WidgetViews.SmallSize), 160, 150)

    @Test fun shotSignedOut() = shot("93-widget-wide-signed-out", WidgetViews.build(app, signedOutState(), WidgetViews.FullWideTallSize), 360, 180)

    /** 3×2: до бутоните — само емблемата. */
    @Test fun shotNarrow() = shot("94-widget-narrow", WidgetViews.build(app, signedInState(), WidgetViews.WideSize), 270, 140)

    @Test fun shotSmallTallSignedOut() = shot("95-widget-small-tall", WidgetViews.build(app, signedOutState(), WidgetViews.SmallTallSize), 160, 200)

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

    private companion object {
        /** index.json на сайта с едно годишно събитие (18 октомври). */
        const val INDEX = """{"generated":"2026-10-01","docs":[{"id":"event-1","type":"event","title":"Литературна вечер","url":"/events","day":"18","monthNum":"10"}]}"""
    }
}
