package org.chyavorec.data.invlib

import kotlinx.serialization.Serializable

/*
 * DTO-та по ПРЕДЛОЖЕНИЯ договор в docs/API.md. Такъв сървър все още НЕ
 * съществува — това е спецификацията, която InvLib (или отделен мост към
 * базата му) трябва да реализира. Имената на полетата следват схемата на
 * InvLib (readers.card_no, loans.date_out/date_due, holds, settings.loan_days).
 */

@Serializable
data class CapabilitiesDto(
    val apiVersion: Int = 1,
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
    val history: Boolean = false,
    val messages: Boolean = false,
)

@Serializable
data class LoginRequestDto(val cardNumber: String, val password: String, val deviceName: String)

@Serializable
data class RefreshRequestDto(val refreshToken: String)

@Serializable
data class PasswordResetRequestDto(val cardNumber: String)

@Serializable
data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String? = null,
    /** Секунди до изтичане на accessToken. */
    val expiresIn: Long,
    val readerId: String,
)

@Serializable
data class ReaderDto(
    val readerId: String,
    val cardNumber: String,
    val fullName: String,
    val photoUrl: String? = null,
    val category: String? = null,
    val email: String? = null,
    val registeredOn: String? = null,
)

@Serializable
data class LoanDto(
    val loanId: String,
    val inv: Long? = null,
    val title: String,
    val author: String = "",
    val coverUrl: String? = null,
    val dateOut: String? = null,
    val dateDue: String? = null,
    val renewals: Int = 0,
    val canRenew: Boolean = false,
    /** Заявено удължаване, което InvLib още не е обработил. */
    val renewPending: Boolean = false,
    /** Последен резултат от удължаване (до 7 дни назад). */
    val renewResult: RenewResultDto? = null,
)

@Serializable
data class RenewResultDto(
    /** done | rejected */
    val status: String = "",
    val reason: String? = null,
    val at: String? = null,
)

@Serializable
data class LoansResponseDto(val loans: List<LoanDto>)

@Serializable
data class HistoryItemDto(
    val loanId: String,
    val inv: Long? = null,
    val title: String = "",
    val author: String = "",
    val dateOut: String? = null,
    val dateIn: String? = null,
)

@Serializable
data class HistoryResponseDto(val items: List<HistoryItemDto> = emptyList(), val generated: String? = null)

@Serializable
data class ReaderMessageDto(
    val id: String,
    val title: String? = null,
    val text: String? = null,
    val at: String? = null,
    val read: Boolean = false,
)

@Serializable
data class ReaderMessagesResponseDto(val items: List<ReaderMessageDto> = emptyList(), val generated: String? = null)

@Serializable
data class MembershipDto(
    val memberNumber: String,
    val holderName: String,
    val since: String? = null,
    val validUntil: String? = null,
    /** active | expired | suspended */
    val status: String,
    val barcode: String? = null,
)

@Serializable
data class HoldRequestDto(val inv: Long)

@Serializable
data class AvailabilityDto(
    val inv: Long,
    /** available | on_loan | unavailable */
    val status: String,
    val dueOn: String? = null,
)

@Serializable
data class ErrorDto(val error: String, val message: String? = null)
