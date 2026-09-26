package org.chyavorec.app.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
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

@Database(entities = [FavoriteNewsEntity::class, EventReminderEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favorites(): FavoritesDao
    abstract fun reminders(): RemindersDao
}
