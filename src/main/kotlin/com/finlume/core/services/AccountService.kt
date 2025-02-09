package com.finlume.core.services

import com.finlume.core.domain.Account
import com.finlume.core.command.CreateAccountCommand
import com.finlume.core.command.UpdateAccountCommand
import com.finlume.core.gateways.AccountPort
import com.finlume.core.repositories.AccountRepositoryPort
import com.finlume.core.services.exceptions.AccountNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class AccountService(
    private val accountRepository: AccountRepositoryPort

): AccountPort {
    override fun createAccount(request: CreateAccountCommand): Account {

        val account = Account(
            name = request.name,
            description = request.description,
            currency = request.currency,
        )

        return accountRepository.create(account)
    }

    override fun findAccountById(id: UUID): Account {
        return accountRepository.findById(id) ?: throw AccountNotFoundException("Account with id: $id not found")
    }

    override fun findAllAccounts(): List<Account>? {
        return accountRepository.listAll()
    }

    override fun updateAccount(request: UpdateAccountCommand, id: UUID): Account {
        // Get existing account
        val existingAccount: Account = accountRepository.findById(id)
        ?: throw AccountNotFoundException("Account with id: $id not found")

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