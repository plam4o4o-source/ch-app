package org.chyavorec.app.data.local

import android.content.Context
import android.database.SQLException
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.data.catalog.CatalogIndexColumns
import org.chyavorec.data.catalog.CatalogIndexQuery
import org.chyavorec.data.catalog.CatalogIndexRow
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogIndexStore
import org.chyavorec.domain.model.CatalogBook

/**
 * Един запис от каталога: книгата, нормализираните полета за търсене (`n_*`, те са и
 * съдържанието на FTS таблицата) и българските азбучни рангове за подреждане.
 * [ord] е мястото в katalog.json и псевдоним на rowid (връзката с FTS таблицата).
 * Индексите са за справките за отделни книги (инв. №, заглавие + автор, автор, ISBN).
 */
@Entity(
    tableName = CatalogIndexColumns.TABLE,
    indices = [Index("inv"), Index("title", "author"), Index("author"), Index("isbn_digits")],
)
data class CatalogBookEntity(
    @PrimaryKey val ord: Int,
    val inv: Long,
    val author: String,
    val title: String,
    val subtitle: String,
    val city: String,
    val publisher: String,
    val year: String,
    @ColumnInfo(name = "doc_type") val docType: String,
    val language: String,
    val udc: String,
    @ColumnInfo(name = "call_number") val callNumber: String,
    val department: String,
    val keywords: String,
    val annotation: String,
    @ColumnInfo(name = "cover_url") val coverUrl: String,
    val available: Boolean,
    @ColumnInfo(name = "registered_on") val registeredOn: String,
    val isbn: String,
    @ColumnInfo(name = CatalogIndexColumns.TITLE) val nTitle: String,
    @ColumnInfo(name = CatalogIndexColumns.AUTHOR) val nAuthor: String,
    @ColumnInfo(name = CatalogIndexColumns.KEYWORDS) val nKeywords: String,
    @ColumnInfo(name = CatalogIndexColumns.ANNOTATION) val nAnnotation: String,
    @ColumnInfo(name = CatalogIndexColumns.PUBLISHER) val nPublisher: String,
    @ColumnInfo(name = CatalogIndexColumns.DOC_TYPE) val nDocType: String,
    @ColumnInfo(name = CatalogIndexColumns.CALL_NUMBER) val nCallNumber: String,
    @ColumnInfo(name = CatalogIndexColumns.INV) val nInv: String,
    @ColumnInfo(name = CatalogIndexColumns.ISBN) val nIsbn: String,
    @ColumnInfo(name = "isbn_digits") val isbnDigits: String,
    @ColumnInfo(name = "year_num") val yearNum: Int?,
    @ColumnInfo(name = "udc_section") val udcSection: Int?,
    @ColumnInfo(name = "title_rank") val titleRank: Int,
    @ColumnInfo(name = "author_rank") val authorRank: Int,
) {
    fun toRow(): CatalogIndexRow = CatalogIndexRow(
        ord = ord,
        book = CatalogBook(
            inv = inv, author = author, title = title, subtitle = subtitle, city = city, publisher = publisher,
            year = year, docType = docType, language = language, udc = udc, callNumber = callNumber,
            department = department, keywords = keywords, annotation = annotation, coverUrl = coverUrl,
            available = available, registeredOn = registeredOn, isbn = isbn,
        ),
        title = nTitle, author = nAuthor, keywords = nKeywords, annotation = nAnnotation, publisher = nPublisher,
        docType = nDocType, callNumber = nCallNumber, invText = nInv, isbn = nIsbn, isbnDigits = isbnDigits,
        year = yearNum, udcSection = udcSection, titleRank = titleRank, authorRank = authorRank,
    )

    companion object {
        fun of(r: CatalogIndexRow): CatalogBookEntity {
            val b = r.book
            return CatalogBookEntity(
                ord = r.ord, inv = b.inv, author = b.author, title = b.title, subtitle = b.subtitle, city = b.city,
                publisher = b.publisher, year = b.year, docType = b.docType, language = b.language, udc = b.udc,
                callNumber = b.callNumber, department = b.department, keywords = b.keywords, annotation = b.annotation,
                coverUrl = b.coverUrl, available = b.available, registeredOn = b.registeredOn, isbn = b.isbn,
                nTitle = r.title, nAuthor = r.author, nKeywords = r.keywords, nAnnotation = r.annotation,
                nPublisher = r.publisher, nDocType = r.docType, nCallNumber = r.callNumber, nInv = r.invText,
                nIsbn = r.isbn, isbnDigits = r.isbnDigits, yearNum = r.year, udcSection = r.udcSection,
                titleRank = r.titleRank, authorRank = r.authorRank,
            )
        }
    }
}

/**
 * Пълнотекстовият индекс (FTS4, external content върху [CatalogBookEntity] — текстът не
 * се пази втори път; Room синхронизира с тригери). Текстът вече е нормализиран в Kotlin
 * (малки букви, без ударения, „й“ запазено), затова е достатъчен стандартният unicode61
 * (наличен от API 21); `remove_diacritics=2` изисква SQLite 3.27 (API 30+) и тук не е нужен.
 */
