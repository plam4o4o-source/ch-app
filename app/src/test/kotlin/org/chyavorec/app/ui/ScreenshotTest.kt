package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.screens.my.LoanCard
import org.chyavorec.app.ui.screens.my.LoanDueCard
import org.chyavorec.app.ui.screens.my.ReadingStatsCard
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.core.FixedClock
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.LoanSummary
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth

/**
 * Снимки на основните екрани (светла и тъмна тема) за визуален преглед.
 * Записват се в app/build/reports/screenshots/ — CI ги качва в артефакта „reports“.
 * Тестът не сравнява пиксели; проверява, че екраните се рисуват и показват основното си
 * съдържание ([shown]) — счупен екран проваля теста, а не остава само като снимка.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1100dp-xhdpi")
class ScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Въздвижение на Светия Кръст Господен. **Кръстовден**"}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(
        mode: ThemeMode = ThemeMode.LIGHT,
        settings: AppSettings = AppSettings(onboardingDone = true, introShown = true),
        now: String = "2026-09-26T10:00:00Z",
        inflibApiUrl: String = "",
        deepLink: String? = null,
    ) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = inflibApiUrl,
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse(now)), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                ChitalishteRoot(container, settings, MutableStateFlow(deepLink), showIntro = false)
            }
        }
    }

    private fun waitFor(text: String, substring: Boolean = true) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }

    private fun tab(label: String) {
        val m = hasText(label) and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(m).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(m).onFirst().performClick()
    }

    /** Поне един възел с [text] е на екрана (видим, не само в дървото). */
    private fun shown(text: String, substring: Boolean = false) {
        // След преходите (иначе първият намерен може да е от излизащия екран).
        compose.waitForIdle()
        val nodes = compose.onAllNodes(hasText(text, substring = substring))
        val count = nodes.fetchSemanticsNodes().size
        // Някой от съвпадащите (напр. скрито описание за екранния четец не се брои).
        val visible = (0 until count).any { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }
        if (!visible) throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения в дървото)")
    }

    /** Разделът [label] в долната лента е избран. */
    private fun tabSelected(label: String) = compose.onAllNodes(hasText(label) and isSelectable()).onFirst().assertIsSelected()

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun homeLight() {
        launch()
        waitFor("45-ти")
        homeContent()
        shot("01-home-light")
    }

    /** „Начало“: заглавката с името на читалището, новината от сайта, днешният ден, лентата. */
    private fun homeContent() {
        shown("Васил Левски – 1922", substring = true)
        shown("45-ти", substring = true)
        waitFor("Кръстовден")
        shown("Кръстовден", substring = true)
        tabSelected("Начало")
    }

    // Празнични дни: поздрав под заглавката, орнамент и ефект (вж. HolidayCalendar).
    @Test fun homeMay24() {
        launch(now = "2026-05-24T09:00:00Z")
        waitFor("Кирил и Методий")
        shown("Кирил и Методий", substring = true)
        shot("20-home-may24")
    }

    @Test fun homeLevskiQuietDark() {
        launch(ThemeMode.DARK, now = "2026-02-19T09:00:00Z")
        waitFor("Поклон пред Апостола")
        shown("Поклон пред Апостола", substring = true)
        shot("21-home-levski-quiet")
    }

    @Test fun homeBabaMarta() {
        launch(now = "2026-03-01T09:00:00Z")
        waitFor("Баба Марта")
        shown("Баба Марта", substring = true)
        // Празникът за деня е ред в поздрава (не отделен ред под търсенето); снимката — със заредената въртележка.
        waitFor("Кръстовден")
        waitFor("45-ти")
        shot("22-home-baba-marta")
    }

    @Test fun homeDark() {
        launch(ThemeMode.DARK)
        waitFor("45-ти")
        homeContent()
        shot("02-home-dark")
    }

    @Test fun catalog() {
        launch()
        tab("Каталог")
        waitFor("резултата")
        tabSelected("Каталог")
        shown("60 резултата", substring = true)
        // Първата книга от примерния каталог (по заглавие).
        shown("Джиан, Филип", substring = true)
        shot("03-catalog")
    }

    /**
     * Телефон хоризонтално (ниска височина): заглавката е ниска, търсенето и полетата са на един ред
     * и под тях се виждат заглавията на поне три резултата (преди — около резултат и половина).
     */
    @Config(qualifiers = "bg-w891dp-h411dp-land-xxhdpi")
    @Test fun catalogLandscape() {
        launch()
        tab("Каталог")
        waitFor("резултата")
        shown("60 резултата", substring = true)
        compose.waitForIdle()
        val bottom = compose.onRoot().fetchSemanticsNode().boundsInRoot.bottom
        // Първите три книги от примерния каталог (по заглавие) — изцяло над долния ръб.
        listOf("Ох...", "12 -те вятъра", "66 градуса Северна ширина").forEach { title ->
            val nodes = compose.onAllNodes(hasText(title, substring = true), useUnmergedTree = true).fetchSemanticsNodes()
            if (nodes.none { it.boundsInRoot.bottom <= bottom }) throw AssertionError("„$title“ не се вижда изцяло на екрана")
        }
        shot("70-catalog-landscape")
    }

    @Test fun my() {
        launch()
        tab("Моето")
        waitFor("Добре дошъл!")
        tabSelected("Моето")
        shown("Добре дошъл!")
        shown("Читателска карта")
        shown("Моите книги")
        shot("04-my")
    }

    /** Мостът (InvLib API) е недостъпен — без връзка: „Опитай отново“, а не „входът не е включен“. */
    private val unreachableApi = "https://127.0.0.1:1/api/invlib/yavorec"

    @Test fun loginOffline() {
        launch(inflibApiUrl = unreachableApi, deepLink = "login")
        waitFor("Няма връзка със сървъра на библиотеката")
        shown("Няма връзка със сървъра на библиотеката", substring = true)
        shown("Опитай отново")
        if (compose.onAllNodes(hasText("Онлайн входът още не е включен", substring = true)).fetchSemanticsNodes().isNotEmpty()) {
            throw AssertionError("без връзка входът е показан като „не е включен“")
        }
        shot("13-login-offline")
    }

    @Test fun myOffline() {
        launch(inflibApiUrl = unreachableApi)
        tab("Моето")
        // Кратка бележка (лентата „Няма интернет връзка“ казва останалото), а картата се добавя и без връзка.
        waitFor("Не можем да проверим дали онлайн входът е включен")
        shown("Добре дошъл!")
        shown("Опитай отново")
        shown("Добави читателска карта")
        shot("14-my-offline")
    }

    @Test fun more() {
        launch()
        tab("Още")
        waitFor("Настройки")
        tabSelected("Още")
        shown("Съобщения")
        shown("Настройки")
        shown("Политика за поверителност")
        shot("05-more")
    }

    @Test fun emptyMessages() {
        launch()
        tab("Още")
        waitFor("Съобщения")
        compose.onAllNodes(hasText("Съобщения")).onFirst().performClick()
        waitFor("Няма съобщения")
        shown("Няма съобщения", substring = true)
        shown("само за членове", substring = true)
        shot("06-messages-empty")
    }

    // „За теб“ за читател без вход: „Най-четени и нови“ от примерния каталог.
    @Test fun homeForYou() {
        launch()
        waitFor("45-ти")
        // Полицата е под първия екран (LazyColumn не я композира, докато не се превърти до нея)
        // и се изчислява след първия кадър — превърта се, щом се появи в списъка.
        compose.waitUntil(15_000) {
            runCatching { compose.onAllNodes(hasScrollToNodeAction()).onFirst().performScrollToNode(hasText("За теб")) }.isSuccess
        }
        // Началото може още да се допълни над полицата (поздрав, новини от мрежата) и да я
        // избута надолу — превърта се отново, докато след покой остане на екрана.
        for (attempt in 1..5) {
            compose.waitForIdle()
            if (runCatching { compose.onAllNodes(hasText("За теб")).onFirst().assertIsDisplayed() }.isSuccess) break
            runCatching { compose.onAllNodes(hasScrollToNodeAction()).onFirst().performScrollToNode(hasText("За теб")) }
        }
        shown("За теб")
        shot("08-home-for-you")
    }

    /** „Моето четене“ с примерна история (графиките, както са в „Моето“ за влязъл читател). */
    private fun readingCard(mode: ThemeMode, name: String) {
        val months = listOf(1, 2, 0, 3, 4, 2, 1, 0, 2, 3, 5, 2).mapIndexed { i, n ->
            ReadingStats.MonthCount(YearMonth.of(2025, 11).plusMonths(i.toLong()), n)
        }
        val stats = ReadingStats.Summary(
            months = months,
            sections = listOf(8 to 14, 9 to 5, 3 to 3, 1 to 2, 7 to 1, null to 2).map { (s, n) -> ReadingStats.SectionCount(s, n) },
            thisYear = 22, favouriteAuthor = "Вазов, Иван", favouriteAuthorCount = 5, longestStreak = 6, total = 27,
        )
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(vertical = 16.dp)) {
                    SectionLabel("Моето четене", Modifier.padding(start = 20.dp, bottom = 12.dp))
                    ReadingStatsCard(stats, onOpenHistory = {})
                }
            }
        }
        shown("Книги по месеци")
        // Връзката към цялата история и любимият автор (графиките са за екранния четец с описание).
        shown("Цялата история")
        shown("Вазов", substring = true)
        shot(name)
    }

    @Test fun myReadingLight() = readingCard(ThemeMode.LIGHT, "09-my-reading-light")

    @Test fun myReadingDark() = readingCard(ThemeMode.DARK, "10-my-reading-dark")

    /**
     * Картата „Следващ срок / Просрочено“ от „Начало“ и картите в „Моите книги“ с примерни
     * заемания (production няма демо вход, затова — самите компоненти, както на екраните).
     */
    private fun loans(mode: ThemeMode, name: String) {
        val today = LocalDate.of(2026, 10, 17)
        val list = listOf(
            Loan("1", 101, "Под игото", "Иван Вазов", borrowedOn = "2026-09-23", dueOn = "2026-10-21", renewals = 1, canRenew = true),
            Loan("2", 102, "Тютюн", "Димитър Димов", borrowedOn = "2026-09-01", dueOn = "2026-10-12", renewals = 2, canRenew = false),
            Loan("3", 103, "Бай Ганьо", "Алеко Константинов", borrowedOn = "2026-10-10", dueOn = "2026-11-07", canRenew = true),
        )
        val calc = LoanDueCalculator()
        compose.setContent {
            ChitalishteTheme(mode = mode) {
                Column(
                    Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SectionLabel("Начало", Modifier.padding(start = 20.dp))
                    LoanDueCard(LoanSummary.of(list.drop(2) + list.first(), today)!!, onClick = {}, modifier = Modifier.padding(horizontal = 16.dp))
                    LoanDueCard(LoanSummary.of(list, today)!!, onClick = {}, modifier = Modifier.padding(horizontal = 16.dp))
                    SectionLabel("Моите книги", Modifier.padding(start = 20.dp, top = 8.dp))
                    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        list.forEachIndexed { i, loan ->
                            LoanCard(
                                loan, calc, today, stale = false, serverRenews = true,
                                renewing = i == 2, onRenew = {}, onOpen = {},
                            )
                        }
                    }
                }
            }
        }
        listOf("Под игото", "Тютюн", "Бай Ганьо").forEach { shown(it) }
        // Просрочената книга е отбелязана (картата на „Начало“ и картата на заемането).
        shown("просрочена", substring = true)
        shot(name)
    }

    @Test fun loansLight() = loans(ThemeMode.LIGHT, "11-loans-light")

    @Test fun loansDark() = loans(ThemeMode.DARK, "12-loans-dark")

    @Test fun onboarding() {
        launch(settings = AppSettings(onboardingDone = false, introShown = true))
        waitFor("Напред")
        shown("Напред")
        shown("Пропусни")
        shown("Читалище Яворец")
        shot("07-onboarding")
    }

    /** Настройки → Информация → „Диагностика“ (локалният дневник на грешките). */
    @Test fun diagnostics() {
        launch()
        tab("Още")
        waitFor("Настройки")
        compose.onAllNodes(hasText("Настройки")).onFirst().performClick()
        waitFor("Диагностика")
        compose.onAllNodes(hasText("Диагностика")).onFirst().performScrollTo().performClick()
        waitFor("Сподели")
        shown("Диагностика")
        shown("само на устройството", substring = true)
        shown("Сподели")
        shown("Изчисти")
        shot("13-diagnostics")
    }
}
