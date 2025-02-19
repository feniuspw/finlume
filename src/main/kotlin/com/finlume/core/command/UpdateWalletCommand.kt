package com.finlume.core.command

import java.util.*

data class UpdateWalletCommand(
    val id: UUID,
    val name: String
)
