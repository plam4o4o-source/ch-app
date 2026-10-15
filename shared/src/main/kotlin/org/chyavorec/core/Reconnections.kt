package org.chyavorec.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

/** Колко дълго връзката трябва да остане налична, преди да се смята за „върната“. */
const val RECONNECT_STABLE_MILLIS = 2_000L

/**
 * „Връзката се върна“: по едно събитие при всеки преход офлайн → онлайн, след като
 * връзката е останала налична [stableMillis] (кратко „мигане“ на мрежата не опреснява
 * нищо). Началното „онлайн“ не е връщане — събитие има само след видяно „офлайн“.
 * Ново „офлайн“ преди изтичането на [stableMillis] отлага събитието до следващото „онлайн“.
 */
fun Flow<Boolean>.reconnections(stableMillis: Long = RECONNECT_STABLE_MILLIS): Flow<Unit> = channelFlow {
    var wasOffline = false
    distinctUntilChanged().collectLatest { online ->
        if (!online) {
            wasOffline = true
        } else if (wasOffline) {
            // collectLatest прекратява изчакването, ако връзката пак изчезне.
            delay(stableMillis)
            wasOffline = false
            send(Unit)
        }
    }
}
