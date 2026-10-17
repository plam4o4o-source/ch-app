package org.chyavorec.app.data.local

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/** Запазена (любима) новина — достатъчно за показване и офлайн. */
@Entity(tableName = "favorite_news")
data class FavoriteNewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val summary: String,
    val category: String?,
    val publishedAtMillis: Long?,
    val savedAtMillis: Long,
)

/** Напомняне за събитие (планирано с WorkManager). */
@Entity(tableName = "event_reminders")
data class EventReminderEntity(
    @PrimaryKey val eventId: String,
    val title: String,
    val remindAtMillis: Long,
)

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_news ORDER BY savedAtMillis DESC")
    fun observeAll(): Flow<List<FavoriteNewsEntity>>

    @Query("SELECT id FROM favorite_news")
    fun observeIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: FavoriteNewsEntity)

    @Query("DELETE FROM favorite_news WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM favorite_news")
    suspend fun clear()
}

@Dao
interface RemindersDao {
    @Query("SELECT eventId FROM event_reminders")
    fun observeIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: EventReminderEntity)

    @Query("DELETE FROM event_reminders WHERE eventId = :id")
    suspend fun delete(id: String)
}

/**
 * Данните на потребителя (любими новини, напомняния за събития) — за разлика от индекса на
 * каталога ([CatalogDatabase]) не могат да се попълнят отново, затова НЯМА изтриване при
 * смяна на схемата: всяка нова версия идва с миграция в [MIGRATIONS] и с изнесената схема
 * в app/schemas (проверява се в AppDatabaseMigrationTest с MigrationTestHelper).
 */
@Database(entities = [FavoriteNewsEntity::class, EventReminderEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favorites(): FavoritesDao
    abstract fun reminders(): RemindersDao

    companion object {
        const val NAME = "chitalishte.db"

        /** Миграциите между версиите на схемата (при версия 2: `Migration(1, 2) { … }`). */
        val MIGRATIONS: Array<Migration> = emptyArray()

        /**
         * Само при връщане към по-стара версия на приложението (по-нова схема на диска, за която
         * тази версия няма миграция) базата се създава наново — иначе приложението не би тръгнало.
         * Файл, който SQLite обяви за повреден (не се чете изобщо), се изтрива от стандартния
         * обработчик на Android/Room и базата започва празна. Всяка друга грешка при отваряне не
         * трие нищо: четенията минават през [orOnDbError], а записите — през [dbWrite].
         */
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, NAME)
                .addMigrations(*MIGRATIONS)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .build()
    }
}

/**
 * Грешка на базата (не се отваря, пълна памет…) не бива да срива екрана при всяко отваряне:
 * потокът дава [fallback] (напр. празен списък), а грешката отива в лога. Данните не се трият.
 */
fun <T> Flow<T>.orOnDbError(fallback: T): Flow<T> = catch { e ->
    Log.w(DB_LOG_TAG, "Базата с потребителските данни не се чете", e)
    emit(fallback)
}

/** Запис в базата, чиято грешка само се записва в лога (без отмяната); `false` — неуспех. */
suspend fun dbWrite(block: suspend () -> Unit): Boolean =
    try {
        block()
        true
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w(DB_LOG_TAG, "Неуспешен запис в базата с потребителските данни", e)
        false
    }

private const val DB_LOG_TAG = "AppDatabase"
