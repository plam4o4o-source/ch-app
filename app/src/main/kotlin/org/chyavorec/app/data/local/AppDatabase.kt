package org.chyavorec.app.data.local

import android.content.Context
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
import kotlinx.coroutines.flow.Flow

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

    @Query("DELETE FROM event_reminders WHERE remindAtMillis < :now")
    suspend fun deleteOlderThan(now: Long)
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
         */
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, NAME)
                .addMigrations(*MIGRATIONS)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .build()
    }
}
