package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/**
 * Модели на читателските данни. Те идват САМО от онлайн API на InvLib, който
 * към момента НЕ съществува (виж ANALYSIS.md) — докато го няма, приложението
 * не показва такива данни (в production), освен ръчно въведената от читателя карта.
 */
@Serializable
data class ReaderProfile(
    val readerId: String,
    val cardNumber: String,
    val fullName: String,
    val photoUrl: String? = null,
    val category: String? = null,
    val email: String? = null,
    /** ISO дата на регистрация. */
    val registeredOn: String? = null,
    val membership: Membership? = null,
)

@Serializable
data class Membership(
    val memberNumber: String,
    val holderName: String,
    /** ISO дата. */
    val since: String? = null,
    /** ISO дата, до която е валидна регистрацията/членството. */
    val validUntil: String? = null,
    /** Статусът, както го връща библиотечната система. */
    val status: MembershipStatus,
    /** Съдържание за QR/баркод, ако системата го предоставя. */
    val barcodePayload: String? = null,
)

@Serializable
enum class MembershipStatus { ACTIVE, EXPIRED, SUSPENDED, UNKNOWN }

@Serializable
data class Loan(
    val loanId: String,
    val inv: Long?,
    val title: String,
    val author: String,
    val coverUrl: String? = null,
    /** ISO дата на заемане (може да липсва в отговора на сървъра). */
    val borrowedOn: String? = null,
    /** ISO краен срок (може да липсва в отговора на сървъра). */
    val dueOn: String? = null,
    val renewals: Int = 0,
    val canRenew: Boolean = false,
)

/** Индикатор на срока — изчислява се от реалните дати ([LoanDueCalculator]). */
enum class DueStatus { PLENTY_OF_TIME, DUE_SOON, OVERDUE }

/** Какво поддържа свързаният библиотечен сървър. */
@Serializable
data class ServiceCapabilities(
    val login: Boolean = false,
    val profile: Boolean = false,
    val loans: Boolean = false,
    val membership: Boolean = false,
    val holds: Boolean = false,
    val renew: Boolean = false,
    val passwordReset: Boolean = false,
    val accountDeletion: Boolean = false,
    val push: Boolean = false,
    val availability: Boolean = false,
) {
    companion object {
        val NONE = ServiceCapabilities()
    }
}

/** Сесия след успешен вход. Паролата НИКОГА не се пази. */
@Serializable
data class AuthSession(
    val accessToken: String,
    val refreshToken: String? = null,
    /** Епоха в милисекунди. */
    val expiresAtMillis: Long,
    val readerId: String,
)

/**
 * Карта, въведена ръчно от самия читател (номерът от физическата му карта).
 * Не е проверена от библиотеката — UI го казва изрично.
 */
@Serializable
data class SelfDeclaredCard(val cardNumber: String, val holderName: String)
