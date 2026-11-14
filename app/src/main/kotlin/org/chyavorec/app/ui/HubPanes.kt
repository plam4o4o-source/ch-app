package org.chyavorec.app.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.components.UnlockGate
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.gallery.GalleryScreen
import org.chyavorec.app.ui.screens.messages.MessagesScreen
import org.chyavorec.app.ui.screens.my.CardScreen
import org.chyavorec.app.ui.screens.my.HistoryScreen
import org.chyavorec.app.ui.screens.my.LoansScreen
import org.chyavorec.app.ui.screens.my.MembershipScreen
import org.chyavorec.app.ui.screens.my.ProfileScreen
import org.chyavorec.app.ui.screens.settings.AboutAppScreen
import org.chyavorec.app.ui.screens.settings.NotificationSettingsScreen
import org.chyavorec.app.ui.screens.settings.PrivacyScreen
import org.chyavorec.app.ui.screens.settings.SettingsScreen
import org.chyavorec.app.ui.screens.site.AboutChitalishteScreen
import org.chyavorec.app.ui.screens.site.ActivitiesScreen
import org.chyavorec.app.ui.screens.site.ContactsScreen
import org.chyavorec.app.ui.screens.site.DocumentsScreen
import org.chyavorec.app.ui.screens.site.SitePageScreen

/*
 * „Още“ и „Моето“ на таблет (хоризонтално): плочките вляво, избраният раздел — вдясно
 * ([org.chyavorec.app.ui.components.ListDetailPane]). Кои раздели отиват отдясно казва
 * [org.chyavorec.app.ui.navigation.ListDetail]; тук е само как се показват. На телефон — както досега.
 */

/** Какво трябва на разделите отдясно от навигацията на приложението. */
internal class HubPaneActions(
    val navigate: (String) -> Unit,
    val openLink: (String) -> Unit,
    val openExternal: (String) -> Unit,
    val onLogin: () -> Unit,
    val onCardFullscreen: () -> Unit,
    val signedIn: Boolean,
)

/**
 * Разделът [route] отдясно на плочките; „Назад“ в лентата му го затваря ([close]). Личните
 * екрани — зад отключването, както на цял екран (отказът затваря детайла).
 */
@Composable
internal fun HubPaneDetail(route: String, close: () -> Unit, a: HubPaneActions) {
    when (route.substringBefore('?')) {
        Routes.MESSAGES -> UnlockGate(close, required = a.signedIn) { MessagesScreen(close, a.openLink) }
        Routes.ACTIVITIES -> ActivitiesScreen(close, a.navigate, a.openExternal)
        Routes.ABOUT_CHITALISHTE -> AboutChitalishteScreen(close, a.navigate, a.openLink)
        Routes.GALLERY -> GalleryScreen(close, a.navigate)
        Routes.DOCUMENTS -> DocumentsScreen(close, a.openLink, a.navigate)
        Routes.CONTACTS -> ContactsScreen(close)
        Routes.EMBROIDERY -> org.chyavorec.app.ui.screens.embroidery.EmbroideryScreen(close)
        Routes.NEWSPAPER -> org.chyavorec.app.ui.screens.newspaper.NewspaperScreen(close, a.navigate)
        Routes.TIMELINE -> org.chyavorec.app.ui.screens.timeline.TimelineScreen(close, a.navigate)
        Routes.QUIZ -> org.chyavorec.app.ui.screens.quiz.QuizScreen(close)
        Routes.SETTINGS -> SettingsScreen(close, a.navigate)
        Routes.PRIVACY -> PrivacyScreen(close, a.navigate)
        Routes.ABOUT -> AboutAppScreen(close)
        "page" -> {
            val uri = Uri.parse(route)
            SitePageScreen(uri.getQueryParameter("url").orEmpty(), uri.getQueryParameter("title").orEmpty(), close, a.navigate, a.openLink)
        }
        Routes.CARD -> UnlockGate(close) { CardScreen(close, onFullscreen = a.onCardFullscreen, onLogin = a.onLogin) }
        Routes.LOANS -> UnlockGate(close) {
            LoansScreen(close, onLogin = a.onLogin, onOpenBook = { a.navigate(Routes.book(it, personal = true)) }, onOpenHistory = { a.navigate(Routes.HISTORY) })
        }
        Routes.HISTORY -> UnlockGate(close) { HistoryScreen(close, a.navigate, onLogin = a.onLogin) }
        Routes.MEMBERSHIP -> MembershipScreen(close, onLogin = a.onLogin)
        Routes.PROFILE -> ProfileScreen(close)
        Routes.NOTIFICATIONS -> NotificationSettingsScreen(close)
    }
}

/** Отдясно, докато няма избран раздел: тиха розетка и подсказка (вместо широки празни плочки). */
@Composable
internal fun HubPanePlaceholder() {
    Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(
                Modifier.widthIn(max = 360.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                FolkIconPlate(
                    FolkIcons.Chitalishte, FolkMotif.ROSETTE, Modifier.clearAndSetSemantics { }, size = 72.dp,
                    container = MaterialTheme.colorScheme.surfaceContainerHigh, content = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.hub_pane_hint), style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                )
            }
        }
    }
}
