package com.finlume.core.domain

import java.time.LocalDateTime

data class Currency(
    val id: Long,
    val code: String,
    val name: String,
    val symbol: String,
    val isActive: Boolean = true,
    val creation: LocalDateTime = LocalDateTime.now(),
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)
