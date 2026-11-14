package org.chyavorec.app.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Смяна на раздел от долната лента: подстекът на напуснатия раздел се запазва, а този
 * на избрания — възстановява (както е бил оставен).
 */
fun NavController.navigateTab(route: String) = navigate(route) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

/**
 * Връзка от известие, пряк път или адрес от сайта: разделът отдолу, но БЕЗ стария му
 * подстек, после екраните над него — всеки веднъж. Иначе, ако разделът е оставен напр. на
 * „Заеми“, известие за заемите би дало НАЧАЛО › МОЕТО › ЗАЕМИ › ЗАЕМИ, а новина при
 * отворена друга статия — НОВИНИ › А › Б (и „Назад“ — към нещо, което не е избирано).
 *
 * Разделът се отваря чрез [navigateTab] (запазеното му състояние се „изразходва“ и не
 * изскача по-късно при смяна на раздела), а подстекът му веднага се маха без запазване.
 * Подстекът на раздела, от който се идва, се запазва както при обикновена смяна.
 */
fun NavController.openLinkTarget(target: LinkTarget.Screen, twoPane: Boolean = false) {
    navigateTab(target.tab)
    popBackStack(target.tab, inclusive = false, saveState = false)
    target.segment?.let { requestSegment(target.tab, it) }
    // Без избрания отпреди детайл (както и без стария подстек).
    requestDetail(target.tab, null)
    // Таблет: последният екран (книга, статия) — отдясно на списъка под него, не върху него.
    val host = target.stack.getOrNull(target.stack.lastIndex - 1) ?: target.tab
    val detail = target.stack.lastOrNull()?.takeIf { twoPane && ListDetail.accepts(host, it) }
    (if (detail != null) target.stack.dropLast(1) else target.stack).forEach { navigate(it) { launchSingleTop = true } }
    detail?.let { requestDetail(host, it) }
}

/**
 * Връзка от съдържанието на сайта към native екран: екраните ѝ — над текущия („Назад“ се
 * връща към страницата с връзката); раздел без екрани над него (`/events`, `/news`,
 * каталогът) — като от известие ([openLinkTarget]): без стария подстек на раздела и на
 * искания сегмент. Иначе [navigateTab] би възстановил напр. оставена отворена статия.
 */
fun NavController.openSiteLink(target: LinkTarget.Screen) {
    if (target.stack.isEmpty()) openLinkTarget(target) else target.stack.forEach { navigate(it) }
}

/** Ключът в SavedStateHandle на раздела, под който връзката оставя искания сегмент. */
const val SEGMENT_KEY = "segment"

/**
 * Раздел със сегменти („Новини | Календар“) да покаже [segment]. Искането се оставя в
 * състоянието на самия раздел (не в глобален обект) — разделът го взима и изчиства, както
 * при първа композиция, така и ако вече е отворен. Разделът трябва да е в стека.
 */
fun NavController.requestSegment(tab: String, segment: String) {
    runCatching { getBackStackEntry(tab).savedStateHandle[SEGMENT_KEY] = segment }
}

/** Ключът в SavedStateHandle на списъка, под който е маршрутът на детайла до него (виж [ListDetail]). */
const val DETAIL_KEY = "detail"

/**
 * Списък и детайл един до друг (таблет, хоризонтално): кои екрани списъкът показва отдясно,
 * вместо върху себе си. На телефон (и на тесен прозорец) — отделни екрани, както досега.
 */
object ListDetail {
    /** Дали списъкът [host] (маршрутът на дестинацията) показва [route] до себе си. */
    fun accepts(host: String?, route: String): Boolean = when (host) {
        Routes.CATALOG, Routes.WANT_TO_READ -> route.startsWith("book/")
        Routes.NEWS -> route.startsWith("article/")
        // „Още“ и „Моето“: разделите им (плочките) — отдясно; по-дълбоките екрани — на цял екран.
        Routes.MORE -> route in MoreSections || route.startsWith("page?")
        Routes.MY -> route in MySections
        else -> false
    }

    private val MoreSections = setOf(
        Routes.MESSAGES, Routes.ACTIVITIES, Routes.ABOUT_CHITALISHTE, Routes.GALLERY, Routes.DOCUMENTS, Routes.CONTACTS,
        Routes.EMBROIDERY, Routes.NEWSPAPER, Routes.TIMELINE, Routes.QUIZ, Routes.SETTINGS, Routes.PRIVACY, Routes.ABOUT,
    )

    private val MySections = setOf(Routes.CARD, Routes.LOANS, Routes.HISTORY, Routes.MEMBERSHIP, Routes.PROFILE, Routes.NOTIFICATIONS)
}

/** Списъкът [host] да покаже [route] отдясно (`null` — без детайл). Списъкът трябва да е в стека. */
fun NavController.requestDetail(host: String, route: String?) {
    runCatching { getBackStackEntry(host).savedStateHandle[DETAIL_KEY] = route }
}
