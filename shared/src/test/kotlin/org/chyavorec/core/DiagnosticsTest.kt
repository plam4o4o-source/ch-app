package org.chyavorec.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Локалният дневник на грешките: почистване на личните данни и пръстеновидният буфер. */
class DiagnosticsTest {

    // --- почистване ---

    @Test fun urlKeepsHostAndPathButNotQueryOrCredentials() {
        val s = DiagnosticSanitizer.text("failed https://user:pw@chyavorec.org/api/v1/me?card=AB12&pin=1234#x and more")
        assertEquals("failed https://chyavorec.org/api/v1/me and more", s)
    }

    @Test fun numbersLikeCardsAndPinsAreMasked() {
        val s = DiagnosticSanitizer.text("card 0012 3456 7890 1234 rejected, pin 4321, code 409")
        assertFalse(Regex("""\d{4}""").containsMatchIn(s), s)
        // Кратките числа (HTTP кодове) остават — те са полезни и не са лични.
        assertTrue("409" in s, s)
    }

    @Test fun keyValueSecretsAreMasked() {
        val s = DiagnosticSanitizer.text("login failed: password=hunter2; token: abc; Authorization: Bearer xyz.abc; session=s1")
        listOf("hunter2", "abc;", "xyz.abc", "s1").forEach { assertFalse(it in s, "$it in $s") }
        assertTrue("password=[…]" in s, s)
    }

    @Test fun tokensAndJwtAreMasked() {
        val jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSMSJ9.c2lnbmF0dXJl"
        val opaque = "a8f3k2m9x7q4w1e6r5t0y3u8i2o7p1"
        val s = DiagnosticSanitizer.text("refresh with $jwt failed, old $opaque")
        assertFalse(jwt in s || "eyJ" in s, s)
        assertFalse(opaque in s, s)
        // Дълги имена без цифри (напр. на класове) не са токени.
        assertEquals("NoSuchElementExceptionHappenedHere", DiagnosticSanitizer.text("NoSuchElementExceptionHappenedHere"))
    }

    @Test fun emailsQuotedValuesAndCyrillicNamesAreRemoved() {
        val s = DiagnosticSanitizer.text("""user ivan.petrov@abv.bg: For input string: "abc-12" by Иван Петров „Под игото“""")
        listOf("ivan.petrov", "abc-12", "Иван", "Петров", "Под игото").forEach { assertFalse(it in s, "$it in $s") }
        assertTrue("[email]" in s, s)
        assertTrue("For input string" in s, s)
    }

    @Test fun jsonInputTailIsDropped() {
        val s = DiagnosticSanitizer.text("Unexpected JSON token at offset 7: Expected quotation mark\nJSON input: {\"name\":\"Ivan\",\"card\":\"77\"}")
        assertFalse("Ivan" in s || "JSON input" in s, s)
        assertTrue(s.startsWith("Unexpected JSON token at offset 7"), s)
    }

    @Test fun textIsOneShortLine() {
        val s = DiagnosticSanitizer.text("a\n\tb  c" + " x".repeat(500))
        assertFalse('\n' in s)
        assertTrue(s.startsWith("a b c"))
        assertTrue(s.length <= 240 && s.endsWith("…"))
    }

    @Test fun tagIsRestricted() {
        assertEquals("worker.sync", DiagnosticSanitizer.tag("worker.sync"))
        assertEquals("a_b_", DiagnosticSanitizer.tag("a b?"))
        assertEquals("app", DiagnosticSanitizer.tag(""))
        assertEquals(40, DiagnosticSanitizer.tag("x".repeat(100)).length)
    }

    @Test fun describeNamesKindAndRootCause() {
        val e = IllegalStateException("wrapper", IOException("timeout to https://chyavorec.org/x?pin=1"))
        val d = DiagnosticSanitizer.describe(e)
        assertEquals("java.lang.IllegalStateException: wrapper ← java.io.IOException: timeout to https://chyavorec.org/x", d)
        val stack = DiagnosticSanitizer.stack(e)
        assertTrue(stack.size <= 8 + 1 + 1 + 3)
        assertTrue(stack.any { it == "caused by java.io.IOException" }, stack.toString())
        assertTrue(stack.first().startsWith("org.chyavorec.core.DiagnosticsTest."), stack.first())
    }

