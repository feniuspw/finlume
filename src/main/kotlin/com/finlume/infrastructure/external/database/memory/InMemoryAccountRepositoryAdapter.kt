package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.Account
import com.finlume.core.repositories.AccountRepositoryPort
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryAccountRepositoryAdapter: AccountRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, Account>()

    override fun create(account: Account): Account {
        if (storage.containsKey(account.id)) {
            throw IllegalArgumentException("Account with ID ${account.id} already exists.")
        }
        storage[account.id] = account
        return account
    }

    override fun findById(id: UUID): Account? {
        return storage[id]
    }

    override fun listAll(): List<Account>? {
        val values = storage.values.toList()
        return values.ifEmpty { null }
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