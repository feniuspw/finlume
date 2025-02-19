package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.Wallet
import com.finlume.core.repositories.WalletRepositoryPort
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryWalletRepositoryAdapter: WalletRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, Wallet>()

    override fun save(wallet: Wallet): Wallet {
        storage[wallet.id] = wallet
        return wallet
    }

    override fun findById(id: UUID): Wallet? {
        return storage[id]
    }

    override fun delete(id: UUID) {
        storage.remove(id)
    }

}