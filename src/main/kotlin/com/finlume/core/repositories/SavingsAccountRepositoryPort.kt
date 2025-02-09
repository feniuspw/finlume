package com.finlume.core.repositories
import java.util.*
import com.finlume.core.domain.SavingsAccount

interface SavingsAccountRepositoryPort {
    fun save(savingsAccount: SavingsAccount): SavingsAccount
    fun findById(id: UUID): SavingsAccount?
    fun findByAccountId(accountId: UUID): List<SavingsAccount>?
}