    @Test fun selfReferencingCauseDoesNotLoop() {
        val a = RuntimeException("a")
        val b = RuntimeException("b", a)
        a.initCause(b)
        DiagnosticSanitizer.describe(a)
        DiagnosticSanitizer.stack(a)
    }

    // --- пръстеновиден буфер ---

    private var now = 1_000L
    private fun log(store: DiagnosticStore = InMemoryDiagnosticStore(), entries: Int = 200, bytes: Int = 64 * 1024) =
        DiagnosticLog(store, { now }, entries, bytes)

    @Test fun keepsOnlyTheNewestEntries() {
        val l = log(entries = 5)
        repeat(12) { i -> now += 1; l.note("t$i", "error $i") }
        assertEquals((7..11).map { "t$it" }, l.entries().map { it.tag })
    }

    @Test fun keepsUnderTheByteLimit() {
        val store = InMemoryDiagnosticStore()
        val l = log(store, bytes = 2_000)
        repeat(100) { i -> l.record("tag$i", IOException("failure number x$i")) }
        assertTrue(store.text!!.toByteArray().size <= 2_000, "${store.text!!.length}")
        assertTrue(l.entries().isNotEmpty())
        assertEquals("tag99", l.entries().last().tag)
    }

    @Test fun repeatedErrorIsCountedNotDuplicated() {
        val l = log()
        val e = IOException("offline")
        repeat(3) { now += 10; l.record("worker.poll", e) }
        l.record("worker.sync", e)
        val list = l.entries()
        assertEquals(2, list.size)
        assertEquals(3, list[0].count)
        assertEquals(1_030L, list[0].at)
    }

    @Test fun survivesRestartAndSkipsBrokenLines() {
        val store = InMemoryDiagnosticStore()
        log(store).note("a", "first")
        log(store).note("b", "second")
        store.text = store.text + "\n{broken\n"
        val reopened = log(store)
        assertEquals(listOf("a", "b"), reopened.entries().map { it.tag })
        reopened.clear()
        assertEquals(emptyList(), log(store).entries())
    }

    @Test fun cancellationIsNotAnError() = runBlocking {
        val l = log()
        l.record("x", CancellationException("left the screen"))
        val timeout = runCatching { withTimeout(1) { delay(1_000) } }.exceptionOrNull()
        assertTrue(timeout is TimeoutCancellationException)
        l.record("x", timeout!!)
        assertTrue(l.entries().isEmpty())
    }

    @Test fun brokenStoreNeverThrows() {
        val store = object : DiagnosticStore {
            override fun read(): String = throw IOException("disk")
            override fun write(text: String) = throw IOException("disk full")
        }
        val l = log(store)
        l.note("a", "b")
        assertEquals(1, l.entries().size)
    }

    @Test fun exportIsNewestFirstWithHeaderAndNoPersonalData() {
        val l = log()
        now = 1_760_000_000_000L
        l.record("auth.refresh", IOException("401 for card=AB-1234 https://chyavorec.org/api?token=t0k3n"))
        now += 60_000
        l.note("catalog.index", "rebuild failed")
        val text = l.export(listOf("Читалище Яворец 1.0.0 (5)", "Android 15"), ZoneOffset.UTC)
        val lines = text.lines()
        assertEquals("Читалище Яворец 1.0.0 (5)", lines[0])
        assertTrue(lines[3].endsWith("[catalog.index] rebuild failed"), lines[3])
        assertTrue(lines[4].contains("[auth.refresh] java.io.IOException: 401 for card=[…] https://chyavorec.org/api"), lines[4])
        assertFalse("AB-1234" in text || "t0k3n" in text)
    }

    @Test fun globalFacadeIsSafeWithoutLogAndLogsFailures() {
        val l = log()
        Diagnostics.install(l)
        try {
            runCatching { error("boom") }.logFailure("place")
            runCatching { "ok" }.logFailure("place")
            assertEquals(listOf("place"), l.entries().map { it.tag })
        } finally {
            Diagnostics.install(null)
        }
        // Без дневник — без грешка.
        runCatching { error("boom") }.logFailure("place")
        assertEquals(1, l.entries().size)
    }
}
