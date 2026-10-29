package org.chyavorec.app.ui.screens.home

import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModelWithState
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.search.GlobalSearchContent
import org.chyavorec.app.ui.screens.search.GlobalSearchViewModel

/**
 * Полето за търсене на „Начало“ (Material 3 SearchBar): при докосване се разгръща в цялото
 * търсене (същото съдържание като екрана [Routes.SEARCH]) над екрана; „Назад“ (и жестът с
 * предсказуемо „назад“) го прибира. Иконата за скенера остава в полето.
 *
 * Търсенето (ViewModel-ът) се създава едва при първото разгъване — не бави „Начало“.
 * Отворен резултат прибира полето веднага (иначе прозорецът остава над новия екран), а при
 * връщане към „Начало“ то се разгръща отново с търсенето — както „Назад“ от екрана „Търсене“.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    navigate: (String) -> Unit,
    openExternal: (String) -> Unit,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // При изключени анимации (Достъпност) — без преливане на полето в цял екран.
    val reduced = rememberReducedMotion()
    val state = if (reduced) {
        rememberSearchBarState(animationSpecForExpand = snap(), animationSpecForCollapse = snap())
    } else {
        rememberSearchBarState()
    }
    val text = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    var reopen by rememberSaveable { mutableStateOf(false) }
    // За „Търси“ от клавиатурата (запомня търсенето) — ViewModel-ът е само в разгънатото поле.
    val searchVm = remember { mutableStateOf<GlobalSearchViewModel?>(null) }
    val expanded = state.currentValue == SearchBarValue.Expanded

    LifecycleResumeEffect(Unit) {
        if (reopen) {
            reopen = false
            scope.launch { state.animateToExpanded() }
        }
        onPauseOrDispose { }
    }

    // Към резултат: разгънатото поле се прибира без анимация, преди да се смени екранът.
    fun leave(open: (String) -> Unit): (String) -> Unit = { target ->
        if (state.currentValue == SearchBarValue.Expanded) {
            reopen = true
            scope.launch { state.snapTo(0f) }
        }
        open(target)
    }
    val go = leave(navigate)
    val goLink = leave(openLink)

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            searchBarState = state,
            textFieldState = text,
            onSearch = { searchVm.value?.commit() },
            placeholder = { Text(stringResource(R.string.home_search_hint), maxLines = 1, overflow = TextOverflow.Ellipsis) },
            leadingIcon = {
                if (expanded) {
                    IconButton(onClick = { scope.launch { state.animateToCollapsed() } }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.action_back))
                    }
                } else {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            trailingIcon = {
                if (expanded && text.text.isNotEmpty()) {
                    IconButton(onClick = { text.clearText() }) { Icon(Icons.Outlined.Clear, stringResource(R.string.action_clear)) }
                } else {
                    // Скенер на баркодове: ISBN на книга или етикет с инвентарен номер.
                    IconButton(onClick = { go(Routes.SCAN) }) {
                        Icon(Icons.Outlined.QrCodeScanner, contentDescription = stringResource(R.string.scan_action), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            },
        )
    }

    SearchBar(state = state, inputField = inputField, modifier = modifier.fillMaxWidth())
    ExpandedFullScreenSearchBar(state = state, inputField = inputField) {
        val vm = appViewModelWithState(key = "home-search") { c, saved -> GlobalSearchViewModel(c, saved) }
        // Полето е източникът на текста; търсенето го следва.
        LaunchedEffect(vm) { snapshotFlow { text.text.toString() }.collect { if (it != vm.ui.value.query) vm.setQuery(it) } }
        SideEffect { searchVm.value = vm }
        GlobalSearchContent(
            vm, go, openExternal, goLink,
            onRecent = { q -> text.setTextAndPlaceCursorAtEnd(q) },
        )
    }
}
