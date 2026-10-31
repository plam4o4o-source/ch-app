package org.chyavorec.app.ui

import android.app.Application
import androidx.activity.ComponentDialog
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.BuildConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.AppShortcuts
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog
import java.time.Instant
import kotlin.test.assertEquals

/**
 * UI тестове на основните потоци върху истинския Compose UI (Robolectric), с
 * MockWebServer вместо chyavorec.org и GitHub.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class MainFlowsUiTest {
    @get:Rule val compose = createComposeRule()
    private val server = MockWebServer()

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/data/app-messages.json" -> MockResponse().setBody(
                    """[{"id":"msg-1","title":"Промяна в работното време","body":"От 1 октомври библиотеката работи до 17:00.",
                         "audience":"all","priority":"high","createdAt":"2026-09-25T08:00:00Z"},
                        {"id":"msg-2","title":"Общо събрание на членовете","body":"…","audience":"members",
                         "createdAt":"2026-09-24T08:00:00Z"}]""",
                )
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun launch(useMock: Boolean = false, deepLink: String? = null) {
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base,
            catalogUrls = listOf("$base/katalog.json"),
            inflibApiUrl = "",
            environment = "test",
            useMockData = useMock,
            versionName = "test",
            versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true), MutableStateFlow(deepLink), showIntro = false)
            }
        }
    }

    /** Кликва таб от долната навигация (изчаква анимираното ѝ появяване). */
    private fun clickTab(label: String) {
        val matcher = hasText(label) and isSelectable()
        runCatching { compose.waitUntil(10_000) { compose.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty() } }
            .onFailure { println(compose.onRoot(useUnmergedTree = true).printToString()); throw it }
        compose.onAllNodes(matcher).onFirst().performClick()
    }

    private fun clickText(label: String) {
        val matcher = hasText(label) and hasClickAction()
        compose.waitUntil(10_000) { compose.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(matcher).onFirst().performClick()
    }

    private fun waitForText(text: String, substring: Boolean = false) =
        compose.waitUntil(10_000) { compose.onAllNodes(hasText(text, substring = substring)).fetchSemanticsNodes().isNotEmpty() }

    @Test fun homeShowsLatestNewsFromSite() {
        launch()
        waitForText("45-ти общински фолклорен събор", substring = true)
        compose.onAllNodesWithText("45-ти общински фолклорен събор", substring = true).onFirst().assertExists()
    }

    /** Преходът между екраните да свърши: дотогава и полето за търсене на „Начало“ е в дървото. */
    private fun settle() {
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
    }

    @Test fun catalogSearchAndBookDetails() {
        launch()
        clickText("Каталог")
        waitForText("резултата", substring = true)
        settle()
        compose.onNode(hasSetTextAction()).performTextInput("Джиан")
        waitForText("\"Ох...\"")
        clickText("\"Ох...\"")
        waitForText("ЦБ/840/Д 51")
        compose.onNodeWithText("Сигнатура").assertExists()
        compose.onAllNodesWithText("Налична").onFirst().assertExists()
    }

    /** Този екземпляр не е на рафта, но 5 други от заглавието са (по каталога) — без „Попитай“/„Кажи ми“. */
    @Test fun bookOutWithOtherCopiesOnShelfSaysSo() {
        launch(deepLink = server.url("/").toString().trimEnd('/') + "/elektronen-katalog#zapis=624")
        // Етикетът на екземпляра и отделен зелен ред за другите — на един ред всеки.
        waitForText("Други 5 екземпляра са на рафта")
        compose.onAllNodesWithText("Не е на рафта").onFirst().assertExists()
        compose.onAllNodesWithText("Искам да прочета", substring = true).onFirst().assertExists()
        check(compose.onAllNodesWithText("Попитай библиотеката").fetchSemanticsNodes().isEmpty()) { "ask shown while copies are on the shelf" }
        check(compose.onAllNodesWithContentDescription("Кажи ми, когато е на рафта").fetchSemanticsNodes().isEmpty()) { "notify shown while copies are on the shelf" }
    }

    private fun lastBookShortcut() = ShortcutManagerCompat.getDynamicShortcuts(ApplicationProvider.getApplicationContext())
        .filter { it.id == AppShortcuts.ID_LAST_BOOK }

    /** Книга от публичния каталог (тук — App Link от сайта) става динамичен пряк път. */
    @Test fun publicBookBecomesTheLastBookShortcut() {
        launch(deepLink = server.url("/").toString().trimEnd('/') + "/elektronen-katalog#zapis=624")
        waitForText("Сигнатура")
        compose.waitUntil(10_000) { lastBookShortcut().isNotEmpty() }
        assertEquals(DeepLinks.catalogBook(624), LaunchIntents.linkOf(lastBookShortcut().single().intent, "https://chyavorec.org"))
    }

    /** Отворена като лична (известието за „Искам да прочета“) — книгата не става пряк път. */
    @Test fun personalBookIsNotAShortcut() {
        launch(deepLink = DeepLinks.book(624))
        waitForText("Сигнатура")
        compose.waitForIdle()
        check(lastBookShortcut().isEmpty()) { "personal book published as a shortcut" }
    }

    /**
     * Полето на „Начало“ се разгръща в търсенето (отделен прозорец над екрана, скенерът остава
     * в полето); резултатът отваря книгата и прибира търсенето.
     */
    @Test fun homeSearchBarExpandsAndOpensResult() {
        launch()
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasContentDescription("Сканирай баркод")).onFirst().assertExists()
        compose.onAllNodes(hasSetTextAction()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 2 }
        compose.onAllNodes(hasSetTextAction()).onLast().performTextInput("Джиан")
        compose.waitUntil(10_000) { compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).fetchSemanticsNodes().isNotEmpty() }
        // Последният — в прозореца на търсенето (под него „Начало“ може да показва същата книга).
        compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).onLast().performClick()
        waitForText("Сигнатура")
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
    }

    /** „Назад“ в разгънатото поле го прибира обратно на „Начало“ — без старото търсене (с подсказката). */
    @Test fun homeSearchBarCollapsesWithBack() {
        launch()
        searchFor("Джиан")
        compose.onAllNodes(hasContentDescription("Назад") and hasClickAction()).onLast().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
        waitForText("45-ти общински фолклорен събор", substring = true)
        assertSearchBarCleared()
        // Със системното „Назад“ (и жеста) — също.
        searchFor("Джиан")
        systemBackInSearch()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
        assertSearchBarCleared()
    }

    /**
     * „Назад“ от отворен резултат връща разгънатото търсене с резултатите — без фокус в полето
     * (иначе клавиатурата ги закрива), и едно „Назад“ го прибира.
     */
    @Test fun homeSearchBarReturnsToResultsWithoutKeyboard() {
        launch()
        searchFor("Джиан")
        compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).onLast().performClick()
        waitForText("Сигнатура")
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
        settle()
        compose.onAllNodes(hasContentDescription("Назад") and hasClickAction()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 2 }
        settle()
        compose.waitUntil(10_000) { compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasSetTextAction()).onLast().assertIsNotFocused()
        systemBackInSearch()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
        assertSearchBarCleared()
    }

    /** Резултат → друг раздел → „Начало“: търсенето не се разгръща отново и полето е празно. */
    @Test fun homeSearchBarDoesNotReopenAfterAnotherTab() {
        launch()
        searchFor("Джиан")
        // Авторът (не книгата с него) — отваря каталога, т.е. друг раздел.
        val author = hasText("Джиан, Филип") and hasClickAction() and !hasText("\"Ох...\"", substring = true)
        compose.waitUntil(10_000) { compose.onAllNodes(author).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(author).onLast().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 1 }
        settle()
        clickTab("Начало")
        settle()
        waitForText("45-ти общински фолклорен събор", substring = true)
        assertEquals(1, compose.onAllNodes(isRoot()).fetchSemanticsNodes().size, "търсенето не се разгръща")
        assertSearchBarCleared()
    }

    /** Разгръща полето на „Начало“ и търси [query]. */
    private fun searchFor(query: String) {
        compose.waitUntil(10_000) { compose.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasSetTextAction()).onFirst().performClick()
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 2 }
        compose.onAllNodes(hasSetTextAction()).onLast().performTextInput(query)
        compose.waitUntil(10_000) { compose.onAllNodes(hasText("\"Ох...\"") and hasClickAction()).fetchSemanticsNodes().isNotEmpty() }
    }

    /** Системното „Назад“ в прозореца на разгънатото търсене. */
    private fun systemBackInSearch() {
        compose.runOnUiThread { (ShadowDialog.getLatestDialog() as ComponentDialog).onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
    }

    /** Прибраното поле е празно — с подсказката „Търси книга…“, не със старото търсене. */
    private fun assertSearchBarCleared() {
        settle()
        val field = compose.onAllNodes(hasSetTextAction()).onFirst().fetchSemanticsNode()
        assertEquals("", field.config.getOrNull(SemanticsProperties.EditableText)?.text.orEmpty(), "текстът в полето")
        compose.onAllNodes(hasText("Търси книга", substring = true), useUnmergedTree = true).onFirst().assertExists()
    }

    @Test fun guestSeesHonestLoginMessageAndCanAddCard() {
        launch(useMock = false)
        clickText("Читателска карта")
        waitForText("Дигитална карта")
        settle()
        compose.onAllNodes(hasSetTextAction()).onFirst().performTextInput("r-0042")
        clickText("Запази")
        waitForText("Читателски № R-0042")
        compose.onNodeWithText("Покажи картата").performScrollTo().assertExists()
    }

    @Test fun bottomNavigationShowsAllTabs() {
        launch()
        waitForText("45-ти общински фолклорен събор", substring = true)
        clickTab("Моето")
        waitForText("Здравей!")
        waitForText("Онлайн входът още не е включен", substring = true)
    }

    @Test fun demoLoginShowsLoansWithDueIndicators() {
        assumeTrue("демо данните съществуват само в dev flavor", BuildConfig.FLAVOR == "dev")
        launch(useMock = true)
        clickText("Читателска карта")
        clickText("Вход с читателски данни")
        waitForText("Читателски номер")
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("DEMO-0001")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("demo")
        clickText("Вход")
        waitForText("Демо Читател")
        clickText("Моите книги")
        waitForText("Под игото")
        waitForText("остават 7 дни")
        waitForText("просрочена с 3 дни")
    }

    @Test fun guestSeesPublicMessagesButNotMembersOnly() {
        launch(deepLink = "messages")
        waitForText("Промяна в работното време")
        compose.onNodeWithText("От 1 октомври библиотеката работи до 17:00.").assertExists()
        waitForText("Някои съобщения са само за членове", substring = true)
        compose.onAllNodesWithText("Общо събрание на членовете").fetchSemanticsNodes().let { check(it.isEmpty()) { "members-only message shown to a guest" } }
    }
}
