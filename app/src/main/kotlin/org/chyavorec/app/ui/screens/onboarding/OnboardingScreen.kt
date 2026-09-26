package org.chyavorec.app.ui.screens.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.theme.Brand
import kotlin.math.absoluteValue

private data class Slide(val icon: ImageVector?, val title: Int, val text: Int)

private val slides = listOf(
    Slide(null, R.string.onb_welcome_title, R.string.onb_welcome_text),
    Slide(Icons.AutoMirrored.Outlined.MenuBook, R.string.onb_catalog_title, R.string.onb_catalog_text),
    Slide(Icons.Outlined.Badge, R.string.onb_card_title, R.string.onb_card_text),
    Slide(Icons.Outlined.CollectionsBookmark, R.string.onb_books_title, R.string.onb_books_text),
    Slide(Icons.Outlined.Event, R.string.onb_news_title, R.string.onb_news_text),
)

/** Кратко представяне при първо стартиране (5 екрана), след това вход или гост. */
@Composable
fun OnboardingScreen(onFinish: (login: Boolean) -> Unit) {
    val pager = rememberPagerState { slides.size }
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Brand.Ink, androidx.compose.ui.graphics.Color(0xFF2E1612), Brand.Burgundy)))) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onFinish(false) }) { Text(stringResource(R.string.onb_skip), color = Brand.GoldLight) }
            }
            HorizontalPager(pager, modifier = Modifier.weight(1f)) { page ->
                val s = slides[page]
                val offset = ((pager.currentPage - page) + pager.currentPageOffsetFraction).absoluteValue
                Column(
                    Modifier.fillMaxSize().padding(32.dp).graphicsLayer { alpha = 1f - offset.coerceIn(0f, 1f) * 0.6f; translationX = offset * 60f },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (s.icon == null) Emblem(128.dp)
                    else Box(Modifier.size(120.dp).clip(CircleShape).background(Brand.Gold.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
                        Icon(s.icon, null, tint = Brand.Gold, modifier = Modifier.size(56.dp))
                    }
                    Spacer(Modifier.height(36.dp))
                    Text(stringResource(s.title), style = MaterialTheme.typography.displaySmall, color = Brand.Parchment,
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
            Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center) {
                repeat(slides.size) { i ->
                    val w by animateDpAsState(if (pager.currentPage == i) 22.dp else 8.dp, label = "dot")
                    Box(Modifier.padding(4.dp).height(8.dp).width(w).clip(RoundedCornerShape(50))
                        .background(if (pager.currentPage == i) Brand.Gold else Brand.Parchment.copy(alpha = 0.35f)))
                }
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (pager.currentPage < slides.lastIndex) {
                    Button(onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                        Text(stringResource(R.string.onb_next))
                    }
                } else {
                    Button(onClick = { onFinish(true) }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text(stringResource(R.string.onb_login)) }
                    OutlinedButton(onClick = { onFinish(false) }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                        Text(stringResource(R.string.onb_guest), color = Brand.Parchment)
                    }
                }
            }
        }
    }
}
