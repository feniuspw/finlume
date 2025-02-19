package com.finlume.core.command

import java.util.*

data class DepositCommand(
    val id: UUID,
    val amount: Double,
)