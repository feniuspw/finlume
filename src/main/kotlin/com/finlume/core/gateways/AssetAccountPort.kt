package com.finlume.core.gateways

import com.finlume.core.domain.AssetAccount
import org.springframework.context.annotation.Bean
import java.util.UUID


interface AssetAccountPort {
    fun createAccount(account: AssetAccount): AssetAccount
    fun getAccount(id: UUID): AssetAccount?
    fun listMyAccounts(): List<AssetAccount>
    fun updateAccount(account: AssetAccount): AssetAccount
    fun deleteAccount(id: UUID)
    fun withdrawFromAccount(id: UUID, amount: Double): Boolean
    fun depositToAccount(id: UUID, amount: Double): Boolean
    fun transferBetweenAccounts(fromAccountID: UUID, toAccountID: UUID, amount: Double): Boolean
}