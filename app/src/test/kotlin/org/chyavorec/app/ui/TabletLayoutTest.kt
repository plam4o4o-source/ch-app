package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.chyavorec.app.AppConfig
import org.chyavorec.app.TestResources
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.data.local.WantToReadEntity
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.KeyCommand
import org.chyavorec.app.ui.navigation.KeyboardShortcuts
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals

/**
 * Таблет хоризонтално (1280×800 dp): списък и детайл един до друг — каталог › книга, новини ›
 * статия, „Искам да прочета“ › книга; „Назад“ (и Esc) първо затваря детайла; връзката отваря
 * детайла до списъка; изборът оцелява възстановяване на състоянието. И клавишните комбинации.
 * Снимките се записват до тези на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w1280dp-h800dp-land-hdpi")
class TabletLayoutTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val server = MockWebServer()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val keys = MutableSharedFlow<KeyCommand>(extraBufferCapacity = 4)

    @Before fun start() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path?.substringBefore('?')) {
                "/data/news.json" -> MockResponse().setBody(TestResources.text("site-news.json"))
                "/rss.xml" -> MockResponse().setBody(TestResources.text("site-rss.xml"))
                "/javora/index.json" -> MockResponse().setBody(TestResources.text("site-index.json"))
                "/katalog.json" -> MockResponse().setBody(TestResources.text("katalog-sample.json"))
                "/api/calendar" -> MockResponse().setBody("""{"date":"2026-9-26","line":"Преставяне на св. апостол и евангелист **Йоан Богослов**"}""")
                "/data/app-messages.json" -> MockResponse().setBody("[]")
                else -> MockResponse().setResponseCode(404)
            }
        }
        server.start()
    }

    @After fun stop() = server.shutdown()

    private fun container(): AppContainer {
        // С връзка — без лентата „Няма интернет връзка“.
        val cm = ApplicationProvider.getApplicationContext<Application>().getSystemService(ConnectivityManager::class.java)
        val caps = ShadowNetworkCapabilities.newInstance()
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        shadowOf(caps).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        shadowOf(cm).setNetworkCapabilities(cm.activeNetwork, caps)
        val base = server.url("/").toString().trimEnd('/')
        val config = AppConfig(
            siteBaseUrl = base, catalogUrls = listOf("$base/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        return AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
    }

    private fun launch(deepLink: String? = null, wantToRead: Boolean = false) {
        val container = container()
        if (wantToRead) seedWantToRead(container)
        compose.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(deepLink), showIntro = false, keyCommands = keys)
            }
        }
    }

    private fun seedWantToRead(container: AppContainer) = runBlocking {
        val dao = container.database.wantToRead()
        dao.clear()
        val t = Instant.parse("2026-09-26T10:00:00Z").toEpochMilli()
        dao.upsert(WantToReadEntity(624, "Белият дома", "Николай Симеонов", t - 1_000, notifyWhenAvailable = true, lastKnownAvailable = false))
        dao.upsert(WantToReadEntity(7, "Под игото", "Иван Вазов", t - 2_000, notifyWhenAvailable = false, lastKnownAvailable = false))
    }

    private fun waitFor(text: String) =
        compose.waitUntil(15_000) { compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty() }

    private fun settle() {
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
    }

    /** Поне един възел с [text] е на екрана (видим, не само в дървото). */
    private fun shown(text: String) {
        settle()
        val nodes = compose.onAllNodes(hasText(text, substring = true))
        val count = nodes.fetchSemanticsNodes().size
        val visible = (0 until count).any { runCatching { nodes[it].assertIsDisplayed() }.isSuccess }
        if (!visible) throw AssertionError("„$text“ не се вижда на екрана ($count съвпадения в дървото)")
    }

    private fun gone(text: String) {
        settle()
        val count = compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().size
        assertEquals(0, count, "„$text“ е още на екрана")
    }

    private fun click(text: String) {
        val m = hasText(text, substring = true) and hasClickAction()
        compose.waitUntil(15_000) { compose.onAllNodes(m).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(m).onFirst().performClick()
    }

    private fun tab(label: String): SemanticsNodeInteraction {
        val m = hasText(label) and isSelectable()
        compose.waitUntil(10_000) { compose.onAllNodes(m).fetchSemanticsNodes().isNotEmpty() }
        return compose.onAllNodes(m).onFirst()
    }

    /** Страничната лента се вижда само на раздел (не над екран върху него) — значи детайлът е до списъка. */
    private fun onTab(label: String) {
        settle()
        tab(label).assertIsDisplayed().assertIsSelected()
    }

    private fun back() {
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        settle()
    }

    /** Клавиш от физическа клавиатура — през активността, както в MainActivity. */
    private fun press(code: Int, meta: Int = 0) {
        compose.runOnUiThread {
            val activity = compose.activity
            for (action in listOf(KeyEvent.ACTION_DOWN, KeyEvent.ACTION_UP)) {
                KeyboardShortcuts.dispatch(KeyEvent(0L, 0L, action, code, 0, meta), activity::dispatchKeyEvent) { c ->
                    if (c == KeyCommand.Back) activity.onBackPressedDispatcher.onBackPressed() else keys.tryEmit(c)
                }
            }
        }
        settle()
    }

    private val ctrl = KeyEvent.META_CTRL_ON or KeyEvent.META_CTRL_LEFT_ON

    private fun shot(name: String) {
        settle()
        compose.mainClock.advanceTimeBy(500)
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Каталогът: търсене → книга до резултатите. */
    private fun openCatalogBook() {
        tab("Каталог").performClick()
        waitFor("резултата")
        settle()
        compose.onAllNodes(hasSetTextAction()).onFirst().performTextInput("Джиан")
        click("\"Ох...\"")
        waitFor("Сигнатура")
    }

    @Test fun catalogBookOpensBesideTheResults() {
        launch()
        openCatalogBook()
        shown("Сигнатура")
        shown("\"Ох...\"")
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsDisplayed()
        onTab("Каталог")
        shot("86-tablet-catalog-book")
        // „Назад“ — първо детайлът, после разделът.
        back()
        gone("Сигнатура")
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsDisplayed()
        onTab("Каталог")
        back()
        onTab("Начало")
    }

    /** „Още“ на таблет: плочките вляво (двете групи), отдясно — подсказка, докато няма избран раздел. */
    @Test fun moreTilesOnTablet() {
        launch()
        tab("Още").performClick()
        waitFor("Познай Яворец")
        onTab("Още")
        shot("86b-tablet-more")
    }

    @Test fun newsArticleOpensBesideTheList() {
        launch()
        tab("Новини").performClick()
        click("45-ти общински фолклорен събор")
        compose.waitUntil(15_000) { compose.onAllNodes(hasContentDescription("Отвори в сайта")).fetchSemanticsNodes().isNotEmpty() }
        // Списъкът (с полето за търсене в новините) остава отляво.
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsDisplayed()
        onTab("Новини")
        shot("87-tablet-news-article")
        back()
        compose.waitUntil(10_000) { compose.onAllNodes(hasContentDescription("Отвори в сайта")).fetchSemanticsNodes().isEmpty() }
        onTab("Новини")
    }

    @Test fun wantToReadBookOpensBesideTheList() {
        launch(deepLink = DeepLinks.WANT_TO_READ, wantToRead = true)
        waitFor("Под игото")
        click("Белият дома")
        waitFor("Други 5 екземпляра са на рафта")
        shown("Други 5 екземпляра са на рафта")
        shown("Под игото")
        shot("88-tablet-want-to-read-book")
        back()
        gone("Други 5 екземпляра са на рафта")
        shown("Под игото")
        back()
        onTab("Моето")
    }

    /** Известието „вече е на рафта“ на таблет: списъкът отляво, книгата отдясно. */
    @Test fun bookNotificationOpensWithTheListOnTheLeft() {
        launch(deepLink = DeepLinks.book(624), wantToRead = true)
        waitFor("Други 5 екземпляра са на рафта")
        shown("Под игото")
        shown("Други 5 екземпляра са на рафта")
    }

    /** App Link към книга от каталога и известие за новина: детайлът — до списъка, в раздела. */
    @Test fun deepLinksOpenTheDetailBesideTheList() {
        launch(deepLink = server.url("/").toString().trimEnd('/') + "/elektronen-katalog#zapis=624")
        waitFor("Сигнатура")
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsDisplayed()
        onTab("Каталог")
        shot("89-tablet-deeplink-book")
    }

    @Test fun articleNotificationOpensBesideTheNewsList() {
        launch(deepLink = DeepLinks.article("news-mtepl1bvlvop"))
        compose.waitUntil(15_000) { compose.onAllNodes(hasContentDescription("Отвори в сайта")).fetchSemanticsNodes().isNotEmpty() }
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsDisplayed()
        onTab("Новини")
    }

    /** Изборът е в състоянието на списъка — оцелява пресъздаване (завъртане, спрян процес). */
    @Test fun selectionSurvivesStateRestoration() {
        val restoration = StateRestorationTester(compose)
        val container = container()
        restoration.setContent {
            ChitalishteTheme {
                ChitalishteRoot(container, AppSettings(onboardingDone = true, introShown = true), MutableStateFlow(null), showIntro = false)
            }
        }
        openCatalogBook()
        restoration.emulateSavedInstanceStateRestore()
        waitFor("Сигнатура")
        shown("Сигнатура")
        onTab("Каталог")
    }

    // --- Клавиатура ---

    @Test fun ctrlDigitsSwitchTabs() {
        launch()
        waitFor("45-ти")
        press(KeyEvent.KEYCODE_2, ctrl)
        onTab("Каталог")
        press(KeyEvent.KEYCODE_4, ctrl)
        onTab("Новини")
        press(KeyEvent.KEYCODE_1, ctrl)
        onTab("Начало")
    }

    @Test fun ctrlFFocusesTheCatalogSearch() {
        launch()
        press(KeyEvent.KEYCODE_2, ctrl)
        waitFor("резултата")
        press(KeyEvent.KEYCODE_F, ctrl)
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsFocused()
        onTab("Каталог")
    }

    @Test fun slashOpensTheHomeSearch() {
        launch()
        waitFor("45-ти")
        press(KeyEvent.KEYCODE_SLASH)
        // Разгънатото търсене — отделен прозорец над „Начало“.
        compose.waitUntil(10_000) { compose.onAllNodes(isRoot()).fetchSemanticsNodes().size == 2 }
    }

    /** Раздел без собствено поле („Моето“) — екранът „Търсене“. */
    @Test fun ctrlFElsewhereOpensTheSearchScreen() {
        launch()
        press(KeyEvent.KEYCODE_3, ctrl)
        onTab("Моето")
        press(KeyEvent.KEYCODE_F, ctrl)
        compose.waitUntil(10_000) { compose.onAllNodes(hasText("Моето") and isSelectable()).fetchSemanticsNodes().isEmpty() }
        compose.onAllNodes(hasSetTextAction()).onFirst().assertIsFocused()
    }

    /** Esc — като „Назад“ (книгата е от връзка: без фокус в полето, от което Esc първо би излязъл). */
    @Test fun escapeClosesTheDetailThenTheList() {
        launch(deepLink = server.url("/").toString().trimEnd('/') + "/elektronen-katalog#zapis=624")
        waitFor("Сигнатура")
        onTab("Каталог")
        press(KeyEvent.KEYCODE_ESCAPE)
        gone("Сигнатура")
        onTab("Каталог")
        press(KeyEvent.KEYCODE_ESCAPE)
        onTab("Начало")
    }

    /** „Още“ на таблет: разделът се отваря отдясно на плочките; „Назад“ първо го затваря. */
    @Test fun moreSectionOpensBesideTheTiles() {
        launch()
        tab("Още").performClick()
        waitFor("Познай Яворец")
        shown("Избери раздел вляво")
        click("Настройки")
        waitFor("Сезонни украси")
        onTab("Още")
        shown("Познай Яворец")
        gone("Избери раздел вляво")
        shot("w23-tablet-more-settings")
        back()
        gone("Сезонни украси")
        onTab("Още")
        shown("Избери раздел вляво")
    }

    /** „Моето“ на таблет: същото — „Известия“ отдясно на плочките. */
    @Test fun mySectionOpensBesideTheTiles() {
        launch()
        tab("Моето").performClick()
        waitFor("Известия")
        shown("Избери раздел вляво")
        shot("w23-tablet-my-placeholder")
        click("Известия")
        waitFor("Съобщения, изпратени от читалището")
        onTab("Моето")
        gone("Избери раздел вляво")
        shot("w23-tablet-my-notifications")
        back()
        onTab("Моето")
        shown("Избери раздел вляво")
    }
}
