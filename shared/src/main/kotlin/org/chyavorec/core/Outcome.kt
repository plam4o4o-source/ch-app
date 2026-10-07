package org.chyavorec.core

/**
 * Резултат от операция, която може да се провали по предвидим начин.
 *
 * За разлика от [kotlin.Result], грешката е типизирана ([AppError]) — UI слоят
 * превежда всеки вид в ясно съобщение за потребителя, без да показва технически
 * подробности.
 */
sealed interface Outcome<out T> {
    data class Success<T>(val value: T) : Outcome<T>
    data class Failure(val error: AppError) : Outcome<Nothing>
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(value))
    is Outcome.Failure -> this
}

fun <T> Outcome<T>.getOrNull(): T? = (this as? Outcome.Success)?.value

fun <T> Outcome<T>.errorOrNull(): AppError? = (this as? Outcome.Failure)?.error

/** Видове грешки, които приложението различава. */
sealed interface AppError {
    /** Няма връзка със сървъра (DNS, timeout, липса на интернет). */
    data object Network : AppError

    /** Сървърът отговори с грешка. */
    data class Server(val httpCode: Int) : AppError

    /** Отговорът не можа да бъде разчетен (сменен формат на сайта/файла). */
    data class Parse(val detail: String) : AppError

    /** Сесията е изтекла или данните за вход са грешни. */
    data object Unauthorized : AppError

    /** Твърде много опити — клиентът временно спира заявките. */
    data class RateLimited(val retryAfterSeconds: Long) : AppError

    /** Търсеният запис не съществува. */
    data object NotFound : AppError

    /**
     * Сървърът отказа действието заради текущото състояние (HTTP 409), напр.
     * `pending` — вече има чакаща заявка, `not_allowed` — не е позволено.
     */
    data class Conflict(val code: String) : AppError {
        companion object {
            const val PENDING = "pending"
            const val NOT_ALLOWED = "not_allowed"
        }
    }

    /**
     * Функцията зависи от услуга, която библиотечната система (InvLib) още НЕ
     * предоставя онлайн. Това не е грешка на потребителя — UI показва обяснение.
     */
    data class NotAvailable(val feature: Feature) : AppError

    data class Unexpected(val detail: String) : AppError
}

/** Функции, които изискват онлайн достъп до библиотечната система. */
enum class Feature {
    LOGIN, PROFILE, LOANS, MEMBERSHIP, HOLDS, RENEW, PASSWORD_RESET, ACCOUNT_DELETION, PUSH, HISTORY
}