@Fts4(contentEntity = CatalogBookEntity::class, tokenizer = FtsOptions.TOKENIZER_UNICODE61)
@Entity(tableName = CatalogIndexColumns.FTS_TABLE)
data class CatalogFtsEntity(
    @ColumnInfo(name = CatalogIndexColumns.TITLE) val nTitle: String,
    @ColumnInfo(name = CatalogIndexColumns.AUTHOR) val nAuthor: String,
    @ColumnInfo(name = CatalogIndexColumns.KEYWORDS) val nKeywords: String,
    @ColumnInfo(name = CatalogIndexColumns.ANNOTATION) val nAnnotation: String,
    @ColumnInfo(name = CatalogIndexColumns.PUBLISHER) val nPublisher: String,
    @ColumnInfo(name = CatalogIndexColumns.DOC_TYPE) val nDocType: String,
    @ColumnInfo(name = CatalogIndexColumns.CALL_NUMBER) val nCallNumber: String,
    @ColumnInfo(name = CatalogIndexColumns.INV) val nInv: String,
    @ColumnInfo(name = CatalogIndexColumns.ISBN) val nIsbn: String,
)

/** Един автор и нормализираният му вид (за глобалното търсене). */
data class CatalogAuthorRow(
    val author: String,
    @ColumnInfo(name = CatalogIndexColumns.AUTHOR) val nAuthor: String,
)

/** Състоянието на индекса ([CatalogIndexState] като JSON) — един ред. */
@Entity(tableName = CatalogIndexColumns.STATE_TABLE)
data class CatalogIndexStateEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "state_json") val json: String,
)

@Dao
abstract class CatalogIndexDao {
    @Query("SELECT state_json FROM catalog_index_state WHERE id = 0")
    abstract suspend fun stateJson(): String?

    @RawQuery
    abstract suspend fun rows(query: SupportSQLiteQuery): List<CatalogBookEntity>

    @RawQuery
    abstract suspend fun count(query: SupportSQLiteQuery): Int

    @Query("SELECT * FROM catalog_books WHERE inv = :inv ORDER BY ord DESC LIMIT 1")
    abstract suspend fun byInv(inv: Long): CatalogBookEntity?

    @Query("SELECT * FROM catalog_books WHERE inv IN (:invs) ORDER BY ord")
    abstract suspend fun byInvs(invs: List<Long>): List<CatalogBookEntity>

    @Query("SELECT * FROM catalog_books WHERE title = :title AND author = :author ORDER BY ord")
    abstract suspend fun byTitleAndAuthor(title: String, author: String): List<CatalogBookEntity>

    @Query("SELECT * FROM catalog_books WHERE author = :author ORDER BY ord")
    abstract suspend fun byAuthor(author: String): List<CatalogBookEntity>

    @Query("SELECT * FROM catalog_books WHERE isbn_digits IN (:digits) ORDER BY ord")
    abstract suspend fun byIsbn(digits: List<String>): List<CatalogBookEntity>

    /** Различните автори по първата им поява (n_author зависи само от author). */
    @Query("SELECT author, n_author FROM catalog_books WHERE author != '' GROUP BY author ORDER BY MIN(ord)")
    abstract suspend fun authors(): List<CatalogAuthorRow>

    @Query("DELETE FROM catalog_books")
    abstract suspend fun clearBooks()

    @Query("DELETE FROM catalog_index_state")
    abstract suspend fun clearState()

