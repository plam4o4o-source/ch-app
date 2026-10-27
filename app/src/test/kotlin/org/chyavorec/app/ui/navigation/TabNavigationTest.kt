package org.chyavorec.app.ui.navigation

import android.app.Application
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Навигацията по раздели и връзките от известия върху истински NavController (Robolectric):
 * връзката не дублира екрани и не „възкресява“ стария подстек на раздела, а „Назад“ води
 * през екраните на връзката към раздела и после към началото.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class TabNavigationTest {
    @get:Rule val compose = createComposeRule()
    private val base = "https://chyavorec.org"
    private lateinit var nav: NavHostController

    private fun start() {
        compose.setContent {
            val controller = rememberNavController()
            nav = controller
            NavHost(
                controller, startDestination = Routes.HOME,
                enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None },
            ) {
                listOf(
                    Routes.HOME, Routes.CATALOG, Routes.MY, Routes.NEWS, Routes.MORE,
                    Routes.LOANS, Routes.CARD, Routes.HISTORY, Routes.LOGIN, Routes.EVENTS, Routes.MESSAGES,
                    Routes.ARTICLE, Routes.EVENT, Routes.MESSAGE,
                ).forEach { r -> composable(r) { Text(r) } }
            }
        }
        compose.waitForIdle()
    }

    private fun act(block: NavHostController.() -> Unit) {
        compose.runOnIdle { nav.block() }
        compose.waitForIdle()
    }

    private fun link(link: String) = act { openLinkTarget(DeepLinks.target(link, base) as LinkTarget.Screen) }

    /** „article/{id}“ → „article/А“ — за да се различават екраните с различни аргументи. */
    private fun NavBackStackEntry.label(): String {
        var r = destination.route.orEmpty()
        Regex("\\{(\\w+)\\}").findAll(r).forEach { m -> r = r.replace(m.value, arguments?.getString(m.groupValues[1]).orEmpty()) }
        return r
    }

    private fun stack(): List<String> = compose.runOnIdle {
        nav.currentBackStack.value.filter { it.destination !is NavGraph }.map { it.label() }
    }

    /** Поредицата от екрани при последователни „Назад“ (докато има накъде). */
    private fun backOrder(): List<String> {
        val seen = mutableListOf(stack().last())
        while (compose.runOnIdle { nav.popBackStack() }) {
            compose.waitForIdle()
            seen += stack().lastOrNull() ?: break
        }
        return seen
    }

    @Test fun loanNotificationWithMyTabLeftOnLoansHasNoDuplicate() {
        start()
        act { navigateTab(Routes.MY); navigate(Routes.LOANS) }
        act { navigateTab(Routes.HOME) }
        link("my/loans")
        assertEquals(listOf("home", "my", "loans"), stack())
        assertEquals(listOf("loans", "my", "home"), backOrder())
    }

    @Test fun loanNotificationWhileOnLoansHasNoDuplicate() {
        start()
        act { navigateTab(Routes.MY); navigate(Routes.LOANS) }
        link("my/loans")
        assertEquals(listOf("home", "my", "loans"), stack())
        assertEquals(listOf("loans", "my", "home"), backOrder())
    }

    @Test fun newsNotificationReplacesTheArticleLeftOpen() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        link(DeepLinks.article("B"))
        assertEquals(listOf("home", "news", "article/B"), stack())
        assertEquals(listOf("article/B", "news", "home"), backOrder())
    }

    @Test fun newsNotificationFromAnotherTabDoesNotRestoreTheOldArticle() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        act { navigateTab(Routes.CATALOG) }
        link(DeepLinks.article("B"))
        assertEquals(listOf("home", "news", "article/B"), stack())
    }

    @Test fun eventNotificationOverAnotherOpenEvent() {
        start()
        link(DeepLinks.event("A"))
        link(DeepLinks.event("B"))
        assertEquals(listOf("home", "news", "event/B"), stack())
        assertEquals(listOf("event/B", "news", "home"), backOrder())
    }

    /** Връзка към събитията оставя на раздела искане за сегмента „Календар“ (в неговото състояние). */
    @Test fun eventsLinkAsksTheNewsTabForTheCalendarSegment() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        act { navigateTab(Routes.HOME) }
        link(DeepLinks.EVENTS)
        assertEquals(listOf("home", "news"), stack())
        val segment = compose.runOnIdle { nav.getBackStackEntry(Routes.NEWS).savedStateHandle.get<String>(SEGMENT_KEY) }
        assertEquals(Routes.SEGMENT_CALENDAR, segment)
    }

    private fun segmentOf(tab: String): String? = compose.runOnIdle { nav.getBackStackEntry(tab).savedStateHandle.get<String>(SEGMENT_KEY) }

    private fun siteLink(url: String) = act { openSiteLink(DeepLinks.forUrl(url, base)!!) }

    /** `/events` от статия в „Новини“: календарът, не възстановеният стек НОВИНИ › СТАТИЯ. */
    @Test fun eventsSiteLinkFromAnArticleOpensTheCalendar() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        siteLink("$base/events")
        assertEquals(listOf("home", "news"), stack())
        assertEquals(Routes.SEGMENT_CALENDAR, segmentOf(Routes.NEWS))
    }

    /** `/events` от друг раздел: старата статия, оставена в „Новини“, не изскача вместо календара. */
    @Test fun eventsSiteLinkFromAnotherTabDoesNotRestoreTheOldArticle() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        act { navigateTab(Routes.MORE) }
        siteLink("$base/events")
        assertEquals(listOf("home", "news"), stack())
        assertEquals(listOf("news", "home"), backOrder())
    }

    /** Новина от съдържанието — над текущия екран („Назад“ се връща към страницата с връзката). */
    @Test fun articleSiteLinkOpensAboveTheCurrentScreen() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        siteLink("$base/news/b")
        val after = stack()
        assertEquals(listOf("home", "news", "article/A"), after.take(3))
        assertEquals(4, after.size)
        assertTrue(after.last().startsWith("article/"), after.last())
    }

    /** Известие за новина при раздел, оставен на „Календар“ — искане за сегмента „Новини“. */
    @Test fun newsLinkAsksTheNewsTabForTheNewsSegment() {
        start()
        link(DeepLinks.EVENTS)
        act { navigateTab(Routes.HOME) }
        link(DeepLinks.article("B"))
        assertEquals(listOf("home", "news", "article/B"), stack())
        assertEquals(Routes.SEGMENT_NEWS, segmentOf(Routes.NEWS))
        siteLink("$base/news")
        assertEquals(listOf("home", "news"), stack())
        assertEquals(Routes.SEGMENT_NEWS, segmentOf(Routes.NEWS))
    }

    @Test fun staleSavedStateDoesNotComeBackLater() {
        start()
        act { navigateTab(Routes.MY); navigate(Routes.LOANS) }
        act { navigateTab(Routes.HOME) }
        link("my/card")
        assertEquals(listOf("home", "my", "card"), stack())
        // По-късно, през долната лента — разделът е такъв, какъвто е оставен след връзката.
        act { navigateTab(Routes.HOME) }
        act { navigateTab(Routes.MY) }
        assertEquals(listOf("home", "my", "card"), stack())
    }

    @Test fun otherTabKeepsItsSubStackAfterALink() {
        start()
        act { navigateTab(Routes.NEWS); navigate(Routes.article("A")) }
        link("my/loans")
        act { navigateTab(Routes.NEWS) }
        assertEquals(listOf("home", "news", "article/A"), stack())
    }

    @Test fun tabBarStillRestoresTheSubStack() {
        start()
        act { navigateTab(Routes.MY); navigate(Routes.LOANS) }
        act { navigateTab(Routes.CATALOG) }
        act { navigateTab(Routes.MY) }
        assertEquals(listOf("home", "my", "loans"), stack())
    }

    @Test fun repeatedLinkIsIdempotent() {
        start()
        link(DeepLinks.MESSAGES)
        link(DeepLinks.MESSAGES)
        assertEquals(listOf("home", "messages"), stack())
        assertEquals(listOf("messages", "home"), backOrder())
    }
}
