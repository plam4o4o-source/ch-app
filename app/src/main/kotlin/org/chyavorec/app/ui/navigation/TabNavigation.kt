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
fun NavController.openLinkTarget(target: LinkTarget.Screen) {
    navigateTab(target.tab)
    popBackStack(target.tab, inclusive = false, saveState = false)
    target.stack.forEach { navigate(it) { launchSingleTop = true } }
}
