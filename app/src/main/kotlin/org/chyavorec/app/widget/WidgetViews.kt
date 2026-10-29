package org.chyavorec.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.SizeF
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.domain.model.DueStatus
import kotlin.math.roundToInt

/**
 * RemoteViews на уиджета от оформленията layout/widget_small и layout/widget_wide.
 * Размери: малък 2×2 — един главен ред (следващ срок или събитие); широк 4×2 — срокове +
 * събитие + бутони; широк и висок — до 3 реда срокове. От Android 12 — карта „размер →
 * изглед“ (системата избира най-големия, който се побира); по-рано — по размера от
 * опциите на копието, отделно за хоризонтален и вертикален екран.
 * Цветове: от ресурсите (Android 12+ — динамичните на системата, иначе фирмените; ден/нощ).
 */
internal object WidgetViews {
    val SmallSize = SizeF(110f, 100f)
    val WideSize = SizeF(250f, 100f)
    val WideTallSize = SizeF(250f, 150f)
    private val Sizes = listOf(SmallSize, WideSize, WideTallSize)

    /** Един и същ код за всички PendingIntent-и на уиджета — различават се по intent-а (адрес/компонент). */
    private const val REQUEST_CODE = 900

    private val GroupRows = listOf(
        Triple(R.id.widget_group1, R.id.widget_group1_chip, R.id.widget_group1_text),
        Triple(R.id.widget_group2, R.id.widget_group2_chip, R.id.widget_group2_text),
        Triple(R.id.widget_group3, R.id.widget_group3_chip, R.id.widget_group3_text),
    )

