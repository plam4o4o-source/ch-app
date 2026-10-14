package org.chyavorec.data.catalog

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.SearchField

/**
 * Един запис на пълнотекстовия индекс на каталога (таблицата [CatalogIndexColumns.TABLE]):
 * книгата, нормализираните полета за търсене (както ги нормализира [CatalogSearchEngine])
 * и предварително изчислените български азбучни рангове за подреждане.
 */
data class CatalogIndexRow(
    /** Мястото в katalog.json (= rowid в базата) — пази реда на каталога. */
    val ord: Int,
    val book: CatalogBook,
    /** Заглавие + подзаглавие, нормализирани. */
    val title: String,
    val author: String,
    val keywords: String,
    val annotation: String,
    val publisher: String,
    val docType: String,
    /** Сигнатурата с малки букви (без друга нормализация — както в индекса в паметта). */
    val callNumber: String,
    /** Инвентарният номер като текст. */
    val invText: String,
    /** ISBN както е публикуван. */
    val isbn: String,
    val isbnDigits: String,
    val year: Int?,
    val udcSection: Int?,
    val titleRank: Int,
    val authorRank: Int,
) {
    /** „Останалите“ полета, слепени като в [CatalogSearchEngine]. */
    val rest: String get() = restOf(annotation, publisher, docType, callNumber, invText, isbn)

    companion object {
        internal fun restOf(annotation: String, publisher: String, docType: String, callNumber: String, invText: String, isbn: String): String =
            "$annotation $publisher $docType $callNumber $invText $isbn"
    }
}

/** Имената на таблиците и колоните — едни и същи за Room и за SQL заявките. */
object CatalogIndexColumns {
    const val TABLE = "catalog_books"
    const val FTS_TABLE = "catalog_fts"
    const val STATE_TABLE = "catalog_index_state"

    const val TITLE = "n_title"
    const val AUTHOR = "n_author"
    const val KEYWORDS = "n_keywords"
    const val ANNOTATION = "n_annotation"
    const val PUBLISHER = "n_publisher"
    const val DOC_TYPE = "n_doc_type"
    const val CALL_NUMBER = "n_call_number"
    const val INV = "n_inv"
    const val ISBN = "n_isbn"

    /** Колоните на FTS таблицата (в този ред) — всички текстови полета, в които търси „Всички“. */
    val FTS = listOf(TITLE, AUTHOR, KEYWORDS, ANNOTATION, PUBLISHER, DOC_TYPE, CALL_NUMBER, INV, ISBN)

    /** Колоните, нужни за списък без текст за търсене (показване, филтри, подреждане). */
    val LIST = listOf(
        "ord", "inv", "author", "title", "subtitle", "city", "publisher", "year", "doc_type", "language", "udc",
        "call_number", "department", "cover_url", "available", "registered_on", "isbn", "year_num", "udc_section",
        "title_rank", "author_rank",
    )

    /** Колоните, които списъкът без текст не чете — връщат се празни (анотацията и нормализираните полета са най-обемни). */
    val LIST_BLANK = listOf("keywords", "annotation", "isbn_digits") + FTS
}

object CatalogIndexRows {
    /** Записите за индекса — в реда на каталога, с рангове за българско азбучно подреждане. */
    fun build(snapshot: CatalogSnapshot): List<CatalogIndexRow> {
        val books = snapshot.books
        val (titleRanks, authorRanks) = CatalogMatcher.ranks(books)
        return books.mapIndexed { i, b ->
            CatalogIndexRow(
                ord = i,
                book = b,
                title = normalizedTitle(b),
                author = TextNormalizer.normalize(b.author),
                keywords = TextNormalizer.normalize(b.keywords),
                annotation = TextNormalizer.normalize(b.annotation),
                publisher = TextNormalizer.normalize(b.publisher),
                docType = TextNormalizer.normalize(b.docType),
                callNumber = b.callNumber.lowercase(),
                invText = b.inv.toString(),
                isbn = b.isbn,
                isbnDigits = TextNormalizer.digits(b.isbn),
                year = b.yearNumber,
                udcSection = b.udcSection,
                titleRank = titleRanks[i],
                authorRank = authorRanks[i],
            )
        }
    }

    internal fun normalizedTitle(b: CatalogBook): String = TextNormalizer.normalize(b.title + " " + b.subtitle)

    /** „Останалите“ полета на книга (за индекса в паметта). */
    internal fun rest(b: CatalogBook): String = CatalogIndexRow.restOf(
        TextNormalizer.normalize(b.annotation),
        TextNormalizer.normalize(b.publisher),
        TextNormalizer.normalize(b.docType),
        b.callNumber.lowercase(), b.inv.toString(), b.isbn,
    )
}

