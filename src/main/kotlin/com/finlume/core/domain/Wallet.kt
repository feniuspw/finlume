package com.finlume.core.domain

import com.finlume.core.domain.validators.validateCanDeposit
import com.finlume.core.domain.validators.validateCanWithdraw
import java.time.LocalDateTime
import java.util.*

data class Wallet(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    private var balance: Double,
    val currency: Currency,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(name.isNotBlank()) { "Wallet name must not be blank" }
        require(balance >= 0) { "Initial balance cannot be negative" }
    }

    fun getBalance(): Double = balance
    fun deposit(amount: Double): Double {
        validateCanDeposit(amount)
        balance += amount
        return balance
    }
    fun withdraw(amount: Double): Double {
        validateCanWithdraw(amount, balance)
        balance -= amount
        return balance
    }
}