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
        act { navigate(Routes.EVENTS); navigate(Routes.event("A")) }
        link(DeepLinks.event("B"))
        assertEquals(listOf("home", "events", "event/B"), stack())
        assertEquals(listOf("event/B", "events", "home"), backOrder())
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
