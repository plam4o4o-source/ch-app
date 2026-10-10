package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Документ от публичния онлайн каталог на InvLib (`katalog.json`).
 * Полетата следват точно публикуваните от InvLib ключове — виж ANALYSIS.md.
 */
@Serializable
data class CatalogBook(
    val inv: Long,
    val author: String,
    val title: String,
    val subtitle: String = "",
    val city: String = "",
    val publisher: String = "",
    val year: String = "",
    /** Вид документ (книга, видеодокумент, …) — ключ `v`. */
    val docType: String = "",
    val language: String = "",
    /** УДК индекс — ключ `u`. */
    val udc: String = "",
    /** Сигнатура/шифър — ключ `g`. */
    val callNumber: String = "",
    /** Отдел (за възрастни, за деца, …) — ключ `o`. */
    val department: String = "",
    val keywords: String = "",
    val annotation: String = "",
    val coverUrl: String = "",
    /** 1 = налична на рафта (изрично „наличен“ и свободни бройки). */
    val available: Boolean = false,
    /** Дата на постъпване (ISO) — ключ `d`. */
    val registeredOn: String = "",
    /** ISBN — InvLib все още НЕ го публикува; полето е готово за ключ `i`. */
    val isbn: String = "",
) {
    val status: BookStatus get() = if (available) BookStatus.AVAILABLE else BookStatus.NOT_ON_SHELF

    /** Първата цифра на УДК — основен раздел (0–9). */
    val udcSection: Int? get() = udc.firstOrNull { it.isDigit() }?.digitToInt()

    /** Първата четирицифрена година в полето (напр. „[1998]“, „2016 г.“). */
    val yearNumber: Int? get() = parseYear(year)

    /** Отделни автори („Иванов, Иван и др.“ остава както е). */
    val authors: List<String>
        get() = author.split(';').map { it.trim() }.filter { it.isNotEmpty() }
}

/**
 * Първите четири последователни цифри в [year] като година (без Regex — вика се за
 * всеки запис при търсене и филтриране).
 */
internal fun parseYear(year: String): Int? {
    var run = 0
    for (i in year.indices) {
        if (year[i] in '0'..'9') {
            run++
            if (run == 4) {
                val s = i - 3
                return (year[s] - '0') * 1000 + (year[s + 1] - '0') * 100 + (year[s + 2] - '0') * 10 + (year[i] - '0')
            }
        } else run = 0
    }
    return null
}

/**
 * Статус на наличност.
 * Публичният каталог има само „налична“ / „не е на рафта“ (ключ `av`). Разликата
 * между „заета“ и „недостъпна“ (липсваща, за реставрация) ще дойде само от
 * бъдещия API за наличност — виж docs/API.md.
 */
enum class BookStatus { AVAILABLE, ON_LOAN, NOT_ON_SHELF, UNAVAILABLE }

@Serializable
data class CatalogShelf(val name: String, val invNumbers: List<Long>)

@Serializable
data class CatalogSnapshot(
    val library: String,
    val place: String,
    /** Датата, на която InvLib е генерирал файла (ISO). */
    val generatedOn: String,
    val books: List<CatalogBook>,
    val shelves: List<CatalogShelf>,
)

/**
 * Малкото, което началният екран показва от каталога. Записва се до katalog.json при
 * всяко ново съдържание, за да не се разчита и индексира целият каталог при старт.
 */
@Serializable
data class CatalogHomeSummary(
    val count: Int,
    val generatedOn: String,
    /** Нови постъпления — най-новите първо. */
    val newest: List<CatalogBook>,
    val shelves: List<CatalogHomeShelf>,
)

@Serializable
data class CatalogHomeShelf(val name: String, val books: List<CatalogBook>)

enum class SearchField { ALL, TITLE, AUTHOR, ISBN, KEYWORD, INVENTORY }

enum class CatalogSort { RELEVANCE, TITLE, AUTHOR, YEAR_DESC, YEAR_ASC, NEWEST }

data class CatalogQuery(
    val text: String = "",
    val field: SearchField = SearchField.ALL,
    val docType: String? = null,
    val department: String? = null,
    val language: String? = null,
    val udcSection: Int? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val onlyAvailable: Boolean = false,
    val sort: CatalogSort = CatalogSort.RELEVANCE,
) {
    val hasFilters: Boolean
        get() = docType != null || department != null || language != null || udcSection != null ||
            yearFrom != null || yearTo != null || onlyAvailable
}

/** Възможни стойности за филтрите — изчислени от реалните данни. */
data class CatalogFacets(
    val docTypes: List<String>,
    val departments: List<String>,
    val languages: List<String>,
    val udcSections: List<Int>,
    val minYear: Int?,
    val maxYear: Int?,
)

/** Основните раздели на УДК (съкратени наименования). */
object UdcSections {
    val names: Map<Int, String> = mapOf(
        0 to "Общ отдел",
        1 to "Философия. Психология",
        2 to "Религия",
        3 to "Обществени науки",
        5 to "Природни науки",
        6 to "Приложни науки. Техника",
        7 to "Изкуство. Спорт",
        8 to "Език. Литература",
        9 to "География. История",
    )
    fun name(section: Int?): String = section?.let { names[it] } ?: "Без раздел"
}
