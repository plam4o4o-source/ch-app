package org.chyavorec.app.ui.screens.search

import android.app.Application
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.cancel
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.core.FixedClock
import org.chyavorec.data.SearchResults
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertEquals

/**
 * Резултатите на глобалното търсене: в „Всички“ скритите над 5 се обявяват с „Покажи всички (N)“
 * (към обхвата на раздела), а докато още се търси — индикатор, не празен екран.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class GlobalSearchContentTest {
    @get:Rule val compose = createComposeRule()

    private val vm by lazy {
        val config = AppConfig(
            siteBaseUrl = "http://127.0.0.1:9", catalogUrls = listOf("http://127.0.0.1:9/katalog.json"), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        GlobalSearchViewModel(container)
    }

    @After fun stop() = vm.viewModelScope.cancel()

    private fun show(ui: SearchUi) {
        // Състоянието е зададено направо — без истинско търсене (заявката в ViewModel-а е празна).
        vm.ui.value = ui
        compose.setContent { ChitalishteTheme { GlobalSearchContent(vm, {}, {}, {}, onRecent = {}) } }
        compose.waitForIdle()
    }

    @Test fun allScopeOffersTheHiddenResults() {
        val authors = (1..7).map { "Автор $it" }
        show(SearchUi(query = "автор", results = SearchResults(authors = authors)))
        compose.onAllNodes(hasText("Автор ", substring = true)).assertCountEquals(5)
        compose.onAllNodes(hasText("Покажи всички (7)")).onFirst().performClick()
        compose.waitForIdle()
        assertEquals(SearchScope.BOOKS, vm.ui.value.scope)
        compose.onAllNodes(hasText("Книги") and isSelected()).assertCountEquals(1)
        compose.onAllNodes(hasText("Автор ", substring = true)).assertCountEquals(7)
        compose.onAllNodes(hasText("Покажи всички", substring = true)).assertCountEquals(0)
    }

    @Test fun fewResultsNeedNoShowAll() {
        show(SearchUi(query = "автор", results = SearchResults(authors = listOf("Автор 1", "Автор 2"))))
        compose.onAllNodes(hasText("Автор ", substring = true)).assertCountEquals(2)
        compose.onAllNodes(hasText("Покажи всички", substring = true)).assertCountEquals(0)
    }

    @Test fun searchingWithoutResultsShowsTheIndicator() {
        show(SearchUi(query = "автор", searching = true))
        compose.onAllNodes(hasContentDescription("Зареждане…")).assertCountEquals(1)
        compose.onAllNodes(hasText("Нищо не открихме")).assertCountEquals(0)
    }
}
