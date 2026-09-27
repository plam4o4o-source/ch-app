package org.chyavorec.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import org.chyavorec.app.ui.components.BrandImage
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.BuildConfig
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.util.Intents
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection

/** Текст от ресурс, разделен на абзаци; редове, започващи с „# “, са заглавия. */
@Composable
private fun RichText(text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        text.split("\n\n").forEach { para ->
            if (para.startsWith("# ")) {
                Text(para.removePrefix("# "), style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp).semantics { heading() })
            } else {
                Text(para, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun LegalScreen(title: String, body: String, siteSection: SiteSection, onBack: () -> Unit, navigate: (String) -> Unit) {
    val c = LocalAppContainer.current
    var siteLink by remember { mutableStateOf<SiteLink?>(null) }
    LaunchedEffect(Unit) {
        siteLink = (c.siteRepository.links(false) as? Outcome.Success)?.value?.data?.firstOrNull { it.kind == siteSection }
    }
    Scaffold(topBar = { BackTopBar(title, onBack) }) { padding ->
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(20.dp).widthIn(max = 760.dp)) {
            // Ако сайтът има собствена страница (напр. политика на читалището) — предлагаме и нея.
            siteLink?.let { l ->
                OutlinedButton(onClick = { navigate(Routes.page(l.url, l.title)) }) {
                    Icon(Icons.AutoMirrored.Outlined.OpenInNew, null)
                    Text(stringResource(R.string.legal_site_version, l.title), modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(Modifier.height(16.dp))
            }
            RichText(body)
        }
    }
}

@Composable
fun PrivacyScreen(onBack: () -> Unit, navigate: (String) -> Unit) =
    LegalScreen(stringResource(R.string.privacy_title), stringResource(R.string.privacy_body), SiteSection.PRIVACY, onBack, navigate)

@Composable
fun TermsScreen(onBack: () -> Unit, navigate: (String) -> Unit) =
    LegalScreen(stringResource(R.string.terms_title), stringResource(R.string.terms_body), SiteSection.TERMS, onBack, navigate)

@Composable
fun AboutAppScreen(onBack: () -> Unit) {
    val c = LocalAppContainer.current
    val context = LocalContext.current
    Scaffold(topBar = { BackTopBar(stringResource(R.string.about_title), onBack) }) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(24.dp).widthIn(max = 720.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Emblem(96.dp)
            Text(stringResource(R.string.app_title), style = MaterialTheme.typography.headlineLarge)
            Text(stringResource(R.string.app_subtitle), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
            Text(stringResource(R.string.about_version_full, c.config.versionName, c.config.versionCode), style = MaterialTheme.typography.bodyMedium)
            if (!c.config.isProduction) {
                Text(stringResource(R.string.about_env, c.config.environment, BuildConfig.USE_MOCK_DATA.toString()), style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(8.dp))
            RichText(stringResource(R.string.about_body))
            OutlinedButton(onClick = { Intents.openUrl(context, c.config.siteBaseUrl) }) { Text(stringResource(R.string.contacts_site)) }
            Spacer(Modifier.height(12.dp))
            // Библиотечната система, от която идва каталогът.
            ElevatedCard(onClick = { Intents.openUrl(context, "https://invlib.com/") }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    BrandImage(R.drawable.logo_invlib, null, Modifier.size(width = 72.dp, height = 44.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.about_invlib), style = MaterialTheme.typography.titleMedium)
                        Text(stringResource(R.string.about_invlib_desc), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    BrandImage(R.drawable.logo_catalog, null, Modifier.size(48.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.catalog_title), style = MaterialTheme.typography.titleMedium)
                        Text(stringResource(R.string.about_catalog_desc), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            // Създателят на приложението.
            Text(stringResource(R.string.about_creator).uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            // Само логото на създателя — без изписано име.
            BrandImage(R.drawable.logo_creator, stringResource(R.string.about_creator), Modifier.fillMaxWidth(0.55f).aspectRatio(2f))
        }
    }
}
