package com.finlume.core.command

import java.util.*

data class DepositSavingsAccountCommand(
    val id: UUID,
    val amount: Double,
)