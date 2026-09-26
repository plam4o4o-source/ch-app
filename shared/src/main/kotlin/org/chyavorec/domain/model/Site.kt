package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/** Раздел на сайта (от индекса на сайта javora/index.json). */
@Serializable
data class SiteLink(val title: String, val url: String, val kind: SiteSection)

/** Разделите на сайта, които приложението разпознава. */
@Serializable
enum class SiteSection {
    ABOUT, HISTORY, LIBRARY, CATALOG, EVENTS, FOLKLORE, ENSEMBLE, DANCE, CLUBS, PROJECTS,
    DIGITAL_CLUB, CONTACTS, GALLERY, EXHIBITION, NEWS, DOCUMENTS, DONATIONS, PUBLICATIONS,
    VILLAGE, NATURE, PRIVACY, ACCESSIBILITY, TERMS, OTHER,
}

@Serializable
data class SitePage(
    val url: String,
    val title: String,
    val blocks: List<ContentBlock>,
    val images: List<String>,
)

@Serializable
data class ContactPerson(val role: String, val name: String)

@Serializable
data class Contacts(
    val organization: String,
    val address: String?,
    val persons: List<ContactPerson> = emptyList(),
    val phones: List<String>,
    val emails: List<String>,
    val website: String,
    val facebook: String?,
    val workingHours: List<String>,
    val mapQuery: String,
    /** true, когато данните са прочетени от страницата „Контакти“ на сайта. */
    val fromSite: Boolean,
    val sourceUrl: String?,
)

@Serializable
data class GalleryPhoto(
    val id: String,
    val title: String,
    val thumbUrl: String,
    val fullUrl: String,
    val album: String,
    val pageUrl: String,
    val publishedAtMillis: Long? = null,
)

data class GalleryAlbum(val name: String, val photos: List<GalleryPhoto>) {
    val cover: GalleryPhoto? get() = photos.firstOrNull()
}

/** Документ (устав, отчет, декларация) или историческа публикация (PDF). */
@Serializable
data class SiteDocument(
    val id: String,
    val title: String,
    val url: String,
    val date: String? = null,
    val category: String? = null,
    val size: String? = null,
    val description: String = "",
    val kind: DocumentKind = DocumentKind.DOCUMENT,
)

@Serializable
enum class DocumentKind { DOCUMENT, PUBLICATION }

/** Запис от търсещия индекс на сайта. */
@Serializable
data class SiteSearchDoc(
    val id: String,
    val type: String,
    val title: String,
    val url: String,
    val excerpt: String = "",
    val text: String = "",
    val date: String? = null,
    val category: String? = null,
    val fileUrl: String? = null,
)

/** Православният празник за деня (от /api/calendar на сайта). */
@Serializable
data class DailyFeast(val date: String, val line: String)
