package org.chyavorec.app.ui.screens.my

import org.chyavorec.app.data.local.KeptDiary
import org.chyavorec.app.data.local.ReadingDiaryEntity
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Изборът при „Изход“: дневникът остава само при махната отметка и само за същия читател. */
class SignOutChoiceTest {
    private fun entry(rating: Int, note: String = "") = ReadingDiaryEntity("k$rating$note", "Заглавие", "Автор", null, rating, note, 2026, 1L)

    @Test fun theDiaryIsKeptOnlyWhenTheReaderUnticksDelete() {
        assertFalse(SignOutChoice.keepDiary(hasDiary = true, deleteDiary = true), "по подразбиране — трие се")
        assertTrue(SignOutChoice.keepDiary(hasDiary = true, deleteDiary = false))
        // Без записи отметката не се показва — нищо за пазене.
        assertFalse(SignOutChoice.keepDiary(hasDiary = false, deleteDiary = false))
    }

    @Test fun onlyRatingsAndNotesCountAsEntries() {
        assertFalse(KeptDiary.hasEntries(emptyList()))
        assertFalse(KeptDiary.hasEntries(listOf(entry(0))), "„Не сега“ без бележка не е запис")
        assertTrue(KeptDiary.hasEntries(listOf(entry(0, "Хареса ми"))))
        assertTrue(KeptDiary.hasEntries(listOf(entry(0), entry(4))))
    }

    @Test fun aDiaryKeptByAnotherReaderIsDeletedAtSignIn() {
        val r1 = KeptDiary.ownerKey("R1")
        assertFalse(KeptDiary.clearOnSignIn(null, "R1"), "нищо не е оставено")
        assertFalse(KeptDiary.clearOnSignIn(r1, "R1"))
        assertTrue(KeptDiary.clearOnSignIn(r1, "R2"))
    }

    @Test fun theOwnerIsAFingerprintNotTheCardNumber() {
        val key = KeptDiary.ownerKey("1234567")
        assertFalse("1234567" in key)
        assertEquals(64, key.length)
        assertEquals(key, KeptDiary.ownerKey("1234567"))
    }
}
