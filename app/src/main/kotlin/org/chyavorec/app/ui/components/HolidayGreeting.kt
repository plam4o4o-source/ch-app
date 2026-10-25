package org.chyavorec.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequest
import org.chyavorec.app.ui.screens.catalog.CatalogSearchRequests
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.occasion.DayDecor
import org.chyavorec.domain.occasion.HolidayCalendar
import org.chyavorec.domain.occasion.Occasion
import org.chyavorec.domain.occasion.OccasionLink
import org.chyavorec.domain.occasion.SiteOccasion
import java.time.LocalDate

/**
 * Украсата за днес (празници, сезони, обичаи и местните празници от сайта) или
 * [DayDecor.NONE], ако украсите са изключени в настройките.
 *
 * Настройката идва от корена ([LocalSeasonalDecor]) — вярна още в първия кадър; местните
 * празници [site] се зареждат от екрана (ViewModel-а му — кешът, после сайтът), а не тук
 * след първия кадър. [today] се опреснява при връщане към приложението и в полунощ.
 */
@Composable
fun rememberDayDecor(site: List<SiteOccasion>, today: LocalDate = rememberToday()): DayDecor {
    val enabled = rememberSeasonalEnabled()
    return remember(enabled, today, site) { if (enabled) HolidayCalendar.decorFor(today, site) else DayDecor.NONE }
}

/** Текстовете на поздрава: заглавие и ред под него. */
@Composable
private fun greetingTexts(o: Occasion): Pair<String, String>? {
    o.custom?.let { c ->
        val bg = Formatters.isBulgarian(LocalContext.current)
        val en = c.titleEn
        return if (bg || en == null) c.titleBg to c.textBg else en to (c.textEn ?: "")
    }
    val (title, text) = greetingRes(o.id) ?: return null
    return stringResource(title) to stringResource(text)
}

private fun greetingRes(id: String): Pair<Int, Int>? = when (id) {
    "spring" -> R.string.occ_spring_title to R.string.occ_spring_text
    "summer" -> R.string.occ_summer_title to R.string.occ_summer_text
    "autumn" -> R.string.occ_autumn_title to R.string.occ_autumn_text
    "winter" -> R.string.occ_winter_title to R.string.occ_winter_text
    "new_year" -> R.string.occ_new_year_title to R.string.occ_new_year_text
    "liberation" -> R.string.occ_liberation_title to R.string.occ_liberation_text
    "labour" -> R.string.occ_labour_title to R.string.occ_labour_text
    "gergyovden" -> R.string.occ_gergyovden_title to R.string.occ_gergyovden_text
    "may24" -> R.string.occ_may24_title to R.string.occ_may24_text
    "unification" -> R.string.occ_unification_title to R.string.occ_unification_text
    "independence" -> R.string.occ_independence_title to R.string.occ_independence_text
    "good_friday" -> R.string.occ_good_friday_title to R.string.occ_good_friday_text
    "easter" -> R.string.occ_easter_title to R.string.occ_easter_text
    "christmas_eve" -> R.string.occ_christmas_eve_title to R.string.occ_christmas_eve_text
    "christmas" -> R.string.occ_christmas_title to R.string.occ_christmas_text
    "levski_memorial" -> R.string.occ_levski_memorial_title to R.string.occ_levski_memorial_text
    "levski_birth" -> R.string.occ_levski_birth_title to R.string.occ_levski_birth_text
    "botev" -> R.string.occ_botev_title to R.string.occ_botev_text
    "enlighteners" -> R.string.occ_enlighteners_title to R.string.occ_enlighteners_text
    "children_book" -> R.string.occ_children_book_title to R.string.occ_children_book_text
    "book_day" -> R.string.occ_book_day_title to R.string.occ_book_day_text
    "children_day" -> R.string.occ_children_day_title to R.string.occ_children_day_text
    "jordan" -> R.string.occ_jordan_title to R.string.occ_jordan_text
    "babinden" -> R.string.occ_babinden_title to R.string.occ_babinden_text
    "trifon" -> R.string.occ_trifon_title to R.string.occ_trifon_text
    "baba_marta" -> R.string.occ_baba_marta_title to R.string.occ_baba_marta_text
    "lazarovden" -> R.string.occ_lazarovden_title to R.string.occ_lazarovden_text
    "tsvetnitsa" -> R.string.occ_tsvetnitsa_title to R.string.occ_tsvetnitsa_text
    "forgiveness" -> R.string.occ_forgiveness_title to R.string.occ_forgiveness_text
    "ascension" -> R.string.occ_ascension_title to R.string.occ_ascension_text
    "enyovden" -> R.string.occ_enyovden_title to R.string.occ_enyovden_text
    "ilinden" -> R.string.occ_ilinden_title to R.string.occ_ilinden_text
    "assumption" -> R.string.occ_assumption_title to R.string.occ_assumption_text
    "dimitrovden" -> R.string.occ_dimitrovden_title to R.string.occ_dimitrovden_text
    "nikulden" -> R.string.occ_nikulden_title to R.string.occ_nikulden_text
    "ignazhden" -> R.string.occ_ignazhden_title to R.string.occ_ignazhden_text
    else -> null
}

