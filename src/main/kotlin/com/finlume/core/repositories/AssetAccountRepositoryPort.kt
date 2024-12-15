package com.finlume.core.repositories

import com.finlume.core.domain.AssetAccount
import org.springframework.stereotype.Repository
import java.util.UUID

interface AssetAccountRepositoryPort {
    fun create(account: AssetAccount): AssetAccount
    fun findById(id: UUID): AssetAccount?
    fun findAllByUser(): List<AssetAccount>
    fun update(account: AssetAccount): AssetAccount
    fun delete(id: UUID)
}