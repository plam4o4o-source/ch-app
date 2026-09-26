package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Съдържание, извлечено от HTML страница на сайта и превърнато в структурирани
 * блокове. Приложението ги рисува с native Compose компоненти — без WebView.
 */
@Serializable
sealed interface ContentBlock {
    @Serializable
    data class Heading(val level: Int, val text: String) : ContentBlock

    @Serializable
    data class Paragraph(val runs: List<TextRun>) : ContentBlock {
        val plainText: String get() = runs.joinToString("") { it.text }
    }

    @Serializable
    data class Image(val url: String, val caption: String? = null) : ContentBlock

    @Serializable
    data class BulletList(val items: List<List<TextRun>>, val ordered: Boolean = false) : ContentBlock

    @Serializable
    data class Quote(val text: String) : ContentBlock

    /** Бутон/връзка към файл или външен ресурс (напр. PDF документ). */
    @Serializable
    data class LinkButton(val text: String, val url: String) : ContentBlock
}

@Serializable
data class TextRun(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val url: String? = null,
)

fun List<ContentBlock>.plainText(): String = joinToString("\n") { block ->
    when (block) {
        is ContentBlock.Heading -> block.text
        is ContentBlock.Paragraph -> block.plainText
        is ContentBlock.BulletList -> block.items.joinToString("\n") { runs -> runs.joinToString("") { it.text } }
        is ContentBlock.Quote -> block.text
        is ContentBlock.LinkButton -> block.text
        is ContentBlock.Image -> block.caption.orEmpty()
    }
}
