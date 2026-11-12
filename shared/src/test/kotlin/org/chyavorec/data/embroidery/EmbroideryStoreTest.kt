package org.chyavorec.data.embroidery

import org.chyavorec.domain.embroidery.EmbroideryTemplates
import org.chyavorec.domain.embroidery.StitchPattern
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EmbroideryStoreTest {
    private val dir: File = Files.createTempDirectory("shevitsa").toFile().resolve("sub")
    private val store = EmbroideryStore(dir)
    private val tree = EmbroideryTemplates.treeOfLife()
    private val zig = EmbroideryTemplates.zigzag()

    @AfterTest fun cleanup() {
        dir.parentFile.deleteRecursively()
    }

    @Test fun emptyStoreHasNothing() {
        assertTrue(store.list().isEmpty())
        assertNull(store.loadDraft())
    }

    @Test fun savedPatternsComeBackNewestFirst() {
        store.add(tree, 1_000)
        store.add(zig, 2_000)
        val all = store.list()
        assertEquals(listOf(zig, tree), all.map { it.pattern })
        assertEquals(listOf(2_000L, 1_000L), all.map { it.savedAtMs })
    }

    @Test fun twoSavesInOneMillisecondGetDifferentIds() {
        val a = assertNotNull(store.add(tree, 5_000))
        val b = assertNotNull(store.add(zig, 5_000))
        assertTrue(a.id != b.id)
        assertEquals(2, store.list().size)
    }

    @Test fun savingIsBoundedAndDeleteMakesRoom() {
        repeat(EmbroideryStore.MAX_SAVED) { assertNotNull(store.add(tree, 1_000L + it)) }
        assertNull(store.add(zig, 9_000))
        assertEquals(EmbroideryStore.MAX_SAVED, store.list().size)
        val oldest = store.list().last()
        store.delete(oldest.id)
        assertNotNull(store.add(zig, 9_000))
        assertEquals(zig, store.list().first().pattern)
    }

    @Test fun deleteUnknownIdIsHarmless() {
        store.add(tree, 1)
        store.delete("nope")
        assertEquals(1, store.list().size)
    }

    @Test fun draftRoundTripsAndAnEmptyCanvasRemovesIt() {
        store.saveDraft(tree)
        assertEquals(tree, store.loadDraft())
        store.saveDraft(StitchPattern.empty())
        assertNull(store.loadDraft())
        assertFalse(File(dir, "draft.txt").exists())
    }

    @Test fun corruptLinesAndFilesAreSkippedNotFatal() {
        dir.mkdirs()
        val good = "7|7000|" + tree.encode()
        File(dir, "patterns.txt").writeText(
            listOf("", "garbage", "x|1|" + tree.encode(), "8|abc|" + tree.encode(), "9|9|15:12", good, "10|10|" + tree.encode() + "|extra").joinToString("\n"),
        )
        assertEquals(listOf("7"), store.list().map { it.id })
        File(dir, "draft.txt").writeText("not a pattern")
        assertNull(store.loadDraft())
    }

    @Test fun anOversizedFileIsIgnored() {
        dir.mkdirs()
        File(dir, "patterns.txt").writeText("1|1|" + tree.encode() + "\n" + "x".repeat(70_000))
        assertTrue(store.list().isEmpty())
    }

    @Test fun clearRemovesEverything() {
        store.add(tree, 1)
        store.saveDraft(zig)
        store.clear()
        assertTrue(store.list().isEmpty())
        assertNull(store.loadDraft())
    }
}
