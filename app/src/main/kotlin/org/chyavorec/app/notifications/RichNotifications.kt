package org.chyavorec.app.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Precision
import coil3.size.Scale
import coil3.toBitmap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.network.CoverNetworkPolicy
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.NewsArticle
import java.time.ZoneId

/**
 * Снимка и бутони за общите известия (нова новина, напомняне за събитие). Известията с
 * данни на читателя (заемания, лични съобщения) не минават оттук — те остават само с текст.
 */
object RichNotifications {
    /** По-дългата страна на снимката — BigPictureStyle и без това я смалява до ширината на екрана. */
    internal const val MAX_IMAGE_PX = 1024
    /** Фоновата задача не чака снимката повече от толкова — после известието е само с текст. */
    internal const val IMAGE_TIMEOUT_MS = 10_000L

    /**
     * Снимката от сайта за известието — през общия ImageLoader на приложението (дисковият кеш и
     * клиентът му), смалена до [MAX_IMAGE_PX] и софтуерна (известието я копира в системата).
     * При „Корици само по Wi-Fi“ и мрежа с отчитане на трафика — само ако вече е в кеша.
     * `null` (без адрес, грешка, изтекло време) — известието е само с текст.
     */
    suspend fun image(context: Context, c: AppContainer, url: String?): Bitmap? {
        if (url.isNullOrBlank() || !url.startsWith("https://")) return null
        return try {
            val network = CoverNetworkPolicy.networkAllowed(c.settings.current().coversWifiOnly, c.connectivity.isMetered())
            val request = ImageRequest.Builder(context)
                .data(url)
                .size(MAX_IMAGE_PX)
                .scale(Scale.FIT)
                .precision(Precision.INEXACT)
                .allowHardware(false)
                .apply { if (!network) networkCachePolicy(CachePolicy.DISABLED) }
                .build()
            val result = withTimeoutOrNull(IMAGE_TIMEOUT_MS) { SingletonImageLoader.get(context).execute(request) }
            (result as? SuccessResult)?.image?.toBitmap()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            null
        }
    }

    /** „Добави в календара“ (ако събитието е с дата) и „Маршрут“ (ако е с място). */
    fun eventActions(context: Context, event: Event, zone: ZoneId, notificationId: Int): List<NotificationCompat.Action> = listOfNotNull(
        Intents.calendarIntent(context, event, zone)?.let { activityAction(context, notificationId, R.string.event_add_calendar, it) },
        // Както от екрана на събитието: „<място>, с. Яворец“.
        event.place?.takeIf { it.isNotBlank() }?.let {
            activityAction(context, notificationId, R.string.notif_action_route, Intents.mapIntent("$it, с. Яворец"))
        },
    )

    /** „Сподели“ — заглавието и адресът на новината, както от екрана ѝ. */
    fun shareAction(context: Context, article: NewsArticle, notificationId: Int): NotificationCompat.Action? =
        activityAction(context, notificationId, R.string.action_share, Intents.shareIntent(context, article.title, article.title + "\n" + article.url))

    /**
     * Бутон, който отваря направо чуждото activity (календар, карта, „Сподели“) — без
     * BroadcastReceiver „трамплин“ (от Android 12 не може да стартира activity). PendingIntent-ът
     * е неизменим; различното действие/данни го отделят от този на докосването (същият requestCode).
     * Без приложение за действието — без бутон.
     */
    private fun activityAction(context: Context, requestCode: Int, label: Int, intent: Intent): NotificationCompat.Action? {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.action != Intent.ACTION_CHOOSER &&
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()
        ) return null
        val pending = PendingIntent.getActivity(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return NotificationCompat.Action.Builder(R.drawable.ic_notification, context.getString(label), pending).build()
    }
}
