package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.SavingsAccount
import com.finlume.core.repositories.SavingsAccountRepositoryPort
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemorySavingsAccountRepositoryAdapter: SavingsAccountRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, SavingsAccount>()

    override fun save(savingsAccount: SavingsAccount): SavingsAccount {
        if (storage.containsKey(savingsAccount.id)) {
            throw IllegalArgumentException("SavingsAccount with ID ${savingsAccount.id} already exists.")
        }
        storage[savingsAccount.id] = savingsAccount
        return savingsAccount
    }

    override fun findById(id: UUID): SavingsAccount? {
        return storage[id]
    }

    override fun findByAccountId(accountId: UUID): List<SavingsAccount>? {
        return storage.values.filter { it.account.id == accountId }
    }

    override fun findAll(): List<SavingsAccount> {
        return storage.values.toList()
    }

    override fun update(account: Account): Account {
        if (!storage.containsKey(account.id)) {
            throw IllegalArgumentException("Account with ID ${account.id} does not exist.")
        }
        storage[account.id] = account
        return account
    }

    override fun softDelete(id: UUID) {
        val account = storage[id] ?: throw IllegalArgumentException("Account with ID $id does not exist.")
        val updatedAccount = account.copy(active = false, lastUpdated = LocalDateTime.now())
        storage[id] = updatedAccount
    }
}