package com.finlume.core.command

data class CreateWalletCommand(
    val name: String,
    val balance: Double = 0.0,
    val currencyId: Int
)
