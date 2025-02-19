package com.finlume.core.repositories

import com.finlume.core.domain.Wallet
import java.util.*

interface WalletRepositoryPort {
    fun save(wallet: Wallet): Wallet
    fun findById(id: UUID): Wallet?
    fun delete(id: UUID)
}