/** Какво съдържа индексът: за кой каталог (хеш) е, датата му и възможните филтри. */
@Serializable
data class CatalogIndexState(
    /** Хешът на каталога (като в метаданните на [org.chyavorec.data.repository.CatalogRepository]). */
    val hash: String,
    val generatedOn: String,
    val count: Int,
    val facets: CatalogFacets,
) {
    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        fun fromJson(text: String): CatalogIndexState? = runCatching { json.decodeFromString(serializer(), text) }.getOrNull()
    }
}

/**
 * Пълнотекстовият индекс на каталога (в приложението — Room с FTS4). Заявките идват
 * готови ([CatalogIndexQuery.sql]/[CatalogIndexQuery.args]); точното съвпадение и
 * точкуването се правят след това в Kotlin ([FtsCatalogSearcher]).
 */
interface CatalogIndexStore {
    /** Състоянието на индекса или `null`, ако е празен. */
    suspend fun state(): CatalogIndexState?

    /**
     * Заменя цялото съдържание с [rows] — в една транзакция (или изобщо не): през това
     * време четящите виждат стария индекс, след нея — новия, никога празен или наполовина.
     */
    suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>)

    /** Записите по заявката, в реда на каталога. */
    suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow>

    // Справки за отделни книги (по индексирани колони) — всички в реда на каталога.

    /** Книгата с инвентарния номер (при повторение — последната, като в индекса в паметта). */
    suspend fun book(inv: Long): CatalogBook?

    /** Книгите с тези инвентарни номера. */
    suspend fun books(invs: Collection<Long>): List<CatalogBook>

    /** Книгите с точно това заглавие и автор (екземплярите на едно заглавие). */
    suspend fun byTitleAndAuthor(title: String, author: String): List<CatalogBook>

    /** Книгите с точно този автор. */
    suspend fun byAuthor(author: String): List<CatalogBook>

    /** Книгите, чийто ISBN (само цифрите) е някой от [isbnDigits]. */
    suspend fun byIsbn(isbnDigits: Collection<String>): List<CatalogBook>

    /** Различните автори (по първата им поява в каталога) с нормализирания им вид. */
    suspend fun authors(): List<Pair<String, String>>

    /**
     * Наличните книги, които могат да получат точки при препоръките: от разделите на УДК
     * [udcSections] или с някоя от думите [authorWords] в нормализирания автор
     * (`null` — всички налични).
     */
    suspend fun recommendationCandidates(udcSections: Set<Int>, authorWords: Set<String>?): List<CatalogBook>
}

/**
 * Заявка към индекса: FTS съвпадение ([matchTokens] — всяка дума като префикс, по
 * желание само в една колона), подниз ([contains] — за резервното търсене на части от
 * думи), ISBN/инвентарен номер и филтрите на [CatalogQuery]. Резултатът е в реда на
 * каталога — нужно за същото (стабилно) подреждане като в [CatalogSearchEngine].
 */
data class CatalogIndexQuery(
    val matchTokens: List<String> = emptyList(),
    /** FTS колона ([CatalogIndexColumns]) или `null` — във всички колони. */
    val matchColumn: String? = null,
    val contains: List<String> = emptyList(),
    /** Колоните, в които се търси всеки подниз от [contains] (слепени с интервал). */
    val containsColumns: List<String> = emptyList(),
    val isbnDigits: String? = null,
    val invDigits: String? = null,
    val docType: String? = null,
    val department: String? = null,
    val language: String? = null,
    val udcSection: Int? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val onlyAvailable: Boolean = false,
    val limit: Int? = null,
    val offset: Int = 0,
) {
    /**
     * Без текст за търсене (само филтри): точките не се смятат, затова се четат само
     * колоните за списъка ([CatalogIndexColumns.LIST]), а останалите идват празни.
     * Без LIMIT/OFFSET: екранът „Каталог“ държи целия резултат и го показва на страници
     * (`results.take(shown)`); страници от базата биха изисквали асинхронно „Още“.
     */
    val textless: Boolean get() = matchTokens.isEmpty() && contains.isEmpty() && isbnDigits == null && invDigits == null

    /** FTS4 MATCH изразът: всяка дума е фраза в кавички с `*` (префикс) — операторите са неактивни. */
    val match: String? get() = if (matchTokens.isEmpty()) null else matchTokens.joinToString(" ") { "\"$it*\"" }

    val sql: String
        get() = buildString {
            val c = CatalogIndexColumns
            append("SELECT ")
            if (textless) {
                c.LIST.joinTo(this) { "b.$it" }
                c.LIST_BLANK.forEach { append(", '' AS ").append(it) }
            } else {
                append("b.*")
            }
            append(" FROM ").append(c.TABLE).append(" AS b")
            if (match != null) append(" JOIN ").append(c.FTS_TABLE).append(" ON ").append(c.FTS_TABLE).append(".rowid = b.ord")
            append(" WHERE 1")
            if (match != null) {
                append(" AND ").append(c.FTS_TABLE)
                if (matchColumn != null) append('.').append(matchColumn)
                append(" MATCH ?")
            }
            if (contains.isNotEmpty()) {
                val haystack = containsColumns.joinToString(" || ' ' || ") { "b.$it" }
                repeat(contains.size) { append(" AND instr(").append(haystack).append(", ?) > 0") }
            }
            if (isbnDigits != null) append(" AND instr(b.isbn_digits, ?) > 0")
            if (invDigits != null) append(" AND (b.").append(c.INV).append(" = ? OR substr(b.").append(c.INV).append(", 1, ?) = ?)")
            if (onlyAvailable) append(" AND b.available = 1")
            if (docType != null) append(" AND b.doc_type = ?")
            if (department != null) append(" AND b.department = ?")
            if (language != null) append(" AND b.language = ?")
            if (udcSection != null) append(" AND b.udc_section = ?")
            if (yearFrom != null) append(" AND b.year_num >= ?")
            if (yearTo != null) append(" AND b.year_num <= ?")
            append(" ORDER BY b.ord")
            if (limit != null) append(" LIMIT ").append(limit).append(" OFFSET ").append(offset)
        }

    /** Стойностите за `?` в [sql] (в същия ред). */
    val args: List<Any>
        get() = buildList {
            match?.let { add(it) }
            addAll(contains)
            isbnDigits?.let { add(it) }
            invDigits?.let { add(it.trimStart('0')); add(it.length); add(it) }
            docType?.let { add(it) }
            department?.let { add(it) }
            language?.let { add(it) }
            udcSection?.let { add(it) }
            yearFrom?.let { add(it) }
            yearTo?.let { add(it) }
        }
}