    /** Изгледът за копие с опции [options] (AppWidgetManager.getAppWidgetOptions). */
    fun forWidget(context: Context, state: WidgetState, options: Bundle?): RemoteViews {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return RemoteViews(Sizes.associateWith { build(context, state, it) })
        }
        val minW = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) ?: 0
        val minH = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) ?: 0
        val maxW = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) ?: 0
        val maxH = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT) ?: 0
        // Без размер (още не е съобщен) — като минималния в widget_info (широкият).
        if (minW <= 0 || minH <= 0) return build(context, state, WideSize)
        val landscape = pick(SizeF(maxW.toFloat(), minH.toFloat()))
        val portrait = pick(SizeF(minW.toFloat(), maxH.toFloat()))
        return if (landscape == portrait) build(context, state, landscape)
        else RemoteViews(build(context, state, landscape), build(context, state, portrait))
    }

    /** Най-големият от размерите, който се побира в [available]; ако никой — най-малкият. */
    internal fun pick(available: SizeF): SizeF =
        Sizes.filter { it.width <= available.width && it.height <= available.height }.maxByOrNull { it.width * it.height }
            ?: Sizes.minBy { it.width * it.height }

    /** Изгледът за един от размерите [SmallSize], [WideSize], [WideTallSize]. */
    fun build(context: Context, state: WidgetState, size: SizeF): RemoteViews =
        if (size.width >= WideSize.width) wide(context, state, maxGroups = if (size.height >= WideTallSize.height) 3 else 2)
        else small(context, state)

    /** Малкият вариант (2×2): следващият срок, а без заемания — следващото събитие. */
    private fun small(context: Context, state: WidgetState): RemoteViews {
        val v = RemoteViews(context.packageName, R.layout.widget_small)
        v.setOnClickPendingIntent(android.R.id.background, openApp(context))
        val nearest = state.groups.firstOrNull()
        if (state.loansTotal != null && nearest != null) {
            v.setViewVisibility(R.id.widget_loans, View.VISIBLE)
            v.setViewVisibility(R.id.widget_event_column, View.GONE)
            v.setTextViewText(R.id.widget_countdown, nearest.countdown)
            v.textColor(context, R.id.widget_countdown, chipText(nearest.status))
            v.setTextViewText(R.id.widget_due_line, nearest.line)
            v.setViewVisibility(R.id.widget_loans_total, if (state.groups.size > 1) View.VISIBLE else View.GONE)
            v.setTextViewText(R.id.widget_loans_total, state.loansTotal)
            v.setOnClickPendingIntent(R.id.widget_loans, link(context, "my/loans"))
        } else {
            v.setViewVisibility(R.id.widget_loans, View.GONE)
            v.setViewVisibility(R.id.widget_event_column, View.VISIBLE)
            event(context, v, state, titleSp = 15f)
        }
        return v
    }

    /** Широкият вариант (4×2): бутони, до [maxGroups] срока и следващото събитие. */
    private fun wide(context: Context, state: WidgetState, maxGroups: Int): RemoteViews {
        val v = RemoteViews(context.packageName, R.layout.widget_wide)
        v.setOnClickPendingIntent(android.R.id.background, openApp(context))
        v.setOnClickPendingIntent(R.id.widget_scan, link(context, "scan"))
        v.setOnClickPendingIntent(R.id.widget_catalog, link(context, "catalog"))
        val loans = state.hasLoans
        v.setViewVisibility(R.id.widget_loans, if (loans) View.VISIBLE else View.GONE)
        // Половината от отстъпа между колоните (другата е в колоната на заеманията).
        val gap = if (loans) (6 * context.resources.displayMetrics.density).roundToInt() else 0
        v.setViewPadding(R.id.widget_event_column, gap, 0, 0, 0)
        if (loans) {
            v.setTextViewText(R.id.widget_loans_total, state.loansTotal)
            val shown = state.groups.take(maxGroups)
            GroupRows.forEachIndexed { i, (row, chip, text) ->
                val g = shown.getOrNull(i)
                v.setViewVisibility(row, if (g != null) View.VISIBLE else View.GONE)
                if (g != null) {
                    v.setTextViewText(chip, g.countdown)
                    v.setInt(chip, "setBackgroundResource", chipBackground(g.status))
                    v.textColor(context, chip, chipText(g.status))
                    v.setTextViewText(text, g.short)
                }
            }
            v.setOnClickPendingIntent(R.id.widget_loans, link(context, "my/loans"))
        }
        event(context, v, state, titleSp = 14f)
        return v
    }

    /** Следващото събитие (докосване — към „Събития“) или „Няма предстоящи събития“. */
    private fun event(context: Context, v: RemoteViews, state: WidgetState, titleSp: Float) {
        val title = state.eventTitle
        v.setViewVisibility(R.id.widget_event, if (title != null) View.VISIBLE else View.GONE)
        v.setViewVisibility(R.id.widget_no_events, if (title == null) View.VISIBLE else View.GONE)
        if (title == null) return
        v.setTextViewText(R.id.widget_event_title, title)
        v.setTextViewTextSize(R.id.widget_event_title, TypedValue.COMPLEX_UNIT_SP, titleSp)
        v.setViewVisibility(R.id.widget_event_date, if (state.eventDate != null) View.VISIBLE else View.GONE)
        v.setTextViewText(R.id.widget_event_date, state.eventDate.orEmpty())
        v.setOnClickPendingIntent(R.id.widget_event, link(context, "events"))
    }

    @DrawableRes
    private fun chipBackground(status: DueStatus?): Int = when (status) {
        DueStatus.OVERDUE -> R.drawable.widget_chip_overdue
        DueStatus.DUE_SOON -> R.drawable.widget_chip_soon
        else -> R.drawable.widget_chip_ok
    }

    @ColorRes
    internal fun chipText(status: DueStatus?): Int = when (status) {
        DueStatus.OVERDUE -> R.color.widget_overdue_text
        DueStatus.DUE_SOON -> R.color.widget_soon_text
        else -> R.color.widget_ok_text
    }

    /**
     * Цвят на текста от ресурс: от Android 12 се чете при показването (ден/нощ и динамичните
     * цветове на началния екран); по-рано — сега, по темата на системата.
     */
    private fun RemoteViews.textColor(context: Context, @IdRes id: Int, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) setColorStateList(id, "setTextColor", color)
        else setTextColor(id, context.getColor(color))
    }

    /** Отваря приложението на дадена връзка — през собствения (неекспортиран) вход, като известията. */
    private fun link(context: Context, link: String): PendingIntent =
        activity(context, LaunchIntents.openLink(context, link))

    private fun openApp(context: Context): PendingIntent =
        activity(context, Intent(context, MainActivity::class.java).setAction(Intent.ACTION_MAIN))

    /** Изричен intent, неизменим PendingIntent. */
    private fun activity(context: Context, intent: Intent): PendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}
