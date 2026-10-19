package org.chyavorec.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.ServiceCapabilities
import java.util.concurrent.atomic.AtomicBoolean

/** Какво знае екранът за възможностите на сървъра. */
sealed interface CapabilitiesStatus {
    /** Още се питат (първия път или след „Опитай отново“). */
    data object Loading : CapabilitiesStatus

    /** Сървърът е отговорил — вкл. „нищо не се поддържа“ (тогава входът наистина не е включен). */
    data class Known(val caps: ServiceCapabilities) : CapabilitiesStatus

    /** Питането не е успяло (без мрежа, грешка на сървъра) — НЕ значи, че входът е изключен. */
    data class Failed(val error: AppError) : CapabilitiesStatus
}

/**
 * Възможностите за екраните „Моето“ и „Вход“: разделя „не са известни“ (без връзка) от
 * „библиотеката не е включила входа“. Неуспехът не се помни като [ServiceCapabilities.NONE] —
 * [refresh] пита отново (при „Опитай отново“, при връщане на връзката, при ново показване).
 */
class CapabilitiesTracker(private val auth: AuthRepository) {
    private val _status = MutableStateFlow<CapabilitiesStatus>(CapabilitiesStatus.Loading)
    val status: StateFlow<CapabilitiesStatus> = _status.asStateFlow()
    private val running = AtomicBoolean(false)

    /** Известните възможности или `null` (още се питат / питането не е успяло). */
    val known: ServiceCapabilities? get() = (_status.value as? CapabilitiesStatus.Known)?.caps

    /**
     * Пита сървъра, освен ако входът вече е известен като включен или питане вече тече.
     * Известно „не е включен“ се проверява пак (библиотеката може да го е включила;
     * в рамките на 24 ч отговорът е от кеша, без мрежа).
     */
    suspend fun refresh() {
        if (known?.login == true) return
        if (!running.compareAndSet(false, true)) return
        try {
            // След неуспех — отново „зарежда се“, за да се види, че опитът тече.
            if (_status.value is CapabilitiesStatus.Failed) _status.value = CapabilitiesStatus.Loading
            _status.value = when (val r = auth.capabilitiesResult()) {
                is Outcome.Success -> CapabilitiesStatus.Known(r.value)
                // Сървърът отговаря, но няма такъв адрес (мостът още не е пуснат) — входът не е включен.
                is Outcome.Failure -> when {
                    r.error == AppError.NotFound || r.error is AppError.NotAvailable -> CapabilitiesStatus.Known(ServiceCapabilities.NONE)
                    // По-рано известният отговор остава (кешът му е изтекъл, а мрежата сега я няма).
                    _status.value is CapabilitiesStatus.Known -> _status.value
                    else -> CapabilitiesStatus.Failed(r.error)
                }
            }
        } finally {
            running.set(false)
        }
    }
}
