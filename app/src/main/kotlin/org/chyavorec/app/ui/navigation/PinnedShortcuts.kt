package org.chyavorec.app.ui.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.GeneratedCover
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Event

/** Разделите, които се закачат на началния екран (етикетите не носят лични данни). */
enum class PinSection(val link: String, val labelRes: Int, val iconRes: Int) {
    MY_BOOKS(DeepLinks.WANT_TO_READ, R.string.pin_my_books, R.drawable.ic_shortcut_book),
    EVENTS(DeepLinks.EVENTS, R.string.pin_events, R.drawable.ic_shortcut_event),
    NEWS("news", R.string.pin_news, R.drawable.ic_shortcut_news),
    CARD("my/card", R.string.pin_card, R.drawable.ic_shortcut_card),
    EMBROIDERY(DeepLinks.EMBROIDERY, R.string.pin_embroidery, R.drawable.ic_shortcut_shevitsa),
}

/**
 * „Добави на началния екран“: закачен пряк път ([ShortcutManagerCompat.requestPinShortcut],
 * Android 8+) към книга от публичния каталог, събитие, раздел или читателската карта. Води през
 * [LaunchIntents.ACTION_PIN] и вътрешните връзки ([DeepLinks]); картата се отваря през заключването
 * на личните екрани, а на пряк път за карта пише само „Читателска карта“. Книга от личен списък
 * не се закача (заглавието би издало избора на читателя).
 */
object PinnedShortcuts {
    private const val SHORT_LABEL_MAX = 20
    private const val LONG_LABEL_MAX = 40

    /** Страната на иконата на книгата (адаптивна 108 dp при ~2 px/dp) — малко растерно изображение, не повече. */
    internal const val ICON_PX = 216

    fun supported(context: Context): Boolean = runCatching { ShortcutManagerCompat.isRequestPinShortcutSupported(context) }.getOrDefault(false)

    /** Intent-ът на закачения пряк път към [link] — изрично към MainActivity. */
    fun intent(context: Context, link: String): Intent =
        Intent(LaunchIntents.ACTION_PIN, Uri.parse("${LaunchIntents.SHORTCUT_SCHEME}://app/$link"))
            .setComponent(ComponentName(context, MainActivity::class.java))

    fun book(context: Context, book: CatalogBook): ShortcutInfoCompat? {
        val title = book.title.trim().ifEmpty { return null }
        val long = if (book.author.isBlank()) title else "$title — ${book.author.trim()}"
        return ShortcutInfoCompat.Builder(context, "pin-book-${book.inv}")
            .setShortLabel(AppShortcuts.ellipsize(title, SHORT_LABEL_MAX))
            .setLongLabel(AppShortcuts.ellipsize(long, LONG_LABEL_MAX))
            .setIcon(runCatching { IconCompat.createWithAdaptiveBitmap(coverIcon(context, book)) }.getOrElse { IconCompat.createWithResource(context, R.drawable.ic_shortcut_book) })
            .setIntent(intent(context, DeepLinks.catalogBook(book.inv)))
            .build()
    }

    fun event(context: Context, event: Event): ShortcutInfoCompat? {
        val title = event.title.trim().ifEmpty { return null }
        return ShortcutInfoCompat.Builder(context, "pin-event-${Integer.toHexString(GeneratedCover.stableHash(event.id))}")
            .setShortLabel(AppShortcuts.ellipsize(title, SHORT_LABEL_MAX))
            .setLongLabel(AppShortcuts.ellipsize(context.getString(R.string.pin_event_long, title), LONG_LABEL_MAX))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_event))
            .setIntent(intent(context, DeepLinks.event(event.id)))
            .build()
    }

    fun section(context: Context, section: PinSection): ShortcutInfoCompat {
        val label = context.getString(section.labelRes)
        return ShortcutInfoCompat.Builder(context, "pin-${section.name.lowercase()}")
            .setShortLabel(AppShortcuts.ellipsize(label, SHORT_LABEL_MAX))
            .setLongLabel(AppShortcuts.ellipsize(label, LONG_LABEL_MAX))
            .setIcon(IconCompat.createWithResource(context, section.iconRes))
            .setIntent(intent(context, section.link))
            .build()
    }

    /**
     * Моли системата да закачи [info]. `false` — не се поддържа или launcher-ът отказа: екранът
     * показва тиха бележка. Иначе launcher-ът сам пита потребителя.
     */
    fun request(context: Context, info: ShortcutInfoCompat?): Boolean {
        if (info == null || !supported(context)) return false
        return runCatching { ShortcutManagerCompat.requestPinShortcut(context, info, null) }.getOrDefault(false)
    }

    /**
     * Евтина генерирана корица: фон по автора (като в приложението), златна рамка, първата буква
     * на заглавието и три ромбчета отдолу — в безопасната зона на адаптивната икона.
     */
    internal fun coverIcon(context: Context, book: CatalogBook): Bitmap {
        val s = ICON_PX
        val bitmap = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = 0xFFF5F0E8.toInt()
        canvas.drawRect(0f, 0f, s.toFloat(), s.toFloat(), paint)
        val anchors = UdcCoverColors.values.map { it.toArgb() }.toIntArray()
        val key = book.author.ifBlank { book.title }
        paint.color = GeneratedCover.backgroundArgb(key, anchors, dark = false)
        // Корицата 84×120 в центъра (безопасната зона е 144×144).
        val cover = RectF((s - 84) / 2f, (s - 120) / 2f, (s + 84) / 2f, (s + 120) / 2f)
        canvas.drawRoundRect(cover, 5f, 5f, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = 0xFFC9A84C.toInt()
        canvas.drawRoundRect(RectF(cover.left + 6, cover.top + 6, cover.right - 6, cover.bottom - 6), 3f, 3f, paint)
        paint.style = Paint.Style.FILL
        paint.color = GeneratedCover.INK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 58f
        paint.typeface = runCatching { ResourcesCompat.getFont(context, R.font.cormorant_garamond_semibold) }.getOrNull()
        val letter = book.title.trim().firstOrNull { it.isLetterOrDigit() }?.uppercaseChar()?.toString() ?: "К"
        canvas.drawText(letter, cover.centerX(), cover.centerY() + 18f, paint)
        paint.color = 0xFFC9A84C.toInt()
        for (i in -1..1) {
            val cx = cover.centerX() + i * 14f
            val cy = cover.bottom - 20f
            val d = Path().apply { moveTo(cx, cy - 4f); lineTo(cx + 4f, cy); lineTo(cx, cy + 4f); lineTo(cx - 4f, cy); close() }
            canvas.drawPath(d, paint)
        }
        return bitmap
    }
}
