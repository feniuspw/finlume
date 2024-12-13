package com.finlume.interactors

import com.finlume.core.domain.AssetAccount
import java.util.UUID

interface AssetAccountPort {
    fun createAccount(account: AssetAccount): AssetAccount
    fun getAccount(id: UUID): AssetAccount?
    fun getAllAccounts(): List<AssetAccount>
    fun updateAccount(account: AssetAccount): AssetAccount
    fun deleteAccount(id: UUID)
}