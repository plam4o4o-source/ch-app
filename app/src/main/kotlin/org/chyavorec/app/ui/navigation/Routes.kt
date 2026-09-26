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
    const val EVENTS = "events"
    const val EVENT = "event/{id}"
    const val BOOK = "book/{inv}"
    const val SEARCH = "search"
    const val LOGIN = "login"
    const val PROFILE = "profile"
    const val CARD = "card"
    const val CARD_FULL = "card/full"
    const val LOANS = "loans"
    const val MEMBERSHIP = "membership"
    const val NOTIFICATIONS = "notifications"
    const val ACTIVITIES = "activities"
    const val PAGE = "page?url={url}&title={title}"
    const val GALLERY = "gallery"
    const val ALBUM = "album/{name}"
    const val VIEWER = "viewer?album={album}&index={index}&url={url}"
    const val CONTACTS = "contacts"
    const val DOCUMENTS = "documents"
    const val SETTINGS = "settings"
    const val PRIVACY = "privacy"
    const val TERMS = "terms"
    const val ABOUT = "about"
    const val ABOUT_CHITALISHTE = "chitalishte"

    fun article(id: String) = "article/" + Uri.encode(id)
    fun event(id: String) = "event/" + Uri.encode(id)
    fun book(inv: Long) = "book/$inv"
    fun page(url: String, title: String) = "page?url=" + Uri.encode(url) + "&title=" + Uri.encode(title)
    fun album(name: String) = "album/" + Uri.encode(name)
    fun viewer(album: String?, index: Int, url: String? = null) =
        "viewer?album=" + Uri.encode(album ?: "") + "&index=$index&url=" + Uri.encode(url ?: "")

    /** Преглед на произволен списък снимки (напр. галерията на статия). */
    fun viewerUrls(urls: List<String>, index: Int) = viewer(null, index.coerceAtLeast(0), urls.joinToString("\n"))
}

enum class TopTab(val route: String, val labelRes: Int, val icon: ImageVector, val selectedIcon: ImageVector) {
    HOME(Routes.HOME, R.string.tab_home, Icons.Outlined.Home, Icons.Filled.Home),
    CATALOG(Routes.CATALOG, R.string.tab_catalog, Icons.AutoMirrored.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook),
    MY(Routes.MY, R.string.tab_my, Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle),
    NEWS(Routes.NEWS, R.string.tab_news, Icons.Outlined.Newspaper, Icons.Filled.Newspaper),
    MORE(Routes.MORE, R.string.tab_more, Icons.Outlined.Widgets, Icons.Filled.Widgets),
}
