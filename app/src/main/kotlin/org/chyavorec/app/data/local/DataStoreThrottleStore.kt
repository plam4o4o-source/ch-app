package org.chyavorec.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.chyavorec.data.repository.ThrottleState
import org.chyavorec.data.repository.ThrottleStore

/**
 * Отделен малък DataStore само за броячите на защитата от налучкване на ПИН-а — не се
 * изтрива при изход и при „Изчисти кеша“ (иначе защитата би се нулирала с едно докосване).
 * Не е лична информация: само брой опити и час на отключване.
 */
private val Context.throttleDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "login_throttle",
    corruptionHandler = resetOnCorruption("login_throttle"),
)

class DataStoreThrottleStore(private val context: Context) : ThrottleStore {
    private object Keys {
        val failures = intPreferencesKey("failures")
        val lockouts = intPreferencesKey("lockouts")
        val lockedUntil = longPreferencesKey("locked_until")
    }

    override suspend fun load(): ThrottleState? {
        val p = context.throttleDataStore.data.first()
        if (Keys.failures !in p && Keys.lockouts !in p && Keys.lockedUntil !in p) return null
        return ThrottleState(p[Keys.failures] ?: 0, p[Keys.lockouts] ?: 0, p[Keys.lockedUntil] ?: 0L)
    }

    override suspend fun save(state: ThrottleState) {
        context.throttleDataStore.edit {
            it[Keys.failures] = state.failures
            it[Keys.lockouts] = state.lockouts
            it[Keys.lockedUntil] = state.lockedUntilMillis
        }
    }
}
