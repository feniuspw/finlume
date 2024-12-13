package com.finlume.core.domain

import java.time.LocalDateTime
import java.util.UUID


data class AssetAccount(
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val currency: Currency,
    val name: String,
    private var balance: Double,
    val creation: LocalDateTime = LocalDateTime.now(),
    val lastUpdated: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
    }

    private fun validateCanWithdraw(amount: Double) {
        require(amount > 0) { "Withdrawal amount must be positive" }
//        require(balance >= amount) { "Insufficient funds" }
    }

    private fun deposit(amount: Double): Double {
        require(amount > 0) { "Deposit amount must be positive" }
        balance += amount
        return balance
    }

    private fun withdraw(amount: Double): Double {
        validateCanWithdraw(amount)
        balance -= amount
        return balance
    }

    fun transferTo(account: AssetAccount, amount: Double): Boolean {
        withdraw(amount)
        return try {
            account.deposit(amount)
            true
        } catch (e: Exception) {
            deposit(amount)
            false
        }
    }

    fun makeDeposit(amount: Double): Double = deposit(amount)

    fun getBalance(): Double = balance

    fun getFormatedBalance(): String = "${currency.symbol} $balance"
}