/**
 * Дали поздравът за днес се вижда: има текст и не е скрит с „×“ ([dismissed] `null` — още
 * не е прочетено, поздравът още не е показан).
 */
fun isGreetingShown(decor: DayDecor, dismissed: String?, today: LocalDate): Boolean {
    val o = decor.greeting ?: return false
    if (o.custom == null && greetingRes(o.id) == null) return false
    return dismissed != null && dismissed != "$today|${o.id}"
}

/** Скритият с „×“ поздрав от настройките: `null` = още се чете; "" = нищо не е скрито. */
@Composable
fun rememberDismissedOccasion(): String? {
    val container = LocalAppContainer.current
    val flow = remember(container) { container.settings.dismissedOccasion.map { it.orEmpty() } }
    val dismissed by flow.collectAsStateWithLifecycle(initialValue = null)
    return dismissed
}

/**
 * Празничен поздрав под заглавката: затваря се с „×“ за деня; ако има връзка —
 * натискането отваря подходящите книги. В паметни дни ([quiet]) е в приглушени цветове.
 *
 * [dismissed] — скритият днес поздрав (`null` = още не е прочетен); началният екран го
 * държи в своя ViewModel, за да е известен още в първия кадър. Ако стане известен по-късно,
 * поздравът се разгъва плавно, вместо съдържанието под него да „скочи“.
 */
@Composable
fun HolidayGreeting(
    decor: DayDecor,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    dismissed: String? = rememberDismissedOccasion(),
    today: LocalDate = rememberToday(),
    /** Празникът за деня (/api/calendar, „…**Празник**…“) — ред в поздрава вместо отделен ред на „Начало“. */
    feast: String? = null,
) {
    val occasion = decor.greeting ?: return
    val container = LocalAppContainer.current
    val key = "$today|${occasion.id}"
    val (title, text) = greetingTexts(occasion) ?: return
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val link = occasion.link
    val gold = LocalExtendedColors.current.gold
    val accent = if (decor.quiet) MaterialTheme.colorScheme.onSurfaceVariant else gold
    val reduced = rememberReducedMotion()
    AnimatedVisibility(
        visible = dismissed != null && dismissed != key,
        enter = if (reduced) EnterTransition.None else fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = if (reduced) ExitTransition.None else fadeOut() + shrinkVertically(),
        modifier = modifier,
    ) {
        Surface(
            color = if (decor.quiet) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainerLow,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Row(
                Modifier
                    .then(if (link != null) Modifier.clickable(role = Role.Button) { openOccasionLink(link, navigate) } else Modifier)
                    .padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Тънка цветна черта отляво — златна в празник, сива в паметен ден.
                Surface(color = accent, shape = MaterialTheme.shapes.small, modifier = Modifier.size(width = 3.dp, height = 36.dp)) {}
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    if (text.isNotEmpty()) {
                        Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (feast != null) {
                        Row(Modifier.padding(top = 4.dp)) {
                            Icon(Icons.Outlined.AutoAwesome, null, tint = gold, modifier = Modifier.padding(top = 1.dp).size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(boldMarkdown(feast), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    if (link != null) {
                        Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.occ_open_books), style = MaterialTheme.typography.labelMedium, color = if (decor.quiet) accent else MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, tint = if (decor.quiet) accent else MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
                IconButton(onClick = {
                    scope.launch { container.settings.setDismissedOccasion(key) }
                }) {
                    Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.occ_dismiss), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/** Отваря каталога с търсенето/отдела/подредбата от поздрава. */
fun openOccasionLink(link: OccasionLink, navigate: (String) -> Unit) {
    CatalogSearchRequests.pending.value = CatalogSearchRequest(
        text = link.text.orEmpty(),
        departmentHint = link.departmentHint,
        newest = link.newest,
    )
    navigate(Routes.CATALOG)
}
