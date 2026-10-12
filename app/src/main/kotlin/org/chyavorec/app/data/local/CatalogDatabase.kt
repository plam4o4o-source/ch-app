package org.chyavorec.app.data.local

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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
 */
@Entity(tableName = CatalogIndexColumns.TABLE)
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

    @Query("DELETE FROM catalog_books")
    abstract suspend fun clearBooks()

    @Query("DELETE FROM catalog_index_state")
    abstract suspend fun clearState()

    @Insert
    abstract suspend fun insertBooks(rows: List<CatalogBookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putState(state: CatalogIndexStateEntity)

    /** Цялото съдържание наведнъж: прекъсване оставя стария индекс непокътнат. */
    @Transaction
    open suspend fun replaceAll(state: CatalogIndexStateEntity, rows: List<CatalogBookEntity>) {
        clearState()
        clearBooks()
        insertBooks(rows)
        putState(state)
    }
}

/**
 * Отделна база само за каталога (кеш, който се възстановява от katalog.json) — без
 * миграции; потребителските данни в [AppDatabase] не се засягат от промени тук.
 */
@Database(
    entities = [CatalogBookEntity::class, CatalogFtsEntity::class, CatalogIndexStateEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun index(): CatalogIndexDao
}

/** [CatalogIndexStore] върху Room. Заявките (SQL + аргументи) идват от shared ([CatalogIndexQuery]). */
class RoomCatalogIndexStore(private val db: () -> CatalogDatabase) : CatalogIndexStore {
    private val dao by lazy { db().index() }

    override suspend fun state(): CatalogIndexState? = dao.stateJson()?.let { CatalogIndexState.fromJson(it) }

    override suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>) =
        dao.replaceAll(CatalogIndexStateEntity(json = state.toJson()), rows.map { CatalogBookEntity.of(it) })

    override suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow> =
        dao.rows(SimpleSQLiteQuery(query.sql, query.args.toTypedArray())).map(CatalogBookEntity::toRow)
}
