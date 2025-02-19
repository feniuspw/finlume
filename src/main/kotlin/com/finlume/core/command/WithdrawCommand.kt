package com.finlume.core.command

import java.util.*

data class WithdrawCommand(
    val id: UUID,
    val amount: Double,
)
