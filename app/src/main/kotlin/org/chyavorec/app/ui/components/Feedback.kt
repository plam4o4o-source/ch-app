package org.chyavorec.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature
import org.chyavorec.core.WaitDuration
import java.time.Instant

/** Ясно, нетехническо съобщение за всяка грешка — без stack traces. */
@Composable
fun errorMessage(error: AppError, subject: String? = null): String = when (error) {
    AppError.Network -> stringResource(R.string.error_network)
    is AppError.Server, is AppError.Parse, is AppError.Unexpected ->
        if (subject != null) stringResource(R.string.error_temporary_subject, subject) else stringResource(R.string.error_temporary)
    AppError.Unauthorized -> stringResource(R.string.error_unauthorized)
    AppError.NotFound -> stringResource(R.string.error_not_found)
    is AppError.RateLimited -> stringResource(R.string.error_rate_limited_wait, waitText(error.retryAfterSeconds))
    is AppError.NotAvailable -> notAvailableMessage(error.feature)
    is AppError.Conflict -> when (error.code) {
        AppError.Conflict.PENDING -> stringResource(R.string.renew_conflict_pending)
        AppError.Conflict.NOT_ALLOWED -> stringResource(R.string.renew_conflict_not_allowed)
        else -> stringResource(R.string.renew_conflict_other)
    }
}

/** „5 минути“, „около 1 час“ — оставащото време по човешки, а не в секунди ([WaitDuration]). */
@Composable
fun waitText(seconds: Long): String {
    val w = WaitDuration.of(seconds)
    val text = when (w.unit) {
        WaitDuration.Unit.SECONDS -> pluralStringResource(R.plurals.wait_seconds, w.amount, w.amount)
        WaitDuration.Unit.MINUTES -> pluralStringResource(R.plurals.wait_minutes, w.amount, w.amount)
        WaitDuration.Unit.HOURS -> pluralStringResource(R.plurals.wait_hours, w.amount, w.amount)
    }
    return if (w.approximate) stringResource(R.string.wait_about, text) else text
}

@Composable
fun notAvailableMessage(feature: Feature): String = when (feature) {
    Feature.LOGIN, Feature.PROFILE -> stringResource(R.string.na_login)
    Feature.LOANS -> stringResource(R.string.na_loans)
    Feature.MEMBERSHIP -> stringResource(R.string.na_membership)
    Feature.HOLDS -> stringResource(R.string.na_holds)
    Feature.RENEW -> stringResource(R.string.na_renew)
    Feature.PASSWORD_RESET -> stringResource(R.string.na_password_reset)
    Feature.ACCOUNT_DELETION -> stringResource(R.string.na_account_deletion)
    Feature.PUSH -> stringResource(R.string.na_push)
    Feature.HISTORY -> stringResource(R.string.na_history)
    Feature.MESSAGES -> stringResource(R.string.na_messages)
}

/** Илюстрация на празно състояние/грешка (всички в линейния стил на логото). */
enum class Illustration(val res: Int) {
    BOOK(R.drawable.ill_open_book),
    CALENDAR(R.drawable.ill_calendar_empty),
    SHELF(R.drawable.ill_shelf_empty),
}

/**
 * Илюстрацията според значката: календар за събития, празна полица за „няма
 * резултати“/„няма заемания“, иначе отворената книга.
 */
fun illustrationFor(icon: ImageVector): Illustration = when (icon) {
    Icons.Outlined.Event, Icons.Outlined.EventAvailable, Icons.Outlined.EventBusy -> Illustration.CALENDAR
    Icons.Outlined.SearchOff, Icons.Outlined.CollectionsBookmark -> Illustration.SHELF
    else -> Illustration.BOOK
}

@Composable
fun MessageView(
    icon: ImageVector,
    title: String,
    message: String?,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    illustration: Illustration = illustrationFor(icon),
    /** Анимирана илюстрация вместо статичната картинка (виж Illustrations.kt). */
    animation: (@Composable () -> Unit)? = null,
    /** Лека анимация по значката (EmptyAnimations.kt), ако няма [animation]; `null` — статично. */
    motion: EmptyMotion? = emptyMotionFor(icon),
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Илюстрация (в стила на логото) + значка според ситуацията.
        Box(Modifier.size(160.dp, 120.dp)) {
            if (animation != null) {
                Box(Modifier.fillMaxSize()) { animation() }
            } else if (motion != null) {
                EmptyAnimation(motion, Modifier.fillMaxSize())
            } else androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(illustration.res),
                contentDescription = null,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(LocalExtendedColors.current.gold),
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                Modifier.align(Alignment.BottomEnd).padding(end = 8.dp).size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                // Златният контейнер (като индикатора в лентата), не розовият secondaryContainer.
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading(); liveRegion = LiveRegionMode.Polite },
        )
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            FilledTonalButton(
                onClick = onAction,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            ) {
                Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(actionLabel)
            }
        }
    }
}

@Composable
fun ErrorView(error: AppError, onRetry: (() -> Unit)?, modifier: Modifier = Modifier, subject: String? = null) {
    val icon = when (error) {
        AppError.Network -> Icons.Outlined.CloudOff
        is AppError.NotAvailable -> Icons.Outlined.Lock
        else -> Icons.Outlined.ErrorOutline
    }
    val title = when (error) {
        AppError.Network -> stringResource(R.string.error_offline_title)
        is AppError.NotAvailable -> stringResource(R.string.na_title)
        else -> stringResource(R.string.error_title)
    }
    MessageView(
        icon = icon,
        title = title,
        message = errorMessage(error, subject),
        modifier = modifier,
        actionLabel = if (error is AppError.NotAvailable) null else stringResource(R.string.action_retry),
        onAction = onRetry,
    )
}

