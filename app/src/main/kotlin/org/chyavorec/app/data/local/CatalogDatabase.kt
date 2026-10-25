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
import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import org.chyavorec.data.catalog.CatalogIndexColumns
import org.chyavorec.data.catalog.CatalogIndexHit
import org.chyavorec.data.catalog.CatalogIndexLostException
import org.chyavorec.data.catalog.CatalogIndexQuery
import org.chyavorec.data.catalog.CatalogIndexRow
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogIndexStore
import org.chyavorec.domain.model.CatalogBook

/**
 * Един запис от каталога: книгата, нормализираните полета за търсене (`n_*`, те са и
 * съдържанието на FTS таблицата) и българските азбучни рангове за подреждане.
 * [ord] е мястото в katalog.json и псевдоним на rowid (връзката с FTS таблицата).
 * Индексите са за справките за отделни книги (инв. №, заглавие + автор, автор, ISBN) и за
 * страниците на списъка без текст, подреден по заглавие, автор или „Най-нови“ (ORDER BY …
 * LIMIT 40 чете индекса наред, вместо да подрежда целия каталог за всяка страница; rowid
 * = [ord] е последната колона на всеки индекс).
 */
@Entity(
    tableName = CatalogIndexColumns.TABLE,
    indices = [
        Index("inv"), Index("title", "author"), Index("author"), Index("isbn_digits"),
        Index("title_rank"), Index("author_rank", "title_rank"),
        Index(value = ["registered_on", "inv"], orders = [Index.Order.DESC, Index.Order.DESC]),
    ],
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

/** Лекият изглед на запис ([CatalogIndexColumns.HIT]) — за точкуването на резултатите с текст. */
data class CatalogHitRow(
    val ord: Int,
    val inv: Long,
    @ColumnInfo(name = "registered_on") val registeredOn: String,
    @ColumnInfo(name = "year_num") val yearNum: Int?,
    @ColumnInfo(name = "title_rank") val titleRank: Int,
    @ColumnInfo(name = "author_rank") val authorRank: Int,
    @ColumnInfo(name = "isbn_digits") val isbnDigits: String,
    @ColumnInfo(name = CatalogIndexColumns.TITLE) val nTitle: String,
    @ColumnInfo(name = CatalogIndexColumns.AUTHOR) val nAuthor: String,
    @ColumnInfo(name = CatalogIndexColumns.KEYWORDS) val nKeywords: String,
    @ColumnInfo(name = CatalogIndexColumns.ANNOTATION) val nAnnotation: String,
    @ColumnInfo(name = CatalogIndexColumns.PUBLISHER) val nPublisher: String,
    @ColumnInfo(name = CatalogIndexColumns.DOC_TYPE) val nDocType: String,
    @ColumnInfo(name = CatalogIndexColumns.CALL_NUMBER) val nCallNumber: String,
    @ColumnInfo(name = CatalogIndexColumns.INV) val nInv: String,
    @ColumnInfo(name = CatalogIndexColumns.ISBN) val nIsbn: String,
) {
    fun toHit(): CatalogIndexHit = CatalogIndexHit(
        ord = ord, inv = inv, title = nTitle, author = nAuthor, keywords = nKeywords, annotation = nAnnotation,
        publisher = nPublisher, docType = nDocType, callNumber = nCallNumber, invText = nInv, isbn = nIsbn,
        isbnDigits = isbnDigits, year = yearNum, registeredOn = registeredOn, titleRank = titleRank, authorRank = authorRank,
    )
}

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
    abstract suspend fun hits(query: SupportSQLiteQuery): List<CatalogHitRow>

    @RawQuery
    abstract suspend fun count(query: SupportSQLiteQuery): Int

    @Query("SELECT * FROM catalog_books WHERE ord IN (:ords)")
    abstract suspend fun byOrds(ords: List<Int>): List<CatalogBookEntity>

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
}

/**
 * Тригерите, с които Room синхронизира FTS таблицата със съдържанието (external content) —
 * точно както в схемата (app/schemas/…CatalogDatabase/3.json, `contentSyncTriggers`).
 */
