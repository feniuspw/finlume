package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.AssetAccount
import com.finlume.core.repositories.AssetAccountRepositoryPort
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryAssetAccountRepositoryAdapter: AssetAccountRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, AssetAccount>()

    override fun create(account: AssetAccount): AssetAccount {
        if (storage.containsKey(account.id)) {
            throw IllegalArgumentException("Account with ID ${account.id} already exists.")
        }
        storage[account.id] = account
        return account
    }

    override fun findById(id: UUID): AssetAccount? {
        return storage[id]
    }

    override fun findAllByUser(): List<AssetAccount> {
        return storage.values.toList()
    }

    override fun update(account: AssetAccount): AssetAccount {
        if (!storage.containsKey(account.id)) {
            throw IllegalArgumentException("Account with ID ${account.id} does not exist.")
        }
        storage[account.id] = account
        return account
    }

    override fun delete(id: UUID) {
        storage.remove(id)
    }
}