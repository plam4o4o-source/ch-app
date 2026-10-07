package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.model.ContentBlock
import org.chyavorec.domain.model.TextRun

/**
 * Рисува съдържание от сайта с native компоненти. Връзките се отварят през
 * [onLink] (Custom Tab или вътрешна навигация); снимките — в цял екран.
 */
@Composable
fun ContentBlocksView(
    blocks: List<ContentBlock>,
    onLink: (String) -> Unit,
    onImage: (String) -> Unit,
    modifier: Modifier = Modifier,
    skipImages: Boolean = false,
    /** Описание за екранни четци на снимки без надпис (напр. заглавието на страницата). */
    imageFallbackDescription: String? = null,
) {
    val openPhotoLabel = stringResource(R.string.a11y_open_photo)
    val openFileLabel = stringResource(R.string.content_open_file)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        blocks.forEach { block ->
            when (block) {
                is ContentBlock.Heading -> Text(
                    block.text,
                    style = when (block.level) {
                        1 -> MaterialTheme.typography.headlineMedium
                        2 -> MaterialTheme.typography.headlineSmall
                        else -> MaterialTheme.typography.titleLarge
                    },
                    modifier = Modifier.padding(top = 6.dp).semantics { heading() },
                )
                is ContentBlock.Paragraph -> Text(
                    runsToAnnotated(block.runs, onLink),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                is ContentBlock.BulletList -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    block.items.forEachIndexed { i, runs ->
                        Row {
                            Text(
                                if (block.ordered) "${i + 1}." else "•",
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalExtendedColors.current.gold,
                                modifier = Modifier.width(22.dp),
                            )
                            Text(runsToAnnotated(runs, onLink), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                is ContentBlock.Quote -> Row(Modifier.padding(vertical = 4.dp)) {
                    Box(Modifier.width(3.dp).fillMaxHeight().background(LocalExtendedColors.current.gold))
                    Spacer(Modifier.width(14.dp))
                    Text(
                        block.text,
                        style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                is ContentBlock.Image -> if (!skipImages) Column {
                    RemoteImage(
                        url = block.url,
                        contentDescription = block.caption ?: imageFallbackDescription,
                        modifier = Modifier.fillMaxWidth().aspectRatio(4f / 3f).clip(MaterialTheme.shapes.medium)
                            .clickable(onClickLabel = openPhotoLabel, role = Role.Image) { onImage(block.url) },
                    )
                    block.caption?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
                    }
                }
                is ContentBlock.LinkButton -> OutlinedButton(onClick = { onLink(block.url) }) {
                    Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(block.text.ifBlank { openFileLabel })
                }
            }
        }
    }
}

@Composable
fun runsToAnnotated(runs: List<TextRun>, onLink: (String) -> Unit): AnnotatedString {
    val linkColor = MaterialTheme.colorScheme.primary
    return buildAnnotatedString {
        runs.forEach { run ->
            val style = SpanStyle(
                fontWeight = if (run.bold) FontWeight.Bold else null,
                fontStyle = if (run.italic) FontStyle.Italic else null,
            )
            val url = run.url
            if (url != null) {
                withLink(
                    LinkAnnotation.Clickable(
                        tag = url,
                        styles = TextLinkStyles(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)),
                        linkInteractionListener = { onLink(url) },
                    ),
                ) { withStyle(style) { append(run.text) } }
            } else {
                withStyle(style) { append(run.text) }
            }
        }
    }
}

@Suppress("unused")
private val alignment = Alignment.Center
