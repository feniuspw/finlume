package com.finlume.transport.rest.dto


data class CreateAccountRequestDTO(
    val name: String,
    val currencyCode: Int,
    val description: String = ""
)
