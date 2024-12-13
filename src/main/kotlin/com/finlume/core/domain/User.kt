package com.finlume.core.domain

data class User(
    val email: String,
    val password: String
) {
    init {
        require(email.contains('@')) { "Invalid user email format: $email" }
        require(password.length >= 6) { "Password must have at least 6 characters" }
    }
}
