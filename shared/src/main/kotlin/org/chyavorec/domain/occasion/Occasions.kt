package org.chyavorec.domain.occasion

/**
 * Празници и сезони за украсите на „Начало“: падащ ефект над заглавката,
 * малък орнамент в нея и поздрав под нея. Чистата логика е тук (тества се без
 * Android); рисуването и текстовете са в приложението.
 */

/** Настроение: при [QUIET] (паметни дни, Разпети петък) няма анимации и весели цветове. */
enum class OccasionMood { FESTIVE, QUIET }

/** Падащ/блестящ ефект над заглавката (~20 s, после избледнява). */
enum class OccasionEffect {
    NONE,
    /** Сняг (15–19 декември). */
    SNOW,
    /** Сняг и трептящи звездички (20 декември – 7 януари). */
    SNOW_STARS,
    /** Есенни листа. */
    LEAVES,
    /** Цветчета (около Гергьовден). */
    PETALS,
    /** Цветчета и пеперуди (пролетното равноденствие). */
    PETALS_BUTTERFLIES,
    /** Фойерверки и искри (Нова година). */
    FIREWORKS,
    /** Падащи букви от кирилицата (24 май). */
    LETTERS,
    /** Летящи страници (Ден на книгата). */
    PAGES,
    /** Балончета (Ден на детето). */
    BALLOONS,
    /** Блестяща роса (Еньовден). */
    DEW,
}

/** Малка рисунка в заглавката (~44×28 dp). */
enum class OccasionOrnament {
    NONE,
    /** Две писани яйца (Великден). */
    EASTER_EGGS,
    /** Слънце с лъчи-шевица (лятното слънцестоене). */
    SUN,
    /** Развят трикольор (3 март, 6 и 22 септември). */
    TRICOLOR,
    /** Зелен венец (Гергьовден). */
    GREEN_WREATH,
    /** Лавров венец (24 май, рождението на Левски). */
    LAUREL_WREATH,
    /** Обредна пита (Бъдни вечер). */
    RITUAL_BREAD,
    /** Коледна звезда. */
    CHRISTMAS_STAR,
    /** Тих венец с лента — паметни дни, без анимация. */
    MEMORIAL_WREATH,
    /** Отворена книга с пламъче (Ден на народните будители). */
    BOOK_FLAME,
    /** Сурвачка (1 януари). */
    SURVACHKA,
    /** Кръст и вода (Йордановден). */
    CROSS_WATER,
    /** Лоза и грозде (Трифон Зарезан). */
    VINE,
    /** Мартеничка (целия март). */
    MARTENITSA,
    /** Венче от цветя (Лазаровден). */
    FLOWER_WREATH,
    /** Върбови клонки (Цветница). */
    WILLOW,
    /** Венец от билки (Еньовден). */
    HERB_WREATH,
    /** Рибка (Никулден). */
    FISH,
}

/**
 * Какво отваря натискането на поздрава: търсене в каталога ([text]), отдел, чието
 * име съдържа [departmentHint] (напр. детския), или най-новите книги ([newest]).
 */
data class OccasionLink(val text: String? = null, val departmentHint: String? = null, val newest: Boolean = false)

/** Празник, добавен от сайта (`/data/app-occasions.json`) — с готов текст. */
data class CustomGreeting(val titleBg: String, val textBg: String, val titleEn: String?, val textEn: String?)

/**
 * Един повод за деня. [id] е ключът на текстовете в приложението
 * (`occ_<id>_title`/`occ_<id>_text`); при празниците от сайта текстът е в [custom].
 */
data class Occasion(
    val id: String,
    val priority: Int,
    val mood: OccasionMood = OccasionMood.FESTIVE,
    val effect: OccasionEffect = OccasionEffect.NONE,
    val ornament: OccasionOrnament = OccasionOrnament.NONE,
    /** Има ли поздрав под заглавката (периодите — мартеничка, сняг — нямат). */
    val greeting: Boolean = true,
    val link: OccasionLink? = null,
    val custom: CustomGreeting? = null,
)

/**
 * Украсата за един ден: по един ефект, орнамент и поздрав. Всяко от трите идва от
 * най-важния повод, който го има; паметен ден ([OccasionMood.QUIET]) спира всичко
 * останало.
 */
data class DayDecor(
    val effect: OccasionEffect = OccasionEffect.NONE,
    val ornament: OccasionOrnament = OccasionOrnament.NONE,
    val greeting: Occasion? = null,
    val quiet: Boolean = false,
) {
    val isEmpty: Boolean get() = effect == OccasionEffect.NONE && ornament == OccasionOrnament.NONE && greeting == null

    companion object {
        val NONE = DayDecor()
    }
}
