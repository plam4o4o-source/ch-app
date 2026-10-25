package org.chyavorec.app.ui.screens.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.IconPlate
import org.chyavorec.app.ui.components.PagerDots
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.data.repository.CapabilitiesStatus
import org.chyavorec.data.repository.CapabilitiesTracker
import kotlin.math.absoluteValue
import org.chyavorec.app.ui.theme.SystemBarIcons

private data class Slide(val icon: ImageVector?, val title: Int, val text: Int)

private val slides = listOf(
    Slide(null, R.string.onb_welcome_title, R.string.onb_welcome_text),
    Slide(Icons.AutoMirrored.Outlined.MenuBook, R.string.onb_catalog_title, R.string.onb_catalog_text),
    Slide(Icons.Outlined.Badge, R.string.onb_card_title, R.string.onb_card_text),
    Slide(Icons.Outlined.CollectionsBookmark, R.string.onb_books_title, R.string.onb_books_text),
    Slide(Icons.Outlined.Event, R.string.onb_news_title, R.string.onb_news_text),
)

/**
 * Кратко представяне при първо стартиране (5 екрана), след това вход или гост.
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
    val pager = rememberPagerState { slides.size }
    val scope = rememberCoroutineScope()
    // Фонът е винаги тъмен (мастило → бордо) — светли икони в лентите и при светла тема.
    SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
    // Едър системен шрифт (1.3× и повече): по-малки емблема и заглавие, за да не се
    // пренасят думите по букви; текстът на слайда се превърта, вместо да се реже.
    val fontScale = LocalDensity.current.fontScale
    val large = fontScale >= 1.3f
    val titleStyle = when {
        fontScale >= 1.7f -> MaterialTheme.typography.headlineSmall
        large -> MaterialTheme.typography.headlineMedium
        else -> MaterialTheme.typography.displaySmall
    }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Brand.Ink, androidx.compose.ui.graphics.Color(0xFF2E1612), Brand.Burgundy)))) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onFinish(null) }, colors = ButtonDefaults.textButtonColors(contentColor = Brand.GoldLight)) { Text(stringResource(R.string.onb_skip)) }
            }
            HorizontalPager(pager, modifier = Modifier.weight(1f)) { page ->
                val s = slides[page]
                val offset = ((pager.currentPage - page) + pager.currentPageOffsetFraction).absoluteValue
                BoxWithConstraints(Modifier.fillMaxSize().graphicsLayer { alpha = 1f - offset.coerceIn(0f, 1f) * 0.6f; translationX = offset * 60f }) {
                    Column(
                        // Поне височината на страницата (съдържанието е центрирано), а ако не се събира — превърта се.
                        Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).heightIn(min = maxHeight).padding(if (large) 24.dp else 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        if (s.icon == null) Emblem(if (large) 88.dp else 128.dp)
                        else IconPlate(s.icon, size = if (large) 84.dp else 120.dp, container = Brand.Gold, content = Brand.Ink)
                        Spacer(Modifier.height(if (large) 20.dp else 36.dp))
                        Text(stringResource(s.title), style = titleStyle, color = Brand.Parchment,
                            textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
                        if (page == 0) {
                            Text(stringResource(R.string.app_subtitle), style = MaterialTheme.typography.titleMedium, color = Brand.GoldLight,
                                textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(stringResource(s.text), style = MaterialTheme.typography.bodyLarge, color = Brand.Parchment.copy(alpha = 0.82f),
                            textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = 480.dp))
                    }
                }
            }
            PagerDots(
                slides.size, pager.currentPage, Modifier.padding(bottom = 16.dp),
                activeColor = Brand.Gold, inactiveColor = Brand.Parchment.copy(alpha = 0.35f),
            )
            // Фонът е винаги тъмен (мастило → бордо), независимо от темата: бутоните са в злато
            // с мастило (8.1:1), а не основният цвят на темата (в светла — #785A10 върху бордо, 1.76:1).
            val primaryColors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink)
            val outlinedColors = ButtonDefaults.outlinedButtonColors(contentColor = Brand.Parchment)
            val outlinedBorder = BorderStroke(1.dp, Brand.Gold)
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (pager.currentPage < slides.lastIndex) {
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