@Composable
fun EmptyView(
    title: String,
    message: String? = null,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Info,
    illustration: Illustration = illustrationFor(icon),
    animation: (@Composable () -> Unit)? = null,
    motion: EmptyMotion? = emptyMotionFor(icon),
) = MessageView(icon, title, message, modifier, illustration = illustration, animation = animation, motion = motion)

/**
 * Лента „Няма интернет връзка. Показваме последно наличните данни.“ + кога е
 * последната синхронизация. Задължителна за кеширани данни — те не бива да
 * изглеждат като актуални.
 */
@Composable
fun SyncBanner(fromCache: Boolean, syncedAt: Instant?, refreshError: AppError?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AnimatedVisibility(
        visible = fromCache,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier,
    ) {
        // Като лентата „Няма интернет връзка“ (OfflineBar): неутрален контейнер и златна икона.
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
        ) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CloudOff, contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        if (refreshError == AppError.Network || refreshError == null) stringResource(R.string.offline_banner)
                        else stringResource(R.string.stale_banner),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        stringResource(R.string.last_sync, Formatters.relative(context, syncedAt)),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

/**
 * Единственият вид на проблем/бележка вътре в екран или карта (вход без връзка,
 * „картата е въведена ръчно“ …): неутрален контейнер, златна икона и — ако има —
 * тонален бутон (обикновено „Опитай отново“). Празните състояния и грешките на цял
 * екран са [MessageView]/[ErrorView].
 */
@Composable
fun InlineNotice(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    actionIcon: ImageVector? = Icons.Outlined.Refresh,
    onAction: (() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Icon(icon, contentDescription = null, tint = LocalExtendedColors.current.gold)
                Spacer(Modifier.width(12.dp))
                Text(text, style = MaterialTheme.typography.bodyMedium)
            }
            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.height(12.dp))
                FilledTonalButton(
                    onClick = onAction,
                    // Подравнен с текста (след иконата), не с ръба на лентата.
                    modifier = Modifier.padding(start = 36.dp).heightIn(min = 48.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                ) {
                    if (actionIcon != null) {
                        Icon(actionIcon, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(actionLabel)
                }
            }
        }
    }
}

/** Бележка без действие — [InlineNotice]. */
@Composable
fun InfoBanner(icon: ImageVector, text: String, modifier: Modifier = Modifier) = InlineNotice(icon, text, modifier)

/**
 * Лентата „Няма интернет връзка“ над долната навигация: заоблена, отделена с 12 dp
 * от краищата (като snackbar), в най-високия неутрален контейнер — а не ярка
 * inverseSurface ивица (в тъмна тема тя ставаше светъл пергамент на всеки екран).
 * [insetNavigation] — без долна лента под нея лентата сама отстъпва от системната навигация.
 */
@Composable
fun OfflineBar(insetNavigation: Boolean, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.fillMaxWidth()
            .then(if (insetNavigation) Modifier.navigationBarsPadding() else Modifier)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CloudOff, contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text(stringResource(R.string.offline_global), style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Малък надпис „Последна синхронизация: …“ за критични библиотечни данни. */
@Composable
fun SyncStamp(syncedAt: Instant?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.Sync, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(6.dp))
        Text(
            stringResource(R.string.last_sync, Formatters.relative(context, syncedAt)),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Лента „ДЕМО ДАННИ“ — само в dev build с USE_MOCK_DATA=true. */
@Composable
fun DemoBanner(visible: Boolean, modifier: Modifier = Modifier) {
    if (!visible) return
    Surface(
        color = LocalExtendedColors.current.warn,
        contentColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Science, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.demo_banner), style = MaterialTheme.typography.labelMedium)
        }
    }
}

private enum class StatePhase { SKELETON, ERROR, EMPTY, CONTENT }

/**
 * Контейнер: skeleton → грешка → празно → съдържание. Смяната между фазите е
 * плавна (skeleton-ът избледнява, а съдържанието „израства“ на мястото му), за
 * да не „изскача“; при намалено движение — мигновена.
 */
@Composable
fun <T> StateContent(
    state: ScreenState<T>,
    onRetry: () -> Unit,
    isEmpty: (T) -> Boolean,
    skeleton: @Composable () -> Unit,
    empty: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    errorSubject: String? = null,
    content: @Composable (T) -> Unit,
) {
    val reduced = rememberReducedMotion()
    val data = state.data
    val phase = when {
        state.showSkeleton -> StatePhase.SKELETON
        data == null && state.error != null -> StatePhase.ERROR
        data != null && isEmpty(data) -> StatePhase.EMPTY
        data != null -> StatePhase.CONTENT
        else -> StatePhase.SKELETON
    }
    AnimatedContent(
        targetState = phase,
        modifier = modifier.fillMaxSize(),
        transitionSpec = {
            if (reduced) {
                (fadeIn(snap()) togetherWith fadeOut(snap())).using(SizeTransform(clip = false) { _, _ -> snap() })
            } else {
                (fadeIn(tween(320, delayMillis = 40)) + scaleIn(tween(320), initialScale = 0.985f))
                    .togetherWith(fadeOut(tween(180)))
                    .using(SizeTransform(clip = false) { _, _ -> tween(320) })
            }
        },
        label = "state",
    ) { target ->
        Box(Modifier.fillMaxSize()) {
            when (target) {
                StatePhase.SKELETON -> skeleton()
                StatePhase.ERROR -> state.error?.let { ErrorView(it, onRetry, Modifier.align(Alignment.Center), errorSubject) }
                StatePhase.EMPTY -> empty()
                StatePhase.CONTENT -> data?.let { content(it) }
            }
        }
    }
}
