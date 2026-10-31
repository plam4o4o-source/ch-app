package org.chyavorec.app.widget

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.domain.model.Loan
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlin.test.assertTrue

/**
 * Снимка за Google Play (play-07-widget.png, 1080×1920 като PlayScreenshotTest): уиджетите
 * на „начален екран“ — тапет, часовник, широкият уиджет (светъл, със срокове и събитие) и
 * малкият до иконата на приложението. Фирмените цветове (Android 8–11; от 12 — динамичните
 * на системата). tools/play_screenshots.py я рамкира в store/screenshots/07-widget.png.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w360dp-h640dp-xxhdpi", sdk = [30])
class PlayWidgetScreenshotTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val density get() = app.resources.displayMetrics.density

    /** Като примерния читател в PlayScreenshotTest: към 26.09.2026 — просрочена, след 4 дни, след 3 седмици. */
    private fun state(): WidgetState {
        val loans = listOf(
            Loan(loanId = "L1", inv = 7, title = "Под игото", author = "Иван Вазов", dueOn = "2026-09-30"),
            Loan(loanId = "L2", inv = 101, title = "Тютюн", author = "Димитър Димов", dueOn = "2026-09-24"),
            Loan(loanId = "L3", inv = 102, title = "Бай Ганьо", author = "Алеко Константинов", dueOn = "2026-10-17"),
        )
        val total = app.resources.getQuantityString(R.plurals.widget_books_to_return, loans.size, loans.size)
        val event = WidgetEvent("Ден на народните будители", "1 ноември, 11:00")
        return WidgetState(listOf(event), total, dueGroups(app, loans, LocalDate.parse("2026-09-26")))
    }

    private fun dp(v: Float) = v * density
    private fun dp(v: Int) = (v * density).roundToInt()

    /** Изгледът [wDp]×[hDp], нарисуван в [canvas] на ([xDp], [yDp]). */
    private fun draw(canvas: Canvas, views: RemoteViews, xDp: Int, yDp: Int, wDp: Int, hDp: Int): View {
        val v = views.apply(app, FrameLayout(app))
        v.measure(View.MeasureSpec.makeMeasureSpec(dp(wDp), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(dp(hDp), View.MeasureSpec.EXACTLY))
        v.layout(0, 0, dp(wDp), dp(hDp))
        canvas.save()
        canvas.translate(dp(xDp).toFloat(), dp(yDp).toFloat())
        v.draw(canvas)
        canvas.restore()
        return v
    }

    private fun View.texts(): List<String> = when {
        visibility != View.VISIBLE -> emptyList()
        this is TextView -> listOf(text.toString())
        this is ViewGroup -> (0 until childCount).flatMap { getChildAt(it).texts() }
        else -> emptyList()
    }

    @Test fun homeScreenWithWidgets() {
        val w = app.resources.displayMetrics.widthPixels
        val h = app.resources.displayMetrics.heightPixels
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        wallpaper(canvas, w.toFloat(), h.toFloat())

        val white = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        // Лентата на състоянието и часовникът с датата (като на началния екран).
        white.textSize = dp(13f)
        white.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        canvas.drawText("10:00", dp(20f), dp(24f), white)
        white.textSize = dp(64f)
        white.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        canvas.drawText("10:00", dp(24f), dp(118f), white)
        white.textSize = dp(16f)
        white.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        canvas.drawText("събота, 26 септември", dp(28f), dp(146f), white)

        val state = state()
        val wide = draw(canvas, WidgetViews.build(app, state, WidgetViews.FullWideTallSize), 10, 176, 340, 196)
        val small = draw(canvas, WidgetViews.build(app, state, WidgetViews.SmallSize), 10, 388, 164, 164)

        // Иконата на приложението вдясно от малкия уиджет: емблемата върху бял кръг и името.
        val cx = dp(268f)
        val cy = dp(452f)
        canvas.drawCircle(cx, cy, dp(30f), white)
        app.getDrawable(R.drawable.logo_chitalishte_small)!!.apply {
            setBounds((cx - dp(24f)).roundToInt(), (cy - dp(24f)).roundToInt(), (cx + dp(24f)).roundToInt(), (cy + dp(24f)).roundToInt())
            draw(canvas)
        }
        white.textSize = dp(13f)
        white.textAlign = Paint.Align.CENTER
        white.setShadowLayer(dp(2f), 0f, dp(1f), Color.argb(90, 0, 0, 0))
        canvas.drawText(app.getString(R.string.widget_label_short), cx, cy + dp(50f), white)

        // Полето за търсене най-долу (без марка).
        val pill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(70, 255, 255, 255) }
        canvas.drawRoundRect(RectF(dp(20f), dp(574f), w - dp(20f), dp(574f + 50f)), dp(25f), dp(25f), pill)

        // Основното съдържание е на снимката (иначе в магазина отива празен кадър).
        val shown = wide.texts() + small.texts()
        listOf("Читалище Яворец", "3 книги за връщане", "просрочена", "след 4 дни", "Ден на народните будители", "Сканирай")
            .forEach { assertTrue(it in shown, shown.toString()) }

        val out = File("build/reports/screenshots").apply { mkdirs() }
        File(out, "play-07-widget.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Тапет: здрач над хълмове — от тъмносиньо горе към топло отдолу, два пласта хълмове. */
    private fun wallpaper(canvas: Canvas, w: Float, h: Float) {
        val sky = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, h,
                intArrayOf(Color.rgb(0x1F, 0x3A, 0x5C), Color.rgb(0x5B, 0x6E, 0x9A), Color.rgb(0xD8, 0x9A, 0x7A)),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP,
            )
        }
        canvas.drawRect(0f, 0f, w, h, sky)
        hill(canvas, w, h, top = 0.70f, peak = 0.62f, color = Color.rgb(0x6E, 0x4A, 0x5E))
        hill(canvas, w, h, top = 0.82f, peak = 0.76f, color = Color.rgb(0x45, 0x2A, 0x3C))
    }

    private fun hill(canvas: Canvas, w: Float, h: Float, top: Float, peak: Float, color: Int) {
        val path = Path().apply {
            moveTo(0f, h * top)
            cubicTo(w * 0.3f, h * peak, w * 0.6f, h * (top + 0.04f), w, h * (peak + 0.02f))
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color })
    }
}
