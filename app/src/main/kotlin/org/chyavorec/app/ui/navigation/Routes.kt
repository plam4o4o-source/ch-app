package org.chyavorec.app.ui.navigation

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import org.chyavorec.app.R

object Routes {
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val MY = "my"
    const val NEWS = "news"
    const val MORE = "more"

    const val ARTICLE = "article/{id}"
    /**
     * Събитията. Не е отделен екран над раздел, а сегментът „Календар“ на раздела
     * „Новини и събития“ — `navigate(EVENTS)` отваря раздела на този сегмент (виж
     * ChitalishteRoot). Самият маршрут остава само за запазени стари стекове.
     */
    const val EVENTS = "events"
    /** Сегментът „Календар“ на раздела „Новини и събития“ (виж [LinkTarget.Screen.segment]). */
    const val SEGMENT_CALENDAR = "calendar"
    const val EVENT = "event/{id}"
    /** `personal` — отворена от личен списък (история, заемания, „За теб“): без корици от интернет. */
    const val BOOK = "book/{inv}?personal={personal}"
    const val SEARCH = "search"
    const val SCAN = "scan"
    const val LOGIN = "login"
    const val PROFILE = "profile"
    const val CARD = "card"
    const val CARD_FULL = "card/full"
    const val LOANS = "loans"
    const val HISTORY = "history"
    /** „Искам да прочета“ — списъкът само на устройството. */
    const val WANT_TO_READ = "want-to-read"
    const val MEMBERSHIP = "membership"
    const val NOTIFICATIONS = "notifications"
    const val ACTIVITIES = "activities"
    const val PAGE = "page?url={url}&title={title}"
    const val GALLERY = "gallery"
    const val ALBUM = "album/{name}"
    const val VIEWER = "viewer?album={album}&index={index}&url={url}&alt={alt}&page={page}&title={title}"
    const val CONTACTS = "contacts"
    const val DOCUMENTS = "documents"
    const val MESSAGES = "messages"
    /** Съобщенията, превъртени до едно от тях (от известие). */
    const val MESSAGE = "messages/{id}"
    const val SETTINGS = "settings"
    const val PRIVACY = "privacy"
    const val TERMS = "terms"
    const val ABOUT = "about"
    /** Локалният дневник на грешките (Настройки → Информация). */
    const val DIAGNOSTICS = "diagnostics"
    const val ABOUT_CHITALISHTE = "chitalishte"

    fun article(id: String) = "article/" + Uri.encode(id)
    fun event(id: String) = "event/" + Uri.encode(id)
    fun book(inv: Long, personal: Boolean = false) = if (personal) "book/$inv?personal=true" else "book/$inv"
    fun message(id: String) = "messages/" + Uri.encode(id)
    fun page(url: String, title: String) = "page?url=" + Uri.encode(url) + "&title=" + Uri.encode(title)
    fun album(name: String) = "album/" + Uri.encode(name)
    fun viewer(album: String?, index: Int, url: String? = null, alt: String? = null, page: String? = null, title: String? = null) =
        "viewer?album=" + Uri.encode(album ?: "") + "&index=$index&url=" + Uri.encode(url ?: "") +
            "&alt=" + Uri.encode(alt ?: "") + "&page=" + Uri.encode(page ?: "") + "&title=" + Uri.encode(title ?: "")

    /**
     * Преглед на произволен списък снимки (напр. галерията на статия). [alts] — описанията
     * в същия ред (празно — без); [page]/[title] — статията или страницата, от която са
     * (споделя се тя, не адресът на снимката).
     */
    fun viewerUrls(urls: List<String>, index: Int, alts: List<String> = emptyList(), page: String? = null, title: String? = null) =
        viewer(
            null, index.coerceAtLeast(0), urls.joinToString("\n"),
            alts.takeIf { a -> a.any { it.isNotBlank() } }?.joinToString("\n") { it.replace('\n', ' ') }, page, title,
        )
}

enum class TopTab(val route: String, val labelRes: Int, val icon: ImageVector, val selectedIcon: ImageVector) {
    HOME(Routes.HOME, R.string.tab_home, Icons.Outlined.Home, Icons.Filled.Home),
    CATALOG(Routes.CATALOG, R.string.tab_catalog, Icons.AutoMirrored.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook),
    MY(Routes.MY, R.string.tab_my, Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle),
    NEWS(Routes.NEWS, R.string.tab_news, Icons.Outlined.Newspaper, Icons.Filled.Newspaper),
    MORE(Routes.MORE, R.string.tab_more, Icons.Outlined.Widgets, Icons.Filled.Widgets),
}
