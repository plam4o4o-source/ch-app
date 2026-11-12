package org.chyavorec.domain.quiz

import kotlinx.serialization.Serializable
import java.time.LocalDate

/** Откъде е въпросът — интерфейсът го „облича“ с превеждаем текст ([subject] е подлежащото). */
@Serializable
enum class QuizKind {
    /** „Коя година е: <веха>?“ — отговорите са години. */
    TIMELINE_YEAR,

    /** „Какво се случва през <година>?“ — отговорите са вехи. */
    TIMELINE_EVENT,

    /** „Кой е авторът на <книга>?“ */
    CATALOG_AUTHOR,

    /** „Коя от тези книги е написана от <автор>?“ */
    CATALOG_TITLE,

    /** „Какво е на снимката?“ — [QuizQuestion.imageUrl] е снимката от изложбата. */
    PHOTO,

    /** Въпрос, написан от читалището (`data/app-quiz.json`) — [QuizQuestion.subject] е целият текст. */
    CUSTOM,
}

@Serializable
data class QuizQuestion(
    /** Постоянен в рамките на деня (за запис на отговорите). */
    val id: String,
    val kind: QuizKind,
    val subject: String,
    /** 2–4 различни отговора, вече в реда, в който се показват. */
    val options: List<String>,
    val correctIndex: Int,
    /** Кратка бележка, която се показва след отговора (описанието на вехата, пояснение от читалището). */
    val note: String? = null,
    val imageUrl: String? = null,
) {
    val correct: String get() = options[correctIndex]
}

/** Въпросите за деня. */
@Serializable
data class Quiz(val date: String, val questions: List<QuizQuestion>) {
    val localDate: LocalDate get() = LocalDate.parse(date)
}

/**
 * Въпрос, написан от читалището в по избор файла `data/app-quiz.json`:
 * `{"questions":[{"id":"sabor-1","text":"…?","options":["А","Б","В","Г"],"answer":2,"note":"…","image":"/pictures/x.webp","shuffle":false}]}`.
 */
@Serializable
data class CustomQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    /** Индекс (от 0) на верния отговор в [options]. */
    val answer: Int,
    val note: String? = null,
    /** Снимка (адрес в сайта или https). */
    val image: String? = null,
    /** `true` — отговорите се разбъркват; иначе остават в реда на автора. */
    val shuffle: Boolean = false,
)

/** Снимка с подпис от изложбата („Жътва“, „Харманът“) — за въпроса „Какво е на снимката?“. */
data class QuizPhoto(val caption: String, val url: String)

/** Всичко, от което се съставя викторината; празните части просто не дават въпроси. */
data class QuizSources(
    val timeline: List<org.chyavorec.domain.timeline.TimelineEntry> = emptyList(),
    val books: List<org.chyavorec.domain.model.CatalogBook> = emptyList(),
    val photos: List<QuizPhoto> = emptyList(),
    val custom: List<CustomQuestion> = emptyList(),
)
