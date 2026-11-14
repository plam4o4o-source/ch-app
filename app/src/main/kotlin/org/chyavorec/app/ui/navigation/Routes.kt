package org.chyavorec.app.ui.navigation

import android.net.Uri
import androidx.compose.ui.graphics.vector.ImageVector
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.FolkIcons

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
    /** Сегментът „Новини“ — връзките към новините го искат изрично (иначе би останал „Календар“). */
    const val SEGMENT_NEWS = "news"
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
    /** „Моята читателска година“ — историята на цял екран за година. */
    const val YEAR = "my/year/{year}"
    /** „Искам да прочета“ — списъкът само на устройството. */
    const val WANT_TO_READ = "want-to-read"
    const val MEMBERSHIP = "membership"
    const val NOTIFICATIONS = "notifications"
    const val ACTIVITIES = "activities"
    const val PAGE = "page?url={url}&title={title}"
    const val GALLERY = "gallery"
    /** „Извези шевица“ — платното за кръстат бод (под „Още“). */
    const val EMBROIDERY = "embroidery"
    const val ALBUM = "album/{name}"
    const val VIEWER = "viewer?album={album}&index={index}&url={url}&alt={alt}&page={page}&title={title}"
    const val CONTACTS = "contacts"
    const val DOCUMENTS = "documents"
    /** „Старият вестник“: списъкът и четецът на статия (PDF в приложението). */
    const val NEWSPAPER = "newspaper"
    const val NEWSPAPER_READ = "newspaper/read?url={url}&title={title}&sub={sub}"
    /** „Лента на времето“ 1900 → днес и викторината „Познай Яворец“. */
    const val TIMELINE = "timeline"
    const val QUIZ = "quiz"
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
    fun year(year: Int) = "my/year/$year"
    fun page(url: String, title: String) = "page?url=" + Uri.encode(url) + "&title=" + Uri.encode(title)
    fun newspaperRead(url: String, title: String, sub: String? = null) =
        "newspaper/read?url=" + Uri.encode(url) + "&title=" + Uri.encode(title) + "&sub=" + Uri.encode(sub.orEmpty())
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

/** Разделите на лентата; иконите са от набора „Бод“ ([FolkIcons]) — контур, а избраният — плътен. */
enum class TopTab(val route: String, val labelRes: Int, val icon: ImageVector, val selectedIcon: ImageVector) {
    HOME(Routes.HOME, R.string.tab_home, FolkIcons.Home, FolkIcons.HomeSelected),
    CATALOG(Routes.CATALOG, R.string.tab_catalog, FolkIcons.Catalog, FolkIcons.CatalogSelected),
    MY(Routes.MY, R.string.tab_my, FolkIcons.My, FolkIcons.MySelected),
    NEWS(Routes.NEWS, R.string.tab_news, FolkIcons.News, FolkIcons.NewsSelected),
    MORE(Routes.MORE, R.string.tab_more, FolkIcons.More, FolkIcons.MoreSelected),
}
