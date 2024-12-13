package com.finlume.core.domain

import java.time.LocalDate


data class SavingsAccount(
    val user: User,
    val currency: Currency,
    val name: String,
    private var balance: Double,
    private val interestRate: Double,
    private val interestInterval: Int, // days
    private val lastInterestRateDate: LocalDate
) {
    init {
        require(name.isNotBlank()) { "Account name must not be blank" }
        require(balance >= 0) { "Initial balance cannot be negative" }
        require(interestRate > 0) { "Interest rate must be positive" }
        require(interestInterval > 0) { "Interest interval must be positive" }
        require(!lastInterestRateDate.isAfter(LocalDate.now())) {
            "Last interest rate date cannot be in the future"
        }
    }
    fun getBalance(): Double = balance
    fun getInterestRate(): Double = interestRate
    fun calculateInterest(): Double = balance * interestRate

}