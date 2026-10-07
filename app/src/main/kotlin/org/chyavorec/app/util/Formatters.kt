package org.chyavorec.app.util

import android.content.Context
import android.text.format.DateUtils
import org.chyavorec.app.R
import org.chyavorec.core.BulgarianDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object Formatters {
    private val sofia = ZoneId.of("Europe/Sofia")

    fun isBulgarian(context: Context): Boolean =
        context.resources.configuration.locales[0].language == "bg"

    fun date(context: Context, iso: String?): String? {
        val d = iso?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() } ?: return null
        return date(context, d)
    }

    fun date(context: Context, d: LocalDate): String =
        if (isBulgarian(context)) BulgarianDates.formatLong(d)
        else d.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.ENGLISH))

    /** Кратка дата: „07.10.2026“ на български, „Oct 7, 2026“ на английски. */
    fun shortDate(context: Context, iso: String?): String? =
        iso?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() }?.let { shortDate(context, it) }

    fun shortDate(context: Context, d: LocalDate): String =
        if (isBulgarian(context)) BulgarianDates.formatShort(d)
        else d.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH))

    fun millisDate(context: Context, millis: Long?): String? =
        millis?.let { date(context, Instant.ofEpochMilli(it).atZone(sofia).toLocalDate()) }

    /** „преди 5 минути“ / „вчера“ — за „Последна синхронизация“. */
    fun relative(context: Context, instant: Instant?, now: Long = System.currentTimeMillis()): String {
        if (instant == null) return context.getString(R.string.sync_never)
        return DateUtils.getRelativeTimeSpanString(instant.toEpochMilli(), now, DateUtils.MINUTE_IN_MILLIS).toString()
    }

    fun dateTime(context: Context, instant: Instant): String {
        val z = instant.atZone(sofia)
        return date(context, z.toLocalDate()) + ", " + "%02d:%02d".format(z.hour, z.minute)
    }
}