internal val FTS_SYNC_TRIGGERS: Map<String, String> = run {
    val fts = "`${CatalogIndexColumns.FTS_TABLE}`"
    val books = "`${CatalogIndexColumns.TABLE}`"
    val cols = listOf(
        CatalogIndexColumns.TITLE, CatalogIndexColumns.AUTHOR, CatalogIndexColumns.KEYWORDS, CatalogIndexColumns.ANNOTATION,
        CatalogIndexColumns.PUBLISHER, CatalogIndexColumns.DOC_TYPE, CatalogIndexColumns.CALL_NUMBER, CatalogIndexColumns.INV,
        CatalogIndexColumns.ISBN,
    )
    val insert = "INSERT INTO $fts(`docid`, ${cols.joinToString(", ") { "`$it`" }}) " +
        "VALUES (NEW.`rowid`, ${cols.joinToString(", ") { "NEW.`$it`" }}); END"
    val delete = "DELETE FROM $fts WHERE `docid`=OLD.`rowid`; END"
    val prefix = "room_fts_content_sync_${CatalogIndexColumns.FTS_TABLE}_"
    linkedMapOf(
        "${prefix}BEFORE_UPDATE" to "BEFORE UPDATE ON $books BEGIN $delete",
        "${prefix}BEFORE_DELETE" to "BEFORE DELETE ON $books BEGIN $delete",
        "${prefix}AFTER_UPDATE" to "AFTER UPDATE ON $books BEGIN $insert",
        "${prefix}AFTER_INSERT" to "AFTER INSERT ON $books BEGIN $insert",
    ).mapValues { (name, body) -> "CREATE TRIGGER IF NOT EXISTS $name $body" }
}

/**
 * Цялото съдържание наведнъж, в една транзакция: прекъсване (или грешка) оставя стария
 * индекс непокътнат, а четенето от други връзки (WAL) вижда стария индекс до края ѝ —
 * никога празен или наполовина попълнен. [state] `null` — празен индекс („Изчисти кеша“).
 *
 * Бързо: тригерите на FTS таблицата (по един INSERT в индекса за всеки ред) се махат за
 * времето на транзакцията, съдържанието се изтрива и записва без тях, а пълнотекстовият
 * индекс се строи наведнъж с командата 'rebuild' на FTS4 (≈ 3 пъти по-бързо при голям
 * каталог). Тригерите се създават отново със същия SQL като в схемата на Room — в същата
 * транзакция, така че никога не липсват извън нея.
 */
internal suspend fun CatalogDatabase.replaceIndex(state: CatalogIndexStateEntity?, rows: List<CatalogBookEntity>) {
    val dao = index()
    withTransaction {
        val sql = openHelper.writableDatabase
        FTS_SYNC_TRIGGERS.keys.forEach { sql.execSQL("DROP TRIGGER IF EXISTS `$it`") }
        dao.clearState()
        dao.clearBooks()
        if (rows.isNotEmpty()) dao.insertBooks(rows)
        sql.execSQL("INSERT INTO `${CatalogIndexColumns.FTS_TABLE}`(`${CatalogIndexColumns.FTS_TABLE}`) VALUES('rebuild')")
        FTS_SYNC_TRIGGERS.values.forEach { sql.execSQL(it) }
        if (state != null) dao.putState(state)
    }
}

/**
 * Отделна база само за каталога (кеш, който се възстановява от katalog.json) — без
 * миграции; потребителските данни в [AppDatabase] не се засягат от промени тук.
 * Версия 2: индекси за справките (старата база се изтрива и се попълва отново).
 * Версия 3: индекси за подредените страници (заглавие, автор, „Най-нови“).
 */
@Database(
    entities = [CatalogBookEntity::class, CatalogFtsEntity::class, CatalogIndexStateEntity::class],
    version = 3,
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
 * схема, която Room не приема…), [recreate] веднъж в процеса я създава наново — празна.
 * Записът ([replace], [clear]) и [state] се повтарят върху новата база ([state] е `null` —
 * индекс няма). Справките не се повтарят: празната база не е празен каталог, затова до
 * следващото попълване ([replace]) или изчистване ([clear]) хвърлят [CatalogIndexLostException] —
 * търсенето минава през индекса в паметта, а CatalogRepository попълва индекса отново от
 * кеширания katalog.json. Втора грешка на базата също стига до CatalogRepository — без срив.
 */
