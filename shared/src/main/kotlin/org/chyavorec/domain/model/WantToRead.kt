package org.chyavorec.domain.model

/**
 * Книга от списъка „Искам да прочета“ — колкото трябва за сравнението на наличността.
 * [lastKnownAvailable] — наличността при последната проверка (или при добавянето).
 */
data class WatchedBook(
    val inv: Long,
    val notifyWhenAvailable: Boolean,
    val lastKnownAvailable: Boolean,
)

/**
 * Резултатът от сравнението: [changed] — новата наличност на книгите, при които се е
 * променила (записва се като последно известна); [becameAvailable] — книгите с „Кажи ми,
 * когато е на рафта“, които току-що са се върнали на рафта (за тях — известие).
 */
data class WatchCheck(
    val changed: Map<Long, Boolean>,
    val becameAvailable: List<Long>,
) {
    val isEmpty: Boolean get() = changed.isEmpty()
}

/**
 * „Кажи ми, когато книгата е на рафта“ — чисти изчисления, без Android. Мостът няма API за
 * наличност и запазване, затова наличността идва само от публичния каталог (`av`): при всяко
 * опресняване на каталога наличността на книгите от списъка се сравнява със запомнената.
 *
 * - известие има само при преход „не е на рафта“ → „на рафта“ и само за книгите с включено
 *   „Кажи ми“; новата наличност се записва веднага, затова същият преход не се известява втори
 *   път (следващата проверка вижда вече „на рафта“). Ако книгата пак бъде заета и после
 *   върната — това е нов преход и ново известие;
 * - книга, която я няма в каталога (напр. отписана или каталогът е непълен), не се променя —
 *   запомнената наличност остава до следващия каталог, в който я има;
 * - [defer] (нощем, във фонов режим): преходите, които биха дали известие, не се записват —
 *   известието идва при следващата проверка през деня, ако книгата още е на рафта.
 */
object WatchTransitions {
    fun check(watched: List<WatchedBook>, current: Map<Long, Boolean>, defer: Boolean = false): WatchCheck {
        val changed = LinkedHashMap<Long, Boolean>()
        val became = ArrayList<Long>()
        for (w in watched) {
            val now = current[w.inv] ?: continue
            if (now == w.lastKnownAvailable) continue
            val announce = now && w.notifyWhenAvailable
            if (announce && defer) continue
            changed[w.inv] = now
            if (announce) became += w.inv
        }
        return WatchCheck(changed, became)
    }
}
