package com.finlume.core.command

import com.finlume.core.domain.Currency

data class CreateAccountCommand(
    val name: String,
    val currency: Currency,
    val description: String = ""
)