class RoomCatalogIndexStore(
    private val recreate: (() -> CatalogDatabase)? = null,
    private val open: () -> CatalogDatabase,
) : CatalogIndexStore {
    private val lock = Any()
    private var db: CatalogDatabase? = null
    private var recreated = false
    /** Съдържанието е изгубено при [reset] и още не е попълнено отново (или изчистено). */
    @Volatile private var lost = false

    private fun database(): CatalogDatabase = synchronized(lock) { db ?: open().also { db = it } }

    private fun dao(): CatalogIndexDao = database().index()

    private fun isDatabaseError(e: Exception) = e is SQLException || e is IllegalStateException

    /** Запис или състоянието: при повредена база операцията се повтаря върху новата (празна). */
    private suspend fun <T> healing(block: suspend (CatalogIndexDao) -> T): T =
        try {
            block(dao())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (!isDatabaseError(e) || !reset(e)) throw e
            block(dao())
        }

    /** Справка: при повредена или вече пресъздадена база — [CatalogIndexLostException], не празен резултат. */
    private suspend fun <T> reading(block: suspend (CatalogIndexDao) -> T): T {
        if (lost) throw CatalogIndexLostException()
        return try {
            block(dao())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (!isDatabaseError(e) || !reset(e)) throw e
            throw CatalogIndexLostException(e)
        }
    }

    /** Затваря счупената база и отваря нова (само веднъж в процеса); `false` — не е пресъздадена. */
    private fun reset(cause: Exception): Boolean {
        val make = recreate ?: return false
        synchronized(lock) {
            if (recreated) return false
            recreated = true
            lost = true
            Log.w(TAG, "Индексът на каталога не се чете — базата се създава наново", cause)
            runCatching { db?.close() }
            db = null
            db = make()
        }
        return true
    }

    override suspend fun state(): CatalogIndexState? = healing { dao -> dao.stateJson()?.let { CatalogIndexState.fromJson(it) } }

    override suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>) {
        val entities = rows.map { CatalogBookEntity.of(it) }
        healing { database().replaceIndex(CatalogIndexStateEntity(json = state.toJson()), entities) }
        lost = false
    }

    override suspend fun clear() {
        healing { database().replaceIndex(null, emptyList()) }
        lost = false
        // Освободеното място се връща на системата — иначе файлът на базата остава със същия размер.
        // Индексът вече е празен: свиването не се прекъсва по средата (и отмяната не стига дотук).
        withContext(NonCancellable + Dispatchers.IO) {
            runCatching { synchronized(lock) { db }?.openHelper?.writableDatabase?.execSQL("VACUUM") }
                .onFailure { Log.w(TAG, "Базата на каталога не е свита след изчистването", it) }
        }
    }

    override suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow> = reading { dao ->
        dao.rows(SimpleSQLiteQuery(query.sql, query.args.toTypedArray())).map(CatalogBookEntity::toRow)
    }

    override suspend fun hits(query: CatalogIndexQuery): List<CatalogIndexHit> = reading { dao ->
        dao.hits(SimpleSQLiteQuery(query.hitSql, query.args.toTypedArray())).map(CatalogHitRow::toHit)
    }

    override suspend fun booksAt(ords: List<Int>): List<CatalogBook> {
        if (ords.isEmpty()) return emptyList()
        return reading { dao ->
            val byOrd = ords.distinct().chunked(MAX_ARGS).flatMap { dao.byOrds(it) }.associateBy { it.ord }
            ords.mapNotNull { byOrd[it]?.toRow()?.book }
        }
    }

    override suspend fun count(query: CatalogIndexQuery): Int =
        reading { dao -> dao.count(SimpleSQLiteQuery(query.countSql, query.args.toTypedArray())) }

    override suspend fun book(inv: Long): CatalogBook? = reading { dao -> dao.byInv(inv)?.toRow()?.book }

    override suspend fun books(invs: Collection<Long>): List<CatalogBook> = reading { dao ->
        // На части — броят на параметрите в една заявка е ограничен (999 на по-старите версии).
        invs.distinct().chunked(MAX_ARGS).flatMap { dao.byInvs(it) }.sortedBy { it.ord }.map { it.toRow().book }
    }

    override suspend fun byTitleAndAuthor(title: String, author: String): List<CatalogBook> =
        reading { dao -> dao.byTitleAndAuthor(title, author).map { it.toRow().book } }

    override suspend fun byAuthor(author: String): List<CatalogBook> = reading { dao -> dao.byAuthor(author).map { it.toRow().book } }

    override suspend fun byIsbn(isbnDigits: Collection<String>): List<CatalogBook> =
        if (isbnDigits.isEmpty()) emptyList() else reading { dao -> dao.byIsbn(isbnDigits.toList()).map { it.toRow().book } }

    override suspend fun authors(): List<Pair<String, String>> = reading { dao -> dao.authors().map { it.author to it.nAuthor } }

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
        return reading { dao -> dao.rows(SimpleSQLiteQuery(sql.toString(), args.toTypedArray())).map { it.toRow().book } }
    }

    private companion object {
        const val MAX_ARGS = 500
        const val TAG = "CatalogIndex"
    }
}