    @Insert
    abstract suspend fun insertBooks(rows: List<CatalogBookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putState(state: CatalogIndexStateEntity)

    /**
     * Цялото съдържание наведнъж, в една транзакция: прекъсване оставя стария индекс
     * непокътнат. Тригерите на FTS таблицата (external content) вървят в същата
     * транзакция, а четенето от други връзки (WAL) вижда стария индекс до края ѝ —
     * никога празен или наполовина попълнен.
     */
    @Transaction
    open suspend fun replaceAll(state: CatalogIndexStateEntity, rows: List<CatalogBookEntity>) {
        clearState()
        clearBooks()
        insertBooks(rows)
        putState(state)
    }

    /** „Изчисти кеша“: индексът става празен (FTS таблицата се изпразва от тригерите си). */
    @Transaction
    open suspend fun clearAll() {
        clearState()
        clearBooks()
    }
}

/**
 * Отделна база само за каталога (кеш, който се възстановява от katalog.json) — без
 * миграции; потребителските данни в [AppDatabase] не се засягат от промени тук.
 * Версия 2: индекси за справките (старата база се изтрива и се попълва отново).
 */
@Database(
    entities = [CatalogBookEntity::class, CatalogFtsEntity::class, CatalogIndexStateEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun index(): CatalogIndexDao

    companion object {
        const val NAME = "catalog-index.db"

        fun create(context: Context): CatalogDatabase =
            Room.databaseBuilder(context, CatalogDatabase::class.java, NAME).fallbackToDestructiveMigration(true).build()

        /** Изтрива файловете на базата (и журнала ѝ) и я създава наново — празна. */
        fun recreate(context: Context): CatalogDatabase {
            context.deleteDatabase(NAME)
            return create(context)
        }
    }
}

/**
 * [CatalogIndexStore] върху Room. Заявките за търсене (SQL + аргументи) идват от shared ([CatalogIndexQuery]).
 *
 * Базата е само кеш: ако не се отваря или чете (повреден файл, който SQLite не е изтрил сам,
 * схема, която Room не приема…), [recreate] веднъж в процеса я създава наново — празна, а
 * операцията се повтаря; индексът после се попълва отново от katalog.json. Втора грешка
 * стига до CatalogRepository, който тогава търси през индекса в паметта — без срив.
 */
class RoomCatalogIndexStore(
    private val recreate: (() -> CatalogDatabase)? = null,
    private val open: () -> CatalogDatabase,
) : CatalogIndexStore {
    private val lock = Any()
    private var db: CatalogDatabase? = null
    private var recreated = false

    private fun dao(): CatalogIndexDao = synchronized(lock) { db ?: open().also { db = it } }.index()

    private suspend fun <T> healing(block: suspend (CatalogIndexDao) -> T): T =
        try {
            block(dao())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if ((e !is SQLException && e !is IllegalStateException) || !reset(e)) throw e
            block(dao())
        }

    /** Затваря счупената база и отваря нова (само веднъж в процеса); `false` — не е пресъздадена. */
    private fun reset(cause: Exception): Boolean {
        val make = recreate ?: return false
        synchronized(lock) {
            if (recreated) return false
            recreated = true
            Log.w(TAG, "Индексът на каталога не се чете — базата се създава наново", cause)
            runCatching { db?.close() }
            db = null
            db = make()
        }
        return true
    }

    override suspend fun state(): CatalogIndexState? = healing { dao -> dao.stateJson()?.let { CatalogIndexState.fromJson(it) } }

    override suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>) = healing { dao ->
        dao.replaceAll(CatalogIndexStateEntity(json = state.toJson()), rows.map { CatalogBookEntity.of(it) })
    }

    override suspend fun clear() {
        healing { dao -> dao.clearAll() }
        // Освободеното място се връща на системата — иначе файлът на базата остава със същия размер.
        withContext(Dispatchers.IO) {
            runCatching { synchronized(lock) { db }?.openHelper?.writableDatabase?.execSQL("VACUUM") }
                .onFailure { Log.w(TAG, "Базата на каталога не е свита след изчистването", it) }
        }
    }

    override suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow> = healing { dao ->
        dao.rows(SimpleSQLiteQuery(query.sql, query.args.toTypedArray())).map(CatalogBookEntity::toRow)
    }

    override suspend fun count(query: CatalogIndexQuery): Int =
        healing { dao -> dao.count(SimpleSQLiteQuery(query.countSql, query.args.toTypedArray())) }

    override suspend fun book(inv: Long): CatalogBook? = healing { dao -> dao.byInv(inv)?.toRow()?.book }

    override suspend fun books(invs: Collection<Long>): List<CatalogBook> = healing { dao ->
        // На части — броят на параметрите в една заявка е ограничен (999 на по-старите версии).
        invs.distinct().chunked(MAX_ARGS).flatMap { dao.byInvs(it) }.sortedBy { it.ord }.map { it.toRow().book }
    }

    override suspend fun byTitleAndAuthor(title: String, author: String): List<CatalogBook> =
        healing { dao -> dao.byTitleAndAuthor(title, author).map { it.toRow().book } }

    override suspend fun byAuthor(author: String): List<CatalogBook> = healing { dao -> dao.byAuthor(author).map { it.toRow().book } }

    override suspend fun byIsbn(isbnDigits: Collection<String>): List<CatalogBook> =
        if (isbnDigits.isEmpty()) emptyList() else healing { dao -> dao.byIsbn(isbnDigits.toList()).map { it.toRow().book } }

    override suspend fun authors(): List<Pair<String, String>> = healing { dao -> dao.authors().map { it.author to it.nAuthor } }

    override suspend fun recommendationCandidates(udcSections: Set<Int>, authorWords: Set<String>?): List<CatalogBook> {
        // Само колоните за списъка — без анотацията и нормализираните полета (най-обемните).
        val sql = StringBuilder("SELECT ").append(CatalogIndexColumns.LIST_SELECT)
            .append(" FROM ").append(CatalogIndexColumns.TABLE).append(" AS b WHERE available = 1")
        val args = ArrayList<Any>()
        if (authorWords != null) {
            val any = buildList {
                if (udcSections.isNotEmpty()) add("udc_section IN (" + udcSections.joinToString(",") + ")")
                authorWords.forEach { add("instr(" + CatalogIndexColumns.AUTHOR + ", ?) > 0"); args += it }
            }
            if (any.isEmpty()) return emptyList()
            sql.append(" AND (").append(any.joinToString(" OR ")).append(')')
        }
        sql.append(" ORDER BY ord")
        return healing { dao -> dao.rows(SimpleSQLiteQuery(sql.toString(), args.toTypedArray())).map { it.toRow().book } }
    }

    private companion object {
        const val MAX_ARGS = 500
        const val TAG = "CatalogIndex"
    }
}
