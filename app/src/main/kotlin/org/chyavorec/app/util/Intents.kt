package org.chyavorec.app.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import org.chyavorec.app.R
import org.chyavorec.domain.model.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** Външни действия: браузър, телефон, имейл, карта, споделяне, календар. */
object Intents {

    /** Публична страница за искане на изтриване на акаунта и данните (изискване на Google Play). */
    const val ACCOUNT_DELETION_URL = "https://chyavorec.org/app-delete"

    private fun Context.safeStart(intent: Intent) {
        try {
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_no_app), Toast.LENGTH_SHORT).show()
        }
    }

    /** Отваря адрес в Custom Tab (в цветовете на приложението), а не в WebView. */
    fun openUrl(context: Context, url: String) {
        val uri = Uri.parse(url)
        when (uri.scheme) {
            "tel" -> return dial(context, uri.schemeSpecificPart)
            "mailto" -> return email(context, uri.schemeSpecificPart)
            "https" -> Unit
            else -> return
        }
        try {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setDefaultColorSchemeParams(CustomTabColorSchemeParams.Builder().setToolbarColor(0xFF1A1208.toInt()).build())
                .build()
                .launchUrl(context, uri)
        } catch (_: ActivityNotFoundException) {
            context.safeStart(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    fun dial(context: Context, phone: String) =
        context.safeStart(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.filter { it.isDigit() || it == '+' })))

    fun email(context: Context, address: String, subject: String? = null) =
        context.safeStart(
            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
                subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            },
        )

    fun map(context: Context, query: String) =
        context.safeStart(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(query))))

    fun share(context: Context, title: String, text: String) {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.safeStart(Intent.createChooser(send, context.getString(R.string.action_share)))
    }

    /** Добавя събитие в календара на Android (без разрешения — през системния екран). */
    fun addToCalendar(context: Context, event: Event, zone: ZoneId) {
        val date = event.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: return
        val time = event.time?.let { runCatching { LocalTime.parse(it) }.getOrNull() }
        val intent = Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI).apply {
            putExtra(CalendarContract.Events.TITLE, event.title)
            putExtra(CalendarContract.Events.DESCRIPTION, event.description + "\n\n" + event.sourceUrl)
            event.place?.let { putExtra(CalendarContract.Events.EVENT_LOCATION, it + ", с. Яворец") }
            if (time != null) {
                val start = date.atTime(time).atZone(zone)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.toInstant().toEpochMilli())
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, start.plusHours(2).toInstant().toEpochMilli())
            } else {
                putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, date.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
            }
        }
        context.safeStart(intent)
    }
}
