package org.chyavorec.app.data.local

import java.security.MessageDigest

/**
 * Читателският дневник при „Изход“. По подразбиране се трие с останалите лични следи (телефонът
 * може да е общ); в потвърждението на изхода читателят може да го остави — тогава той се
 * показва пак след следващ вход на СЪЩИЯ читател, а при вход на друг се трие.
 * Всеки друг изход (изтекла сесия, премахнат профил, нечетима сесия) трие дневника.
 */
object KeptDiary {
    /**
     * Има ли в дневника нещо, което си заслужава да се пази: оценка или бележка. Запис „Не сега“
     * (без оценка и без бележка) не се брои — тогава изходът не пита и просто трие.
     */
    fun hasEntries(entries: List<ReadingDiaryEntity>): Boolean = entries.any { it.rating > 0 || it.note.isNotBlank() }

    /** Отпечатък на читателя за [SettingsStore.keptDiaryOwner] — номерът му не се пази в настройките. */
    fun ownerKey(readerId: String): String =
        MessageDigest.getInstance("SHA-256").digest(("kept-diary:" + readerId).toByteArray())
            .joinToString("") { "%02x".format(it) }

    /** При вход: дневникът е оставен от друг читател ([keptOwner]) — трие се. */
    fun clearOnSignIn(keptOwner: String?, readerId: String): Boolean = keptOwner != null && keptOwner != ownerKey(readerId)
}
