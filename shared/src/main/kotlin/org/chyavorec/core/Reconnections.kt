package org.chyavorec.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf

/** Колко дълго връзката трябва да остане налична, преди да се смята за „върната“. */
const val RECONNECT_STABLE_MILLIS = 2_000L

/**
 * „Връзката се върна“: по едно събитие при всеки преход офлайн → онлайн, след като
 * връзката е останала налична [stableMillis] (кратко „мигане“ на мрежата не опреснява
 * нищо). Началното „онлайн“ не е връщане — събитие има само след видяно „офлайн“.
 * Ново „офлайн“ преди изтичането на [stableMillis] отлага събитието до следващото „онлайн“.
 *
 * [foreground] — само докато приложението е на преден план: във фонов режим мрежата не се
 * наблюдава изобщо (събирането на `this` спира — напр. системният NetworkCallback се
 * отписва) и събития няма. Видяно „офлайн“ се помни: ако при връщане на преден план
 * връзката е налична, идва ЕДНО събитие (след [stableMillis]) — опресняването, пропуснато
 * във фонов режим. Изчакването, прекъснато от скриване, не праща нищо във фонов режим.
 */
fun Flow<Boolean>.reconnections(
    stableMillis: Long = RECONNECT_STABLE_MILLIS,
    foreground: Flow<Boolean> = flowOf(true),
): Flow<Unit> = channelFlow {
    val online = this@reconnections
    var wasOffline = false
    foreground.distinctUntilChanged().collectLatest { visible ->
        if (!visible) return@collectLatest
        online.distinctUntilChanged().collectLatest { on ->
            if (!on) {
                wasOffline = true
            } else if (wasOffline) {
                // collectLatest прекратява изчакването, ако връзката пак изчезне (или приложението се скрие).
                delay(stableMillis)
                wasOffline = false
                send(Unit)
            }
        }
    }
}
