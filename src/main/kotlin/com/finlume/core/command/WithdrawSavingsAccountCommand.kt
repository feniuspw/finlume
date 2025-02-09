package com.finlume.core.command

import java.util.*

data class WithdrawSavingsAccountCommand(
    val id: UUID,
    val amount: Double,
)
