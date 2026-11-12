package org.chyavorec.data.embroidery

import org.chyavorec.domain.embroidery.StitchPattern
import java.io.File

/**
 * Шевиците на читателя — само на телефона, в малък текстов файл (до [MAX_SAVED] шевици по
 * ~230 знака) и една чернова на текущото платно. Нищо не излиза от телефона освен ако
 * читателят не сподели картичка. Четенето е търпимо: повреден ред се прескача.
 */
class EmbroideryStore(private val dir: File) {
    /** Запазена шевица: [id] (стабилен), кога е запазена и самата мрежа. */
    data class Saved(val id: String, val savedAtMs: Long, val pattern: StitchPattern)

    private val patternsFile get() = File(dir, "patterns.txt")
    private val draftFile get() = File(dir, "draft.txt")

    /** Запазените, най-новите първи. */
    fun list(): List<Saved> = runCatching {
        patternsFile.takeIf { it.isFile && it.length() < MAX_FILE_BYTES }?.readLines().orEmpty().mapNotNull(::parse)
            .distinctBy { it.id }.sortedByDescending { it.savedAtMs }.take(MAX_SAVED)
    }.getOrDefault(emptyList())

    /** Добавя [pattern]; `null`, ако вече са [MAX_SAVED] (читателят изтрива сам). */
    fun add(pattern: StitchPattern, nowMs: Long): Saved? {
        val all = list()
        if (all.size >= MAX_SAVED) return null
        // Идентификаторът е времето; при две запазвания в една милисекунда — следващото число.
        var id = nowMs
        while (all.any { it.id == id.toString() }) id++
        val saved = Saved(id.toString(), nowMs, pattern)
        write(all + saved)
        return saved
    }

    fun delete(id: String) {
        val all = list()
        if (all.none { it.id == id }) return
        write(all.filterNot { it.id == id })
    }

    fun loadDraft(): StitchPattern? = runCatching {
        draftFile.takeIf { it.isFile && it.length() < MAX_FILE_BYTES }?.readText()?.trim()?.let(StitchPattern::decode)
    }.getOrNull()

    /** Празното платно изтрива черновата. */
    fun saveDraft(pattern: StitchPattern) {
        runCatching {
            if (pattern.isEmpty) {
                draftFile.delete()
            } else {
                dir.mkdirs()
                atomicWrite(draftFile, pattern.encode())
            }
        }
    }

    /** Изтрива всичко (напр. при „Изчисти временните данни“, ако някога се иска). */
    fun clear() {
        runCatching { dir.deleteRecursively() }
    }

    private fun write(items: List<Saved>) {
        runCatching {
            dir.mkdirs()
            atomicWrite(patternsFile, items.joinToString("\n") { "${it.id}|${it.savedAtMs}|${it.pattern.encode()}" })
        }
    }

    private fun parse(line: String): Saved? {
        val parts = line.split('|')
        if (parts.size != 3) return null
        val id = parts[0].takeIf { it.isNotEmpty() && it.length <= 20 && it.all(Char::isDigit) } ?: return null
        val at = parts[1].toLongOrNull() ?: return null
        val pattern = StitchPattern.decode(parts[2]) ?: return null
        return Saved(id, at, pattern)
    }

    private fun atomicWrite(target: File, text: String) {
        val tmp = File(target.parentFile, target.name + ".tmp")
        tmp.writeText(text)
        if (!tmp.renameTo(target)) {
            target.delete()
            check(tmp.renameTo(target)) { "rename" }
        }
    }

    companion object {
        const val MAX_SAVED = 8
        /** Файл, по-голям от това, е повреден или чужд — не се чете. */
        private const val MAX_FILE_BYTES = 64 * 1024L
    }
}
