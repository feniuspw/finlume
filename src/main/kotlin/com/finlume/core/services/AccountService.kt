package com.finlume.core.services

import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import com.finlume.core.dto.account.CreateAccountDTO
import com.finlume.core.dto.account.UpdateAccountDTO
import com.finlume.core.gateways.AccountPort
import com.finlume.core.repositories.AccountRepositoryPort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepositoryPort

): AccountPort {
    override fun createAccount(request: CreateAccountDTO): Account {
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")

        val account = Account(
            name = request.name,
            description = request.description,
            currency = currency,
        )

        return accountRepository.create(account)
    }

    override fun findAccountById(id: UUID): Account? {
        return accountRepository.findById(id)
    }

    override fun findAllAccounts(): List<Account>? {
        return accountRepository.listAll()
    }

    override fun updateAccount(request: UpdateAccountDTO, id: UUID): Account {
        // Get existing account
        val existingAccount: Account = accountRepository.findById(id)
            ?: throw IllegalArgumentException("Account with id $id not found")

        // Change fields
        val updatedAccount = existingAccount.copy(
            name = request.name ?: existingAccount.name,
            description = request.description ?: existingAccount.description,
            lastUpdated = LocalDateTime.now()
        )
        return accountRepository.update(updatedAccount)
    }

    override fun deleteAccount(id: UUID) {
        return accountRepository.softDelete(id)
    }

}