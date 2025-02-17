package com.finlume.transport.rest.dto

data class UpdateSavingsAccountDTO(
    val name: String?,
    val interestRate: Double?,
    val interestInterval: Int?,
)