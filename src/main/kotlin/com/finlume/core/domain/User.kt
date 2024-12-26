package com.finlume.core.domain

import java.util.*

data class User(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val password: String
) {
    init {
        require(email.contains('@')) { "Invalid user email format: $email" }
        require(password.length >= 6) { "Password must have at least 6 characters" }
    }
}
