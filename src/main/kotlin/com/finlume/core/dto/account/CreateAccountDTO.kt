package com.finlume.core.dto.account

import com.finlume.core.domain.Currency

data class CreateAccountDTO(
    val name: String,
    val currency: Currency,
    val description: String = ""
)
