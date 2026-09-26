package org.chyavorec.core

import kotlin.test.Test
import kotlin.test.assertEquals

class TextNormalizerTest {
    @Test fun lowercasesAndStripsQuotes() = assertEquals("под игото", TextNormalizer.normalize("„Под  ИГОТО“"))
    @Test fun keepsShortI() = assertEquals("майка", TextNormalizer.normalize("Майка"))
    @Test fun stripsLatinDiacritics() = assertEquals("cafe", TextNormalizer.normalize("Café"))
    @Test fun tokens() = assertEquals(listOf("иван", "вазов"), TextNormalizer.tokens("  Иван,  Вазов "))
    @Test fun digits() = assertEquals("978954090X", TextNormalizer.digits("ISBN 978-954-09-0x"))
}
