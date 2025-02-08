package com.finlume.core.dto.account

data class CreateAccountDTO(
    val name: String,
    val description: String = ""
)
