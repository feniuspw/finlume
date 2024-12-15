package com.finlume.core.services

import com.finlume.core.domain.AssetAccount
import com.finlume.core.gateways.AssetAccountPort
import com.finlume.core.repositories.AssetAccountRepositoryPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class AssetAccountService(
    private val assetAccountRepository: AssetAccountRepositoryPort
): AssetAccountPort {
    override fun createAccount(account: AssetAccount): AssetAccount {
        return assetAccountRepository.create(account)
    }

    override fun getAccount(id: UUID): AssetAccount? {
        return assetAccountRepository.findById(id)
    }

    override fun listMyAccounts(): List<AssetAccount> {
        return assetAccountRepository.findAllByUser()
    }

    override fun updateAccount(account: AssetAccount): AssetAccount {
        return assetAccountRepository.update(account)
    }

    override fun deleteAccount(id: UUID) {
        return assetAccountRepository.delete(id)
    }

    override fun withdrawFromAccount(id: UUID, amount: Double): Boolean {
        val account = this.getAccount(id) ?: return false
        account.makeWithdraw(amount)
        assetAccountRepository.update(account)
        return true

    }

    override fun depositToAccount(id: UUID, amount: Double): Boolean {
        val account = this.getAccount(id) ?: return false
        account.makeDeposit(amount)
        assetAccountRepository.update(account)
        return true
    }

    override fun transferBetweenAccounts(fromAccountID: UUID, toAccountID: UUID, amount: Double): Boolean {
        val fromAccount = this.getAccount(fromAccountID) ?: return false
        val toAccount = this.getAccount(toAccountID) ?: return false
        fromAccount.makeWithdraw(amount)
        toAccount.makeDeposit(amount)
        assetAccountRepository.update(fromAccount)
        assetAccountRepository.update(toAccount)
        return true
    }

}