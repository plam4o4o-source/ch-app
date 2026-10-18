package org.chyavorec.data

import org.chyavorec.data.update.ApkSigners
import org.chyavorec.data.update.SigningCheck
import org.chyavorec.data.update.SigningCheck.Result.MATCH
import org.chyavorec.data.update.SigningCheck.Result.MISMATCH
import org.chyavorec.data.update.SigningCheck.Result.UNREADABLE
import kotlin.test.Test
import kotlin.test.assertEquals

/** Подписът на обновлението: същият ключ → да; различен или неразчетен → не (fail closed). */
class SigningCheckTest {
    private val ours = "a".repeat(64)
    private val rotated = "b".repeat(64)
    private val evil = "e".repeat(64)
    private val second = "c".repeat(64)

    private fun single(cert: String, history: Set<String> = setOf(cert)) = ApkSigners(setOf(cert), history, multipleSigners = false)

    @Test fun sameCertificateMatches() {
        assertEquals(MATCH, SigningCheck.decide(single(ours), single(ours)))
        // Старият начин (GET_SIGNATURES, без история).
        assertEquals(MATCH, SigningCheck.decide(ApkSigners(setOf(ours)), ApkSigners(setOf(ours))))
    }

    @Test fun differentCertificateIsRejected() {
        assertEquals(MISMATCH, SigningCheck.decide(single(evil), single(ours)))
        assertEquals(MISMATCH, SigningCheck.decide(ApkSigners(setOf(evil)), ApkSigners(setOf(ours))))
        // Чуждият APK не може да „добави“ нашия ключ в историята си, без да е подписан от него —
        // но и ако системата върне такава история, текущият чужд ключ без нашия в нея не минава.
        assertEquals(MISMATCH, SigningCheck.decide(single(evil, history = setOf(evil, second)), single(ours)))
    }

    @Test fun unreadableSignaturesFailClosed() {
        assertEquals(UNREADABLE, SigningCheck.decide(null, single(ours)))
        assertEquals(UNREADABLE, SigningCheck.decide(single(ours), null))
        assertEquals(UNREADABLE, SigningCheck.decide(null, null))
    }

    @Test fun emptySignatureListsFailClosed() {
        assertEquals(UNREADABLE, SigningCheck.decide(ApkSigners(emptySet()), single(ours)))
        assertEquals(UNREADABLE, SigningCheck.decide(single(ours), ApkSigners(emptySet())))
        assertEquals(UNREADABLE, SigningCheck.decide(ApkSigners(emptySet(), history = setOf(ours)), single(ours)))
        assertEquals(UNREADABLE, SigningCheck.decide(ApkSigners(setOf("")), ApkSigners(setOf(""))))
    }

    @Test fun multipleSignersMustMatchTheWholeSet() {
        val both = ApkSigners(setOf(ours, second), multipleSigners = true)
        assertEquals(MATCH, SigningCheck.decide(both, ApkSigners(setOf(second, ours), multipleSigners = true)))
        // Само един от двата, или един чужд в повече — не.
        assertEquals(MISMATCH, SigningCheck.decide(ApkSigners(setOf(ours, evil), multipleSigners = true), both))
        assertEquals(MISMATCH, SigningCheck.decide(both, single(ours)))
        assertEquals(MISMATCH, SigningCheck.decide(single(ours), both))
        // Старият начин: наборът от signatures трябва да е същият.
        assertEquals(MATCH, SigningCheck.decide(ApkSigners(setOf(ours, second)), ApkSigners(setOf(ours, second))))
        assertEquals(MISMATCH, SigningCheck.decide(ApkSigners(setOf(ours, evil)), ApkSigners(setOf(ours, second))))
    }

    @Test fun rotatedKeyWithOurCertificateInItsLineageMatches() {
        // Новата версия е подписана с нов ключ, а историята ѝ (v3) доказва смяната от нашия.
        val update = single(rotated, history = setOf(ours, rotated))
        assertEquals(MATCH, SigningCheck.decide(update, single(ours)))
        // Следваща версия след смяната — инсталираното вече е с новия ключ и същата история.
        assertEquals(MATCH, SigningCheck.decide(update, single(rotated, history = setOf(ours, rotated))))
        // Още една смяна по веригата.
        val newer = "d".repeat(64)
        assertEquals(MATCH, SigningCheck.decide(single(newer, history = setOf(ours, rotated, newer)), update))
    }

    @Test fun rollbackToAnOlderKeyOrUnrelatedLineageIsRejected() {
        // Инсталираното е след смяна на ключа; APK със стария ключ не се приема.
        assertEquals(MISMATCH, SigningCheck.decide(single(ours), single(rotated, history = setOf(ours, rotated))))
        // История без нашия ключ.
        assertEquals(MISMATCH, SigningCheck.decide(single(rotated, history = setOf(evil, rotated)), single(ours)))
    }
}
