package org.chyavorec.app.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import org.chyavorec.app.R
import org.chyavorec.core.TrustedLinks
import org.chyavorec.domain.model.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** Външни действия: браузър, телефон, имейл, карта, споделяне, календар, клипборд. */
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
        // Адресите на сайта са App Links на самото приложение — без изричен браузър
        // „Отвори в сайта“ би отворило пак приложението (и екрана, от който е натиснато).
        val browser = if (isOwnAppLink(uri, url)) browserPackage(context) else null
        try {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setDefaultColorSchemeParams(CustomTabColorSchemeParams.Builder().setToolbarColor(0xFF1A1208.toInt()).build())
                .build()
                .apply { browser?.let { intent.setPackage(it) } }
                .launchUrl(context, uri)
        } catch (_: ActivityNotFoundException) {
            context.safeStart(Intent(Intent.ACTION_VIEW, uri).apply { browser?.let { setPackage(it) } })
        }
    }

    /**
     * Адресът би стигнал пак до приложението (App Link за chyavorec.org). Проверява се и как
     * го чете Android ([Uri.getHost] взима хоста след последното `@`, напр. в
     * `https://evil.com\@chyavorec.org/`), и строгото [TrustedLinks] — при което и да е от
     * двете се отваря изрично в браузъра, без да се върне в приложението.
     */
    private fun isOwnAppLink(uri: Uri, url: String): Boolean =
        uri.host.equals(TrustedLinks.SITE_HOST, ignoreCase = true) || TrustedLinks.hostOf(url) == TrustedLinks.SITE_HOST

    /**
     * Браузърът за адресите на сайта: този по подразбиране (ако не е самото приложение),
     * иначе първият инсталиран. `null` — няма браузър (тогава системата избира).
     */
    private fun browserPackage(context: Context): String? {
        val pm = context.packageManager
        val probe = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.org/")).addCategory(Intent.CATEGORY_BROWSABLE)
        val own = context.packageName
        val default = pm.resolveActivity(probe, PackageManager.MATCH_DEFAULT_ONLY)?.activityInfo?.packageName
        if (default != null && default != own && default != "android") return default
        return pm.queryIntentActivities(probe, PackageManager.MATCH_DEFAULT_ONLY)
            .map { it.activityInfo.packageName }
            .firstOrNull { it != own }
    }

    fun dial(context: Context, phone: String) =
        context.safeStart(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.filter { it.isDigit() || it == '+' })))

    fun email(context: Context, address: String, subject: String? = null, body: String? = null) =
        context.safeStart(
            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
                subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
                body?.let { putExtra(Intent.EXTRA_TEXT, it) }
            },
        )

    /**
     * Копира [text] в клипборда. От Android 13 системата сама показва какво е копирано —
     * собственото потвърждение (Toast) е само за по-старите версии, за да не се дублира.
     */
    fun copy(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(ClipboardManager::class.java) ?: return
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(context, context.getString(R.string.copied, text), Toast.LENGTH_SHORT).show()
        }
    }

    fun map(context: Context, query: String) =
        context.safeStart(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(query))))

    /** Authority-то на FileProvider-а (AndroidManifest.xml, res/xml/file_paths.xml). */
    fun fileProviderAuthority(context: Context): String = context.packageName + ".files"

    /**
     * Отваря свален PDF (в cacheDir/documents) с приложение за PDF на телефона — през
     * FileProvider, с временно право само за четене на този файл. `false` — няма такова
     * приложение (тогава документът се отваря в браузъра).
     */
    fun openPdf(context: Context, file: java.io.File): Boolean {
        val uri = runCatching { androidx.core.content.FileProvider.getUriForFile(context, fileProviderAuthority(context), file) }
            .getOrNull() ?: return false
        val view = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, "application/pdf")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        if (context.packageManager.queryIntentActivities(view, 0).isEmpty()) return false
        return try {
            context.startActivity(view)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

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
