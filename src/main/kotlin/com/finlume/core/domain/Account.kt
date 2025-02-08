package com.finlume.core.domain

import java.time.LocalDateTime
import java.util.*

data class Account(

    val id: UUID = UUID.randomUUID(),
    val name: String,
    val currency: Currency,
    val description: String = "",
    val active: Boolean = true,
    val creation: LocalDateTime = LocalDateTime.now(),
    val lastUpdated: LocalDateTime = LocalDateTime.now()

)