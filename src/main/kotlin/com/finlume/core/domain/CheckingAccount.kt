package com.finlume.core.domain

import java.time.LocalDateTime
import java.util.*

data class CheckingAccount(
    val id: UUID = UUID.randomUUID(),
    val account: Account,
    private var balance: Double,
    private val overdraftLimit: Double = 0.0,
    private val monthlyFee: Double = 0.0,
) {
    init {
        require(overdraftLimit >= 0) { "Overdraft limit must be non-negative" }
        require(monthlyFee >= 0) { "Monthly fee must be non-negative" }
        require(balance >= 0) { "Balance must be non-negative" }
    }

    fun getBalance(): Double = balance

    fun deposit(amount: Double) {
        require(amount > 0) { "Deposit amount must be positive" }
        balance += amount
    }

    fun withdraw(amount: Double) {
        require(amount > 0) { "Withdrawal amount must be positive" }
        require(balance - amount >= -overdraftLimit) { "Withdrawal amount exceeds overdraft limit" }
        balance -= amount
    }

    fun applyMonthlyFee() {
        if (monthlyFee > 0) {
            require(balance - monthlyFee >= -overdraftLimit) { "Monthly fee cannot be applied because it exceeds overdraft limit" }
            balance -= monthlyFee
        }
    }
}