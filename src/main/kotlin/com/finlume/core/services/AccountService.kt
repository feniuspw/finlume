package com.finlume.core.services

import com.finlume.core.domain.Account
import com.finlume.core.gateways.AccountPort
import com.finlume.core.repositories.AccountRepositoryPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepositoryPort
): AccountPort {
    override fun createAccount(account: Account): Account {
        return accountRepository.create(account)
    }

    override fun getAccount(id: UUID): Account? {
        return accountRepository.findById(id)
    }

    override fun listAccounts(): List<Account>? {
        return accountRepository.listAll()
    }

    override fun updateAccount(account: Account): Account {
        return accountRepository.update(account)
    }

    override fun deleteAccount(id: UUID) {
        return accountRepository.delete(id)
    }

}