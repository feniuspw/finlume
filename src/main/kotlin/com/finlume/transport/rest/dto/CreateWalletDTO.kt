package com.finlume.transport.rest.dto

data class CreateWalletDTO(
    val name: String,
    val balance: Double = 0.0,
    val currencyId: Int,

)