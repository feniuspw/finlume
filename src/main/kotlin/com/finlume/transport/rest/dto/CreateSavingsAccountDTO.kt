package com.finlume.transport.rest.dto

import java.util.UUID


data class CreateSavingsAccountDTO(
    val name: String,
    val accountId: UUID,
    var balance: Double = 0.0,
    val interestRate: Double = 0.0,
    val interestInterval: Int = 0,
)
