package org.chyavorec.app

import android.content.Intent
import android.util.Log

/*
 * ВРЕМЕННО (само в клона debug/w25-check, не е за издание): тестови флагове и дневник за проверката на
 * емулатор. Флаговете идват с `am start … --es w25 "noscrub,plainmedal"` и важат до края на процеса;
 * без тях приложението се държи както издаденото. Дневникът е с Log.w, защото R8 маха Log.d/i в release.
 */
object W25 {
    const val TAG = "W25"

    @Volatile
    var flags: Set<String> = emptySet()
        private set

    fun read(intent: Intent?) {
        val raw = intent?.getStringExtra("w25") ?: return
        flags = raw.split(',').map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        Log.w(TAG, "flags=$flags")
    }

    fun on(flag: String): Boolean = flag in flags

    fun log(msg: String) {
        Log.w(TAG, msg)
    }
}