/** Превежда [CatalogQuery] в заявки към индекса. */
object CatalogIndexQueries {

    /**
     * Думата като безопасна FTS фраза: без кавички (така операторите AND/OR/NOT/NEAR,
     * `-`, `^`, `:` нямат действие). `null` — думата няма букви/цифри (празна фраза в
     * FTS4 не съвпада с нищо; такава дума се проверява само като подниз).
     */
    fun ftsToken(token: String): String? {
        val clean = token.replace("\"", "")
        return if (clean.any { it.isLetterOrDigit() }) clean else null
    }

    private fun filtered(q: CatalogQuery) = CatalogIndexQuery(
        docType = q.docType,
        department = q.department,
        language = q.language,
        udcSection = q.udcSection,
        yearFrom = q.yearFrom,
        yearTo = q.yearTo,
        onlyAvailable = q.onlyAvailable,
    )

    private fun ftsColumn(field: SearchField): String? = when (field) {
        SearchField.TITLE -> CatalogIndexColumns.TITLE
        SearchField.AUTHOR -> CatalogIndexColumns.AUTHOR
        SearchField.KEYWORD -> CatalogIndexColumns.KEYWORDS
        else -> null
    }

    /**
     * Основната заявка: кандидатите от FTS (думите като начало на дума). `null` — няма
     * как да има резултати (напр. ISBN с по-малко от 3 цифри).
     */
    fun search(q: CatalogQuery, tokens: List<String>, digits: String): CatalogIndexQuery? {
        val base = filtered(q)
        if (tokens.isEmpty()) return base
        return when (q.field) {
            SearchField.ISBN -> if (digits.length >= 3) base.copy(isbnDigits = digits) else null
            SearchField.INVENTORY -> if (digits.isNotEmpty()) base.copy(invDigits = digits) else null
            else -> {
                val fts = tokens.mapNotNull(::ftsToken)
                if (fts.isEmpty()) scan(q, tokens) else base.copy(matchTokens = fts, matchColumn = ftsColumn(q.field))
            }
        }
    }

    /**
     * Резервната заявка — думите като подниз навсякъде в полето (както индекса в паметта);
     * ползва се, когато FTS не намери нищо (напр. търсене по средата на дума).
     * `null` — няма смисъл (ISBN/инв. № вече търсят подниз).
     */
    fun scan(q: CatalogQuery, tokens: List<String>): CatalogIndexQuery? {
        if (tokens.isEmpty()) return null
        val columns = when (q.field) {
            SearchField.ISBN, SearchField.INVENTORY -> return null
            SearchField.ALL -> CatalogIndexColumns.FTS
            else -> listOf(ftsColumn(q.field)!!)
        }
        return filtered(q).copy(contains = tokens, containsColumns = columns)
    }

    /** Кандидатите за предложения (заглавия/автори), на страници. */
    fun suggestions(tokens: List<String>, offset: Int, limit: Int): CatalogIndexQuery? {
        val fts = tokens.mapNotNull(::ftsToken)
        if (fts.isEmpty()) return null
        return CatalogIndexQuery(matchTokens = fts, limit = limit, offset = offset)
    }
}
