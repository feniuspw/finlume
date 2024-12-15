package com.finlume.core.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


data class Bill(
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val currency: Currency,
    val minAmount: Long,
    val maxAmount: Long,
    val name: String,
    val description: String = "",
    val paymentDate: LocalDate,
    val isActive: Boolean = true,
    val repeatInterval: RepeatInterval,
    val creation: LocalDateTime = LocalDateTime.now(),
    val lastUpdate: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(minAmount >= 0) { "minAmount must be non-negative" }
        require(maxAmount >= minAmount) { "maxAmount must be greater than or equal to minAmount" }
    }

    fun getNextPaymentDate(): LocalDate {
        return when (repeatInterval) {
            RepeatInterval.DAILY -> paymentDate.plusDays(1)
            RepeatInterval.WEEKLY -> paymentDate.plusWeeks(1)
            RepeatInterval.BIWEEKLY -> paymentDate.plusWeeks(2)
            RepeatInterval.MONTHLY -> paymentDate.plusMonths(1)
            RepeatInterval.SEMIANNUAL -> paymentDate.plusMonths(6)
            RepeatInterval.YEARLY -> paymentDate.plusYears(1)
        }
    }

    enum class RepeatInterval {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY, SEMIANNUAL, YEARLY
    }

}