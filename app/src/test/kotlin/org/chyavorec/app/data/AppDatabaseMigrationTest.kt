package org.chyavorec.app.data

import android.app.Application
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteFullException
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.data.local.AppDatabase
import org.chyavorec.app.data.local.dbWrite
import org.chyavorec.app.data.local.orOnDbError
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Потребителската база ([AppDatabase]) срещу изнесените схеми в app/schemas (MigrationTestHelper).
 *
 * - Схемата на всяка версия е в Git; базата, създадена по нея, се отваря от приложението
 *   ([AppDatabase.create]) без грешка за цялостта — т.е. записаната схема и identityHash
 *   отговарят точно на кода.
 * - При нова версия: Migration(N, N+1) в [AppDatabase.MIGRATIONS] и тест тук —
 *   `createDatabase(NAME, N)` с данни → `runMigrationsAndValidate(NAME, N+1, true, *MIGRATIONS)` →
 *   данните са там. Без миграция приложението не тръгва (няма изтриване на данните).
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class AppDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), AppDatabase::class.java)

    private val context get() = ApplicationProvider.getApplicationContext<Application>()

    @Test fun version1SchemaMatchesTheCodeAndKeepsData() {
        helper.createDatabase(AppDatabase.NAME, 1).apply {
            execSQL(
                "INSERT INTO favorite_news (id, title, url, imageUrl, summary, category, publishedAtMillis, savedAtMillis) " +
                    "VALUES ('n1', 'Празник', 'https://chyavorec.org/news/1', NULL, 'Кратко', NULL, NULL, 5)",
            )
            execSQL("INSERT INTO event_reminders (eventId, title, remindAtMillis) VALUES ('e1', 'Концерт', 10)")
            close()
        }
        // Схемата на диска = записаната в 1.json (таблици, колони, NOT NULL, ключове).
        helper.runMigrationsAndValidate(AppDatabase.NAME, 1, true, *AppDatabase.MIGRATIONS).close()

        // Приложението отваря същата база (проверка на identityHash) и данните са непокътнати.
        val db = AppDatabase.create(context)
        try {
            runBlocking {
                assertEquals(listOf("n1"), db.favorites().observeIds().first())
                assertEquals(listOf("e1"), db.reminders().observeIds().first())
            }
        } finally {
            db.close()
        }
    }

    @Test fun databaseErrorsDoNotCrashTheScreens() = runBlocking {
        // Грешка при отваряне/четене: потокът дава резервната стойност, записът връща false.
        val broken = flow<List<String>> { throw SQLiteCantOpenDatabaseException("unable to open database file") }
        assertEquals(emptyList<String>(), broken.orOnDbError(emptyList()).first())
        assertFalse(dbWrite { throw SQLiteFullException("database or disk is full") })
        assertTrue(dbWrite { })
    }
}
