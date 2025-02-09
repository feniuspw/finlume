package com.finlume.core.command

import com.finlume.core.domain.Account

data class CreateSavingsAccountCommand(
    val name: String,
    val balance: Double,
    val interestRate: Double,
    val interestInterval: Int,
    val account: Account,
)
