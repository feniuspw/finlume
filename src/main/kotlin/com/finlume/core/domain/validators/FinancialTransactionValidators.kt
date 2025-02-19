package com.finlume.core.domain.validators

import com.finlume.core.domain.exceptions.InsufficientFundsException
import com.finlume.core.domain.exceptions.InvalidAmountException

fun validateCanDeposit(amount: Double) {
    if(amount <= 0.0) {
        throw InvalidAmountException("Deposit amount must be positive")
    }
}

fun validateCanWithdraw(amount: Double, balance: Double) {
    if(amount <= 0.0) {
        throw InvalidAmountException("Withdrawal amount must be positive")
    }
    if(balance <= 0.0 || amount > balance) {
        throw InsufficientFundsException("Insufficient funds: withdrawal amount must not exceed the available balance")
    }
}