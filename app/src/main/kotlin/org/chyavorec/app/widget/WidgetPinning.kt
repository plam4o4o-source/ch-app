package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build

/** Кой от уиджетите се добавя на началния екран. */
enum class WidgetKind(internal val receiver: Class<*>) {
    /** „Читалище Яворец“ — срокове, събитие, бързо сканиране. */
    MAIN(ChitalishteWidgetReceiver::class.java),

    /** „Книга на деня“. */
    BOOK(BookOfTheDayWidgetReceiver::class.java),
}

/**
 * „Добави уиджета“ от настройките: системата пита читателя къде на началния екран да го сложи
 * (AppWidgetManager.requestPinAppWidget, Android 8+). Началните екрани, които не го позволяват,
 * не показват бутона.
 */
object WidgetPinning {
    /** Началният екран позволява добавяне на уиджет от приложението. */
    fun supported(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
        return runCatching { AppWidgetManager.getInstance(context)?.isRequestPinAppWidgetSupported == true }.getOrDefault(false)
    }

    /** Пита системата да добави уиджета; `false` — не е възможно (началният екран отказва или не поддържа). */
    fun request(context: Context, kind: WidgetKind): Boolean {
        if (!supported(context)) return false
        return runCatching {
            AppWidgetManager.getInstance(context).requestPinAppWidget(ComponentName(context, kind.receiver), null, null)
        }.getOrDefault(false)
    }
}
