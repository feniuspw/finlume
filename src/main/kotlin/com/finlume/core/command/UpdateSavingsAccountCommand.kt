package com.finlume.core.command

import java.util.*

data class UpdateSavingsAccountCommand (
    val id: UUID,
    val name: String,
)