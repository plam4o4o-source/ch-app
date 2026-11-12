package org.chyavorec.app.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.TextScale
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.screens.settings.NotificationAccess
import org.chyavorec.app.ui.screens.settings.rememberNotificationPermission
import org.chyavorec.app.ui.screens.settings.textScaleLabel
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.data.repository.CapabilitiesStatus
import org.chyavorec.data.repository.CapabilitiesTracker
import kotlin.math.absoluteValue
import org.chyavorec.app.ui.theme.SystemBarIcons

/** Страниците: добре дошли (какво предлага приложението), настройки, вход/карта. */
private const val PAGES = 3

/**
 * Кратко представяне при първо стартиране (3 екрана): какво предлага приложението; размер на
 * текста и известията; вход или читателска карта. „Пропусни“ — на всеки екран.
 * [onFinish] получава връзката, към която да продължи (`"login"`, `"my/card"`), или `null`.
 */
@Composable
fun OnboardingScreen(onFinish: (link: String?) -> Unit) {
    val container = LocalAppContainer.current
    val caps = remember(container) { CapabilitiesTracker(container.authRepository) }
    LaunchedEffect(caps) { caps.refresh() }
    val capsStatus by caps.status.collectAsStateWithLifecycle()
    // Известно, че онлайн входът не е включен → вместо „Вход“ — дигиталната карта.
    // Докато не е известно (или без връзка) — обичайните бутони.
    val loginOff = (capsStatus as? CapabilitiesStatus.Known)?.caps?.login == false
    val pager = rememberPagerState { PAGES }
    val scope = rememberCoroutineScope()
    // Фонът е винаги тъмен (мастило → бордо) — светли икони в лентите и при светла тема.
    SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
    // Едър системен шрифт (1.3× и повече): по-малки емблема и заглавие, за да не се
    // пренасят думите по букви; текстът на страницата се превърта, вместо да се реже.
    val fontScale = LocalDensity.current.fontScale
    val large = fontScale >= 1.3f
    val titleStyle = when {
        fontScale >= 1.7f -> MaterialTheme.typography.headlineSmall
        large -> MaterialTheme.typography.headlineMedium
        else -> MaterialTheme.typography.displaySmall
    }
    // Снопът светлина по шевицата около печата минава веднъж (за целия екран, не при всяко връщане към страницата).
    val weave = remember { WeaveBudget(cycles = 1) }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Brand.Ink, Color(0xFF2E1612), Brand.Burgundy)))) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            OnboardingRibbon(Modifier.padding(top = 6.dp))
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onFinish(null) }, colors = ButtonDefaults.textButtonColors(contentColor = Brand.GoldLight)) { Text(stringResource(R.string.onb_skip)) }
            }
            HorizontalPager(pager, modifier = Modifier.weight(1f)) { page ->
                val offset = ((pager.currentPage - page) + pager.currentPageOffsetFraction).absoluteValue
                BoxWithConstraints(Modifier.fillMaxSize().graphicsLayer { alpha = 1f - offset.coerceIn(0f, 1f) * 0.6f; translationX = offset * 60f }) {
                    Column(
                        // Поне височината на страницата (съдържанието е центрирано), а ако не се събира — превърта се.
                        Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).heightIn(min = maxHeight).padding(if (large) 24.dp else 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        when (page) {
                            0 -> WelcomePage(large, titleStyle, weave)
                            1 -> SettingsPage(large, titleStyle)
                            else -> SignInPage(large, titleStyle)
                        }
                    }
                }
            }
            // Нишка между три възела вместо точки: следва плъзгането и не иска прекомпозиция.
            Box(Modifier.fillMaxWidth().padding(bottom = 12.dp), contentAlignment = Alignment.Center) {
                ThreadDots(PAGES, position = { pager.currentPage + pager.currentPageOffsetFraction }, current = pager.currentPage)
            }
            // Фонът е винаги тъмен (мастило → бордо), независимо от темата: бутоните са в злато
            // с мастило (8.1:1), а не основният цвят на темата (в светла — #785A10 върху бордо, 1.76:1).
            val primaryColors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink)
            val outlinedColors = ButtonDefaults.outlinedButtonColors(contentColor = Brand.Parchment)
            val outlinedBorder = BorderStroke(1.dp, Brand.Gold)
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (pager.currentPage < PAGES - 1) {
                    Button(onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } }, colors = primaryColors, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                        Text(stringResource(R.string.onb_next), textAlign = TextAlign.Center)
                    }
                } else {
                    Button(onClick = { onFinish(if (loginOff) "my/card" else "login") }, colors = primaryColors, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                        Text(stringResource(if (loginOff) R.string.action_add_card else R.string.onb_login), textAlign = TextAlign.Center)
                    }
                    OutlinedButton(onClick = { onFinish(null) }, colors = outlinedColors, border = outlinedBorder, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                        Text(stringResource(if (loginOff) R.string.onb_continue else R.string.onb_guest), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

/** Заглавието на страницата (за TalkBack — заглавие). */
@Composable
private fun PageTitle(text: String, style: TextStyle) {
    Text(text, style = style, color = Brand.Parchment, textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
}

@Composable
private fun PageText(text: String) {
    Text(
        text, style = MaterialTheme.typography.bodyLarge, color = Brand.Parchment.copy(alpha = 0.82f),
        textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = 480.dp),
    )
}

/** Добре дошли: новини и събития, каталогът с „Искам да прочета“, читателската карта. */
@Composable
private fun WelcomePage(large: Boolean, titleStyle: TextStyle, weave: WeaveBudget) {
    // Печатът е в тъкана рамка от шевица (като във въведението на логото), а не гол върху тъмния фон.
    SealMedallion(if (large) 150.dp else 216.dp, weave)
    Spacer(Modifier.height(if (large) 12.dp else 20.dp))
    PageTitle(stringResource(R.string.onb_welcome_title), titleStyle)
    Text(
        stringResource(R.string.app_subtitle), style = MaterialTheme.typography.titleMedium, color = Brand.GoldLight,
        textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp),
    )
    Spacer(Modifier.height(12.dp))
    PageText(stringResource(R.string.onb_welcome_text))
    Spacer(Modifier.height(if (large) 20.dp else 28.dp))
    Column(Modifier.widthIn(max = 480.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Feature(Icons.Outlined.Event, R.string.onb_news_title, R.string.onb_news_text, large)
        Feature(Icons.AutoMirrored.Outlined.MenuBook, R.string.onb_catalog_title, R.string.onb_catalog_text, large)
        Feature(Icons.Outlined.Badge, R.string.onb_card_title, R.string.onb_card_text, large)
    }
}

/** Една възможност на приложението — TalkBack я чете наведнъж (заглавие и текст). */
@Composable
private fun Feature(icon: ImageVector, title: Int, text: Int, large: Boolean) {
    Row(Modifier.fillMaxWidth().semantics(mergeDescendants = true) {}, verticalAlignment = Alignment.Top) {
        KnotPlate(icon, size = if (large) 44.dp else 52.dp)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(title), style = MaterialTheme.typography.titleMedium, color = Brand.Parchment)
            Text(stringResource(text), style = MaterialTheme.typography.bodyMedium, color = Brand.Parchment.copy(alpha = 0.82f))
        }
    }
}

/**
 * Размерът на текста (прилага се веднага — и на това въведение) и известията: разрешението
 * се иска тук, с обяснението до бутона; после главният екран не пита отново.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SettingsPage(large: Boolean, titleStyle: TextStyle) {
    val container = LocalAppContainer.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val textScale = settings?.textScale ?: TextScale.STANDARD
    val permission = rememberNotificationPermission()
    var access by remember { mutableStateOf(if (Notifier.canNotify(context)) NotificationAccess.GRANTED else null) }

    KnotPlate(Icons.Outlined.FormatSize, size = if (large) 72.dp else 104.dp)
    Spacer(Modifier.height(if (large) 16.dp else 24.dp))
    PageTitle(stringResource(R.string.onb_settings_title), titleStyle)
    Spacer(Modifier.height(12.dp))
    PageText(stringResource(R.string.onb_settings_text))
    Spacer(Modifier.height(if (large) 20.dp else 28.dp))
    Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        SectionTitle(stringResource(R.string.settings_text_size))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth().selectableGroup(),
        ) {
            TextScale.entries.forEach { scale ->
                val selected = scale == textScale
                FilterChip(
                    selected = selected,
                    onClick = { scope.launch { container.settings.setTextScale(scale) } },
                    label = { Text(textScaleLabel(scale)) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent, labelColor = Brand.Parchment,
                        selectedContainerColor = Brand.Gold, selectedLabelColor = Brand.Ink,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true, selected = selected, borderColor = Brand.Gold, selectedBorderColor = Brand.Gold,
                    ),
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        SectionTitle(stringResource(R.string.settings_notifications))
        Text(
            stringResource(R.string.onb_notif_text), style = MaterialTheme.typography.bodyMedium,
            color = Brand.Parchment.copy(alpha = 0.82f), textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        val result = access
        if (result == null) {
            OutlinedButton(
                onClick = {
                    permission.requestWithResult { answer ->
                        access = answer
                        // Отговорено тук — главният екран не пита отново.
                        scope.launch { container.settings.setNotificationPermissionAsked() }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Brand.Parchment),
                border = BorderStroke(1.dp, Brand.Gold),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Icon(Icons.Outlined.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.onb_notif_allow), textAlign = TextAlign.Center)
            }
        } else {
            Text(
                stringResource(if (result == NotificationAccess.GRANTED) R.string.onb_notif_on else R.string.onb_notif_off),
                style = MaterialTheme.typography.bodyMedium, color = Brand.GoldLight, textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text, style = MaterialTheme.typography.titleMedium, color = Brand.GoldLight, textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 8.dp).semantics { heading() },
    )
}

/** Вход с читателските данни или читателската карта (бутоните са отдолу). */
@Composable
private fun SignInPage(large: Boolean, titleStyle: TextStyle) {
    // Примерна карта с шевицата и фолиото — както ще изглежда в телефона; наклони телефона и блести.
    SampleCard(large)
    Spacer(Modifier.height(if (large) 20.dp else 28.dp))
    PageTitle(stringResource(R.string.onb_signin_title), titleStyle)
    Spacer(Modifier.height(16.dp))
    PageText(stringResource(R.string.onb_signin_text))
}
