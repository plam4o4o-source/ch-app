package org.chyavorec.app.data.security

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Отключването на личните екрани: важи до фонов режим над минута или угаснал екран. */
class PersonalUnlockTest {
    private var now = 1_000_000L
    private val unlock = PersonalUnlock({ now })

    @Test fun startsLocked() = assertFalse(unlock.unlocked.value)

    @Test fun shortBackgroundKeepsTheUnlock() {
        unlock.unlock()
        unlock.onBackground()
        now += PersonalUnlock.GRACE_MS
        unlock.onForeground()
        assertTrue(unlock.unlocked.value)
    }

    @Test fun backgroundOverAMinuteLocks() {
        unlock.unlock()
        unlock.onBackground()
        now += PersonalUnlock.GRACE_MS + 1
        unlock.onForeground()
        assertFalse(unlock.unlocked.value)
    }

    @Test fun screenOffLocksAtOnce() {
        unlock.unlock()
        unlock.onScreenOff()
        assertFalse(unlock.unlocked.value)
    }

    /** Ново отключване след фонов режим: старото излизане не се брои при следващото връщане. */
    @Test fun unlockClearsThePendingBackgroundTime() {
        unlock.onBackground()
        now += PersonalUnlock.GRACE_MS * 2
        unlock.unlock()
        unlock.onForeground()
        assertTrue(unlock.unlocked.value)
    }
}
