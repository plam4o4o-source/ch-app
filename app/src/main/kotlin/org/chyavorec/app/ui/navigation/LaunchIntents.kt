package org.chyavorec.app.ui.navigation

import android.content.Intent

/**
 * Кога intent-ът, с който е отворен [org.chyavorec.app.MainActivity], носи *нова* връзка.
 *
 * На Android 8–11 „Назад“ от първия екран затваря activity-то, а задачата остава в
 * „Последни“. Отварянето ѝ оттам подава отново *първоначалния* intent на задачата (със
 * старото известие или адрес) с флага [Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY] —
 * той не е ново действие на потребителя и не бива да отваря екрана отново. Същото важи и
 * при пресъздаване (завъртане, спрян от системата процес): тогава intent-ът е вече
 * обработен, а неотработената връзка (ако има) идва от запазеното състояние.
 */
object LaunchIntents {
    /** Ключът в `savedInstanceState` за връзката, която още не е отворена. */
    const val KEY_PENDING_LINK = "pending_deep_link"

    /** [flags] — флаговете на intent-а; [restored] — activity-то е пресъздадено (има запазено състояние). */
    fun shouldHandle(flags: Int, restored: Boolean): Boolean =
        !restored && (flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0

    /**
     * Обикновен intent на мястото на обработения (за `setIntent`): без връзка, extra и
     * данни — повторно четене на `getIntent()` не намира нищо за отваряне.
     */
    fun consumed(intent: Intent): Intent =
        Intent(Intent.ACTION_MAIN).setComponent(intent.component)
}
