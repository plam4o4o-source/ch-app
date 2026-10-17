package org.chyavorec.app.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.data.local.resetOnCorruption
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

/** Повреден файл с настройки не спира приложението — заменя се с празни настройки. */
class DataStoreCorruptionTest {
    @get:Rule val tmp = TemporaryFolder()

    @Test fun corruptPreferencesFileIsReplacedWithEmpty() = runBlocking {
        val file = tmp.newFile("settings.preferences_pb")
        file.writeBytes(byteArrayOf(0x0a, 0x7f, 0x13, 0x37) + ByteArray(64) { 0xFF.toByte() })
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        try {
            val store = PreferenceDataStoreFactory.create(corruptionHandler = resetOnCorruption("test"), scope = scope) { file }
            assertEquals(emptyPreferences(), store.data.first())
            // И после се пише нормално.
            val key = booleanPreferencesKey("onboarding_done")
            store.edit { it[key] = true }
            assertEquals(true, store.data.first()[key])
        } finally {
            scope.cancel()
        }
    }
}
