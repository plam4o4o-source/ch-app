package org.chyavorec.app.ui.screens.events

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Отрязъкът и календарът: намерението за вмъкване не връща резултат, затова отрязъкът
 * се откъсва само при връщане след отворен календар — не при всяко ON_RESUME — а думите на
 * листа са „Отворено в календара“ (виж EventLeafScreenshotTest.tornStubKeepsClearOfTheTitleAndSaysOpened).
 */
class CalendarStubTest {
    @Test fun resumeWithoutOpeningTheCalendarDoesNotTear() {
        val stub = CalendarStub()
        stub.resumed()
        stub.resumed()
        assertFalse(stub.torn.value)
    }

    @Test fun returningFromTheCalendarTearsOnce() {
        val stub = CalendarStub()
        stub.opened()
        assertFalse(stub.torn.value, "докато календарът е отпред — още не")
        stub.resumed()
        assertTrue(stub.torn.value)
        // По-нататъшни връщания (друго приложение, заключване) не го „залепват“ обратно.
        stub.resumed()
        assertTrue(stub.torn.value)
    }
}
