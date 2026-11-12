package org.chyavorec.app.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.GeneratedCover
import org.chyavorec.app.ui.theme.ThemePalette
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Генерираната корица на книга като Bitmap за уиджета — същият вид като BookCover в приложението
 * (фон по автора от палитрата, Cormorant за заглавието, Raleway за автора, лентата шевица, гръбче),
 * само че с android.graphics: RemoteViews не може да рисува с Compose. Всички размери са в единици
 * на корица от 72 единици ширина — като в BookCover. Оскъдни записи (без автор) са добре.
 */
internal object BookWidgetCover {
    /** Височината спрямо ширината — като корицата в приложението. */
    const val RATIO = 1.42f

    fun render(context: Context, book: BookPick, widthPx: Int, palette: ThemePalette, dark: Boolean): Bitmap {
        val w = max(widthPx, 24)
        val h = (w * RATIO).roundToInt()
        val u = w / 72f
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val wf = w.toFloat()
        val hf = h.toFloat()
        val base = GeneratedCover.backgroundArgb(book.author.ifBlank { book.title }, palette.coverAnchors, dark)
        val ink = GeneratedCover.INK

        // Всичко върху корицата — в слой, после заоблените ъгли (3 отляво, 8 отдясно) го изрязват с маска.
        val layer = canvas.saveLayer(0f, 0f, wf, hf, null)
        val fill = Paint(Paint.ANTI_ALIAS_FLAG)
        fill.shader = LinearGradient(
            0f, 0f, 0f, hf,
            intArrayOf(ColorUtils.blendARGB(base, Color.WHITE, 0.08f), base, darken(base, 0.78f)),
            floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP,
        )
        canvas.drawRect(0f, 0f, wf, hf, fill)
        // Мека светлина горе вляво.
        fill.shader = RadialGradient(wf * 0.3f, hf * 0.22f, max(wf, hf) * 0.8f, Color.argb(26, 255, 255, 255), Color.TRANSPARENT, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, wf, hf, fill)
        // По-тъмно дъно — авторът се чете и върху по-светлите тонове.
        fill.shader = LinearGradient(0f, hf * 0.55f, 0f, hf, Color.TRANSPARENT, Color.argb(89, 0, 0, 0), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, wf, hf, fill)
        fill.shader = null

        // Вътрешната „релефна“ рамка на твърда корица.
        val line = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = max(1f, 0.75f * u)
            color = Color.argb(56, 255, 255, 255)
        }
        canvas.drawRoundRect(RectF(9 * u, 4 * u, wf - 4 * u, hf - 4 * u), 2 * u, 2 * u, line)

        val left = 12 * u
        val textWidth = (wf - left - 7 * u).roundToInt().coerceAtLeast(1)
        val serif = ResourcesCompat.getFont(context, R.font.cormorant_garamond_semibold) ?: Typeface.SERIF
        val sans = ResourcesCompat.getFont(context, R.font.raleway_regular) ?: Typeface.SANS_SERIF

        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { color = ColorUtils.setAlphaComponent(ink, 235); typeface = sans; textSize = 8.5f * u }
        val author = book.author.takeIf { it.isNotBlank() }?.let { layout(it, authorPaint, textWidth, maxLines = 2, spacing = 1.15f) }
        val authorTop = hf - 10 * u - (author?.height ?: 0)
        author?.let {
            canvas.save()
            canvas.translate(left, authorTop)
            it.draw(canvas)
            canvas.restore()
        }
        val stripTop = authorTop - 5 * u - 6 * u
        stitches(canvas, left, stripTop, wf - 7 * u, 6 * u, u)

        // Заглавието заема мястото до лентата (в приложението — Spacer с тегло); и в тесен уиджет не я застъпва.
        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { color = ink; typeface = serif; textSize = 13.5f * u }
        val lineHeight = (titlePaint.descent() - titlePaint.ascent()) * 1.07f
        val titleLines = ((stripTop - 11 * u) / lineHeight).toInt().coerceIn(1, 4)
        val title = layout(book.title, titlePaint, textWidth, maxLines = titleLines, spacing = 1.07f)
        canvas.save()
        canvas.translate(left, 9 * u)
        title.draw(canvas)
        canvas.restore()

        // Гръбче отляво.
        fill.shader = LinearGradient(0f, 0f, 7 * u, 0f, Color.argb(71, 0, 0, 0), Color.TRANSPARENT, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, 7 * u, hf, fill)

        val mask = Paint(Paint.ANTI_ALIAS_FLAG).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) }
        val r3 = 3 * u
        val r8 = 8 * u
        val shape = Path().apply {
            addRoundRect(RectF(0f, 0f, wf, hf), floatArrayOf(r3, r3, r8, r8, r8, r8, r3, r3), Path.Direction.CW)
        }
        canvas.drawPath(shape, mask)
        canvas.restoreToCount(layer)
        return bmp
    }

    /** Редът кръстат бод по цялата ширина — светло злато, полупрозрачно; ъглите на двете нишки са по 2 единици. */
    private fun stitches(canvas: Canvas, x0: Float, y0: Float, x1: Float, height: Float, u: Float) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = max(1f, 0.8f * u)
            color = Color.argb(199, 232, 213, 163)
        }
        val rule = Paint(p).apply { strokeWidth = max(1f, 0.6f * u) }
        canvas.drawLine(x0, y0, x1, y0, rule)
        canvas.drawLine(x0, y0 + height, x1, y0 + height, rule)
        val cell = 3.2f * u
        val pad = 1.0f * u
        var x = x0 + pad
        while (x + cell <= x1) {
            canvas.drawLine(x, y0 + pad + u, x + cell - pad, y0 + height - pad - u, p)
            canvas.drawLine(x, y0 + height - pad - u, x + cell - pad, y0 + pad + u, p)
            x += cell + 0.4f * u
        }
    }

    private fun layout(text: String, paint: TextPaint, width: Int, maxLines: Int, spacing: Float): StaticLayout =
        StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(maxLines)
            .setEllipsize(TextUtils.TruncateAt.END)
            .setLineSpacing(0f, spacing)
            .build()

    private fun darken(color: Int, f: Float): Int =
        Color.argb(Color.alpha(color), (Color.red(color) * f).roundToInt(), (Color.green(color) * f).roundToInt(), (Color.blue(color) * f).roundToInt())
}
