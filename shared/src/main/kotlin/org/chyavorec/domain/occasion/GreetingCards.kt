package org.chyavorec.domain.occasion

/**
 * „Честитка от Яворец“: за кои поводи има картичка и какво може да е личният ред върху нея.
 * Текстовете (заглавие и пожелание) са в приложението по [Occasion.id]; тук е чистата логика.
 */
object GreetingCards {
    /** Най-много знаци в личния ред („За баба Мария“). */
    const val MAX_LINE = 60

    /**
     * Поводите с готови текстове за картичка (виж `greeting_*` в приложението). Паметните
     * дни (Левски, Ботев) и Разпети петък нямат: честитка в такъв ден е неуместна.
     */
    val IDS: Set<String> = setOf(
        "spring", "summer", "autumn", "winter", "new_year", "liberation", "labour", "gergyovden", "may24",
        "unification", "independence", "easter", "christmas_eve", "christmas", "levski_birth", "enlighteners",
        "children_book", "book_day", "children_day", "jordan", "babinden", "trifon", "baba_marta", "lazarovden",
        "tsvetnitsa", "forgiveness", "ascension", "enyovden", "ilinden", "assumption", "dimitrovden", "nikulden",
        "ignazhden",
    )

    /** Има ли за днешната украса [decor] картичка (весел повод с текст; празниците от сайта — с техния текст). */
    fun available(decor: DayDecor): Boolean {
        val o = decor.greeting ?: return false
        if (decor.quiet || o.mood == OccasionMood.QUIET) return false
        return o.custom != null || o.id in IDS
    }

    /**
     * Знаците, които шрифтовете на приложението (Cormorant, Raleway) и картата рисуват сигурно:
     * латиница (основна, Latin-1, Latin Extended-A), основната кирилица, Ґ ґ и няколко знака
     * за препинание („ “ ” ‘ ’ ‚ – — …). Емоджита, символи и невидимите знаци за посока са вън.
     */
    fun isAllowed(c: Char): Boolean = when (c.code) {
        in 0x20..0x7E -> true
        in 0xA1..0x17F -> c.code != 0xAD
        in 0x400..0x45F -> true
        0x490, 0x491 -> true
        0x2013, 0x2014, 0x2018, 0x2019, 0x201A, 0x201C, 0x201D, 0x201E, 0x2026, 0x2116 -> true
        else -> false
    }

    /**
     * Личният ред за картата: празните знаци (нов ред, табулация) стават интервал, непозволените
     * отпадат, поредните интервали се свиват, краищата се изрязват, най-много [MAX_LINE] знака.
     */
    fun sanitizeLine(raw: String): String {
        val out = StringBuilder()
        for (ch in raw) {
            val c = if (ch.isWhitespace() || ch == ' ') ' ' else ch
            if (!isAllowed(c)) continue
            if (c == ' ' && (out.isEmpty() || out.last() == ' ')) continue
            if (out.length >= MAX_LINE) break
            out.append(c)
        }
        return out.toString().trimEnd()
    }

    /**
     * Колко голям да е личният ред според дължината му (картата е с постоянен размер):
     * кратък — едър, дълъг — по-дребен, за да се събира на два реда.
     */
    fun lineSizeSp(line: String): Float = when {
        line.length <= 18 -> 30f
        line.length <= 32 -> 25f
        line.length <= 46 -> 21f
        else -> 18f
    }
}
