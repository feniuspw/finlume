package com.finlume.core.repositories

import com.finlume.core.domain.Account
import java.util.UUID

interface AccountRepositoryPort {
    fun create(account: Account): Account
    fun findById(id: UUID): Account?
    fun listAll(): List<Account>?
    fun update(account: Account): Account
    fun softDelete(id: UUID)
}