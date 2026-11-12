package org.chyavorec.app.data.local

import kotlinx.coroutines.flow.Flow
import org.chyavorec.core.AppClock
import org.chyavorec.domain.model.DiaryEntry
import org.chyavorec.domain.model.ReadingDiary

/** Книгата, за която се води записът: заглавие и автор (ключът), последният инв. № и годината на заемане. */
data class DiaryBook(val title: String, val author: String, val inv: Long?, val readYear: Int?) {
    val key: String get() = ReadingDiary.bookKey(title, author)
}

fun ReadingDiaryEntity.toEntry(): DiaryEntry = DiaryEntry(bookKey, title, author, inv, rating, note, readYear, ratedAtMillis)

/**
 * „Читателски дневник“ на телефона: оценка (1–5) и бележка на ред за прочетена книга. Всички записи
 * са през [dbWrite] (грешка на базата не сривa екрана); четенията — през [orOnDbError]. Трие се
 * при изход, както историята на четенето (AppContainer.clearReaderTraces).
 */
class ReadingDiaryStore(private val dao: ReadingDiaryDao, private val clock: AppClock) {
    val entries: Flow<List<ReadingDiaryEntity>> = dao.observeAll().orOnDbError(emptyList())

    fun observeFor(book: DiaryBook): Flow<ReadingDiaryEntity?> = dao.observeFor(book.key, book.inv).orOnDbError(null)

    /** Оценките за препоръките ([org.chyavorec.data.catalog.ReadingRecommender]): ключ на книгата → 1–5. */
    suspend fun ratings(): Map<String, Int> =
        runCatching { ReadingDiary.ratings(dao.all().map { it.toEntry() }) }.getOrDefault(emptyMap())

    /** Оценка 1–5; бележката на книгата се пази. */
    suspend fun rate(book: DiaryBook, rating: Int) = write(book) { old -> (old ?: blank(book)).copy(rating = ReadingDiary.clampRating(rating)) }

    /** Бележка (един ред, най-много [ReadingDiary.MAX_NOTE] знака); без оценка се записва като „пропусната“ (0). */
    suspend fun setNote(book: DiaryBook, note: String) = write(book) { old -> (old ?: blank(book)).copy(note = ReadingDiary.cleanNote(note)) }

    /** „Не сега“ — повече не се пита за тази книга; оценка няма. */
    suspend fun dismiss(book: DiaryBook) = write(book) { old -> old ?: blank(book) }

    suspend fun remove(book: DiaryBook) {
        dbWrite { dao.delete(book.key) }
    }

    private fun blank(book: DiaryBook) =
        ReadingDiaryEntity(book.key, book.title, book.author, book.inv, rating = 0, note = "", readYear = book.readYear, ratedAtMillis = 0)

    private suspend fun write(book: DiaryBook, change: (ReadingDiaryEntity?) -> ReadingDiaryEntity) {
        dbWrite {
            val old = dao.get(book.key)
            val next = change(old)
            dao.upsert(next.copy(inv = book.inv ?: next.inv, readYear = book.readYear ?: next.readYear, ratedAtMillis = clock.now().toEpochMilli()))
        }
    }
}
