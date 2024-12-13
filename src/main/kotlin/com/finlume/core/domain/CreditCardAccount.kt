package com.finlume.core.domain

import java.time.LocalDate

data class CreditCardAccount(
    val user: User,
    val name: String,
    val limit: Double,
    var balance: Double = 0.0,
    val paymentDate: LocalDate,
    val closeDate: LocalDate,
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(limit > 0) { "Limit must be positive" }
    }

    fun getAvailableLimit(): Double = limit - balance
}