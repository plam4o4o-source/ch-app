package org.chyavorec.app.notifications

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.flow.first
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.DigestLanguage
import org.chyavorec.domain.model.DigestTarget
import org.chyavorec.domain.model.NewBooks
import org.chyavorec.domain.model.TomorrowReminder
import org.chyavorec.domain.model.WeeklyDigest
import java.time.LocalDateTime

/**
 * „Тази седмица в Яворец“ и напомнянето „Утре: …“ — стъпки на [PeriodicSyncWorker] (без нови
 * аларми и разрешения; работят само при включена настройка и разрешени известия).
 *
 * Прегледът: решението е в [WeeklyDigest.decide] (слот неделя 18:00, един на седмица, не нощем);
 * събитията и новините са от кеша (при липса — една условна заявка), каталогът — най-много веднъж
 * на денонощие (условна заявка, ETag) и само когато прегледът наистина се съставя. „Нови книги“ са
 * само постъпилите СЛЕД началната точка, затова тестовите дати в миналото не заливат.
 */
object WeeklyDigestStep {
    /** Каталогът се пита не по-често от веднъж на толкова. */
    internal const val CATALOG_CHECK_MS = 23 * 60 * 60 * 1000L
    internal const val STEP_CATALOG = "digest-catalog"

    /** Колко най-нови записа от каталога се гледат за „нови“ (над [NewBooks.MAX] е достатъчно за „над 99“). */
    private const val NEWEST_WINDOW = 400

    suspend fun run(context: Context, c: AppContainer) {
        val settings = c.settings.current()
        if (!settings.weeklyDigest) return
        val now = c.clock.now()
        val zone = c.clock.zone()
        val state = c.settings.digestState()
        when (val decision = WeeklyDigest.decide(now, zone, state)) {
            WeeklyDigest.Decision.None -> Unit
            // Първо пускане без начална точка (напр. след възстановяване на данни): само се записва.
            is WeeklyDigest.Decision.Baseline -> c.settings.markDigestSlot(decision.slot.toEpochMilli(), baselineMillis = now.toEpochMilli())
            is WeeklyDigest.Decision.Skip -> c.settings.markDigestSlot(decision.slot.toEpochMilli())
            is WeeklyDigest.Decision.Send -> {
                val baseline = state.baselineMillis ?: now.toEpochMilli()
                val sent = compose(context, c, settings.digestSections, baseline, LocalDateTime.ofInstant(now, zone))
                // Първо записът, после известието: прекъсване между двете губи един преглед, но не го повтаря.
                c.settings.markDigestSlot(decision.slot.toEpochMilli(), baselineMillis = now.toEpochMilli())
                sent?.let { show(context, it) }
            }
        }
    }

    private suspend fun compose(
        context: Context,
        c: AppContainer,
        sections: Set<String>,
        baselineMillis: Long,
        now: LocalDateTime,
    ): org.chyavorec.domain.model.Digest? {
        val zone = c.clock.zone()
        val lang = if (Formatters.isBulgarian(context)) DigestLanguage.BG else DigestLanguage.EN
        val events = (c.eventsRepository.cachedRolled()?.data
            ?: (c.eventsRepository.events(force = false) as? Outcome.Success)?.value?.data).orEmpty()
        val news = (c.newsRepository.cached()?.data
            ?: (c.newsRepository.latest(force = false) as? Outcome.Success)?.value?.data).orEmpty()
        val baselineDate = java.time.Instant.ofEpochMilli(baselineMillis).atZone(zone).toLocalDate()
        return WeeklyDigest.compose(
            WeeklyDigest.upcomingEvents(events, now),
            newBooks(c, baselineDate, sections),
            WeeklyDigest.newArticles(news, baselineMillis),
            lang,
        )
    }

    /** От каталога: условна заявка не по-често от веднъж на денонощие, иначе — каквото е на диска. */
    private suspend fun newBooks(c: AppContainer, baseline: java.time.LocalDate, sections: Set<String>): NewBooks {
        val nowMs = c.clock.now().toEpochMilli()
        val last = c.settings.lastSyncStep(STEP_CATALOG)
        if (last > nowMs || nowMs - last >= CATALOG_CHECK_MS) {
            if (c.catalogRepository.refreshHomeSummary(force = false) is Outcome.Success) c.settings.setLastSyncStep(STEP_CATALOG, nowMs)
        }
        val searcher = (c.catalogRepository.latestLookup() ?: c.catalogRepository.lookupSearcher(network = false))?.data ?: return NewBooks.NONE
        val newest = searcher.searchFirst(CatalogQuery(sort = CatalogSort.NEWEST), NEWEST_WINDOW).books.take(NEWEST_WINDOW)
        return NewBooks.since(newest, baseline, sections)
    }

    private fun show(context: Context, digest: org.chyavorec.domain.model.Digest) {
        val link = linkOf(digest.target)
        // Бутони за другите раздели, в които също има нещо (най-много два).
        val others = digest.targets.drop(1).take(2)
        Notifier.show(
            context, Channel.DIGEST, NotificationIds.DIGEST, digest.title, digest.summary, link,
            actions = others.mapIndexedNotNull { i, t -> linkAction(context, NotificationIds.DIGEST + 1 + i, t) },
            category = NotificationCompat.CATEGORY_RECOMMENDATION,
            bigText = digest.lines.joinToString("\n").ifBlank { null },
        )
    }

    internal fun linkOf(target: DigestTarget): String = when (target) {
        DigestTarget.EVENTS -> DeepLinks.EVENTS
        DigestTarget.CATALOG -> "catalog"
        DigestTarget.NEWS -> "news"
    }

    private fun linkAction(context: Context, requestCode: Int, target: DigestTarget): NotificationCompat.Action {
        val label = when (target) {
            DigestTarget.EVENTS -> R.string.digest_open_events
            DigestTarget.CATALOG -> R.string.digest_open_books
            DigestTarget.NEWS -> R.string.digest_open_news
        }
        val intent = LaunchIntents.openLink(context, linkOf(target)).apply {
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Action.Builder(R.drawable.ic_notification, context.getString(label), pending).build()
    }

    // ---------------------------------------------------------------- „Утре: …“

    /**
     * Събитие на читалището с начален час утре → кратко известие, веднъж на събитие и дата; през
     * деня (не нощем). Не и за събитие със звънче (то си има напомняне) — виж [TomorrowReminder.pick].
     */
    suspend fun runTomorrow(context: Context, c: AppContainer) {
        if (!c.settings.current().tomorrowReminder) return
        val now = c.clock.now()
        if (org.chyavorec.domain.model.LoanReminderPlan.isQuiet(now, c.clock.zone())) return
        val events = c.eventsRepository.cachedRolled()?.data ?: return
        val bell = c.reminders.reminderIds.first().toSet()
        val today = c.clock.today()
        val picked = TomorrowReminder.pick(events, today, c.settings.tomorrowReminded(), bell)
        if (picked.isEmpty()) return
        val lang = if (Formatters.isBulgarian(context)) DigestLanguage.BG else DigestLanguage.EN
        val date = today.plusDays(1).toString()
        // Първо записът, после известията — прекъсване не повтаря известие.
        c.settings.addTomorrowReminded(picked.map { TomorrowReminder.key(it, date) })
        picked.forEach { e ->
            Notifier.show(
                context, Channel.EVENTS, NotificationIds.tomorrow(e.id),
                TomorrowReminder.title(e, lang), TomorrowReminder.text(e, lang), DeepLinks.event(e.id),
                category = NotificationCompat.CATEGORY_EVENT,
            )
        }
    }
}
