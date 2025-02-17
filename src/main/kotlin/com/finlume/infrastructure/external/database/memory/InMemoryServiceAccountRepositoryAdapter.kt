package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.SavingsAccount
import com.finlume.core.repositories.SavingsAccountRepositoryPort
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemorySavingsAccountRepositoryAdapter: SavingsAccountRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, SavingsAccount>()

    override fun save(savingsAccount: SavingsAccount): SavingsAccount {
        storage[savingsAccount.id] = savingsAccount
        return savingsAccount
    }

    override fun findById(id: UUID): SavingsAccount? {
        return storage[id]
    }

    override fun findByAccountId(accountId: UUID): List<SavingsAccount>? {
        return storage.values.filter { it.account.id == accountId }
    }

    override fun delete(id: UUID) {
        storage.remove(id)
    }

}